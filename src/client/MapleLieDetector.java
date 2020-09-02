/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import constants.GameConstants;
import handling.world.World;
import scripting.LieDetectorScript;
import server.Timer.EtcTimer;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import tools.HexTool;
import tools.Pair;
import tools.packet.CWvsContext;

/**
 *
 * @author wubin
 */
public class MapleLieDetector {

    public MapleCharacter chr;
    public byte type;
    public int attempt;
    public String tester;
    public String answer;
    public boolean inProgress;
    public boolean passed;
    public long lasttime;

    public MapleLieDetector(MapleCharacter c) {
        this.chr = c;
        reset();
    }

    public final boolean startLieDetector(final String tester, final boolean isItem, final boolean anotherAttempt) {
        if ((!anotherAttempt) && (((isPassed()) && (isItem)) || (inProgress()) || (this.attempt == -1))) {
            return false;
        }
        Pair captcha = LieDetectorScript.getImageBytes();
        if (captcha == null) {
            return false;
        }
        byte[] image = HexTool.getByteArrayFromHexString((String) captcha.getLeft());
        this.answer = ((String) captcha.getRight());
        this.tester = tester;
        this.inProgress = true;
        this.type = (byte) (isItem ? 0 : 1);
        attempt -= 1;
        this.chr.getClient().getSession().write(CWvsContext.sendLieDetector(image, attempt + 1));
        EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if ((!MapleLieDetector.this.isPassed()) && (MapleLieDetector.this.chr != null)) {
                    if (MapleLieDetector.this.attempt == -1) {
                        MapleCharacter search_chr = MapleLieDetector.this.chr.getMap().getCharacterByName(tester);
                        if ((search_chr != null) && (search_chr.getId() != MapleLieDetector.this.chr.getId())) {
                            search_chr.dropMessage(5, MapleLieDetector.this.chr.getName() + " 没有通过测谎仪的检测，恭喜你获得7000的金币.");
                            search_chr.gainMeso(7000, true);
                        }
                        MapleLieDetector.this.end();
                        MapleLieDetector.this.chr.getClient().getSession().write(CWvsContext.LieDetectorResponse((byte) 7, (byte) 0));
                        //MapleMap map = MapleLieDetector.this.chr.getClient().getChannelServer().getMapFactory().getMap(GameConstants.JAIL);
                        //MapleLieDetector.this.chr.getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME)).setCustomData(String.valueOf(System.currentTimeMillis()));
                        //MapleLieDetector.this.chr.getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData(String.valueOf(1800));
                        //MapleLieDetector.this.chr.changeMap(map, map.getPortal(0));
                        MapleLieDetector.this.chr.getStat().setMp((short) 0, MapleLieDetector.this.chr);
                        MapleLieDetector.this.chr.getStat().setHp((short) 0, MapleLieDetector.this.chr);
                        MapleLieDetector.this.chr.updateSingleStat(MapleStat.HP, MapleLieDetector.this.chr.getStat().getHp());
                        MapleLieDetector.this.chr.updateSingleStat(MapleStat.MP, MapleLieDetector.this.chr.getStat().getMp());
                        World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 信息] 玩家: " + MapleLieDetector.this.chr.getName() + " (等级 " + MapleLieDetector.this.chr.getLevel() + ") 未通过测谎仪检测，系统将其杀死！"));
                    } else {
                        MapleLieDetector.this.startLieDetector(tester, isItem, true);
                    }
                }
            }
        }, 60000L);

        return true;
    }

    public final int getAttempt() {
        return this.attempt;
    }

    public final byte getLastType() {
        return this.type;
    }

    public final String getTester() {
        return this.tester;
    }

    public final String getAnswer() {
        return this.answer;
    }

    public final boolean inProgress() {
        return this.inProgress;
    }

    public final boolean isPassed() {
        return this.passed;
    }

    public final boolean canDetector(long time) {
        return this.lasttime + 600000 > time;
    }

    public final void end() {
        this.inProgress = false;
        this.passed = true;
        this.attempt = 1;
        this.lasttime = System.currentTimeMillis();
    }

    public final void reset() {
        this.tester = "";
        this.answer = "";
        this.attempt = 1;
        this.inProgress = false;
        this.passed = false;
    }
}
