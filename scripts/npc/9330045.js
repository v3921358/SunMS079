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
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "         #v3992025##e#b欢迎来到知音冒险岛钓鱼场#k#v3992025#\r\n"//3
            text += "如果您想钓鱼的话，首先要到#r商城#k购买#r#v5340001##z5340001##k和#v2300001#，再买一个#v3011000#,进入地图，用钓鱼椅子坐下开始钓鱼!#l\r\n"//3
            text += "#L2##d购买#v3011000# 50万金币#l"//3
            text += "#L3##d#v2300001#200点卷购买鱼饵20个#l\r\n\r\n"//3
			//text += " #L4##d#v5340001#2000点卷购买#l"//3
			text += "#L7##d#v2300001#500点卷购买鱼饵50个#l"//3
			text += " #L8##d#v5350000#兑换鱼饵1:10#l\r\n\r\n"//3
			text += "#L6##r满载而归兑换钓鱼奖励."//3
			text += "#L1##b出发送我去钓鱼场.#l\r\n"//3
            //text += "#L7##e#d#v1032060# 锻造阿尔泰耳环.#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if (cm.getMeso() > 0) { 
			cm.saveLocation("FISHING");
		   	cm.warp(741000200);
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("准备好了才点我");
			cm.dispose(); 
			}
        } else if (selection == 2) {
			if (cm.haveItem(3011000, 1)||cm.getMeso() <= 500000) { 
                   	cm.sendOk("你的冒险币不足50万，或者你已经买了一把椅子了!");
                   	cm.dispose();
                   	} else {
			cm.gainItem(3011000,1); //钓鱼椅子
			cm.gainMeso(-500000);
		   	cm.sendOk("你已经成功买到了钓鱼椅子!花费了你50万冒险币！"); 
		   	cm.dispose();
			}		
        } else if (selection == 3) {	
			if(cm.getPlayer().getCSPoints(1) >= 200){
				cm.gainDJ(-200);
                   cm.gainItem(2300001,20);
                   cm.sendOk("购买成功");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有足够的点卷购买"); 
		   cm.dispose();
		   }
        } else if (selection == 8) {
           	   if (cm.haveItem(5350000, 1)) { 
                   cm.gainItem(5350000,-1);
                   cm.gainItem(2300001,15);
                   cm.sendOk("兑换成功！");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有高级鱼饵"); 
		   cm.dispose();
		   }		   
        } else if (selection == 4) {
			if(cm.getPlayer().getCSPoints(1) >= 2000){
				cm.gainDJ(-2000);
		   cm.gainItem(5340001,1);
                   cm.sendOk("高级鱼竿购买成功");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有足够的点卷购买,购买高级钓鱼竿需要2000点卷"); 
		   cm.dispose(); 
		   }	   
        } else if (selection == 7) {
			if(cm.getPlayer().getCSPoints(1) >= 500){
				cm.gainDJ(-500);
                   cm.gainItem(2300001,50);
                   cm.sendOk("购买成功");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有足够的点卷购买"); 
		   cm.dispose();
		   }		   
        } else if (selection == 5) {
                   cm.sendNextPrev("进入钓鱼场需要#b高级鱼竿#k或者#b鱼竿#k,也需要#b钓鱼场专用椅子#k,和#b鱼饵#k,这些你都可以通过我来购买.#b鱼竿#k请去点卷购物商场购买!");
                   cm.dispose();
        } else if (selection == 6) {
		cm.openNpc(9209001,10);
	}

    }
}


