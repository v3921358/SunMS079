package server.buffs.buffclasses.sengoku;

import client.MapleBuffStat;
import constants.GameConstants;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Charmander
 */
public class HayatoBuff extends AbstractBuffClass {

    public HayatoBuff() {
        buffs = new int[]{
            41001001, // Battoujutsu Stance
            41101003, // Military Might
            41101005, // Katana Booster
            41121003, // Iron Skin
            41121005, // Akatsuki Hero
            41121054, // God of Blades
            41121053, // Princess's Vow
        };
    }

    @Override
    public boolean containsJob(int job) {
        return GameConstants.isHayato(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 41001001: // Battoujutsu Stance - fixpls!!
//                eff.info.put(MapleStatInfo.time, Integer.valueOf(2100000000));
//                eff.statups.put(MapleBuffStat.CRITICAL_RATE, Integer.valueOf(-((Integer) eff.info.get(MapleStatInfo.y)).intValue()));
//                eff.statups.put(MapleBuffStat.HAYATO_STANCE, Integer.valueOf(-1));
//                eff.statups.put(MapleBuffStat.HAYATO_STANCE, eff.info.get(MapleStatInfo.prop));
//                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
                break;
            case 41101003: // Military Might
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.u));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.HAYATO3, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.HAYATO4, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.HAYATO5, eff.info.get(MapleStatInfo.x));
                break;
            case 41101005: // Katana Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 41121003: // Iron Skin,
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                break;
            case 41121005: // Akatsuki Hero
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 41121054: // God of Blades
                //TODO - works without
                break;
            case 41121053: // Princess's Vow
                //TODO
                break;
            default:
                //System.out.println("Hayato skill not coded: " + skill);
                break;
        }
    }
}
