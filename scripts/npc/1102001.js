/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    cm.sendYesNo("你找到所有的测试证明了吗？ 你想离开这里?");
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.sendNext("你可能需要更多的时间.");
    } else {
	cm.warp(130020000, 0);
    }
    cm.dispose();
}