/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import client.MapleBuffStat;
import server.MapleStatInfo;
import tools.Pair;

/**
 *
 * @author Itzik
 */
public enum CustomSkills {

    A(1, 1.0),
    B(2, new Pair<>(MapleBuffStat.DARKSIGHT, 1)),
    C(3, new Pair<>(MapleBuffStat.DARKSIGHT, MapleStatInfo.x), true);
    int id;
    double damageModifier;
    boolean buff, statinfobuff;
    Pair<MapleBuffStat, Integer> stat;
    Pair<MapleBuffStat, MapleStatInfo> statinfo;

    CustomSkills(int id, double damageModifier) {
        this.id = id;
        this.damageModifier = damageModifier;
        this.buff = false;
        this.statinfobuff = false;
    }

    CustomSkills(int id, Pair<MapleBuffStat, Integer> stat) {
        this.id = id;
        this.stat = stat;
        this.buff = true;
        this.statinfobuff = false;
    }

    CustomSkills(int id, Pair<MapleBuffStat, MapleStatInfo> statinfo, boolean statinfobuff) {
        this.id = id;
        this.statinfo = statinfo;
        this.buff = true;
        this.statinfobuff = true;
    }

    public int getId() {
        return id;
    }

    public double getDamageModifier() {
        return damageModifier;
    }

    public Pair<MapleBuffStat, Integer> getStat() {
        return stat;
    }

    public Pair<MapleBuffStat, MapleStatInfo> getStatInfo() {
        return statinfo;
    }

    public boolean isBuff() {
        return buff;
    }

    public boolean isStatInfoBuff() {
        return statinfobuff;
    }
}
