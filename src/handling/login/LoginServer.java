/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.login;

import client.MapleClient;
import constants.GameConstants;
import constants.ServerConfig;
import handling.MapleServerHandler;
import handling.mina.MapleCodecFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import tools.Triple;

/**
 * 登入服务器实体。
 */
public class LoginServer {

    public static final int PORT = ServerConfig.loginPort;
    private static InetSocketAddress InetSocketadd;
    private static IoAcceptor acceptor;
    private static Map<Integer, Integer> load = new HashMap<>();
    private static String serverName, eventMessage;
    private static byte flag;
    private static int maxCharacters, userLimit, usersOn = 0;
    private static boolean finishedShutdown = true, adminOnly = false;
    private static final HashMap<Integer, Triple<String, String, Integer>> loginAuth = new HashMap<>();
    private static final HashSet<String> loginIPAuth = new HashSet<>();
    private static final Map<Integer, Long> LoginTime = new WeakHashMap<>();
    private static final Map<Integer, MapleClient> accounts = new HashMap<>();

    public static final Collection<MapleClient> getAllClients() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public static final List<MapleClient> getAllClientsThreadSafe() {
        List<MapleClient> ret = new ArrayList<>();
        ret.addAll(getAllClients());

        return ret;
    }

    public static void forceRemoveAccounts(MapleClient client, boolean remove) {
        Collection<MapleClient> cls = getAllClientsThreadSafe();
        for (MapleClient c : cls) {
            if (c == null) {
                continue;
            }
            if (c.getAccID() == client.getAccID() || c == client) {
                if (c != client) {
                    c.getSession().close();
                }
                if (remove) {
                    removeAccounts(c);
                }
            }
        }
    }

    public static final void addAccounts(MapleClient c, int accid) {
        accounts.put(accid, c);
    }

    public static final void removeAccounts(MapleClient c) {
        List<Integer> clients = new ArrayList();
        for (Map.Entry<Integer, MapleClient> entry : accounts.entrySet()) {
            if (entry.getValue() == c) {
                clients.add(entry.getKey());
            }
        }
        for (int id : clients) {
            accounts.remove(id);
        }
    }

    public static boolean CheckSelectChar(int accid, long lastTime) {
        if (LoginTime.containsKey(accid)) {
            long lastLoginTime = LoginTime.get(accid);
            if (lastLoginTime + 8000 > System.currentTimeMillis()) {
                return false;
            }
            LoginTime.remove(accid);
        } else {
            LoginTime.put(accid, lastTime);
        }
        return true;
    }

    public static void putLoginTime(int accid, long lastTime) {
        LoginTime.put(accid, lastTime);
    }

    public static void putLoginAuth(int chrid, String ip, String tempIP, int channel) {
        Triple<String, String, Integer> put = loginAuth.put(chrid, new Triple<>(ip, tempIP, channel));
        loginIPAuth.add(ip);
    }

    public static Triple<String, String, Integer> getLoginAuth(int chrid) {
        return loginAuth.remove(chrid);
    }

    public static boolean containsIPAuth(String ip) {
        return loginIPAuth.contains(ip);
    }

    public static void removeIPAuth(String ip) {
        loginIPAuth.remove(ip);
    }

    public static void addIPAuth(String ip) {
        loginIPAuth.add(ip);
    }

    public static final void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public static final void removeChannel(final int channel) {
        load.remove(channel);
    }

    /**
     * 初始化并启动。
     */
    public static final void run_startup_configurations() {
        System.out.print("正在载入登入伺服器...");
        userLimit = ServerConfig.userLimit;
        serverName = ServerConfig.serverName;
        eventMessage = ServerConfig.eventMessage;
        flag = ServerConfig.flag;
        adminOnly = ServerConfig.adminOnly;
        maxCharacters = ServerConfig.maxCharacters;

        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        acceptor = new SocketAcceptor();
        final SocketAcceptorConfig cfg = new SocketAcceptorConfig();
        cfg.getSessionConfig().setTcpNoDelay(true);
        cfg.setDisconnectOnUnbind(true);
        cfg.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));

        try {
            InetSocketadd = new InetSocketAddress(PORT);
            acceptor.bind(InetSocketadd, new MapleServerHandler(), cfg);
            System.out.println(" 完成!");
            System.out.println("登入伺服器使用端口 " + PORT + ".");
        } catch (IOException e) {
            System.out.println(" 失败!");
            System.err.println("登入伺服器无法绑定端口 " + PORT + ": " + e);
        }
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("正在关闭登入伺服器...");
        acceptor.unbindAll();
        finishedShutdown = true; //nothing. lol
    }

    public static final String getServerName() {
        return serverName;
    }

    public static final String getTrueServerName() {
        return serverName.substring(0, serverName.length() - (GameConstants.GMS ? 2 : 3));
    }

    public static String getEventMessage() {
        return eventMessage;
    }

    public static int getMaxCharacters() {
        return maxCharacters;
    }

    public static Map<Integer, Integer> getLoad() {
        return load;
    }

    public static void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public static String getEventMessage(int world) { //TODO: Finish this
        switch (world) {
            case 0:
                return null;
        }
        return null;
    }

    public static final void setFlag(final byte newflag) {
        flag = newflag;
    }

    public static final int getUserLimit() {
        return userLimit;
    }

    public static final int getUsersOn() {
        return usersOn;
    }

    public static final void setUserLimit(final int newLimit) {
        userLimit = newLimit;
    }

    public static final int getNumberOfSessions() {
        return acceptor.getManagedSessions(InetSocketadd).size();
    }

    public static final boolean isAdminOnly() {
        return adminOnly;
    }

    public static final boolean isShutdown() {
        return finishedShutdown;
    }

    public static final void setOn() {
        finishedShutdown = false;
    }

    public static String ChangeAdminOnly() {
        adminOnly = !isAdminOnly();
        return adminOnly ? "开启" : "关闭";
    }
}
