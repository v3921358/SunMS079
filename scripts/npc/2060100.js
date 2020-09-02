/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
	map = cm.getPlayer().getMap();
    if (cm.getQuestStatus(6301) == 1) {
	if (cm.haveItem(4000175)) {
	    cm.gainItem(4000175, -1);
	    if (cm.getParty() == null) {
		cm.warp(923000000);
		cm.getPlayer().startMapTimeLimitTask(600, map);
	    } else {
		cm.warpParty(923000000);
		cm.getPlayer().startMapTimeLimitTask(600, map);
	    }
	} else {
	    cm.sendOk("为了打开时间齿轮的裂缝，你必须拥有一块#v4000175#。 这些可以通过击败获得.");
	}
    } else {
	cm.sendOk("我是#b女巫#k不要骗我，因为我知道我的习惯把人变成蠕虫.");
    }
    cm.dispose();
}