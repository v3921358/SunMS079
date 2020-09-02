var changemode;
var slot;
var rate = 1;
var item = 4031250;
var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else if (mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendSimple("选择兑换方法\r\n" +
			"#L1##i" + item + "# 换成 " + rate + " 点卷#l\r\n" +
			"#L2#" + rate + " 点卷换成 #i" + item + "# #l\r\n");
	} else if (status == 1) {
		changemode = selection;
		if (selection == 1) {
			cm.sendGetNumber("请输入要兑换点卷的数量. \r\n 一个#i" + item + "##r一个兑换" + rate + "点点卷", 1, 1, 1000);
		} else if (selection == 2) {
			cm.sendGetNumber("请输入要兑换#i" + item + "#的数量. \r\n 一个需要" + rate + "点点卷", 1, 1, 1000);
		} else {
			cm.dispose();
		}
	} else if (status == 2) {
		slot = selection;
		var sentence = "确定要兑换吗?你将获得#r" + rate * slot + "#k";
		if (changemode == 1) {
			sentence += "点卷";
		} else if (changemode == 2) {
			sentence += "个#i" + item + "#";
		}
		cm.sendYesNo(sentence)
	} else if (status == 3) {
		if (changemode == 1) {
			if (!cm.haveItem(item, slot)) {
				cm.sendOk("身上没有该物品!")
				cm.dispose();
				return;
			}
			cm.gainItem(item, -slot);
			cm.gainD( rate * slot);
		} else if (changemode == 2) {
			if (cm.getPlayer().getCSPoints(1) < slot) {
				cm.sendOk("点卷数量不足!")
				cm.dispose();
				return;
			} else if (!cm.canHold(item, slot)) {
				cm.sendOk("背包空间不足!")
				cm.dispose();
				return;
			}
			cm.gainItem(item, slot);
			cm.gainD( -rate * slot)
		}
		cm.sendOk("#b恭喜你成功拉!快快看你的包裹吧!#k");
		cm.dispose();
	} else {
		cm.dispose();
	}
}