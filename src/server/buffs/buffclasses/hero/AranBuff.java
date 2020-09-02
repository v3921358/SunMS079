/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.hero;

import client.MapleBuffStat;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Charmander
 */
public class AranBuff extends AbstractBuffClass {

    public AranBuff() {
        buffs = new int[]{
            21001003, // Polearm Booster
            21101006, // Snow Charge
            21101005, // Combo Drain
            21111001, // Might
            21111009, // Combo Recharge
            21111012, // Maha Blessing
            21121007, // Combo Barrier
            21121000, // Maple Warrior
            21121054, // Unlimited Combo
            21121053, // Heroic Memories
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isAran(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 21001003: // Polearm Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 21101006: // Snow Charge
                eff.statups.put(MapleBuffStat.WK_CHARGE, eff.info.get(MapleStatInfo.x));
                break;
            case 21101005: // Combo Drain
                eff.statups.put(MapleBuffStat.COMBO_DRAIN, eff.info.get(MapleStatInfo.x));
                break;
            case 21111001: // Might
                eff.statups.put(MapleBuffStat.SMART_KNOCKBACK, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ENHANCED_WATK, eff.info.get(MapleStatInfo.epad));
                eff.statups.put(MapleBuffStat.ENHANCED_WDEF, eff.info.get(MapleStatInfo.epdd));
                eff.statups.put(MapleBuffStat.ENHANCED_MDEF, eff.info.get(MapleStatInfo.emdd));
                break;
            case 21111009: // Combo Recharge
                eff.statups.put(MapleBuffStat.ARAN_COMBO, eff.info.get(MapleStatInfo.x));
                break;
            case 21111012: // Maha Blessing
                eff.statups.put(MapleBuffStat.MATK, eff.info.get(MapleStatInfo.mad));
                eff.statups.put(MapleBuffStat.WATK, eff.info.get(MapleStatInfo.pad));
                break;
            case 21121007: // Combo Barrier
                eff.statups.put(MapleBuffStat.COMBO_BARRIER, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MDEF, eff.info.get(MapleStatInfo.mdd));
                eff.statups.put(MapleBuffStat.WDEF, eff.info.get(MapleStatInfo.pdd));
                break;
            case 21121000: // Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 21121054: // Unlimited Combo
                eff.statups.put(MapleBuffStat.DEFAULT_BUFFSTAT, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 21121053: // Heroic Memories
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                break;
            default:
                //System.out.println("Aran skill not coded: " + skill);
                break;
        }
    }
}
