package handling.world;

import client.BuddylistEntry;
import client.CardData;
import client.CharacterNameAndId;
import client.MapleCharacter;
import client.MapleMarriage;
import client.MapleQuestStatus;
import client.MapleTrait.MapleTraitType;
import client.MonsterFamiliar;
import client.Skill;
import client.SkillEntry;
import client.anticheat.ReportType;
import client.inventory.Item;
import client.inventory.MapleImp;
import client.inventory.MapleMount;
import client.inventory.MaplePet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import server.quest.MapleQuest;
import tools.Pair;

public class CharacterTransfer implements Externalizable {
    
    public int characterid, accountid, fame, pvpExp, pvpPoints, hair, face,
            faceMarking, ears, tail, elf, mapid, honourexp, honourlevel, guildid,
            partyid, messengerid, ACash, nxCredit, MaplePoints,
            mount_itemid, mount_exp, points, vpoints, dpoints, epoints, marriageId, maxhp, maxmp, hp, mp,
            familyid, seniorid, junior1, junior2, currentrep, totalrep, battleshipHP, gachexp, guildContribution, totalWins, totalLosses, exp, meso, mBookCover,
            mega_times;
    public byte channel, gender, gmLevel, guildrank, alliancerank, clonez,
            fairyExp, cardStack, buddysize, world, initialSpawnPoint, skinColor, mount_level, mount_Fatigue, subcategory;
    public long lastfametime, TranferTime;
    public String name, accountname, BlessOfFairy, BlessOfEmpress, chalkboard, tempIP;
    public short level, hpApUsed, job, fatigue;
    public Object inventorys, skillmacro, storage, cs, anticheat, antiMacro, innerSkills, azwanShopList;
    public int[] savedlocation, wishlist, rocks, remainingSp, remainingHSp, regrocks, hyperrocks;
    public byte[] petStore;
    public MapleImp[] imps;
    public Map<Integer, Integer> mbook;
    public Map<Byte, Integer> reports = new LinkedHashMap<>();
    public List<Pair<Integer, Boolean>> stolenSkills;
    public Map<Integer, Pair<Byte, Integer>> keymap;
    public Map<Integer, MonsterFamiliar> familiars;
    public List<Integer> finishedAchievements = null, finishedMoonlightAchievements = null, famedcharacters = null, battledaccs = null, extendedSlots = null;
    public List<Item> rebuy = null;
    public final Map<MapleTraitType, Integer> traits = new EnumMap<>(MapleTraitType.class);
    public final Map<CharacterNameAndId, Boolean> buddies = new LinkedHashMap<>();
    public final Map<Integer, Object> Quest = new LinkedHashMap<>(); // Questid instead of MapleQuest, as it's huge. Cant be transporting MapleQuest.java
    public Map<Integer, String> InfoQuest;
    public final Map<Integer, SkillEntry> Skills = new LinkedHashMap<>(); // Skillid instead of Skill.java, as it's huge. Cant be transporting Skill.java and MapleStatEffect.java
    public final Map<Integer, CardData> cardsInfo = new LinkedHashMap<>();
    public MapleMarriage marriage;
    /*
     * Start of Custom Feature
     */
    /*
     * All custom shit declare here
     */
    public int reborns, apstorage, str, dex, int_, luk, remainingAp, beans;
    public boolean DebugMessage;
    /*
     * End of Custom Feature
     */
    
    public CharacterTransfer() {
        finishedAchievements = new ArrayList<>();
        finishedMoonlightAchievements = new ArrayList<>();
        famedcharacters = new ArrayList<>();
        battledaccs = new ArrayList<>();
        extendedSlots = new ArrayList<>();
        rebuy = new ArrayList<>();
        InfoQuest = new LinkedHashMap<>();
        keymap = new LinkedHashMap<>();
        familiars = new LinkedHashMap<>();
        mbook = new LinkedHashMap<>();
    }
    
    public CharacterTransfer(final MapleCharacter chr) {
        this.mega_times = chr.getMegaTimes();
        this.mBookCover = chr.getMonsterBookCover();
        this.beans = chr.getBeans();
        this.DebugMessage = chr.getDebugMessage();
        this.characterid = chr.getId();
        this.accountid = chr.getAccountID();
        this.accountname = chr.getClient().getAccountName();
        this.channel = (byte) chr.getClient().getChannel();
        this.nxCredit = chr.getCSPoints(1);
        this.ACash = chr.getCSPoints(4);
        this.MaplePoints = chr.getCSPoints(2);
        this.stolenSkills = chr.getStolenSkills();
        this.vpoints = chr.getVPoints();
        this.name = chr.getName();
        this.fame = chr.getFame();
        this.gender = chr.getGender();
        this.level = chr.getLevel();
        this.str = chr.getStat().getStr();
        this.dex = chr.getStat().getDex();
        this.int_ = chr.getStat().getInt();
        this.luk = chr.getStat().getLuk();
        this.hp = chr.getStat().getHp();
        this.mp = chr.getStat().getMp();
        this.maxhp = chr.getStat().getMaxHp();
        this.maxmp = chr.getStat().getMaxMp();
        this.exp = chr.getExp();
        this.hpApUsed = chr.getHpApUsed();
        this.remainingAp = chr.getRemainingAp();
        this.remainingSp = chr.getRemainingSps();
        this.remainingHSp = chr.getRemainingHSps();
        this.meso = chr.getMeso();
        this.pvpExp = chr.getTotalBattleExp();
        this.pvpPoints = chr.getBattlePoints();
        /*
         * Start of Custom Feature
         */
        this.reborns = chr.getReborns();
        this.apstorage = chr.getAPS();
        /*
         * End of Custom Feature
         */
        this.skinColor = chr.getSkinColor();
        this.job = chr.getJob();
        this.hair = chr.getHair();
        this.face = chr.getFace();
        this.faceMarking = chr.getFaceMarking();
        this.ears = chr.getEars();
        this.tail = chr.getTail();
        this.elf = chr.getElf();
        this.mapid = chr.getMapId();
        this.initialSpawnPoint = chr.getInitialSpawnpoint();
        this.marriageId = chr.getMarriageId();
        this.marriage = chr.getMarriage();
        this.world = chr.getWorld();
        this.guildid = chr.getGuildId();
        this.guildrank = chr.getGuildRank();
        this.guildContribution = chr.getGuildContribution();
        this.alliancerank = chr.getAllianceRank();
        this.gmLevel = (byte) chr.getGMLevel();
        this.points = chr.getPoints();
        this.dpoints = chr.getDPoints();
        this.epoints = chr.getEPoints();
        this.fairyExp = chr.getFairyExp();
        this.cardStack = chr.getCardStack();
        this.clonez = chr.getNumClones();
        this.petStore = chr.getPetStores();
        this.subcategory = chr.getSubcategory();
        this.imps = chr.getImps();
        this.fatigue = chr.getFatigue();
        this.currentrep = chr.getCurrentRep();
        this.totalrep = chr.getTotalRep();
        this.familyid = chr.getFamilyId();
        this.totalWins = chr.getTotalWins();
        this.totalLosses = chr.getTotalLosses();
        this.seniorid = chr.getSeniorId();
        this.junior1 = chr.getJunior1();
        this.junior2 = chr.getJunior2();
        this.battleshipHP = chr.currentBattleshipHP();
        this.gachexp = chr.getGachExp();
        this.familiars = chr.getFamiliars();
        chr.getCheatTracker().dispose();
        this.anticheat = chr.getCheatTracker();
        this.antiMacro = chr.getAntiMacro();
        this.tempIP = chr.getClient().getTempIP();
        this.rebuy = chr.getRebuy();
        boolean uneq = false;
        for (int i = 0; i < this.petStore.length; i++) {
            final MaplePet pet = chr.getPet(i);
            if (this.petStore[i] == 0) {
                this.petStore[i] = (byte) -1;
            }
            if (pet != null) {
                uneq = true;
                this.petStore[i] = (byte) Math.max(this.petStore[i], pet.getInventoryPosition());
            }
            
        }
        if (uneq) {
            chr.unequipAllPets();
        }
        
        for (MapleTraitType t : MapleTraitType.values()) {
            this.traits.put(t, chr.getTrait(t).getTotalExp());
        }
        for (final BuddylistEntry qs : chr.getBuddylist().getBuddies()) {
            this.buddies.put(new CharacterNameAndId(qs.getCharacterId(), qs.getName(), qs.getGroup()), qs.isVisible());
        }
        for (final Entry<ReportType, Integer> ss : chr.getReports().entrySet()) {
            this.reports.put(ss.getKey().i, ss.getValue());
        }
        this.buddysize = chr.getBuddyCapacity();
        
        this.partyid = chr.getParty() == null ? -1 : chr.getParty().getId();
        
        if (chr.getMessenger() != null) {
            this.messengerid = chr.getMessenger().getId();
        } else {
            this.messengerid = 0;
        }
        
        this.finishedAchievements = chr.getFinishedAchievements();
        
        this.finishedMoonlightAchievements = chr.getFinishedMoonlightAchievements();
        
        this.InfoQuest = chr.getInfoQuest_Map();
        
        for (final Map.Entry<MapleQuest, MapleQuestStatus> qs : chr.getQuest_Map().entrySet()) {
            this.Quest.put(qs.getKey().getId(), qs.getValue());
        }
        
        this.mbook = chr.getMonsterBook().getCards();
        this.inventorys = chr.getInventorys();
        
        for (final Map.Entry<Skill, SkillEntry> qs : chr.getSkills().entrySet()) {
            this.Skills.put(qs.getKey().getId(), qs.getValue());
        }
        for (final Map.Entry<Integer, CardData> ii : chr.getCharacterCard().getCards().entrySet()) {
            this.cardsInfo.put(ii.getKey(), ii.getValue());
        }
        
        this.BlessOfFairy = chr.getBlessOfFairyOrigin();
        this.BlessOfEmpress = chr.getBlessOfEmpressOrigin();
        this.chalkboard = chr.getChalkboard();
        this.skillmacro = chr.getMacros();
        this.keymap = chr.getKeyLayout().Layout();
        this.savedlocation = chr.getSavedLocations();
        this.wishlist = chr.getWishlist();
        this.rocks = chr.getRocks();
        this.regrocks = chr.getRegRocks();
        this.hyperrocks = chr.getHyperRocks();
        this.famedcharacters = chr.getFamedCharacters();
        this.battledaccs = chr.getBattledCharacters();
        this.lastfametime = chr.getLastFameTime();
        this.storage = chr.getStorage();
        this.cs = chr.getCashInventory();
        this.extendedSlots = chr.getExtendedSlots();
        this.honourexp = chr.getHonourExp();
        this.honourlevel = chr.getHonorLevel();
        this.innerSkills = chr.getInnerSkills();
        this.azwanShopList = chr.getAzwanShop();
        
        final MapleMount mount = chr.getMount();
        this.mount_itemid = mount.getItemId();
        this.mount_Fatigue = mount.getFatigue();
        this.mount_level = mount.getLevel();
        this.mount_exp = mount.getExp();
        TranferTime = System.currentTimeMillis();
    }
    
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.mega_times = in.readInt();
        this.mBookCover = in.readInt();
        this.beans = in.readInt();
        this.DebugMessage = in.readBoolean();
        this.characterid = in.readInt();
        this.accountid = in.readInt();
        this.accountname = in.readUTF();
        this.channel = in.readByte();
        this.nxCredit = in.readInt();
        this.ACash = in.readInt();
        this.MaplePoints = in.readInt();
        this.name = in.readUTF();
        this.fame = in.readInt();
        this.gender = in.readByte();
        this.level = in.readShort();
        this.str = in.readShort();
        this.dex = in.readShort();
        this.int_ = in.readShort();
        this.luk = in.readShort();
        this.hp = in.readInt();
        this.mp = in.readInt();
        this.maxhp = in.readInt();
        this.maxmp = in.readInt();
        this.exp = in.readInt();
        this.hpApUsed = in.readShort();
        this.remainingAp = in.readShort();
        this.remainingSp = new int[in.readByte()];
        for (int i = 0; i < this.remainingSp.length; i++) {
            this.remainingSp[i] = in.readInt();
        }
        this.remainingHSp = new int[in.readByte()];
        for (int i = 0; i < this.remainingHSp.length; i++) {
            this.remainingHSp[i] = in.readInt();
        }
        this.meso = in.readInt();
        this.skinColor = in.readByte();
        this.job = in.readShort();
        this.hair = in.readInt();
        this.face = in.readInt();
        this.faceMarking = in.readInt();
        this.ears = in.readInt();
        this.tail = in.readInt();
        this.elf = in.readInt();
        this.mapid = in.readInt();
        this.initialSpawnPoint = in.readByte();
        this.world = in.readByte();
        this.guildid = in.readInt();
        this.guildrank = in.readByte();
        this.guildContribution = in.readInt();
        this.alliancerank = in.readByte();
        this.gmLevel = in.readByte();
        this.points = in.readInt();
        this.vpoints = in.readInt();
        this.dpoints = in.readInt();
        this.epoints = in.readInt();
        if (in.readByte() == 1) {
            this.BlessOfFairy = in.readUTF();
        } else {
            this.BlessOfFairy = null;
        }
        if (in.readByte() == 1) {
            this.BlessOfEmpress = in.readUTF();
        } else {
            this.BlessOfEmpress = null;
        }
        if (in.readByte() == 1) {
            this.chalkboard = in.readUTF();
        } else {
            this.chalkboard = null;
        }
        this.clonez = in.readByte();
        this.skillmacro = in.readObject();
        this.lastfametime = in.readLong();
        this.storage = in.readObject();
        this.cs = in.readObject();
        this.mount_itemid = in.readInt();
        this.mount_Fatigue = in.readByte();
        this.mount_level = in.readByte();
        this.mount_exp = in.readInt();
        this.partyid = in.readInt();
        this.messengerid = in.readInt();
        this.inventorys = in.readObject();
        this.fairyExp = in.readByte();
        this.cardStack = in.readByte();
        this.subcategory = in.readByte();
        this.fatigue = in.readShort();
        this.marriageId = in.readInt();
        this.marriage = new MapleMarriage(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readUTF(), in.readUTF());
        this.familyid = in.readInt();
        this.seniorid = in.readInt();
        this.junior1 = in.readInt();
        this.junior2 = in.readInt();
        this.currentrep = in.readInt();
        this.totalrep = in.readInt();
        this.battleshipHP = in.readInt();
        this.gachexp = in.readInt();
        this.totalWins = in.readInt();
        this.totalLosses = in.readInt();
        this.anticheat = in.readObject();
        this.antiMacro = in.readObject();
        this.tempIP = in.readUTF();
        this.honourexp = in.readInt();
        this.honourlevel = in.readInt();
        this.innerSkills = in.readObject();
        this.azwanShopList = in.readObject();
        this.pvpExp = in.readInt();
        this.pvpPoints = in.readInt();
        /*
         * Start of Custom Feature
         */
        this.reborns = in.readInt();
        this.apstorage = in.readInt();
        /*
         * End of Custom Feature
         */
        
        final int mbooksize = in.readShort();
        for (int i = 0; i < mbooksize; i++) {
            this.mbook.put(in.readInt(), in.readInt());
        }
        
        final int skillsize = in.readShort();
        for (int i = 0; i < skillsize; i++) {
            this.Skills.put(in.readInt(), new SkillEntry(in.readInt(), in.readByte(), in.readLong()));
        }
        
        final int cardsize = in.readByte();
        for (int i = 0; i < cardsize; i++) {
            this.cardsInfo.put(in.readInt(), new CardData(in.readInt(), in.readShort(), in.readShort()));
        }
        
        this.buddysize = in.readByte();
        final short addedbuddysize = in.readShort();
        for (int i = 0; i < addedbuddysize; i++) {
            buddies.put(new CharacterNameAndId(in.readInt(), in.readUTF(), in.readUTF()), in.readBoolean());
        }
        
        final int questsize = in.readShort();
        for (int i = 0; i < questsize; i++) {
            this.Quest.put(in.readInt(), in.readObject());
        }
        
        final int rzsize = in.readByte();
        for (int i = 0; i < rzsize; i++) {
            this.reports.put(in.readByte(), in.readInt());
        }
        
        final int achievesize = in.readByte();
        for (int i = 0; i < achievesize; i++) {
            this.finishedAchievements.add(in.readInt());
        }
        
        final int moonlightachievesize = in.readByte();
        for (int i = 0; i < moonlightachievesize; i++) {
            this.finishedMoonlightAchievements.add(in.readInt());
        }
        
        final int famesize = in.readByte(); //max 31
        for (int i = 0; i < famesize; i++) {
            this.famedcharacters.add(in.readInt());
        }
        
        final int battlesize = in.readInt();
        for (int i = 0; i < battlesize; i++) {
            this.battledaccs.add(in.readInt());
        }
        
        final int esize = in.readByte();
        for (int i = 0; i < esize; i++) {
            this.extendedSlots.add(in.readInt());
        }
        
        final int savesize = in.readByte();
        savedlocation = new int[savesize];
        for (int i = 0; i < savesize; i++) {
            savedlocation[i] = in.readInt();
        }
        
        final int wsize = in.readByte();
        wishlist = new int[wsize];
        for (int i = 0; i < wsize; i++) {
            wishlist[i] = in.readInt();
        }
        
        final int rsize = in.readByte();
        rocks = new int[rsize];
        for (int i = 0; i < rsize; i++) {
            rocks[i] = in.readInt();
        }
        
        final int resize = in.readByte();
        regrocks = new int[resize];
        for (int i = 0; i < resize; i++) {
            regrocks[i] = in.readInt();
        }
        
        final int hesize = in.readByte();
        hyperrocks = new int[resize];
        for (int i = 0; i < hesize; i++) {
            hyperrocks[i] = in.readInt();
        }
        
        final int infosize = in.readShort();
        for (int i = 0; i < infosize; i++) {
            this.InfoQuest.put(in.readInt(), in.readUTF());
        }
        
        final int keysize = in.readInt();
        for (int i = 0; i < keysize; i++) {
            this.keymap.put(in.readInt(), new Pair<>(in.readByte(), in.readInt()));
        }
        
        final int fsize = in.readShort();
        for (int i = 0; i < fsize; i++) {
            this.familiars.put(in.readInt(), new MonsterFamiliar(this.characterid, in.readInt(), in.readInt(), in.readLong(), in.readUTF(), in.readInt(), in.readByte()));
        }
        
        this.petStore = new byte[in.readByte()];
        for (int i = 0; i < this.petStore.length; i++) {
            this.petStore[i] = in.readByte();
        }
        
        final int rebsize = in.readShort();
        for (int i = 0; i < rebsize; i++) {
            this.rebuy.add((Item) in.readObject());
        }
        
        this.imps = new MapleImp[in.readByte()];
        for (int x = 0; x < this.imps.length; x++) {
            if (in.readByte() > 0) {
                MapleImp i = new MapleImp(in.readInt());
                i.setFullness(in.readShort());
                i.setCloseness(in.readShort());
                i.setState(in.readByte());
                i.setLevel(in.readByte());
                
                this.imps[x] = i;
            }
        }
        for (MapleTraitType value : MapleTraitType.values()) {
            this.traits.put(MapleTraitType.values()[in.readByte()], in.readInt());
        }
        TranferTime = System.currentTimeMillis();
    }
    
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.mega_times);
        out.writeInt(this.mBookCover);
        out.writeInt(this.beans);
        out.writeBoolean(this.DebugMessage);
        out.writeInt(this.characterid);
        out.writeInt(this.accountid);
        out.writeUTF(this.accountname);
        out.writeByte(this.channel);
        out.writeInt(this.nxCredit);
        out.writeInt(this.ACash);
        out.writeInt(this.MaplePoints);
        out.writeUTF(this.name);
        out.writeInt(this.fame);
        out.writeByte(this.gender);
        out.writeShort(this.level);
        out.writeShort(this.str);
        out.writeShort(this.dex);
        out.writeShort(this.int_);
        out.writeShort(this.luk);
        out.writeInt(this.hp);
        out.writeInt(this.mp);
        out.writeInt(this.maxhp);
        out.writeInt(this.maxmp);
        out.writeInt(this.exp);
        out.writeShort(this.hpApUsed);
        out.writeShort(this.remainingAp);
        out.writeByte(this.remainingSp.length);
        for (int i = 0; i < this.remainingSp.length; i++) {
            out.writeInt(this.remainingSp[i]);
        }
        out.writeByte(this.remainingHSp.length);
        for (int i = 0; i < this.remainingHSp.length; i++) {
            out.writeInt(this.remainingHSp[i]);
        }
        out.writeInt(this.meso);
        out.writeByte(this.skinColor);
        out.writeShort(this.job);
        out.writeInt(this.hair);
        out.writeInt(this.face);
        out.writeInt(this.faceMarking);
        out.writeInt(this.ears);
        out.writeInt(this.tail);
        out.writeInt(this.elf);
        out.writeInt(this.mapid);
        out.writeByte(this.initialSpawnPoint);
        out.writeByte(this.world);
        out.writeInt(this.guildid);
        out.writeByte(this.guildrank);
        out.writeInt(this.guildContribution);
        out.writeByte(this.alliancerank);
        out.writeByte(this.gmLevel);
        out.writeInt(this.points);
        out.writeInt(this.vpoints);
        out.writeInt(this.dpoints);
        out.writeInt(this.epoints);
        out.writeByte(this.BlessOfFairy == null ? 0 : 1);
        if (this.BlessOfFairy != null) {
            out.writeUTF(this.BlessOfFairy);
        }
        out.writeByte(this.BlessOfEmpress == null ? 0 : 1);
        if (this.BlessOfEmpress != null) {
            out.writeUTF(this.BlessOfEmpress);
        }
        out.writeByte(this.chalkboard == null ? 0 : 1);
        if (this.chalkboard != null) {
            out.writeUTF(this.chalkboard);
        }
        out.writeByte(this.clonez);
        
        out.writeObject(this.skillmacro);
        out.writeLong(this.lastfametime);
        out.writeObject(this.storage);
        out.writeObject(this.cs);
        out.writeInt(this.mount_itemid);
        out.writeByte(this.mount_Fatigue);
        out.writeByte(this.mount_level);
        out.writeInt(this.mount_exp);
        out.writeInt(this.partyid);
        out.writeInt(this.messengerid);
        out.writeObject(this.inventorys);
        out.writeByte(this.fairyExp);
        out.writeByte(this.cardStack);
        out.writeByte(this.subcategory);
        out.writeShort(this.fatigue);
        out.writeInt(this.marriageId);
        if (this.marriage == null) {
            out.writeInt(0);
            out.writeInt(0);
            out.writeInt(0);
            out.writeInt(0);
            out.writeUTF("");
            out.writeUTF("");
        } else {
            out.writeInt(marriage.getId());
            out.writeInt(marriage.getRing());
            out.writeInt(marriage.getHusbandId());
            out.writeInt(marriage.getWifeId());
            out.writeUTF(marriage.getHusbandName());
            out.writeUTF(marriage.getWifeName());
        }
        out.writeInt(this.familyid);
        out.writeInt(this.seniorid);
        out.writeInt(this.junior1);
        out.writeInt(this.junior2);
        out.writeInt(this.currentrep);
        out.writeInt(this.totalrep);
        out.writeInt(this.battleshipHP);
        out.writeInt(this.gachexp);
        out.writeInt(this.totalWins);
        out.writeInt(this.totalLosses);
        out.writeObject(this.anticheat);
        out.writeUTF(this.tempIP);
        out.writeInt(this.pvpExp);
        out.writeInt(this.pvpPoints);
        out.writeObject(this.antiMacro);
        out.writeInt(this.honourexp);
        out.writeInt(this.honourlevel);
        out.writeObject(this.innerSkills);
        out.writeObject(this.azwanShopList);
        /*
         * Start of Custom Feature
         */
        out.writeInt(this.reborns);
        out.writeInt(this.apstorage);
        /*
         * End of Custom Feature
         */
        
        out.writeShort(this.mbook.size());
        for (Map.Entry<Integer, Integer> ms : this.mbook.entrySet()) {
            out.writeInt(ms.getKey());
            out.writeInt(ms.getValue());
        }
        
        out.writeShort(this.Skills.size());
        for (final Map.Entry<Integer, SkillEntry> qs : this.Skills.entrySet()) {
            out.writeInt(qs.getKey()); // Questid instead of Skill, as it's huge :(
            out.writeInt(qs.getValue().skillevel);
            out.writeByte(qs.getValue().masterlevel);
            out.writeLong(qs.getValue().expiration);
            // Bless of fairy is transported here too.
        }
        out.writeByte(this.cardsInfo.size());
        for (final Map.Entry<Integer, CardData> qs : this.cardsInfo.entrySet()) {
            out.writeInt(qs.getKey());
            out.writeInt(qs.getValue().cid);
            out.writeShort(qs.getValue().level);
            out.writeShort(qs.getValue().job);
        }
        
        out.writeByte(this.buddysize);
        out.writeShort(this.buddies.size());
        for (final Map.Entry<CharacterNameAndId, Boolean> qs : this.buddies.entrySet()) {
            out.writeInt(qs.getKey().getId());
            out.writeUTF(qs.getKey().getName());
            out.writeUTF(qs.getKey().getGroup());
            out.writeBoolean(qs.getValue());
        }
        
        out.writeShort(this.Quest.size());
        for (final Map.Entry<Integer, Object> qs : this.Quest.entrySet()) {
            out.writeInt(qs.getKey()); // Questid instead of MapleQuest, as it's huge :(
            out.writeObject(qs.getValue());
        }
        
        out.writeByte(this.reports.size());
        for (Entry<Byte, Integer> ss : reports.entrySet()) {
            out.writeByte(ss.getKey());
            out.writeInt(ss.getValue());
        }
        
        out.writeByte(this.finishedAchievements.size());
        for (final Integer zz : finishedAchievements) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.finishedMoonlightAchievements.size());
        for (final Integer zz : finishedMoonlightAchievements) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.famedcharacters.size());
        for (final Integer zz : famedcharacters) {
            out.writeInt(zz);
        }
        
        out.writeInt(this.battledaccs.size());
        for (final Integer zz : battledaccs) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.extendedSlots.size());
        for (final Integer zz : extendedSlots) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.savedlocation.length);
        for (int zz : savedlocation) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.wishlist.length);
        for (int zz : wishlist) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.rocks.length);
        for (int zz : rocks) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.regrocks.length);
        for (int zz : regrocks) {
            out.writeInt(zz);
        }
        
        out.writeByte(this.hyperrocks.length);
        for (int zz : hyperrocks) {
            out.writeInt(zz);
        }
        
        out.writeShort(this.InfoQuest.size());
        for (final Map.Entry<Integer, String> qs : this.InfoQuest.entrySet()) {
            out.writeInt(qs.getKey());
            out.writeUTF(qs.getValue());
        }
        
        out.writeInt(this.keymap.size());
        for (final Map.Entry<Integer, Pair<Byte, Integer>> qs : this.keymap.entrySet()) {
            out.writeInt(qs.getKey());
            out.writeByte(qs.getValue().left);
            out.writeInt(qs.getValue().right);
        }
        
        out.writeShort(this.familiars.size());
        for (final Map.Entry<Integer, MonsterFamiliar> qs : this.familiars.entrySet()) {
            out.writeInt(qs.getKey());
            final MonsterFamiliar f = qs.getValue();
            out.writeInt(f.getId());
            out.writeInt(f.getFamiliar());
            out.writeLong(f.getExpiry());
            out.writeUTF(f.getName());
            out.writeInt(f.getFatigue());
            out.writeByte(f.getVitality());
        }
        
        out.writeByte(petStore.length);
        for (int i = 0; i < petStore.length; i++) {
            out.writeByte(petStore[i]);
        }
        
        out.writeShort(rebuy.size());
        for (int i = 0; i < rebuy.size(); i++) {
            out.writeObject(rebuy.get(i));
        }
        
        out.writeByte(this.imps.length);
        for (MapleImp imp : this.imps) {
            if (imp != null) {
                out.writeByte(1);
                out.writeInt(imp.getItemId());
                out.writeShort(imp.getFullness());
                out.writeShort(imp.getCloseness());
                out.writeByte(imp.getState());
                out.writeByte(imp.getLevel());
            } else {
                out.writeByte(0);
            }
        }
        
        for (Entry<MapleTraitType, Integer> ts : this.traits.entrySet()) {
            out.writeByte(ts.getKey().ordinal());
            out.writeInt(ts.getValue());
        }
    }
}
