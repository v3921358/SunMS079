/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var section = 0;
importPackage(java.lang);
//questid 29931, infoquest 7662
function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 1) {
	if (cm.getMapId() == 910320001) {
		cm.warp(910320000, 0);
		cm.dispose();
	} else if (cm.getMapId() == 910330001) {
		var itemid = 4001321;
		if (!cm.canHold(itemid)) {
			cm.sendOk("请腾出背包空间");
		} else {
			cm.gainItem(itemid,1);
			cm.warp(910320000, 0);
		}
		cm.dispose();
	} else if (cm.getMapId() >= 910320100 && cm.getMapId() <= 910320304) {
		cm.sendYesNo("你想退出这个地方吗？");
		status = 99;
	} else {
		cm.sendSimple("我的名字是林车长.\r\n#b#L1#进入灰尘的平台。#l#n\r\n#L2#挑战999列车平台。#l\r\n#L3#荣誉乘务员勋章。#l#k");
	}
    } else if (status == 2) {
		section = selection;
		if (selection == 1) {
			if (cm.getPlayer().getLevel() < 25 || cm.getPlayer().getLevel() > 30 || !cm.isLeader()) {
				cm.sendOk("你必须在等级范围25-30和队伍队长");
			} else {
				if (!cm.start_PyramidSubway(-1)) {
					cm.sendOk("里面有人，请稍后挑战");
				}
			}
			//todo
		} else if (selection == 2) {
			if (cm.haveItem(4001321)) {
				if (cm.bonus_PyramidSubway(-1)) {
					cm.gainItem(4001321, -1);
				} else {
					cm.sendOk("999列车平台目前有人挑战，请稍后再试。");
				}
			} else {
				cm.sendOk("你没有999列车车票");
			}
		} else if (selection == 3) {
			var record = cm.getQuestRecord(7662);
			var data = record.getCustomData();
			if (data == null) {
				record.setCustomData("0");
				data = record.getCustomData();
			}
			var mons = parseInt(data);
			if (mons < 3000) {
				cm.sendOk("请在车站击败至少10000个怪物，然后再找我。目前杀死  : " + mons);
			} else if (cm.canHold(1142141) && !cm.haveItem(1142141)){
				cm.gainItem(1142141,1);
				cm.forceStartQuest(29931);
				cm.forceCompleteQuest(29931);
			cm.worldMessage(6,"玩家：["+cm.getName()+"] 领取 荣誉乘务员勋章!");				
			} else {
				cm.sendOk("请腾出背包空间.");
			}
		}
		cm.dispose();
	} else if (status == 100) {
		cm.warp(910320000,0);
		cm.dispose();
	}
}