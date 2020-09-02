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
 * @author Sunny
 */
public class DemonBuff extends AbstractBuffClass {

    public DemonBuff() {
        buffs = new int[]{
            31001001, // booster
            31101003, // Vengeance
            31111004, // Black-Hearted Strength
            31121007, // Boundless Rage
            31121004, // MW
            31121002, // Leech Aura
            31011001, // Overload Release
            31201002, // Battle Pact
            31201003, // Abyssal Connection
            31211003, // Ward Evil
            31211004, // Diabolic Recovery
            31221004, // Overwhelming Power
            31221008, // MW
            31121054, // Blue Blood
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isDemonSlayer(job) || GameConstants.isDemonAvenger(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 31001001:
            case 31201002: // booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 31011001: // Overload Release
                eff.statups.put(MapleBuffStat.HP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
            case 31101003: // Vengeance
                eff.statups.put(MapleBuffStat.POWERGUARD, eff.info.get(MapleStatInfo.y));
                break;
            case 31201003: // Abyssal Connection 
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 31211003: // Ward Evil
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                break;
            case 31211004: // Diabolic Recovery
                eff.statups.put(MapleBuffStat.DIABOLIC_RECOVERY, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.HP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
            case 31221004: // Overwhelming Power
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ATTACK_SPEED, eff.info.get(MapleStatInfo.indieBooster));
                break;
            case 31111004: // Black-Hearted Strength
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.DEFENCE_BOOST_R, eff.info.get(MapleStatInfo.x));
                break;
            case 31121007: // Boundless Rage
                eff.statups.put(MapleBuffStat.BOUNDLESS_RAGE, 1);
                break;
            case 31121004: // MW
            case 31221008: // MW
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 31121002: //Leech Aura
                eff.statups.put(MapleBuffStat.LEECH_AURA, eff.info.get(MapleStatInfo.x));
                break;
            case 31121054:
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            default:
                //     System.out.println("Unhandled Demon Buff: " + skill);
                break;
        }
    }
}
