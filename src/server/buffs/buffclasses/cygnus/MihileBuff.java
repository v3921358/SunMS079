/*
 * This file was designed for Luminous.
 * Do not redistribute without explicit permission from the
 * developer(s).
 */
package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class MihileBuff extends AbstractBuffClass {

    public MihileBuff() {
        buffs = new int[]{
            51101003, // Sword Booster
            51101004, // Rage
            51111003, // Radiant Charge
            51111004, // Enduring Spirit
            51121004, // Stance
            51121005, // Maple Warrior
            51121006, // Roiling Soul
            51121053, // Queen of Tomorrow
            51121054, // Sacred Cube
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isMihile(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        // If this initial check and the corresponding arrays are removed, 
        // there should not be any impact (i.e., it will keep its functionality). 
        if (!containsSkill(skill)) {
            return;
        }

        switch (skill) {
            case 51101003: // booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 51101004: // rage, not special
                break;
            case 51111003: // rad charge
                eff.statups.put(MapleBuffStat.WK_CHARGE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.z));
                break;
            case 51111004: // end spirit
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.DEFENCE_BOOST_R, eff.info.get(MapleStatInfo.x));
                break;
            case 51121004: // stance
                eff.statups.put(MapleBuffStat.STANCE, (int) eff.info.get(MapleStatInfo.prop));
                break;
            case 51121005: // mw
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 51121006: // roiling soul
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, eff.info.get(MapleStatInfo.z));
                break;
            default:
                //System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
