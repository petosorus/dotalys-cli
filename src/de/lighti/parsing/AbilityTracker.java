package de.lighti.parsing;

import de.lighti.DefaultGameEventListener;
import de.lighti.model.AppState;
import de.lighti.model.Entity;
import de.lighti.model.Property;
import de.lighti.model.game.Ability;

public class AbilityTracker extends DefaultGameEventListener {
    private final AppState state;

    public AbilityTracker( AppState appState ) {
        state = appState;
    }

    @Override
    public void entityCreated( long tickMs, Entity e ) {
        super.entityCreated( tickMs, e );
        final String className = e.getEntityClass().getName();
        if (className.startsWith( "CDOTA_Ability" ) || className.startsWith( "CDOTABaseAbility" )) {
            if (state.getAbility( tickMs, e.getId() ) == Ability.UNKNOWN_ABILITY) {
                state.addAbility( tickMs, e.getId(), (String) e.getProperty( "DT_BaseEntity.m_iName" ).getValue() );
            }
        }
    }

    @Override
    public void entityRemoved( long tickMs, Entity removed ) {
        super.entityRemoved( tickMs, removed );

        state.removeAbility( tickMs, removed.getId() );
    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {

        final Ability a = state.getAbility( tickMs, e.getId() );
        if (a != Ability.UNKNOWN_ABILITY) {
            if (name.equals( "DT_BaseEntity.m_iName" )) {
                final Property<String> p = e.getProperty( "DT_BaseEntity.m_iName" );
                a.setKey( p != null ? (String) p.getValue() : "" );
            }
            else if (name.endsWith( "m_iLevel" )) {
                a.setLevel( tickMs, (Integer) e.getProperty( name ).getValue() );
            }
            else if (name.endsWith( "m_fCooldown" )) {
                final Float value = (Float) e.getProperty( name ).getValue();
                if (!value.equals( oldValue )) {
                    final Float cd = (Float) e.getProperty( "DT_DOTABaseAbility.m_flCooldownLength" ).getValue();
                    state.getAbility( tickMs, e.getId() ).addInvocation( (long) ((value - cd) * 1000f) );
                }
            }

        }
    }

}
