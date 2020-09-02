
/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */


function start(){
	if (cm.getQuestStatus(2358) == 1) { //too lazy
		cm.forceCompleteQuest(2358);
	}
	cm.dispose();
}