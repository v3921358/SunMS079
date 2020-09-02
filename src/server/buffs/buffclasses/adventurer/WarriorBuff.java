/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.adventurer;

import client.MapleBuffStat;
import client.MonsterStatus;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Itzik
 */
public class WarriorBuff extends AbstractBuffClass {

    public WarriorBuff() {
        buffs = new int[]{
            1001003,
            1101004,
            1101005,
            1101006,
            1101007,
            1111002,
            1121000,
            1121002,
            1121010,
            1211003,
            1211004,
            1211005,
            1211006,
            1211007,
            1211008,
            1201004,
            1201005,
            1201007,
            1221000,
            1221002,
            1221003,
            1221004,
            1301004,
            1301005,
            1301006,
            1301007,
            1311008,
            1321000,
            1321002,
            1301013, //Evil Eye
            1211004, //Flame Charge
            1211006, //Blizzard Charge
            1211008, //Lightning Charge
            1211011, //Combat Orders
            1311013, //Evil Eye of Domination
            1311015, //Cross Surge
            1211010, //HP Recovery
            1211013, //Threaten
            1211014, //Parashock Guard
            1211011, //Combat Orders
            1221015, //Void Elemental
            1221016, //Guardian
            1221004, //Holy Charge

            1320011, //Revenge of the Beholden
            1121054, //Cry Valhalla
            1221054, //Sacrosanctity
            1121053, //Epic Adventure
            1221053, //Epic Adventure
            1221054, //Sacrosanctity
            1321053, //Epic Adventure
            1321054, //Dark Thrist
            1321007,};
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isAdventurer(job) && job / 100 == 1;
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 1101004:
            case 1101005:
            case 1201004:
            case 1201005:
            case 1301004: //Weapon Booster
            case 1301005: //Weapon Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 1101006: //愤怒之火
                eff.statups.put(MapleBuffStat.WATK, eff.info.get(MapleStatInfo.pad));
                eff.statups.put(MapleBuffStat.WDEF, eff.info.get(MapleStatInfo.pdd));
                break;
            case 1101007: //伤害反击
            case 1201007:
                eff.statups.put(MapleBuffStat.POWERGUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 1111002:
                eff.statups.put(MapleBuffStat.COMBO, 0);
                //eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 1211010: //HP Recovery
                eff.statups.put(MapleBuffStat.HP_RECOVER, 1);
                break;
            case 1211013: //Threaten
                eff.monsterStatus.put(MonsterStatus.WATK, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.WDEF, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.MATK, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.MDEF, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.AVOID, eff.info.get(MapleStatInfo.z));
                break;
            case 1211014: //Parashock Guard
                eff.statups.put(MapleBuffStat.PARASHOCK_GUARD, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.PARASHOCK_GUARD, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                //TODO
                break;
            case 1211011: //Combat Orders
                eff.statups.put(MapleBuffStat.COMBAT_ORDERS, eff.info.get(MapleStatInfo.x));
                break;
            case 1221015: //Void Elemental
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ELEMENT_RESET, eff.info.get(MapleStatInfo.x));
                break;
            case 1221054: //Sacrosanctity
                eff.statups.put(MapleBuffStat.KAISER_MAJESTY3, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ENRAGE, eff.info.get(MapleStatInfo.x));
                //TODO
                break;
            case 1301006: //Iron Will
                eff.statups.put(MapleBuffStat.WDEF, eff.info.get(MapleStatInfo.pdd));
                eff.statups.put(MapleBuffStat.MDEF, eff.info.get(MapleStatInfo.mdd));
                break;
            case 1301007: //Hyper Body
                eff.statups.put(MapleBuffStat.MAXHP, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MAXMP, eff.info.get(MapleStatInfo.y));
                break;
            case 1301013: //Evil Eye
                eff.statups.put(MapleBuffStat.BEHOLDER, eff.info.get(MapleStatInfo.x));
                break;
            case 1311015: //Cross Surge
                eff.statups.put(MapleBuffStat.CROSS_SURGE, eff.info.get(MapleStatInfo.x));
                break;
            case 1121000: //Maple Warrior
            case 1221000: //Maple Warrior
            case 1321000: //Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 1121053: //Epic Adventure
            case 1221053: //Epic Adventure
            case 1321053: //Epic Adventure
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.DAMAGE_CAP_INCREASE, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            case 1121002:
            case 1221002:
            case 1321002:
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 1121010:
                eff.statups.put(MapleBuffStat.WATK, eff.info.get(MapleStatInfo.pad));
                break;
            case 1211003:
            case 1211004:
            case 1211005:
            case 1211006:
            case 1211007:
            case 1211008:
            case 1221003:
            case 1221004:
                eff.statups.put(MapleBuffStat.WK_CHARGE, eff.info.get(MapleStatInfo.x));
                break;
            case 1311008: // dragon blood
                eff.statups.put(MapleBuffStat.DRAGONBLOOD, eff.info.get(MapleStatInfo.x));
                break;
            case 1321007:
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                break;
            default:
                //System.out.println("Warrior skill not coded: " + skill);
                break;
        }
    }
}
