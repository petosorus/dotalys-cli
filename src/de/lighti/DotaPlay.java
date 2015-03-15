package de.lighti;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.valve.dota2.Demo.CDemoClassInfo;
import com.valve.dota2.Demo.CDemoClassInfo.class_t;
import com.valve.dota2.Demo.CDemoFileHeader;
import com.valve.dota2.Demo.CDemoFileInfo;
import com.valve.dota2.Demo.CDemoFullPacket;
import com.valve.dota2.Demo.CDemoPacket;
import com.valve.dota2.Demo.CDemoSendTables;
import com.valve.dota2.Demo.CGameInfo;
import com.valve.dota2.Demo.CGameInfo.CDotaGameInfo;
import com.valve.dota2.Demo.CGameInfo.CDotaGameInfo.CPlayerInfo;
import com.valve.dota2.Demo.EDemoCommands;
import com.valve.dota2.Netmessages.NET_Messages;
import com.valve.dota2.Netmessages.SVC_Messages;

import de.lighti.model.state.ParseState;
import de.lighti.model.state.StateUtils.SP_Type;
import de.lighti.model.state.StateUtils.SendProp;
import de.lighti.model.state.StateUtils.SendTable;
import de.lighti.packet.SVCMessageHandler;
import de.lighti.util.Utils;

/**
 * Facade class for reading Dota2 replay files. It encapsulate the complete functionality of the library.
 * Users should add their own listeners to the listeners structure and call loadFile.
 * @author Tobias Mahlmann
 *
 */
public class DotaPlay {
    public interface ProgressListener {
        void bytesRemaining( int position );
    }

    /**
     * The current parsing state.
     */
    private static ParseState state;

    private static List<GameEventListener> listeners = new ArrayList<GameEventListener>();

    private static int tick;

    private final static Logger LOGGER = Logger.getLogger( DotaPlay.class.getName() );

    public static void addListener( GameEventListener l ) {
        listeners.add( l );
    }

    public static List<GameEventListener> getListeners() {
        return listeners;
    }

    public static ParseState getState() {
        return state;
    }

    public static int getTick() {
        return tick;
    }

    public static long getTickMs() {
        return state.getTickAsMs( tick );
    }

    private static void handleClassInfo( CDemoClassInfo info ) {
        if (state == null) {
            throw new IllegalStateException( "DEM_ClassInfo but no state." );
        }

        for (int i = 0; i < info.getClassesCount(); ++i) {
            final class_t clazz = info.getClasses( i );
            state.createClass( clazz.getClassId(), clazz.getTableName(), clazz.getNetworkName() );
        }

        for (final SendTable table : state.getSendTables()) {
            for (int i = 0; i < table.getProps().size(); ++i) {
                final SendProp prop = table.getProps().get( i );

                prop.setInTable( table );

                if (prop.getType() == SP_Type.SP_Array) {
                    if (i == 0) {
                        throw new IllegalStateException( "Array prop " + prop.getName() + " is at index zero." );
                    }
                    prop.setArrayProp( table.getProps().get( i - 1 ) );
                }
            }
        }
        LOGGER.info( "Received " + info.getClassesCount() + " classes." );

        state.compileSendTables();

    }

    private static void handleDemoCommand( EDemoCommands type, byte[] message, int tick ) throws IOException {
        switch (type) {
            case DEM_FileHeader:
                final CDemoFileHeader header = CDemoFileHeader.parseFrom( message );
                handleHeader( header );

                break;
            case DEM_Stop:
                handleStop();
                break;
            case DEM_FullPacket:
                final CDemoFullPacket full = CDemoFullPacket.parseFrom( message );
                state.clearEntities();

                handlePacket( full.getPacket() );
                break;
            case DEM_Packet:
            case DEM_SignonPacket:

                final CDemoPacket p = CDemoPacket.parseFrom( message );
                handlePacket( p );
                break;
            case DEM_ClassInfo:
                final CDemoClassInfo info = CDemoClassInfo.parseFrom( message );
                handleClassInfo( info );
                break;
            case DEM_SendTables:
                final CDemoSendTables tables = CDemoSendTables.parseFrom( message );
                SVCMessageHandler.handleSendTables( tables, state );
                break;
            case DEM_FileInfo:
                final CDemoFileInfo finfo = CDemoFileInfo.parseFrom( message );
                handleFileInfo( finfo );
                break;
            case DEM_StringTables:

            case DEM_ConsoleCmd:
            case DEM_CustomData:
            case DEM_CustomDataCallbacks:
            case DEM_Error:

            case DEM_IsCompressed:
            case DEM_Max:

            case DEM_SyncTick:
            case DEM_UserCmd:
            default:
                LOGGER.info( "Skipping paket of type " + type + " in tick " + tick );
                break;
        }

    }

    private static void handleFileInfo( CDemoFileInfo finfo ) {
        final CGameInfo info = finfo.getGameInfo();

        final CDotaGameInfo dInfo = info.getDota();
        for (final CPlayerInfo p : dInfo.getPlayerInfoList()) {
            state.setPlayerInfo( p.getPlayerName(), p.getHeroName() );
        }

    }

    private static void handleHeader( CDemoFileHeader header ) {
        LOGGER.info( "Server name: " + header.getServerName() );
        LOGGER.info( "Server client name: " + header.getClientName() );

    }

    private static void handleNetMessage( int cmd, int size, ByteBuffer buffer ) {
        final NET_Messages msg = NET_Messages.valueOf( cmd );
        Logger.getLogger( DotaPlay.class.getName() ).finest( msg.name() );
        final byte[] data = new byte[size];
        buffer.get( data );

        switch (msg) {
            case net_Disconnect:
            case net_File:
            case net_NOP:
            case net_SetConVar:
            case net_SignonState:
            case net_SplitScreenUser:
            case net_StringCmd:
            case net_Tick:
                break;

        }
    }

    private static void handlePacket( CDemoPacket p ) throws IOException {

        final ByteBuffer buffer = p.getData().asReadOnlyByteBuffer();

        while (buffer.hasRemaining()) {
            final int cmd = Utils.readVarInt( buffer );
            final int size = Utils.readVarInt( buffer );

            if (SVC_Messages.valueOf( cmd ) != null) {
                SVCMessageHandler.handleSVCMessage( cmd, size, buffer );
            }
            else if (NET_Messages.valueOf( cmd ) != null) {
                handleNetMessage( cmd, size, buffer );
            }
            else {
                throw new IllegalStateException( "CDemo Packet with unknown command " + cmd + " and size " + size );
            }

        }

    }

    private static void handleStop() {

        LOGGER.info( "Demo ended after " + getTickMs() + " ms" );

    }

    public static void loadFile( String path ) {
        loadFile( path, (ProgressListener[]) null );
    }

    public static void loadFile( String path, ProgressListener... pl ) {
        setState( null );
        final DemoFile file = new DemoFile();
        try {
            file.open( path );
            while (!file.isDone()) {
                final int[] pTick = new int[1];
                final boolean[] compressed = new boolean[1];
                final EDemoCommands type = file.readMessageType( pTick, compressed );
                setTick( pTick[0] );

                Logger.getLogger( DotaPlay.class.getName() ).fine( "Tick: " + pTick[0] );
                final byte[] message = file.readMessage( compressed[0] );
                handleDemoCommand( type, message, pTick[0] );
                if (pl != null) {
                    for (final ProgressListener p : pl) {
                        p.bytesRemaining( file.bytesRemaining() );
                    }
                }
            }

            file.close();
            for (final GameEventListener l : listeners) {
                l.parseComplete( getTickMs(), getState() );
            }
        }
        catch (final IOException e) {
            Logger.getLogger( DotaPlay.class.getName() ).severe( e.getLocalizedMessage() );
            e.printStackTrace();
        }

    }

    public static void removeListener( GameEventListener l ) {
        listeners.remove( l );
    }

    public static void setState( ParseState state ) {
        DotaPlay.state = state;
    }

    public static void setTick( int i ) {
        tick = i;

    }

}
