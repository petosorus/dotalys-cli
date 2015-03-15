package de.lighti.model;

import java.util.Vector;

import de.lighti.model.state.StateUtils.SP_Flags;
import de.lighti.model.state.StateUtils.SP_Type;
import de.lighti.model.state.StateUtils.SendProp;
import de.lighti.util.BitInputBuffer;

/**
 * This class represents a property of an Entity.
 *
 * The code is based on the c++ parser library https://github.com/dschleck/edith
 *
 * @author Tobias Mahlmann
 *
 * @param <T>
 */
public class Property<T> {
    enum FloatType {
        FT_None, FT_LowPrecision, FT_Integral,
    }

    private static int getArrayLengthBits( SendProp prop ) {
        int n = prop.getNumberOfElements();
        int bits = 0;

        while (n > 0) {
            ++bits;
            n >>>= 1;
        }

        return bits;
    };

    private static Vector<Property> readArray( BitInputBuffer stream, SendProp prop ) {
        if (prop.getArrayProp() == null) {
            throw new IllegalArgumentException( "Array prop has no inner prop." );
        }

        final int count = stream.readBitsAsInt( getArrayLengthBits( prop ) );

        final Vector<Property> elements = new Vector();
        for (int i = 0; i < count; ++i) {
            elements.add( readProperty( stream, prop.getArrayProp() ) );
        }
        return elements;
    }

    private static Float readFloat( BitInputBuffer stream, SendProp prop ) {
        final int flags = prop.getFlags();
        if ((flags & SP_Flags.SP_Coord.getId()) > 0) {
            return readFloatCoord( stream );
        }
        else if ((flags & SP_Flags.SP_CoordMp.getId()) > 0) {
            return readFloatCoordMp( stream, FloatType.FT_None );
        }
        else if ((flags & SP_Flags.SP_CoordMpLowPrecision.getId()) > 0) {
            return readFloatCoordMp( stream, FloatType.FT_LowPrecision );
        }
        else if ((flags & SP_Flags.SP_CoordMpIntegral.getId()) > 0) {
            return readFloatCoordMp( stream, FloatType.FT_Integral );
        }
        else if ((flags & SP_Flags.SP_NoScale.getId()) > 0) {
            return readFloatNoScale( stream );
        }
        else if ((flags & SP_Flags.SP_Normal.getId()) > 0) {
            return readFloatNormal( stream );
        }
        else if ((flags & SP_Flags.SP_CellCoord.getId()) > 0) {
            return readFloatCellCoord( stream, FloatType.FT_None, prop.getNumBits() );
        }
        else if ((flags & SP_Flags.SP_CellCoordLowPrecision.getId()) > 0) {
            return readFloatCellCoord( stream, FloatType.FT_LowPrecision, prop.getNumBits() );
        }
        else if ((flags & SP_Flags.SP_CellCoordIntegral.getId()) > 0) {
            return readFloatCellCoord( stream, FloatType.FT_Integral, prop.getNumBits() );
        }
        else {
            final float value = stream.readBitsAsInt( prop.getNumBits() );
            float a = value;

            float b = (1 << prop.getNumBits()) - 1;

            a /= b;

            float range = prop.getHighValue() - prop.getLowValue();
            b = range;
            a *= b;

            range = prop.getLowValue();
            b = range;
            a += b;

            return a;
        }
    }

    private static float readFloatCellCoord( BitInputBuffer stream, FloatType type, int bits ) {
        final int value = stream.readBitsAsInt( bits );

        if (type == FloatType.FT_None || type == FloatType.FT_LowPrecision) {
            final boolean lp = type == FloatType.FT_LowPrecision;

            int second;
            if (!lp) {
                second = stream.readBitsAsInt( 5 );
            }
            else {
                second = stream.readBitsAsInt( 3 );
            }

            float a;
            if (!lp) {
                a = 0x3FA00000;
            }
            else {
                a = 0x3FC00000;
            }

            double b = second;
            b *= a;

            a = 0f;
            a = value;

            b += a;
            a = (float) b;

            return a;
        }
        else if (type == FloatType.FT_Integral) {

            float f = value;

            if (value >>> 31 > 0) {
                f += 4.2949673e9f;
            }

            return f;
        }
        else {
            throw new IllegalStateException( "unknown float type" );
        }
    }

    private static float readFloatCoord( BitInputBuffer stream ) {
        int first = stream.readBitsAsInt( 1 );
        int second = stream.readBitsAsInt( 1 );

        float a = 0;
        float b = 0;

        if (first > 0 || second > 0) {
            final int third = stream.readBitsAsInt( 1 );

            if (first > 0) {
                first = stream.readBitsAsInt( 0x0E ) + 1;
            }

            if (second > 0) {
                second = stream.readBitsAsInt( 5 );
            }

            b = first;
            a = second;

            final float special = 0x3D000000;
            a *= special;
            double ad = a;

            final double bd = b;

            ad *= bd;
            a = (float) ad;

            if (third > 0) {
                final int mask = 0x80000000;
                a = (int) a ^ mask;
            }
        }

        return a;
    }

    private static float readFloatCoordMp( BitInputBuffer stream, FloatType type ) {
        int value;

        if (type == FloatType.FT_LowPrecision || type == FloatType.FT_None) {
            throw new UnsupportedOperationException();
        }
        else if (type == FloatType.FT_Integral) {
            int a = stream.readBitsAsInt( 1 );
            final int b = stream.readBitsAsInt( 1 );
            a = a + 2 * b;

            if (b == 0) {
                return 0;
            }
            else {
                if (a > 0) {
                    value = stream.readBitsAsInt( 12 );
                }
                else {
                    value = stream.readBitsAsInt( 15 );
                }

                if ((value & 1) > 0) {
                    value = -((value >>> 1) + 1);
                }
                else {
                    value = (value >>> 1) + 1;
                }
            }
        }
        else {
            throw new IllegalArgumentException( "Unknown coord type" );
        }

        return value;
    }

    private static float readFloatNormal( BitInputBuffer stream ) {

        final boolean first = stream.readBit();
        final int second = stream.readBitsAsInt( 11 );

        float f = second;

        if (second >>> 31 > 0) {
            f += 4.2949673e9f;
        }

        f *= 4.885197850512946e-4f;

        return (int) f ^ 0x80000000;

    }

    private static float readFloatNoScale( BitInputBuffer stream ) {
        return stream.readBitsAsFloat( 32 );
    }

    private static Integer readInteger( BitInputBuffer stream, SendProp prop ) {

        if ((prop.getFlags() & SP_Flags.SP_EncodedAgainstTickcount.getId()) > 0) {
            if ((prop.getFlags() & SP_Flags.SP_Unsigned.getId()) > 0) {
                return stream.readVar35();
            }
            else {
                final int value = stream.readVar35();
                return -(value & 1) ^ value >> 1;
            }
        }
        else {
            int value = stream.readBitsAsInt( prop.getNumBits() );
            final int signer = 0x80000000 >> 32 - prop.getNumBits() & (prop.getFlags() & SP_Flags.SP_Unsigned.getId()) - 1;

            value = value ^ signer;
            return value - signer;

        }
    }

    private static long readLong( BitInputBuffer stream, SendProp prop ) {
        if ((SP_Flags.SP_EncodedAgainstTickcount.getId() & prop.getFlags()) > 0) {
            if ((prop.getFlags() & SP_Flags.SP_Unsigned.getId()) > 0) {
                return stream.readVar35();
            }
            else {
                final long value = stream.readVar35();
                return -(value & 1) ^ value >> 1;
            }
        }
        else {
            boolean negate = false;
            int second_bits = prop.getNumBits() - 32;

            if ((SP_Flags.SP_Unsigned.getId() & prop.getFlags()) == 0) {
                --second_bits;

                if (stream.readBit()) {
                    negate = true;
                }
            }

            if (prop.getNumBits() < second_bits) {
                throw new IllegalStateException( "Invalid number of bits" );
            }

            final long a = stream.readBitsAsInt( 32 );
            final long b = stream.readBitsAsInt( second_bits );
            long value = a << 32 | b;

            if (negate) {
                value *= -1;
            }

            return value;
        }
    }

    public static Property<?> readProperty( BitInputBuffer stream, SendProp prop ) {

        final String name = prop.getInTable().getName() + "." + prop.getName();
        final Property<?> out;
        switch (prop.getType()) {
            case SP_Array:
                final Vector<Property> elements = readArray( stream, prop );
                out = new Property<Vector<Property>>( name, SP_Type.SP_Array, elements );
                break;
            case SP_Float:
                out = new Property<Float>( name, SP_Type.SP_Float, readFloat( stream, prop ) );
                break;
            case SP_Int:
                out = new Property<Integer>( name, SP_Type.SP_Int, readInteger( stream, prop ) );
                break;
            case SP_Int64:
                out = new Property<Long>( name, SP_Type.SP_Int64, readLong( stream, prop ) );
                break;
            case SP_String:
                final String stringValue = readString( MAX_STRING_LENGTH, stream, prop );
                out = new Property<String>( name, SP_Type.SP_String, stringValue );
                break;

            case SP_Vector:
                final float[] vectorValue = readVector( stream, prop );
                out = new Property<float[]>( name, SP_Type.SP_Vector, vectorValue );
                break;

            case SP_VectorXY:
                final float[] xyValue = readVectorXY( stream, prop );
                out = new Property<float[]>( name, SP_Type.SP_VectorXY, xyValue );
                break;
            default:
                throw new IllegalArgumentException( "Unsupported send prop type " + prop.getType() );

        }
        return out;
    }

    private static String readString( int max_length, BitInputBuffer stream, SendProp prop ) {
        final int length = stream.readBitsAsInt( 9 );
        if (length > max_length) {
            throw new IllegalStateException( "String too long " + length + " > " + max_length );
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append( (char) stream.readBitsAsInt( 8 ) );
        }

        return builder.toString();
    }

    private static float[] readVector( BitInputBuffer stream, SendProp prop ) {
        final float vector[] = new float[3];
        vector[0] = readFloat( stream, prop );
        vector[1] = readFloat( stream, prop );

        if ((prop.getFlags() & SP_Flags.SP_Normal.getId()) > 0) {
            final boolean first = stream.readBit();

            float a = vector[0] * vector[0];
            float b = vector[1] * vector[1];

            a += b;

            b = 0x3D000000;

            if (a <= b) {
                a = 0f;
            }
            else {
                b -= a;
                a = (float) Math.sqrt( b );
            }

            if (first) {
                final float special = 0x3D000000;
                a *= special;
            }

            vector[2] = a;
        }
        else {
            vector[2] = readFloat( stream, prop );
        }

        return vector;
    }

    private static float[] readVectorXY( BitInputBuffer stream, SendProp prop ) {
        final float[] vector = new float[2];
        vector[0] = readFloat( stream, prop );
        vector[1] = readFloat( stream, prop );
        return vector;
    }

    private static final int MAX_STRING_LENGTH = 0x200;

    private final String name;

    private final SP_Type type;

    private final T value;

    public Property( String name, SP_Type type, T v ) {
        super();
        this.name = name;
        this.type = type;
        this.value = v;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Property [name=" + name + ", type=" + type + ", value=" + value + "]";
    }

}
