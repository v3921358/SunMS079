var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if( cm.getPlayer().getBossLog("mhuan") == 1){
			cm.sendOk("你已经已经完成任务了喔,请明天再来吧");
			cm.dispose();
			}else{
			cm.sendNext("想找个刺激又好玩的地方吗?梦幻主题公园将会是你第一首选.但,最近梦幻主题公园还有点危险.你知道怎么了吗?");
			}
		} else if (status == 1) {
            cm.sendNext("我的好友,#b艾尔多鲁#k告诉我在梦幻主题公园正被一群怪物围攻,而她是唯一一人正在处理此事,但这群怪物一天比一天强大,她非常需要他人的帮助.");
			
		} else if (status == 2) {
            cm.sendAcceptDecline("你能助她一臂之力保持梦幻主题公园的安全吗?");
		} else if (status == 3) {
			cm.gainItem(4032246, 2);
			cm.getPlayer().setBossLog('mhuan');
			cm.completeQuest();
            cm.sendOk("太好了!我现在不能离开甘榜村,#b艾尔多鲁#k知道有关在梦幻主题公园两只怪物的详情,请去找#b艾尔多鲁#k吧,她会教你下一步该怎么做.");
			cm.dispose();
		} 
	}
}