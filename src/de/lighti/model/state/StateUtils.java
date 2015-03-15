package de.lighti.model.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import de.lighti.util.Utils;

public class StateUtils {
    static class DTProp {
        SendTable sendTable;
        Vector<DTProp> dtProps;
        Vector<SendProp> nonDtProps;

        int propStart;
        int propCount;

        public DTProp() {
            dtProps = new Vector<DTProp>();
            nonDtProps = new Vector<SendProp>();
        }
    };

    public static class FlatSendTable {

        private final String netTableName;
        private Vector<SendProp> props;

        private DTProp dtProp;

        FlatSendTable( String netTableName ) {
            this.netTableName = netTableName;
        }

        public DTProp getDtProp() {
            return dtProp;
        }

        public String getNetTableName() {
            return netTableName;
        }

        public Vector<SendProp> getProps() {
            return props;
        }

        public void setDtProp( DTProp dtProp ) {
            this.dtProp = dtProp;
        }

        public void setProps( Vector<SendProp> props ) {
            this.props = props;
        }
    };

    public static class SendProp {
        private final SP_Type type;

        private final String name;

        private final int flags;
        private final int priority;
        private final String dtName;
        private final int numElements;

        private final float lowValue;
        private final float highValue;

        private final int numBits;
        private SendTable inTable;
        private SendProp arrayProp;

        public SendProp( SP_Type type, String name, int flags, int priority, String dtName, int numElements, float lowValue, float highValue, int numBits ) {

            this.type = type;
            this.name = name;
            this.flags = flags;
            this.priority = priority;
            this.dtName = dtName;
            this.numElements = numElements;
            this.lowValue = lowValue;
            this.highValue = highValue;
            this.numBits = numBits;
        }

        public SendProp getArrayProp() {
            return arrayProp;
        }

        public String getDtName() {
            return dtName;
        }

        public int getFlags() {
            return flags;
        }

        public float getHighValue() {
            return highValue;
        }

        public SendTable getInTable() {
            return inTable;
        }

        public float getLowValue() {
            return lowValue;
        }

        public String getName() {
            return name;
        }

        public int getNumberOfElements() {
            return numElements;
        }

        public int getNumBits() {
            return numBits;
        }

        public int getPriority() {
            return priority;
        }

        public SP_Type getType() {

            return type;
        }

        public void setArrayProp( SendProp sendProp ) {
            arrayProp = sendProp;

        }

        public void setInTable( SendTable inTable ) {
            this.inTable = inTable;
        }

        @Override
        public String toString() {
            return "SendProp [type=" + type + ", name=" + name + ", lowValue=" + lowValue + ", highValue=" + highValue + "]";
        }

    };

    public static class SendTable {

        private String netTableName;

        private boolean needsDecoder;
        private final List<SendProp> props;

        public SendTable() {
            props = new ArrayList<SendProp>();
        }

        public SendTable( String netTableName, boolean needsDecoder ) {
            this();
            this.netTableName = netTableName;
            this.needsDecoder = needsDecoder;
        }

        public String getName() {
            return netTableName;
        }

        public List<SendProp> getProps() {
            return props;
        }
    };

    public enum SP_Flags {
        SP_Unsigned( 1 << 0 ), SP_Coord( 1 << 1 ), SP_NoScale( 1 << 2 ), SP_RoundDown( 1 << 3 ), SP_RoundUp( 1 << 4 ), SP_Normal( 1 << 5 ), SP_Exclude( 1 << 6 ), SP_Xyze(
                        1 << 7 ), SP_InsideArray( 1 << 8 ),

        SP_Collapsible( 1 << 11 ), SP_CoordMp( 1 << 12 ), SP_CoordMpLowPrecision( 1 << 13 ), SP_CoordMpIntegral( 1 << 14 ), SP_CellCoord( 1 << 15 ), SP_CellCoordLowPrecision(
                        1 << 16 ), SP_CellCoordIntegral( 1 << 17 ), SP_ChangesOften( 1 << 18 ), SP_EncodedAgainstTickcount( 1 << 19 );
        public static SP_Flags valueOf( int id ) {
            for (final SP_Flags f : SP_Flags.values()) {
                if (f.id == id) {
                    return f;
                }
            }

            return null;
        }

        private final int id;

        private SP_Flags( int id ) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    };

    public enum SP_Type {
        SP_Int( 0 ), SP_Float( 1 ), SP_Vector( 2 ), SP_VectorXY( 3 ), SP_String( 4 ), SP_Array( 5 ), SP_DataTable( 6 ), SP_Int64( 7 );
        public static SP_Type valueOf( int id ) {
            switch (id) {
                case 0:
                    return SP_Int;
                case 1:
                    return SP_Float;
                case 2:
                    return SP_Vector;
                case 3:
                    return SP_VectorXY;
                case 4:
                    return SP_String;
                case 5:
                    return SP_Array;
                case 6:
                    return SP_DataTable;
                case 7:
                    return SP_Int64;
                default:
                    return null;
            }
        }

        private final int id;

        private SP_Type( int id ) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    };

    public enum ST_Flags {
        ST_Something( 2 ), ST_FixedLength( 8 );

        public static ST_Flags valueOf( int id ) {
            switch (id) {
                case 2:
                    return ST_Something;
                case 8:
                    return ST_FixedLength;
                default:
                    return null;
            }
        }

        private final int id;

        private ST_Flags( int id ) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    };

    public static class StringTable extends LinkedHashMap<String, StringTableEntry> {

        /**
         * 
         */
        private static final long serialVersionUID = -3539920946466168199L;
        private final String name;
        private final int max_entries;

        private final boolean user_data_fixed_size;
        private final int userDataSize;
        private final int userDataSizeBits;
        private final int flags;
        private final int entry_bits;

        public StringTable( String name, int max_entries, boolean user_data_fixed_size, int user_data_size, int user_data_size_bits, int flags ) {
            super();
            this.name = name;
            this.max_entries = max_entries;
            this.user_data_fixed_size = user_data_fixed_size;
            userDataSize = user_data_size;
            userDataSizeBits = user_data_size_bits;
            this.flags = flags;
            entry_bits = Utils.log2( max_entries );

        }

        public StringTableEntry get( int id ) {
            final Iterator<String> iter = keySet().iterator();
            String key = iter.next();
            for (int i = 0; i < id; i++) {
                key = iter.next();
            }
            return get( key );
        }

        public int getEntryBits() {
            return entry_bits;
        }

        public int getFlags() {
            return flags;
        }

        public int getMaxEntries() {
            return max_entries;
        }

        public String getName() {
            return name;
        }

        public int getUserDataSize() {
            return userDataSize;
        }

        public int getUserDataSizeBits() {
            return userDataSizeBits;
        }

        public boolean isUser_data_fixed_size() {
            return user_data_fixed_size;
        }

        public StringTableEntry put( String key, byte[] value ) {
            if (containsKey( key )) {
                throw new IllegalArgumentException( "Entry " + key + " already exists." );
            }

            final StringTableEntry entry = new StringTableEntry( key, value );
            return put( key, entry );
        }

    };

    public static class StringTableEntry {
        public String key;
        public byte[] value;

        public StringTableEntry( String key, byte[] value ) {
            super();
            this.key = key;
            this.value = value;
        }
    };
}
