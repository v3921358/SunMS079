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
			text += "\t\t\t#e欢迎来到#b明日谷飞天猪兑换中心 #k!#n\r\n\r\n"
			text += "#n#k当前点卷点：#r" +cm.getPlayer().getCSPoints(1) +  "#k点    豆豆点：#r" + cm.getPlayer().getBeans() + "#k点\r\n"
            text += "#L1##b#v5220040#兑换#v2140002#金币#l\t\t#L2##b点券购买#v5220040##l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) { 
		cm.openNpc(9310022, 1); //兑换金币		
        } else if (selection == 2) {  
        cm.openNpc(9310022, 2); //购买飞天猪的蛋
			}
		}
    }



