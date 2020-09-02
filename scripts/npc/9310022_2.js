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
			text += "\t\t\t#e欢迎来到#b明日谷飞天猪兑换金币中心 #k!#n\r\n"
			text += "#n#k当前点卷点：#r" +cm.getPlayer().getCSPoints(1) +  "#k点    豆豆点：#r" + cm.getPlayer().getBeans() + "#k点\r\n"
            text += "#L1##b1个#v5220040#兑换#v2140000#10万金币#l\r\n\r\n"
            text += "#L2##b2个#v5220040#兑换#v2140000#20万金币#l\r\n\r\n"
            text += "#L3##b5个#v5220040#兑换#v2140001#50万金币#l\r\n\r\n"
            text += "#L4##b10个#v5220040#兑换#v2140002#100万金币#l\r\n\r\n"
            cm.sendSimple(text);
        } else if (selection == 1) { 
		  if (cm.getMeso() >= 2000000000){//判断多少金币
                cm.sendOk("你的背包金币超过20E,无法兑换！");
        } else if (cm.haveItem(5220040,1)){
			    cm.gainItem(5220040, -1);
			    cm.gainMeso(100000);//给金币	
				cm.sendOk("1个#v5220040#兑换10万金币成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]1个飞天猪兑换10万金币成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有1个飞天猪无法兑换10万金币！");
				cm.dispose();
			}
        } else if (selection == 2) {  
		  if (cm.getMeso() >= 2000000000){//判断多少金币
                cm.sendOk("你的背包金币超过20E,无法兑换！");
        } else if (cm.haveItem(5220040,2)){
			     cm.gainItem(5220040, -2);
			    cm.gainMeso(200000);//给金币
				cm.sendOk("2个#v5220040#兑换20万金币成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]2个飞天猪兑换20万金币成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有2个飞天猪无法兑换20万金币！");
				cm.dispose();
			}
        } else if (selection == 3) { 
		  if (cm.getMeso() >= 2000000000){//判断多少金币
                cm.sendOk("你的背包金币超过20E,无法兑换！");
        } else if (cm.haveItem(5220040,5)){
			     cm.gainItem(5220040, -5);
			    cm.gainMeso(500000);//给金币
				cm.sendOk("5个#v5220040#兑换50万金币成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]5个飞天猪兑换50万金币成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有5个飞天猪无法兑换50万金币！");
				cm.dispose();
			}
        } else if (selection == 4) {
		  if (cm.getMeso() >= 2000000000){//判断多少金币
                cm.sendOk("你的背包金币超过20E,无法兑换！");
        } else if (cm.haveItem(5220040,10)){
			     cm.gainItem(5220040, -10);
			    cm.gainMeso(1000000);//给金币
				cm.sendOk("10个#v5220040#兑换100万金币成功！");
			    cm.worldMessage(6,"【兑换系统】["+cm.getName()+"]10个飞天猪兑换100万金币成功！");
				cm.dispose();
			}else{
				cm.sendOk("你没有10个飞天猪无法兑换100万金币！");
				cm.dispose();
			}
			}
			}
		}
    



