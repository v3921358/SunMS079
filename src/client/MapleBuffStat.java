package client;

import handling.Buffstat;
import java.io.Serializable;

public enum MapleBuffStat implements Serializable, Buffstat {

    WATK(0x1, 1),//物理攻击
    WDEF(0x2, 1),//物理防御
    MATK(0x4, 1),
    MDEF(0x8, 1),
    ACC(0x10, 1),
    AVOID(0x20, 1),
    HANDS(0x40, 1),
    SPEED(0x80, 1), //d
    JUMP(0x100, 1), //d
    MAGIC_GUARD(0x200, 1),
    DARKSIGHT(0x400, 1), //d
    BOOSTER(0x800, 1), // d
    POWERGUARD(0x1000, 1),//伤害反弹
    MAXHP(0x2000, 1),
    MAXMP(0x4000, 1),
    BEARASSAULT(0x6000, 1),
    INVINCIBLE(0x8000, 1),
    SOULARROW(0x10000, 1),
    STUN(0x20000, 1),
    POISON(0x40000, 1),
    SEAL(0x80000, 1),
    DARKNESS(0x100000, 1),
    COMBO(0x200000, 1),
    SUMMON(0x200000, 1), //hack buffstat for summons ^.- (does/should not increase damage... hopefully <3)
    WK_CHARGE(0x400000, 1),
    DRAGONBLOOD(0x800000, 1),
    HOLY_SYMBOL(0x1000000, 1),
    MESOUP(0x2000000, 1),
    SHADOWPARTNER(0x4000000, 1), // d
    PICKPOCKET(0x8000000, 1),
    PUPPET(0x8000000, 1), // HACK - shares buffmask with pickpocket - odin special ^.-
    MESOGUARD(0x10000000, 1),
    HP_LOSS_GUARD(0x20000000, 1),
    WEAKEN(0x40000000, 1),
    CURSE(0x80000000, 1),
    SLOW(0x1, 2),
    MORPH(0x2, 2),
    RECOVERY(0x4, 2),
    MAPLE_WARRIOR(0x8, 2),
    STANCE(0x10, 2),//稳如泰山
    SHARP_EYES(0x20, 2),
    MANA_REFLECTION(0x40, 2),
    SEDUCE(0x80, 2),
    SPIRIT_CLAW(0x100, 2), // d
    INFINITY(0x200, 2),
    HOLY_SHIELD(0x400, 2), //advanced blessing after ascension
    HAMSTRING(0x800, 2),
    BLIND(0x1000, 2),
    CONCENTRATE(0x2000, 2),
    ZOMBIFY(0x4000, 2),
    ECHO_OF_HERO(0x8000, 2),
    MESO_RATE(0x10000, 2), //confirmed
    GHOST_MORPH(0x20000, 2),
    ARIANT_COSS_IMU(0x40000, 2), // The white ball around you
    REVERSE_DIRECTION(0x80000, 2),
    DROP_RATE(0x100000, 2), //d
    //2 = unknown
    EXPRATE(0x400000, 2),
    ACASH_RATE(0x800000, 2),
    ILLUSION(0x1000000, 2), //hack buffstat //apply monster status error 38
    //2 and 4 are unknown
    BERSERK_FURY(0x8000000, 2),
    DIVINE_BODY(0x10000000, 2),
    //SPARK(0x20000000, 2),
    ARIANT_COSS_IMU2(0x40000000, 2), // no idea, seems the same
    FINALATTACK(0x80000000, 2),
    //4 = unknown
    //0x1?
    ELEMENT_RESET(0x2, 3),
    WIND_WALK(0x4, 3),
    //0x8
    ARAN_COMBO(0x4, 3),//0x10, 3
    COMBO_DRAIN(0x8, 3),//0x20, 3
    COMBO_BARRIER(0x10, 3),//0x40, 3
    BODY_PRESSURE(0x20, 3),//0x80, 3
    SMART_KNOCKBACK(0x40, 3),//0x100, 3
    PYRAMID_PQ(0x200, 3),
    // 4 ?
    POTION_CURSE(0x800, 3),
    SHADOW(0x1000, 3), //receiving damage/moving
    BLINDNESS(0x2000, 3),
    SLOWNESS(0x4000, 3),
    MAGIC_SHIELD(0x8000, 3),
    MAGIC_RESISTANCE(0x40000, 3),
    SOUL_STONE(0x20000, 3),
    SOARING(0x10000, 3),
    FREEZE(0x80000, 3),
    LIGHTNING_CHARGE(0x100000, 3),
    ENRAGE(0x200000, 3),
    OWL_SPIRIT(0x400000, 3),
    //0x800000 debuff, shiny yellow
    FINAL_CUT(0x1000000, 3),
    DAMAGE_BUFF(0x2000000, 3),
    ATTACK_BUFF(0x1000000, 3), //attack %? feline berserk v143
    RAINING_MINES(0x8000000, 3),
    ENHANCED_MAXHP(0x4000000, 3),//0x10000000
    ENHANCED_MAXMP(0x4000000, 3),//0x20000000
    ENHANCED_WATK(0x10000000, 3),//0x40000000
    ENHANCED_MATK(0x20000000, 3),//0x80000000
    ENHANCED_WDEF(0x40000000, 3),//0x1, 4
    ENHANCED_MDEF(0x80000000, 3),//0x2, 4
    PERFECT_ARMOR(0x1, 4),//0x4, 4
    SATELLITESAFE_PROC(0x2, 4),//0x8, 4
    SATELLITESAFE_ABSORB(0x4, 4),// 0x10, 4
    TORNADO(0x20, 4),
    CRITICAL_RATE_BUFF(0x40, 4),
    MP_BUFF(0x80, 4),
    DAMAGE_TAKEN_BUFF(0x100, 4),
    DODGE_CHANGE_BUFF(0x200, 4),
    CONVERSION(0x400, 4),
    REAPER(0x800, 4),
    INFILTRATE(0x1000, 4),
    MECH_CHANGE(0x2000, 4),
    DARK_AURA(0x2000, 4),
    BLUE_AURA(0x4000, 4),
    YELLOW_AURA(0x8000, 4),
    BODY_BOOST(0x10000, 4),//0x40000
    FELINE_BERSERK(0x20000, 4),//0x80000
    DICE_ROLL(0x40000, 4),//0x100000
    DIVINE_SHIELD(0x80000, 4),//0x200000
    DAMAGE_RATE(0x100000, 4),//0x400000
    TELEPORT_MASTERY(0x200000, 4),//-2
    COMBAT_ORDERS(0x400000, 4),//0x1000000
    BEHOLDER(0x800000, 4),//-2test
    DISABLE_POTENTIAL(0x4000000, 4),
    GIANT_POTION(0x8000000, 4),
    ONYX_SHROUD(0x10000000, 4),
    ONYX_WILL(0x8000000, 4),//-2
    TORNADO_CURSE(0x40000000, 4),
    BLESS(0x20000000, 4),//-2
    //1 //blue star + debuff
    //2 debuff	 but idk
    THREATEN_PVP(0x4, 5),
    ICE_KNIGHT(0x8, 5),
    //1 debuff idk.
    //2 unknown
    STR(0x10, 5),//-2
    INT(0x20, 5),//-2
    DEX(0x40, 5),//-2
    LUK(0x80, 5),//-2
    ATTACK(0x100, 5), //used also for kaiser majesty //-2
    //8 unknown tornado debuff? - hp
    INDIE_PAD(0x400, 5, true), // indiePad
    INDIE_MAD(0x800, 5, true),
    HP_BOOST(0x1000, 5, true), //0x4000 - 142 - weapon acc/magic acc pct boost
    MP_BOOST(0x2000, 5, true),
    ANGEL_ACC(0x4000, 5, true),
    ANGEL_AVOID(0x8000, 5, true),//0x20000
    ANGEL_JUMP(0x10000, 5, true),//0x40000
    INDIE_SPEED(0x20000, 5, true),//0x80000
    ANGEL_STAT(0x40000, 5, true),//0x100000
    PVP_DAMAGE(0x200000, 5),
    PVP_ATTACK(0x400000, 5), //skills
    INVINCIBILITY(0x800000, 5),
    HIDDEN_POTENTIAL(0x1000000, 5),
    ELEMENT_WEAKEN(0x2000000, 5),
    STACK_ALLSTATS(0x40000, 5),
    SNATCH(0x4000000, 5), //however skillid is 90002000, 1500 duration
    FROZEN(0x8000000, 5),
    //1, unknown
    ICE_SKILL(0x20000000, 5),
    //4 - debuff
    BOUNDLESS_RAGE(0x20000000, 5),
    // 1 unknown
    PVP_FLAG(0x2, 6),
    //4 unknown
    //8 unknown
    HOLY_MAGIC_SHELL(0x1, 6), //max amount of attacks absorbed
    //2 unknown a debuff
    MANY_USES(0x4, 6, true),//was40
    BUFF_MASTERY(0x200, 2), //buff duration increase //was 0x80, 6
    ABNORMAL_STATUS_R(0x10, 6), // %
    ELEMENTAL_STATUS_R(0x20, 6), // %
    WATER_SHIELD(0x40, 6),
    DARK_METAMORPHOSIS(0x800, 6), // mob count
    BARREL_ROLL(0x1000, 6),
    DAMAGE_R(0x200, 6),
    MDEF_BOOST(0x2000, 6),
    WDEF_BOOST(0x4000, 6),
    SPIRIT_LINK(0x4000, 6, true),
    CRITICAL_RATE(0x8000, 6), //used for perspective shift
    VIRTUE_EFFECT(0x10000, 6),
    //2, 4, 8 unknown
    NO_SLIP(0x100000, 6),
    FAMILIAR_SHADOW(0x200000, 6),
    LEECH_AURA(0x2000000, 6),
    // 4
    // 8
    //CRITICAL_RATE(0x1000000, 6),
    //0x2000000 unknown
    //0x4000000 unknown DEBUFF?
    //0x8000000 unknown DEBUFF?
    // 1 unknown	
    // 2 unknown	
    ABSORB_DAMAGE_HP(0x20000000, 6),
    DEFENCE_BOOST_R(0x4000000, 6), // weapon def and magic def
    // 8 unknown
    // 0x1
    // 0x2
    // 0x4
    HP_R(0x8, 7, true), //efficiency streamline
    // 0x10
    UNKNOWN8(0x20, 7),
    // 0x40 add attack, 425 wd, 425 md, 260 for acc, and avoid
    // 0x80
    ATTACK_SPEED(0x100, 7), //UNIGNORABLE_PDR or DAMAGE_REFLECT or ATTACK_SPEED or WATK
    HP_BOOST_PERCENT(0x200, 7, true),
    MP_BOOST_PERCENT(0x400, 7, true),
    //WEAPON_ATTACK(0x800, 7),
    UNKNOWN12(0x1000, 7), //+ 5003 wd
    // 0x2000,
    // 0x4000, true
    // 0x8000
    // WEAPON ATTACK 0x10000, true
    // 0x20000, true
    // 0x40000, true
    MP_R(0x80000, 7, true), //efficiency streamline
    // 0x100000  true
    // 0x200000 idk
    // 0x400000  true
    MOUNT_MORPH(0x100000, 7), //ion thrust 220
    UNKNOWN9(0x800000, 7),
    KILL_COUNT(0x800000, 7),
    IGNORE_DEF(0x2000000, 7),
    DAMAGE_PERCENT(0x80000000, 7, true),
    PHANTOM_MOVE(0x8, 8),
    JUDGMENT_DRAW(0x10, 8),
    HYBRID_DEFENSES(0x400, 8),
    UNKNOWN10(0x10, 8),
    LUMINOUS_GAUGE(0x200, 8),
    DARK_CRESCENDO(0x400, 8),
    BLACK_BLESSING(0x800, 8),
    PRESSURE_VOID(0x1000, 8),
    LUNAR_TIDE(0x2000, 8), //hp to damage
    KAISER_COMBO(0x8000, 8),
    IGNORE_MONSTER_DEF(0x10000, 8),
    KAISER_MODE_CHANGE(0x20000, 8),
    TEMPEST_BLADES(0x100000, 8),
    CRIT_DAMAGE(0x200000, 8),
    DAMAGE_ABSORBED(0x800000, 8),
    DAMAGE_CAP_INCREASE(0x40000000, 8, true),
    PRETTY_EXALTATION(0x1, 9),
    KAISER_MAJESTY3(0x2, 9), //UNIGNORABLE_PDR or DAMAGE_REFLECT or ATTACK_SPEED or WATK
    KAISER_MAJESTY4(0x4, 9), //UNIGNORABLE_PDR or DAMAGE_REFLECT or ATTACK_SPEED or WATK\
    WILL_OF_SWORD(0x100000, 8),
    PARTY_STANCE(0x10, 9),
    STATUS_RESIST_TWO(0x20, 9),
    BOWMASTERHYPER(0x400, 9),
    CRITICAL_PERCENT_UP(0x40000, 9), //critical or damage%
    MOON_STANCE2(0x80000000, 9), //critical or damage%
    ATTACK_COUNT(0x80000000, 9),
    EXCEED_ATTACK(0x4000000, 9),
    EXCEED(0x40000000, 9),
    DIABOLIC_RECOVERY(0x8000000, 9),
    BOSS_DAMAGE(0x1000000, 9),
    SUPPLY_SURPLUS(0x2, 10),
    XENON_FLY(0x10, 10),
    AMARANTH_GENERATOR(0x20, 10),
    STORM_ELEMENTAL(0x80, 10),
    PROP(0x100, 10),
    FROZEN_SHIKIGAMI(0x400, 10),
    TOUCH_OF_THE_WIND1(0x1000, 10),
    TOUCH_OF_THE_WIND2(0x2000, 10, true),
    ALBATROSS(0x4000, 10),
    SPEED_LEVEL(0x8000, 10),//0x8000, 10
    ADD_AVOIDABILITY(0x1000, 10),
    ACCURACY_PERCENT(0x20000, 10),
    WARRIOR_STANCE(0x10000, 10, true),
    SOUL_ELEMENT(0x40000, 10),
    EQUINOX_STANCE(0x80000, 10),
    SOLUNA_EFFECT(0x10000, 10),
    BATTOUJUTSU_STANCE(0x400000, 10),
    CROSS_SURGE(0x8000000, 10),
    HP_RECOVER(0x4000000, 10),
    PARASHOCK_GUARD(0x80000000, 10, true),
    PASSIVE_BLESS(0x4, 11),
    DIVINE_FORCE_AURA(0x1000, 11),
    DIVINE_SPEED_AURA(0x2000, 11),
    HAYATO1(0x2, 12),
    HAYATO2(0x4, 12),
    SHIKIGAMI(0x10, 12),
    HAYATO3(0x20, 12),
    HAYATO4(0x40, 12),
    HAYATO5(0x80, 12),
    HAYATO_STANCE(0x100, 12),
    FOX_FIRE(0x2000, 12),
    HAKU_REBORN(0x4000, 12), // 0x100000, 10
    HAKU_BLESS(0x8000, 12),
    ANIMAL_SELECT(0x100000, 12),
    //072
    ENERGY_CHARGE(0x2000, 3),//0x8 4
    DASH_SPEED(0x4000, 3),//0x80 4
    DASH_JUMP(0x8000, 3),//0x40 4
    MONSTER_RIDING(0x10000, 3),
    SPEED_INFUSION(0x20000, 3),
    HOMING_BEACON(0x40000, 3),
    DEFAULT_BUFFSTAT(0x80000, 3),
    召唤玩家1(0x2000, 3),
    召唤玩家2(0x4000, 3),
    召唤玩家3(0x8000, 3),
    召唤玩家4(0x10000, 3),
    召唤玩家5(0x20000, 3),
    召唤玩家6(0x40000, 3),
    召唤玩家7(0x80000, 3),
    召唤玩家8(0x100000, 3),
    ;

    private static final long serialVersionUID = 0L;
    private final int buffstat;
    private final int first;
    private boolean stacked = false;
    // [12] [11] [10] [9] [8] [7] [6] [5] [4] [3] [2] [1]
    // [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11]

    private MapleBuffStat(int buffstat, int first) {
        this.buffstat = buffstat;
        this.first = first;
    }

    private MapleBuffStat(int buffstat, int first, boolean stacked) {
        this.buffstat = buffstat;
        this.first = first;
        this.stacked = stacked;
    }

    @Override
    public int getPosition() {
        return getPosition(false);
    }

    public final int getPosition(boolean fromZero) {
        if (!fromZero) {
            return this.first;
        }
        switch (this.first) {
            case 4:
                return 0;
            case 3:
                return 1;
            case 2:
                return 2;
            case 1:
                return 3;
        }
        return 0;
    }

    @Override
    public int getValue() {
        return buffstat;
    }

    public final boolean canStack() {
        return stacked;
    }
}
