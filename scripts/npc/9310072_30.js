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
            text +="木元素戒指#v1112770#\r\n除了固定的四维和攻击魔力等加成之外额外还有血量成长\r\n";
			text +="当你选择了合成木元素之后\r\n";
			text +="该戒指往后将不能合成其它元素的戒指\r\n";
			text +="请慎重考虑\r\n";
            text += "#L1##v1112770##e#kC级木元素(四维+5 攻击+2 魔力+4 hp+120)#l\r\n";
			text += "#L2##v1112769##e#kB级木元素(四维+7 攻击+5 魔力+8 hp+300)#l\r\n";
			text += "#L3##v1112768##e#kA级木元素(四维+10 攻击+10 魔力+15 hp+600)#l\r\n";
            cm.sendSimple(text);
			        } else if (selection == 1) {
        cm.openNpc(9310072, 31);//战士
        } else if (selection == 2) {
		cm.openNpc(9310072, 32);//法师
		} else if (selection == 3) {
		cm.openNpc(9310072, 33);//弓箭手
		} else if (selection == 3) {
		cm.openNpc(9201123, 4);//飞侠
		} else if (selection == 4) {
		cm.openNpc(9900004, 5);//海盗
		} else if (selection == 5) {
        cm.openNpc(9900004, 6);//无效
	}
    }
}