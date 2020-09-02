package tools.packet;

import client.MapleCharacter;
import client.MapleClient;
import client.PartTimeJob;
import constants.GameConstants;
import constants.JobConstants;
import constants.JobConstants.LoginJob;
import constants.ServerConstants;
import constants.WorldConstants.WorldOption;
import handling.SendPacketOpcode;
import handling.login.LoginServer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import server.Randomizer;
import tools.HexTool;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class LoginPacket {

    /**
     * 构造握手帧。
     * @param sendIv
     * @param recvIv
     * @return
     */
    public static byte[] getHello(byte[] sendIv, byte[] recvIv) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(0x0E);  // 14 - GMS
        mplew.writeShort(ServerConstants.MAPLE_VERSION);  // 79
        mplew.writeMapleAsciiString(ServerConstants.MAPLE_PATCH);  // 1
        mplew.write(recvIv);
        mplew.write(sendIv);
        mplew.write(ServerConstants.MAPLE_LOCAL); // // 7 = MSEA, 8 = GlobalMS, 5 = Test Server

        return mplew.getPacket();
    }

    public static final byte[] getPing() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(2);

        mplew.writeShort(SendPacketOpcode.PING.getValue());

        return mplew.getPacket();
    }

    public static final byte[] licenseResult() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LICENSE_RESULT.getValue());
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] RegisterInfo(boolean isAllow) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REGISTER_INFO.getValue());
        if (isAllow == true) {
            mplew.write(1);
        } else {
            mplew.write(0);
        }
        return mplew.getPacket();
    }

    public static byte[] CheckAccount(String accountName, boolean isUsed) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHECK_ACCOUNT_INFO.getValue());
        mplew.writeMapleAsciiString(accountName);
        mplew.write(isUsed ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] RegisterAccount(boolean result) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REGISTER_ACCOUNT.getValue());
        mplew.write(result ? 0 : 1);
        if (result) {
            mplew.write(0);
        }

        return mplew.getPacket();
    }

    public static final byte[] genderNeeded(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.CHOOSE_GENDER.getValue());
        mplew.writeMapleAsciiString(c.getAccountName());

        return mplew.getPacket();
    }

    public static final byte[] genderChanged(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.GENDER_SET.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(c.getAccountName());
        mplew.writeMapleAsciiString(String.valueOf(c.getAccID()));

        return mplew.getPacket();
    }

    public static byte[] getAuthSuccessRequest(MapleClient client) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        /*
         mplew.writeInt(client.getAccID());
         mplew.write(client.getGender());
         mplew.write(client.isGm() ? 1 : 0); // Admin byte - Find, Trade, etc.
         mplew.write(client.isGm() ? 1 : 0); // Admin byte - Commands
         mplew.writeMapleAsciiString(client.getAccountName());
         mplew.writeInt(0); //0 for new accounts
         mplew.write(0);
         mplew.write(0);
         mplew.write(0);
         mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
         mplew.write(0);
         mplew.writeLong(0);
         mplew.writeShort(0); //writeMapleAsciiString  CInPacket::DecodeStr
         mplew.write(0);
         mplew.writeMapleAsciiString(String.valueOf(client.getAccID()));
         mplew.writeMapleAsciiString(client.getAccountName());
         mplew.write(1);
         */
        mplew.write(0);
        mplew.writeInt(client.getAccID());
        mplew.write(client.getGender());
        mplew.write(client.isGm() ? 1 : 0); // Admin byte - Find, Trade, etc.
        mplew.write(0); // Admin byte - Commands
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.write(HexTool.getByteArrayFromHexString("00 00 00 03 01 00 00 00 E2 ED A3 7A FA C9 01"));
        mplew.write(0);
        mplew.writeLong(0);
        mplew.writeShort(0); //writeMapleAsciiString  CInPacket::DecodeStr
        mplew.write(0);
        mplew.writeMapleAsciiString(String.valueOf(client.getAccID()));
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.write(1);

        return mplew.getPacket();
    }

    public static final byte[] getSecondAuthSuccess(MapleClient client) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOGIN_SECOND.getValue());
        mplew.write(0);
        mplew.writeInt(client.getAccID());
        mplew.writeZeroBytes(9);
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.writeLong(3);
        mplew.writeShort(0);
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.write(HexTool.getByteArrayFromHexString("F0 3A DF AD 8E 0B CA 01"));
        mplew.writeInt(4);//v148
        mplew.write(HexTool.getByteArrayFromHexString("BA B5 BC EF F1 CA 91 7A"));
        mplew.writeShort(0);
        mplew.write(JobConstants.enableJobs ? 1 : 0);
        mplew.write(JobConstants.jobOrder);
        for (LoginJob j : LoginJob.values()) {
            mplew.write(j.getFlag());
        }
        mplew.write(1);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static final byte[] getLoginFailed(int reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(reason);
        if (reason == 84) {
            mplew.writeLong(PacketHelper.getTime(-2));
        } else if (reason == 7) { //prolly this
            mplew.writeZeroBytes(5);
        }
        mplew.write(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    /*
     * location: UI.wz/Login.img/Notice/text
     * reasons:
     * useful:
     * 32 - server under maintenance check site for updates
     * 35 - your computer is running thirdy part programs close them and play again
     * 36 - due to high population char creation has been disabled
     * 43 - revision needed your ip is temporary blocked
     * 75-78 are cool for auto register
     
     */
    public static byte[] getPermBan(byte reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(2);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeShort(reason);
        mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));

        return mplew.getPacket();
    }

    public static byte[] getTempBan(long timestampTill, byte reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(17);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(2);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(reason);
        mplew.writeLong(timestampTill);

        return mplew.getPacket();
    }

    public static byte[] getWorldSelected(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        byte lastWorld = c.getAccountWorld(c.getAccountName());
        mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
        mplew.writeInt(lastWorld == 0 ? -3 : lastWorld);

        return mplew.getPacket();
    }

    public static final byte[] deleteCharResponse(int cid, int state) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
        mplew.writeInt(cid);
        mplew.write(state);

        return mplew.getPacket();
    }

    public static byte[] secondPwError(byte mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendAuthResponse(int response) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AUTH_RESPONSE.getValue());
        mplew.writeInt(response);

        return mplew.getPacket();
    }

    public static byte[] enableRecommended(int world) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
        mplew.writeInt(world);
        return mplew.getPacket();
    }

    public static byte[] sendRecommended(int world, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SEND_RECOMMENDED.getValue());
        mplew.write(message != null ? 1 : 0);
        if (message != null) {
            mplew.writeInt(world);
            mplew.writeMapleAsciiString(message);
        }
        return mplew.getPacket();
    }

    public static byte[] ResetScreen() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.RESET_SCREEN.getValue());

        //mplew.write(HexTool.getByteArrayFromHexString("02 08 00 32 30 31 32 30 38 30 38 00 08 00 32 30 31 32 30 38 31 35 00"));

        return mplew.getPacket();
    }

    public static byte[] getServerList(int serverId, Map<Integer, Integer> channelLoad) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(serverId);
        String worldName = LoginServer.getTrueServerName();
        mplew.writeMapleAsciiString(worldName);
        mplew.write(WorldOption.getById(serverId).getFlag());
        mplew.writeMapleAsciiString(LoginServer.getEventMessage());
        mplew.writeShort(100);
        mplew.writeShort(100);
        int lastChannel = 1;
        Set<Integer> channels = channelLoad.keySet();
        for (int i = 30; i > 0; i--) {
            if (channels.contains(i)) {
                lastChannel = i;
                break;
            }
        }
        mplew.write(lastChannel);
        mplew.writeInt(300);

        for (int i = 1; i <= lastChannel; i++) {
            int load;

            if (channels.contains(i)) {
                load = channelLoad.get(i);
            } else {
                load = 1200;
            }
            mplew.writeMapleAsciiString(worldName + "-" + i);
            mplew.writeInt(load);
            mplew.write(serverId);
            mplew.writeShort(i - 1);
        }
        mplew.writeShort(0); //size: (short x, short y, string msg)

        return mplew.getPacket();
    }

    public static byte[] getEndOfServerList() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(0xFF);

        return mplew.getPacket();
    }

    /*public static final byte[] getLoginWelcome() {
     List flags = new LinkedList();

     return CField.spawnFlags(flags);
     }*/
    public static byte[] getServerStatus(int status) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
        mplew.write(status);

        return mplew.getPacket();
    }

    public static byte[] changeBackground(List<Triple<String, Integer, Boolean>> backgrounds) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANGE_BACKGROUND.getValue());
        mplew.write(backgrounds.size()); //number of bgs
        for (Triple<String, Integer, Boolean> background : backgrounds) {
            mplew.writeMapleAsciiString(background.getLeft());
            mplew.write(background.getRight() ? Randomizer.nextInt(2) : background.getMid());
        }
        /* 
         Map.wz/Obj/login.img/WorldSelect/background/background number
         Backgrounds ids sometime have more than one background anumation
         Background are like layers, backgrounds in the packets are
         removed, so the background which was hiden by the last one
         is shown.
         */
        return mplew.getPacket();
    }

    public static byte[] getChannelSelected() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANNEL_SELECTED.getValue());
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] getCharList(String secondpw, List<MapleCharacter> chars, int charslots) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(chars.size()); // 1
        for (MapleCharacter chr : chars) {
            addCharEntry(mplew, chr, (!chr.isGM()) && (chr.getLevel() >= 30), false);
            mplew.write(0);
        }
        mplew.write(3);
        mplew.write(0);
        mplew.writeInt(charslots);

        return mplew.getPacket();
    }

    public static byte[] addNewCharEntry(MapleCharacter chr, boolean worked) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());
        mplew.write(worked ? 0 : 1);
        addCharEntry(mplew, chr, false, false);

        return mplew.getPacket();
    }

    public static byte[] charNameResponse(String charname, boolean nameUsed) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
        mplew.writeMapleAsciiString(charname);
        mplew.write(nameUsed ? 1 : 0);

        return mplew.getPacket();
    }

    private static void addCharEntry(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean ranking, boolean viewAll) {
        PacketHelper.addCharStats(mplew, chr);
        PacketHelper.addCharLook(mplew, chr, true, false);
    }

    public static byte[] enableSpecialCreation(int accid, boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_CREATION.getValue());
        mplew.writeInt(accid);
        mplew.write(enable ? 0 : 1);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] partTimeJob(int cid, short type, long time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PART_TIME.getValue());
        mplew.writeInt(cid);
        mplew.write(0);
        mplew.write(type);
        //1) 0A D2 CD 01 70 59 9F EA
        //2) 0B D2 CD 01 B0 6B 9C 18
        mplew.writeReversedLong(PacketHelper.getTime(time));
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] updatePartTimeJob(PartTimeJob partTime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(21);
        mplew.writeShort(SendPacketOpcode.PART_TIME.getValue());
        mplew.writeInt(partTime.getCharacterId());
        mplew.write(0);
        PacketHelper.addPartTimeJob(mplew, partTime);
        return mplew.getPacket();
    }

    public static byte[] sendLink() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SEND_LINK.getValue());
        mplew.write(1);
        mplew.write(ServerConstants.Gateway_IP);
        mplew.writeShort(0x2057);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static final byte[] getServerIP(final MapleClient c, final int port, final int clientId) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVER_IP.getValue());
        mplew.writeShort(0);
        if (c.getTempIP().length() > 0) {
            for (String s : c.getTempIP().split(",")) {
                mplew.write(Integer.parseInt(s));
            }
        } else {
            mplew.write(ServerConstants.Gateway_IP);
        }
        mplew.writeShort(port);
        mplew.writeInt(clientId);
        mplew.write(GameConstants.GMS ? 0 : 1); //?  not sure
        mplew.writeZeroBytes(5);
        mplew.writeInt(-1);

        return mplew.getPacket();
    }
}
