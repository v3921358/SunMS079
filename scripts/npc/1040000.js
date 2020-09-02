
/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start(){
	if (cm.getQuestStatus(28177) == 1 && !cm.haveItem(4032479)) { //too lazy
		cm.gainItem(4032479,1);
		cm.dispose();
	}
	cm.sendNext("穿过这里，你会发现金银岛中心地牢。请小心...");
	cm.dispose();
}