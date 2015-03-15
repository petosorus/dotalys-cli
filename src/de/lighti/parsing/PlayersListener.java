package de.lighti.parsing;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.lighti.DefaultGameEventListener;
import de.lighti.DotaPlay;
import de.lighti.model.AppState;
import de.lighti.model.Entity;
import de.lighti.model.Property;
import de.lighti.model.game.Hero;
import de.lighti.model.game.Player;
import de.lighti.model.state.ParseState;

/**
 * The game keeps re-creating an entity CDOTA_PlayerResource throughout a match, hence it's entity id is. This volatile
 * more or less meaningless. Instead, this entity holds various vectors whose fields have to be tracked
 * and mapped to our own Player objects.
 *
 * @author Tobias Mahlmann
 *
 */
public class PlayersListener extends DefaultGameEventListener {
    private final AppState state;

    private final Pattern playerPattern;

    private final Map<String, Player> playerBuffer;

    public PlayersListener( AppState state ) {
        super();
        this.state = state;
        playerPattern = Pattern.compile( "\\.[0-9][0-9][0-9][0-9]$" );
        playerBuffer = new HashMap<String, Player>();
    }

    @Override
    public void entityCreated( long tickMs, Entity e ) {
        if (e.getEntityClass().getName().equals( "CDOTA_PlayerResource" )) {
            for (final Property<?> p : e.getProperties()) {
                handleWorldVar( tickMs, p.getName(), p.getValue() );
            }
        }

    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {
        if (e.getEntityClass().getName().equals( "CDOTA_PlayerResource" )) {
            handleWorldVar( tickMs, name, e.getProperty( name ).getValue() );
        }

    }

    private void handleWorldVar( long time, String name, Object value ) {

        final Matcher m = playerPattern.matcher( name );
        if (m.find()) {

            final String id = name.substring( name.lastIndexOf( "." ) + 1 );
            final String valueName = name.substring( 0, name.lastIndexOf( "." ) );
            Player p = playerBuffer.get( id );
            if (p == null) {
                playerBuffer.put( id, new Player( Integer.valueOf( id ) ) );
                p = playerBuffer.get( id );
            }

            switch (valueName) {
                case "m_iszPlayerNames":
                    p.setName( (String) value );
                    break;
                case "m_iTotalEarnedGold":
                    p.setTotalEarnedGold( time, (Integer) value );
                    break;
                case "m_iTotalEarnedXP":
                    p.setTotalXP( time, (Integer) value );
                    break;
                case "m_hSelectedHero":
                    p.setHero( state.getHero( (Integer) value & 0x7FF ) );
                    break;
                case "m_iPlayerTeams":
                    p.setRadiant( (Integer) value == 2 ); //2 = Radiant, 3 = Dire, 5 = Spectator
                    break;
                case "m_iDeaths":
                    //As PlayerResource is recreated over and over again, we will get an update value = 0, and value = <the actual value> one after another.
                    //Since deaths can't decrease, we simply check if the value we get is higher than what we've stored
                    if (p.getDeaths( time ).size() < (Integer) value) {
                        p.addDeath( time );
                    }

                    break;
                default:
                    Map<String, Object> tickMap = state.gameEventsPerMs.get( DotaPlay.getTickMs() );
                    if (tickMap == null) {
                        tickMap = new HashMap<String, Object>();
                        state.gameEventsPerMs.put( DotaPlay.getTickMs(), tickMap );
                    }
                    tickMap.put( name, value );
                    state.addPlayerVariable( valueName );
                    break;

            }

        }

    }

    @Override
    public void parseComplete( long tickMs, ParseState state ) {
        for (final Player p : playerBuffer.values()) {
            final Hero h = p.getHero();
            if (h != null) {
                //Propagate the player's deaths into the hero as only the heros stored where it died
                for (final Long l : p.getDeaths()) {
                    final int x = h.getX( l );
                    final int y = h.getY( l );
                    h.addDeath( l, x, y );
                }
                this.state.addPlayer( p );
            }
        }
    }

}
