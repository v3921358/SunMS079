package handling.cashshop.handler;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.inventory.InventoryConstants;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;
import database.DBConPool;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import server.AutobanManager;
import server.CashItem;
import server.CashItemFactory;
import server.CashItemInfo;
import server.CashShop;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;

public class CashShopOperation {

    public static void LeaveCS(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) throws InterruptedException {
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        final String s = c.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
        c.getSession().write(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        chr.saveToDB(false, true);
        c.setPlayer(null);
        c.setReceiving(false);
        //c.getSession().close();
    }

    public static void EnterCS(final CharacterTransfer transfer, final MapleClient c) {
        if (transfer == null) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }

        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        CashShopServer.getPlayerStorage().registerPlayer(chr);
        c.getSession().write(CSPacket.warpCS(c));

        //c.getSession().write(CSPacket.disableCS()); // Updated to v146.1 // Nothing changed
        //c.getSession().write(CSPacket.loadCategories()); // Updated to v146.1 // Noting changed
        //c.getSession().write(CSPacket.CS_Picture_Item()); // Updated - Need to check if nothing changed
        //c.getSession().write(CSPacket.CS_Top_Items()); // Updated to v146.1
        //c.getSession().write(CSPacket.CS_Special_Item()); // Updated to v146.1
        //c.getSession().write(CSPacket.CS_Featured_Item()); // Updated to v146.
        //c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
        //doCSPackets(c);
        //loadCashShop(c);
        doCSPackets(c);
        if (c.getPlayer().isAdmin()) {
            c.StartWindow();
        }
    }

    public static void loadCashShop(MapleClient c) {
        c.getSession().write(CSPacket.loadCategories());
        String head = "E2 02";
        c.getSession().write(CField.getPacketFromHexString(head + " 04 01 09 00 09 3D 00 40 A5 3D 00 38 6D 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 1B E5 F5 05 30 71 54 00 01 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 B8 0B 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 40 36 59 61 3A CF 01 00 00 A0 83 2A 3B CF 01 84 03 00 00 00 00 00 00 01 00 00 00 01 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 09 3D 00 40 A5 3D 00 E4 DE 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 0F E4 F5 05 E2 E7 8A 00 01 00 00 00 03 00 00 00 01 00 00 00 00 00 00 00 40 38 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 30 2A 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 10 E4 F5 05 79 3D 4D 00 01 00 00 00 40 38 00 00 30 2A 00 00 00 00 00 00 0C 00 00 00 5A 00 00 00 02 00 00 00 BC E1 F5 05 FF 61 54 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 00 09 3D 00 40 A5 3D 00 E4 DE 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 13 E4 F5 05 E4 E7 8A 00 01 00 00 00 03 00 00 00 01 00 00 00 00 00 00 00 20 67 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 58 4D 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 14 E4 F5 05 7A 3D 4D 00 01 00 00 00 20 67 00 00 58 4D 00 00 00 00 00 00 0C 00 00 00 5A 00 00 00 02 00 00 00 BC E1 F5 05 FF 61 54 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 00 09 3D 00 40 A5 3D 00 E4 DE 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 0A E4 F5 05 79 3D 4D 00 01 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 E0 2E 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 40 36 59 61 3A CF 01 80 69 07 83 2A 3B CF 01 10 27 00 00 00 00 00 00 0B 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 09 3D 00 40 A5 3D 00 E4 DE 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 73 E2 F5 05 64 3F 4D 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 5D 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 C0 5D 00 00 00 00 00 00 0B 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 09 3D 00 40 A5 3D 00 A0 E1 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 7F E2 F5 05 93 E7 8A 00 01 00 00 00 03 00 00 00 01 00 00 00 00 00 00 00 B8 3D 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 40 50 3D 41 30 CE 01 00 80 05 BB 46 E6 17 02 00 32 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 00 00 00 74 E2 F5 05 64 3F 4D 00 00 00 00 00 E0 2E 00 00 AC 26 00 00 00 00 00 00 05 00 00 00 5A 00 00 00 02 00 00 00 06 2D 9A 00 9C 62 54 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 71 E2 F5 05 F8 62 54 00 01 00 00 00 D8 0E 00 00 54 0B 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 00 09 3D 00 40 A5 3D 00 A0 E1 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 80 E2 F5 05 94 E7 8A 00 01 00 00 00 03 00 00 00 01 00 00 00 00 00 00 00 98 6C 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 40 50 3D 41 30 CE 01 00 80 05 BB 46 E6 17 02 28 55 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 00 00 00 75 E2 F5 05 64 3F 4D 00 00 00 00 00 C0 5D 00 00 D4 49 00 00 00 00 00 00 0A 00 00 00 5A 00 00 00 02 00 00 00 06 2D 9A 00 9C 62 54 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 09 2D 9A 00 9D 62 54 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 71 E2 F5 05 F8 62 54 00 01 00 00 00 D8 0E 00 00 54 0B 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 02 00 00 00 00 09 3D 00 40 A5 3D 00 48 DF 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 71 E2 F5 05 F8 62 54 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 D8 0E 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 D8 0E 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 18 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 09 3D 00 40 A5 3D 00 48 DF 0F 00 55 00 68 74 74 70 3A 2F 2F 6E 78 63 61 63 68 65 2E 6E 65 78 6F 6E 2E 6E 65 74 2F 73 70 6F 74 6C 69 67 68 74 2F 32 38 36 2F 30 30 45 53 33 2D 64 62 33 63 63 36 64 38 2D 32 36 31 62 2D 34 35 36 30 2D 38 33 31 33 2D 62 30 36 61 66 62 66 30 66 34 39 34 2E 6A 70 67 7E E2 F5 05 E6 62 54 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8C 0A 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 8C 0A 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
        c.getSession().write(CField.getPacketFromHexString(head + " 05 01 04 C0 C6 2D 00 D0 ED 2D 00 48 DF 0F 00 00 00 71 E2 F5 05 F8 62 54 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 D8 0E 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 D8 0E 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 18 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 D0 ED 2D 00 48 DF 0F 00 00 00 7E E2 F5 05 E6 62 54 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 8C 0A 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 8C 0A 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 D0 ED 2D 00 D4 B7 0F 00 00 00 DA FE FD 02 A0 A6 4F 00 01 00 00 00 00 00 00 00 00 00 00 00 32 00 00 00 10 27 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 10 27 00 00 00 00 00 00 0B 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 23 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 D0 ED 2D 00 7C 6A 0F 00 00 00 87 2C 9A 00 AC AE 4F 00 01 00 00 00 00 00 00 00 00 00 00 00 22 00 00 00 48 0D 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 48 0D 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 23 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
        c.getSession().write(CField.getPacketFromHexString(head + " 06 01 05 C0 C6 2D 00 E0 14 2E 00 15 54 10 00 00 00 9C F1 FA 02 58 95 4E 00 01 00 00 00 04 00 00 00 00 00 00 00 32 00 00 00 E4 0C 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 E4 0C 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 11 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 E0 14 2E 00 16 54 10 00 00 00 C9 F1 FA 02 35 9D 4E 00 01 00 00 00 04 00 00 00 00 00 00 00 32 00 00 00 E4 0C 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 E4 0C 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 77 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 E0 14 2E 00 C4 90 0F 00 00 00 BF C3 C9 01 84 E7 4C 00 01 00 00 00 00 00 00 00 00 00 00 00 0F 00 00 00 AC 26 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 AC 26 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 24 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 E0 14 2E 00 7C 6A 0F 00 00 00 87 2C 9A 00 AC AE 4F 00 01 00 00 00 00 00 00 00 00 00 00 00 22 00 00 00 48 0D 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 48 0D 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 23 08 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 E0 14 2E 00 D4 B7 0F 00 00 00 D9 FE FD 02 A0 A6 4F 00 01 00 00 00 00 00 00 00 00 00 00 00 32 00 00 00 30 75 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 30 75 00 00 00 00 00 00 23 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 DA 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
        c.getSession().write(CField.getPacketFromHexString(head + " 09 01 01 C0 C6 2D 00 00 63 2E 00 B0 08 10 00 00 00 18 E3 F5 05 A8 69 52 00 01 00 00 00 05 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 01 00 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 01 01 01 01 01 02 00 00 00 65 01 00 00 32 00 00 00 0A 00 31 4D 53 35 34 30 31 30 30 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
        c.getSession().write(CField.getPacketFromHexString(head + " 08 01 05 C0 C6 2D 00 F0 3B 2E 00 C4 90 0F 00 00 00 BF C3 C9 01 84 E7 4C 00 01 00 00 00 04 00 00 00 00 00 00 00 0F 00 00 00 AC 26 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 AC 26 00 00 00 00 00 00 01 00 00 00 1E 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 24 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 F0 3B 2E 00 74 E0 0F 00 00 00 7C FE FD 02 81 3A 54 00 01 00 00 00 04 00 00 00 00 00 00 00 02 00 00 00 A0 0F 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 A0 0F 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 B8 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 F0 3B 2E 00 E8 07 10 00 00 00 40 FE FD 02 70 13 54 00 01 00 00 00 04 00 00 00 00 00 00 00 01 00 00 00 F4 01 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 F4 01 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 8E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 F0 3B 2E 00 10 E0 0F 00 00 00 3D FE FD 02 D0 FD 54 00 01 00 00 00 04 00 00 00 00 00 00 00 04 00 00 00 24 13 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 24 13 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 77 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 C6 2D 00 F0 3B 2E 00 74 E0 0F 00 00 00 35 FE FD 02 80 3A 54 00 01 00 00 00 04 00 00 00 00 00 00 00 03 00 00 00 B8 0B 00 00 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 00 80 22 D6 94 EF C4 01 00 80 05 BB 46 E6 17 02 B8 0B 00 00 00 00 00 00 01 00 00 00 5A 00 00 00 01 00 01 00 01 00 00 00 01 00 02 00 00 00 F2 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"));
    }

    public static void CSUpdate(final MapleClient c) {

        doCSPackets(c);
    }

    private static boolean CouponCodeAttempt(final MapleClient c) {
        c.couponAttempt++;
        return c.couponAttempt > 5;
    }

    public static void CouponCode(final String code, final MapleClient c) {
        if (code.length() <= 0) {
            return;
        }
        Triple<Boolean, Integer, Integer> info = null;
        try {
            info = MapleCharacterUtil.getNXCodeInfo(code);
        } catch (SQLException e) {
        }
        if (info != null && info.left) {
            if (!CouponCodeAttempt(c)) {
                int type = info.mid, item = info.right;
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                }
                /*
                 * Explanation of type!
                 * Basically, this makes coupon codes do
                 * different things!
                 *
                 * Type 1: A-Cash,
                 * Type 2: Maple Points
                 * Type 3: Item.. use SN
                 * Type 4: Mesos
                 */
                Map<Integer, Item> itemz = new HashMap<>();
                int maplePoints = 0, mesos = 0;
                switch (type) {
                    case 1:
                    case 2:
                        c.getPlayer().modifyCSPoints(type, item, false);
                        maplePoints = item;
                        break;
                    case 3:
                        CashItemInfo itez = CashItemFactory.getInstance().getItem(item);
                        if (itez == null) {
                            c.getSession().write(CSPacket.sendCSFail(0));
                            return;
                        }
                        byte slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", "Cash shop: coupon code" + " on " + FileoutputUtil.CurrentReadable_Date());
                        if (slot < 0) {
                            c.getSession().write(CSPacket.sendCSFail(0));
                            return;
                        } else {
                            itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                        }
                        break;
                    case 4:
                        c.getPlayer().gainMeso(item, false);
                        mesos = item;
                        break;
                }
                c.getSession().write(CSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
                doCSPackets(c);
            }
        } else {
            if (CouponCodeAttempt(c) == true) {
                c.getSession().write(CSPacket.sendCSFail(48)); //A1, 9F
            } else {
                c.getSession().write(CSPacket.sendCSFail(info == null ? 14 : 17)); //A1, 9F
            }
        }
    }

    public static void BuyCashItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int action = slea.readByte();
        if (action == 0) {
            slea.skip(2);
            CouponCode(slea.readMapleAsciiString(), c);
        } else if (action == 2) {
            slea.skip(1);
            int type = slea.readInt();//type
            int sn = slea.readInt();
//            final CashItem item = CashItemFactory.getInstance().getMenuItem(sn);
            final CashItem item = CashItemFactory.getInstance().getAllItem(sn);
            final int toCharge = slea.readInt();//price
            if (item == null) {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
            chr.modifyCSPoints(type, -toCharge, true);
            Item itemz = chr.getCashInventory().toItem(item);
            if (itemz != null) {
                chr.getCashInventory().addToInventory(itemz);
                c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 101) {//TODO BETTER idk what it is
//            System.out.println("action 101");//might be farm mesos? RITE NOW IS FREEH
            slea.skip(1);
            int type = slea.readInt();//type
            int sn = slea.readInt();
            final CashItem item = CashItemFactory.getInstance().getAllItem(sn);
            if (item == null) {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
//            chr.modifyCSPoints(type, -toCharge, true);
            Item itemz = chr.getCashInventory().toItem(item);
            if (itemz != null) {
                chr.getCashInventory().addToInventory(itemz);
                c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 0x03) {//商城购买

            final int toCharge = slea.readByte() + 1;
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());

            if (item != null) {
                System.out.println("购买事件ID: " + action + " 物品代码: " + item.getId() + " 物品SN:" + item.getSN());
            }
            if (item != null && chr.getCSPoints(toCharge) >= item.getPrice()) {
                if (isHideItem(item.getId())) {
                    c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                    c.getSession().write(CWvsContext.enableActions());
                    doCSPackets(c);
                    return;
                }
                if (!item.genderEquals(c.getPlayer().getGender())/* && c.getPlayer().getAndroid() == null*/) {
                    c.getSession().write(CSPacket.sendCSFail(0xA7));
                    doCSPackets(c);
                    return;
                } else if (item.getId() == 5211046 || item.getId() == 5211048 || item.getId() == 5050100 || item.getId() == 5051001) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "You cannot purchase this item through cash shop."));
                    c.getSession().write(CWvsContext.enableActions());
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    c.getSession().write(CSPacket.sendCSFail(0xB2));
                    doCSPackets(c);
                    return;
                }
                for (int id : GameConstants.cashBlock) {
                    if (item.getId() == id) {
                        c.getSession().write(CWvsContext.broadcastMsg(1, "You cannot purchase this item through cash shop."));
                        c.getSession().write(CWvsContext.enableActions());
                        doCSPackets(c);
                        return;
                    }
                }
                chr.modifyCSPoints(toCharge, -item.getPrice(), false);
                Item itemz = chr.getCashInventory().toItem(item);
                if (toCharge == 1) {
                    short flag = itemz.getFlag();
                    boolean 交易 = true;
                    if (itemz.getItemId() / 10000 == 521 || itemz.getItemId() / 10000 == 536 || GameConstants.isPets(itemz.getItemId()) || itemz.getItemId() == 5110000) {
                        交易 = false;
                    }
                    if (交易 == true) {
                        if (itemz.getType() == MapleInventoryType.EQUIP.getType()) {
                            flag |= ItemFlag.KARMA_EQ.getValue();
                        } else {
                            flag |= ItemFlag.KARMA_USE.getValue();
                        }
                        itemz.setFlag(flag);
                    }
                }

                if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                    chr.getCashInventory().addToInventory(itemz);
                    //c.getSession().write(CSPacket.confirmToCSInventory(itemz, c.getAccID(), item.getSN()));
                    c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));

                } else {
                    c.getSession().write(CSPacket.sendCSFail(0));
                }
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 4 || action == 32) { //gift, package
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            final int toCharge = slea.readByte() + 1;
            String partnerName = slea.readMapleAsciiString();
            String msg = slea.readMapleAsciiString();

            if (item == null || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || msg.length() > 73 || msg.length() < 1) { //dont want packet editors gifting random stuff =P
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            if (isHideItem(item.getId())) {
                c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                c.getSession().write(CWvsContext.enableActions());
                doCSPackets(c);
                return;
            }
            Triple<Integer, Integer, Integer> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft() <= 0 || info.getLeft() == c.getPlayer().getId() || info.getMid() == c.getAccID()) {
                c.getSession().write(CSPacket.sendCSFail(0)); //9E v75
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(info.getRight())) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else {
                //get the packets for that
                c.getPlayer().getCashInventory().gift(info.getLeft(), c.getPlayer().getName(), msg, item.getSN(), MapleInventoryIdentifier.getInstance());
                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                c.getSession().write(CSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName));
            }
        } else if (action == 5) { //was Wishlist - Now inc inv slot
            slea.skip(1);
            final int toCharge = slea.readInt();
            final boolean coupon = slea.readByte() > 0;
            if (coupon) {
                final MapleInventoryType type = getInventoryType(slea.readInt());
                if (chr.getCSPoints(toCharge) >= 6000 && chr.getInventory(type).getSlotLimit() < 89) {
                    chr.modifyCSPoints(toCharge, -6000, false);
                    chr.getInventory(type).addSlot((byte) 8);
                    chr.dropMessage(1, "栏位已增加到 " + chr.getInventory(type).getSlotLimit());
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            } else {
                final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                if (chr.getCSPoints(toCharge) >= 4000 && chr.getInventory(type).getSlotLimit() < 93) {
                    chr.modifyCSPoints(toCharge, -4000, false);
                    chr.getInventory(type).addSlot((byte) 4);
                    chr.dropMessage(1, "栏位已增加到 " + chr.getInventory(type).getSlotLimit());
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            }
//            chr.clearWishlist();
//            if (slea.available() < 40) {
//                c.getSession().write(CSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//            }
//            int[] wishlist = new int[10];
//            for (int i = 0; i < 10; i++) {
//                wishlist[i] = slea.readInt();
//            }
//            chr.setWishlist(wishlist);
//            c.getSession().write(CSPacket.sendWishList(chr, true));

        } else if (action == 6) { // Increase inv
            int useNX = slea.readByte() + 1;
            final boolean coupon = slea.readByte() > 0;
            if (coupon) {
                final MapleInventoryType type = getInventoryType(slea.readInt());
                if (chr.getCSPoints(useNX) >= 1100 && chr.getInventory(type).getSlotLimit() < 89) {
                    chr.modifyCSPoints(useNX, -1100, false);
                    chr.getInventory(type).addSlot((byte) 8);

                    chr.dropMessage(1, "栏位已增加到 " + chr.getInventory(type).getSlotLimit());
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0));

                    //   c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            } else {
                final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                if (chr.getCSPoints(useNX) >= 600 && chr.getInventory(type).getSlotLimit() < 93) {
                    chr.modifyCSPoints(useNX, -600, false);
                    chr.getInventory(type).addSlot((byte) 4);
                    chr.dropMessage(1, "栏位已增加到 " + chr.getInventory(type).getSlotLimit());
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0));

                    //   c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            }

        } else if (action == 7) { // Increase slot space
            int useNX = slea.readByte() + 1;
            if (chr.getCSPoints(useNX) >= 600 && chr.getStorage().getSlots() < 45) {
                chr.modifyCSPoints(useNX, -600, false);
                chr.getStorage().increaseSlots((byte) 4);
                chr.getStorage().saveToDB();
                chr.dropMessage(1, "仓库栏位已增加到 " + chr.getStorage().getSlots());

                //     c.getSession().write(MTSCSPacket.increasedStorageSlots(chr.getStorage().getSlots()));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
                //  c.getSession().write(CSPacket.sendCSFail(0xA4));
            }
        } else if (action == 8) { //...10 = pendant slot expansion
            slea.skip(1);
            final int toCharge = slea.readInt();
            CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            int slots = c.getCharacterSlots();
            if (item == null || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || slots > 15 || item.getId() != 5430000) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            if (c.gainCharacterSlot()) {
                c.getPlayer().modifyCSPoints(toCharge, -item.getPrice(), false);
                chr.dropMessage(1, "角色栏位增加到: " + (slots + 1));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 10) { //...10 = pendant slot expansion
            //Data: 00 01 00 00 00 DC FE FD 02
            slea.readByte(); //Action is short?
            slea.readInt(); //always 1 - No Idea
            final int sn = slea.readInt();
            CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
            if (item == null || c.getPlayer().getCSPoints(1) < item.getPrice() || item.getId() / 10000 != 555) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            MapleQuestStatus marr = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(GameConstants.PENDANT_SLOT));
            if (marr != null && marr.getCustomData() != null && Long.parseLong(marr.getCustomData()) >= System.currentTimeMillis()) {
                c.getSession().write(CSPacket.sendCSFail(0));
            } else {
                c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.PENDANT_SLOT)).setCustomData(String.valueOf(System.currentTimeMillis() + ((long) item.getPeriod() * 24 * 60 * 60000)));
                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                chr.dropMessage(1, "获得额外的挂件槽.");
            }
        } else if (action == 0x0D) { //商城到背包
            //uniqueid, 00 01 01 00, type->position(short)
            Item item = c.getPlayer().getCashInventory().findByCashId((int) slea.readLong());
//            Item item = MapleItemInformationProvider.getInstance().getEquipById(item);

            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                Item item_ = item.copy();
                short pos = MapleInventoryManipulator.addbyItem(c, item_, true);
                if (pos >= 0) {
                    if (item_.getPet() != null) {
                        item_.getPet().setInventoryPosition(pos);
                        c.getPlayer().addPet(item_.getPet());
                    }
                    c.getPlayer().getCashInventory().removeFromInventory(item);
                    c.getSession().write(CSPacket.confirmFromCSInventory(item_, pos));
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xB1));
                }
            } else {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
            }
        } else if (action == 0x0E) { //背包到商城
            Item item1;
            int sn;
            CashShop cs = chr.getCashInventory();
            int cashId = (int) slea.readLong();
            byte type = slea.readByte();
            MapleInventory mi = chr.getInventory(MapleInventoryType.getByType(type));
            item1 = mi.findByUniqueId(cashId);
            if (item1 == null) {
                c.getSession().write(CSPacket.showNXMapleTokens(chr));
                return;
            }
            if (cs.getItemsSize() < 100) {
                sn = CashItemFactory.getInstance().getItemSN(item1.getItemId());
                cs.addToInventory(item1);
                mi.removeSlot(item1.getPosition());
                c.getSession().write(CSPacket.confirmToCSInventory(item1, c.getAccID(), sn));
            } else {
                chr.dropMessage(1, "移动失败。");
            }
        } else if (action == 0x1A) {
            int toCharge = 2;//抵用卷
            long uniqueId = (int) slea.readLong();
            Item item = c.getPlayer().getCashInventory().findByCashId((int) uniqueId);

            if (item == null) {
                c.getSession().write(CSPacket.showNXMapleTokens(chr));
                return;
            }
            int sn = CashItemFactory.getInstance().getItemSN(item.getItemId());
            CashItemInfo cItem = CashItemFactory.getInstance().getItem((int) sn);

            if (!MapleItemInformationProvider.getInstance().isCash(cItem.getId())) {
                AutobanManager.getInstance().autoban(chr.getClient(), "商城非法换购道具.");
                return;
            }
            int Money = cItem.getPrice() / 10 * 3;
            c.getPlayer().getCashInventory().removeFromInventory(item);
            chr.modifyCSPoints(toCharge, Money, false);
            c.getSession().write(CWvsContext.broadcastMsg(1, "成功换购抵用券" + Money + "点。"));
            doCSPackets(c);

        } else if (action == 29 || action == 36) { //38 = 挚友戒, 28 = 恋人戒
            final int toCharge = 1;
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            final String partnerName = slea.readMapleAsciiString();
            final String msg = slea.readMapleAsciiString();
            if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.getSession().write(CSPacket.sendCSFail(0));//0xA6
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                c.getSession().write(CSPacket.sendCSFail(0));//0xB1
                doCSPackets(c);
                return;

            }
            Triple<Integer, Integer, Integer> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft() <= 0 || info.getLeft() == c.getPlayer().getId()) {
                c.getSession().write(CSPacket.sendCSFail(0)); //9E v75 -- B4
                doCSPackets(c);
                return;
            } else if (info.getMid() == c.getAccID()) {
                c.getSession().write(CSPacket.sendCSFail(0)); //9D v75 --  A3
                doCSPackets(c);
                return;
            } else {
                if (info.getRight() == c.getPlayer().getGender() && action == 29) {
                    c.getSession().write(CSPacket.sendCSFail(0)); //9B v75--  A1
                    doCSPackets(c);
                    return;
                }

                int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());

                if (err != 1) {
                    c.getSession().write(CSPacket.sendCSFail(0)); //9E v75
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(toCharge, -item.getPrice(), false);
                c.getSession().write(CSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName));
            }
        } else if (action == 0x1F) {//was 33 - packages
            int type = slea.readByte() + 1;
            int snID = slea.readInt();
            final CashItemInfo item = CashItemFactory.getInstance().getItem(snID);
            List<Integer> ccc = null;
            if (item != null) {
                ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
            }
            if (item == null || ccc == null || c.getPlayer().getCSPoints(type) < item.getPrice()) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.getSession().write(CSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            Item itemz = null;
            Map<Integer, Item> ccz = new HashMap<>();
            for (int i : ccc) {
                final CashItemInfo cii = CashItemFactory.getInstance().getPackageItem(i);
                if (cii == null) {
                    continue;
                }
                itemz = c.getPlayer().getCashInventory().toItem(cii);
                if (itemz == null || itemz.getUniqueId() <= 0) {
                    continue;
                }
                for (int iz : GameConstants.cashBlock) {
                    if (itemz.getItemId() == iz) {
                    }
                }
                ccz.put(i, itemz);
                c.getPlayer().getCashInventory().addToInventory(itemz);
            }
            chr.modifyCSPoints(type, -item.getPrice(), false);
            //     c.getSession().write(CSPacket.showBoughtCSPackage(ccz, c.getAccID()));
            c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));

        } else if (action == 33 || action == 100) { //99 buy with mesos
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getMeso() < item.getPrice()) {
                c.getSession().write(CSPacket.sendCSFail(0xB8));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, "Cash shop: quest item" + " on " + FileoutputUtil.CurrentReadable_Date());
            if (pos < 0) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            chr.gainMeso(-item.getPrice(), false);
            c.getSession().write(CSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
        } else if (action == 49) {
            c.getSession().write(CSPacket.updatePurchaseRecord());
        } else if (action == 92) { // Open random box.
            final int uniqueid = (int) slea.readLong();

            //c.getSession().write(CSPacket.sendRandomBox(uniqueid, new Item(1302000, (short) 1, (short) 1, (short) 0, 10), (short) 0));
            //} else if (action == 99) { //buy with mesos
            //    int sn = slea.readInt();
            //    int price = slea.readInt();
        } else if (action == 44) { // Open random box.
            doCSPackets(c);
        } else {
            System.out.println("New Action: " + action + " Remaining: " + slea.toString());
            c.getSession().write(CSPacket.sendCSFail(0));
        }
        doCSPackets(c);
    }

    public static void SwitchCategory(final LittleEndianAccessor slea, final MapleClient c) {
        int Scategory = slea.readByte();
//        System.out.println("Scategory " + Scategory);
        if (Scategory == 103) {
            slea.skip(1);
            int itemSn = slea.readInt();
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO `wishlist` VALUES (?, ?)")) {
                    ps.setInt(1, c.getPlayer().getId());
                    ps.setInt(2, itemSn);
                    ps.executeUpdate();
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println("SwitchCategory 103" + ex);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            }
            c.getSession().write(CSPacket.addFavorite(itemSn));
        } else if (Scategory == 105) {
            int item = slea.readInt();
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                try (PreparedStatement ps = con.prepareStatement("UPDATE cashshop_items SET likes = likes+" + 1 + " WHERE sn = ?")) {
                    ps.setInt(1, item);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.err.println("SwitchCategory 105" + ex);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            }
            c.getSession().write(CSPacket.Like(item));
        } else if (Scategory == 109) {
            c.getSession().write(CSPacket.Favorite(c.getPlayer()));
        } else if (Scategory == 112) {//click on special item TODO
            //int C8 - C9 - CA
        } else if (Scategory == 113) {//buy from cart inventory TODO
            //byte buy = 1 or gift = 0
            //byte amount
            //for each SN
        } else {
            int category = slea.readInt();
            if (category == 4000000) {
                c.getSession().write(CSPacket.CS_Top_Items());
                c.getSession().write(CSPacket.CS_Picture_Item());
            } else if (category == 1060100) {
                c.getSession().write(CSPacket.showNXChar(category));
                c.getSession().write(CSPacket.changeCategory(category));
            } else {
//                System.err.println(category);
                c.getSession().write(CSPacket.changeCategory(category));
            }
        }
    }

    private static MapleInventoryType getInventoryType(final int id) {
        switch (id) {
            case 50200018:
                return MapleInventoryType.EQUIP;
            case 50200019:
                return MapleInventoryType.USE;
            case 50200020:
                return MapleInventoryType.SETUP;
            case 50200021:
                return MapleInventoryType.ETC;
            default:
                System.out.println("未处理的 InventoryType : " + id);
                return MapleInventoryType.UNDEFINED;
        }
    }

    public static void doCSPackets(MapleClient c) {
        c.getSession().write(CSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
        c.getSession().write(CSPacket.getCSGifts(c));
        c.getSession().write(CSPacket.getCSInventory(c));
        //c.getSession().write(CSPacket.doCSMagic());
        //c.getSession().write(CSPacket.getCSGifts(c));
        //c.getSession().write(CWvsContext.BuddylistPacket.updateBuddylist(c.getPlayer().getBuddylist().getBuddies()));
        //c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
//        c.getSession().write(CSPacket.sendWishList(c.getPlayer(), false));
//        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
        c.getSession().write(CSPacket.enableCSUse());
//         c.getPlayer().getCashInventory().checkExpire(c);
    }

    public static boolean isHideItem(int item) {
        String Hide_Items[] = null;
        Properties itempb_cfg = new Properties();
        try {
            InputStreamReader is = new FileReader("settings.ini");
            itempb_cfg.load(is);
            is.close();
            Hide_Items = itempb_cfg.getProperty("cashban", "0").split(",");
        } catch (Exception e) {
        }
        if (Hide_Items != null) {
            for (int i = 0; i < Hide_Items.length; i++) {
                if (item == Integer.parseInt(Hide_Items[i])) {
                    return true;
                }
            }
        }
        return false;

    }
}
