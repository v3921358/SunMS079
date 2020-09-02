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
            text +=" 嗨.我是周末活动奖励兑换管理员.\r\n"
            text += " #r如果你有500个#v4031250##z4031250#的话可以找我兑换活动奖励.\r\n\r\n"
			text += "#b#v5201000#x500 #v5150040#x1 #v5110000#x1 #v2140002#x10万 #v5440000#x1500 \r\n            #v2022452#x10000#k    #v3012006#x1\r\n"			
			text += "#L0##e#d#v4031250#500个领取我的活动奖励#l\r\n\r\n"
			//text += "#L2##e#d#v5220000#快乐百宝券购买#l\r\n"
            cm.sendOk(text);
        } else if (selection == 0) {
		if (cm.haveItem(4031250, 500)) {//判断猪物品
		cm.gainItem(4031250, -500);//扣除猪物品
		cm.gainItem(1442039, 1, 1);//奖励物品+数量 或者cm.gainItem(1072369, 1, 1);奖励物品+数量+天数
		cm.gainItem(5150040, 1);		
		cm.gainItem(5110000, 1, 7);
		cm.gainItem(3012006, 1);		
		//cm.getPlayer().modifyCSPoints(1, +666, true);//给点券
		cm.getPlayer().modifyCSPoints(2, +1500, true);//给抵用券
		cm.getPlayer().gainBeans(+500);//给豆豆
		cm.gainMeso(100000);//给金币
		cm.gainExp(10000);		
		cm.sendOk("兑换活动奖励成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 领取了活动奖励!一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else{
		cm.sendOk("您没有足够的#v4031250##r#z4031250#!");
		cm.dispose();
			}		
	}
    }
}


