/*
Moose
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	if (cm.getQuestStatus(6180) == 1) {
	    cm.sendOk("好。 我会送你到盾牌训练场。 再次与我说话。" );
	} else {
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.warp(924000000, 0);
	cm.dispose();
    }
}