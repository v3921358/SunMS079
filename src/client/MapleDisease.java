package client;

import handling.Buffstat;
import java.io.Serializable;
import server.Randomizer;

public enum MapleDisease implements Serializable, Buffstat {

    SEAL(MapleBuffStat.SEAL, 120),
    DARKNESS(MapleBuffStat.DARKNESS, 121),
    WEAKEN(MapleBuffStat.WEAKEN, 122),
    STUN(MapleBuffStat.STUN, 123),
    CURSE(MapleBuffStat.CURSE, 124),
    POISON(MapleBuffStat.POISON, 125),
    SLOW(MapleBuffStat.SLOWNESS, 126),
    SEDUCE(MapleBuffStat.SEDUCE, 128),
    REVERSE_DIRECTION(MapleBuffStat.REVERSE_DIRECTION, 132),
    ZOMBIFY(MapleBuffStat.ZOMBIFY, 133),
    POTION(MapleBuffStat.POTION_CURSE, 134),
    SHADOW(MapleBuffStat.SHADOW, 135), //receiving damage/moving
    BLIND(MapleBuffStat.BLINDNESS, 136),
    FREEZE(MapleBuffStat.FREEZE, 137),
    DISABLE_POTENTIAL(MapleBuffStat.DISABLE_POTENTIAL, 138),
    MORPH(MapleBuffStat.MORPH, 172),
    TORNADO(MapleBuffStat.TORNADO_CURSE, 173),
    FLAG(MapleBuffStat.PVP_FLAG, 799); // PVP - Capture the Flag
    // 127 = 1 snow?
    // 129 = turn?
    // 131 = poison also, without msg
    // 133, become undead?..50% recovery?
    // 0x100 is disable skill except buff
    private static final long serialVersionUID = 0L;
    private final int buffstat;
    private final int first;
    private final int disease;

    private MapleDisease(MapleBuffStat buffstat, int disease) {
        this.buffstat = buffstat.getValue();
        this.first = buffstat.getPosition();
        this.disease = disease;
    }

    @Override
    public int getPosition() {
        return first;
    }

    @Override
    public int getValue() {
        return buffstat;
    }

    public int getDisease() {
        return disease;
    }

    public static MapleDisease getRandom() {
        while (true) {
            for (MapleDisease dis : MapleDisease.values()) {
                if (Randomizer.nextInt(MapleDisease.values().length) == 0) {
                    return dis;
                }
            }
        }
    }

    public static MapleDisease getBySkill(final int skill) {
        for (MapleDisease d : MapleDisease.values()) {
            if (d.getDisease() == skill) {
                return d;
            }
        }
        return null;
    }
}
