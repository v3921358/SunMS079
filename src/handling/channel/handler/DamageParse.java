package handling.channel.handler;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MonsterStatus;
import client.MonsterStatusEffect;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import client.anticheat.CheatTracker;
import client.anticheat.CheatingOffense;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.world.World;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import server.life.ElementalEffectiveness;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.AttackPair;
import tools.FileoutputUtil;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class DamageParse {

    @SuppressWarnings("empty-statement")
    public static void applyAttack(AttackInfo attack, Skill theSkill, MapleCharacter player, int attackCount, double maxDamagePerMonster, MapleStatEffect effect, AttackType attack_type) {
        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
            return;
        }
        if ((attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
            player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
        }
        if (attack.skill != 0) {
            if (effect == null) {
                player.getClient().getSession().write(CWvsContext.enableActions());
                return;
            }

            /* 檢測打怪數量 */
            if (attack.targets > effect.getMobCount()) {
                if (player.hasGmLevel(1)) {
                    player.dropMessage("打怪數量異常,技能代碼: " + attack.skill + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
                } else {
                    player.getCheatTracker().registerOffense(CheatingOffense.攻击怪物数量异常, " 玩家: " + player.getName() + "(" + player.getLevel() + ") 地图: " + player.getMapId() + "技能代码: " + attack.skill + " 技能等級: " + player.getSkillLevel(effect.getSourceId()) + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
                    World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + player.getName() + " (等級 " + player.getLevel() + ") " + "攻击怪物数量异常。 " + "封包怪物量 " + attack.targets + " 服務端怪物量 " + effect.getMobCount() + " 技能ID " + attack.skill));
                    FileoutputUtil.logToFile("打怪数量异常.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 地图: " + player.getMapId() + "技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(effect.getSourceId()) + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
                }
                return;
            }

            /* 檢測技能次數 */
            int last = attackCount;
            boolean mirror_fix = false;
            if (player.getJob() >= 411 && player.getJob() <= 412) {
                mirror_fix = true;
            }
            if (mirror_fix) {
                last *= 2;
            }
            if (attack.hits > last) {
                if (player.hasGmLevel(1)) {
                    player.dropMessage("攻擊次數異常攻擊次數 " + attack.hits + " 正確攻擊次數 " + last + " 技能ID " + attack.skill);
                } else {
                    player.getCheatTracker().registerOffense(CheatingOffense.技能攻击次数异常, "玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + " 技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(attack.skill) + " 攻擊次數 : " + attack.hits + " 正常攻擊次數 :" + last);
                    World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + player.getName() + " (等級 " + player.getLevel() + ") 攻擊次數異常已自動封鎖。 玩家攻擊次數 " + attack.hits + " 正確次數 " + last + " 技能ID " + attack.skill));
                    FileoutputUtil.logToFile("技能攻击次数.txt", "\r\n" + FileoutputUtil.CurrentReadable_TimeGMT() + "玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + " 技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(attack.skill) + " 攻擊次數 : " + attack.hits + " 正常攻擊次數 :" + last);
                }
                return;
            }

            if (GameConstants.isMulungSkill(attack.skill)) {
                if (player.getMapId() / 10000 != 92502) {
                    return;
                }
                if (player.getMulungEnergy() < 300) {
                    return;
                }
                player.mulung_EnergyModify(false);
            } else if (GameConstants.isPyramidSkill(attack.skill)) {
                if (player.getMapId() / 1000000 != 926) {
                    return;
                }

                if ((player.getPyramidSubway() != null) && (player.getPyramidSubway().onSkillUse(player)));
            } else if (GameConstants.isInflationSkill(attack.skill)) {
                if (player.getBuffedValue(MapleBuffStat.GIANT_POTION) != null);
            } else if ((attack.targets > effect.getMobCount()) && (attack.skill != 1211002) && (attack.skill != 1220010)) {
                player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
                return;
            }
        }
        if (player.getClient().getChannelServer().isAdminOnly()) {
            player.dropMessage(-1, new StringBuilder().append("Animation: ").append(Integer.toHexString((attack.display & 0x8000) != 0 ? attack.display - 32768 : attack.display)).toString());
        }
        boolean useAttackCount = (attack.skill != 4211006) && (attack.skill != 3221007) && (attack.skill != 23121003) && ((attack.skill != 1311001) || (player.getJob() != 132)) && (attack.skill != 3211006);

        if ((attack.hits > 0) && (attack.targets > 0)) {
            if (!player.getStat().checkEquipDurabilitys(player, -1)) {
                player.dropMessage(5, "一个道具的耐久度已经归零，但背包空位不足.");
                return;
            }
        }
        int totDamage = 0;
        MapleMap map = player.getMap();
        if (attack.skill == 4211006) {
            for (AttackPair oned : attack.allDamage) {
                if (oned.attack == null) {
                    MapleMapObject mapobject = map.getMapObject(oned.objectid, MapleMapObjectType.ITEM);

                    if (mapobject != null) {
                        MapleMapItem mapitem = (MapleMapItem) mapobject;
                        mapitem.getLock().lock();
                        try {
                            if (mapitem.getMeso() > 0) {
                                if (mapitem.isPickedUp()) {
                                    return;
                                }
                                map.removeMapObject(mapitem);
                                map.broadcastMessage(CField.explodeDrop(mapitem.getObjectId()));
                                mapitem.setPickedUp(true);
                            } else {
                                player.getCheatTracker().registerOffense(CheatingOffense.ETC_EXPLOSION);
                                return;
                            }
                        } finally {
                            mapitem.getLock().unlock();
                        }
                    } else {
                        player.getCheatTracker().registerOffense(CheatingOffense.EXPLODING_NONEXISTANT);
                        return;
                    }
                }
            }
        }
        int totDamageToOneMonster = 0;
        long hpMob = 0L;
        PlayerStats stats = player.getStat();

        int CriticalDamage = stats.passive_sharpeye_percent();
        int ShdowPartnerAttackPercentage = 0;
        if ((attack_type == AttackType.RANGED_WITH_SHADOWPARTNER) || (attack_type == AttackType.NON_RANGED_WITH_MIRROR)) {
            MapleStatEffect shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
            if (shadowPartnerEffect != null) {
                ShdowPartnerAttackPercentage += shadowPartnerEffect.getX();
            }
            attackCount /= 2;
        }
        ShdowPartnerAttackPercentage *= (CriticalDamage + 100) / 100;
        if (attack.skill == 4221001) {
            ShdowPartnerAttackPercentage *= 10;
        }

        double maxDamagePerHit = 0.0D;
        for (AttackPair oned : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(oned.objectid);
            if ((monster != null) && (monster.getLinkCID() <= 0)) {
                totDamageToOneMonster = 0;
                hpMob = monster.getMobMaxHp();
                MapleMonsterStats monsterstats = monster.getStats();
                int fixeddmg = monsterstats.getFixedDamage();
                boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) || (attack.skill == 21120006);

                if ((!Tempest) && (!player.isGM())) {
                    if (((player.getJob() >= 3200) && (player.getJob() <= 3212) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 3221007) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)))) {
                        maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, CriticalDamage);
                    } else {
                        maxDamagePerHit = 1.0D;
                    }
                }
                byte overallAttackCount = 0;

                int criticals = 0;
                for (Pair eachde : oned.attack) {
                    Integer eachd = (Integer) eachde.left;
                    overallAttackCount++;
                    /* 确认是否超过预计伤害*/
                    if (!GameConstants.isElseSkill(attack.skill)) {
                        int atk = 200000;
                        boolean ban = false;
                        if ((player.getLevel() <= 10)) {
                            atk = 250;
                        } else if (player.getLevel() <= 15) {
                            atk = 600;
                        } else if (player.getLevel() <= 20) {
                            atk = 1000;
                        } else if (player.getLevel() <= 30) {
                            atk = 2500;
                        } else if (player.getLevel() <= 60) {
                            atk = 8000;
                        }
                        if (eachd >= atk && eachd > maxDamagePerHit) {
                            ban = true;
                        }
                        if (eachd == monster.getMobMaxHp()) {
                            ban = false;
                        }
                        if (player.hasGmLevel(1)) {
                            ban = false;
                        }
                        // 此处是根据异常伤害进行封号处理。已经注释掉，20/8/16 ez
//                        if (ban) {
//                            boolean apple = false;
//                            if (player.getBuffSource(MapleBuffStat.WATK) == 2022179 || player.getBuffSource(MapleBuffStat.MATK) == 2022179 || player.getBuffSource(MapleBuffStat.WDEF) == 2022179) {
//                                apple = true;
//                            }
//                            FileoutputUtil.logToFile("Hack/Ban/伤害异常.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家<" + player.getLevel() + ">: " + player.getName() + " 怪物" + monster.getId() + " 地图: " + player.getMapId() + " 技能代码: " + attack.skill + " 最高伤害: " + atk + " 本次伤害:" + eachd + " 预计伤害: " + (int) maxDamagePerHit + "是否为BOSS: " + monster.getStats().isBoss() + " 紫色苹果: " + apple);
//                            World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[系统封号] " + player.getName() + " 系统扫描伤害异常自动封号永久停权处理。。"));
//                            World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 密语系统] " + player.getName() + " (等级" + player.getLevel() + ") " + "伤害异常。 " + "最高伤害" + atk + " 本次伤害" + eachd + " 技能ID " + attack.skill));
//                            player.ban(player.getName() + "伤害异常", true, true, false);
//                            player.getClient().getSession().close();
//                            FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
//                            return;
//                        }

                    }
                    if (((Boolean) eachde.right)) {
                        criticals++;
                    }
                    if ((useAttackCount) && (overallAttackCount - 1 == attackCount)) {
                        maxDamagePerHit = maxDamagePerHit / 100.0D * (ShdowPartnerAttackPercentage * (monsterstats.isBoss() ? stats.bossdam_r : stats.dam_r) / 100.0D);
                    }

                    if (fixeddmg != -1) {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : fixeddmg;
                        } else {
                            eachd = fixeddmg;
                        }
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = attack.skill != 0 ? 0 : Math.min(eachd, (int) maxDamagePerHit);
                    } else if (!player.isGM()) {

                        if (Tempest) {
                            if (eachd > monster.getMobMaxHp()) {
                                player.getCheatTracker().registerOffense(CheatingOffense.伤害过高, " 技能: " + attack.skill + " 怪物: " + monster.getId() + " 最大Hp: " + monster.getMobMaxHp() + " 本次傷害: " + eachd);
                                eachd = (int) Math.min(monster.getMobMaxHp(), Integer.MAX_VALUE);
                            }

                        } else if (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY) && !monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY) && !monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) {
                            if (eachd > maxDamagePerHit) {
                                player.getCheatTracker().registerOffense(CheatingOffense.伤害过高, "[傷害: " + eachd + ", 預期: " + (long) maxDamagePerHit + ", 怪物: " + monster.getId() + "] [職業: " + player.getJob() + ", 等級: " + player.getLevel() + ", 技能: " + attack.skill + "]");

                                // 確認相同傷害
                                if (attack.real) {
                                    player.getCheatTracker().checkSameDamage(eachd, maxDamagePerHit);
                                }

                                if (eachd > maxDamagePerHit * 2) {
                                    if (ServerConstants.LOG_DAMAGE) {
                                        FileoutputUtil.logToFile("hack/伤害计算/伤害计算修正.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 職業: " + player.getJob() + " 怪物:" + monster.getId() + " 技能: " + attack.skill + " 封包傷害 :" + eachd + " 預計傷害 :" + (int) maxDamagePerHit + " 是否為BOSS: " + monster.getStats().isBoss());
                                    }
                                    player.getCheatTracker().registerOffense(CheatingOffense.伤害过高2, "[傷害: " + eachd + ", 預期: " + (long) maxDamagePerHit + ", 怪物: " + monster.getId() + "] [職業: " + player.getJob() + ", 等級: " + player.getLevel() + ", 技能: " + attack.skill + "]");
                                    eachd = (int) (maxDamagePerHit * 2); // 轉換為伺服器計算的傷害
                                }

                            }
                        } else if (eachd > maxDamagePerHit) {
                            if (ServerConstants.LOG_DAMAGE) {
                                FileoutputUtil.logToFile("伤害计算/伤害计算修正.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 職業: " + player.getJob() + " 怪物:" + monster.getId() + " 技能: " + attack.skill + " 封包傷害 :" + eachd + " 預計傷害 :" + (int) maxDamagePerHit + " 是否為BOSS: " + monster.getStats().isBoss());
                            }
                            eachd = (int) (maxDamagePerHit);
                            player.getCheatTracker().registerOffense(CheatingOffense.伤害过高2, "[傷害: " + eachd + ", 預期: " + (long) maxDamagePerHit + ", 怪物: " + monster.getId() + "] [職業: " + player.getJob() + ", 等級: " + player.getLevel() + ", 技能: " + attack.skill + "]");
                        }

                        /*if (Tempest) {
                         if (eachd > monster.getMobMaxHp()) {
                         eachd = (int) Math.min(monster.getMobMaxHp(), 2147483647L);
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE);
                         }
                         } else if (((player.getJob() >= 3200) && (player.getJob() <= 3212) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)))) {
                         if (eachd > maxDamagePerHit) {
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(maxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                         if (attack.real) {
                         player.getCheatTracker().checkSameDamage(eachd, maxDamagePerHit);
                         }
                         if (eachd > maxDamagePerHit * 2.0D) {
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_2, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(maxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                         eachd = (int) (maxDamagePerHit * 2.0D);
                         if (eachd >= 999999) {
                         player.getClient().getSession().close();
                         }
                         }
                         }

                         } else if (eachd > maxDamagePerHit) {
                         eachd = (int) maxDamagePerHit;
                         }*/
                    }

                    if (player == null) {
                        return;
                    }
                    totDamageToOneMonster += eachd;

                    if (((eachd == 0) || (monster.getId() == 9700021)) && (player.getPyramidSubway() != null)) {
                        player.getPyramidSubway().onMiss(player);
                    }
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (attack.skill != 3101005) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
                    player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[Distance: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", Expected Distance: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" Job: ").append(player.getJob()).append("]").toString());
                }

                if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null) {
                    switch (attack.skill) {
                        case 0:
                        case 4001334:
                        case 4201005:
                        case 4211002:
                        case 4211004:
                        case 4221003:
                        case 4221007:
                            handlePickPocket(player, monster, oned);
                    }

                }

                if ((totDamageToOneMonster > 0) || (attack.skill == 21120006)) {
                    if (attack.skill != 1221011) {
                        monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    } else {
                        if (monster.getStats().isBoss()) {
                            monster.damage(player, 99999, true, attack.skill);
                        }
                    }

                    if (monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) {
                        player.addHP(-(7000 + Randomizer.nextInt(8000)));
                    }
                    player.onAttack(monster.getMobMaxHp(), monster.getMobMaxMp(), attack.skill, monster.getObjectId(), totDamage, 0);
                    switch (attack.skill) {
                        case 4001002:
                        case 4001334:
                        case 4001344:
                        case 4111005:
                        case 4121007:
                        case 4201005:
                        case 4211002:
                        case 4221001:
                        case 4221007:
                        case 4301001:
                        case 4311002:
                        case 4311003:
                        case 4331000:
                        case 4331004:
                        case 4331005:
                        case 4331006:
                        case 4341002:
                        case 4341004:
                        case 4341005:
                        case 4341009:
                        case 14001004:
                        case 14111002:
                        case 14111005:
                            int[] skills = {4120005, 4220005, 4340001, 14110004};
                            for (int i : skills) {
                                Skill skill = SkillFactory.getSkill(i);
                                if (player.getTotalSkillLevel(skill) > 0) {
                                    MapleStatEffect venomEffect = skill.getEffect(player.getTotalSkillLevel(skill));
                                    if (!venomEffect.makeChanceResult()) {
                                        break;
                                    }
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.POISON, 1, i, null, false), true, venomEffect.getDuration(), true, venomEffect);
                                    break;
                                }

                            }

                            break;
                        case 4201004:
                            monster.handleSteal(player);
                            break;
                    }
                    switch (attack.skill) {
                        //case 21101003: // body pressure
                        case 21000002: // Double attack
                        case 21100001: // Triple Attack
                        case 21100002: // Pole Arm Push
                        case 21100004: // Pole Arm Smash
                        case 21110002: // Full Swing
                        case 21110003: // Pole Arm Toss
                        case 21110004: // Fenrir Phantom
                        case 21110006: // Whirlwind
                        case 21110007: // (hidden) Full Swing - Double Attack
                        case 21110008: // (hidden) Full Swing - Triple Attack
                        case 21120002: // Overswing
                        case 21120005: // Pole Arm finale
                        case 21120006: // Tempest
                        case 21120009: // (hidden) Overswing - Double Attack
                        case 21111005:
                        case 21120010: { // (hidden) Overswing - Triple Attack
                            if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.WK_CHARGE);
                                if (eff != null && eff.getSourceId() == 21111005) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), eff.getSourceId(), null, false), false, eff.getY() * 1000, false, eff);

                                }
                            }
                            if (player.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BODY_PRESSURE);

                                if (eff != null && eff.makeChanceResult() && !monster.isBuffed(MonsterStatus.NEUTRALISE)) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.NEUTRALISE, 1, eff.getSourceId(), null, false), false, eff.getX() * 1000, false, eff);
                                }
                            }
                            break;
                        }
                        default: //passives attack bonuses
                            break;
                    }
//          int randomDMG = Randomizer.nextInt(player.getDamage2() - player.getReborns() + 1) + player.getReborns();
//          monster.damage(player, randomDMG, true, attack.skill);
//          if (player.getshowdamage() == 1)
//            player.dropMessage(5, new StringBuilder().append("You have done ").append(randomDMG).append(" extra RB damage! (disable/enable this with @dmgnotice)").toString());
//        }
                    //else {
//          if (player.getDamage() > 2147483647L) {
//            long randomDMG = player.getDamage();
//            monster.damage(player, monster.getMobMaxHp(), true, attack.skill);
//            if (player.getshowdamage() == 1) {
//              player.dropMessage(5, new StringBuilder().append("You have done ").append(randomDMG).append(" extra RB damage! (disable/enable this with @dmgnotice)").toString());
//            }
//          }
                    if (totDamageToOneMonster > 0) {
                        Item weapon_ = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        if (weapon_ != null) {
                            MonsterStatus stat = GameConstants.getStatFromWeapon(weapon_.getItemId());
                            if ((stat != null) && (Randomizer.nextInt(100) < GameConstants.getStatChance())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(stat, Integer.valueOf(GameConstants.getXForStat(stat)), GameConstants.getSkillForStat(stat), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, 10000L, false, null);
                            }
                        }
                        if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
                            MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BLIND);

                            if ((eff != null) && (eff.makeChanceResult())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.ACC, Integer.valueOf(eff.getX()), eff.getSourceId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
                            }
                        }

                        if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                            MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.HAMSTRING);

                            if ((eff != null) && (eff.makeChanceResult())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.SPEED, Integer.valueOf(eff.getX()), 3121007, null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
                            }
                        }
                        if ((player.getJob() == 121) || (player.getJob() == 122)) {
                            Skill skill = SkillFactory.getSkill(1211006);
                            if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
                                MapleStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, Integer.valueOf(1), skill.getId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 2000, monster.getStats().isBoss(), eff);
                            }
                            skill = SkillFactory.getSkill(1211005);
                            if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
                                MapleStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, Integer.valueOf(1), skill.getId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 2000, monster.getStats().isBoss(), eff);
                            }

                        }
                    }
                    if ((effect != null) && (effect.getMonsterStati().size() > 0) && (effect.makeChanceResult())) {
                        for (Map.Entry z : effect.getMonsterStati().entrySet()) {
                            monster.applyStatus(player, new MonsterStatusEffect((MonsterStatus) z.getKey(), (Integer) z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), true, effect);
                        }
                    }
                }
            }
        }

        MapleMonster monster;
        if ((attack.skill == 4331003) && ((hpMob <= 0L) || (totDamageToOneMonster < hpMob))) {
            return;
        }
        if ((hpMob > 0L) && (totDamageToOneMonster > 0)) {
            player.afterAttack(attack.targets, attack.hits, attack.skill);
        }
        if ((attack.skill != 0) && ((attack.targets > 0) || ((attack.skill != 4331003) && (attack.skill != 4341002))) && (!GameConstants.isNoDelaySkill(attack.skill))) {
            boolean applyTo = effect.applyTo(player, attack.position);
        }
        if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
            CheatTracker tracker = player.getCheatTracker();

            tracker.setAttacksWithoutHit(true);
            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }
    }

    @SuppressWarnings("empty-statement")
    public static final void applyAttackMagic(AttackInfo attack, Skill theSkill, MapleCharacter player, MapleStatEffect effect, double maxDamagePerHit) {
        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
            return;
        }
        if (!(attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 50)) {
            player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
            return;
        }

        /* 檢測打怪數量 */
        if (attack.targets > effect.getMobCount()) {
            if (player.hasGmLevel(1)) {
                player.dropMessage("打怪數量異常,技能代碼: " + attack.skill + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
            } else {
                player.getCheatTracker().registerOffense(CheatingOffense.攻击怪物数量异常, " 玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + "技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(effect.getSourceId()) + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
                FileoutputUtil.logToFile("打怪数量异常.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + "技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(effect.getSourceId()) + " 封包怪物量 : " + attack.targets + " 服務端怪物量 :" + effect.getMobCount());
                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 密語系統] " + player.getName() + " (等級 " + player.getLevel() + ") " + "攻擊怪物數量異常。 " + "封包怪物量 " + attack.targets + " 服務端怪物量 " + effect.getMobCount() + " 技能ID " + attack.skill));
            }
            return;
        }

        /* 檢測技能次數 */
        int last = effect.getAttackCount();
        boolean mirror_fix = false;
        if (player.getJob() >= 411 && player.getJob() <= 412) {
            mirror_fix = true;
        }
        if (mirror_fix) {
            last *= 2;
        }
        if (attack.hits > last) {
            if (player.hasGmLevel(1)) {
                player.dropMessage("攻擊次數異常攻擊次數 " + attack.hits + " 正確攻擊次數 " + last + " 技能ID " + attack.skill);
            } else {
                player.getCheatTracker().registerOffense(CheatingOffense.技能攻击次数异常, "玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + " 技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(attack.skill) + " 攻擊次數 : " + attack.hits + " 正常攻擊次數 :" + last);
                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[公告事项] " + player.getName() + " (等級 " + player.getLevel() + ") 攻擊次數異常。 玩家攻擊次數 " + attack.hits + " 正確次數 " + last + " 技能ID " + attack.skill));
                FileoutputUtil.logToFile("技能攻击次数.txt", "\r\n" + FileoutputUtil.CurrentReadable_TimeGMT() + "玩家: " + player.getName() + "(" + player.getLevel() + ") 地圖: " + player.getMapId() + " 技能代碼: " + attack.skill + " 技能等級: " + player.getSkillLevel(attack.skill) + " 攻擊次數 : " + attack.hits + " 正常攻擊次數 :" + last);
            }
            return;
        }

        if (effect.getBulletCount() > 1) {
            if ((attack.hits > effect.getBulletCount()) || (attack.targets > effect.getMobCount())) {
                player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
                System.out.println("Return 9");
                return;
            }

        } else if (((attack.hits > effect.getAttackCount()) && (effect.getAttackCount() != 0)) || (attack.targets > effect.getMobCount())) {
            player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
            System.out.println("Return 10");
            return;
        }

        if ((attack.hits > 0) && (attack.targets > 0) && (!player.getStat().checkEquipDurabilitys(player, -1))) {
            player.dropMessage(5, "一个项目已经用完的耐久性，但没有库存室去o.");
            System.out.println("Return 11");
            return;
        }

        if (GameConstants.isMulungSkill(attack.skill)) {
            if (player.getMapId() / 10000 != 92502) {
                return;
            }
            if (player.getMulungEnergy() < 300) {
                return;
            }
            player.mulung_EnergyModify(false);
        } else if (GameConstants.isPyramidSkill(attack.skill)) {
            if (player.getMapId() / 1000000 != 926) {
                return;
            }

            if ((player.getPyramidSubway() != null) && (player.getPyramidSubway().onSkillUse(player)));
        } else if ((GameConstants.isInflationSkill(attack.skill)) && (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null)) {
            return;
        }

        if (player.getClient().getChannelServer().isAdminOnly()) {
            player.dropMessage(-1, new StringBuilder().append("Animation: ").append(Integer.toHexString((attack.display & 0x8000) != 0 ? attack.display - 32768 : attack.display)).toString());
        }
        PlayerStats stats = player.getStat();

        Element element = player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null ? Element.NEUTRAL : theSkill.getElement();

        double MaxDamagePerHit = 0.0D;
        int totDamage = 0;

        int CriticalDamage = stats.passive_sharpeye_percent();
        Skill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
        int eaterLevel = player.getTotalSkillLevel(eaterSkill);

        MapleMap map = player.getMap();
        for (AttackPair oned : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(oned.objectid);

            if ((monster != null) && (monster.getLinkCID() <= 0)) {
                boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) && (!monster.getStats().isBoss());
                int totDamageToOneMonster = 0;
                MapleMonsterStats monsterstats = monster.getStats();
                int fixeddmg = monsterstats.getFixedDamage();
                if ((!Tempest) && (!player.isGM())) {
                    if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) {
                        MaxDamagePerHit = CalculateMaxMagicDamagePerHit(player, theSkill, monster, monsterstats, stats, element, CriticalDamage, maxDamagePerHit, effect);
                    } else {
                        MaxDamagePerHit = 1.0D;
                    }
                }
                byte overallAttackCount = 0;

                for (Pair eachde : oned.attack) {
                    Integer eachd = (Integer) eachde.left;
                    overallAttackCount++;
                    /* 确认是否超过预计伤害*/
                    if (!GameConstants.isElseSkill(attack.skill)) {
                        if (GameConstants.Novice_Skill(attack.skill)) {//新手技能
                            int lv = player.getSkillLevel(attack.skill);
                            MapleStatEffect eff = SkillFactory.getSkill(attack.skill).getEffect(lv);
                            if (!player.haveItem(eff.getItemCon(), eff.getItemConNo(), false, true)) {
                                FileoutputUtil.logToFile("Hack/Ban/修改技能WZ.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家<" + player.getLevel() + ">: " + player.getName() + " 修改技能WZ。没有锅牛壳使用投掷术怪物" + monster.getId() + " 地图: " + player.getMapId() + " 技能代码: " + attack.skill + " 技能等级" + lv + " 预计伤害: " + (int) maxDamagePerHit + "是否为BOSS: " + monster.getStats().isBoss());
                                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封锁系统] " + player.getName() + " 因为使用不法程式而被管理员永久停权。"));
                                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 密语系统] " + player.getName() + " (等级" + player.getLevel() + ") " + "修改技能WZ。没有锅牛壳使用投掷术技能ID " + attack.skill + " 技能等级" + lv));
                                player.ban(player.getName() + "修改技能WZ", true, true, false);
                                player.getClient().getSession().close();
                                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
                                return;
                            }
                            int fixdam = SkillFactory.getSkill(attack.skill).getEffect(lv).getFixDamage();

                            // 此处是封号处理，根据异常伤害进行封号。 20/8/16 ez
//                            if (eachd > fixdam) {
//                                FileoutputUtil.logToFile("Hack/Ban/伤害异常.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家<" + player.getLevel() + ">: " + player.getName() + " 怪物" + monster.getId() + " 地图: " + player.getMapId() + " 技能代码: " + attack.skill + " 技能等级" + lv + " 最高伤害: " + fixdam + " 本次伤害:" + eachd + " 预计伤害: " + (int) maxDamagePerHit + "是否为BOSS: " + monster.getStats().isBoss());
//                                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[系统封号] " + player.getName() + " 系统扫描伤害异常自动封号永久停权处理。。"));
//                                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 密语系统] " + player.getName() + " (等级" + player.getLevel() + ") " + "伤害异常。 " + "最高伤害" + fixdam + " 本次伤害" + eachd + " 技能ID " + attack.skill + " 技能等级" + lv));
//                                player.ban(player.getName() + "伤害异常", true, true, false);
//                                player.getClient().getSession().close();
//                                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
//                                return;
//                            }
                        }
                        int atk = 200000;
                        if ((player.getLevel() >= 10)) {
                            boolean ban = false;
                            if (player.getLevel() <= 15) {
                                atk = 600;
                            } else if (player.getLevel() <= 20) {
                                atk = 1000;
                            } else if (player.getLevel() <= 30) {
                                atk = 2500;
                            } else if (player.getLevel() <= 60) {
                                atk = 8000;
                            }
                            if (eachd >= atk && eachd > maxDamagePerHit) {
                                ban = true;
                            }
                            if (eachd == monster.getMobMaxHp()) {
                                ban = false;
                            }
                            if (player.hasGmLevel(1)) {
                                ban = false;
                            }
                            // 此处是封号处理，在攻击力足够高的情况下进行封号。20/8/16 ez
//                            if (ban) {
//                                boolean apple = false;
//                                if (player.getBuffSource(MapleBuffStat.WATK) == 2022179 || player.getBuffSource(MapleBuffStat.MATK) == 2022179 || player.getBuffSource(MapleBuffStat.WDEF) == 2022179) {
//                                    apple = true;
//                                }
//                                FileoutputUtil.logToFile("Hack/Ban/伤害异常.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家<" + player.getLevel() + ">: " + player.getName() + " 怪物" + monster.getId() + " 地图: " + player.getMapId() + " 技能代码: " + attack.skill + " 最高伤害: " + atk + " 本次伤害:" + eachd + " 预计伤害: " + (int) maxDamagePerHit + "是否为BOSS: " + monster.getStats().isBoss() + " 紫色苹果: " + apple);
//                                World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "[封锁系统] " + player.getName() + " 因为伤害异常而被管理员永久停权。"));
//                                World.Broadcast.broadcastGMMessage(CWvsContext.serverNotice(6, "[GM 密语系统] " + player.getName() + " (等级" + player.getLevel() + ") " + "伤害异常。 " + "最高伤害" + atk + " 本次伤害" + eachd + " 技能ID " + attack.skill));
//                                player.ban(player.getName() + "伤害异常", true, true, false);
//                                player.getClient().getSession().close();
//                                FileoutputUtil.logToFile(FileoutputUtil.DC_Log, "\r\n伺服器主动断开用户端连接，调用位置: " + new java.lang.Throwable().getStackTrace()[0]);
//                                return;
//                            }
                        }
                    }
                    if (fixeddmg != -1) {
                        eachd = monsterstats.getOnlyNoramlAttack() ? 0 : fixeddmg;
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = 0;
                    } else if (!player.isGM()) {

                        if (Tempest) {
                            if (eachd > monster.getMobMaxHp()) {
                                eachd = (int) Math.min(monster.getMobMaxHp(), Integer.MAX_VALUE);
                                player.getCheatTracker().registerOffense(CheatingOffense.魔法伤害过高);
                            }
                        } else if (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY) && !monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) {
                            if (eachd > MaxDamagePerHit) {
                                player.getCheatTracker().registerOffense(CheatingOffense.魔法伤害过高, "[傷害: " + eachd + ", 預計: " + (long) MaxDamagePerHit + ", 怪物: " + monster.getId() + "] [職業: " + player.getJob() + ", 等級: " + player.getLevel() + ", 技能: " + attack.skill + "]");

                                // 檢測相同商害
                                if (attack.real) {
                                    player.getCheatTracker().checkSameDamage(eachd, MaxDamagePerHit);
                                }

                                if (eachd > MaxDamagePerHit * 2) {
                                    if (ServerConstants.LOG_DAMAGE) {
                                        FileoutputUtil.logToFile("hack/伤害计算/魔法伤害计算修正.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 职业: " + player.getJob() + " 怪物:" + monster.getId() + " 技能: " + attack.skill + " 封包伤害 :" + eachd + " 预计伤害 :" + (int) maxDamagePerHit + "是否為BOSS: " + monster.getStats().isBoss(), false, true);
                                    }
                                    player.getCheatTracker().registerOffense(CheatingOffense.魔法伤害过高2, "[伤害: " + eachd + ", 预计: " + (long) MaxDamagePerHit + ", 怪物: " + monster.getId() + "] [职业: " + player.getJob() + ", 等级: " + player.getLevel() + ", 技能: " + attack.skill + "]");
                                    eachd = (int) (MaxDamagePerHit * 2); // 轉換為伺服器計算的傷害
                                }
                            }
                        } else if (eachd > MaxDamagePerHit) {
                            eachd = (int) (MaxDamagePerHit);
                            if (ServerConstants.LOG_DAMAGE) {
                                FileoutputUtil.logToFile("hack/伤害计算/魔法伤害计算修正.txt", "\r\n " + FileoutputUtil.CurrentReadable_TimeGMT() + " 玩家: " + player.getName() + "(" + player.getLevel() + ") 职业: " + player.getJob() + " 怪物:" + monster.getId() + " 技能: " + attack.skill + " 封包伤害 :" + eachd + " 预计伤害 :" + (int) maxDamagePerHit + "是否為BOSS: " + monster.getStats().isBoss(), false, true);
                            }
                        }

                        /*if (Tempest) {
                         if (eachd > monster.getMobMaxHp()) {
                         eachd = (int) Math.min(monster.getMobMaxHp(), 2147483647L);
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC);
                         }
                         } else if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) {
                         if (eachd > MaxDamagePerHit) {
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                         if (attack.real) {
                         player.getCheatTracker().checkSameDamage(eachd, MaxDamagePerHit);
                         }
                         if (eachd > MaxDamagePerHit * 2.0D) {
                         player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC_2, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                         eachd = (int) (MaxDamagePerHit * 2.0D);

                         if (eachd >= 999999) {
                         player.getClient().getSession().close();
                         }
                         }
                         }

                         } else if (eachd > MaxDamagePerHit) {
                         eachd = (int) MaxDamagePerHit;
                         }*/
                    }

                    totDamageToOneMonster += eachd;
                }

                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
                    player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[Distance: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", Expected Distance: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" Job: ").append(player.getJob()).append("]").toString());
                    return;
                }
                if ((attack.skill == 2301002) && (!monsterstats.getUndead())) {
                    player.getCheatTracker().registerOffense(CheatingOffense.HEAL_ATTACKING_UNDEAD);
                    return;
                }
                if (attack.skill != 1221011) {
                    monster.damage(player, totDamageToOneMonster, true, attack.skill);
                } else {
                    if (monster.getHp() == 1) {
                        monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    }
                    monster.damage(player, (int) (monster.getStats().isBoss() ? 99999 : monster.getHp() - 1), true, attack.skill);
                }
                if (eaterLevel > 0) {
                    eaterSkill.getEffect(eaterLevel).applyPassive(player, monster);
                }
                //monster.damage(player, totDamage, true, attack.skill);
            }

            if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
                CheatTracker tracker = player.getCheatTracker();
                tracker.setAttacksWithoutHit(true);

                if (tracker.getAttacksWithoutHit() > 1000) {
                    tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
                }
            }
            if ((player.getJob() == 220) || (player.getJob() == 221) || (player.getJob() == 222)) {
                if ((attack.skill == 2201004) || (attack.skill == 2221003) || (attack.skill == 2211002) || (attack.skill == 2221007) || (attack.skill == 2211006)) {
                    Skill skill = SkillFactory.getSkill(attack.skill);
                    MapleStatEffect eff = skill.getEffect(player.getTotalSkillLevel(skill));
                    MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, Integer.valueOf(1), skill.getId(), null, false);
                    monster.applyStatus(player, monsterStatusEffect, false, 2, true, eff);
                }
            }
            if ((player.getJob() == 210) || (player.getJob() == 211) || (player.getJob() == 212)) {
                if ((attack.skill == 2111006) || (attack.skill == 2101005)) {
                    Skill skill = SkillFactory.getSkill(attack.skill);
                    MapleStatEffect eff = skill.getEffect(player.getTotalSkillLevel(skill));
                    MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, skill.getId(), null, false);
                    monster.applyStatus(player, monsterStatusEffect, true, eff.getDuration(), true, eff);
                }
            }
        }

        if (attack.skill
                != 2301002) {
            effect.applyTo(player);
        }

    }

    private static double CalculateMaxMagicDamagePerHit(MapleCharacter chr, Skill skill, MapleMonster monster, MapleMonsterStats mobstats, PlayerStats stats, Element elem, Integer sharpEye, double maxDamagePerMonster, MapleStatEffect attackEffect) {
        int dLevel = Math.max(mobstats.getLevel() - chr.getLevel(), 0) * 2;
        int HitRate = Math.min((int) Math.floor(Math.sqrt(stats.getAccuracy())) - (int) Math.floor(Math.sqrt(mobstats.getEva())) + 100, 100);
        if (dLevel > HitRate) {
            HitRate = dLevel;
        }
        HitRate -= dLevel;
        if ((HitRate <= 0) && ((!GameConstants.isBeginnerJob(skill.getId() / 10000)) || (skill.getId() % 10000 != 1000))) {
            return 0.0D;
        }

        int CritPercent = sharpEye;
        ElementalEffectiveness ee = monster.getEffectiveness(elem);
        double elemMaxDamagePerMob;
        switch (ee) {
            case IMMUNE:
                elemMaxDamagePerMob = 1.0D;
                break;
            default:
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * ee.getValue(), stats);
        }

        int MDRate = monster.getStats().getMDRate();
        MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.MDEF);
        if (pdr != null) {
            MDRate += pdr.getX();
        }
        elemMaxDamagePerMob -= elemMaxDamagePerMob * (Math.max(MDRate - stats.ignoreTargetDEF - attackEffect.getIgnoreMob(), 0) / 100.0D);

        elemMaxDamagePerMob += elemMaxDamagePerMob / 100.0D * CritPercent;

        elemMaxDamagePerMob *= (monster.getStats().isBoss() ? chr.getStat().bossdam_r : chr.getStat().dam_r) / 100.0D;
        MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
        if (imprint != null) {
            elemMaxDamagePerMob += elemMaxDamagePerMob * imprint.getX() / 100.0D;
        }
        elemMaxDamagePerMob += elemMaxDamagePerMob * chr.getDamageIncrease(monster.getObjectId()) / 100.0D;
        if (GameConstants.isBeginnerJob(skill.getId() / 10000)) {
            switch (skill.getId() % 10000) {
                case 1000:
                    elemMaxDamagePerMob = 40.0D;
                    break;
                case 1020:
                    elemMaxDamagePerMob = 1.0D;
                    break;
                case 1009:
                    elemMaxDamagePerMob = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
            }
        }

        if (elemMaxDamagePerMob > 999999.0D) {
            elemMaxDamagePerMob = 999999.0D;
        } else if (elemMaxDamagePerMob <= 0.0D) {
            elemMaxDamagePerMob = 1.0D;
        }

        return elemMaxDamagePerMob;
    }

    private static double ElementalStaffAttackBonus(Element elem, double elemMaxDamagePerMob, PlayerStats stats) {
        switch (elem) {
            case FIRE:
                return elemMaxDamagePerMob / 100.0D * (stats.element_fire + stats.getElementBoost(elem));
            case ICE:
                return elemMaxDamagePerMob / 100.0D * (stats.element_ice + stats.getElementBoost(elem));
            case LIGHTING:
                return elemMaxDamagePerMob / 100.0D * (stats.element_light + stats.getElementBoost(elem));
            case POISON:
                return elemMaxDamagePerMob / 100.0D * (stats.element_psn + stats.getElementBoost(elem));
        }
        return elemMaxDamagePerMob / 100.0D * (stats.def + stats.getElementBoost(elem));
    }

    private static void handlePickPocket(MapleCharacter player, MapleMonster mob, AttackPair oned) {
        int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET);

        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if ((player.getStat().pickRate >= 100) || (Randomizer.nextInt(99) < player.getStat().pickRate)) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }

    private static double CalculateMaxWeaponDamagePerHit(MapleCharacter player, MapleMonster monster, AttackInfo attack, Skill theSkill, MapleStatEffect attackEffect, double maximumDamageToMonster, Integer CriticalDamagePercent) {
        int dLevel = Math.max(monster.getStats().getLevel() - player.getLevel(), 0) * 2;
        int HitRate = Math.min((int) Math.floor(Math.sqrt(player.getStat().getAccuracy())) - (int) Math.floor(Math.sqrt(monster.getStats().getEva())) + 100, 100);
        if (dLevel > HitRate) {
            HitRate = dLevel;
        }
        HitRate -= dLevel;
        if ((HitRate <= 0) && ((!GameConstants.isBeginnerJob(attack.skill / 10000)) || (attack.skill % 10000 != 1000)) && (!GameConstants.isPyramidSkill(attack.skill)) && (!GameConstants.isMulungSkill(attack.skill)) && (!GameConstants.isInflationSkill(attack.skill))) {
            return 0.0D;
        }
        if ((player.getMapId() / 1000000 == 914) || (player.getMapId() / 1000000 == 927)) {
            return 999999.0D;
        }

        List<Element> elements = new ArrayList();
        boolean defined = false;
        int CritPercent = CriticalDamagePercent;
        int PDRate = monster.getStats().getPDRate();
        MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.WDEF);
        if (pdr != null) {
            PDRate += pdr.getX();
        }
        if (theSkill != null) {
            elements.add(theSkill.getElement());
            if (GameConstants.isBeginnerJob(theSkill.getId() / 10000)) {
                switch (theSkill.getId() % 10000) {
                    case 1000:
                        maximumDamageToMonster = 40.0D;
                        defined = true;
                        break;
                    case 1020:
                        maximumDamageToMonster = 1.0D;
                        defined = true;
                        break;
                    case 1009:
                        maximumDamageToMonster = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
                        defined = true;
                }
            }

            switch (theSkill.getId()) {
                //case 1311005:
                //PDRate = monster.getStats().isBoss() ? PDRate : 0;
                //break;
                case 3221001:
                case 33101001:
                    maximumDamageToMonster *= attackEffect.getMobCount();
                    defined = true;
                    break;
                case 3101005:
                    defined = true;
                    break;
                case 32001000:
                case 32101000:
                case 32111002:
                case 32121002:
                    maximumDamageToMonster *= 1.5D;
                    break;
                case 3221007:
                case 4331003:
                case 23121003:
                    if (!monster.getStats().isBoss()) {
                        maximumDamageToMonster = monster.getMobMaxHp();
                        defined = true;
                    }
                    break;
                case 1221011:
                case 21120006:
                    maximumDamageToMonster = monster.getStats().isBoss() ? 99999.0D : monster.getHp() - 1;
                    defined = true;
                    break;
                case 3211006:
                    if (monster.getStatusSourceID(MonsterStatus.FREEZE) == 3211003) {
                        defined = true;
                        maximumDamageToMonster = 999999.0D;
                    }
                    break;
            }
        }
        double elementalMaxDamagePerMonster = maximumDamageToMonster;
        if ((player.getJob() == 311) || (player.getJob() == 312) || (player.getJob() == 321) || (player.getJob() == 322)) {
            Skill mortal = SkillFactory.getSkill((player.getJob() == 311) || (player.getJob() == 312) ? 3110001 : 3210001);
            if (player.getTotalSkillLevel(mortal) > 0) {
                MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
                if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
                    elementalMaxDamagePerMonster = 999999.0D;
                    defined = true;
                    if (mort.getZ() > 0) {
                        player.addHP(player.getStat().getMaxHp() * mort.getZ() / 100);
                    }
                }
            }
        } else if ((player.getJob() == 221) || (player.getJob() == 222)) {
            Skill mortal = SkillFactory.getSkill(2210000);
            if (player.getTotalSkillLevel(mortal) > 0) {
                MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
                if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
                    elementalMaxDamagePerMonster = 999999.0D;
                    defined = true;
                }
            }
        }
        if ((!defined) || ((theSkill != null) && ((theSkill.getId() == 33101001) || (theSkill.getId() == 3221001)))) {
            if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
                int chargeSkillId = player.getBuffSource(MapleBuffStat.WK_CHARGE);

                switch (chargeSkillId) {
                    case 1211003:
                    case 1211004:
                        elements.add(Element.FIRE);
                        break;
                    case 1211005:
                    case 1211006:
                    case 21111005:
                        elements.add(Element.ICE);
                        break;
                    case 1211007:
                    case 1211008:
                    case 15101006:
                        elements.add(Element.LIGHTING);
                        break;
                    case 1221003:
                    case 1221004:
                    case 11111007:
                        elements.add(Element.HOLY);
                        break;
                    case 12101005:
                }

            }

            if (player.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
                elements.add(Element.LIGHTING);
            }
            if (player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null) {
                elements.clear();
            }
            double elementalEffect;
            if (elements.size() > 0) {
                switch (attack.skill) {
                    case 3111003:
                    case 3211003:
                        elementalEffect = attackEffect.getX() / 100.0D;
                        break;
                    default:
                        elementalEffect = 0.5D / elements.size();
                }

                for (Element element : elements) {
                    switch (monster.getEffectiveness(element)) {
                        case IMMUNE:
                            elementalMaxDamagePerMonster = 1.0D;
                            break;
                        case WEAK:
                            elementalMaxDamagePerMonster *= (1.0D + elementalEffect + player.getStat().getElementBoost(element));
                            break;
                        case STRONG:
                            elementalMaxDamagePerMonster *= (1.0D - elementalEffect - player.getStat().getElementBoost(element));
                    }

                }

            }

            elementalMaxDamagePerMonster -= elementalMaxDamagePerMonster * (Math.max(PDRate - Math.max(player.getStat().ignoreTargetDEF, 0) - Math.max(attackEffect == null ? 0 : attackEffect.getIgnoreMob(), 0), 0) / 100.0D);

            elementalMaxDamagePerMonster += elementalMaxDamagePerMonster / 100.0D * CritPercent;

            MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
            if (imprint != null) {
                elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * imprint.getX() / 100.0D;
            }

            elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * player.getDamageIncrease(monster.getObjectId()) / 100.0D;
            elementalMaxDamagePerMonster *= ((monster.getStats().isBoss()) && (attackEffect != null) ? player.getStat().bossdam_r + attackEffect.getBossDamage() : player.getStat().dam_r) / 100.0D;
        }
        if (elementalMaxDamagePerMonster > 999999.0D) {
            if (!defined) {
                elementalMaxDamagePerMonster = 999999.0D;
            }
        } else if (elementalMaxDamagePerMonster <= 0.0D) {
            elementalMaxDamagePerMonster = 1.0D;
        }
        return elementalMaxDamagePerMonster;
    }

    public static final AttackInfo DivideAttack(final AttackInfo attack, final int rate) {
        attack.real = false;
        if (rate <= 1) {
            return attack; //lol
        }
        for (AttackPair p : attack.allDamage) {
            if (p.attack != null) {
                for (Pair<Integer, Boolean> eachd : p.attack) {
                    eachd.left /= rate; //too ex.
                }
            }
        }
        return attack;
    }

    public static final AttackInfo Modify_AttackCrit(AttackInfo attack, MapleCharacter chr, int type, MapleStatEffect effect) {
        int CriticalRate;
        boolean shadow;
        List damages;
        List damage;
        if ((attack.skill != 4211006) && (attack.skill != 3211003) && (attack.skill != 4111004)) {
            CriticalRate = chr.getStat().passive_sharpeye_rate() + (effect == null ? 0 : effect.getCr());
            shadow = (chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) && ((type == 1) || (type == 2));
            damages = new ArrayList();
            damage = new ArrayList();

            for (AttackPair p : attack.allDamage) {
                if (p.attack != null) {
                    int hit = 0;
                    int mid_att = shadow ? p.attack.size() / 2 : p.attack.size();

                    int toCrit = (attack.skill == 4221001) || (attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 4341005) || (attack.skill == 4331006) || (attack.skill == 21120005) ? mid_att : 0;
                    if (toCrit == 0) {
                        for (Pair eachd : p.attack) {
                            if ((!((Boolean) eachd.right)) && (hit < mid_att)) {
                                if ((((Integer) eachd.left) > 999999) || (Randomizer.nextInt(100) < CriticalRate)) {
                                    toCrit++;
                                }
                                damage.add(eachd.left);
                            }
                            hit++;
                        }
                        if (toCrit == 0) {
                            damage.clear();
                        } else {
                            Collections.sort(damage);
                            for (int i = damage.size(); i > damage.size() - toCrit; i--) {
                                damages.add(damage.get(i - 1));
                            }
                            damage.clear();
                        }
                    } else {
                        hit = 0;
                        for (Pair eachd : p.attack) {
                            if (!((Boolean) eachd.right)) {
                                if (attack.skill == 4221001) {
                                    eachd.right = hit == 3;
                                } else if ((attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 21120005) || (attack.skill == 4341005) || (attack.skill == 4331006) || (((Integer) eachd.left) > 999999)) {
                                    eachd.right = true;
                                } else if (hit >= mid_att) {
                                    eachd.right = ((Pair) p.attack.get(hit - mid_att)).right;
                                } else {
                                    eachd.right = damages.contains(eachd.left);
                                }
                            }
                            hit++;
                        }
                        damages.clear();
                    }
                }
            }
        }
        return attack;
    }

    public static final AttackInfo parseDmgMa(final LittleEndianAccessor lea, final MapleCharacter chr) {
        final AttackInfo ret = new AttackInfo();
        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8);
        ret.skill = lea.readInt();
        lea.skip(12);
        if (ret.skill >= 91000000 && ret.skill >= 200000000) { //guild/recipe? no
            System.out.println("Return 1");
            return null;
        }
        if (GameConstants.isMagicChargeSkill(ret.skill)) {
            ret.charge = lea.readInt();
        } else {
            ret.charge = -1;
        }
        ret.unk = lea.readByte();
        ret.display = lea.readUShort();
        lea.skip(1);
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<AttackPair>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            //if (chr.getMap().isTown()) {
            //    final MapleMonster od = chr.getMap().getMonsterByOid(oid);
            //    if (od != null && od.getLinkCID() > 0) {
            //	    return null;
            //    }
            //}
            lea.skip(4);
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort();

            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();

            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
                allDamageNumbers.add(new Pair<>(damage, false));
                //System.out.println("parseDmgMa Damage: " + damage);
            }
            lea.readInt(); //idk
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers));
        }
        ret.position = lea.readPos();
        //System.out.println("Return ret");
        return ret;
    }

    public static AttackInfo parseDmgMa1(LittleEndianAccessor lea, MapleCharacter chr) // magic
    {
        AttackInfo ret = new AttackInfo();
        lea.skip(1);
        ret.tbyte = lea.readByte();

        ret.targets = ((byte) (ret.tbyte >>> 4 & 0xF));
        ret.hits = ((byte) (ret.tbyte & 0xF));
        ret.skill = lea.readInt();
        if (ret.skill >= 91000000 && ret.skill >= 200000000) {
            return null;
        }

        lea.skip(1); // 1
        lea.readInt(); // 5
        lea.readShort(); // 7 
        if (GameConstants.isMagicChargeSkill(ret.skill)) {
            ret.charge = lea.readInt();
        } else {
            ret.charge = -1;
        }
        ret.unk = lea.readByte();
        ret.display = lea.readUShort();

        lea.skip(4);
        ret.speed = lea.readByte();
        ret.lastAttackTickCount = lea.readInt(); // FD 53 13 07
        lea.skip(4); //

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(20);
            allDamageNumbers = new ArrayList<>();

            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(8);
            //lea.readInt();
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers));
        }
        ret.position = lea.readPos();
        lea.skip(1); // v146 ?
        return ret;
    }

    public static AttackInfo parseDmgM(LittleEndianAccessor lea, MapleCharacter chr)//reg att
    {
        AttackInfo ret = new AttackInfo();
        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();

        ret.targets = ((byte) (ret.tbyte >>> 4 & 0xF));
        ret.hits = ((byte) (ret.tbyte & 0xF));
        lea.skip(8);
        ret.skill = lea.readInt();
        lea.skip(12);
        switch (ret.skill) {
            case 5101004:
            case 5201002:
                ret.charge = lea.readInt();
            default:
                ret.charge = 0;
                break;
        }

        ret.unk = lea.readByte();
        ret.display = lea.readUShort();
        lea.skip(1);
        ret.speed = lea.readByte();
        ret.lastAttackTickCount = lea.readInt();

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<>();
        if (ret.skill == 4211006) {
            return parseExplosionAttack(lea, ret, chr);
        }
        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(4);
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.readShort();
            lea.skip(2);
            allDamageNumbers = new ArrayList<>();

            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(4);
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers));
        }
        ret.position = lea.readPos();
        return ret;
    }

    public static AttackInfo parseDmgR(LittleEndianAccessor lea, MapleCharacter chr)//ranged att
    {

        AttackInfo ret = new AttackInfo();
        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        ret.targets = ((byte) (ret.tbyte >>> 4 & 0xF));
        ret.hits = ((byte) (ret.tbyte & 0xF));
        lea.skip(8);
        ret.skill = lea.readInt();
        lea.skip(12);
        switch (ret.skill) {
            case 3121004:
            case 3221001:
            case 5321052:
            case 5221004:
            case 5311002:
            case 5711002:
            case 5721001:
            case 3101008:
            case 3111009:// Hurricane
            case 3121013:// Arrow Blaster
                lea.skip(4);
        }

        ret.charge = -1;
        ret.unk = lea.readByte();
        ret.display = lea.readUShort();
        lea.skip(1);
        ret.speed = lea.readByte();
        ret.lastAttackTickCount = lea.readInt();
        ret.slot = ((byte) lea.readShort());//飞镖
        ret.csstar = ((byte) lea.readShort());//飞镖点商
        ret.AOE = lea.readByte();
        ret.allDamage = new ArrayList<>();

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<>();

        for (int i = 0; i < ret.targets; i++) {

            oid = lea.readInt();
            lea.skip(14);
            allDamageNumbers = new ArrayList<>();

            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(4);
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers));
        }
        lea.skip(4);
        ret.position = lea.readPos();
        return ret;
    }

    public static AttackInfo parseExplosionAttack(LittleEndianAccessor lea, AttackInfo ret, MapleCharacter chr) {
        if (ret.hits == 0) {
            lea.skip(4);
            byte bullets = lea.readByte();
            for (int j = 0; j < bullets; j++) {
                ret.allDamage.add(new AttackPair(lea.readInt(), null));
                lea.skip(1);
            }
            lea.skip(2);
            return ret;
        }
        for (int i = 0; i < ret.targets; i++) {
            int oid = lea.readInt();
            lea.skip(12);
            byte bullets = lea.readByte();
            List allDamageNumbers = new ArrayList();
            for (int j = 0; j < bullets; j++) {
                int damage = lea.readInt();
                allDamageNumbers.add(new Pair(damage, false));
            }
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers));
            lea.skip(4);
        }
        lea.skip(4);
        byte bullets = lea.readByte();

        for (int j = 0; j < bullets; j++) {
            ret.allDamage.add(new AttackPair(lea.readInt(), null));
            lea.skip(1);
        }
        lea.skip(2);
        return ret;
    }
}
