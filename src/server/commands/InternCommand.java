package server.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleDisease;
import client.MapleJob;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.anticheat.ReportType;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.OnlyID;
import constants.GameConstants;
import constants.ServerConstants.PlayerGMRank;
import database.DBConPool;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.CheaterData;
import handling.world.World;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import server.ItemInformation;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleSquad.MapleSquadType;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CField.NPCPacket;
import tools.packet.CWvsContext;

/**
 *
 * @author Emilyx3
 */
public class InternCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.INTERN;
    }

    public static class 传送 extends Map {

    }
    
    public static class 传送到id extends Warp {
    }

    public static class 跟踪 extends Warp {
    }

    public static class 封号 extends Ban {
    }

    public static class 隐身 extends Hide {
    }

    public static class 清空 extends ClearInv {
    }

    public static class 拉 extends WarpHere {
    }

    public static class 踢人 extends DC {
    }

    public static class 在线人数 extends Online {
    }

    public static class 清除地板 extends ClearDrops {
    }

    public static class 计时器 extends Clock {
    }

    public static class 清怪 extends KillAll {
    }

    public static class 全部复活 extends HealMap {
    }

    public static class 地图代码 extends WhereAmI {
    }

    public static class mapid extends WhereAmI {
    }

    public static class Hide extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().isHidden()) {
                c.getPlayer().dispelBuff(9101004);
                //MapleItemInformationProvider.getInstance().getItemEffect(2100069).applyTo(c.getPlayer());
                //c.getSession().write(CWvsContext.InfoPacket.getStatusMsg(2100069));
            } else {
                SkillFactory.getSkill(9101004).getEffect(1).applyTo(c.getPlayer());
            }
            return 0;
        }
    }

    public static class Heal extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getStat().heal(c.getPlayer());
            c.getPlayer().dispelDebuffs();
            return 0;
        }
    }
    
    public static class 怪物数量 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
             c.getPlayer().dropMessage(6, "當前地圖共 " + monsters.size() + " 怪物");
            return 0;
        }
    }
    
    public static class 地图玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.PLAYER));
             c.getPlayer().dropMessage(6, "當前地圖共 " + monsters.size() + " 怪物");
            return 0;
        }
    }

    public static class HealMap extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    mch.getStat().setHp(mch.getStat().getMaxHp(), mch);
                    mch.updateSingleStat(MapleStat.HP, mch.getStat().getMaxHp());
                    mch.getStat().setMp(mch.getStat().getMaxMp(), mch);
                    mch.updateSingleStat(MapleStat.MP, mch.getStat().getMaxMp());
                    mch.dispelDebuffs();
                }
            }
            return 1;
        }
    }

    public static class 临时封号 extends CommandExecute {

        protected boolean ipBan = false;
        private final String[] types = {"HACK", "BOT", "AD", "HARASS", "CURSE", "SCAM", "MISCONDUCT", "SELL", "ICASH", "TEMP", "GM", "IPROGRAM", "MEGAPHONE"};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                c.getPlayer().dropMessage(6, "<玩家名字> <理由> <时长(小时)>");
                StringBuilder s = new StringBuilder("临时封号理由:");
                for (int i = 0; i < types.length; i++) {
                    s.append(i + 1).append(" - ").append(types[i]).append(", ");
                }
                c.getPlayer().dropMessage(6, s.toString());
                return 0;
            }
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            final int reason = Integer.parseInt(splitted[2]);
            final int numHour = Integer.parseInt(splitted[3]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, numHour);
            final DateFormat df = DateFormat.getInstance();

            if (victim == null || reason < 0 || reason >= types.length) {
                c.getPlayer().dropMessage(6, "无法找到玩家或者理由是无效的, 输入" + splitted[0] + "查看可用理由");
                return 0;
            }
            victim.tempban("已经被 " + c.getPlayer().getName() + " 因为" + types[reason] + "临时封号", cal, reason, ipBan);
            c.getPlayer().dropMessage(6, "玩家 " + splitted[1] + " 被临时封号到 " + df.format(cal.getTime()));
            return 1;
        }
    }

    public static class Ban extends CommandExecute {

        protected boolean hellban = false, ipBan = false;

        private String getCommand() {
            if (hellban) {
                return "HellBan";
            } else {
                return "Ban";
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(5, "[使用方法] !" + getCommand() + " <玩家名称> <原因>");
                c.getPlayer().dropMessage(5, "If you want to consider this ban as an autoban, set the reason \"AutoBan\"");
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            if (hellban) {
                sb.append(StringUtil.joinStringFrom(splitted, 2));
            } else {
                sb.append(StringUtil.joinStringFrom(splitted, 2));
            }
            int ch = World.Find.findChannel(splitted[1]);
            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
            if (target == null) {
                if (MapleCharacter.ban(splitted[1], sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), splitted[0].equals("!hellban"))) {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功离线封锁 " + splitted[1] + ".");
                    World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封号系统]" + splitted[1] + " 使用非法程序进行打猎被管理员永久封号处理。。"));//广播
                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封锁失败 " + splitted[1]);
                    return 0;
                }
            } else if (c.getPlayer().getGMLevel() > target.getGMLevel()) {
                //sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
                if (target.ban(sb.toString(), hellban || ipBan, false, hellban)) {
                    World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封号系统]" + splitted[1] + " 使用非法程序进行打猎被管理员永久封号处理。。"));//广播
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 已经成功封锁 " + splitted[1] + ".");
                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封锁失败.");
                    return 0;
                }
            } else {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] 无法封锁 GM...");
                return 1;
            }
        }
    }

    public static class DC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[splitted.length - 1]);
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
                victim.getClient().getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

                victim.getClient().disconnect(true, false);
                return 1;
            } else {
                c.getPlayer().dropMessage(6, "不存在.");
                return 0;
            }
        }
    }

    public static class 杀死 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "Syntax: !kill <list player names>");
                return 0;
            }
            MapleCharacter victim = null;
            for (int i = 1; i < splitted.length; i++) {
                try {
                    victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[i]);
                } catch (Exception e) {
                    c.getPlayer().dropMessage(6, "Player " + splitted[i] + " not found.");
                }
                if (player.allowedToTarget(victim) && player.getGMLevel() >= victim.getGMLevel()) {
                    victim.getStat().setHp((short) 0, victim);
                    victim.getStat().setMp((short) 0, victim);
                    victim.updateSingleStat(MapleStat.HP, 0);
                    victim.updateSingleStat(MapleStat.MP, 0);
                }
            }
            return 1;
        }
    }

    public static class WhereAmI extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "地图ID： " + c.getPlayer().getMap().getId());
            return 1;
        }
    }

    public static class Online extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String online = "";
            for (int i = 1; i <= ChannelServer.getChannelCount(); i++) {
                online += ChannelServer.getInstance(i).getPlayerStorage().getOnlinePlayers(true);
            }
            c.getPlayer().dropMessage(6, online);
            return 1;
        }
    }

    public static class CharInfo extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final StringBuilder builder = new StringBuilder();
            final MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (other == null) {
                builder.append("物品不存在");
                c.getPlayer().dropMessage(6, builder.toString());
                return 0;
            }
            //if (other.getClient().getLastPing() <= 0) {
            //    other.getClient().sendPing();
            //}
            builder.append(MapleClient.getLogMessage(other, ""));
            builder.append(" at (").append(other.getPosition().x);
            builder.append(", ").append(other.getPosition().y);
            builder.append(")");

            builder.append("\r\nHP : ");
            builder.append(other.getStat().getHp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxHp());

            builder.append(" || MP : ");
            builder.append(other.getStat().getMp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxMp(other.getJob()));

            builder.append(" || BattleshipHP : ");
            builder.append(other.currentBattleshipHP());

            builder.append("\r\nWATK : ");
            builder.append(other.getStat().getTotalWatk());
            builder.append(" || MATK : ");
            builder.append(other.getStat().getTotalMagic());
            builder.append(" || MAXDAMAGE : ");
            builder.append(other.getStat().getCurrentMaxBaseDamage());
            builder.append(" || DAMAGE% : ");
            builder.append(other.getStat().dam_r);
            builder.append(" || BOSSDAMAGE% : ");
            builder.append(other.getStat().bossdam_r);
            builder.append(" || CRIT CHANCE : ");
            builder.append(other.getStat().passive_sharpeye_rate());
            builder.append(" || CRIT DAMAGE : ");
            builder.append(other.getStat().passive_sharpeye_percent());

            builder.append("\r\nSTR : ");
            builder.append(other.getStat().getStr()).append(" + (").append(other.getStat().getTotalStr() - other.getStat().getStr()).append(")");
            builder.append(" || DEX : ");
            builder.append(other.getStat().getDex()).append(" + (").append(other.getStat().getTotalDex() - other.getStat().getDex()).append(")");
            builder.append(" || INT : ");
            builder.append(other.getStat().getInt()).append(" + (").append(other.getStat().getTotalInt() - other.getStat().getInt()).append(")");
            builder.append(" || LUK : ");
            builder.append(other.getStat().getLuk()).append(" + (").append(other.getStat().getTotalLuk() - other.getStat().getLuk()).append(")");

            builder.append("\r\nEXP : ");
            builder.append(other.getExp());
            builder.append(" || MESO : ");
            builder.append(other.getMeso());

            builder.append("\r\nVote Points : ");
            builder.append(other.getVPoints());
            builder.append(" || Event Points : ");
            builder.append(other.getPoints());
            builder.append(" || NX Prepaid : ");
            builder.append(other.getCSPoints(1));

            builder.append("\r\nParty : ");
            builder.append(other.getParty() == null ? -1 : other.getParty().getId());

            builder.append(" || hasTrade: ");
            builder.append(other.getTrade() != null);
            //builder.append(" || Latency: ");
            //builder.append(other.getClient().getLatency());
            //builder.append(" || PING: ");
            //builder.append(other.getClient().getLastPing());
            //builder.append(" || PONG: ");
            //builder.append(other.getClient().getLastPong());
            //builder.append(" || remoteAddress: ");
            //other.getClient().DebugMessage(builder);

            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }

    public static class Cheaters extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<CheaterData> cheaters = World.getCheaters();
            for (int x = cheaters.size() - 1; x >= 0; x--) {
                CheaterData cheater = cheaters.get(x);
                c.getPlayer().dropMessage(6, cheater.getInfo());
            }
            return 1;
        }
    }

    public static class GoTo extends CommandExecute {

        private static final HashMap<String, Integer> gotomaps = new HashMap<>();

        static {
            gotomaps.put("ardent", 910001000);
            gotomaps.put("ariant", 260000100);
            gotomaps.put("amherst", 1010000);
            gotomaps.put("amoria", 680000000);
            gotomaps.put("aqua", 860000000);
            gotomaps.put("aquaroad", 230000000);
            gotomaps.put("boatquay", 541000000);
            gotomaps.put("cwk", 610030000);
            gotomaps.put("edelstein", 310000000);
            gotomaps.put("ellin", 300000000);
            gotomaps.put("ellinia", 101000000);
            gotomaps.put("ellinel", 101071300);
            gotomaps.put("elluel", 101050000);
            gotomaps.put("elnath", 211000000);
            gotomaps.put("erev", 130000000);
            gotomaps.put("florina", 120000300);
            gotomaps.put("fm", 910000000);
            gotomaps.put("future", 271000000);
            gotomaps.put("gmmap", 180000000);
            gotomaps.put("happy", 209000000);
            gotomaps.put("harbor", 104000000);
            gotomaps.put("henesys", 100000000);
            gotomaps.put("herbtown", 251000000);
            gotomaps.put("kampung", 551000000);
            gotomaps.put("kerning", 103000000);
            gotomaps.put("korean", 222000000);
            gotomaps.put("leafre", 240000000);
            gotomaps.put("ludi", 220000000);
            gotomaps.put("malaysia", 550000000);
            gotomaps.put("mulung", 250000000);
            gotomaps.put("nautilus", 120000000);
            gotomaps.put("nlc", 600000000);
            gotomaps.put("omega", 221000000);
            gotomaps.put("orbis", 200000000);
            gotomaps.put("pantheon", 400000000);
            gotomaps.put("pinkbean", 270050100);
            gotomaps.put("phantom", 610010000);
            gotomaps.put("perion", 102000000);
            gotomaps.put("rien", 140000000);
            gotomaps.put("showatown", 801000000);
            gotomaps.put("singapore", 540000000);
            gotomaps.put("sixpath", 104020000);
            gotomaps.put("sleepywood", 105000000);
            gotomaps.put("southperry", 2000000);
            gotomaps.put("stumptown", 866000000);
            gotomaps.put("tot", 270000000);
            gotomaps.put("twilight", 273000000);
            gotomaps.put("tynerum", 301000000);
            gotomaps.put("pianus", 230040420);
            gotomaps.put("horntail", 240060200);
            gotomaps.put("chorntail", 240060201);
            gotomaps.put("griffey", 240020101);
            gotomaps.put("manon", 240020401);
            gotomaps.put("zakum", 280030000);
            gotomaps.put("czakum", 280030001);
            gotomaps.put("pap", 220080001);
            gotomaps.put("oxquiz", 109020001);
            gotomaps.put("ola", 109030101);
            gotomaps.put("fitness", 109040000);
            gotomaps.put("snowball", 109060000);
            gotomaps.put("zipangu", 800000000);
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "Syntax: !goto <mapname>");
            } else if (gotomaps.containsKey(splitted[1])) {
                MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(splitted[1]));
                if (target == null) {
                    c.getPlayer().dropMessage(6, "地图不存在");
                    return 0;
                }
                MaplePortal targetPortal = target.getPortal(0);
                c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[1].equals("locations")) {
                c.getPlayer().dropMessage(6, "Use !goto <location>. 地点如下:");
                StringBuilder sb = new StringBuilder();
                for (String s : gotomaps.keySet()) {
                    sb.append(s).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.substring(0, sb.length() - 2));
            } else {
                c.getPlayer().dropMessage(6, "无效的命令语法 - Use !goto <location>. 对于一个位置列表，使用!去的地方.");
            }
            return 1;
        }
    }

    public static class Clock extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().broadcastMessage(CField.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
            return 1;
        }
    }

    public static class WarpHere extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (c.getPlayer().inPVP() || (!c.getPlayer().isGM() && (victim.isInBlockedMap() || victim.isGM()))) {
                    c.getPlayer().dropMessage(5, "请稍后再试.");
                    return 0;
                }
                victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestPortal(c.getPlayer().getTruePosition()));
            } else {
                int ch = World.Find.findChannel(splitted[1]);
                if (ch < 0) {
                    c.getPlayer().dropMessage(5, "没有找到.");
                    return 0;
                }
                victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim == null || victim.inPVP() || (!c.getPlayer().isGM() && (victim.isInBlockedMap() || victim.isGM()))) {
                    c.getPlayer().dropMessage(5, "请稍后再试.");
                    return 0;
                }
                c.getPlayer().dropMessage(5, "Victim is cross changing channel.");
                victim.dropMessage(5, "Cross changing channel.");
                if (victim.getMapId() != c.getPlayer().getMapId()) {
                    final MapleMap mapp = victim.getClient().getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                    victim.changeMap(mapp, mapp.findClosestPortal(c.getPlayer().getTruePosition()));
                }
                victim.changeChannel(c.getChannel());
            }
            return 1;
        }
    }

    public static class Warp extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel() && !victim.inPVP() && !c.getPlayer().inPVP()) {
                if (splitted.length == 2) {
                    c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getTruePosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "地图不存在");
                        return 0;
                    }
                    MaplePortal targetPortal = null;
                    if (splitted.length > 3) {
                        try {
                            targetPortal = target.getPortal(Integer.parseInt(splitted[3]));
                        } catch (IndexOutOfBoundsException e) {
                            // noop, assume the gm didn't know how many portals there are
                            c.getPlayer().dropMessage(5, "选择了无效的光柱.");
                        } catch (NumberFormatException a) {
                            // noop, assume that the gm is drunk
                        }
                    }
                    if (targetPortal == null) {
                        targetPortal = target.getPortal(0);
                    }
                    victim.changeMap(target, targetPortal);
                }
            } else {
                try {
                    int ch = World.Find.findChannel(splitted[1]);
                    if (ch < 0) {
                        MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        if (target == null) {
                            c.getPlayer().dropMessage(6, "地图不存在");
                            return 0;
                        }
                        MaplePortal targetPortal = null;
                        if (splitted.length > 2) {
                            try {
                                targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                            } catch (IndexOutOfBoundsException e) {
                                // noop, assume the gm didn't know how many portals there are
                                c.getPlayer().dropMessage(5, "选择了无效的光柱.");
                            } catch (NumberFormatException a) {
                                // noop, assume that the gm is drunk
                            }
                        }
                        if (targetPortal == null) {
                            targetPortal = target.getPortal(0);
                        }
                        c.getPlayer().changeMap(target, targetPortal);
                    } else {
                        victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                        c.getPlayer().dropMessage(6, "正在换频道,请等待.");
                        if (victim.getMapId() != c.getPlayer().getMapId()) {
                            final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
                            c.getPlayer().changeMap(mapp, mapp.findClosestPortal(victim.getTruePosition()));
                        }
                        c.getPlayer().changeChannel(ch);
                    }
                } catch (NumberFormatException e) {
                    c.getPlayer().dropMessage(6, "Something went wrong " + e.getMessage());
                    return 0;
                }
            }
            return 1;
        }
    }

    public static class Map extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            try {
                MapleCharacter victim;
                int ch = World.Find.findChannel(splitted[1]);
                if (ch < 0) {
                    MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "地图不存在");
                        return 0;
                    }
                    MaplePortal targetPortal = null;
                    if (splitted.length > 2) {
                        try {
                            targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                        } catch (IndexOutOfBoundsException e) {
                            // noop, assume the gm didn't know how many portals there are
                            c.getPlayer().dropMessage(5, "选择了无效的光柱.");
                        } catch (NumberFormatException a) {
                            // noop, assume that the gm is drunk
                        }
                    }
                    if (targetPortal == null) {
                        targetPortal = target.getPortal(0);
                    }
                    c.getPlayer().changeMap(target, targetPortal);
                } else {
                    victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                    c.getPlayer().dropMessage(6, "正在换频道,请等待.");
                    if (victim.getMapId() != c.getPlayer().getMapId()) {
                        final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
                        c.getPlayer().changeMap(mapp, mapp.findClosestPortal(victim.getTruePosition()));
                    }
                    c.getPlayer().changeChannel(ch);
                }
            } catch (NumberFormatException e) {
                c.getPlayer().dropMessage(6, "Something went wrong " + e.getMessage());
                return 0;
            }
            return 1;
        }
    }

    public static class Say extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (!c.getPlayer().isGM()) {
                    sb.append("Intern ");
                }
                sb.append(c.getPlayer().getName());
                sb.append("] ");
                sb.append(StringUtil.joinStringFrom(splitted, 1));
                World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(c.getPlayer().isGM() ? 6 : 5, sb.toString()));
            } else {
                c.getPlayer().dropMessage(6, "Syntax: say <message>");
                return 0;
            }
            return 1;
        }
    }

    public static class Find extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length == 1) {
                c.getPlayer().dropMessage(6, splitted[0] + ": <NPC> <MOB> <ITEM> <MAP> <SKILL> <QUEST> <HEADER/OPCODE>");
            } else if (splitted.length == 2) {
                c.getPlayer().dropMessage(6, "提供一些搜索.");
            } else {
                String type = splitted[1];
                String search = StringUtil.joinStringFrom(splitted, 2);
                MapleData data;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/String.wz"));
                StringBuilder sb = new StringBuilder();
                sb.append("<<" + "Type: ").append(type).append(" | " + "Search: ").append(search).append(">>");

                if (type.equalsIgnoreCase("NPC")) {
                    List<String> retNpcs = new ArrayList<>();
                    data = dataProvider.getData("Npc.img");
                    List<Pair<Integer, String>> npcPairList = new LinkedList<>();
                    for (MapleData npcIdData : data.getChildren()) {
                        npcPairList.add(new Pair<>(Integer.parseInt(npcIdData.getName()), MapleDataTool.getString(npcIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> npcPair : npcPairList) {
                        if (npcPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retNpcs.add("\r\n" + npcPair.getLeft() + " - " + npcPair.getRight());
                        }
                    }
                    if (retNpcs != null && retNpcs.size() > 0) {
                        for (String singleRetNpc : retNpcs) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的m.");
                                break;
                            }
                            sb.append(singleRetNpc.toString());
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retNpcs.toString(), "00 00", (byte) 0, 9010000));
                            //c.getPlayer().dropMessage(6, singleRetNpc);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "No NPC's Found");
                    }

                } else if (type.equalsIgnoreCase("MAP")) {
                    List<String> retMaps = new ArrayList<>();
                    data = dataProvider.getData("Map.img");
                    List<Pair<Integer, String>> mapPairList = new LinkedList<>();
                    for (MapleData mapAreaData : data.getChildren()) {
                        for (MapleData mapIdData : mapAreaData.getChildren()) {
                            mapPairList.add(new Pair<>(Integer.parseInt(mapIdData.getName()), MapleDataTool.getString(mapIdData.getChildByPath("streetName"), "NO-NAME") + " - " + MapleDataTool.getString(mapIdData.getChildByPath("mapName"), "NO-NAME")));
                        }
                    }
                    for (Pair<Integer, String> mapPair : mapPairList) {
                        if (mapPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMaps.add("\r\n" + mapPair.getLeft() + " - " + mapPair.getRight());
                        }
                    }
                    if (retMaps != null && retMaps.size() > 0) {
                        for (String singleRetMap : retMaps) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的.");
                                break;
                            }
                            sb.append(singleRetMap);
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retMaps.toString(), "00 00", (byte) 0, 9010000));
                            //c.getPlayer().dropMessage(6, singleRetMap);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "没有地图");
                    }
                } else if (type.equalsIgnoreCase("MOB")) {
                    List<String> retMobs = new ArrayList<>();
                    data = dataProvider.getData("Mob.img");
                    List<Pair<Integer, String>> mobPairList = new LinkedList<>();
                    for (MapleData mobIdData : data.getChildren()) {
                        mobPairList.add(new Pair<>(Integer.parseInt(mobIdData.getName()), MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
                    }
                    for (Pair<Integer, String> mobPair : mobPairList) {
                        if (mobPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                            retMobs.add("\r\n" + mobPair.getLeft() + " - " + mobPair.getRight());
                        }
                    }
                    if (retMobs != null && retMobs.size() > 0) {
                        for (String singleRetMob : retMobs) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的.");
                                break;
                            }
                            sb.append(singleRetMob);
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retMobs.toString(), "00 00", (byte) 0, 9010000));
                            //c.getPlayer().dropMessage(6, singleRetMob);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "没有怪物发现");
                    }

                } else if (type.equalsIgnoreCase("ITEM")) {
                    List<String> retItems = new ArrayList<>();
                    for (ItemInformation itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair != null && itemPair.name != null && itemPair.name.toLowerCase().contains(search.toLowerCase())) {
                            retItems.add("\r\n" + itemPair.itemId + " - " + itemPair.name);
                        }
                    }
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的.");
                                break;
                            }
                            sb.append(singleRetItem);
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retItems.toString(), "00 00", (byte) 0, 9010000));
                            //c.getPlayer().dropMessage(6, singleRetItem);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "没有找到物品");
                    }
                } else if (type.equalsIgnoreCase("QUEST")) {
                    List<String> retQuests = new ArrayList<>();
                    for (MapleQuest questPair : MapleQuest.getAllInstances()) {
                        if (questPair.getName().length() > 0 && questPair.getName().toLowerCase().contains(search.toLowerCase())) {
                            retQuests.add("\r\n" + questPair.getId() + " - " + questPair.getName());
                        }
                    }
                    if (retQuests != null && retQuests.size() > 0) {
                        for (String singleRetQuest : retQuests) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的.");
                                break;
                            }
                            sb.append(singleRetQuest);
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retQuests.toString(), "00 00", (byte) 0, 9010000));
                            //    c.getPlayer().dropMessage(6, singleRetItem);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "没有任务发现");
                    }
                } else if (type.equalsIgnoreCase("SKILL")) {
                    List<String> retSkills = new ArrayList<>();
                    for (Skill skill : SkillFactory.getAllSkills()) {
                        if (skill.getName() != null && skill.getName().toLowerCase().contains(search.toLowerCase())) {
                            retSkills.add("\r\n" + skill.getId() + " - " + skill.getName());
                        }
                    }
                    if (retSkills != null && retSkills.size() > 0) {
                        for (String singleRetSkill : retSkills) {
                            if (sb.length() > 10000) {
                                sb.append("\r\n有太多的结果，并不能显示所有的.");
                                break;
                            }
                            sb.append(singleRetSkill);
                            //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, retSkills.toString(), "00 00", (byte) 0, 9010000));
                            //    c.getPlayer().dropMessage(6, singleRetSkill);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "发现没有技能");
                    }
                } else if (type.equalsIgnoreCase("HEADER") || type.equalsIgnoreCase("OPCODE")) {
                    List<String> headers = new ArrayList<>();
                    headers.add("\r\nSend Opcodes:");
                    for (SendPacketOpcode send : SendPacketOpcode.values()) {
                        if (send.name() != null && send.name().toLowerCase().contains(search.toLowerCase())) {
                            headers.add("\r\n" + send.name() + " Value: " + send.getValue() + " Hex: " + HexTool.getOpcodeToString(send.getValue()));
                        }
                    }
                    headers.add("\r\nRecv Opcodes:");
                    for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
                        if (recv.name() != null && recv.name().toLowerCase().contains(search.toLowerCase())) {
                            headers.add("\r\n" + recv.name() + " Value: " + recv.getValue() + " Hex: " + HexTool.getOpcodeToString(recv.getValue()));
                        }
                    }
                    for (String header : headers) {
                        if (sb.length() > 10000) {
                            sb.append("\r\n有太多的结果，并不能显示所有的.");
                            break;
                        }
                        sb.append(header);
                        //c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, headers.toString(), "00 00", (byte) 0, 9010000));
                        //c.getPlayer().dropMessage(6, header);
                    }
                } else {
                    c.getPlayer().dropMessage(6, "对不起，该搜索调用不可用");
                }
                c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, sb.toString(), "00 00", (byte) 0, 9010000));
            }
            return 0;
        }
    }

    public static class ID extends Find {
    }

    public static class LookUp extends Find {
    }

    public static class Search extends Find {
    }

    public static class WhosFirst extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            //probably bad way to do it
            final long currentTime = System.currentTimeMillis();
            List<Pair<String, Long>> players = new ArrayList<>();
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (!chr.isIntern()) {
                    players.add(new Pair<>(MapleCharacterUtil.makeMapleReadable(chr.getName()) + (currentTime - chr.getCheatTracker().getLastAttack() > 600000 ? " (AFK)" : ""), chr.getChangeTime()));
                }
            }
            Collections.sort(players, new WhoComparator());
            StringBuilder sb = new StringBuilder("List of people in this map in order, counting AFK (10 minutes):  ");
            for (Pair<String, Long> z : players) {
                sb.append(z.left).append(", ");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }

        public static class WhoComparator implements Comparator<Pair<String, Long>>, Serializable {

            @Override
            public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
                if (o1.right > o2.right) {
                    return 1;
                } else if (Objects.equals(o1.right, o2.right)) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    }

    public static class WhosLast extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                StringBuilder sb = new StringBuilder("whoslast [type] where type can be:  ");
                for (MapleSquadType t : MapleSquadType.values()) {
                    sb.append(t.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            final MapleSquadType t = MapleSquadType.valueOf(splitted[1].toLowerCase());
            if (t == null) {
                StringBuilder sb = new StringBuilder("whoslast [type] where type can be:  ");
                for (MapleSquadType z : MapleSquadType.values()) {
                    sb.append(z.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            if (t.queuedPlayers.get(c.getChannel()) == null) {
                c.getPlayer().dropMessage(6, "The queue has not been initialized in this channel yet.");
                return 0;
            }
            c.getPlayer().dropMessage(6, "Queued players: " + t.queuedPlayers.get(c.getChannel()).size());
            StringBuilder sb = new StringBuilder("List of participants:  ");
            for (Pair<String, String> z : t.queuedPlayers.get(c.getChannel())) {
                sb.append(z.left).append('(').append(z.right).append(')').append(", ");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }
    }

    public static class WhosNext extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                StringBuilder sb = new StringBuilder("whosnext [type] where type can be:  ");
                for (MapleSquadType t : MapleSquadType.values()) {
                    sb.append(t.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            final MapleSquadType t = MapleSquadType.valueOf(splitted[1].toLowerCase());
            if (t == null) {
                StringBuilder sb = new StringBuilder("whosnext [type] where type can be:  ");
                for (MapleSquadType z : MapleSquadType.values()) {
                    sb.append(z.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            if (t.queue.get(c.getChannel()) == null) {
                c.getPlayer().dropMessage(6, "The queue has not been initialized in this channel yet.");
                return 0;
            }
            c.getPlayer().dropMessage(6, "Queued players: " + t.queue.get(c.getChannel()).size());
            StringBuilder sb = new StringBuilder("List of participants:  ");
            final long now = System.currentTimeMillis();
            for (Pair<String, Long> z : t.queue.get(c.getChannel())) {
                sb.append(z.left).append('(').append(StringUtil.getReadableMillis(z.right, now)).append(" ago),");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }
    }

    public static class WarpMap extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            try {
                final MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                if (target == null) {
                    c.getPlayer().dropMessage(6, "地图不存在");
                    return 0;
                }
                final MapleMap from = c.getPlayer().getMap();
                for (MapleCharacter chr : from.getCharactersThreadsafe()) {
                    chr.changeMap(target, target.getPortal(0));
                }
            } catch (NumberFormatException e) {
                c.getPlayer().dropMessage(5, "Error: " + e.getMessage());
                return 0; //assume drunk GM
            }
            return 1;
        }
    }

    public static class KillAll extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;

            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
                }
            }
            if (map == null) {
                c.getPlayer().dropMessage(6, "地图不存在");
                return 0;
            }
            MapleMonster mob;
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (!mob.getStats().isBoss() || mob.getStats().isPartyBonus() || c.getPlayer().isGM()) {
                    map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
                }
            }
            return 1;
        }
    }

    public static class 清怪爆物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;

            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
                }
            }
            if (map == null) {
                c.getPlayer().dropMessage(6, "地图不存在");
                return 0;
            }
            MapleMonster mob;
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (!mob.getStats().isBoss() || mob.getStats().isPartyBonus() || c.getPlayer().isGM()) {
                    map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
                }
            }
            return 1;
        }
    }

    public static class 全图捡物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final List<MapleMapObject> items = c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), GameConstants.maxViewRangeSq(), Arrays.asList(MapleMapObjectType.ITEM));
            MapleMapItem mapitem;
            for (MapleMapObject item : items) {
                mapitem = (MapleMapItem) item;
                if (mapitem.getMeso() > 0) {
                    c.getPlayer().gainMeso(mapitem.getMeso(), true);
                } else if (mapitem.getItem() == null || !MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                    continue;
                }
                mapitem.setPickedUp(true);
                c.getPlayer().getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 2, c.getPlayer().getId()), mapitem.getPosition());
                c.getPlayer().getMap().removeMapObject(item);

            }
            return 1;
        }
    }

    public static class CancelBuffs extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().cancelAllBuffs();
            return 1;
        }
    }

    public static class CC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().changeChannel(Integer.parseInt(splitted[1]));
            return 1;
        }
    }

    public static class Reports extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<CheaterData> cheaters = World.getReports();
            for (int x = cheaters.size() - 1; x >= 0; x--) {
                CheaterData cheater = cheaters.get(x);
                c.getPlayer().dropMessage(6, cheater.getInfo());
            }
            return 1;
        }
    }

    public static class ClearReport extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                StringBuilder ret = new StringBuilder("report [ign] [all/");
                for (ReportType type : ReportType.values()) {
                    ret.append(type.theId).append('/');
                }
                ret.setLength(ret.length() - 1);
                c.getPlayer().dropMessage(6, ret.append(']').toString());
                return 0;
            }
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                c.getPlayer().dropMessage(5, "不存在");
                return 0;
            }
            final ReportType type = ReportType.getByString(splitted[2]);
            if (type != null) {
                victim.clearReports(type);
            } else {
                victim.clearReports();
            }
            c.getPlayer().dropMessage(5, "多恩.");
            return 1;
        }
    }

    public static class FakeRelog extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().fakeRelog();
            return 1;
        }
    }

    public static class Fly extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            SkillFactory.getSkill(1146).getEffect(1).applyTo(c.getPlayer());
            SkillFactory.getSkill(1142).getEffect(1).applyTo(c.getPlayer());
            c.getPlayer().dispelBuff(1146);
            return 1;
        }
    }

    public static class OpenNpc extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, Integer.parseInt(splitted[1]), splitted.length > 2 ? StringUtil.joinStringFrom(splitted, 2) : splitted[1]);
            return 1;
        }
    }

    public static class OpenShop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory.getInstance().getShop(Integer.parseInt(splitted[1]));
            return 1;
        }
    }

    public static class ClearDrops extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().removeDrops();
            return 1;
        }
    }

    public static class Shop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId = Integer.parseInt(splitted[1]);
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            }
            return 1;
        }
    }

    public static class Job extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int jobid = Integer.parseInt(splitted[1]);
            if (!MapleJob.isExist(jobid)) {
                c.getPlayer().dropMessage(5, "无效的职业");
                return 0;
            }
            c.getPlayer().changeJob((short) jobid);
            c.getPlayer().setSubcategory(c.getPlayer().getSubcategory());
            return 1;
        }
    }

    public static class 杀死附近玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            List<MapleMapObject> players = map.getMapObjectsInRange(c.getPlayer().getPosition(), 25000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
                if (playernear.isAlive() && playernear != c.getPlayer() && playernear.getJob() != 910) {
                    playernear.setHp(0);
                    playernear.updateSingleStat(MapleStat.HP, 0);
                    playernear.dropMessage(5, "You were too close to the MapleGM.");
                }
            }
            return 1;
        }
    }

    public static class ManualEvent extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getChannelServer().manualEvent(c.getPlayer())) {
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    //chrs.dropMessage(0, "MapleGM is hosting an event! Use the @joinevent command to join the event!");
                    //chrs.dropMessage(0, "Event Map: " + c.getPlayer().getMap().getMapName());
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(25, 0, "MapleGM is hosting an event! Use the @joinevent command to join the event!"));
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(26, 0, "Event Map: " + c.getPlayer().getMap().getMapName()));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 25, c.getChannel(), "Event : MapleGM is hosting an event! Use the @joinevent command to join the event!"));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 26, c.getChannel(), "Event : Event Channel: " + c.getChannel() + " Event Map: " + c.getPlayer().getMap().getMapName()));
                }
            } else {
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(22, 0, "Enteries to the GM event are closed. The event has began!"));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Enteries to the GM event are closed. The event has began!"));
                }
            }
            return 1;
        }
    }

    public static class ActiveBomberman extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.getMapId() != 109010100) {
                player.dropMessage(5, "此命令只在地图中使用 109010100.");
            } else {
                c.getChannelServer().toggleBomberman(c.getPlayer());
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    if (!chr.isIntern()) {
                        chr.cancelAllBuffs();
                        chr.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, 1));
                        //MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, 2100067, chr.getItemQuantity(2100067, false), true, true);
                        //chr.gainItem(2100067, 30);
                        //MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.ETC, 4031868, chr.getItemQuantity(4031868, false), true, true);
                        //chr.gainItem(4031868, (short) 5);
                        //chr.dropMessage(0, "You have been granted 5 jewels(lifes) and 30 bombs.");
                        //chr.dropMessage(0, "Pick up as many bombs and jewels as you can!");
                        //chr.dropMessage(0, "Check inventory for Bomb under use");
                    }
                }
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Bomberman event has started!"));
                }
                player.getMap().broadcastMessage(CField.getClock(60));
            }
            return 1;
        }
    }

    public static class DeactiveBomberman extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.getMapId() != 109010100) {
                player.dropMessage(5, "此命令只在地图中使用 109010100.");
            } else {
                c.getChannelServer().toggleBomberman(c.getPlayer());
                int count = 0;
                String winner = "";
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    if (!chr.isGM()) {
                        if (count == 0) {
                            winner = chr.getName();
                            count++;
                        } else {
                            winner += " , " + chr.getName();
                        }
                    }
                }
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Bomberman event has ended! The winners are: " + winner));
                }
            }
            return 1;
        }
    }

    public static class ClearInv extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2 || player.hasBlockedInventory()) {
                c.getPlayer().dropMessage(5, "!clearinv <eq / use / setup / etc / cash / all >");
                return 0;
            } else {
                MapleInventoryType type;
                if (splitted[1].equalsIgnoreCase("eq")) {
                    type = MapleInventoryType.EQUIP;
                } else if (splitted[1].equalsIgnoreCase("use")) {
                    type = MapleInventoryType.USE;
                } else if (splitted[1].equalsIgnoreCase("setup")) {
                    type = MapleInventoryType.SETUP;
                } else if (splitted[1].equalsIgnoreCase("etc")) {
                    type = MapleInventoryType.ETC;
                } else if (splitted[1].equalsIgnoreCase("cash")) {
                    type = MapleInventoryType.CASH;
                } else if (splitted[1].equalsIgnoreCase("all")) {
                    type = null;
                } else {
                    c.getPlayer().dropMessage(5, "Invalid. @clearslot <eq / use / setup / etc / cash / all >");
                    return 0;
                }
                if (type == null) { //All, a bit hacky, but it's okay 
                    MapleInventoryType[] invs = {MapleInventoryType.EQUIP, MapleInventoryType.USE, MapleInventoryType.SETUP, MapleInventoryType.ETC, MapleInventoryType.CASH};
                    for (MapleInventoryType t : invs) {
                        type = t;
                        MapleInventory inv = c.getPlayer().getInventory(type);
                        byte start = -1;
                        for (byte i = 0; i < inv.getSlotLimit(); i++) {
                            if (inv.getItem(i) != null) {
                                start = i;
                                break;
                            }
                        }
                        if (start == -1) {
                            c.getPlayer().dropMessage(5, "库存中没有项目.");
                            return 0;
                        }
                        int end = 0;
                        for (byte i = start; i < inv.getSlotLimit(); i++) {
                            if (inv.getItem(i) != null) {
                                MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
                            } else {
                                end = i;
                                break;//Break at first empty space. 
                            }
                        }
                        c.getPlayer().dropMessage(5, "Cleared slots " + start + " to " + end + ".");
                    }
                } else {
                    MapleInventory inv = c.getPlayer().getInventory(type);
                    byte start = -1;
                    for (byte i = 0; i < inv.getSlotLimit(); i++) {
                        if (inv.getItem(i) != null) {
                            start = i;
                            break;
                        }
                    }
                    if (start == -1) {
                        c.getPlayer().dropMessage(5, "库存中没有项目.");
                        return 0;
                    }
                    byte end = 0;
                    for (byte i = start; i < inv.getSlotLimit(); i++) {
                        if (inv.getItem(i) != null) {
                            MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
                        } else {
                            end = i;
                            break;//Break at first empty space. 
                        }
                    }
                    c.getPlayer().dropMessage(5, "Cleared slots " + start + " to " + end + ".");
                }
                return 1;
            }
        }
    }

    public static class debugWindow extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.StartWindow();
            return 1;
        }
    }

    public static class Bob extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = MapleLifeFactory.getMonster(9400551);
            for (int i = 0; i < 10; i++) {
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
            }
            return 1;
        }
    }

    public static class 杀死地图玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter map : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (map != null && !map.isIntern()) {
                    map.getStat().setHp((short) 0, map);
                    map.getStat().setMp((short) 0, map);
                    map.updateSingleStat(MapleStat.HP, 0);
                    map.updateSingleStat(MapleStat.MP, 0);
                }
            }
            return 1;
        }
    }

    public static class ChatType extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            try {
                //c.getPlayer().setChatColour(c.getPlayer().getChatColor() == 0 ? (short) 11 : 0);
                c.getPlayer().setChatType(c.getPlayer().getChatType() == 0 ? (short) 11 : 0);
                c.getPlayer().dropMessage(0, "文本颜色已成功更改.");
            } catch (Exception e) {
                c.getPlayer().dropMessage(0, "发生了一个错误.");
            }
            return 1;
        }
    }

    public static class giveMap extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage("!giveMap <道具代码> <道具数量>");
                return 1;
            }
            int item = Integer.parseInt(splitted[1]);
            int quantity = Integer.parseInt(splitted[2]);
            for (MapleCharacter Mplayer : c.getPlayer().getMap().getCharactersThreadsafe()) {
                Mplayer.gainItem(item, quantity);
                Mplayer.dropMessage("你收到了管理员发送的" + MapleItemInformationProvider.getInstance().getName(item));
            }
            c.getPlayer().getMap().startMapEffect("管理员发送物品 祝你游戏愉快玩的开心！", 5121010);
            return 1;
        }
    }

    public static class 查询洗道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage("开始查询复制装备....");
            OnlyID.getInstance().StartCheckings();
            c.getPlayer().dropMessage("复制装备查询完毕");
            return 1;
        }

    }

    public static class 处理洗道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<Triple<Integer, Long, Long>> OnlyIDList = OnlyID.getData();
            if (OnlyIDList.isEmpty()) {
                c.getPlayer().dropMessage("目前没有复制装备的资料，请输入 !查询洗道具 来获得资料");
                return 1;
            }
            try {
                final ListIterator<Triple<Integer, Long, Long>> OnlyId = OnlyIDList.listIterator();
                while (OnlyId.hasNext()) {
                    Triple<Integer, Long, Long> Only = OnlyId.next();
                    int chr = Only.getLeft();
                    long invetoryitemid = Only.getMid();
                    long equiponlyid = Only.getRight();
                    int ch = World.Find.findChannel(chr);
                    if (ch < 0 && ch != -10) {
                        HandleOffline(c, chr, invetoryitemid, equiponlyid);
                    } else {
                        MapleCharacter chrs = null;
                        if (ch == -10) {
                            chrs = CashShopServer.getPlayerStorage().getCharacterById(chr);
                        } else {
                            chrs = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(chr);
                        }
                        if (chrs == null) {
                            break;
                        }
                        MapleInventoryManipulator.removeAllByEquipOnlyId(chrs.getClient(), equiponlyid);
                    }
                }
                OnlyID.clearData();
            } catch (Exception ex) {
                c.getPlayer().dropMessage("发生错误 " + ex.toString());
            }
            return 1;
        }

        public void HandleOffline(MapleClient c, int chr, long inventoryitemid, long equiponlyid) {
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                String itemname = "null";

                try (PreparedStatement ps = con.prepareStatement("select itemid from inventoryitems WHERE inventoryitemid = ?")) {
                    ps.setLong(1, inventoryitemid);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int itemid = rs.getInt("itemid");
                            itemname = MapleItemInformationProvider.getInstance().getName(itemid);
                        } else {
                            c.getPlayer().dropMessage("发生错误: 流水号无法指向道具代码");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement("Delete from inventoryequipment WHERE inventoryitemid = " + inventoryitemid)) {
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("Delete from inventoryitems WHERE inventoryitemid = ?")) {
                    ps.setLong(1, inventoryitemid);
                    ps.executeUpdate();
                }

                String msgtext = "玩家ID: " + chr + " 在玩家道具中发现复制装备[" + itemname + "]已经将其删除。";
                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM密语] " + msgtext));
                FileoutputUtil.logToFile("Hack/复制装备_已删除.txt", msgtext + " 道具唯一ID: " + equiponlyid);

            } catch (Exception ex) {

            }
        }
    }
}
