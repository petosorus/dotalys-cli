package de.lighti.model.state;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import com.valve.dota2.Netmessages.CSVCMsg_GameEventList;

import de.lighti.model.Entity;
import de.lighti.model.state.StateUtils.DTProp;
import de.lighti.model.state.StateUtils.FlatSendTable;
import de.lighti.model.state.StateUtils.SP_Flags;
import de.lighti.model.state.StateUtils.SP_Type;
import de.lighti.model.state.StateUtils.SendProp;
import de.lighti.model.state.StateUtils.SendTable;
import de.lighti.model.state.StateUtils.StringTable;
import de.lighti.util.Utils;

public class ParseState {
    private class CompileState {
        Set<String> excluding;
        Vector<SendProp> props;

        CompileState() {
            excluding = new HashSet<String>();
            props = new Vector<SendProp>();
            playedHeroes = new HashMap<String, String>();
        }
    }

    private final Map<Integer, Entity> entities;

    private final Map<Integer, EntityClass> entityClasses;

    private final int classBits;

    private final Map<String, SendTable> sendTables;

    private final LinkedHashMap<String, StringTable> stringTables;

    private final Map<String, FlatSendTable> flatSendTables;

    private Map<String, String> playedHeroes;

    private float tickInterval;

    private CSVCMsg_GameEventList gameEventList;

    private final int protocolVersion;

    public ParseState( int version, int maxClasses ) {
        protocolVersion = version;
        entities = new HashMap<Integer, Entity>();
        sendTables = new HashMap<String, SendTable>();
        flatSendTables = new HashMap<String, FlatSendTable>();
        entityClasses = new HashMap<Integer, EntityClass>();
        classBits = Utils.log2( maxClasses );
        stringTables = new LinkedHashMap<String, StringTable>();
    }

    private void buildHierarchy( SendTable sendTable, DTProp dtProp, CompileState state ) {
        dtProp.sendTable = sendTable;

        dtProp.propStart = state.props.size();
        gather( sendTable, dtProp, state );

        final Iterator<SendProp> iter = dtProp.nonDtProps.iterator();
        while (iter.hasNext()) {
            final SendProp p = iter.next();
            state.props.add( p );
        }

        dtProp.propCount = state.props.size() - dtProp.propStart;

    }

    public void clearEntities() {
        entities.clear();
    }

    private void compileSendTable( SendTable table ) {
        final CompileState state = new CompileState();
        gatherExcludes( table, state.excluding );

        final DTProp dtProp = new DTProp();
        buildHierarchy( table, dtProp, state );

        final Vector<Integer> priorities = new Vector();
        priorities.add( 64 );
        for (final SendProp prop : state.props) {
            final int priority = prop.getPriority();

            final Iterator<Integer> iter = priorities.iterator();
            int p = iter.next();
            while (iter.hasNext() && p != priority) {
                p = iter.next();
            }

            if (p == priorities.lastElement()) {
                priorities.add( priority );
            }
        }

        Collections.sort( priorities );

        int propOffset = 0;
        for (int priorityIndex = 0; priorityIndex < priorities.size(); ++priorityIndex) {
            final int priority = priorities.get( priorityIndex );

            int hole = propOffset;
            int cursor = hole;
            while (cursor < state.props.size()) {
                final SendProp prop = state.props.get( cursor );

                if (prop.getPriority() == priority || priority == 64 && (SP_Flags.SP_ChangesOften.getId() & prop.getFlags()) > 0) {
                    final SendProp temp = state.props.get( hole );
                    state.props.set( hole, state.props.get( cursor ) );
                    state.props.set( cursor, temp );

                    ++hole;
                    ++propOffset;
                }

                ++cursor;
            }
        }

        final FlatSendTable flatTable = new FlatSendTable( table.getName() );
        flatTable.setProps( state.props );
        flatTable.setDtProp( dtProp );

        flatSendTables.put( table.getName(), flatTable );

    }

    public void compileSendTables() {
        for (final SendTable t : sendTables.values()) {
            compileSendTable( t );
        }

    }

    public void createClass( int classId, String tableName, String networkName ) {
        if (entityClasses.containsKey( classId )) {
            throw new IllegalArgumentException( "Class with id " + classId + " already exists." );
        }

        final EntityClass ec = new EntityClass( classId, tableName, networkName );
        Logger.getLogger( ParseState.class.getName() ).fine( "Created class: " + ec );
        entityClasses.put( classId, ec );

    }

    public SendTable createSendTable( String netTableName, boolean needsDecoder ) {
        final SendTable table = new SendTable( netTableName, needsDecoder );
        sendTables.put( netTableName, table );
        return table;
    }

    public StringTable createStringTable( String name, int maxEntries, int flags, boolean userDataFixedSize, int userDataSize, int userDataSizeBits ) {
        if (stringTables.containsKey( name )) {
            throw new IllegalStateException( "StringTable " + name + " already exists." );
        }

        final StringTable table = new StringTable( name, maxEntries, userDataFixedSize, userDataSize, userDataSizeBits, flags );
        stringTables.put( name, table );

        return table;
    }

    public Entity deleteEntity( int entityId ) {
        if (!entities.containsKey( entityId )) {
            throw new IllegalArgumentException( "Entity " + entityId + " does not exist" );
        }
        return entities.remove( entityId );

    }

    private void gather( SendTable from, DTProp dt_prop, CompileState state ) {
        for (final SendProp prop : from.getProps()) {

            if (((SP_Flags.SP_Exclude.getId() | SP_Flags.SP_InsideArray.getId()) & prop.getFlags()) > 0) {
                continue;
            }
            else if (state.excluding.contains( from.getName() + prop.getName() )) {
                continue;
            }

            if (SP_Type.SP_DataTable == prop.getType()) {
                final SendTable dtTable = sendTables.get( prop.getDtName() );
                if (dtTable == from) {
                    throw new IllegalStateException();
                }
                if ((SP_Flags.SP_Collapsible.getId() & prop.getFlags()) > 0) {
                    gather( dtTable, dt_prop, state );
                }
                else {
                    final DTProp newDtProp = new DTProp();
                    dt_prop.dtProps.add( newDtProp );

                    buildHierarchy( dtTable, newDtProp, state );
                }
            }
            else {
                if (dt_prop.nonDtProps.contains( prop )) {
                    throw new IllegalStateException();
                }
                dt_prop.nonDtProps.add( prop );
            }
        }
    }

    private void gatherExcludes( SendTable table, Set<String> excluding ) {
        for (final SendProp prop : table.getProps()) {

            if ((SP_Flags.SP_Exclude.getId() & prop.getFlags()) > 0) {
                excluding.add( prop.getDtName() + prop.getName() );
            }
            else if (SP_Type.SP_DataTable == prop.getType()) {
                gatherExcludes( sendTables.get( prop.getDtName() ), excluding );
            }
        }
    }

    public int getClassBits() {
        return classBits;
    }

    public Entity getEntity( int id ) {
        return entities.get( id );
    }

    public EntityClass getEntityClass( int id ) {
        return entityClasses.get( id );
    }

    public FlatSendTable getFlatSendTable( String dtName ) {
        return flatSendTables.get( dtName );
    }

    public CSVCMsg_GameEventList getGameEventList() {
        return gameEventList;
    }

    public String getHeroForPlayer( String player ) {
        return playedHeroes.get( player );
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public Collection<SendTable> getSendTables() {
        return sendTables.values();
    }

    public StringTable getStringTable( int tableId ) {
        final Iterator<String> iter = stringTables.keySet().iterator();
        String key = iter.next();
        while (tableId > 0) {
            key = iter.next();
            tableId--;
        }
        return stringTables.get( key );
    }

    public StringTable getStringTable( String key ) {
        return stringTables.get( key );
    }

    public LinkedHashMap<String, StringTable> getStringTables() {
        return stringTables;
    }

    /**
     * Dota2 game time is counted in "ticks". A wallclock minute
     * has 30 ticks, therefore a tick occurs roughly every 33ms.
     * This method converts a tick number in the number of miliseconds
     * passed since game start time.
     * @param tick
     * @return
     */
    public long getTickAsMs( int tick ) {
        return (long) (tick * 1000 * tickInterval);
    }

    public float getTickInterval() {
        return tickInterval;
    }

    public boolean hasEntity( int id ) {
        return entities.containsKey( id );
    }

    public void setEntity( int id, Entity entity ) {
        entities.put( id, entity );

    }

    public void setGameEventList( CSVCMsg_GameEventList e ) {
        gameEventList = e;

    }

    public void setPlayerInfo( String playerName, String heroName ) {
        playedHeroes.put( playerName, heroName );

    }

    public void setTickInterval( float tickInterval ) {
        this.tickInterval = tickInterval;

    }

}
