package client.inventory;

import constants.EventConstants;
import constants.GameConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.Randomizer;

public class Equip extends Item implements Serializable {

    public static enum ScrollResult {

        SUCCESS,
        FAIL,
        CURSE
    }
    public static final long ARMOR_RATIO = 350000L;
    public static final long WEAPON_RATIO = 700000L;
    //charm: -1 = has not been initialized yet, 0 = already been worn, >0 = has teh charm exp
    private byte upgradeSlots = 0, level = 0, vicioushammer = 0, enhance = 0, enhanctBuff = 0, reqLevel = 0, yggdrasilWisdom = 0, bossDamage = 0, ignorePDR = 0, totalDamage = 0, allStat = 0, karmaCount = -1;
    private short str = 0, dex = 0, _int = 0, luk = 0, hp = 0, mp = 0, watk = 0, matk = 0, wdef = 0, mdef = 0, acc = 0, avoid = 0, hands = 0, speed = 0, jump = 0, charmExp = 0, pvpDamage = 0;
    private int durability = -1, incSkill = -1, potential1 = 0, potential2 = 0, potential3 = 0, potential4 = 0, potential5 = 0, potential6 = 0, potential7 = 0, potential8 = 0, bonuspotential1 = 0, bonuspotential2 = 0, bonuspotential3 = 0, fusionAnvil = 0, socket1 = 0, socket2 = 0, socket3 = 0;
    private long itemEXP = 0;
    private boolean finalStrike = false;
    private MapleRing ring = null;
    private MapleAndroid android = null;
    private List<EquipStat> stats = new LinkedList();
    private List<EquipSpecialStat> specialStats = new LinkedList();
    private Map<EquipStat, Long> statsTest = new LinkedHashMap<>();

    public Equip(int id, short position, byte flag) {
        super(id, position, (short) 1, flag);
    }

    public Equip(int id, short position, int uniqueid, short flag) {
        super(id, position, (short) 1, flag, uniqueid);
    }

    @Override
    public Item copy() {
        Equip ret = new Equip(getItemId(), getPosition(), getUniqueId(), getFlag());
        ret.str = str;
        ret.dex = dex;
        ret._int = _int;
        ret.luk = luk;
        ret.hp = hp;
        ret.mp = mp;
        ret.matk = matk;
        ret.mdef = mdef;
        ret.watk = watk;
        ret.wdef = wdef;
        ret.acc = acc;
        ret.avoid = avoid;
        ret.hands = hands;
        ret.speed = speed;
        ret.jump = jump;
        ret.enhance = enhance;
        ret.upgradeSlots = upgradeSlots;
        ret.level = level;
        ret.itemEXP = itemEXP;
        ret.durability = durability;
        ret.vicioushammer = vicioushammer;
        ret.potential1 = potential1;
        ret.potential2 = potential2;
        ret.potential3 = potential3;
        ret.potential4 = potential4;
        ret.potential5 = potential5;
        ret.bonuspotential1 = bonuspotential1;
        ret.bonuspotential2 = bonuspotential2;
        ret.bonuspotential3 = bonuspotential3;
        ret.fusionAnvil = fusionAnvil;
        ret.socket1 = socket1;
        ret.socket2 = socket2;
        ret.socket3 = socket3;
        ret.charmExp = charmExp;
        ret.pvpDamage = pvpDamage;
        ret.incSkill = incSkill;
        ret.enhanctBuff = enhanctBuff;
        ret.reqLevel = reqLevel;
        ret.yggdrasilWisdom = yggdrasilWisdom;
        ret.finalStrike = finalStrike;
        ret.bossDamage = bossDamage;
        ret.ignorePDR = ignorePDR;
        ret.totalDamage = totalDamage;
        ret.allStat = allStat;
        ret.karmaCount = karmaCount;
        ret.setGiftFrom(getGiftFrom());
        ret.setOwner(getOwner());
        ret.setQuantity(getQuantity());
        ret.setExpiration(getExpiration());
        ret.setEquipOnlyId(getEquipOnlyId());
        ret.stats = stats;
        ret.specialStats = specialStats;
        ret.statsTest = statsTest;
        return ret;
    }

    @Override
    public byte getType() {
        return 1;
    }

    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    public short getStr() {
        return str;
    }

    public short getDex() {
        return dex;
    }

    public short getInt() {
        return _int;
    }

    public short getLuk() {
        return luk;
    }

    public short getHp() {
        return hp;
    }

    public short getMp() {
        return mp;
    }

    public short getWatk() {
        return watk;
    }

    public short getMatk() {
        return matk;
    }

    public short getWdef() {
        return wdef;
    }

    public short getMdef() {
        return mdef;
    }

    public short getAcc() {
        return acc;
    }

    public short getAvoid() {
        return avoid;
    }

    public short getHands() {
        return hands;
    }

    public short getSpeed() {
        return speed;
    }

    public short getJump() {
        return jump;
    }

    public void setStr(short str) {
        if (str < 0) {
            str = 0;
        }
        this.str = str;
    }

    public void setDex(short dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.dex = dex;
    }

    public void setInt(short _int) {
        if (_int < 0) {
            _int = 0;
        }
        this._int = _int;
    }

    public void setLuk(short luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.luk = luk;
    }

    public void setHp(short hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setMp(short mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public void setWatk(short watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.watk = watk;
    }

    public void setMatk(short matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.matk = matk;
    }

    public void setWdef(short wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.wdef = wdef;
    }

    public void setMdef(short mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.mdef = mdef;
    }

    public void setAcc(short acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.acc = acc;
    }

    public void setAvoid(short avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.avoid = avoid;
    }

    public void setHands(short hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.hands = hands;
    }

    public void setSpeed(short speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.speed = speed;
    }

    public void setJump(short jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.jump = jump;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getViciousHammer() {
        return vicioushammer;
    }

    public void setViciousHammer(byte ham) {
        vicioushammer = ham;
    }

    public long getItemEXP() {
        return itemEXP;
    }

    public void setItemEXP(long itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.itemEXP = itemEXP;
    }

    public long getEquipExp() {
        if (itemEXP <= 0) {
            return 0;
        }
        //aproximate value
        if (GameConstants.isWeapon(getItemId())) {
            return itemEXP / WEAPON_RATIO;
        } else {
            return itemEXP / ARMOR_RATIO;
        }
    }

    public long getEquipExpForLevel() {
        if (getEquipExp() <= 0) {
            return 0;
        }
        long expz = getEquipExp();
        for (int i = getBaseLevel(); i <= GameConstants.getMaxLevel(getItemId()); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return expz;
    }

    public long getExpPercentage() {
        if (getEquipLevel() < getBaseLevel() || getEquipLevel() > GameConstants.getMaxLevel(getItemId()) || GameConstants.getExpForLevel(getEquipLevel(), getItemId()) <= 0) {
            return 0;
        }
        return getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(getEquipLevel(), getItemId());
    }

    public int getEquipLevel() {
        if (GameConstants.getMaxLevel(getItemId()) <= 0) {
            return 0;
        } else if (getEquipExp() <= 0) {
            return getBaseLevel();
        }
        int levelz = getBaseLevel();
        long expz = getEquipExp();
        for (int i = levelz; (GameConstants.getStatFromWeapon(getItemId()) == null ? (i <= GameConstants.getMaxLevel(getItemId())) : (i < GameConstants.getMaxLevel(getItemId()))); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                levelz++;
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return levelz;
    }

    public int getBaseLevel() {
        return (GameConstants.getStatFromWeapon(getItemId()) == null ? 1 : 0);
    }

    @Override
    public void setQuantity(short quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(final int dur) {
        durability = dur;
    }

    public byte getEnhanctBuff() {
        return enhanctBuff;
    }

    public void setEnhanctBuff(byte enhanctBuff) {
        this.enhanctBuff = enhanctBuff;
    }

    public byte getReqLevel() {
        return reqLevel;
    }

    public void setReqLevel(byte reqLevel) {
        this.reqLevel = reqLevel;
    }

    public byte getYggdrasilWisdom() {
        return yggdrasilWisdom;
    }

    public void setYggdrasilWisdom(byte yggdrasilWisdom) {
        this.yggdrasilWisdom = yggdrasilWisdom;
    }

    public boolean getFinalStrike() {
        return finalStrike;
    }

    public void setFinalStrike(boolean finalStrike) {
        this.finalStrike = finalStrike;
    }

    public byte getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(byte bossDamage) {
        this.bossDamage = bossDamage;
    }

    public byte getIgnorePDR() {
        return ignorePDR;
    }

    public void setIgnorePDR(byte ignorePDR) {
        this.ignorePDR = ignorePDR;
    }

    public byte getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(byte totalDamage) {
        this.totalDamage = totalDamage;
    }

    public byte getAllStat() {
        return allStat;
    }

    public void setAllStat(byte allStat) {
        this.allStat = allStat;
    }

    public byte getKarmaCount() {
        return karmaCount;
    }

    public void setKarmaCount(byte karmaCount) {
        this.karmaCount = karmaCount;
    }

    public byte getEnhance() {
        return enhance;
    }

    public void setEnhance(final byte en) {
        enhance = en;
    }

    public int getPotential1() {
        return potential1;
    }

    public void setPotential1(final int en) {
        potential1 = en;
    }

    public int getPotential2() {
        return potential2;
    }

    public void setPotential2(final int en) {
        potential2 = en;
    }

    public int getPotential3() {
        return potential3;
    }

    public void setPotential3(final int en) {
        potential3 = en;
    }

    public int getPotential4() {
        return potential4;
    }

    public void setPotential4(int en) {
        potential4 = en;
    }

    public int getPotential5() {
        return potential5;
    }

    public void setPotential5(int en) {
        potential5 = en;
    }

    public int getPotential6() {
        return potential6;
    }

    public void setPotential6(int en) {
        potential6 = en;
    }

    public int getPotential7() {
        return potential7;
    }

    public void setPotential7(int en) {
        potential7 = en;
    }

    public int getPotential8() {
        return potential8;
    }

    public void setPotential8(int en) {
        potential8 = en;
    }

    public int getBonusPotential1() {
        return bonuspotential1;
    }

    public void setBonusPotential1(final int en) {
        bonuspotential1 = en;
    }

    public int getBonusPotential2() {
        return bonuspotential2;
    }

    public void setBonusPotential2(final int en) {
        bonuspotential2 = en;
    }

    public int getBonusPotential3() {
        return bonuspotential3;
    }

    public void setBonusPotential3(final int en) {
        bonuspotential3 = en;
    }

    public int getFusionAnvil() {
        return fusionAnvil;
    }

    public void setFusionAnvil(final int en) {
        fusionAnvil = en;
    }

    public byte getState() {
        int pots = potential1 + potential2 + potential3 + potential4 + potential5;
        if (potential1 < 0) {
            return 1;
        }
        if ((potential1 >= 40000) || (potential2 >= 40000) || (potential3 >= 40000) || (potential4 >= 40000) || (potential5 >= 40000)) {
            return 20;
        }
        if ((potential1 >= 30000) || (potential2 >= 30000) || (potential3 >= 30000) || (potential4 >= 30000) || (potential5 >= 30000)) {
            return 19;
        }
        if ((potential1 >= 20000) || (potential2 >= 20000) || (potential3 >= 20000) || (potential4 >= 20000) || (potential5 >= 20000)) {
            return 18;
        }
        if (pots >= 1) {
            return 17;
        }
        if (pots < 0) {
            return 1;
        }
        return 0;
    }

    public byte getBonusState() {
        final int pots = bonuspotential1 + bonuspotential2 + bonuspotential3;
        //if (potential4 >= 60000 || potential5 >= 60000) {
        //    return 14; // special
        //} else 
        if (bonuspotential1 >= 40000 || bonuspotential2 >= 40000 || bonuspotential3 >= 40000) {
            return 20; // legendary
        } else if (bonuspotential1 >= 30000 || bonuspotential2 >= 30000 || bonuspotential3 >= 30000) {
            return 19; // unique
        } else if (bonuspotential1 >= 20000 || bonuspotential2 >= 20000 || bonuspotential3 >= 20000) {
            return 18; // epic
        } else if (pots >= 1) {
            return 17; // rare
        } else if (pots < 0) {
            return 1; // hidden
        }
        return 0;
    }

    public void resetPotential_Fuse(boolean half, int potentialState) { //maker skill - equip first receive
        //no legendary, 0.16% chance unique, 4% chance epic, else rare
        potentialState = -potentialState;
        if (Randomizer.nextInt(100) < 4) {
            potentialState -= Randomizer.nextInt(100) < 4 ? 2 : 1;
        }
        setPotential1(potentialState);
        setPotential2((Randomizer.nextInt(half ? 5 : 10) == 0 ? potentialState : 0)); //1/10 chance of 3 line
        setPotential3(0); //just set it theoretically
        setPotential4(0);
        setPotential5(0);
    }

    public void resetBonusPotential_Fuse(boolean half, int potentialState) { //maker skill - equip first receive
        //no legendary, 0.16% chance unique, 4% chance epic, else rare
        potentialState = -potentialState;
        if (Randomizer.nextInt(100) < 4) {
            potentialState -= Randomizer.nextInt(100) < 4 ? 2 : 1;
        }
        setBonusPotential1(potentialState);
        setBonusPotential2((Randomizer.nextInt(half ? 5 : 10) == 0 ? potentialState : 0)); //1/10 chance of 3 line
        setBonusPotential3(0); //just set it theoretically
        setPotential4(0);
        setPotential5(0);
    }

    public void resetPotential() {
        final int rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -19 : -18) : -17;
        setPotential1(rank);
        setPotential2((Randomizer.nextInt(10) == 0 ? rank : 0)); //1/10 chance of 3 line
        setPotential3(0); //just set it theoretically
    }

    public void resetBonusPotential() {
        final int rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -19 : -18) : -17;
        setBonusPotential1(rank);
        setBonusPotential2((Randomizer.nextInt(10) == 0 ? rank : 0)); //1/10 chance of 3 line
        setBonusPotential3(0); //just set it theoretically
    }

    public void renewPotential(int type, int line, int toLock, boolean bonus) { // 0 = normal miracle cube, 1 = premium, 2 = epic pot scroll, 3 = super, 5 = enlightening
        int miracleRate = 1;
        if (EventConstants.DoubleMiracleTime) {
            miracleRate *= 2;
        }
        int rank;
        if (bonus) {
            if (type != 6) {
                return;
            }
            rank = (Randomizer.nextInt(100) < 4 * miracleRate && getBonusState() != 20 ? -(getBonusState() + 1) : -(getBonusState())); // 4 % chance to up 1 tier
            setBonusPotential1(rank);
        } else {
            rank = type == 2 ? -18 : type == 5 ? (Randomizer.nextInt(100) < 3 * miracleRate && getState() != 20 ? -20 : Randomizer.nextInt(100) < 10 * miracleRate && getState() != 20 ? -(getState() + 1) : -(getState())) : (Randomizer.nextInt(100) < 4 * miracleRate && getState() != (type == 3 ? 20 : 19) ? -(getState() + 1) : -(getState())); // 4 % chance to up 1 tier
            setPotential1(rank);
        }
        if (getPotential3() > 0 && !bonus) {
            setPotential2(rank); // put back old 3rd line
            setPotential3(0);
        } else {
            switch (type) {
                case 1: // premium-> suppose to be 25%
                    setPotential2(Randomizer.nextInt(10) == 0 ? rank : 0); //1/10 chance of 3 line
                    break;
                case 2: // epic pot
                    setPotential2(Randomizer.nextInt(10) <= 1 ? rank : 0); //2/10 chance of 3 line
                    break;
                case 3: // super
                    setPotential2(Randomizer.nextInt(10) <= 2 ? rank : 0); //3/10 chance of 3 line
                    break;
                case 4: // revolutionary
                    setPotential2(Randomizer.nextInt(10) <= 3 ? rank : 0); //4/10 chance of 3 line
                    break;
                case 5: // enlightening
                    setPotential2(Randomizer.nextInt(10) <= 2 ? rank : 0); //3/10 chance of 3 line
                    break;
                case 6: // master
                    if (!bonus) {
                        return;
                    }
                    setBonusPotential2(Randomizer.nextInt(10) <= 2 ? rank : 0); //3/10 chance of 3 line
                    break;
                default:
                    setPotential2(0);
                    break;
            }
        }

        if (getPotential4() > 0) {
            setPotential3(rank);
        } else if (type == 3) {
            setPotential3(Randomizer.nextInt(100) <= 2 ? rank : 0);
        } else {
            setPotential3(0);
        }
        if (getPotential5() > 0) {
            setPotential4(rank);
        } else if (type == 3) {
            setPotential4(Randomizer.nextInt(100) <= 1 ? rank : 0);
        } else {
            setPotential4(0);
        }
        setPotential5(0);

        if (bonus) {
            if (getBonusPotential2() > 0) {
                setBonusPotential1(rank); // put back old 5th line
            } else if (type == 6) { // super, revolutionary and enlightening
                setBonusPotential1(Randomizer.nextInt(100) <= 1 ? rank : 0); // 2/100 to get 5 lines
            } else {
                setBonusPotential1(0); //just set it theoretically
            }
            setBonusPotential2(0); //just set it theoretically
        }

        switch (line) {
            case 0:
                //Don't lock
                break;
            case 1:
                setPotential1(-(toLock + line * 100000 + (rank > getState() ? 10000 : 0)));
                break;
            case 2:
                setPotential2(-(toLock + line * 100000));
                break;
            case 3:
                setPotential3(-(toLock + line * 100000));
                break;
            default:
                System.out.println("[Hacking Attempt] Try to lock potential line which does not exists.");
                break;
        }
    }

    public int getIncSkill() {
        return incSkill;
    }

    public void setIncSkill(int inc) {
        incSkill = inc;
    }

    public short getCharmEXP() {
        return charmExp;
    }

    public short getPVPDamage() {
        return pvpDamage;
    }

    public void setCharmEXP(short s) {
        charmExp = s;
    }

    public void setPVPDamage(short p) {
        pvpDamage = p;
    }

    public MapleRing getRing() {
        if (!GameConstants.isEffectRing(getItemId()) || getUniqueId() <= 0) {
            return null;
        }
        if (ring == null) {
            ring = MapleRing.loadFromDb(getUniqueId(), getPosition() < 0);
        }
        return ring;
    }

    public void setRing(MapleRing ring) {
        this.ring = ring;
    }

    public MapleAndroid getAndroid() {
        if (getItemId() / 10000 != 166 || getUniqueId() <= 0) {
            return null;
        }
        if (android == null) {
            android = MapleAndroid.loadFromDb(getItemId(), getUniqueId());
        }
        return android;
    }

    public void setAndroid(MapleAndroid ring) {
        android = ring;
    }

    public short getSocketState() {
        int flag = 0;
        if (socket1 > 0 || socket2 > 0 || socket3 > 0) { // Got empty sockets show msg 
            flag |= SocketFlag.DEFAULT.getValue();
        }
        if (socket1 > 0) {
            flag |= SocketFlag.SOCKET_BOX_1.getValue();
        }
        if (socket1 > 1) {
            flag |= SocketFlag.USED_SOCKET_1.getValue();
        }
        if (socket2 > 0) {
            flag |= SocketFlag.SOCKET_BOX_2.getValue();
        }
        if (socket2 > 1) {
            flag |= SocketFlag.USED_SOCKET_2.getValue();
        }
        if (socket3 > 0) {
            flag |= SocketFlag.SOCKET_BOX_3.getValue();
        }
        if (socket3 > 1) {
            flag |= SocketFlag.USED_SOCKET_3.getValue();
        }
        return (short) flag;
    }

    public int getSocket1() {
        return socket1;
    }

    public void setSocket1(int socket1) {
        this.socket1 = socket1;
    }

    public int getSocket2() {
        return socket2;
    }

    public void setSocket2(int socket2) {
        this.socket2 = socket2;
    }

    public int getSocket3() {
        return socket3;
    }

    public void setSocket3(int socket3) {
        this.socket3 = socket3;
    }

    public List<EquipStat> getStats() {
        return stats;
    }

    public List<EquipSpecialStat> getSpecialStats() {
        return specialStats;
    }

    public Map<EquipStat, Long> getStatsTest() {
        return statsTest;
    }

    public static Equip calculateEquipStatsTest(Equip eq) {
        eq.getStatsTest().clear();
        if (eq.getUpgradeSlots() > 0) {
            eq.getStatsTest().put(EquipStat.SLOTS, Long.valueOf(eq.getUpgradeSlots()));
        }
        if (eq.getLevel() > 0) {
            eq.getStatsTest().put(EquipStat.LEVEL, Long.valueOf(eq.getLevel()));
        }
        if (eq.getStr() > 0) {
            eq.getStatsTest().put(EquipStat.STR, Long.valueOf(eq.getStr()));
        }
        if (eq.getDex() > 0) {
            eq.getStatsTest().put(EquipStat.DEX, Long.valueOf(eq.getDex()));
        }
        if (eq.getInt() > 0) {
            eq.getStatsTest().put(EquipStat.INT, Long.valueOf(eq.getInt()));
        }
        if (eq.getLuk() > 0) {
            eq.getStatsTest().put(EquipStat.LUK, Long.valueOf(eq.getLuk()));
        }
        if (eq.getHp() > 0) {
            eq.getStatsTest().put(EquipStat.MHP, Long.valueOf(eq.getHp()));
        }
        if (eq.getMp() > 0) {
            eq.getStatsTest().put(EquipStat.MMP, Long.valueOf(eq.getMp()));
        }
        if (eq.getWatk() > 0) {
            eq.getStatsTest().put(EquipStat.WATK, Long.valueOf(eq.getWatk()));
        }
        if (eq.getMatk() > 0) {
            eq.getStatsTest().put(EquipStat.MATK, Long.valueOf(eq.getMatk()));
        }
        if (eq.getWdef() > 0) {
            eq.getStatsTest().put(EquipStat.WDEF, Long.valueOf(eq.getWdef()));
        }
        if (eq.getMdef() > 0) {
            eq.getStatsTest().put(EquipStat.MDEF, Long.valueOf(eq.getMdef()));
        }
        if (eq.getAcc() > 0) {
            eq.getStatsTest().put(EquipStat.ACC, Long.valueOf(eq.getAcc()));
        }
        if (eq.getAvoid() > 0) {
            eq.getStatsTest().put(EquipStat.AVOID, Long.valueOf(eq.getAvoid()));
        }
        if (eq.getHands() > 0) {
            eq.getStatsTest().put(EquipStat.HANDS, Long.valueOf(eq.getHands()));
        }
        if (eq.getSpeed() > 0) {
            eq.getStatsTest().put(EquipStat.SPEED, Long.valueOf(eq.getSpeed()));
        }
        if (eq.getJump() > 0) {
            eq.getStatsTest().put(EquipStat.JUMP, Long.valueOf(eq.getJump()));
        }
        if (eq.getFlag() > 0) {
            eq.getStatsTest().put(EquipStat.FLAG, Long.valueOf(eq.getFlag()));
        }
        if (eq.getIncSkill() > 0) {
            eq.getStatsTest().put(EquipStat.INC_SKILL, Long.valueOf(eq.getIncSkill()));
        }
        if (eq.getEquipLevel() > 0) {
            eq.getStatsTest().put(EquipStat.ITEM_LEVEL, Long.valueOf(eq.getEquipLevel()));
        }
        if (eq.getItemEXP() > 0) {
            eq.getStatsTest().put(EquipStat.ITEM_EXP, eq.getItemEXP());
        }
        if (eq.getDurability() > -1) {
            eq.getStatsTest().put(EquipStat.DURABILITY, Long.valueOf(eq.getDurability()));
        }
        if (eq.getViciousHammer() > 0) {
            eq.getStatsTest().put(EquipStat.VICIOUS_HAMMER, Long.valueOf(eq.getViciousHammer()));
        }
        if (eq.getPVPDamage() > 0) {
            eq.getStatsTest().put(EquipStat.PVP_DAMAGE, Long.valueOf(eq.getPVPDamage()));
        }
        if (eq.getEnhanctBuff() > 0) {
            eq.getStatsTest().put(EquipStat.ENHANCT_BUFF, Long.valueOf(eq.getEnhanctBuff()));
        }
        if (eq.getReqLevel() > 0) {
            eq.getStatsTest().put(EquipStat.REQUIRED_LEVEL, Long.valueOf(eq.getReqLevel()));
        }
        if (eq.getYggdrasilWisdom() > 0) {
            eq.getStatsTest().put(EquipStat.YGGDRASIL_WISDOM, Long.valueOf(eq.getYggdrasilWisdom()));
        }
        if (eq.getFinalStrike()) {
            eq.getStatsTest().put(EquipStat.FINAL_STRIKE, Long.valueOf(eq.getFinalStrike() ? 1 : 0));
        }
        if (eq.getBossDamage() > 0) {
            eq.getStatsTest().put(EquipStat.BOSS_DAMAGE, Long.valueOf(eq.getBossDamage()));
        }
        if (eq.getIgnorePDR() > 0) {
            eq.getStatsTest().put(EquipStat.IGNORE_PDR, Long.valueOf(eq.getIgnorePDR()));
        }
        //SPECIAL STATS:
        if (eq.getTotalDamage() > 0) {
            eq.getStatsTest().put(EquipStat.TOTAL_DAMAGE, Long.valueOf(eq.getTotalDamage()));
        }
        if (eq.getAllStat() > 0) {
            eq.getStatsTest().put(EquipStat.ALL_STAT, Long.valueOf(eq.getAllStat()));
        }
        eq.getStatsTest().put(EquipStat.KARMA_COUNT, Long.valueOf(eq.getKarmaCount())); //no count = -1
        //eq.getStatsTest().put(EquipStat.UNK8, Long.valueOf(-1)); // test
        //eq.getStatsTest().put(EquipStat.UNK10, Long.valueOf(0)); // test
        return (Equip) eq.copy();
    }

    public static Equip calculateEquipStats(Equip eq) {
        eq.getStats().clear();
        eq.getSpecialStats().clear();
        if (eq.getUpgradeSlots() > 0) {
            eq.getStats().add(EquipStat.SLOTS);
        }
        if (eq.getLevel() > 0) {
            eq.getStats().add(EquipStat.LEVEL);
        }
        if (eq.getStr() > 0) {
            eq.getStats().add(EquipStat.STR);
        }
        if (eq.getDex() > 0) {
            eq.getStats().add(EquipStat.DEX);
        }
        if (eq.getInt() > 0) {
            eq.getStats().add(EquipStat.INT);
        }
        if (eq.getLuk() > 0) {
            eq.getStats().add(EquipStat.LUK);
        }
        if (eq.getHp() > 0) {
            eq.getStats().add(EquipStat.MHP);
        }
        if (eq.getMp() > 0) {
            eq.getStats().add(EquipStat.MMP);
        }
        if (eq.getWatk() > 0) {
            eq.getStats().add(EquipStat.WATK);
        }
        if (eq.getMatk() > 0) {
            eq.getStats().add(EquipStat.MATK);
        }
        if (eq.getWdef() > 0) {
            eq.getStats().add(EquipStat.WDEF);
        }
        if (eq.getMdef() > 0) {
            eq.getStats().add(EquipStat.MDEF);
        }
        if (eq.getAcc() > 0) {
            eq.getStats().add(EquipStat.ACC);
        }
        if (eq.getAvoid() > 0) {
            eq.getStats().add(EquipStat.AVOID);
        }
        if (eq.getHands() > 0) {
            eq.getStats().add(EquipStat.HANDS);
        }
        if (eq.getSpeed() > 0) {
            eq.getStats().add(EquipStat.SPEED);
        }
        if (eq.getJump() > 0) {
            eq.getStats().add(EquipStat.JUMP);
        }
        if (eq.getFlag() > 0) {
            eq.getStats().add(EquipStat.FLAG);
        }
        if (eq.getIncSkill() > 0) {
            eq.getStats().add(EquipStat.INC_SKILL);
        }
        if (eq.getEquipLevel() > 0) {
            eq.getStats().add(EquipStat.ITEM_LEVEL);
        }
        if (eq.getItemEXP() > 0) {
            eq.getStats().add(EquipStat.ITEM_EXP);
        }
        if (eq.getDurability() > -1) {
            eq.getStats().add(EquipStat.DURABILITY);
        }
        if (eq.getViciousHammer() > 0) {
            eq.getStats().add(EquipStat.VICIOUS_HAMMER);
        }
        if (eq.getPVPDamage() > 0) {
            eq.getStats().add(EquipStat.PVP_DAMAGE);
        }
        if (eq.getEnhanctBuff() > 0) {
            eq.getStats().add(EquipStat.ENHANCT_BUFF);
        }
        if (eq.getReqLevel() > 0) {
            eq.getStats().add(EquipStat.REQUIRED_LEVEL);
        }
        if (eq.getYggdrasilWisdom() > 0) {
            eq.getStats().add(EquipStat.YGGDRASIL_WISDOM);
        }
        if (eq.getFinalStrike()) {
            eq.getStats().add(EquipStat.FINAL_STRIKE);
        }
        if (eq.getBossDamage() > 0) {
            eq.getStats().add(EquipStat.BOSS_DAMAGE);
        }
        if (eq.getIgnorePDR() > 0) {
            eq.getStats().add(EquipStat.IGNORE_PDR);
        }
        //SPECIAL STATS:
        if (eq.getTotalDamage() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.TOTAL_DAMAGE);
        }
        if (eq.getAllStat() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.ALL_STAT);
        }
        eq.getSpecialStats().add(EquipSpecialStat.KARMA_COUNT); //no count = -1
        //if (0 != 0) {
        //    eq.getSpecialStats().add(EquipSpecialStat.UNK10);
        //}
        eq.getSpecialStats().add(EquipSpecialStat.UNK8); // test
        eq.getSpecialStats().add(EquipSpecialStat.UNK10); // test
        return (Equip) eq.copy();
    }
}
