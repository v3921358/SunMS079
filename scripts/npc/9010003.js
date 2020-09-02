/* Ria
	lolcastle NPC
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getMapId() != 101000000) {
	cm.dispose();
	return;
    }
    if (mode == 0) {
	cm.sendOk("好吧，下次见.");
	cm.dispose();
	return;
    }
    status++;
    if (status == 0) {
	cm.sendNext("我是利雅。对于一个小的费用 #b1000000 金币#k我可以送你到 #r审判现场#k.");
    } else if (status == 1) {
	cm.sendYesNo("你想进入 #r审判现场#k 现在?");
    } else if (status == 2) {
	var em = cm.getEventManager("lolcastle");
	if (em == null || !em.getProperty("entryPossible").equals("true")) {
	    cm.sendOk("Sorry, but #r审判现场#k is currently closed.");
	} else if (cm.getMeso() < 1000000) {
	    cm.sendOk("你没有足够的金币.");
	} else if (cm.getPlayerStat("LVL") < 21) {
	    cm.sendOk("你必须至少达到21级才能进入 #r审判现场.#k");
	} else if (cm.getPlayerStat("LVL") >= 21 && cm.getPlayerStat("LVL") < 31) {
	    cm.gainMeso(-1000000);
	    em.getInstance("lolcastle1").registerPlayer(cm.getChar());
	} else if (cm.getPlayerStat("LVL") >= 31 && cm.getPlayerStat("LVL") < 51) {
	    cm.gainMeso(-1000000);
	    em.getInstance("lolcastle2").registerPlayer(cm.getChar());
	} else if (cm.getPlayerStat("LVL") >= 51 && cm.getPlayerStat("LVL") < 71) {
	    cm.gainMeso(-1000000);
	    em.getInstance("lolcastle3").registerPlayer(cm.getChar());
	} else if (cm.getPlayerStat("LVL") >= 71 && cm.getPlayerStat("LVL") < 91) {
	    cm.gainMeso(-1000000);
	    em.getInstance("lolcastle4").registerPlayer(cm.getChar());
	} else {
	    cm.gainMeso(-1000000);
	    em.getInstance("lolcastle5").registerPlayer(cm.getChar());
	}
	cm.dispose();
    }
}