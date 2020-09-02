
function start() {
	if(cm.getzb() > 0){
			var 充值金额 = cm.getzb();
			var 点卷倍率 = 充值金额 * 150;
		//	var 积分倍率 = 充值金额 * 1;
			cm.gainDJ(点卷倍率);
		//	cm.gainjf(积分倍率);
			cm.setzb(-充值金额);
			cm.sendOk("您已成功领取： "+点卷倍率+"点卷!\r\n点卷已添加到您的帐户! \r\n#r感谢您的赞助!#k");
			cm.getChar().saveToDB(false, false);
			cm.worldMessage(11, cm.getC().getChannel(),"〖充值系统〗" + " : " + " [" + cm.getPlayer().getName() + "]充值了"+ 点卷倍率 +"点卷,赶快去商场挑选喜欢的物品吧！", false);  
			cm.dispose();
		} else {
			cm.sendOk("请确定你是否赞助过！");
			cm.dispose();
		}
}

