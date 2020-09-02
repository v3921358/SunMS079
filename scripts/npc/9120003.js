/*
	Hikari - Showa Town(801000000)
*/

var status = -1;

function start() {
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else {
	cm.sendOk("想好了在回来吧.");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你想进入浴室吗？你将花费"+10000+"金币");
    } else if (status == 1) {
	if (cm.getMeso() < 10000) {
	    cm.sendOk("请检查，看看你有没有 "+10000+"金币进入这个地方.");
	} else {
	    cm.gainMeso(-10000);
	    if (cm.getPlayerStat("GENDER") == 0) {
		cm.warp(801000100);
	    } else {
		cm.warp(801000200);
	    }
	}
	cm.dispose();
    }
}
