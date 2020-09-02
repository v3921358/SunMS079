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
public class PirateBuff extends AbstractBuffClass {

    public PirateBuff() {
        buffs = new int[]{
            5101011, //Dark Clarity
            5701006, //Dark Clarity
            5101006, //Knuckle Booster
            5111007, //Roll Of The Dice
            5211007, //Roll Of The Dice
            5111010, //Admiral's Wings
            5121015, //Crossbones
            5121010, //Time Leap
            5121009, //Speed Infusion
            5121000, //Maple Warrior
            5221000, //Maple Warrior
            5321005, //Maple Warrior
            5721000, //Maple Warrior
            5121054, //Stimulating Conversation
            5121053, //Epic Adventure
            5221053, //Epic Adventure
            5321053, //Epic Adventure
            5721053, //Epic Adventure
            5201012, //Scurvy Summons
            5201003, //Gun Booster
            5201008, //Infinity Blast
            5211009, //Cross Cut Blast
            5221018, //Jolly Roger
            5221021, //Quickdraw
            5221054, //Whaler's Potion
            5301002, //Cannon Booster
            5301003, //Monkey Magic
            5311004, //Barrel Roulette
            5311005, //Luck of the Die
            5321010, //Pirate's Spirit
            5321054, //Buckshot
            5701005, //Gun Booster
            5711011, //Roll of the Dice
            5721009, //Relentless
            5721054, //Bionic Maximizer
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isAdventurer(job) && job / 100 == 5;
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 5101011: //Dark Clarity
            case 5701006: //Dark Clarity
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 5101006: //Knuckle Booster
            case 5201003: //Gun Booster
            case 5301002: //Cannon Booster
            case 5701005: //Gun Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 5111007: //Roll Of The Dice
            case 5211007: //Roll Of The Dice
            case 5311005: //Luck of the Die
            case 5711011: //Roll of the Dice
                eff.statups.put(MapleBuffStat.DICE_ROLL, 0);
                break;
            case 5111010: //Admiral's Wings
                //TODO
                break;
            case 5121015: //Crossbones
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.x));
                break;
            case 5121009: //Speed Infusion
                eff.statups.put(MapleBuffStat.SPEED_INFUSION, eff.info.get(MapleStatInfo.x));
                break;
            case 5121054: //Stimulating Conversation
                //TODO
                break;
            case 5221018: //Jolly Roger
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));//or x?
                eff.statups.put(MapleBuffStat.DAMAGE_RATE, eff.info.get(MapleStatInfo.damR));
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.AVOID, eff.info.get(MapleStatInfo.eva));
                break;
            case 5201008: //Infinity Blast
                eff.statups.put(MapleBuffStat.SPIRIT_CLAW, eff.info.get(MapleStatInfo.x));
                break;
            case 5211009: //Cross Cut Blast
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 5221054: //Whaler's Potion
                //TODO
                break;
            case 5301003: //Monkey Magic
                eff.statups.put(MapleBuffStat.HP_BOOST, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.MP_BOOST, eff.info.get(MapleStatInfo.indieMmp));
                eff.statups.put(MapleBuffStat.ANGEL_ACC, eff.info.get(MapleStatInfo.indieAcc));
                eff.statups.put(MapleBuffStat.ANGEL_AVOID, eff.info.get(MapleStatInfo.indieEva));
                eff.statups.put(MapleBuffStat.ANGEL_JUMP, eff.info.get(MapleStatInfo.indieJump));
                eff.statups.put(MapleBuffStat.INDIE_SPEED, eff.info.get(MapleStatInfo.indieSpeed));
                eff.statups.put(MapleBuffStat.ANGEL_STAT, eff.info.get(MapleStatInfo.indieAllStat));
                break;
            case 5311004: //Barrel Roulette
                eff.statups.put(MapleBuffStat.BARREL_ROLL, 0);
                break;
            case 5321010: //Pirate's Spirit
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 5321054: //Buckshot
                //TODO
                break;
            case 5721009: //Relentless
                //TODO
                break;
            case 5721054: //Bionic Maximizer
                //TODO
                break;
            case 5121000: //Maple Warrior
            case 5221000: //Maple Warrior
            case 5321005: //Maple Warrior
            case 5721000: //Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 5121053: //Epic Adventure
            case 5221053: //Epic Adventure
            case 5321053: //Epic Adventure
            case 5721053: //Epic Adventure
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                //System.out.println("Pirate skill not coded: " + skill);
                break;
        }
    }
}
