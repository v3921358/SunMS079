/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.inventory;

/**
 *
 * @author Itzik
 */
public enum EquipStat {

    SLOTS(0x01, 1, 1),
    LEVEL(0x02, 1, 1),
    STR(0x04, 2, 1),
    DEX(0x08, 2, 1),
    INT(0x10, 2, 1),
    LUK(0x20, 2, 1),
    MHP(0x40, 2, 1),
    MMP(0x80, 2, 1),
    WATK(0x100, 2, 1),
    MATK(0x200, 2, 1),
    WDEF(0x400, 2, 1),
    MDEF(0x800, 2, 1),
    ACC(0x1000, 2, 1),
    AVOID(0x2000, 2, 1),
    HANDS(0x4000, 2, 1),
    SPEED(0x8000, 2, 1),
    JUMP(0x10000, 2, 1),
    FLAG(0x20000, 2, 1),
    INC_SKILL(0x40000, 1, 1),
    ITEM_LEVEL(0x80000, 1, 1),
    ITEM_EXP(0x100000, 8, 1),
    DURABILITY(0x200000, 4, 1),
    VICIOUS_HAMMER(0x400000, 4, 1),
    PVP_DAMAGE(0x800000, 2, 1),
    DOWNLEVEL(0x1000000, 1, 1),
    ENHANCT_BUFF(0x2000000, 2, 1),
    DURABILITY_SPECIAL(0x4000000, 4, 1),
    REQUIRED_LEVEL(0x8000000, 1, 1),
    YGGDRASIL_WISDOM(0x10000000, 1, 1),
    FINAL_STRIKE(0x20000000, 1, 1),
    BOSS_DAMAGE(0x40000000, 1, 1),
    IGNORE_PDR(0x80000000, 1, 1),
    TOTAL_DAMAGE(0x1, 1, 2),
    ALL_STAT(0x2, 1, 2),
    KARMA_COUNT(0x4, 1, 2),
    UNK8(0x8, 8, 2), //long
    UNK10(0x10, 4, 2); //int
    private final int value, datatype, first;

    private EquipStat(int value, int datatype, int first) {
        this.value = value;
        this.datatype = datatype;
        this.first = first;
    }

    public final int getValue() {
        return value;
    }

    public final int getDatatype() {
        return datatype;
    }

    public final int getPosition() {
        return first;
    }

    public final boolean check(int flag) {
        return (flag & value) != 0;
    }

    public enum EnhanctBuff {

        UPGRADE_TIER(0x1),
        NO_DESTROY(0x2),
        SCROLL_SUCCESS(0x4);
        private final int value;

        private EnhanctBuff(int value) {
            this.value = value;
        }

        public final byte getValue() {
            return (byte) value;
        }

        public final boolean check(int flag) {
            return (flag & value) != 0;
        }
    }
}
