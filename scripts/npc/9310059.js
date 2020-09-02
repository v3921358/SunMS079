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
            text +="   #v4000425##v3994077##v3994079##v3994076##v3994074##v3994076##v3994067##v3994077##v3994063##v4000425#\r\n欢迎赞助本服，我这里有很多好东西，需要10点积分就可以抽1次奖,积分可以通过赞助充值获得,物品全随即,好坏全靠脸\r\n"
			text += "         #e#v3992018#您当前积分为: #r"+ cm.getcz() +"#k 点#v3992018#\r\n"
			text += "  #L0##e#r#v4000425#10积分抽取好礼#l\r\n\r\n"
			//text += "#L2##e#d#v5220000#快乐百宝券购买#l\r\n"
            cm.sendOk(text);
        } else if (selection == 0) {
		cm.openNpc(9310059, 1);//抽取好礼
        } else if (selection == 2) {
		cm.openNpc(9050001, 2);//快乐百宝券购买
	}
    }
}


