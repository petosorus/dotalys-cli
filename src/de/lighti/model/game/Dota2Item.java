package de.lighti.model.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.lighti.io.ImageCache;
import de.lighti.model.Statics;

public class Dota2Item {
    private String key;
    //TODO unused yet
    private String name;

    public final static Dota2Item UNKNOWN_ITEM = new Dota2Item( Statics.UNKNOWN_ITEM, false );

    private static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    private final boolean fetchImage;
    private final List<Long> usage;

    public Dota2Item( String key ) {
        this( key, true );
    }

    public Dota2Item( String key, boolean fetchImage ) {
        super();
        if (!key.isEmpty() && !key.startsWith( "item" ) && key != Statics.UNKNOWN_ITEM) {
            Logger.getLogger( getClass().getName() ).warning( key + ": an item that doesn't start with \"item\" is probably not an item" );
        }

        this.key = key;
        this.fetchImage = fetchImage;
        usage = new ArrayList<Long>();
    }

    public void addUsage( long time ) {
        if (time < 0l) {
            throw new IllegalArgumentException( "time out of range" );
        }

        usage.add( time );
    }

    public BufferedImage getImage() throws IOException {
        if (fetchImage) {
            BufferedImage i = images.get( key );
            if (i == null && !images.containsKey( key )) {
                i = ImageCache.getItemImage( key );
                images.put( key, i );
            }
            return i;
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name != null ? name : key;
    }

    public List<Long> getUsage() {
        return usage;
    }

    public void setKey( String key ) {
        if (!key.isEmpty() && !key.startsWith( "item" ) && key != Statics.UNKNOWN_ITEM) {
            Logger.getLogger( getClass().getName() ).warning( key + ": an item that doesn't start with \"item\" is probably not an item" );
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException( "key must not be empty" );
        }
        this.key = key;
    }

}
