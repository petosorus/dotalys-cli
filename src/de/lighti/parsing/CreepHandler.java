package de.lighti.parsing;

import java.util.HashSet;
import java.util.Set;

import de.lighti.DefaultGameEventListener;
import de.lighti.model.AppState;
import de.lighti.model.Entity;

public class CreepHandler extends DefaultGameEventListener {
    private enum EVENT_TYPE {
        DAMAGE( 0 ), HEALING( 1 ), BUUFF_APPLIED( 2 ), BUFF_REMOVED( 3 ), DEATH( 4 );
        public static EVENT_TYPE valueOf( int i ) {
            for (final EVENT_TYPE e : EVENT_TYPE.values()) {
                if (e.id == i) {
                    return e;
                }
            }
            return null;
        }

        private int id;

        EVENT_TYPE( int i ) {
            id = i;
        }
    }

    private final AppState state;

    private final Set<String> creepClasses = new HashSet<String>();

    public CreepHandler( AppState appState ) {
        state = appState;
    }

    @Override
    public void entityCreated( long tickMs, Entity entity ) {
        if (entity.getId() == 1088) {
            if (entity.getEntityClass().getDtName().contains( "DT_DOTA_BaseNPC_Creep" ) && !creepClasses.contains( entity.getEntityClass().getDtName() )) {
//                System.out.println( entity.getEntityClass().getDtName() );
//                creepClasses.add( entity.getEntityClass().getDtName() );
            }
        }
    }

    @Override
    public void entityRemoved( long tickMs, Entity e ) {
//        if (e.getEntityClass().getDtName().contains( "DT_DOTA_BaseNPC_Creep" )) {
//            System.out.println( "Creep died at " + e.getProperty( "DT_DOTA_BaseNPC.m_cellX" ).getValue() + ", "
//                            + e.getProperty( "DT_DOTA_BaseNPC.m_cellY" ).getValue() );
//        }
    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {
        if (e.getId() == 1088) {
            if (e.getEntityClass().getDtName().contains( "DT_DOTA_BaseNPC_Creep" )) {
                if (name.equals( "DT_DOTA_BaseNPC.m_lifeState" )) {
                    final int value = (Integer) e.getProperty( name ).getValue();

                    if (value == 1) {
                        //dies                      
                    }
                    else if (value == 2) {
                        //is dead
                    }

                }
//                System.out.println( e.getId() + "." + name + " " + oldValue + "->" + e.getProperty( name ).getValue() );
            }
        }
    }

    @Override
    public void gameEvent( int eventType, int sourceName, int attackerEntity, int target, int value, int inflictor, float timeStamp ) {
//        System.out.println( EVENT_TYPE.valueOf( eventType ) + " attacker: " + attackerEntity + " source: " + sourceName + " target: " + target );
    }

}
