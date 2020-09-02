var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
		return;
	}
	status--;
    }
    switch(cm.getPlayer().getMapId()) {
	case 889100100: //event map
    	if (status == 0) {
	        cm.sendSimple("你好我是#b雪精灵#k！#b\r\n\r\n#L0#保护雪人-容易(推荐等级10级)#l\r\n#L1#保护雪人-中等(推荐等级30级)#l\r\n#L2#保护雪人-困难(推荐等级70级)#l");
    	} else if (status == 1) {
	        if (selection == 0) {
		    cm.warp(889100000,0); //exit map lobby
		    cm.dispose();
			} else if (selection == 1) {
			cm.warp(889100010,0); //exit map lobby
		    cm.dispose();
			} else if (selection == 2) {
			cm.warp(889100020,0); //exit map lobby
		    cm.dispose();
			}
		}
	    break;
	case 889100000:
	case 889100010:
	case 889100020:
    	    if (status == 0) {
	        cm.sendSimple("你好我是#k雪精灵#b！#b\r\n\r\n#L0#保护雪人#l");
    	    } else if (status == 1) {
			var s = ((cm.getMapId() % 100) / 10) | 0;
   		    var em = cm.getEventManager("Christmas");
    		    if (em == null) {
			cm.sendOk("请稍后再试。.");
			cm.dispose();
			return;
    		    }
		    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
			cm.sendOk("队伍队长必须在这里.");
		    } else {
			var party = cm.getPlayer().getParty().getMembers();
			var mapId = cm.getPlayer().getMapId();
			var next = true;
			var size = 0;
			var it = party.iterator();
			while (it.hasNext()) {
				var cPlayer = it.next();
				var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
				if (ccPlayer == null || ccPlayer.getLevel() < (s == 0 ? 10 : (s == 1 ? 30 : 70))) {
					next = false;
					break;
				}
				size++;
			}	
			if (next && size >= 1) {
		    		if (em.getInstance("Christmas" + s) == null) {
					em.startInstance_Party("" + s, cm.getPlayer());
		    		} else {
					cm.sendOk("另一队伍已经进入.");
		    		}
			} else {
				cm.sendOk("你队伍的所有成员都必须在这里。");
			}
		    }
	        cm.dispose();
            }
	    break;
	case 889100001:
	case 889100011:
	case 889100021:
		if (cm.getPlayer().getEventInstance() == null || !cm.isLeader()) {
			cm.sendOk("请让你的队长与我对话.");
		} else {
			if (!cm.getPlayer().getEventInstance().getProperty("stage").equals("1")) {
				cm.sendOk("请保护雪人，直到时间用完为止!");
			} else if (cm.getPlayer().getMap().countMonsterById(9400319) > 0 || cm.getPlayer().getMap().countMonsterById(9400320) > 0 || cm.getPlayer().getMap().countMonsterById(9400321) > 0) {
				cm.sendOk("请打败克洛斯!");
			} else {
				var s = ((cm.getMapId() % 100) / 10) | 0;
				cm.givePartyExp((s == 0 ? 2500 : (s == 1 ? 7500 : 20000)));
				cm.givePartyNX((s == 0 ? 250 : (s == 1 ? 500 : 750)));
				cm.warpParty(cm.getMapId() + 1);
			}
		}
		cm.dispose();
		break;
    }
}