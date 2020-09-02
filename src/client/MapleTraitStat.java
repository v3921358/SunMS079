/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author wubin
 */
public enum MapleTraitStat {

    CHARISMA(0x100000),
    INSIGHT(0x200000),
    WILL(0x400000),
    CRAFT(0x800000),
    SENSE(0x1000000),
    CHARM(0x2000000);
    private final int i;

    private MapleTraitStat(int i) {
        this.i = i;
    }

    public int getValue() {
        return i;
    }

    public static MapleTraitStat getByValue(final int value) {
        for (final MapleTraitStat stat : MapleTraitStat.values()) {
            if (stat.i == value) {
                return stat;
            }
        }
        return null;
    }
}
