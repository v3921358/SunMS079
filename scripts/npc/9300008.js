var status = 0

function start(){
	action(1, 0, 0);
}

function action(mode, type ,selection){
	if(mode == 1) {
		status++;
	} else if(mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if(status == 1){
		cm.sendYesNo("你想回去吗？");
	} else if(status == 2){
		cm.warpParty(700000000);
		cm.dispose();	
	} else {
		cm.dispose();
	}
}