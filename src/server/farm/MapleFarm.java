/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.farm;

import server.farm.inventory.FarmBuilding;
import client.MapleClient;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Itzik
 */
public class MapleFarm {

    private int id;
    private MapleClient owner;
    private String name;
    private int waru = 0;
    private int level = 0;
    private int exp = 0;
    private int aesthetic = 0;
    private List<FarmBuilding> buildings;
    private FarmAestheticStats aestheticStats;

    private MapleFarm(int id, MapleClient owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
    }

    public static MapleFarm getDefault(int id, MapleClient owner, String name) {
        MapleFarm farm = new MapleFarm(id, owner, name);
        farm.setLevel(0);
        farm.setWaru(0);
        farm.setExp(0);
        farm.setAestheticPoints(0);
        FarmAestheticStats stats = new FarmAestheticStats(farm);
        stats.setShopDiscountR(0);
        stats.setSpaceAddition(0);
        stats.setSpecialMerchantR(0);
        farm.setAestheticStats(stats);
        return farm;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwner(MapleClient owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWaru(int waru) {
        this.waru = waru;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setAestheticPoints(int aesthetic) {
        this.aesthetic = aesthetic;
    }

    public void setAestheticStats(FarmAestheticStats aestheticStats) {
        this.aestheticStats = aestheticStats;
    }

    public int getId() {
        return id;
    }

    public MapleClient getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public int getWaru() {
        return waru;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getAestheticPoints() {
        return aesthetic;
    }

    public FarmAestheticStats getAestheticStats() {
        return aestheticStats;
    }

    public List<Integer> getHousePositions() {
        List<Integer> housePositions = new LinkedList();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                housePositions.add(104 - j - i);
            }
        }
        return housePositions;
    }

    public boolean checkSpace(int size, int position) {
        List<Integer> housePositions = getHousePositions();
        for (FarmBuilding b : buildings) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (housePositions.contains(position)) {
                        return false;
                    }
                    if (b.getPosition() - j - i == position) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
