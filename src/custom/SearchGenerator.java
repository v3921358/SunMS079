/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package custom;

import client.Skill;
import client.SkillFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.ItemInformation;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.Pair;

/**
 *
 * @author Itzik
 */
public class SearchGenerator {

    public static String searchData(int type, String search) {
        String result = "";
        MapleData data;
        MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/String.wz"));
        List<String> ret = new ArrayList<>();
        List<Pair<Integer, String>> pairList = new LinkedList<>();
        if (type == 1) {
            for (ItemInformation pair : MapleItemInformationProvider.getInstance().getAllItems()) {
                if (pair != null && pair.name != null && pair.name.toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.itemId + "##b" + pair.itemId + " " + " #k- " + " #r#z" + pair.itemId + "##k");
                }
            }
        } else if (type == 2) {
            data = dataProvider.getData("Npc.img");
            for (MapleData IdData : data.getChildren()) {
                pairList.add(new Pair<>(Integer.parseInt(IdData.getName()), MapleDataTool.getString(IdData.getChildByPath("name"), "NO-NAME")));
            }
            for (Pair<Integer, String> pair : pairList) {
                if (pair.getRight().toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.getLeft() + "#" + pair.getLeft() + " - " + pair.getRight());
                }
            }
        } else if (type == 3) {
            data = dataProvider.getData("Map.img");
            for (MapleData mapAreaData : data.getChildren()) {
                for (MapleData IdData : mapAreaData.getChildren()) {
                    pairList.add(new Pair<>(Integer.parseInt(IdData.getName()), MapleDataTool.getString(IdData.getChildByPath("streetName"), "NO-NAME") + " - " + MapleDataTool.getString(IdData.getChildByPath("mapName"), "NO-NAME")));
                }
            }
            for (Pair<Integer, String> pair : pairList) {
                if (pair.getRight().toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.getLeft() + "#" + pair.getLeft() + " - " + pair.getRight());
                }
            }
        } else if (type == 4) {
            data = dataProvider.getData("Mob.img");
            for (MapleData IdData : data.getChildren()) {
                pairList.add(new Pair<>(Integer.parseInt(IdData.getName()), MapleDataTool.getString(IdData.getChildByPath("name"), "NO-NAME")));
            }
            for (Pair<Integer, String> pair : pairList) {
                if (pair.getRight().toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.getLeft() + "#" + pair.getLeft() + " - " + pair.getRight());
                }
            }
        } else if (type == 5) {
            for (MapleQuest pair : MapleQuest.getAllInstances()) {
                if (pair.getName().length() > 0 && pair.getName().toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.getId() + "#" + pair.getId() + " - " + pair.getName());
                }
            }
        } else if (type == 6) {
            for (Skill pair : SkillFactory.getAllSkills()) {
                if (pair.getName() != null && pair.getName().toLowerCase().contains(search.toLowerCase())) {
                    ret.add("\r\n#L" + pair.getId() + "#" + pair.getId() + " - " + pair.getName());
                }
            }
        }
        if (ret != null && ret.size() > 0) {
            for (String singleRet : ret) {
                if (result.length() < 10000) {
                    result += singleRet;
                } else {
                    result += "\r\n#bCouldn't load all data, there are too many results.#k";
                    return result;
                }
            }
        } else {
            result = "";
        }
        return result;
    }

    public static boolean foundData(int type, String search) {
        return !searchData(type, search).isEmpty();
    }
}
