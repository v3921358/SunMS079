var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("你确定此任务并且出去吗?");
    } else if (status == 1) {
	if (cm.getInventory(2).isFull(1)){
	cm.sendOk("#b请保证消耗栏位至少有1个空格,否则无法领取.");
	cm.dispose();		
	} else {
	cm.removeAll(4031450);
	cm.gainItem(2280011, 1);
	cm.warp(211040200);
	cm.dispose();
    }
}}