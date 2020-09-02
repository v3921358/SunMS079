/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 var status = -1;

function action(mode, type, selection) {
	if (cm.isQuestActive(2566) && !cm.haveItem(4032985,1)) {
		cm.gainItem(4032985,1);
		cm.sendNext("您已恢复点火设备.");
	}
	cm.dispose();
}