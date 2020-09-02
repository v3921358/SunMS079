/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
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
	    cm.sendNext("如果你想体验什么是弓箭手，再来找我.");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("弓箭手拥有敏捷和力量，以远程攻击负责.");
    } else if (status == 1) {
	cm.sendYesNo("你想体验什么是弓箭手?");
    } else if (status == 2) {
	cm.MovieClipIntroUI(true);
	cm.warp(1020300, 0); // Effect/Direction3.img/archer/Scene00
	cm.dispose();
    }
}