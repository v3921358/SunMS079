package constants;

public class ServerConfig {

    public static boolean checkCopyItem = true;
    public static boolean Business = false;  // 是否是商业端，这个用于控制接口占用。
    public static boolean adminOnly = false;
    public static boolean logPackets = false;
    public static boolean AutoDisconnect = true;
    public static boolean sq = false;
    public static final double Activity_Bonus_Rate = 50 / 100.0D;
    public static final int flags = 3;
    public static String serverName = "冒险岛";
    public static String eventMessage = "冒险岛";
    public static final int flag = 3;
    public static final int maxCharacters = 3;
    public static String scrollingMessage = "冒险岛Ver079";
    public static int userLimit = 1500;
    public static String interface_ = Business ? "117.174.164.222" : "127.0.0.1";
    public static int channelCount = 1;
    public static int loginPort = 9595;
    public static short channelPort = 7575;
    public static int cashShopPort = 7777;
    public static int ExpRate = 1;
    public static int MesoRate = 1;
    public static int DropRate = 1;
    public static int BossDropRate = 1;
    public static int maxLevel = 200;
    public static int charslots = 3;
    public static boolean koqst = false;
    public static boolean kozs = false;
    public static int mxjcs = 0;
    public static int qstcs = 130030000;
    public static int zscs = 914000000;
    public static int dyjy = 1;
    public static int dyjb = 1;
    public static int pdmap = 0;
    public static int pdexp = 0;
    public static int pdzsjb = 0;
    public static int pdzdjb = 0;
    public static int pdzsdy = 0;
    public static int pdzddy = 0;
    public static int pdzsdj = 0;
    public static int pdzddj = 0;
    public static int pdzsdd = 0;
    public static int pdzddd = 0;

    public static String events = ""/* + "AutomatedEvent,"*/ + "Relic,HontalePQ,HorntailBattle,cpq2,elevator,Christmas,FireDemon,Amoria,cpq,AutomatedEvent,Flight,English,English0,English1,English2,WuGongPQ,ElementThanatos,4jberserk,4jrush,Trains,Geenie,AirPlane,OrbisPQ,HenesysPQ,Romeo,Juliet,Pirate,Ellin,DollHouse,BossBalrog_NORMAL,Nibergen,PinkBeanBattle,ZakumBattle,NamelessMagicMonster,Dunas,Dunas2,ZakumPQ,LudiPQ,KerningPQ,ProtectTylus,Vergamot,CoreBlaze,GuildQuest,Aufhaven,KyrinTrainingGroundC,KyrinTrainingGroundV,ProtectPig,ScarTarBattle,s4resurrection,s4resurrection2,s4nest,s4aWorld,DLPracticeField,ServerMessage,BossQuestEASY,BossQuestHARD,BossQuestHELL,BossQuestMed,shaoling,Ravana,MV,BossBalrog,QiajiPQ,Relic,Boats";
    public static String pbdt = "";
    public static String killnpc = "";
    public static int[] noSpawnNPC = {};

    /*Anti-Sniff*/
    public static boolean USE_FIXED_IV;
    public static final byte[] Static_LocalIV = new byte[]{71, 113, 26, 44};
    public static final byte[] Static_RemoteIV = new byte[]{70, 112, 25, 43};
}
