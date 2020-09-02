
var status = 0;

function start() {
    cm.sendYesNo("你想回到大厅吗?");
}

function action(mode, type, selection) {
	if (mode == 1) {
		cm.warp(889100100);
	}
    cm.dispose();
}