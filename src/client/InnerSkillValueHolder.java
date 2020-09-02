package client;

public class InnerSkillValueHolder {

    private int skillId = 0;
    private byte skillLevel = 0;
    private byte maxLevel = 0;
    private byte rank = 0;
    private boolean locked = false;

    public InnerSkillValueHolder(int skillId, byte skillLevel, byte maxLevel, byte rank, boolean locked) {
        this.skillId = skillId;
        this.skillLevel = skillLevel;
        this.maxLevel = maxLevel;
        this.rank = rank;
        this.locked = locked;
    }

    public int getSkillId() {
        return skillId;
    }

    public byte getSkillLevel() {
        return skillLevel;
    }

    public byte getMaxLevel() {
        return maxLevel;
    }

    public byte getRank() {
        return rank;
    }

    public boolean isLocked() {
        return locked;
    }
}
