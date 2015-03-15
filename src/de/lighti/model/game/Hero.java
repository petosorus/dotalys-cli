package de.lighti.model.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Hero extends Unit {
    public class ItemEvent {
        public Dota2Item item;
        public int slot;
        public boolean added;
        public long tick;

        private ItemEvent( long tick, Dota2Item item, int slot, boolean added ) {
            super();
            this.tick = tick;
            this.item = item;
            this.slot = slot;
            this.added = added;
        }

    }

    private static int countItem( Dota2Item[] list, Dota2Item n ) {
        if (n == null) {
            throw new IllegalArgumentException( "n must not be null" );
        }
        int count = 0;
        for (final Dota2Item m : list) {
            if (m != null && m.getKey().equals( n.getKey() )) {
                count++;
            }
        }
        return count;
    }

    private final TreeMap<Long, Dota2Item[]> items;

    private final Set<Ability> abilities;
    private final Queue<ItemEvent> itemLog;

    private final TreeMap<Long, int[]> deaths;

    public final static int BAG_SIZE = 12; //two bags of 6. Not sure where courier itmes go

    public Hero( String name ) {
        super( name );

        items = new TreeMap<Long, Dota2Item[]>();
        items.put( 0l, new Dota2Item[BAG_SIZE] );
        itemLog = new LinkedBlockingQueue<ItemEvent>();
        abilities = new HashSet<Ability>();
        deaths = new TreeMap<>();
    }

    public void addDeath( long tickMs, int x, int y ) {
        deaths.put( tickMs, new int[] { x, y } );
    }

    private void generateLogEntries( long tick, Dota2Item[] previous, Dota2Item[] current ) {
        for (int i = 0; i < previous.length; i++) {
            if (previous[i] != current[i]) {
                if (current[i] != null) {
                    if (countItem( previous, current[i] ) == 0) {
                        itemLog.add( new ItemEvent( tick, current[i], i, true ) );
                    }
                }
                else if (previous[i] != null) {
                    itemLog.add( new ItemEvent( tick, previous[i], i, false ) );
                }
            }

        }
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public Ability getAbilityByName( String name ) {
        for (final Ability a : abilities) {
            if (a.getKey().equals( name )) {
                return a;
            }
        }
        return null;
    }

    /**
     * Get all items held by this hero. Beware that this will give
     * you a Dota2Item for every internal Entity that was attached to this hero, the set
     * might return a number of objects for the same actual game item. If you're interested
     * in the unique items a hero bought, you should use getItemLog() instead.
     * @return a set of items
     */
    public Set<Dota2Item> getAllItems() {
        final Set<Dota2Item> ret = new HashSet<Dota2Item>();
        for (final Dota2Item[] a : items.values()) {
            for (final Dota2Item i : a) {
                if (i != null) {
                    ret.add( i );
                }
            }
        }
        return ret;
    }

    public TreeMap<Long, int[]> getDeaths() {
        return deaths;
    }

    public Queue<ItemEvent> getItemLog() {
        return itemLog;
    }

    /**
     * Same behaviour as getAllItems, allows to filter returned items by name.
     * @param itemKey the item name
     * @return all items with a certain a hero has
     */
    public Set<Dota2Item> getItemsByName( String itemKey ) {
        final Set<Dota2Item> ret = new HashSet<Dota2Item>();
        for (final Dota2Item[] a : items.values()) {
            for (final Dota2Item i : a) {
                if (i != null && i.getKey().equals( itemKey )) {
                    ret.add( i );
                }
            }
        }
        return ret;
    }

    public void setItem( long tickMs, int slot, Dota2Item newItem ) {
        if (items.containsKey( tickMs )) {
            //Just store the update
            items.get( tickMs )[slot] = newItem;
        }
        else {
            //We advanced. Push the current bag configuration, calculate the diff to the previous one and make
            //a new array for the new tick
            final Entry<Long, Dota2Item[]> current = items.floorEntry( tickMs );
            final Entry<Long, Dota2Item[]> previous = items.floorEntry( current.getKey() - 1 );
            final Dota2Item[] newBag = Arrays.copyOf( current.getValue(), current.getValue().length );
            newBag[slot] = newItem;
            items.put( tickMs, newBag );

            //previous might be null if we actually pulled the 0l entry into current
            if (previous != null) {
                generateLogEntries( current.getKey(), previous.getValue(), current.getValue() );
            }
        }
    }

    @Override
    public String toString() {
        return "Hero [getName()=" + getName() + "]";
    }

}
