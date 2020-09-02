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
            //text += "         #v3992025##e#b欢迎来到知音冒险岛钓鱼场#k#v3992025#\r\n"//3
            //text += "如果您想钓鱼的话，首先要到#r商城#k购买#r#v5340001##z5340001##k和#v2300001#，再买一个#v3011000#,进入地图，用钓鱼椅子坐下开始钓鱼!#l\r\n"//3
            text += "#L1#20张#v4002001#和#v4000115#500兑换#v1052167##z1052167##l\r\n"//1
			//text += "#L2#20张#v4002001#和#v4000115#500兑换#v1052271##z1052271##l\r\n"//2
			//text += "#L3#20张#v4002001#和#v4000115#500兑换#v1052272##z1052272##l\r\n"//3
			//text += "#L4#20张#v4002001#和#v4000115#500兑换#v1052273##z1052273##l\r\n"//4
			//text += "#L5#20张#v4002001#和#v4000115#500兑换#v1052274##z1052274##l\r\n"//5			
            cm.sendSimple(text);
        } else if (selection == 10) {
			if (cm.getMeso() > 0) { 
			cm.saveLocation("FISHING");
		   	cm.warp(741000200);
                  	cm.dispose();
                   	} else {
	           	cm.sendOk("准备好了才点我");
			cm.dispose(); 
			}
        } else if (selection == 20) {
			if (cm.haveItem(3011000, 1)||cm.getMeso() <= 500000) { 
                   	cm.sendOk("你的冒险币不足50万，或者你已经买了一把椅子了!");
                   	cm.dispose();
                   	} else {
			cm.gainItem(3011000,10); //钓鱼椅子
			cm.gainMeso(-500000);
		   	cm.sendOk("你已经成功买到了钓鱼椅子!花费了你50万冒险币！"); 
		   	cm.dispose();
			}		
        } else if (selection == 30) {	
			if(cm.getPlayer().getCSPoints(1) >= 200){
				cm.gainDJ(-200);
                   cm.gainItem(2300001,20);
                   cm.sendOk("购买成功");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有足够的点卷购买"); 
		   cm.dispose();
		   }
        } else if (selection == 1) {
           	   if (cm.haveItem(4002001, 20)&&cm.haveItem(4000115, 500)) { 
                   cm.gainItem(4002001,-20);
				   cm.gainItem(4000115,-500);
                   cm.gainItem(1052167,1);
                   cm.sendOk("兑换成功!");
cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换最强工作人员套装!");				   
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }		   
        } else if (selection == 2) {
           	   if (cm.haveItem(4002001, 20)&&cm.haveItem(4000115, 500)) { 
                   cm.gainItem(4002001,-20);
				   cm.gainItem(4000115,-500);
                   cm.gainItem(1052271,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换挑战之衣!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }
        } else if (selection == 3) {
           	   if (cm.haveItem(4002001, 20)&&cm.haveItem(4000115, 500)) { 
                   cm.gainItem(4002001,-20);
				   cm.gainItem(4000115,-500);
                   cm.gainItem(1052272,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换挑战之衣!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }
        } else if (selection == 4) {
           	   if (cm.haveItem(4002001, 20)&&cm.haveItem(4000115, 500)) { 
                   cm.gainItem(4002001,-20);
				   cm.gainItem(4000115,-500);
                   cm.gainItem(1052273,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换挑战之衣!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }
        } else if (selection == 5) {
           	   if (cm.haveItem(4002001, 20)&&cm.haveItem(4000115, 300)) { 
                   cm.gainItem(4002001,-20);
				   cm.gainItem(4000115,-300);
                   cm.gainItem(1052274,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换挑战之衣!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }
        } else if (selection == 6) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1412046,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你材料不足或者没有20张#v4002001#"); 
		   cm.dispose();
		   }
        } else if (selection == 7) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1432061,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }
        } else if (selection == 8) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1452085,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }
        } else if (selection == 9) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1462075,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }
        } else if (selection == 10) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1472100,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }
        } else if (selection == 11) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1482046,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }
        } else if (selection == 12) {
           	   if (cm.haveItem(4002000, 30)) { 
                   cm.gainItem(4002000,-30);
                   cm.gainItem(1492048,1);
                   cm.sendOk("兑换成功!");
				   cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换地狱大公武器!");	
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有30张#v4002000#"); 
		   cm.dispose();
		   }		   
        } else if (selection == 70) {
			if(cm.getPlayer().getCSPoints(1) >= 500){
				cm.gainDJ(-500);
                   cm.gainItem(2300001,50);
                   cm.sendOk("购买成功");
                   cm.dispose();
                   } else {
		   cm.sendOk("你没有足够的点卷购买"); 
		   cm.dispose();
		   }		   
        } else if (selection == 50) {
                   cm.sendNextPrev("进入钓鱼场需要#b高级鱼竿#k或者#b鱼竿#k,也需要#b钓鱼场专用椅子#k,和#b鱼饵#k,这些你都可以通过我来购买.#b鱼竿#k请去点卷购物商场购买!");
                   cm.dispose();
        } else if (selection == 60) {
		cm.openNpc(9209001,10);
	}

    }
}


