/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function start() {
    cm.sendYesNo("(你可以回到原来的世界! 你愿意回去吗?)");
}

function action(mode, type, selection) {
    if (mode == 1) {
	var map = cm.getMapId();
	if (map == 108010101) {
	    tomap = 100000000;
	} else if (map == 108010201) {
	    tomap = 101000000;
	} else if (map == 108010301) {
	    tomap = 102000000;
	} else if (map == 108010401) {
	    tomap = 103000000;
	} else if (map == 108010501) {
	    tomap = 120000000;
	}
	cm.warp(tomap);
    }
    cm.dispose();
}
