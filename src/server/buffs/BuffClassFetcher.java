/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs;

import server.MapleStatEffect;
import server.buffs.buffclasses.adventurer.BowmanBuff;
import server.buffs.buffclasses.adventurer.MagicianBuff;
import server.buffs.buffclasses.adventurer.PirateBuff;
import server.buffs.buffclasses.adventurer.ThiefBuff;
import server.buffs.buffclasses.adventurer.WarriorBuff;
import server.buffs.buffclasses.cygnus.DawnWarriorBuff;
import server.buffs.buffclasses.cygnus.MihileBuff;
import server.buffs.buffclasses.cygnus.WindArcherBuff;
import server.buffs.buffclasses.hero.AranBuff;
import server.buffs.buffclasses.hero.EvanBuff;
import server.buffs.buffclasses.hero.LuminousBuff;
import server.buffs.buffclasses.nova.AngelicBusterBuff;
import server.buffs.buffclasses.resistance.DemonBuff;
import server.buffs.buffclasses.resistance.WildHunterBuff;
import server.buffs.buffclasses.resistance.XenonBuff;
import server.buffs.buffclasses.sengoku.HayatoBuff;
import server.buffs.buffclasses.sengoku.KannaBuff;
import server.buffs.buffclasses.zero.ZeroBuff;

/**
 *
 * @author Saint
 */
public class BuffClassFetcher {

    public static final Class<?>[] buffClasses = {
        WarriorBuff.class,
        MagicianBuff.class,
        BowmanBuff.class,
        ThiefBuff.class,
        PirateBuff.class,
        DawnWarriorBuff.class,
        WindArcherBuff.class,
        MihileBuff.class,
        AranBuff.class,
        EvanBuff.class,
        LuminousBuff.class,
        AngelicBusterBuff.class,
        XenonBuff.class,
        WildHunterBuff.class,
        DemonBuff.class,
        KannaBuff.class,
        HayatoBuff.class,
        ZeroBuff.class
    };

    public static boolean getHandleMethod(MapleStatEffect eff, int skillid) {
        int jobid = skillid / 10000;
        for (Class<?> c : buffClasses) {
            try {
                if (!AbstractBuffClass.class.isAssignableFrom(c)) {
                    continue;
                }
                AbstractBuffClass cls = (AbstractBuffClass) c.newInstance();
                if (cls.containsJob(jobid)) {
                    if (!cls.containsSkill(skillid)) {
                        continue;
                    }
                    cls.handleBuff(eff, skillid);
                    return true;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                System.err.println("Error: handleBuff method was not found in " + c.getSimpleName() + ".class");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
