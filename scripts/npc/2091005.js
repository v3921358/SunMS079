/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;
var sel;
var mapid;
var selectedBuff;
var belts = Array(1132000, 1132001, 1132002, 1132003, 1132004)
var belt_level = Array(25, 35, 45, 60, 75, 120, 120, 90);
var belt_points = Array(500, 2500, 5000, 8000, 10000);

function start() {
    mapid = cm.getMapId();
    if (mapid == 925020001) {
        cm.sendSimple("#e<道场副本:武陵道场38层>#n\n\r 我的师傅是武陵道场最强大的人,他住在道场最后一层！你是在告诉我你想挑战我伟大的师傅吗？不要说我没有警告过你. \r #b#L0# 我想单独挑战.#l \n\r #L1# 我想组队挑战.#l \n\r #L2# 道场腰带兑换.#l \n\r #L3# 重置我修炼点.#l \n\r #L5# 解释武陵道场?#l");
    } else if (isRestingSpot(mapid)) {
        cm.sendSimple("我很惊讶你走了这么远！但它不会很容易从这里出来。你仍然想要挑战? " + (cm.getPlayer().getInfoQuest(7218).equals("1") ? "" : "") + "\n\r #b#L0#我想继续.#l \n\r #L1#我想离开.#l");
    } else {
        cm.sendYesNo("什么？你已经准备好退出了吗？你确定要退出吗?");
    }
}

function action(mode, type, selection) {
    if (mapid == 925020001) {
        if (mode == 1) {
            status++;
        } else {
            cm.dispose();
            return;
        }
        if (status == 0) {
            sel = selection;

            if (sel == 5) {
                cm.sendNext("我的师傅是最强大的人在道场，他是负责建立这个惊人的道场训练塔。道场训练塔是一个巨大的训练设施，由38层楼组成。每层代表不一样的困难度。当然，看起来你这么弱小，达到顶层将是不可能的...");
                cm.dispose();
            } else if (sel == 3) {
                cm.sendYesNo("你知道如果你重新设置你的修炼点，然后它会回到0，你知道吗？我可以诚实地说，这不一定是一件坏事。一旦你重新设置你的修炼点，重新开始，然后你就可以再次兑换腰带了。你想重新设置你的修炼点吗?");
            } else if (sel == 2) {
                cm.sendSimple("你的总共训练点到目前为止 #b"+cm.getDojoPoints()+"#k. 我的师傅爱有天赋的人，所以如果你机架足够的训练点，你将能够收到一个带你的训练点的基础上...\n\r #L0##i1132000:# #t1132000#(500)#l \n\r #L1##i1132001:# #t1132001#(2500)#l \n\r #L2##i1132002:# #t1132002#(5000)#l \n\r #L3##i1132003:# #t1132003#(8000)#l \n\r #L4##i1132004:# #t1132004#(10000)#l");
            } else if (sel == 1) {
                if (cm.getParty() != null) {
                    if (cm.isLeader()) {
                        cm.sendYesNo("你现在愿意进来吗?");
                    } else {
                        cm.sendOk("嘿，你甚至不是你的队伍队长。你想偷偷溜进来什么？告诉你的队长让他跟我说话，如果你想进入的前提...");
                    }
                } else {
					cm.sendOk("请先创建属于你的队伍");
					cm.dispose();
				}
            } else if (sel == 0) {
                if (cm.getParty() != null) {
                    cm.sendOk("请离开你的队伍.");
                    cm.dispose();
					return;
                }
                var record = cm.getQuestRecord(150000);
                var data = record.getCustomData();

                if (data != null) {
                    var idd = get_restinFieldID(parseInt(data));
                    if (idd != 925020002) {
                        cm.dojoAgent_NextMap(true, true, idd);
                        record.setCustomData(null);
                    } else {
                        cm.sendOk("请稍后再试.");
                    }
                } else {
                    cm.start_DojoAgent(true, false);
                }
                cm.getPlayer().updateInfoQuest(7218, "1");
                cm.dispose();
  }
        } else if (status == 1) {
            if (sel == 3) {
                cm.getQuestRecord(150100).gainCustomData(-cm.getDojoPoints());
                cm.sendOk("我把你的训练点重新设置为#r0.");
				cm.dispose();
            } else if (sel == 2) {
                var belt = belts[selection];
                var level = belt_level[selection];
                var points = belt_points[selection];
                if (cm.getDojoPoints() >= points) {
                    if (cm.getPlayerStat("LVL") >= level) {
                        if (cm.canHold(belt)) {
                            cm.gainItem(belt, 1);
                            cm.getQuestRecord(150100).gainCustomData(-points);
							cm.sendOk("兑换成功！");
							cm.dispose();
                        } else {
                            cm.sendOk("背包空间不足.");
							 cm.dispose();
                        }
                    } else {
                        cm.sendOk("#b兑换#i" + belt + "# #t" + belt + "##r\r\n1.条件:#b" + level + "等级以上.\r\n#r2.条件:#k修炼点 #b" + points + " #k点数。\r\n\r\n你想要获得这腰带的话还需要更多的修练点。\r\n目前点数还差:#r" + (cm.getDojoPoints() - points) + "数.");
                 cm.dispose();
					}
                } else {
                    cm.sendOk("#b兑换#i" + belt + "# #t" + belt + "##r\r\n1.条件:#b" + level + "等级以上.\r\n#r2.条件:#k修炼点 #b" + points + " #k点数。\r\n\r\n你想要获得这腰带的话还需要更多的修练点。\r\n目前点数还差:#r" + (cm.getDojoPoints() - points) + "数.");
                 cm.dispose();
				}
                cm.dispose();
            } else if (sel == 1) {
                cm.start_DojoAgent(true, true);
                cm.dispose();
            }
        }
    } else if (isRestingSpot(mapid)) {
        if (mode == 1) {
            status++;
        } else {
            cm.dispose();
            return;
        }

        if (status == 0) {
            sel = selection;

            if (sel == 0) {
                if (cm.getParty() == null || cm.isLeader()) {
                    cm.dojoAgent_NextMap(true, true);
                } else {
                    cm.sendOk("只有队长可以继续.");
					cm.dispose();
                }
                //cm.getQuestRecord(150000).setCustomData(null);
                cm.dispose();
            } else if (sel == 1) {
                cm.askAcceptDecline("你想退出吗？你真的想离开这里?");
            } else if (sel == 4) {
                cm.sendSlideMenu(4, "#0#恢复 50% HP #1# 恢复 100% HP #2# MaxHP + 10000 (持续时间:10分钟) #3# 武器/魔法攻击 + 30 (持续时间:10分钟) #4# 武器/魔法攻击 + 60 (持续时间:10分钟) #5# 武器/魔法防御 + 2500 (持续时间:10分钟) #6# 武器/魔法防御 + 4000 (持续时间:10分钟) #7# 精度/结果 + 2000 (持续时间:10分钟) #8# 最大速度/跳跃 (持续时间:10分钟) #9# 攻击速度 + 1 (持续时间:10分钟)");
                cm.dispose();
            }
        } else if (status == 1) {
            if (sel == 1) {
                if (cm.isLeader()) {
                    cm.warpParty(925020002);
                } else {
                    cm.warp(925020002);
                }
                cm.getPlayer().updateInfoQuest(7218, "1");
                cm.dispose();
            } else if (sel == 4) {
                selectedBuff = 2022856 - 1 + selection;
                cm.sendYesNo("当你使用 #i" + selectedBuff + "# #t" + selectedBuff + "#, 不会得到任何额外的积分。那好吗？你只能选择一个物品每休息阶段，所以请明智地选择.");
            }
        } else if (status == 2) {
            if (sel == 4) {
                cm.getPlayer().updateInfoQuest(7218, "0");
                cm.useItem(selectedBuff);
            }
            cm.dispose();
        }
    } else {
        if (mode == 1) {
            if (cm.isLeader()) {
                cm.warpParty(925020002);
            } else {
                cm.warp(925020002);
            }
        }
        cm.dispose();
    }
}

function get_restinFieldID(id) {
    var idd = 925020002;
    switch (id) {
        case 1:
            idd =  925020600;//925031200 //925020600
            break;
        case 2:
            idd =  925031200;//925031200//925021200
            break;
        case 3:
            idd =  925021800;
            break;
        case 4:
            idd =  925022400;
            break;
        case 5:
            idd =  925023000;
            break;
        case 6:
            idd =  925023600;
            break;
        case 7:
            idd =  925024200;
            break;
    }
    for (var i = 0; i < 10; i++) {
        var canenterr = true;
        for (var x = 1; x < 39; x++) {
            var map = cm.getMap(925020000 + 100 * x + i);
            if (map.getCharactersSize() > 0) {
                canenterr = false;
                break;
            }
        }
        if (canenterr) {
            idd += i;
            break;
        }
    }
    return idd;
}

function get_stageId(mapid) {
    if (mapid >= 925020600 && mapid <= 925020614 || mapid >= 925030600 && mapid <= 925030614) {
        return 1;
    } else if (mapid >= 925021200 && mapid <= 925021214 || mapid >= 925031200 && mapid <= 925031214) {
        return 2;
    } else if (mapid >= 925021800 && mapid <= 925021814 || mapid >= 925031800 && mapid <= 925031814) {
        return 3;
    } else if (mapid >= 925022400 && mapid <= 925022414) {
        return 4;
    } else if (mapid >= 925023000 && mapid <= 925023014) {
        return 5;
    } else if (mapid >= 925023600 && mapid <= 925023614) {
        return 6;
    } else if (mapid >= 925024200 && mapid <= 925024214) {
        return 7;
    }
    return 0;
}

function isRestingSpot(id) {
    return (get_stageId(id) > 0);
}