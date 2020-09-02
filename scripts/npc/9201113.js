var status = -1;

function start() {
	cm.removeAll(4001256);
	cm.removeAll(4001257);
	cm.removeAll(4001258);
	cm.removeAll(4001259);
	cm.removeAll(4001260);
		if (cm.getPlayer().getLevel() < 89) {
			cm.sendOk("你必须达到90级的要求，才可以挑战.");
			cm.dispose();
			return;
		}
		if (cm.getPlayer().getClient().getChannel() != 1 && cm.getPlayer().getClient().getChannel() != 2) {
			cm.sendOk("只能在1、2频道上挑战.");
			cm.dispose();
			return;
		}
    var em = cm.getEventManager("CWKPQ");

    if (em == null) {
	cm.sendOk("事件未启动，请联系GM.");
	cm.dispose();
	return;
    }
    var prop = em.getProperty("state");

    if (prop == null || prop.equals("0")) {
	var squadAvailability = cm.getSquadAvailability("CWKPQ");
	if (squadAvailability == -1) {
	    status = 0;
	    cm.sendYesNo("你有兴趣成为远征队的领导者吗？?");

	} else if (squadAvailability == 1) {
	    // -1 = Cancelled, 0 = not, 1 = true
	    var type = cm.isSquadLeader("CWKPQ");
	    if (type == -1) {
		cm.sendOk("该队已经结束，请重新注册.");
		cm.dispose();
	    } else if (type == 0) {
		var memberType = cm.isSquadMember("CWKPQ");
		if (memberType == 2) {
		    cm.sendOk("你被禁止参加远征队.");
		    cm.dispose();
		} else if (memberType == 1) {
		    status = 5;
		    cm.sendSimple("那你想做什么? \r\n#b#L0#查看队伍#l \r\n#b#L1#加入队伍#l \r\n#b#L2#从队伍中退出#l \r\n#b#L3#我要退出#l");
		} else if (memberType == -1) {
		    cm.sendOk("The squad has ended, please re-register.");
		    cm.dispose();
		} else {
		    status = 5;
		    cm.sendSimple("那你想做什么? \r\n#b#L0#查看队伍#l \r\n#b#L1#加入队伍#l \r\n#b#L2#从队伍中退出#l \r\n#b#L3#我要退出#l");
		}
	    } else { // Is leader
		status = 10;
		cm.sendSimple("那你想做什么? \r\n#b#L0#查看队伍#l \r\n#b#L1#删除队员#l \r\n#b#L2#编辑限制列表#l \r\n#b#L3#我要退出#l \r\n#r#L4#进入地图#l");
	    // TODO viewing!
	    }
	} else {
			var eim = cm.getDisconnected("CWKPQ");
			if (eim == null) {
				var squd = cm.getSquad("CWKPQ");
				if (squd != null) {
					if (squd.getNextPlayer() != null) {
						cm.sendOk("小组与老板的战斗已经开始了。 保留下一个地方的玩家 " + squd.getNextPlayer());
						cm.safeDispose();
					} else {
						cm.sendYesNo("队内对抗boss战已经开始了。你愿意排队下一场?");
						status = 3;
					}
				} else {
					cm.sendOk("对抗boss战已经开始.");
					cm.safeDispose();
				}
			} else {
				cm.sendYesNo("啊，你回来了。你想重新加入你的远征队在打?");
				status = 1;
			}
	}
    } else {
			var eim = cm.getDisconnected("CWKPQ");
			if (eim == null) {
				var squd = cm.getSquad("CWKPQ");
				if (squd != null) {
					if (squd.getNextPlayer() != null) {
						cm.sendOk("小组与老板的战斗已经开始了。 保留下一个地方的玩家 " + squd.getNextPlayer());
						cm.safeDispose();
					} else {
						cm.sendYesNo("队内对抗boss战已经开始了。你愿意排队下一场？?");
						status = 3;
					}
				} else {
					cm.sendOk("对抗boss战已经开始.");
					cm.safeDispose();
				}
			} else {
				cm.sendYesNo("啊，你回来了。你想重新加入你的远征队在打?");
				status = 1;
			}
    }
}

function action(mode, type, selection) {
    switch (status) {
	case 0:
	    	if (mode == 1) {
			if (!cm.haveItem(4032012, 1)) {
				cm.sendOk("您需要1个#v4032012##z4032012#才可以申请.");
			} else if (cm.registerSquad("CWKPQ", 5, " 已被任命为远征队队长。 如果你愿意加入，请在这段时间内注册远征队.")) {
				cm.sendOk("你已经被任命为远征队的领导者。在接下来的5分钟，你可以添加探险队的成员.");
			} else {
				cm.sendOk("发生了错误加入你的远征队.");
			}
	    	}
	    cm.dispose();
	    break;
	case 1:
		if (!cm.reAdd("CWKPQ", "CWKPQ")) {
			cm.sendOk("错误...请重试.");
		}
		cm.safeDispose();
		break;
	case 3:
		if (mode == 1) {
			var squd = cm.getSquad("CWKPQ");
			if (squd != null && squd.getNextPlayer() == null) {
				squd.setNextPlayer(cm.getPlayer().getName());
				cm.sendOk("你已经预留了点.");
			}
		}
		cm.dispose();
		break;
	case 5:
	    if (selection == 0 || selection == 3) {
		if (!cm.getSquadList("CWKPQ", selection)) {
		    cm.sendOk("由于未知错误，参加远征队的请求已被拒绝.");
		}
	    } else if (selection == 1) { // join
		var ba = cm.addMember("CWKPQ", true);
		if (ba == 2) {
		    cm.sendOk("这支队伍目前已满，请稍后再试.");
		} else if (ba == 1) {
		    cm.sendOk("您已成功加入了远征队");
		} else {
		    cm.sendOk("你已经是远征队的一员.");
		}
	    } else {// withdraw
		var baa = cm.addMember("CWKPQ", false);
		if (baa == 1) {
		    cm.sendOk("你已经成功地从远征队中退出了");
		} else {
		    cm.sendOk("你不是远征队的一部分.");
		}
	    }
	    cm.dispose();
	    break;
	case 10:
	    if (mode == 1) {
		if (selection == 0 || selection == 3) {
		    if (!cm.getSquadList("CWKPQ", selection)) {
			cm.sendOk("由于未知错误，参加远征队的请求已被拒绝.");
		    }
		    cm.dispose();
		} else if (selection == 1) {
		    status = 11;
		    if (!cm.getSquadList("CWKPQ", 1)) {
			cm.sendOk("由于未知错误，参加远征队的请求已被拒绝.");
			cm.dispose();
		    }
		} else if (selection == 2) {
		    status = 12;
		    if (!cm.getSquadList("CWKPQ", 2)) {
			cm.sendOk("由于未知错误，参加远征队的请求已被拒绝.");
			cm.dispose();
		    }
		} else if (selection == 4) { // get insode
		    if (cm.getSquad("CWKPQ") != null) {
			if (cm.haveItem(4032012, 1)) {
			    cm.gainItem(4032012, -1);
			    var dd = cm.getEventManager("CWKPQ");
			    dd.startInstance(cm.getSquad("CWKPQ"), cm.getMap());
			} else {
		 	    cm.sendOk("我的#v4032012##z4032012#在哪里?");
			}
		    } else {
			cm.sendOk("由于未知错误，参加远征队的请求已被拒绝.");
		    }
		    cm.dispose();
		}
	    } else {
		cm.dispose();
	    }
	    break;
	case 11:
	    cm.banMember("CWKPQ", selection);
	    cm.dispose();
	    break;
	case 12:
	    if (selection != -1) {
		cm.acceptMember("CWKPQ", selection);
	    }
	    cm.dispose();
	    break;
	default:
	    cm.dispose();
	    break;
    }
}