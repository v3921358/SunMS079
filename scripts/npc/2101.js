/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendOk("你还没有完成培训计划吗？如果你想离开这个地方，请不要犹豫，告诉我.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("你是觉得你很强大想去看看外面的世界吗？如果你愿意，我会把你从这个训练营里送出去.");
		} else if (status == 1) {
			cm.sendNext("然后，我会把你从这里送出去。干得好.");
		} else if (status == 2) {
			cm.warp(3, 0);
			cm.dispose();
		}
	}
}