var status = 0;

function start() {
    cm.sendYesNo("Would you like to go to Six Path Crossway in Victoria Island?");
}

function action(mode, type, selection) {
	if (mode == 1) {
		cm.warp(104020000);
	}
    cm.dispose();
}