/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 2 && mode == 0) {
	cm.sendOk("好吧，下次见.");
	cm.dispose();
	return;
    }
    if (mode == 1) {
	status++;
    }
    else {
	status--;
    }
    if (status == 0) {
	if (cm.getQuestStatus(2055) == 1 && !cm.haveItem(4031039)) {
	    cm.gainItem(4031039, 1); // Shumi's Coin
	    cm.warp(103000000, 0);
	}
	else {
	    var rand = 1 + Math.floor(Math.random() * 6);
	    if (rand == 1) {
		cm.gainItem(4010003, 2); // Adamantium Ore
	    }
	    else if (rand == 2) {
		cm.gainItem(4010000, 2); // Bronze Ore
	    }
	    else if (rand == 3) {
		cm.gainItem(4010002, 2); // Mithril Ore
	    }
	    else if (rand == 4) {
		cm.gainItem(4010005, 2); // Orihalcon Ore
	    }
	    else if (rand == 5) {
		cm.gainItem(4010004, 2); // Silver Ore
	    }
	    else if (rand == 6) {
		cm.gainItem(4010001, 2); // Steel Ore
	    }
	    cm.warp(103000000, 0);
	}
	cm.dispose();
    }
}	

