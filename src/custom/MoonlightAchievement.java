/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import client.MapleCharacter;

/**
 *
 * @author Itzik
 */
public class MoonlightAchievement {

    private String name;
    private int reward;
    private final boolean notice;

    public MoonlightAchievement(String name, int reward) {
        this.name = name;
        this.reward = reward;
        this.notice = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean getNotice() {
        return notice;
    }

    public void finishMoonlightAchievement(MapleCharacter chr) {
        chr.setMoonlightAchievementFinished(MoonlightAchievements.getInstance().getByMapleAchievement(this));
    }

    public void unfinishMoonlightAchievement(MapleCharacter chr) {
        chr.setMoonlightAchievementUnfinished(MoonlightAchievements.getInstance().getByMapleAchievement(this));
    }
}
