/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
importPackage(net.sf.Start.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		map = cm.getPlayer().getMap();
		if ((cm.haveItem(4031059))) {
			cm.sendNext("你貌似已经有了#t4031059#。。");
		cm.dispose();
		return;
				}else if (!cm.isQuestActive(100110)){
		cm.sendNext("你没有接受相关任务!");
		cm.dispose();
		return;
		}else if (cm.isQuestActive(100111)){
		cm.sendNext("你已经完成了此任务");
		cm.dispose();
		return;
		}else if ((cm.getPlayerCount(108010301) <= 0)) {
		cm.getMap(108010301).resetFully();
		cm.removeAll(4031059);
        cm.warp(108010301, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
        cm.dispose();
} else if ((cm.getPlayerCount(108010201) <= 0)) {
		cm.getMap(108010201).resetFully();
	    cm.removeAll(4031059);
        cm.warp(108010201, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
		cm.dispose();
} else if ((cm.getPlayerCount(108010101) <= 0)) {
	    cm.getMap(108010101).resetFully();
	    cm.removeAll(4031059);
        cm.warp(108010101, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
		cm.dispose();
} else if ((cm.getPlayerCount(108010401) <= 0)) {
		cm.getMap(108010401).resetFully();
	    cm.removeAll(4031059);
        cm.warp(108010401, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
		cm.dispose();
		
} else if ((cm.getPlayerCount(108010501) <= 0)) {
		cm.getMap(108010501).resetFully();
	    cm.removeAll(4031059);
        cm.warp(108010501, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
		cm.dispose();
		}else{
		cm.sendNext("里面已经有人在挑战了。。");
		cm.dispose();
		return;
		}
	}
}	}
