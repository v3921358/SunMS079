var map;
var cost;
var location;
var mapname;
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.sendNext("嗯......我是危险地带出租车,我的费用可是很贵,你想好了吗？");
	cm.dispose();
	return;
    }

    if (status == 0) {
	switch (cm.getMapId()) {
	    case 540000000: // CBD
		map = 541020000;
		cost = 30000;
		mapname = "乌鲁城";
		break;
	    case 240000000: // Leafre
		map = 240030000;
		cost = 150000;
		mapname = "神木村-龙林入口";
		break;
	    case 220000000: // Ludi
		map = 220050300;
		cost = 100000;
		mapname = "时间通道";
		break;
	    case 211000000: // El Nath
		map = 211040200;
		cost = 100000;
		mapname = "冰雪峡谷II";
		break;
	    default:
		map = 211040200;
		cost = 100000;
		mapname = "冰雪峡谷II";
		break;
	}
	cm.sendNext("你好！这种高级出租车将带你从任何危险区域 #m"+cm.getMapId()+"# 到 #b#m"+map+"##k 再 "+mapname+"! 费用 #b"+cost+" 金币#k 可能看起来很贵，但并不多，当你想方便地通过危险区域到达!");
    } else if (status == 1) {
	cm.sendYesNo("#b你需要支付金币#k 传送到 #b#m"+map+"##k?");
    } else if (status == 2) {
	if (cm.getMeso() < cost) {
	    cm.sendNext("你的金币不够,我不能搭你！");
	    cm.dispose();
	} else {
	    cm.gainMeso(-cost);
	    cm.warp(map, 0);
	    cm.dispose();
	}
    }
}