package de.lighti.packet;

import com.valve.dota2.Netmessages.CSVCMsg_GameEventList.descriptor_t;
import com.valve.dota2.Netmessages.CSVCMsg_GameEventList.key_t;
import com.valve.dota2.Networkbasetypes.CSVCMsg_GameEvent;

import de.lighti.DotaPlay;
import de.lighti.GameEventListener;

public class GameEvent {

    /**
     * Handles Game event logic.
     * @param e the event
     */
    static void handleGameEvent( CSVCMsg_GameEvent e ) {

        int index;

        for (index = 0; index < DotaPlay.getState().getGameEventList().getDescriptorsCount(); index++) {
            final descriptor_t Descriptor = DotaPlay.getState().getGameEventList().getDescriptors( index );

            if (Descriptor.getEventid() == e.getEventid()) {
                break;
            }
        }

        if (index == DotaPlay.getState().getGameEventList().getDescriptorsCount()) {
            //System.out.println( e );
            //Not sure what this means.

            return;
        }
        else {
            final int numKeys = e.getKeysCount();
            final descriptor_t typeDescriptor = DotaPlay.getState().getGameEventList().getDescriptors( index );

            //We only care about combat log events
            if (!typeDescriptor.getName().equals( "dota_combatlog" )) {
                return;
            }

            int eventType = -1;
            int source = -1;
            int target = -1;
            int value = -1;
            float timeStamp = -1;
            int inflictor = -1;
            int attackerEntity = -1;

            for (int i = 0; i < numKeys; i++) {
                final key_t key = typeDescriptor.getKeys( i );
                final CSVCMsg_GameEvent.key_t keyValue = e.getKeys( i );

                final String keyName = key.getName();
                switch (keyName) {
                    case "value":
                        value = keyValue.getValShort();
                        break;
                    case "type":
                        eventType = keyValue.getValByte();
                        break;
                    case "sourcename":
                        source = keyValue.getValShort();
                        break;
                    case "attackername":
                        attackerEntity = keyValue.getValShort();
                        break;
                    case "targetname":
                        target = keyValue.getValShort();
                        break;
                    case "timestamp":
                        timeStamp = keyValue.getValFloat();
                        break;
                    case "inflictorname":
                        inflictor = keyValue.getValShort();
                    default:
                        break;
                }

            }
            if (attackerEntity < 0 || eventType < 0 || source < 0 || target < 0 || inflictor < 0 || timeStamp < 0) {
                throw new IllegalStateException( "GameEvent is missing values" );
            }

            for (final GameEventListener l : DotaPlay.getListeners()) {
                l.gameEvent( eventType, source, attackerEntity, target, value, inflictor, timeStamp );
            }
        }
    }
}
