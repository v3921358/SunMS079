/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    if (cm.getQuestStatus(6108) == 1) {
	var ret = checkJob();
	if (ret == -1) {
	    cm.sendOk("请形成了组队和再次跟我说话.");
	} else if (ret == 0) {
	    cm.sendOk("请确保您的队伍有2个人.");
	} else if (ret == 1) {
	    cm.sendOk("其中一个队员的没有资格进入另一个世界.");
	} else if (ret == 2) {
	    cm.sendOk("你的一个队员的条件不符合进入另一个世界.");
	} else {
	    var em = cm.getEventManager("s4aWorld");
	    if (em == null) {
		cm.sendOk("你不得与不明原因进入。 再试一次." );
	    } else if (em.getProperty("started").equals("true")) {
		cm.sendOk("有人已经试图挑战." );
	    } else {
		em.startInstance(cm.getParty(), cm.getMap());
	    }
	}
    }
    cm.dispose();
}

function action(mode, type, selection) {
}

function checkJob() {
    var party = cm.getParty();

    if (party == null) {
	return -1;
    }
       if (party.getMembers().size() != 2) {
    return 0;
       }
    var it = party.getMembers().iterator();

    while (it.hasNext()) {
	var cPlayer = it.next();

	if (cPlayer.getJobId() == 312 || cPlayer.getJobId() == 322 || cPlayer.getJobId() == 900) {
	    if (cPlayer.getLevel() < 120) {
		return 2;
	    }
	} else {
	    return 1;
	}
    }
    return 3;
}