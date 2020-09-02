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
package handling.channel;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConfig;
import constants.WorldConstants.WorldOption;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.login.LoginServer;
import handling.mina.MapleCodecFactory;
import handling.world.CheaterData;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import scripting.EventScriptManager;
import server.MapleSquad;
import server.MapleSquad.MapleSquadType;
import server.events.*;
import server.life.PlayerNPC;
import server.maps.AramiaFireWorks;
import server.maps.MapleMapFactory;
import server.maps.MapleMapObject;
import server.stores.HiredMerchant;
import tools.ConcurrentEnumMap;
import tools.FileoutputUtil;
import tools.packet.CWvsContext;

public class ChannelServer {

    public static long serverStartTime;
    private int cashRate = 1, traitRate = 1, BossDropRate = ServerConfig.BossDropRate, ExpRate = ServerConfig.ExpRate, MesoRate = ServerConfig.MesoRate, DropRate = ServerConfig.DropRate;
    private short port = ServerConfig.channelPort;
    private static final short DEFAULT_PORT = (short) (ServerConfig.channelPort - 1);
    private int channel, running_MerchantID = 0, flags = 0;
    private String serverMessage, ip, serverName;
    private boolean shutdown = false, finishedShutdown = false, MegaphoneMuteState = false, adminOnly = false;
    private PlayerStorage players;
    private IoAcceptor acceptor;
    private final MapleMapFactory mapFactory;
    private EventScriptManager eventSM;
    private final AramiaFireWorks works = new AramiaFireWorks();
    private static final Map<Integer, ChannelServer> instances = new HashMap<>();
    private final Map<MapleSquadType, MapleSquad> mapleSquads = new ConcurrentEnumMap<>(MapleSquadType.class);
    private final Map<Integer, HiredMerchant> merchants = new HashMap<>();
    private final List<PlayerNPC> playerNPCs = new LinkedList<>();
    private final ReentrantReadWriteLock merchLock = new ReentrantReadWriteLock(); //merchant
    private int eventmap = -1;
    private final Map<MapleEventType, MapleEvent> events = new EnumMap<>(MapleEventType.class);
    public boolean eventOn = false;
    public int eventMap = 0;
    private boolean eventWarp;
    private String eventHost;
    private String eventName;
    private boolean manualEvent = false;
    private int manualEventMap = 0;
    private boolean bomberman = false;

    private ChannelServer(final int channel) {
        this.channel = channel;
        mapFactory = new MapleMapFactory(channel);
    }

    public static Set<Integer> getAllInstance() {
        return new HashSet<>(instances.keySet());
    }

    public final void loadEvents() {
        if (!events.isEmpty()) {
            return;
        }
        events.put(MapleEventType.打椰子比赛, new MapleCoconut(channel, MapleEventType.打椰子比赛.mapids));
        events.put(MapleEventType.打瓶盖比赛, new MapleCoconut(channel, MapleEventType.打瓶盖比赛.mapids));
        events.put(MapleEventType.向高地, new MapleFitness(channel, MapleEventType.向高地.mapids));
        events.put(MapleEventType.上楼上楼, new MapleOla(channel, MapleEventType.上楼上楼.mapids));
        events.put(MapleEventType.快速0X猜题, new MapleOxQuiz(channel, MapleEventType.快速0X猜题.mapids));
        events.put(MapleEventType.雪球赛, new MapleSnowball(channel, MapleEventType.雪球赛.mapids));
    }

    /**
     * 初始化并启动。
     */
    public final void run_startup_configurations() {
        setChannel(channel); //instances.put
        try {
            serverMessage = ServerConfig.scrollingMessage;
            serverName = ServerConfig.serverName;
            flags = ServerConfig.flags;
            adminOnly = ServerConfig.adminOnly;
            eventSM = new EventScriptManager(this, ServerConfig.events.split(","));
            port = (short) (DEFAULT_PORT + channel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ip = ServerConfig.interface_ + ":" + port;

        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        acceptor = new SocketAcceptor();
        final SocketAcceptorConfig acceptor_config = new SocketAcceptorConfig();
        acceptor_config.getSessionConfig().setTcpNoDelay(true);
        acceptor_config.setDisconnectOnUnbind(true);
        acceptor_config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        players = new PlayerStorage(channel);
        loadEvents();

        try {
            acceptor.bind(new InetSocketAddress(port), new MapleServerHandler(), acceptor_config);
            System.out.println("频道 " + channel + " 使用端口 " + port + ".");
            eventSM.init();
        } catch (IOException e) {
            System.out.println("Could not bind port " + port + " (ch: " + getChannel() + ")" + e);
        }
    }

    public final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        broadcastPacket(CWvsContext.broadcastMsg(0, "本频道即将关闭."));
        // dc all clients by hand so we get sessionClosed...
        shutdown = true;

        System.out.println("频道 " + channel + ", 保存角色资料中...");
        try {
            getPlayerStorage().disconnectAll();
        } catch (Exception ex) {
            FileoutputUtil.log("logs/关服异常.log", "错误信息：" + ex.toString());
        }
        System.out.println("频道 " + channel + ", 解除绑定...");

        //temporary while we dont have !addchannel
        instances.remove(channel);
        setFinishShutdown();
    }

    public final boolean hasFinishedShutdown() {
        return finishedShutdown;
    }

    public final MapleMapFactory getMapFactory() {
        return mapFactory;
    }

    public static final ChannelServer newInstance(final int channel) {
        return new ChannelServer(channel);
    }

    public static final ChannelServer getInstance(final int channel) {
        return instances.get(channel);
    }

    public final void addPlayer(final MapleCharacter chr) {
        getPlayerStorage().registerPlayer(chr);
    }

    public final PlayerStorage getPlayerStorage() {
        if (players == null) { //wth
            players = new PlayerStorage(channel); //wthhhh
        }
        return players;
    }

    public final void removePlayer(final MapleCharacter chr) {
        getPlayerStorage().deregisterPlayer(chr);

    }

    public final void removePlayer(final int idz, final String namez) {
        getPlayerStorage().deregisterPlayer(idz, namez);

    }

    public final String getServerMessage() {
        return serverMessage;
    }

    public final void setServerMessage(final String newMessage) {
        serverMessage = newMessage;
        broadcastPacket(CWvsContext.broadcastMsg(serverMessage));
    }

    public final void broadcastPacket(final byte[] data) {
        getPlayerStorage().broadcastPacket(data);
    }

    public final void broadcastSmegaPacket(final byte[] data) {
        getPlayerStorage().broadcastSmegaPacket(data);
    }

    public final void broadcastGMPacket(final byte[] data) {
        getPlayerStorage().broadcastGMPacket(data);
    }

    public final int getCashRate() {
        return cashRate;
    }

    public final int getChannel() {
        return channel;
    }

    public final void setChannel(final int channel) {
        instances.put(channel, this);
        LoginServer.addChannel(channel);
    }

    public static ArrayList<ChannelServer> getAllInstances() {
        return new ArrayList<>(instances.values());
    }

    public final String getIP() {
        return ip;
    }

    public final boolean isShutdown() {
        return shutdown;
    }

    public final int getLoadedMaps() {
        return mapFactory.getLoadedMaps();
    }

    public final EventScriptManager getEventSM() {
        return eventSM;
    }

    public final void reloadEvents() {
        eventSM.cancel();
        eventSM = new EventScriptManager(this, ServerConfig.events.split(","));
        eventSM.init();
    }

    public final void setExpRate(int rate) {
        ExpRate = rate;
    }

    public final int getExpRate(int world) {
        return WorldOption.getById(world).getExp() * ExpRate;
    }

    public final void setMesoRate(int rate) {
        MesoRate = rate;
    }

    public final int getMesoRate(int world) {
        return WorldOption.getById(world).getMeso() * MesoRate;
    }

    public final void setDropRate(int rate) {
        DropRate = rate;
    }

    public final int getDropRate(int world) {
        return WorldOption.getById(world).getDrop() * DropRate;
    }

    public final int getBossDropRate() {
        return BossDropRate;
    }

    public static void startChannel_Main() {
        System.out.println("正在载入频道...");
        serverStartTime = System.currentTimeMillis();

        for (int i = 0; i < ServerConfig.channelCount; i++) {
            newInstance(i + 1).run_startup_configurations();
        }
    }

    public Map<MapleSquadType, MapleSquad> getAllSquads() {
        return Collections.unmodifiableMap(mapleSquads);
    }

    public final MapleSquad getMapleSquad(final String type) {
        return getMapleSquad(MapleSquadType.valueOf(type.toLowerCase()));
    }

    public final MapleSquad getMapleSquad(final MapleSquadType type) {
        return mapleSquads.get(type);
    }

    public final boolean addMapleSquad(final MapleSquad squad, final String type) {
        final MapleSquadType types = MapleSquadType.valueOf(type.toLowerCase());
        if (types != null && !mapleSquads.containsKey(types)) {
            mapleSquads.put(types, squad);
            squad.scheduleRemoval();
            return true;
        }
        return false;
    }

    public final boolean removeMapleSquad(final MapleSquadType types) {
        if (types != null && mapleSquads.containsKey(types)) {
            mapleSquads.remove(types);
            return true;
        }
        return false;
    }

    public final int closeAllMerchant() {
        int ret = 0;
        merchLock.writeLock().lock();
        try {
            final Iterator<Entry<Integer, HiredMerchant>> merchants_ = merchants.entrySet().iterator();
            while (merchants_.hasNext()) {
                HiredMerchant hm = merchants_.next().getValue();
                hm.closeShop(true, false);
                //HiredMerchantSave.QueueShopForSave(hm);
                hm.getMap().removeMapObject(hm);
                merchants_.remove();
                ret++;
            }
        } finally {
            merchLock.writeLock().unlock();
        }
        try {
            //hacky
            for (int i = 910000001; i <= 910000022; i++) {
                for (MapleMapObject mmo : mapFactory.getMap(i).getAllHiredMerchantsThreadsafe()) {
                    ((HiredMerchant) mmo).closeShop(true, false);
                    ret++;
                }
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public final int getMerchantMap(MapleCharacter chr) {
        int ret = -1;
        for (int i = 910000001; i <= 910000022; i++) {
            for (MapleMapObject mmo : mapFactory.getMap(i).getAllHiredMerchantsThreadsafe()) {
                if (((HiredMerchant) mmo).getOwnerId() == chr.getId()) {
                    return mapFactory.getMap(i).getId();
                }
            }
        }
        return ret;
    }

    public final int addMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();
        try {
            running_MerchantID++;
            merchants.put(running_MerchantID, hMerchant);
            return running_MerchantID;
        } finally {
            merchLock.writeLock().unlock();
        }
    }

    public final void removeMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();

        try {
            merchants.remove(hMerchant.getStoreId());
        } finally {
            merchLock.writeLock().unlock();
        }
    }

    public final boolean containsMerchant(final int accid, int cid) {
        boolean contains = false;

        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                HiredMerchant hm = (HiredMerchant) itr.next();
                if (hm.getOwnerAccId() == accid || hm.getOwnerId() == cid) {
                    contains = true;
                    break;
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return contains;
    }

    public final List<HiredMerchant> searchMerchant(final int itemSearch) {
        final List<HiredMerchant> list = new LinkedList<>();
        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                HiredMerchant hm = (HiredMerchant) itr.next();
                if (hm.searchItem(itemSearch).size() > 0) {
                    list.add(hm);
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return list;
    }

    public final void toggleMegaphoneMuteState() {
        this.MegaphoneMuteState = !this.MegaphoneMuteState;
    }

    public final boolean getMegaphoneMuteState() {
        return MegaphoneMuteState;
    }

    public int getEvent() {
        return eventmap;
    }

    public final void setEvent(final int ze) {
        this.eventmap = ze;
    }

    public MapleEvent getEvent(final MapleEventType t) {
        return events.get(t);
    }

    public final Collection<PlayerNPC> getAllPlayerNPC() {
        return playerNPCs;
    }

    public final void addPlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.contains(npc)) {
            return;
        }
        playerNPCs.add(npc);
        getMapFactory().getMap(npc.getMapId()).addMapObject(npc);
    }

    public final void removePlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.contains(npc)) {
            playerNPCs.remove(npc);
            getMapFactory().getMap(npc.getMapId()).removeMapObject(npc);
        }
    }

    public final String getServerName() {
        return serverName;
    }

    public final void setServerName(final String sn) {
        this.serverName = sn;
    }

    public final String getTrueServerName() {
        return serverName.substring(0, serverName.length() - 2);
    }

    public final int getPort() {
        return port;
    }

    public static final Set<Integer> getChannelServer() {
        return new HashSet<>(instances.keySet());
    }

    public final void setShutdown() {
        this.shutdown = true;
        System.out.println("频道 " + channel + " 正在关闭中...");
    }

    public final void setFinishShutdown() {
        this.finishedShutdown = true;
        System.out.println("频道 " + channel + " 已经关闭完成.");
    }

    public final boolean isAdminOnly() {
        return adminOnly;
    }

    public final static int getChannelCount() {
        return instances.size();
    }

    public final int getTempFlag() {
        return flags;
    }

    public static Map<Integer, Integer> getChannelLoad() {
        Map<Integer, Integer> ret = new HashMap<>();
        for (ChannelServer cs : instances.values()) {
            ret.put(cs.getChannel(), cs.getConnectedClients());
        }
        return ret;
    }

    public int getConnectedClients() {
        return getPlayerStorage().getConnectedClients();
    }

    public List<CheaterData> getCheaters() {
        List<CheaterData> cheaters = getPlayerStorage().getCheaters();

        Collections.sort(cheaters);
        return cheaters;
    }

    public List<CheaterData> getReports() {
        List<CheaterData> cheaters = getPlayerStorage().getReports();

        Collections.sort(cheaters);
        return cheaters;
    }

    public void broadcastMessage(byte[] message) {
        broadcastPacket(message);
    }

    public void broadcastSmega(byte[] message) {
        broadcastSmegaPacket(message);
    }

    public void broadcastGMMessage(byte[] message) {
        broadcastGMPacket(message);
    }

    public AramiaFireWorks getFireWorks() {
        return works;
    }

    public int getTraitRate() {
        return traitRate;
    }

    public boolean manualEvent(MapleCharacter chr) {
        if (manualEvent) {
            manualEvent = false;
            manualEventMap = 0;
        } else {
            manualEvent = true;
            manualEventMap = chr.getMapId();
        }
        if (manualEvent) {
            chr.dropMessage(5, "Manual event has " + (manualEvent ? "began" : "begone") + ".");
        }
        return manualEvent;
    }

    public void warpToEvent(MapleCharacter chr) {
        if (!manualEvent || manualEventMap <= 0) {
            chr.dropMessage(5, "没有被托管的事件.");
            return;
        }
        chr.dropMessage(5, "你被扭曲成活动地图.");
        chr.changeMap(manualEventMap, 0);
    }

    public boolean bombermanActive() {
        return bomberman;
    }

    public void toggleBomberman(MapleCharacter chr) {
        bomberman = !bomberman;
        if (bomberman) {
            chr.dropMessage(5, "炸弹活动是积极的.");
        } else {
            chr.dropMessage(5, "炸弹人活动不活跃.");
        }
    }

    public static void forceRemovePlayerByAccId(MapleClient c, int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chr.getClient() != c) {
                        if (chrs.contains(chr)) {
                            ch.removePlayer(chr);
                        }
                        if (chr.getMap() != null) {
                            chr.getMap().removePlayer(chr);
                        }
                    }
                }
            }
        }
        try {
            Collection<MapleCharacter> chrs = CashShopServer.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public static void forceRemovePlayerByAccId(int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getAccountID() == accid) {
                    try {
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                    }
                    if (c.getMap() != null) {
                        c.getMap().removePlayer(c);
                    }
                }
            }
        }
    }

    public static boolean forceRemovePlayerByCharName(String Name) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getName().equalsIgnoreCase(Name)) {
                    try {
                        if (c.getMap() != null) {
                            c.getMap().removePlayer(c);
                        }
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                            c.getClient().getSession().close();
                        }

                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                        return true;
                    }

                }
            }
        }
        return false;
    }
    
    public void AutoNx(int dy) {
        mapFactory.getMap(ServerConfig.pdmap).AutoNx(dy);//挂机地点
    }
    
    
}
