package de.lighti;

import de.lighti.model.Entity;
import de.lighti.model.state.ParseState;

public abstract class DefaultGameEventListener implements GameEventListener {

    @Override
    public void entityCreated( long tickMs, Entity entity ) {

    }

    @Override
    public void entityRemoved( long tickMs, Entity removed ) {

    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {

    }

    @Override
    public void gameEvent( int eventType, int attacker, int attackerEntity, int target, int value, int inflictor, float timeStamp ) {

    }

    @Override
    public void parseComplete( long tickMs, ParseState state ) {

    }

}
