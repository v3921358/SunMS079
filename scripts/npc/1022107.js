/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function action(mode, type, selection) {
	if (cm.isQuestActive(22530)) {
		if (!cm.canHold(1952000,1)) {
			cm.sendOk("你需要库存空间..");
		} else {
			cm.forceCompleteQuest(22530);
			cm.gainExp(710);
			cm.gainItem(1952000,1);
			cm.getPlayer().gainSP(1, 1);
			cm.sendOk("你检查标志。 完成了警卫的请求.");
		}
	} else {
		cm.sendOk("这是一个标志.");
	}
	cm.dispose();
}