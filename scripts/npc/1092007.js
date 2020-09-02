/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    if (cm.isQuestActive(2175)) {
	cm.sendOk("你准备好了吗？好的，我会送你到黑魔法师的徒弟们去的地方。在我要送你的地方寻找。你将能够找到它通过跟踪他们.");
    } else {
    	cm.sendOk("黑魔术师及其追随者。诺特勒斯船员. \n 他们会互相追逐，直到其中的一个不存在，这是肯定的.");
	cm.safeDispose();
    }
}

function action(mode, type, selection) {
    cm.warp(912000000,0);
    cm.dispose();
}
