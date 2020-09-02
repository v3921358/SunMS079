/* Adobis
 * 
 * El Nath: The Door to Zakum (211042300)
 * 
 * Zakum Quest NPC 
 
 * Custom Quest 100200 = whether you can do Zakum
 * Custom Quest 100201 = Collecting Gold Teeth <- indicates it's been started
 * Custom Quest 100203 = Collecting Gold Teeth <- indicates it's finished
 * Quest 7000 - Indicates if you've cleared first stage / fail
 * 4031061 = Piece of Fire Ore - stage 1 reward
 * 4031062 = Breath of Fire    - stage 2 reward
 * 4001017 = Eye of Fire       - stage 3 reward
 * 4000082 = Zombie's Gold Tooth (stage 3 req)
*/

var status;
var mapId = 211042300;
var stage;
var teethmode;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
	cm.removeAll(4001015);
	cm.removeAll(4001016);
	cm.removeAll(4001018);
    if (status == 0) {
	if (cm.getPlayerStat("LVL") >= 50) {
	    if (cm.getQuestStatus(100200) != 2 && cm.getQuestStatus(100200) != 1) {
		cm.startQuest(100200);
		cm.sendOk("你想要进行废矿地区探索?  好的，我是#b阿杜比斯#k... 判断你是合适的。你应该在地牢里安全的漫游。只是要小心...");
		cm.dispose();
		return;
	    } else if (cm.getQuestStatus(100201) == 1) {
		// if they have gold teeth and the other items, they are good to go
		teethmode = 1;
		cm.sendNext("你有我要的东西吗？");
	    } else {
		if (cm.haveItem(4001109)) {
		    cm.sendSimple("好吧，看起来你能满足我的条件，你想开始哪一个任务？#b\r\n#L0#废矿调查  (关卡 1)#l\r\n#L1#火山心脏  (关卡 2)#l\r\n#L2#治炼邀请  (关卡 3)#l\r\n#L3#进入熔岩中心.#l");
		} else {
		    cm.sendSimple("好吧，看起来你能满足我的条件，你想开始哪一个任务？#b\r\n#L0#废矿调查  (关卡 1)#l\r\n#L1#火山心脏  (关卡 2)#l\r\n#L2#治炼邀请  (关卡 3)#l");
		}
	    }
	    if (cm.getQuestStatus(100201) == 2) { // They're done the quests
		teethmode = 2;
	    }
	} else {
	    cm.sendOk("按照你目前的情况，你还不能满足进行这项任务的能力，当你变的強大的时候，再來找我吧.");
	    cm.dispose();
	}
    } else if (status == 1) {
	//quest is good to go.
	// if they're working on this quest, he checks for items.
	if (teethmode == 1) {
	    // check for items
	    if (cm.haveItem(4000082,30)) { // take away items, give eyes of fire, complete quest
		if (cm.canHold(4001017)) {
		    cm.removeAll(4031061);
		    cm.removeAll(4031062);
		    cm.gainItem(4000082, -30);
		    cm.gainItem(4001017, 3);
		    cm.sendNext("冶炼好了。 看到的门了吗？它就是通往扎昆祭台的门。 不过你需要 #b#t4001017##k 才能进入里面。让我看看有多少人能进入到那个恐怖的地方？");
		    cm.completeQuest(100201);
		    cm.completeQuest(100200);
		} else {
		    cm.sendNext("恩... 你确定你有所需要的物品制作#r火焰的眼#k？如果是这样的话，请检查，看看你的等级");
		}
		cm.dispose();
	    } else { // go get more
		cm.sendNext("你shtill没有得到我的teef！Howsh一个人shupposhed到conshentrate去teef?");
		cm.dispose();
	    }
	    return;
	}
	if (selection == 0) { //ZPQ
	    if (cm.getParty() == null) { //no party
		cm.sendNext("你现在不在一个队伍。你可能只解决这个分配的一方.");
		cm.safeDispose();
		return;
	    }
	    else if (!cm.isLeader()) { //not party leader
		cm.sendNext("让你的队伍队长跟我说话.");
		cm.safeDispose();
		return;
	    }
	    else {
		//check each party member, make sure they're above 50 and still in the door map
		//TODO: add zakum variable to characters, check that instead; less hassle
		var party = cm.getParty().getMembers();
		mapId = cm.getMapId();
		var next = true;

		for (var i = 0; i < party.size(); i++) {
		    if ((party.get(i).getLevel() < 50) || (party.get(i).getMapid() != mapId)) {
			next = false;
		    }
		}

		if (next) {
		    //all requirements met, make an instance and start it up
		    var em = cm.getEventManager("ZakumPQ");
		    if (em == null) {
			cm.sendOk("发现未知错误。请稍后再试。.");
		    } else {
			var prop = em.getProperty("state");
			if (prop.equals("0") || prop == null) {
			    em.startInstance(cm.getParty(), cm.getMap());
			} else {
			    cm.sendOk("另一方已经开始了这个任务。请稍后再试。.");
			}
		    }
		    cm.dispose();
		} else {
		    cm.sendNext("请确保你所有的成员都有资格开始我的试验...");
		    cm.dispose();
		}
	    }
	} else if (selection == 1) { //Zakum Jump Quest
	    stage = 1;
	    if (cm.haveItem(4031061) && !cm.haveItem(4031062)) {
		// good to go
		cm.sendYesNo("你已经安全地完成第一个阶段。还有很长的路要走，但是。那么，你准备好进入下一个阶段了吗?");
	    } else {
		if (cm.haveItem(4031062))
		    cm.sendNext("你已经得到了 #b火山的呼吸#k, 你不需要做这个阶段.");
		else
		    cm.sendNext("它看起来你还没有完成上一个阶段，但。在进入下一个阶段之前，请先完成上阶段.");
		cm.dispose();
	    }
	} else if (selection == 2) { //Golden Tooth Collection
	    stage = 2;
	    if (teethmode == 2 && cm.haveItem(4031061) && cm.haveItem(4031062)) {
		// Already done it once, they want more
		cm.sendYesNo("如果你想要更多 #b火眼#k,你需要给我带来#b30个僵尸丢失的金牙#k\r\n你有那些牙齿给我吗?");
	    } else if (cm.haveItem(4031061) && cm.haveItem(4031062)) {
		// check if quest is complete, if so reset it (NOT COMPLETE)
		cm.sendYesNo("好吧， 你已经完成了早期的阶段。  现在， 努力一点我可以帮你得到进入扎昆祭台所需要的 火焰的眼。 哦，我听说僵尸们有几颗金牙。我需要你找到 #b30 个僵尸丟失的金牙齿#k \r\n任务要求：\r\n#i4000082##b x 30 个");
				
	    } else {
		cm.sendNext("请完成上一阶段的任务再来挑战此阶段.");
		cm.dispose();
	    }
	} else if (selection == 3) { // Enter the center of Lava, quest
	    var dd = cm.getEventManager("FireDemon");
	    if (dd != null && cm.haveItem(4001109)) {
		dd.startInstance(cm.getPlayer());
	    } else {
		cm.sendOk("暂时不能进入.");
	    }
	    cm.dispose();
	} else if (selection == 4) {
	    if (cm.getQuestStatus(100200) == 2) {
		cm.sendOk("你已完成了这个任务。");
		cm.dispose();
	    } else {
	    	cm.sendYesNo("你想收买我？哈哈，可以啊！但你必须给我 #e3,000,000#n 金币，我就可以让你直接跳过任务。");
		status = 3;
	    }
	}
    } else if (status == 2) {
	if (stage == 1) {
	    cm.warp(280020000, 0); // Breath of Lava I
	    cm.dispose();
	}
	else if (stage == 2) {
	    if (teethmode == 2) {
		if (cm.haveItem(4031061,1) && cm.haveItem(4031062,1) && cm.haveItem(4000082,30)) { // take away items, give eyes of fire, complete quest
		    if (cm.canHold(4001017)) {
			cm.gainItem(4031061, -1);
			cm.gainItem(4031062, -1);
			cm.gainItem(4000082, -30);
			cm.gainItem(4001017, 3);
			cm.sendNext("冶炼好了。 看到的门了吗？它就是通往扎昆祭台的门。 不过你需要 #b#t4001017##k 才能进入里面。让我看看有多少人能进入到那个恐怖的地方？");
			cm.completeQuest(100201);
			cm.completeQuest(100200);
		    } else {
			cm.sendNext("你好像沒有足够的背包空间，请检查一下再来");
		    }
		    cm.dispose();
		} else {
		    cm.sendNext("我不认为你帶來了30个 僵尸丟失的金牙呢……请快点找來，我就会给你需要的东西。" );
		    cm.dispose();
		}
	    } else {
		cm.startQuest(100201);
		cm.dispose();
	    }
	}
    } else if (status == 5) { //bribe
	if (cm.getPlayer().getMeso() < 300000000) {
	    cm.sendNext("你好像沒有足够的金币來支付.");
	} else if (!cm.canHold(4001017,5)) {
	    cm.sendNext("你好像沒有足够的背包空间.");
	} else {
	    cm.gainItem(4001017,5);
	    cm.completeQuest(100201);
	    cm.completeQuest(100200);
	    cm.forceCompleteQuest(7000);
	    cm.completeQuest(100203);
	    cm.sendOk("好了，祝你玩的愉快！");
	    cm.gainMeso(-300000000);
	}
	cm.dispose();
    } else {
	cm.dispose();
    }
}