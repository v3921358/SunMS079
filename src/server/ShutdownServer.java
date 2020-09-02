package server;

import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import server.Timer.BuffTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import tools.FileoutputUtil;
import tools.packet.CWvsContext;

public class ShutdownServer implements ShutdownServerMBean {

    public static ShutdownServer instance;

    public static void registerMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            instance = new ShutdownServer();
            mBeanServer.registerMBean(instance, new ObjectName("server:type=ShutdownServer"));
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            System.out.println("Error registering Shutdown MBean");
        }
    }

    public static ShutdownServer getInstance() {
        return instance;
    }
    public int mode = 0;

    @Override
    public void shutdown() {//can execute twice
        run();
    }

    @Override
    public void run() {
        try {
            int ret = 0;
            World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0, "游戏服务器将关闭维护，请玩家安全下线..."));
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.setShutdown();
                cs.setServerMessage("游戏服务器将关闭维护，请玩家安全下线...");
                ret += cs.closeAllMerchant();
            }
            System.out.println("共保存了" + ret + "个雇佣商人");
            World.Guild.save();
            World.Alliance.save();
            World.Family.save();
            System.out.println("服务端关闭事件 1 已完成.");
            System.out.println("服务端关闭事件 2 开始...");

            Integer[] chs = ChannelServer.getAllInstance().toArray(new Integer[0]);
            for (int i : chs) {
                try {
                    ChannelServer cs = ChannelServer.getInstance(i);
                    synchronized (this) {
                        cs.shutdown();
                    }
                } catch (Exception e) {
                }
            }
            try {
                LoginServer.shutdown();
                System.out.println("登录伺服器关闭完成...");
            } catch (Exception e) {
            }
            try {
                CashShopServer.shutdown();
                System.out.println("商城伺服器关闭完成...");
            } catch (Exception e) {
            }
            //try {
                //DatabaseConnection.closeAll();
           // } catch (Exception e) {
           // }
            try {
                WorldTimer.getInstance().stop();
                MapTimer.getInstance().stop();
                BuffTimer.getInstance().stop();
                CloneTimer.getInstance().stop();
                EventTimer.getInstance().stop();
                EtcTimer.getInstance().stop();
                PingTimer.getInstance().stop();
            } catch (Exception e) {
                e.printStackTrace ();
            }
            System.out.println("服务器关闭事件2已完成. 5秒后自动关闭程序...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        } catch (Exception ex) {
            FileoutputUtil.log("logs/关服异常.log", "错误信息：" + ex.toString());
        }
        System.exit(0); //not sure if this is really needed for ChannelServer

    }
}
