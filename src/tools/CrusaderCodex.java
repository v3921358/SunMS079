/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericSeekableLittleEndianAccessor;
import tools.data.input.SeekableLittleEndianAccessor;

/**
 * @Author: Maxcloud
 *
 */
public class CrusaderCodex {

    public static void main(String[] args) {
        System.out.println("LOADING :: Please wait...");
        //String out = args[0];
        //String out = "CrusaderItemDump";
        //File dir = new File(out);
        File text = new File("Codex.txt");
        //dir.mkdir();

        BufferedReader b = null;
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Item.wz")));
        try {
            String s;

            text.createNewFile();
            b = new BufferedReader(new FileReader("monsterbook.txt"));

            StringBuilder sb = new StringBuilder();
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(text))) {
                while ((s = b.readLine()) != null) {
                    byte[] bArray = HexTool.getByteArrayFromHexString(s);
                    SeekableLittleEndianAccessor slea = new GenericSeekableLittleEndianAccessor(new ByteArrayByteStream(bArray));
                    int card = slea.readInt();
                    int size = slea.readShort();
                    MapleData consume = data.getData("Consume/0238.img").getChildByPath("0" + String.valueOf(card));
                    int monsterid = MapleDataTool.getIntConvert("info/mob", consume, 0);
                    // Don't forget to insert the card, along with the rest of the drops.
                    sb.append("INSERT INTO `drop_data` (`dropperid`, `itemid`, `questid`, `chance`) VALUES (").append(monsterid).append(", ").append(card).append(", 0, 400);\r\n");
                    for (int i = 0; i < size; i++) {
                        int itemid = slea.readInt();
                        sb.append("INSERT INTO `drop_data` (`dropperid`, `itemid`, `questid`, `chance`) VALUES ");
                        sb.append("(").append(monsterid).append(", ").append(itemid).append(", 0, ").append(getChance(itemid) == -1 ? -1 : getChance(itemid) * 10).append(");\r\n");
                    }
                    writer.println(sb.toString());
                    sb.delete(0, sb.length());
                }
                writer.flush();
            }
            System.out.println("Success! The task has been completed.");
        } catch (IOException e) {
        } finally {
            try {
                if (b != null) {
                    b.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private static int getChance(int id) {
        switch (id / 10000) {
            case 100: // Hat
                switch (id) {
                    case 1003023: // Targa Hat (INT)
                    case 1003024: // Targa Hat (LUK)
                    case 1003025: // Scarlion (DEX)
                    case 1003026: // Scarlion (STR)
                    case 1002357: // Zakum Helmet
                    case 1002390: // Zakum Helmet 2
                    case 1002430: // Zakum Helmet 3
                    case 1003112: // Chaos Zakum Helmet
                    case 1003361: // Super Zakum Helmet
                    case 1003439: // Pink Zakum helmet
                        return 2;
                }
            case 104: // Topwear
            case 105: // Overall
            case 106: // Pants
            case 107: // Shoes
            case 108: // Gloves
            case 109: // Shield
            case 110: // Cape
            case 111: // Ring
            case 112: // Pendant
                switch (id) {
                    case 1122000: // Horntail Necklace
                    case 1122076: // Chaos Horntail Necklace
                        return 2;
                    case 1122011: // Timeless Pendant (30)
                    case 1122012: // Timeless Pendant (140)
                        return 2;
                }
            case 130: // One Handed Sword
            case 131: // One Handed Axe
            case 132: // One Handed Blunt Weapon
            case 133: // Dagger
            case 134: // Katara
            case 137: // Wand
            case 138: // Staff
            case 140: // One Handed Sword and Two Handed Sword
            case 141: // Two Handed Axe
            case 142: // Two Handed Blunt Weapon
            case 143: // Spear
            case 144: // Pole Arm
            case 145: // Bow
            case 146: // Crossbow
            case 147: // Claw
            case 148: // Knuckle
            case 149: // Gun
            case 150: // Shovel (Professions)
            case 151: // Pickaxe (Professions)
            case 152: // Dual Bowgun
            case 153: // Cannon
                return 5;
            case 135: // Magic Arrows
            case 233: // Bullets and Capsules
                return 15;
            case 204: // Scrolls
                switch (id) {
                    case 2049000: // Chaos Scroll
                    case 2049100: // Chaos Scroll 60%
                    case 2049116: // Miraculous Chaos Scroll 60%
                    case 2049117: // Chaos Scroll 60%
                    case 2049119: // Incredible Chaos Scroll 60%
                    case 2049122: // Chaos Scroll of Goodness 50%
                    case 2049409: // Legendary Black Dragon Chaos Scroll
                        return 1;
                }
                return 2;
            case 206: // Arrows
                return 30;
            case 228: // Skillbook
            case 229: // Mastery book
                switch (id) {
                    case 2290096: // Maple Hero 20
                    case 2290125: // Maple Hero 30
                        return 2;
                }
                return 5;
            case 251: // recipe
                switch (id / 1000) {
                    case 2510: // equipment
                        return 2;
                    case 2511: // accessories
                        return 1;
                    case 2512: // potions
                        return 5;
                }
                return 1;
            case 286: // Familiar
            case 287: // Familiar
                return 10;
            case 301: // Chair
                return 1;
            case 399: // Quest Items
                return -1;
        }
        switch (id / 1000000) {
            case 1: // Equipment that hasn't been stated above.
                return 3;
            case 2:
                switch (id) {
                    case 2000004: // Elixir
                    case 2000005: // Power Elixir
                    case 2000006: // Mana Elixir
                        return 15;
                    case 2000000: // Red Potion
                    case 2000002: // White Potion
                    case 2000003: // Blue Potion
                    case 2001001: // Ice Cream Pop
                    case 2002000: // Dexterity Potion
                    case 2002001: // Speed Potion
                    case 2002003: // Wizard Potion
                    case 2002004: // Warrior Potion
                    case 2002006: // Warrior Pill
                    case 2002011: // Pain Reliever
                    case 2010009: // Green Apple
                    case 2012001: // Fairy's Honey
                    case 2012002: // Sap of Ancient Tree
                    case 2022001: // Red Bean Porridge
                    case 2020013: // Reindeer Milk
                    case 2020014: // Sunrise Dew
                    case 2020015: // Sunset Dew
                    case 2022142: // Mind & Heart Medicine
                    case 2022186: // Soft White Bun
                        return 10;
                    case 2060000: // Arrow for Bow
                    case 2061000: // Arrow for Crossbow
                    case 2060001: // Bronze Arrow for Bow
                    case 2061001: // Bronze Arrow for Crossbow
                        return 15;
                    case 2070000: // Subi Throwing-Stars
                    case 2070001: // Wolbi Throwing-Stars
                    case 2070002: // Mokbi Throwing-Stars
                    case 2070003: // Kumbi Throwing-Stars
                    case 2070004: // Tobi Throwing-Stars
                    case 2070005: // Steely Throwing-Knives
                    case 2070006: // Ilbi Throwing-Stars
                    case 2070007: // Hwabi Throwing-Stars
                    case 2070008: // Snowball
                    case 2070009: // Wooden Top
                    case 2070010: // Icicle
                        return 10;
                    default:
                        return 5;
                }
            case 4:
                switch (id / 1000) {
                    case 4000: // monster piece(s)
                    case 4001: // quest piece(s)
                    case 4002: // stamp(s)
                    case 4003: // processing
                    case 4004: // crystal ore(s)
                    case 4005: // crystal(s)
                    case 4006: // magic rock & summoning rock
                    case 4007: // magic powder(s)
                    case 4010: // basic ore(s)
                    case 4011: // plates
                    case 4020: // advanced ore(s)
                    case 4021: // jewel
                    case 4022: // herbalism seeds
                    case 4023: // herbalism oils
                    case 4024: // herbalism bottles
                    case 4025: // herbalism coagulants
                    case 4030: // omok pieces, tetris pieces
                    case 4031: // quest items
                    case 4032: // ???
                    case 4033: // ???
                    case 4055: // surgery coupons
                    case 4080: // omok set
                    case 4130: // production stimulator
                    case 4140: // ???
                    case 4160: // pet command guides
                    case 4161: // ???
                    case 4162: // ???
                    case 4170: // pigmy eggs, etc.
                    case 4210: // rings
                    case 4211: // regular invitation
                    case 4212: // premium invitation
                    case 4213: // ???
                    case 4214: // wedding receipt
                    case 4220: // ???
                    case 4250: // item-production
                    case 4251: // item-production
                    case 4260: // item-production
                    case 4280: // treasure boxes
                    case 4290: // effects
                    case 4300: // ???
                    case 4310: // shop currency
                    case 4320: // ???
                    case 4330: // profession bags
                        return 55;
                    default:
                        return 55;
                }
        }
        return 0;
    }
}
