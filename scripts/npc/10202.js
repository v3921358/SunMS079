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
	    cm.sendNext("如果你想体验什么是战士，再来找我.");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("战士拥有强大的体魄，以强壮的身躯抵挡敌人。");
    } else if (status == 1) {
	cm.sendYesNo("你想体验什么是战士?");
    } else if (status == 2) {
	cm.MovieClipIntroUI(true);
	cm.warp(1020100, 0); // Effect/Direction3.img/swordman/Scene00
	cm.dispose();
    }
}