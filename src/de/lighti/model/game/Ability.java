package de.lighti.model.game;

import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import de.lighti.model.AppState;

public class Ability {
    public static final Ability UNKNOWN_ABILITY = new Ability( "<Unknown>" );
    private String key;
    private String localisedName;

    private final TreeMap<Long, Integer> level;
    private final SortedSet<Long> invocations;

    public Ability( String key ) {
        super();
        this.key = key;
        localisedName = AppState.getAbilityName( key );
        level = new TreeMap<Long, Integer>();
        invocations = new TreeSet<Long>();
    }

    public void addInvocation( long tickMs ) {
        invocations.add( tickMs );
    }

    public SortedSet<Long> getInvocations() {
        return invocations;
    }

    public String getKey() {
        return key;
    }

    public TreeMap<Long, Integer> getLevel() {
        return level;
    }

    public String getLocalisedName() {
        return localisedName;
    }

    public void setKey( String key ) {
        this.key = key;
        localisedName = AppState.getAbilityName( key );
    }

    public void setLevel( long tickMs, int level ) {
        if (this.level.isEmpty() || this.level.floorEntry( tickMs ).getValue() < level) {
            this.level.put( tickMs, level );
        }
    }

    @Override
    public String toString() {
        return getLocalisedName();
    }

}
