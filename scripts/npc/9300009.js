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
		if (cm.haveItem(1112804)>= 1 && cm.getPlayer().hasEquipped(1112804)){//判断背包是不是大于2个结婚戒指
		cm.removeAll(1112804);
		var map = cm.getSavedLocation("WEDDING");
		cm.warp(map, 0);
		cm.clearSavedLocation("WEDDING");
		cm.dispose();
		} else if (cm.haveItem(1112804)>= 2){//判断背包是不是大于2个结婚戒指
		cm.removeAll(1112804);
		cm.gainItem(1112804,1);
		var map = cm.getSavedLocation("WEDDING");
		cm.warp(map, 0);
		cm.clearSavedLocation("WEDDING");
		cm.dispose();
           } else  {
		var map = cm.getSavedLocation("WEDDING");
		cm.warp(map, 0);
		cm.clearSavedLocation("WEDDING");
		cm.dispose();	
	}
}}