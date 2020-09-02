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
            text += "   #k欢迎来到枫之谷戒指制作!制作之前请先确保检查背包装备栏还有空位没.因此制作造成的损失自行承担!\r\n"
			text += "#L1##e#d小鱼戒指#l#v1112907#四维属性+1\r\n\r\n" 
			text += "#L2##e#d蓝调戒指#l#v1112915#四维属性+3\r\n\r\n"
			text += "#L3##e#d奈基女神的守护#l#v1112932#四维属性+5\r\n"
            cm.sendSimple(text);
        } else if (selection == 1) {
        cm.openNpc(9310074, 1);//小鱼戒指
        } else if (selection == 2) {
		cm.openNpc(9310074, 2);//蓝调戒指
        } else if (selection == 3) {
		cm.openNpc(9310074, 3);//奈基女神的守护
	}
    }
}


