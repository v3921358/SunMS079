/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode,type,selection) {
    if (status == -1) {
	if (cm.getMapId() == 108000500) {
	    if (!(cm.haveItem(4031857,15))) {
		cm.sendNext("快去收集 15个 #b#v4031857##z4031857##k 给我.");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("恭喜通过这次个考验 你已经是个强大的海盗了所以我将将会给你证明你的强大!");
	    }
	} else if (cm.getMapId() == 108000502) {
	    if (!(cm.haveItem(4031856,15))) {
		cm.sendNext("快去收集15个 #b#v4031856##z4031856##k 给我.");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("恭喜通过这次个考验 你已经是个强大的海盗了所以我将将会给你证明你的强大,出去以后点找!");
	    }
	} else {
	    cm.sendNext("错误请再尝试一次.");
	    cm.dispose();
	}
    } else if (status == 2) {
	cm.warp(120000101,0);
	cm.dispose();
    }
}