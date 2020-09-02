importPackage(Packages.client);
importPackage(Packages.client.inventory);
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
                text += "";//
            }
			text += "\t\t\t #e#v3992009#欢迎来到#r知音公益岛#v3992009# #k#n\r\n"
			text += "\t\t #e#v3992010#  您当前点卷为：#b"+cm.getPlayer().getCSPoints(1)+"   #v3992010##k#n\r\n#e#b#v3992011#这里是枫叶兑换成点卷打任何怪物有机率爆枫叶#v3992011# #k       请再次确认你的背包栏有足够的空间\r\n"
		    text += "\t #L6##r#v4001126#枫叶兑换点卷（批量1 : 1）#l\r\n\r\n"//3
            text += "\t #L0##b我有#b1个#v4000463#兑换#r【100商城点卷】#l\r\n\r\n"//3
		    text += "\t #L1##b我有#b10个#v4000463#兑换#r【1000商城点卷】#l\r\n\r\n"//3
			text += "\t #L2##b我有#b100个#v4000463#兑换#r【10000商城点卷】#l\r\n\r\n"//3
			text += "\t #L3##b100点卷兑换1个#v4000463##r【国庆纪念币】#l\r\n\r\n"//3
			text += "\t #L4##b1000点卷兑换10个#v4000463##r【国庆纪念币】#l\r\n\r\n"//3
			text += "\t #L5##b10000点卷兑换100个#v4000463##r【国庆纪念币】#l\r\n\r\n"//3
            cm.sendSimple(text);
			 } else if (selection == 0) {
			if (cm.haveItem(4000463, 1)) {
			cm.gainItem(4000463,-1);
			cm.getPlayer().modifyCSPoints(1, 100);
			cm.sendOk("你获得了100点卷！。");
		}else{
            cm.sendOk("#b物品不足无法兑换！.");
		}
                    cm.dispose();
			} else if (selection == 1) {
			if (cm.haveItem(4000463, 10)) {
			cm.gainItem(4000463,-10);
			cm.getPlayer().modifyCSPoints(1, 1000);
			cm.sendOk("你获得了1000点卷！。");
		}else{
            cm.sendOk("#b物品不足无法兑换！.");
		}
                    cm.dispose();
			} else if (selection == 2) {
			if (cm.haveItem(4000463, 100)) {
			cm.gainItem(4000463,-100);
			cm.getPlayer().modifyCSPoints(1, 10000);
			cm.sendOk("你获得了10000点卷！。");
		}else{
            cm.sendOk("#b物品不足无法兑换！.");
		}
                    cm.dispose();
			} else if (selection == 3) {
				if (cm.getInventory(4).isFull(0)){//判断第四个也就是其它栏的装备栏是否有一个空格
		        cm.sendOk("#b请保证其它栏位至少有1个空格,否则无法抽奖.");
		        cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 100) {
					item = cm.gainGachaponItem(4000463, 1);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 1 + "个。");
						cm.getPlayer().modifyCSPoints(1, -100);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();	
				}
					} else if (selection == 4) {
										if (cm.getInventory(4).isFull(0)){//判断第四个也就是其它栏的装备栏是否有一个空格
		        cm.sendOk("#b请保证其它栏位至少有1个空格,否则无法抽奖.");
		        cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 1000) {
					item = cm.gainGachaponItem(4000463, 10);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 10 + "个。");
						cm.getPlayer().modifyCSPoints(1, -1000);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
				 }		
	          } else if (selection == 5) {
				if (cm.getInventory(4).isFull(0)){//判断第四个也就是其它栏的装备栏是否有一个空格
		        cm.sendOk("#b请保证其它栏位至少有1个空格,否则无法抽奖.");
		        cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 10000) {
					item = cm.gainGachaponItem(4000463, 100);
					if (item != -1) {
						cm.sendOk("你获得了 #b#t" + item + "##k " + 100 + "个。");
						cm.getPlayer().modifyCSPoints(1, -10000);
					} else {
						cm.sendOk("请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
					}
                    cm.dispose();
                    
                } else {
                    cm.sendOk("#b您没有足够的点卷进行购买,请充值.");
                    cm.dispose();
				 }					
          }  else if (selection == 6) {
            cm.openNpc(9000041, 1);				 
	 }
	 }
			}