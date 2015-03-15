package de.lighti;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.xerial.snappy.Snappy;

import com.valve.dota2.Demo.EDemoCommands;

import de.lighti.util.Utils;

/**
 * A DemoFile wraps the contents of a Dota2 replay file on disc. It provides
 * low-level access to CDemo packets.
 * 
 * @author Tobias Mahlmann
 *
 */
public class DemoFile {
    /**
     * Basic file information.
     * @author Tobias Mahlmann
     *
     */
    private class DemoHeader {
        /**
         * The expect header of a replay file.
         */
        private final static String PROTODEMO_HEADER_ID = "PBUFDEM\0";

        char[] demoFilestamp = new char[8]; // PROTODEMO_HEADER_ID
        int fileinfoOffset;
    }

    /**
     * Pointer to underlying filestream.
     */
    private FileInputStream inputFile;

    /**
     * Header structure.
     */
    private DemoHeader header;

    public int bytesRemaining() {
        try {
            return (inputFile != null) ? inputFile.available() : 0;
        }
        catch (final IOException e) {
            return 0;
        }
    }

    public void close() throws IOException {
        if (inputFile != null) {
            inputFile.close();
        }
        header = null;
    }

    public boolean isDone() throws IOException {
        return (inputFile == null) || (inputFile.available() == 0);
    }

    public void open( String filename ) throws IOException {

        close();

        final File fp = new File( filename );
        if (fp.canRead()) {
            inputFile = new FileInputStream( fp );
            header = new DemoHeader();

            for (int i = 0; i < 8; i++) {
                header.demoFilestamp[i] = (char) inputFile.read();
            }
            final byte[] offset = new byte[4];
            offset[0] = (byte) inputFile.read();
            offset[1] = (byte) inputFile.read();
            offset[2] = (byte) inputFile.read();
            offset[3] = (byte) inputFile.read();
            header.fileinfoOffset = Utils.toInt( offset, 0 );
            Logger.getLogger( DemoFile.class.getName() ).fine( "Fileinfo offset: " + header.fileinfoOffset );

            if (!DemoHeader.PROTODEMO_HEADER_ID.equals( new String( header.demoFilestamp ) )) {
                throw new IllegalStateException( "Open: demofilestamp doesn't match. " + filename );
            }
        }
        else {
            throw new IllegalArgumentException( "Can't open file" );
        }
    }

    public byte[] readMessage( boolean compressed ) throws IOException {
        final int size = Utils.readVarInt( inputFile );
        Logger.getLogger( DemoFile.class.getName() ).fine( "Reading Message of " + size + " bytes. Compression= " + compressed );
        final byte[] buffer = new byte[size];
        inputFile.read( buffer );

        if (!compressed) {
            return buffer;
        }
        else {
            return Snappy.uncompress( buffer );
        }
    }

    public EDemoCommands readMessageType( int[] tick, boolean[] compressed ) throws IOException {
        int cmd = Utils.readVarInt( inputFile );

        compressed[0] = (cmd & EDemoCommands.DEM_IsCompressed.getNumber()) != 0;
        cmd = (cmd & ~EDemoCommands.DEM_IsCompressed.getNumber());

        tick[0] = Utils.readVarInt( inputFile );

        return EDemoCommands.valueOf( cmd );
    }
}
