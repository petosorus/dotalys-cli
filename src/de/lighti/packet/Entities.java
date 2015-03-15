package de.lighti.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.valve.dota2.Netmessages.CSVCMsg_PacketEntities;

import de.lighti.DotaPlay;
import de.lighti.GameEventListener;
import de.lighti.model.Entity;
import de.lighti.model.state.EntityClass;
import de.lighti.model.state.ParseState;
import de.lighti.model.state.StateUtils.FlatSendTable;
import de.lighti.model.state.StateUtils.StringTable;
import de.lighti.model.state.StateUtils.StringTableEntry;
import de.lighti.util.BitInputBuffer;

/**
 * This class handles the CSVCMsg_PacketEntities SVCMessage. The packet is originally
 * a binary field and denotes created, changed, and deleted entities.
 * 
 * The code is based on the c++ parser library https://github.com/dschleck/edith
 * 
 * @author Tobias Mahlmann
 *
 */
public final class Entities {
    /**
     * Indicates if an Entity has been created or deleted.
     * @author Tobias Mahlmann
     *
     */
    private enum EntityUpdateFlag {
        /**
         * Created.
         */
        UF_EnterPVS,
        /**
         * Deleted. 
         */
        UF_Delete,
        /**
         * Deleted. 
         */
        UF_LeavePVS
    }

    private final static int MAX_EDICTS = 0x800;

    private final static String INSTANCE_BASELINE_TABLE = "instancebaseline";

    private static StringTableEntry getBaseline( int class_i, ParseState state ) {
        if (state == null) {
            throw new IllegalArgumentException( "No state created." );
        }

        final StringTable instance_baseline = state.getStringTable( INSTANCE_BASELINE_TABLE );
        return instance_baseline.get( "" + class_i );
    }

    public static void handlePacketEntities( CSVCMsg_PacketEntities e, ParseState state ) throws IOException {
        final ByteBuffer entityData = e.getEntityData().asReadOnlyByteBuffer();
        final BitInputBuffer stream = new BitInputBuffer( entityData );

        final int[] entityId = { -1 };
        for (int i = 0; i < e.getUpdatedEntries(); i++) {

            final Set<EntityUpdateFlag> flags = readEntityHeader( entityId, stream );

            if (flags.contains( EntityUpdateFlag.UF_EnterPVS )) {
                readEntityEnterPvs( entityId[0], stream, state );
            }
            else if (flags.contains( EntityUpdateFlag.UF_LeavePVS )) {

                if (!e.hasIsDelta()) {
                    throw new IllegalStateException( "Leave PVS on full update" );
                }

                if (flags.contains( EntityUpdateFlag.UF_Delete )) {
                    Logger.getLogger( Entities.class.getName() ).fine( "Entity " + entityId[0] + " deleted" );
                    final Entity removed = state.getEntity( entityId[0] );
                    if (removed.getId() != 1) {
                        for (final GameEventListener l : DotaPlay.getListeners()) {
                            l.entityRemoved( DotaPlay.getTickMs(), removed );
                        }
                        removed.setId( -1 );
                    }

                }
            }
            else {
                readEntityUpdate( entityId[0], stream, state );
            }

        }

        if (e.hasIsDelta()) {
            while (stream.readBit()) {
                entityId[0] = stream.readBitsAsInt( 11 );
                Logger.getLogger( Entities.class.getName() ).fine( "Entity " + entityId[0] + " deleted" );

                final Entity removed = state.getEntity( entityId[0] );
                if ((removed != null) && (removed.getId() != 1)) {
                    for (final GameEventListener l : DotaPlay.getListeners()) {
                        l.entityRemoved( DotaPlay.getTickMs(), removed );
                    }
                    removed.setId( -1 );
                }

            }
        }
    }

    private static void readEntityEnterPvs( int entityId, BitInputBuffer stream, ParseState state ) throws IOException {
        final int class_i = stream.readBitsAsInt( state.getClassBits() );
        //read serial, but we actually don't need this
        stream.readBitsAsInt( 10 );

        if (entityId >= MAX_EDICTS) {
            throw new IllegalStateException( "Entity " + entityId + " exceeds max edicts" );
        }

        final EntityClass eClass = state.getEntityClass( class_i );
        final FlatSendTable flatSendTable = state.getFlatSendTable( eClass.getDtName() );

        Entity entity = state.getEntity( entityId );

        if (entity != null) {
            if (entity.getId() != -1) {
                Logger.getLogger( Entities.class.getName() ).fine( "Entity " + entityId + " deleted" );
                for (final GameEventListener l : DotaPlay.getListeners()) {
                    l.entityRemoved( DotaPlay.getTickMs(), entity );
                }
                entity.setId( -1 );
            }
        }

        entity = new Entity( entityId, eClass, flatSendTable );
        state.setEntity( entityId, entity );

        final StringTableEntry baseline = getBaseline( class_i, state );

        final BitInputBuffer baselineStream = new BitInputBuffer( baseline.value );
        entity.update( baselineStream );

        entity.update( stream );

    }

    private static Set<EntityUpdateFlag> readEntityHeader( int[] base, BitInputBuffer stream ) throws IOException {
        int value = stream.readBitsAsInt( 6 );
        if ((value & 0x30) > 0) {
            final int a = (value >>> 4) & 3;
            final int b = (a == 3) ? 16 : 0;
            value = (stream.readBitsAsInt( (4 * a) + b ) << 4) | (value & 0xF);

        }
        base[0] += value + 1;

        final Set<EntityUpdateFlag> updateFlags = new HashSet<EntityUpdateFlag>();

        if (!stream.readBit()) {
            if (stream.readBit()) {
                updateFlags.add( EntityUpdateFlag.UF_EnterPVS );
            }
        }
        else {
            updateFlags.add( EntityUpdateFlag.UF_LeavePVS );
            if (stream.readBit()) {
                updateFlags.add( EntityUpdateFlag.UF_Delete );
            }
        }

        return updateFlags;
    }

    private static void readEntityUpdate( int entityId, BitInputBuffer stream, ParseState state ) {
        if (entityId >= MAX_EDICTS) {
            throw new IllegalArgumentException( "Entity id too big" );
        }

        final Entity entity = state.getEntity( entityId );
        if (entity == null) {
            throw new IllegalStateException( "Entity " + entityId + " is not set up." );
        }

        entity.update( stream );

    }

    /**
     * Private constructor for utility class.
     */
    private Entities() {

    }

}
