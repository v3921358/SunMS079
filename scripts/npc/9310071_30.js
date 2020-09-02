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
            text += "		#v4001221# #v4001222# #v4001223# #v4001224# #v4001225#\r\n "
			text += "  #r完成提交任务之前请检查背包空位!\r\n "
			text += "  #d任务条件不具备唯一性,所需材料不定期变动!\r\n "
			text +="#v3994085##L2##e#k简单任务#l#v3994085#\r\n\r\n"
			text +=" #v3994085##L3##e#k困难任务#l#v3994085#\r\n\r\n" 
            cm.sendSimple(text);
        } else if (selection == 1) {
						if(cm.getPlayer().getMapId() == 910000000){
	cm.sendOk("你已经在市场了，别逗了好吗?");
cm.dispose();
return;
}		
		cm.warp(910000000);
		cm.dispose();
        } else if (selection == 2) {//10
		cm.openNpc(9310071, 14);
        } else if (selection == 3) {//20-50
		cm.openNpc(9310071, 15);
        } else if (selection == 4) {//20-50
		cm.openNpc(9310102, 30);
        } else if (selection == 5) {//51-100
		cm.openNpc(9310102, 40);
        } else if (selection == 6) {//51-100
		cm.openNpc(9900004, 205);
        } else if (selection == 7) {//101-200
		cm.openNpc(9900004, 206);
        } else if (selection == 8) {//101-200
		cm.openNpc(9900004, 207);
		} else if (selection == 9) {//同步点装
		cm.openNpc(9900004, 9);
		} else if (selection == 10) {//删除指定物品
		cm.openNpc(9900004, 10);
	    } else if (selection == 11) {//美容美发
		cm.openNpc(9900004, 91);
		cm.dispose();
		} else if (selection == 12) {//枫叶兑换
		cm.openNpc(9900004, 12);
		} else if (selection == 13) {//枫叶兑换
		cm.openNpc(9900004, 33);
	}
    }
}


