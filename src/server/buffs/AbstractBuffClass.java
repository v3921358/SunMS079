/*
 * This file was designed for Luminous.
 * Do not redistribute without explicit permission from the
 * developer(s).
 */
package server.buffs;

import server.MapleStatEffect;

/**
 * These classes have two major purposes. 1) These classes will serve as a
 * reference to buffs. 2) These classes will hopefully provide a structured way
 * to add new buffs.
 *
 * I am aware that these could be written without including the buffs for each
 * job. However, this would defeat the purpose of having it structured this way.
 */
public abstract class AbstractBuffClass {

    protected int[] buffs;

    /*public int[] getJobIds() {
     return jobIds;
     }*/
    public int[] getBuffs() {
        return buffs;
    }

    /*public boolean containsJob(int search) {

     for (int i : jobIds) {
     if (i == search) {
     return true;
     }
     }
     return false;
     }*/
    public boolean containsSkill(int search) {
        for (int i : buffs) {
            if (i == search) {
                return true;
            }
        }
        return false;
    }

    //public abstract boolean containsJob(int job);
    public boolean containsJob(int job) {
        return false;
    }

    public abstract void handleBuff(MapleStatEffect eff, int skill);
}
