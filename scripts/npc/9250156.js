function start() {
	cm.sendYesNo("Would you like to get out?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(682020000,0);
    }
    cm.dispose();
}