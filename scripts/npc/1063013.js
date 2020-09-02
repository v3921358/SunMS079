/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function action(mode, type, selection) {
    if (cm.isQuestActive(2236)) {
	cm.forceCompleteQuest(2236);
	cm.removeAll(4032263);
	cm.sendOk("任务完成.");
    }
    cm.dispose();
}