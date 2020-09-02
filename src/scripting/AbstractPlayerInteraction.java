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
package scripting;

import client.*;
import client.MapleTrait.MapleTraitType;
import client.inventory.*;
import constants.GameConstants;
import database.DBConPool;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.exped.MapleExpedition;
import handling.world.guild.MapleGuild;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.Timer;
import server.Timer.EventTimer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.*;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CField.EffectPacket;
import tools.packet.CField.NPCPacket;
import tools.packet.CField.UIPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.InfoPacket;
import tools.packet.PetPacket;

public abstract class AbstractPlayerInteraction {

    protected MapleClient c;
    protected int id;
    protected int id2;
    protected String script;

    public AbstractPlayerInteraction(final MapleClient c, final int id, final int id2, final String script) {
        this.c = c;
        this.id = id;
        this.id2 = id2;
        this.script = script;
    }

    public final MapleClient getClient() {
        return c;
    }

    public final MapleClient getC() {
        return c;
    }

    public MapleCharacter getChar() {
        return c.getPlayer();
    }

    public final ChannelServer getChannelServer() {
        return c.getChannelServer();
    }

    public final MapleCharacter getPlayer() {
        return c.getPlayer();
    }

    public final EventManager getEventManager(final String event) {
        return c.getChannelServer().getEventSM().getEventManager(event);
    }

    public final EventInstanceManager getEventInstance() {
        return c.getPlayer().getEventInstance();
    }

    public final void openNpc(int npc, String filename) {
        NPCScriptManager.getInstance().start(c, npc, filename);
    }

    public final void openNpc(MapleClient client, int npc, String filename) {
        NPCScriptManager.getInstance().start(client, npc, filename);
    }

    public final void warp(final int map) {
        final MapleMap mapz = getWarpMap(map);
        try {
            c.getPlayer().changeMap(mapz, mapz.getPortal(Randomizer.nextInt(mapz.getPortals().size())));
        } catch (Exception e) {
            c.getPlayer().changeMap(mapz, mapz.getPortal(0));
        }
    }

    public final void warp_Instanced(final int map) {
        final MapleMap mapz = getMap_Instanced(map);
        try {
            c.getPlayer().changeMap(mapz, mapz.getPortal(Randomizer.nextInt(mapz.getPortals().size())));
        } catch (Exception e) {
            c.getPlayer().changeMap(mapz, mapz.getPortal(0));
        }
    }

    public final void warp(final int map, final int portal) {
        final MapleMap mapz = getWarpMap(map);
        if (portal != 0 && map == c.getPlayer().getMapId()) { //test
            final Point portalPos = new Point(c.getPlayer().getMap().getPortal(portal).getPosition());
            if (portalPos.distanceSq(getPlayer().getTruePosition()) < 90000.0) { //estimation
                c.getSession().write(CField.instantMapWarp((byte) portal)); //until we get packet for far movement, this will do
                c.getPlayer().checkFollow();
                c.getPlayer().getMap().movePlayer(c.getPlayer(), portalPos);
            } else {
                c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
            }
        } else {
            c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
        }
    }

    public final void warpS(final int map, final int portal) {
        final MapleMap mapz = getWarpMap(map);
        c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
    }

    public final void warp(final int map, String portal) {
        final MapleMap mapz = getWarpMap(map);
        if (map == 109060000 || map == 109060002 || map == 109060004) {
            portal = mapz.getSnowballPortal();
        }
        if (map == c.getPlayer().getMapId()) { //test
            final Point portalPos = new Point(c.getPlayer().getMap().getPortal(portal).getPosition());
            if (portalPos.distanceSq(getPlayer().getTruePosition()) < 90000.0) { //estimation
                c.getPlayer().checkFollow();
                c.getSession().write(CField.instantMapWarp((byte) c.getPlayer().getMap().getPortal(portal).getId()));
                c.getPlayer().getMap().movePlayer(c.getPlayer(), new Point(c.getPlayer().getMap().getPortal(portal).getPosition()));
            } else {
                c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
            }
        } else {
            c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
        }
    }

    public final void warpS(final int map, String portal) {
        final MapleMap mapz = getWarpMap(map);
        if (map == 109060000 || map == 109060002 || map == 109060004) {
            portal = mapz.getSnowballPortal();
        }
        c.getPlayer().changeMap(mapz, mapz.getPortal(portal));
    }

    public final void warpMap(final int mapid, final int portal) {
        final MapleMap map = getMap(mapid);
        for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
            chr.changeMap(map, map.getPortal(portal));
        }
    }

    public final void warpByName(final int mapid, final String chrname) {
        MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(chrname);
        if (chr == null) {
            c.getPlayer().dropMessage(1, "找不到角色.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final MapleMap mapz = getWarpMap(mapid);
        try {
            chr.changeMap(mapz, mapz.getPortal(Randomizer.nextInt(mapz.getPortals().size())));
            chr.getClient().removeClickedNPC();
            NPCScriptManager.getInstance().dispose(chr.getClient());
            chr.getClient().getSession().write(CWvsContext.enableActions());
        } catch (Exception e) {
            chr.changeMap(mapz, mapz.getPortal(0));
            chr.getClient().removeClickedNPC();
            NPCScriptManager.getInstance().dispose(chr.getClient());
            chr.getClient().getSession().write(CWvsContext.enableActions());
        }
    }

    public final void mapChangeTimer(final int map, final int nextmap, final int time, final boolean notice) {
        final List<MapleCharacter> current = c.getChannelServer().getMapFactory().getMap(map).getCharacters();
        c.getChannelServer().getMapFactory().getMap(map).broadcastMessage(CField.getClock(time));
        if (notice) {
            c.getChannelServer().getMapFactory().getMap(map).startMapEffect("You will be moved out of the map when the timer ends.", 5120041);
        }
        EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (current != null) {
                    for (MapleCharacter chrs : current) {
                        chrs.changeMap(nextmap, 0);
                    }
                }
            }
        }, time * 1000); // seconds
    }

    public final void playPortalSE() {
        c.getSession().write(EffectPacket.showOwnBuffEffect(0, 8, 1, 1));
    }

    private MapleMap getWarpMap(final int map) {
        return ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(map);
    }

    public final MapleMap getMap() {
        return c.getPlayer().getMap();
    }

    public final MapleMap getMap(final int map) {
        return getWarpMap(map);
    }

    public final MapleMap getMap_Instanced(final int map) {
        return c.getPlayer().getEventInstance() == null ? getMap(map) : c.getPlayer().getEventInstance().getMapInstance(map);
    }

    public void spawnMonster(final int id, final int qty) {
        spawnMob(id, qty, c.getPlayer().getTruePosition());
    }

    public final void spawnMobOnMap(final int id, final int qty, final int x, final int y, final int map) {
        for (int i = 0; i < qty; i++) {
            getMap(map).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(id), new Point(x, y));
        }
    }

    public final void spawnMob(final int id, final int qty, final int x, final int y) {
        spawnMob(id, qty, new Point(x, y));
    }

    public final void spawnMob(final int id, final int x, final int y) {
        spawnMob(id, 1, new Point(x, y));
    }

    private void spawnMob(final int id, final int qty, final Point pos) {
        for (int i = 0; i < qty; i++) {
            c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(id), pos);
        }
    }

    public final void killMob(int ids) {
        c.getPlayer().getMap().killMonster(ids);
    }

    public final void killAllMob() {
        c.getPlayer().getMap().killAllMonsters(true);
    }

    public final void addHP(final int delta) {
        c.getPlayer().addHP(delta);
    }

    public final int getPlayerStat(final String type) {
        switch (type) {
            case "LVL":
                return c.getPlayer().getLevel();
            case "STR":
                return c.getPlayer().getStat().getStr();
            case "DEX":
                return c.getPlayer().getStat().getDex();
            case "INT":
                return c.getPlayer().getStat().getInt();
            case "LUK":
                return c.getPlayer().getStat().getLuk();
            case "HP":
                return c.getPlayer().getStat().getHp();
            case "MP":
                return c.getPlayer().getStat().getMp();
            case "MAXHP":
                return c.getPlayer().getStat().getMaxHp();
            case "MAXMP":
                return c.getPlayer().getStat().getMaxMp();
            case "RAP":
                return c.getPlayer().getRemainingAp();
            case "RSP":
                return c.getPlayer().getRemainingSp();
            case "GID":
                return c.getPlayer().getGuildId();
            case "GRANK":
                return c.getPlayer().getGuildRank();
            case "ARANK":
                return c.getPlayer().getAllianceRank();
            case "GM":
                return c.getPlayer().isGM() ? 1 : 0;
            case "ADMIN":
                return c.getPlayer().isAdmin() ? 1 : 0;
            case "GENDER":
                return c.getPlayer().getGender();
            case "FACE":
                return c.getPlayer().getFace();
            case "HAIR":
                return c.getPlayer().getHair();
        }
        return -1;
    }

    public final String getName() {
        return c.getPlayer().getName();
    }

    public final boolean haveItem(final int itemid) {
        return haveItem(itemid, 1);
    }

    public final boolean haveItem(final int itemid, final int quantity) {
        return haveItem(itemid, quantity, false, true);
    }

    public final boolean haveItem(final int itemid, final int quantity, final boolean checkEquipped, final boolean greaterOrEquals) {
        return c.getPlayer().haveItem(itemid, quantity, checkEquipped, greaterOrEquals);
    }

    public final boolean canHold() {
        for (int i = 1; i <= 5; i++) {
            if (c.getPlayer().getInventory(MapleInventoryType.getByType((byte) i)).getNextFreeSlot() <= -1) {
                return false;
            }
        }
        return true;
    }

    public final boolean canHoldSlots(final int slot) {
        for (int i = 1; i <= 5; i++) {
            if (c.getPlayer().getInventory(MapleInventoryType.getByType((byte) i)).isFull(slot)) {
                return false;
            }
        }
        return true;
    }

    public final boolean canHold(final int itemid) {
        return c.getPlayer().getInventory(GameConstants.getInventoryType(itemid)).getNextFreeSlot() > -1;
    }

    public final boolean canHold(final int itemid, final int quantity) {
        return MapleInventoryManipulator.checkSpace(c, itemid, quantity, "");
    }

    public final MapleQuestStatus getQuestRecord(final int id) {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(id));
    }

    public final MapleQuestStatus getQuestNoRecord(final int id) {
        return c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(id));
    }

    public final byte getQuestStatus(final int id) {
        return c.getPlayer().getQuestStatus(id);
    }

    public final boolean isQuestActive(final int id) {
        return getQuestStatus(id) == 1;
    }

    public final boolean isQuestFinished(final int id) {
        return getQuestStatus(id) == 2;
    }

    public final void showQuestMsg(final String msg) {
        c.getSession().write(CWvsContext.showQuestMsg(msg));
    }

    public final void forceStartQuest(final int id, final String data) {
        MapleQuest.getInstance(id).forceStart(c.getPlayer(), 0, data);
    }

    public final void forceStartQuest(final int id, final int data, final boolean filler) {
        MapleQuest.getInstance(id).forceStart(c.getPlayer(), 0, filler ? String.valueOf(data) : null);
    }

    public void forceStartQuest(final int id) {
        MapleQuest.getInstance(id).forceStart(c.getPlayer(), 0, null);
    }

    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), 0);
    }

    public void spawnNpc(final int npcId) {
        c.getPlayer().getMap().spawnNpc(npcId, c.getPlayer().getPosition());
    }

    public final void spawnNpc(final int npcId, final int x, final int y) {
        c.getPlayer().getMap().spawnNpc(npcId, new Point(x, y));
    }

    public final void spawnNpc(final int npcId, final Point pos) {
        c.getPlayer().getMap().spawnNpc(npcId, pos);
    }

    public final void spawnNpcForPlayer(final int npcId, final int x, final int y) {
        c.getPlayer().getMap().spawnNpcForPlayer(c, npcId, new Point(x, y));
    }

    public final void removeNpc(final int mapid, final int npcId) {
        c.getChannelServer().getMapFactory().getMap(mapid).removeNpc(npcId);
    }

    public final void removeNpc(final int npcId) {
        c.getPlayer().getMap().removeNpc(npcId);
    }

    public final void hideNpc(final int npcId) {
        c.getPlayer().getMap().hideNpc(npcId);
    }

    public final void respawn(final boolean force) {
        c.getPlayer().getMap().respawn(force);
    }

    public final void forceStartReactor(final int mapid, final int id) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        MapleReactor react;

        for (final MapleMapObject remo : map.getAllReactorsThreadsafe()) {
            react = (MapleReactor) remo;
            if (react.getReactorId() == id) {
                react.forceStartReactor(c);
                break;
            }
        }
    }

    public final void destroyReactor(final int mapid, final int id) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        MapleReactor react;

        for (final MapleMapObject remo : map.getAllReactorsThreadsafe()) {
            react = (MapleReactor) remo;
            if (react.getReactorId() == id) {
                react.hitReactor(c);
                break;
            }
        }
    }

    public final void hitReactor(final int mapid, final int id) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        MapleReactor react;

        for (final MapleMapObject remo : map.getAllReactorsThreadsafe()) {
            react = (MapleReactor) remo;
            if (react.getReactorId() == id) {
                react.hitReactor(c);
                break;
            }
        }
    }

    public final int getJob() {
        return c.getPlayer().getJob();
    }

    public final void gainNX(final int amount) {
        c.getPlayer().modifyCSPoints(4, amount, true); //theremk you can change it to prepaid yae since cspoiint is xncredit so make it prepaid
    }

    public final void gainItemPeriod(final int id, final short quantity, final int period) { //period is in days
        gainItem(id, quantity, false, period, false, -1, "");
    }

    public final void gainItemPeriod(final int id, final short quantity, final long period, final String owner) { //period is in days
        gainItem(id, quantity, false, period, false, -1, owner);
    }

    public final void gainItemPeriod(final int id, final short quantity, final int period, boolean hours) { //period is in days
        gainItem(id, quantity, false, period, hours, -1, "");
    }

    public final void gainItemPeriodA(final int id, final short quantity, final int period, boolean hours) { //period is in days
        gainItem(id, quantity, false, period, hours, -1, "");
    }

    public final void gainItemPeriod(final int id, final short quantity, final long period, boolean hours, final String owner) { //period is in days
        gainItem(id, quantity, false, period, hours, -1, owner);
    }

    public final void gainItem(final int id, final short quantity) {
        gainItem(id, quantity, false, 0, false, -1, "");
    }

    public final void gainItemSilent(final int id, final short quantity) {
        gainItem(id, quantity, false, 0, false, -1, "", c, false);
    }

    public final void gainItem(final int id, final short quantity, final boolean randomStats) {
        gainItem(id, quantity, randomStats, 0, false, -1, "");
    }

    public final void gainItem(final int id, final short quantity, final boolean randomStats, final int slots) {
        gainItem(id, quantity, randomStats, 0, false, slots, "");
    }

    public final void gainItem(final int id, final short quantity, final long period) {
        gainItem(id, quantity, false, period, false, -1, "");
    }

    public final void gainItem(final int id, final short quantity, final boolean randomStats, final long period, final int slots) {
        gainItem(id, quantity, randomStats, period, false, slots, "");
    }

    public final void gainItem(final int id, final short quantity, final boolean randomStats, final long period, boolean hours, final int slots, final String owner) {
        gainItem(id, quantity, randomStats, period, hours, slots, owner, c);
    }

    public final void gainItem(final int id, final short quantity, final boolean randomStats, final long period, boolean hours, final int slots, final String owner, final MapleClient cg) {
        gainItem(id, quantity, randomStats, period, hours, slots, owner, cg, true);
    }

//    public final void gainItem(final int id, final short quantity, final boolean randomStats, final long period, boolean hours, final int slots, boolean potential, final String owner) {
//        gainItem(id, quantity, randomStats, period, hours, slots, potential, owner, c);
//    }
    public final void gainItem(final int id, final short quantity, final boolean randomStats, final long period, boolean hours, final int slots, final String owner, final MapleClient cg, final boolean show) {
        if (quantity >= 0) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final MapleInventoryType type = GameConstants.getInventoryType(id);

            if (!MapleInventoryManipulator.checkSpace(cg, id, quantity, "")) {
                return;
            }
            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(id) && !GameConstants.isBullet(id)) {
                final Equip item = (Equip) (randomStats ? ii.randomizeStats((Equip) ii.getEquipById(id)) : ii.getEquipById(id));
                if (period > 0) {
                    item.setExpiration(System.currentTimeMillis() + (period * (hours ? 1 : 24) * 60 * 60 * 1000));
                }
                if (slots > 0) {
                    item.setUpgradeSlots((byte) (item.getUpgradeSlots() + slots));
                }
                if (owner != null) {
                    item.setOwner(owner);
                }
                item.setGMLog("Received from interaction " + this.id + " (" + id2 + ") on " + FileoutputUtil.CurrentReadable_Time());
                final String name = ii.getName(id);
                if (id / 10000 == 114 && name != null && name.length() > 0) { //medal
                    final String msg = "< " + name + " > has been rewarded.";
                    cg.getPlayer().dropMessage(-1, msg);
                    cg.getPlayer().dropMessage(5, msg);
                }
                MapleInventoryManipulator.addbyItem(cg, item.copy());
            } else {
                MapleInventoryManipulator.addById(cg, id, quantity, owner == null ? "" : owner, null, period, hours, "Received from interaction " + this.id + " (" + id2 + ") on " + FileoutputUtil.CurrentReadable_Date());
            }
        } else {
            MapleInventoryManipulator.removeById(cg, GameConstants.getInventoryType(id), id, -quantity, true, false);
        }
        if (show) {
            cg.getSession().write(InfoPacket.getShowItemGain(id, quantity, true));
        }
    }

    public final boolean removeItem(final int id) { //quantity 1
        if (MapleInventoryManipulator.removeById_Lock(c, GameConstants.getInventoryType(id), id)) {
            c.getSession().write(InfoPacket.getShowItemGain(id, (short) -1, true));
            return true;
        }
        return false;
    }

    public final void changeMusic(final String songName) {
        getPlayer().getMap().broadcastMessage(CField.musicChange(songName));
    }

    public final void worldMessage(final int type, int channel, final String message, boolean smegaEar) {
        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(type, channel, message, smegaEar));
    }

    public final void worldMessage(final int type, final String message) {
        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(type, message));
    }

    // default playerMessage and mapMessage to use type 5
    public final void playerMessage(final String message) {
        playerMessage(5, message);
    }

    public final void mapMessage(final String message) {
        mapMessage(5, message);
    }

    public final void guildMessage(final String message) {
        guildMessage(5, message);
    }

    public final void playerMessage(final int type, final String message) {
        c.getPlayer().dropMessage(type, message);
    }

    public final void mapMessage(final int type, final String message) {
        c.getPlayer().getMap().broadcastMessage(CWvsContext.broadcastMsg(type, message));
    }

    public final void guildMessage(final int type, final String message) {
        if (getPlayer().getGuildId() > 0) {
            World.Guild.guildPacket(getPlayer().getGuildId(), CWvsContext.broadcastMsg(type, message));
        }
    }

    public final MapleGuild getGuild() {
        return getGuild(getPlayer().getGuildId());
    }

    public final MapleGuild getGuild(int guildid) {
        return World.Guild.getGuild(guildid);
    }

    public final MapleParty getParty() {
        return c.getPlayer().getParty();
    }

    public final int getCurrentPartyId(int mapid) {
        return getMap(mapid).getCurrentPartyId();
    }

    public final boolean isLeader() {
        if (getPlayer().getParty() == null) {
            return false;
        }
        return getParty().getLeader().getId() == c.getPlayer().getId();
    }

    public final boolean isAllPartyMembersAllowedJob(final int job) {
        if (c.getPlayer().getParty() == null) {
            return false;
        }
        for (final MaplePartyCharacter mem : c.getPlayer().getParty().getMembers()) {
            if (mem.getJobId() / 100 != job) {
                return false;
            }
        }
        return true;
    }

    public final boolean allMembersHere() {
        if (c.getPlayer().getParty() == null) {
            return false;
        }
        for (final MaplePartyCharacter mem : c.getPlayer().getParty().getMembers()) {
            final MapleCharacter chr = c.getPlayer().getMap().getCharacterById(mem.getId());
            if (chr == null) {
                return false;
            }
        }
        return true;
    }

    public final void warpParty(final int mapId) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            warp(mapId, 0);
            return;
        }
        final MapleMap target = getMap(mapId);
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.changeMap(target, target.getPortal(0));
            }
        }
    }

    public final void warpParty(final int mapId, final int portal) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            if (portal < 0) {
                warp(mapId);
            } else {
                warp(mapId, portal);
            }
            return;
        }
        final boolean rand = portal < 0;
        final MapleMap target = getMap(mapId);
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                if (rand) {
                    try {
                        curChar.changeMap(target, target.getPortal(Randomizer.nextInt(target.getPortals().size())));
                    } catch (Exception e) {
                        curChar.changeMap(target, target.getPortal(0));
                    }
                } else {
                    curChar.changeMap(target, target.getPortal(portal));
                }
            }
        }
    }

    public final void warpParty_Instanced(final int mapId) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            warp_Instanced(mapId);
            return;
        }
        final MapleMap target = getMap_Instanced(mapId);

        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.changeMap(target, target.getPortal(0));
            }
        }
    }

    public void gainMeso(int gain) {
        c.getPlayer().gainMeso(gain, true, true);
    }

    public void gainExp(int gain) {
        c.getPlayer().gainExp(gain, true, true, true);
    }

    public void gainExpR(int gain) {
        c.getPlayer().gainExp(gain * c.getChannelServer().getExpRate(c.getPlayer() != null ? c.getPlayer().getWorld() : 0), true, true, true);
    }

    public void gainSp(final int amount) {
        c.getPlayer().gainSP((short) amount);
    }

    public final void givePartyItems(final int id, final short quantity, final List<MapleCharacter> party) {
        for (MapleCharacter chr : party) {
            if (quantity >= 0) {
                MapleInventoryManipulator.addById(chr.getClient(), id, quantity, "Received from party interaction " + id + " (" + id2 + ")");
            } else {
                MapleInventoryManipulator.removeById(chr.getClient(), GameConstants.getInventoryType(id), id, -quantity, true, false);
            }
            chr.getClient().getSession().write(InfoPacket.getShowItemGain(id, quantity, true));
        }
    }

    public void addPartyTrait(String t, int e, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            chr.getTrait(MapleTraitType.valueOf(t)).addExp(e, chr);
        }
    }

    public void addPartyTrait(String t, int e) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            addTrait(t, e);
            return;
        }
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.getTrait(MapleTraitType.valueOf(t)).addExp(e, curChar);
            }
        }
    }

    public void addTrait(String t, int e) {
        getPlayer().getTrait(MapleTraitType.valueOf(t)).addExp(e, getPlayer());
    }

    public final void givePartyItems(final int id, final short quantity) {
        givePartyItems(id, quantity, false);
    }

    public final void givePartyItems(final int id, final short quantity, final boolean removeAll) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            gainItem(id, (short) (removeAll ? -getPlayer().itemQuantity(id) : quantity));
            return;
        }

        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                gainItem(id, (short) (removeAll ? -curChar.itemQuantity(id) : quantity), false, 0, false, 0, "", curChar.getClient());
            }
        }
    }

    public final void givePartyExp_PQ(final int maxLevel, final double mod, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            final int amount = (int) Math.round(GameConstants.getExpNeededForLevel(chr.getLevel() > maxLevel ? (maxLevel + ((maxLevel - chr.getLevel()) / 10)) : chr.getLevel()) / (Math.min(chr.getLevel(), maxLevel) / 5.0) / (mod * 2.0));
            chr.gainExp(amount * c.getChannelServer().getExpRate(chr.getWorld()), true, true, true);
        }
    }

    public final void gainExp_PQ(final int maxLevel, final double mod) {
        final int amount = (int) Math.round(GameConstants.getExpNeededForLevel(getPlayer().getLevel() > maxLevel ? (maxLevel + (getPlayer().getLevel() / 10)) : getPlayer().getLevel()) / (Math.min(getPlayer().getLevel(), maxLevel) / 10.0) / mod);
        gainExp(amount * c.getChannelServer().getExpRate(c.getPlayer() != null ? c.getPlayer().getWorld() : 0));
    }

    public final void givePartyExp_PQ(final int maxLevel, final double mod) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            final int amount = (int) Math.round(GameConstants.getExpNeededForLevel(getPlayer().getLevel() > maxLevel ? (maxLevel + (getPlayer().getLevel() / 10)) : getPlayer().getLevel()) / (Math.min(getPlayer().getLevel(), maxLevel) / 10.0) / mod);
            gainExp(amount * c.getChannelServer().getExpRate(c.getPlayer() != null ? c.getPlayer().getWorld() : 0));
            return;
        }
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                final int amount = (int) Math.round(GameConstants.getExpNeededForLevel(curChar.getLevel() > maxLevel ? (maxLevel + (curChar.getLevel() / 10)) : curChar.getLevel()) / (Math.min(curChar.getLevel(), maxLevel) / 10.0) / mod);
                curChar.gainExp(amount * c.getChannelServer().getExpRate(curChar.getWorld()), true, true, true);
            }
        }
    }

    public final void givePartyExp(final int amount, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            chr.gainExp(amount * c.getChannelServer().getExpRate(chr.getWorld()), true, true, true);
        }
    }

    public final void givePartyExp(final int amount) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            gainExp(amount * c.getChannelServer().getExpRate(c.getPlayer() != null ? c.getPlayer().getWorld() : 0));
            return;
        }
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.gainExp(amount * c.getChannelServer().getExpRate(curChar.getWorld()), true, true, true);
            }
        }
    }

    public final void endPartyQuest(final int amount, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            chr.endPartyQuest(amount);
        }
    }

    public final void endPartyQuest(final int amount) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            getPlayer().endPartyQuest(amount);
            return;
        }
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.endPartyQuest(amount);
            }
        }
    }

    public final void removeFromParty(final int id, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            final int possesed = chr.getInventory(GameConstants.getInventoryType(id)).countById(id);
            if (possesed > 0) {
                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(id), id, possesed, true, false);
                chr.getClient().getSession().write(InfoPacket.getShowItemGain(id, (short) -possesed, true));
            }
        }
    }

    public final void removeFromParty(final int id) {
        givePartyItems(id, (short) 0, true);
    }

    public final void useSkill(final int skill, final int level) {
        if (level <= 0) {
            return;
        }
        SkillFactory.getSkill(skill).getEffect(level).applyTo(c.getPlayer());
    }

    public final void useItem(final int id) {
        MapleItemInformationProvider.getInstance().getItemEffect(id).applyTo(c.getPlayer());
        c.getSession().write(InfoPacket.getStatusMsg(id));
    }

    public final void cancelItem(final int id) {
        c.getPlayer().cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(id), false, -1);
    }

    public final int getMorphState() {
        return c.getPlayer().getMorphState();
    }

    public final void removeAll(final int id) {
        c.getPlayer().removeAll(id);
    }

    public final void gainCloseness(final int closeness, final int index) {
        final MaplePet pet = getPlayer().getPet(index);
        if (pet != null) {
            pet.setCloseness(pet.getCloseness() + (closeness * getChannelServer().getTraitRate()));
            getClient().getSession().write(PetPacket.updatePet(pet, getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
        }
    }

    public final void gainClosenessAll(final int closeness) {
        for (final MaplePet pet : getPlayer().getPets()) {
            if (pet != null && pet.getSummoned()) {
                pet.setCloseness(pet.getCloseness() + closeness);
                getClient().getSession().write(PetPacket.updatePet(pet, getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
            }
        }
    }

    public final void givePartyNX(final int amount, final List<MapleCharacter> party) {
        for (final MapleCharacter chr : party) {
            chr.modifyCSPoints(1, amount, true);
        }
    }

    public final void givePartyNX(final int amount) {
        if (getPlayer().getParty() == null || getPlayer().getParty().getMembers().size() == 1) {
            gainNX(amount);
            return;
        }
        final int cMap = getPlayer().getMapId();
        for (final MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            final MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar != null && (curChar.getMapId() == cMap || curChar.getEventInstance() == getPlayer().getEventInstance())) {
                curChar.modifyCSPoints(1, amount, true);
            }
        }
    }

    public final void resetMap(final int mapid) {
        getMap(mapid).resetFully();
    }

    public final void openNpc(final int id) {
        getClient().removeClickedNPC();
        NPCScriptManager.getInstance().start(getClient(), id, null);
    }

    public final void openNpc(final int id, final int mode) {
        getClient().removeClickedNPC();
        NPCScriptManager.getInstance().start(getClient(), id, mode, null);
    }

    public final void openNpc(final MapleClient cg, final int id) {
        cg.removeClickedNPC();
        NPCScriptManager.getInstance().start(cg, id, null);
    }

    public final int getMapId() {
        return c.getPlayer().getMap().getId();
    }

    public final boolean haveMonster(final int mobid) {
        for (MapleMapObject obj : c.getPlayer().getMap().getAllMonstersThreadsafe()) {
            final MapleMonster mob = (MapleMonster) obj;
            if (mob.getId() == mobid) {
                return true;
            }
        }
        return false;
    }

    public final int getChannelNumber() {
        return c.getChannel();
    }

    public final int getMonsterCount(final int mapid) {
        return c.getChannelServer().getMapFactory().getMap(mapid).getNumMonsters();
    }

    public final void teachSkill(final int id, final int level, final byte masterlevel) {
        getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(id), level, masterlevel);
    }

    public final void teachSkill(final int id, int level) {
        final Skill skil = SkillFactory.getSkill(id);
        if (getPlayer().getSkillLevel(skil) > level) {
            level = getPlayer().getSkillLevel(skil);
        }
        getPlayer().changeSingleSkillLevel(skil, level, (byte) skil.getMaxLevel());
    }

    public final int getPlayerCount(final int mapid) {
        return c.getChannelServer().getMapFactory().getMap(mapid).getCharactersSize();
    }

    public final void dojo_getUp() {
        //int sec = 12;//getCurrentTime
//        long curtime = getCurrentTime();
//        System.err.println(curtime);
        c.getPlayer().updateInfoQuest(7215, "stage=6;type=1;token=3");
        c.getPlayer().updateInfoQuest(7218, "1");
        for (int i = 0; i < 3; i++) {
            c.getPlayer().updateInfoQuest(7281, "item=0;chk=0;cNum=0;sec=2;stage=0;lBonus=0"); //last stage
        }
        for (int i = 0; i < 2; i++) {
            c.getPlayer().updateInfoQuest(7281, "item=0;chk=0;cNum=0;sec=2;stage=0;lBonus=0");
        }
        c.getPlayer().updateInfoQuest(7216, "3");
        c.getPlayer().updateInfoQuest(7214, "5");
        c.getPlayer().updateInfoQuest(7215, "0");
        //c.getSession().write(InfoPacket.updateInfoQuest(1207, "min=1;tuto=1")); //old - 1207, "pt=1;min=4;belt=1;tuto=1")); //todo
        //c.getSession().write(InfoPacket.updateInfoQuest(7281, "item=0;chk=0;cNum=0;sec=" + sec + ";stage=0;lBonus=0"));
        // c.getSession().write(EffectPacket.Mulung_DojoUp2());
        c.getSession().write(CField.instantMapWarp((byte) 6));
    }

    public final boolean dojoAgent_NextMap(final boolean dojo, final boolean fromresting) {
        if (dojo) {
            return Event_DojoAgent.warpNextMap(c.getPlayer(), fromresting, c.getPlayer().getMap());
        }
        return Event_DojoAgent.warpNextMap_Agent(c.getPlayer(), fromresting);
    }

    public final boolean dojoAgent_NextMap(final boolean dojo, final boolean fromresting, final int mapid) {
        if (dojo) {
            return Event_DojoAgent.warpNextMap(c.getPlayer(), fromresting, getMap(mapid));
        }
        return Event_DojoAgent.warpNextMap_Agent(c.getPlayer(), fromresting);
    }

    public final int dojo_getPts() {
        return c.getPlayer().getIntNoRecord(GameConstants.DOJO);
    }

    public final MapleEvent getEvent(final String loc) {
        return c.getChannelServer().getEvent(MapleEventType.valueOf(loc));
    }

    public final int getSavedLocation(final String loc) {
        final Integer ret = c.getPlayer().getSavedLocation(SavedLocationType.fromString(loc));
        if (ret == null || ret == -1) {
            return 100000000;
        }
        return ret;
    }

    public final void saveLocation(final String loc) {
        c.getPlayer().saveLocation(SavedLocationType.fromString(loc));
    }

    public final void saveReturnLocation(final String loc) {
        c.getPlayer().saveLocation(SavedLocationType.fromString(loc), c.getPlayer().getMap().getReturnMap().getId());
    }

    public final void clearSavedLocation(final String loc) {
        c.getPlayer().clearSavedLocation(SavedLocationType.fromString(loc));
    }

    public final void summonMsg(final String msg) {
        if (!c.getPlayer().hasSummon()) {
            playerSummonHint(true);
        }
        c.getSession().write(UIPacket.summonMessage(msg));
    }

    public final void summonMsg(final int type) {
        if (!c.getPlayer().hasSummon()) {
            playerSummonHint(true);
        }
        c.getSession().write(UIPacket.summonMessage(type));
    }

    public final void showInstruction(final String msg, final int width, final int height) {
        c.getSession().write(CField.sendHint(msg, width, height));
    }

    public final void playerSummonHint(final boolean summon) {
        c.getPlayer().setHasSummon(summon);
        c.getSession().write(UIPacket.summonHelper(summon));
    }

    public final String getInfoQuest(final int id) {
        return c.getPlayer().getInfoQuest(id);
    }

    public final void updateInfoQuest(final int id, final String data) {
        c.getPlayer().updateInfoQuest(id, data);
    }

    public final boolean getEvanIntroState(final String data) {
        return getInfoQuest(22013).equals(data);
    }

    public final void updateEvanIntroState(final String data) {
        updateInfoQuest(22013, data);
    }

    public final void Aran_Start() {
        c.getSession().write(CField.Aran_Start());
    }

    public final void AranTutInstructionalBubble(final String data) {
        c.getSession().write(EffectPacket.TutInstructionalBalloon(data));
    }

    public final void ShowWZEffect(final String data) {
        c.getSession().write(EffectPacket.TutInstructionalBalloon(data));
    }

    public final void showWZEffect(final String data) {
        c.getSession().write(EffectPacket.ShowWZEffect(data));
    }

    public final void EarnTitleMsg(final String data) {
        c.getSession().write(CWvsContext.getTopMsg(data));
    }

    public final void topMsg(final String data) {
        c.getSession().write(CWvsContext.getTopMsg(data));
    }

    public final void EnableUI(final short i) {
        c.getSession().write(UIPacket.IntroEnableUI(i));
    }

    public final void MovieClipIntroUI(final boolean enabled) {
        c.getSession().write(UIPacket.IntroEnableUI(1));
        c.getSession().write(UIPacket.IntroLock(enabled));
    }

    public MapleInventoryType getInvType(int i) {
        return MapleInventoryType.getByType((byte) i);
    }

    public String getItemName(final int id) {
        return MapleItemInformationProvider.getInstance().getName(id);
    }

    public void gainPet(int id, String name, int level, int closeness, int fullness, long period, short flags) {
        if (id >= 5001000 || id < 5000000) {
            id = 5000000;
        }
        if (level > 30) {
            level = 30;
        }
        if (closeness > 30000) {
            closeness = 30000;
        }
        if (fullness > 100) {
            fullness = 100;
        }
        try {
            MapleInventoryManipulator.addById(c, id, (short) 1, "", MaplePet.createPet(id, name, level, closeness, fullness, MapleInventoryIdentifier.getInstance(), id == 5000054 ? (int) period : 0, flags), 45, false, "Pet from interaction " + id + " (" + id2 + ")" + " on " + FileoutputUtil.CurrentReadable_Date());
        } catch (NullPointerException ex) {
        }
    }

    public void removeSlot(int invType, byte slot, short quantity) {
        MapleInventoryManipulator.removeFromSlot(c, getInvType(invType), slot, quantity, true);
    }

    public void gainGP(final int gp) {
        if (getPlayer().getGuildId() <= 0) {
            return;
        }
        World.Guild.gainGP(getPlayer().getGuildId(), gp); //1 for
    }

    public int getGP() {
        if (getPlayer().getGuildId() <= 0) {
            return 0;
        }
        return World.Guild.getGP(getPlayer().getGuildId()); //1 for
    }

    public void showMapEffect(String path) {
        getClient().getSession().write(CField.MapEff(path));
    }

    public int itemQuantity(int itemid) {
        return getPlayer().itemQuantity(itemid);
    }

    public EventInstanceManager getDisconnected(String event) {
        EventManager em = getEventManager(event);
        if (em == null) {
            return null;
        }
        for (EventInstanceManager eim : em.getInstances()) {
            if (eim.isDisconnected(c.getPlayer()) && eim.getPlayerCount() > 0) {
                return eim;
            }
        }
        return null;
    }

    public boolean isAllReactorState(final int reactorId, final int state) {
        boolean ret = false;
        for (MapleReactor r : getMap().getAllReactorsThreadsafe()) {
            if (r.getReactorId() == reactorId) {
                ret = r.getState() == state;
            }
        }
        return ret;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void spawnMonster(int id) {
        spawnMonster(id, 1, getPlayer().getTruePosition());
    }

    // summon one monster, remote location
    public void spawnMonster(int id, int x, int y) {
        spawnMonster(id, 1, new Point(x, y));
    }

    // multiple monsters, remote location
    public void spawnMonster(int id, int qty, int x, int y) {
        spawnMonster(id, qty, new Point(x, y));
    }

    // handler for all spawnMonster
    public void spawnMonster(int id, int qty, Point pos) {
        for (int i = 0; i < qty; i++) {
            getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(id), pos);
        }
    }

    public void sendNPCText(final String text, final int npc) {
        getMap().broadcastMessage(NPCPacket.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
    }

    public boolean getTempFlag(final int flag) {
        return (c.getChannelServer().getTempFlag() & flag) == flag;
    }

    public void sendUIWindow(final int type, final int npc) {
        c.getSession().write(CField.UIPacket.openUIOption(type, npc));
    }

    public void logPQ(String text) {
//	FileoutputUtil.log(FileoutputUtil.PQ_Log, text);
    }

    public void outputFileError(Throwable t) {
        FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, t);
    }

    public void trembleEffect(int type, int delay) {
        c.getSession().write(CField.trembleEffect(type, delay));
    }

    public int nextInt(int arg0) {
        return Randomizer.nextInt(arg0);
    }

    public MapleQuest getQuest(int arg0) {
        return MapleQuest.getInstance(arg0);
    }

    public void achievement(int a) {
        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(a));
    }

    public final MapleInventory getInventory(int type) {
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    public final void prepareAswanMob(int mapid, EventManager eim) {
        MapleMap map = eim.getMapFactory().getMap(mapid);
        if (c.getPlayer().getParty() != null) {
            map.setChangeableMobOrigin(ChannelServer.getInstance(c.getChannel()).getPlayerStorage().getCharacterById(c.getPlayer().getParty().getLeader().getId()));
        } else {
            map.setChangeableMobOrigin(c.getPlayer());
        }
//        map.setChangeableMobUsing(true);
        map.killAllMonsters(false);
        map.respawn(true);
    }

    public final void startAswanOffSeason(final MapleCharacter leader) {
        final List<MapleCharacter> check1 = c.getChannelServer().getMapFactory().getMap(955000100).getCharacters();
        final List<MapleCharacter> check2 = c.getChannelServer().getMapFactory().getMap(955000200).getCharacters();
        final List<MapleCharacter> check3 = c.getChannelServer().getMapFactory().getMap(955000300).getCharacters();
        c.getChannelServer().getMapFactory().getMap(955000100).broadcastMessage(CField.getClock(20 * 60));
        c.getChannelServer().getMapFactory().getMap(955000200).broadcastMessage(CField.getClock(20 * 60));
        c.getChannelServer().getMapFactory().getMap(955000300).broadcastMessage(CField.getClock(20 * 60));
        EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (check1 != null && check2 != null && check3 != null && (leader.getMapId() == 955000100 || leader.getMapId() == 955000200 || leader.getMapId() == 955000300)) {
                    for (MapleCharacter chrs : check1) {
                        chrs.changeMap(262010000, 0);
                    }
                    for (MapleCharacter chrs : check2) {
                        chrs.changeMap(262010000, 0);
                    }
                    for (MapleCharacter chrs : check3) {
                        chrs.changeMap(262010000, 0);
                    }
                } else {
                    EventTimer.getInstance().stop();
                }
            }
        }, 20 * 60 * 1000);
    }

    public boolean isGMS() {
        return GameConstants.GMS;
    }

    public int randInt(int arg0) {
        return Randomizer.nextInt(arg0);
    }

    public void sendDirectionStatus(int key, int value) {
        c.getSession().write(UIPacket.getDirectionInfo(key, value));
        c.getSession().write(UIPacket.getDirectionStatus(true));
    }

    public void sendDirectionStatus(int key, int value, boolean direction) {
        c.getSession().write(UIPacket.getDirectionInfo(key, value));
        c.getSession().write(UIPacket.getDirectionStatus(direction));
    }

    public void sendDirectionInfo(String data) {
        c.getSession().write(UIPacket.getDirectionInfo(data, 2000, 0, -100, 0, 0));
        c.getSession().write(UIPacket.getDirectionInfo(1, 2000));
    }

    public void getDirectionInfo(String data, int value, int x, int y, int a, int b) {
        c.getSession().write(CField.UIPacket.getDirectionInfo(data, value, x, y, a, b));
    }

    public void getDirectionInfo(byte type, int value) {
        c.getSession().write(CField.UIPacket.getDirectionInfo(type, value));
    }

    public void sendDirectionFacialExpression(int expression, int duration) {
        c.getSession().write(UIPacket.getDirectionFacialExpression(expression, duration));
    }

    public void introEnableUI(int wtf) {
        c.getSession().write(CField.UIPacket.IntroEnableUI(wtf));
    }

    public void getDirectionStatus(boolean enable) {
        c.getSession().write(CField.UIPacket.getDirectionStatus(enable));
    }

    public void playMovie(String data, boolean show) {
        c.getSession().write(UIPacket.playMovie(data, show));
    }

    public void getTopMsg(String message) {
        c.getSession().write(CWvsContext.getTopMsg(message));
    }

    public String getCharacterName(int characterid) {
        return c.getChannelServer().getPlayerStorage().getCharacterById(characterid).getName();
    }

    public final MapleExpedition getExpedition() {
        return World.Party.getExped(c.getPlayer().getParty().getId());
    }

    public int getExpeditionMembers(int id) {
        return World.Party.getExped(c.getPlayer().getParty().getId()).getAllMembers();
    }

    public void warpExpedition(int mapid, int portal) {
        for (MapleCharacter chr : World.Party.getExped(c.getPlayer().getParty().getId()).getExpeditionMembers(c)) {
            chr.changeMap(mapid, portal);
        }
    }

    public String getMasteryBooksByJob(String job) {
        StringBuilder sb = new StringBuilder();
        for (Pair<Integer, String> book : MapleItemInformationProvider.getInstance().getAllItems2()) {
            if (book.getLeft() >= 2280000 && book.getLeft() < 2300000) {
                String skilldesc = MapleItemInformationProvider.getInstance().getDesc(book.getLeft());
                if (skilldesc.contains(job)) {
                    sb.append("~").append(book.getLeft());
                }
            }
        }
        return sb.toString();
    }

    public void test(String test) {
        System.out.println(test);
    }

    public String format(String format, Object... toFormat) {
        return String.format(format, toFormat);
    }

    public void addReward(int type, int item, int mp, int meso, int exp, String desc) {
        getPlayer().addReward(type, item, mp, meso, exp, desc);
    }

    public void addReward(long start, long end, int type, int item, int mp, int meso, int exp, String desc) {
        getPlayer().addReward(start, end, type, item, mp, meso, exp, desc);
    }

    public int getPQLog(String pqid) {
        return getPlayer().getPQLog(pqid);
    }

//    public int getGiftLog(String pqid) {
//        return getPlayer().getGiftLog(pqid);
//    }
    //@Override
    public void setPQLog(String pqid) {
        getPlayer().setPQLog(pqid);
    }

    public void showAriantScoreBoard() {
        getClient().getSession().write(CField.showAriantScoreBoard());
    }

    public final void startAriantPQ(int mapid) {
        for (final MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
            chr.updateAriantScore();
            chr.changeMap(mapid);
            c.getPlayer().getMap().resetAriantPQ(c.getPlayer().getAverageMapLevel());
            chr.getClient().getSession().write(CField.getClock(8 * 60));
            chr.dropMessage(5, "建议把你的小地图忘下移动，来查看排名.");
            Timer.MapTimer.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    chr.updateAriantScore();
                }
            }, 800);
            Timer.EtcTimer.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (chr.getMapId() == 980010101 || chr.getMapId() == 980010201 || chr.getMapId() == 980010301) {
                        chr.getClient().getSession().write(CField.showAriantScoreBoard());
                    }
                    Timer.MapTimer.getInstance().schedule(new Runnable() {

                        @Override
                        public void run() {
                            if (chr.getMapId() == 980010101 || chr.getMapId() == 980010201 || chr.getMapId() == 980010301) {
                                chr.changeMap(980010010, 0);
                            }
                            chr.resetAriantScore();

                        }
                    }, 9000);
                }
            }, (8 * 60) * 1000);
        }
    }

    public int MarrageChecking() {
        if (getPlayer().getParty() == null) {
            return -1;
        }
        if (getPlayer().getMarriageId() > 0) {
            return 0;
        }
        if (getPlayer().getParty().getMembers().size() != 2) {
            return 1;
        }
        if ((getPlayer().getGender() == 0) && (!getPlayer().haveItem(1050121)) && (!getPlayer().haveItem(1050122)) && (!getPlayer().haveItem(1050113))) {
            return 5;
        }
        if ((getPlayer().getGender() == 1) && (!getPlayer().haveItem(1051129)) && (!getPlayer().haveItem(1051130)) && (!getPlayer().haveItem(1051114))) {
            return 5;
        }
        if (!getPlayer().haveItem(1112001) && (!getPlayer().haveItem(1112012)) && (!getPlayer().haveItem(1112002)) && (!getPlayer().haveItem(1112007))) {
            return 6;
        }
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            if (chr.getId() == getPlayer().getId()) {
                continue;
            }
            MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar == null) {
                return 2;
            }
            if (curChar.getMarriageId() > 0) {
                return 3;
            }
            if (curChar.getGender() == getPlayer().getGender()) {
                return 4;
            }
            if ((curChar.getGender() == 0) && (!curChar.haveItem(1050121)) && (!curChar.haveItem(1050122)) && (!curChar.haveItem(1050113))) {
                return 5;
            }
            if ((curChar.getGender() == 1) && (!curChar.haveItem(1051129)) && (!curChar.haveItem(1051130)) && (!curChar.haveItem(1051114))) {
                return 5;
            }
            if (!curChar.haveItem(1112001) && (!curChar.haveItem(1112012)) && (!curChar.haveItem(1112002)) && (!curChar.haveItem(1112007))) {
                return 6;
            }
        }
        return 9;
    }

    public int getPartyFormID() {
        int curCharID = -1;
        if (getPlayer().getParty() == null) {
            curCharID = -1;
        } else if (getPlayer().getMarriageId() > 0) {
            curCharID = -2;
        } else if (getPlayer().getParty().getMembers().size() != 2) {
            curCharID = -3;
        }
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            if (chr.getId() == getPlayer().getId()) {
                continue;
            }
            MapleCharacter curChar = getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
            if (curChar == null) {
                curCharID = -4;
            } else {
                curCharID = chr.getId();
            }
        }
        return curCharID;
    }

    public void makeRing(int itemID, String Name) {
        int itemId = itemID;
        if (!GameConstants.isEffectRing(itemId)) {
            c.getPlayer().dropMessage(6, "Invalid itemID.");
        } else {
            MapleCharacter fff = c.getChannelServer().getPlayerStorage().getCharacterByName(Name);
            if (fff == null) {
                c.getPlayer().dropMessage(6, "Player must be online");
            } else {
                int[] ringID = {MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance()};
                try {
                    MapleCharacter[] chrz = {fff, c.getPlayer()};
                    for (int i = 0; i < chrz.length; i++) {
                        Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId, ringID[i]);
                        if (eq == null) {
                            c.getPlayer().dropMessage(6, "Invalid itemID.");
                            return;
                        }
                        MapleInventoryManipulator.addbyItem(chrz[i].getClient(), eq.copy());
                        chrz[i].dropMessage(6, "Successfully married with " + chrz[i == 0 ? 1 : 0].getName());
                    }
                    MapleRing.addToDB(itemId, c.getPlayer(), fff.getName(), fff.getId(), ringID);
                } catch (SQLException e) {
                }
            }
        }
    }

    public int getzb() {
        int money = 0;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int cid = getPlayer().getAccountID();
            ResultSet rs;
            try (PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "")) {
                rs = limitCheck.executeQuery();
                if (rs.next()) {
                    money = rs.getInt("money");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("getzb" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            ex.getStackTrace();
        }
        return money;
    }

    public void setzb(int slot) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int cid = getPlayer().getAccountID();
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET money =money+ " + slot + " WHERE id = " + cid + "")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println("setzb" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            ex.getStackTrace();
        }
    }

    public int getmoneyb() {
        int moneyb = 0;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int cid = getPlayer().getAccountID();
            ResultSet rs;
            try (PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "")) {
                rs = limitCheck.executeQuery();
                if (rs.next()) {
                    moneyb = rs.getInt("moneyb");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.err.println("getmoneyb" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            ex.getStackTrace();
        }
        return moneyb;
    }

    public void setmoneyb(int slot) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int cid = getPlayer().getAccountID();
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET moneyb =moneyb+ " + slot + " WHERE id = " + cid + "")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println("setmoneyb" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            ex.getStackTrace();
        }
    }

    public final void gainDJ(final int amount) {
        c.getPlayer().modifyCSPoints(1, amount, true); //theremk you can change it to prepaid yae since cspoiint is xncredit so make it prepaid
    }

    public int gainGetItemMegas(int id, int quantity) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            //if (rareness > 0) {
            World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : 完成了玩具塔101副本获取该物品。大家一起恭喜他（她）吧！", item, rareness));
            //}
            c.getSession().write(InfoPacket.getShowItemGain(item.getItemId(), (short) quantity, true));
            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    public int gainGetItemMegas1(int id, int quantity) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            //if (rareness > 0) {
            World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : 完成了废弃都市副本获取该物品。大家一起恭喜他（她）吧！", item, rareness));
            //}
            c.getSession().write(InfoPacket.getShowItemGain(item.getItemId(), (short) quantity, true));
            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    public final void gainD(final int amount) {
        c.getPlayer().modifyCSPoints(2, amount, true);
    }

    public void openWeb(String web) {
        c.getSession().write(CWvsContext.openWeb(web));
    }

    public Map<String, Integer> seniorBox() {
        Map<String, Integer> s = c.getPlayer().seniorBox();
        c.getPlayer().setSeniorBox(s);
        return s;
    }

    public int seniorBoxitemId() {
        int s = c.getPlayer().getSeniorBox().get("itemId");
        return s;
    }

    public int seniorBoxlevel() {
        int s = c.getPlayer().getSeniorBox().get("level");
        return s;
    }

    public Map<String, Integer> primaryBox() {
        Map<String, Integer> p = c.getPlayer().primaryBox();
        c.getPlayer().setPrimaryBox(p);
        return p;
    }

    public int primaryBoxitemId() {
        int p = c.getPlayer().getPrimaryBox().get("itemId");
        return p;
    }

    public int primaryBoxlevel() {
        int p = c.getPlayer().getPrimaryBox().get("level");
        return p;
    }

    public MapleMapFactory getMapFactory() {
        return getChannelServer().getMapFactory();
    }
}
