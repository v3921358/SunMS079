/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author Itzik
 */
public enum CSCategory {

    FAV(2000000, "Favorite", 1, CSFlag.NORMAL.getValue(), 0),
    CAT1(1010000, "Special Promotions", 1, CSFlag.HOT.getValue(), 0),
    CAT1_1(1010100, "New Arrivals", 2, CSFlag.HOT.getValue(), 0),
    CAT1_2(1011000, "Daily Deals", 2, CSFlag.HOT.getValue(), 0),
    CAT1_3(1010200, "Discounted", 2, CSFlag.NORMAL.getValue(), 0),
    CAT1_4(1010300, "Limited Time Specials", 2, CSFlag.NORMAL.getValue(), 0),
    CAT1_5(1010400, "Limited Quantity", 2, CSFlag.NORMAL.getValue(), 0),
    CAT2(1020000, "Time Savers", 1, CSFlag.NORMAL.getValue(), 0),
    CAT2_1(1020100, "Teleport Rocks", 2, CSFlag.NORMAL.getValue(), 0),
    CAT2_2(1020200, "Item Stores", 2, CSFlag.NORMAL.getValue(), 0),
    CAT2_3(1020300, "Quest Helpers", 2, CSFlag.NORMAL.getValue(), 0),
    CAT2_4(1020500, "Packages", 2, CSFlag.NORMAL.getValue(), 1),
    CAT3(1030000, "Random Rewards", 1, CSFlag.NORMAL.getValue(), 0),
    CAT3_1(1030100, "Gachapon Tickets", 2, CSFlag.NORMAL.getValue(), 0),
    CAT3_2(1030200, "Surprise Boxes", 2, CSFlag.NORMAL.getValue(), 0),
    CAT3_3(1030300, "Special Items", 2, CSFlag.NORMAL.getValue(), 0),
    CAT4(1040000, "Equipment Modifications", 1, CSFlag.NORMAL.getValue(), 0),
    CAT4_1(1040100, "Miracle Cubes", 2, CSFlag.NORMAL.getValue(), 0),
    CAT4_2(1040400, "Upgrade Slots", 2, CSFlag.NORMAL.getValue(), 0),
    CAT4_3(1040500, "Trade", 2, CSFlag.NORMAL.getValue(), 0),
    CAT4_4(1040700, "Other", 2, CSFlag.NORMAL.getValue(), 0),
    CAT4_4_1(1040701, "Item Tag", 3, CSFlag.NORMAL.getValue(), 0),
    CAT4_4_2(1040702, "Item Guards", 3, CSFlag.NORMAL.getValue(), 0),
    CAT4_5(1040800, "Packages", 2, CSFlag.NORMAL.getValue(), 1),
    CAT5(1050000, "Character Modifications", 1, CSFlag.NORMAL.getValue(), 0),
    CAT5_1(1050100, "SP/AP modifications", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_2(1050200, "EXP Coupons", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_3(1050300, "Drop Coupons", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_4(1050400, "Inventory slots", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_5(1050500, "Skill Modifications", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_6(1050600, "Protection", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_7(1050700, "Wedding", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_8(1050800, "Other", 2, CSFlag.NORMAL.getValue(), 0),
    CAT5_9(1050900, "Packages", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6(1060000, "Equipment", 1, CSFlag.NORMAL.getValue(), 0),
    CAT6_1(1060100, "Weapon", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_2(1060200, "Hat", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_1(1060201, "Full Head Cover", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_2(1060202, "Beanies", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_3(1060203, "Hairpin", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_4(1060204, "Hairband", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_5(1060205, "Full Brim Hat", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_6(1060206, "Caps", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_2_7(1060211, "Other", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_3(1060300, "Face", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_4(1060400, "Eye", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_5(1060500, "Accessory", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_5_1(1060501, "Stats", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_6(1060600, "Earrings", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_7(1060700, "Overall", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_8(1060800, "Top", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_8_1(1060801, "Long Sleeves", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_8_2(1060802, "Short Sleeves", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_9(1060900, "Bottom", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_9_1(1060901, "Shorts", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_9_2(1060902, "Pants", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_9_3(1060903, "Skirts", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_9_4(1061000, "Shoes", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_9_5(1061100, "Glove", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_10(1061200, "Ring", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_10_1(1061201, "Stats", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_10_2(1061202, "Friendship", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_10_3(1061203, "Label", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_10_4(1061204, "Quote", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_10_5(1061206, "Solo", 3, CSFlag.NORMAL.getValue(), 0),
    CAT6_11(1061300, "Cape", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_12(1061500, "Packages", 2, CSFlag.NORMAL.getValue(), 0),
    CAT6_13(1061600, "Transparent", 2, CSFlag.NORMAL.getValue(), 0),
    CAT7(1070000, "Appearance", 1, CSFlag.NORMAL.getValue(), 0),
    CAT7_1(1070100, "Beauty Parlor", 2, CSFlag.NORMAL.getValue(), 0),
    CAT7_1_1(1070101, "Hair", 3, CSFlag.NORMAL.getValue(), 0),
    CAT7_1_2(1070102, "Face", 3, CSFlag.NORMAL.getValue(), 0),
    CAT7_1_3(1070103, "Skin", 3, CSFlag.NORMAL.getValue(), 0),
    CAT7_2(1070200, "Facial Expressions", 2, CSFlag.NORMAL.getValue(), 0),
    CAT7_3(1070300, "Effect", 2, CSFlag.NORMAL.getValue(), 0),
    CAT7_4(1070400, "Transformations", 2, CSFlag.NORMAL.getValue(), 0),
    CAT7_5(1070500, "Special", 2, CSFlag.NORMAL.getValue(), 0),
    CAT8(1080000, "Pet", 1, CSFlag.NORMAL.getValue(), 0),
    CAT8_1(1080100, "Pets", 2, CSFlag.NORMAL.getValue(), 0),
    CAT8_2(1080200, "Pet Appearance", 2, CSFlag.NORMAL.getValue(), 0),
    CAT8_3(1080300, "Pet Use", 2, CSFlag.NORMAL.getValue(), 0),
    CAT8_4(1080400, "Pet Food", 2, CSFlag.NORMAL.getValue(), 0),
    CAT8_5(1080500, "Packages", 2, CSFlag.NORMAL.getValue(), 0),
    CAT9(1090000, "Free Market", 1, CSFlag.NORMAL.getValue(), 0),
    CAT9_1(1090100, "Shop Permits", 2, CSFlag.NORMAL.getValue(), 0),
    CAT9_2(1090200, "Other", 2, CSFlag.NORMAL.getValue(), 0),
    CAT10(1100000, "Messenger and Social", 1, CSFlag.NORMAL.getValue(), 0),
    CAT10_1(1100100, "Megaphones", 2, CSFlag.NORMAL.getValue(), 0),
    CAT10_2(1100200, "Messengers", 2, CSFlag.NORMAL.getValue(), 0),
    CAT10_3(1100300, "Guild Forum Emoticons", 2, CSFlag.NORMAL.getValue(), 0),
    CAT10_4(1100400, "Weather Effects", 2, CSFlag.NORMAL.getValue(), 0),
    CAT10_4_1(1100401, "Stats", 3, CSFlag.NORMAL.getValue(), 0),
    CAT10_4_2(1100402, "Non-Stats", 3, CSFlag.NORMAL.getValue(), 0),
    CAT11(1300000, "Monster Life", 1, CSFlag.NEW.getValue(), 0),
    CAT11_1(1300100, "Incubators", 2, CSFlag.NORMAL.getValue(), 0),
    CAT11_2(1300200, "Gems", 2, CSFlag.NORMAL.getValue(), 0);
    private final int id, parent, flag, sold;
    private final String name;

    private CSCategory(int id, String name, int parent, int flag, int sold) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.flag = flag;
        this.sold = sold;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentDirectory() {
        return parent;
    }

    public int getFlag() {
        return flag;
    }

    public int getSold() {
        return sold;
    }

    public enum CSFlag {

        NORMAL(0),
        NEW(1),
        HOT(2);
        private final int value;

        private CSFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
