function action(mode, type, selection) {
    var em = cm.getEventManager("Romeo");
    if (em == null) {
	cm.sendOk("请稍后再试。.");
	cm.dispose();
	return;
    }
    switch(cm.getPlayer().getMapId()) {
	case 261000011:
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("队伍的队长必须在这里.");
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < 71 || ccPlayer.getLevel() > 200) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && (cm.getPlayer().isGM() || size == 4)) {
			em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
		} else {
			cm.sendOk("你方的4个成员必须在这个地图,等级71级以上.");
		}
	    }
	    break;
	case 926100000:
	    cm.sendOk("你应该试着在这里调查。看看图书馆里的文件，直到你找到实验室的入口.");
	    break;
	case 926100001:
	    cm.sendOk("请，消灭所有的怪物!");
	    break;
	case 926100100:
	    cm.sendOk("这些烧杯里面有漏洞。我们必须将可疑液体的烧杯的洋溢，所以我们可以继续.");
	    break;
	case 926100200:
	    if (cm.haveItem(4001130,1)) {
		cm.sendOk("哦，我写的信！谢谢你!");
		cm.gainItem(4001130,-1);
		em.setProperty("stage", "1");
	    } else if (cm.haveItem(4001134,1)) {
		cm.gainItem(4001134,-1);
		cm.sendOk("谢谢您!现在请看卡帕莱特的实验资料.");
		em.setProperty("stage4", "1");
	    } else if (cm.haveItem(4001135,1)) {
		cm.gainItem(4001135,-1);
		cm.sendOk("谢谢您!现在,请继续.");
		em.setProperty("stage4", "2");
		cm.getMap().getReactorByName("rnj3_out3").hitReactor(cm.getClient());
	    } else {
	    	cm.sendOk("我们必须找到文件,勇敢的你快帮帮我把!");
	    }
	    break;
	case 926100300:
	    cm.sendOk("我们必须到达实验室的顶部，你们的每一个成员.");
	    break;
	case 926100400:
	    cm.sendOk("只要你准备好了，我们就去救我的爱人.");
	    break;
	case 926100401:
	    cm.warpParty(926100500); //urete
	    break;
    }
    cm.dispose();
}