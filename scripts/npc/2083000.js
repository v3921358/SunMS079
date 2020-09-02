function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} 
	else {
		if (mode == 0) {
			cm.sendOk("明智的选择。 如果你改变主意，随时跟我说话.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if (cm.getPlayer().getLevel() < 80) {
				cm.sendOk("最低等级要80级哦！！");
				cm.dispose();
				return;
			}
			else {
				if (cm.haveItem(4001086)) {
					cm.sendYesNo("发现你有物品#b敢死队的象征#k是否愿意进去一探究竟？？ \r\n    前面的路很艰苦。愿意去吗?");
				}
				else {
					cm.sendOk("如果想挑战里面的危险..你需要具备以下物品：\r\n敢死队的象征");
					cm.dispose();
					return;
				}
			}
		} 
		else if (status == 1) {
			cm.warp(240050400);
			cm.dispose();
			return;
		}
	}
}
