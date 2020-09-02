package server;

//import client.LoginCrypto;
import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.OnlyID;
import constants.ServerConfig;
import constants.ServerConstants;
import database.DBConPool;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleDojoRanking;
import handling.channel.MapleGuildRanking;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.guild.MapleGuild;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Timer.BuffTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.life.MobSkillFactory;
import server.life.PlayerNPC;
import server.maps.MapleMapFactory;
import server.quest.MapleQuest;
import tools.CpuSerialTools;
import tools.CustomPlayerRankings;
import tools.FileoutputUtil;
import tools.MacAddressTool;

public class Start {

    public static boolean Check = true;
    public static long startTime = System.currentTimeMillis();
    public static final Start instance = new Start();
    public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);

    /**
     * 此函数由命令行端调用。
     * @throws InterruptedException
     * @throws IOException
     */
    public void run() throws InterruptedException, IOException {
        long start = System.currentTimeMillis();

        /* 载入配置文件 settings.ini，此处貌似没有什么用。
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("settings.ini"));
        } catch (IOException ex) {
            System.out.println("载入设定党 settings.ini 失败");
            System.exit(0);
        }
         */

        /* 貌似是IP授权相关。此处应该没什么用 */
        /*
        if (ServerConfig.sq) {
            String[] qxIp = {"47.97.165.200", "127.0.0.1"};
            String localIp = null;
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (localIp != null) {
                for (int i = 0; i < qxIp.length; i++) {
                    if (!qxIp[i].equals(localIp)) {
                        System.out.println("IP未授權");
                        System.exit(0);
                    }
                }
            } else {
                System.out.println("獲取本機IP失敗。");
                System.exit(0);
            }
        }
         */

        ServerConfig.userLimit = ServerProperties.getProperty("yxh.UserLimit", ServerConfig.userLimit);
        //ServerConfig.interface_ = ServerProperties.getProperty("ip", ServerConfig.interface_);
        ServerConfig.logPackets = ServerProperties.getProperty("logOps", ServerConfig.logPackets);
        ServerConfig.adminOnly = ServerProperties.getProperty("adminOnly", ServerConfig.adminOnly);
        ServerConfig.USE_FIXED_IV = ServerProperties.getProperty("antiSniff", ServerConfig.USE_FIXED_IV);
        ServerConfig.loginPort = ServerProperties.getProperty("yxh.port", ServerConfig.loginPort);
        ServerConfig.channelPort = ServerProperties.getProperty("yxh.Lport", ServerConfig.channelPort);
        ServerConfig.cashShopPort = ServerProperties.getProperty("yxh.商场端口", ServerConfig.channelPort);
        ServerConfig.channelCount = ServerProperties.getProperty("yxh.Count", ServerConfig.channelCount);
        ServerConfig.ExpRate = ServerProperties.getProperty("yxh.Exp", ServerConfig.ExpRate);
        ServerConfig.MesoRate = ServerProperties.getProperty("yxh.Meso", ServerConfig.MesoRate);
        ServerConfig.DropRate = ServerProperties.getProperty("yxh.Drop", ServerConfig.DropRate);
        ServerConfig.BossDropRate = ServerProperties.getProperty("yxh.BDrop", ServerConfig.BossDropRate);
        ServerConstants.自动注册 = ServerProperties.getProperty("yxh.AutoRegister", ServerConstants.自动注册);
        ServerConfig.maxLevel = ServerProperties.getProperty("yxh.等级限制", ServerConfig.maxLevel);
        ServerConfig.charslots = ServerProperties.getProperty("yxh.MaxCharacters", ServerConfig.charslots);
        ServerConfig.serverName = ServerProperties.getProperty("yxh.ServerName", "冒险岛");
        ServerConfig.eventMessage = ServerProperties.getProperty("yxh.EventMessage", "冒险岛");
        ServerConfig.scrollingMessage = ServerProperties.getProperty("yxh.ServerMessage", "");
        ServerConfig.koqst = ServerProperties.getProperty("yxh.koqst", ServerConfig.koqst);
        ServerConfig.kozs = ServerProperties.getProperty("yxh.kozs", ServerConfig.kozs);
        ServerConfig.mxjcs = ServerProperties.getProperty("yxh.冒险家出生", ServerConfig.mxjcs);
        ServerConfig.qstcs = ServerProperties.getProperty("yxh.骑士团出生", ServerConfig.qstcs);
        ServerConfig.zscs = ServerProperties.getProperty("yxh.战神出生", ServerConfig.zscs);
        ServerConfig.events = ServerProperties.getProperty("yxh.Events", ServerConfig.events);
        ServerConfig.dyjy = ServerProperties.getProperty("yxh.钓鱼经验倍率", ServerConfig.dyjy);
        ServerConfig.dyjb = ServerProperties.getProperty("yxh.钓鱼金钱倍率", ServerConfig.dyjb);
        ServerConfig.pdmap = ServerProperties.getProperty("yxh.泡点地图", ServerConfig.pdmap);
        ServerConfig.pdexp = ServerProperties.getProperty("yxh.获得经验等级X倍率", ServerConfig.pdexp);
        ServerConfig.pdzsjb = ServerProperties.getProperty("yxh.获得金币最少量", ServerConfig.pdzsjb);
        ServerConfig.pdzdjb = ServerProperties.getProperty("yxh.获得金币最大量", ServerConfig.pdzdjb);
        ServerConfig.pdzsdy = ServerProperties.getProperty("yxh.获得抵用券最少量", ServerConfig.pdzsdy);
        ServerConfig.pdzddy = ServerProperties.getProperty("yxh.获得抵用券最大量", ServerConfig.pdzddy);
        ServerConfig.pdzsdj = ServerProperties.getProperty("yxh.获得点券最少量", ServerConfig.pdzsdj);
        ServerConfig.pdzddj = ServerProperties.getProperty("yxh.获得点券最大量", ServerConfig.pdzddj);
        ServerConfig.pdzsdd = ServerProperties.getProperty("yxh.获得豆豆最少量", ServerConfig.pdzsdd);
        ServerConfig.pdzddd = ServerProperties.getProperty("yxh.获得豆豆最大量", ServerConfig.pdzddd);
        ServerConfig.pbdt = ServerProperties.getProperty("yxh.pbdt", ServerConfig.pbdt);
        ServerConfig.killnpc = ServerProperties.getProperty("yxh.killnpc", ServerConfig.killnpc);

        // 数据库连接相关配置
        ServerConstants.SQL_PORT = ServerProperties.getProperty("sql_port", ServerConstants.SQL_PORT);
        ServerConstants.SQL_USER = ServerProperties.getProperty("sql_user", ServerConstants.SQL_USER);
        ServerConstants.SQL_PASSWORD = ServerProperties.getProperty("sql_password", ServerConstants.SQL_PASSWORD);
        ServerConstants.SQL_DATABASE = ServerProperties.getProperty("sql_db", ServerConstants.SQL_DATABASE);

        //System.setProperty("wzpath", p.getProperty("wzpath"));
        if (ServerConfig.adminOnly || ServerConstants.Use_Localhost) {
            System.out.println("管理员模式开启");
        }

        /* 尝试进行数据库链接。 此处将对所有的account登录状态进行
         * 复位。 */
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET loggedin = 0")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("执行中出现异常 - 无法连线到数据库." + ex);
        }

        System.out.println("正在载入 MapleSNDA_V079");
        World.init();
        System.out.println("主机位置: " + ServerConfig.interface_ + ":" + LoginServer.PORT);
        System.out.println("客户端版本: " + ServerConstants.MAPLE_VERSION + "." + ServerConstants.MAPLE_PATCH);
        //System.out.println("Source Revision: " + ServerConstants.SOURCE_REVISION);
        System.out.println("主要世界: " + ServerConstants.MAIN_WORLD);

        System.out.println("正在加载定时器...");
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        CloneTimer.getInstance().start();
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
        PingTimer.getInstance().start();

        System.out.println("正在加载排行榜...");
        MapleDojoRanking.getInstance().load();
        MapleGuildRanking.getInstance().load();

        System.out.println("正在加载家族...");
        MapleGuild.loadAll();

        System.out.println("正在加载学院...");
        MapleFamily.loadAll();

        System.out.println("正在加载任务...");
        MapleLifeFactory.loadQuestCounts();
        MapleQuest.initQuests();
        MapleOxQuizFactory.getInstance();

        System.out.println("正在加载道具...");
        MapleItemInformationProvider.getInstance().runEtc();
        MapleMonsterInformationProvider.getInstance().load();  // 怪物
        MapleItemInformationProvider.getInstance().runItems();

        System.out.println("正在加载技能...");
        SkillFactory.load();
        MobSkillFactory.getInstance();
        MapleCarnivalFactory.getInstance();

        System.out.println("正在加载奖励...");
        RandomRewards.load();

        System.out.println("正在加载其他配置...");
        LoginInformationProvider.getInstance();
        SpeedRunner.loadSpeedRuns();
        MapleInventoryIdentifier.getInstance();
        MapleMapFactory.loadCustomLife();

        // 月光成就。
        PreparedStatement ps;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ps = con.prepareStatement("DELETE FROM `moonlightachievements` where achievementid > 0;");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("moonlightachievements" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }

        // 现金道具
        CashItemFactory.getInstance().initialize();

        // 服务器协议响应回调 初始化
        MapleServerHandler.initiate();
        // LoginServer初始化.
        LoginServer.run_startup_configurations();

        // ChannelServer 初始化并启动。
        ChannelServer.startChannel_Main();
        // 现金商城服务器初始化并启动。
        CashShopServer.run_startup_configurations();
        //FarmServer.run_startup_configurations();

        // 服务器shutdown回调。
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        // 怪物重生。刷新每个地图的怪物。
        World.registerRespawn ();
        // 关闭服务器的相关操作初始化。
        ShutdownServer.registerMBean();
        // NPC信息加载。
        PlayerNPC.loadAll();
        MapleMonsterInformationProvider.getInstance().addExtra();
        LoginServer.setOn();
        RankingWorker.run();
        //System.out.println("Event Script List: " + ServerConfig.getEventList());
        if (ServerConfig.logPackets) {
            System.out.println("数据包记录已开启.");
        }
        if (ServerConfig.USE_FIXED_IV) {
            System.out.println("反抓包已开启.");
        }

        /*if (ServerConfig.checkCopyItem) {
         checkCopyItemFromSql();
         }*/
        CustomPlayerRankings.getInstance().load();
        long now = System.currentTimeMillis() - start;
        long seconds = now / 1000;
        long ms = now % 1000;
        System.out.println("开启一共花了: " + seconds + "秒 " + ms + "毫秒");
        OnlyID.getInstance();  // 暂不知何物。
    }

    /**
     * 此函数由窗口程序的中的“服务器启动”按钮调用。
     * 在命令行端不调用此函数。
     * @throws InterruptedException
     */
    public void startServer() throws InterruptedException {
        Check = false;
        long start = System.currentTimeMillis();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("settings.ini"));
        } catch (IOException ex) {
            System.out.println("载入设定党 settings.ini 失败");
            System.exit(0);
        }
        if (ServerConfig.sq) {
            String[] qxIp = {"47.97.165.200", "127.0.0.1"};
            String localIp = null;
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (localIp != null) {
                for (int i = 0; i < qxIp.length; i++) {
                    if (!qxIp[i].equals(localIp)) {
                        System.out.println("IP未授權");
                        System.exit(0);
                    }
                }
            } else {
                System.out.println("獲取本機IP失敗。");
                System.exit(0);
            }
        }
        ServerConfig.userLimit = ServerProperties.getProperty("yxh.UserLimit", ServerConfig.userLimit);
        //ServerConfig.interface_ = ServerProperties.getProperty("ip", ServerConfig.interface_);
        ServerConfig.logPackets = ServerProperties.getProperty("logOps", ServerConfig.logPackets);
        ServerConfig.adminOnly = ServerProperties.getProperty("adminOnly", ServerConfig.adminOnly);
        ServerConfig.USE_FIXED_IV = ServerProperties.getProperty("antiSniff", ServerConfig.USE_FIXED_IV);
        ServerConfig.loginPort = ServerProperties.getProperty("yxh.port", ServerConfig.loginPort);
        ServerConfig.channelPort = ServerProperties.getProperty("yxh.Lport", ServerConfig.channelPort);
        ServerConfig.cashShopPort = ServerProperties.getProperty("yxh.商场端口", ServerConfig.channelPort);
        ServerConfig.channelCount = ServerProperties.getProperty("yxh.Count", ServerConfig.channelCount);
        ServerConfig.ExpRate = ServerProperties.getProperty("yxh.Exp", ServerConfig.ExpRate);
        ServerConfig.MesoRate = ServerProperties.getProperty("yxh.Meso", ServerConfig.MesoRate);
        ServerConfig.DropRate = ServerProperties.getProperty("yxh.Drop", ServerConfig.DropRate);
        ServerConfig.BossDropRate = ServerProperties.getProperty("yxh.BDrop", ServerConfig.BossDropRate);
        ServerConstants.自动注册 = ServerProperties.getProperty("yxh.AutoRegister", ServerConstants.自动注册);
        ServerConfig.maxLevel = ServerProperties.getProperty("yxh.等级限制", ServerConfig.maxLevel);
        ServerConfig.charslots = ServerProperties.getProperty("yxh.MaxCharacters", ServerConfig.charslots);
        ServerConfig.serverName = ServerProperties.getProperty("yxh.ServerName", "冒险岛");
        ServerConfig.eventMessage = ServerProperties.getProperty("yxh.EventMessage", "冒险岛");
        ServerConfig.scrollingMessage = ServerProperties.getProperty("yxh.ServerMessage", "");
        ServerConfig.koqst = ServerProperties.getProperty("yxh.koqst", ServerConfig.koqst);
        ServerConfig.kozs = ServerProperties.getProperty("yxh.kozs", ServerConfig.kozs);
        ServerConfig.mxjcs = ServerProperties.getProperty("yxh.冒险家出生", ServerConfig.mxjcs);
        ServerConfig.qstcs = ServerProperties.getProperty("yxh.骑士团出生", ServerConfig.qstcs);
        ServerConfig.zscs = ServerProperties.getProperty("yxh.战神出生", ServerConfig.zscs);
        ServerConfig.events = ServerProperties.getProperty("yxh.Events", ServerConfig.events);
        ServerConfig.dyjy = ServerProperties.getProperty("yxh.钓鱼经验倍率", ServerConfig.dyjy);
        ServerConfig.dyjb = ServerProperties.getProperty("yxh.钓鱼金钱倍率", ServerConfig.dyjb);
        ServerConfig.pdmap = ServerProperties.getProperty("yxh.泡点地图", ServerConfig.pdmap);
        ServerConfig.pdexp = ServerProperties.getProperty("yxh.获得经验等级X倍率", ServerConfig.pdexp);
        ServerConfig.pdzsjb = ServerProperties.getProperty("yxh.获得金币最少量", ServerConfig.pdzsjb);
        ServerConfig.pdzdjb = ServerProperties.getProperty("yxh.获得金币最大量", ServerConfig.pdzdjb);
        ServerConfig.pdzsdy = ServerProperties.getProperty("yxh.获得抵用券最少量", ServerConfig.pdzsdy);
        ServerConfig.pdzddy = ServerProperties.getProperty("yxh.获得抵用券最大量", ServerConfig.pdzddy);
        ServerConfig.pdzsdj = ServerProperties.getProperty("yxh.获得点券最少量", ServerConfig.pdzsdj);
        ServerConfig.pdzddj = ServerProperties.getProperty("yxh.获得点券最大量", ServerConfig.pdzddj);
        ServerConfig.pdzsdd = ServerProperties.getProperty("yxh.获得豆豆最少量", ServerConfig.pdzsdd);
        ServerConfig.pdzddd = ServerProperties.getProperty("yxh.获得豆豆最大量", ServerConfig.pdzddd);
        ServerConfig.pbdt = ServerProperties.getProperty("yxh.pbdt", ServerConfig.pbdt);
        ServerConfig.killnpc = ServerProperties.getProperty("yxh.killnpc", ServerConfig.killnpc);
        // 数据库连接设定
        ServerConstants.SQL_PORT = ServerProperties.getProperty("sql_port", ServerConstants.SQL_PORT);
        ServerConstants.SQL_USER = ServerProperties.getProperty("sql_user", ServerConstants.SQL_USER);
        ServerConstants.SQL_PASSWORD = ServerProperties.getProperty("sql_password", ServerConstants.SQL_PASSWORD);
        ServerConstants.SQL_DATABASE = ServerProperties.getProperty("sql_db", ServerConstants.SQL_DATABASE);

        //System.setProperty("wzpath", p.getProperty("wzpath"));
        if (ServerConfig.adminOnly || ServerConstants.Use_Localhost) {
            System.out.println("管理员模式开启");
        }
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET loggedin = 0")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("执行中出现异常 - 无法连线到数据库." + ex);
        }

        System.out.println("正在载入 MapleSNDA_V079");
        World.init();
        System.out.println("主机位置: " + ServerConfig.interface_ + ":" + LoginServer.PORT);
        System.out.println("客户端版本: " + ServerConstants.MAPLE_VERSION + "." + ServerConstants.MAPLE_PATCH);
        //System.out.println("Source Revision: " + ServerConstants.SOURCE_REVISION);
        System.out.println("主要世界: " + ServerConstants.MAIN_WORLD);

        System.out.println("正在加载定时器...");
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        CloneTimer.getInstance().start();
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
        PingTimer.getInstance().start();
        System.out.println("正在加载排行榜...");
        MapleDojoRanking.getInstance().load();
        MapleGuildRanking.getInstance().load();
        System.out.println("正在加载家族...");
        MapleGuild.loadAll();
        System.out.println("正在加载学院...");
        MapleFamily.loadAll();
        System.out.println("正在加载任务...");
        MapleLifeFactory.loadQuestCounts();
        MapleQuest.initQuests();
        MapleOxQuizFactory.getInstance();
        System.out.println("正在加载道具...");
        MapleItemInformationProvider.getInstance().runEtc();
        MapleMonsterInformationProvider.getInstance().load();
        MapleItemInformationProvider.getInstance().runItems();
        System.out.println("正在加载技能...");
        SkillFactory.load();
        MobSkillFactory.getInstance();
        MapleCarnivalFactory.getInstance();
        System.out.println("正在加载奖励...");
        RandomRewards.load();
        System.out.println("正在加载其他配置...");
        LoginInformationProvider.getInstance();
        SpeedRunner.loadSpeedRuns();
        MapleInventoryIdentifier.getInstance();
        MapleMapFactory.loadCustomLife();
        PreparedStatement ps;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ps = con.prepareStatement("DELETE FROM `moonlightachievements` where achievementid > 0;");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("moonlightachievements" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        CashItemFactory.getInstance().initialize();
        MapleServerHandler.initiate();
        LoginServer.run_startup_configurations();
        ChannelServer.startChannel_Main();
        CashShopServer.run_startup_configurations();
        //FarmServer.run_startup_configurations();
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        World.registerRespawn();
        ShutdownServer.registerMBean();
        PlayerNPC.loadAll();
        MapleMonsterInformationProvider.getInstance().addExtra();
        LoginServer.setOn();
        RankingWorker.run();
        World.GainPD(1);
        //System.out.println("Event Script List: " + ServerConfig.getEventList());
        if (ServerConfig.logPackets) {
            System.out.println("数据包记录已开启.");
        }
        if (ServerConfig.USE_FIXED_IV) {
            System.out.println("反抓包已开启.");
        }

        /*if (ServerConfig.checkCopyItem) {
         checkCopyItemFromSql();
         }*/
        CustomPlayerRankings.getInstance().load();
        long now = System.currentTimeMillis() - start;
        long seconds = now / 1000;
        long ms = now % 1000;
        System.out.println("开启一共花了: " + seconds + "秒 " + ms + "毫秒");
        OnlyID.getInstance();
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            ShutdownServer.getInstance().run();
            ShutdownServer.getInstance().run();
        }
    }

    public static void main(final String args[]) throws InterruptedException, IOException {
        //instance.run();
        /*if (ServerConfig.Business) {
         String[] Jiqima = {"a3aebd0fdae5ddc7a38cee1c28eb39db88e25613450a10415d5f008a24afcefd3bbe66eea8056c299541941c39e75270d34d231329a63ffe2f2764f7931d63f7"};
         String CPU = CpuSerialTools.getCPUSerial();
           
         String CpuSha512 = LoginCrypto.hexSha512(CPU);
         System.out.println("本机编号:" +   CpuSha512);
         if (CpuSha512 != null) {
         for (int i = 0; i < Jiqima.length; i++) {
         if (Jiqima[i].equals(CpuSha512)) {
         instance.run();
         break;
         }
         }
         } else {
         System.exit(0);
         }
         } else {*/
        instance.run();
        //}
    }
}
