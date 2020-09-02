/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.export;

import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Itzik
 */
public class DropRetriever {

    private static final ArrayList<DropEntry> drops = new ArrayList<>();
    private static final String BASE_URL = "http://maplearchive.com/";
    private static final String MONSTER_PAGE = "mob-wp.php";
    private static final String ITEM_PAGE = "item-wp.php";
    private static int currPage = 1;
    public static String VERSION = "Unknown";
    private static int parsedMobs = 0;
    private static int droppingMobs = 0;
    private static int undroppingMobs = 0;
    private static int exceptions = 0;

    private static boolean parsePage(final String url) {
        try {
            URL page = new URL(url);
            String temp_data;
            try (InputStream is = page.openStream();
                    Scanner s = new Scanner(is)) {
                temp_data = "";
                while (s.hasNext()) {
                    temp_data += s.nextLine() + "\n";
                }
            }
            if (temp_data.contains("<title>Maple Archive @ GMS")
                    && temp_data.contains("· MapleStory Library")) {
                VERSION = getStringBetween(temp_data,
                        "<title>Maple Archive @ GMS ",
                        " · MapleStory Library");
            }
            if (!temp_data.contains("class=\"mobImage\"")) {
                return false;
            }
            while (temp_data.contains("class=\"mobImage\"")) {
                try {
                    String monster_section;
                    if (!temp_data.contains("<div class=\"entityBox\">")) {
                        monster_section = getStringBetween(temp_data,
                                "class=\"mobImage\"",
                                "<div class=\"pagination\">"); // Who cares, it
                        // works
                    } else {
                        monster_section = getStringBetween(temp_data,
                                "class=\"mobImage\"",
                                "<div class=\"entityBox\">");
                    }
                    parseMonsterSection(monster_section);
                    temp_data = trimUntil(temp_data,
                            "<div class=\"entityBox\">");
                    if (temp_data == null) {
                        break;
                    }
                } catch (StringIndexOutOfBoundsException ex) {
                    System.out.println("An exception was found. Skipping.");
                    exceptions++;
                    break;
                }
            }
            System.out.println("Parsing page " + currPage + " complete.");
            if (currPage % 10 == 0) {
                System.out.println();
                System.out.println("Status so far:");
                System.out.println("Monsters: " + parsedMobs
                        + " || Monsters with drops: " + droppingMobs
                        + " || Monsters without drops: " + undroppingMobs
                        + " || Items: " + drops.size() + " || Errors: "
                        + exceptions);
                System.out.println();
            }
        } catch (MalformedURLException mue) {
            System.out
                    .println("A malware informed url has been found and cannot be accessed: "
                            + url);
            exceptions++;
        } catch (IOException ioe) {
            System.out.println("Error reading from URL: "
                    + ioe.getLocalizedMessage());
            exceptions++;
        }
        return true;
    }

    public static void dumpQuery() {
        String filename = "MapleArchive-Drops-v" + VERSION + ".sql";
        try {
            File output = new File(filename);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(output));
                    PrintWriter pw = new PrintWriter(bw)) {
                StringBuilder sb = new StringBuilder();
                pw.write("TRUNCATE TABLE `drop_data`;\r\n");
                pw.write("INSERT INTO `drop_data` (`dropperid`, `itemid`, `minimum_quantity`, `maximum_quantity`, `questid`, `chance`) VALUES ");
                for (Iterator<DropEntry> i = drops.iterator(); i.hasNext();) {
                    DropEntry de = i.next();
                    if (!pw.toString().contains(de.getQuerySegment())) {
                        pw.write(de.getQuerySegment());
                    }
                    if (i.hasNext()) {
                        pw.write(", \r\n");
                    }
                }
                pw.write(sb.toString());
            }
        } catch (IOException ioe) {
            System.out.println("Error writing to file: "
                    + ioe.getLocalizedMessage());
        }
    }

    public static String getStringBetween(final String line,
            final String start, final String end) {
        int start_offset = line.indexOf(start) + start.length();
        return line.substring(start_offset, line.substring(start_offset)
                .indexOf(end) + start_offset);
    }

    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("MapleArchive Drop Retriever");
        System.out.println("---------------------------------------");
        System.out.println("Average proccess time: 9 hours.");
        System.out.println();
        currPage = 1;
        while (true) {
            System.out.println("Parsing page " + currPage);
            if (!parsePage(BASE_URL + MONSTER_PAGE + "?page=" + currPage)) {
                break;
            }
            currPage++;
        }
        long dataEndTime = System.currentTimeMillis();

        System.out.println("Retrieving drop data is complete.");
        System.out.println();
        System.out.println("Creating an sql query and saving into file.");
        long sqlStartTime = System.currentTimeMillis();
        dumpQuery();
        long sqlEndTime = System.currentTimeMillis();
        System.out.println("Sql query is ready.");
        System.out.println("------------------------");
        System.out.println("Process finished!");
        System.out.println("Total monsters parsed: " + parsedMobs
                + " || Total monsters with drops: " + droppingMobs
                + "|| Total monsters without drops: " + undroppingMobs
                + " || Total items: " + drops.size() + " || Total errors: "
                + exceptions);
        System.out.println("Data reading time: "
                + ((dataEndTime - startTime) / 1000)
                + " seconds || SQL creation time: "
                + ((sqlEndTime - sqlStartTime) / 1000) + " seconds");
        System.out.println("Total time: "
                + ((System.currentTimeMillis() - startTime) / 1000)
                + " seconds.");
        System.out.println("------------------------");
    }

    private static void parseItemSection(final String html_data,
            final int MonsterId, final boolean isBoss) {
        String temp_data = html_data;
        while (temp_data.contains("AJAXLoad('Item', 'id=")) {
            int ItemId = Integer.parseInt(getStringBetween(temp_data,
                    "AJAXLoad('Item', 'id=", "');\">"));
            int Quest;
            try {
                Quest = getQuestById(ItemId);
            } catch (IOException ex) {
                Quest = 0;
            }
            DropEntry drop = new DropEntry(ItemId, MonsterId, Quest, isBoss);
            if (!drops.contains(drop)) {
                drops.add(drop);
            }
            if (temp_data.contains("javascript:return ")) {
                temp_data = trimUntil(temp_data, "javascript:return ");
            } else {
                return;
            }
        }
    }

    private static void parseMonsterSection(final String html_data) {
        try {
            parsedMobs++;
            int MonsterId = Integer.parseInt(getStringBetween(html_data,
                    "alt=\"Mob:", "\" />")); // Will it blend? ;-)
            boolean isBoss = false;
            String BossString = getStringBetween(
                    html_data,
                    "<tr><td class=\"statName\"><b>Boss:</b></td><td class=\"statValue\">",
                    "</td></tr>");
            if (BossString.equalsIgnoreCase("No")) {
                isBoss = false;
            } else if (BossString.equalsIgnoreCase("Yes")) {
                isBoss = true;
            }

            if (getStringBetween(html_data, "<td class=\"tdDrops\" ", "</td>")
                    .contains("<ul><li>None/Unknown</li></ul>")) {
                undroppingMobs++;
                return;
            }

            // Parse Equipment drops
            if (html_data.contains(">Equipment</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Equipment</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Potion drops
            if (html_data.contains(">Potion</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Potion</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Food drops
            if (html_data.contains(">Food</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Food</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Arrow drops
            if (html_data.contains(">Arrows</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Arrows</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Bullet drops
            if (html_data.contains(">Bullet</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Bullet</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Throwing Star drops
            if (html_data.contains(">Throwing Star</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Throwing Star</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Status Removal Potions drops (Anidote, tonic, etc.)
            if (html_data.contains(">Status Removal Potion</a>")) {
                parseItemSection(
                        getStringBetween(html_data,
                                ">Status Removal Potion</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Mastery Book drops
            if (html_data.contains(">Mastery Book</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Mastery Book</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Skill Book drops
            if (html_data.contains(">Skill Book</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Skill Book</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Misc. Box (The hell is that? :O)
            if (html_data.contains(">Misc. Box</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Misc. Box</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Summoning Sack drops
            if (html_data.contains(">Summoning Sack</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Summoning Sack</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Familiar drops
            if (html_data.contains(">Familiar</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Familiar</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Item Pot drops
            if (html_data.contains(">Item Pot</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Item Pot</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Jett Core Modifier drops
            if (html_data.contains(">Jett Core Modifier</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Jett Core Modifier</a>",
                                "</ul></li>"), MonsterId, isBoss);
            }
            // Parse Recipe drops
            if (html_data.contains(">Recipe</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Recipe</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse Setup drops
            if (html_data.contains(">Setup</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Setup</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
            // Parse ETC drops
            if (html_data.contains(">Etc</a>")) {
                parseItemSection(
                        getStringBetween(html_data, ">Etc</a>", "</ul></li>"),
                        MonsterId, isBoss);
            }
        } catch (StringIndexOutOfBoundsException ex) {
            System.out
                    .println("Uh oh! Something went wrong. Skipping this one...");
            exceptions++;
        }

        droppingMobs++;
    }

    public static int getQuestById(final int itemId) throws IOException {
        int quest;
        try {
            URL page = new URL(BASE_URL + ITEM_PAGE + "?id=" + itemId);
            String temp_data;
            try (InputStream is = page.openStream();
                    Scanner s = new Scanner(is)) {
                temp_data = "";
                while (s.hasNext()) {
                    temp_data += s.nextLine() + "\n";
                }
            }
            if (!temp_data
                    .contains("<i>Required for quests <a class=\"loadLink\"")) {
                return 0;
            }
            quest = Integer.parseInt(getStringBetween(temp_data,
                    "AJAXLoad('Quest', 'id=", "');\">"));
        } catch (MalformedURLException | NumberFormatException ex) {
            return 0;
        }
        return quest;
    }

    public static String trimUntil(final String line, final String until) {
        int until_pos = line.indexOf(until);
        if (until_pos == -1) {
            return null;
        } else {
            return line.substring(until_pos + until.length());
        }
    }

    private static class DropEntry {

        private final int itemId;
        private final int monsterId;
        private final int chance;
        private int mindrop;
        private int maxdrop;
        private final int quest;
        private final boolean isBoss;

        public DropEntry(final int itemId, final int monsterId,
                final int quest, final boolean isBoss) {
            this.itemId = itemId;
            this.monsterId = monsterId;
            mindrop = 1;
            maxdrop = 1;
            if (getChance(itemId) != 0) {
                chance = getChance(itemId) * 10;
            } else {
                chance = calculateChance(itemId) / 1000;
            }
            this.quest = quest;
            this.isBoss = isBoss;
        }

        private int calculateChance(final int itemId) {
            MapleInventoryType mit = GameConstants.getInventoryType(itemId);
            int number = (itemId / 1000) % 1000;
            switch (mit) {
                case EQUIP:
                    if (isBoss) {
                        return 300000;
                    }
                    return 7000;
                case USE:
                    if (isBoss) {
                        mindrop = 1;
                        maxdrop = 4;
                    }
                    switch (number) {
                        case 0: // Normal potions
                            mindrop = 1;
                            maxdrop = 5;
                            return 100000;
                        case 1: // watermelons, pills, speed potions, etc
                        case 2: // same thing
                            return 50000;
                        case 3: // advanced potions from crafting (should not drop)
                        case 4: // same thing
                        case 11: // poison mushroom
                        case 28: // cool items
                        case 30: // return scrolls
                        case 46: // gallant scrolls
                            return 0;
                        case 10: // strange potions like apples, eggs
                        case 12: // drakes blood, sap of ancient tree (rare use)
                        case 20: // salad, fried chicken, dews
                        case 22: // air bubbles and stuff. ALSO nependeath honey but oh
                        // well
                        case 50: // antidotes and stuff
                        case 290: // mastery books
                            return 10000;
                        case 40:
                        case 41:
                        case 43:
                        case 44:
                        case 48: // pet scrolls
                        case 100: // summon bags
                        case 101: // summon bags
                        case 102: // summon bags
                        case 109: // summon bags
                        case 120: // pet food
                        case 211: // cliffs special potion
                        case 240: // rings
                        case 270: // pheromone, additional weird stuff
                        case 310: // teleport rock
                        case 320: // weird drops
                        case 390: // weird
                        case 430: // quiz things? compass?
                        case 440: // jukebox
                        case 460: // magnifying glass
                        case 470: // golden hammer
                        case 490: // crystanol
                        case 500: // sp reset
                            return 0;
                        case 47: // tablets from dragon rider
                            return 250000;
                        case 49: // clean slats, potential scroll, ees
                        case 70: // throwing stars
                        case 210: // rare monster piece drops
                        case 330: // bullets
                            return 1000;
                        case 60: // bow arrows
                        case 61: // crossbow arrows
                            mindrop = 10;
                            maxdrop = 50;
                            return 20000;
                        case 213: // boss transfrom
                            return 300000;
                        case 280: // skill books
                            return 200000;
                        case 381: // monster book things
                        case 382:
                        case 383:
                        case 384:
                        case 385:
                        case 386:
                        case 387:
                        case 388:
                            return 20000;
                        case 510: // recipes
                        case 511:
                        case 512:
                            return 10000;
                        default:
                            return 100000;

                    }
                case ETC:
                    switch (number) {
                        case 0: // monster pieces
                            return 400000;
                        case 4: // crystal ores
                        case 130: // simulators
                        case 131: // manuals
                            return 10000;
                        case 30: // game pieces
                            return 50000;
                        case 32: // misc items
                            return 250000;
                        default:
                            return 10000;
                    }
                default:
                    return 10000;
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

        public String getQuerySegment() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(monsterId);
            sb.append(", ");
            sb.append(itemId);
            sb.append(", ");
            sb.append(mindrop);
            sb.append(", ");
            sb.append(maxdrop);
            sb.append(", ");
            sb.append(quest); // Quest ID
            sb.append(", ");
            sb.append(chance);
            sb.append(")");
            return sb.toString();
        }
    }
}
