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
            text +="火元素戒指#v1112766#\r\n除了固定的四维和攻击魔力等加成之外额外还有物理防御成长\r\n";
			text +="当你选择了合成火元素之后\r\n";
			text +="该戒指往后将不能合成其它元素的戒指\r\n";
			text +="请慎重考虑\r\n";
            text += "#L1##v1112766##e#kC级火元素(四维+5 攻击+2 魔力+4 物防+15)#l\r\n";
			text += "#L2##v1112765##e#kB级火元素(四维+7 攻击+5 魔力+8 物防+30)#l\r\n";
			text += "#L3##v1112764##e#kA级火元素(四维+10攻击+10魔力+15物防+50)#l\r\n";
            cm.sendSimple(text);
			        } else if (selection == 1) {
        cm.openNpc(9310072, 41);//
        } else if (selection == 2) {
		cm.openNpc(9310072, 42);//
		} else if (selection == 3) {
		cm.openNpc(9310072, 43);//
		} else if (selection == 3) {
		cm.openNpc(9201123, 4);//
		} else if (selection == 4) {
		cm.openNpc(9900004, 5);//
		} else if (selection == 5) {
        cm.openNpc(9900004, 6);//
	}
    }
}