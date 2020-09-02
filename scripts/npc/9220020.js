var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (!cm.isLeader()) {
	cm.sendNext("让你的队长与我对话.");
	cm.dispose();
	return;
    }
    if (cm.haveItem(4032118,7)) {
	cm.warpParty(674030200);
	cm.removeAll(4032118);
    } else {
	cm.sendOk("嘿!请找到#b7个#l#v4032118!");
    }
    cm.dispose();
}