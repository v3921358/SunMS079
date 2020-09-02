package client;

import client.MapleTrait.MapleTraitType;
import client.anticheat.CheatTracker;
import client.anticheat.ReportType;
import client.inventory.*;
import client.inventory.MapleImp.ImpFlag;
import constants.*;
import custom.MoonlightAchievements;
import database.DBConPool;
import database.DatabaseException;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider.JobType;
import handling.login.LoginServer;
import handling.world.*;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.mina.common.WriteFuture;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.*;
import server.MapleStatEffect.CancelEffectAction;
import server.Timer.BuffTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.WorldTimer;
import server.life.*;
import server.maps.*;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import server.shops.MapleShopFactory;
import server.shops.MapleShopItem;
import server.stores.IMaplePlayerShop;
import tools.*;
import tools.packet.*;
import tools.packet.CField.EffectPacket;
import tools.packet.CField.SummonPacket;
import tools.packet.CWvsContext.BuddylistPacket;
import tools.packet.CWvsContext.BuffPacket;
import tools.packet.CWvsContext.InfoPacket;
import tools.packet.CWvsContext.InventoryPacket;
import tools.packet.CWvsContext.Reward;
import tools.packet.JobPacket.AvengerPacket;
import tools.packet.JobPacket.LuminousPacket;
import tools.packet.JobPacket.PhantomPacket;
import tools.packet.JobPacket.XenonPacket;

public class MapleCharacter extends AnimatedMapleMapObject implements Serializable, MapleCharacterLook {

    private String name, chalktext, BlessOfFairy_Origin, BlessOfEmpress_Origin, teleportname;
    private static final long serialVersionUID = 845748950829L;
    private long lastCombo, lastfametime, keydown_skill, nextConsume, pqStartTime, lastDragonBloodTime,
            lastBerserkTime, lastRecoveryTime, lastSummonTime, mapChangeTime, lastFishingTime, lastFairyTime,
            lastHPTime, lastMPTime, lastFamiliarEffectTime, lastExceedTime, lastDOTTime, lastCheckPeriodTime;
    private int accountid, id, hair, face, secondHair, secondFace, faceMarking, ears, tail, elf, mapid, fame, pvpExp, pvpPoints, totalWins, totalLosses,
            guildid = 0, fallcounter, maplepoints, acash, nxcredit, chair, itemEffect, points, vpoints, dpoints, epoints,
            rank = 1, rankMove = 0, jobRank = 1, jobRankMove = 0, marriageId, marriageItemId, dotHP,
            currentrep, totalrep, coconutteam, followid, battleshipHP, gachexp, challenge, guildContribution = 0,
            remainingAp, honourExp, honorLevel, runningLight, runningLightSlot, runningDark, runningDarkSlot, luminousState, exp, meso, APQScore,
            reborns, apstorage, str, luk, int_, dex, friendshiptoadd, wheelItem = 0, deathCount = 0, beans, apprentice = 0, master = 0, bookCover, beansNum, beansRange, MobVac = 0, mega_times = 0;

    private short level, job, mulung_energy, combo, force, availableCP, fatigue, totalCP, hpApUsed, scrolledPosition,
            kaiserCombo, xenonSurplus, exceed, exceedAttack = 0, chattype = 0;
    private byte gmLevel, gender, initialSpawnPoint, skinColor, guildrank = 5, allianceRank = 5,
            world, fairyExp, numClones, subcategory, cardStack, runningStack, runningBless = 0;
    private boolean invincible, canTalk, clone, followinitiator, followon, smega, hasSummon, keyvalue_changed = false,
            changed_wishlist, changed_trocklocations, changed_regrocklocations, changed_hyperrocklocations, changed_skillmacros, changed_achievements, changed_moonlight_achievements,
            changed_savedlocations, changed_questinfo, changed_skills, changed_reports, changed_extendedSlots, update_skillswipe,
            DebugMessage, canSetBeansNum;
    private final byte lastWorld = -3;
    private final boolean innerskill_changed = true;
    private static String[] ariantroomleader = new String[3]; // AriantPQ
    private int[] wishlist, rocks, savedLocations, regrocks, hyperrocks, remainingSp = new int[10], remainingHSp = new int[3], friendshippoints = new int[4];
    private static int[] ariantroomslot = new int[3]; // AriantPQ
    private byte[] petStore;
    private Point old;
    private MonsterFamiliar summonedFamiliar;
    private transient AtomicInteger inst, insd;
    private MapleShop azwanShopList;
    private MapleImp[] imps;
    private transient WeakReference<MapleCharacter>[] clones;
    private transient Set<MapleMonster> controlled;
    private transient Set<MapleMapObject> visibleMapObjects;
    private transient MapleAndroid android;
    private CashShop cs;
    private transient Deque<MapleCarnivalChallenge> pendingCarnivalRequests;
    private transient MapleCarnivalParty carnivalParty;
    private BuddyList buddylist;
    private MonsterBook monsterbook;
    private transient CheatTracker anticheat;
    private transient MapleLieDetector antiMacro;
    private MapleClient client;
    private transient MapleParty party;
    private PlayerStats stats;
    private final MapleCharacterCards characterCard;
    private transient MapleMap map;
    private transient MapleShop shop;
    private transient MapleDragon dragon;
    private transient MapleHaku haku;
    private transient MapleExtractor extractor;
    private transient RockPaperScissors rps;
    private MapleStorage storage;
    private transient MapleTrade trade;
    private MapleMount mount;
    private MapleMessenger messenger;
    private transient IMaplePlayerShop playerShop;
    private MapleGuildCharacter mgc;
    private MapleFamilyCharacter mfc;
    private MapleMarriage marriage;
    private MapleCoreAura coreAura;
    private MapleKeyLayout keylayout;
    private transient EventInstanceManager eventInstance;
    private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();
    private final Lock rL = mutex.readLock(), wL = mutex.writeLock();
    private transient EventManager eventInstanceAzwan;
    private MapleInventory[] inventory;
    private SkillMacro[] skillMacros = new SkillMacro[5];
    private transient ScheduledFuture<?> mapTimeLimitTask;
    private transient Event_PyramidSubway pyramidSubway = null;
    private final EnumMap<MapleTraitType, MapleTrait> traits;
    private Map<Integer, MonsterFamiliar> familiars;
    private final Map<Integer, String> entered = new LinkedHashMap<>();
    private final Map<MapleQuest, MapleQuestStatus> quests;
    private Map<Integer, String> questinfo;
    private transient Map<Skill, SkillEntry> pendingSkills = null;
    private final Map<Skill, SkillEntry> skills;
    private transient Map<MapleBuffStat, MapleBuffStatValueHolder> effects;
    private final Map<String, String> CustomValues = new HashMap<>();
    private transient Map<Integer, MapleCoolDownValueHolder> coolDowns;
    private transient Map<MapleDisease, MapleDiseaseValueHolder> diseases;
    private Map<ReportType, Integer> reports;
    private List<MaplePotionPot> potionPots;
    private final List<MapleCharacter> chars = new LinkedList<>();
    private transient List<LifeMovementFragment> lastres;
    private List<Integer> lastmonthfameids, lastmonthbattleids, extendedSlots;
    private List<MapleDoor> doors;
    private List<MechDoor> mechDoors;
    private List<MaplePet> pets;
    private List<Item> rebuy;
    private transient List<MapleSummon> summons;
    private List<InnerSkillValueHolder> innerSkills;
    private List<Integer> finishedAchievements;
    private List<Integer> finishedMoonlightAchievements;
    private transient List<Integer> pendingExpiration = null;
    private List<Pair<Integer, Boolean>> stolenSkills = new ArrayList<>();
    private transient Map<Integer, Integer> linkMobs;
    private transient ReentrantReadWriteLock visibleMapObjectsLock;
    private transient ReentrantReadWriteLock summonsLock;
    private transient ReentrantReadWriteLock controlledLock;
    public transient static ScheduledFuture<?> XenonSupplyTask = null;
    private Map<String, Integer> seniorBox = new HashMap<>();
    private Map<String, Integer> primaryBox = new HashMap<>();

    private MapleCharacter(final boolean ChannelServer) {
        setStance(0);
        setPosition(new Point(0, 0));

        inventory = new MapleInventory[MapleInventoryType.values().length];
        for (MapleInventoryType type : MapleInventoryType.values()) {
            inventory[type.ordinal()] = new MapleInventory(type);
        }
        quests = new LinkedHashMap<>(); // Stupid erev quest.
        skills = new LinkedHashMap<>(); //Stupid UAs.
        stats = new PlayerStats();
        innerSkills = new LinkedList<>();
        azwanShopList = null;
        characterCard = new MapleCharacterCards();
        for (int i = 0; i < remainingSp.length; i++) {
            remainingSp[i] = 0;
        }
        for (int i = 0; i < remainingHSp.length; i++) {
            remainingHSp[i] = 0;
        }
        traits = new EnumMap<>(MapleTraitType.class);
        for (MapleTraitType t : MapleTraitType.values()) {
            traits.put(t, new MapleTrait(t));
        }
        if (ChannelServer) {
            changed_reports = false;
            changed_skills = false;
            changed_achievements = false;
            changed_moonlight_achievements = false;
            changed_wishlist = false;
            changed_trocklocations = false;
            changed_regrocklocations = false;
            changed_hyperrocklocations = false;
            changed_skillmacros = false;
            changed_savedlocations = false;
            changed_extendedSlots = false;
            changed_questinfo = false;
            update_skillswipe = false;
            scrolledPosition = 0;
            lastCombo = 0;
            mulung_energy = 0;
            combo = 0;
            force = 0;
            keydown_skill = 0;
            nextConsume = 0;
            pqStartTime = 0;
            fairyExp = 0;
            cardStack = 0;
            runningStack = 1;
            mapChangeTime = 0;
            lastRecoveryTime = 0;
            lastDragonBloodTime = 0;
            lastBerserkTime = 0;
            lastFishingTime = 0;
            lastFairyTime = 0;
            lastHPTime = 0;
            lastMPTime = 0;
            lastFamiliarEffectTime = 0;
            lastCheckPeriodTime = 0;
            old = new Point(0, 0);
            coconutteam = 0;
            followid = 0;
            battleshipHP = 0;
            marriageItemId = 0;
            marriage = null;
            fallcounter = 0;
            challenge = 0;
            dotHP = 0;
            lastSummonTime = 0;
            hasSummon = false;
            invincible = false;
            canTalk = true;
            clone = false;
            followinitiator = false;
            followon = false;
            rebuy = new ArrayList<>();
            linkMobs = new HashMap<>();
            finishedAchievements = new ArrayList<>();
            finishedMoonlightAchievements = new ArrayList<>();
            reports = new EnumMap<>(ReportType.class);
            teleportname = "";
            smega = true;
            petStore = new byte[3];
            for (int i = 0; i < petStore.length; i++) {
                petStore[i] = (byte) -1;
            }
            wishlist = new int[30];
            rocks = new int[10];
            regrocks = new int[5];
            hyperrocks = new int[13];
            imps = new MapleImp[3];
            clones = new WeakReference[5]; //for now
            for (int i = 0; i < clones.length; i++) {
                clones[i] = new WeakReference<>(null);
            }
            familiars = new LinkedHashMap<>();
            extendedSlots = new ArrayList<>();
            effects = new ConcurrentEnumMap<>(MapleBuffStat.class);
            coolDowns = new LinkedHashMap<>();
            diseases = new ConcurrentEnumMap<>(MapleDisease.class);
            inst = new AtomicInteger(0);// 1 = NPC/ Quest, 2 = Donald, 3 = Hired Merch store, 4 = Storage
            insd = new AtomicInteger(-1);
            keylayout = new MapleKeyLayout();
            doors = new ArrayList<>();
            mechDoors = new ArrayList<>();
            controlled = new LinkedHashSet<>();
            controlledLock = new ReentrantReadWriteLock();
            summons = new LinkedList<>();
            summonsLock = new ReentrantReadWriteLock();
            visibleMapObjects = new LinkedHashSet<>();
            visibleMapObjectsLock = new ReentrantReadWriteLock();
            pendingCarnivalRequests = new LinkedList<>();

            savedLocations = new int[SavedLocationType.values().length];
            for (int i = 0; i < SavedLocationType.values().length; i++) {
                savedLocations[i] = -1;
            }
            questinfo = new LinkedHashMap<>();
            pets = new ArrayList<>();
            friendshippoints = new int[4];
            coreAura = new MapleCoreAura(id, 24 * 60);
            potionPots = new ArrayList<>();
        }
    }

    public static MapleCharacter getDefault(final MapleClient client, final JobType type) {
        MapleCharacter ret = new MapleCharacter(false);
        ret.client = client;
        ret.map = null;
        ret.exp = 0;
        ret.gmLevel = (byte) (ServerConstants.isBetaForAdmins ? 4 : 0);
        client.setGm(ServerConstants.isBetaForAdmins);
        ret.job = (short) type.id;
        ret.meso = 0;
        ret.level = 1;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.buddylist = new BuddyList((byte) 50);

        ret.stats.str = 12;
        ret.stats.dex = 5;
        ret.stats.int_ = 4;
        ret.stats.luk = 4;
        ret.stats.maxhp = 50;
        ret.stats.hp = 50;
        ret.stats.maxmp = 50;
        ret.stats.mp = 50;
        ret.gachexp = 0;
        ret.friendshippoints = new int[]{0, 0, 0, 0};
        ret.friendshiptoadd = 0;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");) {
            ps.setInt(1, ret.accountid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret.client.setAccountName(rs.getString("name"));
                    ret.nxcredit = rs.getInt("nxCredit");
                    ret.acash = rs.getInt("ACash");
                    ret.maplepoints = rs.getInt("mPoints");
                    ret.points = rs.getInt("points");
                    ret.vpoints = rs.getInt("vpoints");
                    ret.epoints = rs.getInt("epoints");
                    ret.dpoints = rs.getInt("dpoints");
                }
            }
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }

        return ret;
    }

    public static MapleCharacter ReconstructChr(final CharacterTransfer ct, final MapleClient client, final boolean isChannel) {
        final MapleCharacter ret = new MapleCharacter(true); // Always true, it's change channel
        ret.client = client;
        if (!isChannel) {
            ret.client.setChannel(ct.channel);
        }
        ret.mega_times = ct.mega_times;
        ret.bookCover = ct.mBookCover;
        ret.beans = ct.beans;
        ret.DebugMessage = ct.DebugMessage;
        ret.id = ct.characterid;
        ret.name = ct.name;
        ret.level = ct.level;
        ret.fame = ct.fame;

        ret.CRand = new PlayerRandomStream();

        ret.stats.str = ct.str;
        ret.stats.dex = ct.dex;
        ret.stats.int_ = ct.int_;
        ret.stats.luk = ct.luk;
        ret.stats.maxhp = ct.maxhp;
        ret.stats.maxmp = ct.maxmp;
        ret.stats.hp = ct.hp;
        ret.stats.mp = ct.mp;

        ret.characterCard.setCards(ct.cardsInfo);

        ret.chalktext = ct.chalkboard;
        ret.gmLevel = ct.gmLevel;
        ret.exp = ret.level >= ret.maxLevel ? 0 : ct.exp;
        ret.hpApUsed = ct.hpApUsed;
        ret.remainingSp = ct.remainingSp;
        ret.remainingHSp = ct.remainingHSp;
        ret.remainingAp = ct.remainingAp;
        ret.meso = ct.meso;
        ret.stolenSkills = ct.stolenSkills;
        ret.skinColor = ct.skinColor;
        ret.gender = ct.gender;
        ret.job = ct.job;
        ret.hair = ct.hair;
        ret.face = ct.face;
        ret.faceMarking = ct.faceMarking;
        ret.ears = ct.ears;
        ret.tail = ct.tail;
        ret.elf = ct.elf;
        ret.accountid = ct.accountid;
        ret.totalWins = ct.totalWins;
        ret.totalLosses = ct.totalLosses;
        client.setAccID(ct.accountid);
        ret.mapid = ct.mapid;
        ret.initialSpawnPoint = ct.initialSpawnPoint;
        ret.world = ct.world;
        ret.guildid = ct.guildid;
        ret.guildrank = ct.guildrank;
        ret.guildContribution = ct.guildContribution;
        ret.allianceRank = ct.alliancerank;
        ret.points = ct.points;
        ret.vpoints = ct.vpoints;
        ret.epoints = ct.epoints;
        ret.dpoints = ct.dpoints;
        ret.fairyExp = ct.fairyExp;
        ret.cardStack = ct.cardStack;
        ret.marriageId = ct.marriageId;
        ret.marriage = ct.marriage;
        ret.currentrep = ct.currentrep;
        ret.totalrep = ct.totalrep;
        ret.gachexp = ct.gachexp;
        ret.honourExp = ct.honourexp;
        ret.honorLevel = ct.honourlevel;
        ret.innerSkills = (LinkedList<InnerSkillValueHolder>) ct.innerSkills;
        ret.azwanShopList = (MapleShop) ct.azwanShopList;
        ret.pvpExp = ct.pvpExp;
        ret.pvpPoints = ct.pvpPoints;
        /*
         * Start of Custom Feature
         */
        ret.reborns = ct.reborns;
        ret.apstorage = ct.apstorage;
        /*
         * End of Custom Feature
         */
        ret.makeMFC(ct.familyid, ct.seniorid, ct.junior1, ct.junior2);
        if (ret.guildid > 0) {
            ret.mgc = new MapleGuildCharacter(ret);
        }
        ret.fatigue = ct.fatigue;
        ret.buddylist = new BuddyList(ct.buddysize);
        ret.subcategory = ct.subcategory;

        if (isChannel) {
            final MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) { //char is on a map that doesn't exist warp it to spinel forest
                ret.map = mapFactory.getMap(950000100);
            } else if (ret.map.getForcedReturnId() != 999999999 && ret.map.getForcedReturnMap() != null && ret.mapid != GameConstants.JAIL) {
                ret.map = ret.map.getForcedReturnMap();
                if (ret.map.getForcedReturnId() == 4000000) {
                    ret.initialSpawnPoint = 0;
                }
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());

            final int messengerid = ct.messengerid;
            if (messengerid > 0) {
                ret.messenger = World.Messenger.getMessenger(messengerid);
            }
        } else {

            ret.messenger = null;
        }
        int partyid = ct.partyid;
        if (partyid >= 0) {
            MapleParty party = World.Party.getParty(partyid);
            if (party != null && party.getMemberById(ret.id) != null) {
                ret.party = party;
            }
        }

        MapleQuestStatus queststatus_from;
        for (final Map.Entry<Integer, Object> qs : ct.Quest.entrySet()) {
            queststatus_from = (MapleQuestStatus) qs.getValue();
            queststatus_from.setQuest(qs.getKey());
            ret.quests.put(queststatus_from.getQuest(), queststatus_from);
        }
        for (final Map.Entry<Integer, SkillEntry> qs : ct.Skills.entrySet()) {
            ret.skills.put(SkillFactory.getSkill(qs.getKey()), qs.getValue());
        }
        for (final Integer zz : ct.finishedAchievements) {
            ret.finishedAchievements.add(zz);
        }
        for (final Integer zz : ct.finishedMoonlightAchievements) {
            ret.finishedMoonlightAchievements.add(zz);
        }
        for (Entry<MapleTraitType, Integer> t : ct.traits.entrySet()) {
            ret.traits.get(t.getKey()).setExp(t.getValue());
        }
        for (final Map.Entry<Byte, Integer> qs : ct.reports.entrySet()) {
            ret.reports.put(ReportType.getById(qs.getKey()), qs.getValue());
        }
        ret.monsterbook = new MonsterBook(ct.mbook);
        ret.inventory = (MapleInventory[]) ct.inventorys;
        ret.BlessOfFairy_Origin = ct.BlessOfFairy;
        ret.BlessOfEmpress_Origin = ct.BlessOfEmpress;
        ret.skillMacros = (SkillMacro[]) ct.skillmacro;
        ret.petStore = ct.petStore;
        ret.keylayout = new MapleKeyLayout(ct.keymap);
        ret.questinfo = ct.InfoQuest;
        ret.familiars = ct.familiars;
        ret.savedLocations = ct.savedlocation;
        ret.wishlist = ct.wishlist;
        ret.rocks = ct.rocks;
        ret.regrocks = ct.regrocks;
        ret.hyperrocks = ct.hyperrocks;
        ret.buddylist.loadFromTransfer(ct.buddies);
        ret.keydown_skill = 0; // Keydown skill can't be brought over
        ret.lastfametime = ct.lastfametime;
        ret.lastmonthfameids = ct.famedcharacters;
        ret.lastmonthbattleids = ct.battledaccs;
        ret.extendedSlots = ct.extendedSlots;
        ret.storage = (MapleStorage) ct.storage;
        ret.cs = (CashShop) ct.cs;
        client.setAccountName(ct.accountname);
        ret.nxcredit = ct.nxCredit;
        ret.acash = ct.ACash;
        ret.maplepoints = ct.MaplePoints;
        ret.numClones = ct.clonez;
        ret.imps = ct.imps;
        ret.anticheat = (CheatTracker) ct.anticheat;
        ret.anticheat.start(ret);
        ret.rebuy = ct.rebuy;
        ret.antiMacro = ((MapleLieDetector) ct.antiMacro);
        ret.mount = new MapleMount(ret, ct.mount_itemid, PlayerStats.getSkillByJob(1004, ret.job), ct.mount_Fatigue, ct.mount_level, ct.mount_exp);
        ret.expirationTask(false, false);
        ret.stats.recalcLocalStats(true, ret);
        client.setTempIP(ct.tempIP);

        return ret;
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver) {
        return loadCharFromDB(charid, client, channelserver, null);
    }

    /**
     * 加载角色信息。
     * @param charid
     * @param client
     * @param channelserver
     * @param cads
     * @return
     */
    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver, final Map<Integer, CardData> cads) {
        final MapleCharacter ret = new MapleCharacter(channelserver);
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            ret.client = client;
            ret.id = charid;
            PreparedStatement ps = null;
            PreparedStatement pse;
            ResultSet rs = null;

            try {
                ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    throw new RuntimeException("Loading the Char Failed (char not found)");
                }
                ret.mega_times = rs.getInt("megatimes");
                ret.beans = rs.getInt("beans");
                ret.name = rs.getString("name");
                ret.level = rs.getShort("level");
                ret.fame = rs.getInt("fame");

                ret.stats.str = rs.getShort("str");
                ret.stats.dex = rs.getShort("dex");
                ret.stats.int_ = rs.getShort("int");
                ret.stats.luk = rs.getShort("luk");
                ret.stats.maxhp = rs.getInt("maxhp");
                ret.stats.maxmp = rs.getInt("maxmp");
                ret.stats.hp = rs.getInt("hp");
                ret.stats.mp = rs.getInt("mp");
                ret.job = rs.getShort("job");
                ret.gmLevel = rs.getByte("gm");
                ret.exp = ret.level >= ret.maxLevel ? 0 : rs.getInt("exp");
                ret.hpApUsed = rs.getShort("hpApUsed");
                String[] sp = rs.getString("sp").split(",");
                for (int i = 0; i < ret.remainingSp.length; i++) {
                    ret.remainingSp[i] = Integer.parseInt(sp[i]);
                }
                String[] hsp = rs.getString("hsp").split(",");
                for (int i = 0; i < ret.remainingHSp.length; i++) {
                    ret.remainingHSp[i] = Integer.parseInt(hsp[i]);
                }
                ret.remainingAp = rs.getShort("ap");
                ret.meso = rs.getInt("meso");
                ret.skinColor = rs.getByte("skincolor");
                ret.gender = rs.getByte("gender");

                ret.hair = rs.getInt("hair");
                ret.face = rs.getInt("face");
                ret.faceMarking = rs.getInt("faceMarking");
                ret.ears = rs.getInt("ears");
                ret.bookCover = rs.getInt("tail");
                ret.elf = rs.getInt("elf");
                ret.accountid = rs.getInt("accountid");
                client.setAccID(ret.accountid);
                ret.mapid = rs.getInt("map");
                ret.initialSpawnPoint = rs.getByte("spawnpoint");
                ret.world = rs.getByte("world");
                ret.guildid = rs.getInt("guildid");
                ret.guildrank = rs.getByte("guildrank");
                ret.allianceRank = rs.getByte("allianceRank");
                ret.guildContribution = rs.getInt("guildContribution");
                ret.totalWins = rs.getInt("totalWins");
                ret.totalLosses = rs.getInt("totalLosses");
                ret.currentrep = rs.getInt("currentrep");
                ret.totalrep = rs.getInt("totalrep");
                ret.makeMFC(rs.getInt("familyid"), rs.getInt("seniorid"), rs.getInt("junior1"), rs.getInt("junior2"));
                if (ret.guildid > 0) {
                    ret.mgc = new MapleGuildCharacter(ret);
                }
                ret.gachexp = rs.getInt("gachexp");
                ret.buddylist = new BuddyList(rs.getByte("buddyCapacity"));
                ret.honourExp = rs.getInt("honourExp");
                ret.honorLevel = rs.getInt("honourLevel");
                ret.subcategory = rs.getByte("subcategory");
                ret.mount = new MapleMount(ret, 0, PlayerStats.getSkillByJob(1004, ret.job), (byte) 0, (byte) 1, 0);
                ret.rank = rs.getInt("rank");
                ret.rankMove = rs.getInt("rankMove");
                ret.jobRank = rs.getInt("jobRank");
                ret.jobRankMove = rs.getInt("jobRankMove");
                ret.marriageId = rs.getInt("marriageId");
                ret.fatigue = rs.getShort("fatigue");
                ret.pvpExp = rs.getInt("pvpExp");
                ret.pvpPoints = rs.getInt("pvpPoints");
                ret.friendshiptoadd = rs.getInt("friendshiptoadd");
                ret.chattype = rs.getShort("chatcolour");
                /*
                 * Start of Custom Features
                 */
                ret.reborns = rs.getInt("reborns");
                ret.apstorage = rs.getInt("apstorage");
                /*
                 * End of Custom Features
                 */
//            for (MapleTrait t : ret.traits.values()) {
//                t.setExp(rs.getInt(t.getType().name()));
//            }
                if (channelserver) {
                    ret.CRand = new PlayerRandomStream();
                    ret.anticheat = new CheatTracker(ret);
                    ret.antiMacro = new MapleLieDetector(ret);
                    MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
                    ret.map = mapFactory.getMap(ret.mapid);
                    if (ret.map == null) { //char is on a map that doesn't exist warp it to spinel forest
                        ret.map = mapFactory.getMap(100000000);
                    }
                    MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
                    if (portal == null) {
                        portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                        ret.initialSpawnPoint = 0;
                    }
                    ret.setPosition(portal.getPosition());

                    int partyid = rs.getInt("party");
                    if (partyid >= 0) {
                        MapleParty party = World.Party.getParty(partyid);
                        if (party != null && party.getMemberById(ret.id) != null) {
                            ret.party = party;
                        }
                    }
                    String[] pets = rs.getString("pets").split(",");
                    for (int i = 0; i < ret.petStore.length; i++) {
                        ret.petStore[i] = Byte.parseByte(pets[i]);
                    }
                    String[] friendshippoints = rs.getString("friendshippoints").split(",");
                    for (int i = 0; i < 4; i++) {
                        ret.friendshippoints[i] = Integer.parseInt(friendshippoints[i]);
                    }
                    rs.close();
                    ps.close();
                    ps = con.prepareStatement("SELECT * FROM achievements WHERE accountid = ?");
                    ps.setInt(1, ret.accountid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ret.finishedAchievements.add(rs.getInt("achievementid"));
                    }
                    ps.close();
                    rs.close();
                    ps = con.prepareStatement("SELECT * FROM moonlightachievements WHERE accountid = ?");
                    ps.setInt(1, ret.accountid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ret.finishedMoonlightAchievements.add(rs.getInt("achievementid"));
                    }
                    ps.close();
                    rs.close();

                    ps = con.prepareStatement("SELECT * FROM reports WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (ReportType.getById(rs.getByte("type")) != null) {
                            ret.reports.put(ReportType.getById(rs.getByte("type")), rs.getInt("count"));
                        }
                    }

                }
                rs.close();
                ps.close();

                /*if (ret.marriageId > 0) {
                 ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
                 ps.setInt(1, ret.marriageId);
                 rs = ps.executeQuery();
                 int partnerId = rs.getInt("id");
                 ret.marriage = new MapleMarriage(partnerId, ret.marriageItemId);
                 ret.marriage.setHusbandId(ret.gender == 0 ? ret.id : partnerId);
                 ret.marriage.setWifeId(ret.gender == 1 ? ret.id : partnerId);
                 String partnerName = rs.getString("name");
                 ret.marriage.setHusbandName(ret.gender == 0 ? ret.name : partnerName);
                 ret.marriage.setWifeName(ret.gender == 1 ? ret.name : partnerName);*/
 /*
                 * if (rs.next()) { ret.marriage = new
                 * MapleMarriage(rs.getInt("id"), rs.getInt("ring"));
                 * ret.marriage.setHusbandId(rs.getInt("husbandId"));
                 * ret.marriage.setWifeId(rs.getInt("husbandId"));
                 * ret.marriage.setHusbandName(rs.getString("husbandName"));
                 * ret.marriage.setWifeName(rs.getString("husbandName")); } else
                 * { ret.marriage = null; }
                 */
 /*rs.close();
                 ps.close();
                 }*/
                if (cads != null) { // so that we load only once.
                    ret.characterCard.setCards(cads);
                } else { // load
                    ret.characterCard.loadCards(client, channelserver);
                }

                ps = con.prepareStatement("SELECT * FROM queststatus WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                pse = con.prepareStatement("SELECT * FROM queststatusmobs WHERE queststatusid = ?");

                while (rs.next()) {
                    final int id = rs.getInt("quest");
                    final MapleQuest q = MapleQuest.getInstance(id);
                    final byte stat = rs.getByte("status");
                    if ((stat == 1 || stat == 2) && channelserver && (q == null || q.isBlocked())) { //bigbang
                        continue;
                    }
                    if (stat == 1 && channelserver && !q.canStart(ret, null)) { //bigbang
                        continue;
                    }
                    final MapleQuestStatus status = new MapleQuestStatus(q, stat);
                    final long cTime = rs.getLong("time");
                    if (cTime > -1) {
                        status.setCompletionTime(cTime * 1000);
                    }
                    status.setForfeited(rs.getInt("forfeited"));
                    status.setCustomData(rs.getString("customData"));
                    ret.quests.put(q, status);
                    pse.setInt(1, rs.getInt("queststatusid"));
                    try (ResultSet rsMobs = pse.executeQuery()) {
                        while (rsMobs.next()) {
                            status.setMobKills(rsMobs.getInt("mob"), rsMobs.getInt("count"));
                        }
                    }
                }
                rs.close();
                ps.close();
                pse.close();
                if (channelserver) {
                    ret.monsterbook = MonsterBook.loadCards(charid);
                    ps = con.prepareStatement("SELECT * FROM inventoryslot where characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        throw new RuntimeException("No Inventory slot column found in SQL. [inventoryslot]");
                    } else {
                        ret.getInventory(MapleInventoryType.EQUIP).setSlotLimit(rs.getByte("equip"));
                        ret.getInventory(MapleInventoryType.USE).setSlotLimit(rs.getByte("use"));
                        ret.getInventory(MapleInventoryType.SETUP).setSlotLimit(rs.getByte("setup"));
                        ret.getInventory(MapleInventoryType.ETC).setSlotLimit(rs.getByte("etc"));
                        ret.getInventory(MapleInventoryType.CASH).setSlotLimit(rs.getByte("cash"));
                    }
                    ps.close();
                    rs.close();
                    for (Pair<Item, MapleInventoryType> mit : ItemLoader.INVENTORY.loadItems(false, charid).values()) {
                        ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
                        if (mit.getLeft().getPet() != null) {
                            ret.pets.add(mit.getLeft().getPet());
                        }
                    }

                    //TODO {TEST} LOAD POTION POTS
                    /*
                     * ps = con.prepareStatement("SELECT * FROM potionpots WHERE cid
                     * = ?"); ps.setInt(1, ret.id); rs = ps.executeQuery();
                     * ret.potionPots = new ArrayList(); while (rs.next()) {
                     * MaplePotionPot pot = MaplePotionPot.loadFromResult(rs); if
                     * (pot != null) { ret.potionPots.add(pot); } } rs.close();
                     * ps.close();
                     */
                    ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
                    ps.setInt(1, ret.accountid);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        ret.getClient().setAccountName(rs.getString("name"));
                        ret.nxcredit = rs.getInt("nxCredit");
                        ret.acash = rs.getInt("ACash");
                        ret.maplepoints = rs.getInt("mPoints");
                        ret.points = rs.getInt("points");
                        ret.vpoints = rs.getInt("vpoints");
                        ret.epoints = rs.getInt("epoints");
                        ret.dpoints = rs.getInt("dpoints");

                        if (rs.getTimestamp("lastlogon") != null) {
                            final Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(rs.getTimestamp("lastlogon").getTime());
                        }
                        if (rs.getInt("banned") > 0) {
                            rs.close();
                            ps.close();
                            ret.getClient().getSession().close();
                            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                            throw new RuntimeException("Loading a banned character");
                        }
                        rs.close();
                        ps.close();

                        ps = con.prepareStatement("UPDATE accounts SET lastlogon = CURRENT_TIMESTAMP() WHERE id = ?");
                        ps.setInt(1, ret.accountid);
                        ps.executeUpdate();
                    } else {
                        rs.close();
                    }
                    ps.close();

                    ps = con.prepareStatement("SELECT * FROM questinfo WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        ret.questinfo.put(rs.getInt("quest"), rs.getString("customData"));
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT skillid, skilllevel, masterlevel, expiration FROM skills WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    Skill skil;
                    while (rs.next()) {
                        final int skid = rs.getInt("skillid");
                        skil = SkillFactory.getSkill(skid);
                        int skl = rs.getInt("skilllevel");
                        byte msl = rs.getByte("masterlevel");
                        if (skil != null && GameConstants.isApplicableSkill(skid)) {
                            if (skl > skil.getMaxLevel() && (skid < 92000000 || skid > 99999999)) {
                                if (!skil.isBeginnerSkill() && skil.canBeLearnedBy(ret.job) && !skil.isSpecialSkill()) {
                                    ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] += (skl - skil.getMaxLevel());
                                }
                                skl = (byte) skil.getMaxLevel();
                            }
                            if (msl > skil.getMaxLevel()) {
                                msl = (byte) skil.getMaxLevel();
                            }
                            ret.skills.put(skil, new SkillEntry(skl, msl, rs.getLong("expiration")));
                        } else if (skil == null) { //doesnt. exist. e.g. bb
                            if (!GameConstants.isBeginnerJob(skid / 10000) && skid / 10000 != 900 && skid / 10000 != 800 && skid / 10000 != 9000) {
                                ret.remainingSp[GameConstants.getSkillBookForSkill(skid)] += skl;
                            }
                        }
                    }
                    rs.close();
                    ps.close();

                    ret.expirationTask(false, true); //do it now

                    ps = con.prepareStatement("SELECT * FROM coreauras WHERE cid = ?");
                    ps.setInt(1, ret.id);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        ret.coreAura = new MapleCoreAura(ret.id, rs.getInt("expire"));
                        ret.coreAura.setStr(rs.getInt("str"));
                        ret.coreAura.setDex(rs.getInt("dex"));
                        ret.coreAura.setInt(rs.getInt("int"));
                        ret.coreAura.setLuk(rs.getInt("luk"));
                        ret.coreAura.setAtt(rs.getInt("att"));
                        ret.coreAura.setMagic(rs.getInt("magic"));
                        ret.coreAura.setTotal(rs.getInt("total"));
                    } else {
                        ret.coreAura = new MapleCoreAura(ret.id, 24 * 60);
                    }
                    rs.close();
                    ps.close();

                    // Bless of Fairy handling
                    ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ? ORDER BY level DESC");
                    ps.setInt(1, ret.accountid);
                    rs = ps.executeQuery();
                    int maxlevel_ = 0, maxlevel_2 = 0;
                    while (rs.next()) {
                        if (rs.getInt("id") != charid) { // Not this character
                            /*if (GameConstants.isKOC(rs.getShort("job"))) {
                             int maxlevel = (rs.getShort("level") / 5);

                             if (maxlevel > 24) {
                             maxlevel = 24;
                             }
                             if (maxlevel > maxlevel_2 || maxlevel_2 == 0) {
                             maxlevel_2 = maxlevel;
                             ret.BlessOfEmpress_Origin = rs.getString("name");
                             }
                             }*/
                            int maxlevel = (rs.getShort("level") / 10);

                            if (maxlevel > 20) {
                                maxlevel = 20;
                            }
                            if (maxlevel > maxlevel_ || maxlevel_ == 0) {
                                maxlevel_ = maxlevel;
                                ret.BlessOfFairy_Origin = rs.getString("name");
                            }

                        }
                    }

                    /*
                     * if (!compensate_previousSP) { for (Entry<Skill, SkillEntry>
                     * skill : ret.skills.entrySet()) { if
                     * (!skill.getKey().isBeginnerSkill() &&
                     * !skill.getKey().isSpecialSkill()) {
                     * ret.remainingSp[GameConstants.getSkillBookForSkill(skill.getKey().getId())]
                     * += skill.getValue().skillevel; skill.getValue().skillevel =
                     * 0; } } ret.setQuestAdd(MapleQuest.getInstance(170000), (byte)
                     * 0, null); //set it so never again }
                     */
                    if (ret.BlessOfFairy_Origin == null) {
                        ret.BlessOfFairy_Origin = ret.name;
                    }
                    ret.skills.put(SkillFactory.getSkill(GameConstants.getBOF_ForJob(ret.job)), new SkillEntry(maxlevel_, (byte) 0, -1));
                    /*if (SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)) != null) {
                     if (ret.BlessOfEmpress_Origin == null) {
                     ret.BlessOfEmpress_Origin = ret.BlessOfFairy_Origin;
                     }
                     ret.skills.put(SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)), new SkillEntry(maxlevel_2, (byte) 0, -1));
                     }*/
                    ps.close();
                    rs.close();
                    // END
                    ps = con.prepareStatement("SELECT skill_id, skill_level, max_level, rank, locked FROM inner_ability_skills WHERE player_id = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ret.innerSkills.add(new InnerSkillValueHolder(rs.getInt("skill_id"), rs.getByte("skill_level"), rs.getByte("max_level"), rs.getByte("rank"), rs.getBoolean("locked")));
                    }
                    ps = con.prepareStatement("SELECT * FROM skillmacros WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    int position;
                    while (rs.next()) {
                        position = rs.getInt("position");
                        SkillMacro macro = new SkillMacro(rs.getInt("skill1"), rs.getInt("skill2"), rs.getInt("skill3"), rs.getString("name"), rs.getInt("shout"), position);
                        ret.skillMacros[position] = macro;
                    }
                    rs.close();
                    ps.close();
                    /*
                     * ps = con.prepareStatement("SELECT victimid, skillid,
                     * skilllevel, slot, category FROM stolen_skills WHERE chrid =
                     * ?"); ps.setInt(1, charid); rs = ps.executeQuery(); int slot;
                     * while (rs.next()) { slot = rs.getInt("slot"); SkillSwipe ss =
                     * new SkillSwipe(rs.getInt("victimid"), rs.getInt("skillid"),
                     * rs.getInt("skilllevel"), rs.getInt("category"), slot);
                     * ret.skillSwipe[slot] = ss; } rs.close(); ps.close();
                     */
                    ps = con.prepareStatement("SELECT * FROM familiars WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getLong("expiry") <= System.currentTimeMillis()) {
                            continue;
                        }
                        ret.familiars.put(rs.getInt("familiar"), new MonsterFamiliar(charid, rs.getInt("id"), rs.getInt("familiar"), rs.getLong("expiry"), rs.getString("name"), rs.getInt("fatigue"), rs.getByte("vitality")));
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT `key`,`type`,`action` FROM keymap WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();

                    final Map<Integer, Pair<Byte, Integer>> keyb = ret.keylayout.Layout();
                    while (rs.next()) {
                        keyb.put(rs.getInt("key"), new Pair<>(rs.getByte("type"), rs.getInt("action")));
                    }
                    rs.close();
                    ps.close();
                    ret.keylayout.unchanged();

                    ps = con.prepareStatement("SELECT `locationtype`,`map` FROM savedlocations WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ret.savedLocations[rs.getInt("locationtype")] = rs.getInt("map");
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT `characterid_to`,`when` FROM famelog WHERE characterid = ? AND DATEDIFF(NOW(),`when`) < 30");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    ret.lastfametime = 0;
                    ret.lastmonthfameids = new ArrayList<>(31);
                    while (rs.next()) {
                        ret.lastfametime = Math.max(ret.lastfametime, rs.getTimestamp("when").getTime());
                        ret.lastmonthfameids.add(rs.getInt("characterid_to"));
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT `accid_to`,`when` FROM battlelog WHERE accid = ? AND DATEDIFF(NOW(),`when`) < 30");
                    ps.setInt(1, ret.accountid);
                    rs = ps.executeQuery();
                    ret.lastmonthbattleids = new ArrayList<>();
                    while (rs.next()) {
                        ret.lastmonthbattleids.add(rs.getInt("accid_to"));
                    }
                    rs.close();
                    ps.close();

                    // 额外背包空间
//                    ps = con.prepareStatement("SELECT `itemId` FROM extendedSlots WHERE characterid = ?");
//                    ps.setInt(1, charid);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        ret.extendedSlots.add(rs.getInt("itemId"));
//                    }
//                    rs.close();
//                    ps.close();

                    ret.buddylist.loadFromDb(charid);
                    ret.storage = MapleStorage.loadStorage(ret.accountid);
                    ret.cs = new CashShop(ret.accountid, charid, ret.getJob());

                    ps = con.prepareStatement("SELECT sn FROM wishlist WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    int i = 0;
                    while (rs.next()) {
                        ret.wishlist[i] = rs.getInt("sn");
                        i++;
                    }
                    while (i < 30) {
                        ret.wishlist[i] = 0;
                        i++;
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT mapid FROM trocklocations WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    int r = 0;
                    while (rs.next()) {
                        ret.rocks[r] = rs.getInt("mapid");
                        r++;
                    }
                    while (r < 10) {
                        ret.rocks[r] = 999999999;
                        r++;
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT mapid FROM regrocklocations WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    r = 0;
                    while (rs.next()) {
                        ret.regrocks[r] = rs.getInt("mapid");
                        r++;
                    }
                    while (r < 5) {
                        ret.regrocks[r] = 999999999;
                        r++;
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT mapid FROM hyperrocklocations WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    r = 0;
                    while (rs.next()) {
                        ret.hyperrocks[r] = rs.getInt("mapid");
                        r++;
                    }
                    while (r < 13) {
                        ret.hyperrocks[r] = 999999999;
                        r++;
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT * from stolen WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        ret.stolenSkills.add(new Pair<>(rs.getInt("skillid"), rs.getInt("chosen") > 0));
                    }
                    rs.close();
                    ps.close();
                    ps = con.prepareStatement("SELECT * FROM imps WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    r = 0;
                    while (rs.next()) {
                        ret.imps[r] = new MapleImp(rs.getInt("itemid"));
                        ret.imps[r].setLevel(rs.getByte("level"));
                        ret.imps[r].setState(rs.getByte("state"));
                        ret.imps[r].setCloseness(rs.getShort("closeness"));
                        ret.imps[r].setFullness(rs.getShort("fullness"));
                        r++;
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("SELECT * FROM mountdata WHERE characterid = ?");
                    ps.setInt(1, charid);
                    rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new RuntimeException("No mount data found on SQL column");
                    }
                    final Item mount = ret.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18);
                    ret.mount = new MapleMount(ret, mount != null ? mount.getItemId() : 0, 80001000, rs.getByte("Fatigue"), rs.getByte("Level"), rs.getInt("Exp"));
                    ps.close();
                    rs.close();

                    ret.stats.recalcLocalStats(true, ret);
                } else { // Not channel server
                    for (Pair<Item, MapleInventoryType> mit : ItemLoader.INVENTORY.loadItems(true, charid).values()) {
                        ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
                    }
                    ret.stats.recalcPVPRank(ret);
                }
            } catch (SQLException ess) {
                ess.printStackTrace();
                System.out.println("Failed to load character.." + ess);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", ess);
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ignore) {
                }
            }
        } catch (SQLException essx) {
            System.out.println("Failed to load character.." + essx);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", essx);
        }
        return ret;
    }

    public static int getQuestKillCount(MapleCharacter chr, final int mobid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement pse;
            try (PreparedStatement ps = con.prepareStatement("SELECT queststatusid FROM queststatus WHERE characterid = ?")) {
                ResultSet rse;
                try (ResultSet rs = ps.executeQuery()) {
                    pse = con.prepareStatement("SELECT count FROM queststatusmobs WHERE queststatusid = ?");
                    rse = pse.executeQuery();
                    while (rs.next()) {
                        return rse.getInt("count");
                    }
                }
                rse.close();
            }
            pse.close();
        } catch (SQLException e) {
            System.err.println("getQuestKillCount" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return -1;
    }

    public static void saveNewCharToDB(final MapleCharacter chr, final JobType type, short db) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = null;
            PreparedStatement pse = null;
            ResultSet rs = null;

            try {

                con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                con.setAutoCommit(false);

                ps = con.prepareStatement("INSERT INTO characters (level, str, dex, luk, `int`, hp, mp, maxhp, maxmp, sp, hsp, ap, skincolor, gender, job, hair, face, faceMarking, ears, tail, map, meso, party, buddyCapacity, pets, subcategory, elf, friendshippoints, chatcolour, gm, accountid, name, world, beans, megatimes)"
                        + "                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", DBConPool.RETURN_GENERATED_KEYS);
                int index = 0;
//            if (GameConstants.isZero(chr.getJob())) {
//               chr.level = 100;
//            }
                ps.setInt(++index, chr.level); // Level
                final PlayerStats stat = chr.stats;
                ps.setInt(++index, stat.getStr()); // Str
                ps.setInt(++index, stat.getDex()); // Dex
                ps.setInt(++index, stat.getInt()); // Int
                ps.setInt(++index, stat.getLuk()); // Luk
                ps.setInt(++index, stat.getHp()); // HP
                ps.setInt(++index, stat.getMp());
                ps.setInt(++index, stat.getMaxHp()); // MP
                ps.setInt(++index, stat.getMaxMp());
                final StringBuilder sps = new StringBuilder();
                for (int i = 0; i < chr.remainingSp.length; i++) {
                    sps.append(chr.remainingSp[i]);
                    sps.append(",");
                }
                final String sp = sps.toString();
                ps.setString(++index, sp.substring(0, sp.length() - 1));
                final StringBuilder hsps = new StringBuilder();
                for (int i = 0; i < chr.remainingHSp.length; i++) {
                    hsps.append(chr.remainingHSp[i]);
                    hsps.append(",");
                }
                final String hsp = hsps.toString();
                ps.setString(++index, hsp.substring(0, hsp.length() - 1));
                if (chr.remainingAp > (999 + 16) - (chr.str + chr.dex + chr.int_ + chr.luk)) {
                    chr.remainingAp = (999 + 16) - (chr.str + chr.dex + chr.int_ + chr.luk);
                }
                ps.setShort(++index, (short) chr.remainingAp); // Remaining AP
                ps.setByte(++index, chr.skinColor);
                ps.setByte(++index, chr.gender);
                ps.setInt(++index, chr.job);
                ps.setInt(++index, chr.hair);
                ps.setInt(++index, chr.face);
                ps.setInt(++index, chr.faceMarking);
                ps.setInt(++index, chr.ears);
                ps.setInt(++index, chr.bookCover);
                if (db < 0 || db > 10) {
                    db = 0;
                }
                ps.setInt(++index, db == 2 ? 3000600 : type.map); //TODO fix jett, db tutorials
                ps.setInt(++index, chr.meso); // Meso
                ps.setInt(++index, -1); // Party
                ps.setByte(++index, chr.buddylist.getCapacity()); // Buddylist
                ps.setString(++index, "-1,-1,-1");
                ps.setInt(++index, db); //for now
                ps.setInt(++index, chr.elf);
                ps.setString(++index, chr.friendshippoints[0] + "," + chr.friendshippoints[1] + "," + chr.friendshippoints[2] + "," + chr.friendshippoints[3]);
                ps.setShort(++index, (short) 0);
                if (chr.isGM()) {
                    ps.setByte(++index, (byte) 6);
                } else {
                    ps.setByte(++index, (byte) 0);
                }
                ps.setInt(++index, chr.getAccountID());
                ps.setString(++index, chr.name);
                ps.setByte(++index, chr.world);
                ps.setInt(++index, chr.beans);
                ps.setInt(++index, chr.mega_times);
                ps.executeUpdate();

                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    chr.id = rs.getInt(1);
                } else {
                    ps.close();
                    rs.close();
                    throw new DatabaseException("生成新角色到数据库出错...");
                }
                ps.close();
                rs.close();
                ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", DBConPool.RETURN_GENERATED_KEYS);
                pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                ps.setInt(1, chr.id);
                for (final MapleQuestStatus q : chr.quests.values()) {
                    ps.setInt(2, q.getQuest().getId());
                    ps.setInt(3, q.getStatus());
                    ps.setInt(4, (int) (q.getCompletionTime() / 1000));
                    ps.setInt(5, q.getForfeited());
                    ps.setString(6, q.getCustomData());
                    ps.execute();
                    rs = ps.getGeneratedKeys();
                    if (q.hasMobKills()) {
                        rs.next();
                        for (int mob : q.getMobKills().keySet()) {
                            pse.setInt(1, rs.getInt(1));
                            pse.setInt(2, mob);
                            pse.setInt(3, q.getMobKills(mob));
                            pse.execute();
                        }
                    }
                    rs.close();
                }
                ps.close();
                pse.close();

                ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, chr.id);

                for (final Entry<Skill, SkillEntry> skill : chr.skills.entrySet()) {
                    if (GameConstants.isApplicableSkill(skill.getKey().getId())) { //do not save additional skills
                        ps.setInt(2, skill.getKey().getId());
                        ps.setInt(3, skill.getValue().skillevel);
                        ps.setByte(4, skill.getValue().masterlevel);
                        ps.setLong(5, skill.getValue().expiration);
                        ps.execute();
                    }
                }
                ps.close();

                //TODO {TEST} SAVE NEW JETT CORE AURA
                ps = con.prepareStatement("INSERT INTO coreauras (cid, str, dex, `int`, luk, att, magic, total, expire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, chr.id);
                if (GameConstants.isJett(chr.job)) {
                    ps.setInt(2, 3);
                    ps.setInt(3, 3);
                    ps.setInt(4, 3);
                    ps.setInt(5, 3);
                    ps.setInt(6, 3);
                    ps.setInt(7, 3);
                    ps.setInt(8, 24 * 60);
                }

                ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, chr.id);
                ps.setByte(2, (byte) 32); // Eq
                ps.setByte(3, (byte) 32); // Use
                ps.setByte(4, (byte) 32); // Setup
                ps.setByte(5, (byte) 32); // ETC
                ps.setByte(6, (byte) 60); // Cash
                ps.execute();
                ps.close();

                ps = con.prepareStatement("INSERT INTO mountdata (characterid, `Level`, `Exp`, `Fatigue`) VALUES (?, ?, ?, ?)");
                ps.setInt(1, chr.id);
                ps.setByte(2, (byte) 1);
                ps.setInt(3, 0);
                ps.setByte(4, (byte) 0);
                ps.execute();
                ps.close();
                // old
                //final int[] array1 = {2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 23, 25, 26, 27, 31, 34, 37, 38, 41, 44, 45, 46, 50, 57, 59, 60, 61, 62, 63, 64, 65, 8, 9, 24, 30};
                //final int[] array2 = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 4, 5, 6, 6, 6, 6, 6, 6, 6, 4, 4, 4, 4};
                //final int[] array3 = {10, 12, 13, 18, 6, 11, 8, 5, 0, 4, 1, 19, 14, 15, 3, 17, 9, 20, 22, 50, 51, 52, 7, 53, 100, 101, 102, 103, 104, 105, 106, 16, 23, 24, 2};
                //int[] array1 = {18, 65, 2, 23, 3, 4, 5, 6, 16, 17, 19, 25, 26, 27, 31, 34, 35, 37, 38, 40, 43, 44, 45, 46, 50, 56, 59, 60, 61, 62, 63, 64, 57, 48, 29, 7, 24, 33, 41, 39, 8, 20, 21, 49};
                int[] array1 = {2, 3, 64, 4, 65, 5, 6, 7, 8, 13, 17, 16, 19, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 31, 34, 35, 33, 38, 39, 37, 43, 40, 41, 46, 47, 44, 45, 51, 50, 49, 48, 59, 57, 56, 63, 62, 61, 60};
                //int[] array2 = {4, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 5, 6, 6, 6, 6, 6, 6, 5, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4};
                int[] array2 = {4, 4, 6, 4, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 6, 5, 5, 6, 6, 6, 6};
                //int[] array3 = {0, 106, 10, 1, 12, 13, 18, 24, 8, 5, 4, 19, 14, 15, 2, 17, 11, 3, 20, 16, 9, 50, 51, 6, 7, 53, 100, 101, 102, 103, 104, 105, 54, 30, 52, 21, 25, 26, 23, 27, 29, 28, 31, 22};
                int[] array3 = {10, 12, 105, 13, 106, 18, 24, 21, 29, 33, 5, 8, 4, 0, 31, 28, 1, 34, 19, 25, 15, 14, 52, 2, 17, 11, 26, 20, 27, 3, 9, 16, 23, 6, 32, 50, 51, 35, 7, 22, 30, 100, 54, 53, 104, 103, 102, 101};
                /*
                 * 0 0 0 0 0 4 104 124 134 184 244 214 290 0 0 0 0 0 0 0 4 330 0 0 0
                 * 4 84 54 04 44 284 314 344 14 254 194 144 150 0 5 520 0 4 20 0 4
                 * 264 174 110 0 4 34 204 274 164 230 0 4 95 505 514 64 324 304 224
                 * 74 350 0 0 0 0 0 0 0 5 535 540 0 6 1006 1016 1026 1036 1046 1056
                 * 1060 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                 * 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                 */
                ps = con.prepareStatement("INSERT INTO keymap (characterid, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
                ps.setInt(1, chr.id);
                for (int i = 0; i < array1.length; i++) {
                    ps.setInt(2, array1[i]);
                    ps.setInt(3, array2[i]);
                    ps.setInt(4, array3[i]);
                    ps.execute();
                }
                ps.close();

                List<Pair<Item, MapleInventoryType>> listing = new ArrayList<>();
                for (final MapleInventory iv : chr.inventory) {
                    for (final Item item : iv.list()) {
                        listing.add(new Pair<>(item, iv.getType()));
                    }
                }
                ItemLoader.INVENTORY.saveItems(listing, con, chr.id);

                con.commit();
            } catch (SQLException | DatabaseException e) {
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                System.err.println("[charsave] Error saving character data");
                e.printStackTrace();
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
                    System.err.println("[newcharsave] Error Rolling Back");
                }
            } finally {
                try {
                    if (pse != null) {
                        pse.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.setAutoCommit(true);
                    con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                    System.err.println("[charsave] Error going back to autocommit mode");
                }
            }
        } catch (SQLException exx) {
            FileoutputUtil.outputFileError("logs/数据库异常.txt", exx);
        }
    }

    public void saveToDB(boolean dc, boolean fromcs) {
        if (isClone()) {
            return;
        }
        //Connection con = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = null;
            PreparedStatement pse = null;
            ResultSet rs = null;

            try {
                con.setTransactionIsolation(1);
                con.setAutoCommit(false);

                ps = con.prepareStatement("UPDATE characters SET level = ?, fame = ?, str = ?, dex = ?, luk = ?, `int` = ?, exp = ?, hp = ?, mp = ?, maxhp = ?, maxmp = ?, sp = ?, hsp = ?, ap = ?, gm = ?, skincolor = ?, gender = ?, job = ?, hair = ?, face = ?, faceMarking = ?, ears = ?, tail = ?, map = ?, meso = ?, hpApUsed = ?, spawnpoint = ?, party = ?, buddyCapacity = ?, pets = ?, subcategory = ?, marriageId = ?, totalrep = ?, gachexp = ?, fatigue = ?, charm = ?, charisma = ?, craft = ?, insight = ?, sense = ?, will = ?, totalwins = ?, totallosses = ?, pvpExp = ?, pvpPoints = ?, reborns = ?, apstorage = ?, elf = ?, honourExp = ?, honourLevel = ?, friendshippoints = ?, friendshiptoadd = ?, chatcolour = ?, name = ?, beans = ?, megatimes = ? WHERE id = ?", DBConPool.RETURN_GENERATED_KEYS);
                int index = 0;
                ps.setInt(++index, level);
                ps.setInt(++index, fame);
                ps.setInt(++index, stats.getStr());
                ps.setInt(++index, stats.getDex());
                ps.setInt(++index, stats.getLuk());
                ps.setInt(++index, stats.getInt());
                ps.setLong(++index, level >= maxLevel ? 0 : exp);
                ps.setInt(++index, stats.getHp() < 1 ? 50 : stats.getHp());
                ps.setInt(++index, stats.getMp());
                ps.setInt(++index, stats.getMaxHp());
                ps.setInt(++index, stats.getMaxMp());
                final StringBuilder sps = new StringBuilder();
                for (int i = 0; i < remainingSp.length; i++) {
                    sps.append(remainingSp[i]);
                    sps.append(",");
                }
                final String skillpoints = sps.toString();
                ps.setString(++index, skillpoints.substring(0, skillpoints.length() - 1));
                final StringBuilder hsps = new StringBuilder();
                for (int i = 0; i < remainingHSp.length; i++) {
                    hsps.append(remainingHSp[i]);
                    hsps.append(",");
                }
                final String hskillpoints = hsps.toString();
                ps.setString(++index, hskillpoints.substring(0, hskillpoints.length() - 1));
                ps.setInt(++index, remainingAp);
                ps.setByte(++index, gmLevel);
                ps.setByte(++index, skinColor);
                ps.setByte(++index, gender);
                ps.setInt(++index, job);
                ps.setInt(++index, hair);
                ps.setInt(++index, face);
                ps.setInt(++index, faceMarking);
                ps.setInt(++index, ears);
                ps.setInt(++index, bookCover);
                // ps.setInt(++index, tail);
                if (!fromcs && map != null) {
                    if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
                        if (getMapId() != GameConstants.JAIL) {
                            ps.setInt(++index, map.getForcedReturnId());
                        } else {
                            ps.setInt(++index, mapid);
                        }
                    } else {
                        ps.setInt(++index, stats.getHp() < 1 ? map.getReturnMapId() : map.getId());
                    }
                } else {
                    ps.setInt(++index, mapid);
                }
                ps.setInt(++index, meso);
                ps.setShort(++index, hpApUsed);
                if (map == null) {
                    ps.setByte(++index, (byte) 0);
                } else {
                    final MaplePortal closest = map.findClosestSpawnpoint(getTruePosition());
                    ps.setByte(++index, (byte) (closest != null ? closest.getId() : 0));
                }
                ps.setInt(++index, party == null ? -1 : party.getId());
                ps.setShort(++index, buddylist.getCapacity());
                final StringBuilder petz = new StringBuilder();
                int petLength = 0;
                for (final MaplePet pet : pets) {
                    if (pet.getSummoned()) {
                        pet.saveToDb();
                        petz.append(pet.getInventoryPosition());
                        petz.append(",");
                        petLength++;
                    }
                }
                while (petLength < 3) {
                    petz.append("-1,");
                    petLength++;
                }
                final String petstring = petz.toString();
                ps.setString(++index, petstring.substring(0, petstring.length() - 1));
                ps.setByte(++index, subcategory);
                ps.setInt(++index, marriageId);
                //  ps.setInt(++index, currentrep);
                ps.setInt(++index, totalrep);
                ps.setInt(++index, gachexp);
                ps.setShort(++index, fatigue);
                ps.setInt(++index, traits.get(MapleTraitType.charm).getTotalExp());
                ps.setInt(++index, traits.get(MapleTraitType.charisma).getTotalExp());
                ps.setInt(++index, traits.get(MapleTraitType.craft).getTotalExp());
                ps.setInt(++index, traits.get(MapleTraitType.insight).getTotalExp());
                ps.setInt(++index, traits.get(MapleTraitType.sense).getTotalExp());
                ps.setInt(++index, traits.get(MapleTraitType.will).getTotalExp());
                ps.setInt(++index, totalWins);
                ps.setInt(++index, totalLosses);
                ps.setInt(++index, pvpExp);
                ps.setInt(++index, pvpPoints);
                /*
                 * Start of Custom Features
                 */
                ps.setInt(++index, reborns);
                ps.setInt(++index, apstorage);
                /*
                 * End of Custom Features
                 */
                ps.setInt(++index, elf);
                ps.setInt(++index, honourExp);
                ps.setInt(++index, honorLevel);
                ps.setString(++index, friendshippoints[0] + "," + friendshippoints[1] + "," + friendshippoints[2] + "," + friendshippoints[3]);
                ps.setInt(++index, friendshiptoadd);
                ps.setShort(++index, chattype);
                ps.setString(++index, name);
                ps.setInt(++index, beans);
                ps.setInt(++index, mega_times);
                ps.setInt(++index, id);
                if (ps.executeUpdate() < 1) {
                    ps.close();
                    throw new DatabaseException("Character not in database (" + id + ")");
                }
                ps.close();
                deleteWhereCharacterId(con, "DELETE FROM stolen WHERE characterid = ?");
                for (Pair<Integer, Boolean> st : stolenSkills) {
                    ps = con.prepareStatement("INSERT INTO stolen (characterid, skillid, chosen) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, st.left);
                    ps.setInt(3, st.right ? 1 : 0);
                    ps.execute();
                    ps.close();
                }

                if (changed_skillmacros) {
                    deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?");
                    for (int i = 0; i < 5; i++) {
                        final SkillMacro macro = skillMacros[i];
                        if (macro != null) {
                            ps = con.prepareStatement("INSERT INTO skillmacros (characterid, skill1, skill2, skill3, name, shout, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
                            ps.setInt(1, id);
                            ps.setInt(2, macro.getSkill1());
                            ps.setInt(3, macro.getSkill2());
                            ps.setInt(4, macro.getSkill3());
                            ps.setString(5, macro.getName());
                            ps.setInt(6, macro.getShout());
                            ps.setInt(7, i);
                            ps.execute();
                            ps.close();
                        }
                    }
                }
                deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setByte(2, getInventory(MapleInventoryType.EQUIP).getSlotLimit());
                ps.setByte(3, getInventory(MapleInventoryType.USE).getSlotLimit());
                ps.setByte(4, getInventory(MapleInventoryType.SETUP).getSlotLimit());
                ps.setByte(5, getInventory(MapleInventoryType.ETC).getSlotLimit());
                ps.setByte(6, getInventory(MapleInventoryType.CASH).getSlotLimit());
                ps.execute();
                ps.close();

                List<Pair<Item, MapleInventoryType>> listing = new ArrayList<>();
                for (final MapleInventory iv : inventory) {
                    for (final Item item : iv.list()) {
                        listing.add(new Pair<>(item, iv.getType()));
                    }
                }
                ItemLoader.INVENTORY.saveItems(listing, con, id);

                //saveInventory(con);
                if (changed_questinfo) {
                    deleteWhereCharacterId(con, "DELETE FROM questinfo WHERE characterid = ?");
                    ps = con.prepareStatement("INSERT INTO questinfo (`characterid`, `quest`, `customData`) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    for (final Entry<Integer, String> q : questinfo.entrySet()) {
                        ps.setInt(2, q.getKey());
                        ps.setString(3, q.getValue());
                        ps.execute();
                    }
                    ps.close();
                }

                deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", DBConPool.RETURN_GENERATED_KEYS);
                pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                ps.setInt(1, id);
                for (final MapleQuestStatus q : quests.values()) {
                    ps.setInt(2, q.getQuest().getId());
                    ps.setInt(3, q.getStatus());
                    ps.setInt(4, (int) (q.getCompletionTime() / 1000));
                    ps.setInt(5, q.getForfeited());
                    ps.setString(6, q.getCustomData());
                    ps.execute();
                    rs = ps.getGeneratedKeys();
                    if (q.hasMobKills()) {
                        rs.next();
                        for (int mob : q.getMobKills().keySet()) {
                            pse.setInt(1, rs.getInt(1));
                            pse.setInt(2, mob);
                            pse.setInt(3, q.getMobKills(mob));
                            pse.execute();
                        }
                    }
                    rs.close();
                }
                ps.close();
                pse.close();

                if (changed_skills) {
                    deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?");
                    ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
                    ps.setInt(1, id);

                    for (final Entry<Skill, SkillEntry> skill : skills.entrySet()) {
                        if (GameConstants.isApplicableSkill(skill.getKey().getId())) { //do not save additional skills
                            ps.setInt(2, skill.getKey().getId());
                            ps.setInt(3, skill.getValue().skillevel);
                            ps.setByte(4, skill.getValue().masterlevel);
                            ps.setLong(5, skill.getValue().expiration);
                            ps.execute();
                        }
                    }
                    ps.close();
                }

                //TODO {TEST} SAVE NEW JETT CORE AURA
                deleteWhereCharacterId(con, "DELETE FROM coreauras WHERE cid = ?");
                ps = con.prepareStatement("INSERT INTO coreauras (cid, str, dex, `int`, luk, att, magic, total, expire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setInt(2, getCoreAura().getStr());
                ps.setInt(3, getCoreAura().getDex());
                ps.setInt(4, getCoreAura().getInt());
                ps.setInt(5, getCoreAura().getLuk());
                ps.setInt(6, getCoreAura().getAtt());
                ps.setInt(7, getCoreAura().getMagic());
                ps.setInt(8, getCoreAura().getTotal());
                ps.setInt(9, getCoreAura().getExpire());
                ps.execute();
                ps.close();

                if (innerskill_changed) {
                    if (innerSkills != null) {
                        deleteWhereCharacterId(con, "DELETE FROM inner_ability_skills WHERE player_id = ?");
                        ps = con.prepareStatement("INSERT INTO inner_ability_skills (player_id, skill_id, skill_level, max_level, rank, locked) VALUES (?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, id);

                        for (int i = 0; i < innerSkills.size(); ++i) {
                            ps.setInt(2, innerSkills.get(i).getSkillId());
                            ps.setInt(3, innerSkills.get(i).getSkillLevel());
                            ps.setInt(4, innerSkills.get(i).getMaxLevel());
                            ps.setInt(5, innerSkills.get(i).getRank());
                            ps.setBoolean(6, innerSkills.get(i).isLocked());
                            ps.executeUpdate();
                        }
                        ps.close();
                    }
                }

                List<MapleCoolDownValueHolder> cd = getCooldowns();
                if (dc && cd.size() > 0) {
                    ps = con.prepareStatement("INSERT INTO skills_cooldowns (charid, SkillID, StartTime, length) VALUES (?, ?, ?, ?)");
                    ps.setInt(1, getId());
                    for (final MapleCoolDownValueHolder cooling : cd) {
                        ps.setInt(2, cooling.skillId);
                        ps.setLong(3, cooling.startTime);
                        ps.setLong(4, cooling.length);
                        ps.execute();
                    }
                    ps.close();
                }

                if (changed_savedlocations) {
                    deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?");
                    ps = con.prepareStatement("INSERT INTO savedlocations (characterid, `locationtype`, `map`) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    for (final SavedLocationType savedLocationType : SavedLocationType.values()) {
                        if (savedLocations[savedLocationType.getValue()] != -1) {
                            ps.setInt(2, savedLocationType.getValue());
                            ps.setInt(3, savedLocations[savedLocationType.getValue()]);
                            ps.execute();
                        }
                    }
                    ps.close();
                }

                if (changed_achievements) {
                    ps = con.prepareStatement("DELETE FROM achievements WHERE accountid = ?");
                    ps.setInt(1, accountid);
                    ps.executeUpdate();
                    ps.close();
                    ps = con.prepareStatement("INSERT INTO achievements(charid, achievementid, accountid) VALUES(?, ?, ?)");
                    for (Integer achid : finishedAchievements) {
                        ps.setInt(1, id);
                        ps.setInt(2, achid);
                        ps.setInt(3, accountid);
                        ps.execute();
                    }
                    ps.close();
                }

                if (changed_moonlight_achievements) {
                    ps = con.prepareStatement("DELETE FROM achievements WHERE accountid = ?");
                    ps.setInt(1, accountid);
                    ps.executeUpdate();
                    ps.close();
                    ps = con.prepareStatement("INSERT INTO moonlightachievements(charid, achievementid, accountid) VALUES(?, ?, ?)");
                    for (Integer achid : finishedMoonlightAchievements) {
                        ps.setInt(1, id);
                        ps.setInt(2, achid);
                        ps.setInt(3, accountid);
                        ps.execute();
                    }
                    ps.close();
                }

                if (changed_reports) {
                    deleteWhereCharacterId(con, "DELETE FROM reports WHERE characterid = ?");
                    ps = con.prepareStatement("INSERT INTO reports VALUES(DEFAULT, ?, ?, ?)");
                    for (Entry<ReportType, Integer> achid : reports.entrySet()) {
                        ps.setInt(1, id);
                        ps.setByte(2, achid.getKey().i);
                        ps.setInt(3, achid.getValue());
                        ps.execute();
                    }
                    ps.close();
                }

                if (buddylist.changed()) {
                    deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ?");
                    ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    for (BuddylistEntry entry : buddylist.getBuddies()) {
                        ps.setInt(2, entry.getCharacterId());
                        ps.setInt(3, entry.isVisible() ? 0 : 1);
                        ps.execute();
                    }
                    ps.close();
                    buddylist.setChanged(false);
                }

                ps = con.prepareStatement("UPDATE accounts SET `nxCredit` = ?, `ACash` = ?, `mPoints` = ?, `points` = ?, `vpoints` = ?, `dpoints` = ?, `epoints` = ?, `lastWorld` = ? WHERE id = ?");
                ps.setInt(1, nxcredit);
                ps.setInt(2, acash);
                ps.setInt(3, maplepoints);
                ps.setInt(4, points);
                ps.setInt(5, vpoints);
                ps.setInt(6, dpoints);
                ps.setInt(7, epoints);
                ps.setByte(8, lastWorld);
                ps.setInt(9, client.getAccID());
                ps.executeUpdate();
                ps.close();

                if (storage != null) {
                    storage.saveToDB(con);
                }
                if (cs != null) {
                    cs.save(con);
                }
                if (PlayerNPC.Auto_Update) {
                    PlayerNPC.updateByCharId(con, this);
                }
                keylayout.saveKeys(con, id);
                mount.saveMount(con, id);
                monsterbook.saveCards(con, id);

                deleteWhereCharacterId(con, "DELETE FROM familiars WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO familiars (characterid, expiry, name, fatigue, vitality, familiar) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                for (MonsterFamiliar f : familiars.values()) {
                    ps.setLong(2, f.getExpiry());
                    ps.setString(3, f.getName());
                    ps.setInt(4, f.getFatigue());
                    ps.setByte(5, f.getVitality());
                    ps.setInt(6, f.getFamiliar());
                    ps.executeUpdate();
                }
                ps.close();

                deleteWhereCharacterId(con, "DELETE FROM imps WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO imps (characterid, itemid, closeness, fullness, state, level) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                for (MapleImp imp : imps) {
                    if (imp != null) {
                        ps.setInt(2, imp.getItemId());
                        ps.setShort(3, imp.getCloseness());
                        ps.setShort(4, imp.getFullness());
                        ps.setByte(5, imp.getState());
                        ps.setByte(6, imp.getLevel());
                        ps.executeUpdate();
                    }
                }
                ps.close();
                if (changed_wishlist) {
                    deleteWhereCharacterId(con, "DELETE FROM wishlist WHERE characterid = ?");
                    for (int i = 0; i < getWishlistSize(); i++) {
                        ps = con.prepareStatement("INSERT INTO wishlist(characterid, sn) VALUES(?, ?) ");
                        ps.setInt(1, getId());
                        ps.setInt(2, wishlist[i]);
                        ps.execute();
                        ps.close();
                    }
                }
                if (changed_trocklocations) {
                    deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?");
                    for (int i = 0; i < rocks.length; i++) {
                        if (rocks[i] != 999999999) {
                            ps = con.prepareStatement("INSERT INTO trocklocations(characterid, mapid) VALUES(?, ?) ");
                            ps.setInt(1, getId());
                            ps.setInt(2, rocks[i]);
                            ps.execute();
                            ps.close();
                        }
                    }
                }

                if (changed_regrocklocations) {
                    deleteWhereCharacterId(con, "DELETE FROM regrocklocations WHERE characterid = ?");
                    for (int i = 0; i < regrocks.length; i++) {
                        if (regrocks[i] != 999999999) {
                            ps = con.prepareStatement("INSERT INTO regrocklocations(characterid, mapid) VALUES(?, ?) ");
                            ps.setInt(1, getId());
                            ps.setInt(2, regrocks[i]);
                            ps.execute();
                            ps.close();
                        }
                    }
                }
                if (changed_hyperrocklocations) {
                    deleteWhereCharacterId(con, "DELETE FROM hyperrocklocations WHERE characterid = ?");
                    for (int i = 0; i < hyperrocks.length; i++) {
                        if (hyperrocks[i] != 999999999) {
                            ps = con.prepareStatement("INSERT INTO hyperrocklocations(characterid, mapid) VALUES(?, ?) ");
                            ps.setInt(1, getId());
                            ps.setInt(2, hyperrocks[i]);
                            ps.execute();
                            ps.close();
                        }
                    }
                }
                if (changed_extendedSlots) {
                    deleteWhereCharacterId(con, "DELETE FROM extendedSlots WHERE characterid = ?");
                    for (int i : extendedSlots) {
                        if (getInventory(MapleInventoryType.ETC).findById(i) != null) { //just in case
                            ps = con.prepareStatement("INSERT INTO extendedSlots(characterid, itemId) VALUES(?, ?) ");
                            ps.setInt(1, getId());
                            ps.setInt(2, i);
                            ps.execute();
                            ps.close();
                        }
                    }
                }
                changed_wishlist = false;
                changed_trocklocations = false;
                changed_regrocklocations = false;
                changed_hyperrocklocations = false;
                changed_skillmacros = false;
                changed_savedlocations = false;
                changed_questinfo = false;
                changed_achievements = false;
                changed_moonlight_achievements = false;
                changed_extendedSlots = false;
                changed_skills = false;
                changed_reports = false;
                con.commit();
            } catch (SQLException | DatabaseException e) {
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                System.err.println(MapleClient.getLogMessage(this, "[charsave] Error saving character data") + e);
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
                    System.err.println(MapleClient.getLogMessage(this, "[charsave] Error Rolling Back") + e);
                }
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (pse != null) {
                        pse.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.setAutoCommit(true);
                    con.setTransactionIsolation(4);
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
                    System.err.println(MapleClient.getLogMessage(this, "[charsave] Error going back to autocommit mode") + e);
                }
            }
        } catch (SQLException exx) {
            FileoutputUtil.outputFileError("logs/数据库异常.txt", exx);

        }
    }

    private void deleteWhereCharacterId(Connection con, String sql) throws SQLException {
        deleteWhereCharacterId(con, sql, id);
    }

    public static void deleteWhereCharacterId(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void deleteWhereCharacterId_NoLock(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }

    public void saveInventory(final Connection con) throws SQLException {
        List<Pair<Item, MapleInventoryType>> listing = new ArrayList<>();
        for (final MapleInventory iv : inventory) {
            for (final Item item : iv.list()) {
                listing.add(new Pair<>(item, iv.getType()));
            }
        }
        if (con != null) {
            ItemLoader.INVENTORY.saveItems(listing, con, id);
        } else {
            ItemLoader.INVENTORY.saveItems(listing, id);
        }
    }

    public final PlayerStats getStat() {
        return stats;
    }

    public final void QuestInfoPacket(final tools.data.MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(questinfo.size()); // // Party Quest data (quest needs to be added in the quests list)

        for (final Entry<Integer, String> q : questinfo.entrySet()) {
            mplew.writeShort(q.getKey());
            mplew.writeMapleAsciiString(q.getValue() == null ? "" : q.getValue());
        }
    }

    public final void updateInfoQuest(final int questid, final String data) {
        questinfo.put(questid, data);
        changed_questinfo = true;
        client.getSession().write(InfoPacket.updateInfoQuest(questid, data));
    }

    public final String getInfoQuest(final int questid) {
        if (questinfo.containsKey(questid)) {
            return questinfo.get(questid);
        }
        return "";
    }

    public final int getNumQuest() {
        int i = 0;
        for (final MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !(q.isCustom())) {
                i++;
            }
        }
        return i;
    }

    public final byte getQuestStatus(final int quest) {
        final MapleQuest qq = MapleQuest.getInstance(quest);
        if (getQuestNoAdd(qq) == null) {
            return 0;
        }
        return getQuestNoAdd(qq).getStatus();
    }

    public final MapleQuestStatus getQuest(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, (byte) 0);
        }
        return quests.get(quest);
    }

    public final void setQuestAdd(final MapleQuest quest, final byte status, final String customData) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus stat = new MapleQuestStatus(quest, status);
            stat.setCustomData(customData);
            quests.put(quest, stat);
        }
    }

    public final MapleQuestStatus getQuestNAdd(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus status = new MapleQuestStatus(quest, (byte) 0);
            quests.put(quest, status);
            return status;
        }
        return quests.get(quest);
    }

    public final MapleQuestStatus getQuestNoAdd(final MapleQuest quest) {
        return quests.get(quest);
    }

    public final MapleQuestStatus getQuestRemove(final MapleQuest quest) {
        return quests.remove(quest);
    }

    public final void updateQuest(final MapleQuestStatus quest) {
        updateQuest(quest, false);
    }

    public final void updateQuest(final MapleQuestStatus quest, final boolean update) {
        quests.put(quest.getQuest(), quest);
        if (!(quest.isCustom())) {
            client.getSession().write(InfoPacket.updateQuest(quest));
            if (quest.getStatus() == 1 && !update) {
                client.getSession().write(CField.updateQuestInfo(this, quest.getQuest().getId(), quest.getNpc(), (byte) 0x8));//was10
            }
        }
    }

    public final Map<Integer, String> getInfoQuest_Map() {
        return questinfo;
    }

    public final Map<MapleQuest, MapleQuestStatus> getQuest_Map() {
        return quests;
    }

    public Integer getBuffedValue(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.value;
    }

    public final Integer getBuffedSkill_X(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getX();
    }

    public final Integer getBuffedSkill_Y(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getY();
    }

    public boolean isBuffFrom(MapleBuffStat stat, Skill skill) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        if (mbsvh == null || mbsvh.effect == null) {
            return false;
        }
        return mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skill.getId();
    }

    public int getBuffSource(MapleBuffStat stat) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        return mbsvh == null ? -1 : mbsvh.effect.getSourceId();
    }

    public int getTrueBuffSource(MapleBuffStat stat) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        return mbsvh == null ? -1 : (mbsvh.effect.isSkill() ? mbsvh.effect.getSourceId() : -mbsvh.effect.getSourceId());
    }

    public int getItemQuantity(int itemid, boolean checkEquipped) {
        int possesed = inventory[GameConstants.getInventoryType(itemid).ordinal()].countById(itemid);
        if (checkEquipped) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        return possesed;
    }

    public void setBuffedValue(MapleBuffStat effect, int value) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.value = value;
    }

    public void setSchedule(MapleBuffStat effect, ScheduledFuture<?> sched) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.schedule.cancel(false);
        mbsvh.schedule = sched;
    }

    public Long getBuffedStarttime(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.startTime;
    }

    public MapleStatEffect getStatForBuff(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.effect;
    }

    public void doDragonBlood() {
        final MapleStatEffect bloodEffect = getStatForBuff(MapleBuffStat.DRAGONBLOOD);
        if (bloodEffect == null) {
            lastDragonBloodTime = 0;
            return;
        }
        prepareDragonBlood();
        if (stats.getHp() - bloodEffect.getX() <= 1) {
            cancelBuffStats(MapleBuffStat.DRAGONBLOOD);

        } else {
            addHP(-bloodEffect.getX());
            client.getSession().write(EffectPacket.showOwnBuffEffect(bloodEffect.getSourceId(), 5, getLevel(), bloodEffect.getLevel()));
            map.broadcastMessage(MapleCharacter.this, EffectPacket.showBuffeffect(getId(), bloodEffect.getSourceId(), 5, getLevel(), bloodEffect.getLevel()), false);
        }
    }

    public final boolean canBlood(long now) {
        return lastDragonBloodTime > 0 && lastDragonBloodTime + 4000 < now;
    }

    private void prepareDragonBlood() {
        lastDragonBloodTime = System.currentTimeMillis();
    }

    public void doRecovery() {
        MapleStatEffect bloodEffect = getStatForBuff(MapleBuffStat.RECOVERY);
        if (bloodEffect == null) {
            bloodEffect = getStatForBuff(MapleBuffStat.MECH_CHANGE);
            if (bloodEffect == null) {
                lastRecoveryTime = 0;
            } else if (bloodEffect.getSourceId() == 35121005) {
                prepareRecovery();
                if (stats.getMp() < bloodEffect.getU()) {
                    cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
                    cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
                } else {
                    addMP(-bloodEffect.getU());
                }
            }
        } else {
            prepareRecovery();
            if (stats.getHp() >= stats.getCurrentMaxHp()) {
                cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
            } else {
                healHP(bloodEffect.getX());
            }
        }
    }

    public final boolean canRecover(long now) {
        return lastRecoveryTime > 0 && lastRecoveryTime + 5000 < now;
    }

    private void prepareRecovery() {
        lastRecoveryTime = System.currentTimeMillis();
    }

    public void startMapTimeLimitTask(int time, final MapleMap to) {
        if (time <= 0) { //jail
            time = 1;
        }
        client.getSession().write(CField.getClock(time));
        final MapleMap ourMap = getMap();
        time *= 1000;
        mapTimeLimitTask = MapTimer.getInstance().register(new Runnable() {

            @Override
            public void run() {
                if (ourMap.getId() == GameConstants.JAIL) {
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME)).setCustomData(String.valueOf(System.currentTimeMillis()));
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData("0"); //release them!
                }
                changeMap(to, to.getPortal(0));
            }
        }, time, time);
    }

    public boolean canDOT(long now) {
        return lastDOTTime > 0 && lastDOTTime + 8000 < now;
    }

    public boolean hasDOT() {
        return dotHP > 0;
    }

    public void doDOT() {
        addHP(-(dotHP * 4));
        dotHP = 0;
        lastDOTTime = 0;
    }

    public void setDOT(int d, int source, int sourceLevel) {
        this.dotHP = d;
        addHP(-(dotHP * 4));
        map.broadcastMessage(CField.getPVPMist(id, source, sourceLevel, d));
        lastDOTTime = System.currentTimeMillis();
    }

    public void startFishingTask() {
        cancelFishingTask();
        lastFishingTime = System.currentTimeMillis();
    }

    public boolean canFish(long now) {
        return lastFishingTime > 0 && lastFishingTime + GameConstants.getFishingTime(false, isGM()) < now;
    }

    public void doFish(long now) {
        lastFishingTime = now;
        boolean expMulti = haveItem(2300001, 1, false, true);
        if (client == null || client.getPlayer() == null || !client.isReceiving() || ((!expMulti) && (!haveItem(2300000, 1, false, true))) || !GameConstants.isFishingMap(getMapId()) || chair <= 0) {
            cancelFishingTask();
            return;
        }
        MapleInventoryManipulator.removeById(client, MapleInventoryType.USE, expMulti ? 2300001 : 2300000, 1, false, false);
        boolean passed = false;
        while (!passed) {
            int randval = RandomRewards.getFishingReward();
            switch (randval) {
                case 0: // Meso
                    int money = ServerConfig.dyjb * Randomizer.rand(expMulti ? 15 : 10, expMulti ? 75000 : 50000);
                    gainMeso(money, true);
                    client.getSession().write(CWvsContext.fishingUpdate((byte) 1, money));
                    passed = true;
                    break;
                case 1: // EXP
                    final int experi = ServerConfig.dyjy * Randomizer.nextInt(Math.min((Math.abs(getNeededExp() / 200) + 1), 500000));
                    gainExp(expMulti ? experi * 3 / 2 : experi, true, false, true);
                    client.getSession().write(CWvsContext.fishingUpdate((byte) 2, experi));
                    passed = true;
                    break;
                default:
                    if (MapleItemInformationProvider.getInstance().itemExists(randval)) {
                        MapleInventoryManipulator.addById(client, randval, (short) 1, "钓鱼" + " 时间 " + FileoutputUtil.CurrentReadable_Date());
                        client.getSession().write(CWvsContext.fishingUpdate((byte) 0, randval));
                        passed = true;
                    }
                    break;
            }
        }
        map.broadcastMessage(CWvsContext.fishingCaught(id));
    }

    public void cancelMapTimeLimitTask() {
        if (mapTimeLimitTask != null) {
            mapTimeLimitTask.cancel(false);
            mapTimeLimitTask = null;
        }
    }

    public int getNeededExp() {
        return GameConstants.getExpNeededForLevel(level);
    }

    public void cancelFishingTask() {
        lastFishingTime = 0;
    }

    public final boolean canCheckPeriod() {
        if (lastCheckPeriodTime + 30000 > System.currentTimeMillis()) {
            return false;
        }
        lastCheckPeriodTime = System.currentTimeMillis();
        return true;
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule, int from) {
        registerEffect(effect, starttime, schedule, effect.getStatups(), false, effect.getDuration(), from);
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule, Map<MapleBuffStat, Integer> statups, boolean silent, final int localDuration, final int cid) {
        if (effect.isHide()) {
            map.broadcastNONGMMessage(this, CField.removePlayerFromMap(getId()), false);
        } else if (effect.isDragonBlood()) {
            prepareDragonBlood();
        } else if (effect.isRecovery()) {
            prepareRecovery();
        } else if (effect.isBerserk()) {
            checkBerserk();
        } else if (effect.isMonsterRiding_()) {
            getMount().startSchedule();
        }
        int clonez = 0;
        for (Entry<MapleBuffStat, Integer> statup : statups.entrySet()) {
            if (statup.getKey() == MapleBuffStat.ILLUSION) {
                clonez = statup.getValue();
            }
            int value = statup.getValue();
            if (statup.getKey() == MapleBuffStat.MONSTER_RIDING) {
                if (effect.getSourceId() == 5221006 && battleshipHP <= 0) {
                    battleshipHP = maxBattleshipHP(effect.getSourceId()); //copy this as well
                }
                removeFamiliar();
            }
            effects.put(statup.getKey(), new MapleBuffStatValueHolder(effect, starttime, schedule, value, localDuration, cid));
        }
        if (clonez > 0) {
            int cloneSize = Math.max(getNumClones(), getCloneSize());
            if (clonez > cloneSize) { //how many clones to summon
                for (int i = 0; i < clonez - cloneSize; i++) { //1-1=0
                    cloneLook();
                }
            }
        }
        if (!silent) {
            stats.recalcLocalStats(this);
        }
        if (getDebugMessage()) {
            Iterator<Entry<MapleBuffStat, Integer>> iterator = statups.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<MapleBuffStat, Integer> entry = iterator.next();
                entry.getKey();
                entry.getValue();
                dropMessage(6, "[系統提示]" + entry.getKey().toString() + "(0x" + HexTool.toString(entry.getKey().getValue()) + ")");

            }
        }
        if (ServerConfig.logPackets) {
            if (isGM()) {
                showMessage(25, new StringBuilder().append("注册BUFF效果 - 当前BUFF总数: ").append(this.effects.size()).append(" 技能: ").append(effect.getSourceId()).toString());
            }
        }
        //System.out.println("Effect registered. Effect: " + effect.getSourceId());
    }

    public List<MapleBuffStat> getBuffStats(final MapleStatEffect effect, final long startTime) {
        final List<MapleBuffStat> bstats = new ArrayList<>();
        final Map<MapleBuffStat, MapleBuffStatValueHolder> allBuffs = new EnumMap<>(effects);
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> stateffect : allBuffs.entrySet()) {
            final MapleBuffStatValueHolder mbsvh = stateffect.getValue();
            if (mbsvh.effect.sameSource(effect) && (startTime == -1 || startTime == mbsvh.startTime || stateffect.getKey().canStack())) {
                bstats.add(stateffect.getKey());
            }
        }
        return bstats;
    }

    private boolean deregisterBuffStats(List<MapleBuffStat> stats) {
        boolean clonez = false;
        List<MapleBuffStatValueHolder> effectsToCancel = new ArrayList<>(stats.size());
        for (MapleBuffStat stat : stats) {
            final MapleBuffStatValueHolder mbsvh = effects.remove(stat);
            if (mbsvh != null) {
                boolean addMbsvh = true;
                for (MapleBuffStatValueHolder contained : effectsToCancel) {
                    if (mbsvh.startTime == contained.startTime && contained.effect == mbsvh.effect) {
                        addMbsvh = false;
                    }
                }
                if (addMbsvh) {
                    effectsToCancel.add(mbsvh);
                }
                if (stat == MapleBuffStat.SUMMON || stat == MapleBuffStat.PUPPET || stat == MapleBuffStat.REAPER || stat == MapleBuffStat.BEHOLDER || stat == MapleBuffStat.DAMAGE_BUFF || stat == MapleBuffStat.RAINING_MINES || stat == MapleBuffStat.INDIE_PAD) {
                    final int summonId = mbsvh.effect.getSourceId();
                    final List<MapleSummon> toRemove = new ArrayList<>();
                    visibleMapObjectsLock.writeLock().lock(); //We need to lock this later on anyway so do it now to prevent deadlocks.
                    summonsLock.writeLock().lock();
                    try {
                        for (MapleSummon summon : summons) {
                            if (summon.getSkill() == summonId || (stat == MapleBuffStat.RAINING_MINES && summonId == 33101008) || (summonId == 35121009 && summon.getSkill() == 35121011) || ((summonId == 86 || summonId == 88 || summonId == 91 || summonId == 180 || summonId == 96) && summon.getSkill() == summonId + 999) || ((summonId == 1085 || summonId == 1087 || summonId == 1090 || summonId == 1179 || summonId == 1154) && summon.getSkill() == summonId - 999)) { //removes bots n tots
                                map.broadcastMessage(SummonPacket.removeSummon(summon, true));
                                map.removeMapObject(summon);
                                visibleMapObjects.remove(summon);
                                toRemove.add(summon);
                            }
                        }
                        for (MapleSummon s : toRemove) {
                            summons.remove(s);
                        }
                    } finally {
                        summonsLock.writeLock().unlock();
                        visibleMapObjectsLock.writeLock().unlock(); //lolwut
                    }
                    if (summonId == 3111005 || summonId == 3211005) {
                        cancelEffectFromBuffStat(MapleBuffStat.SPIRIT_LINK);
                    }
                } else if (stat == MapleBuffStat.DRAGONBLOOD) {
                    lastDragonBloodTime = 0;
                } else if (stat == MapleBuffStat.RECOVERY || mbsvh.effect.getSourceId() == 35121005) {
                    lastRecoveryTime = 0;
                } else if (stat == MapleBuffStat.HOMING_BEACON || stat == MapleBuffStat.MANY_USES) {
                    linkMobs.clear();
                } else if (stat == MapleBuffStat.ILLUSION) {
                    disposeClones();
                    clonez = true;
                }
            }
        }
        for (MapleBuffStatValueHolder cancelEffectCancelTasks : effectsToCancel) {
            if (getBuffStats(cancelEffectCancelTasks.effect, cancelEffectCancelTasks.startTime).isEmpty()) {
                if (cancelEffectCancelTasks.schedule != null) {
                    cancelEffectCancelTasks.schedule.cancel(false);
                }
            }
        }
        return clonez;
    }

    /**
     * @param effect
     * @param overwrite when overwrite is set no data is sent and all the
     * Buffstats in the StatEffect are deregistered
     * @param startTime
     */
    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime) {
        if (effect == null) {
            return;
        }
        cancelEffect(effect, overwrite, startTime, effect.getStatups());
    }

    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime, Map<MapleBuffStat, Integer> statups) {
        if (effect == null) {
            return;
        }
        List<MapleBuffStat> buffstats;
        if (!overwrite) {
            buffstats = getBuffStats(effect, startTime);
        } else {
            buffstats = new ArrayList<>(statups.keySet());
        }
        if (buffstats.size() <= 0) {
            return;
        }
//        if (effect.isInfinity() && getBuffedValue(MapleBuffStat.INFINITY) != null) { //before
//            int duration = Math.max(effect.getDuration(), effect.alchemistModifyVal(this, effect.getDuration(), false));
//            final long start = getBuffedStarttime(MapleBuffStat.INFINITY);
//            duration += (int) ((start - System.currentTimeMillis()));
//            if (duration > 0) {
//                final int neworbcount = getBuffedValue(MapleBuffStat.INFINITY) + effect.getDamage();
//                final Map<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
//                stat.put(MapleBuffStat.INFINITY, neworbcount);
//                setBuffedValue(MapleBuffStat.INFINITY, neworbcount);
//                client.getSession().write(BuffPacket.giveBuff(effect.getSourceId(), duration, stat, effect));
//                addHP((int) (effect.getHpR() * this.stats.getCurrentMaxHp()));
//                addMP((int) (effect.getMpR() * this.stats.getCurrentMaxMp(this.getJob())));
//                setSchedule(MapleBuffStat.INFINITY, BuffTimer.getInstance().schedule(new CancelEffectAction(this, effect, start, stat), effect.alchemistModifyVal(this, 4000, false)));
//                return;
//            }
//        }
        final boolean clonez = deregisterBuffStats(buffstats);
        if (effect.isMagicDoor()) {
            // remove for all on maps
            if (!getDoors().isEmpty()) {
                removeDoor();
                silentPartyUpdate();
            }
        } else if (effect.isMechDoor()) {
            if (!getMechDoors().isEmpty()) {
                removeMechDoor();
            }
        } else if (effect.isMonsterRiding_()) {
            getMount().cancelSchedule();
        } else if (effect.isMonsterRiding()) {
            cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
        } else if (effect.isAranCombo()) {
            combo = 0;
        }
        // check if we are still logged in o.o
        cancelPlayerBuffs(buffstats, overwrite);
        if (!overwrite) {
            if (effect.isHide() && client.getChannelServer().getPlayerStorage().getCharacterById(this.getId()) != null) { //Wow this is so fking hacky...
                map.broadcastMessage(this, CField.spawnPlayerMapobject(this), false);

                for (final MaplePet pet : pets) {
                    if (pet.getSummoned()) {
                        map.broadcastMessage(this, PetPacket.showPet(this, pet, false, false), false);
                    }
                }
                for (final WeakReference<MapleCharacter> chr : clones) {
                    if (chr.get() != null) {
                        map.broadcastMessage(chr.get(), CField.spawnPlayerMapobject(chr.get()), false);
                    }
                }
            }
        }
//        if (!clonez) {
//            for (WeakReference<MapleCharacter> chr : clones) {
//                if (chr.get() != null) {
//                    chr.get().cancelEffect(effect, overwrite, startTime);
//                }
//            }
//        }
        //System.out.println("Effect deregistered. Effect: " + effect.getSourceId());
        if (ServerConfig.logPackets) {
            if (isGM()) {
                dropMessage(5, new StringBuilder().append("取消一般BUFF效果 - 当前BUFF总数：").append(this.effects.size()).append(" 技能：").append(effect.getSourceId()).toString());
            }
        }
    }

    public void cancelBuffStats(MapleBuffStat... stat) {
        List<MapleBuffStat> buffStatList = Arrays.asList(stat);
        deregisterBuffStats(buffStatList);
        cancelPlayerBuffs(buffStatList, false);
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat) {
        if (effects.get(stat) != null) {
            cancelEffect(effects.get(stat).effect, false, -1);
        }
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat, int from) {
        if (effects.get(stat) != null && effects.get(stat).cid == from) {
            cancelEffect(effects.get(stat).effect, false, -1);
        }
    }

    private void cancelPlayerBuffs(List<MapleBuffStat> buffstats, boolean overwrite) {
        boolean write = client != null && client.getChannelServer() != null && client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
        if (buffstats.contains(MapleBuffStat.HOMING_BEACON)) {
            client.getSession().write(BuffPacket.cancelHoming());
        } else {
            if (overwrite) {
                List<MapleBuffStat> z = new ArrayList<>();
                for (MapleBuffStat s : buffstats) {
                    if (s.canStack()) {
                        z.add(s);
                    }
                }
                if (z.size() > 0) {
                    buffstats = z;
                } else {
                    return; //don't write anything
                }
            } else if (write) {
                stats.recalcLocalStats(this);
            }
            if (ServerConfig.logPackets) {
                if (isGM()) {
                    dropMessage(5, new StringBuilder().append("取消BUFF效果 - 发送封包 - 是否注册BUFF时：").append(overwrite).toString());
                }
            }
            client.getSession().write(BuffPacket.cancelBuff(buffstats));
            map.broadcastMessage(this, BuffPacket.cancelForeignBuff(getId(), buffstats), false);
        }
    }

    public void dispel() {
        if (!isHidden()) {
            final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());
            for (MapleBuffStatValueHolder mbsvh : allBuffs) {
                if (mbsvh.effect.isSkill() && mbsvh.schedule != null && !mbsvh.effect.isMorph() && !mbsvh.effect.isGmBuff() && !mbsvh.effect.isMonsterRiding() && !mbsvh.effect.isMechChange() && !mbsvh.effect.isEnergyCharge() && !mbsvh.effect.isAranCombo()) {
                    cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                }
            }
        }
    }

    public void dispelSkill(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void dispelSummons() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.getSummonMovementType() != null) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
            }
        }
    }

    public void dispelBuff(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void cancelAllBuffs_() {
        effects.clear();
    }

    public void cancelAllBuffs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
        }
    }

    public void cancelMorphs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            switch (mbsvh.effect.getSourceId()) {
                case 5111005:
                case 5121003:
                case 15111002:
                case 13111005:
                    return; // Since we can't have more than 1, save up on loops
                default:
                    if (mbsvh.effect.isMorph()) {
                        if (mbsvh.effect.isMorph()) {
                            disposeClones();
                            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                        }
                    }
            }
        }
    }

    public int getMorphState() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMorph()) {
                return mbsvh.effect.getSourceId();
            }
        }
        return -1;
    }

    public void silentGiveBuffs(List<PlayerBuffValueHolder> buffs) {
        if (buffs == null) {
            return;
        }
        for (PlayerBuffValueHolder mbsvh : buffs) {
            mbsvh.effect.silentApplyBuff(this, mbsvh.startTime, mbsvh.localDuration, mbsvh.statup, mbsvh.cid);
        }
    }

    public List<PlayerBuffValueHolder> getAllBuffs() {
        final List<PlayerBuffValueHolder> ret = new ArrayList<>();
        final Map<Pair<Integer, Byte>, Integer> alreadyDone = new HashMap<>();
        final LinkedList<Entry<MapleBuffStat, MapleBuffStatValueHolder>> allBuffs = new LinkedList<>(effects.entrySet());
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> mbsvh : allBuffs) {
            final Pair<Integer, Byte> key = new Pair<>(mbsvh.getValue().effect.getSourceId(), mbsvh.getValue().effect.getLevel());
            if (alreadyDone.containsKey(key)) {
                ret.get(alreadyDone.get(key)).statup.put(mbsvh.getKey(), mbsvh.getValue().value);
            } else {
                alreadyDone.put(key, ret.size());
                final EnumMap<MapleBuffStat, Integer> list = new EnumMap<>(MapleBuffStat.class);
                list.put(mbsvh.getKey(), mbsvh.getValue().value);
                ret.add(new PlayerBuffValueHolder(mbsvh.getValue().startTime, mbsvh.getValue().effect, list, mbsvh.getValue().localDuration, mbsvh.getValue().cid));
            }
        }
        return ret;
    }

    public void cancelMagicDoor() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMagicDoor()) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public int getSkillLevel(int skillid) {
        return getSkillLevel(SkillFactory.getSkill(skillid));
    }

    public int getTotalSkillLevel(int skillid) {
        return getTotalSkillLevel(SkillFactory.getSkill(skillid));
    }

    public final void handleEnergyCharge(int skillid, int targets) {
        Skill echskill = SkillFactory.getSkill(skillid);
        int skilllevel = getTotalSkillLevel(echskill);
        if (skilllevel > 0) {
            MapleStatEffect echeff = echskill.getEffect(skilllevel);
            if (targets > 0) {
                if (getBuffedValue(MapleBuffStat.ENERGY_CHARGE) == null) {
                    echeff.applyEnergyBuff(this, true, targets);
                } else {
                    Integer energyLevel = getBuffedValue(MapleBuffStat.ENERGY_CHARGE);

                    if (energyLevel < 10000) {
                        energyLevel = energyLevel + echeff.getX() * targets;
                        if (isAdmin()) {
                            energyLevel += 1000;
                            dropMessage("管理员特殊加成能量 目前 " + energyLevel);
                        }
                        this.client.getSession().write(CField.EffectPacket.showOwnBuffEffect(skillid, 2, getLevel(), skilllevel));
                        this.map.broadcastMessage(this, CField.EffectPacket.showBuffeffect(this.id, skillid, 2, getLevel(), skilllevel), false);

                        if (energyLevel >= 10000) {
                            energyLevel = 10000;
                        }
                        this.client.getSession().write(CWvsContext.BuffPacket.giveEnergyChargeTest(energyLevel, echeff.getDuration() / 1000));
                        setBuffedValue(MapleBuffStat.ENERGY_CHARGE, Integer.valueOf(energyLevel));
                    } else if (energyLevel == 10000) {
                        echeff.applyEnergyBuff(this, false, targets);
                        setBuffedValue(MapleBuffStat.ENERGY_CHARGE, Integer.valueOf(10001));
                    }
                }
            }
        }
    }

    public final void handleBattleshipHP(int damage) {
        if (damage < 0) {
            final MapleStatEffect effect = getStatForBuff(MapleBuffStat.MONSTER_RIDING);
            if (effect != null && effect.getSourceId() == 5221006) {
                battleshipHP += damage;
                client.getSession().write(CField.skillCooldown(5221999, battleshipHP / 10));
                if (battleshipHP <= 0) {
                    battleshipHP = 0;
                    client.getSession().write(CField.skillCooldown(5221006, effect.getCooldown(this)));
                    addCooldown(5221006, System.currentTimeMillis(), effect.getCooldown(this) * 1000);
                    cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
                }
            }
        }
    }

    public final void handleOrbgain() {
        int orbcount = getBuffedValue(MapleBuffStat.COMBO);
        Skill combo;
        Skill advcombo;
        switch (getJob()) {
            case 1110:
            case 1111:
            case 1112:
                combo = SkillFactory.getSkill(11111001);
                advcombo = SkillFactory.getSkill(11110005);
                break;
            default:
                combo = SkillFactory.getSkill(1111002);
                advcombo = SkillFactory.getSkill(1120003);
        }

        MapleStatEffect ceffect = null;
        int advComboSkillLevel = getTotalSkillLevel(advcombo);
        if (advComboSkillLevel > 0) {
            ceffect = advcombo.getEffect(advComboSkillLevel);
        } else if (getSkillLevel(combo) > 0) {
            ceffect = combo.getEffect(getTotalSkillLevel(combo));
        } else {
            return;
        }

        if (orbcount < ceffect.getX() + 1) {
            int neworbcount = orbcount + 1;
            if ((advComboSkillLevel > 0) && (ceffect.makeChanceResult())
                    && (neworbcount < ceffect.getX() + 1)) {
                neworbcount++;
            }

            EnumMap stat = new EnumMap(MapleBuffStat.class);
            stat.put(MapleBuffStat.COMBO, neworbcount);
            setBuffedValue(MapleBuffStat.COMBO, neworbcount);
            int duration = ceffect.getDuration();
            duration += (int) (getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis());

            this.client.getSession().write(CWvsContext.BuffPacket.giveBuff(combo.getId(), duration, stat, ceffect));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(getId(), stat, ceffect), false);
        }
    }

    public void handleOrbconsume(int howmany) {
        Skill normalcombo;

        switch (getJob()) {
            case 1110:
            case 1111:
            case 1112:
                normalcombo = SkillFactory.getSkill(11111001);
                break;
            default:
                normalcombo = SkillFactory.getSkill(1111002);
                break;
        }
        if (getSkillLevel(normalcombo) <= 0) {
            return;
        }
        MapleStatEffect ceffect = getStatForBuff(MapleBuffStat.COMBO);
        if (ceffect == null) {
            return;
        }
        EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
        stat.put(MapleBuffStat.COMBO, Math.max(1, getBuffedValue(MapleBuffStat.COMBO) - howmany));
        setBuffedValue(MapleBuffStat.COMBO, Math.max(1, getBuffedValue(MapleBuffStat.COMBO) - howmany));
        int duration = ceffect.getDuration();
        duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));

        client.getSession().write(BuffPacket.giveBuff(normalcombo.getId(), duration, stat, ceffect));
        map.broadcastMessage(this, BuffPacket.giveForeignBuff(getId(), stat, ceffect), false);
    }

    public void silentEnforceMaxHpMp() {
        stats.setMp(stats.getMp(), this);
        stats.setHp(stats.getHp(), true, this);
    }

    public void enforceMaxHpMp() {
        Map<MapleStat, Integer> statups = new EnumMap<>(MapleStat.class);
        if (stats.getMp() > stats.getCurrentMaxMp(this.getJob())) {
            stats.setMp(stats.getMp(), this);
            statups.put(MapleStat.MP, stats.getMp());
        }
        if (stats.getHp() > stats.getCurrentMaxHp()) {
            stats.setHp(stats.getHp(), this);
            statups.put(MapleStat.HP, stats.getHp());
        }
        if (statups.size() > 0) {
            client.getSession().write(CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public MapleMap getMap() {
        return map;
    }

    public MonsterBook getMonsterBook() {
        return monsterbook;
    }

    public void setMap(MapleMap newmap) {
        this.map = newmap;
    }

    public void setMap(int PmapId) {
        this.mapid = PmapId;
    }

    public int getMapId() {
        if (map != null) {
            return map.getId();
        }
        return mapid;
    }

    public byte getInitialSpawnpoint() {
        return initialSpawnPoint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public final String getBlessOfFairyOrigin() {
        return this.BlessOfFairy_Origin;
    }

    public final String getBlessOfEmpressOrigin() {
        return this.BlessOfEmpress_Origin;
    }
//the sp isnt fixed oh hold on

    public final short getLevel() {
        return level;
    }

    public final int getFame() {
        return fame;
    }

    public final int getFallCounter() {
        return fallcounter;
    }

    public final MapleClient getClient() {
        return client;
    }

    public final void setClient(final MapleClient client) {
        this.client = client;
    }

    public int getExp() {
        return exp;
    }

    public int getRemainingAp() {
        return remainingAp;
    }

    public int getRemainingSp() {
        return remainingSp[GameConstants.getSkillBook(job, 0)]; //default
    }

    public int getRemainingSp(final int skillbook) {
        return remainingSp[skillbook];
    }

    public int[] getRemainingSps() {
        return remainingSp;
    }

    public int getRemainingSpSize() {
        int ret = 0;
        for (int i = 0; i < remainingSp.length; i++) {
            if (remainingSp[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public int getRemainingHSp(final int mode) {
        return remainingHSp[mode];
    }

    public int[] getRemainingHSps() {
        return remainingHSp;
    }

    public int getRemainingHSpSize() {
        int ret = 0;
        for (int i = 0; i < remainingHSp.length; i++) {
            if (remainingHSp[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public short getHpApUsed() {
        return hpApUsed;
    }

    public boolean isHidden() {
        return getBuffSource(MapleBuffStat.DARKSIGHT) / 1000000 == 9;
    }

    public void setHpApUsed(short hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    @Override
    public byte getSkinColor() {
        return skinColor;
    }

    @Override
    public byte getSecondSkinColor() {
        return skinColor;
    }

    public void setSkinColor(byte skinColor) {
        this.skinColor = skinColor;
    }

    @Override
    public short getJob() {
        return job;
    }

    public String getJobName(short id) {
        return MapleJob.getName(MapleJob.getById(id));
    }

    @Override
    public byte getGender() {
        return gender;
    }

    @Override
    public byte getSecondGender() {
        if (GameConstants.isZero(getJob())) {
            return 1;
        }
        return gender; //todo zero
    }

    @Override
    public int getHair() {
        return hair;
    }

    @Override
    public int getSecondHair() {
        if (GameConstants.isAngelicBuster(getJob())) {
            return 37141; //default id here
        } else if (GameConstants.isZero(getJob())) {
            return 37623;
        }
        return hair;
    }

    @Override
    public int getFace() {
        return face;
    }

    @Override
    public int getSecondFace() {
        if (GameConstants.isAngelicBuster(getJob())) {
            return 21173; //default id here
        } else if (GameConstants.isZero(getJob())) {
            return 21290;
        }
        return face;
    }

    @Override
    public int getFaceMarking() {
        return faceMarking;
    }

    public void setFaceMarking(int mark) {
        this.faceMarking = mark;
    }

    @Override
    public int getEars() {
        return ears;
    }

    public void setEars(int ears) {
        this.ears = ears;
        equipChanged();
    }

    @Override
    public int getTail() {
        return tail;
    }

    public void setTail(int tail) {
        this.tail = tail;
        equipChanged();
    }

    @Override
    public int getElf() {
        return elf;
    }

    public void setElf(int elf) {
        this.elf = elf;
        equipChanged();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public void setFace(int face) {
        this.face = face;
        equipChanged();
    }

    public void setSecondHair(int hair) {
        this.secondHair = hair;
    }

    public void setSecondFace(int face) {
        this.secondFace = face;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public void setFallCounter(int fallcounter) {
        this.fallcounter = fallcounter;
    }

    public Point getOldPosition() {
        return old;
    }

    public void setOldPosition(Point x) {
        this.old = x;
    }

    public void setRemainingAp(int remainingAp) {
        this.remainingAp = remainingAp;
    }

    public void setRemainingSp(int remainingSp) {
        this.remainingSp[GameConstants.getSkillBook(job, 0)] = remainingSp; //default
    }

    public void setRemainingSp(int remainingSp, final int skillbook) {
        this.remainingSp[skillbook] = remainingSp;
    }

    public void setRemainingHSp(int mode, int amount) {
        this.remainingHSp[mode] = amount;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public void setInvincible(boolean invinc) {
        invincible = invinc;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public CheatTracker getCheatTracker() {
        return anticheat;
    }

    public MapleLieDetector getAntiMacro() {
        return this.antiMacro;
    }

    public BuddyList getBuddylist() {
        return buddylist;
    }

    public void addFame(int famechange) {
        this.fame += famechange;
        getTrait(MapleTraitType.charm).addLocalExp(famechange);
        if (this.fame >= 50) {
            finishAchievement(7);
        }
    }

    public void updateFame() {
        updateSingleStat(MapleStat.FAME, this.fame);
    }

    public void changeMapBanish(final int mapid, final String portal, final String msg) {
        dropMessage(5, msg);
        final MapleMap tomap = client.getChannelServer().getMapFactory().getMap(mapid);
        changeMap(tomap, tomap.getPortal(portal));
    }

    public void changeMap(final MapleMap to, final Point pos) {
        changeMapInternal(to, pos, CField.getWarpToMap(to, 0x80, this), null);
    }

    public void changeMap(final int to) {
        MapleMap map = ChannelServer.getInstance(getClient().getChannel()).getMapFactory().getMap(to);
        changeMapInternal(map, map.getPortal(0).getPosition(), CField.getWarpToMap(map, 0, this), map.getPortal(0));
    }

    public void changeMap(final MapleMap to) {
        changeMapInternal(to, to.getPortal(0).getPosition(), CField.getWarpToMap(to, 0, this), to.getPortal(0));
    }

    public void changeMap(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), null);
    }

    public void changeMapPortal(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), pto);
    }

    private void changeMapInternal(final MapleMap to, final Point pos, byte[] warpPacket, final MaplePortal pto) {
        try {
            if (to == null) {
                return;
            }
            if (getAntiMacro().inProgress()) {
                dropMessage(5, "被使用测谎仪时无法操作。");
                return;
            }
            MapleQuestStatus JailTime = getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST));
            if (JailTime != null && JailTime.getCustomData() != null && Long.parseLong(JailTime.getCustomData()) != 0 && to.getId() != GameConstants.JAIL) {
                changeMap(GameConstants.JAIL);
                return;
            }
            final int nowmapid = map.getId();
            if (eventInstance != null) {
                eventInstance.changedMap(this, to.getId());
            }
            final boolean pyramid = pyramidSubway != null;
            if (map.getId() == nowmapid) {
                client.getSession().write(warpPacket);
                final boolean shouldChange = !isClone() && client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
                final boolean shouldState = map.getId() == to.getId();
                if (shouldChange && shouldState) {
                    to.setCheckStates(false);
                }
                map.removePlayer(this);
//                if (ServerConstants.AntiKS) {
//
//                    MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(732648172));
//                    boolean antiks;
//                    if (stat == null || stat.getCustomData() == null) {
//                        antiks = false;
//                        stat.setCustomData(antiks + ";" + 0);
//                    } else {
//                        String[] statss = stat.getCustomData().split(";");
//                        antiks = Boolean.parseBoolean(statss[0]);
//                        List<MapleMonster> monsters = map.getAllMonster();
//                        for (MapleMapObject mmo : monsters) {
//                            MapleMonster m = (MapleMonster) mmo;
//                            if (m.getBelongsTo() == getId()) {
//                                stat.setCustomData(antiks + ";" + (Integer.parseInt(statss[1]) - 1));
//                                m.expireAntiKS();
//                            }
//                        }
//                    }
//                }
                if (shouldChange) {
                    map = to;
                    setPosition(pos);
                    to.addPlayer(this);
                    stats.relocHeal(this);
                    if (shouldState) {
                        to.setCheckStates(true);
                    }
                }
            }
            if (pyramid && pyramidSubway != null) { //checks if they had pyramid before AND after changing
                pyramidSubway.onChangeMap(this, to.getId());
            }
            updateDeathCount();

            if (deathCount < 1) {
//            dropMessage(0, "I must be very talented if I died so many times!");
//            World.Broadcast.broadcastMessage(CField.getGameMessage(name + " has somehow died 99 times! He is very talented!", (short) 13));
//            deathCount = 99;
//            client.getSession().write(EffectPacket.updateDeathCount(99)); //for fun
            }

        } catch (Exception ex) {
            FileoutputUtil.log("logs/过图异常.log", "错误信息：" + ex.toString());
        }
        client.getSession().write(CWvsContext.enableActions());
    }

    public void cancelChallenge() {
        if (challenge != 0 && client.getChannelServer() != null) {
            final MapleCharacter chr = client.getChannelServer().getPlayerStorage().getCharacterById(challenge);
            if (chr != null) {
                chr.dropMessage(6, getName() + " 拒绝了你的要求.");
                chr.setChallenge(0);
            }
            dropMessage(6, "拒绝挑战.");
            challenge = 0;
        }
    }

    public void leaveMap(MapleMap map) {
        controlledLock.writeLock().lock();
        visibleMapObjectsLock.writeLock().lock();
        try {
            for (MapleMonster mons : controlled) {
                if (mons != null) {
                    mons.setController(null);
                    mons.setControllerHasAggro(false);
                    mons.setControllerKnowsAboutAggro(false);
                    map.updateMonsterController(mons);
                }
            }
            controlled.clear();
            visibleMapObjects.clear();
        } finally {
            controlledLock.writeLock().unlock();
            visibleMapObjectsLock.writeLock().unlock();
        }
        if (chair != 0) {
            chair = 0;
        }
        clearLinkMid();
        cancelFishingTask();
        cancelChallenge();
        if (!getMechDoors().isEmpty()) {
            removeMechDoor();
        }
        cancelMapTimeLimitTask();
        if (getTrade() != null) {
            MapleTrade.cancelTrade(getTrade(), client, this);
        }
        //antiMacro.reset(); // reset lie detector
    }

    public void changeJob(short newJob) {
        try {
            cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
            this.job = newJob;
            this.remainingSp[0]++;
            updateSingleStat(MapleStat.JOB, newJob);
            if (newJob % 10 == 2) {
                this.remainingSp[0] += 2;
            }
            if (newJob > 0 && !isGM()) {
                resetStatsByJob(true);
            }
            if (newJob % 10 >= 1 && level >= 70) { //3rd job or higher. lucky for evans who get 80, 100, 120, 160 ap...
                remainingAp += 5;
                updateSingleStat(MapleStat.AVAILABLEAP, remainingAp);
            }

            updateSingleStat(MapleStat.AVAILABLESP, remainingSp[0]); // we don't care the value here

            int maxhp = stats.getMaxHp(), maxmp = stats.getMaxMp();

            switch (job) {
                case 100: // Warrior
                case 1100: // Soul Master
                case 2100: // Aran
                case 3100:
                case 3200:
                case 4100:
                case 5100:
                case 6100:
                    maxhp += Randomizer.rand(200, 250);
                    break;
                case 3110:
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 200: // Magician
                case 2200: //evan
                case 2210: //evan
                case 2700:
                case 4200:
                    maxmp += Randomizer.rand(100, 150);
                    break;
                case 300: // Bowman
                case 400: // Thief
                case 500: // Pirate
                case 2300:
                case 3300:
                case 3500:
                case 3600:
                case 6500:
                    maxhp += Randomizer.rand(100, 150);
                    maxmp += Randomizer.rand(25, 50);
                    break;
                case 110: // Fighter
                case 120: // Page
                case 130: // Spearman
                case 1110: // Soul Master
                case 2110: // Aran
                case 3210:
                case 4110:
                case 5110:
                case 6110:
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 210: // FP
                case 220: // IL
                case 230: // Cleric
                    maxmp += Randomizer.rand(400, 450);
                    break;
                case 310: // Bowman
                case 320: // Crossbowman
                case 410: // Assasin
                case 420: // Bandit
                case 430: // Semi Dualer
                case 510:
                case 520:
                case 530:
                case 2310:
                case 1310: // Wind Breaker
                case 1410: // Night Walker
                case 3310:
                case 3510:
                    maxhp += Randomizer.rand(200, 250);
                    maxhp += Randomizer.rand(150, 200);
                    break;
                case 800: // Manager
                case 900: // GM
                case 910: // SuperGM
                    break; //No additions for now
            }
            if (maxhp >= 30000) {
                maxhp = 30000;
            }
            if (maxmp >= 30000) {
                maxmp = 30000;
            }
            stats.setInfo(maxhp, maxmp, maxhp, maxmp);
            Map<MapleStat, Integer> statup = new EnumMap<>(MapleStat.class);
            statup.put(MapleStat.MAXHP, maxhp);
            statup.put(MapleStat.MAXMP, maxmp);
            statup.put(MapleStat.HP, maxhp);
            statup.put(MapleStat.MP, maxmp);
            characterCard.recalcLocalStats(this);
            stats.recalcLocalStats(this);
            client.getSession().write(CWvsContext.updatePlayerStats(statup, this));
            map.broadcastMessage(this, EffectPacket.showForeignEffect(getId(), 9), false);
            silentPartyUpdate();
            guildUpdate();
            familyUpdate();
            if (dragon != null) {
                map.broadcastMessage(CField.removeDragon(this.id));
                dragon = null;
            }
            if (haku != null) {
                haku = null;
            }
            baseSkills();
            checkForceShield();
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void handleDemonJob(int selection) {
        if (selection == 0) {
            changeJob((short) 3101);
        } else if (selection == 1) {
            changeJob((short) 3100);
        }
    }

    public void addJobSkills() {
        if (GameConstants.isPhantom(job)) {
            changeSkillLevel(SkillFactory.getSkill(job == 2412 ? 20031210 : 20031209), (byte) 1, (byte) 1);
            changeSkillLevel(SkillFactory.getSkill(job == 2412 ? 20031209 : 20031210), (byte) -1, (byte) 0);
        } else if (GameConstants.isXenon(job)) {
            int[] _skills = new int[]{job >= 3610 ? 30021235 : 0,
                job != 3002 ? 30021236 : 0, job != 3002 ? 30021237 : 0, 36000004,
                36100007, 36110007, 36120010};
            for (int skill : _skills) {
                Skill _skill = SkillFactory.getSkill(skill);
                if (_skill != null && _skill.canBeLearnedBy(job)) {
                    changeSkillLevel(_skill, (byte) 1, (byte) 1);
                }
            }
        }
    }

    public void addLevelSkills() {
        if (job == 3612 && level >= 200) {
            Skill skill = SkillFactory.getSkill(36120016);
            if (getSkillLevel(skill) < 1) {
                if (skill != null && skill.canBeLearnedBy(job)) {
                    changeSkillLevel(skill, (byte) 1, (byte) 1);
                }
            }
        }
    }

    public void baseSkills() {
        Map<Skill, SkillEntry> list = new HashMap<>();
        if (GameConstants.getJobNumber(job) >= 3) { //third job.
            List<Integer> base_skills = SkillFactory.getSkillsByJob(job);
            if (base_skills != null) {
                for (int i : base_skills) {
                    final Skill skil = SkillFactory.getSkill(i);
                    if (skil != null && !skil.isInvisible() && skil.isFourthJob() && getSkillLevel(skil) <= 0 && getMasterLevel(skil) <= 0 && skil.getMasterLevel() > 0) {
                        list.put(skil, new SkillEntry((byte) 0, (byte) skil.getMasterLevel(), SkillFactory.getDefaultSExpiry(skil))); //usually 10 master
                    } else if (skil != null && skil.getName() != null && (skil.getName().contains("Maple Warrior") || skil.getName().contains("Nova Warrior")) && getSkillLevel(skil) <= 0 && getMasterLevel(skil) <= 0) {
                        list.put(skil, new SkillEntry((byte) 0, (byte) 10, SkillFactory.getDefaultSExpiry(skil))); //hackish
                    }
                }

            }
        }
        Skill skil;

        int start = (job / 100) * 1000000;
        int end = job * 10000 + 3000;
//        System.out.println(start + " | " + end);
        for (int i = start; i < end; i++) {
            skil = SkillFactory.getSkill(i);
            if (skil != null) {
                if (getSkillLevel(skil) <= 0 && !skil.isInvisible()) {
                    boolean fourth = skil.isFourthJob();
                    int master = skil.getMasterLevel();
                    list.put(skil, new SkillEntry((byte) 0, (byte) (fourth ? master > 0 ? master : skil.getMaxLevel() : skil.getMaxLevel()), -1));
                }
            }
        }

        /*
         * if (job >= 2211 && job <= 2218) { // evan fix magic guard skil =
         * SkillFactory.getSkill(22111001); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 0, (byte) 20, -1)); } } } if (job >= 2100 && job <=
         * 2112) { int[] ss = {21120002, 21120001, 21120005, 21120003, 21121000,
         * 211210012, 211210004, 211210006, 211210007}; for (int i : ss) { skil
         * = SkillFactory.getSkill(i); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 0, (byte) 10, -1)); } } } } if (job >= 430 && job
         * <= 434) { final int[] ss0 = {4331002, 4330009, 4341004, 4341006,
         * 4341007, 4341011, 4340013}; for (int i : ss0) { skil =
         * SkillFactory.getSkill(i); if (skil != null) { if (getSkillLevel(skil)
         * <= 0) { // no total list.put(skil, new SkillEntry((byte) 0, (byte)
         * 10, -1)); } } } final int[] ss1 = {4311003, 4321006}; for (int i :
         * ss1) { skil = SkillFactory.getSkill(i); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 0, (byte) 5, -1)); } } } skil =
         * SkillFactory.getSkill(4341006); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 0, (byte) 10, -1)); } } skil =
         * SkillFactory.getSkill(4341011); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 0, (byte) 10, -1)); } } } if (job >= 430 && job <=
         * 434) { Skill skill; int ss431 = 4311003; int ss432 = 4321006; int[]
         * ss433 = {4331002, 4330009}; if (job == 431) { skill =
         * SkillFactory.getSkill(ss431); if (skill != null) {
         * changeSingleSkillLevel(skill, 0, (byte) 20); } } if (job == 432) {
         * skill = SkillFactory.getSkill(ss432); if (skill != null) {
         * changeSingleSkillLevel(skill, 0, (byte) 20); } } if (job == 433) {
         * for (int i : ss433) { skill = SkillFactory.getSkill(i); if (skill !=
         * null) { changeSingleSkillLevel(skill, 0, (byte) 20); } } } }
         *
         * if (GameConstants.isWildHunter(job)) { int[] ss = {30001061,
         * 30001062}; for (int i : ss) { skil = SkillFactory.getSkill(i); if
         * (skil != null) { if (getSkillLevel(skil) <= 0) { // no total
         * list.put(skil, new SkillEntry((byte) 1, (byte) 1, -1)); } } } }
         *
         * if (GameConstants.isMercedes(job)) { final int[] ss = {20021000,
         * 20021001, 20020002, 20020022, 20020109, 20021110, 20020111,
         * 20020112}; for (int i : ss) { skil = SkillFactory.getSkill(i); if
         * (skil != null) { if (getSkillLevel(skil) <= 0) { // no total
         * list.put(skil, new SkillEntry((byte) 1, (byte) 1, -1)); } } } skil =
         * SkillFactory.getSkill(20021181); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) -1, (byte) 0, -1)); } } skil =
         * SkillFactory.getSkill(20021166); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) -1, (byte) 0, -1)); } } } if
         * (GameConstants.isDemonSlayer(job)) { final int[] ss1 = {30011000,
         * 30011001, 30010002, 30010185, 30010112, 30010111, 30010110, 30010022,
         * 30011109}; for (int i : ss1) { skil = SkillFactory.getSkill(i); if
         * (skil != null) { if (getSkillLevel(skil) <= 0) { // no total
         * list.put(skil, new SkillEntry((byte) 1, (byte) 1, -1)); } } } final
         * int[] ss2 = {30011170, 30011169, 30011168, 30011167, 30010166,
         * 30010184, 30010183, 30010186}; for (int i : ss2) { skil =
         * SkillFactory.getSkill(i); if (skil != null) { if (getSkillLevel(skil)
         * <= 0) { // no total list.put(skil, new SkillEntry((byte) -1, (byte)
         * -1, -1)); } } } }
         *
         * if (GameConstants.isXenon(job)) { final int[] ss = {30020232,
         * 30020233, 30020234, 30020240, 30021236, 30021237}; for (int i : ss) {
         * skil = SkillFactory.getSkill(i); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { list.put(skil, new SkillEntry((byte) 1,
         * (byte) 1, -1)); } } } final int[] ss2 = {36120005, 36120006,
         * 36120015, 36121000, 36121001, 36121002, 36121003, 36121004, 36121007,
         * 36121011, 36121012, 36121013, 36121014}; for (int i : ss2) { skil =
         * SkillFactory.getSkill(i); if (skil != null) { if (getSkillLevel(skil)
         * <= 0) { list.put(skil, new SkillEntry((byte) 0, (byte)
         * skil.getMasterLevel(), -1)); } } } int[][] jobs = {{3002, 30020234},
         * {3600, 36000004}, {3610, 36100007}, {3611, 36110007}, {3612,
         * 36120010}}; for (int[] _job : jobs) { if (job >= _job[0]) { skil =
         * SkillFactory.getSkill(_job[1]); if (skil != null) { if
         * (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 1, (byte) 1, -1)); } } } } if (job == 3612 && level
         * >= 200) { skil = SkillFactory.getSkill(36120016); if (skil != null) {
         * if (getSkillLevel(skil) <= 0) { // no total list.put(skil, new
         * SkillEntry((byte) 1, (byte) 1, -1)); } } } }
         */
        if (!list.isEmpty()) {
            changeSkillsLevel(list);
        }
        //redemption for completed quests. holy fk. ex
        /*
         * List<MapleQuestStatus> cq = getCompletedQuests(); for
         * (MapleQuestStatus q : cq) { for (MapleQuestAction qs :
         * q.getQuest().getCompleteActs()) { if (qs.getType() ==
         * MapleQuestActionType.skill) { for (Pair<Integer, Pair<Integer,
         * Integer>> skill : qs.getSkills()) { final Skill skil =
         * SkillFactory.getSkill(skill.left); if (skil != null &&
         * getSkillLevel(skil) <= skill.right.left && getMasterLevel(skil) <=
         * skill.right.right) { changeSkillLevel(skil, (byte)
         * (int)skill.right.left, (byte) (int)skill.right.right); } } } else if
         * (qs.getType() == MapleQuestActionType.item) { //skillbooks for
         * (MapleQuestAction.QuestItem item : qs.getItems()) { if (item.itemid /
         * 10000 == 228 && !haveItem(item.itemid,1)) { //skillbook //check if we
         * have the skill final Map<String, Integer> skilldata =
         * MapleItemInformationProvider.getInstance().getSkillStats(item.itemid);
         * if (skilldata != null) { byte i = 0; Skill finalSkill = null; Integer
         * skillID = 0; while (finalSkill == null) { skillID =
         * skilldata.get("skillid" + i); i++; if (skillID == null) { break; }
         * final Skill CurrSkill = SkillFactory.getSkill(skillID); if (CurrSkill
         * != null && CurrSkill.canBeLearnedBy(job) && getSkillLevel(CurrSkill)
         * <= 0 && getMasterLevel(CurrSkill) <= 0) { finalSkill = CurrSkill; } }
         * if (finalSkill != null) { //may as well give the skill
         * changeSkillLevel(finalSkill, (byte) 0, (byte)10);
         * //MapleInventoryManipulator.addById(client, item.itemid, item.count);
         * } } } } } } }
         */
    }

    public void makeDragon() {
        dragon = new MapleDragon(this);
        map.broadcastMessage(CField.spawnDragon(dragon));
    }

    public MapleDragon getDragon() {
        return dragon;
    }

    public void makeHaku() {
        haku = new MapleHaku(this);
        map.broadcastMessage(CField.spawnHaku(haku));
    }

    public MapleHaku getHaku() {
        return haku;
    }

    public void gainAp(short ap) {
        this.remainingAp += ap;
        updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
    }

    public void gainSP(int sp) {
        this.remainingSp[GameConstants.getSkillBook(job, 0)] += sp; //default
        updateSingleStat(MapleStat.AVAILABLESP, 0); // we don't care the value here
        //client.getSession().write(InfoPacket.getSPMsg((byte) sp, (short) job));
    }

    public void gainSP(int sp, final int skillbook) {
        this.remainingSp[skillbook] += sp;
        updateSingleStat(MapleStat.AVAILABLESP, 0);
        //client.getSession().write(InfoPacket.getSPMsg((byte) sp, (short) 0));
    }

    public void gainHSP(int mode, int hsp) {
        this.remainingHSp[mode] += hsp;
        client.getSession().write(CWvsContext.updateHyperSp(mode, hsp));
    }

    public void setHSP(int mode, int hsp) {
        this.remainingHSp[mode] = hsp;
        client.getSession().write(CWvsContext.updateHyperSp(mode, hsp));
    }

    public void updateHSP() {
        for (int i = 0; i < 3; i++) {
            client.getSession().write(CWvsContext.updateHyperSp(i, remainingHSp[i]));
        }
    }

    public void resetSP(int sp) {
        for (int i = 0; i < remainingSp.length; i++) {
            this.remainingSp[i] = sp;
        }
        updateSingleStat(MapleStat.AVAILABLESP, 0);
    }

    public void resetAPSP() {
        resetSP(0);
        gainAp((short) -this.remainingAp);
    }

    public void resetSp(int jobid) {
        int skillpoint = 0;
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getJob()) && skil.getId() >= jobid * 1000000 && skil.getId() < (jobid + 1) * 1000000 && !skil.isInvisible()) {
                skillpoint += getSkillLevel(skil);
            }
        }
        gainSP(skillpoint, GameConstants.getSkillBook(jobid, 0));
        final Map<Skill, SkillEntry> skillmap = new HashMap<>(getSkills());
        final Map<Skill, SkillEntry> newList = new HashMap<>();
        for (Entry<Skill, SkillEntry> skill : skillmap.entrySet()) {
            newList.put(skill.getKey(), new SkillEntry((byte) 0, (byte) 0, -1));
        }
        changeSkillsLevel(newList);
        newList.clear();
        skillmap.clear();
    }

    public List<Integer> getProfessions() {
        List<Integer> prof = new ArrayList<>();
        for (int i = 9200; i <= 9204; i++) {
            if (getProfessionLevel(id * 10000) > 0) {
                prof.add(i);
            }
        }
        return prof;
    }

    public byte getProfessionLevel(int id) {
        int ret = getSkillLevel(id);
        if (ret <= 0) {
            return 0;
        }
        return (byte) ((ret >>> 24) & 0xFF); //the last byte
    }

    public short getProfessionExp(int id) {
        int ret = getSkillLevel(id);
        if (ret <= 0) {
            return 0;
        }
        return (short) (ret & 0xFFFF); //the first two byte
    }

    public boolean addProfessionExp(int id, int expGain) {
        int ret = getProfessionLevel(id);
        if (ret <= 0 || ret >= 10) {
            return false;
        }
        int newExp = getProfessionExp(id) + expGain;
        if (newExp >= GameConstants.getProfessionEXP(ret)) {
            //gain level
            changeProfessionLevelExp(id, ret + 1, newExp - GameConstants.getProfessionEXP(ret));
            int traitGain = (int) Math.pow(2, ret + 1);
            switch (id) {
                case 92000000:
                    traits.get(MapleTraitType.sense).addExp(traitGain, this);
                    break;
                case 92010000:
                    traits.get(MapleTraitType.will).addExp(traitGain, this);
                    break;
                case 92020000:
                case 92030000:
                case 92040000:
                    traits.get(MapleTraitType.craft).addExp(traitGain, this);
                    break;
            }
            return true;
        } else {
            changeProfessionLevelExp(id, ret, newExp);
            return false;
        }
    }

    public void changeProfessionLevelExp(int id, int level, int exp) {
        changeSingleSkillLevel(SkillFactory.getSkill(id), ((level & 0xFF) << 24) + (exp & 0xFFFF), (byte) 10);
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel) { //1 month
        if (skill == null) {
            return;
        }
        changeSingleSkillLevel(skill, newLevel, newMasterlevel, SkillFactory.getDefaultSExpiry(skill));
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel, long expiration) {
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        if (changeSkillData(skill, newLevel, newMasterlevel, expiration)) { // no loop, only 1
            list.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (GameConstants.isRecoveryIncSkill(skill.getId())) {
                hasRecovery = true;
            }
            if (skill.getId() < 80000000) {
                recalculate = true;
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, false));
        reUpdateStat(hasRecovery, recalculate);
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel, long expiration, boolean hyper) {
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        if (changeSkillData(skill, newLevel, newMasterlevel, expiration)) { // no loop, only 1
            list.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (GameConstants.isRecoveryIncSkill(skill.getId())) {
                hasRecovery = true;
            }
            if (skill.getId() < 80000000) {
                recalculate = true;
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, hyper));
        reUpdateStat(hasRecovery, recalculate);
    }

    public void changeSkillsLevel(final Map<Skill, SkillEntry> ss) {
        changeSkillsLevel(ss, false);
    }

    public void changeSkillsLevel(final Map<Skill, SkillEntry> ss, boolean hyper) {
        if (ss.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        for (final Entry<Skill, SkillEntry> data : ss.entrySet()) {
            if (changeSkillData(data.getKey(), data.getValue().skillevel, data.getValue().masterlevel, data.getValue().expiration)) {
                list.put(data.getKey(), data.getValue());
                if (GameConstants.isRecoveryIncSkill(data.getKey().getId())) {
                    hasRecovery = true;
                }
                if (data.getKey().getId() < 80000000) {
                    recalculate = true;
                }
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, hyper));
        reUpdateStat(hasRecovery, recalculate);
    }

    public void reUpdateStat(boolean hasRecovery, boolean recalculate) {
        changed_skills = true;
        if (hasRecovery) {
            stats.relocHeal(this);
        }
        if (recalculate) {
            stats.recalcLocalStats(this);
        }
    }

    public boolean changeSkillData(final Skill skill, int newLevel, byte newMasterlevel, long expiration) {
        if (skill == null || (!GameConstants.isApplicableSkill(skill.getId()) && !GameConstants.isApplicableSkill_(skill.getId()))) {
            return false;
        }
        if (newLevel == 0 && newMasterlevel == 0) {
            if (skills.containsKey(skill)) {
                skills.remove(skill);
            } else {
                return false; //nothing happen
            }
        } else {
            skills.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
        }
        return true;
    }

    public void changeSkillLevel_Skip(final Map<Skill, SkillEntry> skill, final boolean write) { // only used for temporary skills (not saved into db)
        if (skill.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> newL = new HashMap<>();
        for (final Entry<Skill, SkillEntry> z : skill.entrySet()) {
            if (z.getKey() == null) {
                continue;
            }
            newL.put(z.getKey(), z.getValue());
            if (z.getValue().skillevel == 0 && z.getValue().masterlevel == 0) {
                if (skills.containsKey(z.getKey())) {
                    skills.remove(z.getKey());
                } else {
                    continue;
                }
            } else {
                skills.put(z.getKey(), z.getValue());
            }
        }
        if (write && !newL.isEmpty()) {
            client.getSession().write(CWvsContext.updateSkills(newL, false));
        }
    }

    public void playerDead() {
        if (deathCount > 0) {
            deathCount--;
            updateDeathCount();
        }
        //final MapleStatEffect statss = getStatForBuff(MapleBuffStat.SOUL_STONE);
        //if (statss != null) {
        //    dropMessage(5, "You have been revived by Soul Stone.");
        //    getStats().setHp(((getStats().getMaxHp() / 100) * statss.getX()), this);
        //    setStance(0);
        //    changeMap(getMap(), getMap().getPortal(0));
        //    return;
        //}
        if (getEventInstance() != null) {
            getEventInstance().playerKilled(this);
        }
        cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
        cancelEffectFromBuffStat(MapleBuffStat.MORPH);
        cancelEffectFromBuffStat(MapleBuffStat.SOARING);
        cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
        cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
        cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
        cancelEffectFromBuffStat(MapleBuffStat.HP_BOOST_PERCENT);
        cancelEffectFromBuffStat(MapleBuffStat.MP_BOOST_PERCENT);
        cancelEffectFromBuffStat(MapleBuffStat.HP_BOOST);
        cancelEffectFromBuffStat(MapleBuffStat.MP_BOOST);
        cancelEffectFromBuffStat(MapleBuffStat.ENHANCED_MAXHP);
        cancelEffectFromBuffStat(MapleBuffStat.ENHANCED_MAXMP);
        cancelEffectFromBuffStat(MapleBuffStat.MAXHP);
        cancelEffectFromBuffStat(MapleBuffStat.MAXMP);
        dispelSummons();
        checkFollow();
        dotHP = 0;
        lastDOTTime = 0;
        if (GameConstants.isAzwanMap(getMapId())) {
            client.getSession().write(CWvsContext.showAzwanKilled());
        }
        if (!GameConstants.isBeginnerJob(job) && !inPVP() && !GameConstants.isAzwanMap(getMapId())) {
            int charms = getItemQuantity(5130000, false);
            if (charms > 0) {
                MapleInventoryManipulator.removeById(client, MapleInventoryType.CASH, 5130000, 1, true, false);
                charms--;
                if (charms > 0xFF) {
                    charms = 0xFF;
                }
                client.getSession().write(EffectPacket.useCharm((byte) charms, (byte) 0, true));
            } else {
                float diepercentage;
                long expforlevel = getNeededExp();
                if (map.isTown() || FieldLimitType.RegularExpLoss.check(map.getFieldLimit())) {
                    diepercentage = 0.01f;
                } else {
                    diepercentage = (float) (0.1f - ((traits.get(MapleTraitType.charisma).getLevel() / 20) / 100f) - (stats.expLossReduceR / 100f));
                }
                int v10 = (exp - (int) ((double) expforlevel * diepercentage));
                if (v10 < 0) {
                    v10 = 0;
                }
                this.exp = v10;
            }
            this.updateSingleStat(MapleStat.EXP, this.exp);
        }
        if (!stats.checkEquipDurabilitys(this, -100)) { //i guess this is how it works ?
            dropMessage(5, "一个物品已经用完的耐久性，但没有背包去.");
        } //lol
        if (pyramidSubway != null) {
            stats.setHp((short) 50, this);
            setXenonSurplus((short) 0);
            pyramidSubway.fail(this);
        }
    }

    public void updatePartyMemberHP() {
        if (party != null && client.getChannelServer() != null) {
            final int channel = client.getChannel();
            for (MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar != null && partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                    final MapleCharacter other = client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other != null) {
                        other.getClient().getSession().write(CField.updatePartyMemberHP(getId(), stats.getHp(), stats.getCurrentMaxHp()));
                    }
                }
            }
        }
    }

    public void receivePartyMemberHP() {
        if (party == null) {
            return;
        }
        int channel = client.getChannel();
        for (MaplePartyCharacter partychar : party.getMembers()) {
            if (partychar != null && partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                MapleCharacter other = client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                if (other != null) {
                    client.getSession().write(CField.updatePartyMemberHP(other.getId(), other.getStat().getHp(), other.getStat().getCurrentMaxHp()));
                }
            }
        }
    }

    public void healHP(int delta) {
        addHP(delta);
        //client.getSession().write(EffectPacket.showOwnHpHealed(delta));
        //getMap().broadcastMessage(this, EffectPacket.showHpHealed(getId(), delta), false);
    }

    public void healMP(int delta) {
        addMP(delta);
        //client.getSession().write(EffectPacket.showOwnHpHealed(delta));
        //getMap().broadcastMessage(this, EffectPacket.showHpHealed(getId(), delta), false);
    }

    /**
     * Convenience function which adds the supplied parameter to the current hp
     * then directly does a updateSingleStat.
     *
     * @see MapleCharacter#setHp(int)
     * @param delta
     */
    public void addHP(int delta) {
        if (stats.setHp(stats.getHp() + delta, this)) {
            updateSingleStat(MapleStat.HP, stats.getHp());
        }
    }

    /**
     * Convenience function which adds the supplied parameter to the current mp
     * then directly does a updateSingleStat.
     *
     * @see MapleCharacter#setMp(int)
     * @param delta
     */
    public void addMP(int delta) {
        addMP(delta, false);
    }

    public void addMP(int delta, boolean ignore) {
        if ((delta < 0 && GameConstants.isDemonSlayer(getJob())) || !GameConstants.isDemonSlayer(getJob()) || ignore) {
            if (stats.setMp(stats.getMp() + delta, this)) {
                updateSingleStat(MapleStat.MP, stats.getMp());
            }
        }
    }

    public void addMPHP(int hpDiff, int mpDiff) {
        Map<MapleStat, Integer> statups = new EnumMap<>(MapleStat.class);

        if (stats.setHp(stats.getHp() + hpDiff, this)) {
            statups.put(MapleStat.HP, stats.getHp());
        }
        if ((mpDiff < 0 && GameConstants.isDemonSlayer(getJob())) || !GameConstants.isDemonSlayer(getJob())) {
            if (stats.setMp(stats.getMp() + mpDiff, this)) {
                statups.put(MapleStat.MP, stats.getMp());
            }
        }
        if (statups.size() > 0) {
            client.getSession().write(CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public void updateSingleStat(MapleStat stat, int newval) {
        updateSingleStat(stat, newval, false);
    }

    /**
     * Updates a single stat of this MapleCharacter for the client. This method
     * only creates and sends an update packet, it does not update the stat
     * stored in this MapleCharacter instance.
     *
     * @param stat
     * @param newval
     * @param itemReaction
     */
    public void updateSingleStat(MapleStat stat, int newval, boolean itemReaction) {
        Map<MapleStat, Integer> statup = new EnumMap<>(MapleStat.class);
        statup.put(stat, newval);
        client.getSession().write(CWvsContext.updatePlayerStats(statup, itemReaction, this));
    }

    public void gainExp(final int total, final boolean show, final boolean inChat, final boolean white) {
        try {
            int prevexp = getExp();
            int needed = getNeededExp();
            if (total > 0) {
                stats.checkEquipLevels(this, total); //gms like
            }
            if (level >= maxLevel) {
                setExp(0);
                //if (exp + total > needed) {
                //    setExp(needed);
                //} else {
                //    exp += total;
                //}
            } else {
                boolean leveled = false;
                int tot = exp + total;
                if (tot >= needed) {
                    exp += total;
                    while (exp > needed) {
                        levelUp();
                        needed = getNeededExp();
                    }
                    leveled = true;
                    if (level >= maxLevel && !isIntern()) {
                        setExp(0);
                    } else {
                        needed = getNeededExp();
                        if (exp >= needed) {
                            setExp(needed - 1);
                        }
                    }
                } else {
                    exp += total;
                }
                if (total > 0) {
                    familyRep(prevexp, needed, leveled);
                }
            }
            if (total != 0) {
                if (exp < 0) { // After adding, and negative
                    if (total > 0) {
                        setExp(needed);
                    } else if (total < 0) {
                        setExp(0);
                    }
                }
                updateSingleStat(MapleStat.EXP, getExp());
                if (show) { // still show the expgain even if it's not there
                    client.getSession().write(InfoPacket.GainEXP_Others(total, inChat, white));
                }
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void setGmLevel(byte level) {
        this.gmLevel = level;
    }

    public void dispose(byte level) {
        this.gmLevel = level;
    }

    public void familyRep(long prevexp, long needed, boolean leveled) {
        if (mfc != null) {
            long onepercent = needed / 100;
            if (onepercent <= 0) {
                return;
            }
            int percentrep = (int) (getExp() / onepercent - prevexp / onepercent);
            if (leveled) {
                percentrep = 100 - percentrep + (level / 2);
            }
            if (percentrep > 0) {
                int sensen = World.Family.setRep(mfc.getFamilyId(), mfc.getSeniorId(), percentrep, level, name);
                if (sensen > 0) {
                    World.Family.setRep(mfc.getFamilyId(), sensen, percentrep / 2, level, name); //and we stop here
                }
            }
        }
    }

    public void gainExpMonster(final int gain, final boolean show, final boolean white, final byte pty, int Class_Bonus_EXP, int Equipment_Bonus_EXP, int Premium_Bonus_EXP, boolean partyBonusMob, final int partyBonusRate) {
        if (!isAlive()) {
            return;
        }
        if (canCheckPeriod()) {
            expirationTask2(true);
        }

        int Activity_Bonus_EXP = 0;
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = getRings(true);
        List<MapleRing> fRing = rings.getMid();
        for (MapleRing ring : fRing) {
            if (ring.getPartnerChrId() > 0) {
                MapleCharacter fRingChr = map.getCharacterById(ring.getPartnerChrId());
                if (fRingChr != null) {
                    Activity_Bonus_EXP = (int) (gain / 100.0D * 30.0D);
                }
            }
        }

        int Marriage_Bonus_EXP = 0;
        if (marriageId > 0) {
            MapleCharacter marrChr = map.getCharacterById(marriageId);
            if (marrChr != null) {
                Marriage_Bonus_EXP = (int) (gain / 100.0D * 30.0D);
                Activity_Bonus_EXP = 0;
            }
        }

        int partyinc = 0;
        int Team_Bonus_Rate = 0;//组队
        int prevexp = getExp();
        if (pty > 1) {
            final double rate = (partyBonusRate > 0 ? (partyBonusRate / 100.0) : (map == null || !partyBonusMob || map.getPartyBonusRate() <= 0 ? 0.05 : (map.getPartyBonusRate() / 100.0)));
            partyinc = (int) (((float) (gain * rate)) * (pty + (rate > 0.05 ? -1 : 1)));
            Team_Bonus_Rate = (pty - 1) * 5;
        }
        int total = gain /*+ Class_Bonus_EXP + Equipment_Bonus_EXP+ Premium_Bonus_EXP*/ + Marriage_Bonus_EXP + partyinc + /*Activity_Bonus_EXP*/ 0;
        if (gain > 0 && total < gain) { //just in case
            total = Integer.MAX_VALUE;
        }
        if (total > 0) {
            stats.checkEquipLevels(this, total); //gms like
        }
        int needed = getNeededExp();
        if (level >= maxLevel) {
            setExp(0);
            //if (exp + total > needed) {
            //    setExp(needed);
            //} else {
            //    exp += total;
            //}
        } else {
            boolean leveled = false;
            if (exp + total >= needed || exp >= needed) {
                exp += total;
                if (exp + total >= needed) {
                    exp += total;
                    levelUp();
                    leveled = true;
                    needed = GameConstants.getExpNeededForLevel(level);
                    if (exp > needed) {
                        setExp(needed);
                    }
                } else {
                    exp += total;
                }
                leveled = true;
                if (level >= maxLevel && !isIntern()) {
                    setExp(0);
                } else {
                    needed = getNeededExp();
                    if (exp >= needed) {
                        setExp(needed);
                    }
                }
            } else {
                exp += total;
            }
            if (total > 0) {
                familyRep(prevexp, needed, leveled);
            }
        }
        if (gain != 0) {
            if (exp < 0) { // After adding, and negative
                if (gain > 0) {
                    setExp(getNeededExp());
                } else if (gain < 0) {
                    setExp(0);
                }
            }

            //if (ServerConfig.Activity_Bonus_Rate > 0) {
            // Activity_Bonus_EXP = (int) (gain * ServerConfig.Activity_Bonus_Rate);
            //}
            updateSingleStat(MapleStat.EXP, getExp());
            int expM = gain + partyinc + Marriage_Bonus_EXP;
            if (show) { // still show the expgain even if it's not there
                client.getSession().write(InfoPacket.GainEXP_Monster(expM, white, partyinc,/* Class_Bonus_EXP*/ 0, /*Equipment_Bonus_EXP*/ 0, /*Premium_Bonus_EXP*/ 0, Marriage_Bonus_EXP, Team_Bonus_Rate, /*Activity_Bonus_EXP*/ 0));
            }
        }
    }

    public void forceReAddItem_NoUpdate(Item item, MapleInventoryType type) {
        getInventory(type).removeSlot(item.getPosition());
        getInventory(type).addFromDB(item);
    }

    public void forceReAddItem(Item item, MapleInventoryType type) { //used for stuff like durability, item exp/level, probably owner?
        forceReAddItem_NoUpdate(item, type);
        type = MapleInventoryType.EQUIP;
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(InventoryPacket.addInventorySlot(type, item, false));
//            client.getSession().write(InventoryPacket.updateSpecialItemUse(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType(), this));
        }
    }

    public void forceReAddItem_Flag(Item item, MapleInventoryType type) { //used for flags
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(InventoryPacket.updateSpecialItemUse_(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType(), this));
        }
    }

    public void forceReAddItem_Book(Item item, MapleInventoryType type) { //used for mbook
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(CWvsContext.upgradeBook(item, this));
        }
    }

    public void silentPartyUpdate() {
        if (party != null) {
            World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, new MaplePartyCharacter(this));
        }
    }

    public boolean isSuperGM() {
        return gmLevel >= 3;
    }

    public boolean isIntern() {
        return gmLevel >= 1;
    }

    public boolean isGM() {
        return gmLevel >= 2;
    }

    public boolean isAdmin() {
        return gmLevel >= 4;
    }

    public int getGMLevel() {
        return gmLevel;
    }

    public boolean hasGmLevel(int level) {
        return gmLevel >= level;
    }

    public boolean isDonator() {
        //if (getInventory(MapleInventoryType.EQUIP).findById(1002419) != null) {
        //updateDonorMedal(getInventory(MapleInventoryType.EQUIP).findById(1002419));
        // }
        //if (getInventory(MapleInventoryType.EQUIPPED).findById(1002419) != null) {
        //    updateDonorMedal(getInventory(MapleInventoryType.EQUIPPED).findById(1002419));
        // }
        if (getInventory(MapleInventoryType.EQUIP).findById(1142229) != null) {
            //      updateDonorMedal(getInventory(MapleInventoryType.EQUIP).findById(1142229));
            return true;
        }
        if (getInventory(MapleInventoryType.EQUIPPED).findById(1142229) != null) {
            //  updateDonorMedal(getInventory(MapleInventoryType.EQUIPPED).findById(1142229));
            return true;
        }
        return false;
    }

    public void updateDonorMedal(Item medal) {
        Equip eq = (Equip) medal;
        eq.setStr((short) 50);
        eq.setDex((short) 50);
        eq.setInt((short) 50);
        eq.setLuk((short) 50);
        eq.setWatk((short) 30);
        eq.setMatk((short) 30);
        eq.setWdef((short) 300);
        eq.setMdef((short) 300);
        eq.setSpeed((short) 40);
        eq.setJump((short) 20);
        eq.setPotential1(60001);
        eq.setPotential2(60002);
        eq.setPotential3(60003);
    }

    public final MapleInventory getInventory(MapleInventoryType type) {
        return inventory[type.ordinal()];
    }

    public final MapleInventory[] getInventorys() {
        return inventory;
    }

    public final void expirationTask2(boolean packet) {
        long expiration;
        final List<Integer> ret = new ArrayList<>();
        final long currenttime = System.currentTimeMillis();
        final List<Pair<MapleInventoryType, Item>> toberemove = new ArrayList<>(); // This is here to prevent deadlock.
        final List<Item> tobeunlock = new ArrayList<>(); // This is here to prevent deadlock.

        for (final MapleInventoryType inv : MapleInventoryType.values()) {
            for (final Item item : getInventory(inv)) {
                expiration = item.getExpiration();

                if (expiration != -1 && !GameConstants.isPet(item.getItemId()) && currenttime > expiration) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        tobeunlock.add(item);
                    } else if (currenttime > expiration) {
                        toberemove.add(new Pair<>(inv, item));
                    }
                } else if (item.getItemId() == 5000054 && item.getPet() != null && item.getPet().getSecondsLeft() <= 0) {
                    toberemove.add(new Pair<>(inv, item));
                }
            }
        }
        Item item;
        for (final Pair<MapleInventoryType, Item> itemz : toberemove) {
            item = itemz.getRight();
            ret.add(item.getItemId());
            if (packet) {
                getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false, this);
            } else {
                getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false);
            }
        }
        for (final Item itemz : tobeunlock) {
            itemz.setExpiration(-1);
            itemz.setFlag((byte) (itemz.getFlag() - ItemFlag.LOCK.getValue()));
        }

        this.pendingExpiration = ret;

        final Map<Skill, SkillEntry> skilz = new HashMap<>();
        final List<Skill> toberem = new ArrayList<>();
        for (Entry<Skill, SkillEntry> skil : skills.entrySet()) {
            if (skil.getValue().expiration != -1 && currenttime > skil.getValue().expiration) {
                toberem.add(skil.getKey());
            }
        }
        for (Skill skil : toberem) {
            skilz.put(skil, new SkillEntry(0, (byte) 0, -1));
            this.skills.remove(skil);
            changed_skills = true;
        }
        this.pendingSkills = skilz;
    }

    public final void expirationTask(boolean pending, boolean firstLoad) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (pending) {
            if (pendingExpiration != null) {
                for (Integer z : pendingExpiration) {
                    client.getSession().write(InfoPacket.itemExpired(z));
                    if (!firstLoad) {
                        final Pair<Integer, String> replace = ii.replaceItemInfo(z.intValue());
                        if (replace != null && replace.left > 0 && replace.right.length() > 0) {
                            dropMessage(5, replace.right);
                        }
                    }
                }
            }
            pendingExpiration = null;
            if (pendingSkills != null) {
                client.getSession().write(CWvsContext.updateSkills(pendingSkills, false));
                for (Skill z : pendingSkills.keySet()) {
                    client.getSession().write(CWvsContext.broadcastMsg(5, "[" + SkillFactory.getSkillName(z.getId()) + "] 技能已过期，将无法使用."));
                }
            } //not real msg
            pendingSkills = null;
            return;
        }
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(GameConstants.PENDANT_SLOT));
        long expiration;
        final List<Integer> ret = new ArrayList<>();
        final long currenttime = System.currentTimeMillis();
        final List<Triple<MapleInventoryType, Item, Boolean>> toberemove = new ArrayList<>(); // This is here to prevent deadlock.
        final List<Item> tobeunlock = new ArrayList<>(); // This is here to prevent deadlock.

        for (final MapleInventoryType inv : MapleInventoryType.values()) {
            for (final Item item : getInventory(inv)) {
                expiration = item.getExpiration();

                if ((expiration != -1 && !GameConstants.isPet(item.getItemId()) && currenttime > expiration) || (firstLoad && ii.isLogoutExpire(item.getItemId()))) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        tobeunlock.add(item);
                    } else if (currenttime > expiration) {
                        toberemove.add(new Triple<>(inv, item, false));
                    }
                } else if (item.getItemId() == 5000054 && item.getPet() != null && item.getPet().getSecondsLeft() <= 0) {
                    toberemove.add(new Triple<>(inv, item, false));
                } else if (item.getPosition() == -59) {
                    if (stat == null || stat.getCustomData() == null || Long.parseLong(stat.getCustomData()) < currenttime) {
                        toberemove.add(new Triple<>(inv, item, true));
                    }
                }
            }
        }
        Item item;
        for (final Triple<MapleInventoryType, Item, Boolean> itemz : toberemove) {
            item = itemz.getMid();
            getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false);
            if (itemz.getRight() && getInventory(GameConstants.getInventoryType(item.getItemId())).getNextFreeSlot() > -1) {
                item.setPosition(getInventory(GameConstants.getInventoryType(item.getItemId())).getNextFreeSlot());
                getInventory(GameConstants.getInventoryType(item.getItemId())).addFromDB(item);
            } else {
                ret.add(item.getItemId());
            }
            if (!firstLoad) {
                final Pair<Integer, String> replace = ii.replaceItemInfo(item.getItemId());
                if (replace != null && replace.left > 0) {
                    Item theNewItem;
                    if (GameConstants.getInventoryType(replace.left) == MapleInventoryType.EQUIP) {
                        theNewItem = ii.getEquipById(replace.left);
                        theNewItem.setPosition(item.getPosition());
                    } else {
                        theNewItem = new Item(replace.left, item.getPosition(), (short) 1, (byte) 0);
                    }
                    getInventory(itemz.getLeft()).addFromDB(theNewItem);
                }
            }
        }
        for (final Item itemz : tobeunlock) {
            itemz.setExpiration(-1);
            itemz.setFlag((byte) (itemz.getFlag() - ItemFlag.LOCK.getValue()));
        }
        this.pendingExpiration = ret;

        final Map<Skill, SkillEntry> skilz = new HashMap<>();
        final List<Skill> toberem = new ArrayList<>();
        for (Entry<Skill, SkillEntry> skil : skills.entrySet()) {
            if (skil.getValue().expiration != -1 && currenttime > skil.getValue().expiration) {
                toberem.add(skil.getKey());
            }
        }
        for (Skill skil : toberem) {
            skilz.put(skil, new SkillEntry(0, (byte) 0, -1));
            this.skills.remove(skil);
            changed_skills = true;
        }
        this.pendingSkills = skilz;
        if (stat != null && stat.getCustomData() != null && Long.parseLong(stat.getCustomData()) < currenttime) { //expired bro
            quests.remove(MapleQuest.getInstance(7830));
            quests.remove(MapleQuest.getInstance(GameConstants.PENDANT_SLOT));
        }
    }

    public void refreshBattleshipHP() {
        if (getJob() == 592) {
            client.getSession().write(CWvsContext.giveKilling(currentBattleshipHP()));
        }
    }

    public MapleShop getShop() {
        return shop;
    }

    public void setShop(MapleShop shop) {
        this.shop = shop;
    }

    public int getMeso() {
        return meso;
    }

    public final int[] getSavedLocations() {
        return savedLocations;
    }

    public int getSavedLocation(SavedLocationType type) {
        return savedLocations[type.getValue()];
    }

    public void saveLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = getMapId();
        changed_savedlocations = true;
    }

    public void saveLocation(SavedLocationType type, int mapz) {
        savedLocations[type.getValue()] = mapz;
        changed_savedlocations = true;
    }

    public void clearSavedLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = -1;
        changed_savedlocations = true;
    }

    public void gainMeso(int gain, boolean show) {
        gainMeso(gain, show, false, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions) {
        gainMeso(gain, show, enableActions, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions, boolean inChat) {
        if (meso + gain < 0) {
            gain = -meso;
            meso = 0;
        } else if (meso + gain > 2147483647) {
            gain = 2147483647 - meso;
            meso = 2147483647;
        } else {
            meso += gain;
        }
        updateSingleStat(MapleStat.MESO, meso, enableActions);
        //client.getSession().write(CWvsContext.enableActions());
        if (show) {
            client.getSession().write(InfoPacket.showMesoGain(gain, inChat));
        }
    }

    public void controlMonster(MapleMonster monster, boolean aggro) {
        if (clone || monster == null) {
            return;
        }
        monster.setController(this);
        controlledLock.writeLock().lock();
        try {
            controlled.add(monster);
        } finally {
            controlledLock.writeLock().unlock();
        }
        client.getSession().write(MobPacket.controlMonster(monster, false, aggro));

        monster.sendStatus(client);
    }

    public void stopControllingMonster(MapleMonster monster) {
        if (clone || monster == null) {
            return;
        }
        controlledLock.writeLock().lock();
        try {
            if (controlled.contains(monster)) {
                controlled.remove(monster);
            }
        } finally {
            controlledLock.writeLock().unlock();
        }
    }

    public void checkMonsterAggro(MapleMonster monster) {
        if (clone || monster == null) {
            return;
        }
        if (monster.getController() == this) {
            monster.setControllerHasAggro(true);
        } else {
            monster.switchController(this, true);
        }
    }

    public int getControlledSize() {
        return controlled.size();
    }

    public int getAccountID() {
        return accountid;
    }

    public void mobKilled(final int id, final int skillID) {
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() != 1 || !q.hasMobKills()) {
                continue;
            }
            if (q.mobKilled(id, skillID)) {
                client.getSession().write(InfoPacket.updateQuestMobKills(q));
                if (q.getQuest().canComplete(this, null)) {
                    client.getSession().write(CWvsContext.showQuestCompletion(q.getQuest().getId()));
                }
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 1 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<Pair<Integer, Long>> getCompletedMedals() {
        List<Pair<Integer, Long>> ret = new ArrayList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked() && q.getQuest().getMedalItem() > 0 && GameConstants.getInventoryType(q.getQuest().getMedalItem()) == MapleInventoryType.EQUIP) {
                ret.add(new Pair<>(q.getQuest().getId(), q.getCompletionTime()));
            }
        }
        return ret;
    }

    public Map<Skill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public int getTotalSkillLevel(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        return Math.min(skill.getTrueMax(), ret.skillevel + (skill.isBeginnerSkill() ? 0 : (stats.combatOrders + (skill.getMaxLevel() > 10 ? stats.incAllskill : 0) + stats.getSkillIncrement(skill.getId()))));
    }

    public int getAllSkillLevels() {
        int rett = 0;
        for (Entry<Skill, SkillEntry> ret : skills.entrySet()) {
            if (!ret.getKey().isBeginnerSkill() && !ret.getKey().isSpecialSkill() && ret.getValue().skillevel > 0) {
                rett += ret.getValue().skillevel;
            }
        }
        return rett;
    }

    public long getSkillExpiry(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        return ret.expiration;
    }

    public int getSkillLevel(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillevel <= 0) {
            return 0;
        }
        return ret.skillevel;
    }

    public byte getMasterLevel(final int skill) {
        return getMasterLevel(SkillFactory.getSkill(skill));
    }

    public byte getMasterLevel(final Skill skill) {
        final SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.masterlevel;
    }
    public final int maxLevel = ServerConfig.maxLevel;

    public void levelUp() {
        Skill improvingMaxHP = null;
        int improvingMaxHPLevel = 0;
        Skill improvingMaxMP = null;
        int improvingMaxMPLevel = 0;

        if (getLevel() >= maxLevel) {
            return;
        }

        remainingAp += 5;
        int maxhp = stats.getMaxHp();
        int maxmp = stats.getMaxMp();

        if (GameConstants.isBeginnerJob(job)) { // Beginner
            maxhp += Randomizer.rand(12, 16);
            maxmp += Randomizer.rand(10, 12);
        } else if (job >= 100 && job <= 132) { // 战士
            improvingMaxHP = SkillFactory.getSkill(1000001);
            improvingMaxHPLevel = getSkillLevel(improvingMaxHP);
            //maxhp += Randomizer.rand(48, 52);
            maxhp += Randomizer.rand(24, 28);
            maxmp += Randomizer.rand(4, 6);
        } else if (job >= 200 && job <= 232) { // Magician
            improvingMaxMP = SkillFactory.getSkill(2000001);
            improvingMaxMPLevel = getSkillLevel(improvingMaxMP);
            maxhp += Randomizer.rand(10, 14);
            //maxmp += Randomizer.rand(48, 52);
            maxmp += Randomizer.rand(22, 24);
        } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434)) { // Bowman, Thief, Wind Breaker and Night Walker
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(14, 16);
            //} else if ((job >= 510 && job <= 512)) { // Pirate
            //   maxhp += Randomizer.rand(37, 41);
            //   maxmp += Randomizer.rand(18, 22);
        } else if ((job >= 500 && job <= 532)) { // Pirate
            //maxhp += Randomizer.rand(20, 24);
            improvingMaxHP = SkillFactory.getSkill(5100000);
            improvingMaxHPLevel = getSkillLevel(improvingMaxHP);
            maxhp += Randomizer.rand(22, 26);
            maxmp += Randomizer.rand(18, 23);
        } else if (job >= 2100 && job <= 2112) { // Aran
            maxhp += Randomizer.rand(50, 52);
            maxmp += Randomizer.rand(4, 6);
        } else { // GameMaster
            maxhp += Randomizer.rand(50, 100);//its already there lol
            maxmp += Randomizer.rand(50, 100);
        }
        if (improvingMaxHPLevel > 0) {
            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getX();
        }
        if (improvingMaxMPLevel > 0) {
            maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getX();
        }
        maxmp += stats.getTotalInt() / 10;

        exp -= getNeededExp();
        level += 1;
        setExp(0);
        if (!GameConstants.isBeginnerJob(job)) {
            remainingSp[0] += 3;
        } else if (level <= 10) {
            stats.str += remainingAp;
            remainingAp = 0;
        }

        maxhp = Math.min(30000, Math.abs(maxhp));
        maxmp = Math.min(30000, Math.abs(maxmp));
        final Map<MapleStat, Integer> statup = new EnumMap<>(MapleStat.class);

        statup.put(MapleStat.MAXHP, maxhp);
        statup.put(MapleStat.MAXMP, maxmp);
        statup.put(MapleStat.HP, 30000);  // 因为不知道装备加成多少，这里直接取最大值。
        statup.put(MapleStat.MP, 30000);
        statup.put(MapleStat.EXP, (int) exp);
        statup.put(MapleStat.LEVEL, (int) level);
        statup.put(MapleStat.STR, stats.getStr());
        updateSingleStat(MapleStat.AVAILABLESP, 0);

        statup.put(MapleStat.AVAILABLEAP, remainingAp);
        statup.put(MapleStat.AVAILABLESP, remainingSp[0]);
        stats.setInfo(maxhp, maxmp, maxhp, maxmp);
        client.getSession().write(CWvsContext.updatePlayerStats(statup, this));
        map.broadcastMessage(this, EffectPacket.showForeignEffect(getId(), 0), false);
        characterCard.recalcLocalStats(this);
        stats.recalcLocalStats(this);
        silentPartyUpdate();
        guildUpdate();
        familyUpdate();
        if (!isGM()) {
            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[升级提示]" + getName() + "在" + getMap().getMapName() + " 等级达到" + level + "级，大家一起祝贺一下吧。"));
            //if (level == 10 || level == 20 || level == 30 || level == 40 || level == 50 || level == 60 || level == 70 || level == 80 || level == 90 || level == 100 || level == 110 || level == 120 || level == 130 || level == 140 || level == 150 || level == 160 || level == 170 || level == 180 || level == 190 || level == 200) {
            //StringBuilder sb = new StringBuilder();
            //sb.append("[公告事项]");
            //addMedalString(client.getPlayer(), sb);
            //sb.append(getName()).append("终于达到了").append(level).append("级,大家一起祝贺下吧。");
            //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(6, sb.toString()));
            //}
        }
    }

    public void obtainHyperSP() {
        if (GameConstants.isZero(job)) {
            return; //no hypers for zero
        }
        switch (level) {
            case 140:
            case 160:
            case 190:
                gainHSP(0, 1);
                gainHSP(1, 1);
                break;
            case 150:
            case 170:
                gainHSP(0, 1);
                gainHSP(2, 1);
                break;
            case 180:
                gainHSP(1, 1);
                break;
            case 200:
                gainHSP(0, 1);
                gainHSP(1, 1);
                gainHSP(2, 1);
                break;
        }
    }

    public void autoJob() {
        if (GameConstants.isEvan(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 2200);
                    break;
                case 20:
                    changeJob((short) (job + 10));
                    break;
                case 30:
                case 40:
                case 50:
                case 60:
                case 80:
                case 100:
                case 120:
                case 160:
                    changeJob((short) (job + 1));
                    break;
            }
        } else if (GameConstants.isMercedes(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 2300);
                    break;
                case 30:
                    changeJob((short) 2310);
                    break;
                case 60:
                    changeJob((short) 2311);
                    break;
                case 100:
                    changeJob((short) 2312);
                    break;
            }
        } else if (GameConstants.isHayato(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 4100);
                    break;
                case 30:
                    changeJob((short) 4110);
                    break;
                case 60:
                    changeJob((short) 4111);
                    break;
                case 100:
                    changeJob((short) 4112);
                    break;
            }
        } else if (GameConstants.isKanna(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 4200);
                    break;
                case 30:
                    changeJob((short) 4210);
                    break;
                case 60:
                    changeJob((short) 4211);
                    break;
                case 100:
                    changeJob((short) 4212);
                    break;
            }
        } else if (GameConstants.isAngelicBuster(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 6500);
                    break;
                case 30:
                    changeJob((short) 6510);
                    break;
                case 60:
                    changeJob((short) 6511);
                    break;
                case 100:
                    changeJob((short) 6512);
                    break;
            }
        } else if (GameConstants.isLuminous(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 2700);
                    break;
                case 30:
                    changeJob((short) 2710);
                    break;
                case 60:
                    changeJob((short) 2711);
                    break;
                case 100:
                    changeJob((short) 2712);
                    break;
            }
        } else if (GameConstants.isKaiser(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 6100);
                    break;
                case 30:
                    changeJob((short) 6110);
                    break;
                case 60:
                    changeJob((short) 6111);
                    break;
                case 100:
                    changeJob((short) 6112);
                    break;
            }
        } else if (GameConstants.isXenon(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 3600);
                    break;
                case 30:
                    changeJob((short) 3610);
                    break;
                case 60:
                    changeJob((short) 3611);
                    break;
                case 100:
                    changeJob((short) 3612);
                    break;
            }
        } else if (GameConstants.isDualBlade(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) 400);
                    break;
                case 20:
                    changeJob((short) 430);
                    break;
                case 30:
                    changeJob((short) 431);
                    break;
                case 45:
                    changeJob((short) 432);
                    break;
                case 60:
                    changeJob((short) 433);
                    break;
                case 100:
                    changeJob((short) 434);
                    break;
            }
        } else if (GameConstants.isJett(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) 570);
                    break;
                case 60:
                    changeJob((short) 571);
                    break;
                case 100:
                    changeJob((short) 572);
                    break;
            }
        }
    }

    public void sendPolice(int greason, String reason, int duration) {
        client.getSession().write(CWvsContext.GMPoliceMessage(String.format("You have been blocked by #bMapleGM for the %s reason.#k", "HACK")));
        ban(reason, true);
        WorldTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                client.disconnect(false, false); //FAGGOTS
            }
        }, duration);
    }

    public void sendPolice(String text) {
        client.getSession().write(CWvsContext.GMPoliceMessage(text));
        ban(text, true);
        WorldTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                client.disconnect(false, false); //FAGGOTS
            }
        }, 6000);
    }

    public void changeKeybinding(int key, byte type, int action) {
        if (type != 0) {
            keylayout.Layout().put(key, new Pair<>(type, action));
        } else {
            keylayout.Layout().remove(key);
        }
    }

    public void changeKeybinding(String key_name, byte type, int action) { //not finished yet TODO: finish it
        int key;
        switch (key_name.toUpperCase()) {
            case "F1":
            case "F2":
            case "F3":
            case "F4":
            case "F5":
            case "F6":
            case "F7":
            case "F8":
            case "F9":
            case "F10":
            case "F11":
            case "F12":
                key = 58 + Integer.parseInt(key_name.replace("F", ""));
                break;
            case "1":
            case "!":
            case "2":
            case "@":
            case "3":
            case "#":
            case "4":
            case "$":
            case "5":
            case "%":
            case "6":
            case "^":
            case "7":
            case "&":
            case "8":
            case "*":
            case "9":
            case "(":
                key = 1 + Integer.parseInt(key_name);
                break;
            case "0":
            case ")":
                key = 11;
                break;
            case "-":
            case "_":
                key = 12;
                break;
            case "=":
            case "+":
                key = 13;
                break;
            default:
                key = -1;
                break;
        }
        if (key != -1) {
            if (type != 0) {
                keylayout.Layout().put(key, new Pair<>(type, action));
            } else {
                keylayout.Layout().remove(key);
            }
        }
    }

    public void sendMacros() {
        for (int i = 0; i < 5; i++) {
            if (skillMacros[i] != null) {
                client.getSession().write(CField.getMacros(skillMacros));
                break;
            }
        }
    }

    public void updateMacros(int position, SkillMacro updateMacro) {
        skillMacros[position] = updateMacro;
        changed_skillmacros = true;
    }

    public final SkillMacro[] getMacros() {
        return skillMacros;
    }

    public void tempban(String reason, Calendar duration, int greason, boolean IPMac) {
        if (IPMac) {
            client.banMacs();
        }
        //client.getSession().write(CWvsContext.GMPoliceMessage(true));
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps;
            if (IPMac) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, client.getSession().getRemoteAddress().toString().split(":")[0]);
                ps.execute();
                ps.close();
            }

            client.getSession().close();
            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

            ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?");
            Timestamp TS = new Timestamp(duration.getTimeInMillis());
            ps.setTimestamp(1, TS);
            ps.setString(2, reason);
            ps.setInt(3, greason);
            ps.setInt(4, accountid);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error while tempbanning" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }

    }

    public int getMaxHp() {
        return getStat().getMaxHp();
    }

    public int getMaxMp() {
        return getStat().getMaxMp();
    }

    public void setHp(int amount) {
        getStat().setHp(amount, this);
    }

    public void setMp(int amount) {
        getStat().setMp(amount, this);
    }

    public final boolean ban(String reason, boolean IPMac, boolean autoban, boolean hellban) {
        if (lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }
        //client.getSession().write(CWvsContext.GMPoliceMessage(true));
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE id = ?");
            ps.setInt(1, autoban ? 2 : 1);
            ps.setString(2, reason);
            ps.setInt(3, accountid);
            ps.execute();
            ps.close();

            if (IPMac) {
                client.banMacs();
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, client.getSessionIPAddress());
                ps.execute();
                ps.close();

                if (hellban) {
                    try (PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
                        psa.setInt(1, accountid);
                        try (ResultSet rsa = psa.executeQuery()) {
                            if (rsa.next()) {
                                try (PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE email = ? OR SessionIP = ?")) {
                                    pss.setInt(1, autoban ? 2 : 1);
                                    pss.setString(2, reason);
                                    pss.setString(3, rsa.getString("email"));
                                    pss.setString(4, client.getSessionIPAddress());
                                    pss.execute();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            return false;
        }
        FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

        client.getSession().close();
        return true;
    }

    /**
     * 封号处理。此函数主要操作的是ipbans表和accounts表。
     * @param id
     * @param reason
     * @param accountId
     * @param gmlevel
     * @param hellban
     * @return
     */
    public static boolean ban(String id, String reason, boolean accountId, int gmlevel, boolean hellban) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps;
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.execute();
                ps.close();
                return true;
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int z = rs.getInt(1);
                    try (PreparedStatement psb = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ? AND gm < ?")) {
                        psb.setString(1, reason);
                        psb.setInt(2, z);
                        psb.setInt(3, gmlevel);
                        psb.execute();
                    }

                    if (gmlevel > 100) { //admin ban
                        try (PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
                            psa.setInt(1, z);
                            try (ResultSet rsa = psa.executeQuery()) {
                                if (rsa.next()) {
                                    String sessionIP = rsa.getString("sessionIP");
                                    if (sessionIP != null && sessionIP.matches("/[0-9]{1,3}\\..*")) {
                                        try (PreparedStatement psz = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)")) {
                                            psz.setString(1, sessionIP);
                                            psz.execute();
                                        }
                                    }
                                    if (rsa.getString("macs") != null) {
                                        String[] macData = rsa.getString("macs").split(", ");
                                        if (macData.length > 0) {
                                            MapleClient.banMacs(macData);
                                        }
                                    }
                                    if (hellban) {
                                        try (PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE email = ?" + (sessionIP == null ? "" : " OR SessionIP = ?"))) {
                                            pss.setString(1, reason);
                                            pss.setString(2, rsa.getString("email"));
                                            if (sessionIP != null) {
                                                pss.setString(3, sessionIP);
                                            }
                                            pss.execute();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ret = true;
                }
            }
            ps.close();
            return ret;
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        return false;
    }

    /**
     * Oid of players is always = the cid
     *
     * @return
     */
    @Override
    public int getObjectId() {
        return getId();
    }

    /**
     * Throws unsupported operation exception, oid of players is read only
     *
     * @param id
     */
    @Override
    public void setObjectId(int id) {
        throw new UnsupportedOperationException();
    }

    public MapleStorage getStorage() {
        return storage;
    }

    public void addVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.add(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public void removeVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.remove(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public boolean isMapObjectVisible(MapleMapObject mo) {
        visibleMapObjectsLock.readLock().lock();
        try {
            return !clone && visibleMapObjects.contains(mo);
        } finally {
            visibleMapObjectsLock.readLock().unlock();
        }
    }

    public Collection<MapleMapObject> getAndWriteLockVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().lock();
        return visibleMapObjects;
    }

    public void unlockWriteVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().unlock();
    }

    public boolean isAlive() {
        return stats.getHp() > 0;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(CField.removePlayerFromMap(this.getObjectId()));
        for (final WeakReference<MapleCharacter> chr : clones) {
            if (chr.get() != null) {
                chr.get().sendDestroyData(client);
            }
        }
        //don't need this, client takes care of it
        /*
         * if (dragon != null) {
         * client.getSession().write(CField.removeDragon(this.getId())); } if
         * (android != null) {
         * client.getSession().write(CField.deactivateAndroid(this.getId())); }
         * if (summonedFamiliar != null) {
         * client.getSession().write(CField.removeFamiliar(this.getId())); }
         */
        XenonSupplyTask.cancel(true);
        XenonSupplyTask = null;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (client.getPlayer().allowedToTarget(this)) {
            //if (client.getPlayer() != this)
            client.getSession().write(CField.spawnPlayerMapobject(this));
            map.broadcastMessage(this, CField.updateCharLook(this, false), false);

            for (final MaplePet pet : pets) {
                if (pet.getSummoned()) {
                    if (this.getId() != client.getPlayer().getId()) {
                        //getMap().broadcastMessage(this, PetPacket.showPet(this, pet, false, false), false);
                        client.getSession().write(PetPacket.showPet(this, pet, false, false));
                        //client.getSession().write(PetPacket.petStatUpdate(this));
                        //client.getSession().write(PetPacket.loadExceptionList(this, pet));
                    }
                }
            }
            for (final WeakReference<MapleCharacter> chr : clones) {
                if (chr.get() != null) {
                    chr.get().sendSpawnData(client);
                }
            }
            if (dragon != null) {
                client.getSession().write(CField.spawnDragon(dragon));
            }
            if (haku != null) {
                client.getSession().write(CField.spawnHaku(haku));
            }
            if (android != null) {
                client.getSession().write(CField.spawnAndroid(this, android));
            }
            if (summonedFamiliar != null) {
                client.getSession().write(CField.spawnFamiliar(summonedFamiliar, true, true));
            }
            if (summons != null && summons.size() > 0) {
                summonsLock.readLock().lock();
                try {
                    for (final MapleSummon summon : summons) {
                        client.getSession().write(SummonPacket.spawnSummon(summon, false));
                    }
                } finally {
                    summonsLock.readLock().unlock();
                }
            }
            if (followid > 0 && followon) {
                client.getSession().write(CField.followEffect(followinitiator ? followid : id, followinitiator ? id : followid, null));
            }
            if (party != null) {
                this.updatePartyMemberHP();
            }
        }
    }

    public final void equipChanged() {
        if (map == null) {
            return;
        }
        map.broadcastMessage(this, CField.updateCharLook(this, false), false);
        stats.recalcLocalStats(this);
        if (getMessenger() != null) {
            World.Messenger.updateMessenger(getMessenger().getId(), getName(), client.getChannel());
        }
    }

    /*public void checkCopyItems() {
     List<Integer> equipOnlyIds = new ArrayList<>();
     Map checkItems = new HashMap();

     for (Item item : getInventory(MapleInventoryType.EQUIP).list()) {
     int equipOnlyId = item.getEquipOnlyId();
     if (equipOnlyId > 0) {
     if (checkItems.containsKey(equipOnlyId)) {
     if (((Integer) checkItems.get(equipOnlyId)) == item.getItemId()) {
     equipOnlyIds.add(equipOnlyId);
     }
     } else {
     checkItems.put(equipOnlyId, item.getItemId());
     }
     }
     }

     for (Item item : getInventory(MapleInventoryType.EQUIPPED).list()) {
     int equipOnlyId = item.getEquipOnlyId();
     if (equipOnlyId > 0) {
     if (checkItems.containsKey(equipOnlyId)) {
     if (((Integer) checkItems.get(equipOnlyId)) == item.getItemId()) {
     equipOnlyIds.add(equipOnlyId);
     }
     } else {
     checkItems.put(equipOnlyId, item.getItemId());
     }
     }
     }

     boolean autoban = false;
     for (Integer equipOnlyId : equipOnlyIds) {
     MapleInventoryManipulator.removeAllByEquipOnlyId(this.client, equipOnlyId);
     autoban = true;
     }
     if (autoban) {
     AutobanManager.getInstance().autoban(this.client, "无理由.");
     }
     checkItems.clear();
     equipOnlyIds.clear();
     }*/
    public final MaplePet getPet(final int index) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (count == index) {
                    return pet;
                }
                count++;
            }
        }
        return null;
    }

    public final void shiftPetsRight() {
        List<MaplePet> petsz = getSummonedPets();
        if (petsz.size() >= 3 || petsz.size() < 1) {
            return;
        } else {
            boolean[] indexBool = new boolean[]{false, false, false};
            for (int i = 0; i < 3; i++) {
                for (MaplePet p : petsz) {
                    if (p.getSummonedValue() == i + 1) {
                        indexBool[i] = true;
                    }
                }
            }
            if (petsz.size() > 1) {
                if (!indexBool[2]) {
                    petsz.get(0).setSummoned(2);
                    petsz.get(1).setSummoned(3);
                } else if (!indexBool[1]) {
                    petsz.get(0).setSummoned(2);
                }
            } else if (indexBool[0]) {
                petsz.get(0).setSummoned(2);
            }
        }
    }

    public void removePetCS(MaplePet pet) {
        pets.remove(pet);
    }

    public void addPet(final MaplePet pet) {
        if (pets.contains(pet)) {
            pets.remove(pet);
        }
        pets.add(pet);
        // So that the pet will be at the last
        // Pet index logic :(
    }

    public void removePet(MaplePet pet, boolean shiftLeft) {
        pet.setSummoned(0);
        /*
         * int slot = -1; for (int i = 0; i < 3; i++) { if (pets[i] != null) {
         * if (pets[i].getUniqueId() == pet.getUniqueId()) { pets[i] = null;
         * slot = i; break; } } } if (shiftLeft) { if (slot > -1) { for (int i =
         * slot; i < 3; i++) { if (i != 2) { pets[i] = pets[i + 1]; } else {
         * pets[i] = null; } } } }
         */
    }

    public final byte getPetIndex(final MaplePet petz) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getUniqueId() == petz.getUniqueId()) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final byte getPetIndex(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getUniqueId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final List<MaplePet> getSummonedPets() {
        List<MaplePet> ret = new ArrayList<>();
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                ret.add(pet);
            }
        }
        return ret;
    }

    public final byte getPetById(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getPetItemId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final List<MaplePet> getPets() {
        return pets;
    }

    public final void unequipAllPets() {
        for (final MaplePet pet : pets) {
            if (pet != null) {
                unequipPet(pet, true, false);
            }
        }
    }

    public void unequipPet(MaplePet pet, boolean shiftLeft, boolean hunger) {
        if (pet.getSummoned()) {
            pet.saveToDb();
            this.client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH).getItem((short) (byte) pet.getInventoryPosition()), false));
            if (map != null) {
                map.broadcastMessage(this, PetPacket.showPet(this, pet, true, hunger), true);
            }
            removePet(pet, shiftLeft);
            //List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
            //stats.put(MapleStat.PET, Integer.valueOf(0)));
            //showpetupdate isn't done here...
            client.getSession().write(CWvsContext.enableActions());
        }
    }

    /*
     * public void shiftPetsRight() { if (pets[2] == null) { pets[2] = pets[1];
     * pets[1] = pets[0]; pets[0] = null; } }
     */
    public final long getLastFameTime() {
        return lastfametime;
    }

    public final List<Integer> getFamedCharacters() {
        return lastmonthfameids;
    }

    public final List<Integer> getBattledCharacters() {
        return lastmonthbattleids;
    }

    public FameStatus canGiveFame(MapleCharacter from) {
        if (lastfametime >= System.currentTimeMillis() - 60 * 60 * 24 * 1000) {
            return FameStatus.NOT_TODAY;
        } else if (from == null || lastmonthfameids == null || lastmonthfameids.contains(from.getId())) {
            return FameStatus.NOT_THIS_MONTH;
        }
        return FameStatus.OK;
    }

    public void hasGivenFame(MapleCharacter to) {
        lastfametime = System.currentTimeMillis();
        lastmonthfameids.add(to.getId());
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO famelog (characterid, characterid_to) VALUES (?, ?)")) {
                ps.setInt(1, getId());
                ps.setInt(2, to.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("ERROR writing famelog for char " + getName() + " to " + to.getName() + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public boolean canBattle(MapleCharacter to) {
        return to != null && lastmonthbattleids != null && !lastmonthbattleids.contains(to.getAccountID());
    }

    public void hasBattled(MapleCharacter to) {
        lastmonthbattleids.add(to.getAccountID());
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO battlelog (accid, accid_to) VALUES (?, ?)")) {
                ps.setInt(1, getAccountID());
                ps.setInt(2, to.getAccountID());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("ERROR writing battlelog for char " + getName() + " to " + to.getName() + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public final MapleKeyLayout getKeyLayout() {
        return this.keylayout;
    }

    public MapleParty getParty() {
        if (party == null) {
            return null;
        } else if (party.isDisbanded()) {
            party = null;
        }
        return party;
    }

    public byte getWorld() {
        return world;
    }

    public void setWorld(byte world) {
        this.world = world;
    }

    public void setParty(MapleParty party) {
        this.party = party;
    }

    public MapleTrade getTrade() {
        return trade;
    }

    public void setTrade(MapleTrade trade) {
        this.trade = trade;
    }

    public EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void setEventInstanceAzwan(EventManager eventInstance) {
        this.eventInstanceAzwan = eventInstance;
    }

    public void addDoor(MapleDoor door) {
        doors.add(door);
    }

    public void clearDoors() {
        doors.clear();
    }

    public List<MapleDoor> getDoors() {
        return new ArrayList<>(doors);
    }

    public void addMechDoor(MechDoor door) {
        mechDoors.add(door);
    }

    public void clearMechDoors() {
        mechDoors.clear();
    }

    public List<MechDoor> getMechDoors() {
        return new ArrayList<>(mechDoors);
    }

    public void setSmega() {
        if (smega) {
            smega = false;
            dropMessage(5, "你把扩音器禁用模式");
        } else {
            smega = true;
            dropMessage(5, "你把扩音器启用模式");
        }
    }

    public boolean getSmega() {
        return smega;
    }

    public List<MapleSummon> getSummonsReadLock() {
        summonsLock.readLock().lock();
        return summons;
    }

    public int getSummonsSize() {
        return summons.size();
    }

    public void unlockSummonsReadLock() {
        summonsLock.readLock().unlock();
    }

    public void addSummon(MapleSummon s) {
        summonsLock.writeLock().lock();
        try {
            summons.add(s);
        } finally {
            summonsLock.writeLock().unlock();
        }
    }

    public void removeSummon(MapleSummon s) {
        summonsLock.writeLock().lock();
        try {
            summons.remove(s);
        } finally {
            summonsLock.writeLock().unlock();
        }
    }

    public int getChair() {
        return chair;
    }

    public int getItemEffect() {
        return itemEffect;
    }

    public void setChair(int chair) {
        this.chair = chair;
        stats.relocHeal(this);
    }

    public void setItemEffect(int itemEffect) {
        this.itemEffect = itemEffect;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.PLAYER;
    }

    public int getFamilyId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getFamilyId();
    }

    public int getSeniorId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getSeniorId();
    }

    public int getJunior1() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior1();
    }

    public int getJunior2() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior2();
    }

    public int getCurrentRep() {
        return currentrep;
    }

    public int getTotalRep() {
        return totalrep;
    }

    public void setCurrentRep(int _rank) {
        currentrep = _rank;
        if (mfc != null) {
            mfc.setCurrentRep(_rank);
        }
    }

    public void setTotalRep(int _rank) {
        totalrep = _rank;
        if (mfc != null) {
            mfc.setTotalRep(_rank);
        }
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void increaseTotalWins() {
        totalWins++;
    }

    public void increaseTotalLosses() {
        totalLosses++;
    }

    public int getGuildId() {
        return guildid;
    }

    public byte getGuildRank() {
        return guildrank;
    }

    public int getGuildContribution() {
        return guildContribution;
    }

    public void setGuildId(int _id) {
        guildid = _id;
        if (guildid > 0) {
            if (mgc == null) {
                mgc = new MapleGuildCharacter(this);
            } else {
                mgc.setGuildId(guildid);
            }
        } else {
            mgc = null;
            guildContribution = 0;
        }
    }

    public void setGuildRank(byte _rank) {
        guildrank = _rank;
        if (mgc != null) {
            mgc.setGuildRank(_rank);
        }
    }

    public void setGuildContribution(int _c) {
        this.guildContribution = _c;
        if (mgc != null) {
            mgc.setGuildContribution(_c);
        }
    }

    public MapleGuildCharacter getMGC() {
        return mgc;
    }

    public void setAllianceRank(byte rank) {
        allianceRank = rank;
        if (mgc != null) {
            mgc.setAllianceRank(rank);
        }
    }

    public byte getAllianceRank() {
        return allianceRank;
    }

    public MapleGuild getGuild() {
        if (getGuildId() <= 0) {
            return null;
        }
        return World.Guild.getGuild(getGuildId());
    }

    public void setJob(int j) {
        this.job = (short) j;
    }

    public void guildUpdate() {
        if (guildid <= 0) {
            return;
        }
        mgc.setLevel((short) level);
        mgc.setJobId(job);
        World.Guild.memberLevelJobUpdate(mgc);
    }

    public void saveGuildStatus() {
        MapleGuild.setOfflineGuildStatus(guildid, guildrank, guildContribution, allianceRank, id);
    }

    public void familyUpdate() {
        if (mfc == null) {
            return;
        }
        World.Family.memberFamilyUpdate(mfc, this);
    }

    public void saveFamilyStatus() {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE characters SET familyid = ?, seniorid = ?, junior1 = ?, junior2 = ? WHERE id = ?")) {
                if (mfc == null) {
                    ps.setInt(1, 0);
                    ps.setInt(2, 0);
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                } else {
                    ps.setInt(1, mfc.getFamilyId());
                    ps.setInt(2, mfc.getSeniorId());
                    ps.setInt(3, mfc.getJunior1());
                    ps.setInt(4, mfc.getJunior2());
                }
                ps.setInt(5, id);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            FileoutputUtil.outputFileError("logs/数据库异常.txt", se);
        }
        //MapleFamily.setOfflineFamilyStatus(familyid, seniorid, junior1, junior2, currentrep, totalrep, id);
    }

    public void modifyCSPoints(int type, int quantity) {
        modifyCSPoints(type, quantity, false);
    }

    public void modifyCSPoints(int type, int quantity, boolean show) {
        switch (type) {
            case 1:
                if (nxcredit + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "你已经获得了最大的点卷数。没有点卷将被授予");
                    }
                    return;
                }
                nxcredit += quantity;
                break;

            case 2:
                if (maplepoints + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "你已经获得了最大的抵用卷数。没有抵用卷将被授予.");
                    }
                    return;
                }
                maplepoints += quantity;
                break;
            case 4:
                if (acash + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "你已经获得了最大的点卷数。没有点卷将被授予.");
                    }
                    return;
                }
                acash += quantity;
                break;
            default:
                break;
        }
        if (show && quantity != 0) {
            dropMessage("您" + (quantity > 0 ? "获得了 " : "花费了 ") + quantity + (type == 1 ? " 点卷." : " 抵用券."));
        }
    }

    public int getCSPoints(int type) {
        switch (type) {
            case 1:
                return nxcredit;
            case 4:// 目前没使用到
                return acash;
            case 2:
                return maplepoints;
            default:
                return 0;
        }
    }

    public final boolean hasEquipped(int itemid) {
        return inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid) >= 1;
    }

    public final boolean haveItem(int itemid, int quantity, boolean checkEquipped, boolean greaterOrEquals) {
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        int possesed = inventory[type.ordinal()].countById(itemid);
        if (checkEquipped && type == MapleInventoryType.EQUIP) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        if (greaterOrEquals) {
            return possesed >= quantity;
        } else {
            return possesed == quantity;
        }
    }

    public final boolean haveItem(int itemid, int quantity) {
        return haveItem(itemid, quantity, true, true);
    }

    public final boolean haveItem(int itemid) {
        return haveItem(itemid, 1, true, true);
    }

    public static boolean tempban(String reason, Calendar duration, int greason, int accountid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?")) {
                Timestamp TS = new Timestamp(duration.getTimeInMillis());
                ps.setTimestamp(1, TS);
                ps.setString(2, reason);
                ps.setInt(3, greason);
                ps.setInt(4, accountid);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("Error while tempbanning" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        return false;
    }

    public void setGM(byte level) {
        this.gmLevel = level;
    }

    public List<Item> getRebuy() {
        return this.rebuy;
    }

    public void setRebuy(List<Item> rebuy) {
        this.rebuy = rebuy;
    }

    public static enum FameStatus {

        OK,
        NOT_TODAY,
        NOT_THIS_MONTH
    }

    public byte getBuddyCapacity() {
        return buddylist.getCapacity();
    }

    public void setBuddyCapacity(byte capacity) {
        buddylist.setCapacity(capacity);
        client.getSession().write(BuddylistPacket.updateBuddyCapacity(capacity));
    }

    public MapleMessenger getMessenger() {
        return messenger;
    }

    public void setMessenger(MapleMessenger messenger) {
        this.messenger = messenger;
    }

    public void addCooldown(int skillId, long startTime, long length) {
        coolDowns.put(skillId, new MapleCoolDownValueHolder(skillId, startTime, length));
    }

    public void removeCooldown(int skillId) {
        if (coolDowns.containsKey(skillId)) {
            coolDowns.remove(skillId);
        }
    }

    public boolean skillisCooling(int skillId) {
        return coolDowns.containsKey(skillId);
    }

    public void giveCoolDowns(final int skillid, long starttime, long length) {
        addCooldown(skillid, starttime, length);
    }

    public void giveCoolDowns(final List<MapleCoolDownValueHolder> cooldowns) {
        giveCoolDowns(cooldowns, false);
    }

    public void giveCoolDowns(final List<MapleCoolDownValueHolder> cooldowns, boolean sendPacket) {
        int time;
        if (cooldowns != null) {
            for (MapleCoolDownValueHolder cooldown : cooldowns) {
                coolDowns.put(cooldown.skillId, cooldown);
            }
        } else {
            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
                ResultSet rs;
                try (PreparedStatement ps = con.prepareStatement("SELECT SkillID,StartTime,length FROM skills_cooldowns WHERE charid = ?")) {
                    ps.setInt(1, getId());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getLong("length") + rs.getLong("StartTime") - System.currentTimeMillis() <= 0) {
                            continue;
                        }
                        giveCoolDowns(rs.getInt("SkillID"), rs.getLong("StartTime"), rs.getLong("length"));
                    }
                }
                rs.close();
                deleteWhereCharacterId(con, "DELETE FROM skills_cooldowns WHERE charid = ?");

            } catch (SQLException e) {
                System.err.println("Error while retriving cooldown from SQL storage" + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            }
        }
        if (sendPacket && getCooldowns() != null) {
            for (MapleCoolDownValueHolder cooldown : getCooldowns()) {
                int lefttime = (int) ((cooldown.length - (System.currentTimeMillis() - cooldown.startTime)) / 1000);
                if (lefttime > 0) {
                    if (getDebugMessage()) {
                        dropMessage(6, "技能: " + SkillFactory.getSkillName(cooldown.skillId) + " CD:" + lefttime);
                    }
                    getClient().getSession().write(CField.skillCooldown(cooldown.skillId, lefttime));
                }
            }
        }
    }

    public int getCooldownSize() {
        return coolDowns.size();
    }

    public int getDiseaseSize() {
        return diseases.size();
    }

    public List<MapleCoolDownValueHolder> getCooldowns() {
        List<MapleCoolDownValueHolder> ret = new ArrayList<>();
        for (MapleCoolDownValueHolder mc : coolDowns.values()) {
            if (mc != null) {
                ret.add(mc);
            }
        }
        return ret;
    }

    public final List<MapleDiseaseValueHolder> getAllDiseases() {
        return new ArrayList<>(diseases.values());
    }

    public final boolean hasDisease(final MapleDisease dis) {
        return diseases.containsKey(dis);
    }

    public void giveDebuff(MapleDisease disease, MobSkill skill) {
        giveDebuff(disease, skill.getX(), skill.getDuration(), skill.getSkillId(), skill.getSkillLevel());
    }

    public void giveDebuff(MapleDisease disease, int x, long duration, int skillid, int level) {
        if ((this.map != null) && (!hasDisease(disease))) {
            if ((disease != MapleDisease.SEDUCE) && (disease != MapleDisease.STUN) && (disease != MapleDisease.FLAG)
                    && (getBuffedValue(MapleBuffStat.HOLY_SHIELD) != null)) {
                return;
            }

            int mC = getBuffSource(MapleBuffStat.MECH_CHANGE);
            if ((mC > 0) && (mC != 35121005)) {
                return;
            }
            if ((this.stats.ASR > 0) && (Randomizer.nextInt(100) < this.stats.ASR)) {
                return;
            }

            this.diseases.put(disease, new MapleDiseaseValueHolder(disease, System.currentTimeMillis(), duration - this.stats.decreaseDebuff));
            this.client.getSession().write(CWvsContext.BuffPacket.giveDebuff(disease, x, skillid, level, (int) duration));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignDebuff(this.id, disease, skillid, level, x), false);

            if ((x > 0) && (disease == MapleDisease.POISON)) {
                addHP((int) (-(x * ((duration - this.stats.decreaseDebuff) / 1000L))));
            }
        }
    }

    public final void giveSilentDebuff(final List<MapleDiseaseValueHolder> ld) {
        if (ld != null) {
            for (final MapleDiseaseValueHolder disease : ld) {
                diseases.put(disease.disease, disease);
            }
        }
    }

    public void dispelDebuff(MapleDisease debuff) {
        if (hasDisease(debuff)) {
            client.getSession().write(BuffPacket.cancelDebuff(debuff));
            map.broadcastMessage(this, BuffPacket.cancelForeignDebuff(id, debuff), false);

            diseases.remove(debuff);
        }
    }

    public void dispelDebuffs() {
        List<MapleDisease> diseasess = new ArrayList<>(diseases.keySet());
        for (MapleDisease d : diseasess) {
            dispelDebuff(d);
        }
    }

    public void cancelAllDebuffs() {
        diseases.clear();
    }

    public void setLevel(final short level) {
        this.level = level;
    }

    public void sendNote(String to, String msg) {
        sendNote(to, msg, 0);
    }

    public void sendNote(String to, String msg, int fame) {
        MapleCharacterUtil.sendNote(to, getName(), msg, fame);
    }

    public void sendMapleGMNote(String to, String msg, int fame) {
        MapleCharacterUtil.sendNote(to, "MapleGM", msg, fame);
    }

    public void showNote() {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM notes WHERE `to`=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.last();
                    int count = rs.getRow();
                    rs.first();
                    client.getSession().write(CSPacket.showNotes(rs, count));
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to show note" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public void deleteNote(int id, int fame) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT gift FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("gift") == fame && fame > 0) { //not exploited! hurray
                    addFame(fame);
                    updateSingleStat(MapleStat.FAME, getFame());
                    client.getSession().write(CWvsContext.InfoPacket.getShowFameGain(fame));
                }
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("DELETE FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Unable to delete note" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public int getMulungEnergy() {
        return mulung_energy;
    }

    public void mulung_EnergyModify(boolean inc) {
        if (inc) {
            if (mulung_energy + 100 > 10000) {// 改版後武陵道场
                mulung_energy = 10000;
            } else {
                mulung_energy += 100;
            }

//            if (mulung_energy + 3 > 300) { // 第一版的武陵道场
//                mulung_energy = 300;
//            } else {
//                mulung_energy += 3;
//            }
        } else {
            mulung_energy = 0;
        }
        client.getSession().write(CWvsContext.MulungEnergy(mulung_energy));
    }

    public void writeMulungEnergy() {
        client.getSession().write(CWvsContext.MulungEnergy(mulung_energy));
    }

    public void writeEnergy(String type, String inc) {
        client.getSession().write(CWvsContext.sendPyramidEnergy(type, inc));
    }

    public void writeStatus(String type, String inc) {
        client.getSession().write(CWvsContext.sendGhostStatus(type, inc));
    }

    public void writePoint(String type, String inc) {
        client.getSession().write(CWvsContext.sendGhostPoint(type, inc));
    }

    public final short getCombo() {
        return combo;
    }

    public void setCombo(final short combo) {
        this.combo = combo;
    }

    public final long getLastCombo() {
        return lastCombo;
    }

    public void setLastCombo(final long combo) {
        this.lastCombo = combo;
    }

    public final long getKeyDownSkill_Time() {
        return keydown_skill;
    }

    public void setKeyDownSkill_Time(final long keydown_skill) {
        this.keydown_skill = keydown_skill;
    }

    public void checkBerserk() { //berserk is special in that it doesn't use worldtimer :)
        if (job != 132 || lastBerserkTime < 0 || lastBerserkTime + 10000 > System.currentTimeMillis()) {
            return;
        }
        final Skill BerserkX = SkillFactory.getSkill(1320006);
        final int skilllevel = getTotalSkillLevel(BerserkX);
        if (skilllevel >= 1 && map != null) {
            lastBerserkTime = System.currentTimeMillis();
            final MapleStatEffect ampStat = BerserkX.getEffect(skilllevel);
            stats.Berserk = stats.getHp() * 100 / stats.getCurrentMaxHp() <= ampStat.getX();
            client.getSession().write(EffectPacket.showOwnBuffEffect(1320006, 1, getLevel(), skilllevel, (byte) (stats.Berserk ? 1 : 0)));
            map.broadcastMessage(this, EffectPacket.showBuffeffect(getId(), 1320006, 1, getLevel(), skilllevel, (byte) (stats.Berserk ? 1 : 0)), false);
        } else {
            lastBerserkTime = -1; // somebody thre? O_O
        }
    }

    public void setChalkboard(String text) {
        this.chalktext = text;
        if (map != null) {
            map.broadcastMessage(CSPacket.useChalkboard(getId(), text));
        }
    }

    public String getChalkboard() {
        return chalktext;
    }

    public MapleMount getMount() {
        return mount;
    }

    public int[] getWishlist() {
        return wishlist;
    }

    public void clearWishlist() {
        for (int i = 0; i < 10; i++) {
            wishlist[i] = 0;
        }
        changed_wishlist = true;
    }

    public int getWishlistSize() {
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            if (wishlist[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public void setWishlist(int[] wl) {
        this.wishlist = wl;
        changed_wishlist = true;
    }

    public int[] getRocks() {
        return rocks;
    }

    public int getRockSize() {
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            if (rocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRocks(int map) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == map) {
                rocks[i] = 999999999;
                changed_trocklocations = true;
                break;
            }
        }
    }

    public void addRockMap() {
        if (getRockSize() >= 10) {
            return;
        }
        rocks[getRockSize()] = getMapId();
        changed_trocklocations = true;
    }

    public boolean isRockMap(int id) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getRegRocks() {
        return regrocks;
    }

    public int getRegRockSize() {
        int ret = 0;
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRegRocks(int map) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == map) {
                regrocks[i] = 999999999;
                changed_regrocklocations = true;
                break;
            }
        }
    }

    public void addRegRockMap() {
        if (getRegRockSize() >= 5) {
            return;
        }
        regrocks[getRegRockSize()] = getMapId();
        changed_regrocklocations = true;
    }

    public boolean isRegRockMap(int id) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getHyperRocks() {
        return hyperrocks;
    }

    public int getHyperRockSize() {
        int ret = 0;
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromHyperRocks(int map) {
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] == map) {
                hyperrocks[i] = 999999999;
                changed_hyperrocklocations = true;
                break;
            }
        }
    }

    public void addHyperRockMap() {
        if (getRegRockSize() >= 13) {
            return;
        }
        hyperrocks[getHyperRockSize()] = getMapId();
        changed_hyperrocklocations = true;
    }

    public boolean isHyperRockMap(int id) {
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public List<LifeMovementFragment> getLastRes() {
        return lastres;
    }

    public void setLastRes(List<LifeMovementFragment> lastres) {
        this.lastres = lastres;
    }

    public void dropMessage(String message) {
        dropMessage(6, message);
    }

    public void dropMessage(int type, String message) {
        switch (type) {
            case -1:
                client.getSession().write(CWvsContext.getTopMsg(message));
                break;
            case -2:
                client.getSession().write(PlayerShopPacket.shopChat(message, 0)); //0 or what
                break;
            case -3:
                client.getSession().write(CField.getChatText(getId(), message, isSuperGM(), 0)); //1 = hide
                break;
            case -4:
                client.getSession().write(CField.getChatText(getId(), message, isSuperGM(), 1)); //1 = hide
                break;
            case -5:
                client.getSession().write(CField.getGameMessage(message, (short) 6)); //pink
                break;
            case -6:
                client.getSession().write(CField.getGameMessage(message, (short) 11)); //white bg
                break;
            case -7:
                client.getSession().write(CWvsContext.getMidMsg(message, false, 0));
                break;
            case -8:
                client.getSession().write(CWvsContext.getMidMsg(message, true, 0));
            case -9:
                client.getSession().write(InfoPacket.showInfo(message));
            case -10:
                client.getSession().write(CField.getFollowMessage(message));
            case -11:
                this.client.getSession().write(CWvsContext.yellowChat(message));
            default:
                client.getSession().write(CWvsContext.broadcastMsg(type, message));
        }
    }

    public void showMessage(int type, String msg) {
        client.getSession().write(CField.getGameMessage(msg, (short) type));
    }

    public void showInfo(String caption, boolean pink, String msg) {
        short type = (short) (pink ? 6 : 7);
        if (caption == null || caption.isEmpty()) {
            client.getSession().write(CField.getGameMessage(msg, type));
        } else {
            client.getSession().write(CField.getGameMessage("[" + caption + "] " + msg, type));
        }
    }

    public IMaplePlayerShop getPlayerShop() {
        return playerShop;
    }

    public void setPlayerShop(IMaplePlayerShop playerShop) {
        this.playerShop = playerShop;
    }

    public int getConversation() {
        return inst.get();
    }

    public void setConversation(int inst) {
        this.inst.set(inst);
    }

    public int getDirection() {
        return insd.get();
    }

    public void setDirection(int inst) {
        this.insd.set(inst);
    }

    public MapleCarnivalParty getCarnivalParty() {
        return carnivalParty;
    }

    public void setCarnivalParty(MapleCarnivalParty party) {
        carnivalParty = party;
    }

    public void addCP(int ammount) {
        totalCP += ammount;
        availableCP += ammount;
    }

    public void useCP(int ammount) {
        availableCP -= ammount;
    }

    public int getAvailableCP() {
        return availableCP;
    }

    public int getTotalCP() {
        return totalCP;
    }

    public void resetCP() {
        totalCP = 0;
        availableCP = 0;
    }

    public void addCarnivalRequest(MapleCarnivalChallenge request) {
        pendingCarnivalRequests.add(request);
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return pendingCarnivalRequests.pollLast();
    }

    public void clearCarnivalRequests() {
        pendingCarnivalRequests = new LinkedList<>();
    }

    public void startMonsterCarnival(final int enemyavailable, final int enemytotal) {
        client.getSession().write(MonsterCarnivalPacket.startMonsterCarnival(this, enemyavailable, enemytotal));
    }

    public void CPUpdate(final boolean party, final int available, final int total, final int team) {
        client.getSession().write(MonsterCarnivalPacket.CPUpdate(party, available, total, team));
    }

    public void playerDiedCPQ(final String name, final int lostCP, final int team) {
        client.getSession().write(MonsterCarnivalPacket.playerDiedMessage(name, lostCP, team));
    }

    public void setAchievementFinished(int id) {
        if (!finishedAchievements.contains(id)) {
            finishedAchievements.add(id);
            changed_achievements = true;
        }
    }

    public boolean achievementFinished(int achievementid) {
        return finishedAchievements.contains(achievementid);
    }

    public void finishAchievement(int id) {
        if (!achievementFinished(id)) {
            if (isAlive() && !isClone()) {
                MapleAchievements.getInstance().getById(id).finishAchievement(this);
            }
        }
    }

    public List<Integer> getFinishedAchievements() {
        return finishedAchievements;
    }

    public void setMoonlightAchievementFinished(int id) {
        if (!finishedMoonlightAchievements.contains(id)) {
            finishedMoonlightAchievements.add(id);
            changed_moonlight_achievements = true;
        }
    }

    public void setMoonlightAchievementUnfinished(int id) {
        if (finishedMoonlightAchievements.contains(id)) {
            finishedMoonlightAchievements.remove(id);
            changed_moonlight_achievements = true;
        }
    }

    public boolean moonlightAchievementFinished(int achievementid) {
        return finishedMoonlightAchievements.contains(achievementid);
    }

    public void finishMoonlightAchievement(int id) {
        if (!moonlightAchievementFinished(id)) {
            if (isAlive() && !isClone()) {
                MoonlightAchievements.getInstance().getById(id).finishMoonlightAchievement(this);
            }
        }
    }

    public void unfinishMoonlightAchievement(int id) {
        if (moonlightAchievementFinished(id)) {
            if (isAlive() && !isClone()) {
                MoonlightAchievements.getInstance().getById(id).unfinishMoonlightAchievement(this);
            }
        }
    }

    public List<Integer> getFinishedMoonlightAchievements() {
        return finishedMoonlightAchievements;
    }

    public boolean getCanTalk() {
        return this.canTalk;
    }

    public void canTalk(boolean talk) {
        this.canTalk = talk;
    }

    public double getEXPMod() {
        return stats.expMod;
    }

    public double getDropMod() {
        return stats.dropMod;
    }

    public int getCashMod() {
        return stats.cashMod;
    }

    public void setPoints(int p) {
        this.points = p;
        if (this.points >= 1) {
            finishAchievement(1);
        }
    }

    public int getPoints() {
        return points;
    }

    public void setVPoints(int p) {
        this.vpoints = p;
    }

    public int getVPoints() {
        return vpoints;
    }

    public void setEPoints(int p) {
        this.epoints = p;
    }

    public int getEPoints() {
        return epoints;
    }

    public void setDPoints(int p) {
        this.dpoints = p;
    }

    public int getDPoints() {
        return dpoints;
    }

    public int getGMlevel() {
        return gmLevel;
    }

    public CashShop getCashInventory() {
        return cs;
    }

    public void removeItem(int id, int quantity) {
        MapleInventoryManipulator.removeById(client, GameConstants.getInventoryType(id), id, -quantity, true, false);
        client.getSession().write(InfoPacket.getShowItemGain(id, (short) quantity, true));
    }

    public void removeAll(int id) {
        removeAll(id, true);
    }

    public void removeAll(int id, boolean show) {
        MapleInventoryType type = GameConstants.getInventoryType(id);
        int possessed = getInventory(type).countById(id);

        if (possessed > 0) {
            MapleInventoryManipulator.removeById(getClient(), type, id, possessed, true, false);
            if (show) {
                getClient().getSession().write(InfoPacket.getShowItemGain(id, (short) -possessed, true));
            }
        }
        /*
         * if (type == MapleInventoryType.EQUIP) { //check equipped type =
         * MapleInventoryType.EQUIPPED; possessed =
         * getInventory(type).countById(id);
         *
         * if (possessed > 0) {
         * MapleInventoryManipulator.removeById(getClient(), type, id,
         * possessed, true, false);
         * getClient().getSession().write(CField.getShowItemGain(id,
         * (short)-possessed, true)); } }
         */
    }

    public Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> getRings(boolean equip) {
        MapleInventory iv = getInventory(MapleInventoryType.EQUIPPED);
        List<Item> equipped = iv.newList();
        Collections.sort(equipped);
        List<MapleRing> crings = new ArrayList<>(), frings = new ArrayList<>(), mrings = new ArrayList<>();
        MapleRing ring;
        for (Item ite : equipped) {
            Equip item = (Equip) ite;
            if (item.getRing() != null) {
                ring = item.getRing();
                ring.setEquipped(true);
                if (GameConstants.isEffectRing(item.getItemId())) {
                    if (equip) {
                        if (GameConstants.isCrushRing(item.getItemId())) {
                            crings.add(ring);
                        } else if (GameConstants.isFriendshipRing(item.getItemId())) {
                            frings.add(ring);
                        } else if (GameConstants.isMarriageRing(item.getItemId())) {
                            mrings.add(ring);
                        }
                    } else if (crings.isEmpty() && GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    } else if (frings.isEmpty() && GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } else if (mrings.isEmpty() && GameConstants.isMarriageRing(item.getItemId())) {
                        mrings.add(ring);
                    } //for 3rd person the actual slot doesnt matter, so we'll use this to have both shirt/ring same?
                    //however there seems to be something else behind this, will have to sniff someone with shirt and ring, or more conveniently 3-4 of those
                }
            }
        }
        if (equip) {
            iv = getInventory(MapleInventoryType.EQUIP);
            for (Item ite : iv.list()) {
                Equip item = (Equip) ite;
                if (item.getRing() != null && GameConstants.isCrushRing(item.getItemId())) {
                    ring = item.getRing();
                    ring.setEquipped(false);
                    if (GameConstants.isFriendshipRing(item.getItemId())) {
                        frings.add(ring);
                    } else if (GameConstants.isCrushRing(item.getItemId())) {
                        crings.add(ring);
                    } else if (GameConstants.isMarriageRing(item.getItemId())) {
                        mrings.add(ring);
                    }
                }
            }
        }
        Collections.sort(frings, new MapleRing.RingComparator());
        Collections.sort(crings, new MapleRing.RingComparator());
        Collections.sort(mrings, new MapleRing.RingComparator());
        return new Triple<>(crings, frings, mrings);
    }

    public int getFH() {
        MapleFoothold fh = getMap().getFootholds().findBelow(getTruePosition());
        if (fh != null) {
            return fh.getId();
        }
        return 0;
    }

    public void startFairySchedule(boolean exp) {
        startFairySchedule(exp, false);
    }

    public void startFairySchedule(boolean exp, boolean equipped) {
        cancelFairySchedule(exp || stats.equippedFairy == 0);
        if (fairyExp <= 0) {
            fairyExp = (byte) stats.equippedFairy;
        }
        if (equipped && fairyExp < stats.equippedFairy * 3 && stats.equippedFairy > 0) {
            dropMessage(5, "仙女的经验点会增加到 " + (fairyExp + stats.equippedFairy) + "% 一个小时后.");
        }
        lastFairyTime = System.currentTimeMillis();
    }

    public final boolean canFairy(long now) {
        return lastFairyTime > 0 && lastFairyTime + (60 * 60 * 1000) < now;
    }

    public final boolean canHP(long now) {
        if (lastHPTime + 5000 < now) {
            lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMP(long now) {
        if (lastMPTime + 5000 < now) {
            lastMPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canHPRecover(long now) {
        if (stats.hpRecoverTime > 0 && lastHPTime + stats.hpRecoverTime < now) {
            lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMPRecover(long now) {
        if (stats.mpRecoverTime > 0 && lastMPTime + stats.mpRecoverTime < now) {
            lastMPTime = now;
            return true;
        }
        return false;
    }

    public void cancelFairySchedule(boolean exp) {
        lastFairyTime = 0;
        if (exp) {
            this.fairyExp = 0;
        }
    }

    public void doFairy() {
        if (fairyExp < stats.equippedFairy * 3 && stats.equippedFairy > 0) {
            fairyExp += stats.equippedFairy;
            dropMessage(5, "仙女吊坠的经验提高到了 " + fairyExp + "%.");
        }
        if (getGuildId() > 0) {
            World.Guild.gainGP(getGuildId(), 20, id);
            client.getSession().write(InfoPacket.getGPContribution(20));
        }
        traits.get(MapleTraitType.will).addExp(5, this); //willpower every hour
        startFairySchedule(false, true);
    }

    public byte getFairyExp() {
        return fairyExp;
    }

    public int getTeam() {
        return coconutteam;
    }

    public void setTeam(int v) {
        this.coconutteam = v;
    }

    public void spawnPet(byte slot) {
        spawnPet(slot, false, true);
    }

    public void spawnPet(byte slot, boolean lead) {
        spawnPet(slot, lead, true);
    }

    public void spawnPet(byte slot, boolean lead, boolean broadcast) {
        final Item item = getInventory(MapleInventoryType.CASH).getItem(slot);
        if (item == null || item.getItemId() >= 5010000 || item.getItemId() < 5000000) {
            return;
        }
        switch (item.getItemId()) {
            case 5000047:
            case 5000028: {
                final MaplePet pet = MaplePet.createPet(item.getItemId() + 1, MapleInventoryIdentifier.getInstance());
                if (pet != null) {
                    MapleInventoryManipulator.addById(client, item.getItemId() + 1, (short) 1, item.getOwner(), pet, 90, false, "Evolved from pet " + item.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
                    MapleInventoryManipulator.removeFromSlot(client, MapleInventoryType.CASH, slot, (short) 1, false);
                }
                break;
            }
            default: {
                final MaplePet pet = item.getPet();
                if (pet != null && (item.getItemId() != 5000054 || pet.getSecondsLeft() > 0) && (item.getExpiration() == -1 || item.getExpiration() > System.currentTimeMillis())) {
                    if (pet.getSummoned()) { // Already summoned, let's keep it
                        unequipPet(pet, true, false);
                    } else {
                        int leadid = 8;
                        if (GameConstants.isKOC(getJob())) {
                            leadid = 10000018;
                        } else if (GameConstants.isAran(getJob())) {
                            leadid = 20000024;
                        } else if (GameConstants.isEvan(getJob())) {
                            leadid = 20011024;
                        } else if (GameConstants.isMercedes(getJob())) {
                            leadid = 20021024;
                        } else if (GameConstants.isPhantom(getJob())) {
                            leadid = 20031024;
                        } else if (GameConstants.isDemonSlayer(getJob())) {
                            leadid = 30011024;
                        } else if (GameConstants.isResistance(getJob())) {
                            leadid = 30001024;
                        } else if (GameConstants.isMihile(getJob())) {
                            leadid = 50001018;
                            //} else if (GameConstants.isCannon(getJob())) {
                            //    leadid = 10008; //idk, TODO JUMP
                        }
                        if (getSkillLevel(SkillFactory.getSkill(leadid)) == 0 && getPet(0) != null) {
                            unequipPet(getPet(0), false, false);
                        } else if (lead && getSkillLevel(SkillFactory.getSkill(leadid)) > 0) { // Follow the Lead
                            //shiftPetsRight();
                        }
                        final Point pos = getPosition();
                        pet.setPos(pos);
                        try {
                            pet.setFh(getMap().getFootholds().findBelow(pos).getId());
                        } catch (NullPointerException e) {
                            pet.setFh(0); //lol, it can be fixed by movement
                        }
                        pet.setStance(0);
                        pet.setSummoned(1); //let summoned be true..
                        addPet(pet);
                        pet.setSummoned(getPetIndex(pet) + 1); //then get the index
                        if (/*broadcast && */getMap() != null) {
                            getMap().broadcastMessage(this, PetPacket.showPet(this, pet, false, false), true);
                            client.getSession().write(PetPacket.loadExceptionList(this, pet));
                            client.getSession().write(PetPacket.petStatUpdate(this));
                        }
                    }
                }
                break;
            }
        }
        client.getSession().write(CWvsContext.enableActions());
    }

    public void clearLinkMid() {
        linkMobs.clear();
        cancelEffectFromBuffStat(MapleBuffStat.HOMING_BEACON);
        cancelEffectFromBuffStat(MapleBuffStat.MANY_USES);
    }

    public int getFirstLinkMid() {
        for (Integer lm : linkMobs.keySet()) {
            return lm;
        }
        return 0;
    }

    public Map<Integer, Integer> getAllLinkMid() {
        return linkMobs;
    }

    public void setLinkMid(int lm, int x) {
        linkMobs.put(lm, x);
    }

    public int getDamageIncrease(int lm) {
        if (linkMobs.containsKey(lm)) {
            return linkMobs.get(lm);
        }
        return 0;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean c) {
        this.clone = c;
    }

    public WeakReference<MapleCharacter>[] getClones() {
        return clones;
    }

    public MapleCharacter cloneLooks() {
        MapleClient cloneclient = new MapleClient(null, null, new MockIOSession());

        final int minus = (getId() + Randomizer.nextInt(Integer.MAX_VALUE - getId())); // really randomize it, dont want it to fail

        MapleCharacter ret = new MapleCharacter(true);
        ret.id = minus;
        ret.client = cloneclient;
        ret.exp = 0;
        ret.meso = 0;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.anticheat = anticheat;
        ret.name = name;
        ret.level = level;
        ret.fame = fame;
        ret.job = job;
        ret.hair = hair;
        ret.face = face;
        ret.faceMarking = faceMarking;
        ret.ears = ears;
        ret.tail = tail;
        ret.elf = elf;
        ret.skinColor = skinColor;
        ret.monsterbook = monsterbook;
        ret.mount = mount;
        ret.CRand = new PlayerRandomStream();
        ret.gmLevel = gmLevel;
        ret.gender = gender;
        ret.mapid = map.getId();
        ret.map = map;
        ret.setStance(getStance());
        ret.chair = chair;
        ret.itemEffect = itemEffect;
        ret.guildid = guildid;
        ret.currentrep = currentrep;
        ret.totalrep = totalrep;
        ret.stats = stats;
        ret.effects.putAll(effects);
        ret.dispelSummons();
        ret.guildrank = guildrank;
        ret.guildContribution = guildContribution;
        ret.allianceRank = allianceRank;
        ret.setPosition(getTruePosition());
        for (Item equip : getInventory(MapleInventoryType.EQUIPPED).newList()) {
            ret.getInventory(MapleInventoryType.EQUIPPED).addFromDB(equip.copy());
        }
        ret.skillMacros = skillMacros;
        ret.keylayout = keylayout;
        ret.questinfo = questinfo;
        ret.savedLocations = savedLocations;
        ret.wishlist = wishlist;
        ret.buddylist = buddylist;
        ret.keydown_skill = 0;
        ret.lastmonthfameids = lastmonthfameids;
        ret.lastfametime = lastfametime;
        ret.storage = storage;
        ret.cs = this.cs;
        ret.client.setAccountName(client.getAccountName());
        ret.nxcredit = nxcredit;
        ret.acash = acash;
        ret.maplepoints = maplepoints;
        ret.clone = true;
        ret.client.setChannel(this.client.getChannel());
        while (map.getCharacterById(ret.id) != null || client.getChannelServer().getPlayerStorage().getCharacterById(ret.id) != null) {
            ret.id++;
        }
        ret.client.setPlayer(ret);
        return ret;
    }

    public final void cloneLook() {
        if (clone || inPVP()) {
            return;
        }
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() == null) {
                final MapleCharacter newp = cloneLooks();
                map.addPlayer(newp);
                map.broadcastMessage(CField.updateCharLook(newp, false));
                map.movePlayer(newp, getTruePosition());
                clones[i] = new WeakReference<>(newp);
                return;
            }
        }
    }

    public final void disposeClones() {
        numClones = 0;
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                map.removePlayer(clones[i].get());
                if (clones[i].get().getClient() != null) {
                    clones[i].get().getClient().setPlayer(null);
                    clones[i].get().client = null;
                }
                clones[i] = new WeakReference<>(null);
                numClones++;
            }
        }
    }

    public final int getCloneSize() {
        int z = 0;
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                z++;
            }
        }
        return z;
    }

    public void spawnClones() {
        if (!isGM()) { //removed tetris piece likely, expired or whatever
            numClones = (byte) (stats.hasClone ? 1 : 0);
        }
        for (int i = 0; i < numClones; i++) {
            cloneLook();
        }
        numClones = 0;
    }

    public byte getNumClones() {
        return numClones;
    }

    public void setDragon(MapleDragon d) {
        this.dragon = d;
    }

    public void setHaku(MapleHaku h) {
        this.haku = h;
    }

    public MapleExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(MapleExtractor me) {
        removeExtractor();
        this.extractor = me;
    }

    public void removeExtractor() {
        if (extractor != null) {
            map.broadcastMessage(CField.removeExtractor(this.id));
            map.removeMapObject(extractor);
            extractor = null;
        }
    }

    public final void spawnSavedPets() {
        for (int i = 0; i < petStore.length; i++) {
            if (petStore[i] > -1) {
                spawnPet(petStore[i], false, false);
            }
        }
        client.getSession().write(PetPacket.petStatUpdate(this));
        petStore = new byte[]{-1, -1, -1};
    }

    public final byte[] getPetStores() {
        return petStore;
    }

    public void resetStats(final int str, final int dex, final int int_, final int luk) {
        Map<MapleStat, Integer> stat = new EnumMap<>(MapleStat.class);
        int total = stats.getStr() + stats.getDex() + stats.getLuk() + stats.getInt() + getRemainingAp();

        total -= str;
        stats.str = (int) str;

        total -= dex;
        stats.dex = (int) dex;

        total -= int_;
        stats.int_ = (int) int_;

        total -= luk;
        stats.luk = (int) luk;

        setRemainingAp((int) total);
        stats.recalcLocalStats(this);
        stat.put(MapleStat.STR, str);
        stat.put(MapleStat.DEX, dex);
        stat.put(MapleStat.INT, int_);
        stat.put(MapleStat.LUK, luk);
        stat.put(MapleStat.AVAILABLEAP, total);
        client.getSession().write(CWvsContext.updatePlayerStats(stat, false, this));
    }

    public Event_PyramidSubway getPyramidSubway() {
        return pyramidSubway;
    }

    public void setPyramidSubway(Event_PyramidSubway ps) {
        this.pyramidSubway = ps;
    }

    public byte getSubcategory() {
        if (GameConstants.isJett(job)) {
            return 10;
        }
        if (job >= 430 && job <= 434) {
            return 1;
        }
        if (GameConstants.isCannon(job) || job == 1) {
            return 2;
        }
        if (job != 0 && job != 400) {
            return 0;
        }
        return subcategory;
    }

    public void setSubcategory(int z) {
        this.subcategory = (byte) z;
    }

    public int itemQuantity(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).countById(itemid);
    }

    public void setRPS(RockPaperScissors rps) {
        this.rps = rps;
    }

    public RockPaperScissors getRPS() {
        return rps;
    }

    public long getNextConsume() {
        return nextConsume;
    }

    public void setNextConsume(long nc) {
        this.nextConsume = nc;
    }

    public int getRank() {
        return rank;
    }

    public int getRankMove() {
        return rankMove;
    }

    public int getJobRank() {
        return jobRank;
    }

    public int getJobRankMove() {
        return jobRankMove;
    }

    public void getAllModes() {
        if (GameConstants.isBeastTamer(job)) {
            final Map<Skill, SkillEntry> ss = new HashMap<>();
            ss.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001502), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001503), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001504), new SkillEntry((byte) 1, (byte) 1, -1));
            changeSkillsLevel(ss);
            NPCScriptManager.getInstance().dispose(client);
            System.out.println("All modes are added");
        } else {
            dropMessage(6, "你不是驯兽师!");
        }
    }

    public void changeChannel(final int channel) {
        final ChannelServer toch = ChannelServer.getInstance(channel);

        if (channel == client.getChannel() || toch == null || toch.isShutdown()) {
            client.getSession().write(CField.serverBlocked(1));
            return;
        }
        changeRemoval();

        final ChannelServer ch = ChannelServer.getInstance(client.getChannel());
        if (getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(getId(), getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(getId(), getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(getId(), getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(this), getId(), channel);
        ch.removePlayer(this);
        client.updateLoginState(MapleClient.CHANGE_CHANNEL, client.getSessionIPAddress());
        final String s = client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        client.getSession().write(CField.getChannelChange(client, Integer.parseInt(toch.getIP().split(":")[1])));
        saveToDB(false, false);
        getMap().removePlayer(this);

        client.setPlayer(null);
        client.setReceiving(false);
    }

    public void forceChangeChannel(final int channel) {
        final ChannelServer toch = ChannelServer.getInstance(channel);

        changeRemoval();

        final ChannelServer ch = ChannelServer.getInstance(client.getChannel());
        if (getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(getId(), getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(getId(), getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(getId(), getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(this), getId(), channel);
        ch.removePlayer(this);
        client.updateLoginState(MapleClient.CHANGE_CHANNEL, client.getSessionIPAddress());
        final String s = client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        client.getSession().write(CField.getChannelChange(client, Integer.parseInt(toch.getIP().split(":")[1])));
        saveToDB(false, false);
        getMap().removePlayer(this);

        client.setPlayer(null);
        client.setReceiving(false);
    }

    public void expandInventory(byte type, int amount) {
        final MapleInventory inv = getInventory(MapleInventoryType.getByType(type));
        inv.addSlot((byte) amount);
        client.getSession().write(InventoryPacket.getSlotUpdate(type, (byte) inv.getSlotLimit()));
    }

    public boolean allowedToTarget(MapleCharacter other) {
        return other != null && (!other.isHidden() || getGMLevel() >= other.getGMLevel());
    }

    public int getFollowId() {
        return followid;
    }

    public void setFollowId(int fi) {
        this.followid = fi;
        if (fi == 0) {
            this.followinitiator = false;
            this.followon = false;
        }
    }

    public void setFollowInitiator(boolean fi) {
        this.followinitiator = fi;
    }

    public void setFollowOn(boolean fi) {
        this.followon = fi;
    }

    public boolean isFollowOn() {
        return followon;
    }

    public boolean isFollowInitiator() {
        return followinitiator;
    }

    public void checkFollow() {
        if (followid <= 0) {
            return;
        }
        if (followon) {
            map.broadcastMessage(CField.followEffect(id, 0, null));
            map.broadcastMessage(CField.followEffect(followid, 0, null));
        }
        MapleCharacter tt = map.getCharacterById(followid);
        client.getSession().write(CField.getFollowMessage("Follow canceled."));
        if (tt != null) {
            tt.setFollowId(0);
            tt.getClient().getSession().write(CField.getFollowMessage("Follow canceled."));
        }
        setFollowId(0);
    }

    public int getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(final int mi) {
        this.marriageId = mi;
    }

    public int getMarriageItemId() {
        return marriageItemId;
    }

    public void setMarriageItemId(final int mi) {
        this.marriageItemId = mi;
    }

    public MapleMarriage getMarriage() {
        return marriage;
    }

    public void setMarriage(MapleMarriage marriage) {
        this.marriage = marriage;
    }

    public boolean isStaff() {
        return this.gmLevel >= 1;
    }

    // TODO: gvup, vic, lose, draw, VR
    public boolean startPartyQuest(final int questid) {
        boolean ret = false;
        MapleQuest q = MapleQuest.getInstance(questid);
        if (q == null || !q.isPartyQuest()) {
            return false;
        }
        if (!quests.containsKey(q) || !questinfo.containsKey(questid)) {
            final MapleQuestStatus status = getQuestNAdd(q);
            status.setStatus((byte) 1);
            updateQuest(status);
            switch (questid) {
                case 1300:
                case 1301:
                case 1302: //carnival, ariants.
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0;gvup=0;vic=0;lose=0;draw=0");
                    break;
                case 1303: //ghost pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0;vic=0;lose=0");
                    break;
                case 1204: //herb town pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;have2=0;have3=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                case 1206: //ellin pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                default:
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
            }
            ret = true;
        } //started the quest.
        return ret;
    }

    public String getOneInfo(final int questid, final String key) {
        if (!questinfo.containsKey(questid) || key == null || MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return null;
        }
        final String[] split = questinfo.get(questid).split(";");
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length == 2 && split2[0].equals(key)) {
                return split2[1];
            }
        }
        return null;
    }

    public void updateOneInfo(final int questid, final String key, final String value) {
        if (!questinfo.containsKey(questid) || key == null || value == null || MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        final String[] split = questinfo.get(questid).split(";");
        boolean changed = false;
        final StringBuilder newQuest = new StringBuilder();
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length != 2) {
                continue;
            }
            if (split2[0].equals(key)) {
                newQuest.append(key).append("=").append(value);
            } else {
                newQuest.append(x);
            }
            newQuest.append(";");
            changed = true;
        }

        updateInfoQuest(questid, changed ? newQuest.toString().substring(0, newQuest.toString().length() - 1) : newQuest.toString());
    }

    public void recalcPartyQuestRank(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        if (!startPartyQuest(questid)) {
            final String oldRank = getOneInfo(questid, "rank");
            if (oldRank == null || oldRank.equals("S")) {
                return;
            }
            String newRank;
            switch (oldRank) {
                case "A":
                    newRank = "S";
                    break;
                case "B":
                    newRank = "A";
                    break;
                case "C":
                    newRank = "B";
                    break;
                case "D":
                    newRank = "C";
                    break;
                case "F":
                    newRank = "D";
                    break;
                default:
                    return;
            }
            final List<Pair<String, Pair<String, Integer>>> questInfo = MapleQuest.getInstance(questid).getInfoByRank(newRank);
            if (questInfo == null) {
                return;
            }
            for (Pair<String, Pair<String, Integer>> q : questInfo) {
                boolean found = false;
                final String val = getOneInfo(questid, q.right.left);
                if (val == null) {
                    return;
                }
                int vall;
                try {
                    vall = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return;
                }
                switch (q.left) {
                    case "less":
                        found = vall < q.right.right;
                        break;
                    case "more":
                        found = vall > q.right.right;
                        break;
                    case "equal":
                        found = vall == q.right.right;
                        break;
                }
                if (!found) {
                    return;
                }
            }
            //perfectly safe
            updateOneInfo(questid, "rank", newRank);
        }
    }

    public void tryPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            startPartyQuest(questid);
            pqStartTime = System.currentTimeMillis();
            updateOneInfo(questid, "try", String.valueOf(Integer.parseInt(getOneInfo(questid, "try")) + 1));
        } catch (NumberFormatException e) {
            System.out.println("tryPartyQuest error");
        }
    }

    public void endPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            startPartyQuest(questid);
            if (pqStartTime > 0) {
                final long changeTime = System.currentTimeMillis() - pqStartTime;
                final int mins = (int) (changeTime / 1000 / 60), secs = (int) (changeTime / 1000 % 60);
                final int mins2 = Integer.parseInt(getOneInfo(questid, "min"));
                if (mins2 <= 0 || mins < mins2) {
                    updateOneInfo(questid, "min", String.valueOf(mins));
                    updateOneInfo(questid, "sec", String.valueOf(secs));
                    updateOneInfo(questid, "date", FileoutputUtil.CurrentReadable_Date());
                }
                final int newCmp = Integer.parseInt(getOneInfo(questid, "cmp")) + 1;
                updateOneInfo(questid, "cmp", String.valueOf(newCmp));
                updateOneInfo(questid, "CR", String.valueOf((int) Math.ceil((newCmp * 100.0) / Integer.parseInt(getOneInfo(questid, "try")))));
                recalcPartyQuestRank(questid);
                pqStartTime = 0;
            }
        } catch (Exception e) {
            System.out.println("endPartyQuest error");
        }

    }

    public void havePartyQuest(final int itemId) {
        int questid, index = -1;
        switch (itemId) {
            case 1002798:
                questid = 1200; //henesys
                break;
            case 1072369:
                questid = 1201; //kerning
                break;
            case 1022073:
                questid = 1202; //ludi
                break;
            case 1082232:
                questid = 1203; //orbis
                break;
            case 1002571:
            case 1002572:
            case 1002573:
            case 1002574:
                questid = 1204; //herbtown
                index = itemId - 1002571;
                break;
            case 1102226:
                questid = 1303; //ghost
                break;
            case 1102227:
                questid = 1303; //ghost
                index = 0;
                break;
            case 1122010:
                questid = 1205; //magatia
                break;
            case 1032061:
            case 1032060:
                questid = 1206; //ellin
                index = itemId - 1032060;
                break;
            case 3010018:
                questid = 1300; //ariant
                break;
            case 1122007:
                questid = 1301; //carnival
                break;
            case 1122058:
                questid = 1302; //carnival2
                break;
            default:
                return;
        }
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        startPartyQuest(questid);
        updateOneInfo(questid, "have" + (index == -1 ? "" : index), "1");
    }

    public void resetStatsByJob(boolean beginnerJob) {
        int baseJob = (beginnerJob ? (job % 1000) : (((job % 1000) / 100) * 100)); //1112 -> 112 -> 1 -> 100
        boolean UA = getQuestNoAdd(MapleQuest.getInstance(GameConstants.ULT_EXPLORER)) != null;
        if (baseJob == 100) { //first job = warrior
            resetStats(UA ? 4 : 35, 4, 4, 4);
        } else if (baseJob == 200) {
            resetStats(4, 4, UA ? 4 : 20, 4);
        } else if (baseJob == 300 || baseJob == 400) {
            resetStats(4, UA ? 4 : 25, 4, 4);
        } else if (baseJob == 500) {
            resetStats(4, UA ? 4 : 20, 4, 4);
        } else if (baseJob == 0) {
            resetStats(4, 4, 4, 4);
        }
    }

    public boolean hasSummon() {
        return hasSummon;
    }

    public void setHasSummon(boolean summ) {
        this.hasSummon = summ;
    }

    public void removeDoor() {
        final MapleDoor door = getDoors().iterator().next();
        for (final MapleCharacter chr : door.getTarget().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleCharacter chr : door.getTown().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleDoor destroyDoor : getDoors()) {
            door.getTarget().removeMapObject(destroyDoor);
            door.getTown().removeMapObject(destroyDoor);
        }
        clearDoors();
    }

    public void removeMechDoor() {
        for (final MechDoor destroyDoor : getMechDoors()) {
            for (final MapleCharacter chr : getMap().getCharactersThreadsafe()) {
                destroyDoor.sendDestroyData(chr.getClient());
            }
            getMap().removeMapObject(destroyDoor);
        }
        clearMechDoors();
    }

    public void changeRemoval() {
        changeRemoval(false);
    }

    public void changeRemoval(boolean dc) {
        if (getTrade() != null) {
            MapleTrade.cancelTrade(getTrade(), client, this);
        }
        if (getCheatTracker() != null && dc) {
            getCheatTracker().dispose();
        }
        removeFamiliar();
        // dispelSummons();
        if (!dc) {
            cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            cancelEffectFromBuffStat(MapleBuffStat.SOARING);
            cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
            cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
            cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
        }
        if (getPyramidSubway() != null) {
            getPyramidSubway().dispose(this);
        }
        if (playerShop != null && !dc) {
            playerShop.removeVisitor(this);
            if (playerShop.isOwner(this)) {
                playerShop.setOpen(true);
            }
        }
        if (!getDoors().isEmpty()) {
            removeDoor();
        }
        if (!getMechDoors().isEmpty()) {
            removeMechDoor();
        }
        disposeClones();
        NPCScriptManager.getInstance().dispose(client);
        cancelFairySchedule(false);
    }

    public void updateTick(int newTick) {
        anticheat.updateTick(newTick);
    }

    public boolean canUseFamilyBuff(MapleFamilyBuff buff) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(buff.questID));
        if (stat == null) {
            return true;
        }
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Long.parseLong(stat.getCustomData()) + (24 * 3600000) < System.currentTimeMillis();
    }

    public void useFamilyBuff(MapleFamilyBuff buff) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(buff.questID));
        stat.setCustomData(String.valueOf(System.currentTimeMillis()));
    }

    public List<Integer> usedBuffs() {
        //assume count = 1
        List<Integer> used = new ArrayList<>();
        MapleFamilyBuff[] z = MapleFamilyBuff.values();
        for (int i = 0; i < z.length; i++) {
            if (!canUseFamilyBuff(z[i])) {
                used.add(i);
            }
        }
        return used;
    }

    public String getTeleportName() {
        return teleportname;
    }

    public void setTeleportName(final String tname) {
        teleportname = tname;
    }

    public int getNoJuniors() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getNoJuniors();
    }

    public MapleFamilyCharacter getMFC() {
        return mfc;
    }

    public void makeMFC(final int familyid, final int seniorid, final int junior1, final int junior2) {
        if (familyid > 0) {
            MapleFamily f = World.Family.getFamily(familyid);
            if (f == null) {
                mfc = null;
            } else {
                mfc = f.getMFC(id);
                if (mfc == null) {
                    mfc = f.addFamilyMemberInfo(this, seniorid, junior1, junior2);
                }
                if (mfc.getSeniorId() != seniorid) {
                    mfc.setSeniorId(seniorid);
                }
                if (mfc.getJunior1() != junior1) {
                    mfc.setJunior1(junior1);
                }
                if (mfc.getJunior2() != junior2) {
                    mfc.setJunior2(junior2);
                }
            }
        } else {
            mfc = null;
        }
    }

    public void setFamily(final int newf, final int news, final int newj1, final int newj2) {
        if (mfc == null || newf != mfc.getFamilyId() || news != mfc.getSeniorId() || newj1 != mfc.getJunior1() || newj2 != mfc.getJunior2()) {
            makeMFC(newf, news, newj1, newj2);
        }
    }

    public int maxBattleshipHP(int skillid) {
        return (getTotalSkillLevel(skillid) * 5000) + ((getLevel() - 120) * 3000);
    }

    public int currentBattleshipHP() {
        return battleshipHP;
    }

    public void setBattleshipHP(int v) {
        this.battleshipHP = v;
    }

    public void decreaseBattleshipHP() {
        this.battleshipHP--;
    }

    public int getGachExp() {
        return gachexp;
    }

    public void setGachExp(int ge) {
        this.gachexp = ge;
    }

    public boolean isInBlockedMap() {
        if (!isAlive() || getPyramidSubway() != null || getMap().getSquadByMap() != null || getEventInstance() != null || getMap().getEMByMap() != null) {
            return true;
        }
        if ((getMapId() >= 680000210 && getMapId() <= 680000502) || (getMapId() / 10000 == 92502 && getMapId() >= 925020100) || (getMapId() / 10000 == 92503) || getMapId() == GameConstants.JAIL) {
            return true;
        }
        for (int i : GameConstants.blockedMaps) {
            if (getMapId() == i) {
                return true;
            }
        }
        if (getMapId() >= 689010000 && getMapId() < 689014000) { //Pink Zakum
            return true;
        }
        return false;
    }

    public boolean isInTownMap() {
        if (hasBlockedInventory() || !getMap().isTown() || FieldLimitType.VipRock.check(getMap().getFieldLimit()) || getEventInstance() != null) {
            return false;
        }
        for (int i : GameConstants.blockedMaps) {
            if (getMapId() == i) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBlockedInventory() {
        return !isAlive() || getTrade() != null || getConversation() > 0 || getDirection() >= 0 || getPlayerShop() != null || map == null;
    }

    public void startPartySearch(final List<Integer> jobs, final int maxLevel, final int minLevel, final int membersNeeded) {
        for (MapleCharacter chr : map.getCharacters()) {
            if (chr.getId() != id && chr.getParty() == null && chr.getLevel() >= minLevel && chr.getLevel() <= maxLevel && (jobs.isEmpty() || jobs.contains((int) chr.getJob())) && (isGM() || !chr.isGM())) {
                if (party != null && party.getMembers().size() < 6 && party.getMembers().size() < membersNeeded) {
                    chr.setParty(party);
                    World.Party.updateParty(party.getId(), PartyOperation.JOIN, new MaplePartyCharacter(chr));
                    chr.receivePartyMemberHP();
                    chr.updatePartyMemberHP();
                } else {
                    break;
                }
            }
        }
    }

    public int getChallenge() {
        return challenge;
    }

    public void setChallenge(int c) {
        this.challenge = c;
    }

    public short getFatigue() {
        return fatigue;
    }

    public void setFatigue(int j) {
        this.fatigue = (short) Math.max(0, j);
//        updateSingleStat(MapleStat.FATIGUE, this.fatigue);
        client.getSession().write(CWvsContext.enableActions());
    }

    public void fakeRelog() {
        client.getSession().write(CField.getCharInfo(this));
        final MapleMap mapp = getMap();
        mapp.setCheckStates(false);
        mapp.removePlayer(this);
        mapp.addPlayer(this);
        mapp.setCheckStates(true);

        client.getSession().write(CWvsContext.getFamiliarInfo(this));
    }

    public boolean canSummon() {
        return canSummon(5000);
    }

    public boolean canSummon(int g) {
        if (lastSummonTime + g < System.currentTimeMillis()) {
            lastSummonTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public int getIntNoRecord(int questID) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public long getLongNoRecord(int questID) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Long.parseLong(stat.getCustomData());
    }

    public int getIntRecord(int questID) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public long getLongRecord(int questID) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Long.parseLong(stat.getCustomData());
    }

    public void updatePetAuto() {
        if (getIntNoRecord(GameConstants.HP_ITEM) > 0) {
            client.getSession().write(CField.petAutoHP(getIntRecord(GameConstants.HP_ITEM)));
        }
        if (getIntNoRecord(GameConstants.MP_ITEM) > 0) {
            client.getSession().write(CField.petAutoMP(getIntRecord(GameConstants.MP_ITEM)));
        }
        //if (getIntNoRecord(GameConstants.BUFF_ITEM) > 0) {
        //     client.getSession().write(CField.petAutoBuff(getIntRecord(GameConstants.BUFF_ITEM)));
        //}
    }

    public void setChangeTime() {
        mapChangeTime = System.currentTimeMillis();
    }

    public long getChangeTime() {
        return mapChangeTime;
    }

    public Map<ReportType, Integer> getReports() {
        return reports;
    }

    public void addReport(ReportType type) {
        Integer value = reports.get(type);
        reports.put(type, value == null ? 1 : (value + 1));
        changed_reports = true;
    }

    public void clearReports(ReportType type) {
        reports.remove(type);
        changed_reports = true;
    }

    public void clearReports() {
        reports.clear();
        changed_reports = true;
    }

    public final int getReportPoints() {
        int ret = 0;
        for (Integer entry : reports.values()) {
            ret += entry;
        }
        return ret;
    }

    public final String getReportSummary() {
        final StringBuilder ret = new StringBuilder();
        final List<Pair<ReportType, Integer>> offenseList = new ArrayList<>();
        for (final Entry<ReportType, Integer> entry : reports.entrySet()) {
            offenseList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        Collections.sort(offenseList, new Comparator<Pair<ReportType, Integer>>() {

            @Override
            public final int compare(final Pair<ReportType, Integer> o1, final Pair<ReportType, Integer> o2) {
                final int thisVal = o1.getRight();
                final int anotherVal = o2.getRight();
                return (thisVal < anotherVal ? 1 : (thisVal == anotherVal ? 0 : -1));
            }
        });
        for (int x = 0; x < offenseList.size(); x++) {
            ret.append(StringUtil.makeEnumHumanReadable(offenseList.get(x).left.name()));
            ret.append(": ");
            ret.append(offenseList.get(x).right);
            ret.append(" ");
        }
        return ret.toString();
    }

    public short getScrolledPosition() {
        return scrolledPosition;
    }

    public void setScrolledPosition(short s) {
        this.scrolledPosition = s;
    }

    public MapleTrait getTrait(MapleTraitType t) {
        return traits.get(t);
    }

    public void forceCompleteQuest(int id) {
        MapleQuest.getInstance(id).forceComplete(this, 9270035); //troll
    }

    public List<Integer> getExtendedSlots() {
        return extendedSlots;
    }

    public int getExtendedSlot(int index) {
        if (extendedSlots.size() <= index || index < 0) {
            return -1;
        }
        return extendedSlots.get(index);
    }

    public void changedExtended() {
        changed_extendedSlots = true;
    }

    public MapleAndroid getAndroid() {
        return android;
    }

    public void removeAndroid() {
        if (map != null) {
            map.broadcastMessage(CField.deactivateAndroid(this.id));
        }
        android = null;
    }

    public void setAndroid(MapleAndroid a) {
        this.android = a;
        if ((this.map != null) && (a != null)) {
            this.map.broadcastMessage(CField.spawnAndroid(this, a));
            this.map.broadcastMessage(CField.showAndroidEmotion(getId(),
                    (byte) Randomizer.nextInt(5), (byte) Randomizer.nextInt(1)));
        }
    }

    public Map<Integer, MonsterFamiliar> getFamiliars() {
        return familiars;
    }

    public MonsterFamiliar getSummonedFamiliar() {
        return summonedFamiliar;
    }

    public void removeFamiliar() {
        if (summonedFamiliar != null && map != null) {
            removeVisibleFamiliar();
        }
        summonedFamiliar = null;
    }

    public void removeVisibleFamiliar() {
        getMap().removeMapObject(summonedFamiliar);
        removeVisibleMapObject(summonedFamiliar);
        getMap().broadcastMessage(CField.removeFamiliar(this.getId()));
        anticheat.resetFamiliarAttack();
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        cancelEffect(ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive), false, System.currentTimeMillis());
    }

    public void spawnFamiliar(MonsterFamiliar mf, boolean respawn) {
        summonedFamiliar = mf;

        mf.setStance(0);
        mf.setPosition(getPosition());
        mf.setFh(getFH());
        addVisibleMapObject(mf);
        getMap().spawnFamiliar(mf, respawn);

        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleStatEffect eff = ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive);
        if (eff != null && eff.getInterval() <= 0 && eff.makeChanceResult()) { //i think this is actually done through a recv, which is ATTACK_FAMILIAR +1
            eff.applyTo(this);
        }
        lastFamiliarEffectTime = System.currentTimeMillis();
    }

    public final boolean canFamiliarEffect(long now, MapleStatEffect eff) {
        return lastFamiliarEffectTime > 0 && lastFamiliarEffectTime + eff.getInterval() < now;
    }

    public void doFamiliarSchedule(long now) {
        if (familiars == null) {
            return;
        }
        for (MonsterFamiliar mf : familiars.values()) {
            if (summonedFamiliar != null && summonedFamiliar.getId() == mf.getId()) {
                mf.addFatigue(this, 5);
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final MapleStatEffect eff = ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive);
                if (eff != null && eff.getInterval() > 0 && canFamiliarEffect(now, eff) && eff.makeChanceResult()) {
                    eff.applyTo(this);
                }
            } else if (mf.getFatigue() > 0) {
                mf.setFatigue(Math.max(0, mf.getFatigue() - 5));
            }
        }
    }

    public MapleImp[] getImps() {
        return imps;
    }

    public void sendImp() {
        for (int i = 0; i < imps.length; i++) {
            if (imps[i] != null) {
                client.getSession().write(CWvsContext.updateImp(imps[i], ImpFlag.SUMMONED.getValue(), i, true));
            }
        }
    }

    public int getBattlePoints() {
        return pvpPoints;
    }

    public int getTotalBattleExp() {
        return pvpExp;
    }

    public void setBattlePoints(int p) {
        if (p != pvpPoints) {
            client.getSession().write(InfoPacket.getBPMsg(p - pvpPoints));
            //updateSingleStat(MapleStat.BATTLE_POINTS, p);
            client.getSession().write(CWvsContext.enableActions());
        }
        this.pvpPoints = p;
    }

    public void setTotalBattleExp(int p) {
        final int previous = pvpExp;
        this.pvpExp = p;
        if (p != previous) {
            stats.recalcPVPRank(this);

            //updateSingleStat(MapleStat.BATTLE_EXP, stats.pvpExp);
            // updateSingleStat(MapleStat.BATTLE_RANK, stats.pvpRank);
            client.getSession().write(CWvsContext.enableActions());
        }
    }

    public void changeTeam(int newTeam) {
        this.coconutteam = newTeam;

        if (inPVP()) {
            client.getSession().write(CField.getPVPTransform(newTeam + 1));
            map.broadcastMessage(CField.changeTeam(id, newTeam + 1));
        } else {
            client.getSession().write(CField.showEquipEffect(newTeam));
        }
    }

    public void disease(int type, int level) {
        if (MapleDisease.getBySkill(type) == null) {
            return;
        }
        chair = 0;
        client.getSession().write(CField.cancelChair(-1));
        map.broadcastMessage(this, CField.showChair(id, 0), false);
        giveDebuff(MapleDisease.getBySkill(type), MobSkillFactory.getMobSkill(type, level));
    }

    public boolean inPVP() {
        return eventInstance != null && eventInstance.getName().startsWith("PVP");
    }

    public boolean inAzwan() {
        return mapid >= 262020000 && mapid < 262023000;
    }

    public void clearAllCooldowns() {
        for (MapleCoolDownValueHolder m : getCooldowns()) {
            final int skil = m.skillId;
            removeCooldown(skil);
            client.getSession().write(CField.skillCooldown(skil, 0));
        }
    }

    public Pair<Double, Boolean> modifyDamageTaken(double damage, MapleMapObject attacke) {
        Pair<Double, Boolean> ret = new Pair<>(damage, false);
        if (damage <= 0) {
            return ret;
        }
        if (stats.ignoreDAMr > 0 && Randomizer.nextInt(100) < stats.ignoreDAMr_rate) {
            damage -= Math.floor((stats.ignoreDAMr * damage) / 100.0f);
        }
        if (stats.ignoreDAM > 0 && Randomizer.nextInt(100) < stats.ignoreDAM_rate) {
            damage -= stats.ignoreDAM;
        }
        final Integer div = getBuffedValue(MapleBuffStat.DIVINE_SHIELD);
        final Integer div2 = getBuffedValue(MapleBuffStat.HOLY_MAGIC_SHELL);
        if (div2 != null) {
            if (div2 <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.HOLY_MAGIC_SHELL);
            } else {
                setBuffedValue(MapleBuffStat.HOLY_MAGIC_SHELL, div2 - 1);
                damage = 0;
            }
        } else if (div != null) {
            if (div <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.DIVINE_SHIELD);
            } else {
                setBuffedValue(MapleBuffStat.DIVINE_SHIELD, div - 1);
                damage = 0;
            }
        }
        MapleStatEffect barrier = getStatForBuff(MapleBuffStat.COMBO_BARRIER);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        barrier = getStatForBuff(MapleBuffStat.MAGIC_SHIELD);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        barrier = getStatForBuff(MapleBuffStat.WATER_SHIELD);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        List<Integer> attack = attacke instanceof MapleMonster || attacke == null ? null : (new ArrayList<Integer>());
        if (damage > 0) {
            if (getJob() == 122 && !skillisCooling(1220013)) {
                final Skill divine = SkillFactory.getSkill(1220013);
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        divineShield.applyTo(this);
                        client.getSession().write(CField.skillCooldown(1220013, divineShield.getCooldown(this)));
                        addCooldown(1220013, System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                    }
                }
            } else if (getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC) != null && getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB) != null && getBuffedValue(MapleBuffStat.PUPPET) != null) {
                double buff = getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC).doubleValue();
                double buffz = getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB).doubleValue();
                if ((int) ((buff / 100.0) * getStat().getMaxHp()) <= damage) {
                    damage -= ((buffz / 100.0) * damage);
                    cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
                }
            } else if (getJob() == 433 || getJob() == 434) {
                final Skill divine = SkillFactory.getSkill(4330001);
                if (getTotalSkillLevel(divine) > 0 && getBuffedValue(MapleBuffStat.DARKSIGHT) == null && !skillisCooling(divine.getId())) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (Randomizer.nextInt(100) < divineShield.getX()) {
                        divineShield.applyTo(this);
                    }
                }
            } else if ((getJob() == 512 || getJob() == 522) && getBuffedValue(MapleBuffStat.DAMAGE_RATE) == null) {
                final Skill divine = SkillFactory.getSkill(getJob() == 512 ? 5120011 : 5220012);
                if (getTotalSkillLevel(divine) > 0 && !skillisCooling(divine.getId())) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        divineShield.applyTo(this);
                        client.getSession().write(CField.skillCooldown(divine.getId(), divineShield.getCooldown(this)));
                        addCooldown(divine.getId(), System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                    }
                }
            } else if (getJob() == 312 && attacke != null) {
                final Skill divine = SkillFactory.getSkill(3120010);
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        if (attacke instanceof MapleMonster) {
                            final Rectangle bounds = divineShield.calculateBoundingBox(getTruePosition(), isFacingLeft());
                            final List<MapleMapObject> affected = getMap().getMapObjectsInRect(bounds, Arrays.asList(attacke.getType()));
                            int i = 0;

                            for (final MapleMapObject mo : affected) {
                                MapleMonster mons = (MapleMonster) mo;
                                if (mons.getStats().isFriendly() || mons.isFake()) {
                                    continue;
                                }
                                mons.applyStatus(this, new MonsterStatusEffect(MonsterStatus.STUN, 1, divineShield.getSourceId(), null, false), false, divineShield.getDuration(), true, divineShield);
                                final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                                mons.damage(this, theDmg, true);
                                getMap().broadcastMessage(MobPacket.damageMonster(mons.getObjectId(), theDmg));
                                i++;
                                if (i >= divineShield.getMobCount()) {
                                    break;
                                }
                            }
                        } else {
                            MapleCharacter chr = (MapleCharacter) attacke;
                            chr.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            } else if ((getJob() == 531 || getJob() == 532) && attacke != null) {
                final Skill divine = SkillFactory.getSkill(5310009); //slea.readInt() = 5310009, then slea.readInt() = damage. (175000)
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        if (attacke instanceof MapleMonster) {
                            final MapleMonster attacker = (MapleMonster) attacke;
                            final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                            attacker.damage(this, theDmg, true);
                            getMap().broadcastMessage(MobPacket.damageMonster(attacker.getObjectId(), theDmg));
                        } else {
                            final MapleCharacter attacker = (MapleCharacter) attacke;
                            attacker.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            } else if (getJob() == 132 && attacke != null) {
                final Skill divine = SkillFactory.getSkill(1320011);
                if (getTotalSkillLevel(divine) > 0 && !skillisCooling(divine.getId()) && getBuffSource(MapleBuffStat.BEHOLDER) == 1321007) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        client.getSession().write(CField.skillCooldown(divine.getId(), divineShield.getCooldown(this)));
                        addCooldown(divine.getId(), System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                        if (attacke instanceof MapleMonster) {
                            final MapleMonster attacker = (MapleMonster) attacke;
                            final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                            attacker.damage(this, theDmg, true);
                            getMap().broadcastMessage(MobPacket.damageMonster(attacker.getObjectId(), theDmg));
                        } else {
                            final MapleCharacter attacker = (MapleCharacter) attacke;
                            attacker.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            }
            if (attacke != null) {
                final int damr = (Randomizer.nextInt(100) < getStat().DAMreflect_rate ? getStat().DAMreflect : 0) + (getBuffedValue(MapleBuffStat.POWERGUARD) != null ? getBuffedValue(MapleBuffStat.POWERGUARD) : 0);
                final int bouncedam_ = damr + (getBuffedValue(MapleBuffStat.PERFECT_ARMOR) != null ? getBuffedValue(MapleBuffStat.PERFECT_ARMOR) : 0);
                if (bouncedam_ > 0) {
                    long bouncedamage = (long) (damage * bouncedam_ / 100);
                    long bouncer = (long) (damage * damr / 100);
                    damage -= bouncer;
                    if (attacke instanceof MapleMonster) {
                        final MapleMonster attacker = (MapleMonster) attacke;
                        bouncedamage = Math.min(bouncedamage, attacker.getMobMaxHp() / 10);
                        attacker.damage(this, (int) bouncedamage, true);
                        getMap().broadcastMessage(this, MobPacket.damageMonster(attacker.getObjectId(), (int) bouncedamage), getTruePosition());
                        if (getBuffSource(MapleBuffStat.PERFECT_ARMOR) == 31101003) {
                            MapleStatEffect eff = this.getStatForBuff(MapleBuffStat.PERFECT_ARMOR);
                            if (eff.makeChanceResult()) {
                                attacker.applyStatus(this, new MonsterStatusEffect(MonsterStatus.STUN, 1, eff.getSourceId(), null, false), false, eff.getSubTime(), true, eff);
                            }
                        }
                    } else {
                        final MapleCharacter attacker = (MapleCharacter) attacke;
                        bouncedamage = Math.min(bouncedamage, attacker.getStat().getCurrentMaxHp() / 10);
                        attacker.addHP(-((int) bouncedamage));
                        attack.add((int) bouncedamage);
                        if (getBuffSource(MapleBuffStat.PERFECT_ARMOR) == 31101003) {
                            MapleStatEffect eff = this.getStatForBuff(MapleBuffStat.PERFECT_ARMOR);
                            if (eff.makeChanceResult()) {
                                attacker.disease(MapleDisease.STUN.getDisease(), 1);
                            }
                        }
                    }
                    ret.right = true;
                }
                if ((getJob() == 411 || getJob() == 412 || getJob() == 421 || getJob() == 422) && getBuffedValue(MapleBuffStat.SUMMON) != null && attacke != null) {
                    final List<MapleSummon> ss = getSummonsReadLock();
                    try {
                        for (MapleSummon sum : ss) {
                            if (sum.getTruePosition().distanceSq(getTruePosition()) < 400000.0 && (sum.getSkill() == 4111007 || sum.getSkill() == 4211007 || sum.getSkill() == 14111010)) {
                                final List<Pair<Integer, Integer>> allDamage = new ArrayList<>();
                                if (attacke instanceof MapleMonster) {
                                    final MapleMonster attacker = (MapleMonster) attacke;
                                    final int theDmg = (int) (SkillFactory.getSkill(sum.getSkill()).getEffect(sum.getSkillLevel()).getX() * damage / 100.0);
                                    allDamage.add(new Pair<>(attacker.getObjectId(), theDmg));
                                    getMap().broadcastMessage(SummonPacket.summonAttack(sum.getOwnerId(), sum.getObjectId(), (byte) 0x84, allDamage, getLevel(), true));
                                    attacker.damage(this, theDmg, true);
                                    checkMonsterAggro(attacker);
                                    if (!attacker.isAlive()) {
                                        getClient().getSession().write(MobPacket.killMonster(attacker.getObjectId(), 1, false));
                                    }
                                } else {
                                    final MapleCharacter chr = (MapleCharacter) attacke;
                                    final int dmg = SkillFactory.getSkill(sum.getSkill()).getEffect(sum.getSkillLevel()).getX();
                                    chr.addHP(-dmg);
                                    attack.add(dmg);
                                }
                            }
                        }
                    } finally {
                        unlockSummonsReadLock();
                    }
                }
            }
        }
        if (attack != null && attack.size() > 0 && attacke != null) {
            getMap().broadcastMessage(CField.pvpCool(attacke.getObjectId(), attack));
        }
        ret.left = damage;
        return ret;
    }

    public void onAttack(long maxhp, int maxmp, int skillid, int oid, int totDamage, int critCount) {
        if (stats.hpRecoverProp > 0) {
            if (Randomizer.nextInt(100) <= stats.hpRecoverProp) {//i think its out of 100, anyway
                if (stats.hpRecover > 0) {
                    healHP(stats.hpRecover);
                }
                if (stats.hpRecoverPercent > 0) {
                    addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) stats.hpRecoverPercent / 100.0)), stats.getMaxHp() / 2))));
                }
            }
        }
        if (stats.mpRecoverProp > 0 && !GameConstants.isDemonSlayer(getJob())) {
            if (Randomizer.nextInt(100) <= stats.mpRecoverProp) {//i think its out of 100, anyway
                healMP(stats.mpRecover);
            }
        }
        if (getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
            addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxHp() / 2))));
        }
        if (getBuffSource(MapleBuffStat.COMBO_DRAIN) == 23101003) {
            addMP(((int) Math.min(maxmp, Math.min(((int) ((double) totDamage * (double) getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxMp() / 2))));
        }
        if (getBuffedValue(MapleBuffStat.REAPER) != null && getBuffedValue(MapleBuffStat.SUMMON) == null && getSummonsSize() < 4 && canSummon()) {
            final MapleStatEffect eff = getStatForBuff(MapleBuffStat.REAPER);
            if (eff.makeChanceResult()) {
                eff.applyTo(this, this, false, null, eff.getDuration());
            }
        }
        if (getJob() == 212 || getJob() == 222 || getJob() == 232) {
            int[] venomskills = {2120010, 2220010, 2320011};
            for (int i : venomskills) {
                final Skill skill = SkillFactory.getSkill(i);
                if (getTotalSkillLevel(skill) > 0) {
                    final MapleStatEffect venomEffect = skill.getEffect(getTotalSkillLevel(skill));
                    if (venomEffect.makeChanceResult() && getAllLinkMid().size() < venomEffect.getY()) {
                        setLinkMid(oid, venomEffect.getX());
                        venomEffect.applyTo(this);
                    }
                    break;
                }
            }
        }
        int[] venomskills = {4110011, 4120005, 4210010, 4220005, 4320005, 4340001, 14110004};
        for (int i : venomskills) {
            if (i == 4110011) {
                if (getTotalSkillLevel(4120011) > 0) {
                    i = 4120011;
                }
            } else if (i == 4210010) {
                if (getTotalSkillLevel(4220011) > 0) {
                    i = 4220011;
                }
            }
            final Skill skill = SkillFactory.getSkill(i);
            if (getTotalSkillLevel(skill) > 0) {
                final MapleStatEffect venomEffect = skill.getEffect(getTotalSkillLevel(skill));
                final MapleMonster monster = map.getMonsterByOid(oid);
                if (venomEffect.makeChanceResult() && monster != null) {
                    monster.applyStatus(this, new MonsterStatusEffect(MonsterStatus.POISON, 1, i, null, false), true, venomEffect.getDuration(), true, venomEffect);
                }
                break;
            }
        }
        // effects
        if (skillid > 0) {
            final Skill skil = SkillFactory.getSkill(skillid);
            final MapleStatEffect effect = skil.getEffect(getTotalSkillLevel(skil));
            switch (skillid) {
                case 3111008:
                case 1078:
                case 11078:
                case 4101005: //drain
                case 5111004: { // Energy Drain
                    addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) effect.getX() / 100.0)), stats.getMaxHp() / 2))));
                    break;
                }
                case 5211006:
                case 22151002: //killer wing
                case 5220011: {//homing
                    setLinkMid(oid, effect.getX());
                    break;
                }
            }
        }
    }

    public void attackCarte(final MapleStatEffect eff, final int oid, final int x) {
        if (x > 0) {
            lastBerserkTime += x; //lol unused variable.
            map.broadcastMessage(PhantomPacket.getCarteAnimation(id, oid, job, (int) lastBerserkTime, x));
            client.getSession().write(PhantomPacket.updateCardStack(currentBattleshipHP()));
        }
    }

    public void handleForceGain(int oid, int skillid) {
        handleForceGain(oid, skillid, 0);
    }

    public void handleForceGain(int oid, int skillid, int extraForce) {
        if (!GameConstants.isForceIncrease(skillid) && extraForce <= 0) {
            return;
        }
        int forceGain = 1;
        if (getLevel() >= 30 && getLevel() < 60) {
            forceGain = 2;
        } else if (getLevel() >= 60 && getLevel() < 100) {
            forceGain = 3;
        } else if (getLevel() >= 100) {
            forceGain = 4;
        }
        force++; // counter
        if (GameConstants.isDemonSlayer(getJob())) {
            addMP(extraForce > 0 ? extraForce : forceGain, true);
        }
        getClient().getSession().write(CField.gainForce(oid, force, forceGain));
        if (GameConstants.isDemonSlayer(getJob())) {
            if (stats.mpRecoverProp > 0 && extraForce <= 0) {
                if (Randomizer.nextInt(100) <= stats.mpRecoverProp) {//i think its out of 100, anyway
                    force++; // counter
                    addMP(stats.mpRecover, true);
                    getClient().getSession().write(CField.gainForce(oid, force, stats.mpRecover));
                }
            }
        }
    }

    public void afterAttack(int mobCount, int attackCount, int skillid) {
        switch (getJob()) {
            case 511:
            case 512: {
                handleEnergyCharge(5110001, mobCount * attackCount);
                break;
            }
            case 111:
            case 112:
                if (skillid != 1111008 & getBuffedValue(MapleBuffStat.COMBO) != null) { // shout should not give orbs
                    handleOrbgain();
                }
                break;
        }
        if (getBuffedValue(MapleBuffStat.OWL_SPIRIT) != null) {
            if (currentBattleshipHP() > 0) {
                decreaseBattleshipHP();
            }
            if (currentBattleshipHP() <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.OWL_SPIRIT);
            }
        }
        if (!isIntern()) {
            cancelEffectFromBuffStat(MapleBuffStat.WIND_WALK);
            cancelEffectFromBuffStat(MapleBuffStat.INFILTRATE);
            final MapleStatEffect ds = getStatForBuff(MapleBuffStat.DARKSIGHT);
            if (ds != null) {
                if (ds.getSourceId() != 4330001 || !ds.makeChanceResult()) {
                    cancelEffectFromBuffStat(MapleBuffStat.DARKSIGHT);
                }
            }
        }
    }

    public void applyIceGage(int x) {
        //updateSingleStat(MapleStat.ICE_GAGE, x);
        client.getSession().write(CWvsContext.enableActions());
    }

    public Rectangle getBounds() {
        return new Rectangle(getTruePosition().x - 25, getTruePosition().y - 75, 50, 75);
    }

    @Override
    public Map<Byte, Integer> getEquips(boolean fusionAnvil) {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            int itemId = item.getItemId();
            if (item instanceof Equip && fusionAnvil) {
                if (((Equip) item).getFusionAnvil() != 0) {
                    itemId = ((Equip) item).getFusionAnvil();
                }
            }
            eq.put((byte) item.getPosition(), itemId);
        }
        return eq;
    }

    @Override
    public Map<Byte, Integer> getSecondEquips(boolean fusionAnvil) {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            int itemId = item.getItemId();
            if (item instanceof Equip) {
                if (fusionAnvil) {
                    if (((Equip) item).getFusionAnvil() != 0) {
                        itemId = ((Equip) item).getFusionAnvil();
                    }
                }
                if (GameConstants.isAngelicBuster(getJob()) && GameConstants.isOverall(itemId)) {
                    itemId = 1051291; //ab def overall
                }
            }
            if (GameConstants.isAngelicBuster(getJob())) {
                if (!GameConstants.isOverall(itemId) && !GameConstants.isSecondaryWeapon(itemId)
                        && !GameConstants.isWeapon(itemId) && !GameConstants.isMedal(itemId)) {
                    continue;
                }
            }
            eq.put((byte) item.getPosition(), itemId);
        }
        return eq;
    }

    @Override
    public Map<Byte, Integer> getTotems() {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            eq.put((byte) (item.getPosition() + 5000), item.getItemId());
        }
        return eq;
    }
    private transient PlayerRandomStream CRand;

    public final PlayerRandomStream CRand() {
        return CRand;
    }

    public void handleCardStack() {
        Skill noir = SkillFactory.getSkill(24120002);
        Skill blanc = SkillFactory.getSkill(24100003);
        MapleStatEffect ceffect;
        int advSkillLevel = getTotalSkillLevel(noir);
        boolean isAdv = false;
        if (advSkillLevel > 0) {
            ceffect = noir.getEffect(advSkillLevel);
            isAdv = true;
        } else if (getSkillLevel(blanc) > 0) {
            ceffect = blanc.getEffect(getTotalSkillLevel(blanc));
        } else {
            return;
        }
        if (getJob() == 2412 && getCardStack() == 40) {
            return;
        }
        if (getJob() == 2400 && getCardStack() == 20 || getJob() == 2410 && getCardStack() == 20 || getJob() == 2411 && getCardStack() == 20) {
            return;
        }
        if (ceffect.makeChanceResult()) {
            if (this.cardStack < (getJob() == 2412 ? 40 : 20)) {
                this.cardStack = (byte) (this.cardStack + 1);
            }
            this.runningStack += 1;
            this.client.getSession().write(PhantomPacket.gainCardStack(getId(), this.runningStack, isAdv ? 2 : 1, ceffect.getSourceId(), Randomizer.rand(100000, 500000), 1));
            this.client.getSession().write(PhantomPacket.updateCardStack(this.cardStack));
        }
    }

    /*
     * public void resetRunningStack() { this.runningStack = 0; }
     *
     * public int getRunningStack() { return this.runningStack; }
     *
     * public void addRunningStack(int s) { this.runningStack += s; }
     *
     * public void setCardStack(byte amount) { this.cardStack = amount; }
     *
     * public byte getCardStack() { return cardStack; } // running id plox
     */
    public final MapleCharacterCards getCharacterCard() {
        return characterCard;
    }

    /*
     * Start of Custom Feature
     */
    public int getReborns() {
        return reborns;
    }

    public int getAPS() {
        return apstorage;
    }

    public void gainAPS(int aps) {
        apstorage += aps;
    }

    public final boolean canHold(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).getNextFreeSlot() > -1;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
        stats.recalcLocalStats(this);
    }

    public int getInt() {
        return int_;
    }

    public void setInt(int int_) {
        this.int_ = int_;
        stats.recalcLocalStats(this);
    }

    public int getLuk() {
        return luk;
    }

    public int getDex() {
        return dex;
    }

    public void setLuk(int luk) {
        this.luk = luk;
        stats.recalcLocalStats(this);
    }

    public void setDex(int dex) {
        this.dex = dex;
        stats.recalcLocalStats(this);
    }

    public void gainVPoints(int amount) {
        this.vpoints += amount;
    }

    public void gainEPoints(int amount) {
        this.epoints += amount;
    }

    public void gainDPoints(int amount) {
        this.dpoints += amount;
    }

    public static String makeMapleReadable(String in) {
        String i = in.replace('I', 'i');
        i = i.replace('l', 'L');
        i = i.replace("rn", "Rn");
        i = i.replace("vv", "Vv");
        i = i.replace("VV", "Vv");
        return i;
    }

    public void ban(String reason, boolean autoban, boolean mac) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            if (mac) {
                client.banMacs();
            }
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?");
            ps.setString(1, reason);
            ps.setInt(2, accountid);
            ps.executeUpdate();
            ps.close();
            final String ip = client.getSession().getRemoteAddress().toString().split(":")[0];
            ps = con.prepareStatement("SELECT ip FROM ipbans WHERE ip = ?");
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, ip);
                ps.executeUpdate();
                ps.close();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("ban" + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);

        client.getSession().close();
    }

    public void ban(String reason, boolean permBan) {
        if (lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            getClient().banMacs();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE id = ?");
            ps.setInt(1, 1);
            ps.setString(2, reason);
            ps.setInt(3, accountid);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
            String[] ipSplit = client.getSession().getRemoteAddress().toString().split(":");
            ps.setString(1, ipSplit[0]);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("ban" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        client.getSession().close();
    }

    public static boolean ban(String id, String reason, boolean accountId) {
        PreparedStatement ps = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement psb = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?")) {
                        psb.setString(1, reason);
                        psb.setInt(2, rs.getInt(1));
                        psb.executeUpdate();
                    }
                    ret = true;
                }
            }
            ps.close();
            return ret;
        } catch (SQLException ex) {
            System.err.println("ban" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println("ban" + e);
                FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
            }
        }
        return false;
    }

    public static boolean unban(String name) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int accountid = -1;
            PreparedStatement ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                accountid = rs.getInt("accountid");
            }
            ps.close();
            rs.close();
            if (accountid == -1) {
                return false;
            }
            ps = con.prepareStatement("UPDATE accounts SET banned = -1 WHERE id = ?");
            ps.setInt(1, accountid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error while unbanning" + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
            return false;
        }
        return true;
    }

    public void changeMap(int map, int portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, warpMap.getPortal(portal));
    }

    public void unchooseStolenSkill(int skillID) { //base skill
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[装备]技能正在冷却。请等待.");
            return;
        }
        final int stolenjob = GameConstants.getJobNumber(skillID / 10000);
        boolean changed = false;
        for (Pair<Integer, Boolean> sk : stolenSkills) {
            if (sk.right && GameConstants.getJobNumber(sk.left / 10000) == stolenjob) {
                cancelStolenSkill(sk.left);
                sk.right = false;
                changed = true;
            }
        }
        if (changed) {
            final Skill skil = SkillFactory.getSkill(skillID);
            changeSkillLevel_Skip(skil, getSkillLevel(skil), (byte) 0);
            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(stolenjob), 0));
        }
    }

    public void cancelStolenSkill(int skillID) {
        final Skill skk = SkillFactory.getSkill(skillID);
        final MapleStatEffect eff = skk.getEffect(getTotalSkillLevel(skk));

        if (eff.isMonsterBuff() || (eff.getStatups().isEmpty() && !eff.getMonsterStati().isEmpty())) {
            for (MapleMonster mons : map.getAllMonstersThreadsafe()) {
                for (MonsterStatus b : eff.getMonsterStati().keySet()) {
                    if (mons.isBuffed(b) && mons.getBuff(b).getFromID() == this.id) {
                        mons.cancelStatus(b);
                    }
                }
            }
        } else if (eff.getDuration() > 0 && !eff.getStatups().isEmpty()) {
            for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                chr.cancelEffect(eff, false, -1, eff.getStatups());

            }
        }
    }

    public void chooseStolenSkill(int skillID) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[装备]技能正在冷却。请等待.");
            return;
        }
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, false);
        if (stolenSkills.contains(dummy)) {
            unchooseStolenSkill(skillID);
            stolenSkills.get(stolenSkills.indexOf(dummy)).right = true;

            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(GameConstants.getJobNumber(skillID / 10000)), skillID));
            //if (ServerConstants.CUSTOM_SKILL) {
            //    client.getSession().write(MaplePacketCreator.skillCooldown(20031208, 5));
            //    addCooldown(20031208, System.currentTimeMillis(), 5000);
            //}
        }
    }

    public void addStolenSkill(int skillID, int skillLevel) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[装备]技能正在冷却。请等待.");
            return;
        }
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, true);
        final Skill skil = SkillFactory.getSkill(skillID);
        if (!stolenSkills.contains(dummy) && GameConstants.canSteal(skil)) {
            dummy.right = false;
            skillLevel = Math.min(skil.getMaxLevel(), skillLevel);
            final int jobid = GameConstants.getJobNumber(skillID / 10000);
            if (!stolenSkills.contains(dummy) && getSkillLevel(GameConstants.getStealSkill(jobid)) > 0) {
                int count = 0;
                skillLevel = Math.min(getSkillLevel(GameConstants.getStealSkill(jobid)), skillLevel);
                for (Pair<Integer, Boolean> sk : stolenSkills) {
                    if (GameConstants.getJobNumber(sk.left / 10000) == jobid) {
                        count++;
                    }
                }
                if (count < GameConstants.getNumSteal(jobid)) {
                    stolenSkills.add(dummy);
                    changed_skills = true;
                    changeSkillLevel_Skip(skil, skillLevel, (byte) skillLevel);
                    client.getSession().write(PhantomPacket.addStolenSkill(jobid, count, skillID, skillLevel));
                    //client.getSession().write(MaplePacketCreator.updateStolenSkills(this, jobid));
                }
            }
        }
    }

    public void removeStolenSkill(int skillID) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[装备]技能正在冷却。请等待.");
            return;
        }
        final int jobid = GameConstants.getJobNumber(skillID / 10000);
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, false);
        int count = -1, cc = 0;
        for (int i = 0; i < stolenSkills.size(); i++) {
            if (stolenSkills.get(i).left == skillID) {
                if (stolenSkills.get(i).right) {
                    unchooseStolenSkill(skillID);
                }
                count = cc;
                break;
            } else if (GameConstants.getJobNumber(stolenSkills.get(i).left / 10000) == jobid) {
                cc++;
            }
        }
        if (count >= 0) {
            cancelStolenSkill(skillID);
            stolenSkills.remove(dummy);
            dummy.right = true;
            stolenSkills.remove(dummy);
            changed_skills = true;
            changeSkillLevel_Skip(SkillFactory.getSkill(skillID), 0, (byte) 0);
            //hacky process begins here
            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(jobid), 0));
            for (int i = 0; i < GameConstants.getNumSteal(jobid); i++) {
                client.getSession().write(PhantomPacket.removeStolenSkill(jobid, i));
            }
            count = 0;
            for (Pair<Integer, Boolean> sk : stolenSkills) {
                if (GameConstants.getJobNumber(sk.left / 10000) == jobid) {
                    client.getSession().write(PhantomPacket.addStolenSkill(jobid, count, sk.left, getSkillLevel(sk.left)));
                    if (sk.right) {
                        client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(jobid), sk.left));
                    }
                    count++;
                }
            }
            client.getSession().write(PhantomPacket.removeStolenSkill(jobid, count));
            //client.getSession().write(MaplePacketCreator.updateStolenSkills(this, jobid));
        }
    }

    public List<Pair<Integer, Boolean>> getStolenSkills() {
        return stolenSkills;
    }

    public void changeSkillLevel_Skip(Skill skil, int skilLevel, byte masterLevel) {
        final Map<Skill, SkillEntry> enry = new HashMap<>(1);
        enry.put(skil, new SkillEntry(skilLevel, masterLevel, -1L));
        changeSkillLevel_Skip(enry, true);
    }

    public void setHonourExp(int exp) {
        this.honourExp = exp;
    }

    public int getHonourExp() {
        return honourExp;
    }

    public void setHonorLevel(int level) {
        this.honorLevel = level;
    }

    public int getHonorLevel() {
        if (honorLevel == 0) {
            honorLevel++;
        }
        return honorLevel;
    }

    public List<InnerSkillValueHolder> getInnerSkills() {
        return innerSkills;
    }

    public void addHonorExp(int amount, boolean show) {
        if (getHonorLevel() < 1) {
            setHonorLevel(1);
        }
        if (getHonourExp() + amount >= getHonorLevel() * 500) {
            honourLevelUp();
            int leftamount = (getHonourExp() + amount) - ((getHonorLevel() - 1) * 500);
            leftamount = Math.min(leftamount, ((getHonorLevel()) * 500) - 1);
            setHonourExp(leftamount);
            return;
        }
        setHonourExp(getHonourExp() + amount);
        client.getSession().write(CWvsContext.updateAzwanFame(getHonorLevel(), getHonourExp(), true));
        client.getSession().write(CWvsContext.updateSpecialStat("honorLeveling", 0, getHonorLevel(), getHonourNextExp()));
        if (show) {
            dropMessage(5, "你得到的 " + amount + " Honor EXP.");
        }
    }

    public int getHonourNextExp() {
        if (getHonorLevel() == 0) {
            return 0;
        }
        return (getHonorLevel() + 1) * 500;
    }

    public void honourLevelUp() {
        setHonorLevel(getHonorLevel() + 1);
        client.getSession().write(CWvsContext.updateAzwanFame(getHonorLevel(), getHonourExp(), true));
        client.getSession().write(CWvsContext.updateSpecialStat("honorLeveling", 0, getHonorLevel(), getHonourNextExp()));
        InnerSkillValueHolder inner = null;
        byte ability = 0;
        switch (getHonorLevel()) {
            case 2:
                ability = 1;
                break;
            case 30:
                ability = 2;
                break;
            case 70:
                ability = 3;
                break;
        }
        if (getHonorLevel() == 2 || getHonorLevel() == 30 || getHonorLevel() == 70 && inner != null && ability >= 1 && ability <= 3) {
            inner = new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, false);
            innerSkills.add(inner);
            changeSkillLevel(SkillFactory.getSkill(inner.getSkillId()), inner.getSkillLevel(), inner.getSkillLevel());
            client.getSession().write(CField.getCharInfo(this));
            client.getSession().write(CField.updateInnerPotential(ability, inner.getSkillId(), inner.getSkillLevel(), inner.getRank()));
        }
    }

    public void gainHonor(int honor, boolean show) {
        addHonorExp(honor, false);
        if (show) {
            client.getSession().write(InfoPacket.showInfo(honor + " Honor EXP obtained."));
        }
    }

    public void azwanReward(final int map, final int portal) {
        client.getSession().write(CField.UIPacket.sendAzwanResult());
        client.getSession().write(CWvsContext.enableActions());
        MapTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                changeMap(map, portal);
            }
        }, 5 * 1000);
    }

    public int getPQLog(String pqid) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int ret_count = 0;
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT COUNT(*) FROM pqlog WHERE charid = ? and pqid = ? and lastattempt >= subtime(current_timestamp, '1 0:0:0.0')");
            ps.setInt(1, id);
            ps.setString(2, pqid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = -1;
                }
            }
            ps.close();
            return ret_count;
        } catch (SQLException Ex) {
            System.err.println("getPQLog" + Ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", Ex);
            return -1;
        }
    }

//    public int getGiftLog(String pqid) {
//        Connection con1 = DatabaseConnection.getConnection();
//        try {
//            int ret_count = 0;
//            PreparedStatement ps;
//            ps = con1.prepareStatement("select count(*) from pqlog where accountid = ? and pqid = ? and lastattempt >= subtime(current_timestamp, '1 0:0:0.0')");
//            ps.setInt(1, accountid);
//            ps.setString(2, pqid);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) 
//                ret_count = rs.getInt(1);
//            else
//                ret_count = -1;
//            rs.close();
//            ps.close();
//            return ret_count;
//        } catch (Exception Ex) {
//            return -1;
//        }
//    }
    //setPQLog module
    public void setPQLog(String pqid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps;
            ps = con.prepareStatement("INSERT INTO pqlog (accid, charid, pqid) values (?,?,?)");
            ps.setInt(1, accountid);
            ps.setInt(2, id);
            ps.setString(3, pqid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("setPQLog" + Ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", Ex);
        }
    }

    public void changeSkillLevel(Skill skill, byte newLevel, byte newMasterlevel) {
        changeSkillLevel_Skip(skill, newLevel, newMasterlevel);
    }

    public void setKeyValue(String key, String values) {
        if (CustomValues.containsKey(key)) {
            CustomValues.remove(key);
        }
        CustomValues.put(key, values);
        keyvalue_changed = true;
    }

    public String getKeyValue(String key) {
        if (CustomValues.containsKey(key)) {
            return CustomValues.get(key);
        }
        return null;
    }

    public void setChatType(short chattype) {
        this.chattype = chattype;
    }

    public short getChatType() {
        return chattype;
    }

    public void gainItem(int code, int amount) {
        MapleInventoryManipulator.addById(client, code, (short) amount, null);
    }

    public final int[] getFriendShipPoints() {
        return friendshippoints;
    }

    public final void setFriendShipPoints(int joejoe, int hermoninny, int littledragon, int ika) {
        this.friendshippoints[0] = joejoe;
        this.friendshippoints[1] = hermoninny;
        this.friendshippoints[2] = littledragon;
        this.friendshippoints[3] = ika;
    }

    public final int getFriendShipToAdd() {
        return friendshiptoadd;
    }

    public final void setFriendShipToAdd(int points) {
        this.friendshiptoadd = points;
    }

    public final void addFriendShipToAdd(int points) {
        this.friendshiptoadd += points;
    }

    public void makeNewAzwanShop() {
        /*
         * Azwan Etc Scrolls 60% - 30 conqueror coins Azwan Weapon Scrolls 60% -
         * 50 conqueror coins 9 Conqueror's Coin - 1 emperor coin Circulator -
         * rank * 1 emperor coins Azwan Scrolls 50% - 3 emperor coins Azwan
         * Scrolls 40% - 5 emperor coins Azwan Scrolls 30% - 7 emperor coins
         * Azwan Scrolls 20% - 10 emperor coins Emperor Armor Recipes - 70
         * emperor coins Emperor Accessory & Weapon Recipes - 75 emperor coins
         * Emperor Weapon - 300 emperor coins Emperor Armor - 250 emperor coins
         * Duke Accessory - 50 emperor coins Duke Accessory - 50 emperor coins
         * 10 Sunrise Dew - 3 conqueror coins 10 Simset Dew - 4 conqueror coins
         * 10 Reindeer Milk - 2 conqueror coins 100% Scrolls - 3 conqueror coins
         */
        azwanShopList = new MapleShop(100000000 + getId(), 2182002);
        int itemid = GameConstants.getAzwanRecipes()[(int) Math.floor(Math.random() * GameConstants.getAzwanRecipes().length)];
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310038, 75, (byte) 0, 0, 0, 0, false));
        itemid = GameConstants.getAzwanScrolls()[(int) Math.floor(Math.random() * GameConstants.getAzwanScrolls().length)];
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310036, 15, (byte) 0, 0, 0, 0, false));
        itemid = (Integer) GameConstants.getUseItems()[(int) Math.floor(Math.random() * GameConstants.getUseItems().length)].getLeft();
        int price = (Integer) GameConstants.getUseItems()[(int) Math.floor(Math.random() * GameConstants.getUseItems().length)].getRight();
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, price, (short) 0, 0, 0, (byte) 0, 0, 0, 0, false));
        itemid = GameConstants.getCirculators()[(int) Math.floor(Math.random() * GameConstants.getCirculators().length)];
        price = InnerAbillity.getInstance().getCirculatorRank(itemid);
        if (price > 10) {
            price = 10;
        }
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310038, price, (byte) 0, 0, 0, 0, false));
        client.getSession().write(CField.getWhisper("Jean Pierre", client.getChannel(), "Psst! I got some new items in stock! Come take a look! Oh, but if your Honor Level increased, why not wait until you get a Circulator?"));
    }

    public MapleShop getAzwanShop() {
        return azwanShopList;
    }

    public void openAzwanShop() {
        if (azwanShopList == null) {
            MapleShopFactory.getInstance().getShop(2182002).sendShop(client);
        } else {
            getAzwanShop().sendShop(client);
        }
    }

    public void sendWelcome() {
        getClient().getSession().write(CField.startMapEffect("欢迎光临 " + getClient().getChannelServer().getServerName() + "!", 5122000, true));
        dropMessage(1, "欢迎光临 " + getClient().getChannelServer().getServerName() + ", " + getName() + " ! \r\n使用 @npc 一旦你10级，收集你的升值项目! \r\n使用 @help 命令. \r\n祝你好运，玩得开心!");
        dropMessage(5, "你的经验值将设置为1直到你完成本教程.");
        dropMessage(5, "使用 @npc 一旦你10级，收集你的升值项目! 使用 @help 命令。祝你好运，玩得开心!");
        dropMessage(1, "使用 @help 额外的命令.");
    }

    private static void addMedalString(final MapleCharacter c, final StringBuilder sb) {
        final Item medal = c.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -46);
        if (medal != null) { // Medal
            sb.append("<");
            if (medal.getItemId() == 1142257 && GameConstants.isAdventurer(c.getJob())) {
                MapleQuestStatus stat = c.getQuestNoAdd(MapleQuest.getInstance(GameConstants.ULT_EXPLORER));
                if (stat != null && stat.getCustomData() != null) {
                    sb.append(stat.getCustomData());
                    sb.append("'s Successor");
                } else {
                    sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
                }
            } else {
                sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
            }
            sb.append("> ");
        }
    }

    public void updateReward() {
        List<MapleReward> rewards = new LinkedList();
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM rewards WHERE `cid`=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    //rewards.last();
                    //int size = rewards.getRow();
                    //rewards.first();
                    //client.getSession().write(Reward.updateReward(rewards.getInt("id"), (byte) 9, rewards, size, 9));
                    while (rs.next()) {
                        rewards.add(new MapleReward(
                                rs.getInt("id"),
                                rs.getLong("start"),
                                rs.getLong("end"),
                                rs.getInt("type"),
                                rs.getInt("itemId"),
                                rs.getInt("mp"),
                                rs.getInt("meso"),
                                rs.getInt("exp"),
                                rs.getString("desc")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to update rewards: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        client.getSession().write(Reward.updateReward(0, (byte) 9, rewards, 9));
    }

    public MapleReward getReward(int id) {
        MapleReward reward = null;
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM rewards WHERE `id` = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        reward = new MapleReward(rs.getInt("id"), rs.getLong("start"), rs.getLong("end"), rs.getInt("type"), rs.getInt("itemId"), rs.getInt("mp"), rs.getInt("meso"), rs.getInt("exp"), rs.getString("desc"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain reward information: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return reward;
    }

    public void addReward(int type, int item, int mp, int meso, int exp, String desc) {
        addReward(0L, 0L, type, item, mp, meso, exp, desc);
    }

    public void addReward(long start, long end, int type, int item, int mp, int meso, int exp, String desc) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO rewards (`cid`, `start`, `end`, `type`, `itemId`, `mp`, `meso`, `exp`, `desc`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, id);
                ps.setLong(2, start);
                ps.setLong(3, end);
                ps.setInt(4, type);
                ps.setInt(5, item);
                ps.setInt(6, mp);
                ps.setInt(7, meso);
                ps.setInt(8, exp);
                ps.setString(9, desc);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain reward: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public void deleteReward(int id) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM rewards WHERE `id` = ?")) {
                ps.setInt(1, id);
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("Unable to delete reward: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        updateReward();
    }

    public void checkCustomReward(int level) {
        List<Integer> rewards = new LinkedList<>();
        int mp = 0;
        switch (level) {
            case 10:
                rewards.add(2450000);
                rewards.add(2022918);
                mp = 1000;
                break;
            case 20:
                rewards.add(2022918);
                rewards.add(1032099);
                mp = 3000;
                break;
            case 30:
                addReward(4, 0, 0, 5000000, 0, "Here is a special reward for beginners to help you start your journey!");//was 1000000
                rewards.add(2450000);
                rewards.add(1112659);
                mp = 5000;
                break;
            case 50:
                rewards.add(2022918);
                rewards.add(1003361);
                rewards.add(1082399);
                rewards.add(1102337);
                mp = 7500;
                break;
            case 70:
                rewards.add(2450000);
                rewards.add(2022918);
                rewards.add(1003016);
                mp = 10000;
                break;
            case 100:
                addReward(4, 0, 0, 50000000, 0, "Here is a special reward for the experts! Beta only tho.");//just for beta
                rewards.add(2450000);
                rewards.add(2022918);
                rewards.add(1122195);
                rewards.add(1132043);
                rewards.add(1152084);
                mp = 12500;
                break;
            case 120:
                rewards.add(2450000);
                rewards.add(1182007);
                mp = 15000;
                break;
            case 150:
                rewards.add(1142349);
                mp = 17500;
                break;
            case 170:
                rewards.add(1142295);
                mp = 20000;
                break;
            case 200:
                rewards.add(1142456);
                mp = 25000;
                break;
        }
        for (int reward : rewards) {
            addReward(1, reward, 0, 0, 0, "Here is a special reward for reaching Lv. " + level + "!");
        }
        if (mp != 0) {
            addReward(3, 0, mp, 0, 0, "Here is a special reward for reaching Lv. " + level + "!");
        }
        updateReward();
    }

    public void newCharRewards() {
        List<Integer> rewards = new LinkedList<>();
        rewards.add(2022680);
        rewards.add(2450031);
        rewards.add(1142358);
        for (int reward : rewards) {
            addReward(1, reward, 0, 0, 0, "Here is a special reward for beginners to help you start your journey!");
        }
        updateReward();
    }

    public boolean canVote() {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM ipvotes WHERE `accid` = ?")) {
                ps.setInt(1, getAccountID());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if ((rs.getLong("lastvote") - (System.currentTimeMillis() / 1000)) > 6 * 60 * 60) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain vote information: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        return false;
    }

    public void setVote() {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ipvotes WHERE `accid` = ?")) {
                ps.setInt(1, getAccountID());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("Unable to delete vote entry: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
        try (Connection con1 = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con1.prepareStatement("INSERT INTO ipvotes (`ip`, `accid`, `lastvote`) VALUES (?, ?, ?)")) {
                ps.setString(1, getClient().getSession().getRemoteAddress().toString().split(":")[0].replaceAll("/", ""));
                ps.setInt(2, getAccountID());
                ps.setLong(3, System.currentTimeMillis() / 1000);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Unable to update votes: " + e);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", e);
        }
    }

    public void removeBGLayers() {
        for (byte i = 0; i < 127; i++) {
            client.getSession().write(CField.removeBGLayer(true, 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }

    public void showBGLayers() {
        for (byte i = 0; i < 127; i++) {
            client.getSession().write(CField.removeBGLayer(false, 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }
    public static final int CUSTOM_BG = 61000000;

    public int getCustomBGState() {
        return getIntNoRecord(CUSTOM_BG);
    }

    public void toggleCustomBGState() {
        getQuestNAdd(MapleQuest.getInstance(CUSTOM_BG)).setCustomData(String.valueOf(getCustomBGState() == 1 ? 0 : 1));
        for (byte i = 0; i < 127; i++) {
            WriteFuture write = client.getSession().write(CField.removeBGLayer((getCustomBGState() == 1), 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }

    public static void addLinkSkill(int cid, int skill) {

        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, cid);
                if (GameConstants.isApplicableSkill(skill)) { //do not save additional skills
                    ps.setInt(2, skill);
                    ps.setInt(3, 1);
                    ps.setByte(4, (byte) 1);
                    ps.setLong(5, -1);
                    ps.execute();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Failed adding link skill: " + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
    }

    public int getWheelItem() {
        return wheelItem;
    }

    public void setWheelItem(int wheelItem) {
        this.wheelItem = wheelItem;
    }

    public static void removePartTime(int cid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM parttime where cid = ?")) {
                ps.setInt(1, cid);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Failed to remove part time job: " + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
    }

    public static void addPartTime(PartTimeJob partTime) {
        if (partTime.getCharacterId() < 1) {
            return;
        }
        addPartTime(partTime.getCharacterId(), partTime.getJob(), partTime.getTime(), partTime.getReward());
    }

    public static void addPartTime(int cid, byte job, long time, int reward) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO parttime (cid, job, time, reward) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, cid);
                ps.setByte(2, job);
                ps.setLong(3, time);
                ps.setInt(4, reward);
                ps.execute();
            }
        } catch (SQLException ex) {
            System.out.println("Failed to add part time job: " + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
    }

    public static PartTimeJob getPartTime(int cid) {
        PartTimeJob partTime = new PartTimeJob(cid);
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM parttime WHERE cid = ?")) {
                ps.setInt(1, cid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        partTime.setJob(rs.getByte("job"));
                        partTime.setTime(rs.getLong("time"));
                        partTime.setReward(rs.getInt("reward"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Failed to retrieve part time job: " + ex);
            FileoutputUtil.outputFileError("logs/数据库异常.txt", ex);
        }
        return partTime;
    }

    public void handleKaiserCombo() {
        if (kaiserCombo < 1000) {
            kaiserCombo += 1;
        }
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiser_Combo(this, kaiserCombo);
    }

    public void resetKaiserCombo() {
        kaiserCombo = 0;
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiser_Combo(this, kaiserCombo);
    }

    public void changeLuminousMode(final int skillid) {
        final boolean equilibrium = skillid == 20040220;
        final boolean eclipse = skillid == 20040217;
        final boolean sunfire = skillid == 20040216;
        final MapleCharacter chr = this;
        if (equilibrium || (!eclipse && !sunfire)) {
            return; //impossible
        }
        dispelBuff(skillid);
        luminousState = skillid;
        if (sunfire) {
            runningLight--;
        } else if (eclipse) {
            runningDark--;
        }
        client.getSession().write(LuminousPacket.updateLuminousGauge(runningDarkSlot, runningLightSlot, runningDark, runningLight));
        luminousState = 20040200;
        client.getSession().write(LuminousPacket.giveLuminousState(20040220, chr.getLightGauge(), chr.getDarkGauge(), 0));
        SkillFactory.getSkill(20040220).getEffect(1).applyTo(this);
        equipChanged();
        WorldTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                dispelBuff(20040220);
                client.getSession().write(LuminousPacket.giveLuminousState(skillid, chr.getLightGauge(), chr.getDarkGauge(), 0));
                SkillFactory.getSkill(skillid).getEffect(1).applyTo(chr);
            }
        }, 10000);
    }

    public void handleLuminous(int skillid) {
        int[] lightSkills = new int[]{27001100, 27101100, 27101101, 27111100, 27111101, 27121100};
        int[] darkSkills = new int[]{27001201, 27101202, 27111202, 27121201, 27121202, 27120211};
        boolean found = false;
        for (int light : lightSkills) {
            if (skillid == light) {
                runningLightSlot += Randomizer.nextInt(200) + 100;
                if (runningLightSlot > 10000) {
                    runningLightSlot = 0;
                    runningLight += 1;
                    if (runningLight > 5) {
                        runningLight = 5;
                    }
                }
                found = true;
            }
        }
        for (int dark : darkSkills) {
            if (skillid == dark) {
                runningDarkSlot += Randomizer.nextInt(200) + 100;
                if (runningDarkSlot > 10000) {
                    runningDarkSlot = 0;
                    runningDark += 1;
                    if (runningDark > 5) {
                        runningDark = 5;
                    }
                }
                found = true;
            }
        }
        if (!found) {
            return;
        }

        client.getSession().write(LuminousPacket.updateLuminousGauge(runningDarkSlot, runningLightSlot, runningDark, runningLight));
    }

    public void resetRunningStack() {
        this.runningStack = 0;
        this.runningDark = 0;
        this.runningDarkSlot = 0;
        this.runningLight = 0;
        this.runningLightSlot = 0;
    }

    public int getRunningStack() {
        return this.runningStack;
    }

    public void addRunningStack(int s) {
        runningStack += s;
    }

    public int getLightGauge() {
        return runningLightSlot;
    }

    public int getDarkGauge() {
        return runningDarkSlot;
    }

    public int getLuminousState() {
        return luminousState;
    }

    public void setLuminousState(int luminousState) {
        this.luminousState = luminousState;
    }

    public void setCardStack(byte amount) {
        this.cardStack = amount;
    }

    public byte getCardStack() {
        return this.cardStack;
    }

    public void applyBlackBlessingBuff(int combos) {
        if ((combos == -1) && (this.runningBless == 0)) {
            //combos = 0;
            return;
        }
        Skill skill = SkillFactory.getSkill(27100003);
        int lvl = getTotalSkillLevel(27100003);
        if (lvl > 0) {
            runningBless = ((byte) (runningBless + combos));
            if (runningBless > 3) {
                runningBless = 3;
            }
            if (runningBless == 0) {
                if (getBuffedValue(MapleBuffStat.BLACK_BLESSING) != null) {
                    cancelBuffStats(new MapleBuffStat[]{MapleBuffStat.BLACK_BLESSING});
                }
            } else {
                skill.getEffect(lvl).applyBlackBlessingBuff(this, runningBless);
            }
        }
    }

    public short getXenonSurplus() {
        return xenonSurplus;
    }

    public void setXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (xenonSurplus + amount > maxSupply) {
            this.xenonSurplus = (short) maxSupply;
            updateXenonSurplus(xenonSurplus);
            return;
        }
        this.xenonSurplus = amount;
        updateXenonSurplus(xenonSurplus);
    }

    public void gainXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (xenonSurplus + amount > maxSupply) {
            this.xenonSurplus = (short) maxSupply;
            updateXenonSurplus(xenonSurplus);
            return;
        }
        this.xenonSurplus += amount;
        updateXenonSurplus(xenonSurplus);
    }

    public void updateXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (amount > maxSupply) {
            amount = (short) maxSupply;
        }
        client.getSession().write(XenonPacket.giveXenonSupply(amount));
    }

    public final void startXenonSupply() {
        BuffTimer tMan = BuffTimer.getInstance();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
                if (maxSupply > getXenonSurplus()) {
                    gainXenonSurplus((short) 1);
                }
            }
        };
        if (client.isLoggedIn()) {
            XenonSupplyTask = tMan.register(r, 4000);
        }
    }

    public short getExceed() {
        return exceed;
    }

    public void setExceed(short amount) {
        this.exceed = amount;
    }

    public void gainExceed(short amount) {
        this.exceed += amount;
        updateExceed(exceed);
    }

    public void updateExceed(short amount) {
        client.getSession().write(AvengerPacket.giveExceed(amount));
    }

    public void handleExceedAttack(int skill) {
        long now = System.currentTimeMillis();
        if (lastExceedTime + 15000 < now) {
            exceedAttack = 0;
            lastExceedTime = now;
        }
        client.getSession().write(AvengerPacket.giveExceedAttack(skill, ++exceedAttack));
    }

    public MapleCoreAura getCoreAura() {
        return coreAura;
    }

    public void changeWarriorStance(final int skillid) {
        if (skillid == 11101022) {
            dispelBuff(11111022);
            /*
             * List<MapleBuffStat> statups = new LinkedList();
             * statups.add(MapleBuffStat.INDIE_BOOSTER);
             * statups.add(MapleBuffStat.DAMAGE_PERCENT);
             * statups.add(MapleBuffStat.WARRIOR_STANCE);
             * //client.getSession().write(BuffPacket.cancelBuff(statups));
             */
            client.getSession().write(JobPacket.DawnWarriorPacket.giveMoonfallStance(getSkillLevel(skillid)));
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
        } else if (skillid == 11111022) {
            dispelBuff(11101022);
            /*
             * List<MapleBuffStat> statups = new LinkedList();
             * statups.add(MapleBuffStat.MOON_STANCE1);
             * statups.add(MapleBuffStat.MOON_STANCE2);
             * statups.add(MapleBuffStat.WARRIOR_STANCE);
             * //client.getSession().write(BuffPacket.cancelBuff(statups));
             */
            client.getSession().write(JobPacket.DawnWarriorPacket.giveSunriseStance(getSkillLevel(skillid)));
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
            /*
             * } else if (skillid == 11121005) { //equinox } else if (skillid ==
             * 11121011) { dispelBuff(11101022);
             * client.getSession().write(DawnWarriorPacket.giveEquinox_Moon(getSkillLevel(skillid),
             * Integer.MAX_VALUE));
             * SkillFactory.getSkill(skillid).getEffect(1).applyTo(this); } else
             * if (skillid == 11121012) { dispelBuff(11101022);
             * client.getSession().write(DawnWarriorPacket.giveEquinox_Sun(getSkillLevel(skillid),
             * Integer.MAX_VALUE));
             * SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
             */
        }
    }

    public List<MaplePotionPot> getPotionPots() {
        return potionPots;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public void updateDeathCount() {
        if (deathCount < 0) {
            deathCount = 0;
        }
        if (deathCount > 99) {
            deathCount = 99;
        }
//        client.getSession().write(EffectPacket.updateDeathCount(deathCount));
    }

    public void giveMiracleBlessing() {
        for (MapleCharacter chr : getMap().getCharactersThreadsafe()) {
            MapleItemInformationProvider.getInstance().getItemEffect(2023055).applyTo(chr);
        }
        getMap().broadcastMessage(CField.startMapEffect(name + " has received Double Miracle Time's Mysterious Blessing. Congratulations!", 2023055, true));
        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0x19, 0, name + " has received [Double Miracle Time's Miraculous Blessing]. Congratulations!", false));
    }

    public void checkForceShield() {
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
        Equip equip;
        boolean potential = false;
        switch (job) {
            case 508:
                equip = (Equip) li.getEquipById(1352300);
                break;
            case 572:
                potential = true;
            case 570:
            case 571:
                equip = (Equip) li.getEquipById(1352301 + job % 10);
                break;
            case 3001:
                equip = (Equip) li.getEquipById(1099000);
                break;
            case 3112:
                potential = true;
            case 3100:
            case 3110:
            case 3111:
                equip = (Equip) li.getEquipById(1099001 + job % 10 + ((job % 100) / 10));
                break;
            case 5112:
                potential = true;
            case 5100:
            case 5110:
            case 5111:
                equip = (Equip) li.getEquipById(1098000 + job % 10 + ((job % 100) / 10));
                break;
            case 6001:
                equip = (Equip) li.getEquipById(1352600);
                break;
            case 6512:
                potential = true;
            case 6500:
            case 6510:
            case 6511:
                equip = (Equip) li.getEquipById(1352601 + job % 10 + ((job % 100) / 10));
                break;
            default:
                equip = null;
        }
        if (equip != null) {
            if (potential) {
                equip.resetPotential();
            }
            equip.setPosition((short) -10);
            equip.setQuantity((short) 1);
            equip.setGMLog("Job Advance");
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) -10));
            equipChanged();
        }
    }

    public void checkZeroWeapon() {
        if (level < 100) {
            return;
        }
        int lazuli = getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId();
        int lapis = getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10).getItemId();
        if (lazuli == getZeroWeapon(false) && lapis == getZeroWeapon(true)) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (int i = 0; i < 2; i++) {
            int itemId = i == 0 ? getZeroWeapon(false) : getZeroWeapon(true);
            Equip equip = (Equip) ii.getEquipById(itemId);
            equip.setPosition((short) (i == 0 ? -11 : -10));
            equip.setQuantity((short) 1);
            equip.setGMLog("Zero Weapon Advance");
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) (i == 0 ? -11 : -10)));
        }
        equipChanged();
    }

    public int getZeroWeapon(boolean lapis) {
        if (level < 100) {
            return lapis ? 1562000 : 1572000;
        }
        int weapon = lapis ? 1562001 : 1572001;
        if (level >= 100 && level < 160) {
            weapon += (level % 100) / 10;
        } else if (level >= 160 && level < 170) {
            weapon += 5;
        } else if (level >= 170) {
            weapon += 6;
        }
        return weapon;
    }

    public void zeroChange(boolean beta) {
        getMap().broadcastMessage(this, CField.updateCharLook(this, beta), getPosition());
    }

    public void checkZeroTranscendent() {
        int skill = -1;
        switch (level / 10) {
            case 10:
                skill = 100000267;
                break;
            case 11:
                skill = 100001261;
                break;
            case 12:
                skill = 100001274;
                break;
            case 14:
                skill = 100001272;
                break;
            case 17:
                skill = 100001283;
                break;
            case 20:
                skill = 100001005;
                break;
        }
        if (skill == -1) {
            return;
        }
        Skill skil = SkillFactory.getSkill(skill);
        if (skil != null) {
            changeSkillLevel_Skip(skil, 1, (byte) 1);
        }
    }

    public boolean hasEntered(String script) {
        for (int mapId : entered.keySet()) {
            if (entered.get(mapId).equals(script)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEntered(String script, int mapId) {
        if (entered.containsKey(mapId)) {
            if (entered.get(mapId).equals(script)) {
                return true;
            }
        }
        return false;
    }

    public void enteredScript(String script, int mapid) {
        if (!entered.containsKey(mapid)) {
            entered.put(mapid, script);
        }
    }

    public void femaleMihile() {
        setGender((byte) 1);
        setFace(21158); //Elegant Lady face sounds nice
        setHair(34773); //Aria hair fits there most
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip equip;
        int[][] items = new int[][]{{1051355, -5}, {1072833, -6}};
        for (int[] item : items) {
            equip = (Equip) ii.getEquipById(item[0]);
            equip.setPosition((short) item[1]);
            equip.setQuantity((short) 1);
            equip.setGMLog("Female Mihile");
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) item[1]));
        }
        updateSingleStat(MapleStat.FACE, this.face);
        updateSingleStat(MapleStat.HAIR, this.hair);
        equipChanged();
    }

    public void updateSkills() {
        final Map<Skill, SkillEntry> sa = new HashMap<>();
        sa.put(SkillFactory.getSkill(110001506), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001514), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001510), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001500), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001502), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(112100000), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(112000000), new SkillEntry((byte) 1, (byte) 1, -1));
        changeKeybinding(42, (byte) 1, 110001510);
        client.getSession().write(CField.getKeymap(getKeyLayout()));
        client.getPlayer().changeSkillsLevel(sa);
    }

    public Collection<MapleMonster> getControlledMonsters() {
        return Collections.unmodifiableCollection(controlled);
    }

    public void startMapEffect(String msg, int itemId) {
        startMapEffect(msg, itemId, 10000);
    }

    public void startMapEffect(String msg, int itemId, int duration) {
        final MapleMapEffect mapEffect = new MapleMapEffect(msg, itemId);
        getClient().getSession().write(mapEffect.makeStartData());
        EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                getClient().getSession().write(mapEffect.makeDestroyData());
            }
        }, duration);
    }

    public void maxSkills() {
        for (Skill sk : SkillFactory.getAllSkills()) {
            if (sk.getId() != 12) {
                changeSingleSkillLevel(sk, (int) sk.getMaxLevel(), (byte) sk.getMaxLevel());
            }
        }
    }

    public void minSkills() {
        for (Skill sk : SkillFactory.getAllSkills()) {
            changeSingleSkillLevel(sk, (int) 0, (byte) 0);
        }
    }

    public void startLieDetector(boolean isItem) {
        if (!getAntiMacro().inProgress()) {
            getAntiMacro().startLieDetector(getName(), isItem, false);
        }
    }

    public void sendEnglishQuiz(String msg) {
        client.getSession().write(CWvsContext.englishQuizMsg(msg));
    }

    public void setDebugMessage(boolean set) {
        DebugMessage = set;
    }

    public boolean getDebugMessage() {
        return DebugMessage;
    }

    public int getBeans() {
        return beans;
    }

    public void setBeans(int b) {
        beans = b;
    }

    public void gainBeans(int s) {
        beans += s;
    }

    public int getBeansNum() {
        return beansNum;
    }

    public void setBeansNum(int beansNum) {
        this.beansNum = beansNum;
    }

    public int getBeansRange() {
        return beansRange;
    }

    public void setBeansRange(int beansRange) {
        this.beansRange = beansRange;
    }

    public boolean isCanSetBeansNum() {
        return canSetBeansNum;
    }

    public void setCanSetBeansNum(boolean canSetBeansNum) {
        this.canSetBeansNum = canSetBeansNum;
    }

    public boolean changeFace(short item, int color) {
        int newFace = ((face / 1000) * 1000) + color + (face % 10);
        if (!MapleItemInformationProvider.faceExists(newFace)) {
            newFace = face;
            gainItem(item, 1);
        } else {
            face = newFace;
            updateSingleStat(MapleStat.FACE, newFace);
            equipChanged();
        }
        return true;
    }

    public static String getAriantRoomLeaderName(int room) {
        return ariantroomleader[room];
    }

    public static int getAriantSlotsRoom(int room) {
        return ariantroomslot[room];
    }

    public static void removeAriantRoom(int room) {
        ariantroomleader[room] = "";
        ariantroomslot[room] = 0;
    }

    public static void setAriantRoomLeader(int room, String charname) {
        ariantroomleader[room] = charname;
    }

    public static void setAriantSlotRoom(int room, int slot) {
        ariantroomslot[room] = slot;
    }

    public void addAriantScore() {
        APQScore++;
    }

    public void resetAriantScore() {
        APQScore = 0;
    }

    public int getAriantScore() { // TODO: code entire score system
        return APQScore;
    }

    public void updateAriantScore() {
        List<MapleCharacter> chrs = getMap().getCharacters();
        getMap().broadcastMessage(CField.updateAriantScore(chrs));
    }

    public void setApprentice(int app) {
        this.apprentice = app;
    }

    public boolean hasApprentice() {
        if (apprentice > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getMaster() {
        return this.master;
    }

    public int getApprentice() {
        return this.apprentice;
    }

    public MapleCharacter getApp() {
        return client.getChannelServer().getPlayerStorage().getCharacterById(this.apprentice);
    }

    public MapleCharacter getMster() {
        return client.getChannelServer().getPlayerStorage().getCharacterById(this.master);
    }

    public void setMaster(int mstr) {
        this.master = mstr;
    }

    public int getAveragePartyLevel() {
        int averageLevel = 0, size = 0;
        for (MaplePartyCharacter pl : getParty().getMembers()) {
            averageLevel += pl.getLevel();
            size++;
        }
        if (size <= 0) {
            return level;
        }
        averageLevel /= size;
        return averageLevel;
    }

    public int getAverageMapLevel() {
        int averageLevel = 0, size = 0;
        for (MapleCharacter pl : getMap().getCharacters()) {
            averageLevel += pl.getLevel();
            size++;
        }
        if (size <= 0) {
            return level;
        }
        averageLevel /= size;
        return averageLevel;
    }

    public void setMonsterBookCover(int bookCover) {
        this.bookCover = bookCover;
    }

    public int getMonsterBookCover() {
        return bookCover;
    }

    public void setAPQScore(int score) {
        this.APQScore = score;
    }

    public int getAPQScore() {
        return APQScore;
    }

    public void addMobVac() {
        MobVac++;
    }

    public int getMobVac() {
        return MobVac;
    }

    public int getMegaTimes() {
        return mega_times;
    }

    public void addMegaTimes() {
        mega_times++;
    }

    public int getBossLog(String bossid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select count(*) from bosslog where characterid = ? and bossid = ? and lastattempt >= subtime(current_timestamp, '1 0:0:0.0')");
            ps.setInt(1, id);
            ps.setString(2, bossid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = -1;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            //log.error("Error while read bosslog.", Ex);
            return -1;
        }
    }

    public void setBossLog(String bossid) {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            PreparedStatement ps;
            ps = con.prepareStatement("insert into bosslog (characterid, bossid) values (?,?)");
            ps.setInt(1, id);
            ps.setString(2, bossid);
            ps.executeUpdate();
            ps.close();
        } catch (Exception Ex) {
            //   log.error("Error while insert bosslog.", Ex);
        }
    }

    public int getPDTime() {
        try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
            int ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select count(*) from bosslog where characterid = ? and bossid = ? and lastattempt >= DATE_SUB(curdate(),INTERVAL 0 DAY)");
            ps.setInt(1, id);
            ps.setString(2, "pd");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = 0;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            return -1;
        }
    }
    
    /**
     * 高级百宝箱
     *
     * @return
     */
    public Map<String, Integer> seniorBox() {
        return ServerConstants.seniorBox.next();
    }

    /**
     * 初级百宝箱
     *
     * @return
     */
    public Map<String, Integer> primaryBox() {
        return ServerConstants.primaryBox.next();
    }

    public void setSeniorBox(Map<String, Integer> s) {
        seniorBox = s;
    }

    public Map<String, Integer> getSeniorBox() {
        return seniorBox;
    }

    public void setPrimaryBox(Map<String, Integer> p) {
        primaryBox = p;
    }

    public Map<String, Integer> getPrimaryBox() {
        return primaryBox;
    }
}
