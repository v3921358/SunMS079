package scripting;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.MapleStat;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.ItemLoader;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import custom.SearchGenerator;
import custom.CustomPlayerRankings;
import database.DBConPool;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.HiredMerchantHandler;
import handling.channel.handler.InventoryHandler;
import handling.channel.handler.PlayersHandler;
import handling.login.LoginInformationProvider;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.exped.ExpeditionType;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.script.Invocable;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import server.MapleCarnivalChallenge;
import server.MapleCarnivalParty;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleSlideMenu;
import server.MapleSquad;
import server.MapleStatEffect;
import server.Randomizer;
import server.MapleSlideMenu.SlideMenu0;
import server.MapleSlideMenu.SlideMenu1;
import server.MapleSlideMenu.SlideMenu2;
import server.MapleSlideMenu.SlideMenu3;
import server.MapleSlideMenu.SlideMenu4;
import server.MapleSlideMenu.SlideMenu5;
import server.MerchItemPackage;
import server.SpeedRunner;
import server.StructItemOption;
import server.Timer;
import server.Timer.CloneTimer;
import server.life.*;
import server.maps.Event_DojoAgent;
import server.maps.Event_PyramidSubway;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import server.stores.IMaplePlayerShop;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CField.NPCPacket;
import tools.packet.CField.UIPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.GuildPacket;
import tools.packet.CWvsContext.InfoPacket;
import tools.packet.PlayerShopPacket;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private String getText;
    private final byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private final Invocable iv;
    private int mod;

    public NPCConversationManager(MapleClient c, int npc, int questid, String npcscript, byte type, Invocable iv, int mod) {
        super(c, npc, questid, npcscript);
        this.type = type;
        this.iv = iv;
        this.mod = mod;
    }

    public Invocable getIv() {
        return iv;
    }

    public int getNpc() {
        return id;
    }
    
    public int getMod() {
        return mod;
    }

    public int getQuest() {
        return id2;
    }

    public String getScript() {
        return script;
    }

    public byte getType() {
        return type;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void sendSlideMenu(final int type, final String sel) {
        if (lastMsg > -1) {
            return;
        }
        int lasticon = 0;
        c.getSession().write(NPCPacket.getSlideMenu(id, type, lasticon, sel));
        lastMsg = 0x11;//was12
    }

    public String getDimensionalMirror(MapleCharacter character) {
        return MapleSlideMenu.SlideMenu0.getSelectionInfo(character, id);
    }

    public String getSlideMenuSelection(int type) {
        switch (type) {
            case 0:
                return SlideMenu0.getSelectionInfo(getPlayer(), id);
            case 1:
                return SlideMenu1.getSelectionInfo(getPlayer(), id);
            case 2:
                return SlideMenu2.getSelectionInfo(getPlayer(), id);
            case 3:
                return SlideMenu3.getSelectionInfo(getPlayer(), id);
            case 4:
                return SlideMenu4.getSelectionInfo(getPlayer(), id);
            case 5:
                return SlideMenu5.getSelectionInfo(getPlayer(), id);
            default:
                return SlideMenu0.getSelectionInfo(getPlayer(), id);
        }
    }

    public int getSlideMenuDataInteger(int type) {
        switch (type) {
            case 0:
                return SlideMenu0.getDataInteger(type);
            case 1:
                return SlideMenu1.getDataInteger(type);
            case 2:
                return SlideMenu2.getDataInteger(type);
            case 3:
                return SlideMenu3.getDataInteger(type);
            case 4:
                return SlideMenu4.getDataInteger(type);
            case 5:
                return SlideMenu5.getDataInteger(type);
            default:
                return SlideMenu0.getDataInteger(type);
        }
    }

    public void sendNext(String text) {
        sendNext(text, id);
    }

    public void sendNext(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendPlayerToNpc(String text) {
        sendNextS(text, (byte) 3, id);
    }

    public void sendNextNoESC(String text) {
        sendNextS(text, (byte) 1, id);
    }

    public void sendNextNoESC(String text, int id) {
        sendNextS(text, (byte) 1, id);
    }

    public void sendNextS(String text, byte type) {
        sendNextS(text, type, id);
    }

    public void sendNextS(String text, byte type, int idd) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", type, idd));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        sendPrev(text, id);
    }

    public void sendPrev(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        sendPrevS(text, type, id);
    }

    public final void DisableUI(final boolean enabled) {
        c.getSession().write(UIPacket.IntroDisableUI(enabled));
    }

    public void sendPrevS(String text, byte type, int idd) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", type, idd));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        sendNextPrev(text, id);
    }

    public void sendNextPrev(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        sendNextPrevS(text, type, id);
    }

    public void sendNextPrevS(String text, byte type, int idd) {
        sendNextPrevS(text, type, idd, id);
    }

    public void sendNextPrevS(String text, byte type, int idd, int npcid) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(npcid, (byte) 0, text, "01 01", type, idd));
        lastMsg = 0;
    }

    public void sendOk(String text) {
        sendOk(text, id);
    }

    public void sendOk(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        sendOkS(text, type, id);
    }

    public void sendOkS(String text, byte type, int idd) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", type, idd));
        lastMsg = 0;
    }

//    public void sendSelfTalk(String text, byte type) {
//        c.getSession().write(NPCPacket.getSelfTalkText(text));
//    }
    public void sendSelfTalk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getSelfTalkText(text));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        sendYesNo(text, id);
    }

    public void sendYesNo(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    public void sendYesNoS(String text, byte type) {
        sendYesNoS(text, type, id);
    }

    public void sendYesNoS(String text, byte type, int idd) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 1, text, "", type, idd));
        lastMsg = 1;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        askAcceptDecline(text, id);
    }

    public void askAcceptDecline(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        lastMsg = (byte) 0x0B;//was 0xF
        c.getSession().write(NPCPacket.getNPCTalk(id, lastMsg, text, "", (byte) 0));
    }

    public void askAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text, id);
    }

    public void askAcceptDeclineNoESC(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        lastMsg = (byte) 0x0C;
        c.getSession().write(NPCPacket.getNPCTalk(id, lastMsg, text, "", (byte) 1));
    }

    public void askAvatar(String text, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, args));
        lastMsg = 7;
    }

    public void sendSimple(String text) {
        sendSimple(text, id);
    }

    public void sendSimple(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 4, text, "", (byte) 0));
        lastMsg = 4;
    }

    public void sendSimpleS(String text, byte type) {
        sendSimpleS(text, type, id);
    }

    public void sendSimpleS(String text, byte type, int idd) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 5, text, "", type, idd));
        lastMsg = 4;
    }

    public void sendStyle(String text, int caid, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, caid, styles));
        lastMsg = 7;
    }

    public void sendStyle(String text, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, styles));
        lastMsg = 7;
    }

    public void sendSecondStyle(String text, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, styles));
        lastMsg = 7;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkNum(id, text, def, min, max));
        lastMsg = 3;
    }

    public void sendGetText(String text) {
        sendGetText(text, id);
    }

    public void sendGetText(String text, int id) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkText(id, text));
        lastMsg = 2;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public void setHair(int hair) {
        if (hairExists(hair)) {
            getPlayer().setHair(hair);
            getPlayer().updateSingleStat(MapleStat.HAIR, hair);
            getPlayer().equipChanged();
        }
    }

    public void setFace(int face) {
        if (faceExists(face)) {
            getPlayer().setFace(face);
            getPlayer().updateSingleStat(MapleStat.FACE, face);
            getPlayer().equipChanged();
        }
    }

    public void setHp(int Hp) {
        getPlayer().setHp(Hp);
        getPlayer().updateSingleStat(MapleStat.HP, Hp);
        c.getSession().write(CWvsContext.enableActions());

    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public static boolean hairExists(int hair) {
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Hair"));
        final MapleDataDirectoryEntry root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (id == hair) {
                return true;
            }
        }
        return false;
    }

    public static boolean faceExists(int face) {
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz/Face"));
        final MapleDataDirectoryEntry root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 8));
            if (id == face) {
                return true;
            }
        }
        return false;
    }

    public static boolean itemExists(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (Pair<Integer, String> item : ii.getAllItems2()) {
            if (item.getLeft() == itemId) {
                return true;
            }
        }
        return false;
    }

    public int setRandomAvatar(int ticket, int... args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, id);
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public void openShopNPC(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c, this.id);
    }

    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName());
    }

    public int gainGachaponItem(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0) {
                World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : got a(n)", item, rareness, msg));
            }
            c.getSession().write(InfoPacket.getShowItemGain(item.getItemId(), (short) quantity, true));
            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    public int useNebuliteGachapon() {
        try {
            if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < 1) {
                return -1;
            }
            int grade; // Default D
            final int chance = Randomizer.nextInt(100); // cannot gacha S, only from alien cube.
            if (chance < 1) { // Grade A
                grade = 3;
            } else if (chance < 3) { // Grade B
                grade = 2;
            } else if (chance < 40) { // Grade C
                grade = 1;
            } else { // grade == 0
                grade = Randomizer.nextInt(100) < 25 ? 5 : 0; // 25% again to get premium ticket piece				
            }
            int newId = 0;
            if (grade == 5) {
                newId = 4420000;
            } else {
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final List<StructItemOption> pots = new LinkedList<>(ii.getAllSocketInfo(grade).values());
                while (newId == 0) {
                    StructItemOption pot = pots.get(Randomizer.nextInt(pots.size()));
                    if (pot != null) {
                        newId = pot.opID;
                    }
                }
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, newId, (short) 1);
            if (item == null) {
                return -1;
            }
            if (grade >= 3 && grade != 5) {
                World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : got a(n)", item, (byte) 0, "Maple World"));
            }
            c.getSession().write(InfoPacket.getShowItemGain(newId, (short) 1, true));
            gainItem(2430748, (short) 1);
            gainItemSilent(5220094, (short) -1);
            return item.getItemId();
        } catch (Exception e) {
            System.out.println("[Error] Failed to use Nebulite Gachapon. " + e);
        }
        return -1;
    }

    public void changeJob(short job) {
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int idd) {
        MapleQuest.getInstance(idd).start(getPlayer(), id);
    }

    public void completeQuest(int idd) {
        MapleQuest.getInstance(idd).complete(getPlayer(), id);
    }

    public void forfeitQuest(int idd) {
        MapleQuest.getInstance(idd).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(id2).forceStart(getPlayer(), getNpc(), null);
    }

    @Override
    public void forceStartQuest(int idd) {
        MapleQuest.getInstance(idd).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(id2).forceStart(getPlayer(), getNpc(), customData);
    }

    public void completeQuest() {
        forceCompleteQuest();
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(id2).forceComplete(getPlayer(), getNpc());
    }

    @Override
    public void forceCompleteQuest(final int idd) {
        MapleQuest.getInstance(idd).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).getCustomData();
    }

    public String getQuestCustomData(int quest) {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(quest)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).setCustomData(customData);
    }

    public long getMeso() {
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<>();
        for (Item item : equipped.newList()) {
            ids.add(item.getPosition());
        }
        for (short itemid : ids) {
            MapleInventoryManipulator.unequip(getC(), itemid, equip.getNextFreeSlot());
        }
    }

    public final void clearSkills() {
        final Map<Skill, SkillEntry> skills = new HashMap<>(getPlayer().getSkills());
        final Map<Skill, SkillEntry> newList = new HashMap<>();
        for (Entry<Skill, SkillEntry> skill : skills.entrySet()) {
            newList.put(skill.getKey(), new SkillEntry((byte) 0, (byte) 0, -1));
        }
        getPlayer().changeSkillsLevel(newList);
        newList.clear();
        skills.clear();
    }

    public boolean hasSkill(int skillid) {
        Skill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void showEffect(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(CField.showEffect(effect));
        } else {
            c.getSession().write(CField.showEffect(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(CField.playSound(sound));
        } else {
            c.getSession().write(CField.playSound(sound));
        }
    }

    public void environmentChange(boolean broadcast, String env) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(CField.environmentChange(env, 2));
        } else {
            c.getSession().write(CField.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        int inMap = 0;
        if (getPlayer().getParty() == null) {
            return inMap;
        }
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() != null && char2.getParty().getId() == getPlayer().getParty().getId()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        if (getPlayer().getParty() == null) {
            warp(mapId, 0);
            gainExp(exp);
            return;
        }
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        if (getPlayer().getParty() == null) {
            warp(mapId, 0);
            gainExp(exp);
            gainMeso(meso);
            return;
        }
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerExpedition(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();
                map.broadcastMessage(CField.getClock(minutes * 60));
                map.broadcastMessage(CWvsContext.broadcastMsg(-6, startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();
                map.broadcastMessage(CField.getClock(minutes * 60));
                map.broadcastMessage(CWvsContext.broadcastMsg(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {
        try {
            final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
            if (squad == null) {
                return false;
            }
            if (type_ == 0 || type_ == 3) { // Normal viewing
                sendNext(squad.getSquadMemberString(type_));
            } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
                sendSimple(squad.getSquadMemberString(type_));
            } else if (type_ == 2) {
                if (squad.getBannedMemberSize() > 0) {
                    sendSimple(squad.getSquadMemberString(type_));
                } else {
                    sendNext(squad.getSquadMemberString(type_));
                }
            }
            return true;
        } catch (NullPointerException ex) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            return false;
        }
    }
//public void teachSkill(int id, int skillevel, byte masterlevel, long expiration) {
    //  getPlayer().changeSkillLevelAll(SkillFactory.getSkill(id), skillevel, masterlevel, expiration);
    //}

    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public int addMember(String type, boolean join) {
        try {
            final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
            if (squad != null) {
                return squad.addMember(c.getPlayer(), join);
            }
            return -1;
        } catch (NullPointerException ex) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            return -1;
        }
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getMembers().contains(c.getPlayer())) {
            return 1;
        } else if (squad.isBanned(c.getPlayer())) {
            return 2;
        } else {
            return 0;
        }
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {
        c.getSession().write(GuildPacket.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity(boolean trueMax) {
        if (c.getPlayer().getMeso() < 500000 && !trueMax) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "你没有足够的金币"));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        if (World.Guild.increaseGuildCapacity(gid, trueMax)) {
            if (!trueMax) {
                c.getPlayer().gainMeso(-500000, true, true);
            } else {
                gainGP(-25000);
            }
//            sendNext("你的家族人数上限提高了...");
        } else if (!trueMax) {
            sendNext("请检查是否你的家族人数上限已达到最大限制. (最大上限: 100)");
        } else {
            sendNext("请检查是否你的家族人数上限已达到最大限制, 如果你家族的GP减少，将下降家族等级. (最大上限: 200)");
        }
    }

    public void displayGuildRanks() {
        c.getSession().write(GuildPacket.showGuildRanks(id, MapleGuildRanking.getInstance().getRank()));
    }

    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        return c.getPlayer().getEventInstance() != null;
    }

    public void makeTaintedEquip(byte slot) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        sel.setStr((short) 69);
        sel.setDex((short) 69);
        sel.setInt((short) 69);
        sel.setLuk((short) 69);
        sel.setHp((short) 69);
        sel.setMp((short) 69);
        sel.setWatk((short) 69);
        sel.setMatk((short) 69);
        sel.setWdef((short) 69);
        sel.setMdef((short) 69);
        sel.setAcc((short) 69);
        sel.setAvoid((short) 69);
        sel.setHands((short) 69);
        sel.setSpeed((short) 69);
        sel.setJump((short) 69);
        sel.setUpgradeSlots((byte) 69);
        sel.setViciousHammer((byte) 69);
        sel.setEnhance((byte) 69);
        c.getPlayer().equipChanged();
        c.getPlayer().fakeRelog();
    }

    public void changeStat(byte slot, int type, int amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr((short) amount);
                break;
            case 1:
                sel.setDex((short) amount);
                break;
            case 2:
                sel.setInt((short) amount);
                break;
            case 3:
                sel.setLuk((short) amount);
                break;
            case 4:
                sel.setHp((short) amount);
                break;
            case 5:
                sel.setMp((short) amount);
                break;
            case 6:
                sel.setWatk((short) amount);
                break;
            case 7:
                sel.setMatk((short) amount);
                break;
            case 8:
                sel.setWdef((short) amount);
                break;
            case 9:
                sel.setMdef((short) amount);
                break;
            case 10:
                sel.setAcc((short) amount);
                break;
            case 11:
                sel.setAvoid((short) amount);
                break;
            case 12:
                sel.setHands((short) amount);
                break;
            case 13:
                sel.setSpeed((short) amount);
                break;
            case 14:
                sel.setJump((short) amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setBonusPotential1(amount);
                break;
            case 23:
                sel.setBonusPotential2(amount);
                break;
            case 24:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
        c.getPlayer().fakeRelog();
    }

    public void openPackageDeliverer() {
        c.getPlayer().setConversation(2);
        c.getSession().write(CField.sendPackageMSG((byte) 9, null));
    }

    public void openMerchantItemStore() {
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getAccountID());
        c.getPlayer().setConversation(3);
        boolean merch = World.hasMerchant(c.getPlayer().getAccountID(), c.getPlayer().getId());
        if (merch) {
            c.getSession().write(PlayerShopPacket.ShowMerchItemStore(9030000, World.getMerchantMap(c.getPlayer()), World.getMerchantChannel(c.getPlayer())));
            c.getPlayer().setConversation(0);
        } else if (pack == null) {
            c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x25, 0, 0));
            c.getPlayer().setConversation(0);
        } else {
            c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
        }
        c.getSession().write(CWvsContext.enableActions());
    }

    public void sendPVPWindow() {
        c.getSession().write(UIPacket.openUI(0x32));
        c.getSession().write(CField.sendPVPMaps());
    }

    public void sendAzwanWindow() {
        c.getSession().write(UIPacket.openUI(0x46));
    }

    public void sendOpenJobChangeUI() {
        c.getSession().write(UIPacket.openUI(0xA4)); // job selections change depending on ur job
    }

    public void sendTimeGateWindow() {
        c.getSession().write(UIPacket.openUI(0xA8));
    }

    public void sendRepairWindow() {
        c.getSession().write(UIPacket.sendRepairWindow(id));
    }

    public void sendJewelCraftWindow() {
        c.getSession().write(UIPacket.sendJewelCraftWindow(id));
    }

    public void sendRedLeaf(boolean viewonly, boolean autocheck) {
        if (autocheck) {
            viewonly = c.getPlayer().getFriendShipToAdd() == 0;
        }
        c.getSession().write(UIPacket.sendRedLeaf(viewonly ? 0 : c.getPlayer().getFriendShipToAdd(), viewonly));
    }

    public void sendProfessionWindow() {
        c.getSession().write(UIPacket.openUI(42));
    }

    public void sendQuestWindow() {
        c.getSession().write(UIPacket.openUI(6));
    }

    public void sendBeastTamerGiftWindow() {
        c.getSession().write(UIPacket.openUI(194));
    }

    public void sendCassandrasCollectionWindow() {
        c.getSession().write(UIPacket.openUI(127));
    }

    public void sendLuckyLuckyMonstoryWindow() {
        c.getSession().write(UIPacket.openUI(120));
    }

    public void OpenUI(int ui) {
        c.getPlayer().getMap().broadcastMessage(UIPacket.openUI(ui));
    }

    public void getMulungRanking() {
        c.getSession().write(CWvsContext.getMulungRanking());
    }

    public final int getDojoPoints() {
        return dojo_getPts();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getIntNoRecord(GameConstants.DOJO_RECORD);
    }

    public void setDojoRecord(final boolean reset, final boolean take, int amount) {
        if (reset) {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData("0");
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO)).setCustomData("0");
        } else if (take) {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) - amount));
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) - amount));
        } else {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) + 1));
        }
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party, final int mapid) {
        return start_DojoAgent(dojo, party);
    }

    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {
        return c.getChannelServer().getFireWorks().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {
        c.getChannelServer().getFireWorks().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return c.getChannelServer().getFireWorks().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        c.getChannelServer().getFireWorks().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return c.getChannelServer().getFireWorks().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            c.getChannelServer().getFireWorks().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
        }
    }

    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    public void maxStats() {
        Map<MapleStat, Integer> statup = new EnumMap<>(MapleStat.class);
        c.getPlayer().getStat().str = (short) 999;
        c.getPlayer().getStat().dex = (short) 999;
        c.getPlayer().getStat().int_ = (short) 999;
        c.getPlayer().getStat().luk = (short) 999;

        int overrDemon = 30000;

        c.getPlayer().getStat().maxhp = 30000;
        c.getPlayer().getStat().maxmp = overrDemon;
        c.getPlayer().getStat().setHp(30000, c.getPlayer());
        c.getPlayer().getStat().setMp(overrDemon, c.getPlayer());

        statup.put(MapleStat.STR,
                999);
        statup.put(MapleStat.DEX,
                999);
        statup.put(MapleStat.LUK,
                999);
        statup.put(MapleStat.INT,
                999);
        statup.put(MapleStat.HP,
                30000);
        statup.put(MapleStat.MAXHP,
                30000);
        statup.put(MapleStat.MP, overrDemon);

        statup.put(MapleStat.MAXMP, overrDemon);

        c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        //c.getSession().write(CWvsContext.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public int setAndroid(int args) {
        if (args < 30000) {
            c.getPlayer().getAndroid().setFace(args);
            c.getPlayer().getAndroid().saveToDb();
        } else {
            c.getPlayer().getAndroid().setHair(args);
            c.getPlayer().getAndroid().saveToDb();
        }
        CField.updateAndroidLook(false, c.getPlayer(), c.getPlayer().getAndroid());
        c.getPlayer().setAndroid(c.getPlayer().getAndroid()); //Respawn it
        c.getPlayer().equipChanged();
        return 1;
    }

    public int getAndroidStat(final String type) {
        switch (type) {
            case "HAIR":
                return c.getPlayer().getAndroid().getHair();
            case "FACE":
                return c.getPlayer().getAndroid().getFace();
            case "GENDER":
                int itemid = c.getPlayer().getAndroid().getItemId();
                if (itemid == 1662000 || itemid == 1662002) {
                    return 0;
                } else {
                    return 1;
                }
        }
        return -1;
    }

    public void reloadChar() {
        getPlayer().getClient().getSession().write(CField.getCharInfo(getPlayer()));
        getPlayer().getMap().removePlayer(getPlayer());
        getPlayer().getMap().addPlayer(getPlayer());
    }

    @Override
    public MapleCharacter getChar() {
        return getPlayer();
    }

    public static int editEquipById(MapleCharacter chr, int max, int itemid, String stat, int newval) {
        return editEquipById(chr, max, itemid, stat, (short) newval);
    }

    public static int editEquipById(MapleCharacter chr, int max, int itemid, String stat, short newval) {
        // Is it an equip?
        if (!MapleItemInformationProvider.getInstance().isEquip(itemid)) {
            return -1;
        }

        // Get List
        List<Item> equips = chr.getInventory(MapleInventoryType.EQUIP).listById(itemid);
        List<Item> equipped = chr.getInventory(MapleInventoryType.EQUIPPED).listById(itemid);

        // Do you have any?
        if (equips.isEmpty() && equipped.isEmpty()) {
            return 0;
        }

        int edited = 0;

        // edit items
        for (Item itm : equips) {
            Equip e = (Equip) itm;
            if (edited >= max) {
                break;
            }
            edited++;
            switch (stat) {
                case "str":
                    e.setStr(newval);
                    break;
                case "dex":
                    e.setDex(newval);
                    break;
                case "int":
                    e.setInt(newval);
                    break;
                case "luk":
                    e.setLuk(newval);
                    break;
                case "watk":
                    e.setWatk(newval);
                    break;
                case "matk":
                    e.setMatk(newval);
                    break;
                default:
                    return -2;
            }
        }
        for (Item itm : equipped) {
            Equip e = (Equip) itm;
            if (edited >= max) {
                break;
            }
            edited++;
            switch (stat) {
                case "str":
                    e.setStr(newval);
                    break;
                case "dex":
                    e.setDex(newval);
                    break;
                case "int":
                    e.setInt(newval);
                    break;
                case "luk":
                    e.setLuk(newval);
                    break;
                case "watk":
                    e.setWatk(newval);
                    break;
                case "matk":
                    e.setMatk(newval);
                    break;
                default:
                    return -2;
            }
        }

        // Return items edited
        return (edited);
    }

    public int getReborns() { // tjat
        return getPlayer().getReborns();
    }

    public Triple<String, Map<Integer, String>, Long> getSpeedRun(String typ) {
        final ExpeditionType expedtype = ExpeditionType.valueOf(typ);
        if (SpeedRunner.getSpeedRunData(expedtype) != null) {
            return SpeedRunner.getSpeedRunData(expedtype);
        }
        return new Triple<String, Map<Integer, String>, Long>("", new HashMap<Integer, String>(), 0L);
    }

    public boolean getSR(Triple<String, Map<Integer, String>, Long> ma, int sel) {
        if (ma.mid.get(sel) == null || ma.mid.get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.mid.get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof Item) {
            final Item it = (Item) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public int getVPoints() {
        return getPlayer().getVPoints();
    }

    public void setVPoints(int vpoints) {
        getPlayer().setVPoints(getPlayer().getVPoints() + vpoints);
    }

    public int getDPoints() {
        return getPlayer().getVPoints();
    }

    public void setDPoints(int dpoints) {
        getPlayer().setDPoints(getPlayer().getDPoints() + dpoints);
    }

    public int getEPoints() {
        return getPlayer().getEPoints();
    }

    public void setEPoints(int epoints) {
        getPlayer().setEPoints(getPlayer().getEPoints() + epoints);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        Item item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof Item) {
            item = (Item) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
                if (eq.getExpiration() == -1) {
                    eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
                } else {
                    eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADABLE.getValue()));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration(eq.getExpiration() + offset);
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public int getTotalStat(final int itemId) {
        return MapleItemInformationProvider.getInstance().getTotalStat((Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId));
    }

    public int getReqLevel(final int itemId) {
        return MapleItemInformationProvider.getInstance().getReqLevel(itemId);
    }

    public MapleStatEffect getEffect(int buff) {
        return MapleItemInformationProvider.getInstance().getItemEffect(buff);
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "你的家族得到了一个 " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public final void maxAllSkills() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.getId() < 90000000) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void maxSkillsByJob() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob()) && !skil.isInvisible()) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void removeSkillsByJob() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob())) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) -1, (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void maxSkillsByJobId(int jobid) {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob()) && skil.getId() >= jobid * 1000000 && skil.getId() < (jobid + 1) * 1000000 && !skil.isInvisible()) {
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public void killAllMonsters(int mapid) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        map.killAllMonsters(false); // No drop.
    }

    public final void killAllMobs() {
        c.getPlayer().getMap().killAllMonsters(true);
    }

    public final void levelUp() {
        c.getPlayer().levelUp();
    }

    public void cleardrops() {
        MapleMonsterInformationProvider.getInstance().clearDrops();
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        List<Integer> list = new ArrayList<>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
        Collections.sort(list);
        return list;
    }

    public final List<Integer> getAllPotentialInfoSearch(String content) {
        List<Integer> list = new ArrayList<>();
        for (Entry<Integer, List<StructItemOption>> i : MapleItemInformationProvider.getInstance().getAllPotentialInfo().entrySet()) {
            for (StructItemOption ii : i.getValue()) {
                if (ii.toString().contains(content)) {
                    list.add(i.getKey());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    public void MakeGMItem(byte slot, MapleCharacter player) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, slot, (byte) 0);
        nItem.setStr((short) 32767); // STR
        nItem.setDex((short) 32767); // DEX
        nItem.setInt((short) 32767); // INT
        nItem.setLuk((short) 32767); //LUK
        nItem.setUpgradeSlots((byte) 0);
        nItem.setHands(hand);
        nItem.setLevel(level);
        player.getInventory(MapleInventoryType.EQUIP).removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public final String getPotentialInfo(final int id) {
        final List<StructItemOption> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructItemOption item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.get(potInfo.toString()));
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.getSession().write(CField.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        final MapleCharacter player = getPlayer();
        getMap().broadcastMessage(CWvsContext.yellowChat(player.getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        CloneTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (chr == null || player == null) {
                    warpMap(680000500, 0);
                } else {
                    chr.getMap().broadcastMessage(CWvsContext.yellowChat(chr.getName() + ", do you take " + player.getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (chr == null || player == null) {
                    if (player != null) {
                        setQuestRecord(player, 160001, "3");
                        setQuestRecord(player, 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(player, 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(player.getName() + " and " + chr.getName() + ", I wish you two all the best on your " + chr.getClient().getChannelServer().getServerName() + " journey together!", 9201002);
                    chr.getMap().startExtendedMapEffect("You may now kiss the bride, " + player.getName() + "!", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), CWvsContext.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), CWvsContext.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (player.getGuildId() > 0) {
                        World.Guild.guildPacket(player.getGuildId(), CWvsContext.sendMarriage(false, player.getName()));
                    }
                    if (player.getFamilyId() > 0) {
                        World.Family.familyPacket(player.getFamilyId(), CWvsContext.sendMarriage(true, chr.getName()), player.getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec

    }

    public void putKey(int key, int type, int action) {
        getPlayer().changeKeybinding(key, (byte) type, action);
        getClient().getSession().write(CField.getKeymap(getPlayer().getKeyLayout()));
    }

    public void doRing(final String name, final int itemid) {
        PlayersHandler.DoRing(getClient(), name, itemid);
    }

    public int getNaturalStats(final int itemid, final String it) {
        Map<String, Integer> eqStats = MapleItemInformationProvider.getInstance().getEquipStats(itemid);
        if (eqStats != null && eqStats.containsKey(it)) {
            return eqStats.get(it);
        }
        return 0;
    }

    public boolean isEligibleName(String t) {
        return MapleCharacterUtil.canCreateChar(t, getPlayer().isGM()) && (!LoginInformationProvider.getInstance().isForbiddenName(t) || getPlayer().isGM());
    }

    public String checkDrop(MapleCharacter chr, int mobId) {
        final List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if (ranks != null && ranks.size() > 0) {
            int num = 0;
            int itemId;
            int ch;
            MonsterDropEntry de;
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                de = ranks.get(i);
                if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    itemId = de.itemId;
                    if (num == 0) {
                        name.append("Drops for #o").append(mobId).append("#\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = "#z" + itemId + "#";
                    if (itemId == 0) { //meso
                        itemId = 4031041; //display sack of cash
                        namez = (de.Minimum * getClient().getChannelServer().getMesoRate(chr.getWorld())) + " to " + (de.Maximum * getClient().getChannelServer().getMesoRate(chr.getWorld())) + " meso";
                    }
                    ch = de.chance * getClient().getChannelServer().getDropRate(chr.getWorld());
                    name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0).append("% chance. ").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("Requires quest " + MapleQuest.getInstance(de.questid).getName() + " to be started.") : "").append("\r\n");
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }

        }
        return "No drops was returned.";
    }

    public String getLeftPadded(final String in, final char padchar, final int length) {
        return StringUtil.getLeftPaddedStr(in, padchar, length);
    }

    public void handleDivorce() {
        if (getPlayer().getMarriageId() <= 0) {
            sendNext("请确保你有一个婚姻.");
            return;
        }
        final int chz = World.Find.findChannel(getPlayer().getMarriageId());
        if (chz == -1) {
            //sql queries
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                PreparedStatement ps = con.prepareStatement("UPDATE queststatus SET customData = ? WHERE characterid = ? AND (quest = ? OR quest = ?)");
                ps.setString(1, "0");
                ps.setInt(2, getPlayer().getMarriageId());
                ps.setInt(3, 160001);
                ps.setInt(4, 160002);
                ps.executeUpdate();
                ps.close();

                ps = con.prepareStatement("UPDATE characters SET marriageid = ? WHERE id = ?");
                ps.setInt(1, 0);
                ps.setInt(2, getPlayer().getMarriageId());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.err.println("handleDivorce" + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                outputFileError(e);
                return;
            }
            setQuestRecord(getPlayer(), 160001, "0");
            setQuestRecord(getPlayer(), 160002, "0");
            getPlayer().setMarriageId(0);
            sendNext("你已经成功离婚了..");
            return;
        } else if (chz < -1) {
            sendNext("请确认您的伴侣已登录.");
            return;
        }
        MapleCharacter cPlayer = ChannelServer.getInstance(chz).getPlayerStorage().getCharacterById(getPlayer().getMarriageId());
        if (cPlayer != null) {
            cPlayer.dropMessage(1, "你的伴侣已经和你离婚了.");
            cPlayer.setMarriageId(0);
            setQuestRecord(cPlayer, 160001, "0");
            setQuestRecord(getPlayer(), 160001, "0");
            setQuestRecord(cPlayer, 160002, "0");
            setQuestRecord(getPlayer(), 160002, "0");
            getPlayer().setMarriageId(0);
            sendNext("你已经成功离婚了...");
        } else {
            sendNext("发生错误...");
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public void sendUltimateExplorer() {
        getClient().getSession().write(CWvsContext.ultimateExplorer());
    }

    public void sendPendant(boolean b) {
        c.getSession().write(CWvsContext.pendantSlot(b));
    }

    public Triple<Integer, Integer, Integer> getCompensation() {
        Triple<Integer, Integer, Integer> ret = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM compensationlog_confirmed WHERE chrname LIKE ?")) {
                ps.setString(1, getPlayer().getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ret = new Triple<>(rs.getInt("value"), rs.getInt("taken"), rs.getInt("donor"));
                    }
                }
            }
            return ret;
        } catch (SQLException e) {
            System.err.println("getCompensation()" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return ret;
        }
    }

    public boolean deleteCompensation(int taken) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE compensationlog_confirmed SET taken = ? WHERE chrname LIKE ?")) {
                ps.setInt(1, taken);
                ps.setString(2, getPlayer().getName());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("deleteCompensation" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return false;
        }
    }

    /*
     * Start of Custom Features
     */
    public void gainAPS(int gain) {
        getPlayer().gainAPS(gain);
    }

    /*
     * End of Custom Features
     */

    public void changeJobById(short job) {
        c.getPlayer().changeJob(job);
    }

    public int getJobId() {
        return getPlayer().getJob();
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public int getEquipId(byte slot) {
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        return equip.getItem(slot).getItemId();
    }

    public int getUseId(byte slot) {
        MapleInventory use = getPlayer().getInventory(MapleInventoryType.USE);
        return use.getItem(slot).getItemId();
    }

    public int getSetupId(byte slot) {
        MapleInventory setup = getPlayer().getInventory(MapleInventoryType.SETUP);
        return setup.getItem(slot).getItemId();
    }

    public int getCashId(byte slot) {
        MapleInventory cash = getPlayer().getInventory(MapleInventoryType.CASH);
        return cash.getItem(slot).getItemId();
    }

    public int getETCId(byte slot) {
        MapleInventory etc = getPlayer().getInventory(MapleInventoryType.ETC);
        return etc.getItem(slot).getItemId();
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<>();
        for (Item item : equip.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<>();
        for (Item item : use.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<>();
        for (Item item : cash.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<>();
        for (Item item : etc.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<>();
        for (Item item : setup.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String PotentialedEquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<>();
        for (Item item : equip.list()) {
            Equip eq = (Equip) item;
            if (eq.getBonusPotential1() != 0) {
                stra.add("\r\n#L" + item.getPosition() + "##v" + item.getItemId() + "# - " + (eq.getBonusPotential2() != 0 ? 2 : 1) + " additional potential lines #l");
            }
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String EquipPotentialList(short slot) {
        Equip equip = (Equip) getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        StringBuilder sb = new StringBuilder();
        int[] potentials;
        potentials = new int[]{equip.getPotential1(), equip.getPotential2(), equip.getPotential3()};
        for (int i : potentials) {
            StructItemOption op = MapleItemInformationProvider.getInstance().getPotentialInfo(equip.getPotential1()).get(MapleItemInformationProvider.getInstance().getReqLevel(equip.getItemId()) / 10);
            sb.append("\r\nPotential ").append(i).append(" - ").append(op.toString());
        }
        return sb.toString();
    }

    public void wearEquip(int itemid, byte slot) {
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
        final MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        Item item = li.getEquipById(itemid);
        item.setPosition(slot);
        equip.addFromDB(item);
    }

    public void showFredrick() {
        HiredMerchantHandler.showFredrick(c);
    }

    public String searchId(int type, String search) {
        return MapleInventoryManipulator.searchId(type, search);
    }

    public int parseInt(String s) {
        return Integer.parseInt(s);
    }

    public byte parseByte(String s) {
        return Byte.parseByte(s);
    }

    public short parseShort(String s) {
        return Short.parseShort(s);
    }

    public long parseLong(String s) {
        return Long.parseLong(s);
    }

    public void getEventEnvelope(int questid, int time) {
        CWvsContext.getEventEnvelope(questid, time);
    }

    public void write(Object o) {
        c.getSession().write(o);
    }

    public void openUIOption(int type) {
        CField.UIPacket.openUIOption(type, id);
    }

    public void showHilla() {
        try {
            c.getSession().write(CField.MapEff("phantom/hillah"));
            MapleNPC hilla = new MapleNPC(1402400, "Hilla");
            hilla.setPosition(new Point(-131, -2));
            hilla.setCy(-7);
            hilla.setF(1);
            hilla.setFh(12);
            hilla.setRx0(-181);
            hilla.setRx1(-81);
            MapleNPC guard1 = new MapleNPC(1402401, "Hilla's Guard");
            guard1.setPosition(new Point(-209, -2));
            guard1.setCy(-7);
            guard1.setF(1);
            guard1.setFh(12);
            guard1.setRx0(-259);
            guard1.setRx1(-159);
            MapleNPC guard2 = new MapleNPC(1402401, "Hilla's Guard");
            guard2.setPosition(new Point(-282, -2));
            guard2.setCy(-7);
            guard2.setF(1);
            guard2.setFh(12);
            guard2.setRx0(-332);
            guard2.setRx1(-232);
            MapleNPC guard3 = new MapleNPC(1402401, "Hilla's Guard");
            guard3.setPosition(new Point(-59, -2));
            guard3.setCy(-7);
            guard3.setF(1);
            guard3.setFh(12);
            guard3.setRx0(-109);
            guard3.setRx1(-9);
            c.getSession().write(NPCPacket.spawnNPC(hilla, true));
            c.getSession().write(NPCPacket.spawnNPC(guard1, true));
            c.getSession().write(NPCPacket.spawnNPC(guard2, true));
            c.getSession().write(NPCPacket.spawnNPC(guard3, true));
            Thread.sleep(6000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_2");
    }

    public void showSkaia() {
        try {
            c.getSession().write(CField.MapEff("phantom/skaia"));
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_3");
    }

    public void showPhantomWait() {
        try {
            c.getSession().write(CField.MapEff("phantom/phantom"));
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_4");
    }

    public void movePhantom() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 2000));
            Thread.sleep(2000);
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 0));
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_1");
    }

    public void showPhantomMovie() {
        warp(150000000);
        try {
            c.getSession().write(UIPacket.playMovie("phantom.avi", true));
            Thread.sleep(4 * 60 * 1000); //4 minutes
        } catch (InterruptedException e) {
        }
        MapleQuest.getInstance(25000).forceComplete(c.getPlayer(), 1402000);
        c.getSession().write(CField.UIPacket.getDirectionStatus(false));
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
    }

    public void mihileNeinheartDisappear() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction7.img/effect/tuto/step0/4", 2000, 0, -100, 1, 0));
            c.getSession().write(UIPacket.getDirectionFacialExpression(6, 2000));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 2000));
            Thread.sleep(2000);
            NPCScriptManager.getInstance().start(c, 1106000, "tuto002");
        } catch (InterruptedException e) {
        }
    }

    public void mihileMove913070001() {
        try {
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 800));
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        while (c.getPlayer().getLevel() < 2) {
            c.getPlayer().levelUp();
        }
        c.getPlayer().setExp(0);
        warp(913070001, 0);
        c.getSession().write(CWvsContext.enableActions());
    }

    public void mihileSoul() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction7.img/effect/tuto/soul/0", 4000, 0, -100, 1, 0));
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c, 1106000, "tuto003");
    }

    public void mihileMove913070050() {
        try {
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 6000));
            Thread.sleep(5000);
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 0));
            NPCScriptManager.getInstance().start(c, 1106001, "tuto005");
        } catch (InterruptedException e) {
        }
    }

    public void mihileAssailantSummon() {
        for (int i = 0; i < 10; i++) {
            c.getPlayer().getMap().spawnMonster_sSack(MapleLifeFactory.getMonster(9001050), new Point(240, 65), 0);
        }
        c.getSession().write(CWvsContext.enableActions());
    }

    public void displayCustomRanks() {
        c.getSession().write(CustomPlayerRankings.getInstance().customRanks(id));
    }

    public List<Triple<Short, String, Integer>> rankList(short[] ranks, String[] names, int[] values) {
        List<Triple<Short, String, Integer>> list = new LinkedList();
        if (ranks.length != names.length || names.length != values.length || values.length != ranks.length) {
            return null;
        }
        for (int i = 0; i < ranks.length; i++) {
            list.add(new Triple<>(ranks[i], names[i], values[i]));
        }
        return list;
    }

    public void displayRank(int npcid, List<Triple<Short, String, Integer>> list) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
        mplew.write(0x50);
        mplew.writeInt(npcid);
        mplew.writeInt(list.size());
        for (Triple<Short, String, Integer> info : list) {
            mplew.writeShort(info.getLeft()); //Rank
            mplew.writeMapleAsciiString(info.getMid()); //Name
            mplew.writeInt(info.getRight()); //Value
            mplew.writeZeroBytes(16);
        }
        c.getSession().write(mplew.getPacket());
    }

    public void dragonShoutReward(int reward) {
        int itemid;
        switch (reward) {
            case 0:
                itemid = 1102207;
                break;
            case 1:
                itemid = 1122080;
                break;
            case 2:
                itemid = 2041213;
                break;
            case 3:
                itemid = 2022704;
                break;
            default:
                itemid = 2022704;
                break;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleInventoryType invtype = GameConstants.getInventoryType(itemid);
        if (!MapleInventoryManipulator.checkSpace(c, itemid, 1, "")) {
            return;
        }
        if (invtype.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(itemid) && !GameConstants.isBullet(itemid)) {
            final Equip item = (Equip) (ii.getEquipById(itemid));
            switch (reward) {
                case 0: //9% ATT, 9% MAGIC, 30% Boss Damage
                    item.setPotential1(40051); //9% Att
                    item.setPotential2(40052); //9% Magic
                    item.setPotential3(40601); //30% Boss Damage
                    break;
                case 1: //30% All Stat
                    item.setPotential1(40086); //9% All Stat
                    item.setPotential2(40086); //9% All Stat
                    item.setPotential3(40086); //9% All Stat
                    item.setSocket1(ii.getSocketInfo(3063280).opID); //3% All Stat
                    break;
            }
            item.setOwner("Hyperious");
            item.setGMLog("Received from interaction " + this.id + " (" + id2 + ") (The Dragon's Shout PQ) on " + FileoutputUtil.CurrentReadable_Time());
            final String name = ii.getName(itemid);
            if (itemid / 10000 == 114 && name != null && name.length() > 0) { //medal
                final String msg = "< " + name + " > has been rewarded.";
                c.getPlayer().dropMessage(-1, msg);
                c.getPlayer().dropMessage(5, msg);
            }
            MapleInventoryManipulator.addbyItem(c, item.copy());
        } else {
            MapleInventoryManipulator.addById(c, itemid, (short) 1, "Hyperious", null, 0, false, "Received from interaction " + this.id + " (" + id2 + ") on " + FileoutputUtil.CurrentReadable_Date());
        }
        c.getSession().write(InfoPacket.getShowItemGain(itemid, (short) 1, true));
    }

    public String searchData(int type, String search) {
        return SearchGenerator.searchData(type, search);
    }

    public boolean foundData(int type, String search) {
        return SearchGenerator.foundData(type, search);
    }

    public boolean partyHaveItem(int itemid, short quantity) {
        if (getPlayer().getParty() == null) {
            return false;
        }
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) {
                    if (!ch.haveItem(itemid, quantity)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public final boolean scrollItem(final short scroll, final short item) {
        return InventoryHandler.UseUpgradeScroll(scroll, item, (short) 0, getClient(), getPlayer(), 0, false);
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /*
     * public final int WEAPON_RENTAL = 57463816;
     *
     * public int weaponRentalState() { if
     * (c.getPlayer().getIntNoRecord(WEAPON_RENTAL) == 0) { return 0; } return
     * (System.currentTimeMillis() / (60 * 1000) -
     * c.getPlayer().getIntNoRecord(WEAPON_RENTAL)) >= 15 ? 1 : 2; }
     *
     * public void setWeaponRentalUnavailable() {
     * c.getPlayer().getQuestNAdd(MapleQuest.getInstance(WEAPON_RENTAL)).setCustomData(""
     * + System.currentTimeMillis() / (60 * 1000)); }
     */
    public MapleQuest getQuestById(int questId) {
        return MapleQuest.getInstance(questId);
    }

    public int getEquipLevelById(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getEquipStats(itemId).get("reqLevel");
    }

    public void sendGMBoard(String url) {
        c.getSession().write(CWvsContext.gmBoard(c.getNextClientIncrenement(), url));
    }

    public void addPendantSlot(int days) {
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.PENDANT_SLOT)).setCustomData(String.valueOf(System.currentTimeMillis() + ((long) days * 24 * 60 * 60 * 1000)));
    }

    public long getCustomMeso() {
        return c.getPlayer().getLongNoRecord(GameConstants.CUSTOM_BANK);
    }

    public void setCustomMeso(long meso) {
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.CUSTOM_BANK)).setCustomData(meso + "");
    }

    public void enter_931060110() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuUI", 6000, 285, 186, 1, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 900));
            Thread.sleep(900);
            c.getSession().write(CWvsContext.getTopMsg("First, click MENU at the bottom of the screen."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToMenu", 1740, -114, -14, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1680));
            Thread.sleep(1680);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 1440, 246, 196, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1440));
            Thread.sleep(1440);
            c.getSession().write(CWvsContext.getTopMsg("Now, select Go to Farm."));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 600));
            Thread.sleep(600);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuOpen", 50000, 285, 186, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 600));
            Thread.sleep(600);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToMyfarm", 750, 246, 196, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 720));
            Thread.sleep(720);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuMouseOver", 50000, 285, 186, 1, 2));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 50000, 246, 166, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1440));
            Thread.sleep(1440);
        } catch (InterruptedException ex) {
        }
    }

    public void enter_931060120() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/character", 120000, -200, 0, 1, 1));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
            c.getSession().write(CWvsContext.getTopMsg("Hover over any other character..."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToChar", 1680, -400, -210, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1650));
            Thread.sleep(1650);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseUp", 600, -190, -30, 1, 3));
            c.getSession().write(CWvsContext.getTopMsg("Then right-click."));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 540));
            Thread.sleep(540);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 1200, -190, -30, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/characterMenu", 50000, -200, 0, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 900));
            Thread.sleep(900);
            c.getSession().write(CWvsContext.getTopMsg("When the Character Menu appears, click Go to Farm."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToOtherfarm", 1440, -190, -30, 1, 5));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1380));
            Thread.sleep(1380);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuMouseOver", 50000, -200, 0, 1, 4));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 60000, -130, 150, 1, 6));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
        } catch (InterruptedException ex) {
        }
    }

    public void showJettWanted() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(1000);
            c.getSession().write(CField.environmentChange("newPirate/pendant_w", 12));
            c.getSession().write(UIPacket.getDirectionFacialExpression(5, 3000));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/1", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 3000));
            Thread.sleep(3000);
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 1));
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9270083, "np_tuto_0_2");
    }

    public void np_tuto_0_2() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 10));
            Thread.sleep(10);
            c.getSession().write(UIPacket.getDirectionFacialExpression(5, 3000));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/1", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9270083, "np_tuto_0_3");
    }

    public void spawnJettGuards() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 300));
            Thread.sleep(300);
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 0));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/3", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 500));
            Thread.sleep(500);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/attack_tuto", 2000, 0, -80, 0, 0));
        } catch (InterruptedException ex) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        c.getSession().write(CWvsContext.getTopMsg("Eliminate all Guards."));
        forceStartQuest(53245);
        spawnMob(9420564, 3, 600, -120);
    }

    public static String getMobImg(int mob) {
        MapleMonster monster = MapleLifeFactory.getMonster(mob);
        if (monster.getStats().getLink() != 0) {
            mob = monster.getStats().getLink();
        }
        String mobStr = String.valueOf(mob);
        while (mobStr.length() < 7) {
            String newStr = "0" + mobStr;
            mobStr = newStr;
        }
        return "#fMob/" + mobStr + ".img/stand/0#";
    }

    public void showKannaMovie() {
        try {
            c.getSession().write(UIPacket.playMovie("JPKanna.avi", true));
            Thread.sleep(1 * 60 * 1000);
        } catch (InterruptedException e) {
        }
        c.getSession().write(CField.UIPacket.getDirectionStatus(false));
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
    }

    public void showAdvanturerBoatScene() {
        try {
            c.getSession().write(UIPacket.getDirectionStatus(true));
            c.getSession().write(CField.UIPacket.IntroEnableUI(1));
            c.getSession().write(CField.environmentChange("advStory/whistle", 5));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 208));
            Thread.sleep(208);
            c.getSession().write(CField.EffectPacket.ShowWZEffect("Effect/Direction3.img/adventureStory/Scene2"));
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 10306, "ExplorerTut07");
    }

    public void showMapleLeafScene() {
        try {
            c.getSession().write(UIPacket.getDirectionStatus(true));
            c.getSession().write(CField.UIPacket.IntroEnableUI(1));
            c.getSession().write(CField.environmentChange("adventureStory/mapleLeaf/0", 12));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1800));
            Thread.sleep(1800);
        } catch (InterruptedException ex) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 10306, "ExplorerTut08");
    }

    public void showBeastTamerTutScene() {
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9390305, "BeastTamerTut01");
    }

    public void showBeastTamerTutScene1() {
        try {
            c.getSession().write(UIPacket.getDirectionStatus(true));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(2000);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", 2000, 0, -120, 1, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 800));
            Thread.sleep(800);
            c.getSession().write(CField.UIPacket.getDirectionInfoNew((byte) 0, 1000, 700, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1200));
            Thread.sleep(1200);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/7", 2000, 571, -120, 1, 0));
            c.getSession().write(CField.environmentChange("ShamanBTTuto/sound0", 5));
            c.getSession().write(UIPacket.getDirectionInfo(1, 3000));
            Thread.sleep(3000);
            c.getSession().write(UIPacket.getDirectionInfo(1, 1000));
            c.getSession().write(UIPacket.getDirectionInfo(1, 500));
            Thread.sleep(1500);
            c.getSession().write(UIPacket.getDirectionFacialExpression(4, 5000));
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9390305, "BeastTamerTut02");
    }

    private static final MerchItemPackage loadItemFrom_Database(final int accountid) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ResultSet rs;

            final int packageid;
            final MerchItemPackage pack;
            try (PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where accountid = ?")) {
                ps.setInt(1, accountid);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    ps.close();
                    rs.close();
                    return null;
                }
                packageid = rs.getInt("PackageId");
                pack = new MerchItemPackage();
                pack.setPackageid(packageid);
                pack.setMesos(rs.getInt("Mesos"));
                pack.setSavedTime(rs.getLong("time"));
            }
            rs.close();

            Map<Long, Pair<Item, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems(false, packageid);
            if (items != null) {
                List<Item> iters = new ArrayList<>();
                for (Pair<Item, MapleInventoryType> z : items.values()) {
                    iters.add(z.left);
                }
                pack.setItems(iters);
            }

            return pack;
        } catch (SQLException e) {
            System.err.println("loadItemFrom_Database" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            return null;
        }
    }

    public String checkDrop(int mobId) {
        int rate = getClient().getChannelServer().getDropRate(getPlayer().getWorld());
        MapleMonster mob = MapleLifeFactory.getMonster(mobId);
        if (MapleLifeFactory.getMonster(mobId) != null) {
            if (mob.getStats().isBoss()) {
                rate = getClient().getChannelServer().getBossDropRate();
            }
        }
        List ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;
            int itemId = 0;
            int ch = 0;

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterDropEntry de = (MonsterDropEntry) ranks.get(i);
                if ((de.chance > 0) && ((de.questid <= 0) || ((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0)))) {
                    itemId = de.itemId;
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("当前怪物 #o").append(mobId).append("# 的爆率为:\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if (itemId == 0) {
                        itemId = 4031041;
                        namez = new StringBuilder().append(de.Minimum * getClient().getChannelServer().getMesoRate(0)).append(" - ").append(de.Maximum * getClient().getChannelServer().getMesoRate(0)).append(" 的金币").toString();
                    }
                    ch = de.chance * rate;
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0D).append("%的爆率. ").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "没有找到这个怪物的爆率数据。";
    }

    public void worldMessage(String text) {
        World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, text));
    }

    public void worldMessage(String text, int byt) {
        World.Broadcast.broadcastMessage(CWvsContext.serverNotice(byt, text));
    }

    public void warpBack(int mid, final int retmap, final int time) {

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.getSession().write(CField.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.getSession().write(CField.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    c.getPlayer().dropMessage(6, "已到达目的地!");
                }
            }
        }, 1000 * time);
    }

    public int getLeftJailTime() {
        long seconds = 0;
        final MapleQuestStatus stat1 = getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME));//系统时间
        final MapleQuestStatus stat2 = getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST));//在监牢秒数
        if (stat1 != null && stat2 != null) {
            if (stat1.getCustomData() != null && stat2.getCustomData() != null) {
                // 剩余秒数 = 应该在监牢秒数 - ((目前时间 - 当时系统时间) / 1000);
                long SecondAfterJail = (System.currentTimeMillis() - Long.parseLong(stat1.getCustomData())) / 1000;
                int SecondinJail = Integer.parseInt(stat2.getCustomData());
                seconds = SecondinJail - SecondAfterJail;
                if (seconds < 0) {
                    seconds = 0;
                    stat2.setCustomData("0");
                }
            }
        }
        return (int) seconds;
    }

    public void ShowMarrageEffect() {
        c.getPlayer().getMap().broadcastMessage((CField.sendMarrageEffect()));
    }

    public void openBeans() {//打开豆豆机界面
        c.getSession().write(CField.openBeans(c.getPlayer()));
    }
    
    public int gainGachaponItema(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = 1;
            if (rareness > 0) {
                World.Broadcast.broadcastSmega(CWvsContext.itemMegaphone(msg + " : 恭喜" + "[" + c.getPlayer().getName() + "]通过" + msg + "获得大奖！", true, c.getChannel(), item));
                //World.Broadcast.broadcastSmega(MaplePacketCreator.itemMegaphone("恭喜 " +  c.getPlayer().getName() + " : 从 "+ msg +" 获得大奖！", true, c.getChannel(), item));
                //World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("恭喜 " + "[" + c.getPlayer().getName() + "] ", "从 " + msg + " 获得大奖", item, rareness));
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
