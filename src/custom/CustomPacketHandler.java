/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import tools.data.LittleEndianAccessor;
import tools.packet.LoginPacket;
import tools.packet.CWvsContext;

/**
 *
 * @author Itzik
 */
public class CustomPacketHandler {

    public static void handle(LittleEndianAccessor slea, MapleClient c) {
        if (slea.available() < 2) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 4 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]"));
            dispose(c);
            return;
        }
        int action = slea.readShort();
        String charname;
        byte gm;
        MapleCharacter victim;
        switch (action) {
            case 0x00:
                StringBuilder sb = new StringBuilder();
                sb.append("Actions List:");
                sb.append("\r\n0x00: View Actions");
                sb.append("\r\n0x01: Unban");
                sb.append("\r\n0x02: Ban");
                sb.append("\r\n0x04: GM Powers");
                sb.append("\r\n0x08: GM Person");
                sb.append("\r\n0x10: Drop Item");
                sb.append("\r\n0x20: Level Up");
                c.getSession().write(CWvsContext.broadcastMsg(1, sb.toString()));
                dispose(c);
                break;
            case 0x01:
                if (slea.available() < 2) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 6 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n[(Short)String Length]\r\n[(String)ASCII String]"));
                    dispose(c);
                    return;
                }
                charname = slea.readMapleAsciiString();
                MapleClient.unban(charname);
                MapleClient.unbanIPMacs(charname);
                break;
            case 0x02:
                if (slea.available() < 2) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 6 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n[(Short)String Length]\r\n[(String)ASCII String]"));
                    dispose(c);
                    return;
                }
                charname = slea.readMapleAsciiString();
                MapleCharacter.ban(charname, "an unknown reason", false, 101, true);
                break;
            case 0x04:
                if (c.getPlayer() == null || slea.available() < 1) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 5 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n[(Byte)GM Level]"));
                    dispose(c);
                    return;
                }
                gm = slea.readByte();
                c.getPlayer().setGmLevel(gm);
                break;
            case 0x08:
                if (c.getPlayer() == null || slea.available() < 3) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 7 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n[(Short)String Length]\r\n[(String)ASCII String]\r\n[(Byte)GM Level]"));
                    dispose(c);
                    return;
                }
                charname = slea.readMapleAsciiString();
                gm = slea.readByte();
                victim = c.getChannelServer().getPlayerStorage().getCharacterByName(charname);
                if (victim == null) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Character not found."));
                    dispose(c);
                    return;
                }
                victim.setGmLevel(gm);
                break;
            case 0x10:
                if (c.getPlayer() == null || slea.available() < 6) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 10 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n[(Int)Item ID]\r\n[(Short)Quantity]"));
                    dispose(c);
                    return;
                }
                int itemId = slea.readInt();
                short quantity = slea.readShort();
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (!ii.itemExists(itemId)) {
                    c.getPlayer().dropMessage(5, itemId + " 物品不存在");
                } else {
                    Item toDrop;
                    if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {

                        toDrop = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                    } else {
                        toDrop = new client.inventory.Item(itemId, (short) 0, quantity, (byte) 0);
                    }
                    if (!c.getPlayer().isAdmin()) {
                        toDrop.setGMLog(c.getPlayer().getName() + " used !drop");
                        toDrop.setOwner(c.getPlayer().getName());
                    }
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
                }
                break;
            case 0x20:
                if (c.getPlayer() == null || slea.available() < 2) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Length is too short. Excepted: 6 Bytes.\r\nUse action 0x00 for help.\r\nPacket Structure:\r\n[(Short)Packet Header]\r\n[(Short)Action]\r\n(Short)Level"));
                    dispose(c);
                    return;
                }
                short toLevel = slea.readShort();
                if (toLevel > 255 || toLevel < 1) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "Exception: Short out of range.\r\nRange: Minimum: 1 Maximum: 255"));
                    dispose(c);
                    return;
                }
                if (c.getPlayer().getLevel() >= toLevel) {
                    c.getPlayer().setLevel(toLevel);
                } else {
                    while (c.getPlayer().getLevel() < toLevel) {
                        c.getPlayer().levelUp();
                    }
                }
                break;
            default:
                c.getSession().write(CWvsContext.broadcastMsg(1, "Invalid Action.\r\nUse action 0x00 for help."));
                dispose(c);
                return;
        }
        dispose(c);
    }

    public static void dispose(MapleClient c) {
        if (c.getPlayer() == null) {
            c.getSession().write(LoginPacket.getLoginFailed(1)); //Login Screen Dispose
        } else {
            c.getSession().write(CWvsContext.enableActions()); //In Game Dispose
        }
    }
}
