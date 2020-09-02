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
                text += "";//
            }
			text += "\t\t\t     #e#b#v3992026#一线海冒险岛#v3992026# #k#n\r\n"
			text += "\t #d战胜指定BOSS获得战利品可以兑换相应的抵用卷#k\r\n              #e当前抵用卷为：#r"+cm.getPlayer().getCSPoints(2)+"#k 点   \r\n"
            text += "#L9##r1个#v4000040#兑换100抵用卷#l"//3
            text += "#L10##r1个#v4000176#兑换100抵用卷#l\r\n\r\n"//3
			text += "#L14##r1个#v4031227#兑换100抵用卷#l"//3
			text += "#L15##r1个#v4001111#兑换200抵用卷#l\r\n\r\n"//3
			text += "#L16##r1个#v4000235#兑换200抵用卷#l"//3
			text += "#L17##r1个#v4000243#兑换200抵用卷#l\r\n\r\n"//3
			text += "#L11##r1个#v4001085#兑换200抵用卷#l"//3
			text += "#L12##r1个#v4001084#兑换200抵用卷#l\r\n\r\n"//3
			text += "#L13##r1个#v4001083#兑换500抵用卷#l"//3
            cm.sendSimple(text);
        } else if (selection == 9) {
        if (cm.haveItem(4000040, 1)) {
			cm.gainItem(4000040,-1);
			cm.getPlayer().modifyCSPoints(2, 100);
			cm.sendOk("你获得了100抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 10) {
        if (cm.haveItem(4000176, 1)) {
			cm.gainItem(4000176,-1);
			cm.getPlayer().modifyCSPoints(2, 100);
			cm.sendOk("你获得了100抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();
		} else if (selection == 11) {
        if (cm.haveItem(4001085, 1)) {
			cm.gainItem(4001085,-1);
			cm.getPlayer().modifyCSPoints(2, 200);
			cm.sendOk("你获得了200点卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();
		} else if (selection == 12) {
        if (cm.haveItem(4001084, 1)) {
			cm.gainItem(4001084,-1);
			cm.getPlayer().modifyCSPoints(2, 200);
			cm.sendOk("你获得了200点卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();	
		} else if (selection == 13) {
        if (cm.haveItem(4001083, 1)) {
			cm.gainItem(4001083,-1);
			cm.getPlayer().modifyCSPoints(2, 500);
			cm.sendOk("你获得了500点卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();	
        } else if (selection == 14) {
        if (cm.haveItem(4031227, 1)) {
			cm.gainItem(4031227,-1);
			cm.getPlayer().modifyCSPoints(2, 100);
			cm.sendOk("你获得了100抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();	
        } else if (selection == 15) {
        if (cm.haveItem(4001111, 1)) {
			cm.gainItem(4001111,-1);
			cm.getPlayer().modifyCSPoints(2, 100);
			cm.sendOk("你获得了200抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();	
        } else if (selection == 16) {
        if (cm.haveItem(4000235, 1)) {
			cm.gainItem(4000235,-1);
			cm.getPlayer().modifyCSPoints(2, 100);
			cm.sendOk("你获得了100抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();
        } else if (selection == 17) {
        if (cm.haveItem(4000243, 1)) {
			cm.gainItem(4000243,-1);
			cm.getPlayer().modifyCSPoints(2, 200);
			cm.sendOk("你获得了200抵用卷！。");
		}else{
            cm.sendOk("#b物品不足 无法兑换！.");
		}
                    cm.dispose();					
		}
    }
}


