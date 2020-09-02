var status = -1;
function action(mode, type, selection) {
    if (cm.getMapId() == 926100600) {
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	var em = cm.getEventManager("Romeo");
    if (em != null) {
	var itemid = 4001160;
	if (!cm.canHold(itemid, 1)) {
	    cm.sendOk("请清除1个背包栏位.");
	    cm.dispose();
	    return;
	}
	cm.gainItem(itemid, 1);
	if (em.getProperty("stage").equals("2")) {
    		cm.gainD(5000);
	} else {
		//cm.gainD(3500);
	}
	cm.gainExp_PQ(120, 1.0);
    }
    cm.addTrait("will", 25);
    cm.addTrait("sense", 1);
    cm.getPlayer().endPartyQuest(1205);
    cm.warp(926100700,0);
    cm.dispose();
    return;
    }
    if (mode > 0) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	cm.sendSimple("#b#L0#让我离开这里#l\r\n\r\n#L1#兑换#v1122118##k#z1122118##l");
    } else {
	if (selection == 0) {
    	    cm.warp(926100600,0);
	} else if (selection == 1) {
	    if (cm.canHold(cm.isGMS() ? 1122118 : 1122010,1) && cm.haveItem(4001160,10) && cm.haveItem(4001159,10)) {
		cm.gainItem(cm.isGMS() ? 1122118 : 1122010,1);
		cm.gainItem(4001160,-10);
		cm.gainItem(4001159,-10);
	    } else {
		cm.sendOk("你将需要10个#v4001160##k和10个#v4001159##k,得到爱的证明,以及确认你的背包是否有足够空间!");
	    }
	}
    	cm.dispose();
    }
}