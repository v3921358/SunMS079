package client.anticheat;

import client.MapleCharacter;
import client.SkillFactory;
import constants.GameConstants;
import constants.ServerConstants;
import handling.world.World;
import java.awt.Point;
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import server.AutobanManager;
import server.Timer.CheatTimer;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.packet.CWvsContext;

public class CheatTracker {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rL = lock.readLock(), wL = lock.writeLock();
    private final Map<CheatingOffense, CheatingOffenseEntry> offenses = new LinkedHashMap<>();
    private WeakReference<MapleCharacter> chr;
    // For keeping track of speed attack hack.
    private long lastAttackTime = 0;
    private int lastAttackTickCount = 0;
    private byte Attack_tickResetCount = 0;
    private long Server_ClientAtkTickDiff = 0;
    private long lastDamage = 0;
    private long takingDamageSince;
    private int numSequentialDamage = 0;
    private long lastDamageTakenTime = 0;
    private byte numZeroDamageTaken = 0;
    private int numSequentialSummonAttack = 0;
    private long summonSummonTime = 0;
    private int numSameDamage = 0;
    private Point lastMonsterMove;
    private int monsterMoveCount;
    private int attacksWithoutHit = 0;
    private byte dropsPerSecond = 0;
    private long lastDropTime = 0;
    private byte msgsPerSecond = 0;
    private long lastMsgTime = 0;
    private ScheduledFuture<?> invalidationTask;
    private int gm_message = 0;
    private int lastTickCount = 0, tickSame = 0;
    private long lastSmegaTime = 0, lastBBSTime = 0, lastASmegaTime = 0;
    private long lastSaveTime = 0;
    private long lastLieDetectorTime = 0;
    private long[] lastTime = new long[6];

    //private int lastFamiliarTickCount = 0;
    //private byte Familiar_tickResetCount = 0;
    //private long Server_ClientFamiliarTickDiff = 0;
    private int numSequentialFamiliarAttack = 0;
    private long familiarSummonTime = 0;

    public CheatTracker(final MapleCharacter chr) {
        start(chr);
    }

    public final void checkAttack(final int skillId, final int tickcount) {
        final int AtkDelay = GameConstants.getAttackDelay(skillId, skillId == 0 ? null : SkillFactory.getSkill(skillId));

        if ((tickcount - lastAttackTickCount) < AtkDelay && (skillId != 3121004)) {
            registerOffense(CheatingOffense.快速攻击, "攻击速度异常，技能: " + skillId + " check: " + (tickcount - lastAttackTickCount) + " " + "AtkDelay: " + AtkDelay);
        }

        //if ((tickcount - lastAttackTickCount) < AtkDelay) {
        //   registerOffense(CheatingOffense.FASTATTACK);
        // }
        lastAttackTime = System.currentTimeMillis();
        if (chr.get() != null && lastAttackTime - chr.get().getChangeTime() > 600000) { //chr was afk for 10 mins and is now attacking
            chr.get().setChangeTime();
        }
        final long STime_TC = lastAttackTime - tickcount; // hack = - more
        if (Server_ClientAtkTickDiff - STime_TC > 1000) { // 250 is the ping, TODO
            registerOffense(CheatingOffense.快速攻击2);
        }
        //if (Server_ClientAtkTickDiff - STime_TC > 1000) { // 250 is the ping, TODO
        //    registerOffense(CheatingOffense.FASTATTACK2);
        //}
        // if speed hack, client tickcount values will be running at a faster pace
        // For lagging, it isn't an issue since TIME is running simotaniously, client
        // will be sending values of older time

//	System.out.println("Delay [" + skillId + "] = " + (tickcount - lastAttackTickCount) + ", " + (Server_ClientAtkTickDiff - STime_TC));
        Attack_tickResetCount++; // Without this, the difference will always be at 100
        if (Attack_tickResetCount >= (AtkDelay <= 200 ? 1 : 4)) {
            Attack_tickResetCount = 0;
            Server_ClientAtkTickDiff = STime_TC;
        }
        updateTick(tickcount);
        lastAttackTickCount = tickcount;
    }

    //unfortunately PVP does not give a tick count
    public final void checkPVPAttack(final int skillId) {
        final int AtkDelay = GameConstants.getAttackDelay(skillId, skillId == 0 ? null : SkillFactory.getSkill(skillId));
        final long STime_TC = System.currentTimeMillis() - lastAttackTime; // hack = - more
        if (STime_TC < AtkDelay) { // 250 is the ping, TODO
            registerOffense(CheatingOffense.FASTATTACK);
        }
        lastAttackTime = System.currentTimeMillis();
    }

    public final long getLastAttack() {
        return lastAttackTime;
    }

    public final void checkTakeDamage(final int damage) {
        numSequentialDamage++;
        lastDamageTakenTime = System.currentTimeMillis();

        // System.out.println("tb" + timeBetweenDamage);
        // System.out.println("ns" + numSequentialDamage);
        // System.out.println(timeBetweenDamage / 1500 + "(" + timeBetweenDamage / numSequentialDamage + ")");
        //if (lastDamageTakenTime - takingDamageSince / 500 < numSequentialDamage) {
        //    registerOffense(CheatingOffense.FAST_TAKE_DAMAGE);
        // }
        if (lastDamageTakenTime - takingDamageSince / 500 < numSequentialDamage) {
            registerOffense(CheatingOffense.怪物碰撞过快);
        }
        if (lastDamageTakenTime - takingDamageSince > 4500) {
            takingDamageSince = lastDamageTakenTime;
            numSequentialDamage = 0;
        }
        /*	(non-thieves)
         Min Miss Rate: 2%
         Max Miss Rate: 80%
         (thieves)
         Min Miss Rate: 5%
         Max Miss Rate: 95%*/
        if (damage == 0) {
            numZeroDamageTaken++;
            if (numZeroDamageTaken >= 35) { // Num count MSEA a/b players
                numZeroDamageTaken = 0;
                registerOffense(CheatingOffense.HIGH_AVOID);
            }
        } else if (damage != -1) {
            numZeroDamageTaken = 0;
        }
    }

    public final void checkSameDamage(final int dmg, final double expected) {
        if (dmg == 999999) {
            return;
        }
        if (dmg > 2000 && lastDamage == dmg && chr.get() != null && (chr.get().getLevel() < 175 || dmg > expected * 2)) {
            numSameDamage++;

            if (numSameDamage > 5) {
                registerOffense(CheatingOffense.SAME_DAMAGE, numSameDamage + " times, damage " + dmg + ", expected " + expected + " [Level: " + chr.get().getLevel() + ", Job: " + chr.get().getJob() + "]");
                numSameDamage = 0;
            }
        } else {
            lastDamage = dmg;
            numSameDamage = 0;
        }
    }

    public final void checkMoveMonster(final Point pos) {
        if (pos == lastMonsterMove) {
            monsterMoveCount++;
            if (monsterMoveCount > 10) {
                registerOffense(CheatingOffense.MOVE_MONSTERS, "Position: " + pos.x + ", " + pos.y);
                monsterMoveCount = 0;
            }
        } else {
            lastMonsterMove = pos;
            monsterMoveCount = 1;
        }
    }

    public final void resetSummonAttack() {
        summonSummonTime = System.currentTimeMillis();
        numSequentialSummonAttack = 0;
    }

    public final boolean checkSummonAttack() {
        numSequentialSummonAttack++;
        return (System.currentTimeMillis() - summonSummonTime) / (1000 + 1) >= numSequentialSummonAttack;
    }

    public final void resetFamiliarAttack() {
        familiarSummonTime = System.currentTimeMillis();
        numSequentialFamiliarAttack = 0;
        //lastFamiliarTickCount = 0;
        //Familiar_tickResetCount = 0;
        //Server_ClientFamiliarTickDiff = 0;
    }

    public final boolean checkFamiliarAttack(final MapleCharacter chr) {
        /*final int tickdifference = (tickcount - lastFamiliarTickCount);
         if (tickdifference < 500) {
         chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
         }
         final long STime_TC = System.currentTimeMillis() - tickcount;
         final long S_C_Difference = Server_ClientFamiliarTickDiff - STime_TC;
         if (S_C_Difference > 500) {
         chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
         }
         Familiar_tickResetCount++;
         if (Familiar_tickResetCount > 4) {
         Familiar_tickResetCount = 0;
         Server_ClientFamiliarTickDiff = STime_TC;
         }
         lastFamiliarTickCount = tickcount;*/
        numSequentialFamiliarAttack++;
        //estimated
        // System.out.println(numMPRegens + "/" + allowedRegens);
        if ((System.currentTimeMillis() - familiarSummonTime) / (600 + 1) < numSequentialFamiliarAttack) {
            registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
            return false;
        }
        return true;
    }

    public final void checkDrop() {
        checkDrop(false);
    }

    public final void checkDrop(final boolean dc) {
        if ((System.currentTimeMillis() - lastDropTime) < 1000) {
            dropsPerSecond++;
            if (dropsPerSecond >= (dc ? 32 : 16) && chr.get() != null && !chr.get().isGM()) {
                if (dc) {
                    chr.get().getClient().getSession().close();
                    FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                } else {
                    chr.get().getClient().setMonitored(true);
                }
            }
        } else {
            dropsPerSecond = 0;
        }
        lastDropTime = System.currentTimeMillis();
    }

    public final void checkMsg() { //ALL types of msg. caution with number of  msgsPerSecond
        if ((System.currentTimeMillis() - lastMsgTime) < 1000) { //luckily maplestory has auto-check for too much msging
            msgsPerSecond++;
            if (msgsPerSecond > 10 && chr.get() != null && !chr.get().isGM()) {
                chr.get().getClient().getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
        } else {
            msgsPerSecond = 0;
        }
        lastMsgTime = System.currentTimeMillis();
    }

    public final int getAttacksWithoutHit() {
        return attacksWithoutHit;
    }

    public final void setAttacksWithoutHit(final boolean increase) {
        if (increase) {
            this.attacksWithoutHit++;
        } else {
            this.attacksWithoutHit = 0;
        }
    }

    public final void registerOffense(final CheatingOffense offense) {
        registerOffense(offense, null);
    }

    public final void registerOffense(final CheatingOffense offense, final String param) {
        final MapleCharacter chrhardref = chr.get();
        if (chrhardref == null || !offense.isEnabled() || chrhardref.isClone() || chrhardref.isGM()) {
            return;
        }
        //System.out.println("OFFENSE REGISTERED: " + offense.name() + " on " + chrhardref.getName());
        CheatingOffenseEntry entry = null;
        rL.lock();
        try {
            entry = offenses.get(offense);
        } finally {
            rL.unlock();
        }
        if (entry != null && entry.isExpired()) {
            expireEntry(entry);
            entry = null;
            gm_message = 0;
        }
        if (entry == null) {
            entry = new CheatingOffenseEntry(offense, chrhardref.getId());
        }
        if (param != null) {
            entry.setParam(param);
        }
        entry.incrementCount();
        //if (offense.shouldAutoban(entry.getCount())) {
        //   final byte type = offense.getBanType();
        //    if (type == 1) {
        // AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()));
        //    } else if (type == 2) {
        //        chrhardref.getClient().getSession().close();
        //    }
        //    gm_message = 0;
        //    return;
        // }
        if (offense.shouldAutoban(entry.getCount())) {
            final byte type = offense.getBanType();
            String outputFileName;
            if (type == 1) {
                AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name() + param));
            } else if (type == 2) {
                outputFileName = "断线";
                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + chrhardref.getName() + " 自动断线 类型: " + offense.toString() + " 原因: " + (param == null ? "" : (" - " + param))));
                FileoutputUtil.logToFile(outputFileName + ".txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家：" + chr.get().getName() + " 项目：" + offense.toString() + " 原因： " + (param == null ? "" : (" - " + param)));
                chrhardref.getClient().getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                return;
            } else if (type == 3) {
                boolean ban = true;
                outputFileName = "封号";
                String reason = offense.name();
                switch (offense) {
                    case 攻击怪物数量异常:
                        outputFileName = "攻击怪物数量异常";
                        break;
                    case 技能攻击次数异常:
                        outputFileName = "技能攻击次数异常";
                        break;
                    case 吸怪:
                        outputFileName = "吸怪";
                        if (!ServerConstants.autoban) {
                            ban = false;
                        }
                        break;
                }

                if (chr.get().hasGmLevel(1)) {
                    chr.get().dropMessage("发现异常: " + offense.name() + " param: " + (param == null ? "" : (" - " + param)));
                } else {
                    if (ban) {
                        FileoutputUtil.logToFile("Ban/" + outputFileName + ".txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家：" + chr.get().getName() + " 项目：" + offense.toString() + " 原因： " + (param == null ? "" : (" - " + param)));
                        World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[外挂检测] " + chrhardref.getName() + " 因为使用非法程序而被管理员永久停封。"));
                        World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + chrhardref.getName() + " " + reason + "自动封号! "));
                        chrhardref.ban(chrhardref.getName() + reason, true, true, false);
                        chrhardref.getClient().getSession().close();
                        FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                    } else {
                        FileoutputUtil.logToFile(outputFileName + ".txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家：" + chr.get().getName() + " 项目：" + offense.toString() + " 原因： " + (param == null ? "" : (" - " + param)));
                    }
                }
            }
            gm_message = 0;
            return;
        }

        wL.lock();
        try {
            offenses.put(offense, entry);
        } finally {
            wL.unlock();
        }
        switch (offense) {
            case 伤害过高:
            case 魔法伤害过高:
            case 伤害过高2:
            case 魔法伤害过高2:
            case 快速攻击:
            case 快速攻击2:
            //  case HIGH_DAMAGE_MAGIC:
            // HIGH_DAMAGE_MAGIC_2:
            // case HIGH_DAMAGE:
            //case HIGH_DAMAGE_2:
            case 怪物碰撞过快:
            case ATTACK_WITHOUT_GETTING_HIT:
            case HIGH_AVOID:
            case ATTACK_FARAWAY_MONSTER:
            case ATTACK_FARAWAY_MONSTER_SUMMON:
            case SAME_DAMAGE:
                gm_message++;
                if (gm_message % 20 == 0) {
                    World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + chrhardref.getName() + " (编号:" + chrhardref.getId() + ")怀疑使用外挂! " + offense.name() + (param == null ? "" : (" - " + param))));
//                    if (log) {
//                        FileoutputUtil.logToFile("Hack/" + out_log + ".txt", "\r\n" + FileoutputUtil.CurrentReadable_TimeGMT()+ " " + chrhardref.getName() + " (編號:" + chrhardref.getId() + ")疑似外掛! " + show + (param == null ? "" : (" - " + param)));
//                    }
                }
                if (gm_message >= 300 && chrhardref.getLevel() < (offense == CheatingOffense.SAME_DAMAGE ? 175 : 150)) {
                    final Timestamp created = chrhardref.getClient().getCreated();
                    long time = System.currentTimeMillis();
                    if (created != null) {
                        time = created.getTime();
                    }
                    if (time + (15 * 24 * 60 * 60 * 1000) >= System.currentTimeMillis()) { //made within 15
                        //AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()) + " over 500 times " + (param == null ? "" : (" - " + param)));
                    } else {
                        gm_message = 0;
                        //World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM Message] " + MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + " (level " + chrhardref.getLevel() + ") suspected of autoban! " + StringUtil.makeEnumHumanReadable(offense.name()) + (param == null ? "" : (" - " + param))));
                        //FileoutputUtil.log(FileoutputUtil.Hacker_Log, "[GM Message] " + MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + " (level " + chrhardref.getLevel() + ") suspected of autoban! " + StringUtil.makeEnumHumanReadable(offense.name()) + (param == null ? "" : (" - " + param)));
                    }
                }
                break;
        }
        CheatingOffensePersister.getInstance().persistEntry(entry);
    }

    public void updateTick(int newTick) {
        if (newTick <= lastTickCount) { //definitely packet spamming or the added feature in many PEs which is to generate random tick
            if (tickSame >= 5 && chr.get() != null && !chr.get().isGM()) {
                chr.get().getClient().getSession().close();
                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            } else {
                tickSame++;
            }
        } else {
            tickSame = 0;
        }
        lastTickCount = newTick;
    }

    public boolean canSmega() {
        if (lastSmegaTime + 15000 > System.currentTimeMillis() && chr.get() != null && !chr.get().isGM()) {
            return false;
        }
        lastSmegaTime = System.currentTimeMillis();
        return true;
    }

    public boolean canAvatarSmega() {
        if (lastASmegaTime + 300000 > System.currentTimeMillis() && chr.get() != null && !chr.get().isGM()) {
            return false;
        }
        lastASmegaTime = System.currentTimeMillis();
        return true;
    }

    public boolean canBBS() {
        if (lastBBSTime + 60000 > System.currentTimeMillis() && chr.get() != null && !chr.get().isGM()) {
            return false;
        }
        lastBBSTime = System.currentTimeMillis();
        return true;
    }

    public final void expireEntry(final CheatingOffenseEntry coe) {
        wL.lock();
        try {
            offenses.remove(coe.getOffense());
        } finally {
            wL.unlock();
        }
    }

    public final int getPoints() {
        int ret = 0;
        CheatingOffenseEntry[] offenses_copy;
        rL.lock();
        try {
            offenses_copy = offenses.values().toArray(new CheatingOffenseEntry[offenses.size()]);
        } finally {
            rL.unlock();
        }
        for (final CheatingOffenseEntry entry : offenses_copy) {
            if (entry.isExpired()) {
                expireEntry(entry);
            } else {
                ret += entry.getPoints();
            }
        }
        return ret;
    }

    public final Map<CheatingOffense, CheatingOffenseEntry> getOffenses() {
        return Collections.unmodifiableMap(offenses);
    }

    public final String getSummary() {
        final StringBuilder ret = new StringBuilder();
        final List<CheatingOffenseEntry> offenseList = new ArrayList<>();
        rL.lock();
        try {
            for (final CheatingOffenseEntry entry : offenses.values()) {
                if (!entry.isExpired()) {
                    offenseList.add(entry);
                }
            }
        } finally {
            rL.unlock();
        }
        Collections.sort(offenseList, new Comparator<CheatingOffenseEntry>() {

            @Override
            public final int compare(final CheatingOffenseEntry o1, final CheatingOffenseEntry o2) {
                final int thisVal = o1.getPoints();
                final int anotherVal = o2.getPoints();
                return (thisVal < anotherVal ? 1 : (thisVal == anotherVal ? 0 : -1));
            }
        });
        final int to = Math.min(offenseList.size(), 4);
        for (int x = 0; x < to; x++) {
            ret.append(StringUtil.makeEnumHumanReadable(offenseList.get(x).getOffense().name()));
            ret.append(": ");
            ret.append(offenseList.get(x).getCount());
            if (x != to - 1) {
                ret.append(" ");
            }
        }
        return ret.toString();
    }

    public final void dispose() {
        if (invalidationTask != null) {
            invalidationTask.cancel(false);
        }
        invalidationTask = null;
        chr = new WeakReference<>(null);
    }

    public final void start(final MapleCharacter chr) {
        this.chr = new WeakReference<>(chr);
        invalidationTask = CheatTimer.getInstance().register(new InvalidationTask(), 60000);
        takingDamageSince = System.currentTimeMillis();
    }

    public synchronized boolean GMSpam(int limit, int type) {
        if (type < 0 || lastTime.length < type) {
            type = 1; // default xD
        }
        if (System.currentTimeMillis() < limit + lastTime[type]) {
            return true;
        }
        lastTime[type] = System.currentTimeMillis();
        return false;
    }

    private final class InvalidationTask implements Runnable {

        @Override
        public final void run() {
            CheatingOffenseEntry[] offenses_copy;
            rL.lock();
            try {
                offenses_copy = offenses.values().toArray(new CheatingOffenseEntry[offenses.size()]);
            } finally {
                rL.unlock();
            }
            for (CheatingOffenseEntry offense : offenses_copy) {
                if (offense.isExpired()) {
                    expireEntry(offense);
                }
            }
            if (chr.get() == null) {
                dispose();
            }
        }
    }

    public boolean canSaveDB() {
        if ((System.currentTimeMillis() - lastSaveTime < 5 * 60 * 1000)) {
            return false;
        }
        this.lastSaveTime = System.currentTimeMillis();
        return true;
    }

    public int getlastSaveTime() {
        return (int) ((System.currentTimeMillis() - this.lastSaveTime) / 1000);
    }

    public boolean canLieDetector() {
        if ((this.lastLieDetectorTime + 300000L > System.currentTimeMillis()) && (this.chr.get() != null)) {
            return false;
        }
        this.lastLieDetectorTime = System.currentTimeMillis();
        return true;
    }
}
