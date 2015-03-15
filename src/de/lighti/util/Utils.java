package de.lighti.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Utility class that provides a few data conversion routines.
 * @author Tobias Mahlmann
 *
 */
public final class Utils {

    /**
     * Calculates the logarithm dualis aka the number of bytes necessary
     * to represent n distinct values.
     * @param n the maximum number of values
     * @return the number of bits necessary to represent n different values
     */
    public static int log2( int n ) {
        if (n < 1) {
            throw new IllegalArgumentException( "Invalid number passed to log2 " + n + "." );
        }

        int r = 0;
        int acc = 1;

        while (acc < n) {
            ++r;
            acc *= 2;
        }

        return r;
    }

    public static int readVarInt( ByteBuffer stream ) throws IOException {
        final byte b[] = new byte[1];
        int count = 0;
        int result = 0;

        do {
            if (count == 5) {
                // If we get here it means that the fifth bit had its
                // high bit set, which implies corrupt data.
                throw new IOException( "stream corrupted" );
            }
            stream.get( b );

            result |= (b[0] & 0x7F) << (7 * count);
            ++count;
        }
        while ((b[0] & 0x80) > 0);

        return result;
    }

    public static int readVarInt( InputStream stream ) throws IOException {
        final byte b[] = new byte[1];
        int count = 0;
        int result = 0;

        do {
            if (count == 5) {
                // If we get here it means that the fifth bit had its
                // high bit set, which implies corrupt data.
                throw new IOException( "stream corrupted" );
            }
            if (stream.read( b ) == -1) {
                throw new IOException( "end of stream" );
            }
            result |= (b[0] & 0x7F) << (7 * count);
            ++count;
        }
        while ((b[0] & 0x80) > 0);

        return result;
    }

    public static int toInt( byte[] bytes, int offset ) {
        int ret = 0;
        for (int i = 0; (i < 4) && ((i + offset) < bytes.length); i++) {
            ret <<= 8;
            ret |= bytes[i] & 0xFF;
        }
        return ret;
    }

    /**
     * Private constructor for utility class.
     */
    private Utils() {

    }

}
