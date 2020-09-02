/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var item;
var selected;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 1 && mode == 0) {
	cm.dispose();
	return;
    } else if (status == 2 && mode == 0) {
	cm.sendNext("这个 " + item + " 不好制作，准备好材料再来找我。");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getPlayerStat("LVL") >= 40) {
	    cm.sendNext("是啊...我是仙女的炼金大师。但是，仙女不应该在一个人的时间很长一段时间的接触......。如果你得到了我的资料，我会让你成为一个特殊的道具。");
	} else {
	    cm.sendOk("我可以做出稀有，贵重物品，但不幸的是，我不能让它像你这样的陌生人。");
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.sendSimple("你想要做什么#b\r\n#L0#月石#l\r\n#L1#星石#l\r\n#L2#黑羽毛#l");
    } else if (status == 2) {
	selected = selection;
	if (selection == 0) {
	    item = "#v4011007##r#z4011007#";
	    cm.sendYesNo("所以，你想要做" + item + "? \r\n那么你需要的材料有: #b1个#v4011000##z4011000##k, #b1个#v4011001##z4011001##k, #b1个#v4011002##z4011002##k, #b1个#v4011003##z4011003##k, #b1个#v4011004##z4011004##k, #b1个#v4011005##z4011005##k, #b1个#v4011006##z4011006##k. 然后还有 10,000 金币");
	} else if (selection == 1) {
	    item = "#v4021009##r#z4021009#";
	    cm.sendYesNo("所以，你想要做" + item + "? \r\n那么你需要的材料有: #b1个#v4021000##z4021000##k, #b1个#v4021001##z4021001##k, #b1个#v4021002##z4021002##k, #b1个#v4021003##z4021003##k, #b1个#v4021004##z4021004##k, #b1个#v4021005##z4021005##k, #b1个#v4021006##z4021006##k, #b1个#v4021007##z4021007##k, #b1个#v4021008##z4021008##k. 然后还有 15,000 金币");
	} else if (selection == 2) {
	    item = "#v4031042##r#z4031042#";
	    cm.sendYesNo("所以，你想要做" + item + "? \r\n那么你需要的材料有: #b1个#v4001006##z4001006##k, #b1个#v4021008##z4021008##k, #b1个#v4021009##z4021009##k. 然后还有 30,000 金币");
	}
    } else if (status == 3) {
	if (selected == 0) {
	    if (!cm.haveItem(4011000)) { 
		cm.sendOk("#v4011000##z4011000#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011001)) {
		cm.sendOk("#v4011001##z4011001#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011002)) {
		cm.sendOk("#v4011002##z4011002#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011003)) {
		cm.sendOk("#v4011003##z4011003#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011004)) {
		cm.sendOk("#v4021004##z4021004#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011005)) {
		cm.sendOk("#v4011005##z4011005#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4011006)) {
		cm.sendOk("#v4011006##z4011006#不足！");
        cm.dispose();
		} else if (cm.getMeso() < 10000){//判断多少金币
		cm.sendOk("你没有1万金币！");
        cm.dispose();
	    } else {
		cm.gainMeso(-10000);
		cm.gainItem(4011000, -1);
		cm.gainItem(4011001, -1);
		cm.gainItem(4011002, -1);
		cm.gainItem(4011003, -1);
		cm.gainItem(4011004, -1);
		cm.gainItem(4011005, -1);
		cm.gainItem(4011006, -1);
		cm.gainItem(4011007, 1);
		cm.sendOk("做好了你要的 " + item + "。");
		cm.dispose();
	    }
	} else if (selected == 1) {
	    if (!cm.haveItem(4021000)) { 
		cm.sendOk("#v4021000##z4021000#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021001)) {
		cm.sendOk("#v4021001##z4021001#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021002)) {
		cm.sendOk("#v4021002##z4021002#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021003)) {
		cm.sendOk("#v4021003##z4021003#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021004)) {
		cm.sendOk("#v4021004##z4021004#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021005)) {
		cm.sendOk("#v4021005##z4021005#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021006)) {
		cm.sendOk("#v4021006##z4021006#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021007)) {
		cm.sendOk("#v4021007##z4021007#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021008)) {
		cm.sendOk("#v4021008##z4021008#不足！");
        cm.dispose();
		} else if (cm.getMeso() < 15000){//判断多少金币
		cm.sendOk("你没有1万5金币！");
        cm.dispose();
	    } else {
		cm.gainMeso(-15000);
		cm.gainItem(4021000, -1);
		cm.gainItem(4021001, -1);
		cm.gainItem(4021002, -1);
		cm.gainItem(4021003, -1);
		cm.gainItem(4021004, -1);
		cm.gainItem(4021005, -1);
		cm.gainItem(4021006, -1);
		cm.gainItem(4021007, -1);
		cm.gainItem(4021008, -1);
		cm.gainItem(4021009, 1);
		cm.sendOk("做好了你要的 " + item + "。");
		cm.dispose();
	    }
	} else if (selected == 2) {
	    if (!cm.haveItem(4001006)) { 
		cm.sendOk("#v4001006##z4001006#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021008)) {
		cm.sendOk("#v4021008##z4021008#不足！");
        cm.dispose();
		} else if (!cm.haveItem(4021009)) {
		cm.sendOk("#v4021009##z4021009#不足！");
        cm.dispose();
		} else if (cm.getMeso() < 30000){//判断多少金币
		cm.sendOk("你没有3万金币！");
        cm.dispose();
	    } else {
		cm.gainMeso(-30000);
		cm.gainItem(4001006, -1);
		cm.gainItem(4021008, -1);
		cm.gainItem(4021009, -1);
		cm.gainItem(4031042, 1);
		cm.sendOk("做好了你要的 " + item + "。");
		cm.dispose();
	    }
	}
	cm.dispose();
    }
}