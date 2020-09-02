package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.WorldConstants;
import handling.cashshop.CashShopServer;
import handling.cashshop.handler.CashShopOperation;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.*;
import handling.world.exped.MapleExpedition;
import handling.world.guild.*;
import java.util.ArrayList;
import java.util.List;
import scripting.NPCScriptManager;
import server.*;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.BuddylistPacket;
import tools.packet.CWvsContext.FamilyPacket;
import tools.packet.CWvsContext.GuildPacket;
import tools.packet.FarmPacket;

public class InterServerHandler {

    public static void EnterCS(final MapleClient c, final MapleCharacter chr) {
        if (chr.hasBlockedInventory() || chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
            c.getSession().write(CField.serverBlocked(2));
            CharacterTransfer farmtransfer = FarmServer.getPlayerStorage().getPendingCharacter(chr.getId());
            if (farmtransfer != null) {
                c.getSession().write(FarmPacket.farmMessage("在访问你的农场时，你不能进入现金商店，但."));
            }
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize() >= 10) {
            chr.dropMessage(1, "服务器正忙着。请在一分钟或更少的时间再试.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        LoginServer.forceRemoveAccounts(c, false);
        ChannelServer ch = ChannelServer.getInstance(c.getChannel());
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
        c.updateLoginState(3, c.getSessionIPAddress());
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.getSession().write(CField.getChannelChange(c, Integer.parseInt(CashShopServer.getIP().split(":")[1])));
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static void EnterFarm(final MapleClient c, final MapleCharacter chr) {
        if (chr.hasBlockedInventory() || chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
            c.getSession().write(CField.serverBlocked(2));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize() >= 10) {
            chr.dropMessage(1, "服务器正忙着。请在一分钟或更少的时间再试.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        ChannelServer ch = ChannelServer.getInstance(c.getChannel());
        chr.changeRemoval();
        if (chr.getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
            World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -30);
        ch.removePlayer(chr);
        c.updateLoginState(3, c.getSessionIPAddress());
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.getSession().write(CField.getChannelChange(c, Integer.parseInt(FarmServer.getIP().split(":")[1])));
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static final void EnterMTS(final MapleClient c, final MapleCharacter chr) {
        //if (chr.hasBlockedInventory() || chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
        //   c.getSession().write(CField.serverBlocked(2));
        //   c.getSession().write(CWvsContext.enableActions());
        //    return;
        //}
        //chr.dropMessage(1, "目前拍卖尚未开放。");
        NPCScriptManager.getInstance().start(c, 9900004);
        c.getSession().write(CWvsContext.enableActions());
    }

    public static void Loggedin(final int playerid, final MapleClient c) {
        //try {
        MapleCharacter player;
        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        if (transfer != null) {
//            c.getSession().write(CWvsContext.BuffPacket.cancelBuff());
            //System.out.println("Enter CS");
            CashShopOperation.EnterCS(transfer, c);
            return;
        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            transfer = cserv.getPlayerStorage().getPendingCharacter(playerid);
            if (transfer != null) {
                //System.out.println("Set channel to " + cserv.getChannel());
                c.setChannel(cserv.getChannel());
                break;
            }
        }
        if (transfer == null) { // Player isn't in storage, probably isn't CC
            Triple<String, String, Integer> ip = LoginServer.getLoginAuth(playerid);
            String s = c.getSessionIPAddress();
            if (ip == null || !s.substring(s.indexOf('/') + 1, s.length()).equals(ip.left)) {
                if (ip != null) {
                    LoginServer.putLoginAuth(playerid, ip.left, ip.mid, ip.right);
                }
                //System.out.println("Session close");
//                c.getSession().close();
//                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
//                return;
            }
            c.setTempIP(ip.mid);
            c.setChannel(ip.right);
            /*List<String> charNames = c.loadCharacterNamesByCharId(playerid);
             for (ChannelServer cs : ChannelServer.getAllInstances()) {
             for (final String name : charNames) {
             if (cs.getPlayerStorage().getCharacterByName(name) != null) {
             c.getSession().close();
             return;
             }
             }
             }
             for (final String name : charNames) {
             if (CashShopServer.getPlayerStorage().getCharacterByName(name) != null) {
             c.getSession().close();
             return;
             }
             }*/
            LoginServer.removeAccounts(c);
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
            //System.out.println("从数据库载入角色");
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
            //System.out.println("重新连接角色");
        }
        final ChannelServer channelServer = c.getChannelServer();
        c.setPlayer(player);
        //System.out.println("设定玩家: " + player);
        c.setAccID(player.getAccountID());
        //System.out.println("设定账号编号 : " + player.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
//            c.getSession().close();
//            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            //System.out.println("连接 Hack");
//            return;
        }
        //對在線上角色做斷線
        LoginServer.forceRemoveAccounts(c, false);
        ChannelServer.forceRemovePlayerByAccId(c, player.getAccountID());

        final int state = c.getLoginState();
        //System.out.println("账号登入状态 = " + c.getLoginState());
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL || state == MapleClient.LOGIN_NOTLOGGEDIN) {
            allowLogin = !World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()));
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            //System.out.println("同意登入 = false");
            return;
        }

        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        //System.out.println("增加玩家");
        channelServer.addPlayer(player);

        player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()), true);
        player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
        player.giveSilentDebuff(PlayerBuffStorage.getDiseaseFromStorage(player.getId()));
        System.out.println("[进入游戏] " + c.getSessionIPAddress() +" 角色名： " + player.getName());
        c.getSession().write(CField.getCharInfo(player));
        c.getSession().write(CWvsContext.updateMount(player, false));
        c.getSession().write(CWvsContext.temporaryStats_Reset());
        if (player.isGM()) {
            SkillFactory.getSkill(9001004).getEffect(1).applyTo(player);
        }
        c.getSession().write(CWvsContext.updateSkills(c.getPlayer().getSkills(), false));//skill to 0 "fix"
        c.getSession().write(CWvsContext.showCharCash(player));
        c.getSession().write(CSPacket.enableCSUse());

        player.getMap().addPlayer(player);
        try {
            // Start of buddylist
            final int buddyIds[] = player.getBuddylist().getBuddyIds();
            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds);
            if (player.getParty() != null) {
                final MapleParty party = player.getParty();
                World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));

                if (party != null && party.getExpeditionId() > 0) {
                    final MapleExpedition me = World.Party.getExped(party.getExpeditionId());
                    if (me != null) {
                        c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(me, false, true));
                    }
                }
            }
            final CharacterIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                player.getBuddylist().get(onlineBuddy.getCharacterId()).setChannel(onlineBuddy.getChannel());
            }
            c.getSession().write(BuddylistPacket.updateBuddylist(player.getBuddylist().getBuddies()));

            // Start of Messenger
            final MapleMessenger messenger = player.getMessenger();
            if (messenger != null) {
                World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
            }

            // Start of Guild and alliance
            if (player.getGuildId() > 0) {
                World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.getSession().write(GuildPacket.showGuildInfo(player));
                final MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (byte[] pack : packetList) {
                            if (pack != null) {
                                c.getSession().write(pack);
                            }
                        }
                    }
                } else { //guild not found, change guild id
                    player.setGuildId(0);
                    player.setGuildRank((byte) 5);
                    player.setAllianceRank((byte) 5);
                    player.saveGuildStatus();
                }
            }
            if (player.getFamilyId() > 0) {
                World.Family.setFamilyMemberOnline(player.getMFC(), true, c.getChannel());
            }
            c.getSession().write(FamilyPacket.getFamilyData());
            c.getSession().write(FamilyPacket.getFamilyInfo(player));
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Login_Error, e);
        }
        player.getClient().getSession().write(CWvsContext.broadcastMsg(channelServer.getServerMessage()));
        player.sendMacros();
        player.showNote();
        player.sendImp();
        player.updatePartyMemberHP();
        player.startFairySchedule(false);
        player.baseSkills(); //fix people who've lost skills.
        player.updatePetAuto();
        c.getSession().write(CField.getKeymap(player.getKeyLayout()));
        player.expirationTask(true, transfer == null);
        //c.getSession().write(CWvsContext.updateMaplePoint(player.getCSPoints(2)));
        if (player.getJob() == 132) { // DARKKNIGHT
            player.checkBerserk();
        }
        player.spawnClones();
        player.spawnSavedPets();
        if (player.getStat().equippedSummon > 0) {
            SkillFactory.getSkill(player.getStat().equippedSummon + (GameConstants.getBeginnerJob(player.getJob()) * 1000)).getEffect(1).applyTo(player);
        }
        MapleInventory equipped = player.getInventory(MapleInventoryType.EQUIPPED);
        List<Short> slots = new ArrayList<>();
        for (Item item : equipped.newList()) {
            slots.add(item.getPosition());
        }
        if (!player.isGM()) {
            for (short slot : slots) {
                if (GameConstants.isIllegalItem(equipped.getItem(slot).getItemId())) {
                    MapleInventoryManipulator.removeFromSlot(player.getClient(), MapleInventoryType.EQUIPPED, slot, (short) 1, false);
                    return;
                }
            }
        }
        //player.updateReward();
        player.getClient().getSession().write(CWvsContext.broadcastMsg(channelServer.getServerMessage()));
        //Thread.sleep(1000);
        //c.getSession().write(CWvsContext.getTopMsg("Earned Forever Single title!"));
        //Thread.sleep(3100);
        //if (c.getPlayer().getLevel() < 11) { 
        //NPCScriptManager.getInstance().start(c, 9010000, "LoginTot");
        //}
        //} catch (InterruptedException e) {
        //}
    }

    public static final void ChangeChannel(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr, final boolean room) {
        if (chr == null || chr.hasBlockedInventory() || chr.getEventInstance() != null || chr.getMap() == null || chr.isInBlockedMap() || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize() >= 10) {
            chr.dropMessage(1, "服务器正忙着。请在不到一分钟的时间再试一次.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int chc = slea.readByte() + 1;
        int mapid = 0;
        if (room) {
            mapid = slea.readInt();
        }
        chr.updateTick(slea.readInt());
        if (!World.isChannelAvailable(chc, chr.getWorld())) {
            chr.dropMessage(1, "请求被拒绝由于未知的错误.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (room && (mapid < 910000001 || mapid > 910000022)) {
            chr.dropMessage(1, "请求被拒绝由于未知的错误.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (room) {
            if (chr.getMapId() == mapid) {
                if (c.getChannel() == chc) {
                    chr.dropMessage(1, "你已经在 " + chr.getMap().getMapName());
                    c.getSession().write(CWvsContext.enableActions());
                } else { // diff channel
                    chr.changeChannel(chc);
                }
            } else { // diff map
                if (c.getChannel() != chc) {
                    chr.changeChannel(chc);
                }
                final MapleMap warpz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
                if (warpz != null) {
                    chr.changeMap(warpz, warpz.getPortal("out00"));
                } else {
                    chr.dropMessage(1, "请求被拒绝由于未知的错误.");
                    c.getSession().write(CWvsContext.enableActions());
                }
            }
        } else {
            chr.changeChannel(chc);
        }
    }
}
