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
			text += "\t\t\t#e欢迎来到#b明日谷飞天猪购买金币中心 #k!#n\r\n"
			text += "#n#k当前点卷点：#r" +cm.getPlayer().getCSPoints(1) +  "#k点    豆豆点：#r" + cm.getPlayer().getBeans() + "#k点\r\n"
            text += "#L1##b200点券购买1个#v5220040##l\r\n\r\n"
            text += "#L2##b400点券购买2个#v5220040##l\r\n\r\n"
            text += "#L3##b1000点券购买5个#v5220040##l\r\n\r\n"
            text += "#L4##b2000点券购买10个#v5220040##l\r\n\r\n"
            cm.sendSimple(text);
        } else if (selection == 1) { 
		  if (cm.getPlayer().getCSPoints(1) >= 200) {//修炼点代码 记得换
		        cm.gainDJ(-200);
	            cm.gainItem(5220040, 1);//物品
				cm.sendOk("200点券购买1个飞天猪成功！");
			    cm.worldMessage(6,"【购买系统】["+cm.getName()+"]200点券购买1个飞天猪成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有200点券!");
				cm.dispose();
			}
        } else if (selection == 2) {  
		  if (cm.getPlayer().getCSPoints(1) >= 400) {//修炼点代码 记得换
		        cm.gainDJ(-400);
	            cm.gainItem(5220040, 2);//物品
				cm.sendOk("400点券购买2个飞天猪成功！");
			    cm.worldMessage(6,"【购买系统】["+cm.getName()+"]400点券购买2个飞天猪成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有400点券!");
				cm.dispose();
			}
        } else if (selection == 3) { 
		  if (cm.getPlayer().getCSPoints(1) >= 1000) {//修炼点代码 记得换
		        cm.gainDJ(-1000);
	            cm.gainItem(5220040, 5);//物品
				cm.sendOk("1000点券购买5个飞天猪成功！");
			    cm.worldMessage(6,"【购买系统】["+cm.getName()+"]1000点券购买5个飞天猪成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有1000点券!");
				cm.dispose();
			}
        } else if (selection == 4) {
		  if (cm.getPlayer().getCSPoints(1) >= 2000) {//修炼点代码 记得换
		        cm.gainDJ(-2000);
	            cm.gainItem(5220040, 10);//物品
				cm.sendOk("2000点券购买10个飞天猪成功！");
			    cm.worldMessage(6,"【购买系统】["+cm.getName()+"]2000点券购买10个飞天猪成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有2000点券!");
				cm.dispose();
			}
			}
			}
		}
    



