
/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start(){
	if (cm.getQuestStatus(28177) == 1 && !cm.haveItem(4032479)) { //too lazy
		cm.gainItem(4032479,1);
	}
	cm.sendNext("通过何去何从，你会发现维多利亚地牢冰岛的中心。请小心...");
	cm.dispose();
}