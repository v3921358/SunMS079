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
	} else if (mode == 0) {
		cm.sendOk("需要的时候在来找我。");
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;		
	if (status == 0) {		
		cm.sendYesNo("您是否想要领#b#t4032388##k？");	
	} else if (status == 1)  {
		if (!cm.canHold(4032388)) {
		cm.sendOk("你的背包已满");
		cm.dispose();
		} else if (cm.haveItem(4032388)) {
		cm.sendOk("您好像已经有了#b#t4032388##k了...")
		cm.dispose();
		} else {
		cm.gainItem(4032388, 1);
		cm.dispose();	
	}
}
}
}