/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Itzik
 */
public class PartTimeJob {

    private final int cid;
    private byte job = 0;
    private long time = 0;
    private int reward = 0;

    public PartTimeJob(int cid) {
        this.cid = cid;
    }

    public int getCharacterId() {
        return cid;
    }

    public byte getJob() {
        return job;
    }

    public long getTime() {
        return time;
    }

    public int getReward() {
        return reward;
    }

    public void setJob(byte job) {
        this.job = job;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
