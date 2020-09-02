package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import client.MonsterStatus;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class DawnWarriorBuff extends AbstractBuffClass {

    public DawnWarriorBuff() {
        buffs = new int[]{
            11001021, // Hand of Light
            11001022, // Soul Element
            11101023, // Divine Hand
            11101022, // Falling Moon
            11101003, // Rage
            11101024, // Soul Speed
            11101001, // Booster
            11111007, // Radiant Charge
            11111022, // Rising Sun
            11121012, // Equinox Cycle (Rising Sun)
            11121011, // Equinox Cycle (Falling Moon)
            11121005, // Equinox Cycle
            11111024, // Soul of the Guardian
            11121000, // Call of Cygnus
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isDawnWarrior(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 11101001: // booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 11001021: //Hand of Light
                eff.statups.put(MapleBuffStat.ADD_AVOIDABILITY, 20);
                eff.statups.put(MapleBuffStat.ACCURACY_PERCENT, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 11001022: // Soul Element
                eff.statups.put(MapleBuffStat.SOUL_ELEMENT, eff.info.get(MapleStatInfo.prop));
                eff.monsterStatus.put(MonsterStatus.STUN, 1);
                break;
            case 11101023: // Divine Hand
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 11101022: //Falling Moon
                eff.statups.put(MapleBuffStat.SOLUNA_EFFECT, 1);
                eff.statups.put(MapleBuffStat.ATTACK_COUNT, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.CRITICAL_PERCENT_UP, eff.info.get(MapleStatInfo.indieCr));
                break;
            case 11111022: // Rising Sun
                eff.statups.put(MapleBuffStat.SOLUNA_EFFECT, 2);
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ATTACK_SPEED, eff.info.get(MapleStatInfo.indieBooster));
                break;
            case 11111007: // rad charge
                eff.statups.put(MapleBuffStat.WK_CHARGE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.z));
                break;
            case 11111024: // soul of guardian
                eff.statups.put(MapleBuffStat.HP_BOOST, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.MDEF_BOOST, eff.info.get(MapleStatInfo.indiePdd));
                eff.statups.put(MapleBuffStat.WDEF_BOOST, eff.info.get(MapleStatInfo.indiePdd));
                break;
            case 11121005: // Equinox Cycle
                eff.statups.put(MapleBuffStat.SOLUNA_EFFECT, 1); // should be level but smd
                break;
            case 11121011: //Equinox Cycle (Falling Moon)
                eff.statups.put(MapleBuffStat.EQUINOX_STANCE, 11121012);
                eff.statups.put(MapleBuffStat.DAMAGE_PERCENT, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ATTACK_SPEED, eff.info.get(MapleStatInfo.indieBooster));
                break;
            case 11121012: //Equinox Cycle (Rising Sun)
                eff.statups.put(MapleBuffStat.EQUINOX_STANCE, 11121011);
                eff.statups.put(MapleBuffStat.CRITICAL_PERCENT_UP, eff.info.get(MapleStatInfo.indieCr));
                eff.statups.put(MapleBuffStat.ATTACK_COUNT, eff.info.get(MapleStatInfo.x));
                break;
            case 11121006: // soul pledge
                eff.statups.put(MapleBuffStat.CRITICAL_PERCENT_UP, eff.info.get(MapleStatInfo.indieCr));
                eff.statups.put(MapleBuffStat.STACK_ALLSTATS, eff.info.get(MapleStatInfo.indieAllStat));
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            default:
                //System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
