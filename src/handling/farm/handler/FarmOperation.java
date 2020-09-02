/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.farm.handler;

import client.MapleCharacter;
import client.MapleClient;
import constants.WorldConstants.WorldOption;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import server.farm.MapleFarm;
import tools.FileoutputUtil;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.FarmPacket;

/**
 *
 * @author Itzik
 */
public class FarmOperation {

    public static void EnterFarm(final CharacterTransfer transfer, final MapleClient c) {
        if (transfer == null) {
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }

        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        FarmServer.getPlayerStorage().registerPlayer(chr);
        c.getSession().write(FarmPacket.updateMonster(new LinkedList()));
        c.getSession().write(FarmPacket.enterFarm(c));
        c.getSession().write(FarmPacket.farmQuestData(new LinkedList(), new LinkedList()));
        c.getSession().write(FarmPacket.updateMonsterInfo(new LinkedList()));
        c.getSession().write(FarmPacket.updateAesthetic(c.getFarm().getAestheticPoints()));
        c.getSession().write(FarmPacket.spawnFarmMonster1());
        c.getSession().write(FarmPacket.farmPacket1());
        c.getSession().write(FarmPacket.updateFarmFriends(new LinkedList()));
        c.getSession().write(FarmPacket.updateFarmInfo(c));
        c.getSession().write(FarmPacket.updateQuestInfo(21002, (byte) 1, ""));
        SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        sdfGMT.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        String timeStr = sdfGMT.format(Calendar.getInstance().getTime()).replaceAll("-", "");
        c.getSession().write(FarmPacket.updateQuestInfo(21001, (byte) 1, timeStr));
        c.getSession().write(FarmPacket.updateQuestInfo(21003, (byte) 1, "30"));
        c.getSession().write(FarmPacket.updateUserFarmInfo(chr, false));
        List<Pair<MapleFarm, Integer>> ranking = new LinkedList();
        ranking.add(new Pair<>(MapleFarm.getDefault(1, c, "Pyrous"), 999999));
        ranking.add(new Pair<>(MapleFarm.getDefault(1, c, "Sango"), 1));
        ranking.add(new Pair<>(MapleFarm.getDefault(1, c, "Hemmi"), -1));
        c.getSession().write(FarmPacket.sendFarmRanking(chr, ranking));
        c.getSession().write(FarmPacket.updateAvatar(new Pair<>(WorldOption.Scania, chr), null, false));
        if (c.getFarm().getName().equals("Creating...")) { //todo put it on farm update handler
            c.getSession().write(FarmPacket.updateQuestInfo(1111, (byte) 0, "A1/"));
            c.getSession().write(FarmPacket.updateQuestInfo(2001, (byte) 0, "A1/"));
        }
    }

    public static void LeaveFarm(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        FarmServer.getPlayerStorage().deregisterPlayer(chr);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        try {
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            c.getSession().write(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } finally {
            final String s = c.getSessionIPAddress();
            LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
            chr.saveToDB(false, true);
            c.setPlayer(null);
            c.setReceiving(false);
            c.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
    }
}
