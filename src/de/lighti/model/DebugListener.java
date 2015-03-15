package de.lighti.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.lighti.GameEventListener;
import de.lighti.model.state.ParseState;

public class DebugListener implements GameEventListener {
    private final BufferedWriter bw;
    private final Set<String> names;

    public DebugListener() throws IOException {
        bw = new BufferedWriter( new FileWriter( new File( "names.txt" ) ) );

        names = new HashSet();
    }

    @Override
    public void entityCreated( long tickMs, Entity entity ) {
        for (final Property p : entity.getProperties()) {
            if (!names.contains( p.getName() )) {
                names.add( p.getName() );
                try {
                    bw.write( p.getName() + "\n" );
                }
                catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void entityRemoved( long tickMs, Entity removed ) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {
        for (final Property p : e.getProperties()) {
            if (!names.contains( p.getName() )) {
                names.add( p.getName() );
                try {
                    bw.write( p.getName() + "\n" );
                }
                catch (final IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

    }

    @Override
    public void gameEvent( int eventType, int attacker, int attackerEntity, int target, int value, int inflictor, float timeStamp ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void parseComplete( long tickMs, ParseState state ) {
        // TODO Auto-generated method stub

    }

}
