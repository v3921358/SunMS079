package client;

import constants.GameConstants;
import server.Randomizer;

public class InnerAbillity {

    private static InnerAbillity instance = null;

    public static InnerAbillity getInstance() {
        if (instance == null) {
            instance = new InnerAbillity();
        }
        return instance;
    }

    public InnerSkillValueHolder renewSkill(int rank, int circulator, boolean locked) {
        return renewSkill(rank, circulator, false, locked);
    }

    public InnerSkillValueHolder renewSkill(int rank, int circulator, boolean ultimateCirculatorPos, boolean locked) {
        if (ultimateCirculatorPos && circulator == 2701000) {
            int randomSkill = GameConstants.getInnerSkillbyRank(3)[(int) Math.floor(Math.random() * GameConstants.getInnerSkillbyRank(rank).length)];
            int random = Randomizer.nextInt(100);
            int skillLevel;
            if (random < 38) {
                skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 2, SkillFactory.getSkill(randomSkill).getMaxLevel());
            } else if (random < 70) {
                skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 3, SkillFactory.getSkill(randomSkill).getMaxLevel() / 2);
            } else {
                skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 4, SkillFactory.getSkill(randomSkill).getMaxLevel() / 3);
            }
            return new InnerSkillValueHolder(randomSkill, (byte) skillLevel, (byte) SkillFactory.getSkill(randomSkill).getMaxLevel(), (byte) 3, locked);
        }

        int circulatorRate = 0;
        if (circulator == -1) {
            circulatorRate = 10;
        } else {
            int circulatorRank = getCirculatorRank(circulator);
            if (circulatorRank == 0) {
                circulatorRate = 10;
            } else if (circulatorRank == 1) {
                circulatorRate = 20;
            } else if (circulatorRank == 2) {
                circulatorRate = 30;
            } else if (circulatorRank == 3) {
                circulatorRate = 35;
            } else if (circulatorRank == 4) {
                circulatorRate = 40;
            } else if (circulatorRank == 5) {
                circulatorRate = 45;
            } else if (circulatorRank == 6) {
                circulatorRate = 50;
            } else if (circulatorRank == 7) {
                circulatorRate = 55;
            } else if (circulatorRank == 8) {
                circulatorRate = 60;
            } else if (circulatorRank == 9) {
                circulatorRate = 65;
            } else if (circulatorRank == 10) {
                circulatorRate = 70;
            }
        }

        if (Randomizer.isSuccess(3 + circulatorRate)) {
            rank = 1;
        } else if (Randomizer.isSuccess(2 + circulatorRate / 2)) {
            rank = 2;
        } else if (Randomizer.isSuccess(1 + circulatorRate / 4)) {
            rank = 3;
        } else {
            rank = 0;
        }

        int randomSkill = GameConstants.getInnerSkillbyRank(rank)[(int) Math.floor(Math.random() * GameConstants.getInnerSkillbyRank(rank).length)];
        int random = Randomizer.nextInt(100);
        int skillLevel;
        if (random < 3 + circulatorRate / 2) {
            skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 2, SkillFactory.getSkill(randomSkill).getMaxLevel());
        } else if (random < circulatorRate) {
            skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 3, SkillFactory.getSkill(randomSkill).getMaxLevel() / 2);
        } else {
            skillLevel = Randomizer.rand(SkillFactory.getSkill(randomSkill).getMaxLevel() / 4, SkillFactory.getSkill(randomSkill).getMaxLevel() / 3);
        }
        return new InnerSkillValueHolder(randomSkill, (byte) skillLevel, (byte) SkillFactory.getSkill(randomSkill).getMaxLevel(), (byte) rank, locked);
    }

    public int getCirculatorRank(int circulator) {
        return ((circulator % 1000) / 100) + 1;
    }
}
