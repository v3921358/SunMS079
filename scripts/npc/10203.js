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
	    cm.sendNext("如果你想体验什么是飞侠，再来找我.");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("飞侠独自在阴暗的地方，从不惧怕黑暗。");
    } else if (status == 1) {
	cm.sendYesNo("你想体验什么是飞侠？");
    } else if (status == 2) {
	cm.MovieClipIntroUI(true);
	cm.warp(1020400, 0); // Effect/Direction3.img/rouge/Scene00
	cm.dispose();
    }
}