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
            text +="  #v2140000##v3994077##v3994079##v3994076##v3994074##v3994076##v3994067##v3994077##v3994063##v2140000#\r\n"
            text += " 我就是#b一线海冒险岛#k的飞天猪,您是不是要使用#r金币#k进行抽奖呢?也有可能不中奖是否继续?物品全随即,好坏全靠脸\r\n         #e#v2140002#您当前金币为:#b"+cm.getMeso() + "\r\n"
			text += " #L0##e#r#v2140002#500万金币抽取好礼#l\r\n\r\n"
			//text += "#L2##e#d#v5220000#快乐百宝券购买#l\r\n"
            cm.sendOk(text);
        } else if (selection == 0) {
		cm.openNpc(9100200, 1);//抽取好礼
        } else if (selection == 2) {
		cm.openNpc(9100200, 2);//快乐百宝券购买
	}
    }
}


