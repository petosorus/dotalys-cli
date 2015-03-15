package de.lighti.model.game;

import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Player {
    private String name;
    private final int id;

    private Hero hero;

    private boolean isRadiant;

    private final TreeMap<Long, Integer> xp;
    private final TreeMap<Long, Integer> gold;
    private final SortedSet<Long> deaths;

    public Player( int id ) {

        this.id = id;
        xp = new TreeMap<Long, Integer>();
        xp.put( 0l, 0 );
        gold = new TreeMap<Long, Integer>();
        gold.put( 0l, 0 );

        deaths = new TreeSet<Long>();
    }

    public void addDeath( Long time ) {
        deaths.add( time );
    }

    public SortedSet<Long> getDeaths() {
        return getDeaths( null );
    }

    public SortedSet<Long> getDeaths( Long upUnntilTime ) {
        if (upUnntilTime == null) {
            return deaths;
        }
        else {
            return deaths.headSet( upUnntilTime );
        }
    }

    public int getEarnedGold( long time ) {
        return gold.floorEntry( time ).getValue();
    }
    
    public TreeMap<Long, Integer> getGold() {
		return gold;
	}

    public Hero getHero() {
        return hero;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalEarnedGold() {
        return gold.lastEntry().getValue();
    }

    public int getTotalXP() {
        return xp.lastEntry().getValue();
    }

    public int getXP( long time ) {
        return xp.floorEntry( time ).getValue();
    }
    
    public TreeMap<Long, Integer> getXp() {
		return xp;
	}

    public boolean isRadiant() {
        return isRadiant;
    }

    public void setHero( Hero hero ) {
        this.hero = hero;
    }

    public void setName( String value ) {
        name = value;
    }

    public void setRadiant( boolean isRadiant ) {
        this.isRadiant = isRadiant;
    }

    public void setTotalEarnedGold( long time, int totalEarnedGold ) {
        gold.put( time, totalEarnedGold );
    }

    public void setTotalXP( long time, int value ) {
        xp.put( time, value );
    }

}
