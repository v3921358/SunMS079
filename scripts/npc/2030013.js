/*
	NPC Name: 		Adobis
	Map(s): 		El Nath : Entrance to Zakum Altar
	Description: 		Zakum battle starter
*/
var status = 0;

function start() {
    status =0;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getPlayer().getMapId() == 211042200) {
        if (selection < 100) {
            cm.sendSimple("#r#L100#Zakum#l\r\n#L101#Chaos Zakum#l");
        } else {
            if (selection == 100) {
                cm.warp(211042300,0);
            } else if (selection == 101) {
                cm.warp(211042301,0);
            }
            cm.dispose();
        }
        return;
    } else if (cm.getPlayer().getMapId() == 211042401) {
        switch (status) {
            case 0:
                if (cm.getPlayer().getLevel() < 100) {
                    cm.sendOk("你的等级必须达到100级以上.");
                    cm.dispose();
                    return;
                }
                if (cm.getPlayer().getClient().getChannel() != 2 && cm.getPlayer().getClient().getChannel() != 3) {
                    cm.sendOk("混乱扎昆只能试图在频道（2）（3）挑战.");
                    cm.dispose();
                    return;
                }
                var em = cm.getEventManager("ChaosZakum");

                if (em == null) {
                    cm.sendOk("事件还没有开始，请联系一个通用汽车公司.");
                    cm.safeDispose();
                    return;
                }
                var prop = em.getProperty("state");
                var marr = cm.getQuestRecord(160102);
                var data = marr.getCustomData();
                if (data == null) {
                    marr.setCustomData("0");
                    data = "0";
                }
                var time = parseInt(data);
                if (prop == null || prop.equals("0")) {
                    var squadAvailability = cm.getSquadAvailability("ChaosZak");
                    if (squadAvailability == -1) {
                        status = 1;
                        if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("你已经去了扎昆在过去的12小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                            cm.dispose();
                            return;
                        }
                        cm.sendYesNo("你有兴趣成为远征队的队长吗？");

                    } else if (squadAvailability == 1) {
                        if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("你已经去了扎昆在过去的12小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                            cm.dispose();
                            return;
                        }
                        // -1 = Cancelled, 0 = not, 1 = true
                        var type = cm.isSquadLeader("ChaosZak");
                        if (type == -1) {
                            cm.sendOk("远征队已经结束，请重新注册.");
                            cm.safeDispose();
                        } else if (type == 0) {
                            var memberType = cm.isSquadMember("ChaosZak");
                            if (memberType == 2) {
                                cm.sendOk("你被禁止参加远征队.");
                                cm.safeDispose();
                            } else if (memberType == 1) {
                                status = 5;
                                cm.sendSimple("你想做什么? \r\n#b#L0#检查成员#l \r\n#b#L1#加入远征队#l \r\n#b#L2#退出小队#l");
                            } else if (memberType == -1) {
                                cm.sendOk("队伍已经结束，请重新注册.");
                                cm.safeDispose();
                            } else {
                                status = 5;
                                cm.sendSimple("你想做什么? \r\n#b#L0#检查成员#l \r\n#b#L1#加入队伍#l \r\n#b#L2#退出小队#l");
                            }
                        } else { // Is leader
                            status = 10;
                            cm.sendSimple("你想做什么? \r\n#b#L0#检查成员#l \r\n#b#L1#删除成员#l \r\n#b#L2#编辑限制列表#l \r\n#r#L3#进入地图#l");
                        // TODO viewing!
                        }
                    } else {
                        var eim = cm.getDisconnected("ChaosZakum");
                        if (eim == null) {
                            var squd = cm.getSquad("ChaosZak");
                            if (squd != null) {
                                if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                    cm.sendOk("你已经去了扎昆在过去的12小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                                    cm.dispose();
                                    return;
                                }
                                cm.sendYesNo("队伍的对抗老板的战斗已经开始了.\r\n" + squd.getNextPlayer());
                                status = 3;
                            } else {
                                cm.sendOk("队伍的对抗老板的战斗已经开始了.");
                                cm.safeDispose();
                            }
                        } else {
                            cm.sendYesNo("啊，你回来了。你愿意加入你的队伍在战斗中吗?");
                            status = 2;
                        }
                    }
                } else {
                    var eim = cm.getDisconnected("ChaosZakum");
                    if (eim == null) {
                        var squd = cm.getSquad("ChaosZak");
                        if (squd != null) {
                            if (time + (12 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                cm.sendOk("你已经去了扎昆在过去的12小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (12 * 3600000)));
                                cm.dispose();
                                return;
                            }
                            cm.sendYesNo("队伍的对抗老板的战斗已经开始了.\r\n" + squd.getNextPlayer());
                            status = 3;
                        } else {
                            cm.sendOk("队伍的对抗老板的战斗已经开始了.");
                            cm.safeDispose();
                        }
                    } else {
                        cm.sendYesNo("啊，你回来了。你愿意加入你的队伍在战斗中吗?");
                        status = 2;
                    }
                }
                break;
            case 1:
                if (mode == 1) {
                    if (cm.registerSquad("ChaosZak", 5, " 已被任命为班长（混乱）。如果你想加入请在时间段内注册的远征队.")) {
                        cm.sendOk("你已经被任命为队伍的领袖。在接下来的5分钟，你可以加入远征队的成员.");
                    } else {
                        cm.sendOk("添加你的小队时发生了一个错误.");
                    }
                } else {
                    cm.sendOk("如果你想成为远征队的领队的话，跟我谈谈.")
                }
                cm.safeDispose();
                break;
            case 2:
                if (!cm.reAdd("ChaosZakum", "ChaosZak")) {
                    cm.sendOk("误差…请再试一次.");
                }
                cm.dispose();
                break;
            case 3:
                if (mode == 1) {
                    var squd = cm.getSquad("ChaosZak");
                    if (squd != null && !squd.getAllNextPlayer().contains(cm.getPlayer().getName())) {
                        squd.setNextPlayer(cm.getPlayer().getName());
                        cm.sendOk("你已经保留了现场.");
                    }
                }
                cm.dispose();
                break;
            case 5:
                if (selection == 0) {
                    if (!cm.getSquadList("ChaosZak", 0)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    } else {
                        cm.dispose();
                    }
                } else if (selection == 1) { // join
                    var ba = cm.addMember("ChaosZak", true);
                    if (ba == 2) {
                        cm.sendOk("队伍目前已满，请稍后再试。");
                        cm.safeDispose();
                    } else if (ba == 1) {
                        cm.sendOk("你已经成功加入了队伍");
                        cm.safeDispose();
                    } else {
                        cm.sendOk("你已经是队伍的一部分了.");
                        cm.safeDispose();
                    }
                } else {// withdraw
                    var baa = cm.addMember("ChaosZak", false);
                    if (baa == 1) {
                        cm.sendOk("你已经退出了队伍的成功");
                        cm.safeDispose();
                    } else {
                        cm.sendOk("你不是队伍的一部分.");
                        cm.safeDispose();
                    }
                }
                break;
            case 10:
                if (selection == 0) {
                    if (!cm.getSquadList("ChaosZak", 0)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                    }
                    cm.safeDispose();
                } else if (selection == 1) {
                    status = 11;
                    if (!cm.getSquadList("ChaosZak", 1)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }

                } else if (selection == 2) {
                    status = 12;
                    if (!cm.getSquadList("ChaosZak", 2)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }

                } else if (selection == 3) { // get insode
                    if (cm.getSquad("ChaosZak") != null) {
                        var dd = cm.getEventManager("ChaosZakum");
                        dd.startInstance(cm.getSquad("ChaosZak"), cm.getMap(), 160102);
                        cm.dispose();
                    } else {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }
                }
                break;
            case 11:
                cm.banMember("ChaosZak", selection);
                cm.dispose();
                break;
            case 12:
                if (selection != -1) {
                    cm.acceptMember("ChaosZak", selection);
                }
                cm.dispose();
                break;
        }
    } else {
        switch (status) {
            case 0:
                if (cm.getPlayer().getLevel() < 50) {
                    cm.sendOk("你必须到达50级才可以挑战扎昆.");
                    cm.dispose();
                    return;
                }
                if (cm.getPlayer().getClient().getChannel() != 2 && cm.getPlayer().getClient().getChannel() != 3) {
                    cm.sendOk("扎昆只能在2线.3线频道挑战.");
                    cm.dispose();
                    return;
                }
                var em = cm.getEventManager("ZakumBattle");

                if (em == null) {
                    cm.sendOk("事件未启动，请联系GM.");
                    cm.safeDispose();
                    return;
                }
                var prop = em.getProperty("state");
                var marr = cm.getQuestRecord(160101);
                var data = marr.getCustomData();
                if (data == null) {
                    marr.setCustomData("0");
                    data = "0";
                }
                var time = parseInt(data);
                if (prop == null || prop.equals("0")) {
                    var squadAvailability = cm.getSquadAvailability("ZAK");
                    if (squadAvailability == -1) {
                        status = 1;
                        if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("你已经去了炎魔在过去6小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                            cm.dispose();
                            return;
                        }
                        cm.sendYesNo("你有兴趣成为远征队的领队吗?");

                    } else if (squadAvailability == 1) {
                        if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                            cm.sendOk("你已经去了炎魔在过去6小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                            cm.dispose();
                            return;
                        }
                        // -1 = Cancelled, 0 = not, 1 = true
                        var type = cm.isSquadLeader("ZAK");
                        if (type == -1) {
                            cm.sendOk("队伍已经结束，请重新注册.");
                            cm.safeDispose();
                        } else if (type == 0) {
                            var memberType = cm.isSquadMember("ZAK");
                            if (memberType == 2) {
                                cm.sendOk("你被禁止参加队伍.");
                                cm.safeDispose();
                            } else if (memberType == 1) {
                                status = 5;
                                cm.sendSimple("你想做什么? \r\n#b#L0#检查成员#l \r\n#b#L1#加入队伍#l \r\n#b#L2#退出小队#l");
                            } else if (memberType == -1) {
                                cm.sendOk("队伍已经结束，请重新注册.");
                                cm.safeDispose();
                            } else {
                                status = 5;
                                cm.sendSimple("你想做什么? \r\n#b#L0#检查成员#l \r\n#b#L1#加入队伍#l \r\n#b#L2#退出小队#l");
                            }
                        } else { // Is leader
                            status = 10;
                            cm.sendSimple("你想做什么啊啊啊啊? \r\n#b#L0#检查成员#l \r\n#b#L1#删除成员#l \r\n#b#L2#编辑限制列表#l \r\n#r#L3#进入地图#l");
                        // TODO viewing!
                        }
                    } else {
                        var eim = cm.getDisconnected("ZakumBattle");
                        if (eim == null) {
                            var squd = cm.getSquad("ZAK");
                            if (squd != null) {
                                if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                    cm.sendOk("你已经去了炎魔在过去6小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                                    cm.dispose();
                                    return;
                                }
                                cm.sendYesNo("远征队对抗扎昆已经开始了.\r\n" + squd.getNextPlayer());
                                status = 3;
                            } else {
                                cm.sendOk("远征队对抗扎昆已经开始了.");
                                cm.safeDispose();
                            }
                        } else {
                            cm.sendYesNo("啊，你回来了。你愿意加入你的队伍在战斗中吗?");
                            status = 2;
                        }
                    }
                } else {
                    var eim = cm.getDisconnected("ZakumBattle");
                    if (eim == null) {
                        var squd = cm.getSquad("ZAK");
                        if (squd != null) {
                            if (time + (6 * 3600000) >= cm.getCurrentTime() && !cm.getPlayer().isGM()) {
                                cm.sendOk("你已经去了炎魔在过去6小时。剩下的时间: " + cm.getReadableMillis(cm.getCurrentTime(), time + (6 * 360000)));
                                cm.dispose();
                                return;
                            }
                            cm.sendYesNo("远征队对抗扎昆已经开始了.\r\n" + squd.getNextPlayer());
                            status = 3;
                        } else {
                            cm.sendOk("远征队对抗扎昆已经开始了.");
                            cm.safeDispose();
                        }
                    } else {
                        cm.sendYesNo("啊，你回来了。你愿意加入你的队伍在战斗中吗?");
                        status = 2;
                    }
                }
                break;
            case 1:
                if (mode == 1) {
                    if (cm.registerSquad("ZAK", 5, " 已被任命为班长（定期）。如果你想加入请在时间段内注册的远征队.")) {
                        cm.sendOk("你已经被任命为远征队领袖领袖。在接下来的5分钟，你可以加入远征队的成员。");
                    } else {
                        cm.sendOk("添加你的小队时发生了一个错误.");
                    }
                } else {
                    cm.sendOk("如果你想成为远征队领队的话，跟我谈谈.")
                }
                cm.safeDispose();
                break;
            case 2:
                if (!cm.reAdd("ZakumBattle", "ZAK")) {
                    cm.sendOk("误差…请再试一次.");
                }
                cm.safeDispose();
                break;
            case 3:
                if (mode == 1) {
                    var squd = cm.getSquad("ZAK");
                    if (squd != null && !squd.getAllNextPlayer().contains(cm.getPlayer().getName())) {
                        squd.setNextPlayer(cm.getPlayer().getName());
                        cm.sendOk("你已经保留了现场.");
                    }
                }
                cm.dispose();
                break;
            case 5:
                if (selection == 0) {
                    if (!cm.getSquadList("ZAK", 0)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    } else {
                        cm.dispose();
                    }
                } else if (selection == 1) { // join
                    var ba = cm.addMember("ZAK", true);
                    if (ba == 2) {
                        cm.sendOk("队伍目前已满，请稍后再试.");
                        cm.safeDispose();
                    } else if (ba == 1) {
                        cm.sendOk("你已经成功加入了队伍");
                        cm.safeDispose();
                    } else {
                        cm.sendOk("你已经是队伍的一部分了.");
                        cm.safeDispose();
                    }
                } else {// withdraw
                    var baa = cm.addMember("ZAK", false);
                    if (baa == 1) {
                        cm.sendOk("你已经退出了队伍的成功");
                        cm.safeDispose();
                    } else {
                        cm.sendOk("你不是队伍的一部分.");
                        cm.safeDispose();
                    }
                }
                break;
            case 10:
                if (selection == 0) {
                    if (!cm.getSquadList("ZAK", 0)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了。");
                    }
                    cm.safeDispose();
                } else if (selection == 1) {
                    status = 11;
                    if (!cm.getSquadList("ZAK", 1)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }

                } else if (selection == 2) {
                    status = 12;
                    if (!cm.getSquadList("ZAK", 2)) {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }

                } else if (selection == 3) { // get insode
                    if (cm.getSquad("ZAK") != null) {
                        var dd = cm.getEventManager("ZakumBattle");
                        dd.startInstance(cm.getSquad("ZAK"), cm.getMap(), 160101);
                        cm.dispose();
                    } else {
                        cm.sendOk("由于未知的错误，对队伍的要求被拒绝了.");
                        cm.safeDispose();
                    }
                }
                break;
            case 11:
                cm.banMember("ZAK", selection);
                cm.dispose();
                break;
            case 12:
                if (selection != -1) {
                    cm.acceptMember("ZAK", selection);
                }
                cm.dispose();
                break;
        }
    }
}