package server.commands;

import client.LoginCrypto;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import custom.LoadPacket;
import database.DBConPool;
import handling.SendPacketOpcode;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.login.handler.AutoRegister;
import handling.world.CharacterTransfer;
import handling.world.MapleMessengerCharacter;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.Randomizer;
import server.ShutdownServer;
import server.Timer.EventTimer;
import server.Timer.WorldTimer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.StringUtil;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PetPacket;

public class AdminCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.ADMIN;
    }

    public static class 人数上限 extends setUserLimit {
    }

    public static class 开放地图 extends openmap {
    }

    public static class 关闭地图 extends closemap {
    }

    public static class 注册 extends register {
    }

    public static class 满属性 extends maxstats {
    }

    public static class 满技能 extends maxSkills {
    }

    public static class 清技能 extends maxSkills {
    }

    public static class 拉全部 extends WarpAllHere {
    }

    public static class 给所有人经验 extends ExpEveryone {
    }

    public static class 给所有人点卷 extends CashEveryone {
    }

    public static class 刷新地图 extends ReloadMap {
    }

    public static class 给所有人金币 extends mesoEveryone {
    }

    public static class 祝福 extends buff {
    }

    public static class 自动注册 extends autoreg {
    }

    public static class 怪物代码 extends mob {
    }

    public static class 封号状态 extends BanStatus {
    }

    public static class 文件封包 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getSession().write(LoadPacket.getPacket());
            return 1;
        }
    }

    public static class 关闭服务器 extends CommandExecute {

        protected static Thread t = null;

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "正在关闭服务器...");
            if (t == null || !t.isAlive()) {
                t = new Thread(ShutdownServer.getInstance());
                ShutdownServer.getInstance().shutdown();
                t.start();
            } else {
                c.getPlayer().dropMessage(6, "关闭线程已经在进行或关闭还没有完成。请等待.");
            }
            return 1;
        }
    }

    public static class 关闭服务器时间 extends 关闭服务器 {

        private static ScheduledFuture<?> ts = null;
        private int minutesLeft = 0;

        @Override
        public int execute(MapleClient c, String[] splitted) {
            minutesLeft = Integer.parseInt(splitted[1]);
            c.getPlayer().dropMessage(6, "服务器将在 " + minutesLeft + " 分钟后关闭");
            if (ts == null && (t == null || !t.isAlive())) {
                t = new Thread(ShutdownServer.getInstance());
                ts = EventTimer.getInstance().register(new Runnable() {
                    @Override
                    public void run() {
                        if (minutesLeft == 0) {
                            ShutdownServer.getInstance().shutdown();
                            t.start();
                            ts.cancel(false);
                            return;
                        }
                        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0, "服务器将在 " + minutesLeft + " 分钟后关闭."));
                        minutesLeft--;
                    }
                }, 60000);
            } else {
                c.getPlayer().dropMessage(6, "关闭线程已经在进行或关闭还没有完成。请等待.");
            }
            return 1;
        }
    }

    public static class autoreg extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage("目前自动注册已经 " + ServerConstants.ChangeAutoReg());
            return 1;
        }
    }

    public static class 调试模式 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage("目前调试模式已经 " + LoginServer.ChangeAdminOnly());
            return 1;
        }
    }

    public static class BanStatus extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            String name = splitted[1];
            String mac = "";
            String ip = "";
            int acid = 0;
            boolean Systemban = false;
            boolean ACbanned = false;
            boolean IPbanned = false;
            boolean MACbanned = false;
            String reason = null;
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                PreparedStatement ps;
                ps = (PreparedStatement) con.prepareStatement("select accountid from characters where name = ?");
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        acid = rs.getInt("accountid");
                    }
                }
                ps = (PreparedStatement) con.prepareStatement("select banned, banreason, macs, Sessionip from accounts where id = ?");
                ps.setInt(1, acid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Systemban = rs.getInt("banned") == 2;
                        ACbanned = rs.getInt("banned") == 1 || rs.getInt("banned") == 2;
                        reason = rs.getString("banreason");
                        mac = rs.getString("macs");
                        ip = rs.getString("Sessionip");
                    }
                }
                ps.close();
            } catch (SQLException e) {
                System.err.println("BanStatus" + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            }
            if (reason == null || reason == "") {
                reason = "?";
            }
            if (c.isBannedIP(ip)) {
                IPbanned = true;
            }
            if (c.isBannedMac(mac)) {
                MACbanned = true;
            }
            c.getPlayer().dropMessage("玩家[" + name + "] 帐号ID[" + acid + "]是否被封锁: " + (ACbanned ? "是" : "否") + (Systemban ? "(系统自动封锁)" : "") + ", 原因: " + reason);
            c.getPlayer().dropMessage("IP: " + ip + " 是否在封锁IP名单: " + (IPbanned ? "是" : "否"));
            c.getPlayer().dropMessage("MAC: " + mac + " 是否在封锁MAC名单: " + (MACbanned ? "是" : "否"));
            return 1;
        }
    }

    public static class mob extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster monster = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                monster = (MapleMonster) monstermo;
                if (monster.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物 " + monster.toString());
                }
            }
            if (monster == null) {
                c.getPlayer().dropMessage(6, "找不到怪物");
            }
            return 1;
        }
    }

    public static class UpdatePet extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MaplePet pet = c.getPlayer().getPet(0);
            if (pet == null) {
                return 0;
            }
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.petColor(c.getPlayer().getId(), (byte) 0, Color.yellow.getAlpha()), true);
            return 1;
        }
    }

    public static class DamageBuff extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            SkillFactory.getSkill(9001003).getEffect(1).applyTo(c.getPlayer());
            return 1;
        }
    }

    public static class MagicWheel extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<Integer> items = new LinkedList();
            for (int i = 1; i <= 10; i++) {
                try {
                    items.add(Integer.parseInt(splitted[i]));
                } catch (NumberFormatException ex) {
                    items.add(GameConstants.eventRareReward[GameConstants.eventRareReward.length]);
                }
            }
            int end = Randomizer.nextInt(10);
            String data = "Magic Wheel";
            c.getPlayer().setWheelItem(items.get(end));
            c.getSession().write(CWvsContext.magicWheel((byte) 3, items, data, end));
            return 1;
        }
    }

    public static class UnsealItem extends CommandExecute {

        @Override
        public int execute(final MapleClient c, String[] splitted) {
            short slot = Short.parseShort(splitted[1]);
            Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
            if (item == null) {
                return 0;
            }
            final int itemId = item.getItemId();
            Integer[] itemArray = {1002140, 1302000, 1302001,
                1302002, 1302003, 1302004, 1302005, 1302006,
                1302007};
            final List<Integer> items = Arrays.asList(itemArray);
            c.getSession().write(CField.sendSealedBox(slot, 2028162, items)); //sealed box
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            WorldTimer.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemId), itemId, 1, false, false);
                    Item item = ii.getEquipById(items.get(Randomizer.nextInt(items.size())));
                    MapleInventoryManipulator.addbyItem(c, item);
                    c.getSession().write(CField.unsealBox(item.getItemId()));
                    c.getSession().write(CField.EffectPacket.showRewardItemAnimation(2028162, "")); //sealed box
                }
            }, 10000);
            return 1;
        }
    }

    public static class CPacket extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getSession().write(LoadPacket.getPacket());
            return 1;
        }
    }

    public static class NearestPortal extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MaplePortal portal = c.getPlayer().getMap().findClosestPortal(c.getPlayer().getTruePosition());
            c.getPlayer().dropMessage(6, portal.getName() + " id: " + portal.getId() + " 脚本: " + portal.getScriptName());

            return 1;
        }
    }

    public static class Uptime extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "Server has been up for " + StringUtil.getReadableMillis(ChannelServer.serverStartTime, System.currentTimeMillis()));
            return 1;
        }
    }

    public static class Reward extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            chr.addReward(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]), StringUtil.joinStringFrom(splitted, 7));
            chr.updateReward();
            return 1;
        }
    }

    public static class DropMessage extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String type = splitted[1];
            String text = splitted[2];
            if (type == null) {
                c.getPlayer().dropMessage(6, "Syntax error: !dropmessage type text");
                return 0;
            }
            if (type.length() > 1) {
                c.getPlayer().dropMessage(6, "Type must be just with one word");
                return 0;
            }
            if (text == null && text.length() < 1) {
                c.getPlayer().dropMessage(6, "Text must be 1 letter or more!!");
                return 0;
            }
            c.getPlayer().dropMessage(Integer.parseInt(type), text);
            return 1;
        }
    }

    public static class DropMsg extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String type = splitted[1];
            String text = splitted[2];
            if (type == null) {
                c.getPlayer().dropMessage(6, "Syntax error: !dropmessage type text");
                return 0;
            }
            if (type.length() > 1) {
                c.getPlayer().dropMessage(6, "Type must be just with one word");
                return 0;
            }
            if (text == null && text.length() < 1) {
                c.getPlayer().dropMessage(6, "Text must be 1 letter or more!!");
                return 0;
            }
            //c.getPlayer().dropMsg(Integer.parseInt(type), text);
            return 1;
        }
    }

    public static class GMPerson extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]).setGM(Byte.parseByte(splitted[2]));
            return 1;
        }
    }

    public static class WarpCashShop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            MapleClient client = chr.getClient();
            final ChannelServer ch = ChannelServer.getInstance(client.getChannel());

            chr.changeRemoval();

            if (chr.getMessenger() != null) {
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
            }
            PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -10);
            ch.removePlayer(chr);
            client.updateLoginState(MapleClient.CHANGE_CHANNEL, client.getSessionIPAddress());
            chr.saveToDB(false, false);
            chr.getMap().removePlayer(chr);
            client.getSession().write(CField.getChannelChange(client, Integer.parseInt(CashShopServer.getIP().split(":")[1])));
            client.setPlayer(null);
            client.setReceiving(false);
            return 1;
        }
    }

    public static class TestDirection extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getSession().write(CField.UIPacket.getDirectionInfo(StringUtil.joinStringFrom(splitted, 5), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5])));
            return 1;
        }
    }

    public static class ToggleAutoRegister extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            AutoRegister.autoRegister = !AutoRegister.autoRegister;
            c.getPlayer().dropMessage(0, "Auto Register has been " + (AutoRegister.autoRegister ? "enabled" : "disabled") + ".");
            System.out.println("Auto Register has been " + (AutoRegister.autoRegister ? "enabled" : "disabled") + ".");
            return 1;
        }
    }

    public static class Packet extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            int packetheader = Integer.parseInt(splitted[1]);
            String packet_in = " 00 00 00 00 00 00 00 00 00 ";
            if (splitted.length > 2) {
                packet_in = StringUtil.joinStringFrom(splitted, 2);
            }
            mplew.writeShort(packetheader);
            mplew.write(HexTool.getByteArrayFromHexString(packet_in));
            mplew.writeZeroBytes(20);
            c.getSession().write(mplew.getPacket());

            c.getPlayer().dropMessage(packetheader + "已传送封包[" + mplew.getPacket().length + "] : " + mplew.toString());

            return 1;
        }
    }

    public static class StripEveryone extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ChannelServer cs = c.getChannelServer();
            for (MapleCharacter mchr : cs.getPlayerStorage().getAllCharacters()) {
                if (c.getPlayer().isGM()) {
                    continue;
                }
                MapleInventory equipped = mchr.getInventory(MapleInventoryType.EQUIPPED);
                MapleInventory equip = mchr.getInventory(MapleInventoryType.EQUIP);
                List<Short> ids = new ArrayList<>();
                for (Item item : equipped.newList()) {
                    ids.add(item.getPosition());
                }
                for (short id : ids) {
                    MapleInventoryManipulator.unequip(mchr.getClient(), id, equip.getNextFreeSlot());
                }
            }
            return 1;
        }
    }

    public static class Strip extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            MapleInventory equipped = victim.getInventory(MapleInventoryType.EQUIPPED);
            MapleInventory equip = victim.getInventory(MapleInventoryType.EQUIP);
            List<Short> ids = new ArrayList<>();
            for (Item item : equipped.newList()) {
                ids.add(item.getPosition());
            }
            for (short id : ids) {
                MapleInventoryManipulator.unequip(victim.getClient(), id, equip.getNextFreeSlot());
            }
            boolean notice = false;
            if (splitted.length > 1) {
                notice = true;
            }
            if (notice) {
                World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0, victim.getName() + " has been stripped by " + c.getPlayer().getName()));
            }
            return 1;
        }
    }

    public static class buff extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            SkillFactory.getSkill(9001002).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001003).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001008).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001001).getEffect(1).applyTo(player);
            return 1;
        }
    }

    public static class mesoEveryone extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <金币量>");
                return 0;
            }
            int ret = 0;
            int gain = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                mch.gainMeso(gain, true);
                ret++;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "冒险币给在线的所有玩家！祝您在这里玩的开心玩的快乐！", 5121006);
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 冒险币 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给当前地图冒险币 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <金币量>");
                return 0;
            }
            int ret = 0;
            int gain = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (c.getPlayer().getMapId() == mch.getMapId()) {
                    mch.gainMeso(gain, true);
                    ret++;
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "冒险币给当前地图所有玩家！祝您在这里玩的开心玩的快乐！", 5121006);
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 冒险币 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class ScheduleHotTime extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 1) {
                c.getPlayer().dropMessage(0, "!ScheduleHotTime <Item Id>");
                return 0;
            }
            if (!MapleItemInformationProvider.getInstance().itemExists(Integer.parseInt(splitted[1]))) {
                c.getPlayer().dropMessage(0, "物品不存在");
                return 0;
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (c.canClickNPC()) {
                        mch.gainItem(Integer.parseInt(splitted[1]), 1);
                        mch.getClient().getSession().write(CField.NPCPacket.getNPCTalk(9010010, (byte) 0, "You got the #t" + Integer.parseInt(splitted[1]) + "#, right? Click it to see what's inside. Go ahead and check your item inventory now, if you're curious.", "00 00", (byte) 1, 9010010));
                    }
                }
            }
            System.out.println("Hot Time had been scheduled successfully.");
            return 1;
        }
    }

    public static class WarpAllHere extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() != c.getPlayer().getMapId()) {
                    mch.changeMap(c.getPlayer().getMap(), c.getPlayer().getPosition());
                }
            }
            return 1;
        }
    }

    public static class 踢所有人 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int range = -1;
            switch (splitted[1]) {
                case "m":
                    range = 0;
                    break;
                case "c":
                    range = 1;
                    break;
                case "w":
                    range = 2;
                    break;
            }
            if (range == -1) {
                range = 1;
            }
            if (range == 0) {
                c.getPlayer().getMap().disconnectAll();
            } else if (range == 1) {
                c.getChannelServer().getPlayerStorage().disconnectAll(true);
            } else if (range == 2) {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.getPlayerStorage().disconnectAll(true);
                }
            }
            return 1;
        }
    }

    public static class Sql extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                PreparedStatement ps = con.prepareStatement(StringUtil.joinStringFrom(splitted, 1));
                ps.executeUpdate();
            } catch (SQLException e) {
                c.getPlayer().dropMessage(0, "Failed to execute SQL command.");
                System.err.println("Failed to execute SQL command." + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                return 0;
            }
            return 1;
        }
    }

    public static class ExpEveryone extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <经验量>");
                return 0;
            }
            int gain = Integer.parseInt(splitted[1]);
            int ret = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    mch.gainExp(gain, true, true, true);
                    ret++;
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "经验给在线的所有玩家！祝您在这里玩的开心玩的快乐！", 5121003);
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 点的").append(" 经验 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给当前地图经验 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <经验量>");
                return 0;
            }
            int gain = Integer.parseInt(splitted[1]);
            int ret = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.gainExp(gain, true, true, true);
                        ret++;
                    }
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "经验给地图的所有玩家！祝您在这里玩的开心玩的快乐！", 5121003);
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 点的").append(" 经验 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class CashEveryone extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length > 2) {
                int type = Integer.parseInt(splitted[1]);
                int quantity = Integer.parseInt(splitted[2]);
                if (type == 1) {
                    type = 1;
                } else if (type == 2) {
                    type = 2;
                } else {
                    c.getPlayer().dropMessage(6, "用法: !给所有人点卷 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
                    return 0;
                }
                if (quantity > 10000) {
                    quantity = 10000;
                }
                int ret = 0;
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                        mch.modifyCSPoints(type, quantity);
                        ret++;
                    }
                }
                String show = type == 1 ? "点卷" : "抵用卷";
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("管理员发放" + quantity + show + "点卷给在线的所有玩家！祝您在这里玩的开心玩的快乐！", 5121016);
                    }
                }
                c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(quantity).append(" 点的").append(type == 1 ? "点券 " : " 抵用券 ").append(" 总计: ").append(ret * quantity).toString());
            } else {
                c.getPlayer().dropMessage(6, "用法: !给所有人点卷 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
            }
            return 1;
        }
    }

    public static class 给当前地图点卷 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length > 2) {
                int type = Integer.parseInt(splitted[1]);
                int quantity = Integer.parseInt(splitted[2]);
                if (type == 1) {
                    type = 1;
                } else if (type == 2) {
                    type = 2;
                } else {
                    c.getPlayer().dropMessage(6, "用法: !给当前地图点卷 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
                    return 0;
                }
                if (quantity > 10000) {
                    quantity = 10000;
                }
                int ret = 0;
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                        if (c.getPlayer().getMapId() == mch.getMapId()) {
                            mch.modifyCSPoints(type, quantity);
                            ret++;
                        }
                    }
                }
                String show = type == 1 ? "点卷" : "抵用卷";
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("管理员发放" + quantity + show + "点卷给当前地图的所有玩家！祝您在这里玩的开心玩的快乐！", 5121016);
                    }
                }
                c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(quantity).append(" 点的").append(type == 1 ? "点券 " : " 抵用券 ").append(" 总计: ").append(ret * quantity).toString());
            } else {
                c.getPlayer().dropMessage(6, "用法: !给当前地图点卷 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
            }
            return 1;
        }
    }

    public static class register extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String acc = null;
            String password = null;
            try {
                acc = splitted[1];
                password = splitted[2];
            } catch (Exception ex) {
            }
            if (acc == null || password == null) {
                c.getPlayer().dropMessage("账号或密码异常");
                return 0;
            }
            boolean ACCexist = AutoRegister.getAccountExists(acc);
            if (ACCexist) {
                c.getPlayer().dropMessage("帐号已被使用");
                return 0;
            }
            if (acc.length() >= 12) {
                c.getPlayer().dropMessage("密码长度过长");
                return 0;
            }

            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password) VALUES (?, ?)")) {
                    ps.setString(1, acc);
                    ps.setString(2, LoginCrypto.hexSha1(password));
                    ps.executeUpdate();
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println("register" + ex);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
                return 0;
            }
            c.getPlayer().dropMessage("[注册完成]账号: " + acc + " 密码: " + password);
            return 1;
        }
    }

    public static class maxstats extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            player.getStat().setMaxHp((short) 30000, player);
            player.getStat().setMaxMp((short) 30000, player);
            player.getStat().setStr(Short.MAX_VALUE, player);
            player.getStat().setDex(Short.MAX_VALUE, player);
            player.getStat().setInt(Short.MAX_VALUE, player);
            player.getStat().setLuk(Short.MAX_VALUE, player);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
            player.updateSingleStat(MapleStat.STR, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.DEX, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.INT, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.LUK, Short.MAX_VALUE);
            player.getStat().heal(player);
            return 1;
        }
    }

    public static class maxSkills extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            player.maxSkills();
            return 1;
        }
    }

    public static class minSkills extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            player.minSkills();
            return 1;
        }
    }

    public static class openmap extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            int mapid = 0;
            String input = null;
            MapleMap map = null;
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " - 开放地图");
                return 0;
            }
            try {
                input = splitted[1];
                mapid = Integer.parseInt(input);
            } catch (Exception ex) {
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().HealMap(mapid);
            }
            return 1;
        }
    }

    public static class closemap extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            int mapid = 0;
            String input = null;
            MapleMap map = null;
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " - 关闭地图");
                return 0;
            }
            try {
                input = splitted[1];
                mapid = Integer.parseInt(input);
            } catch (Exception ex) {
            }
            if (c.getChannelServer().getMapFactory().getMap(mapid) == null) {
                c.getPlayer().dropMessage("地图不存在");
                return 0;
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().destroyMap(mapid, true);
            }
            return 1;
        }
    }

    public static class ReloadMap extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final int mapId = Integer.parseInt(splitted[1]);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                if (cserv.getMapFactory().isMapLoaded(mapId) && cserv.getMapFactory().getMap(mapId).getCharactersSize() > 0) {
                    c.getPlayer().dropMessage(5, "There exists characters on channel " + cserv.getChannel());
                    return 0;
                }
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                if (cserv.getMapFactory().isMapLoaded(mapId)) {
                    cserv.getMapFactory().removeMap(mapId);
                }
            }
            return 1;
        }
    }

    public static class SavePlayerShops extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
                cserv.closeAllMerchant();
            }
            c.getPlayer().dropMessage(6, "雇佣商人保存成功.");
            return 1;
        }
    }

    public static class Debug extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().setDebugMessage(!c.getPlayer().getDebugMessage());
            c.getPlayer().dropMessage("DeBug讯息已经" + (c.getPlayer().getDebugMessage() ? "开启" : "关闭"));
            return 1;
        }
    }

    public static class 关闭活动入口 extends CommandExecute {

        private static boolean tt = false;

        public int execute(MapleClient c, String splitted[]) {
            if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
                MapleEvent.setEvent(c.getChannelServer(), false);
                c.getPlayer().dropMessage(5, "已经关闭活动入口，可以使用 !活动开始 來启动。");
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "頻道:" + c.getChannel() + "活动目前已经关闭大门口。"));
                c.getPlayer().getMap().broadcastMessage(CField.getClock(60));
                EventTimer.getInstance().register(new Runnable() {

                    public void run() {
                        关闭活动入口.tt = true;
                    }
                }, 60 * 1000);
                if (tt) {
                    MapleEvent.onStartEvent(c.getPlayer());
                }
                return 1;
            } else {
                c.getPlayer().dropMessage(5, "您必须先使用 !选择活动 设定当前頻道的活动，并在当前頻道活动地图里使用。");
                return 1;
            }
        }

        public String getMessage() {
            return new StringBuilder().append("!关闭活动入口 -关闭活动入口").toString();
        }
    }

    public static class 选择活动 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            final MapleEventType type = MapleEventType.getByString(splitted[1]);
            if (type == null) {
                final StringBuilder sb = new StringBuilder("目前开放的活动有: ");
                for (MapleEventType t : MapleEventType.values()) {
                    sb.append(t.name()).append(",");
                }
                c.getPlayer().dropMessage(5, sb.toString().substring(0, sb.toString().length() - 1));
            }
            final String msg = MapleEvent.scheduleEvent(type, c.getChannelServer());
            if (msg.length() > 0) {
                c.getPlayer().dropMessage(5, msg);
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!选择活动 - 选择活动").toString();
        }
    }

    public static class 活动开始 extends CommandExecute {

        private static ScheduledFuture<?> ts = null;
        private int min = 1;

        public int execute(final MapleClient c, String splitted[]) {
            if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
                MapleEvent.setEvent(c.getChannelServer(), false);
                c.getPlayer().dropMessage(5, "已经关闭活动入口，可以使用 !活动开始 來启动。");
                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "頻道:" + c.getChannel() + "活动目前已经关闭大门口。"));
                c.getPlayer().getMap().broadcastMessage(CField.getClock(60));
                ts = EventTimer.getInstance().register(new Runnable() {

                    public void run() {
                        if (min == 0) {
                            MapleEvent.onStartEvent(c.getPlayer());
                            ts.cancel(false);
                            return;
                        }
                        min--;
                    }
                }, 60 * 1000);
                return 1;
            } else {
                c.getPlayer().dropMessage(5, "您必须先使用 !选择活动 设定當前頻道的活动，并在当前頻道活动地图里使用。");
                return 1;
            }
        }

        public String getMessage() {
            return new StringBuilder().append("!活动开始 - 活动开始").toString();
        }
    }

    public static class setUserLimit extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            int UserLimit = LoginServer.getUserLimit();
            try {
                UserLimit = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            LoginServer.setUserLimit(UserLimit);
            c.getPlayer().dropMessage("服务器人数上限已更改为" + UserLimit);
            return 1;
        }
    }

    public static class Str extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setStr(id);
            player.updateSingleStat(MapleStat.STR, id);
            player.dropMessage(5, "当前力量已经修改为: " + id);
            return 1;
        }

    }

    public static class Int extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setInt(id);
            player.updateSingleStat(MapleStat.INT, id);
            player.dropMessage(5, "当前智力已经修改为: " + id);
            return 1;
        }

    }

    public static class Luk extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setLuk(id);
            player.updateSingleStat(MapleStat.LUK, id);
            player.dropMessage(5, "当前幸运已经修改为: " + id);
            return 1;
        }
    }

    public static class Dex extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setDex(id);
            player.updateSingleStat(MapleStat.DEX, id);
            player.dropMessage(5, "当前敏捷已经修改为: " + id);
            return 1;
        }

    }

    public static class HP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setHp(id);
            player.getStat().setMaxHp(id, player);
            player.updateSingleStat(MapleStat.HP, id);
            player.updateSingleStat(MapleStat.MAXHP, id);
            player.dropMessage(5, "当前HP已经修改为: " + id);
            return 1;
        }

    }

    public static class MP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return 0;
            }
            id = Integer.parseInt(splitted[1]);
            player.setMp(id);
            player.getStat().setMaxMp(id, player);
            player.updateSingleStat(MapleStat.MP, id);
            player.updateSingleStat(MapleStat.MAXMP, id);
            player.dropMessage(5, "当前MP已经修改为: " + id);
            return 1;
        }

    }
}
