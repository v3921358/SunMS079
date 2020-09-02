/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
	//map = cm.getPlayer().getMap();
    if (cm.getQuestStatus(6410) != 1) {
	cm.sendNext("你想做什么?");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你觉得你能保护好吗?");
    } else if (status == 1) {
	//cm.getMap(925010000).resetFully();//地图刷新
	//cm.getMap(925010100).resetFully();//地图刷新
	//cm.getMap(925010200).resetFully();//地图刷新
	cm.warp(925010000,0);
	//cm.getPlayer().startMapTimeLimitTask(1200, map);
	cm.dispose();
    }
}