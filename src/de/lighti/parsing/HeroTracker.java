package de.lighti.parsing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import de.lighti.DefaultGameEventListener;
import de.lighti.model.AppState;
import de.lighti.model.Entity;
import de.lighti.model.Property;
import de.lighti.model.game.Ability;
import de.lighti.model.game.Dota2Item;
import de.lighti.model.game.Hero;
import de.lighti.model.state.ParseState;

public class HeroTracker extends DefaultGameEventListener {
    private final AppState state;

    private final Map<Hero, TreeMap<Long, Integer[]>> itemCache;
    private final Map<Hero, TreeMap<Long, Set<Integer>>> abilityCache;

    public HeroTracker( AppState state ) {
        super();
        this.state = state;
        itemCache = new HashMap<Hero, TreeMap<Long, Integer[]>>();
        abilityCache = new HashMap<Hero, TreeMap<Long, Set<Integer>>>();
    }

    @Override
    public void entityCreated( long tickMs, Entity e ) {
        if (e.getEntityClass().getName().contains( "CDOTA_Unit_Hero_" )) {

            if (state.getHero( e.getId() ) == null) {
                state.setHero( e.getId(), new Hero( AppState.getHeroName( e.getEntityClass().getName() ) ) );
            }
            for (final Property p : e.getProperties()) {
                final String name = p.getName();
                if (name.contains( "m_hAbilities" )) {

                    int value = (int) e.getProperty( name ).getValue();
                    if (value != 0x1FFFFF) {
                        final Hero h = state.getHero( e.getId() );
                        final int slot = Integer.parseInt( name.substring( name.lastIndexOf( "." ) + 1 ) );
                        value &= 0x7ff;

//                        h.addAbility( DotaPlay.getTickMs(), slot, value );
                        final Ability a = state.getAbility( tickMs, value );
                        if (a == null) {
                            Logger.getLogger( getClass().getName() ).warning( "Hero " + h.getName() + " has an odd ability" ); //Most likely a temporary entity
                        }
                        else {
                            h.getAbilities().add( a );
                        }
                    }
                }

            }
        }
    }

    @Override
    public <T> void entityUpdated( long tickMs, Entity e, String name, T oldValue ) {
        if (e.getEntityClass().getDtName().contains( "DT_DOTA_Unit_Hero" )) {
            final Hero h = state.getHero( e.getId() );
            if (name.equals( "DT_DOTA_BaseNPC.m_cellX" )) {
                state.getHero( e.getId() ).addX( tickMs, (Integer) e.getProperty( name ).getValue() );
            }
            else if (name.equals( "DT_DOTA_BaseNPC.m_cellY" )) {
                state.getHero( e.getId() ).addY( tickMs, (Integer) e.getProperty( name ).getValue() );
            }
            else if (name.contains( "m_hItems" )) {
                final int slot = Integer.parseInt( name.substring( name.lastIndexOf( "." ) + 1 ) );
                int value = (int) e.getProperty( name ).getValue();
                if (value != 0x1FFFFF) {
                    value &= 0x7ff;
//                    h.setItem( DotaPlay.getTickMs(), slot, value );
                    setItemInCache( h, tickMs, slot, value );
                }
                else {
//                    h.setItem( DotaPlay.getTickMs(), slot, null );
                    setItemInCache( h, tickMs, slot, null );
                }
            }
            else if (name.contains( "m_hAbilities" )) {
                int value = (int) e.getProperty( name ).getValue();
                if (value != 0x1FFFFF) {

//                    final int slot = Integer.parseInt( name.substring( name.lastIndexOf( "." ) + 1 ) );
                    value &= 0x7ff;
//                    final Ability a = state.getAbility( tickMs, value );
//                    if (a == Ability.UNKNOWN_ABILITY) {
//                        Logger.getLogger( getClass().getName() ).warning( "Hero " + h.getName() + " has an odd ability" ); //Most likely a temporary entity
//                    }
//                    else {
//                        h.getAbilities().add( a );
//                    }
                    setAbilityInCache( h, tickMs, value );
                }
            }
        }
    }

    @Override
    public void parseComplete( long tickMs, ParseState state ) {
        super.parseComplete( tickMs, state );

        //Items
        for (final Hero h : itemCache.keySet()) {
            final TreeMap<Long, Integer[]> heroItems = itemCache.get( h );
            for (final Long l : heroItems.keySet()) {
                final Integer[] frameItems = heroItems.get( l );
                for (int slot = 0; slot < frameItems.length; slot++) {
                    if (frameItems[slot] != null) {
                        final Dota2Item i = this.state.getItem( l, frameItems[slot] );
                        h.setItem( l, slot, i );
                    }
                    else {
                        h.setItem( l, slot, null );
                    }
                }
            }
        }

        //Abilities
        for (final Hero h : abilityCache.keySet()) {
            final TreeMap<Long, Set<Integer>> heroAbilities = abilityCache.get( h );
            for (final Entry<Long, Set<Integer>> e : heroAbilities.entrySet()) {
                for (final Integer value : e.getValue()) {
                    final Ability a = this.state.getAbility( e.getKey(), value );
                    if (a == Ability.UNKNOWN_ABILITY) {
                        Logger.getLogger( getClass().getName() ).warning( "Hero " + h.getName() + " has an odd ability " + value ); //Most likely a temporary entity
                    }
                    else {
                        h.getAbilities().add( a );
                    }
                }
            }
        }
        abilityCache.clear();
        itemCache.clear();
    }

    private void setAbilityInCache( Hero h, long tickMs, int value ) {
        TreeMap<Long, Set<Integer>> abilities = abilityCache.get( h );
        if (abilities == null) {
            abilities = new TreeMap<Long, Set<Integer>>();
            abilities.put( 0l, new HashSet() );
            abilityCache.put( h, abilities );
        }
        if (abilities.containsKey( tickMs )) {
            //Just store the update
            abilities.get( tickMs ).add( value );
        }
        else {
            //We advanced. Push the current bag configuration, calculate the diff to the previous one and make
            //a new array for the new tick
            final Entry<Long, Set<Integer>> current = abilities.floorEntry( tickMs );

            final Set<Integer> newBag = new HashSet( current.getValue() );
            newBag.add( value );
            abilities.put( tickMs, newBag );

        }

    }

    private void setItemInCache( Hero h, long tickMs, int slot, Integer value ) {
        TreeMap<Long, Integer[]> items = itemCache.get( h );
        if (items == null) {
            items = new TreeMap<Long, Integer[]>();
            items.put( 0l, new Integer[Hero.BAG_SIZE] );
            itemCache.put( h, items );
        }
        if (items.containsKey( tickMs )) {
            //Just store the update
            items.get( tickMs )[slot] = value;
        }
        else {
            //We advanced. Push the current bag configuration, calculate the diff to the previous one and make
            //a new array for the new tick
            final Entry<Long, Integer[]> current = items.floorEntry( tickMs );

            final Integer[] newBag = Arrays.copyOf( current.getValue(), current.getValue().length );
            newBag[slot] = value;
            items.put( tickMs, newBag );

        }
    }

}
