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
	if (cm.getQuestStatus(2057) == 1) {
	    cm.gainItem(4031041, 1); // Shumi's Sack of Cash
	} else {
	    var rand = 1 + Math.floor(Math.random() * 3);
	    if (rand == 1) {
		cm.gainItem(4020007, 2); // Diamond Ore
	    } else if (rand == 2) {
		cm.gainItem(4020008, 2); // Black Crystal Ore
	    } else if (rand == 3) {
		cm.gainItem(4010006, 2); // Gold Ore
	    }
	}
	cm.warp(103000100, 0);
	cm.dispose();
    }
}	



