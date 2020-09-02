var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
	} else {
		status++;
		if (!cm.getClient().getChannel() == 1) {
			cm.sendNext("该事件可能不试图在信道.");
			cm.dispose();
			return;
		}
		cm.sendNext("这次活动是不是现在正在发生.");
		cm.dispose();
		if(status == 0){
			cm.sendNext("您好,这是 #b枫叶周年#k 现在，你想和我一起种植枫树吗？ 与阳光从 #b温暖的太阳#k 它可以让树生长健康！ 请收集所有 温暖的太阳 你从怪物得到...");
		} else if (status == 1) {
			cm.sendSimple("每次用户收集所需的 温暖的太阳, 我们可以设置树增长到其最大值!\r\n#b#L0#在这里，我带来了 温暖的太阳.#l#k\r\n#b#L1# 请向我显示收集时的当前状态 温暖的太阳.#l#k");
		} else if (status == 2) {
			if (selection == 0) {
				cm.sendGetNumber("你带来了 温暖的太阳 和你？ 然后请给我 #b温暖的太阳#k 你有。 我会做一个好的爆竹。 你愿意给我多少?", cm.itemQuantity(4001246), 0, cm.itemQuantity(4001246));
			} else {
				cm.sendOk("树的生长状态\r\n#B" + cm.getSunshines() + "#\r\n如果我们收集他们所有，树将成长到最充分.");
				cm.dispose();
			}
		} else if (status == 3) {
			if (selection < 0 || selection > cm.itemQuantity(4001246)) {
				selection = cm.itemQuantity(4001246);
			}
			if (selection == 0) {
				cm.sendOk("请回来一些 温暖的太阳.");
			} else {
				cm.addSunshines(selection);
				cm.gainItem(4001246, -selection);
				cm.sendOk("谢谢你的 温暖的太阳.");
			}
			cm.dispose();
		}
	}
}