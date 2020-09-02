/*function start() {
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
			if(cm.getPlayer().getMapId() == 108010101 || cm.getPlayer().getMapId() == 108010201 || cm.getPlayer().getMapId() == 108010301 || cm.getPlayer().getMapId() == 108010401 || cm.getPlayer().getMapId() == 108010501){
	cm.sendOk("本地图暂时无法使用使用拍卖功能");
cm.dispose();
return;
}		
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text +="#v4000423##v3994077##v3994079##v3994076##v3994074##v3994076##v3994067##v3994077##v3994063##v4000422#\r\n#v4000424##n迎春节,精彩纷呈.#v4000425#\r\n"
            text += "   #r如果你有#v5220000#考虑好了交给我，可以随即抽取各种好礼,惊喜多多,不容错过.\r\n\r\n"
			text += "#L2##e#d#v5220000#抽取好礼#l\r\n\r\n"
			text += "#L3##e#d#v5220000#快乐百宝券购买#l\r\n"
            cm.sendSimple(text);
        } else if (selection == 1) {
						if(cm.getPlayer().getMapId() == 910000000){
	cm.sendOk("你已经在市场了，别逗了好吗?");
cm.dispose();
return;
}		
		cm.warp(910000000);//回到市场
		cm.dispose();
        } else if (selection == 2) {
		cm.openNpc(9110009, 1);//新手礼包
        } else if (selection == 3) {
		cm.openNpc(9110009, 2);//转职转生
        } else if (selection == 4) {
		cm.openNpc(9310074, 3);//地图传送
        } else if (selection == 5) {
		cm.openNpc(9310074, 4);//副本传送
        } else if (selection == 6) {
		cm.openNpc(9310073, 5);//排 行 榜
        } else if (selection == 7) {//在线奖励
		cm.openNpc(9310073, 7);
        } else if (selection == 8) {//充值领取
		cm.openNpc(9310073, 2);
		} else if (selection == 9) {//同步点装
		cm.openNpc(9310073, 9);
		} else if (selection == 10) {//删除指定物品
		cm.openNpc(9310073, 1);
	    } else if (selection == 11) {//美容美发
		cm.warp(100000104);
		cm.dispose();
		} else if (selection == 12) {//枫叶兑换
		cm.openNpc(9310073, 12);
		} else if (selection == 13) {//枫叶兑换
		cm.openNpc(9310073, 13);
	}
    }
}


