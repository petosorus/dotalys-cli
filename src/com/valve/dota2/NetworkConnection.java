// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: network_connection.proto

package com.valve.dota2;

public final class NetworkConnection {
  private NetworkConnection() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
  }
  /**
   * Protobuf enum {@code ENetworkDisconnectionReason}
   */
  public enum ENetworkDisconnectionReason
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>NETWORK_DISCONNECT_INVALID = 0;</code>
     */
    NETWORK_DISCONNECT_INVALID(0, 0),
    /**
     * <code>NETWORK_DISCONNECT_SHUTDOWN = 1;</code>
     */
    NETWORK_DISCONNECT_SHUTDOWN(1, 1),
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECT_BY_USER = 2;</code>
     */
    NETWORK_DISCONNECT_DISCONNECT_BY_USER(2, 2),
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECT_BY_SERVER = 3;</code>
     */
    NETWORK_DISCONNECT_DISCONNECT_BY_SERVER(3, 3),
    /**
     * <code>NETWORK_DISCONNECT_LOST = 4;</code>
     */
    NETWORK_DISCONNECT_LOST(4, 4),
    /**
     * <code>NETWORK_DISCONNECT_OVERFLOW = 5;</code>
     */
    NETWORK_DISCONNECT_OVERFLOW(5, 5),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_BANNED = 6;</code>
     */
    NETWORK_DISCONNECT_STEAM_BANNED(6, 6),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_INUSE = 7;</code>
     */
    NETWORK_DISCONNECT_STEAM_INUSE(7, 7),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_TICKET = 8;</code>
     */
    NETWORK_DISCONNECT_STEAM_TICKET(8, 8),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_LOGON = 9;</code>
     */
    NETWORK_DISCONNECT_STEAM_LOGON(9, 9),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHCANCELLED = 10;</code>
     */
    NETWORK_DISCONNECT_STEAM_AUTHCANCELLED(10, 10),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHALREADYUSED = 11;</code>
     */
    NETWORK_DISCONNECT_STEAM_AUTHALREADYUSED(11, 11),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHINVALID = 12;</code>
     */
    NETWORK_DISCONNECT_STEAM_AUTHINVALID(12, 12),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_VACBANSTATE = 13;</code>
     */
    NETWORK_DISCONNECT_STEAM_VACBANSTATE(13, 13),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_LOGGED_IN_ELSEWHERE = 14;</code>
     */
    NETWORK_DISCONNECT_STEAM_LOGGED_IN_ELSEWHERE(14, 14),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_VAC_CHECK_TIMEDOUT = 15;</code>
     */
    NETWORK_DISCONNECT_STEAM_VAC_CHECK_TIMEDOUT(15, 15),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_DROPPED = 16;</code>
     */
    NETWORK_DISCONNECT_STEAM_DROPPED(16, 16),
    /**
     * <code>NETWORK_DISCONNECT_STEAM_OWNERSHIP = 17;</code>
     */
    NETWORK_DISCONNECT_STEAM_OWNERSHIP(17, 17),
    /**
     * <code>NETWORK_DISCONNECT_SERVERINFO_OVERFLOW = 18;</code>
     */
    NETWORK_DISCONNECT_SERVERINFO_OVERFLOW(18, 18),
    /**
     * <code>NETWORK_DISCONNECT_TICKMSG_OVERFLOW = 19;</code>
     */
    NETWORK_DISCONNECT_TICKMSG_OVERFLOW(19, 19),
    /**
     * <code>NETWORK_DISCONNECT_STRINGTABLEMSG_OVERFLOW = 20;</code>
     */
    NETWORK_DISCONNECT_STRINGTABLEMSG_OVERFLOW(20, 20),
    /**
     * <code>NETWORK_DISCONNECT_DELTAENTMSG_OVERFLOW = 21;</code>
     */
    NETWORK_DISCONNECT_DELTAENTMSG_OVERFLOW(21, 21),
    /**
     * <code>NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW = 22;</code>
     */
    NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW(22, 22),
    /**
     * <code>NETWORK_DISCONNECT_SOUNDSMSG_OVERFLOW = 23;</code>
     */
    NETWORK_DISCONNECT_SOUNDSMSG_OVERFLOW(23, 23),
    /**
     * <code>NETWORK_DISCONNECT_SNAPSHOTOVERFLOW = 24;</code>
     */
    NETWORK_DISCONNECT_SNAPSHOTOVERFLOW(24, 24),
    /**
     * <code>NETWORK_DISCONNECT_SNAPSHOTERROR = 25;</code>
     */
    NETWORK_DISCONNECT_SNAPSHOTERROR(25, 25),
    /**
     * <code>NETWORK_DISCONNECT_RELIABLEOVERFLOW = 26;</code>
     */
    NETWORK_DISCONNECT_RELIABLEOVERFLOW(26, 26),
    /**
     * <code>NETWORK_DISCONNECT_BADDELTATICK = 27;</code>
     */
    NETWORK_DISCONNECT_BADDELTATICK(27, 27),
    /**
     * <code>NETWORK_DISCONNECT_NOMORESPLITS = 28;</code>
     */
    NETWORK_DISCONNECT_NOMORESPLITS(28, 28),
    /**
     * <code>NETWORK_DISCONNECT_TIMEDOUT = 29;</code>
     */
    NETWORK_DISCONNECT_TIMEDOUT(29, 29),
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECTED = 30;</code>
     */
    NETWORK_DISCONNECT_DISCONNECTED(30, 30),
    /**
     * <code>NETWORK_DISCONNECT_LEAVINGSPLIT = 31;</code>
     */
    NETWORK_DISCONNECT_LEAVINGSPLIT(31, 31),
    /**
     * <code>NETWORK_DISCONNECT_DIFFERENTCLASSTABLES = 32;</code>
     */
    NETWORK_DISCONNECT_DIFFERENTCLASSTABLES(32, 32),
    /**
     * <code>NETWORK_DISCONNECT_BADRELAYPASSWORD = 33;</code>
     */
    NETWORK_DISCONNECT_BADRELAYPASSWORD(33, 33),
    /**
     * <code>NETWORK_DISCONNECT_BADSPECTATORPASSWORD = 34;</code>
     */
    NETWORK_DISCONNECT_BADSPECTATORPASSWORD(34, 34),
    /**
     * <code>NETWORK_DISCONNECT_HLTVRESTRICTED = 35;</code>
     */
    NETWORK_DISCONNECT_HLTVRESTRICTED(35, 35),
    /**
     * <code>NETWORK_DISCONNECT_NOSPECTATORS = 36;</code>
     */
    NETWORK_DISCONNECT_NOSPECTATORS(36, 36),
    /**
     * <code>NETWORK_DISCONNECT_HLTVUNAVAILABLE = 37;</code>
     */
    NETWORK_DISCONNECT_HLTVUNAVAILABLE(37, 37),
    /**
     * <code>NETWORK_DISCONNECT_HLTVSTOP = 38;</code>
     */
    NETWORK_DISCONNECT_HLTVSTOP(38, 38),
    /**
     * <code>NETWORK_DISCONNECT_KICKED = 39;</code>
     */
    NETWORK_DISCONNECT_KICKED(39, 39),
    /**
     * <code>NETWORK_DISCONNECT_BANADDED = 40;</code>
     */
    NETWORK_DISCONNECT_BANADDED(40, 40),
    /**
     * <code>NETWORK_DISCONNECT_KICKBANADDED = 41;</code>
     */
    NETWORK_DISCONNECT_KICKBANADDED(41, 41),
    /**
     * <code>NETWORK_DISCONNECT_HLTVDIRECT = 42;</code>
     */
    NETWORK_DISCONNECT_HLTVDIRECT(42, 42),
    /**
     * <code>NETWORK_DISCONNECT_PURESERVER_CLIENTEXTRA = 43;</code>
     */
    NETWORK_DISCONNECT_PURESERVER_CLIENTEXTRA(43, 43),
    /**
     * <code>NETWORK_DISCONNECT_PURESERVER_MISMATCH = 44;</code>
     */
    NETWORK_DISCONNECT_PURESERVER_MISMATCH(44, 44),
    /**
     * <code>NETWORK_DISCONNECT_USERCMD = 45;</code>
     */
    NETWORK_DISCONNECT_USERCMD(45, 45),
    /**
     * <code>NETWORK_DISCONNECT_REJECTED_BY_GAME = 46;</code>
     */
    NETWORK_DISCONNECT_REJECTED_BY_GAME(46, 46),
    ;

    /**
     * <code>NETWORK_DISCONNECT_INVALID = 0;</code>
     */
    public static final int NETWORK_DISCONNECT_INVALID_VALUE = 0;
    /**
     * <code>NETWORK_DISCONNECT_SHUTDOWN = 1;</code>
     */
    public static final int NETWORK_DISCONNECT_SHUTDOWN_VALUE = 1;
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECT_BY_USER = 2;</code>
     */
    public static final int NETWORK_DISCONNECT_DISCONNECT_BY_USER_VALUE = 2;
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECT_BY_SERVER = 3;</code>
     */
    public static final int NETWORK_DISCONNECT_DISCONNECT_BY_SERVER_VALUE = 3;
    /**
     * <code>NETWORK_DISCONNECT_LOST = 4;</code>
     */
    public static final int NETWORK_DISCONNECT_LOST_VALUE = 4;
    /**
     * <code>NETWORK_DISCONNECT_OVERFLOW = 5;</code>
     */
    public static final int NETWORK_DISCONNECT_OVERFLOW_VALUE = 5;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_BANNED = 6;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_BANNED_VALUE = 6;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_INUSE = 7;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_INUSE_VALUE = 7;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_TICKET = 8;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_TICKET_VALUE = 8;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_LOGON = 9;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_LOGON_VALUE = 9;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHCANCELLED = 10;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_AUTHCANCELLED_VALUE = 10;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHALREADYUSED = 11;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_AUTHALREADYUSED_VALUE = 11;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_AUTHINVALID = 12;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_AUTHINVALID_VALUE = 12;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_VACBANSTATE = 13;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_VACBANSTATE_VALUE = 13;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_LOGGED_IN_ELSEWHERE = 14;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_LOGGED_IN_ELSEWHERE_VALUE = 14;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_VAC_CHECK_TIMEDOUT = 15;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_VAC_CHECK_TIMEDOUT_VALUE = 15;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_DROPPED = 16;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_DROPPED_VALUE = 16;
    /**
     * <code>NETWORK_DISCONNECT_STEAM_OWNERSHIP = 17;</code>
     */
    public static final int NETWORK_DISCONNECT_STEAM_OWNERSHIP_VALUE = 17;
    /**
     * <code>NETWORK_DISCONNECT_SERVERINFO_OVERFLOW = 18;</code>
     */
    public static final int NETWORK_DISCONNECT_SERVERINFO_OVERFLOW_VALUE = 18;
    /**
     * <code>NETWORK_DISCONNECT_TICKMSG_OVERFLOW = 19;</code>
     */
    public static final int NETWORK_DISCONNECT_TICKMSG_OVERFLOW_VALUE = 19;
    /**
     * <code>NETWORK_DISCONNECT_STRINGTABLEMSG_OVERFLOW = 20;</code>
     */
    public static final int NETWORK_DISCONNECT_STRINGTABLEMSG_OVERFLOW_VALUE = 20;
    /**
     * <code>NETWORK_DISCONNECT_DELTAENTMSG_OVERFLOW = 21;</code>
     */
    public static final int NETWORK_DISCONNECT_DELTAENTMSG_OVERFLOW_VALUE = 21;
    /**
     * <code>NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW = 22;</code>
     */
    public static final int NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW_VALUE = 22;
    /**
     * <code>NETWORK_DISCONNECT_SOUNDSMSG_OVERFLOW = 23;</code>
     */
    public static final int NETWORK_DISCONNECT_SOUNDSMSG_OVERFLOW_VALUE = 23;
    /**
     * <code>NETWORK_DISCONNECT_SNAPSHOTOVERFLOW = 24;</code>
     */
    public static final int NETWORK_DISCONNECT_SNAPSHOTOVERFLOW_VALUE = 24;
    /**
     * <code>NETWORK_DISCONNECT_SNAPSHOTERROR = 25;</code>
     */
    public static final int NETWORK_DISCONNECT_SNAPSHOTERROR_VALUE = 25;
    /**
     * <code>NETWORK_DISCONNECT_RELIABLEOVERFLOW = 26;</code>
     */
    public static final int NETWORK_DISCONNECT_RELIABLEOVERFLOW_VALUE = 26;
    /**
     * <code>NETWORK_DISCONNECT_BADDELTATICK = 27;</code>
     */
    public static final int NETWORK_DISCONNECT_BADDELTATICK_VALUE = 27;
    /**
     * <code>NETWORK_DISCONNECT_NOMORESPLITS = 28;</code>
     */
    public static final int NETWORK_DISCONNECT_NOMORESPLITS_VALUE = 28;
    /**
     * <code>NETWORK_DISCONNECT_TIMEDOUT = 29;</code>
     */
    public static final int NETWORK_DISCONNECT_TIMEDOUT_VALUE = 29;
    /**
     * <code>NETWORK_DISCONNECT_DISCONNECTED = 30;</code>
     */
    public static final int NETWORK_DISCONNECT_DISCONNECTED_VALUE = 30;
    /**
     * <code>NETWORK_DISCONNECT_LEAVINGSPLIT = 31;</code>
     */
    public static final int NETWORK_DISCONNECT_LEAVINGSPLIT_VALUE = 31;
    /**
     * <code>NETWORK_DISCONNECT_DIFFERENTCLASSTABLES = 32;</code>
     */
    public static final int NETWORK_DISCONNECT_DIFFERENTCLASSTABLES_VALUE = 32;
    /**
     * <code>NETWORK_DISCONNECT_BADRELAYPASSWORD = 33;</code>
     */
    public static final int NETWORK_DISCONNECT_BADRELAYPASSWORD_VALUE = 33;
    /**
     * <code>NETWORK_DISCONNECT_BADSPECTATORPASSWORD = 34;</code>
     */
    public static final int NETWORK_DISCONNECT_BADSPECTATORPASSWORD_VALUE = 34;
    /**
     * <code>NETWORK_DISCONNECT_HLTVRESTRICTED = 35;</code>
     */
    public static final int NETWORK_DISCONNECT_HLTVRESTRICTED_VALUE = 35;
    /**
     * <code>NETWORK_DISCONNECT_NOSPECTATORS = 36;</code>
     */
    public static final int NETWORK_DISCONNECT_NOSPECTATORS_VALUE = 36;
    /**
     * <code>NETWORK_DISCONNECT_HLTVUNAVAILABLE = 37;</code>
     */
    public static final int NETWORK_DISCONNECT_HLTVUNAVAILABLE_VALUE = 37;
    /**
     * <code>NETWORK_DISCONNECT_HLTVSTOP = 38;</code>
     */
    public static final int NETWORK_DISCONNECT_HLTVSTOP_VALUE = 38;
    /**
     * <code>NETWORK_DISCONNECT_KICKED = 39;</code>
     */
    public static final int NETWORK_DISCONNECT_KICKED_VALUE = 39;
    /**
     * <code>NETWORK_DISCONNECT_BANADDED = 40;</code>
     */
    public static final int NETWORK_DISCONNECT_BANADDED_VALUE = 40;
    /**
     * <code>NETWORK_DISCONNECT_KICKBANADDED = 41;</code>
     */
    public static final int NETWORK_DISCONNECT_KICKBANADDED_VALUE = 41;
    /**
     * <code>NETWORK_DISCONNECT_HLTVDIRECT = 42;</code>
     */
    public static final int NETWORK_DISCONNECT_HLTVDIRECT_VALUE = 42;
    /**
     * <code>NETWORK_DISCONNECT_PURESERVER_CLIENTEXTRA = 43;</code>
     */
    public static final int NETWORK_DISCONNECT_PURESERVER_CLIENTEXTRA_VALUE = 43;
    /**
     * <code>NETWORK_DISCONNECT_PURESERVER_MISMATCH = 44;</code>
     */
    public static final int NETWORK_DISCONNECT_PURESERVER_MISMATCH_VALUE = 44;
    /**
     * <code>NETWORK_DISCONNECT_USERCMD = 45;</code>
     */
    public static final int NETWORK_DISCONNECT_USERCMD_VALUE = 45;
    /**
     * <code>NETWORK_DISCONNECT_REJECTED_BY_GAME = 46;</code>
     */
    public static final int NETWORK_DISCONNECT_REJECTED_BY_GAME_VALUE = 46;


    public final int getNumber() { return value; }

    public static ENetworkDisconnectionReason valueOf(int value) {
      switch (value) {
        case 0: return NETWORK_DISCONNECT_INVALID;
        case 1: return NETWORK_DISCONNECT_SHUTDOWN;
        case 2: return NETWORK_DISCONNECT_DISCONNECT_BY_USER;
        case 3: return NETWORK_DISCONNECT_DISCONNECT_BY_SERVER;
        case 4: return NETWORK_DISCONNECT_LOST;
        case 5: return NETWORK_DISCONNECT_OVERFLOW;
        case 6: return NETWORK_DISCONNECT_STEAM_BANNED;
        case 7: return NETWORK_DISCONNECT_STEAM_INUSE;
        case 8: return NETWORK_DISCONNECT_STEAM_TICKET;
        case 9: return NETWORK_DISCONNECT_STEAM_LOGON;
        case 10: return NETWORK_DISCONNECT_STEAM_AUTHCANCELLED;
        case 11: return NETWORK_DISCONNECT_STEAM_AUTHALREADYUSED;
        case 12: return NETWORK_DISCONNECT_STEAM_AUTHINVALID;
        case 13: return NETWORK_DISCONNECT_STEAM_VACBANSTATE;
        case 14: return NETWORK_DISCONNECT_STEAM_LOGGED_IN_ELSEWHERE;
        case 15: return NETWORK_DISCONNECT_STEAM_VAC_CHECK_TIMEDOUT;
        case 16: return NETWORK_DISCONNECT_STEAM_DROPPED;
        case 17: return NETWORK_DISCONNECT_STEAM_OWNERSHIP;
        case 18: return NETWORK_DISCONNECT_SERVERINFO_OVERFLOW;
        case 19: return NETWORK_DISCONNECT_TICKMSG_OVERFLOW;
        case 20: return NETWORK_DISCONNECT_STRINGTABLEMSG_OVERFLOW;
        case 21: return NETWORK_DISCONNECT_DELTAENTMSG_OVERFLOW;
        case 22: return NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW;
        case 23: return NETWORK_DISCONNECT_SOUNDSMSG_OVERFLOW;
        case 24: return NETWORK_DISCONNECT_SNAPSHOTOVERFLOW;
        case 25: return NETWORK_DISCONNECT_SNAPSHOTERROR;
        case 26: return NETWORK_DISCONNECT_RELIABLEOVERFLOW;
        case 27: return NETWORK_DISCONNECT_BADDELTATICK;
        case 28: return NETWORK_DISCONNECT_NOMORESPLITS;
        case 29: return NETWORK_DISCONNECT_TIMEDOUT;
        case 30: return NETWORK_DISCONNECT_DISCONNECTED;
        case 31: return NETWORK_DISCONNECT_LEAVINGSPLIT;
        case 32: return NETWORK_DISCONNECT_DIFFERENTCLASSTABLES;
        case 33: return NETWORK_DISCONNECT_BADRELAYPASSWORD;
        case 34: return NETWORK_DISCONNECT_BADSPECTATORPASSWORD;
        case 35: return NETWORK_DISCONNECT_HLTVRESTRICTED;
        case 36: return NETWORK_DISCONNECT_NOSPECTATORS;
        case 37: return NETWORK_DISCONNECT_HLTVUNAVAILABLE;
        case 38: return NETWORK_DISCONNECT_HLTVSTOP;
        case 39: return NETWORK_DISCONNECT_KICKED;
        case 40: return NETWORK_DISCONNECT_BANADDED;
        case 41: return NETWORK_DISCONNECT_KICKBANADDED;
        case 42: return NETWORK_DISCONNECT_HLTVDIRECT;
        case 43: return NETWORK_DISCONNECT_PURESERVER_CLIENTEXTRA;
        case 44: return NETWORK_DISCONNECT_PURESERVER_MISMATCH;
        case 45: return NETWORK_DISCONNECT_USERCMD;
        case 46: return NETWORK_DISCONNECT_REJECTED_BY_GAME;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ENetworkDisconnectionReason>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<ENetworkDisconnectionReason>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ENetworkDisconnectionReason>() {
            public ENetworkDisconnectionReason findValueByNumber(int number) {
              return ENetworkDisconnectionReason.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.valve.dota2.NetworkConnection.getDescriptor().getEnumTypes().get(0);
    }

    private static final ENetworkDisconnectionReason[] VALUES = values();

    public static ENetworkDisconnectionReason valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private ENetworkDisconnectionReason(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:ENetworkDisconnectionReason)
  }

  public static final int NETWORK_CONNECTION_TOKEN_FIELD_NUMBER = 50500;
  /**
   * <code>extend .google.protobuf.EnumValueOptions { ... }</code>
   */
  public static final
    com.google.protobuf.GeneratedMessage.GeneratedExtension<
      com.google.protobuf.DescriptorProtos.EnumValueOptions,
      java.lang.String> networkConnectionToken = com.google.protobuf.GeneratedMessage
          .newFileScopedGeneratedExtension(
        java.lang.String.class,
        null);

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030network_connection.proto\032 google/proto" +
      "buf/descriptor.proto*\201\034\n\033ENetworkDisconn" +
      "ectionReason\022\036\n\032NETWORK_DISCONNECT_INVAL" +
      "ID\020\000\022\037\n\033NETWORK_DISCONNECT_SHUTDOWN\020\001\022F\n" +
      "%NETWORK_DISCONNECT_DISCONNECT_BY_USER\020\002" +
      "\032\033\242\324\030\027#GameUI_Disconnect_User\022J\n\'NETWORK" +
      "_DISCONNECT_DISCONNECT_BY_SERVER\020\003\032\035\242\324\030\031" +
      "#GameUI_Disconnect_Server\022B\n\027NETWORK_DIS" +
      "CONNECT_LOST\020\004\032%\242\324\030!#GameUI_Disconnect_C" +
      "onnectionLost\022J\n\033NETWORK_DISCONNECT_OVER",
      "FLOW\020\005\032)\242\324\030%#GameUI_Disconnect_Connectio" +
      "nOverflow\022I\n\037NETWORK_DISCONNECT_STEAM_BA" +
      "NNED\020\006\032$\242\324\030 #GameUI_Disconnect_SteamIDBa" +
      "nned\022G\n\036NETWORK_DISCONNECT_STEAM_INUSE\020\007" +
      "\032#\242\324\030\037#GameUI_Disconnect_SteamIDInUse\022G\n" +
      "\037NETWORK_DISCONNECT_STEAM_TICKET\020\010\032\"\242\324\030\036" +
      "#GameUI_Disconnect_SteamTicket\022E\n\036NETWOR" +
      "K_DISCONNECT_STEAM_LOGON\020\t\032!\242\324\030\035#GameUI_" +
      "Disconnect_SteamLogon\022M\n&NETWORK_DISCONN" +
      "ECT_STEAM_AUTHCANCELLED\020\n\032!\242\324\030\035#GameUI_D",
      "isconnect_SteamLogon\022O\n(NETWORK_DISCONNE" +
      "CT_STEAM_AUTHALREADYUSED\020\013\032!\242\324\030\035#GameUI_" +
      "Disconnect_SteamLogon\022K\n$NETWORK_DISCONN" +
      "ECT_STEAM_AUTHINVALID\020\014\032!\242\324\030\035#GameUI_Dis" +
      "connect_SteamLogon\022I\n$NETWORK_DISCONNECT" +
      "_STEAM_VACBANSTATE\020\r\032\037\242\324\030\033#GameUI_Discon" +
      "nect_SteamVAC\022S\n,NETWORK_DISCONNECT_STEA" +
      "M_LOGGED_IN_ELSEWHERE\020\016\032!\242\324\030\035#GameUI_Dis" +
      "connect_SteamInUse\022T\n+NETWORK_DISCONNECT" +
      "_STEAM_VAC_CHECK_TIMEDOUT\020\017\032#\242\324\030\037#GameUI",
      "_Disconnect_SteamTimeOut\022I\n NETWORK_DISC" +
      "ONNECT_STEAM_DROPPED\020\020\032#\242\324\030\037#GameUI_Disc" +
      "onnect_SteamDropped\022M\n\"NETWORK_DISCONNEC" +
      "T_STEAM_OWNERSHIP\020\021\032%\242\324\030!#GameUI_Disconn" +
      "ect_SteamOwnership\022U\n&NETWORK_DISCONNECT" +
      "_SERVERINFO_OVERFLOW\020\022\032)\242\324\030%#GameUI_Disc" +
      "onnect_ServerInfoOverflow\022K\n#NETWORK_DIS" +
      "CONNECT_TICKMSG_OVERFLOW\020\023\032\"\242\324\030\036#GameUI_" +
      "Disconnect_TickMessage\022Y\n*NETWORK_DISCON" +
      "NECT_STRINGTABLEMSG_OVERFLOW\020\024\032)\242\324\030%#Gam",
      "eUI_Disconnect_StringTableMessage\022S\n\'NET" +
      "WORK_DISCONNECT_DELTAENTMSG_OVERFLOW\020\025\032&" +
      "\242\324\030\"#GameUI_Disconnect_DeltaEntMessage\022Q" +
      "\n&NETWORK_DISCONNECT_TEMPENTMSG_OVERFLOW" +
      "\020\026\032%\242\324\030!#GameUI_Disconnect_TempEntMessag" +
      "e\022O\n%NETWORK_DISCONNECT_SOUNDSMSG_OVERFL" +
      "OW\020\027\032$\242\324\030 #GameUI_Disconnect_SoundsMessa" +
      "ge\022P\n#NETWORK_DISCONNECT_SNAPSHOTOVERFLO" +
      "W\020\030\032\'\242\324\030##GameUI_Disconnect_SnapshotOver" +
      "flow\022J\n NETWORK_DISCONNECT_SNAPSHOTERROR",
      "\020\031\032$\242\324\030 #GameUI_Disconnect_SnapshotError" +
      "\022P\n#NETWORK_DISCONNECT_RELIABLEOVERFLOW\020" +
      "\032\032\'\242\324\030##GameUI_Disconnect_ReliableOverfl" +
      "ow\022N\n\037NETWORK_DISCONNECT_BADDELTATICK\020\033\032" +
      ")\242\324\030%#GameUI_Disconnect_BadClientDeltaTi" +
      "ck\022H\n\037NETWORK_DISCONNECT_NOMORESPLITS\020\034\032" +
      "#\242\324\030\037#GameUI_Disconnect_NoMoreSplits\022@\n\033" +
      "NETWORK_DISCONNECT_TIMEDOUT\020\035\032\037\242\324\030\033#Game" +
      "UI_Disconnect_TimedOut\022H\n\037NETWORK_DISCON" +
      "NECT_DISCONNECTED\020\036\032#\242\324\030\037#GameUI_Disconn",
      "ect_Disconnected\022H\n\037NETWORK_DISCONNECT_L" +
      "EAVINGSPLIT\020\037\032#\242\324\030\037#GameUI_Disconnect_Le" +
      "avingSplit\022X\n\'NETWORK_DISCONNECT_DIFFERE" +
      "NTCLASSTABLES\020 \032+\242\324\030\'#GameUI_Disconnect_" +
      "DifferentClassTables\022P\n#NETWORK_DISCONNE" +
      "CT_BADRELAYPASSWORD\020!\032\'\242\324\030##GameUI_Disco" +
      "nnect_BadRelayPassword\022X\n\'NETWORK_DISCON" +
      "NECT_BADSPECTATORPASSWORD\020\"\032+\242\324\030\'#GameUI" +
      "_Disconnect_BadSpectatorPassword\022L\n!NETW" +
      "ORK_DISCONNECT_HLTVRESTRICTED\020#\032%\242\324\030!#Ga",
      "meUI_Disconnect_HLTVRestricted\022H\n\037NETWOR" +
      "K_DISCONNECT_NOSPECTATORS\020$\032#\242\324\030\037#GameUI" +
      "_Disconnect_NoSpectators\022N\n\"NETWORK_DISC" +
      "ONNECT_HLTVUNAVAILABLE\020%\032&\242\324\030\"#GameUI_Di" +
      "sconnect_HLTVUnavailable\022@\n\033NETWORK_DISC" +
      "ONNECT_HLTVSTOP\020&\032\037\242\324\030\033#GameUI_Disconnec" +
      "t_HLTVStop\022<\n\031NETWORK_DISCONNECT_KICKED\020" +
      "\'\032\035\242\324\030\031#GameUI_Disconnect_Kicked\022@\n\033NETW" +
      "ORK_DISCONNECT_BANADDED\020(\032\037\242\324\030\033#GameUI_D" +
      "isconnect_BanAdded\022H\n\037NETWORK_DISCONNECT",
      "_KICKBANADDED\020)\032#\242\324\030\037#GameUI_Disconnect_" +
      "KickBanAdded\022D\n\035NETWORK_DISCONNECT_HLTVD" +
      "IRECT\020*\032!\242\324\030\035#GameUI_Disconnect_HLTVDire" +
      "ct\022\\\n)NETWORK_DISCONNECT_PURESERVER_CLIE" +
      "NTEXTRA\020+\032-\242\324\030)#GameUI_Disconnect_PureSe" +
      "rver_ClientExtra\022V\n&NETWORK_DISCONNECT_P" +
      "URESERVER_MISMATCH\020,\032*\242\324\030&#GameUI_Discon" +
      "nect_PureServer_Mismatch\022>\n\032NETWORK_DISC" +
      "ONNECT_USERCMD\020-\032\036\242\324\030\032#GameUI_Disconnect" +
      "_UserCmd\022N\n#NETWORK_DISCONNECT_REJECTED_",
      "BY_GAME\020.\032%\242\324\030!#GameUI_Disconnect_Reject" +
      "edByGame:E\n\030network_connection_token\022!.g" +
      "oogle.protobuf.EnumValueOptions\030\304\212\003 \001(\tB" +
      "\021\n\017com.valve.dota2"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          networkConnectionToken.internalInit(descriptor.getExtensions().get(0));
          com.google.protobuf.ExtensionRegistry registry =
            com.google.protobuf.ExtensionRegistry.newInstance();
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          registry.add(com.valve.dota2.NetworkConnection.networkConnectionToken);
          return registry;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.DescriptorProtos.getDescriptor(),
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
