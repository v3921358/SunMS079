/*
	Keroben - Leafre Cave of life - Entrance
*/

var morph;
var status = -1;

function start() {
    status = -1;
    morph = 0;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	morph = cm.getMorphState();
	if (morph == 2210007 || cm.isQuestFinished(7301)) {
	    cm.cancelItem(2210007);
	    cm.warp(229000000, 0);
		cm.dispose();
	} else {
	    var hp = cm.getPlayerStat("HP");
	    if (hp > 500) {
		//cm.addHP(-500);
	    } else if (hp > 1 && hp <= 500) {
		//cm.addHP(-(hp - 1));
	    }
		cm.playerMessage(5,"除非你有万圣节活动装备或者变身成为幽灵,我才能让你进去!"); 
	    cm.dispose();
	}
    } else if (status == 1) {
	if (morph == 2210007 || cm.isQuestFinished(7301)) {
	    cm.cancelItem(2210007);
	    cm.warp(240050000, 0);
	    if (cm.haveItem(4031454)) { // Paladin
		cm.gainItem(4031454, -1);
		cm.gainItem(4031455, 1);
	    }
	    if (cm.getQuestStatus(6169) == 1) { // Temporary
		cm.gainItem(4031461, 1);
	    }
	} else {
	    //cm.warp(240040600, "st00");
	}
	cm.dispose();
    }
}