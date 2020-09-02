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
	    cm.sendNext("如果你想体验什么是海盗，再来找我.");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("海盗被万人拥戴，所有的怪物都对于惧怕");
    } else if (status == 1) {
	cm.sendYesNo("你想体验什么是弓箭手？");
    } else if (status == 2) {
	cm.MovieClipIntroUI(true);
	cm.warp(1020500, 0); // Effect/Direction3.img/pirate/Scene00
	cm.dispose();
    }
}