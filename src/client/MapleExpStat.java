/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author Wubin
 */
public enum MapleExpStat {

    活动奖励经验(0x1),
    特别经验(0x2),
    Activity_Team_EXP(0x4),//活动组队经验
    Team_Bonus_EXP(0x10),
    Marriage_Bonus_EXP(0x20),
    Equipment_Bonus_EXP(0x40),
    Internet_Cafe_EXP(0x80),
    彩虹周奖励经验(0x100),
    欢享奖励经验(0x200),
    飞跃奖励经验(0x400),
    Elves_Bless_EXP(0x800),
    增益奖励经验(0x1000),
    休息经验(0x2000),
    物品奖励经验(0x4000),
    阿斯旺获胜者奖励经验(0x8000),
    使用道具经验(0x10000);
    private final int i;

    private MapleExpStat(int i) {
        this.i = i;
    }

    public int getValue() {
        return this.i;
    }
}
