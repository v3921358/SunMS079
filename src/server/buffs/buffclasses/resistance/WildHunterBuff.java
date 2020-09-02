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
public class WildHunterBuff extends AbstractBuffClass {

    public WildHunterBuff() {
        buffs = new int[]{
            33001001, // Jaguar Rider
            33101003, // Soul Arrow: Crossbow
            33101005, // Call of the Wild
            33101012, // Crossbow Booster
            33111007, // Feline Berserk
            33111009, // Concentrate
            33121004, // Sharp Eyes
            33121007, // Maple Warrior
            33121013, // Extended Magazine
            33121054, // Silent Rampage
            33121053, // For Liberty
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isWildHunter(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 33101003:
                eff.statups.put(MapleBuffStat.SOULARROW, eff.info.get(MapleStatInfo.x));
                break;
            case 33101005:// Call of the Wild
                eff.statups.put(MapleBuffStat.MP_BUFF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.TORNADO, eff.info.get(MapleStatInfo.z));
//                eff.statups.put(MapleBuffStat.SATELLITESAFE_ABSORB, eff.info.get(MapleStatInfo.lt));
//                eff.statups.put(MapleBuffStat.SOULARROW, eff.info.get(MapleStatInfo.rb));                
                break;
            case 33101012:// Crossbow Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 33111007: // Feline Berserk
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.ATTACK_BUFF, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.FELINE_BERSERK, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ATTACK_SPEED, eff.info.get(MapleStatInfo.indieBooster));
                break;
            case 33111009: // Concentrate
                eff.statups.put(MapleBuffStat.ENHANCED_WATK, eff.info.get(MapleStatInfo.epad));
                eff.statups.put(MapleBuffStat.CONCENTRATE, eff.info.get(MapleStatInfo.x));
                break;
            case 33121004: // Sharp Eyes
                eff.statups.put(MapleBuffStat.SHARP_EYES, (eff.info.get(MapleStatInfo.x) << 8) + eff.info.get(MapleStatInfo.criticaldamageMax));
                break;
            case 33121007: // Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 33121013: // Extended Magazine
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ANGEL_STAT, eff.info.get(MapleStatInfo.indieAllStat));
                break;
            case 33121054:// Silent Rampage
                break;
            case 33121053:// For Liberty
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                // System.out.println("Unhandled WildHunter Buff: " + skill);
                break;
        }
    }
}
