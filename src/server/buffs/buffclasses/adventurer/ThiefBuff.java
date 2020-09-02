/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.adventurer;

import client.MapleBuffStat;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Itzik
 */
public class ThiefBuff extends AbstractBuffClass {

    public ThiefBuff() {
        buffs = new int[]{
            4001003, //Dark Sight
            4001005, //Haste
            4101003, //Claw Booster
            4201002, //Dagger Booster
            4201009, //Channel Karma
            4201011, //Meso Guard
            4111002, //Shadow Partner
            4111009, //Shadow Star
            4211003, //Pick Pocket
            4211008, //Shadow Partner
            4121000, //Maple Warrior     
            4121014, //Dark Harmony
            4221000, //Maple Warrior     
            4221013, //Shadow Instinct
            4301003, // Self haste
            4311005, // Channel Karma
            4311009, // Katara Booster
            4331002, // Mirror Image
            4341000, // Maple Warrior
            4341007, // Thorns
            4341006, // Mirrored Target
            4341054, // Blade Clone
            4341053, // Epic Adventure
            4121053, //Epic Adventure
            4121054, //Bleed Dart
            4221053, //Epic Adventure
            4221054, //Flip of the Coin
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isAdventurer(job) && job / 100 == 4;
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 4001005: //Haste
                break;
            case 4301003: // Self haste
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.jump));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.speed));
                break;
            case 4001003: //Dark Sight
                eff.statups.put(MapleBuffStat.DARKSIGHT, eff.info.get(MapleStatInfo.x));
                break;
            case 4101003: //Claw Booster
            case 4201002: //Dagger Booster
            case 4311009: // Katara Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 4201011: //Meso Guard
                eff.statups.put(MapleBuffStat.MESOGUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 4201009: //Channel Karma
            case 4311005: // Channel Karma
                eff.statups.put(MapleBuffStat.WATK, eff.info.get(MapleStatInfo.pad));
                break;
            case 4211003: //Pick Pocket
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.PICKPOCKET, eff.info.get(MapleStatInfo.x));
                break;
            case 4111002: //Shadow Partner
            case 4211008: //Shadow Partner
            case 4331002: // Mirror Image
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            case 4111009: //Shadow Star
                eff.statups.put(MapleBuffStat.SPIRIT_CLAW, 0);
                break;
            case 4121014: //Dark Harmony
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));//test - works without
                break;
            case 4341007: // Thorns
                eff.statups.put(MapleBuffStat.STANCE, (int) eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.ENHANCED_WATK, (int) eff.info.get(MapleStatInfo.epad));
                break;
            case 4221013: //Shadow Instinct
                break;
            case 4121000: //Maple Warrior 
            case 4221000: //Maple Warrior 
            case 4341000: // MW
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 4121054: //Bleed Dart
                break;
            case 4341054: // Blade Clone
                break;
            case 4121053: //Epic Adventure
            case 4221053: //Epic Adventure
            case 4341053: //Epic Adventure
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                // System.out.println("Thief skill not coded: " + skill);
                break;
        }
    }
}
