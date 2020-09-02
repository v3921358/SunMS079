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
 * @author Maple
 */
public class BowmanBuff extends AbstractBuffClass {

    public BowmanBuff() {
        buffs = new int[]{
            3101002, //Bow Booster
            3101004, //SoulArrow
            3201002, //Bow Booster
            3201004, //Soul Arrow xBow
            3121000, //MapleWarrior
            3211011, //PainKiller
            3121002, //SharpEyes
            3121007, //Illusion Step
            3221000, //Maple Warrior
            3221002, //Sharp Eye
            3221006, //Illusion Step
            3121053, //Epic Adventure
            3221053, //Epic Adventure
            3121054, //Constentration
            3221053, //Epic Adventure
            3221054, //Bullseye Shot
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isAdventurer(job) && job / 100 == 3;
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 3101002: //Bow Booster
            case 3201002: //Bow Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 3101004: //SoulArrow bow
            case 3201004: //SoulArrow xbow
                //eff.statups.put(MapleBuffStat.CONCENTRATE, eff.info.get(MapleStatInfo.epad));
                eff.statups.put(MapleBuffStat.SOULARROW, eff.info.get(MapleStatInfo.x));
                break;
            case 3211011: //PainKiller
                eff.statups.put(MapleBuffStat.PRESSURE_VOID, eff.info.get(MapleStatInfo.asrR));
                eff.statups.put(MapleBuffStat.PRESSURE_VOID, eff.info.get(MapleStatInfo.terR));
                break;
            case 3121000: //Maple Warrior
            case 3221000: //Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 3121002: //Sharp Eyes
            case 3221002: //Sharp Eye
                eff.statups.put(MapleBuffStat.SHARP_EYES, (eff.info.get(MapleStatInfo.x) << 8) + eff.info.get(MapleStatInfo.criticaldamageMax));
                break;
            case 3121007: //Illusion Step
            case 3221006: //Illusion Step
                eff.statups.put(MapleBuffStat.DEX, eff.info.get(MapleStatInfo.dex));
                //add more
                break;
            case 3121053: //Epic Adventure
            case 3221053: //Epic Adventure
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            case 3121054: //Consentration
                eff.statups.put(MapleBuffStat.BOWMASTERHYPER, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 3221054: //BullsEye Shot
                break;
            default:
                //System.out.println("Bowman skill not coded: " + skill);
                break;
        }
    }
}
