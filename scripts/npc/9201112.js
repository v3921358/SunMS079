var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    var em = cm.getEventManager("CWKPQ");
    if (em == null) {
	cm.sendNext("事件没有启动 ...");
	cm.dispose();
	return;
    }
    switch(cm.getPlayer().getMapId()) {
	case 803000502:
	    if (status == 0) {
		cm.sendYesNo("你好厉害,果然不负我所望找到了念力钥匙.不过我有件事想提醒你,不管是游戏内获得的念力钥匙,还是商城购买的念力钥匙,1个念力钥匙1天可允许入场2次哦.你现在要用1回现金念力钥匙么?");
	    } else if (status == 1) {
		if(cm.getPlayer().getBossLog("xjcishu") == 3){//判断现金次数
        cm.sendOk("挑战每天进入次数：3次\t你已经进入：#r"+cm.getPlayer().getBossLog("xjcishu")+"#k次 !");//对话提示
		cm.dispose();
		} else if(cm.getPlayer().getBossLog("ptcishu") == 2){//判断非现金次数
        cm.sendOk("挑战每天进入次数：2次\t你已经进入：#r"+cm.getPlayer().getBossLog("ptcishu")+"#k次 !");//对话提示
		cm.dispose();
		} else if (cm.haveItem(5252006)) {//现金念力钥匙
		cm.getPlayer().setBossLog('xjcishu');//给现金次数
		cm.gainItem(5252006, -1);//扣除现金念力钥匙
		cm.warp(803000505, 0);//传送地图
		cm.dispose();
		} else if (cm.haveItem(4001374)) {//念力钥匙
		cm.getPlayer().setBossLog('ptcishu');
		cm.gainItem(4001374, -1);//扣除念力钥匙
		cm.warp(803000505, 0);//传送地图
		cm.dispose();
	    } else {
		cm.sendOk("你没有#v5252006#念力钥匙. ");
		cm.dispose();
	    }
		}
	    break;
	case 803000505:
          if (cm.getPlayer().getMapId() == 108010101 || cm.getPlayer().getMapId() == 108010201 || cm.getPlayer().getMapId() == 108010301 || cm.getPlayer().getMapId() == 108010401 || cm.getPlayer().getMapId() == 108010501) {
                cm.sendOk("本地图暂时无法使用使用拍卖功能");
                cm.dispose();
                return;
            }
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "好的····你想干什么呢？\r\n" 
            text += "#L1##b进入迷宫（难度：普通远征队专用）#l\r\n"
            text += "#L2##b进入迷宫（难度：困难远征队专用）#l\r\n"
			text += "#L3##bBOSS怪物大战（单人）#l\r\n"
			text += "#L4##bBOSS怪物大战（组队）#l\r\n\r\n\r\n"
			text += "注意：1天总共可以使用3次念力钥匙。1个念力钥匙每次可以进行2次组队任务。BOSS怪物大战1天最多可参加5次。\r\n"
            cm.sendOk(text); 
	   break;
	case 610030300:
	   if (status == 0) {
		cm.sendNext("现在我们这里有更多的印记.五的冒险者们爬上,穿过门. ");
	   } else if (status == 1) {
		cm.sendNext("当心这些死亡陷阱,他们真的打了一拳. ");
		cm.dispose();
	   }
	   break;
    }
}