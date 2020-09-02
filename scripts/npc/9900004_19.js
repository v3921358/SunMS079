function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "#n#k欢迎来到#d盛世冒险岛#k,祝您开心如意,财源滚滚!!!\r\n\r\n"
            text += "#L1##e#d查看规矩#l\r\n\r\n#L2##e#r拼手气红包#l\r\n#L3##e#r普通红包#l\r\n#L4##e#r口令红包#l\r\n\r\n#L5##e#d领取红包余额#l"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.sendOk("2017，新年快乐！！！\r\n#r盛世冒险岛#k推出全新#r红包系统#k,以下是活动规则（如您有更好的想法请告诉我们）：\r\n\r\n#r发红包#k\r\n1：每发出一个红包需消耗一个#v5120015##z5120015#,野外均有掉落；\r\n2：玩家在聊天窗口输入“#r@发红包#k”命令即可发出红包；\r\n3:玩家在10分钟之内只能进行一次发红包操作，当发出的红包被抢完之后才能进行下一次发红包操作。\r\n\r\n#r抢红包#k\r\n1：拼手气红包和普通红包的命令为“#r@抢红包+空格+红包代码#k”（注意空格）；\r\n2：口令红包的命令为“#r@抢红包+空格+红包代码+空格+口令#k”（注意空格）；\r\n3:同一个红包每个玩家只能抢一次。"); 
		cm.dispose();
        } else if (selection == 2) {
		if (cm.haveItem(5120015,1) ){
		cm.gainItem(5120015, -1);
        cm.sendOk("执行这里!");
		cm.dispose();
		} else{
		cm.sendOk("你没有1个#v5120015##z5120015#,赶快去收集吧!");
	    cm.dispose();
		}
        } else if (selection == 3) {
		if (cm.haveItem(5120015,1) ){
		cm.gainItem(5120015, -1);
        cm.sendOk("执行这里!");
		cm.dispose();
		} else{
		cm.sendOk("你没有1个#v5120015##z5120015#,赶快去收集吧!");
	    cm.dispose();
		}
        } else if (selection == 4) {
		if (cm.haveItem(5120015,1) ){
		cm.gainItem(5120015, -1);
        cm.sendOk("执行这里!");
		cm.dispose();
		} else{
		cm.sendOk("你没有1个#v5120015##z5120015#,赶快去收集吧!");
	    cm.dispose();
		}
        } else if (selection == 5) {
		if (cm.haveItem(5120015,1) ){//这里不知道判断什么好
		cm.gainItem(5120015, -1);
        cm.sendOk("执行这里!");
		cm.dispose();
		} else{
		cm.sendOk("对不起，你没有可用余额！");
	    cm.dispose();
		}
	}
    }
}


