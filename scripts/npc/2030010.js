/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

function start() {
    cm.sendYesNo("如果你现在离开，你将不得不重新开始。你确定要离开这里到外面去吗？");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(211042300);
    }
    cm.dispose();
}