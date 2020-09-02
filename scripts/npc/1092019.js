/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    if (cm.getJob() == 522 && cm.getPlayerStat("LVL") >= 120) {
	if (!cm.hasSkill(5221003)) {
	    //cm.teachSkill(5221003, 0, 10);
	}
    }
	cm.forceStartQuest(6401,"q3"); //开始任务
    cm.sendOk("我都懒得问你问题了.在找我提交任务把！！");
}

function action(mode, type, selection) {
    cm.dispose();
}
