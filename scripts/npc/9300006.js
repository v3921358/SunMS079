var status = 0
var chair = 3012003

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (mode == 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }
	if(status == 1){
		cm.sendYesNo("请问是否要领取#v"+chair+"##z"+chair+"#");
	} else if(status == 2){
		if(cm.getPlayer().getOneTimeLog("LoveChair") >= 1){
				cm.sendNext("#r你已经领取过结婚礼包了.\r\n#k不管你以后结婚多少次都只能领取一次,所以椅子请不要丢弃.");
				cm.dispose();
				return;
		} else if(cm.getPlayer().getMarriageId() == 0) {
			cm.sendNext("你没有结婚");
				cm.dispose();
				return;
		} else if(!cm.canHold(chair)){
				cm.sendNext("背包空间不足");
				cm.dispose();
				return;
			} else{
			cm.getPlayer().setOneTimeLog("LoveChair");
			cm.removeAll(chair);
			cm.gainItem(chair, 1);
			cm.sendNext("#v"+chair+"##r已经放到了你的背包.\r\n#k不管你以后离婚结婚多少次都只能领取本次!所以椅子请不要丢弃.");
			cm.dispose();
			return;
		}
	}
}