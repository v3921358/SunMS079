/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function action(mode, type, selection) {
    if (cm.isQuestActive(2166)) {
	cm.forceCompleteQuest(2166);
	cm.sendOk("你感觉到石头的力量.");
    }
    cm.dispose();
}