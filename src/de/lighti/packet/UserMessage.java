package de.lighti.packet;

import com.google.protobuf.InvalidProtocolBufferException;
import com.valve.dota2.DotaUsermessages.CDOTAUserMsg_CombatLogData;
import com.valve.dota2.DotaUsermessages.EDotaUserMessages;
import com.valve.dota2.Networkbasetypes.CSVCMsg_UserMessage;

/**
 * Handles UserMessages. Might be used in the future.
 * @author Tobias Mahlmann
 *
 */
public class UserMessage {

    public static void handleUserMessage( CSVCMsg_UserMessage message ) throws InvalidProtocolBufferException {
        final int cmd = message.getMsgType();
        final EDotaUserMessages e = EDotaUserMessages.valueOf( cmd );

        if (e != null) {
            switch (e) {
                case DOTA_UM_ChatEvent:
//                    final CDOTAUserMsg_ChatEvent ce = CDOTAUserMsg_ChatEvent.parseFrom( message.getMsgData() );

                    break;
                case DOTA_UM_CombatLogData:
                    final CDOTAUserMsg_CombatLogData cl = CDOTAUserMsg_CombatLogData.parseFrom( message.getMsgData() );
                    System.err.println( cl.getTargetName() );
                case DOTA_UM_OverheadEvent:
//                    final CDOTAUserMsg_OverheadEvent oh = CDOTAUserMsg_OverheadEvent.parseFrom( message.getMsgData() );
//                    final Descriptor d = oh.getDescriptorForType();
//                    for (final FieldDescriptor fd : d.getFields()) {
//                        System.err.println( fd.getName() + "->" + oh.getField( fd ) );
//                    }
                    break;
                default:
                    //Don't care
                    break;
            }
//            System.err.println( e );
        }

    }

}
