package client;

import java.awt.Point;
import java.io.File;
import java.util.*;
import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.Randomizer;
import tools.StringUtil;
import tools.Triple;

public class SkillFactory {

    private static final Map<Integer, Skill> skills = new HashMap<>();
    private static final Map<String, Integer> delays = new HashMap<>();
    private static final Map<Integer, CraftingEntry> crafts = new HashMap<>();
    private static final Map<Integer, FamiliarEntry> familiars = new HashMap<>();
    private static final Map<Integer, List<Integer>> skillsByJob = new HashMap<>();
    private static final Map<Integer, SummonSkillEntry> SummonSkillInformation = new HashMap<>();

    public static void load() {
        final MapleData delayData = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Character.wz")).getData("00002000.img");
        final MapleData stringData = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/String.wz")).getData("Skill.img");
        final MapleDataProvider datasource = MapleDataProviderFactory.getDataProvider(new File((System.getProperty("wzpath") != null ? System.getProperty("wzpath") : "") + "wz/Skill.wz"));
        final MapleDataDirectoryEntry root = datasource.getRoot();
        int del = 0; //buster is 67 but its the 57th one!
        for (MapleData delay : delayData) {
            if (!delay.getName().equals("info")) {
                delays.put(delay.getName(), del);
                del++;
            }
        }

        int skillid;
        MapleData summon_data;
        SummonSkillEntry sse;

        for (MapleDataFileEntry topDir : root.getFiles()) { // Loop thru jobs
            if (topDir.getName().length() <= 9) {
                for (MapleData data : datasource.getData(topDir.getName())) { // Loop thru each jobs
                    if (data.getName().equals("skill")) {
                        for (MapleData data2 : data) { // Loop thru each jobs
                            if (data2 != null) {
                                skillid = Integer.parseInt(data2.getName());
                                Skill skil = Skill.loadFromData(skillid, data2, delayData);
                                List<Integer> job = skillsByJob.get(skillid / 10000);
                                if (job == null) {
                                    job = new ArrayList<>();
                                    skillsByJob.put(skillid / 10000, job);
                                }
                                job.add(skillid);
                                skil.setName(getName(skillid, stringData));
                                skills.put(skillid, skil);

                                summon_data = data2.getChildByPath("summon/attack1/info");
                                if (summon_data != null) {
                                    sse = new SummonSkillEntry();
                                    sse.type = (byte) MapleDataTool.getInt("type", summon_data, 0);
                                    sse.mobCount = (byte) (skillid == 33101008 ? 3 : MapleDataTool.getInt("mobCount", summon_data, 1));
                                    sse.attackCount = (byte) MapleDataTool.getInt("attackCount", summon_data, 1);
                                    if (summon_data.getChildByPath("range/lt") != null) {
                                        final MapleData ltd = summon_data.getChildByPath("range/lt");
                                        sse.lt = (Point) ltd.getData();
                                        sse.rb = (Point) summon_data.getChildByPath("range/rb").getData();
                                    } else {
                                        sse.lt = new Point(-100, -100);
                                        sse.rb = new Point(100, 100);
                                    }
                                    //sse.range = (short) MapleDataTool.getInt("range/r", summon_data, 0);
                                    sse.delay = MapleDataTool.getInt("effectAfter", summon_data, 0) + MapleDataTool.getInt("attackAfter", summon_data, 0);
                                    for (MapleData effect : summon_data) {
                                        if (effect.getChildren().size() > 0) {
                                            for (final MapleData effectEntry : effect) {
                                                sse.delay += MapleDataTool.getIntConvert("delay", effectEntry, 0);
                                            }
                                        }
                                    }
                                    for (MapleData effect : data2.getChildByPath("summon/attack1")) {
                                        sse.delay += MapleDataTool.getIntConvert("delay", effect, 0);
                                    }
                                    SummonSkillInformation.put(skillid, sse);
                                }
                            }
                        }
                    }
                }
            } else if (topDir.getName().startsWith("Familiar")) {
                for (MapleData data : datasource.getData(topDir.getName())) {
                    skillid = Integer.parseInt(data.getName());
                    FamiliarEntry skil = new FamiliarEntry();
                    skil.prop = (byte) MapleDataTool.getInt("prop", data, 0);
                    skil.time = (byte) MapleDataTool.getInt("time", data, 0);
                    skil.attackCount = (byte) MapleDataTool.getInt("attackCount", data, 1);
                    skil.targetCount = (byte) MapleDataTool.getInt("targetCount", data, 1);
                    skil.speed = (byte) MapleDataTool.getInt("speed", data, 1);
                    skil.knockback = MapleDataTool.getInt("knockback", data, 0) > 0 || MapleDataTool.getInt("attract", data, 0) > 0;
                    if (data.getChildByPath("lt") != null) {
                        skil.lt = (Point) data.getChildByPath("lt").getData();
                        skil.rb = (Point) data.getChildByPath("rb").getData();
                    }
                    if (MapleDataTool.getInt("stun", data, 0) > 0) {
                        skil.status.add(MonsterStatus.STUN);
                    }
                    //if (MapleDataTool.getInt("poison", data, 0) > 0) {
                    //	status.add(MonsterStatus.POISON);
                    //}
                    if (MapleDataTool.getInt("slow", data, 0) > 0) {
                        skil.status.add(MonsterStatus.SPEED);
                    }
                    familiars.put(skillid, skil);
                }
            } else if (topDir.getName().startsWith("Recipe")) {
                for (MapleData data : datasource.getData(topDir.getName())) {
                    skillid = Integer.parseInt(data.getName());
                    CraftingEntry skil = new CraftingEntry(skillid, (byte) MapleDataTool.getInt("incFatigability", data, 0), (byte) MapleDataTool.getInt("reqSkillLevel", data, 0), (byte) MapleDataTool.getInt("incSkillProficiency", data, 0), MapleDataTool.getInt("needOpenItem", data, 0) > 0, MapleDataTool.getInt("period", data, 0));
                    for (MapleData d : data.getChildByPath("target")) {
                        skil.targetItems.add(new Triple<>(MapleDataTool.getInt("item", d, 0), MapleDataTool.getInt("count", d, 0), MapleDataTool.getInt("probWeight", d, 0)));
                    }
                    for (MapleData d : data.getChildByPath("recipe")) {
                        skil.reqItems.put(MapleDataTool.getInt("item", d, 0), MapleDataTool.getInt("count", d, 0));
                    }
                    crafts.put(skillid, skil);
                }
            }
        }
    }

    public static List<Integer> getSkillsByJob(final int jobId) {
        return skillsByJob.get(jobId);
    }

    public static String getSkillName(final int id) {
        Skill skil = getSkill(id);
        if (skil != null) {
            return skil.getName();
        }
        return null;
    }

    public static Integer getDelay(final String id) {
        if (Delay.fromString(id) != null) {
            return Delay.fromString(id).i;
        }
        return delays.get(id);
    }

    private static String getName(final int id, final MapleData stringData) {
        String strId = Integer.toString(id);
        strId = StringUtil.getLeftPaddedStr(strId, '0', 7);
        MapleData skillroot = stringData.getChildByPath(strId);
        if (skillroot != null) {
            return MapleDataTool.getString(skillroot.getChildByPath("name"), "");
        }
        return "";
    }

    public static SummonSkillEntry getSummonData(final int skillid) {
        return SummonSkillInformation.get(skillid);
    }

    public static Collection<Skill> getAllSkills() {
        return skills.values();
    }

    public static Skill getSkill(final int id) {
        if (!skills.isEmpty()) {
            if (id >= 92000000 && id < 100000000 && crafts.containsKey(id)) { //92000000
                return crafts.get(id);
            }
            return skills.get(id);
        }
        return null;
    }

    public static long getDefaultSExpiry(final Skill skill) {
        if (skill == null) {
            return -1;
        }
        return (skill.isTimeLimited() ? (System.currentTimeMillis() + 30L * 24L * 60L * 60L * 1000L) : -1);
    }

    public static CraftingEntry getCraft(final int id) {
        if (!crafts.isEmpty()) {
            return crafts.get(id);
        }

        return null;
    }

    public static FamiliarEntry getFamiliar(final int id) {
        if (!familiars.isEmpty()) {
            return familiars.get(id);
        }

        return null;
    }

    public static class CraftingEntry extends Skill {
        //reqSkillProficiency -> always seems to be 0

        public boolean needOpenItem;
        public int period;
        public byte incFatigability, reqSkillLevel, incSkillProficiency;
        public List<Triple<Integer, Integer, Integer>> targetItems = new ArrayList<>(); // itemId / amount / probability
        public Map<Integer, Integer> reqItems = new HashMap<>(); // itemId / amount

        public CraftingEntry(int id, byte incFatigability, byte reqSkillLevel, byte incSkillProficiency, boolean needOpenItem, int period) {
            super(id);
            this.incFatigability = incFatigability;
            this.reqSkillLevel = reqSkillLevel;
            this.incSkillProficiency = incSkillProficiency;
            this.needOpenItem = needOpenItem;
            this.period = period;
        }
    }

    public static class FamiliarEntry {

        public byte prop, time, attackCount, targetCount, speed;
        public Point lt, rb;
        public boolean knockback;
        public EnumSet<MonsterStatus> status = EnumSet.noneOf(MonsterStatus.class);

        public final boolean makeChanceResult() {
            return prop >= 100 || Randomizer.nextInt(100) < prop;
        }
    }

    public static enum Delay {

        walk1(0x00),
        walk2(0x01),
        stand1(0x02),
        stand2(0x03),
        alert(0x04),
        swingO1(0x05),
        swingO2(0x06),
        swingO3(0x07),
        swingOF(0x08),
        swingT1(0x09),
        swingT2(0x0A),
        swingT3(0x0B),
        swingTF(0x0C),
        swingP1(0x0D),
        swingP2(0x0E),
        swingPF(0x0F),
        stabO1(0x10),
        stabO2(0x11),
        stabOF(0x12),
        stabT1(0x13),
        stabT2(0x14),
        stabTF(0x15),
        swingD1(0x16),
        swingD2(0x17),
        stabD1(0x18),
        swingDb1(0x19),
        swingDb2(0x1A),
        swingC1(0x1B),
        swingC2(0x1C),
        rushBoom(0x1C),
        tripleBlow(0x1D),
        quadBlow(0x1E),
        deathBlow(0x1F),
        finishBlow(0x20),
        finishAttack(0x21),
        finishAttack_link(0x22),
        finishAttack_link2(0x22),
        shoot1(0x23),
        shoot2(0x24),
        shootF(0x25),
        shootDb2(0x28),
        shotC1(0x29),
        dash(0x2B),
        dash2(0x2C), //hack. doesn't really exist
        proneStab(0x2F),
        prone(0x30),
        heal(0x31),
        fly(0x32),
        jump(0x33),
        sit(0x34),
        rope(0x35),
        dead(0x36),
        ladder(0x37),
        rain(0x38),
        alert2(0x40),
        alert3(0x41),
        alert4(0x42),
        alert5(0x43),
        alert6(0x44),
        alert7(0x45),
        ladder2(0x46),
        rope2(0x47),
        shoot6(0x48),
        magic1(0x49),
        magic2(0x4A),
        magic3(0x4B),
        magic5(0x4C),
        magic6(0x4D),
        explosion(0x4D),
        burster1(0x4E),
        burster2(0x4F),
        savage(0x50),
        avenger(0x51),
        assaulter(0x52),
        prone2(0x53),
        assassination(0x54),
        assassinationS(0x55),
        tornadoDash(0x58),
        tornadoDashStop(0x58),
        tornadoRush(0x58),
        rush(0x59),
        rush2(0x5A),
        brandish1(0x5B),
        brandish2(0x5C),
        braveSlash(0x5D),
        braveslash1(0x5D),
        braveslash2(0x5E),
        braveslash3(0x5F),
        braveslash4(0x60),
        darkImpale(0x61),
        sanctuary(0x62),
        meteor(0x63),
        paralyze(0x64),
        blizzard(0x65),
        genesis(0x66),
        blast(0x69),
        smokeshell(0x6A),
        showdown(0x6B),
        ninjastorm(0x6C),
        chainlightning(0x6D),
        holyshield(0x6E),
        resurrection(0x6F),
        somersault(0x70),
        straight(0x71),
        eburster(0x72),
        backspin(0x73),
        eorb(0x74),
        screw(0x75),
        doubleupper(0x76),
        dragonstrike(0x77),
        doublefire(0x78),
        triplefire(0x79),
        fake(0x7A),
        airstrike(0x7B),
        edrain(0x7C),
        octopus(0x7D),
        backstep(0x7E),
        shot(0x7F),
        rapidfire(0x7F),
        fireburner(0x81),
        coolingeffect(0x82),
        fist(0x84),
        timeleap(0x85),
        homing(0x86),
        ghostwalk(0x87),
        ghoststand(0x88),
        ghostjump(0x89),
        ghostproneStab(0x8A),
        ghostladder(0x8B),
        ghostrope(0x8C),
        ghostfly(0x8D),
        ghostsit(0x8E),
        cannon(0x8F),
        torpedo(0x90),
        darksight(0x91),
        bamboo(0x92),
        pyramid(0x93),
        wave(0x94),
        blade(0x95),
        souldriver(0x96),
        firestrike(0x97),
        flamegear(0x98),
        stormbreak(0x99),
        vampire(0x9A),
        swingT2PoleArm(0x9C),
        swingP1PoleArm(0x9D),
        swingP2PoleArm(0x9E),
        doubleSwing(0x9F),
        tripleSwing(0xA0),
        fullSwingDouble(0xA1),
        fullSwingTriple(0xA2),
        overSwingDouble(0xA3),
        overSwingTriple(0xA4),
        rollingSpin(0xA5),
        comboSmash(0xA6),
        comboFenrir(0xA7),
        comboTempest(0xA8),
        finalCharge(0xA9),
        finalBlow(0xAB),
        finalToss(0xAC),
        magicmissile(0xAD),
        lightningBolt(0xAE),
        dragonBreathe(0xAF),
        breathe_prepare(0xB0),
        dragonIceBreathe(0xB1),
        icebreathe_prepare(0xB2),
        blaze(0xB3),
        fireCircle(0xB4),
        illusion(0xB5),
        magicFlare(0xB6),
        elementalReset(0xB7),
        magicRegistance(0xB8),
        magicBooster(0xB9),
        magicShield(0xBA),
        recoveryAura(0xBB),
        flameWheel(0xBC),
        killingWing(0xBD),
        OnixBlessing(0xBE),
        Earthquake(0xBF),
        soulStone(0xC0),
        dragonThrust(0xC1),
        ghostLettering(0xC2),
        darkFog(0xC3),
        slow(0xC4),
        mapleHero(0xC5),
        Awakening(0xC6),
        flyingAssaulter(0xC7),
        tripleStab(0xC8),
        fatalBlow(0xC9),
        slashStorm1(0xCA),
        slashStorm2(0xCB),
        bloodyStorm(0xCC),
        flashBang(0xCD),
        upperStab(0xCE),
        bladeFury(0xCF),
        chainPull(0xD1),
        chainAttack(0xD1),
        owlDead(0xD2),
        monsterBombPrepare(0xD4),
        monsterBombThrow(0xD4),
        finalCut(0xD5),
        finalCutPrepare(0xD5),
        suddenRaid(0xD7), //idk, not in data anymore
        fly2(0xD8),
        fly2Move(0xD9),
        fly2Skill(0xDA),
        knockback(0xDB),
        rbooster_pre(0xDF),
        rbooster(0xDF),
        rbooster_after(0xDF),
        crossRoad(0xE2),
        nemesis(0xE3),
        tank(0xEA),
        tank_laser(0xEE),
        siege_pre(0xF0),
        tank_siegepre(0xF0), //just to make it work with the skill, these two
        sonicBoom(0xF3),
        darkLightning(0xF5),
        darkChain(0xF6),
        cyclone_pre(0),
        cyclone(0), //energy attack
        glacialchain(0xF7),
        flamethrower(0xFB),
        flamethrower_pre(0xFB),
        flamethrower2(0xFC),
        flamethrower_pre2(0xFC),
        gatlingshot(0x101),
        gatlingshot2(0x102),
        drillrush(0x103),
        earthslug(0x104),
        rpunch(0x105),
        clawCut(0x106),
        swallow(0x109),
        swallow_attack(0x109),
        swallow_loop(0x109),
        flashRain(0x111),
        OnixProtection(0x11C),
        OnixWill(0x11D),
        phantomBlow(0x11E),
        comboJudgement(0x11F),
        arrowRain(0x120),
        arrowEruption(0x121),
        iceStrike(0x122),
        swingT2Giant(0x125),
        cannonJump(0x127),
        swiftShot(0x128),
        giganticBackstep(0x12A),
        mistEruption(0x12B),
        cannonSmash(0x12C),
        cannonSlam(0x12D),
        flamesplash(0x12E),
        noiseWave(0x132),
        superCannon(0x136),
        jShot(0x138),
        demonSlasher(0x139),
        bombExplosion(0x13A),
        cannonSpike(0x13B),
        speedDualShot(0x13C),
        strikeDual(0x13D),
        bluntSmash(0x13F),
        crossPiercing(0x140),
        piercing(0x141),
        elfTornado(0x143),
        immolation(0x144),
        multiSniping(0x147),
        windEffect(0x148),
        elfrush(0x149),
        elfrush2(0x149),
        dealingRush(0x14E),
        maxForce0(0x150),
        maxForce1(0x151),
        maxForce2(0x152),
        maxForce3(0x153),
        iceAttack1(0x158),
        iceAttack2(0x159),
        iceSmash(0x15A),
        iceTempest(0x15B),
        iceChop(0x15C),
        icePanic(0x15D),
        iceDoubleJump(0x15E),
        //special: pirate morph attacks
        shockwave(0x169),
        demolition(0x16A),
        snatch(0x16B),
        windspear(0x16C),
        windshot(0x16D);
        public int i;

        private Delay(int i) {
            this.i = i;
        }

        public static Delay fromString(String s) {
            for (Delay b : Delay.values()) {
                if (b.name().equalsIgnoreCase(s)) {
                    return b;
                }
            }
            return null;
        }
    }
}
