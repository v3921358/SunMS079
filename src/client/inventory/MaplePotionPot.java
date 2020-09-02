/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Itzik
 */
public class MaplePotionPot {

    private final int id, maxValue, hp, mp;
    private final long start, end;

    private MaplePotionPot(int id, int maxValue, int hp, int mp, long start, long end) {
        this.id = id;
        this.maxValue = maxValue;
        this.hp = hp;
        this.mp = mp;
        this.start = start;
        this.end = end;
    }

    public static MaplePotionPot loadFromResult(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            int maxValue = rs.getInt("maxValue");
            int hp = rs.getInt("hp");
            int mp = rs.getInt("mp");
            long start = rs.getLong("startDate");
            long end = rs.getLong("endDate");
            return new MaplePotionPot(id, maxValue, hp, mp, start, end);
        } catch (SQLException ex) {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public long getStartDate() {
        return start;
    }

    public long getEndDate() {
        return end;
    }
}
