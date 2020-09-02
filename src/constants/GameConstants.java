package constants;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.MonsterStatus;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import java.awt.Point;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.Randomizer;
import server.maps.MapleMapObjectType;
import tools.FileoutputUtil;
import tools.Pair;
import tools.packet.CField;

public class GameConstants {

    public static boolean GMS = true; //true = GMS
    public static final List<MapleMapObjectType> rangedMapobjectTypes = Collections.unmodifiableList(Arrays.asList(
            MapleMapObjectType.ITEM,
            MapleMapObjectType.MONSTER,
            MapleMapObjectType.DOOR,
            MapleMapObjectType.REACTOR,
            MapleMapObjectType.SUMMON,
            MapleMapObjectType.NPC,
            MapleMapObjectType.MIST,
            MapleMapObjectType.FAMILIAR,
            MapleMapObjectType.EXTRACTOR));
    //private static final int[] exp = new int[201];
    private static final int[] exp = {0, 15, 34, 57, 92, 135, 372, 560, 840, 1242, 1716, 2360, 3216, 4200, 5460, 7050, 8840,
        11040, 13716, 16680, 20216, 24402, 28980, 34320, 40512, 47216, 54900, 63666, 73080, 83720, 95700, 108480,
        122760, 138666, 155540, 174216, 194832, 216600, 240500, 266682, 294216, 324240, 356916, 391160, 428280, 468450,
        510420, 555680, 604416, 655200, 709716, 748608, 789631, 832902, 878545, 926689, 977471, 1031036, 1087536,
        1147032, 1209994, 1276301, 1346242, 1420016, 1497832, 1579913, 1666492, 1757815, 1854143, 1955750, 2062925,
        2175973, 2295216, 2420993, 2553663, 2693603, 2841212, 2996910, 3161140, 3334370, 3517093, 3709829, 3913127,
        4127566, 4353756, 4592341, 4844001, 5109452, 5389449, 5684790, 5996316, 6324914, 6671519, 7037118, 7422752,
        7829518, 8258575, 8711144, 9188514, 9692044, 10223168, 10783397, 11374327, 11997640, 12655110, 13348610,
        14080113, 14851703, 15665576, 16524049, 17429566, 18384706, 19392187, 20454878, 21575805, 22758159, 24005306,
        25320796, 26708375, 28171993, 29715818, 31344244, 33061908, 34873700, 36784778, 38800583, 40926854, 43169645,
        45535341, 48030677, 50662758, 53439077, 56367538, 59456479, 62714694, 66151459, 69776558, 73600313, 77633610,
        81887931, 86375389, 91108760, 96101520, 101367883, 106992842, 112782213, 118962678, 125481832, 132358236,
        139611467, 147262175, 155332142, 163844343, 172823012, 182293713, 192283408, 202820538, 213935103, 225658746,
        238024845, 251068606, 264827165, 279339639, 294647508, 310794191, 327825712, 345790561, 364739883, 384727628,
        405810702, 428049128, 451506220, 476248760, 502347192, 529875818, 558913012, 589541445, 621848316, 655925603,
        691870326, 729784819, 769777027, 811960808, 856456260, 903390063, 952895838, 1005114529, 1060194805,
        1118293480, 1179575962, 1244216724, 1312399800, 1384319309, 1460180007, 1540197871, 1624600714, 1713628833,
        1807535693, 1906558648, 2011069705, 2121276324
    };
    private static final int[] closeness = {0, 1, 3, 6, 14, 31, 60, 108, 181, 287, 434, 632, 891, 1224, 1642, 2161, 2793,
        3557, 4467, 5542, 6801, 8263, 9950, 11882, 14084, 16578, 19391, 22547, 26074,
        30000};
    private static final int[] setScore = {0, 10, 100, 300, 600, 1000, 2000, 4000, 7000, 10000};
    private static final int[] cumulativeTraitExp = {0, 20, 46, 80, 124, 181, 255, 351, 476, 639, 851, 1084,
        1340, 1622, 1932, 2273, 2648, 3061, 3515, 4014, 4563, 5128,
        5710, 6309, 6926, 7562, 8217, 8892, 9587, 10303, 11040, 11788,
        12547, 13307, 14089, 14883, 15689, 16507, 17337, 18179, 19034, 19902,
        20783, 21677, 22584, 23505, 24440, 25399, 26362, 27339, 28331, 29338,
        30360, 31397, 32450, 33519, 34604, 35705, 36823, 37958, 39110, 40279,
        41466, 32671, 43894, 45135, 46395, 47674, 48972, 50289, 51626, 52967,
        54312, 55661, 57014, 58371, 59732, 61097, 62466, 63839, 65216, 66597,
        67982, 69371, 70764, 72161, 73562, 74967, 76376, 77789, 79206, 80627,
        82052, 83481, 84914, 86351, 87792, 89237, 90686, 92139, 93596, 96000};
    private static final int[] mobHpVal = {0, 15, 20, 25, 35, 50, 65, 80, 95, 110, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350,
        375, 405, 435, 465, 495, 525, 580, 650, 720, 790, 900, 990, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800,
        1900, 2000, 2100, 2200, 2300, 2400, 2520, 2640, 2760, 2880, 3000, 3200, 3400, 3600, 3800, 4000, 4300, 4600, 4900, 5200,
        5500, 5900, 6300, 6700, 7100, 7500, 8000, 8500, 9000, 9500, 10000, 11000, 12000, 13000, 14000, 15000, 17000, 19000, 21000, 23000,
        25000, 27000, 29000, 31000, 33000, 35000, 37000, 39000, 41000, 43000, 45000, 47000, 49000, 51000, 53000, 55000, 57000, 59000, 61000, 63000,
        65000, 67000, 69000, 71000, 73000, 75000, 77000, 79000, 81000, 83000, 85000, 89000, 91000, 93000, 95000, 97000, 99000, 101000, 103000,
        105000, 107000, 109000, 111000, 113000, 115000, 118000, 120000, 125000, 130000, 135000, 140000, 145000, 150000, 155000, 160000, 165000, 170000, 175000, 180000,
        185000, 190000, 195000, 200000, 205000, 210000, 215000, 220000, 225000, 230000, 235000, 240000, 250000, 260000, 270000, 280000, 290000, 300000, 310000, 320000,
        330000, 340000, 350000, 360000, 370000, 380000, 390000, 400000, 410000, 420000, 430000, 440000, 450000, 460000, 470000, 480000, 490000, 500000, 510000, 520000,
        530000, 550000, 570000, 590000, 610000, 630000, 650000, 670000, 690000, 710000, 730000, 750000, 770000, 790000, 810000, 830000, 850000, 870000, 890000, 910000};
    private static final int[] pvpExp = {0, 3000, 6000, 12000, 24000, 48000, 960000, 192000, 384000, 768000};
    private static final int[] guildexp = {0, 20000, 160000, 540000, 1280000, 2500000, 4320000, 6860000, 10240000, 14580000};
    private static final int[] mountexp = {0, 6, 25, 50, 105, 134, 196, 254, 263, 315, 367, 430, 543, 587, 679, 725, 897, 1146, 1394, 1701, 2247,
        2543, 2898, 3156, 3313, 3584, 3923, 4150, 4305, 4550};
    public static final int[] itemBlock = {4001168, 5220013, 3993003, 2340000, 2049100, 4001129, 2040037, 2040006, 2040007, 2040303, 2040403, 2040506, 2040507, 2040603, 2040709, 2040710, 2040711, 2040806, 2040903, 2041024, 2041025, 2043003, 2043103, 2043203, 2043303, 2043703, 2043803, 2044003, 2044103, 2044203, 2044303, 2044403, 2044503, 2044603, 2044908, 2044815, 2044019, 2044703};
    public static final int[] cashBlock = {5062000, 5062001, 5062002, 5062003, 5062005, 5062500, 5610000, 5610001, 5640000, 2531000, 2530000,
        5534000, 5521000, 5062200, 5062201, 5133000, 5520001, 5030000, 5030001, 5030006,
        5470000, 1122121, 5155000, 5062400, 5700000, 1112909, 5450005, 5040004, 5220000, 5062000,
        5062001, 5062002, 5062003, 5211046, 5360000, 5051001, 5590000};
    public static final int[] rankC = {70000000, 70000001, 70000002, 70000003, 70000004, 70000005, 70000006, 70000007, 70000008, 70000009, 70000010, 70000011, 70000012, 70000013};
    public static final int[] rankB = {70000014, 70000015, 70000016, 70000017, 70000018, 70000021, 70000022, 70000023, 70000024, 70000025, 70000026};
    public static final int[] rankA = {70000027, 70000028, 70000029, 70000030, 70000031, 70000032, 70000033, 70000034, 70000035, 70000036, 70000039, 70000040, 70000041, 70000042};
    public static final int[] rankS = {70000043, 70000044, 70000045, 70000047, 70000048, 70000049, 70000050, 70000051, 70000052, 70000053, 70000054, 70000055, 70000056, 70000057, 70000058, 70000059, 70000060, 70000061, 70000062};
    public static final int[] circulators = {2700000, 2700100, 2700200, 2700300, 2700400, 2700500, 2700600, 2700700, 2700800, 2700900, 2701000};
    public static final int JAIL = 180000001, MAX_BUFFSTAT = 4, MAX_MONSTERSTATUS = 4;
    public static final int[] blockedSkills = {4341003};
    public static final String[] RESERVED = {"Apocryphal", "MechAviv"};
    public static final String[] stats = {"tuc", "reqLevel", "reqJob", "reqSTR", "reqDEX", "reqINT", "reqLUK", "reqPOP", "cash", "cursed", "success", "setItemID", "equipTradeBlock", "durability", "randOption", "randStat", "masterLevel", "reqSkillLevel", "elemDefault", "incRMAS", "incRMAF", "incRMAI", "incRMAL", "canLevel", "skill", "charmEXP"};
    public static final int[] hyperTele = {10000, 20000, 30000, 40000, 50000, 1000000, 1010000, 1020000, 2000000, //Maple Island
        104000000, 104010000, 104010100, 104010200, 104020000, 103010100, 103010000, 103000000, 103050000, 103020000, 103020020, 103020100, 103020200, 103020300, 103020310, 103020320, 103020400, 103020410, 103020420, 103030000, 103030100, 103030200, 103030300, 103030400, 102000000, 102010000, 102010100, 102020000, 102020100, 102020200, 102020300, 102020400, 102020500, 102040000, 102040100, 102040200, 102040300, 102040400, 102040500, 102040600, 102030000, 102030100, 102030200, 102030300, 102030400, 101000000, 101010000, 101010100, 101020000, 101020100, 101020200, 101020300, 101030000, 101030100, 101030200, 101030300, 101030400, 101030500, 101030101, 101030201, 101040000, 101040100, 101040200, 101040300, 101040310, 101040320, 101050000, 101050400, 100000000, 100010000, 100010100, 100020000, 100020100, 100020200, 100020300, 100020400, 100020500, 100020401, 100020301, 100040000, 100040100, 100040200, 100040300, 100040400, 100020101, 106020000, 120010100, 120010000, 120000000, 120020000, 120020100, 120020200, 120020300, 120020400, 120020500, 120020600, 120020700, 120030000, 120030100, 120030200, 120030300, 120030400, 120030500, //Victoria Island
        105000000, 105010000, 105010100, 105020000, 105020100, 105020200, 105020300, 105020400, 105020500, 105030000, 105030100, 105030200, 105030300, 105030400, 105030500, 105100000, 105100100, //Sleepy Wood
        120000100, 120000101, 120000102, 120000103, 120000104, 120000201, 120000202, 120000301, //Nautilus
        103040000, 103040100, 103040101, 103040102, 103040103, 103040200, 103040201, 103040202, 103040203, 103040300, 103040301, 103040302, 103040303, 103040400, //Kerning Square
        200000000, 200010000, 200010100, 200010110, 200010120, 200010130, 200010111, 200010121, 200010131, 200010200, 200010300, 200010301, 200010302, 200020000, 200030000, 200040000, 200050000, 200060000, 200070000, 200080000, 200000100, 200000200, 200000300, 200100000, 200080100, 200080200, 200081500, 200082200, 200082300, 211000000, 211000100, 211000200, 211010000, 211020000, 211030000, 211040000, 211050000, 211040100, 211040200, 921120000, //Orbis
        211040300, 211040400, 211040500, 211040600, 211040700, 211040800, 211040900, 211041000, 211041100, 211041200, 211041300, 211041400, 211041500, 211041600, 211041700, 211041800, 211041900, 211042000, 211042100, 211042200, 211042300, 211042400, 280030000, 211060000, //Dead Mine
        211060010, 211060100, 211060200, 211060201, 211060300, 211060400, 211060401, 211060410, 211060500, 211060600, 211060601, 211060610, 211060620, 211060700, 211060800, 211060801, 211060810, 211060820, 211060830, 211060900, 211061000, 211061001, 211070000, //Lion King's Castle
        220000000, 220000100, 220000300, 220000400, 220000500, 220010000, 220010100, 220010200, 220010300, 220010400, 220010500, 220010600, 220010700, 220010800, 220010900, 220011000, 220020000, 220020100, 220020200, 220020300, 220020400, 220020500, 220020600, 220030100, 220030200, 220030300, 220030400, 220030000, 220040000, 220040100, 220040200, 220040300, 220040400, 220050000, 220050100, 220050200, 221023200, 221022300, 221022200, 221021700, 221021600, 221021100, 221020000, 221000000, 221030000, 221030100, 221030200, 221030300, 221030400, 221030500, 221030600, 221040000, 221040100, 221040200, 221040300, 221040400, 222000000, 222010000, 222010001, 222010002, 222010100, 222010101, 222010102, 222010200, 222010201, 222010300, 222010400, 222020300, 222020200, 222020100, 222020000, //Ludas Lake
        220050300, 220060000, 220060100, 220060200, 220060300, 220060400, 220070000, 220070100, 220070200, 220070300, 220070400, 220080000, 220080001, //Clock Tower Lower Floor
        300000100, 300000000, 300010000, 300010100, 300010200, 300010400, 300020000, 300020100, 300020200, 300030000, 300030100, 300010410, 300020210, 300030200, 300030300, 300030310, //Ellin Forest
        230010000, 230010100, 230010200, 230010201, 230010300, 230010400, 230020000, 230020100, 230020200, 230020201, 230020300, 230030000, 230030100, 230030101, 230030200, 230040000, 230040100, 230040200, 230040300, 230040400, 230040410, 230040420, 230000000, //Aqua Road
        250000000, 250000100, 250010000, 250010100, 250010200, 250010300, 250010301, 250010302, 250010303, 250010304, 250010400, 250010500, 250010501, 250010502, 250010503, 250010600, 250010700, 250020000, 250020100, 250020200, 250020300, 251000000, 251000100, 251010000, 251010200, 251010300, 251010400, 251010401, 251010402, 251010403, 251010500, //Mu Lung Garden
        240010100, 240010200, 240010300, 240010400, 240010500, 240010600, 240010700, 240010800, 240010900, 240011000, 240020000, 240020100, 240020101, 240020200, 240020300, 240020400, 240020401, 240020500, 240030000, 240030100, 240030101, 240030102, 240030200, 240030300, 240040000, 240040100, 240040200, 240040300, 240040400, 240040500, 240040510, 240040511, 240040520, 240040521, 240040600, 240040700, 240050000, 240010000, 240000000, //Minar Forest
        240070000, 240070010, 240070100, 240070200, 240070300, 240070400, 240070500, 240070600, //Neo City
        260010000, 260010100, 260010200, 260010300, 260010400, 260010500, 260010600, 260010700, 260020000, 260020100, 260020200, 260020300, 260020400, 260020500, 260020600, 260020610, 260020620, 260020700, 261000000, 260000000, 926010000, 261010000, 261010001, 261010002, 261010003, 261010100, 261010101, 261010102, 261010103, 261020000, 261020100, 261020200, 261020300, 261020400, 261020500, 261020600, 261020700, 260000300, 260000200, //Nihal Desert
        270000000, 270000100, 270010000, 270010100, 270010110, 270010111, 270010200, 270010210, 270010300, 270010310, 270010400, 270010500, 270020000, 270020100, 270020200, 270020210, 270020211, 270020300, 270020310, 270020400, 270020410, 270020500, 270030000, 270030100, 270030110, 270030200, 270030210, 270030300, 270030310, 270030400, 270030410, 270030411, 270030500, 270040000, 270050000, //Temple of Time
        271000000, 271000100, 271000200, 271000210, 271000300, 271020000, 271020100, 271010000, 271010100, 271010200, 271010300, 271010301, 271010400, 271010500, 271030000, 271030100, 271030101, 271030102, 271030200, 271030201, 271030300, 271030310, 271030320, 271030400, 271030410, 271030500, 271030510, 271030520, 271030530, 271030540, 271030600, 271040000, 271040100, //Gate of Future
        130000000, 130000100, 130000110, 130000120, 130000200, 130000210, 130010000, 130010010, 130010020, 130010100, 130010110, 130010120, 130010200, 130010210, 130010220, 130020000, 130030005, 130030006, 130030000, //Ereve
        140000000, 140010000, 140010100, 140010200, 140020000, 140020100, 140020200, 140030000, 140090000, 140020300, //Rien
        310000000, 310000010, 310020000, 310020100, 310020200, 310030000, 310030100, 310030110, 310030200, 310030300, 310030310, 310040000, 310040100, 310040110, 310040200, 310040300, 310040400, 310050000, 310050100, 310050200, 310050300, 310050400, 310050500, 310050510, 310050520, 310050600, 310050700, 310050800, 310060000, 310060100, 310060110, 310060120, 310060200, 310060210, 310060220, 310060300, 310010000, //Edelstein
        600000000, 600010100, 600010200, 600010300, 600010400, 600010500, 600010600, 600010700, 600020000, 600020100, 600020200, 600020300, 600020400, 600020500, 600020600, 682000000, 610010000, 610010001, 610010002, 610010004, 610020000, 610020001, 610020006, 610040000, 610040100, 610040200, 610040210, 610040220, 610040230, 610040400//Masteria
    };
    public static final int[] unusedNpcs = {9201142, 9201254, 9201030, 9010037, 9010038, 9010039, 9010040, 9300010, 9070004, 9070006, 9000017, 2041017, 9270075, 9000069, 9201029, 9130024, 9330072, 9133080, 9201152, 9330189, 9000154, 9000131, 9000132};
    //Unused npcs will be removed from map once you enter it.

    /*public static void LoadEXP() {
     exp[2] = 34;
     exp[3] = 57;
     exp[4] = 92;
     exp[5] = 135;
     int v0 = 15;
     exp[1] = 15;
     exp[6] = 372;
     exp[7] = 560;
     exp[8] = 840;
     exp[9] = 1242;
     int v1 = exp[9];
     exp[10] = v1;
     exp[11] = v1;
     exp[12] = v1;
     exp[13] = v1;
     exp[14] = v1;
     do {
     exp[v0] = (int) (exp[v0 - 1] * 1.2);
     ++v0;
     } while (v0 <= 29);
     int v2 = exp[29];
     exp[30] = v2;
     exp[31] = v2;
     exp[32] = v2;
     exp[33] = v2;
     exp[34] = v2;
     int v3 = 35;
     do {
     exp[v3] = (int) (exp[v3 - 1] * 1.2);
     ++v3;
     } while (v3 <= 39);
     int v4 = 40;
     do {
     exp[v4] = (int) (exp[v4 - 1] * 1.08);
     ++v4;
     } while (v4 <= 59);
     int v5 = exp[59];
     exp[60] = v5;
     exp[61] = v5;
     exp[62] = v5;
     exp[63] = v5;
     exp[64] = v5;
     int v6 = 65;
     do {
     exp[v6] = (int) (exp[v6 - 1] * 1.08);
     ++v6;
     } while (v6 <= 74);
     int v7 = 75;
     do {
     exp[v7] = (int) (exp[v7 - 1] * 1.07);
     ++v7;
     } while (v7 <= 99);
     int v8 = exp[99];
     exp[100] = v8;
     exp[101] = v8;
     exp[102] = v8;
     exp[103] = v8;
     exp[104] = v8;
     int v9 = 105;
     do {
     exp[v9] = (int) (exp[v9 - 1] * 1.07);
     ++v9;
     } while (v9 <= 159);
     int v10 = 160;
     do {
     exp[v10] = (int) (exp[v10 - 1] * 1.06);
     ++v10;
     } while (v10 <= 199);
     exp[200] = 0;//exp[199] * 2;
     //int v11 = 201;
     // do {
     //      exp[v11] = (int) (exp[v11 - 1] * 1.2);
     //      ++v11;
     //  } while (v11 <= 249);
     //  exp[250] = 0;
     }*/
    public static int getExpNeededForLevel(final int level) {
        if (level < 1 || level >= exp.length) {
            return Integer.MAX_VALUE;
        }
        return exp[level];
    }

    public static int getGuildExpNeededForLevel(final int level) {
        if (level < 0 || level >= guildexp.length) {
            return Integer.MAX_VALUE;
        }
        return guildexp[level];
    }

    public static int getPVPExpNeededForLevel(final int level) {
        if (level < 0 || level >= pvpExp.length) {
            return Integer.MAX_VALUE;
        }
        return pvpExp[level];
    }

    public static int getClosenessNeededForLevel(final int level) {
        return closeness[level - 1];
    }

    public static int getMountExpNeededForLevel(final int level) {
        return mountexp[level - 1];
    }

    public static int getTraitExpNeededForLevel(final int level) {
        if (level < 0 || level >= cumulativeTraitExp.length) {
            return Integer.MAX_VALUE;
        }
        return cumulativeTraitExp[level];
    }

    public static int getSetExpNeededForLevel(final int level) {
        if (level < 0 || level >= setScore.length) {
            return Integer.MAX_VALUE;
        }
        return setScore[level];
    }

    public static int getMonsterHP(final int level) {
        if (level < 0 || level >= mobHpVal.length) {
            return Integer.MAX_VALUE;
        }
        return mobHpVal[level];
    }

    public static int getBookLevel(final int level) {
        return (5 * level) * (level + 1);
    }

    public static int getTimelessRequiredEXP(final int level) {
        return 70 + (level * 10);
    }

    public static int getReverseRequiredEXP(final int level) {
        return 60 + (level * 5);
    }

    public static int getProfessionEXP(final int level) {
        return ((100 * level * level) + (level * 400)) / 2;
    }

    public static boolean isHarvesting(final int itemId) {
        return itemId >= 1500000 && itemId < 1520000;
    }

    public static int maxViewRangeSq() {
        return Integer.MAX_VALUE;
    }

    public static int maxViewRangeSq_Half() {
        return Integer.MAX_VALUE;
    }

    public static boolean isJobFamily(final int baseJob, final int currentJob) {
        return currentJob >= baseJob && currentJob / 100 == baseJob / 100;
    }

    public static short getBeginnerJob(final short job) {
        if (job % 1000 < 10) {
            return job;
        }
        switch (job / 100) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 0;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return 1000;
            case 20:
                return 2000;
            case 21:
                return 2000;
            case 22:
                return 2001;
            case 23:
                return 2002;
            case 24:
                return 2003;
            case 27:
                return 2004;
            case 31:
                return 3001;
            case 36:
                return 3002;
            case 30:
            case 32:
            case 33:
            case 35:
                return 3000;
            case 41:
                return 4001;
            case 42:
                return 4002;
            case 50:
            case 51:
                return 5000;
            case 60:
            case 61:
                return 6000;
            case 65:
                return 6001;
            case 100:
            case 110:
                return 10000;
        }
        return 0;
    }

    public static boolean isKOC(final int job) {
        return job >= 1000 && job < 2000;
    }

    public static boolean isEvan(final int job) {
        return job == 2001 || (job / 100 == 22);
    }

    public static boolean isMercedes(final int job) {
        return job == 2002 || (job / 100 == 23);
    }

    public static boolean isJett(final int job) {
        return job == 508 || (job / 10 == 57);
    }

    public static boolean isPhantom(final int job) {
        return job == 2003 || (job / 100 == 24);
    }

    public static boolean isWildHunter(final int job) {
        return job == 3000 || (job >= 3300 && job <= 3312);
    }

    public static boolean isDawnWarrior(final int job) {
        return job == 1000 || (job >= 1100 && job <= 1112);
    }
//    public static boolean isSeparatedSp(int job) {
////        System.err.println(job);
////        System.err.println(isSeparatedSp(job));
//        int jobGrade = getTrueJobGrade(job);
//        return job / 1000 == 0 || //new explorers revamped RED
//                job / 1000 == 1 // cygnus
//                && (jobGrade == 0 || jobGrade == 5 || jobGrade == 3 || jobGrade == 1)// thunder breaker wind archer and dawn warrior
//                || job / 1000 == 3 // resistance
//                || job / 100 == 22 // evan
//                || job == 2001 // evan
//                || isMercedes(job)
//                || isPhantom(job)
//                || isMihile(job)
//                || isLuminous(job)
//                || isNova(job)
//                || isZero(job)
//                || isJett(job)
//                || isSengoku(job);
//    }

    public static boolean isSeparatedSp(int job) {
        //return isAdventurer(job) || isZero(job) || isKOC(job) || isEvan(job) || isResistance(job) || isMercedes(job) || isJett(job) || isPhantom(job) || isMihile(job) || isNova(job) || isAngelicBuster(job) || isKaiser(job) || isLuminous(job) || isHayato(job) || isKanna(job) || isDemonAvenger(job);
        if (isKOC(job)) {
            if (getTrueJobGrade(job) == 2 || getTrueJobGrade(job) == 4) {
                return false;
            }
        }
        if (isAran(job)) {
            return false;
        }
        if (isBeastTamer(job)) {
            return false; // not sure
        }
        return true;
    }

    public static int getTrueJobGrade(int job) {
        int result;
        int jobGrade = job % 1000 / 100;
        if (job / 100 == 27) {
            jobGrade = 2;
        }
        result = 4;
        if (job / 100 != 36) {
            result = jobGrade;
        }
        return result;
    }

    public static boolean isDualBladeNoSP(int job) {
        return job == 430 ? true : job == 432;
    }

    public static boolean isDemonSlayer(final int job) {
        return job == 3001 || (job >= 3100 && job <= 3112 && job != 3101);
    }

    public static boolean isAran(final int job) {
        return job >= 2000 && job <= 2112 && job != 2001 && job != 2002 && job != 2003;
    }

    public static boolean isResistance(final int job) {
        return job / 1000 == 3;
    }

    public static boolean isAdventurer(final int job) {
        return job >= 0 && job < 1000;
    }

    public static boolean isCannon(final int job) {
        return job == 1 || job == 501 || (job >= 530 && job <= 532);
    }

    public static boolean isDualBlade(final int job) {
        return (job >= 430 && job <= 434);
    }

    public static boolean isMihile(final int job) {
        return job == 5000 || (job >= 5100 && job <= 5112);
    }

    public static boolean isLuminous(final int job) {
        return job == 2004 || (job >= 2700 && job <= 2712);
    }

    public static boolean isKaiser(final int job) {
        return job == 6000 || (job >= 6100 && job <= 6112);
    }

    public static boolean isAngelicBuster(final int job) {
        return job == 6001 || (job >= 6500 && job <= 6512);
    }

    public static boolean isNova(final int job) {
        return job / 1000 == 6;
    }

    public static boolean isXenon(final int job) {
        return job == 3002 || (job >= 3600 && job <= 3612);
    }

    public static boolean isHayato(int job) {
        return job == 4001 || (job >= 4100 && job <= 4112);
    }

    public static boolean isKanna(int job) {
        return job == 4002 || (job >= 4200 && job <= 4212);
    }

    public static boolean isSengoku(int job) {
        return job / 1000 == 4;
    }

    public static boolean isDemonAvenger(int job) {
        return job == 3001 || job == 3101 || (job >= 3120 && job <= 3122);
    }

    public static boolean isZero(int job) {
        return job == 10000 || (job >= 10100 && job <= 10112);
    }

    public static boolean isBeastTamer(final int job) {
        return job == 11212;//gms like for now
        //return job == 11000 || (job >= 11200 && job <= 11212);
    }

    public static boolean isExceedAttack(int id) {
        switch (id) {
            case 31011000:
            case 31011004:
            case 31011005:
            case 31011006:
            case 31011007:
            case 31201000:
            case 31201007:
            case 31201008:
            case 31201009:
            case 31201010:
            case 31211000:
            case 31211007:
            case 31211008:
            case 31211009:
            case 31211010:
            case 31221000:
            case 31221009:
            case 31221010:
            case 31221011:
            case 31221012:
                return true;
        }
        return false;
    }

    public static int getLuminousSkillMode(int id) {
        switch (id) {
            case 27001100:
            case 27101100:
            case 27111100:
            case 27111101:
            case 27121100:
                return 20040216;//light
            case 27001201:
            case 27101202:
            case 27111202:
            case 27121201:
            case 27121202:
            case 27120211:
                return 20040217;//dark
            case 27111303:
            case 27121303:
                return 20040220;
        }
        return 0;
    }

    public static int getLinkSkillByJob(final int job) {
        if (isCannon(job)) { //Pirate Blessing
            return 80000000;
        } else if (isKOC(job)) { //Cygnus Blessing
            return 80000070;
        } else if (isMercedes(job)) { //Elven Blessing
            return 80001040;
        } else if (isDemonSlayer(job)) { //Fury Unleashed
            return 80000001;
        } else if (isDemonAvenger(job)) { //Wild Rage
            return 80000050;
        } else if (isJett(job)) { //Core Aura
            return 80001151;
        } else if (isPhantom(job)) { //Phantom Instinct
            return 80000002;
        } else if (isMihile(job)) { //Knight's Watch
            return 80001140;
        } else if (isLuminous(job)) { //Light Wash
            return 80000005;
        } else if (isAngelicBuster(job)) { //Terms and Conditions   
            return 80001155;
        } else if (isHayato(job)) { //Keen Edge  
            return 80000003;
        } else if (isKanna(job)) { //Elementalism    
            return 40020002;
        } else if (isKaiser(job)) { //Iron Will
            return 80000006;
        } else if (isXenon(job)) { //Hybrid Logic    
            return 80000047;
        } else if (isBeastTamer(job)) { //Focus Spirit    
            return 80010006;
        }
        return 0;
    }

    public static boolean isRecoveryIncSkill(final int id) {
        switch (id) {
            case 1110000:
            case 2000000:
            case 1210000:
            case 11110000:
            case 4100002:
            case 4200001:
                return true;
        }
        return false;
    }

    public static boolean isLinkedAttackSkill(final int id) {
        return getLinkedAttackSkill(id) != id;
    }

    public static int getLinkedAttackSkill(final int id) {
        switch (id) {
            case 21110007:
            case 21110008:
                return 21110002;
            case 21120009:
            case 21120010:
                return 21120002;
            case 4321001:
                return 4321000;
            case 33101006:
            case 33101007:
                return 33101005;
            case 33101008:
                return 33101004;
            case 35101009:
            case 35101010:
                return 35100008;
            case 35111009:
            case 35111010:
                return 35111001;
            case 35121013:
                return 35111004;
            case 35121011:
                return 35121009;
            case 32001007:
            case 32001008:
            case 32001009:
            case 32001010:
            case 32001011:
                return 32001001;
            case 5300007:
                return 5301001;
            case 5320011:
                return 5321004;
            case 23101007:
                return 23101001;
            case 23111010:
            case 23111009:
                return 23111008;
            case 31001006:
            case 31001007:
            case 31001008:
                return 31000004;
            case 30010183:
            case 30010184:
            case 30010186:
                return 30010110;
            case 5710012:
                return 5711002;
            case 31121010:
                return 31121000;
            case 5211015:
            case 5211016:
                return 5211011;
            case 24111008:
                return 24111006;
            case 24121010:
                return 24121003;
            case 5001008:
                return 5200010;
            case 5001009:
                return 5101004;
            case 41001005:
            case 41001004:
                return 41001000;
            case 41101009:
            case 41101008:
                return 41101000;
            case 41111012:
            case 41111011:
                return 41111000;
            case 41120013:
            case 41120012:
            case 41120011:
                return 41121000;
            case 42001006:
            case 42001005:
                return 42001000;
            case 42001007:
                return 42001002;
            case 42100010:
                return 42101001;
            //Zero:
            case 101000102:
                return 101000101;
            case 101000202:
                return 101000201;
            case 101100202:
                return 101100201;
            case 101110201:
                return 101110200;
            case 101110204:
                return 101110203;
            case 101120101:
                return 101120100;
            case 101120103:
                return 101120102;
            case 101120105:
            case 101120106:
                return 101120104;
            case 101120203:
                return 101120202;
            case 101120205:
            case 101120206:
                return 101120204;
            case 101120200:
                return 101121200;
            case 112000001:
            case 112000002:
                return 112000000;
            case 112120001:
            case 112120002:
            case 112120003:
                return 112120000;
        }
        return id;
    }

    public static final int getLinkedBuffSkill(final int id) {
        switch (id) {
            case 61120008:
                return 61111008;
            case 11121011:
            case 11121012:
                return 11121005;
        }
        return id;
    }

    public static final int getLinkedAranSkill(final int id) {
        switch (id) {
            case 21110007:
            case 21110008:
                return 21110002;
            case 21120009:
            case 21120010:
                return 21120002;
            case 4321001:
                return 4321000;
            case 33101006:
            case 33101007:
                return 33101005;
            case 33101008:
                return 33101004;
            case 35101009:
            case 35101010:
                return 35100008;
            case 35121013:
                return 35111004;
            case 35121011:
                return 35121009;
            case 35111009:
            case 35111010:
                return 35111001;
            case 35100004:
                return 35101004;
            case 35001001:
                return 35101009;
            case 32001007:
            case 32001008:
            case 32001009:
            case 32001010:
            case 32001011:
                return 32001001;
            case 5300007:
                return 5301001;
            case 5320011:
                return 5321004;
            case 23101007:
                return 23101001;
            case 23111010:
            case 23111009:
                return 23111008;
            case 31001006:
            case 31001007:
            case 31001008:
                return 31000004;
            case 27120211:
                return 27121201;
            case 61001004:
            case 61001005:
            case 61110212:
            case 61120219:
                return 61001000;
            case 61110211:
            case 61120007:
            case 61121217:
                return 61101002;
            case 61111113:
                return 61111100;
            case 61121201:
                return 61121100;
            case 61121203:
                return 61121102;
            case 61110009:
                return 61111003;
            case 61121116:
                return 61121104;
            case 65101006:
                return 65101106;
            case 65121007:
            case 65121008:
                return 65121101;
            case 65111007:
                return 65111100;
            case 24121010:
                return 24121003;
            case 24111008:
                return 24111006;
            case 5001008:
                return 5001005;
            case 61120008:
                return 61111008;
            case 51100006:
                return 51101006;
            case 31011004:
            case 31011005:
            case 31011006:
            case 31011007:
                return 31011000;
            case 31201007:
            case 31201008:
            case 31201009:
            case 31201010:
                return 31201000;
            case 31211007:
            case 31211008:
            case 31211009:
            case 31211010:
                return 31211000;
            case 31221009:
            case 31221010:
            case 31221011:
            case 31221012:
                return 31221000;
            case 31211011:
                return 31211002;
            case 31221014:
                return 31221001;
            case 36101008:
            case 36101009:
                return 36101000;
            case 36111009:
            case 36111010:
                return 36111000;
            case 36121011:
            case 36121012:
                return 36121001;
            case 35100009:
                return 35100009;
            case 2121055:
                return 2121052;
            case 11121055:
                return 11121052;
        }
        return id;
    }

    public static boolean isSaintSaverSkill(int skill) {
        switch (skill) {
            case 80001034:
            case 80001035:
            case 80001036:
                return true;
        }
        return false;
    }

    public final static boolean isForceIncrease(int skillid) {
        switch (skillid) {
            case 24100003:
            case 24120002:
            case 31000004:
            case 31001006:
            case 31001007:
            case 31001008:

            case 30010166:
            case 30011167:
            case 30011168:
            case 30011169:
            case 30011170:
                return true;
        }
        return false;
    }

    public static int findSkillByName(String name, int job, int def) {
        int skillid = 0;
        for (Skill skill : SkillFactory.getAllSkills()) {
            if (skill.getName() != null && skill.getName().toLowerCase().contains(name.toLowerCase())) {
                if (skill.getId() / 10000 == job) {
                    skillid = skill.getId();
                }
            }
        }
        if (skillid != 0) {
            return skillid;
        } else {
            return def;
        }
    }

    public static int getBOF_ForJob(final int job) {
        return PlayerStats.getSkillByJob(12, job);
    }

    public static int getEmpress_ForJob(final int job) {
        return PlayerStats.getSkillByJob(73, job);
    }

    public static boolean isElementAmp_Skill(final int skill) {
        switch (skill) {
            case 2110001:
            case 2210001:
            case 12110001:
            case 22150000:
                return true;
        }
        return false;
    }

    public static int getMPEaterForJob(final int job) {
        switch (job) {
            case 210:
            case 211:
            case 212:
                return 2100000;
            case 220:
            case 221:
            case 222:
                return 2200000;
            case 230:
            case 231:
            case 232:
                return 2300000;
        }
        return 2100000; // Default, in case GM
    }

    public static int getJobShortValue(int job) {
        if (job >= 1000) {
            job -= (job / 1000) * 1000;
        }
        job /= 100;
        if (job == 4) { // For some reason dagger/ claw is 8.. IDK
            job *= 2;
        } else if (job == 3) {
            job += 1;
        } else if (job == 5) {
            job += 11; // 16
        }
        return job;
    }

    public static boolean isPyramidSkill(final int skill) {
        return isBeginnerJob(skill / 10000) && skill % 10000 == 1020;
    }

    public static boolean isInflationSkill(final int skill) {
        return isBeginnerJob(skill / 10000) && skill % 10000 == 1092;
    }

    public static boolean isMulungSkill(final int skill) {
        return isBeginnerJob(skill / 10000) && (skill % 10000 == 1009 || skill % 10000 == 1010 || skill % 10000 == 1011);
    }

    public static boolean isIceKnightSkill(final int skill) {
        return isBeginnerJob(skill / 10000) && (skill % 10000 == 1098 || skill % 10000 == 99 || skill % 10000 == 100 || skill % 10000 == 103 || skill % 10000 == 104 || skill % 10000 == 1105);
    }

    public static boolean isTownSkill(final int skill) {
        switch (skill) {
            case 1281: //maple island
            case 10001245: //ereve
            case 20021110: //elluel
            case 20031203: //lumiere
            case 30021235: //veritas
            case 60011220: //pantheon
            case 100001262: //zero temple
            case 110001514: //arboren ferry
                return true;
        }
        return false;
    }

    public static boolean isThrowingStar(final int itemId) {
        return itemId / 10000 == 207;
    }

    public static boolean isBullet(final int itemId) {
        return itemId / 10000 == 233;
    }

    public static boolean isRechargable(final int itemId) {
        return isThrowingStar(itemId) || isBullet(itemId);
    }

    public static boolean isOverall(final int itemId) {
        return itemId / 10000 == 105;
    }

    public static boolean isPet(final int itemId) {
        return itemId / 10000 == 500;
    }

    public static boolean isPets(final int itemId) {
        switch (itemId) {
            case 5000000:
            case 5000001:
            case 5000002:
            case 5000003:
            case 5000004:
            case 5000005:
            case 5000006:
            case 5000007:
            case 5000008:
            case 5000009:
            case 5000010:
            case 5000011:
            case 5000012:
            case 5000013:
            case 5000014:
            case 5000015:
            case 5000016:
            case 5000017:
            case 5000018:
            case 5000020:
            case 5000021:
            case 5000022:
            case 5000023:
            case 5000024:
            case 5000025:
            case 5000026:
            case 5000027:
            case 5000028:
            case 5000029:
            case 5000030:
            case 5000031:
            case 5000032:
            case 5000033:
            case 5000035:
            case 5000036:
            case 5000040:
            case 5000041:
            case 5000042:
            case 5000043:
            case 5000046:
            case 5000047:
            case 5000048:
            case 5000049:
            case 5000050:
            case 5000051:
            case 5000052:
            case 5000053:
            case 5000054:
            case 5000055:
            case 5000056:
            case 5000060:
            case 5000066:
                return true;
        }
        return false;
    }

    public static boolean isArrowForCrossBow(final int itemId) {
        return itemId >= 2061000 && itemId < 2062000;
    }

    public static boolean isArrowForBow(final int itemId) {
        return itemId >= 2060000 && itemId < 2061000;
    }

    public static boolean isMagicWeapon(final int itemId) {
        final int s = itemId / 10000;
        return s == 137 || s == 138; // wand, staff
    }

    public static boolean isWeapon(final int itemId) {
        return itemId >= 1210000 && itemId < 1600000;
    }

    public static boolean isSecondaryWeapon(final int itemId) {
        return itemId / 10000 == 135;
    }

    public static boolean isBowgun(final int itemId) {
        return itemId >= 1522000 && itemId < 1523000;
    }

    public static boolean isCane(final int itemId) {
        return itemId >= 1362000 && itemId < 1363000;
    }

    public static boolean isMagicArrow(final int itemId) {
        return itemId >= 1352000 && itemId < 1352100;
    }

    public static boolean isCard(final int itemId) {
        return itemId >= 1352100 && itemId < 1352200;
    }

    public static boolean isCore(final int itemId) {
        return itemId >= 1352300 && itemId < 1352400;
    }

    public static boolean isGMEquip(final int itemId) {
        switch (itemId) {
            case 1042003:
            case 1062007:
            case 1002140:
            case 1003142:
            case 1322013:
            case 1002959:
                return true;
        }
        return false;
    }

    public static boolean isOverPoweredEquip(final MapleClient c, final int itemId, short slot) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        return source.getAcc() > 600
                || source.getAvoid() > 600
                || source.getDex() > 200
                || source.getEnhance() > 14
                || source.getHands() > 100
                || source.getHp() > 1000
                || source.getInt() > 200
                || source.getJump() > 100
                || source.getSpeed() > 100
                || source.getLuk() > 200
                || source.getMatk() > 500
                || source.getMdef() > 1500
                || source.getMp() > 1000
                || source.getStr() > 200
                || source.getUpgradeSlots() > 32
                || source.getViciousHammer() > 2
                || source.getWatk() > 500
                || source.getWdef() > 1500 /*|| source.getLevel() > 32*/;
    }

    public static boolean isMadeByGM(final MapleClient c, final int itemId, short slot) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        MapleCharacter gm = c.getChannelServer().getPlayerStorage().getCharacterByName(source.getOwner());
        if (source.getOwner() == null || source.getOwner().isEmpty() || gm == null) {
            return false;
        }
        return gm.isStaff();
    }

    public static boolean isIllegalItem(int id) {
        switch (id) {
            case 1042003:
            case 1062007:
            case 1002140:
            case 1003142:
            case 1322013:
            case 1002959:
                return true;
        }
        return false;
    }

    public static MapleInventoryType getInventoryType(final int itemId) {
        final byte type = (byte) (itemId / 1000000);
        if (type < 1 || type > 5) {
            return MapleInventoryType.UNDEFINED;
        }
        return MapleInventoryType.getByType(type);
    }

    public static boolean isInBag(final int slot, final byte type) {
        return ((slot >= 101 && slot <= 512) && type == MapleInventoryType.ETC.getType());
    }

    public static MapleWeaponType getWeaponType(final int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;
        switch (cat) { // 39, 50, 51 ??
            case 21:
                return MapleWeaponType.ROD;
            case 30:
                return MapleWeaponType.单手剑;
            case 31:
                return MapleWeaponType.单手斧;
            case 32:
                return MapleWeaponType.单手钝器;
            case 33:
                return MapleWeaponType.DAGGER;
            case 34:
                return MapleWeaponType.KATARA;
            case 35:
                return MapleWeaponType.MAGIC_ARROW; // can be magic arrow or cards
            case 36:
                return MapleWeaponType.CANE;
            case 37:
                return MapleWeaponType.WAND;
            case 38:
                return MapleWeaponType.STAFF;
            case 40:
                return MapleWeaponType.双手剑;
            case 41:
                return MapleWeaponType.双手斧;
            case 42:
                return MapleWeaponType.双手钝器;
            case 43:
                return MapleWeaponType.矛;
            case 44:
                return MapleWeaponType.枪;
            case 45:
                return MapleWeaponType.BOW;
            case 46:
                return MapleWeaponType.CROSSBOW;
            case 47:
                return MapleWeaponType.CLAW;
            case 48:
                return MapleWeaponType.KNUCKLE;
            case 49:
                return MapleWeaponType.GUN;
            case 52:
                return MapleWeaponType.DUAL_BOW;
            case 53:
                return MapleWeaponType.CANNON;
            case 56:
                return MapleWeaponType.BIG_SWORD;
            case 57:
                return MapleWeaponType.LONG_SWORD;
        }
        //System.out.println("Found new Weapon: " + cat + ", ItemId: " + itemId);
        return MapleWeaponType.NOT_A_WEAPON;
    }

    public static int getEquipForJob(final int job) {
        if (!MapleJob.isExist(job)) {
            return -1;
        }
        switch (job / 10) {
            case 10:
            case 11:
            case 12:
                return 130;
            case 13:
                return 143;
            case 20:
            case 21:
            case 22:
            case 23:
                return 138;
            case 30:
            case 31:
                return 145;
            case 32:
                return 146;
            case 40:
            case 41:
                return 147;
            case 42:
            case 43:
                return 133;
            case 50:
            case 51:
                return 148;
            case 52:
            case 57:
                return 149;
            case 53:
                return 153;
        }
        switch (job / 100) {
            case 11:
                return 130;
            case 12:
                return 138;
            case 13:
                return 145;
            case 14:
                return 147;
            case 15:
                return 148;
            case 21:
                return 144;
            case 22:
                return 137;
            case 23:
                return 152;
            case 24:
                return 136;
            case 27:
                return 121;
            case 31:
                return 132;
            case 32:
                return 138;
            case 33:
                return 146;
            //case 34:
            //    return ;
            case 35:
                return 149;
            case 41:
                return 154;
            case 42:
                return 155;
            //case 43:
            //    return ;
            //case 44:
            //    return ;
            //case 45:
            //    return ;
            case 51:
                return 130;
            //case 52:
            //    return ;
            //case 53:
            //    return ;
            //case 54:
            //    return ;
            //case 55:
            //    return ;
            case 61:
                return 140;
            //case 62:
            //    return ;
            //case 63:
            //    return ;
            //case 64:
            //    return ;
            case 65:
                return 122;
        }
        return -1;
    }

    public static boolean isShield(final int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;
        return cat == 9;
    }

    public static boolean isEquip(final int itemId) {
        return itemId / 1000000 == 1;
    }

    public static boolean isCleanSlate(int itemId) {
        return itemId / 100 == 20490;
    }

    public static boolean isAccessoryScroll(int itemId) {
        return itemId / 100 == 20492;
    }

    public static boolean isChaosScroll(int itemId) {
        if (itemId >= 2049105 && itemId <= 2049110) {
            return false;
        }
        return itemId / 100 == 20491 || itemId == 2040126;
    }

    public static int getChaosNumber(int itemId) {
        return itemId == 2049116 ? 10 : 5;
    }

    public static boolean isEquipScroll(int scrollId) {
        return scrollId / 100 == 20493;
    }

    public static boolean isPotentialScroll(int scrollId) {
        return scrollId / 100 == 20494 || scrollId / 100 == 20497 || scrollId == 5534000;
    }

    public static boolean isAzwanScroll(int scrollId) {
        //return MapleItemInformationProvider.getInstance().getEquipStats(scroll.getItemId()).containsKey("tuc");
        //should add this ^ too.
        return scrollId >= 2046060 && scrollId <= 2046069 || scrollId >= 2046141 && scrollId <= 2046145 || scrollId >= 2046519 && scrollId <= 2046530 || scrollId >= 2046701 && scrollId <= 2046712;
    }

    public static boolean isSpecialSlotScroll(int scrollId) {
        return MapleItemInformationProvider.getInstance().getEquipStats(scrollId).containsKey("tuc");
    }

    public static boolean isSpecialScroll(final int scrollId) {
        switch (scrollId) {
            case 2040727: // Spikes on show
            case 2041058: // Cape for Cold protection
            case 2530000:
            case 2530001:
            case 2531000:
            case 5063000:
            case 5064000:
                return true;
        }
        return false;
    }

    public static boolean isTwoHanded(final int itemId) {
        switch (getWeaponType(itemId)) {
            case 双手斧:
            case GUN:
            case KNUCKLE:
            case 双手钝器:
            case BOW:
            case CLAW:
            case CROSSBOW:
            case 枪:
            case 矛:
            case 双手剑:
            case CANNON:
            case DUAL_BOW:
            case BIG_SWORD:
            case LONG_SWORD:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSpecialShield(final int itemid) {
        return itemid / 1000 == 1098 || itemid / 1000 == 1099 || itemid / 10000 == 135;
    }

    public static boolean isTownScroll(final int id) {
        return id >= 2030000 && id < 2040000;
    }

    public static boolean isUpgradeScroll(final int id) {
        return id >= 2040000 && id < 2050000;
    }

    public static boolean isGun(final int id) {
        return id >= 1492000 && id < 1500000;
    }

    public static boolean isUse(final int id) {
        return id >= 2000000 && id < 3000000;
    }

    public static boolean isSummonSack(final int id) {
        return id / 10000 == 210;
    }

    public static boolean isMonsterCard(final int id) {
        return id / 10000 == 238;
    }

    public static boolean isSpecialCard(final int id) {
        return id / 1000 >= 2388;
    }

    public static int getCardShortId(final int id) {
        return id % 10000;
    }

    public static boolean isGem(final int id) {
        return id >= 4250000 && id <= 4251402;
    }

    public static boolean isOtherGem(final int id) {
        switch (id) {
            case 4001174:
            case 4001175:
            case 4001176:
            case 4001177:
            case 4001178:
            case 4001179:
            case 4001180:
            case 4001181:
            case 4001182:
            case 4001183:
            case 4001184:
            case 4001185:
            case 4001186:
            case 4031980:
            case 2041058:
            case 2040727:
            case 1032062:
            case 4032334:
            case 4032312:
            case 1142156:
            case 1142157:
                return true; //mostly quest items
        }
        return false;
    }

    public static boolean isCustomQuest(final int id) {
        return id > 99999;
    }

    public static int getTaxAmount(final int meso) {
        if (meso >= 100000000) {
            return (int) Math.round(0.06 * meso);
        } else if (meso >= 25000000) {
            return (int) Math.round(0.05 * meso);
        } else if (meso >= 10000000) {
            return (int) Math.round(0.04 * meso);
        } else if (meso >= 5000000) {
            return (int) Math.round(0.03 * meso);
        } else if (meso >= 1000000) {
            return (int) Math.round(0.018 * meso);
        } else if (meso >= 100000) {
            return (int) Math.round(0.008 * meso);
        }
        return 0;
    }

    public static int EntrustedStoreTax(final int meso) {
        if (meso >= 100000000) {
            return (int) Math.round(0.03 * meso);
        } else if (meso >= 25000000) {
            return (int) Math.round(0.025 * meso);
        } else if (meso >= 10000000) {
            return (int) Math.round(0.02 * meso);
        } else if (meso >= 5000000) {
            return (int) Math.round(0.015 * meso);
        } else if (meso >= 1000000) {
            return (int) Math.round(0.009 * meso);
        } else if (meso >= 100000) {
            return (int) Math.round(0.004 * meso);
        }
        return 0;
    }

    public static int getAttackDelay(final int id, final Skill skill) {
        switch (id) {
            case 2111003:
                return 0;
            case 1311011:
            case 3111009:
            case 3120019:
            case 3111013:
            case 5220023:
            case 5221022:
            case 23121000:
            case 31201001:
            case 33121009:
            case 35111004:
            case 35121005:
            case 35121013:
                return 40;
            case 4111010:
                return 99;
            case 13111020:
            case 13121001:
                return 120;
            case 4100012:
            case 4120019:
            case 24100003:
            case 24120002:
            case 24121000:
            case 24121005:
            case 27121201:
            case 36001005:
            case 61101002:
            case 61110211:
            case 61120007:
            case 61121217:
                return 30;
            case 2221012:
            case 32121003:
                return 180;
            case 27120211:
            case 61111100:
            case 61111113:
                return 210;
            case 21110007:
            case 21110008:
            case 21120009:
            case 21120010:
                return 390;
            case 31001006:
            case 31001007:
            case 31001008:
                return 270;
            case 31121005:
                return 510;
            case 61001000:
            case 61001004:
            case 61001005:
                return 240;
            case 60011216:
            case 65121003:
                return 180;
            case 36121000:
                return 120;
            case 0:
                return 330;
        }
        if (skill != null && skill.getSkillType() == 3) {
            return 0; //final attack
        }
        if (skill != null && skill.getDelay() > 0 && !isNoDelaySkill(id)) {
            return skill.getDelay();
        }
        // TODO delay for final attack, weapon type, swing,stab etc
        return 330; // Default usually
    }

    public static byte gachaponRareItem(final int id) {
        switch (id) {
            case 2340000: // White Scroll
            case 2049100: // Chaos Scroll
            case 3010014: // Moon Star Chair
            case 3010043: // Halloween Brromstick
            case 3010073: // Giant Pink bean Cushion
            case 3010072: // Miwok Chief's Chair
            case 3010068: // Lotus Leaf Chair
            case 3010085: // Olivia's Chair
            case 3010118: // Musical Note Chair
            case 3010124: // Dunas Jet Char
            case 3010125: // Nibelung Battleship
            case 3010131: //chewing panda chair
            case 3010137: // Dragon lord Chair
            case 3010156: // Visitor Representative
            case 3010615: // Nao Resting
            case 3010592: //Black Bean Chair
            case 3010602: // Heart Cloud Chair
            case 3010670: // absolute Ring chair
            case 3010728: // ilove Maplestory
            case 1342033: // VIP Katara
            case 1372078: // VIP wand
            case 1382099: // Staff
            case 1402090: // Two handed Sword
            case 1412062: // Two Handed Axe
            case 1422063: // Two handed Blunt Weapon
            case 1432081: // Spear
            case 1442111: // Polearm
            case 1452106: // Bow
            case 1462091: // Crossbow
            case 1472117: // Claw
            case 1482079: // Knuckle
            case 1492079: // Gun
            case 1302147: // one sword
            case 1312062: // One handed Axe
            case 1322090: // One Handed Blunt Weapon
            case 1332120: // Dagger(LUK)
            case 1332125: // Dagger (STR)< end of VIP
            case 1102041: // Pink Adventure Cape
            case 1022082: // Spectrum Goog
            case 1072238: // Violet snow shoes
            case 5062002: // Super Miracle
            case 5062003: // Miracle
            case 5062005: // Miracle
            case 2040834: // Scroll for gloves for att 50%^
            case 1102042: // Purple adventure cape
                return 2;
            //1 = wedding msg o.o
        }
        return 0;
    }
    public final static int[] goldrewards = {
        2049400, 1,
        2049401, 2,
        2049301, 2,
        2340000, 1, // white scroll
        2070007, 2,
        2070016, 1,
        2330007, 1,
        2070018, 1, // balance fury
        1402037, 1, // Rigbol Sword
        2290096, 1, // Maple Warrior 20
        2290049, 1, // Genesis 30
        2290041, 1, // Meteo 30
        2290047, 1, // Blizzard 30
        2290095, 1, // Smoke 30
        2290017, 1, // Enrage 30
        2290075, 1, // Snipe 30
        2290085, 1, // Triple Throw 30
        2290116, 1, // Areal Strike
        1302059, 3, // Dragon Carabella
        2049100, 1, // Chaos Scroll
        1092049, 1, // Dragon Kanjar
        1102041, 1, // Pink Cape
        1432018, 3, // Sky Ski
        1022047, 3, // Owl Mask
        3010051, 1, // Chair
        3010020, 1, // Portable meal table
        2040914, 1, // Shield for Weapon Atk

        1432011, 3, // Fair Frozen
        1442020, 3, // HellSlayer
        1382035, 3, // Blue Marine
        1372010, 3, // Dimon Wand
        1332027, 3, // Varkit
        1302056, 3, // Sparta
        1402005, 3, // Bezerker
        1472053, 3, // Red Craven
        1462018, 3, // Casa Crow
        1452017, 3, // Metus
        1422013, 3, // Lemonite
        1322029, 3, // Ruin Hammer
        1412010, 3, // Colonian Axe

        1472051, 1, // Green Dragon Sleeve
        1482013, 1, // Emperor's Claw
        1492013, 1, // Dragon fire Revlover

        1382049, 1,
        1382050, 1, // Blue Dragon Staff
        1382051, 1,
        1382052, 1,
        1382045, 1, // Fire Staff, Level 105
        1382047, 1, // Ice Staff, Level 105
        1382048, 1, // Thunder Staff
        1382046, 1, // Poison Staff

        1372035, 1,
        1372036, 1,
        1372037, 1,
        1372038, 1,
        1372039, 1,
        1372040, 1,
        1372041, 1,
        1372042, 1,
        1332032, 8, // Christmas Tree
        1482025, 7, // Flowery Tube

        4001011, 8, // Lupin Eraser
        4001010, 8, // Mushmom Eraser
        4001009, 8, // Stump Eraser

        2047000, 1,
        2047001, 1,
        2047002, 1,
        2047100, 1,
        2047101, 1,
        2047102, 1,
        2047200, 1,
        2047201, 1,
        2047202, 1,
        2047203, 1,
        2047204, 1,
        2047205, 1,
        2047206, 1,
        2047207, 1,
        2047208, 1,
        2047300, 1,
        2047301, 1,
        2047302, 1,
        2047303, 1,
        2047304, 1,
        2047305, 1,
        2047306, 1,
        2047307, 1,
        2047308, 1,
        2047309, 1,
        2046004, 1,
        2046005, 1,
        2046104, 1,
        2046105, 1,
        2046208, 1,
        2046209, 1,
        2046210, 1,
        2046211, 1,
        2046212, 1,
        //list
        1132014, 3,
        1132015, 2,
        1132016, 1,
        1002801, 2,
        1102205, 2,
        1332079, 2,
        1332080, 2,
        1402048, 2,
        1402049, 2,
        1402050, 2,
        1402051, 2,
        1462052, 2,
        1462054, 2,
        1462055, 2,
        1472074, 2,
        1472075, 2,
        //pro raven
        1332077, 1,
        1382082, 1,
        1432063, 1,
        1452087, 1,
        1462053, 1,
        1472072, 1,
        1482048, 1,
        1492047, 1,
        2030008, 5, // Bottle, return scroll
        1442018, 3, // Frozen Tuna
        2040900, 4, // Shield for DEF
        2049100, 10,
        2000005, 10, // Power Elixir
        2000004, 10, // Elixir
        4280000, 8,
        2430144, 10,
        2290285, 10,
        2028061, 10,
        2028062, 10,
        2530000, 5,
        2531000, 5}; // Gold Box
    public final static int[] silverrewards = {
        2049401, 2,
        2049301, 2,
        3010041, 1, // skull throne
        1002452, 6, // Starry Bandana
        1002455, 6, // Starry Bandana
        2290084, 1, // Triple Throw 20
        2290048, 1, // Genesis 20
        2290040, 1, // Meteo 20
        2290046, 1, // Blizzard 20
        2290074, 1, // Sniping 20
        2290064, 1, // Concentration 20
        2290094, 1, // Smoke 20
        2290022, 1, // Berserk 20
        2290056, 1, // Bow Expert 30
        2290066, 1, // xBow Expert 30
        2290020, 1, // Sanc 20
        1102082, 1, // Black Raggdey Cape
        1302049, 1, // Glowing Whip
        2340000, 1, // White Scroll
        1102041, 1, // Pink Cape
        1452019, 2, // White Nisrock
        4001116, 3, // Hexagon Pend
        4001012, 3, // Wraith Eraser
        1022060, 2, // Foxy Racoon Eye
        2430144, 5,
        2290285, 5,
        2028062, 5,
        2028061, 5,
        2530000, 1,
        2531000, 1,
        2041100, 1,
        2041101, 1,
        2041102, 1,
        2041103, 1,
        2041104, 1,
        2041105, 1,
        2041106, 1,
        2041107, 1,
        2041108, 1,
        2041109, 1,
        2041110, 1,
        2041111, 1,
        2041112, 1,
        2041113, 1,
        2041114, 1,
        2041115, 1,
        2041116, 1,
        2041117, 1,
        2041118, 1,
        2041119, 1,
        2041300, 1,
        2041301, 1,
        2041302, 1,
        2041303, 1,
        2041304, 1,
        2041305, 1,
        2041306, 1,
        2041307, 1,
        2041308, 1,
        2041309, 1,
        2041310, 1,
        2041311, 1,
        2041312, 1,
        2041313, 1,
        2041314, 1,
        2041315, 1,
        2041316, 1,
        2041317, 1,
        2041318, 1,
        2041319, 1,
        2049200, 1,
        2049201, 1,
        2049202, 1,
        2049203, 1,
        2049204, 1,
        2049205, 1,
        2049206, 1,
        2049207, 1,
        2049208, 1,
        2049209, 1,
        2049210, 1,
        2049211, 1,
        1432011, 3, // Fair Frozen
        1442020, 3, // HellSlayer
        1382035, 3, // Blue Marine
        1372010, 3, // Dimon Wand
        1332027, 3, // Varkit
        1302056, 3, // Sparta
        1402005, 3, // Bezerker
        1472053, 3, // Red Craven
        1462018, 3, // Casa Crow
        1452017, 3, // Metus
        1422013, 3, // Lemonite
        1322029, 3, // Ruin Hammer
        1412010, 3, // Colonian Axe

        1002587, 3, // Black Wisconsin
        1402044, 1, // Pumpkin lantern
        2101013, 4, // Summoning Showa boss
        1442046, 1, // Super Snowboard
        1422031, 1, // Blue Seal Cushion
        1332054, 3, // Lonzege Dagger
        1012056, 3, // Dog Nose
        1022047, 3, // Owl Mask
        3012002, 1, // Bathtub
        1442012, 3, // Sky snowboard
        1442018, 3, // Frozen Tuna
        1432010, 3, // Omega Spear
        1432036, 1, // Fishing Pole
        2000005, 10, // Power Elixir
        2049100, 10,
        2000004, 10, // Elixir
        4280001, 8}; // Silver Box
    public final static int[] peanuts = {2430091, 200, 2430092, 200, 2430093, 200, 2430101, 200, 2430102, 200, 2430136, 200, 2430149, 200,//mounts 
        2340000, 1, //rares
        1152000, 5, 1152001, 5, 1152004, 5, 1152005, 5, 1152006, 5, 1152007, 5, 1152008, 5, //toenail only comes when db is out.
        1152064, 5, 1152065, 5, 1152066, 5, 1152067, 5, 1152070, 5, 1152071, 5, 1152072, 5, 1152073, 5,
        3010019, 2, //chairs
        1001060, 10, 1002391, 10, 1102004, 10, 1050039, 10, 1102040, 10, 1102041, 10, 1102042, 10, 1102043, 10, //equips
        1082145, 5, 1082146, 5, 1082147, 5, 1082148, 5, 1082149, 5, 1082150, 5, //wg
        2043704, 10, 2040904, 10, 2040409, 10, 2040307, 10, 2041030, 10, 2040015, 10, 2040109, 10, 2041035, 10, 2041036, 10, 2040009, 10, 2040511, 10, 2040408, 10, 2043804, 10, 2044105, 10, 2044903, 10, 2044804, 10, 2043009, 10, 2043305, 10, 2040610, 10, 2040716, 10, 2041037, 10, 2043005, 10, 2041032, 10, 2040305, 10, //scrolls
        2040211, 5, 2040212, 5, 1022097, 10, //dragon glasses
        2049000, 10, 2049001, 10, 2049002, 10, 2049003, 10, //clean slate
        1012058, 5, 1012059, 5, 1012060, 5, 1012061, 5,//pinocchio nose msea only.
        1332100, 10, 1382058, 10, 1402073, 10, 1432066, 10, 1442090, 10, 1452058, 10, 1462076, 10, 1472069, 10, 1482051, 10, 1492024, 10, 1342009, 10, //durability weapons level 105
        2049400, 1, 2049401, 2, 2049301, 2,
        2049100, 10,
        2430144, 10,
        2290285, 10,
        2028062, 10,
        2028061, 10,
        2530000, 5,
        2531000, 5,
        1032080, 5,
        1032081, 4,
        1032082, 3,
        1032083, 2,
        1032084, 1,
        1112435, 5,
        1112436, 4,
        1112437, 3,
        1112438, 2,
        1112439, 1,
        1122081, 5,
        1122082, 4,
        1122083, 3,
        1122084, 2,
        1122085, 1,
        1132036, 5,
        1132037, 4,
        1132038, 3,
        1132039, 2,
        1132040, 1,
        //source
        1092070, 5,
        1092071, 4,
        1092072, 3,
        1092073, 2,
        1092074, 1,
        1092075, 5,
        1092076, 4,
        1092077, 3,
        1092078, 2,
        1092079, 1,
        1092080, 5,
        1092081, 4,
        1092082, 3,
        1092083, 2,
        1092084, 1,
        1092087, 1,
        1092088, 1,
        1092089, 1,
        1302143, 5,
        1302144, 4,
        1302145, 3,
        1302146, 2,
        1302147, 1,
        1312058, 5,
        1312059, 4,
        1312060, 3,
        1312061, 2,
        1312062, 1,
        1322086, 5,
        1322087, 4,
        1322088, 3,
        1322089, 2,
        1322090, 1,
        1332116, 5,
        1332117, 4,
        1332118, 3,
        1332119, 2,
        1332120, 1,
        1332121, 5,
        1332122, 4,
        1332123, 3,
        1332124, 2,
        1332125, 1,
        1342029, 5,
        1342030, 4,
        1342031, 3,
        1342032, 2,
        1342033, 1,
        1372074, 5,
        1372075, 4,
        1372076, 3,
        1372077, 2,
        1372078, 1,
        1382095, 5,
        1382096, 4,
        1382097, 3,
        1382098, 2,
        1392099, 1,
        1402086, 5,
        1402087, 4,
        1402088, 3,
        1402089, 2,
        1402090, 1,
        1412058, 5,
        1412059, 4,
        1412060, 3,
        1412061, 2,
        1412062, 1,
        1422059, 5,
        1422060, 4,
        1422061, 3,
        1422062, 2,
        1422063, 1,
        1432077, 5,
        1432078, 4,
        1432079, 3,
        1432080, 2,
        1432081, 1,
        1442107, 5,
        1442108, 4,
        1442109, 3,
        1442110, 2,
        1442111, 1,
        1452102, 5,
        1452103, 4,
        1452104, 3,
        1452105, 2,
        1452106, 1,
        1462087, 5,
        1462088, 4,
        1462089, 3,
        1462090, 2,
        1462091, 1,
        1472113, 5,
        1472114, 4,
        1472115, 3,
        1472116, 2,
        1472117, 1,
        1482075, 5,
        1482076, 4,
        1482077, 3,
        1482078, 2,
        1482079, 1,
        1492075, 5,
        1492076, 4,
        1492077, 3,
        1492078, 2,
        1492079, 1,
        1132012, 2,
        1132013, 1,
        1942002, 2,
        1952002, 2,
        1962002, 2,
        1972002, 2,
        1612004, 2,
        1622004, 2,
        1632004, 2,
        1642004, 2,
        1652004, 2,
        2047000, 1,
        2047001, 1,
        2047002, 1,
        2047100, 1,
        2047101, 1,
        2047102, 1,
        2047200, 1,
        2047201, 1,
        2047202, 1,
        2047203, 1,
        2047204, 1,
        2047205, 1,
        2047206, 1,
        2047207, 1,
        2047208, 1,
        2047300, 1,
        2047301, 1,
        2047302, 1,
        2047303, 1,
        2047304, 1,
        2047305, 1,
        2047306, 1,
        2047307, 1,
        2047308, 1,
        2047309, 1,
        2046004, 1,
        2046005, 1,
        2046104, 1,
        2046105, 1,
        2046208, 1,
        2046209, 1,
        2046210, 1,
        2046211, 1,
        2046212, 1,
        2049200, 1,
        2049201, 1,
        2049202, 1,
        2049203, 1,
        2049204, 1,
        2049205, 1,
        2049206, 1,
        2049207, 1,
        2049208, 1,
        2049209, 1,
        2049210, 1,
        2049211, 1,
        //ele wand
        1372035, 1,
        1372036, 1,
        1372037, 1,
        1372038, 1,
        //ele staff
        1382045, 1,
        1382046, 1,
        1382047, 1,
        1382048, 1,
        1382049, 1,
        1382050, 1, // Blue Dragon Staff
        1382051, 1,
        1382052, 1,
        1372039, 1,
        1372040, 1,
        1372041, 1,
        1372042, 1,
        2070016, 1,
        2070007, 2,
        2330007, 1,
        2070018, 1,
        2330008, 1,
        2070023, 1,
        2070024, 1,
        2028062, 5,
        2028061, 5};
    public static int[] eventCommonReward = {
        0, 10,
        1, 10,
        4, 5,
        5060004, 25,
        4170024, 25,
        4280000, 5,
        4280001, 6,
        5490000, 5,
        5490001, 6
    };
    public static int[] eventUncommonReward = {
        1, 4,
        2, 8,
        3, 8,
        2022179, 5,
        5062000, 20,
        2430082, 20,
        2430092, 20,
        2022459, 2,
        2022460, 1,
        2022462, 1,
        2430103, 2,
        2430117, 2,
        2430118, 2,
        2430201, 4,
        2430228, 4,
        2430229, 4,
        2430283, 4,
        2430136, 4,
        2430476, 4,
        2430511, 4,
        2430206, 4,
        2430199, 1,
        1032062, 5,
        5220000, 28,
        2022459, 5,
        2022460, 5,
        2022461, 5,
        2022462, 5,
        2022463, 5,
        5050000, 2,
        4080100, 10,
        4080000, 10,
        2049100, 10,
        2430144, 10,
        2290285, 10,
        2028062, 10,
        2028061, 10,
        2530000, 5,
        2531000, 5,
        2041100, 1,
        2041101, 1,
        2041102, 1,
        2041103, 1,
        2041104, 1,
        2041105, 1,
        2041106, 1,
        2041107, 1,
        2041108, 1,
        2041109, 1,
        2041110, 1,
        2041111, 1,
        2041112, 1,
        2041113, 1,
        2041114, 1,
        2041115, 1,
        2041116, 1,
        2041117, 1,
        2041118, 1,
        2041119, 1,
        2041300, 1,
        2041301, 1,
        2041302, 1,
        2041303, 1,
        2041304, 1,
        2041305, 1,
        2041306, 1,
        2041307, 1,
        2041308, 1,
        2041309, 1,
        2041310, 1,
        2041311, 1,
        2041312, 1,
        2041313, 1,
        2041314, 1,
        2041315, 1,
        2041316, 1,
        2041317, 1,
        2041318, 1,
        2041319, 1,
        2049200, 1,
        2049201, 1,
        2049202, 1,
        2049203, 1,
        2049204, 1,
        2049205, 1,
        2049206, 1,
        2049207, 1,
        2049208, 1,
        2049209, 1,
        2049210, 1,
        2049211, 1
    };
    public static int[] eventRareReward = {
        2049100, 5,
        2430144, 5,
        2290285, 5,
        2028062, 5,
        2028061, 5,
        2530000, 2,
        2531000, 2,
        2049116, 1,
        2049401, 10,
        2049301, 20,
        2049400, 3,
        2340000, 1,
        3010130, 5,
        3010131, 5,
        3010132, 5,
        3010133, 5,
        3010136, 5,
        3010116, 5,
        3010117, 5,
        3010118, 5,
        1112405, 1,
        1112445, 1,
        1022097, 1,
        2040211, 1,
        2040212, 1,
        2049000, 2,
        2049001, 2,
        2049002, 2,
        2049003, 2,
        1012058, 2,
        1012059, 2,
        1012060, 2,
        1012061, 2,
        2022460, 4,
        2022461, 3,
        2022462, 4,
        2022463, 3,
        2040041, 1,
        2040042, 1,
        2040334, 1,
        2040430, 1,
        2040538, 1,
        2040539, 1,
        2040630, 1,
        2040740, 1,
        2040741, 1,
        2040742, 1,
        2040829, 1,
        2040830, 1,
        2040936, 1,
        2041066, 1,
        2041067, 1,
        2043023, 1,
        2043117, 1,
        2043217, 1,
        2043312, 1,
        2043712, 1,
        2043812, 1,
        2044025, 1,
        2044117, 1,
        2044217, 1,
        2044317, 1,
        2044417, 1,
        2044512, 1,
        2044612, 1,
        2044712, 1,
        2046000, 1,
        2046001, 1,
        2046004, 1,
        2046005, 1,
        2046100, 1,
        2046101, 1,
        2046104, 1,
        2046105, 1,
        2046200, 1,
        2046201, 1,
        2046202, 1,
        2046203, 1,
        2046208, 1,
        2046209, 1,
        2046210, 1,
        2046211, 1,
        2046212, 1,
        2046300, 1,
        2046301, 1,
        2046302, 1,
        2046303, 1,
        2047000, 1,
        2047001, 1,
        2047002, 1,
        2047100, 1,
        2047101, 1,
        2047102, 1,
        2047200, 1,
        2047201, 1,
        2047202, 1,
        2047203, 1,
        2047204, 1,
        2047205, 1,
        2047206, 1,
        2047207, 1,
        2047208, 1,
        2047300, 1,
        2047301, 1,
        2047302, 1,
        2047303, 1,
        2047304, 1,
        2047305, 1,
        2047306, 1,
        2047307, 1,
        2047308, 1,
        2047309, 1,
        1112427, 5,
        1112428, 5,
        1112429, 5,
        1012240, 10,
        1022117, 10,
        1032095, 10,
        1112659, 10,
        2070007, 10,
        2330007, 5,
        2070016, 5,
        2070018, 5,
        1152038, 1,
        1152039, 1,
        1152040, 1,
        1152041, 1,
        1122090, 1,
        1122094, 1,
        1122098, 1,
        1122102, 1,
        1012213, 1,
        1012219, 1,
        1012225, 1,
        1012231, 1,
        1012237, 1,
        2070023, 5,
        2070024, 5,
        2330008, 5,
        2003516, 5,
        2003517, 1,
        1132052, 1,
        1132062, 1,
        1132072, 1,
        1132082, 1,
        1112585, 1,
        //walker
        1072502, 1,
        1072503, 1,
        1072504, 1,
        1072505, 1,
        1072506, 1,
        1052333, 1,
        1052334, 1,
        1052335, 1,
        1052336, 1,
        1052337, 1,
        1082305, 1,
        1082306, 1,
        1082307, 1,
        1082308, 1,
        1082309, 1,
        1003197, 1,
        1003198, 1,
        1003199, 1,
        1003200, 1,
        1003201, 1,
        1662000, 1,
        1662001, 1,
        1672000, 1,
        1672001, 1,
        1672002, 1,
        //crescent moon
        1112583, 1,
        1032092, 1,
        1132084, 1,
        //mounts, 90 day
        2430290, 1,
        2430292, 1,
        2430294, 1,
        2430296, 1,
        2430298, 1,
        2430300, 1,
        2430302, 1,
        2430304, 1,
        2430306, 1,
        2430308, 1,
        2430310, 1,
        2430312, 1,
        2430314, 1,
        2430316, 1,
        2430318, 1,
        2430320, 1,
        2430322, 1,
        2430324, 1,
        2430326, 1,
        2430328, 1,
        2430330, 1,
        2430332, 1,
        2430334, 1,
        2430336, 1,
        2430338, 1,
        2430340, 1,
        2430342, 1,
        2430344, 1,
        2430347, 1,
        2430349, 1,
        2430351, 1,
        2430353, 1,
        2430355, 1,
        2430357, 1,
        2430359, 1,
        2430361, 1,
        2430392, 1,
        2430512, 1,
        2430536, 1,
        2430477, 1,
        2430146, 1,
        2430148, 1,
        2430137, 1,};
    public static int[] eventSuperReward = {
        2022121, 10,
        4031307, 50,
        3010127, 10,
        3010128, 10,
        3010137, 10,
        3010157, 10,
        2049300, 10,
        2040758, 10,
        1442057, 10,
        2049402, 10,
        2049304, 1,
        2049305, 1,
        2040759, 7,
        2040760, 5,
        2040125, 10,
        2040126, 10,
        1012191, 5,
        1112514, 1, //untradable/tradable
        1112531, 1,
        1112629, 1,
        1112646, 1,
        1112515, 1, //untradable/tradable
        1112532, 1,
        1112630, 1,
        1112647, 1,
        1112516, 1, //untradable/tradable
        1112533, 1,
        1112631, 1,
        1112648, 1,
        2040045, 10,
        2040046, 10,
        2040333, 10,
        2040429, 10,
        2040542, 10,
        2040543, 10,
        2040629, 10,
        2040755, 10,
        2040756, 10,
        2040757, 10,
        2040833, 10,
        2040834, 10,
        2041068, 10,
        2041069, 10,
        2043022, 12,
        2043120, 12,
        2043220, 12,
        2043313, 12,
        2043713, 12,
        2043813, 12,
        2044028, 12,
        2044120, 12,
        2044220, 12,
        2044320, 12,
        2044520, 12,
        2044513, 12,
        2044613, 12,
        2044713, 12,
        2044817, 12,
        2044910, 12,
        2046002, 5,
        2046003, 5,
        2046102, 5,
        2046103, 5,
        2046204, 10,
        2046205, 10,
        2046206, 10,
        2046207, 10,
        2046304, 10,
        2046305, 10,
        2046306, 10,
        2046307, 10,
        2040006, 2,
        2040007, 2,
        2040303, 2,
        2040403, 2,
        2040506, 2,
        2040507, 2,
        2040603, 2,
        2040709, 2,
        2040710, 2,
        2040711, 2,
        2040806, 2,
        2040903, 2,
        2040913, 2,
        2041024, 2,
        2041025, 2,
        2044815, 2,
        2044908, 2,
        1152046, 1,
        1152047, 1,
        1152048, 1,
        1152049, 1,
        1122091, 1,
        1122095, 1,
        1122099, 1,
        1122103, 1,
        1012214, 1,
        1012220, 1,
        1012226, 1,
        1012232, 1,
        1012238, 1,
        1032088, 1,
        1032089, 1,
        1032090, 1,
        1032091, 1,
        1132053, 1,
        1132063, 1,
        1132073, 1,
        1132083, 1,
        1112586, 1,
        1112593, 1,
        1112597, 1,
        1662002, 1,
        1662003, 1,
        1672003, 1,
        1672004, 1,
        1672005, 1,
        //130, 140 weapons
        1092088, 1,
        1092089, 1,
        1092087, 1,
        1102275, 1,
        1102276, 1,
        1102277, 1,
        1102278, 1,
        1102279, 1,
        1102280, 1,
        1102281, 1,
        1102282, 1,
        1102283, 1,
        1102284, 1,
        1082295, 1,
        1082296, 1,
        1082297, 1,
        1082298, 1,
        1082299, 1,
        1082300, 1,
        1082301, 1,
        1082302, 1,
        1082303, 1,
        1082304, 1,
        1072485, 1,
        1072486, 1,
        1072487, 1,
        1072488, 1,
        1072489, 1,
        1072490, 1,
        1072491, 1,
        1072492, 1,
        1072493, 1,
        1072494, 1,
        1052314, 1,
        1052315, 1,
        1052316, 1,
        1052317, 1,
        1052318, 1,
        1052319, 1,
        1052329, 1,
        1052321, 1,
        1052322, 1,
        1052323, 1,
        1003172, 1,
        1003173, 1,
        1003174, 1,
        1003175, 1,
        1003176, 1,
        1003177, 1,
        1003178, 1,
        1003179, 1,
        1003180, 1,
        1003181, 1,
        1302152, 1,
        1302153, 1,
        1312065, 1,
        1312066, 1,
        1322096, 1,
        1322097, 1,
        1332130, 1,
        1332131, 1,
        1342035, 1,
        1342036, 1,
        1372084, 1,
        1372085, 1,
        1382104, 1,
        1382105, 1,
        1402095, 1,
        1402096, 1,
        1412065, 1,
        1412066, 1,
        1422066, 1,
        1422067, 1,
        1432086, 1,
        1432087, 1,
        1442116, 1,
        1442117, 1,
        1452111, 1,
        1452112, 1,
        1462099, 1,
        1462100, 1,
        1472122, 1,
        1472123, 1,
        1482084, 1,
        1482085, 1,
        1492085, 1,
        1492086, 1,
        1532017, 1,
        1532018, 1,
        //mounts
        2430291, 1,
        2430293, 1,
        2430295, 1,
        2430297, 1,
        2430299, 1,
        2430301, 1,
        2430303, 1,
        2430305, 1,
        2430307, 1,
        2430309, 1,
        2430311, 1,
        2430313, 1,
        2430315, 1,
        2430317, 1,
        2430319, 1,
        2430321, 1,
        2430323, 1,
        2430325, 1,
        2430327, 1,
        2430329, 1,
        2430331, 1,
        2430333, 1,
        2430335, 1,
        2430337, 1,
        2430339, 1,
        2430341, 1,
        2430343, 1,
        2430345, 1,
        2430348, 1,
        2430350, 1,
        2430352, 1,
        2430354, 1,
        2430356, 1,
        2430358, 1,
        2430360, 1,
        2430362, 1,
        //rising sun
        1012239, 1,
        1122104, 1,
        1112584, 1,
        1032093, 1,
        1132085, 1
    };
    public static int[] tenPercent = {
        //10% scrolls
        2040002,
        2040005,
        2040026,
        2040031,
        2040100,
        2040105,
        2040200,
        2040205,
        2040302,
        2040310,
        2040318,
        2040323,
        2040328,
        2040329,
        2040330,
        2040331,
        2040402,
        2040412,
        2040419,
        2040422,
        2040427,
        2040502,
        2040505,
        2040514,
        2040517,
        2040534,
        2040602,
        2040612,
        2040619,
        2040622,
        2040627,
        2040702,
        2040705,
        2040708,
        2040727,
        2040802,
        2040805,
        2040816,
        2040825,
        2040902,
        2040915,
        2040920,
        2040925,
        2040928,
        2040933,
        2041002,
        2041005,
        2041008,
        2041011,
        2041014,
        2041017,
        2041020,
        2041023,
        2041058,
        2041102,
        2041105,
        2041108,
        2041111,
        2041302,
        2041305,
        2041308,
        2041311,
        2043002,
        2043008,
        2043019,
        2043102,
        2043114,
        2043202,
        2043214,
        2043302,
        2043402,
        2043702,
        2043802,
        2044002,
        2044014,
        2044015,
        2044102,
        2044114,
        2044202,
        2044214,
        2044302,
        2044314,
        2044402,
        2044414,
        2044502,
        2044602,
        2044702,
        2044802,
        2044809,
        2044902,
        2045302,
        2048002,
        2048005
    };
    public static int[] fishingReward = {//钓鱼奖励
        0, 100, // 金币
        1, 100, // 经验
        2022179, 1, // Onyx Apple
        1302021, 5, // Pico Pico Hammer
        1072238, 1, // Voilet Snowshoe
        1072239, 1, // Yellow Snowshoe
        2049100, 2, // Chaos Scroll
        2430144, 1,
        2290285, 1,
        2028062, 1,
        2028061, 1,
        2049301, 1, // Equip Enhancer Scroll
        2049401, 1, // Potential Scroll
        1302000, 3, // Sword
        1442011, 1, // Surfboard
        4000517, 8, // Golden Fish
        4000518, 10, // Golden Fish Egg
        4031627, 2, // White Bait (3cm)
        4031628, 1, // Sailfish (120cm)
        4031630, 1, // Carp (30cm)
        4031631, 1, // Salmon(150cm)
        4031632, 1, // Shovel
        4031633, 2, // Whitebait (3.6cm)
        4031634, 1, // Whitebait (5cm)
        4031635, 1, // Whitebait (6.5cm)
        4031636, 1, // Whitebait (10cm)
        4031637, 2, // Carp (53cm)
        4031638, 2, // Carp (60cm)
        4031639, 1, // Carp (100cm)
        4031640, 1, // Carp (113cm)
        4031641, 2, // Sailfish (128cm)
        4031642, 2, // Sailfish (131cm)
        4031643, 1, // Sailfish (140cm)
        4031644, 1, // Sailfish (148cm)
        4031645, 2, // Salmon (166cm)
        4031646, 2, // Salmon (183cm)
        4031647, 1, // Salmon (227cm)
        4031648, 1, // Salmon (288cm)
        4001187, 20,
        4001188, 20,
        4001189, 20,
        4031629, 1 // Pot
    };

    public static boolean isReverseItem(int itemId) {
        switch (itemId) {
            case 1002790:
            case 1002791:
            case 1002792:
            case 1002793:
            case 1002794:
            case 1082239:
            case 1082240:
            case 1082241:
            case 1082242:
            case 1082243:
            case 1052160:
            case 1052161:
            case 1052162:
            case 1052163:
            case 1052164:
            case 1072361:
            case 1072362:
            case 1072363:
            case 1072364:
            case 1072365:

            case 1302086:
            case 1312038:
            case 1322061:
            case 1332075:
            case 1332076:
            case 1372045:
            case 1382059:
            case 1402047:
            case 1412034:
            case 1422038:
            case 1432049:
            case 1442067:
            case 1452059:
            case 1462051:
            case 1472071:
            case 1482024:
            case 1492025:

            case 1342012:
            case 1942002:
            case 1952002:
            case 1962002:
            case 1972002:
            case 1532016:
            case 1522017:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTimelessItem(int itemId) {
        switch (itemId) {
            case 1032031: //shield earring, but technically
            case 1102172:
            case 1002776:
            case 1002777:
            case 1002778:
            case 1002779:
            case 1002780:
            case 1082234:
            case 1082235:
            case 1082236:
            case 1082237:
            case 1082238:
            case 1052155:
            case 1052156:
            case 1052157:
            case 1052158:
            case 1052159:
            case 1072355:
            case 1072356:
            case 1072357:
            case 1072358:
            case 1072359:
            case 1092057:
            case 1092058:
            case 1092059:

            case 1122011:
            case 1122012:

            case 1302081:
            case 1312037:
            case 1322060:
            case 1332073:
            case 1332074:
            case 1372044:
            case 1382057:
            case 1402046:
            case 1412033:
            case 1422037:
            case 1432047:
            case 1442063:
            case 1452057:
            case 1462050:
            case 1472068:
            case 1482023:
            case 1492023:
            case 1342011:
            case 1532015:
            case 1522016:
                //raven.
                return true;
            default:
                return false;
        }
    }

    public static boolean isRing(int itemId) {
        return itemId >= 1112000 && itemId < 1113000;
    }// 112xxxx - pendants, 113xxxx - belts

    //if only there was a way to find in wz files -.-
    public static boolean isEffectRing(int itemid) {
        return isFriendshipRing(itemid) || isCrushRing(itemid) || isMarriageRing(itemid);
    }

    public static boolean isMarriageRing(int itemId) {
        switch (itemId) {
            case 1112300:
            case 1112301:
            case 1112302:
            case 1112303:
            case 1112304:
            case 1112305:
            case 1112306:
            case 1112307:
            case 1112308:
            case 1112309:
            case 1112310:
            case 1112311:
            case 1112312:
            case 1112315:
            case 1112316:
            case 1112317:
            case 1112318:
            case 1112319:
            case 1112320:
            case 1112804:
                return true;
        }
        return false;
    }

    public static boolean isFriendshipRing(int itemId) {
        switch (itemId) {
            case 1112800:
            case 1112801:
            case 1112802:
            case 1112810: //new
            case 1112811: //new, doesnt work in friendship?
            case 1112812: //new, im ASSUMING it's friendship cuz of itemID, not sure.
            case 1112816: //new, i'm also assuming
            case 1112817:

            case 1049000:
                return true;
        }
        return false;
    }

    public static boolean isCrushRing(int itemId) {
        switch (itemId) {
            case 1112001:
            case 1112002:
            case 1112003:
            case 1112005: //new
            case 1112006: //new
            case 1112007:
            case 1112012:
            case 1112015: //new
            case 1048000:
            case 1048001:
            case 1048002:
            case 1112013:
            case 1112014:
            case 1112816:
            case 1112820:
                return true;
        }
        return false;
    }

    public static int[] Equipments_Bonus = {1122017};

    public static int Equipment_Bonus_EXP(final int itemid) {
        switch (itemid) {
            case 1122017:
                return 10;
        }
        return 0;
    }
    public static int[] blockedMaps = {109050000, 280030000, 240060200, 280090000, 280030001, 240060201, 950101100, 950101010};
    public static final int[] normalDrops = {4001009, 4001010, 4001011, 4001012, 4001013, 4001014, 4001021, 4001038, 4001039, 4001040, 4001041, 4001042, 4001043, 4001038, 4001039, 4001040, 4001041, 4001042, 4001043, 4001038, 4001039, 4001040, 4001041, 4001042, 4001043, 4000164, 2000000, 2000003, 2000004, 2000005, 4000019, 4000000, 4000016, 4000006, 2100121, 4000029, 4000064, 5110000, 4000306, 4032181, 4006001, 4006000, 2050004, 3994102, 3994103, 3994104, 3994105, 2430007, 4000164, 2000000, 2000003, 2000004, 2000005, 4000019, 4000000, 4000016, 4000006, 2100121, 4000029, 4000064, 5110000, 4000306, 4032181, 4006001, 4006000, 2050004, 3994102, 3994103, 3994104, 3994105, 2430007, 4000164, 2000000, 2000003, 2000004, 2000005, 4000019, 4000000, 4000016, 4000006, 2100121, 4000029, 4000064, 5110000, 4000306, 4032181, 4006001, 4006000, 2050004, 3994102, 3994103, 3994104, 3994105, 2430007};
    public static final int[] rareDrops = {2022179, 2049100, 2049100, 2430144, 2028062, 2028061, 2290285, 2049301, 2049401, 2022326, 2022193, 2049000, 2049001, 2049002};
    public static final int[] superDrops = {2040804, 2049400, 2028062, 2028061, 2430144, 2430144, 2430144, 2430144, 2290285, 2049100, 2049100, 2049100, 2049100};
    public static int[] owlItems = {1082002, 2070005, 2070006, 1022047, 1102041, 2044705, 2340000, 2040017, 1092030, 2040804};

    public static int getExpForLevel(int i, int itemId) {
        if (isReverseItem(itemId)) {
            return getReverseRequiredEXP(i);
        } else if (getMaxLevel(itemId) > 0) {
            return getTimelessRequiredEXP(i);
        }
        return 0;
    }

    public static int getMaxLevel(final int itemId) {
        Map<Integer, Map<String, Integer>> inc = MapleItemInformationProvider.getInstance().getEquipIncrements(itemId);
        return inc != null ? (inc.size()) : 0;
    }

    public static int getStatChance() {
        return 25;
    }

    public static MonsterStatus getStatFromWeapon(final int itemid) {
        switch (itemid) {
            case 1302109:
            case 1312041:
            case 1322067:
            case 1332083:
            case 1372048:
            case 1382064:
            case 1402055:
            case 1412037:
            case 1422041:
            case 1432052:
            case 1442073:
            case 1452064:
            case 1462058:
            case 1472079:
            case 1482035:
                return MonsterStatus.DARKNESS;
            case 1302108:
            case 1312040:
            case 1322066:
            case 1332082:
            case 1372047:
            case 1382063:
            case 1402054:
            case 1412036:
            case 1422040:
            case 1432051:
            case 1442072:
            case 1452063:
            case 1462057:
            case 1472078:
            case 1482036:
                return MonsterStatus.SPEED;
        }
        return null;
    }

    public static int getXForStat(MonsterStatus stat) {
        switch (stat) {
            case DARKNESS:
                return -70;
            case SPEED:
                return -50;
        }
        return 0;
    }

    public static int getSkillForStat(MonsterStatus stat) {
        switch (stat) {
            case DARKNESS:
                return 1111003;
            case SPEED:
                return 3121007;
        }
        return 0;
    }

    public static int getSkillBook(final int job, final int skill) {
        if (job >= 2210 && job <= 2218) {
            return job - 2209;
        }
        if (isZero(job)) {
            if (skill > 0) {
                int type = (skill % 1000) / 100; //1 beta 2 alpha
                return type == 1 ? 1 : 0;
            } else {
                return 0;
            }
        }
        switch (job) {

            case 110:
            case 120:
            case 130:
            case 210:
            case 220:
            case 230:
            case 310:
            case 320:
            case 410:
            case 420:
            case 510:
            case 520:

            case 570:
            case 1110:
            case 1310:
            case 1510:
            case 2310:
            case 2410:
            case 2710:
            case 3110:
            case 3120:
            case 3210:
            case 3310:
            case 3510:
            case 3610:
            case 4110:
            case 4210:
            case 5110:
            case 6110:
            case 6510:
                return 0;
            case 111:
            case 121:
            case 131:
            case 211:
            case 221:
            case 231:
            case 311:
            case 321:
            case 411:
            case 421:
            case 511:
            case 521:

            case 571:
            case 1111:
            case 1311:
            case 1511:
            case 2311:
            case 2411:
            case 2711:
            case 3111:
            case 3121:
            case 3211:
            case 3311:
            case 3511:
            case 3611:
            case 4111:
            case 4211:
            case 5111:
            case 6111:
            case 6511:
                return 0;
            case 112:
            case 122:
            case 132:
            case 212:
            case 222:
            case 232:
            case 312:
            case 322:
            case 412:
            case 422:
            case 512:
            case 522:

            case 572:
            case 1112:
            case 1312:
            case 1512:
            case 2312:
            case 2412:
            case 2712:
            case 3112:
            case 3122:
            case 3212:
            case 3312:
            case 3512:
            case 3612:
            case 4112:
            case 4212:
            case 5112:
            case 6112:
            case 6512:
                return 0;
            case 508:
                return 0;
        }
        if (isSeparatedSp(job)) {
            if (job % 10 > 4) {
                return 0;
            }
            return (job % 10);
        }
        return 0;
    }

    public static int getSkillBook(final int job, final int level, final int skill) {
        if (job >= 2210 && job <= 2218) {
            return job - 2209;
        }
        if (isSeparatedSp(job)) {
            return (level <= 30 ? 0 : (level >= 31 && level <= 60 ? 1 : (level >= 61 && level <= 100 ? 2 : (level >= 100 ? 3 : 0))));
        }
        return 0;
    }

//    public static int getSkillBook(final int job, final int level) {
//        if (job >= 2210 && job <= 2218) {
//            return job - 2209;
//        }
//        switch (job) {
//
//            case 508:
//            case 570:
//            case 571:
//            case 572:
//            //cygnus
//            case 1110:
//            case 1111:
//            case 1112:
//            case 1310:
//            case 1311:
//            case 1312:
//            case 1510:
//            case 1511:
//            case 1512:
//            //cygnus end
//            case 2300:
//            case 2310:
//            case 2311:
//            case 2312:
//            case 2400:
//            case 2410:
//            case 2411:
//            case 2412:
//            case 2710:
//            case 2711:
//            case 2712:
//            case 3100:
//            case 3200:
//            case 3300:
//            case 3500:
//            case 3110:
//            case 3210:
//            case 3310:
//            case 3510:
//            case 3111:
//            case 3211:
//            case 3311:
//            case 3511:
//            case 3112:
//            case 3212:
//            case 3312:
//            case 3512:
//            case 5000:
//            case 5100:
//            case 5110:
//            case 5111:
//            case 5112:
//            case 6510:
//            case 6511:
//            case 6512:
//                return (level <= 30 ? 0 : (level >= 31 && level <= 60 ? 1 : (level >= 61 && level <= 100 ? 2 : (level >= 100 ? 3 : 0))));
//        }
//        return 0;
//    }
    public static int getSkillBookForSkill(final int skillid) {
//        System.out.println(getSkillBook(skillid / 10000));
        return getSkillBook(skillid / 10000, skillid);
    }

    public static int getLinkedMountItem(final int sourceid) {
        switch (sourceid % 1000) {
            case 1:
            case 24:
            case 25:
                return 1018;
            case 2:
            case 26:
                return 1019;
            case 3:
                return 1025;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return (sourceid % 1000) + 1023;
            case 9:
            case 10:
            case 11:
                return (sourceid % 1000) + 1024;
            case 12:
                return 1042;
            case 13:
                return 1044;
            case 14:
                return 1049;
            case 15:
            case 16:
            case 17:
                return (sourceid % 1000) + 1036;
            case 18:
            case 19:
                return (sourceid % 1000) + 1045;
            case 20:
                return 1072;
            case 21:
                return 1084;
            case 22:
                return 1089;
            case 23:
                return 1106;
            case 29:
                return 1151;
            case 30:
            case 50:
                return 1054;
            case 31:
            case 51:
                return 1069;
            case 32:
                return 1138;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return (sourceid % 1000) + 1009;
            case 52:
                return 1070;
            case 53:
                return 1071;
            case 54:
                return 1096;
            case 55:
                return 1101;
            case 56:
                return 1102;
            case 58:
                return 1118;
            case 59:
                return 1121;
            case 60:
                return 1122;
            case 61:
                return 1129;
            case 62:
                return 1139;
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
                return (sourceid % 1000) + 1080;
            case 85:
            case 86:
            case 87:
                return (sourceid % 1000) + 928;
            case 88:
                return 1065;

            case 27:
                return 1932049; //airplane
            case 28:
                return 1932050; //airplane
            case 114:
                return 1932099; //bunny buddy
            //33 = hot air
            //37 = bjorn
            //38 = speedy chariot
            //57 = law officer
            //they all have in wz so its ok
        }
        return 0;
    }

    public static int getMountItem(final int sourceid, final MapleCharacter chr) {
        switch (sourceid) {
            case 5221006:
                return 1932000;
            case 33001001: //temp.
                if (chr == null) {
                    return 1932015;
                }
                switch (chr.getIntNoRecord(JAGUAR)) {
                    case 20:
                        return 1932030;
                    case 30:
                        return 1932031;
                    case 40:
                        return 1932032;
                    case 50:
                        return 1932033;
                    case 60:
                        return 1932036;
                    case 70:
                        return 1932100;
                }
                return 1932015;
            case 35001002:
            case 35120000:
                return 1932016;
            //case 30011109:
            //	return 1932085;
        }
        if (!isBeginnerJob(sourceid / 10000)) {
            if (sourceid / 10000 == 8000 && sourceid != 80001000) { //todoo clean up
                final Skill skil = SkillFactory.getSkill(sourceid);
                if (skil != null && skil.getTamingMob() > 0) {
                    return skil.getTamingMob();
                    //} else if (skil != null && skil.getSkillTamingMob() > 0) {
                    //    return skil.getSkillTamingMob();
                } else {
                    final int link = getLinkedMountItem(sourceid);
                    if (link > 0) {
                        if (link < 10000) {
                            return getMountItem(link, chr);
                        } else {
                            return link;
                        }
                    }
                }
            }
            return 0;
        }
        if (isBeginnerJob(sourceid / 10000)) {
            if (sourceid != 80001000) { //todoo clean up
                final Skill skil = SkillFactory.getSkill(sourceid);
                if (skil != null && skil.getSkillTamingMob() > 0) {
                    return skil.getSkillTamingMob();
                }
            }
        }
        switch (sourceid) {
            case 20021160: //Sylvidia
                return 1932086;
            case 20021161: //Sylvidia
                return 1932087;
            case 20031160: //Rolls
                return 1932106;
            case 20031161: //Royce
                return 1932107;
            case 30011109: //Demon Wings
                return 1932051;
            case 30011159: //Demon Wings
                return 1932051;
        }
        switch (sourceid % 10000) {
            case 1013:
            case 1046:
                return 1932001;
            case 1015:
            case 1048:
                return 1932002;
            case 1016:
            case 1017:
            case 1027:
                return 1932007;
            case 1018:
                return 1932003;
            case 1019:
                return 1932005;
            case 1025:
                return 1932006;
            case 1028:
                return 1932008;
            case 1029:
                return 1932009;
            case 1030:
                return 1932011;
            case 1031:
                return 1932010;
            case 1033:
                return 1932013;
            case 1034:
                return 1932014;
            case 1035:
                return 1932012;
            case 1036:
                return 1932017;
            case 1037:
                return 1932018;
            case 1038:
                return 1932019;
            case 1039:
                return 1932020;
            case 1040:
                return 1932021;
            case 1042:
                return 1932022;
            case 1044:
                return 1932023;
            //case 1045:
            //return 1932030; //wth? helicopter? i didnt see one, so we use hog
            case 1049:
                return 1932025;
            case 1050:
                return 1932004;
            case 1051:
                return 1932026;
            case 1052:
                return 1932027;
            case 1053:
                return 1932028;
            case 1054:
                return 1932029;
            case 1063:
                return 1932034;
            case 1064:
                return 1932035;
            case 1065:
                return 1932037;
            case 1069:
                return 1932038;
            case 1070:
                return 1932039;
            case 1071:
                return 1932040;
            case 1072:
                return 1932041;
            case 1084:
                return 1932043;
            case 1089:
                return 1932044;
            case 1096:
                return 1932045;
            case 1101:
                return 1932046;
            case 1102:
                return 1932061;
            case 1106:
                return 1932048;
            case 1118:
                return 1932060;
            case 1115:
                return 1932052;
            case 1121:
                return 1932063;
            case 1122:
                return 1932064;
            case 1123:
                return 1932065;
            case 1128:
                return 1932066;
            case 1130:
                return 1932072;
            case 1136:
                return 1932078;
            case 1138:
                return 1932080;
            case 1139:
                return 1932081;
            //FLYING
            case 1143:
            case 1144:
            case 1145:
            case 1146:
            case 1147:
            case 1148:
            case 1149:
            case 1150:
            case 1151:
            case 1152:
            case 1153:
            case 1154:
            case 1155:
            case 1156:
            case 1157:
                return 1992000 + (sourceid % 10000) - 1143;
            default:
                return 0;
        }
    }

    public static boolean isKatara(int itemId) {
        return itemId / 10000 == 134;
    }

    public static boolean isDagger(int itemId) {
        return itemId / 10000 == 133;
    }

    public static boolean isApplicableSkill(int skil) {
        return ((skil < 80000000 || skil >= 100000000) && (skil % 10000 < 8000 || skil % 10000 > 8006) && !isAngel(skil)) || skil >= 92000000 || (skil >= 80000000 && skil < 80010000); //no additional/decent skills
    }

    public static boolean isApplicableSkill_(int skil) { //not applicable to saving but is more of temporary
        for (int i : PlayerStats.pvpSkills) {
            if (skil == i) {
                return true;
            }
        }
        return (skil >= 90000000 && skil < 92000000) || (skil % 10000 >= 8000 && skil % 10000 <= 8003) || isAngel(skil);
    }

    public static boolean isRidingSKill(int skil) {
        return (skil >= 80001000 && skil < 80010000);
    }

    public static boolean isTablet(int itemId) {
        return itemId / 1000 == 2047;
    }

    public static boolean isGeneralScroll(int itemId) {
        return itemId / 1000 == 2046;
    }

    public static int getSuccessTablet(final int scrollId, final int level) {
        if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 55;
                case 2:
                    return 43;
                case 3:
                    return 33;
                case 4:
                    return 26;
                case 5:
                    return 20;
                case 6:
                    return 16;
                case 7:
                    return 12;
                case 8:
                    return 10;
                default:
                    return 7;
            }
        } else if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 35;
                case 2:
                    return 18;
                case 3:
                    return 12;
                default:
                    return 7;
            }
        } else {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 50; //-20
                case 2:
                    return 36; //-14
                case 3:
                    return 26; //-10
                case 4:
                    return 19; //-7
                case 5:
                    return 14; //-5
                case 6:
                    return 10; //-4
                default:
                    return 7;  //-3
            }
        }
    }

    public static int getCurseTablet(final int scrollId, final int level) {
        if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
            switch (level) {
                case 0:
                    return 10;
                case 1:
                    return 12;
                case 2:
                    return 16;
                case 3:
                    return 20;
                case 4:
                    return 26;
                case 5:
                    return 33;
                case 6:
                    return 43;
                case 7:
                    return 55;
                case 8:
                    return 70;
                default:
                    return 100;
            }
        } else if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 12;
                case 1:
                    return 18;
                case 2:
                    return 35;
                case 3:
                    return 70;
                default:
                    return 100;
            }
        } else {
            switch (level) {
                case 0:
                    return 10;
                case 1:
                    return 14; //+4
                case 2:
                    return 19; //+5
                case 3:
                    return 26; //+7
                case 4:
                    return 36; //+10
                case 5:
                    return 50; //+14
                case 6:
                    return 70; //+20
                default:
                    return 100;  //+30
            }
        }
    }

    public static boolean isAccessory(final int itemId) {
        return (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1153000) || (itemId >= 1112000 && itemId < 1113000) || (itemId >= 1670000 && itemId < 1680000);
    }

    public static boolean isMedal(final int itemId) {
        return itemId / 10000 == 114;
    }

    public static boolean potentialIDFits(final int potentialID, final int newstate, final int i) {
        //first line is always the best
        //but, sometimes it is possible to get second/third line as well
        //may seem like big chance, but it's not as it grabs random potential ID anyway
        if (newstate == 20) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 40000 : potentialID >= 30000 && potentialID < 60004); // xml say so
        } else if (newstate == 19) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 30000 : potentialID >= 20000 && potentialID < 30000);
        } else if (newstate == 18) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 20000 && potentialID < 30000 : potentialID >= 10000 && potentialID < 20000);
        } else if (newstate == 17) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 10000 && potentialID < 20000 : potentialID < 10000);
        } else {
            return false;
        }
    }

    public static boolean optionTypeFits(final int optionType, final int itemId) {
        switch (optionType) {
            case 10: // weapons
                return isWeapon(itemId);
            case 11: // all equipment except weapons
                return !isWeapon(itemId);
            case 20: // all armors
                return !isAccessory(itemId) && !isWeapon(itemId);
            case 40: // accessories
                return isAccessory(itemId);
            case 51: // hat
                return itemId / 10000 == 100;
            case 52: // top and overall
                return itemId / 10000 == 104 || itemId / 10000 == 105;
            case 53: // bottom and overall
                return itemId / 10000 == 106 || itemId / 10000 == 105;
            case 54: // glove
                return itemId / 10000 == 108;
            case 55: // shoe
                return itemId / 10000 == 107;
            default:
                return true;
        }
    }

    public static int getNebuliteGrade(final int id) {
        if (id / 10000 != 306) {
            return -1;
        }
        if (id >= 3060000 && id < 3061000) {
            return 0;
        } else if (id >= 3061000 && id < 3062000) {
            return 1;
        } else if (id >= 3062000 && id < 3063000) {
            return 2;
        } else if (id >= 3063000 && id < 3064000) {
            return 3;
        }
        return 4;
    }

    public static final boolean isMountItemAvailable(int mountid, int jobid) {
        if ((jobid != 900) && (mountid / 10000 == 190)) {
            switch (mountid) {
                case 1902000:
                case 1902001:
                case 1902002:
                case 1902008:
                case 1902009:
                case 1902011:
                    return isAdventurer(jobid);

            }
        }
        return mountid / 10000 == 190;
    }

    public static boolean isMechanicItem(final int itemId) {
        return itemId >= 1610000 && itemId < 1660000;
    }

    public static boolean isEvanDragonItem(final int itemId) {
        return itemId >= 1940000 && itemId < 1980000; //194 = mask, 195 = pendant, 196 = wings, 197 = tail
    }

    public static boolean canScroll(final int itemId) {
        return itemId / 100000 != 19 && itemId / 100000 != 16; //no mech/taming/dragon
    }

    public static boolean canHammer(final int itemId) {
        switch (itemId) {
            case 1122000:
            case 1122076: //ht, chaos ht
                return false;
        }
        return canScroll(itemId);
    }

    public static int ULT_EXPLORER = 111111;

    public static int getMasterySkill(final int job) {
        if (job >= 1410 && job <= 1412) {
            return 14100000;
        } else if (job >= 410 && job <= 412) {
            return 4100000;
        } else if (job >= 520 && job <= 522) {
            return 5200000;
        }
        return 0;
    }

    public static int getExpRate_Below10(final int job) {
        //if (GameConstants.isEvan(job)) {
        //    return 1;
        //} else if (GameConstants.isAran(job) || GameConstants.isKOC(job) || GameConstants.isResistance(job)) {
        //    return 5;
        //}
        //return 10;
        return 1;
    }

    public static int getExpRate_Quest(final int level) {
        return (level >= 30 ? (level >= 70 ? (level >= 120 ? 1 : 1) : 1) : 1);
    }

    public static String getCommandBlockedMsg() {
        return "You may not use this command here.";
    }

    public static int getCustomReactItem(final int rid, final int original) {
        if (rid == 2008006) { //orbis pq LOL
            return (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 4001055);
            //4001056 = sunday. 4001062 = saturday
        } else {
            return original;
        }
    }

    public static int getJobNumber(int jobz) {
        int job = (jobz % 1000);
        if (job / 100 == 0 || isBeginnerJob(jobz)) {
            return 0; //beginner
        } else if ((job / 10) % 10 == 0 || job == 501) {
            return 1;
        } else {
            return 2 + (job % 10);
        }
    }

    public static boolean isBeginnerJob(final int job) {
        return (job == 0) || (job == 1) || (job == 1000) || (job == 2000) || (job == 2001) || (job == 2002) || (job == 2003) || (job == 2004) || (job == 3000) || (job == 3001) || (job == 3002) || (job == 4000) || (job == 4001) || (job == 4002) || (job == 5000) || (job == 6000) || (job == 6001);
    }

    public static boolean isAzwanMap(int mapId) {
        return mapId >= 262020000 && mapId < 262023000;
    }

    public static boolean isForceRespawn(int mapid) {
        switch (mapid) {
            case 103000800: //kerning PQ crocs
            case 925100100: //crocs and stuff
                return true;
            default:
                return mapid / 100000 == 9800 && (mapid % 10 == 1 || mapid % 1000 == 100);
        }
    }

    public static int getFishingTime(boolean vip, boolean gm) {
        return gm ? 1000 : (vip ? 30000 : 60000);
    }

    public static int getCustomSpawnID(int summoner, int def) {
        switch (summoner) {
            case 9400589:
            case 9400748: //MV
                return 9400706; //jr
            default:
                return def;
        }
    }

    public static boolean canForfeit(int questid) {
        switch (questid) {
            case 20000:
            case 20010:
            case 20015: //cygnus quests
            case 20020:
                return false;
            default:
                return true;
        }
    }

    public static double getAttackRange(MapleStatEffect def, int rangeInc) {
        double defRange = ((400.0 + rangeInc) * (400.0 + rangeInc));
        if (def != null) {
            defRange += def.getMaxDistanceSq() + (def.getRange() * def.getRange());
        }
        //rangeInc adds to X
        //400 is approximate, screen is 600.. may be too much
        //200 for y is also too much
        //default 200000
        return defRange + 120000.0;
    }

    public static double getAttackRange(Point lt, Point rb) {
        double defRange = (400.0 * 400.0);
        final int maxX = Math.max(Math.abs(lt == null ? 0 : lt.x), Math.abs(rb == null ? 0 : rb.x));
        final int maxY = Math.max(Math.abs(lt == null ? 0 : lt.y), Math.abs(rb == null ? 0 : rb.y));
        defRange += (maxX * maxX) + (maxY * maxY);
        //rangeInc adds to X
        //400 is approximate, screen is 600.. may be too much
        //200 for y is also too much
        //default 200000
        return defRange + 120000.0;
    }

    public static long getMagnifyPrice(Equip eq) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (!ii.getEquipStats(eq.getItemId()).containsKey("reqLevel")) {
            return -1;
        }
        int level = ii.getEquipStats(eq.getItemId()).get("reqLevel");
        long price;
        int v1; // esi@1
        double v2; // st7@7
        int v3; // eax@7
        double v4; // st6@7
        int v5; // eax@12

        v1 = 0;
        if (level > 120) {
            v1 = 20;
        } else if (level > 70) {
            v1 = 5;
        } else if (level > 30) {
            v1 = 1;
        }
        v2 = level;
        v3 = 2;
        v4 = 1.0;
        while (1 != 0) {
            if ((v3 & 1) != 0) {
                v4 = v4 * v2;
            }
            v3 >>= 1;
            if (!(v3 != 0)) {
                break;
            }
            v2 = v2 * v2;
        }
        v5 = (int) Math.ceil(v4);
        price = ((v1 * v5 <= 0 ? 1 : 0) - 1) & v1 * v5;

        return price;
    }

    public static int getLowestPrice(int itemId) {
        switch (itemId) {
            case 2340000: //ws
            case 2531000:
            case 2530000:
                return 50000000;
        }
        return -1;
    }

    public static boolean isNoDelaySkill(int skillId) {
        return skillId == 5110001 || skillId == 21101003 || skillId == 15100004 || skillId == 33101004 || skillId == 32111010 || skillId == 2111007 || skillId == 2211007 || skillId == 2311007 || skillId == 32121003 || skillId == 35121005 || skillId == 35111004 || skillId == 35121013 || skillId == 35121003 || skillId == 22150004 || skillId == 22181004 || skillId == 11101002 || skillId == 51100002 || skillId == 13101002 || skillId == 24121000 || skillId == 112001008 || skillId == 22161005 || skillId == 22161005;
    }

    public static boolean isNoSpawn(int mapID) {
        return mapID == 809040100 || mapID == 925020010 || mapID == 925020011 || mapID == 925020012 || mapID == 925020013 || mapID == 925020014 || mapID == 682020000 || mapID == 980010000 || mapID == 980010100 || mapID == 980010200 || mapID == 980010300 || mapID == 980010020;
    }

    public static int getExpRate(int job, int def) {
        return def;
    }

    public static int getModifier(int itemId, int up) {
        if (up <= 0) {
            return 0;
        }
        switch (itemId) {
            case 2022459:
            case 2860179:
            case 2860193:
            case 2860207:
                return 130;
            case 2022460:
            case 2022462:
            case 2022730:
                return 150;
            case 2860181:
            case 2860195:
            case 2860209:
                return 200;
        }
        if (itemId / 10000 == 286) { //familiars
            return 150;
        }
        return 200;
    }

    public static short getSlotMax(int itemId) {
        switch (itemId) {
            case 4030003:
            case 4030004:
            case 4030005:
                return 1;
            case 4001168:
            case 4031306:
            case 4031307:
            case 3993000:
            case 3993002:
            case 3993003:
                return 100;
            case 5220010:
            case 5220013:
                return 1000;
            case 5220020:
                return 2000;
        }
        return 0;
    }

    public static boolean isDropRestricted(int itemId) {
        return itemId == 3012000 || itemId == 4030004 || itemId == 1052098 || itemId == 1052202;
    }

    public static boolean isPickupRestricted(int itemId) {
        return itemId == 4030003 || itemId == 4030004;
    }

    public static short getStat(int itemId, int def) {
        switch (itemId) {
            //case 1002419:
            //    return 5;
            case 1002959:
                return 25;
            case 1142002:
                return 10;
            case 1122121:
                return 7;
        }
        return (short) def;
    }

    public static short getHpMp(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 500;
            case 1142002:
            case 1002959:
                return 1000;
        }
        return (short) def;
    }

    public static short getATK(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 3;
            case 1002959:
                return 4;
            case 1142002:
                return 9;
        }
        return (short) def;
    }

    public static short getDEF(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 250;
            case 1002959:
                return 500;
        }
        return (short) def;
    }

    public static boolean isDojo(int mapId) {
        return mapId >= 925020100 && mapId <= 925023814;
    }

    public static int getPartyPlayHP(int mobID) {
        switch (mobID) {
            /*case 4250000:
             return 836000;
             case 4250001:
             return 924000;
             case 5250000:
             return 1100000;
             case 5250001:
             return 1276000;
             case 5250002:
             return 1452000;

             case 9400661:
             return 15000000;
             case 9400660:
             return 30000000;
             case 9400659:
             return 45000000;
             case 9400658:
             return 20000000;*/
        }
        return 0;
    }

    public static int getPartyPlayEXP(int mobID) {
        switch (mobID) {
            /*case 4250000:
             return 5770;
             case 4250001:
             return 6160;
             case 5250000:
             return 7100;
             case 5250001:
             return 7975;
             case 5250002:
             return 8800;

             case 9400661:
             return 40000;
             case 9400660:
             return 70000;
             case 9400659:
             return 90000;
             case 9400658:
             return 50000;*/
        }
        return 0;
    }

    public static int getPartyPlay(int mapId) {
        switch (mapId) {
            case 300010000:
            case 300010100:
            case 300010200:
            case 300010300:
            case 300010400:
            case 300020000:
            case 300020100:
            case 300020200:
            case 300030000:

            case 683070400:
            case 683070401:
            case 683070402:
                return 25;
        }
        return 0;
    }

    public static int getPartyPlay(int mapId, int def) {
        int dd = getPartyPlay(mapId);
        if (dd > 0) {
            return dd;
        }
        return def / 2;
    }

    public static boolean isHyperTeleMap(int mapId) {
        for (int i : hyperTele) {
            if (i == mapId) {
                return true;
            }
        }
        return false;
    }

    public static int getCurrentDate() {
        final String time = FileoutputUtil.CurrentReadable_Time();
        return Integer.parseInt(new StringBuilder(time.substring(0, 4)).append(time.substring(5, 7)).append(time.substring(8, 10)).append(time.substring(11, 13)).toString());
    }

    public static int getCurrentDate_NoTime() {
        final String time = FileoutputUtil.CurrentReadable_Time();
        return Integer.parseInt(new StringBuilder(time.substring(0, 4)).append(time.substring(5, 7)).append(time.substring(8, 10)).toString());
    }

    public static void achievementRatio(MapleClient c) {
        //PQs not affected: Amoria, MV, CWK, English, Zakum, Horntail(?), Carnival, Ghost, Guild, LudiMaze, Elnath(?) 
        switch (c.getPlayer().getMapId()) {
            case 240080600:
            case 920010000:
            case 930000000:
            case 930000100:
            case 910010000:
            case 922010100:
            case 910340100:
            case 925100000:
            case 926100000:
            case 926110000:
            case 921120005:
            case 932000100:
            case 923040100:
            case 921160100:
                c.getSession().write(CField.achievementRatio(0));
                break;
            case 930000200:
            case 922010200:
            case 922010300:
            case 922010400:
            case 922010401:
            case 922010402:
            case 922010403:
            case 922010404:
            case 922010405:
            case 925100100:
            case 926100001:
            case 926110001:
            case 921160200:
                c.getSession().write(CField.achievementRatio(10));
                break;
            case 930000300:
            case 910340200:
            case 922010500:
            case 922010600:
            case 925100200:
            case 925100201:
            case 925100202:
            case 926100100:
            case 926110100:
            case 921120100:
            case 932000200:
            case 923040200:
            case 921160300:
            case 921160310:
            case 921160320:
            case 921160330:
            case 921160340:
            case 921160350:
                c.getSession().write(CField.achievementRatio(25));
                break;
            case 930000400:
            case 926100200:
            case 926110200:
            case 926100201:
            case 926110201:
            case 926100202:
            case 926110202:
            case 921160400:
                c.getSession().write(CField.achievementRatio(35));
                break;
            case 910340300:
            case 922010700:
            case 930000500:
            case 925100300:
            case 925100301:
            case 925100302:
            case 926100203:
            case 926110203:
            case 921120200:
            case 932000300:
            case 240080700:
            case 240080800:
            case 923040300:
            case 921160500:
                c.getSession().write(CField.achievementRatio(50));
                break;
            case 910340400:
            case 922010800:
            case 930000600:
            case 925100400:
            case 926100300:
            case 926110300:
            case 926100301:
            case 926110301:
            case 926100302:
            case 926110302:
            case 926100303:
            case 926110303:
            case 926100304:
            case 926110304:
            case 921120300:
            case 932000400:
            case 923040400:
            case 921160600:
                c.getSession().write(CField.achievementRatio(70));
                break;
            case 910340500:
            case 922010900:
            case 930000700:
            case 920010800:
            case 925100500:
            case 926100400:
            case 926110400:
            case 926100401:
            case 926110401:
            case 921120400:
            case 921160700:
                c.getSession().write(CField.achievementRatio(85));
                break;
            case 922011000:
            case 922011100:
            case 930000800:
            case 920011000:
            case 920011100:
            case 920011200:
            case 920011300:
            case 925100600:
            case 926100500:
            case 926110500:
            case 926100600:
            case 926110600:
            case 921120500:
            case 921120600:
                c.getSession().write(CField.achievementRatio(100));
                break;
        }
    }

    public static boolean isAngel(int sourceid) {
        return isBeginnerJob(sourceid / 10000) && (sourceid % 10000 == 1085 || sourceid % 10000 == 1087 || sourceid % 10000 == 1090 || sourceid % 10000 == 1179 || sourceid % 10000 == 1154);
    }

    public static boolean isFishingMap(int mapid) {

        switch (mapid) {
            case 741000200:
            case 741000201:
            case 741000202:
            case 741000203:
            case 741000204:
            case 741000205:
            case 741000207:
            case 741000208:
                return true;
        }

        return false;
    }

    public static int getRewardPot(int itemid, int closeness) {
        switch (itemid) {
            case 2440000:
                switch (closeness / 10) {
                    case 0:
                    case 1:
                    case 2:
                        return 2028041 + (closeness / 10);
                    case 3:
                    case 4:
                    case 5:
                        return 2028046 + (closeness / 10);
                    case 6:
                    case 7:
                    case 8:
                        return 2028049 + (closeness / 10);
                }
                return 2028057;
            case 2440001:
                switch (closeness / 10) {
                    case 0:
                    case 1:
                    case 2:
                        return 2028044 + (closeness / 10);
                    case 3:
                    case 4:
                    case 5:
                        return 2028049 + (closeness / 10);
                    case 6:
                    case 7:
                    case 8:
                        return 2028052 + (closeness / 10);
                }
                return 2028060;
            case 2440002:
                return 2028069;
            case 2440003:
                return 2430278;
            case 2440004:
                return 2430381;
            case 2440005:
                return 2430393;
        }
        return 0;
    }

    public static boolean isStartingEventMap(final int mapid) {
        switch (mapid) {
            case 109010000:
            case 109020001:
            case 109030001:
            case 109030101:
            case 109030201:
            case 109030301:
            case 109030401:
            case 109040000:
            case 109060001:
            case 109060002:
            case 109060003:
            case 109060004:
            case 109060005:
            case 109060006:
            case 109080000:
            case 109080001:
            case 109080002:
            case 109080003:
                return true;
        }
        return false;
    }

    public static boolean isEventMap(final int mapid) {
        return (mapid >= 109010000 && mapid < 109050000) || (mapid > 109050001 && mapid < 109090000) || (mapid >= 809040000 && mapid <= 809040100);
    }

    public static boolean isCoconutMap(final int mapid) {
        return mapid == 109080000 || mapid == 109080001 || mapid == 109080002 || mapid == 109080003 || mapid == 109080010 || mapid == 109080011 || mapid == 109080012 || mapid == 109090300 || mapid == 109090301 || mapid == 109090302 || mapid == 109090303 || mapid == 109090304 || mapid == 910040100;
    }

    public static boolean isMagicChargeSkill(final int skillid) {
        switch (skillid) {
            case 2121001: // Big Bang
            case 2221001:
            case 2321001:
            case 42121000:
                //case 22121000: //breath
                //case 22151001:
                return true;
        }
        return false;
    }

    public static boolean isTeamMap(final int mapid) {
        return mapid == 109080000 || mapid == 109080001 || mapid == 109080002 || mapid == 109080003 || mapid == 109080010 || mapid == 109080011 || mapid == 109080012 || mapid == 109090300 || mapid == 109090301 || mapid == 109090302 || mapid == 109090303 || mapid == 109090304 || mapid == 910040100 || mapid == 960020100 || mapid == 960020101 || mapid == 960020102 || mapid == 960020103 || mapid == 960030100 || mapid == 689000000 || mapid == 689000010;
    }

    public static int getStatDice(int stat) {
        switch (stat) {
            case 2:
                return 30;
            case 3:
                return 20;
            case 4:
                return 15;
            case 5:
                return 20;
            case 6:
                return 30;
        }
        return 0;
    }

    public static boolean isAuraBuff(MapleBuffStat stat) {
        switch (stat) {
            case DARK_AURA:
            case BLUE_AURA:
            case YELLOW_AURA:
                return true;
        }
        return false;
    }

    public static boolean isSpecialBuff(MapleBuffStat stat) {
        switch (stat) {
            case CRITICAL_RATE:
            case KAISER_COMBO:
            case DAMAGE_ABSORBED:
            case CRIT_DAMAGE:
            //     case SHADOWPARTNER:
            case DAMAGE_R:
                return true;
        }
        return false;
    }

    public static boolean isSpecialStackBuff(MapleBuffStat stat) {
        switch (stat) {
            case WARRIOR_STANCE:
            case PARASHOCK_GUARD:
                return true;
        }
        return false;
    }

    public static boolean isValuelessBuff(int buffid) {
        switch (buffid) {
            case 23101003: //Spirit Surge
                return true;
        }
        return false;
    }

    public static boolean isCustomReturnMap(int mapid) {
        switch (mapid) {
            case 689013000:
                return true;
        }
        return false;
    }

    public static int getCustomReturnMap(int mapid) {
        switch (mapid) {
            case 689013000:
                return 689012001;
        }
        return mapid;
    }

    public static boolean isAnyDropMap(int mapId) {
        switch (mapId) {
            case 180000000:
            case 180000001:
                return true;
        }
        return false;
    }

    public static boolean isNoExpireMap(int mapId) {
        switch (mapId) {
            case 180000000:
            case 180000001:
                return true;
        }
        return false;
    }

    public static String getCashBlockedMsg(final int id) {
        switch (id) {
            case 5211014:
            case 5211015:
            case 5211016:
            case 5211017:
            case 5211018:
            case 5211019:
            case 5211039:
            case 5211042:
            case 5211045:
                //cube
                return "This item is blocked.";
        }
        return "This item is blocked from the Cash Shop.";
    }

    public static final boolean isRedLeaf(int mapid) {
        return mapid / 1000000 == 744;
    }

    public static int getDiceStat(int buffid, int stat) {
        if (buffid == stat || buffid % 10 == stat || buffid / 10 == stat) {
            return getStatDice(stat);
        } else if (buffid == (stat * 100)) {
            return getStatDice(stat) + 10;
        }
        return 0;
    }

    public static boolean isEnergyBuff(int skill) { //body pressure, tele mastery, twister spin. etc
        switch (skill) {
            case 32121003:
            case 21101003:
            case 2311007:
            case 22161005:
            case 2211007:
            case 2111007:
            case 32111010:
            case 12111007:
                return true;
        }
        return false;
    }

    public static int getMPByJob(int job) {
        switch (job) {
            case 3100:
                return 30;
            case 3110:
                return 60;
            case 3111:
                return 100;
            case 3112:
                return 120;
        }
        return 30; // beginner or 3100
    }

    public static int getHpApByJob(short job) {
        if ((job % 1000) / 100 > 5) {
            job -= 500;
        }
        if ((job % 1000) / 100 == 5) {
            switch (job / 10) {
                case 51:
                    return 68;
                case 53:
                    return 28;
            }
        }
        switch (job / 100) {
            case 21:
                return 30;
            case 22:
                return 12;
            case 31:
                return 38;
            case 32:
                return 20;
        }
        switch ((job % 1000) / 100) {
            case 0:
                return 8;
            case 1:
                return 50;
            case 2:
                return 6;
            case 3:
            case 4:
                return 16;
            case 5:
                return 18;
            default:
                return 8;
        }
    }

    public static int getMpApByJob(short job) {
        if (job / 100 == 31 || job / 100 == 65) {
            return 0;
        }
        if ((job % 1000) / 100 > 5) {
            job -= 500;
        }
        switch (job / 100) {
            case 22:
                return 72;
            case 32:
                return 69;
        }
        switch ((job % 1000) / 100) {
            case 0:
                return 57;
            case 1:
                return 53;
            case 2:
                return 79;
            case 3:
            case 4:
                return 61;
            case 5:
                return 65;
            default:
                return 57;
        }
    }

    public static int getSkillLevel(final int level) {
        if (level >= 70 && level < 120) {
            return 2;
        } else if (level >= 120 && level < 200) {
            return 3;
        } else if (level == 200) {
            return 4;
        }
        return 1;
    }

    public static int[] getInnerSkillbyRank(int rank) {
        if (rank == 0) {
            return rankC;
        } else if (rank == 1) {
            return rankB;
        } else if (rank == 2) {
            return rankA;
        } else if (rank == 3) {
            return rankS;
        } else {
            return null;
        }
    }
    private static final int[] azwanRecipes = {2510483, 2510484, 2510485, 2510486, 2510487, 2510488, 2510489, 2510490, 2510491, 2510492, 2510493, 2510494, 2510495, 2510496, 2510497, 2510498, 2510499, 2510500, 2510501, 2510502, 2510503, 2510504, 2510505, 2510506, 2510507, 2510508, 2510509, 2510510, 2510511, 2510512, 2510513, 2510514, 2510515, 2510516, 2510517, 2510518, 2510519, 2510520, 2510521, 2510522, 2510523, 2510524, 2510525, 2510526, 2510527, 2511153, 2511154, 2511155};
    private static final int[] azwanScrolls = {2046060, 2046061, 2046062, 2046063, 2046064, 2046065, 2046066, 2046067, 2046068, 2046069, 2046141, 2046142, 2046143, 2046144, 2046145, 2046519, 2046520, 2046521, 2046522, 2046523, 2046524, 2046525, 2046526, 2046527, 2046528, 2046529, 2046530, 2046701, 2046702, 2046703, 2046704, 2046705, 2046706, 2046707, 2046708, 2046709, 2046710, 2046711, 2046712};
    private static final Pair[] useItems = {new Pair<>(2002010, 500), new Pair<>(2002006, 600), new Pair<>(2002007, 600), new Pair<>(2002008, 600), new Pair<>(2002009, 600), new Pair<>(2022003, 770), new Pair<>(2022000, 1155), new Pair<>(2001001, 2300), new Pair<>(2001002, 4000), new Pair<>(2020012, 4680), new Pair<>(2020013, 5824), new Pair<>(2020014, 8100), new Pair<>(2020015, 10200), new Pair<>(2000007, 5), new Pair<>(2000000, 5), new Pair<>(2000008, 48), new Pair<>(2000001, 48), new Pair<>(2000009, 96), new Pair<>(2000002, 96), new Pair<>(2000010, 20), new Pair<>(2000003, 20), new Pair<>(2000011, 186), new Pair<>(2000006, 186), new Pair<>(2050000, 200), new Pair<>(2050001, 200), new Pair<>(2050002, 300), new Pair<>(2050003, 500)};

    public static int[] getAzwanRecipes() {
        return azwanRecipes;
    }

    public static int[] getAzwanScrolls() {
        return azwanScrolls;
    }

    public static Pair[] getUseItems() {
        return useItems;
    }

    public static int[] getCirculators() {
        return circulators;
    }
    private static final int[] wheelRewardsA = {2512139, 2512159, 2512179, 2512199, 2512219, 2512239, 2512249, 2000000, 2000001, 2000002, 2000003, 2000007, 2000008, 2000009, 2000010, 2002000, 2002001, 2002002, 2000018, 2000019, 2020012, 2020014, 2001003, 2001515, 2001516, 2001517, 2001518, 2001519, 2001520, 2001521, 2001522, 2001523, 2001524, 2001525, 2003503, 2003504, 2003505, 2003506, 2003507, 2003508, 2004003, 2004023, 2004043, 2004063, 2030000, 2030001, 2030002, 2030003, 2030004, 2030005, 2030006, 4000014, 4000030, 4000073, 4000082, 4000085, 4000103, 4000118, 4000235, 4000296, 4000327, 4000352, 4000445, 4000446, 4000600};
    //recipes, alchemy potions, potions, town scrolls, etc items
    private static final int[] wheelRewardsB = {};
    //10%, 60%, 100% scrolls, pvp level 70 equips, blitz helm, power mane, arcana crown, elemental wands, mastery books, other rare equipments
    private static final int[] wheelRewardsC = {};
    //10%, 60% scrolls, pvp level 130 equips, mastery books

    public static void loadWheelRewards(List<Integer> items, int token) {
        int rank = token % 10;
        int[] rewards = rank == 2 ? wheelRewardsC : rank == 1 ? wheelRewardsB : wheelRewardsA;
        for (int i = 0; i < 10; i++) {
            if (Randomizer.nextInt(100) < 15 && rank == 0 && !items.contains(4031349)) {
                items.add(4031349);
            } else {
                int item = rewards[Randomizer.nextInt(rewards.length)];
                while (items.contains(item)) {
                    item = rewards[Randomizer.nextInt(rewards.length)];
                }
                items.add(item);
            }
        }
    }

    public static List<Integer> getSealedBoxItems(int itemId) {
        List<Integer> list = new LinkedList();
        int[] items = {};
        switch (itemId) {
            case 2028155:
                items = new int[]{2510028, 1050104, 1052131, 1050106, 1050099,
                    1050107, 1052072, 1050098, 1050096, 1052076, 1051101,
                    1041122, 1052071, 2510035, 1061123, 1051106, 1050103,
                    1040122, 2510023, 1052075, 2510022};
                break;
            case 2028156:
                items = new int[]{1082151, 1082153, 1082213, 1072223, 1072272,
                    1072269, 1072226, 1082168, 1082167, 1072222, 2510050,
                    1082159, 2510072, 1082139, 1082154, 1082140, 1072321,
                    1072273, 2510066, 1072215, 2510068};
                break;
        }
        for (int i : items) {
            list.add(i);
        }
        return list;
    }

    public static boolean isStealSkill(int skillId) {
        switch (skillId) {
            case 24001001:
            case 24101001:
            case 24111001:
            case 24121001:
                return true;
        }
        return false;
    }

    public static int getStealSkill(int job) {
        switch (job) {
            case 1:
                return 24001001;
            case 2:
                return 24101001;
            case 3:
                return 24111001;
            case 4:
                return 24121001;
        }
        return 0;
    }

    public static int getNumSteal(int jobNum) {
        switch (jobNum) {
            case 1:
                return 4;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
        }
        return 0;
    }

    public static boolean canSteal(Skill skil) {
        return skil != null && !skil.isMovement() && !isLinkedAttackSkill(skil.getId()) && skil.getId() % 10000 >= 1000 && getJobNumber(skil.getId() / 10000) > 0 && !isDualBlade(skil.getId() / 10000) && !isCannon(skil.getId() / 10000) && !isJett(skil.getId() / 10000) && skil.getId() < 8000000 && skil.getEffect(1) != null && skil.getEffect(1).getSummonMovementType() == null && !skil.getEffect(1).isUnstealable();
    }

    public static boolean isHyperSkill(Skill skill) {
        if (skill.isHyper() || skill.getHyper() > 0) {
            return true;
        }
        if (skill.isBeginnerSkill()) {
            return false;
        }
        return skill.getId() % 1000 >= 30;
    }

    public static boolean isTutorialMap(int mapid) {
        if (mapid < 100000000) { //Explorer & Cannoneer
            return true;
        } else if (mapid / 100 == 1030509) { //Dual Blade
            return true;
        } else if (mapid / 10000 == 13003) { //Cygnus
            return true;
        } else if (mapid / 100000 == 9000) { //Evan
            return true;
        } else if (mapid / 10000 == 91015) { //Mercedes
            return true;
        } else if (mapid / 10000 == 91307) { //Mihile
            return true;
        } else if (mapid / 10000 == 91400) { //Aran
            return true;
        } else if (mapid / 10000 == 91500) { //Phantom
            return true;
        } else if (mapid / 10000 == 93100) { //Resistance
            return true;
        } else if (mapid / 10000 == 93105) { //Demon Slayer
            return true;
        } else if (mapid > 866000000) { //Beast Tamer
            return true;
        }
        return false;
        //There might be included other maps like main town or job advancements,
        //But we don't care since you don't get much exp here and you're locked on teasers.
    }
    /*

     public static enum QuickMove {

     //NPC1(9070004, 0, 30, "Move to the Battle Mode zone #cBattle Square#, where you can fight against other users.\n#cLv. 30 or above can participate in Battle Square."),
     NPC2(9010022, 2, 10, "Use the #cDimensional Mirror# to move to a variety of party quests."),
     //NPC3(9071003, 1, 20, "Move to the party zone \n#cMonster Park#, where you can fight against strong monsters with your party members.\n#cOnly Lv. 20 or above can participate in the Monster Park."),
     //NPC4(9000086, 5, 0, "Move to the closest #cIntercontinental Station# to your current location."),
     NPC5(9000087, 3, 0, "Move to the #cFree Market#, where you can trade items with other users."),
     NPC6(9000088, 4, 30, "Move to #cArdentmill#, the town of Professions.\n#cOnly Lv. 30 or above can move to Ardentmill");
     //NPC7(9000089, 6, 0, "Take the #cTaxi# to move to major areas quickly."),
     //NPC8(9010041, 10, 30, "Receive Part-Time Job reward.");
     //tempest:
     //NPC9(9000123, 13, 1, "You can get a stylish haircut from Big Headward."),
     //NPC10(9000124, 14, 1, "You can get plastic surgery from Dr. Plastic Roy.");
     /* types:
     * 0: Maximus (Battle Arena)
     * 1: Monster Park Shuttle
     * 2: Dimensional Mirror
     * 3: Free Market
     * 4: Ardentmill
     * 5: Boat
     * 6: Taxi
     * 7: Randolf (Legends Shop)
     * 8: Lucia (Legends Shop)
     * 9: Conor (Renegades Shop)
     * 10: Ms. Appropriation (Part-Time Job)
     * 13: Big Headward (hairstyle)
     * 14: Dr. Plastic Roy (plastic surgey)
     * /
     private int npc, type, level;
     private String description;

     QuickMove(int npc, int type, int level, String description) {
     this.npc = npc;
     this.type = type;
     this.level = level;
     this.description = description;
     }

     public int getNpc() {
     return npc;
     }

     public int getType() {
     return type;
     }

     public int getLevel() {
     return level;
     }

     public String getDescription() {
     return description;
     }

     public QuickMove getByNpc(int npc) {
     for (QuickMove q : QuickMove.values()) {
     if (npc == q.getNpc()) {
     return q;
     }
     }
     return null;
     }

     public static byte[] getQuickMoveData(MapleCharacter chr) { //Not completed yet
     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
     MapleMap map = chr.getMap();

     for (MapleNPC npc : map.getAllNPCs()) {
     for (QuickMove q : QuickMove.values()) {
     if (npc.getId() == q.getNpc()) {
     mplew.writeMapleAsciiString(""); //npc name?
     mplew.writeInt(q.getNpc());
     mplew.writeInt(q.getType());
     mplew.writeInt(q.getLevel());
     mplew.writeMapleAsciiString(q.getDescription());
     }
     }
     }

     return mplew.getPacket();
     }
     }
     public static final int[] quickMoveNpcIds = {9070004, 9010022, 9071003, 9000086, 9000087, 9000088, 9000089, 9010041};
     public static final int[] quickMoveNpcsTypes = {0, 2, 1, 5, 3, 4, 6, 10};
     public static final int[] quickMoveNpcsLevel = {30, 10, 20, 0, 0, 30, 0, 30};
     public static final String[] quickMoveNpcs = {"Move to the Battle Mode zone #cBattle Square#, where you can fight against other users.\n#cLv. 30 or above can participate in Battle Square.", "Use the #cDimensional Mirror# to move to a variety of party quests.", "Move to the party zone \n#cMonster Park#, where you can fight against strong monsters with your party members.\n#cOnly Lv. 20 or above can participate in the Monster Park.", "Move to the closest #cIntercontinental Station# to your current location.", "Move to the #cFree Market#, where you can trade items with other users.", "Move to #cArdentmill#, the town of Professions.\n#cOnly Lv. 30 or above can move to Ardentmill", "Take the #cTaxi# to move to major areas quickly.", "Receive Part-Time Job reward."};
    
     */
    //questID; FAMILY USES 19000x, MARRIAGE USES 16000x, EXPED USES 16010x
    //dojo = 150000, bpq = 150001, master monster portals: 122600
    //compensate evan = 170000, compensate sp = 170001
    public static final int OMOK_SCORE = 122200;
    public static final int MATCH_SCORE = 122210;
    public static final int HP_ITEM = 122221;
    public static final int MP_ITEM = 122222;
    //public static final int BUFF_ITEM = 122223;
    public static final int PART_JOB = 122750;
    public static final int PART_JOB_REWARD = 122751;
    public static final int JAIL_TIME = 123455;
    public static final int JAIL_QUEST = 123456;
    public static final int REPORT_QUEST = 123457;
    public static final int PLAYER_INFORMATION = 123568;

    //codex = -55 slot
    //crafting/gathering are designated as skills(short exp then byte 0 then byte level), same with recipes(integer.max_value skill level)
    public static final int ENERGY_DRINK = 122500;
    public static final int HARVEST_TIME = 122501;
    public static final int PENDANT_SLOT = 122700;
    public static final int CURRENT_SET = 122800;
    public static final int BOSS_PQ = 150001;
    public static final int CUSTOM_BANK = 150002;
    public static final int JAGUAR = 111112;
    public static final int DOJO = 150100;
    public static final int DOJO_RECORD = 150101;
    public static final int PARTY_REQUEST = 122900;
    public static final int PARTY_INVITE = 122901;
    public static final int QUICK_SLOT = 123000;
    public static final int ITEM_TITLE = 124000;

    public static boolean isElseSkill(int id) {
        switch (id) {
            case 1009:   // 武陵道場技能
            case 1020:   // 金字塔技能
            case 3221001:// 光速神弩
            case 4211006:// 楓幣炸彈
                return true;
        }
        return false;
    }

    public static boolean Novice_Skill(int skill) {
        switch (skill) {
            case 1000://新手 蝸牛殼
                return true;
        }
        return false;
    }

    public static boolean isMobBook(int itemid) {
        return itemid >= 2380000 && itemid <= 2388055;
    }
}
