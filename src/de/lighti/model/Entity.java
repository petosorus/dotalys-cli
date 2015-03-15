package de.lighti.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import de.lighti.DotaPlay;
import de.lighti.GameEventListener;
import de.lighti.model.state.EntityClass;
import de.lighti.model.state.StateUtils.FlatSendTable;
import de.lighti.model.state.StateUtils.SendProp;
import de.lighti.util.BitInputBuffer;

public class Entity {
    private static void readFieldList( Vector<Integer> fields, BitInputBuffer stream ) {
        int lastField = readFieldNumber( -1, stream );

        while (lastField != -1) {
            fields.add( lastField );

            lastField = readFieldNumber( lastField, stream );
        }
    }

    private static int readFieldNumber( int lastField, BitInputBuffer stream ) {
        if (stream.readBit()) {
            return ++lastField;
        }
        else {
            final int value = stream.readVar35();
            if (value == 0x3FFF) {
                return -1;
            }
            else {
                return lastField + value + 1;
            }
        }
    }

    private int id;
    private EntityClass eClass;

    private FlatSendTable flatSendTable;

    private Map<Integer, Property> properties;

    public Entity( int id ) {
        super();
        this.id = id;
        properties = null;
    }

    public Entity( int id, EntityClass eClass, FlatSendTable flatSendTable ) {
        this( id );

        this.eClass = eClass;
        this.flatSendTable = flatSendTable;
    }

    public EntityClass getEntityClass() {
        return eClass;
    }

    public int getId() {
        return id;
    }

    public Collection<Property> getProperties() {
        return properties.values();
    }

    public Property getProperty( String name ) {
        if (properties == null) {
            return null;
        }
        for (final Property p : properties.values()) {
            if (p.getName().equals( name )) {
                return p;
            }
        }
        return null;
    }

    public void setId( int i ) {
        id = i;

    }

    @Override
    public String toString() {
        return "Entity [id=" + id + ", eClass=" + eClass + "]";
    }

    public void update( BitInputBuffer stream ) {
        final boolean first = properties == null;
        if (first) {
            properties = new HashMap();
        }

        final Vector<Integer> fields = new Vector();
        readFieldList( fields, stream );
        if (!first) {
            for (final Integer i : fields) {
                final Object old = properties.get( i ) != null ? properties.get( i ).getValue() : null;
                final SendProp prop = flatSendTable.getProps().get( i );
                final Property p = Property.readProperty( stream, prop );

                properties.put( i, p );
                final Object newValue = properties.get( i );

                for (final GameEventListener l : DotaPlay.getListeners()) {
                    l.entityUpdated( DotaPlay.getTickMs(), this, properties.get( i ).getName(), old );
                }

            }
        }
        else {
            for (final Integer i : fields) {
                final SendProp prop = flatSendTable.getProps().get( i );
                final Property p = Property.readProperty( stream, prop );
                properties.put( i, p );
            }
            for (final GameEventListener l : DotaPlay.getListeners()) {
                l.entityCreated( DotaPlay.getTickMs(), this );
            }
        }

    }

}
