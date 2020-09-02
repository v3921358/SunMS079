/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 
function start() {
    cm.sendYesNo("如果你现在离开，你将不得不重新开始。你确定你要离开?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(105100100);
    }
    cm.dispose();
}