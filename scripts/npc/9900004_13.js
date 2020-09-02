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
            text += "\t\t欢迎来到#e#r"+cm.getChannelServer().getServerName()+"#d-物品兑换中心\r\n\r\n"
			text += "#n#k豆豆点：#r" + cm.getPlayer().getBeans() + "#k点\t  点卷：#r"+cm.getPlayer().getCSPoints(1) + "#k点\t  修炼点：#r" + cm.getDojoPoints() + " #k点\r\n"
			text += "#n#k游戏币：#r" + cm.getMeso()+ "#k元#k\r\n\r\n"
			text += "----------------------枫叶球兑换---------------------"	
			text += "#L1##e#d110级武器#l    #L2##e#d120级武器#l  #L3##e#d130级武器#l\r\n\r\n"
			text += "#k#n----------------------国庆币兑换---------------------"	
            text += "#L4##e#r必成卷轴#l     #L5##e#r祝福混沌#l    #L6##e#d中级防具#l\r\n\r\n"
			text += "#k#n----------------------点卷兑换----------------------"	
            text += "#L7##e#d兑换玩具#l     #L8##e#d兑换椅子#l    #L9##e#r高级防具#l\r\n\r\n" 
            cm.sendSimple(text);
        } else if (selection == 1) {//110级武器兑换
		cm.openNpc(9900004, 131);	
        } else if (selection == 2) {//120级武器兑换
		cm.openNpc(9900004, 132);		
        } else if (selection == 3) {//130级武器兑换
		cm.openNpc(9900004, 133);	
        } else if (selection == 4) {//必成卷轴
		cm.openNpc(9900004, 134);
        } else if (selection == 5) {//祝福混沌
		cm.openNpc(9900004, 135);
        } else if (selection == 6) {//中级防具
		cm.openNpc(9900004, 136);
        } else if (selection == 7) {//兑换玩具
		cm.openNpc(9900004, 137);
        } else if (selection == 8) {//兑换椅子
		cm.openNpc(9900004, 138);
		} else if (selection == 9) {//高级防具
		cm.openNpc(9900004, 139);
	}
    }
}


