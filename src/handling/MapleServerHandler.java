package handling;

import client.MapleClient;
import client.inventory.Item;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import constants.ServerConfig;
import constants.ServerConstants;
import handling.cashshop.handler.*;
import handling.channel.handler.*;
import handling.farm.handler.FarmHandler;
import handling.farm.handler.FarmOperation;
import handling.login.LoginServer;
import handling.login.handler.*;
import handling.mina.MaplePacketDecoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import scripting.NPCScriptManager;
import server.CashItemFactory;
import server.CashItemInfo;
import server.Randomizer;
import server.commands.CommandProcessor;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.Pair;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.LoginPacket;

/**
 * Mina的服务端适配器。
 */
public class MapleServerHandler extends IoHandlerAdapter implements MapleServerHandlerMBean {

    public static boolean Log_Packets = false;
    private static int numDC = 0;
    private static long lastDC = System.currentTimeMillis();
    private final List<String> BlockedIP = new ArrayList<>();
    private final Map<String, Pair<Long, Byte>> tracker = new ConcurrentHashMap<>();
    //Screw locking. Doesn't matter.
    //private static final ReentrantReadWriteLock IPLoggingLock = new ReentrantReadWriteLock();
    private static final String nl = System.getProperty("line.separator");
    private static final HashMap<String, FileWriter> logIPMap = new HashMap<>();
    //Note to Zero: Use an enumset. Don't iterate through an array.
    private static final EnumSet<RecvPacketOpcode> blocked = EnumSet.noneOf(RecvPacketOpcode.class), sBlocked = EnumSet.noneOf(RecvPacketOpcode.class);

    public static void reloadLoggedIPs() {
        //IPLoggingLock.writeLock().lock();
        //try {
        for (FileWriter fw : logIPMap.values()) {
            if (fw != null) {
                try {
                    fw.write("=== Closing Log ===");
                    fw.write(nl);
                    fw.flush(); //Just in case.
                    fw.close();
                } catch (IOException ex) {
                    System.out.println("Error closing Packet Log.");
                    System.out.println(ex);
                }
            }
        }
        logIPMap.clear();
        try {
            File logips = new File("LogIPs.txt");
            if (logips.exists()) {
                Scanner sc = new Scanner(logips);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.length() > 0) {
                        addIP(line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not reload packet logged IPs.");
            System.out.println(e);
        }
        //} finally {
        //    IPLoggingLock.writeLock().unlock();
        //}
    }
    //Return the Filewriter if the IP is logged. Null otherwise.

    private static FileWriter isLoggedIP(IoSession sess) {
        String a = sess.getRemoteAddress().toString();
        String realIP = a.substring(a.indexOf('/') + 1, a.indexOf(':'));
        return logIPMap.get(realIP);
    }

    public static void addIP(String theIP) {
        try {
            FileWriter fw = new FileWriter(new File("PacketLog_" + theIP + ".txt"), true);
            fw.write("=== Creating Log ===");
            fw.write(nl);
            fw.flush();
            logIPMap.put(theIP, fw);
        } catch (IOException e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
        }

    }
// <editor-fold defaultstate="collapsed" desc="Packet Log Implementation">
    private static final int Log_Size = 10000, Packet_Log_Size = 25;
    private static final ArrayList<LoggedPacket> Packet_Log = new ArrayList<>(Log_Size);
    private static final ReentrantReadWriteLock Packet_Log_Lock = new ReentrantReadWriteLock();
    private static final String Packet_Log_Output = "Packet/PacketLog";
    private static int Packet_Log_Index = 0;

    public static void log(String packet, String op, MapleClient c, IoSession io) {
        try {
            Packet_Log_Lock.writeLock().lock();
            LoggedPacket logged = null;
            if (Packet_Log.size() == Log_Size) {
                logged = Packet_Log.remove(0);
            }
            //This way, we don't create new LoggedPacket objects, we reuse them =]
            if (logged == null) {
                logged = new LoggedPacket(packet, op, io.getRemoteAddress().toString(),
                        c == null ? -1 : c.getAccID(), FileoutputUtil.CurrentReadable_Time(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getMap() == null ? "[Null]" : String.valueOf(c.getPlayer().getMapId()),
                        c == null || NPCScriptManager.getInstance().getCM(c) == null ? "[Null]" : String.valueOf(NPCScriptManager.getInstance().getCM(c).getNpc()));
            } else {
                logged.setInfo(packet, op, io.getRemoteAddress().toString(),
                        c == null ? -1 : c.getAccID(), FileoutputUtil.CurrentReadable_Time(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getMap() == null ? "[Null]" : String.valueOf(c.getPlayer().getMapId()),
                        c == null || NPCScriptManager.getInstance().getCM(c) == null ? "[Null]" : String.valueOf(NPCScriptManager.getInstance().getCM(c).getNpc()));
            }
            Packet_Log.add(logged);
        } finally {
            Packet_Log_Lock.writeLock().unlock();
        }
    }

    private static class LoggedPacket {

        private static final String nl = System.getProperty("line.separator");
        private String ip, accName, accId, chrName, packet, mapId, npcId, op, time;
        private long timestamp;

        public LoggedPacket(String p, String op, String ip, int id, String time, String accName, String chrName, String mapId, String npcId) {
            setInfo(p, op, ip, id, time, accName, chrName, mapId, npcId);
        }

        public final void setInfo(String p, String op, String ip, int id, String time, String accName, String chrName, String mapId, String npcId) {
            this.ip = ip;
            this.op = op;
            this.time = time;
            this.packet = p;
            this.accName = accName;
            this.chrName = chrName;
            this.mapId = mapId;
            this.npcId = npcId;
            timestamp = System.currentTimeMillis();
            this.accId = String.valueOf(id);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[IP: ").append(ip).append("] [").append(accId).append('|').append(accName).append('|').append(chrName).append("] [").append(npcId).append('|').append(mapId).append("] [Time: ").append(timestamp).append("] [").append(time).append(']');
            sb.append(nl);
            sb.append("[Op: ").append(op).append("] [").append(packet).append(']');
            return sb.toString();
        }
    }

    public static void registerMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            MapleServerHandler mbean = new MapleServerHandler();
            //The log is a static object, so we can just use this hacky method.
            mBeanServer.registerMBean(mbean, new ObjectName("handling:type=MapleServerHandler"));
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            System.out.println("Error registering PacketLog MBean");
        }
    }

    @Override
    public void writeLog() {
        writeLog(false);
    }

    public void writeLog(boolean crash) {
        Packet_Log_Lock.readLock().lock();
        try {
            try (FileWriter fw = new FileWriter(new File(Packet_Log_Output + Packet_Log_Index + (crash ? "_DC.txt" : ".txt")), true)) {
                String newline = System.getProperty("line.separator");
                for (LoggedPacket loggedPacket : Packet_Log) {
                    fw.write(loggedPacket.toString());
                    fw.write(newline);
                }
                final String logString = "Log has been written at " + lastDC + " [" + FileoutputUtil.CurrentReadable_Time() + "] - " + numDC + " have disconnected, within " + (System.currentTimeMillis() - lastDC) + " milliseconds. (" + System.currentTimeMillis() + ")";
                System.out.println(logString);
                fw.write(logString);
                fw.write(newline);
                fw.flush();
            }
            Packet_Log.clear();
            Packet_Log_Index++;
            if (Packet_Log_Index > Packet_Log_Size) {
                Packet_Log_Index = 0;
                Log_Packets = false;
            }
        } catch (IOException ex) {
            System.out.println("Error writing log to file.");
        } finally {
            Packet_Log_Lock.readLock().unlock();
        }

    }

    public static void initiate() {
        reloadLoggedIPs();
        RecvPacketOpcode[] block = new RecvPacketOpcode[]{RecvPacketOpcode.NPC_ACTION, RecvPacketOpcode.MOVE_PLAYER, RecvPacketOpcode.PONG, RecvPacketOpcode.MOVE_PET, RecvPacketOpcode.MOVE_SUMMON, RecvPacketOpcode.MOVE_DRAGON, RecvPacketOpcode.MOVE_LIFE, RecvPacketOpcode.MOVE_ANDROID, RecvPacketOpcode.HEAL_OVER_TIME, RecvPacketOpcode.STRANGE_DATA, RecvPacketOpcode.AUTO_AGGRO, RecvPacketOpcode.CANCEL_DEBUFF, RecvPacketOpcode.MOVE_FAMILIAR};
        RecvPacketOpcode[] serverBlock = new RecvPacketOpcode[]{RecvPacketOpcode.CHANGE_KEYMAP, RecvPacketOpcode.ITEM_PICKUP, RecvPacketOpcode.PET_LOOT, RecvPacketOpcode.TAKE_DAMAGE, RecvPacketOpcode.FACE_EXPRESSION, RecvPacketOpcode.USE_ITEM, RecvPacketOpcode.CLOSE_RANGE_ATTACK, RecvPacketOpcode.MAGIC_ATTACK, RecvPacketOpcode.RANGED_ATTACK, RecvPacketOpcode.ARAN_COMBO, RecvPacketOpcode.SPECIAL_MOVE, RecvPacketOpcode.GENERAL_CHAT, RecvPacketOpcode.MONSTER_BOMB, RecvPacketOpcode.PASSIVE_ENERGY, RecvPacketOpcode.PET_AUTO_POT, RecvPacketOpcode.USE_CASH_ITEM, RecvPacketOpcode.PARTYCHAT, RecvPacketOpcode.CANCEL_BUFF, RecvPacketOpcode.SKILL_EFFECT, RecvPacketOpcode.CHAR_INFO_REQUEST, RecvPacketOpcode.ALLIANCE_OPERATION, RecvPacketOpcode.AUTO_ASSIGN_AP, RecvPacketOpcode.DISTRIBUTE_AP, RecvPacketOpcode.USE_MAGNIFY_GLASS, RecvPacketOpcode.SPAWN_PET, RecvPacketOpcode.SUMMON_ATTACK, RecvPacketOpcode.ITEM_MOVE, RecvPacketOpcode.PARTY_SEARCH_STOP};
        blocked.addAll(Arrays.asList(block));
        sBlocked.addAll(Arrays.asList(serverBlock));
        if (Log_Packets) {
            for (int i = 1; i <= Packet_Log_Size; i++) {
                if (!(new File(Packet_Log_Output + i + ".txt")).exists() && !(new File(Packet_Log_Output + i + "_DC.txt")).exists()) {
                    Packet_Log_Index = i;
                    break;
                }
            }
            if (Packet_Log_Index <= 0) { //25+ files, do not log
                Log_Packets = false;
            }
        }

        registerMBean();
    }

    public MapleServerHandler() {
        //ONLY FOR THE MBEAN
    }
    // </editor-fold>

    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {//异常捕捉
        if (cause.getMessage() != null) {
            System.err.println("[异常信息] " + cause.getMessage());
        }
        if ((!(cause instanceof IOException))) {
            final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
            if ((client != null) && (client.getPlayer() != null)) {
                client.getPlayer().saveToDB(false, true);
                //client.disconnect(true, true);
                FileoutputUtil.printError("logs/发现异常.txt", cause, "发现异常 by: 玩家:" + client.getPlayer().getName() + " 职业:" + client.getPlayer().getJob() + " 地图:" + client.getPlayer().getMap().getMapName() + " - " + client.getPlayer().getMapId());
            }
        }

        /*
         * MapleClient client = (MapleClient)
         * session.getAttribute(MapleClient.CLIENT_KEY);
         * log.error(MapleClient.getLogMessage(client, cause.getMessage()),
         * cause);
         */
        // cause.printStackTrace();
    }

    /**
     * 会话打开。
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionOpened(final IoSession session) throws Exception {
        System.out.println ("New session opened");
        // Start of IP checking
//        checkLoginIP (session);

        /* 原始的代码.
        // IV used to decrypt packets from client.
//        final byte ivRecv[] = new byte[] {
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255)
//        };
        // IV used to encrypt packets for client.
//        final byte ivSend[] = new byte[] {
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255),
//                (byte) Randomizer.nextInt(255)
//        };
         */

        final byte ivRecv[] = {70, 114, 122, 82};
        final byte ivSend[] = {82, 48, 120, 115};
        ivRecv[3] = (byte) (Math.random() * 255);
        ivSend[3] = (byte) (Math.random() * 255);

        final MapleClient client = new MapleClient(
                new MapleAESOFB(ServerConfig.USE_FIXED_IV ? ServerConfig.Static_LocalIV : ivSend, (short) (0xFFFF - ServerConstants.MAPLE_VERSION)), // Sent Cypher
                new MapleAESOFB(ServerConfig.USE_FIXED_IV ? ServerConfig.Static_RemoteIV : ivRecv, ServerConstants.MAPLE_VERSION), // Recv Cypher
                session);
        client.setChannel(-1);

        MaplePacketDecoder.DecoderState decoderState = new MaplePacketDecoder.DecoderState();
        session.setAttribute(MaplePacketDecoder.DECODER_STATE_KEY, decoderState);

        // 发送握手帧
        session.write(LoginPacket.getHello(ivSend, ivRecv));
        //System.out.println("GETHELLO SENT TO " + address);
        Random r = new Random();
        client.setSessionId(r.nextLong()); // Generates a random session id.  
        session.setAttribute(MapleClient.CLIENT_KEY, client);
        session.setIdleTime(IdleStatus.READER_IDLE, 60);
        session.setIdleTime(IdleStatus.WRITER_IDLE, 60);

//        if (LoginServer.isAdminOnly()) {
            //// 日志
//            StringBuilder sb = new StringBuilder();
//            sb.append("IoSession opened ").append(address);
//            System.out.println(sb.toString());
//        }
        LoginServer.removeAccounts(client);
        FileWriter fw = isLoggedIP(session);
        if (fw != null) {
            client.setMonitored(true);
        }

    }

    private void checkLoginIP (IoSession session) {
        final String address = session.getRemoteAddress().toString().split(":") [0];
        System.out.println("[登陆服务] " + address + " 已连接");

        if (BlockedIP.contains(address)) {
            session.close();
            return;
        }
        final Pair<Long, Byte> track = tracker.get(address);

        byte count;
        if (track == null) {
            count = 1;
        } else {
            count = track.right;

            final long difference = System.currentTimeMillis() - track.left;
            if (difference < 2000) { // Less than 2 sec
                count++;
            } else if (difference > 20000) { // Over 20 sec
                count = 1;
            }
            if (count >= 10) {
                BlockedIP.add(address);
                tracker.remove(address); // Cleanup
                session.close();
                System.out.println("[登陆服务] IP:" + address + " 连接次数超过限制断开连接");
                return;
            }
        }
        tracker.put(address, new Pair<>(System.currentTimeMillis(), count));
        // End of IP checking.
        String IP = address.substring(address.indexOf('/') + 1, address.length());
        if (LoginServer.isShutdown()) {
            session.close();
            return;
        }

        LoginServer.removeIPAuth(IP);
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            System.out.println ("Client " + client.getSessionId () + " closed.");
            try {
                client.disconnect(true, client.getChannel() == -10);
            } finally {
                session.close();
                session.removeAttribute(MapleClient.CLIENT_KEY);
                //session.removeAttribute(MaplePacketDecoder.DECODER_STATE_KEY);
            }
        }
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(final IoSession session, final IdleStatus status) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null)
            client.sendPing();
        super.sessionIdle (session, status);
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) {
        if (message == null || session == null) {
            return;
        }
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
        if (slea.available() < 2) {
            return;
        }
        final MapleClient c = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
        if (c == null || !c.isReceiving()) {
            return;
        }
        final short header_num = slea.readShort();  // 首部的功能码字段。
        // if (ServerConfig.logPackets && !isSpamHeader(RecvPacketOpcode.valueOf(RecvPacketOpcode.nameOf(header_num)))) {
        //      final StringBuilder sb = new StringBuilder("Received data :\n");
        //     sb.append(HexTool.toString((byte[]) message)).append("\n").append(HexTool.toStringFromAscii((byte[]) message));
        //      System.out.println(sb.toString());
        //   }
        for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
            if (recv.getValue() == header_num) {
                if (recv.NeedsChecking()) {
                    if (!c.isLoggedIn()) {
                        return;
                    }
                }
                try {
                    if (c.getPlayer() != null && c.isMonitored() && !blocked.contains(recv)) {
                        try (FileWriter fw = new FileWriter(new File("MonitorLogs/" + c.getPlayer().getName() + "_log.txt"), true)) {
                            fw.write(String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
                            fw.flush();
                        }
                    }
                    //no login packets
                    if (Log_Packets && !blocked.contains(recv) && !sBlocked.contains(recv) && c.getPlayer() != null) {
                        log(slea.toString(), recv.toString(), c, session);
                    }

                    // 处理帧的函数入口。
                    handlePacket(recv, slea, c);
                    FileWriter fw = isLoggedIP(session);
                    if (fw != null && !blocked.contains(recv)) {
                        if (recv == RecvPacketOpcode.PLAYER_LOGGEDIN && c != null) {
                            fw.write(">> [AccountName: "
                                    + (c.getAccountName() == null ? "null" : c.getAccountName()) + "] | [IGN: "
                                    + (c.getPlayer() == null || c.getPlayer().getName() == null ? "null" : c.getPlayer().getName()) + "] | [Time: "
                                    + FileoutputUtil.CurrentReadable_Time() + "]");
                            fw.write(nl);
                        }
                        fw.write("[" + recv.toString() + "]" + slea.toString(true));
                        fw.write(nl);
                        fw.flush();
                    }
                } catch (NegativeArraySizeException | ArrayIndexOutOfBoundsException e) {
                    if (ServerConstants.Use_Localhost) {
                        FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                        FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Packet: " + header_num + "\n" + slea.toString(true));
                    }
                    //      e.printStackTrace();
                } catch (Exception e) {
                    FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                    FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Packet: " + header_num + "\n" + slea.toString(true));
                }

                return;
            }
        }
        //final StringBuilder sb = new StringBuilder("Received data : (Unhandled)\n");
        //sb.append(HexTool.toString((byte[]) message)).append("\n").append(HexTool.toStringFromAscii((byte[]) message));
        //System.out.println(sb.toString());
    }

    /**
     * 处理帧的入口选择函数。
     * @param header  功能码.
     * @param slea    剔除了功能码的剩余帧。
     * @param c       客户端实体。
     * @throws Exception
     */
    public static void handlePacket(final RecvPacketOpcode header, final LittleEndianAccessor slea, final MapleClient c) throws Exception {
        switch (header) {
            // 登录重定向？
            case LOGIN_REDIRECTOR:
                if (!ServerConstants.Redirector) {
                    System.out.println("Redirector login packet recieved, but server is not set to redirector. Please change it in ServerConstants!");
                } else {
                    String username = slea.readMapleAsciiString(); // lol
                    c.loginData(username);
                    c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
                }
                break;

            case LICENSE_REQUEST:// 身份认证？
                break;

            case CHECK_ACCOUNT:// 检查账号是否存在。
                CharLoginHandler.CheckAccount(slea, c);
                break;
            case REGISTER_ACCOUNT: // 注册账号。
                CharLoginHandler.RegisterAccount(slea, c);
                break;
            case CRASH_INFO:  // 客户端crash相关信息。
                System.out.println("Crash" + slea.toString());
                break;
            case CLIENT_HELLO:
                System.out.println(c.getSessionIPAddress() + " Connected!");
                break;
            case PONG:
                c.pongReceived();
                break;
            case STRANGE_DATA:
                break;
            case LOGIN_PASSWORD:  // 输入账号密码,登入账号
                CharLoginHandler.login(slea, c);
                break;
            case SET_GENDER:
                CharLoginHandler.setGender(slea, c);
                break;
            case WRONG_PASSWORD:
//                if (c.getSessionIPAddress().contains("8.31.99.141") || c.getSessionIPAddress().contains("8.31.99.143") || c.getSessionIPAddress().contains("127.0.0.1")) {
//                    c.loginData("admin");
//                    c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
//                }
                break;
            case AUTH_REQUEST:
                CharLoginHandler.handleAuthRequest(slea, c);
                break;
            case CLIENT_START:
//                if (c.getSessionIPAddress().contains("8.31.99.141") || c.getSessionIPAddress().contains("8.31.99.143") || c.getSessionIPAddress().contains("127.0.0.1")) {
//                    c.loginData("admin");
//                    c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
//                }
                break;
            case CLIENT_FAILED:
                break;
            case VIEW_SERVERLIST:
                if (slea.readByte() == 0) {
                    CharLoginHandler.ServerListRequest(c);
                }
                break;
            case REDISPLAY_SERVERLIST:
            case SERVERLIST_REQUEST:
                CharLoginHandler.ServerListRequest(c);
                break;
            case CHARLIST_REQUEST:  // 枚举所有的角色，在用户选定服务器和频道后。
                CharLoginHandler.CharlistRequest(slea, c);
                break;
            case SERVERSTATUS_REQUEST:
                CharLoginHandler.ServerStatusRequest(c);
                break;
            case CHECK_CHAR_NAME:
                CharLoginHandler.CheckCharName(slea.readMapleAsciiString(), c);
                break;
            case CREATE_CHAR:
            case CREATE_SPECIAL_CHAR: // 创建角色
                CharLoginHandler.CreateChar(slea, c);
                break;
            case CREATE_ULTIMATE:
                CharLoginHandler.CreateUltimate(slea, c);
                break;
            case DELETE_CHAR:
                CharLoginHandler.DeleteChar(slea, c);
                break;
            case CHAR_SELECT_NO_PIC:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, false, false);
                break;
            case VIEW_REGISTER_PIC:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, true, true);
                break;
            case PART_TIME_JOB:
                CharLoginHandler.PartJob(slea, c);
                break;
            case CHAR_SELECT:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, true, false);
                break;
            case VIEW_SELECT_PIC:
                CharLoginHandler.Character_WithSecondPassword(slea, c, true);
                break;
            case AUTH_SECOND_PASSWORD:
                CharLoginHandler.Character_WithSecondPassword(slea, c, false);
                break;
            case CHARACTER_CARD:
                CharLoginHandler.updateCCards(slea, c);
                break;
            case CLIENT_ERROR:
                String error = slea.readMapleAsciiString();
                try {
                    try (RandomAccessFile file = new RandomAccessFile("logs/错误信息.txt", "rw")) {
                        int num = (int) file.length();
                        file.seek(num);
                        file.writeBytes("\r\n------------------------ " + FileoutputUtil.CurrentReadable_Time() + " ------------------------\r\n");
                        file.write("错误信息：\r\n".getBytes());
                        file.write((error + "\r\n").getBytes());
                    }
                } catch (IOException ex) {
                    FileoutputUtil.outputFileError("logs/异常输出.txt", ex);
                }
                break;
            case ENABLE_SPECIAL_CREATION:
                c.getSession().write(LoginPacket.enableSpecialCreation(c.getAccID(), true));
                break;
            case RSA_KEY:
                break;
            // END OF LOGIN SERVER
            case CHANGE_CHANNEL:
            case CHANGE_ROOM_CHANNEL:
                InterServerHandler.ChangeChannel(slea, c, c.getPlayer(), header == RecvPacketOpcode.CHANGE_ROOM_CHANNEL);
                break;
            case PLAYER_LOGGEDIN:
                //slea.readInt();
                final int playerid = slea.readInt();
                InterServerHandler.Loggedin(playerid, c);
                break;
            case ENTER_PVP:
            case ENTER_PVP_PARTY:
                PlayersHandler.EnterPVP(slea, c);
                break;
            case PVP_RESPAWN:
                PlayersHandler.RespawnPVP(slea, c);
                break;
            case LEAVE_PVP:
                PlayersHandler.LeavePVP(slea, c);
                break;
            case ENTER_AZWAN:
                PlayersHandler.EnterAzwan(slea, c);
                break;
            case ENTER_AZWAN_EVENT:
                PlayersHandler.EnterAzwanEvent(slea, c);
                break;
            case LEAVE_AZWAN:
                PlayersHandler.LeaveAzwan(slea, c);
                c.getSession().write(CField.showEffect("hillah/fail"));
                c.getSession().write(CField.UIPacket.sendAzwanResult());
                break;
            case PVP_ATTACK:
                PlayersHandler.AttackPVP(slea, c);
                break;
            case PVP_SUMMON:
                SummonHandler.SummonPVP(slea, c);
                break;
            case ENTER_FARM:
                InterServerHandler.EnterFarm(c, c.getPlayer());
                break;
            case FARM_COMPLETE_QUEST:
                FarmHandler.completeQuest(slea, c);
                break;
            case FARM_NAME:
                FarmHandler.createFarm(slea, c);
                break;
            case PLACE_FARM_OBJECT:
                FarmHandler.placeBuilding(slea, c);
                break;
            case FARM_SHOP_BUY:
                FarmHandler.buy(slea, c);
                break;
            case HARVEST_FARM_BUILDING:
                FarmHandler.harvest(slea, c);
                break;
            case USE_FARM_ITEM:
                FarmHandler.useItem(slea, c);
                break;
            case RENAME_MONSTER:
                FarmHandler.renameMonster(slea, c);
                break;
            case NURTURE_MONSTER:
                FarmHandler.nurtureMonster(slea, c);
                break;
            case FARM_QUEST_CHECK:
                FarmHandler.checkQuestStatus(slea, c);
                break;
            case FARM_FIRST_ENTRY:
                FarmHandler.firstEntryReward(slea, c);
                break;
            case EXIT_FARM:
                FarmOperation.LeaveFarm(slea, c, c.getPlayer());
                break;
            case ENTER_CASH_SHOP:
                InterServerHandler.EnterCS(c, c.getPlayer());
                break;
            case ENTER_MTS:
                InterServerHandler.EnterMTS(c, c.getPlayer());
                // c.getPlayer().dropMessage(5, "服务器暂不开放拍卖系统。");
                break;
            case MOVE_PLAYER:
                PlayerHandler.MovePlayer(slea, c, c.getPlayer());
                break;
            case CHAR_INFO_REQUEST:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.CharInfoRequest(slea.readInt(), c, c.getPlayer());
                break;
            case CLOSE_RANGE_ATTACK:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), false);
                break;
            case RANGED_ATTACK:
                PlayerHandler.rangedAttack(slea, c, c.getPlayer());
                break;
            case MAGIC_ATTACK:
                PlayerHandler.MagicDamage(slea, c, c.getPlayer());
                break;
            case SPECIAL_MOVE:
                PlayerHandler.SpecialMove(slea, c, c.getPlayer());
                break;
            case PASSIVE_ENERGY:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), true);
                break;
            case GET_BOOK_INFO:
                PlayersHandler.MonsterBookInfoRequest(slea, c, c.getPlayer());
                break;
            case MONSTER_BOOK_DROPS:
                PlayersHandler.MonsterBookDropsRequest(slea, c, c.getPlayer());
                break;
            case CHANGE_CODEX_SET:
                PlayersHandler.ChangeSet(slea, c, c.getPlayer());
                break;
            case PROFESSION_INFO:
                ItemMakerHandler.ProfessionInfo(slea, c);
                break;
            case CRAFT_DONE:
                ItemMakerHandler.CraftComplete(slea, c, c.getPlayer());
                break;
            case CRAFT_MAKE:
                ItemMakerHandler.CraftMake(slea, c, c.getPlayer());
                break;
            case CRAFT_EFFECT:
                ItemMakerHandler.CraftEffect(slea, c, c.getPlayer());
                break;
            case START_HARVEST:
                ItemMakerHandler.StartHarvest(slea, c, c.getPlayer());
                break;
            case STOP_HARVEST:
                ItemMakerHandler.StopHarvest(slea, c, c.getPlayer());
                break;
            case MAKE_EXTRACTOR:
                ItemMakerHandler.MakeExtractor(slea, c, c.getPlayer());
                break;
            case USE_BAG:
                ItemMakerHandler.UseBag(slea, c, c.getPlayer());
                break;
            case USE_FAMILIAR:
                MobHandler.UseFamiliar(slea, c, c.getPlayer());
                break;
            case SPAWN_FAMILIAR:
                MobHandler.SpawnFamiliar(slea, c, c.getPlayer());
                break;
            case RENAME_FAMILIAR:
                MobHandler.RenameFamiliar(slea, c, c.getPlayer());
                break;
            case MOVE_FAMILIAR:
                MobHandler.MoveFamiliar(slea, c, c.getPlayer());
                break;
            case ATTACK_FAMILIAR:
                MobHandler.AttackFamiliar(slea, c, c.getPlayer());
                break;
            case TOUCH_FAMILIAR:
                MobHandler.TouchFamiliar(slea, c, c.getPlayer());
                break;
            case REVEAL_FAMILIAR:
                break;
            case USE_RECIPE:
                ItemMakerHandler.UseRecipe(slea, c, c.getPlayer());
                break;
            case MOVE_HAKU:
                PlayerHandler.MoveHaku(slea, c, c.getPlayer());
                break;
            case CHANGE_HAKU:
                PlayerHandler.MoveHaku(slea, c, c.getPlayer());
                break;
            case MOVE_ANDROID:
                PlayerHandler.MoveAndroid(slea, c, c.getPlayer());
                break;
            case FACE_EXPRESSION:
                PlayerHandler.ChangeEmotion(slea.readInt(), c.getPlayer());
                break;
            case FACE_ANDROID:
                PlayerHandler.ChangeAndroidEmotion(slea.readInt(), c.getPlayer());
                break;
            case TAKE_DAMAGE:
                PlayerHandler.TakeDamage(slea, c, c.getPlayer());
                break;
            case HEAL_OVER_TIME:
                PlayerHandler.Heal(slea, c.getPlayer());
                break;
            case CANCEL_BUFF:
                PlayerHandler.CancelBuffHandler(slea.readInt(), c.getPlayer());
                break;
            case MECH_CANCEL:
                PlayerHandler.CancelMech(slea, c.getPlayer());
                break;
            case CANCEL_ITEM_EFFECT:
                PlayerHandler.CancelItemEffect(slea.readInt(), c.getPlayer());
                break;
            case USE_TITLE:
                PlayerHandler.UseTitle(slea.readInt(), c, c.getPlayer());
                break;
            case ANGELIC_CHANGE:
                PlayerHandler.AngelicChange(slea, c, c.getPlayer());
                break;
            case DRESSUP_TIME:
                PlayerHandler.DressUpTime(slea, c);
                break;
            case USE_CHAIR:
                PlayerHandler.UseChair(slea.readInt(), c, c.getPlayer());
                break;
            case CANCEL_CHAIR:
                PlayerHandler.CancelChair(slea.readShort(), c, c.getPlayer());
                break;
            case WHEEL_OF_FORTUNE:
                break; //whatever
            case USE_ITEMEFFECT:
                PlayerHandler.UseItemEffect(slea.readInt(), c, c.getPlayer());
                break;
            case SKILL_EFFECT:
                PlayerHandler.SkillEffect(slea, c.getPlayer());
                break;
            case QUICK_SLOT:
                PlayerHandler.QuickSlot(slea, c.getPlayer());
                break;
            case MESO_DROP:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.DropMeso(slea.readInt(), c.getPlayer());
                break;
            case CHANGE_KEYMAP:
                PlayerHandler.ChangeKeymap(slea, c.getPlayer());
                break;
            case PET_BUFF:
                PlayerHandler.ChangePetBuff(slea, c.getPlayer());
                break;
            case UPDATE_ENV:
                // We handle this in MapleMap
                break;
            case CHANGE_MAP:
                if (c.getPlayer().getMap() == null) {
                    CashShopOperation.LeaveCS(slea, c, c.getPlayer());
                } else {
                    PlayerHandler.ChangeMap(slea, c, c.getPlayer());
                }
                break;
            case CHANGE_MAP_SPECIAL:
                slea.skip(1);
                PlayerHandler.ChangeMapSpecial(slea.readMapleAsciiString(), c, c.getPlayer());
                break;
            case USE_INNER_PORTAL:
                slea.skip(1);
                PlayerHandler.InnerPortal(slea, c, c.getPlayer());
                break;
            case TROCK_ADD_MAP:
                PlayerHandler.TrockAddMap(slea, c, c.getPlayer());
                break;
            case LIE_DETECTOR_SKILL:
                //c.getPlayer().dropMessage(5, "服务器暂不开放测谎系统。");
                PlayersHandler.LieDetector(slea, c, c.getPlayer(), false);
                break;
            case LIE_DETECTOR:
                //c.getPlayer().dropMessage(5, "服务器暂不开放测谎系统。");
                PlayersHandler.LieDetector(slea, c, c.getPlayer(), true);
                break;
            case LIE_DETECTOR_RESPONSE:
                //c.getPlayer().dropMessage(5, "服务器暂不开放测谎系统。");
                PlayersHandler.LieDetectorResponse(slea, c);
                break;
            case ARAN_COMBO:
                PlayerHandler.AranCombo(c, c.getPlayer(), 1);
                break;
            case SKILL_MACRO:
                PlayerHandler.ChangeSkillMacro(slea, c.getPlayer());
                break;
            case GIVE_FAME:
                PlayersHandler.GiveFame(slea, c, c.getPlayer());
                break;
            case TRANSFORM_PLAYER:
                PlayersHandler.TransformPlayer(slea, c, c.getPlayer());
                break;
            case NOTE_ACTION:
                PlayersHandler.Note(slea, c.getPlayer());
                break;
            case USE_DOOR:
                PlayersHandler.UseDoor(slea, c.getPlayer());
                break;
            case USE_MECH_DOOR:
                PlayersHandler.UseMechDoor(slea, c.getPlayer());
                break;
            case DAMAGE_REACTOR:
                PlayersHandler.HitReactor(slea, c);
                break;
            case CLICK_REACTOR:
            case TOUCH_REACTOR:
                PlayersHandler.TouchReactor(slea, c);
                break;
            case CLOSE_CHALKBOARD:
                c.getPlayer().setChalkboard(null);
                break;
            case ITEM_SORT:
                InventoryHandler.ItemSort(slea, c);
                break;
            case ITEM_GATHER:
                InventoryHandler.ItemGather(slea, c);
                break;
            case ITEM_MOVE:
                InventoryHandler.ItemMove(slea, c);
                break;
            case MOVE_BAG:
                InventoryHandler.MoveBag(slea, c);
                break;
            case SWITCH_BAG:
                InventoryHandler.SwitchBag(slea, c);
                break;
            case ITEM_MAKER:
                ItemMakerHandler.ItemMaker(slea, c);
                break;
            case ITEM_PICKUP:
                InventoryHandler.Pickup_Player(slea, c, c.getPlayer());
                break;
            case USE_CASH_ITEM:
                InventoryHandler.UseCashItem(slea, c);
                break;
            case USE_ITEM:
                InventoryHandler.UseItem(slea, c, c.getPlayer());
                break;
            case USE_COSMETIC:
                InventoryHandler.UseCosmetic(slea, c, c.getPlayer());
                break;
            case USE_MAGNIFY_GLASS:
                InventoryHandler.UseMagnify(slea, c);
                break;
            case USE_SCRIPTED_NPC_ITEM:
                InventoryHandler.UseScriptedNPCItem(slea, c, c.getPlayer());
                break;
            case USE_RETURN_SCROLL:
                InventoryHandler.UseReturnScroll(slea, c, c.getPlayer());
                break;
            case USE_NEBULITE:
                InventoryHandler.UseNebulite(slea, c);
                break;
            case USE_ALIEN_SOCKET:
                InventoryHandler.UseAlienSocket(slea, c);
                break;
            case USE_ALIEN_SOCKET_RESPONSE:
                slea.skip(4); // all 0
                c.getSession().write(CSPacket.useAlienSocket(false));
                break;
            case GOLDEN_HAMMER:
                InventoryHandler.UseGoldenHammer(slea, c);
                break;
            case VICIOUS_HAMMER:
                slea.skip(4); // 3F 00 00 00
                slea.skip(4); // all 0
                c.getSession().write(CSPacket.ViciousHammer(false, 0));
                break;
            case USE_NEBULITE_FUSION:
                InventoryHandler.UseNebuliteFusion(slea, c);
                break;
            case USE_UPGRADE_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll(slea.readShort(), slea.readShort(), slea.readShort(), c, c.getPlayer(), slea.readByte() > 0);
                break;
            case USE_FLAG_SCROLL:
            case USE_POTENTIAL_SCROLL:
            case USE_EQUIP_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll(slea.readShort(), slea.readShort(), (short) 0, c, c.getPlayer(), slea.readByte() > 0);
                break;
            case USE_ABYSS_SCROLL:
                InventoryHandler.UseAbyssScroll(slea, c);
                break;
            case USE_CARVED_SEAL:
                InventoryHandler.UseCarvedSeal(slea, c);
                break;
            case USE_CRAFTED_CUBE:
                InventoryHandler.UseCube(slea, c);
                break;
            case USE_SUMMON_BAG:
                InventoryHandler.UseSummonBag(slea, c, c.getPlayer());
                break;
            case USE_TREASURE_CHEST:
                InventoryHandler.UseTreasureChest(slea, c, c.getPlayer());
                break;
            case USE_SKILL_BOOK:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseSkillBook((byte) slea.readShort(), slea.readInt(), c, c.getPlayer());
                break;
            case USE_EXP_POTION:
                InventoryHandler.UseExpPotion(slea, c, c.getPlayer());
                break;
            case USE_CATCH_ITEM:
                InventoryHandler.UseCatchItem(slea, c, c.getPlayer());
                break;
            case USE_MOUNT_FOOD:
                InventoryHandler.UseMountFood(slea, c, c.getPlayer());
                break;
            case REWARD_ITEM:
                InventoryHandler.UseRewardItem(slea, c, c.getPlayer());
                break;
            case SOLOMON_EXP:
                InventoryHandler.UseExpItem(slea, c, c.getPlayer());
                break;
            case HYPNOTIZE_DMG:
                MobHandler.HypnotizeDmg(slea, c.getPlayer());
                break;
            case MOB_NODE:
                MobHandler.MobNode(slea, c.getPlayer());
                break;
            case DISPLAY_NODE:
                MobHandler.DisplayNode(slea, c.getPlayer());
                break;
            case MOVE_LIFE:
                MobHandler.MoveMonster(slea, c, c.getPlayer());
                break;
            case AUTO_AGGRO:
                MobHandler.AutoAggro(slea.readInt(), c.getPlayer());
                break;
            case FRIENDLY_DAMAGE:
                MobHandler.FriendlyDamage(slea, c.getPlayer());
                break;
            case REISSUE_MEDAL:
                PlayerHandler.ReIssueMedal(slea, c, c.getPlayer());
                break;
            case MONSTER_BOMB:
                MobHandler.MonsterBomb(slea.readInt(), c.getPlayer());
                break;
            case MOB_BOMB:
                MobHandler.MobBomb(slea, c.getPlayer());
                break;
            case MONSTER_BOOK_COVER:
                PlayerHandler.ChangeMonsterBookCover(slea.readInt(), c, c.getPlayer());
                break;
            case NPC_SHOP:
                NPCHandler.NPCShop(slea, c, c.getPlayer());
                break;
            case NPC_TALK:
                NPCHandler.NPCTalk(slea, c, c.getPlayer());
                break;
            case NPC_TALK_MORE:
                NPCHandler.NPCMoreTalk(slea, c);
                break;
            case NPC_ACTION:
                //NPCHandler.NPCAnimation(slea, c);
                break;
            case MARRAGE_RECV:
                NPCHandler.MarrageNpc(c);
                break;
            case QUEST_ACTION:
                NPCHandler.QuestAction(slea, c, c.getPlayer());
                break;
            case TOT_GUIDE:
                break;
            case STORAGE:
                NPCHandler.Storage(slea, c, c.getPlayer());
                break;
            case GENERAL_CHAT:  // 普通聊天
                if (c.getPlayer() != null && c.getPlayer().getMap() != null) {
                    //c.getPlayer().updateTick(slea.readInt());
                    ChatHandler.GeneralChat(slea.readMapleAsciiString(), slea.readByte(), c, c.getPlayer());
                }
                break;
            case PARTYCHAT:  // 组队聊天
                //c.getPlayer().updateTick(slea.readInt());
                ChatHandler.Others(slea, c, c.getPlayer());
                break;
            case COMMAND: // 悄悄话
                ChatHandler.Command(slea, c);
                break;
            case MESSENGER:
                ChatHandler.Messenger(slea, c);
                break;
            case AUTO_ASSIGN_AP:
                StatsHandling.AutoAssignAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_AP:
                StatsHandling.DistributeAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_SP:
                StatsHandling.DistributeSP(slea, c, c.getPlayer());
                break;
            case PLAYER_INTERACTION:
                PlayerInteractionHandler.PlayerInteraction(slea, c, c.getPlayer());
                break;
            case ADMIN_CHAT:
                ChatHandler.AdminChat(slea, c, c.getPlayer());
                break;
            case ADMIN_COMMAND:
                PlayerHandler.AdminCommand(slea, c, c.getPlayer());
                break;
            case ADMIN_LOG:
                CommandProcessor.logCommandToDB(c.getPlayer(), slea.readMapleAsciiString(), "adminlog");
                break;
            case GUILD_OPERATION:
                GuildHandler.Guild(slea, c);
                break;
            case DENY_GUILD_REQUEST:
                slea.skip(1);
                GuildHandler.DenyGuildRequest(slea.readMapleAsciiString(), c);
                break;
            case ALLIANCE_OPERATION:
                AllianceHandler.HandleAlliance(slea, c, false);
                break;
            case DENY_ALLIANCE_REQUEST:
                AllianceHandler.HandleAlliance(slea, c, true);
                break;
            case QUICK_MOVE:
                NPCHandler.OpenQuickMove(slea, c);
                break;
            case BBS_OPERATION:
                BBSHandler.BBSOperation(slea, c);
                break;
            case PARTY_OPERATION:
                PartyHandler.PartyOperation(slea, c);
                break;
            case DENY_PARTY_REQUEST:
                PartyHandler.DenyPartyRequest(slea, c);
                break;
            case ALLOW_PARTY_INVITE:
                PartyHandler.AllowPartyInvite(slea, c);
                break;
            case BUDDYLIST_MODIFY:
                BuddyListHandler.BuddyOperation(slea, c);
                break;
            case CYGNUS_SUMMON:
                UserInterfaceHandler.CygnusSummon_NPCRequest(c);
                break;
            case SHIP_OBJECT:
                UserInterfaceHandler.ShipObjectRequest(slea.readInt(), c);
                break;
            case BUY_CS_ITEM:
                CashShopOperation.BuyCashItem(slea, c, c.getPlayer());
                break;
            case COUPON_CODE:
                slea.skip(2);
                String code = slea.readMapleAsciiString();
                CashShopOperation.CouponCode(code, c);
//                CashShopOperation.doCSPackets(c);
                break;
            case CASH_CATEGORY:
                CashShopOperation.SwitchCategory(slea, c);
                break;
            case TWIN_DRAGON_EGG:
                System.out.println("TWIN_DRAGON_EGG: " + slea.toString());
                final CashItemInfo item = CashItemFactory.getInstance().getItem(10003055);
                Item itemz = c.getPlayer().getCashInventory().toItem(item);
                //Aristocat c.getSession().write(CSPacket.sendTwinDragonEgg(true, true, 38, itemz, 1));
                break;
            case XMAS_SURPRISE:
                System.out.println("XMAS_SURPRISE: " + slea.toString());
                break;
            case CS_UPDATE:
                CashShopOperation.CSUpdate(c);
                break;
            case USE_POT:
                ItemMakerHandler.UsePot(slea, c);
                break;
            case CLEAR_POT:
                ItemMakerHandler.ClearPot(slea, c);
                break;
            case FEED_POT:
                ItemMakerHandler.FeedPot(slea, c);
                break;
            case CURE_POT:
                ItemMakerHandler.CurePot(slea, c);
                break;
            case REWARD_POT:
                ItemMakerHandler.RewardPot(slea, c);
                break;
            case DAMAGE_SUMMON:
                slea.skip(4);
                SummonHandler.DamageSummon(slea, c.getPlayer());
                break;
            case MOVE_SUMMON:
                SummonHandler.MoveSummon(slea, c.getPlayer());
                break;
            case SUMMON_ATTACK:
                SummonHandler.SummonAttack(slea, c, c.getPlayer());
                break;
            case MOVE_DRAGON:
                SummonHandler.MoveDragon(slea, c.getPlayer());
                break;
            case SUB_SUMMON:
                SummonHandler.SubSummon(slea, c.getPlayer());
                break;
            case REMOVE_SUMMON:
                SummonHandler.RemoveSummon(slea, c);
                break;
            case SPAWN_PET:
                PetHandler.SpawnPet(slea, c, c.getPlayer());
                break;
            case MOVE_PET:
                PetHandler.MovePet(slea, c.getPlayer());
                break;
            case PET_CHAT:
                //System.out.println("Pet chat: " + slea.toString());
                if (slea.available() < 12) {
                    break;
                }
                final int petid = c.getPlayer().getPetIndex(slea.readInt());
                c.getPlayer().updateTick(slea.readInt());
                PetHandler.PetChat(petid, slea.readShort(), slea.readMapleAsciiString(), c.getPlayer());
                break;
            case PET_COMMAND:
                MaplePet pet;
                pet = c.getPlayer().getPet(c.getPlayer().getPetIndex(slea.readInt()));
                //c.getPlayer().updateTick(slea.readInt());
                slea.readInt();
                slea.readByte(); //always 0?
                if (pet == null) {
                    return;
                }
                PetHandler.PetCommand(pet, PetDataFactory.getPetCommand(pet.getPetItemId(), slea.readByte()), c, c.getPlayer());
                break;
            case PET_FOOD:
                PetHandler.PetFood(slea, c, c.getPlayer());
                break;
            case PET_LOOT:
                //System.out.println("PET_LOOT ACCESSED");
                InventoryHandler.Pickup_Pet(slea, c, c.getPlayer());
                break;
            case PET_AUTO_POT:
                PetHandler.Pet_AutoPotion(slea, c, c.getPlayer());
                break;
            case PET_IGNORE:
                PetHandler.PetIgnoreTag(slea, c, c.getPlayer());
                break;
            case MONSTER_CARNIVAL:
                MonsterCarnivalHandler.MonsterCarnival(slea, c);
                break;
            case PACKAGE_OPERATION:
                PackageHandler.handleAction(slea, c);
                break;
            case USE_HIRED_MERCHANT:
                HiredMerchantHandler.UseHiredMerchant(c, true);
                break;
            case MERCH_ITEM_STORE:
                HiredMerchantHandler.MerchantItemStore(slea, c);
                break;
            case CANCEL_DEBUFF:
                // Ignore for now
                break;
            case MAPLETV:
                break;
            case LEFT_KNOCK_BACK:
                PlayerHandler.leftKnockBack(slea, c);
                break;
            case SNOWBALL:
                PlayerHandler.snowBall(slea, c);
                break;
            case COCONUT:
                PlayersHandler.hitCoconut(slea, c);
                break;
            case REPAIR:
                NPCHandler.repair(slea, c);
                break;
            case REPAIR_ALL:
                NPCHandler.repairAll(c);
                break;
            case BUY_SILENT_CRUSADE:
                PlayersHandler.buySilentCrusade(slea, c);
                break;
            //case GAME_POLL:
            //    UserInterfaceHandler.InGame_Poll(slea, c);
            //    break;
            case OWL:
                InventoryHandler.Owl(slea, c);
                break;
            case OWL_WARP:
                InventoryHandler.OwlWarp(slea, c);
                break;
            case USE_OWL_MINERVA:
                InventoryHandler.OwlMinerva(slea, c);
                break;
            case RPS_GAME:
                NPCHandler.RPSGame(slea, c);
                break;
            case UPDATE_QUEST:
                NPCHandler.UpdateQuest(slea, c);
                break;
            case USE_ITEM_QUEST:
                NPCHandler.UseItemQuest(slea, c);
                break;
            case FOLLOW_REQUEST:
                PlayersHandler.FollowRequest(slea, c);
                break;
            case AUTO_FOLLOW_REPLY:
            case FOLLOW_REPLY:
                PlayersHandler.FollowReply(slea, c);
                break;
            case RING_ACTION:
                PlayersHandler.RingAction(slea, c);
                break;
            case REQUEST_FAMILY:
                FamilyHandler.RequestFamily(slea, c);
                break;
            case OPEN_FAMILY:
                FamilyHandler.OpenFamily(slea, c);
                break;
            case FAMILY_OPERATION:
                FamilyHandler.FamilyOperation(slea, c);
                break;
            case DELETE_JUNIOR:
                FamilyHandler.DeleteJunior(slea, c);
                break;
            case DELETE_SENIOR:
                FamilyHandler.DeleteSenior(slea, c);
                break;
            case USE_FAMILY:
                FamilyHandler.UseFamily(slea, c);
                break;
            case FAMILY_PRECEPT:
                FamilyHandler.FamilyPrecept(slea, c);
                break;
            case FAMILY_SUMMON:
                FamilyHandler.FamilySummon(slea, c);
                break;
            case ACCEPT_FAMILY:
                FamilyHandler.AcceptFamily(slea, c);
                break;
            case SOLOMON:
                PlayersHandler.Solomon(slea, c);
                break;
            case GACH_EXP:
                PlayersHandler.GachExp(slea, c);
                break;
            case PARTY_SEARCH_START:
                PartyHandler.MemberSearch(slea, c);
                break;
            case PARTY_SEARCH_STOP:
                PartyHandler.PartySearch(slea, c);
                break;
            case EXPEDITION_LISTING:
                PartyHandler.PartyListing(slea, c);
                break;
            case EXPEDITION_OPERATION:
                PartyHandler.Expedition(slea, c);
                break;
            case USE_TELE_ROCK:
                InventoryHandler.TeleRock(slea, c);
                break;
            case AZWAN_REVIVE:
                PlayersHandler.reviveAzwan(slea, c);
                break;
            case INNER_CIRCULATOR:
                InventoryHandler.useInnerCirculator(slea, c);
                break;
            case PAM_SONG:
                InventoryHandler.PamSong(slea, c);
                break;
            case REPORT:
                PlayersHandler.Report(slea, c);
                break;
            //working
            case CANCEL_OUT_SWIPE:
                slea.readInt();
                break;
            //working
            case VIEW_SKILLS:
                PlayersHandler.viewSkills(slea, c);
                break;
            //working
            case SKILL_SWIPE:
                PlayersHandler.StealSkill(slea, c);
                break;
            case CHOOSE_SKILL:
                PlayersHandler.ChooseSkill(slea, c);
                break;
            case MAGIC_WHEEL:
                System.out.println("[MAGIC_WHEEL] [" + slea.toString() + "]");
                PlayersHandler.magicWheel(slea, c);
                break;
            case REWARD:
                PlayersHandler.onReward(slea, c);
                break;
            case BLACK_FRIDAY:
                PlayersHandler.blackFriday(slea, c);
            case UPDATE_RED_LEAF:
                PlayersHandler.updateRedLeafHigh(slea, c);
                break;
            case SPECIAL_STAT:
                PlayersHandler.updateSpecialStat(slea, c);
                break;
            case UPDATE_HYPER:
                StatsHandling.DistributeHyper(slea, c, c.getPlayer());
                break;
            case RESET_HYPER:
                StatsHandling.ResetHyper(slea, c, c.getPlayer());
                break;
            case DF_COMBO:
                PlayerHandler.absorbingDF(slea, c);
                break;
            case MESSENGER_RANKING:
                PlayerHandler.MessengerRanking(slea, c, c.getPlayer());
                break;
            case OS_INFORMATION:
                System.out.println(c.getSessionIPAddress());
                break;
//            case BUFF_RESPONSE://wat does it do?
//                break;
            case BUTTON_PRESSED:
                break;
            case CASSANDRAS_COLLECTION:
                PlayersHandler.CassandrasCollection(slea, c);
                break;
            case LUCKY_LUCKY_MONSTORY:
                PlayersHandler.LuckyLuckyMonstory(slea, c);
                break;
            case PLAYER_UPDATE:
                PlayerHandler.PlayerUpdate(c.getPlayer());
                break;
            case BUFF_RESPONSE:
                break;
            case ITEM_SUNZI:
                InventoryHandler.SunziBF(slea, c);
                break;
            case ITEM_BAOWU:
                InventoryHandler.UsePenguinBox(slea, c);
                break;
            default:
                System.out.println("[UNHANDLED] Recv [" + header.toString() + "] found");
                break;
        }
    }

    public static boolean isSpamHeader(RecvPacketOpcode header) {
        switch (header) {
            case PONG:
           // case CRASH_INFO:
                // case AUTH_REQUEST:
                //case MOVE_LIFE:
                //case MOVE_PLAYER:
                //case SPECIAL_MOVE:
            /*case MOVE_ANDROID:
                 case MOVE_DRAGON:
                 case MOVE_SUMMON:
                 case MOVE_FAMILIAR:
                 case MOVE_PET:*/
                // case CLOSE_RANGE_ATTACK:
                //  case QUEST_ACTION:
                //  case HEAL_OVER_TIME:
                //case CHANGE_KEYMAP:
                //  case USE_INNER_PORTAL:
                //   case MOVE_HAKU:
                //case FRIENDLY_DAMAGE:
                //case CLOSE_RANGE_ATTACK: //todo code zero
                //case RANGED_ATTACK: //todo code zero
                //case ARAN_COMBO:
                //   case SPECIAL_STAT:
                //    case UPDATE_HYPER:
                //    case RESET_HYPER:
                //    case NPC_ACTION:
                //case ANGELIC_CHANGE:
                //    case MOVE_PET:
//            case DRESSUP_TIME:
                //    case BUTTON_PRESSED:
                return true;
        }
        return false;
    }
}
