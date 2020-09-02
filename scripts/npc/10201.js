/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 1) {
	    cm.sendNext("如果你想体验什么是魔法师，再来找我.");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("魔法师拥有智慧的最高境界，千变万化。");
    } else if (status == 1) {
	cm.sendYesNo("你想体验什么是魔法师?");
    } else if (status == 2) {
	cm.MovieClipIntroUI(true);
	cm.warp(1020200, 0); // Effect/Direction3.img/magician/Scene00
	cm.dispose();
    }
}