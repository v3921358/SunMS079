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
package handling.channel.handler;

import client.*;
import client.anticheat.CheatTracker;
import client.anticheat.CheatingOffense;
import client.anticheat.ReportType;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.mina.common.WriteFuture;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.ReactorScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.Randomizer;
import server.events.MapleCoconut;
import server.events.MapleCoconut.MapleCoconuts;
import server.events.MapleEventType;
import server.life.MapleMonsterInformationProvider;
import server.life.MonsterDropEntry;
import server.life.MonsterGlobalDropEntry;
import server.maps.FieldLimitType;
import server.maps.MapleDoor;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMist;
import server.maps.MapleReactor;
import server.maps.MechDoor;
import server.quest.MapleQuest;
import tools.AttackPair;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.Reward;
import tools.packet.JobPacket;

public class PlayersHandler {

    public static void Note(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final byte type = slea.readByte();

        switch (type) {
            case 0:
                String name = slea.readMapleAsciiString();
                String msg = slea.readMapleAsciiString();
                boolean fame = slea.readByte() > 0;
                slea.readInt(); //0?
                Item itemz = chr.getCashInventory().findByCashId((int) slea.readLong());
                if (itemz == null || !itemz.getGiftFrom().equalsIgnoreCase(name) || !chr.getCashInventory().canSendNote(itemz.getUniqueId())) {
                    return;
                }
                try {
                    chr.sendNote(name, msg, fame ? 1 : 0);
                    chr.getCashInventory().sendedNote(itemz.getUniqueId());
                } catch (Exception e) {
                }
                break;
            case 1:
                short num = slea.readShort();
                if (num < 0) { // note overflow, shouldn't happen much unless > 32767 
                    num = 32767;
                }
                byte fames = slea.readByte();
                for (int i = 0; i < num; i++) {
                    final int id = slea.readInt();
                    chr.deleteNote(id, fames > 0 ? fames : 0);
                }
                break;
            default:
                System.out.println("Unhandled note action, " + type + "");
        }
    }

    public static void GiveFame(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int who = slea.readInt();
        final int mode = slea.readByte();

        final int famechange = mode == 0 ? -1 : 1;
        final MapleCharacter target = chr.getMap().getCharacterById(who);

        if (target == null || target == chr) { // faming self
            c.getSession().write(CWvsContext.giveFameErrorResponse(1));
            return;
        } else if (chr.getLevel() < 15) {
            c.getSession().write(CWvsContext.giveFameErrorResponse(2));
            return;
        }
        switch (chr.canGiveFame(target)) {
            case OK:
                if (Math.abs(target.getFame() + famechange) <= 99999) {
                    target.addFame(famechange);
                    target.updateSingleStat(MapleStat.FAME, target.getFame());
                }
                if (!chr.isGM()) {
                    chr.hasGivenFame(target);
                }
                c.getSession().write(CWvsContext.OnFameResult(0, target.getName(), famechange == 1, target.getFame()));
                target.getClient().getSession().write(CWvsContext.OnFameResult(5, chr.getName(), famechange == 1, 0));
                break;
            case NOT_TODAY:
                c.getSession().write(CWvsContext.giveFameErrorResponse(3));
                break;
            case NOT_THIS_MONTH:
                c.getSession().write(CWvsContext.giveFameErrorResponse(4));
                break;
        }
    }

    public static void UseDoor(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int oid = slea.readInt();
        final boolean mode = slea.readByte() == 0; // specifies if backwarp or not, 1 town to target, 0 target to town

        for (MapleMapObject obj : chr.getMap().getAllDoorsThreadsafe()) {
            final MapleDoor door = (MapleDoor) obj;
            if (door.getOwnerId() == oid) {
                door.warp(chr, mode);
                break;
            }
        }
    }

    public static void UseMechDoor(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int oid = slea.readInt();
        final Point pos = slea.readPos();
        final int mode = slea.readByte(); // specifies if backwarp or not, 1 town to target, 0 target to town
        chr.getClient().getSession().write(CWvsContext.enableActions());
        for (MapleMapObject obj : chr.getMap().getAllMechDoorsThreadsafe()) {
            final MechDoor door = (MechDoor) obj;
            if (door.getOwnerId() == oid && door.getId() == mode) {
                chr.checkFollow();
                chr.getMap().movePlayer(chr, pos);
                break;
            }
        }
    }

    public static void DressUpRequest(final MapleCharacter chr, LittleEndianAccessor slea) {
        int code = slea.readInt();
        switch (code) {
            case 5010093:
                chr.getClient().getSession().write(JobPacket.AngelicPacket.updateDress(code, chr));
                chr.getClient().getSession().write(CField.updateCharLook(chr, true));
                break;
            case 5010094:
                chr.getClient().getSession().write(JobPacket.AngelicPacket.updateDress(code, chr));
                chr.getClient().getSession().write(CField.updateCharLook(chr, true));
                break;
        }
    }

    public static void TransformPlayer(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        // D9 A4 FD 00
        // 11 00
        // A0 C0 21 00
        // 07 00 64 66 62 64 66 62 64
        chr.updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final String target = slea.readMapleAsciiString();

        final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);

        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        switch (itemId) {
            case 2212000:
                final MapleCharacter search_chr = chr.getMap().getCharacterByName(target);
                if (search_chr != null) {
                    MapleItemInformationProvider.getInstance().getItemEffect(2210023).applyTo(search_chr);
                    search_chr.dropMessage(6, chr.getName() + " 有恶作剧你!");
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                }
                break;
        }
    }

    public static void HitReactor(final LittleEndianAccessor slea, final MapleClient c) {
        final int oid = slea.readInt();
        final int charPos = slea.readInt();
        final short stance = slea.readShort();
        final MapleReactor reactor = c.getPlayer().getMap().getReactorByOid(oid);

        if (reactor == null || !reactor.isAlive()) {
            return;
        }
        if (c.getPlayer().isGM()) {
            c.getPlayer().dropMessage("[系统提示]你已攻击反应物" + reactor.getReactorId());
        }
        reactor.hitReactor(charPos, stance, c);
    }

    public static void TouchReactor(final LittleEndianAccessor slea, final MapleClient c) {
        final int oid = slea.readInt();
        final boolean touched = slea.available() == 0 || slea.readByte() > 0; //the byte is probably the state to set it to
        final MapleReactor reactor = c.getPlayer().getMap().getReactorByOid(oid);
        if (!touched || reactor == null || !reactor.isAlive() || reactor.getTouch() == 0) {
            return;
        }
        if (c.getPlayer().isAdmin()) {
            c.getPlayer().dropMessage(5, new StringBuilder().append("反应堆信息 - oid: ").append(oid).append(" Touch: ").append(reactor.getTouch()).append(" isTimerActive: ").append(reactor.isTimerActive()).append(" ReactorType: ").append(reactor.getReactorType()).toString());
        }
        if (reactor.getTouch() == 2) {
            ReactorScriptManager.getInstance().act(c, reactor); //not sure how touched boolean comes into play
        } else if (reactor.getTouch() == 1 && !reactor.isTimerActive()) {
            if (reactor.getReactorType() == 100) {
                final int itemid = GameConstants.getCustomReactItem(reactor.getReactorId(), reactor.getReactItem().getLeft());
                if (c.getPlayer().haveItem(itemid, reactor.getReactItem().getRight())) {
                    if (reactor.getArea().contains(c.getPlayer().getTruePosition())) {
                        MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemid), itemid, reactor.getReactItem().getRight(), true, false);
                        reactor.hitReactor(c);
                    } else {
                        c.getPlayer().dropMessage(5, "距离太远.");
                    }
                } else {
                    c.getPlayer().dropMessage(5, "你没有所需的物品.");
                }
            } else {
                //just hit it
                reactor.hitReactor(c);
            }
        }
    }

    public static void hitCoconut(LittleEndianAccessor slea, MapleClient c) {
        /*CB 00 A6 00 06 01
         * A6 00 = coconut id
         * 06 01 = ?
         */
        int id = slea.readShort();
        String co = "椰子";
        MapleCoconut map = (MapleCoconut) c.getChannelServer().getEvent(MapleEventType.打椰子比赛);
        if (map == null || !map.isRunning()) {
            map = (MapleCoconut) c.getChannelServer().getEvent(MapleEventType.打瓶盖比赛);
            co = "瓶盖";
            if (map == null || !map.isRunning()) {
                return;
            }
        }
        //System.out.println("Coconut1");
        MapleCoconuts nut = map.getCoconut(id);
        if (nut == null || !nut.isHittable()) {
            return;
        }
        if (System.currentTimeMillis() < nut.getHitTime()) {
            return;
        }
        //System.out.println("Coconut2");
        if (nut.getHits() > 2 && Math.random() < 0.4 && !nut.isStopped()) {
            //System.out.println("Coconut3-1");
            nut.setHittable(false);
            if (Math.random() < 0.01 && map.getStopped() > 0) {
                nut.setStopped(true);
                map.stopCoconut();
                c.getPlayer().getMap().broadcastMessage(CField.hitCoconut(false, id, 1));
                return;
            }
            nut.resetHits(); // For next event (without restarts)
            //System.out.println("Coconut4");
            if (Math.random() < 0.05 && map.getBombings() > 0) {
                //System.out.println("Coconut5-1");
                c.getPlayer().getMap().broadcastMessage(CField.hitCoconut(false, id, 2));
                map.bombCoconut();
            } else if (map.getFalling() > 0) {
                //System.out.println("Coconut5-2");
                c.getPlayer().getMap().broadcastMessage(CField.hitCoconut(false, id, 3));
                map.fallCoconut();
                if (c.getPlayer().getTeam() == 0) {
                    map.addMapleScore();
                    c.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(5, c.getPlayer().getName() + " 彩虹队成功打掉了一个 " + co + "."));
                } else {
                    map.addStoryScore();
                    c.getPlayer().getMap().broadcastMessage(CWvsContext.serverNotice(5, c.getPlayer().getName() + " 神秘队成功打掉一个 " + co + "."));
                }
                c.getPlayer().getMap().broadcastMessage(CField.coconutScore(map.getCoconutScore()));
            }
        } else {
            //System.out.println("Coconut3-2");
            nut.hit();
            c.getPlayer().getMap().broadcastMessage(CField.hitCoconut(false, id, 1));
        }
    }

    public static void FollowRequest(final LittleEndianAccessor slea, final MapleClient c) {
        MapleCharacter tt = c.getPlayer().getMap().getCharacterById(slea.readInt());
        if (slea.readByte() > 0) {
            //1 when changing map
            tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
            if (tt != null && tt.getFollowId() == c.getPlayer().getId()) {
                tt.setFollowOn(true);
                c.getPlayer().setFollowOn(true);
            } else {
                c.getPlayer().checkFollow();
            }
            return;
        }
        if (slea.readByte() > 0) { //cancelling follow
            tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
            if (tt != null && tt.getFollowId() == c.getPlayer().getId() && c.getPlayer().isFollowOn()) {
                c.getPlayer().checkFollow();
            }
            return;
        }
        if (tt != null && tt.getPosition().distanceSq(c.getPlayer().getPosition()) < 10000 && tt.getFollowId() == 0 && c.getPlayer().getFollowId() == 0 && tt.getId() != c.getPlayer().getId()) { //estimate, should less
            tt.setFollowId(c.getPlayer().getId());
            tt.setFollowOn(false);
            tt.setFollowInitiator(false);
            c.getPlayer().setFollowOn(false);
            c.getPlayer().setFollowInitiator(false);
            tt.getClient().getSession().write(CWvsContext.followRequest(c.getPlayer().getId()));
        } else {
            c.getSession().write(CWvsContext.broadcastMsg(1, "You are too far away."));
        }
    }

    public static void FollowReply(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer().getFollowId() > 0 && c.getPlayer().getFollowId() == slea.readInt()) {
            MapleCharacter tt = c.getPlayer().getMap().getCharacterById(c.getPlayer().getFollowId());
            if (tt != null && tt.getPosition().distanceSq(c.getPlayer().getPosition()) < 10000 && tt.getFollowId() == 0 && tt.getId() != c.getPlayer().getId()) { //estimate, should less
                boolean accepted = slea.readByte() > 0;
                if (accepted) {
                    tt.setFollowId(c.getPlayer().getId());
                    tt.setFollowOn(true);
                    tt.setFollowInitiator(false);
                    c.getPlayer().setFollowOn(true);
                    c.getPlayer().setFollowInitiator(true);
                    c.getPlayer().getMap().broadcastMessage(CField.followEffect(tt.getId(), c.getPlayer().getId(), null));
                } else {
                    c.getPlayer().setFollowId(0);
                    tt.setFollowId(0);
                    tt.getClient().getSession().write(CField.getFollowMsg(5));
                }
            } else {
                if (tt != null) {
                    tt.setFollowId(0);
                    c.getPlayer().setFollowId(0);
                }
                c.getSession().write(CWvsContext.broadcastMsg(1, "You are too far away."));
            }
        } else {
            c.getPlayer().setFollowId(0);
        }
    }

    public static void DoRing(final MapleClient c, final String name, final int itemid) {
        final int newItemId = itemid == 2240000 ? 1112803 : (itemid == 2240001 ? 1112806 : (itemid == 2240002 ? 1112807 : (itemid == 2240003 ? 1112809 : (1112300 + (itemid - 2240004)))));
        final MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
        int errcode = 0;
        if (c.getPlayer().getMarriageId() > 0) {
            errcode = 0x17;
        } else if (chr == null) {
            errcode = 0x12;
        } else if (chr.getMapId() != c.getPlayer().getMapId()) {
            errcode = 0x13;
        } else if (!c.getPlayer().haveItem(itemid, 1) || itemid < 2240000 || itemid > 2240015) {
            errcode = 0x0D;
        } else if (chr.getMarriageId() > 0 || chr.getMarriageItemId() > 0) {
            errcode = 0x18;
        } else if (!MapleInventoryManipulator.checkSpace(c, newItemId, 1, "")) {
            errcode = 0x14;
        } else if (!MapleInventoryManipulator.checkSpace(chr.getClient(), newItemId, 1, "")) {
            errcode = 0x15;
        }
        if (errcode > 0) {
            c.getSession().write(CWvsContext.sendEngagement((byte) errcode, 0, null, null));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        c.getPlayer().setMarriageItemId(itemid);
        WriteFuture write = chr.getClient().getSession().write(CWvsContext.sendEngagementRequest(c.getPlayer().getName(), c.getPlayer().getId()));
    }

    public static void RingAction(final LittleEndianAccessor slea, final MapleClient c) {
        final byte mode = slea.readByte();
        if (mode == 0) {
            DoRing(c, slea.readMapleAsciiString(), slea.readInt());
            //1112300 + (itemid - 2240004)
        } else if (mode == 1) {
            c.getPlayer().setMarriageItemId(0);
        } else if (mode == 2) { //accept/deny proposal
            final boolean accepted = slea.readByte() > 0;
            final String name = slea.readMapleAsciiString();
            final int id = slea.readInt();
            final MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
            if (c.getPlayer().getMarriageId() > 0 || chr == null || chr.getId() != id || chr.getMarriageItemId() <= 0 || !chr.haveItem(chr.getMarriageItemId(), 1) || chr.getMarriageId() > 0 || !chr.isAlive() || chr.getEventInstance() != null || !c.getPlayer().isAlive() || c.getPlayer().getEventInstance() != null) {
                c.getSession().write(CWvsContext.sendEngagement((byte) 0x1D, 0, null, null));
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            if (accepted) {
                final int itemid = chr.getMarriageItemId();
                final int newItemId = itemid == 2240000 ? 1112803 : (itemid == 2240001 ? 1112806 : (itemid == 2240002 ? 1112807 : (itemid == 2240003 ? 1112809 : (1112300 + (itemid - 2240004)))));
                if (!MapleInventoryManipulator.checkSpace(c, newItemId, 1, "") || !MapleInventoryManipulator.checkSpace(chr.getClient(), newItemId, 1, "")) {
                    c.getSession().write(CWvsContext.sendEngagement((byte) 0x15, 0, null, null));
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
                try {
                    final int[] ringID = MapleRing.makeRing(newItemId, c.getPlayer(), chr);
                    Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(newItemId, ringID[1]);
                    MapleRing ring = MapleRing.loadFromDb(ringID[1]);
                    if (ring != null) {
                        eq.setRing(ring);
                    }
                    MapleInventoryManipulator.addbyItem(c, eq);

                    eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(newItemId, ringID[0]);
                    ring = MapleRing.loadFromDb(ringID[0]);
                    if (ring != null) {
                        eq.setRing(ring);
                    }
                    MapleInventoryManipulator.addbyItem(chr.getClient(), eq);

                    MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, chr.getMarriageItemId(), 1, false, false);

                    chr.getClient().getSession().write(CWvsContext.sendEngagement((byte) 0x10, newItemId, chr, c.getPlayer()));
                    chr.setMarriageId(c.getPlayer().getId());
                    c.getPlayer().setMarriageId(chr.getId());

                    chr.fakeRelog();
                    c.getPlayer().fakeRelog();
                } catch (Exception e) {
                    FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                }

            } else {
                chr.getClient().getSession().write(CWvsContext.sendEngagement((byte) 0x1E, 0, null, null));
            }
            c.getSession().write(CWvsContext.enableActions());
            chr.setMarriageItemId(0);
        } else if (mode == 3) { //drop, only works for ETC
            final int itemId = slea.readInt();
            final MapleInventoryType type = GameConstants.getInventoryType(itemId);
            final Item item = c.getPlayer().getInventory(type).findById(itemId);
            if (item != null && type == MapleInventoryType.ETC && itemId / 10000 == 421) {
                MapleInventoryManipulator.drop(c, type, item.getPosition(), item.getQuantity());
            }
        }
    }

    public static void Solomon(final LittleEndianAccessor slea, final MapleClient c) {
        c.getSession().write(CWvsContext.enableActions());
        c.getPlayer().updateTick(slea.readInt());
        Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slea.readShort());
        if (item == null || item.getItemId() != slea.readInt() || item.getQuantity() <= 0 || c.getPlayer().getGachExp() > 0 || c.getPlayer().getLevel() > 50 || MapleItemInformationProvider.getInstance().getItemEffect(item.getItemId()).getEXP() <= 0) {
            return;
        }
        c.getPlayer().setGachExp(c.getPlayer().getGachExp() + MapleItemInformationProvider.getInstance().getItemEffect(item.getItemId()).getEXP());
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, item.getPosition(), (short) 1, false);
        c.getSession().write(CWvsContext.enableActions());
    }

    public static void GachExp(final LittleEndianAccessor slea, final MapleClient c) {
        c.getSession().write(CWvsContext.enableActions());
        c.getPlayer().updateTick(slea.readInt());
        if (c.getPlayer().getGachExp() <= 0) {
            return;
        }
        c.getPlayer().gainExp(c.getPlayer().getGachExp() * GameConstants.getExpRate_Quest(c.getPlayer().getLevel()), true, true, false);
        c.getPlayer().setGachExp(0);
        c.getSession().write(CWvsContext.enableActions());
    }

    public static void Report(final LittleEndianAccessor slea, final MapleClient c) {
        //0 = success 1 = unable to locate 2 = once a day 3 = you've been reported 4+ = unknown reason
        MapleCharacter other;
        ReportType type;
        type = ReportType.getById(slea.readByte());
        other = c.getPlayer().getMap().getCharacterByName(slea.readMapleAsciiString());
        //then,byte(?) and string(reason)
        if (other == null || type == null || other.isIntern()) {
            c.getSession().write(CWvsContext.report(4));
            return;
        }
        final MapleQuestStatus stat = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.REPORT_QUEST));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        final long currentTime = System.currentTimeMillis();
        final long theTime = Long.parseLong(stat.getCustomData());
        if (theTime + 7200000 > currentTime && !c.getPlayer().isIntern()) {
            c.getSession().write(CWvsContext.enableActions());
            c.getPlayer().dropMessage(5, "你可能只报告每2小时.");
        } else {
            stat.setCustomData(String.valueOf(currentTime));
            other.addReport(type);
            c.getSession().write(CWvsContext.report(2));
        }
    }

    public static void exitSilentCrusadeUI(final LittleEndianAccessor slea, final MapleClient c) {
        c.getPlayer().updateInfoQuest(1652, "alert=-1"); //Hide Silent Crusade icon
    }

    public static void claimSilentCrusadeReward(final LittleEndianAccessor slea, final MapleClient c) {
        short chapter = slea.readShort();
        if (c.getPlayer() == null || !c.getPlayer().getInfoQuest(1648 + chapter).equals("m0=2;m1=2;m2=2;m3=2;m4=2")) {
            System.out.println("[Silent Crusade] " + c.getPlayer().getName() + "has tried to exploit the reward of chapter " + (chapter + 1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int use = c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        final int setup = c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot();
        final int etc = c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (use < 1 || setup < 1 || etc < 1) {
            c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 2));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        switch (chapter) {
            case 0:
                c.getPlayer().gainItem(3700031, 1);
                c.getPlayer().gainItem(4310029, 10);
                c.getPlayer().updateInfoQuest(1648, "m0=2;m1=2;m2=2;m3=2;m4=2;r=1"); //Show Reward Claimed
                break;
            case 1:
                c.getPlayer().gainItem(3700032, 1);
                c.getPlayer().gainItem(2430669, 1);
                c.getPlayer().gainItem(4310029, 15);
                c.getPlayer().updateInfoQuest(1649, "m0=2;m1=2;m2=2;m3=2;m4=2;r=1"); //Show Reward Claimed
                break;
            case 2:
                c.getPlayer().gainItem(3700033, 1);
                c.getPlayer().gainItem(2430668, 1);
                c.getPlayer().gainItem(4310029, 20);
                c.getPlayer().updateInfoQuest(1650, "m0=2;m1=2;m2=2;m3=2;m4=2;r=1"); //Show Reward Claimed
                break;
            case 3:
                c.getPlayer().gainItem(3700034, 1);
                c.getPlayer().gainItem(2049309, 1);
                c.getPlayer().gainItem(4310029, 30);
                c.getPlayer().updateInfoQuest(1651, "m0=2;m1=2;m2=2;m3=2;m4=2;r=1"); //Show Reward Claimed
                break;
            default:
                System.out.println("New Silent Crusade Chapter found: " + (chapter + 1));
        }
        c.getSession().write(CWvsContext.enableActions());
    }

    public static void buySilentCrusade(final LittleEndianAccessor slea, final MapleClient c) {
        //ui window is 0x49
        //slea: [00 00] [4F 46 11 00] [01 00]
        short slot = slea.readShort(); //slot of item in the silent crusade window
        int itemId = slea.readInt();
        short quantity = slea.readShort();
        int tokenPrice = 0, potentialGrade = 0;
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Etc.wz"));
        MapleData data = prov.getData("CrossHunterChapter.img");
        int currItemId = 0;
        for (final MapleData wzdata : data.getChildren()) {
            if (wzdata.getName().equals("Shop")) {
                for (final MapleData wzdata2 : wzdata.getChildren()) {
                    for (MapleData wzdata3 : wzdata2.getChildren()) {
                        switch (wzdata3.getName()) {
                            case "itemId":
                                currItemId = MapleDataTool.getInt(wzdata3);
                                break;
                            case "tokenPrice":
                                if (currItemId == itemId) {
                                    tokenPrice = MapleDataTool.getInt(wzdata3);
                                }
                                break;
                            case "potentialGrade":
                                if (currItemId == itemId) {
                                    potentialGrade = MapleDataTool.getInt(wzdata3);
                                }
                                break;
                        }
                    }
                }
            }
        }
        if (tokenPrice == 0) {
            System.out.println("[Silent Crusade] " + c.getPlayer().getName() + " has tried to exploit silent crusade shop.");
            c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 3));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (c.getPlayer().getInventory(GameConstants.getInventoryType(itemId)).getNumFreeSlot() >= quantity) {
            if (c.getPlayer().itemQuantity(4310029) < tokenPrice) {
                c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 1));
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4310029, tokenPrice, false, false);
                if (itemId < 2000000 && potentialGrade > 0) {
                    Equip equip = (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId);
                    equip.setQuantity((short) 1);
                    equip.setGMLog("BUY_SILENT_CRUSADE");
                    equip.setPotential1(-potentialGrade);
                    if (!MapleInventoryManipulator.addbyItem(c, equip)) {
                        c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 2));
                        c.getSession().write(CWvsContext.enableActions());
                        return;
                    }
                } else {
                    if (!MapleInventoryManipulator.addById(c, itemId, quantity, "BUY_SILENT_CRUSADE")) {
                        c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 2));
                        c.getSession().write(CWvsContext.enableActions());
                        return;
                    }
                }
                c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 0));
                c.getSession().write(CWvsContext.enableActions());
            } else {
                c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 2));
                c.getSession().write(CWvsContext.enableActions());
            }
        } else {
            c.getSession().write(CWvsContext.getSilentCrusadeMsg((byte) 2));
            c.getSession().write(CWvsContext.enableActions());
        }
    }

    public static void UpdatePlayerInformation(final LittleEndianAccessor slea, final MapleClient c) {
        byte mode = slea.readByte(); //01 open ui 03 save info
        if (mode == 1) {
            if (c.getPlayer().getQuestStatus(GameConstants.PLAYER_INFORMATION) > 0) {
                try {
                    String[] info = c.getPlayer().getQuest(MapleQuest.getInstance(GameConstants.PLAYER_INFORMATION)).getCustomData().split(";");
                    c.getSession().write(CWvsContext.loadInformation((byte) 2, Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]), true));
                } catch (NumberFormatException ex) {
                    c.getSession().write(CWvsContext.loadInformation((byte) 4, 0, 0, 0, 0, false));
                    System.out.println("Failed to update account information: " + ex);
                }
            }
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (mode != 3) {
            System.out.println("new account information mode found: " + mode);
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        int country = slea.readInt();
        int birthday = slea.readInt();
        int favoriteAction = slea.readInt(); //kind of mask
        int favoriteLocation = slea.readInt(); //kind of mask
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.PLAYER_INFORMATION)).setCustomData("location=" + country + ";birthday=" + birthday + ";favoriteaction=" + favoriteAction + ";favoritelocation=" + favoriteLocation);
    }

    public static void FindFriends(final LittleEndianAccessor slea, final MapleClient c) {
        byte mode = slea.readByte();
        switch (mode) {
            case 5:
                if (c.getPlayer().getQuestStatus(GameConstants.PLAYER_INFORMATION) == 0) {
                    c.getSession().write(CWvsContext.findFriendResult((byte) 6, null, 0, null));
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
            case 7:
                List<MapleCharacter> characters = new LinkedList();
                for (MapleCharacter chr : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    if (chr != c.getPlayer()) {
                        if (c.getPlayer().getQuestStatus(GameConstants.PLAYER_INFORMATION) == 0 || characters.isEmpty()) {
                            characters.add(chr);
                        } else {
                            if (chr.getQuestStatus(GameConstants.PLAYER_INFORMATION) == 0 && characters.isEmpty()) {
                                continue;
                            }
                            String[] info = c.getPlayer().getQuest(MapleQuest.getInstance(GameConstants.PLAYER_INFORMATION)).getCustomData().split(";");
                            String[] info2 = chr.getQuest(MapleQuest.getInstance(GameConstants.PLAYER_INFORMATION)).getCustomData().split(";");
                            if (info[0].equals(info2[0]) || info[1].equals(info2[1]) || info[2].equals(info2[2]) || info[3].equals(info2[3])) {
                                characters.add(chr);
                            }
                        }
                    }
                }
                if (characters.isEmpty()) {
                    c.getSession().write(CWvsContext.findFriendResult((byte) 9, null, 12, null));
                } else {
                    c.getSession().write(CWvsContext.findFriendResult((byte) 8, characters, 0, null));
                }
                break;
        }
    }

    public static void LinkSkill(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        //slea: [76 7F 31 01] [35 00 00 00]
        int skill = slea.readInt();
        int cid = slea.readInt();
        boolean found = false;
        for (MapleCharacter chr2 : c.loadCharacters(c.getPlayer().getWorld())) {
            if (chr2.getId() == cid) {
                found = true;
            }
        }
        if (GameConstants.getLinkSkillByJob(chr.getJob()) != skill || !found || chr.getLevel() > 10) {
            c.getPlayer().dropMessage(1, "发生了一个错误.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        MapleCharacter.addLinkSkill(cid, skill);
    }

    public static void MonsterBookInfoRequest(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return;
        }
        slea.readInt(); // tick
        final MapleCharacter player = c.getPlayer().getMap().getCharacterById(slea.readInt());
        c.getSession().write(CWvsContext.enableActions());
        if (player != null && !player.isClone()) {
            if (!player.isGM() || c.getPlayer().isGM()) {
                c.getSession().write(CWvsContext.getMonsterBookInfo(player));
            }
        }
    }

    public static void MonsterBookDropsRequest(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return;
        }
        chr.updateTick(slea.readInt()); // tick
        final int cardid = slea.readInt();
        final int mobid = MapleItemInformationProvider.getInstance().getCardMobId(cardid);
        if (mobid <= 0 || !chr.getMonsterBook().hasCard(cardid)) {
            c.getSession().write(CWvsContext.getCardDrops(cardid, null));
            return;
        }
        final MapleMonsterInformationProvider ii = MapleMonsterInformationProvider.getInstance();
        final List<Integer> newDrops = new ArrayList<>();
        for (final MonsterDropEntry de : ii.retrieveDrop(mobid)) {
            if (de.itemId > 0 && de.questid <= 0 && !newDrops.contains(de.itemId)) {
                newDrops.add(de.itemId);
            }
        }
        for (final MonsterGlobalDropEntry de : ii.getGlobalDrop()) {
            if (de.itemId > 0 && de.questid <= 0 && !newDrops.contains(de.itemId)) {
                newDrops.add(de.itemId);
            }
        }
        c.getSession().write(CWvsContext.getCardDrops(cardid, newDrops));
    }

    public static void ChangeSet(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
//        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
//            return;
//        }
//        final int set = slea.readInt();
//        if (chr.getMonsterBook().changeSet(set)) {
//            chr.getMonsterBook().applyBook(chr, false);
//            chr.getQuestNAdd(MapleQuest.getInstance(GameConstants.CURRENT_SET)).setCustomData(String.valueOf(set));
//            c.getSession().write(CWvsContext.changeCardSet(set));
//        }
    }

    public static void EnterPVP(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || c.getPlayer().getMapId() != 960000000) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (c.getPlayer().getParty() != null) {
            c.getSession().write(CField.pvpBlocked(9));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        c.getPlayer().updateTick(slea.readInt());
        slea.skip(1);
        int type = slea.readByte(), lvl = slea.readByte(), playerCount = 0;
        boolean passed = false;
        switch (lvl) {
            case 0:
                passed = c.getPlayer().getLevel() >= 30 && c.getPlayer().getLevel() < 70;
                break;
            case 1:
                passed = c.getPlayer().getLevel() >= 70;
                break;
            case 2:
                passed = c.getPlayer().getLevel() >= 120;
                break;
            case 3:
                passed = c.getPlayer().getLevel() >= 180;
                break;
        }
        final EventManager em = c.getChannelServer().getEventSM().getEventManager("PVP");
        if (!passed || em == null) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final List<Integer> maps = new ArrayList<>();
        switch (type) {
            case 0:
                maps.add(960010100);
                maps.add(960010101);
                maps.add(960010102);
                break;
            case 1:
                maps.add(960020100);
                maps.add(960020101);
                maps.add(960020102);
                maps.add(960020103);
                break;
            case 2:
                maps.add(960030100);
                break;
            case 3:
                maps.add(689000000);
                maps.add(689000010);
                break;
            default:
                passed = false;
                break;
        }
        if (!passed) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        c.getPlayer().getStat().heal(c.getPlayer());
        c.getPlayer().cancelAllBuffs();
        c.getPlayer().dispelDebuffs();
        c.getPlayer().changeRemoval();
        c.getPlayer().clearAllCooldowns();
        c.getPlayer().unequipAllPets();
        final StringBuilder key = new StringBuilder().append(lvl).append(" ").append(type).append(" ");
        //check if any of the maps are available
        for (int i : maps) {
            final EventInstanceManager eim = em.getInstance(new StringBuilder("PVP").append(key.toString()).append(i).toString().replace(" ", "").replace(" ", ""));
            if (eim != null && (eim.getProperty("started").equals("0") || eim.getPlayerCount() < 10)) {
                eim.registerPlayer(c.getPlayer());
                return;
            }
        }
        //make one
        em.startInstance_Solo(key.append(maps.get(Randomizer.nextInt(maps.size()))).toString(), c.getPlayer());
    }

    public static void RespawnPVP(final LittleEndianAccessor slea, final MapleClient c) {
        final Lock ThreadLock = new ReentrantLock();
        /*if (c.getPlayer() == null || c.getPlayer().getMap() == null || !c.getPlayer().inPVP() || c.getPlayer().isAlive()) {
         c.getSession().write(CWvsContext.enableActions());
         return;
         }*/
        final int type = Integer.parseInt(c.getPlayer().getEventInstance().getProperty("type"));
        byte lvl = 0;
        c.getPlayer().getStat().heal_noUpdate(c.getPlayer());
        c.getPlayer().updateSingleStat(MapleStat.MP, c.getPlayer().getStat().getMp());
        //c.getPlayer().getEventInstance().schedule("broadcastType", 500);   
        ThreadLock.lock();
        try {
            c.getPlayer().getEventInstance().schedule("updateScoreboard", 500);
        } finally {
            ThreadLock.unlock();
        }
        c.getPlayer().changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().getPortal(type == 0 ? 0 : (type == 3 ? (c.getPlayer().getTeam() == 0 ? 3 : 1) : (c.getPlayer().getTeam() == 0 ? 2 : 3))));
        c.getSession().write(CField.getPVPScore(Integer.parseInt(c.getPlayer().getEventInstance().getProperty(String.valueOf(c.getPlayer().getId()))), false));

        if (c.getPlayer().getLevel() >= 30 && c.getPlayer().getLevel() < 70) {
            lvl = 0;
        } else if (c.getPlayer().getLevel() >= 70 && c.getPlayer().getLevel() < 120) {
            lvl = 1;
        } else if (c.getPlayer().getLevel() >= 120 && c.getPlayer().getLevel() < 180) {
            lvl = 2;
        } else if (c.getPlayer().getLevel() >= 180) {
            lvl = 3;
        }

        List<MapleCharacter> players = c.getPlayer().getEventInstance().getPlayers();
        List<Pair<Integer, String>> players1 = new LinkedList<>();
        for (int xx = 0; xx < players.size(); xx++) {
            players1.add(new Pair<>(players.get(xx).getId(), players.get(xx).getName()));
        }
        c.getSession().write(CField.getPVPType(type, players1, c.getPlayer().getTeam(), true, lvl));
        c.getSession().write(CField.enablePVP(true));
    }

    public static void LeavePVP(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || !c.getPlayer().inPVP()) {
            c.getSession().write(CField.pvpBlocked(6));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        int x = Integer.parseInt(c.getPlayer().getEventInstance().getProperty(String.valueOf(c.getPlayer().getId())));
        final int lv = Integer.parseInt(c.getPlayer().getEventInstance().getProperty("lvl"));
        if (lv < 2 && c.getPlayer().getLevel() >= 120) { //gladiator, level 120+
            x /= 2;
        }
        c.getPlayer().setTotalBattleExp(c.getPlayer().getTotalBattleExp() + ((x / 10) * 3 / 2));
        c.getPlayer().setBattlePoints(c.getPlayer().getBattlePoints() + ((x / 10) * 3 / 2)); //PVP 1.5 EVENT!
        c.getPlayer().cancelAllBuffs();
        c.getPlayer().changeRemoval();
        c.getPlayer().dispelDebuffs();
        c.getPlayer().clearAllCooldowns();
        c.getPlayer().updateTick(slea.readInt());
        c.getSession().write(CWvsContext.clearMidMsg());
        c.getPlayer().changeMap(c.getChannelServer().getMapFactory().getMap(960000000));
        c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        c.getPlayer().getStat().heal(c.getPlayer());
    }

    public static void EnterAzwan(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || c.getPlayer().getMapId() != 262000300) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (c.getPlayer().getLevel() < 40) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        byte mode = slea.readByte();
        byte difficult = slea.readByte();
        byte party = slea.readByte();
        int mapid = 262020000 + (mode * 1000) + difficult; //Supply doesn't have difficult but it's always 0 so idc
        if (party == 1 && c.getPlayer().getParty() == null) {
            c.getSession().write(CField.pvpBlocked(9));
            c.getSession().write(CWvsContext.enableActions());
        }
        if (party == 1 && c.getPlayer().getParty() != null) {
            for (MaplePartyCharacter partymembers : c.getPlayer().getParty().getMembers()) {
                if (c.getChannelServer().getPlayerStorage().getCharacterById(partymembers.getId()).getMapId() != 262000300) {
                    c.getPlayer().dropMessage(1, "请确保所有的组员都在同一张地图上.");
                    c.getSession().write(CWvsContext.enableActions());
                }
            }
        }
        if (party == 1 && c.getPlayer().getParty() != null) {
            for (MaplePartyCharacter partymember : c.getPlayer().getParty().getMembers()) {
                c.getChannelServer().getPlayerStorage().getCharacterById(partymember.getId()).changeMap(c.getChannelServer().getMapFactory().getMap(mapid));
            }
        } else {
            //party = 0;
            c.getPlayer().changeMap(c.getChannelServer().getMapFactory().getMap(mapid));
        }
        //EventManager em = c.getChannelServer().getEventSM().getEventManager("Azwan");
        //EventInstanceManager eim = em.newInstance("Azwan");
        //eim.setProperty("Global_StartMap", mapid + "");
        //eim.setProperty("Global_ExitMap", (party == 1 ? 262000100 : 262000200) + "");
        //eim.setProperty("Global_MinPerson", 1 + "");
        //eim.setProperty("Global_RewardMap", (party == 1 ? 262000100 : 262000200) + "");
        //eim.setProperty("CurrentStage", "1");
    }

    public static void EnterAzwanEvent(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            c.getSession().write(CField.pvpBlocked(1));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        int mapid = slea.readInt();
        c.getPlayer().changeMap(c.getChannelServer().getMapFactory().getMap(mapid));
    }

    public static void LeaveAzwan(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || !c.getPlayer().inAzwan()) {
            c.getSession().write(CField.pvpBlocked(6));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        //c.getPlayer().cancelAllBuffs();
        //c.getPlayer().changeRemoval();
        //c.getPlayer().dispelDebuffs();
        //c.getPlayer().clearAllCooldowns();
        //c.getSession().write(CWvsContext.clearMidMsg());
        //c.getPlayer().changeMap(c.getChannelServer().getMapFactory().getMap(262000200));
        c.getSession().write(CField.showEffect("hillah/fail"));
        c.getSession().write(CField.UIPacket.sendAzwanResult());
        //c.getPlayer().getStats().recalcLocalStats(c.getPlayer());
        //c.getPlayer().getStats().heal(c.getPlayer());
    }

    public static void reviveAzwan(LittleEndianAccessor slea, MapleClient c) {
        if (c.getPlayer() == null) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (!GameConstants.isAzwanMap(c.getPlayer().getMapId())) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        c.getPlayer().changeMap(c.getPlayer().getMapId(), 0);
        c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        c.getPlayer().getStat().heal(c.getPlayer());
    }

    public static void magicWheel(LittleEndianAccessor slea, MapleClient c) {
        final byte mode = slea.readByte(); // 0 = open 2 = start 4 = receive reward
        if (mode == 2) {
            slea.readInt(); //4
            final short toUseSlot = slea.readShort();
            slea.readShort();
            final int tokenId = slea.readInt();
            if (c.getPlayer().getInventory(MapleInventoryType.ETC).getItem(toUseSlot).getItemId() != tokenId) {
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            for (byte inv = 1; inv <= 5; inv++) {
                if (c.getPlayer().getInventory(MapleInventoryType.getByType(inv)).getNumFreeSlot() < 2) {
                    c.getSession().write(CWvsContext.magicWheel((byte) 7, null, null, 0));
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
            }
            List<Integer> items = new LinkedList();
            GameConstants.loadWheelRewards(items, tokenId);
            int end = Randomizer.nextInt(10);
            String data = "Magic Wheel";
            c.getPlayer().setWheelItem(items.get(end));
            if (!MapleInventoryManipulator.removeFromSlot_Lock(c, GameConstants.getInventoryType(tokenId), toUseSlot, (short) 1, false, false)) {
                c.getSession().write(CWvsContext.magicWheel((byte) 9, null, null, 0));
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            c.getSession().write(CWvsContext.magicWheel((byte) 3, items, data, end));
        } else if (mode == 4) {
            final String data = slea.readMapleAsciiString();
            int item;
            //try {
            //item = Integer.parseInt(data) / 2;
            item = c.getPlayer().getWheelItem();
            if (item == 0 || !MapleInventoryManipulator.addById(c, item, (short) 1, null)) {
                c.getSession().write(CWvsContext.magicWheel((byte) 0xA, null, null, 0));
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            //} catch (Exception ex) {
            //    c.getSession().write(CWvsContext.magicWheel((byte) 0xA, null, null, 0));
            //    c.getSession().write(CWvsContext.enableActions());
            //    return;
            //}
            c.getPlayer().setWheelItem(0);
            c.getSession().write(CWvsContext.magicWheel((byte) 5, null, null, 0));
        }
    }

    public static void onReward(LittleEndianAccessor slea, MapleClient c) throws SQLException {
//        System.err.println("onReward");
        int id = slea.readInt();
        int type = slea.readInt();
        int itemId = slea.readInt();
        slea.readInt(); //might be item quantity
        slea.readInt(); //no idea
        slea.readLong(); //no idea
        slea.readInt(); //no idea
        int mp = slea.readInt();
        int meso = slea.readInt();
        int exp = slea.readInt();
        slea.readInt(); //no idea
        slea.readInt(); //no idea
        slea.readMapleAsciiString(); //no idea
        slea.readMapleAsciiString(); //no idea
        slea.readMapleAsciiString(); //no idea
        byte mode = slea.readByte();
        if (mode == 2) { //Decline
            c.getPlayer().deleteReward(id);
            c.getSession().write(CWvsContext.enableActions());
            return;
        } else if (mode == 1) { //Accept
            if (type < 0 || type > 5) {
                System.out.println("[Hacking Attempt] " + c.getPlayer().getName() + " has tried to receive reward with unavailable type.");
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            MapleReward reward = c.getPlayer().getReward(id);
            if (reward == null) {
                c.getSession().write(Reward.receiveReward(id, (byte) 0x15, 0));
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            if (reward.getType() != type || reward.getItem() != itemId
                    || reward.getMaplePoints() != mp || reward.getMeso() != meso
                    || reward.getExp() != exp) {
                System.out.println("[Hacking Attempt] " + c.getPlayer().getName() + " has tried to exploit the reward receive.");
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            byte msg = 0x15;
            int quantity = 0;
            switch (type) {
                case 1:
                    if (MapleInventoryManipulator.checkSpace(c, itemId, 1, "")) {
                        c.getPlayer().gainItem(itemId, 1);
                        c.getPlayer().deleteReward(id);
                        quantity = 1;
                        msg = 0x0C;
                    } else {
                        msg = 0x16;
                    }
                    break;
                case 3:
                    if (c.getPlayer().getCSPoints(2) + mp >= 0) {
                        c.getPlayer().modifyCSPoints(2, mp, false);
                        c.getPlayer().deleteReward(id);
                        quantity = mp;
                        msg = 0x0B;
                    } else {
                        msg = 0x14;
                    }
                    break;
                case 4:
                    if (c.getPlayer().getMeso() + meso < Integer.MAX_VALUE
                            && c.getPlayer().getMeso() + meso > 0) {
                        c.getPlayer().gainMeso(meso, true, true);
                        c.getPlayer().deleteReward(id);
                        quantity = meso;
                        msg = 0x0E;
                    } else {
                        msg = 0x17;
                    }
                    break;
                case 5:
                    int maxlevel = GameConstants.isKOC(c.getPlayer().getJob()) ? 120 : 200;
                    if (c.getPlayer().getLevel() < maxlevel) {
                        c.getPlayer().gainExp(exp, true, true, true);
                        c.getPlayer().deleteReward(id);
                        quantity = exp;
                        msg = 0x0F;
                    } else {
                        msg = 0x18;
                    }
                    break;
                default:
                    System.out.println("New reward type found: " + type);
                    break;
            }
            c.getSession().write(Reward.receiveReward(id, msg, quantity));
        }
        if (mode < 0 || mode > 2) {
            System.out.println("New reward mode found: " + mode);
        }
    }

    public static void blackFriday(LittleEndianAccessor slea, MapleClient c) {
        SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd");
        sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.getPlayer().updateInfoQuest(5604, sdfGMT.format(Calendar.getInstance().getTime()).replaceAll("-", ""));
        System.out.println(sdfGMT.format(Calendar.getInstance().getTime()).replaceAll("-", ""));
    }

    public static void updateRedLeafHigh(LittleEndianAccessor slea, MapleClient c) { //not finished yet
        //TODO: load and set red leaf high in sql
        slea.readInt(); //questid or something
        slea.readInt(); //joe joe quest
        int joejoe = slea.readInt();
        slea.readInt(); //hermoninny quest
        int hermoninny = slea.readInt();
        slea.readInt(); //little dragon quest
        int littledragon = slea.readInt();
        slea.readInt(); //ika quest
        int ika = slea.readInt();
        if (joejoe + hermoninny + littledragon + ika != c.getPlayer().getFriendShipToAdd()) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        c.getPlayer().setFriendShipPoints(joejoe, hermoninny, littledragon, ika);
    }

    public static void StealSkill(LittleEndianAccessor slea, MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || !GameConstants.isPhantom(c.getPlayer().getJob())) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int skill = slea.readInt();
        final int cid = slea.readInt();

        //then a byte, 0 = learning, 1 = removing, but it doesnt matter since we can just use cid
        if (cid <= 0) {
            c.getPlayer().removeStolenSkill(skill);
        } else {
            final MapleCharacter other = c.getPlayer().getMap().getCharacterById(cid);
            if (other != null && other.getId() != c.getPlayer().getId() && other.getTotalSkillLevel(skill) > 0) {
                c.getPlayer().addStolenSkill(skill, other.getTotalSkillLevel(skill));
            }
        }
    }

    public static void ChooseSkill(LittleEndianAccessor slea, MapleClient c) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null || !GameConstants.isPhantom(c.getPlayer().getJob())) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int base = slea.readInt();
        final int skill = slea.readInt();
        if (skill <= 0) {
            c.getPlayer().unchooseStolenSkill(base);
        } else {
            c.getPlayer().chooseStolenSkill(skill);
        }
    }

    public static void viewSkills(final LittleEndianAccessor slea, final MapleClient c) {
        int victim = slea.readInt();
        int jobid = c.getChannelServer().getPlayerStorage().getCharacterById(victim).getJob();
        List<Integer> list = SkillFactory.getSkillsByJob(jobid);
        if (!c.getChannelServer().getPlayerStorage().getCharacterById(victim).getSkills().isEmpty() && GameConstants.isAdventurer(jobid)) {
            c.getSession().write(CField.viewSkills(c.getChannelServer().getPlayerStorage().getCharacterById(victim)));
        } else {
            c.getPlayer().dropMessage(6, "你不能是用非冒险家技能");
        }
    }

    public static void AttackPVP(final LittleEndianAccessor slea, final MapleClient c) {
        final Lock ThreadLock = new ReentrantLock();
        final MapleCharacter chr = c.getPlayer();
        final int trueSkill = slea.readInt();
        int skillid = trueSkill;
        if (chr == null || chr.isHidden() || !chr.isAlive() || chr.hasBlockedInventory() || chr.getMap() == null || !chr.inPVP() || !chr.getEventInstance().getProperty("started").equals("1") || skillid >= 90000000) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int lvl = Integer.parseInt(chr.getEventInstance().getProperty("lvl"));
        final int type = Integer.parseInt(chr.getEventInstance().getProperty("type"));
        final int ice = Integer.parseInt(chr.getEventInstance().getProperty("ice"));
        final int ourScore = Integer.parseInt(chr.getEventInstance().getProperty(String.valueOf(chr.getId())));
        int addedScore = 0, skillLevel = 0, trueSkillLevel = 0, animation = -1, attackCount, mobCount = 1, fakeMastery = chr.getStat().passive_mastery(), ignoreDEF = chr.getStat().ignoreTargetDEF, critRate = chr.getStat().passive_sharpeye_rate(), skillDamage = 100;
        boolean magic = false, move = false, pull = false, push = false;

        double maxdamage = lvl == 3 ? chr.getStat().getCurrentMaxBasePVPDamageL() : chr.getStat().getCurrentMaxBasePVPDamage();
        MapleStatEffect effect = null;
        chr.checkFollow();
        Rectangle box;

        final Item weapon = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        final Item shield = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
        final boolean katara = shield != null && shield.getItemId() / 10000 == 134;
        final boolean aran = weapon != null && weapon.getItemId() / 10000 == 144 && GameConstants.isAran(chr.getJob());
        slea.skip(1); //skill level
        int chargeTime = 0;
        if (GameConstants.isMagicChargeSkill(skillid)) {
            chargeTime = slea.readInt();
        } else {
            slea.skip(4);
        }
        boolean facingLeft = slea.readByte() > 0;
        if (skillid > 0) {
            if (skillid == 3211006 && chr.getTotalSkillLevel(3220010) > 0) { //hack
                skillid = 3220010;
            }
            final Skill skil = SkillFactory.getSkill(skillid);
            if (skil == null || skil.isPVPDisabled()) {
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            magic = skil.isMagic();
            move = skil.isMovement();
            push = skil.isPush();
            pull = skil.isPull();
            if (chr.getTotalSkillLevel(GameConstants.getLinkedAttackSkill(skillid)) <= 0) {
                if (!GameConstants.isIceKnightSkill(skillid) && chr.getTotalSkillLevel(GameConstants.getLinkedAttackSkill(skillid)) <= 0) {
                    c.getSession().close();
                    FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                    return;
                }
                if (GameConstants.isIceKnightSkill(skillid) && chr.getBuffSource(MapleBuffStat.MORPH) % 10000 != 1105) {
                    FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                    return;
                }
            }
            animation = skil.getAnimation();
            if (animation == -1 && !skil.isMagic()) {
                final String after = aran ? "aran" : (katara ? "katara" : (weapon == null ? "barehands" : MapleItemInformationProvider.getInstance().getAfterImage(weapon.getItemId())));
                if (after != null) {
                    final List<Triple<String, Point, Point>> p = MapleItemInformationProvider.getInstance().getAfterImage(after); //hack
                    if (p != null) {
                        ThreadLock.lock();
                        try {
                            while (animation == -1) {
                                final Triple<String, Point, Point> ep = p.get(Randomizer.nextInt(p.size()));
                                if (!ep.left.contains("stab") && (skillid == 4001002 || skillid == 14001002)) { //disorder hack
                                    continue;
                                } else if (ep.left.contains("stab") && weapon != null && weapon.getItemId() / 10000 == 144) {
                                    continue;
                                }
                                if (SkillFactory.getDelay(ep.left) != null) {
                                    animation = SkillFactory.getDelay(ep.left);
                                }
                            }
                        } finally {
                            ThreadLock.unlock();
                        }
                    }
                }
            } else if (animation == -1 && skil.isMagic()) {
                animation = SkillFactory.getDelay(Randomizer.nextBoolean() ? "dash" : "dash2");
            }
            if (skil.isMagic()) {
                fakeMastery = 0; //whoosh still comes if you put this higher than 0
            }
            skillLevel = chr.getTotalSkillLevel(GameConstants.getLinkedAttackSkill(skillid));
            trueSkillLevel = chr.getTotalSkillLevel(GameConstants.getLinkedAttackSkill(trueSkill));
            effect = skil.getPVPEffect(skillLevel);
            ignoreDEF += effect.getIgnoreMob();
            critRate += effect.getCr();

            skillDamage = (effect.getDamage() + chr.getStat().getDamageIncrease(skillid));
            box = effect.calculateBoundingBox(chr.getTruePosition(), facingLeft, chr.getStat().defRange);
            attackCount = Math.max(effect.getBulletCount(), effect.getAttackCount());
            mobCount = Math.max(1, effect.getMobCount());
            if (effect.getCooldown(chr) > 0 && !chr.isGM()) {
                if (chr.skillisCooling(skillid)) {
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }
                if ((skillid != 35111004 && skillid != 35121013) || chr.getBuffSource(MapleBuffStat.MECH_CHANGE) != skillid) { // Battleship
                    c.getSession().write(CField.skillCooldown(skillid, effect.getCooldown(chr)));
                    chr.addCooldown(skillid, System.currentTimeMillis(), effect.getCooldown(chr) * 1000);
                }
            }
            switch (chr.getJob()) {
                case 111:
                case 112:
                case 1111:
                case 1112:
                    if (PlayerHandler.isFinisher(skillid) > 0) { // finisher
                        if (chr.getBuffedValue(MapleBuffStat.COMBO) == null || chr.getBuffedValue(MapleBuffStat.COMBO) <= 2) {
                            return;
                        }
                        skillDamage *= (chr.getBuffedValue(MapleBuffStat.COMBO) - 1) / 2;
                        chr.handleOrbconsume(PlayerHandler.isFinisher(skillid));
                    }
                    break;
            }
        } else {
            attackCount = (katara ? 2 : 1);
            Point lt = null, rb = null;
            final String after = aran ? "aran" : (katara ? "katara" : (weapon == null ? "barehands" : MapleItemInformationProvider.getInstance().getAfterImage(weapon.getItemId())));
            if (after != null) {
                final List<Triple<String, Point, Point>> p = MapleItemInformationProvider.getInstance().getAfterImage(after);
                if (p != null) {
                    ThreadLock.lock();
                    try {
                        while (animation == -1) {
                            final Triple<String, Point, Point> ep = p.get(Randomizer.nextInt(p.size()));
                            if (!ep.left.contains("stab") && (skillid == 4001002 || skillid == 14001002)) { //disorder hack
                                continue;
                            } else if (ep.left.contains("stab") && weapon != null && weapon.getItemId() / 10000 == 147) {
                                continue;
                            }
                            if (SkillFactory.getDelay(ep.left) != null) {
                                animation = SkillFactory.getDelay(ep.left);
                                lt = ep.mid;
                                rb = ep.right;
                            }
                        }
                    } finally {
                        ThreadLock.unlock();
                    }
                }
            }
            box = MapleStatEffect.calculateBoundingBox(chr.getTruePosition(), facingLeft, lt, rb, chr.getStat().defRange);
        }
        chr.getCheatTracker().checkPVPAttack(skillid);
        final MapleStatEffect shad = chr.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
        final int originalAttackCount = attackCount;
        attackCount *= (shad != null ? 2 : 1);

        slea.skip(4); //?idk
        final int speed = slea.readByte();
        final int slot = slea.readShort();
        final int csstar = slea.readShort();
        int visProjectile = 0;
        if ((chr.getJob() >= 3500 && chr.getJob() <= 3512) || GameConstants.isJett(chr.getJob())) {
            visProjectile = 2333000;
        } else if (GameConstants.isCannon(chr.getJob())) {
            visProjectile = 2333001;
        } else if (!GameConstants.isMercedes(chr.getJob()) && chr.getBuffedValue(MapleBuffStat.SOULARROW) == null && slot > 0) {
            Item ipp = chr.getInventory(MapleInventoryType.USE).getItem((short) slot);
            if (ipp == null) {
                return;
            }
            if (csstar > 0) {
                ipp = chr.getInventory(MapleInventoryType.CASH).getItem((short) csstar);
                if (ipp == null) {
                    return;
                }
            }
            visProjectile = ipp.getItemId();
        }
        maxdamage *= skillDamage / 100.0;
        maxdamage *= chr.getStat().dam_r / 100.0;
        final List<AttackPair> ourAttacks = new ArrayList<>(mobCount);
        final boolean area = inArea(chr);
        boolean didAttack = false, killed = false;
        if (!area) {
            List<Pair<Integer, Boolean>> attacks;
            for (MapleCharacter attacked : chr.getMap().getCharactersIntersect(box)) {
                if (attacked.getId() != chr.getId() && attacked.isAlive() && !attacked.isHidden() && (type == 0 || attacked.getTeam() != chr.getTeam())) {
                    double rawDamage = maxdamage / Math.max(1, ((magic ? attacked.getStat().mdef : attacked.getStat().wdef) * Math.max(1.0, 100.0 - ignoreDEF) / 100.0) * (type == 3 ? 0.2 : 0.5));
                    if (attacked.getBuffedValue(MapleBuffStat.INVINCIBILITY) != null || inArea(attacked)) {
                        rawDamage = 0;
                    }
                    rawDamage *= attacked.getStat().mesoGuard / 100.0;
                    rawDamage += (rawDamage * chr.getDamageIncrease(attacked.getId()) / 100.0);
                    rawDamage = attacked.modifyDamageTaken(rawDamage, attacked).left;
                    final double min = (rawDamage * chr.getStat().trueMastery / 100.0);
                    attacks = new ArrayList<>(attackCount);
                    int totalMPLoss = 0, totalHPLoss = 0;
                    ThreadLock.lock();
                    try {
                        for (int i = 0; i < attackCount; i++) {
                            boolean critical_ = false;
                            int mploss = 0;
                            double ourDamage = Randomizer.nextInt((int) Math.abs(Math.round(rawDamage - min)) + 2) + min;
                            if (attacked.getStat().dodgeChance > 0 && Randomizer.nextInt(100) < attacked.getStat().dodgeChance) {
                                ourDamage = 0;
                            } else if (attacked.hasDisease(MapleDisease.DARKNESS) && Randomizer.nextInt(100) < 50) {
                                ourDamage = 0;
                                //i dont think level actually matters or it'd be too op
                                //} else if (attacked.getLevel() > chr.getLevel() && Randomizer.nextInt(100) < (attacked.getLevel() - chr.getLevel())) {
                                //	ourDamage = 0;
                            } else if (attacked.getJob() == 122 && attacked.getTotalSkillLevel(1220006) > 0 && attacked.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -10) != null) {
                                final MapleStatEffect eff = SkillFactory.getSkill(1220006).getEffect(attacked.getTotalSkillLevel(1220006));
                                if (eff.makeChanceResult()) {
                                    ourDamage = 0;
                                }
                            } else if (attacked.getJob() == 412 && attacked.getTotalSkillLevel(4120002) > 0) {
                                final MapleStatEffect eff = SkillFactory.getSkill(4120002).getEffect(attacked.getTotalSkillLevel(4120002));
                                if (eff.makeChanceResult()) {
                                    ourDamage = 0;
                                }
                            } else if (attacked.getJob() == 422 && attacked.getTotalSkillLevel(4220006) > 0) {
                                final MapleStatEffect eff = SkillFactory.getSkill(4220002).getEffect(attacked.getTotalSkillLevel(4220002));
                                if (eff.makeChanceResult()) {
                                    ourDamage = 0;
                                }
                            } else if (shad != null && i >= originalAttackCount) {
                                ourDamage *= shad.getX() / 100.0;
                            }
                            if (ourDamage > 0 && skillid != 4211006 && skillid != 3211003 && skillid != 4111004 && (skillid == 4221001 || skillid == 3221007 || skillid == 23121003 || skillid == 4341005 || skillid == 4331006 || skillid == 21120005 || Randomizer.nextInt(100) < critRate)) {
                                ourDamage *= (100.0 + (Randomizer.nextInt(Math.max(2, chr.getStat().passive_sharpeye_percent() - chr.getStat().passive_sharpeye_min_percent())) + chr.getStat().passive_sharpeye_min_percent())) / 100.0;
                                critical_ = true;
                            }
                            if (attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                                mploss = (int) Math.min(attacked.getStat().getMp(), (ourDamage * attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD).doubleValue() / 100.0));
                            }
                            ourDamage -= mploss;
                            if (attacked.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                                mploss = 0;
                            }
                            attacks.add(new Pair<>((int) Math.floor(ourDamage), critical_));

                            totalHPLoss += Math.floor(ourDamage);
                            totalMPLoss += mploss;
                        }
                    } finally {
                        ThreadLock.unlock();
                    }
                    if (GameConstants.isDemonSlayer(chr.getJob())) {
                        chr.handleForceGain(attacked.getObjectId(), skillid);
                    }
                    addedScore += Math.min(attacked.getStat().getHp() / 100, (totalHPLoss / 100) + (totalMPLoss / 100)); //ive NO idea
                    attacked.addMPHP(-totalHPLoss, -totalMPLoss);
                    ourAttacks.add(new AttackPair(attacked.getId(), attacked.getPosition(), attacks));
                    chr.onAttack(attacked.getStat().getCurrentMaxHp(), attacked.getStat().getCurrentMaxMp(attacked.getJob()), skillid, attacked.getObjectId(), totalHPLoss, 0);
                    attacked.getCheatTracker().setAttacksWithoutHit(false);
                    if (totalHPLoss > 0) {
                        didAttack = true;
                    }
                    if (attacked.getStat().getHPPercent() <= 20) {
                        SkillFactory.getSkill(PlayerStats.getSkillByJob(93, attacked.getJob())).getEffect(1).applyTo(attacked);
                    }
                    if (effect != null) {
                        if (effect.getMonsterStati().size() > 0 && effect.makeChanceResult()) {
                            ThreadLock.lock();
                            try {
                                for (Map.Entry<MonsterStatus, Integer> z : effect.getMonsterStati().entrySet()) {
                                    MapleDisease d = MonsterStatus.getLinkedDisease(z.getKey());
                                    if (d != null) {
                                        attacked.giveDebuff(d, z.getValue(), effect.getDuration(), d.getDisease(), 1);
                                    }
                                }
                            } finally {
                                ThreadLock.unlock();
                            }
                        }
                        effect.handleExtraPVP(chr, attacked);
                    }
                    if (chr.getJob() == 121 || chr.getJob() == 122 || chr.getJob() == 2110 || chr.getJob() == 2111 || chr.getJob() == 2112) { // WHITEKNIGHT
                        if (chr.getBuffSource(MapleBuffStat.WK_CHARGE) == 1211006 || chr.getBuffSource(MapleBuffStat.WK_CHARGE) == 21101006) {
                            final MapleStatEffect eff = chr.getStatForBuff(MapleBuffStat.WK_CHARGE);
                            if (eff.makeChanceResult()) {
                                attacked.giveDebuff(MapleDisease.FREEZE, 1, eff.getDuration(), MapleDisease.FREEZE.getDisease(), 1);
                            }
                        }
                    } else if (chr.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                        final MapleStatEffect eff = chr.getStatForBuff(MapleBuffStat.HAMSTRING);
                        if (eff != null && eff.makeChanceResult()) {
                            attacked.giveDebuff(MapleDisease.SLOW, 100 - Math.abs(eff.getX()), eff.getDuration(), MapleDisease.SLOW.getDisease(), 1);
                        }
                    } else if (chr.getBuffedValue(MapleBuffStat.SLOW) != null) {
                        final MapleStatEffect eff = chr.getStatForBuff(MapleBuffStat.SLOW);
                        if (eff != null && eff.makeChanceResult()) {
                            attacked.giveDebuff(MapleDisease.SLOW, 100 - Math.abs(eff.getX()), eff.getDuration(), MapleDisease.SLOW.getDisease(), 1);
                        }
                    } else if (chr.getJob() == 412 || chr.getJob() == 422 || chr.getJob() == 434 || chr.getJob() == 1411 || chr.getJob() == 1412) {
                        int[] skills = {4120005, 4220005, 4340001, 14110004};
                        ThreadLock.lock();
                        try {
                            for (int i : skills) {
                                final Skill skill = SkillFactory.getSkill(i);
                                if (chr.getTotalSkillLevel(skill) > 0) {
                                    final MapleStatEffect venomEffect = skill.getEffect(chr.getTotalSkillLevel(skill));
                                    if (venomEffect.makeChanceResult()) {// THIS MIGHT ACTUALLY BE THE DOT
                                        attacked.giveDebuff(MapleDisease.POISON, 1, venomEffect.getDuration(), MapleDisease.POISON.getDisease(), 1);
                                    }
                                    break;
                                }
                            }
                        } finally {
                            ThreadLock.unlock();
                        }
                    }
                    if ((chr.getJob() / 100) % 10 == 2) {//mage
                        int[] skills = {2000007, 12000006, 22000002, 32000012};
                        ThreadLock.lock();
                        try {
                            for (int i : skills) {
                                final Skill skill = SkillFactory.getSkill(i);
                                if (chr.getTotalSkillLevel(skill) > 0) {
                                    final MapleStatEffect venomEffect = skill.getEffect(chr.getTotalSkillLevel(skill));
                                    if (venomEffect.makeChanceResult()) {
                                        venomEffect.applyTo(attacked);
                                    }
                                    break;
                                }
                            }
                        } finally {
                            ThreadLock.unlock();
                        }
                    }
                    if (ice == attacked.getId()) {
                        chr.getClient().getSession().write(CField.getPVPIceHPBar(attacked.getStat().getHp(), attacked.getStat().getCurrentMaxHp()));
                    } else {
                        chr.getClient().getSession().write(CField.getPVPHPBar(attacked.getId(), attacked.getStat().getHp(), attacked.getStat().getCurrentMaxHp()));
                    }

                    if (!attacked.isAlive()) {
                        addedScore += 5; //i guess
                        killed = true;
                    }
                    if (ourAttacks.size() >= mobCount) {
                        break;
                    }
                }
            }
        } else if (type == 3) {
            if (Integer.parseInt(chr.getEventInstance().getProperty("redflag")) == chr.getId() && chr.getMap().getArea(1).contains(chr.getTruePosition())) {
                chr.getEventInstance().setProperty("redflag", "0");
                chr.getEventInstance().setProperty("blue", String.valueOf(Integer.parseInt(chr.getEventInstance().getProperty("blue")) + 1));
                chr.getEventInstance().broadcastPlayerMsg(-7, "Blue Team has scored a point!");
                chr.getMap().spawnAutoDrop(2910000, chr.getMap().getGuardians().get(0).left);
                chr.getEventInstance().broadcastPacket(CField.getCapturePosition(chr.getMap()));
                chr.getEventInstance().broadcastPacket(CField.resetCapture());
                chr.getEventInstance().schedule("updateScoreboard", 1000);
            } else if (Integer.parseInt(chr.getEventInstance().getProperty("blueflag")) == chr.getId() && chr.getMap().getArea(0).contains(chr.getTruePosition())) {
                chr.getEventInstance().setProperty("blueflag", "0");
                chr.getEventInstance().setProperty("red", String.valueOf(Integer.parseInt(chr.getEventInstance().getProperty("red")) + 1));
                chr.getEventInstance().broadcastPlayerMsg(-7, "Red Team has scored a point!");
                chr.getMap().spawnAutoDrop(2910001, chr.getMap().getGuardians().get(1).left);
                chr.getEventInstance().broadcastPacket(CField.getCapturePosition(chr.getMap()));
                chr.getEventInstance().broadcastPacket(CField.resetCapture());
                chr.getEventInstance().schedule("updateScoreboard", 1000);
            }
        }
        if (chr.getEventInstance() == null) { //if the PVP ends
            c.getSession().write(CWvsContext.enableActions());
            return;
        }

        if (killed || addedScore > 0) {
            chr.getEventInstance().addPVPScore(chr, addedScore);
            chr.getClient().getSession().write(CField.getPVPScore(ourScore + addedScore, killed));
        }
        if (didAttack) {
            chr.afterAttack(ourAttacks.size(), attackCount, skillid);
            PlayerHandler.AranCombo(c, chr, ourAttacks.size() * attackCount);
            if (skillid > 0 && (ourAttacks.size() > 0 || (skillid != 4331003 && skillid != 4341002)) && !GameConstants.isNoDelaySkill(skillid)) {
                boolean applyTo = effect.applyTo(chr, chr.getTruePosition());
            } else {
                c.getSession().write(CWvsContext.enableActions());
            }
        } else {
            move = false;
            pull = false;
            push = false;
            c.getSession().write(CWvsContext.enableActions());
        }
        chr.getMap().broadcastMessage(CField.pvpAttack(chr.getId(), chr.getLevel(), trueSkill, trueSkillLevel, speed, fakeMastery, visProjectile, attackCount, chargeTime, animation, facingLeft ? 1 : 0, chr.getStat().defRange, skillid, skillLevel, move, push, pull, ourAttacks));
        if (addedScore > 0 && GameConstants.getAttackDelay(skillid, SkillFactory.getSkill(skillid)) >= 100) {
            final CheatTracker tracker = chr.getCheatTracker();

            tracker.setAttacksWithoutHit(true);
            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }
    }

    public static boolean inArea(MapleCharacter chr) {
        for (Rectangle rect : chr.getMap().getAreas()) {
            if (rect.contains(chr.getTruePosition())) {
                return true;
            }
        }
        for (MapleMist mist : chr.getMap().getAllMistsThreadsafe()) {
            if (mist.getOwnerId() == chr.getId() && mist.isPoisonMist() == 2 && mist.getBox().contains(chr.getTruePosition())) {
                return true;
            }
        }
        return false;
    }

    public static void updateSpecialStat(final LittleEndianAccessor slea, final MapleClient c) {
        String stat = slea.readMapleAsciiString();
        int array = slea.readInt();
        int mode = slea.readInt();
        switch (stat) {
            case "honorLeveling":
                c.getSession().write(CWvsContext.updateSpecialStat(stat, array, mode, c.getPlayer().getHonourNextExp()));
                break;
            case "hyper":
                c.getSession().write(CWvsContext.updateSpecialStat(stat, array, mode, 0));
                break;
        }
    }

    public static void calcHyperSkillPointCount(MapleClient c) {
        for (int i = 0; i < 3; i++) {
            c.getSession().write(CWvsContext.updateHyperSp(i, c.getPlayer().getRemainingHSps()[i]));
        }
    }

    public static void CassandrasCollection(LittleEndianAccessor slea, MapleClient c) {
        c.getSession().write(CField.getCassandrasCollection());
    }

    public static void LuckyLuckyMonstory(LittleEndianAccessor slea, MapleClient c) {
        c.getSession().write(CField.getLuckyLuckyMonstory());
    }

    public static void LieDetector(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr, boolean isItem) {
        if ((chr == null) || (chr.getMap() == null)) {
            return;
        }
        String target = slea.readMapleAsciiString();
        byte slot = 0;
        if (isItem) {
            if (!chr.getCheatTracker().canLieDetector()) {
                chr.dropMessage(1, "您已经使用过一次，暂时还无法使用测谎仪道具.");
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
            slot = (byte) slea.readShort();
            int itemId = slea.readInt();
            Item toUse = chr.getInventory(MapleInventoryType.USE).getItem((short) slot);
            if ((toUse == null) || (toUse.getQuantity() <= 0) || (toUse.getItemId() != itemId) || (itemId != 2190000)) {
                c.getSession().write(CWvsContext.enableActions());
                return;
            }
        } else if (!chr.isIntern()) {
            c.getSession().close();
            return;
        }
        if (((FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) && (isItem)) || (chr.getMap().getReturnMapId() == chr.getMapId())) {
            chr.dropMessage(5, "当前地图无法使用测谎仪.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        MapleCharacter search_chr = chr.getMap().getCharacterByName(target);
        if ((search_chr == null) || (search_chr.getId() == chr.getId()) || ((search_chr.isIntern()) && (!chr.isIntern()))) {
            chr.dropMessage(1, "未找到角色.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if ((search_chr.getEventInstance() != null) || (search_chr.getMapId() == 180000001)) {
            chr.dropMessage(5, "当前地图无法使用测谎仪.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (search_chr.getAntiMacro().inProgress()) {
            c.getSession().write(CWvsContext.LieDetectorResponse((byte) 3));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (((search_chr.getAntiMacro().isPassed()) && (isItem)) || (search_chr.getAntiMacro().canDetector(System.currentTimeMillis()))) {
            c.getSession().write(CWvsContext.LieDetectorResponse((byte) 2));//已经使用过测谎仪
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (!search_chr.getAntiMacro().startLieDetector(chr.getName(), isItem, false)) {
            chr.dropMessage(5, "使用测谎仪失败.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (isItem) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (short) slot, (short) 1, false);
        }
        //search_chr.dropMessage(5, new StringBuilder().append(chr.getName()).append(" 对你使用了测谎仪。").toString());
    }

    public static void LieDetectorResponse(LittleEndianAccessor slea, MapleClient c) {
        if ((c.getPlayer() == null) || (c.getPlayer().getMap() == null)) {
            return;
        }
        String answer = slea.readMapleAsciiString();
        MapleLieDetector ld = c.getPlayer().getAntiMacro();
        if ((!ld.inProgress()) || ((ld.isPassed()) && (ld.getLastType() == 0)) || (ld.getAnswer() == null) || (answer.length() <= 0)) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (answer.equalsIgnoreCase(ld.getAnswer())) {
            MapleCharacter search_chr = c.getPlayer().getMap().getCharacterByName(ld.getTester());
            if ((search_chr != null) && (search_chr.getId() != c.getPlayer().getId())) {
                search_chr.dropMessage(5, new StringBuilder().append(c.getPlayer().getName()).append(" 通过了测谎仪的检测。").toString());
            }
            c.getSession().write(CWvsContext.LieDetectorResponse((byte) 9, (byte) 0));
            //c.getPlayer().gainMeso(5000, true);
            ld.end();
            World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(5, new StringBuilder().append("[GM 信息] 玩家: ").append(c.getPlayer().getName()).append(" (等级 ").append(c.getPlayer().getLevel()).append(") 通过了测谎仪检测。").toString()));
        } else if (ld.getAttempt() > -1) {
            ld.startLieDetector(ld.getTester(), ld.getLastType() == 0, true);
        } else {
            MapleCharacter search_chr = c.getPlayer().getMap().getCharacterByName(ld.getTester());
            if ((search_chr != null) && (search_chr.getId() != c.getPlayer().getId())) {
                search_chr.dropMessage(1, new StringBuilder().append(c.getPlayer().getName()).append(" 没用通过测谎仪的检测\r\n他可能使用非法程序\r\n恭喜你获得 7000 金币.").toString());
                search_chr.gainMeso(7000, true);
            }
            ld.end();
            c.getPlayer().getClient().getSession().write(CWvsContext.LieDetectorResponse((byte) 7, (byte) 0));
            c.getPlayer().getStat().setMp((short) 0, c.getPlayer());
            c.getPlayer().getStat().setHp((short) 0, c.getPlayer());
            c.getPlayer().updateSingleStat(MapleStat.HP, c.getPlayer().getStat().getHp());
            c.getPlayer().updateSingleStat(MapleStat.MP, c.getPlayer().getStat().getMp());
            //MapleMap map = c.getChannelServer().getMapFactory().getMap(GameConstants.JAIL);
            //c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME)).setCustomData(String.valueOf(System.currentTimeMillis()));
            //c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData(String.valueOf(1800));
            //c.getPlayer().changeMap(map, map.getPortal(0));
            World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(5, new StringBuilder().append("[GM 信息] 玩家: ").append(c.getPlayer().getName()).append(" (等级 ").append(c.getPlayer().getLevel()).append(") 未通过测谎仪检测，系统将其杀死！").toString()));

        }
    }
}
