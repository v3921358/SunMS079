/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.farm.handler;

import client.MapleCharacterUtil;
import client.MapleClient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import server.farm.MapleFarm;
import tools.data.LittleEndianAccessor;
import tools.packet.FarmPacket;

/**
 *
 * @author Itzik
 */
public class FarmHandler {

    public static void createFarm(LittleEndianAccessor slea, MapleClient c) {
        String name = slea.readMapleAsciiString();
        if (!MapleCharacterUtil.canCreateChar(name, false)) {
            return;
        }
        MapleFarm farm = MapleFarm.getDefault(35549721, c, name);
        farm.setLevel(1);
        c.setFarm(farm);
        c.getSession().write(FarmPacket.updateQuestInfo(1111, 1, "A1/Z/"));
        //c.getSession().write(FarmPacket.updateFarmInfo(c, true));
        //c.getSession().write(CField.getPacketFromHexString("68 03 19 72 1E 02 00 00 00 00 00 00 00 00 00 00 00 00 0A 00 65 73 6D 69 66 61 72 6D 7A 7A 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 00 01 00 00 00 00 0B 00 43 72 65 61 74 69 6E 67 2E 2E 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 FF FF FF FF 00"));
        c.getSession().write(FarmPacket.farmPacket4());
        c.getSession().write(FarmPacket.updateQuestInfo(30000, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30003, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30007, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30011, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30015, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30019, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30023, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30027, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30045, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30050, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30055, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30060, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30065, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30070, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30076, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30080, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30081, 0, "A1/"));
        c.getSession().write(FarmPacket.updateQuestInfo(30082, 0, "A1/"));
    }

    public static void completeQuest(LittleEndianAccessor slea, MapleClient c) {
        int questId = slea.readInt();
        if (questId == 1111) {
            c.getSession().write(FarmPacket.updateQuestInfo(1111, 1, ""));
            SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            sdfGMT.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
            String timeStr = sdfGMT.format(Calendar.getInstance().getTime()).replaceAll("-", "");
            c.getSession().write(FarmPacket.updateQuestInfo(1111, 2, timeStr));
            System.out.println(timeStr);
            c.getSession().write(FarmPacket.alertQuest(1111, 0));
            c.getSession().write(FarmPacket.updateQuestInfo(1112, 0, "A1/"));
            c.getSession().write(FarmPacket.updateQuestInfo(1112, 1, "A1/Z/"));
        }
    }

    public static void placeBuilding(LittleEndianAccessor slea, MapleClient c) {
        int position = slea.readInt();
        int itemId = slea.readInt();
        slea.readByte(); //idk
        if (itemId / 10000 < 112 || itemId / 10000 > 114) {
            return;
        }
        if (position > (25 * 25) - 1) { //biggest farm 25x25
            return;
        }
        int size = (itemId / 10000) % 10;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!c.getFarm().checkSpace(size, position - j - i)) {
                    return;
                }
            }
        }
        //c.getFarm().getFarmInventory().updateItemQuantity(itemId, -1);
        //c.getFarm().gainAestheticPoints(aesthetic); //rewarded from building
    }

    public static void harvest(LittleEndianAccessor slea, MapleClient c) {
        slea.readInt(); //position
        //c.getFarm().getFarmInventory().updateItemQuantity(oid, -1);
        //c.getFarm().gainAestheticPoints(aesthetic); //rewarded from building
    }

    public static void buy(LittleEndianAccessor slea, MapleClient c) {
        int itemId = slea.readInt();
        //c.getFarm().getFarmInventory().gainWaru(-price);
        //c.getFarm().getFarmInventory().updateItemQuantity(itemId, 1);
        //c.getFarm().gainAestheticPoints(aesthetic); //rewarded from building
    }

    public static void useItem(LittleEndianAccessor slea, MapleClient c) {
        int itemId = slea.readInt();
        //c.getFarm().getFarmInventory().updateItemQuantity(itemId, -1);
        //c.getFarm().gainAestheticPoints(aesthetic); //rewarded from building
    }

    public static void renameMonster(LittleEndianAccessor slea, MapleClient c) {
        int monsterIndex = slea.readInt();
        String name = slea.readMapleAsciiString();
        //c.getFarm().getMonster(monsterIndex).setName(name);
        c.getSession().write(FarmPacket.renameMonster(monsterIndex, name));
    }

    public static void nurtureMonster(LittleEndianAccessor slea, MapleClient c) {
        int monsterIndex = slea.readInt();
    }

    public static void firstEntryReward(LittleEndianAccessor slea, MapleClient c) {
        //give random waru consume item
        c.getSession().write(FarmPacket.farmMessage("Find your reward for logging in today \r\nin your inventory."));
    }

    public static void checkQuestStatus(LittleEndianAccessor slea, MapleClient c) {
        int farmId = slea.readInt();
        //TODO code farm quests
        if (c.getFarm().getName().equals("Creating...")) {
            //c.getSession().write(FarmPacket.updateQuestInfo(1111, 1, "A1/Z/"));
        }
    }
}
