/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
var status = -1;

function start() {
	if (cm.haveItem(4031508, 5) && cm.haveItem(4031507, 5)) {
		cm.sendNext("哇，你已经成功地收集了5个#b#t4031508##k和#b#t4031507##k. 好吧，那么，我现在送你去动物园。当你到那里时，请再和我说话.");
	} else {
		cm.sendYesNo("你还没有完成的任务。你确定你要离开吗?");
	}
}

function action(mode, type, selection) {
	status++;
	if (status == 0) {
		if (cm.haveItem(4031508, 5) && cm.haveItem(4031507, 5)) {
			cm.warp(230000003);
			cm.dispose();
		} else {
			cm.warp(923010100);
			cm.dispose();
		}
		cm.getPlayer().cancelMapTimeLimitTask();
	}
}
