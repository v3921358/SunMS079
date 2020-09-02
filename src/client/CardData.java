package client;

import java.io.Serializable;

/**
 *
 * @author AlphaEta
 */
public class CardData implements Serializable {

    private static final long serialVersionUID = 2550550428979893978L;
    public int cid;
    public short job, level;

    public CardData(int cid, short level, short job) {
        this.cid = cid;
        this.level = level;
        this.job = job;
    }

    @Override
    public String toString() {
        return "CID: " + cid + ", Job: " + job + ", Level: " + level;
    }
}
