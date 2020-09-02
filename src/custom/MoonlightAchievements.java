/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Itzik
 */
public class MoonlightAchievements {

    private final Map<Integer, MoonlightAchievement> achievements = new LinkedHashMap<>();
    private static final MoonlightAchievements instance = new MoonlightAchievements();

    protected MoonlightAchievements() {
        //Moonlight Achievements: (Reset per day)
        achievements.put(99001, new MoonlightAchievement("Log in", 0));
        achievements.put(99002, new MoonlightAchievement("Level up", 0));
        achievements.put(99003, new MoonlightAchievement("Finish Dojo", 0));
        achievements.put(99004, new MoonlightAchievement("Get one fame", 0));
        achievements.put(99005, new MoonlightAchievement("Finish an event", 0));
        achievements.put(99006, new MoonlightAchievement("Finish Monster Park", 0));
        achievements.put(99007, new MoonlightAchievement("Finish Azwan", 0));
        achievements.put(99008, new MoonlightAchievement("Defeat a boss", 0));
        achievements.put(99009, new MoonlightAchievement("Do a PartyQuest", 0));
        achievements.put(99010, new MoonlightAchievement("Craft an item", 0));
    }

    public static MoonlightAchievements getInstance() {
        return instance;
    }

    public MoonlightAchievement getById(int id) {
        return achievements.get(id);
    }

    public Integer getByMapleAchievement(MoonlightAchievement ma) {
        for (Entry<Integer, MoonlightAchievement> achievement : this.achievements.entrySet()) {
            if (achievement.getValue() == ma) {
                return achievement.getKey();
            }
        }
        return null;
    }
}
