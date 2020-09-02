package client.anticheat;

public enum CheatingOffense {

    FAST_SUMMON_ATTACK((byte) 5, 6000, 50, (byte) 2),
    FASTATTACK((byte) 5, 6000, 200, (byte) 2),
    FASTATTACK2((byte) 5, 6000, 500, (byte) 2),
    MOVE_MONSTERS((byte) 5, 30000, 500, (byte) 2),
    FAST_HP_MP_REGEN((byte) 5, 20000, 100, (byte) 2),
    SAME_DAMAGE((byte) 5, 180000),
    ATTACK_WITHOUT_GETTING_HIT((byte) 1, 30000, 1200, (byte) 0),
    HIGH_DAMAGE_MAGIC((byte) 5, 30000),
    HIGH_DAMAGE_MAGIC_2((byte) 10, 180000),
    HIGH_DAMAGE((byte) 5, 30000),
    HIGH_DAMAGE_2((byte) 10, 180000),
    EXCEED_DAMAGE_CAP((byte) 5, 60000, 800, (byte) 0),
    ATTACK_FARAWAY_MONSTER((byte) 5, 180000), // NEEDS A SPECIAL FORMULAR!
    ATTACK_FARAWAY_MONSTER_SUMMON((byte) 5, 180000, 200, (byte) 2),
    REGEN_HIGH_HP((byte) 10, 30000, 1000, (byte) 2),
    REGEN_HIGH_MP((byte) 10, 30000, 1000, (byte) 2),
    ITEMVAC_CLIENT((byte) 3, 10000, 100),
    ITEMVAC_SERVER((byte) 2, 10000, 100, (byte) 2),
    PET_ITEMVAC_CLIENT((byte) 3, 10000, 100),
    PET_ITEMVAC_SERVER((byte) 2, 10000, 100, (byte) 2),
    USING_FARAWAY_PORTAL((byte) 1, 60000, 100, (byte) 0),
    FAST_TAKE_DAMAGE((byte) 1, 60000, 100),
    HIGH_AVOID((byte) 5, 180000, 100),
    //FAST_MOVE((byte) 1, 60000),
    HIGH_JUMP((byte) 1, 60000),
    MISMATCHING_BULLETCOUNT((byte) 1, 300000),
    ETC_EXPLOSION((byte) 1, 300000),
    ATTACKING_WHILE_DEAD((byte) 1, 300000),
    USING_UNAVAILABLE_ITEM((byte) 1, 300000),
    FAMING_SELF((byte) 1, 300000), // purely for marker reasons (appears in the database)
    FAMING_UNDER_15((byte) 1, 300000),
    EXPLODING_NONEXISTANT((byte) 1, 300000),
    SUMMON_HACK((byte) 1, 300000),
    SUMMON_HACK_MOBS((byte) 1, 300000),
    ARAN_COMBO_HACK((byte) 1, 600000, 50, (byte) 2),
    HEAL_ATTACKING_UNDEAD((byte) 20, 30000, 100),
    攻击怪物数量异常((byte) 1, 300000, 10, (byte) 3),// 300秒內 觸發10次 自訂封鎖
    技能攻击次数异常((byte) 1, 300000, 10, (byte) 3),// 300秒內 觸發10次 自訂封鎖
    伤害过高((byte) 5, 30000, -1, (byte) 0),// 30秒內觸發五次即DC
    伤害过高2((byte) 10, 30000, -1, (byte) 0),// 180秒內觸發十次即DC
    魔法伤害过高((byte) 5, 30000, -1, (byte) 0),// 30秒內觸發五次即DC
    魔法伤害过高2((byte) 10, 30000, -1, (byte) 0),// 180秒內觸發十次即DC
    快速攻击((byte) 5, 6000, 200, (byte) 2),// 6秒內觸發兩百次即封鎖
    快速攻击2((byte) 5, 6000, 500, (byte) 2),// 6秒內觸發五百次即封鎖
    怪物碰撞过快((byte) 1, 60000L, 100, (byte) 2),//FAST_TAKE_DAMAGE
    吸怪((byte) 3, 7000, 7, (byte) 3),// 7秒內 觸發7次 自訂封鎖
    ;
    private final byte points;
    private final long validityDuration;
    private final int autobancount;
    private byte bantype = 0; // 0 = Disabled, 1 = Enabled, 2 = DC

    public final byte getPoints() {
        return points;
    }

    public final long getValidityDuration() {
        return validityDuration;
    }

    public final boolean shouldAutoban(final int count) {
        if (autobancount < 0) {
            return false;
        }
        return count >= autobancount;
    }

    public final byte getBanType() {
        return bantype;
    }

    public final void setEnabled(final boolean enabled) {
        bantype = (byte) (enabled ? 1 : 0);
    }

    public final boolean isEnabled() {
        return bantype >= 1;
    }

    private CheatingOffense(final byte points, final long validityDuration) {
        this(points, validityDuration, -1, (byte) 2);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount) {
        this(points, validityDuration, autobancount, (byte) 1);
    }

    private CheatingOffense(final byte points, final long validityDuration, final int autobancount, final byte bantype) {
        this.points = points;
        this.validityDuration = validityDuration;
        this.autobancount = autobancount;
        this.bantype = bantype;
    }
}
