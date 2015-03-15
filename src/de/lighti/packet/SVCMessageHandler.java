package de.lighti.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import com.valve.dota2.Demo.CDemoSendTables;
import com.valve.dota2.Netmessages.CSVCMsg_CreateStringTable;
import com.valve.dota2.Netmessages.CSVCMsg_GameEventList;
import com.valve.dota2.Netmessages.CSVCMsg_PacketEntities;
import com.valve.dota2.Netmessages.CSVCMsg_SendTable;
import com.valve.dota2.Netmessages.CSVCMsg_SendTable.sendprop_t;
import com.valve.dota2.Netmessages.CSVCMsg_ServerInfo;
import com.valve.dota2.Netmessages.CSVCMsg_TempEntities;
import com.valve.dota2.Netmessages.CSVCMsg_UpdateStringTable;
import com.valve.dota2.Netmessages.SVC_Messages;
import com.valve.dota2.Networkbasetypes.CSVCMsg_GameEvent;
import com.valve.dota2.Networkbasetypes.CSVCMsg_UserMessage;

import de.lighti.DotaPlay;
import de.lighti.model.state.ParseState;
import de.lighti.model.state.StateUtils;
import de.lighti.model.state.StateUtils.ST_Flags;
import de.lighti.model.state.StateUtils.SendProp;
import de.lighti.model.state.StateUtils.SendTable;
import de.lighti.model.state.StateUtils.StringTable;
import de.lighti.model.state.StateUtils.StringTableEntry;
import de.lighti.util.BitInputBuffer;
import de.lighti.util.Utils;

public class SVCMessageHandler {
    private static void handleCreateTable( CSVCMsg_CreateStringTable table, ParseState state ) {
        if (state == null) {
            throw new IllegalStateException( "SVC_CreateStringTable but no state." );
        }

        final StringTable converted = state.createStringTable( table.getName(), table.getMaxEntries(), table.getFlags(), table.getUserDataFixedSize(),
                        table.getUserDataSize(), table.getUserDataSizeBits() );

        if (table.getName().equals( INSTANCE_BASELINE_TABLE )) {
            updateStringTable( converted, table.getNumEntries(), table.getStringData().asReadOnlyByteBuffer() );
        }
        else {
            Logger.getLogger( SVCMessageHandler.class.getName() ).fine( "Table " + table.getName() + " is not implemented." );
        }

    }

    private static void handleGameEventList( CSVCMsg_GameEventList e ) {
        DotaPlay.getState().setGameEventList( e );

    }

    private static void handleSendTable( CSVCMsg_SendTable table, ParseState state ) {
        if (state == null) {
            throw new IllegalStateException( "SVC_SendTable but no state created." );
        }

        final SendTable converted = state.createSendTable( table.getNetTableName(), table.getNeedsDecoder() );

        for (final sendprop_t prop : table.getPropsList()) {
            Logger.getLogger( DotaPlay.class.getName() ).finest( "Property created " + prop.getVarName() );
            final SendProp c = new SendProp( StateUtils.SP_Type.valueOf( prop.getType() ), prop.getVarName(), prop.getFlags(), prop.getPriority(),
                            prop.getDtName(), prop.getNumElements(), prop.getLowValue(), prop.getHighValue(), prop.getNumBits() );

            converted.getProps().add( c );
        }

    }

    public static void handleSendTables( CDemoSendTables tables, ParseState state ) throws IOException {
        final ByteBuffer data = tables.getData().asReadOnlyByteBuffer();

        while (data.hasRemaining()) {
            final int command = Utils.readVarInt( data );
            final int size = Utils.readVarInt( data );

            if (command != SVC_Messages.svc_SendTable.getNumber()) {
                throw new IllegalStateException( "Command " + command + " in DEM_SendTables." );
            }

            final byte[] bytes = new byte[size];
            data.get( bytes );
            final CSVCMsg_SendTable sendTable = CSVCMsg_SendTable.parseFrom( bytes );
            handleSendTable( sendTable, state );
        }

    }

    public static void handleSVCMessage( int cmd, int size, ByteBuffer buffer ) throws IOException {
        final SVC_Messages msg = SVC_Messages.valueOf( cmd );

        final byte[] bytes = new byte[size];
        buffer.get( bytes );
        switch (msg) {
            case svc_GameEvent: {
                final CSVCMsg_GameEvent e = CSVCMsg_GameEvent.parseFrom( bytes );
                GameEvent.handleGameEvent( e );
                break;
            }
            case svc_GameEventList: {
                final CSVCMsg_GameEventList e = CSVCMsg_GameEventList.parseFrom( bytes );
                handleGameEventList( e );
                break;
            }
            case svc_PacketEntities: {
                final CSVCMsg_PacketEntities e = CSVCMsg_PacketEntities.parseFrom( bytes );
                Entities.handlePacketEntities( e, DotaPlay.getState() );

                break;
            }
            case svc_UserMessage: {
                final CSVCMsg_UserMessage e = CSVCMsg_UserMessage.parseFrom( bytes );
                UserMessage.handleUserMessage( e );
                break;
            }
            case svc_ServerInfo:
                if (DotaPlay.getState() != null) {
                    throw new IllegalStateException( "We already seen a server info" );
                }
                final CSVCMsg_ServerInfo si = CSVCMsg_ServerInfo.parseFrom( bytes );
                DotaPlay.setState( new ParseState( si.getProtocol(), si.getMaxClasses() ) );
                DotaPlay.getState().setTickInterval( si.getTickInterval() );
                break;
            case svc_CreateStringTable:
                final CSVCMsg_CreateStringTable create = CSVCMsg_CreateStringTable.parseFrom( bytes );
                handleCreateTable( create, DotaPlay.getState() );
                break;
            case svc_UpdateStringTable:

                final CSVCMsg_UpdateStringTable update = CSVCMsg_UpdateStringTable.parseFrom( bytes );
                final StringTable table = DotaPlay.getState().getStringTable( update.getTableId() );
                if (table == null) {
                    throw new IllegalStateException( "Trying to update a non-existing table" );
                }
                if (table.getName().equals( INSTANCE_BASELINE_TABLE )) {
                    updateStringTable( table, update.getNumChangedEntries(), update.getStringData().asReadOnlyByteBuffer() );
                }
                else {
                    Logger.getLogger( SVCMessageHandler.class.getName() ).fine( "not updating " + table.getName() );
                }
                break;
            case svc_TempEntities:
                final CSVCMsg_TempEntities te = CSVCMsg_TempEntities.parseFrom( bytes );
                handleTempEntities( te );
                break;
            case svc_EntityMessage:
            case svc_BSPDecal:
            case svc_ClassInfo:

            case svc_CrosshairAngle:

            case svc_FixAngle:
            case svc_GetCvarValue:
            case svc_Menu:
            case svc_Prefetch:
            case svc_Print:
            case svc_SendTable:

            case svc_SetPause:
            case svc_SetView:
            case svc_Sounds:
            case svc_SplitScreen:

            case svc_VoiceData:
            case svc_VoiceInit:
            default:
//                System.out.println( msg.name() );
                break;

        }
    }

    private static void handleTempEntities( CSVCMsg_TempEntities te ) {
//        System.out.println( "TempEntities with " + te.getNumEntries() + " entries" );

        final BitInputBuffer buffer = new BitInputBuffer( te.getEntityData().asReadOnlyByteBuffer() );
//        System.out.println( buffer );
    }

    private static void updateStringTable( StringTable table, int numEntries, ByteBuffer stringData ) {
        final BitInputBuffer stream = new BitInputBuffer( stringData );

        Logger.getLogger( SVCMessageHandler.class.getName() ).finer( "Update package has " + stringData.capacity() + " bytes" );
        Logger.getLogger( SVCMessageHandler.class.getName() ).finer( "Expecting " + numEntries + " entires" );
        final boolean first = stream.readBit();

        int entryId = -1;
        int entriesRead = 0;
        final int oldSize = table.size();

        while (entriesRead < numEntries) {

            if (!stream.readBit()) {
                entryId = stream.readBitsAsInt( table.getEntryBits() );
            }
            else {
                entryId += 1;
            }
            if (entryId >= table.getMaxEntries()) {
                throw new IllegalStateException( "Entry id too large" );
            }

            String key = null;
            if (stream.readBit()) {
                if (first && stream.readBit()) {
                    throw new UnsupportedOperationException( "please no" );
                }
                else {
                    if (stream.readBit()) {
                        throw new UnsupportedOperationException( "no substring" );
                    }
                    else {
                        key = stream.readString( MAX_KEY_SIZE );
                    }
                }
            }

            byte[] valueBuffer = null;

            int length = 0;
            if (stream.readBit()) {
                if ((table.getFlags() & ST_Flags.ST_FixedLength.getId()) > 0) {
                    length = table.getUserDataSize();
                }
                else {
                    length = stream.readBitsAsInt( 14 );
                }
                if (length >= MAX_VALUE_SIZE) {
                    throw new IllegalStateException( "Message too long." );
                }
                valueBuffer = new byte[length];

                for (int i = 0; i < length; i++) {
                    valueBuffer[i] = (byte) stream.readBitsAsInt( 8 );
                }
            }

            if (entryId < table.size()) {
                final StringTableEntry item = table.get( entryId );

                if (key != null && !item.key.equals( key )) {
                    throw new IllegalStateException( "Entry's keys don't match." );
                }
                item.value = valueBuffer;

            }
            else {
                if (key == null) {
                    throw new IllegalStateException( "Creating a new string table entry but no key specified." );
                }

                table.put( new String( key ), valueBuffer );//new String( value_buffer, 0, length ) );
            }

            ++entriesRead;
        }
        Logger.getLogger( SVCMessageHandler.class.getName() ).finer(
                        "StringTable " + table.getName() + " updated with " + numEntries + " updated enties. (" + oldSize + "->" + table.size() + ")" );
    }

    private final static String INSTANCE_BASELINE_TABLE = "instancebaseline";

    private final static int MAX_KEY_SIZE = 0x400;

    private final static int MAX_VALUE_SIZE = 0x4000;
}
