/*合成NPC 作者:bay 廖*/
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
            for (i = 0; i < 60; i++) {
                text += "";
            }
            text +="水元素戒指#v1112778#\r\n除了固定的四维和攻击魔力等加成之外额外还有魔法防御成长\r\n";
			text +="当你选择了合成水元素之后\r\n";
			text +="该戒指往后将不能合成其它元素的戒指\r\n";
			text +="请慎重考虑\r\n";
            text += "#L1##v1112778##e#kC级水元素(四维+5 攻击+2 魔力+4 魔防+15)#l\r\n";
			text += "#L2##v1112777##e#kB级水元素(四维+7 攻击+5 魔力+8 魔防+30)#l\r\n";
			text += "#L3##v1112776##e#kA级水元素(四维+10攻击+10魔力+15魔防+50)#l\r\n";
            cm.sendSimple(text);
			        } else if (selection == 1) {
        cm.openNpc(9310072, 61);//
        } else if (selection == 2) {
		cm.openNpc(9310072, 62);//
		} else if (selection == 3) {
		cm.openNpc(9310072, 63);//
		} else if (selection == 3) {
		cm.openNpc(9201123, 4);//
		} else if (selection == 4) {
		cm.openNpc(9900004, 5);//
		} else if (selection == 5) {
        cm.openNpc(9900004, 6);//
	}
    }
}