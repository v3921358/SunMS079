/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.resistance;

import client.MapleBuffStat;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class XenonBuff extends AbstractBuffClass {

    public XenonBuff() {
        buffs = new int[]{
            36001002, //Circuit Surge
            36101002, //Perspective Shift
            36101003, //Efficiency Streamline
            36101004, //Xenon Booster
            36111006, //Manifest Projector
            36111008, // Emergency Resupply
            36111003, // Hybird Defenses
            36121003, // OOPArts Code
            36121004, // Offensive Matrix
            36121008, // Maple Warrior
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isXenon(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 36001002: // Circuit Surge
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 36101002: // Perspective Shift - dc
                //eff.info.put(MapleStatInfo.powerCon, Integer.valueOf(6));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
                break;
            case 36101003: // Efficiency Streamline
                eff.statups.put(MapleBuffStat.MP_R, eff.info.get(MapleStatInfo.indieMmpR));
                eff.statups.put(MapleBuffStat.HP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
            case 36101004: // Xenon Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 36111006: // Manifest Projector
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            case 36111008: // Emergency Resupply
                eff.statups.put(MapleBuffStat.SUPPLY_SURPLUS, eff.info.get(MapleStatInfo.x));
                break;
            case 36111003: // Hybrid Defenses
                eff.statups.put(MapleBuffStat.HYBRID_DEFENSES, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.z));
                break;
            case 36121003: // OOPArts Code DAMAGE_PERCENT
                eff.statups.put(MapleBuffStat.BOSS_DAMAGE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 36121004: // Offensive Matrix
                //TODO
                break;
            case 36121008: // Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            default:
                // System.out.println("Unhandled Xenon Buff: " + skill);
                break;
        }
    }
}
