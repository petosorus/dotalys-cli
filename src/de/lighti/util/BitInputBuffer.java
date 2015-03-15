package de.lighti.util;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * A BitInputBuffer wraps a stream of bytes so they can be read bit by bit.
 *
 * @author Tobias Mahlmann
 *
 */
public class BitInputBuffer {

    /**
     * Converts an array of bits into a (signed) integer.
     * @param a an array of bits. The first array element is treated as the highest bit.
     * @return the integer
     */
    private static int encodeBoolAsInt( boolean[] a ) {
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            final int value = (a[i] ? 1 : 0) << i;
            result = result | value;
        }

        return result;
    }

    /**
     * back buffer.
     */
    private final BitSet data;

    /**
     * Current position in data.
     */
    private int position;

    public BitInputBuffer( byte[] bytes ) {
        data = BitSet.valueOf( bytes );
    }

    public BitInputBuffer( ByteBuffer data ) {
        final byte[] temp = new byte[data.capacity()];
        data.get( temp );
        this.data = BitSet.valueOf( temp );
    }

    public int getPosition() {
        return position;
    }

    public boolean hasNext() {

        return position < data.length();
    }

    public boolean readBit() {
        return readBits( 1 )[0];
    }

    public void readBits( byte[] data, int bit_length ) {

        int remaining = bit_length;

        int i = 0;

        while (remaining >= 8) {
            data[i++] = (byte) readBitsAsInt( 8 );
            remaining -= 8;
        }

        if (remaining > 0) {
            data[i++] = (byte) readBitsAsInt( remaining );
        }
    }

    public boolean[] readBits( int bit_length ) {
        final boolean[] ret = new boolean[bit_length];
        for (int i = 0; i < bit_length; i++) {
            ret[i] = data.get( position++ );
        }
        return ret;
    }

    public void readBits( int[] data, int bit_length ) {

        int remaining = bit_length;

        int i = 0;

        while (remaining >= 8) {
            data[i++] = (char) readBitsAsInt( 8 );
            remaining -= 8;
        }

        if (remaining > 0) {
            data[i++] = (char) readBitsAsInt( remaining );
        }
    }

    public float readBitsAsFloat( int entryBits ) {
        return Float.intBitsToFloat( readBitsAsInt( entryBits ) );
    }

    public int readBitsAsInt( int entryBits ) {
        return encodeBoolAsInt( readBits( entryBits ) );
    }

    public String readString( int maxSize ) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < maxSize - 1; ++i) {
            final char c = (char) encodeBoolAsInt( readBits( 8 ) );

            if (c == '\0') {
                return builder.toString();
            }
            else {
                builder.append( c );
            }
        }

        //  builder.append( '\0' );
        return builder.toString();
    }

    public int readVar35() {
        int read = 0;

        int value = 0;
        int got;
        do {
            got = readBitsAsInt( 8 );

            final int lower = got & 0x7F;
//            final int upper = got >>> 7;

            value |= lower << read;
            read += 7;
        }
        while (got >>> 7 > 0 && read < 35);

        return value;
    }

}