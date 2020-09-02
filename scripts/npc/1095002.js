/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;
var map = 912030000;
var num = 5;
var maxp = 5;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status <= 1) {
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
		var selStr = "选择一个你想要去的培训中心.";
		for (var i = 0; i < num; i++) {
			selStr += "\r\n#b#L" + i + "#培训中心 " + i + " (" + cm.getPlayerCount(map + i) + "/" + maxp + ")#l#k";
		}
		cm.sendSimple(selStr);
    } else if (status == 1) {
		if (selection < 0 || selection >= num) {
			cm.dispose();
		} else if (cm.getPlayer().getLevel() >= 21) {
            cm.sendNext("等级超过20级无法使用");
            cm.dispose();
		} else if (cm.getPlayerCount(map + selection) >= maxp) {
			cm.sendNext("这个培训中心已经满人，请稍后再尝试!");
			status = -1;
		} else {
			cm.warp(map + selection, 0);
			cm.dispose();
		}
    }
}