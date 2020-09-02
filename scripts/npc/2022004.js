/* 
 * Tylus
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.getQuestStatus(6192) == 1) {
	    cm.sendOk("谢谢你守护我。 我可以做我的使命感谢你。 当你出去时跟我说话.");
	} else {
	    cm.warp(211000001, 0);
	    cm.dispose();
	}
    } else if (status == 1) {
	if (!cm.haveItem(4031495)) {
	    if (cm.canHold(4031495)) {
		cm.gainItem(4031495, 1);
		cm.warp(211000001, 0);
		cm.dispose();
	    } else {
		cm.sendOk("因为你的背包已满。请留出一个空格，并再次跟我说话.");
		cm.safeDispose();
	    }
	} else {
	    cm.warp(211000001, 0);
	    cm.dispose();
	}
    }
}