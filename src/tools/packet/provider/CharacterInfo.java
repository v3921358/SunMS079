/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.packet.provider;

/**
 *
 * @author Itzik
 */
public enum CharacterInfo {

    STATS(0x1),
    MESOS(0x2),
    INVENTORY(0x8),
    ZERO(0x40),
    SKILL(0x100),
    STARTED_QUESTS(0x200),
    RING(0x800),
    TELEPORT(0x1000),
    COMPLETED_QUESTS(0x4000),
    COOLDOWN(0x8000),
    MONSTER_BOOK(0x10000),
    QUEST_INFO(0x40000),
    JAGUAR(0x200000);
    private long value;

    private CharacterInfo(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public boolean check(int flag) {
        return (flag & value) != 0;
    }
}
