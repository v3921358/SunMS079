/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function action(mode, type, selection) {
	if (cm.getPlayer().getLevel() < 20) {	
		if (cm.getPlayer().getSubcategory() != 1) {
			cm.sendOk("你必须选择双Blader在人物选择跟我说话.");
		} else {
			cm.sendOk("你必须已经接受了2级和9级的任务来跟我说话.");
		}
	} else {
		cm.sendOk("我守卫入口秘密花园......哎呀，没有那么秘密了，它是?");
	}
	cm.safeDispose();
}