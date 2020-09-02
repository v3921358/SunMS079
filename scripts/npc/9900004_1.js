//2017-30-31 修复38掉线问题 --lul

importPackage(java.util);
importPackage(net.sf.sunms.client);
importPackage(net.sf.sunms.server);
importPackage(java.util);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.tools.packet);
var 正在进行中 = "#fUI/UIWindow/Quest/Tab/enabled/1#";
var 完成 = "#fUI/UIWindow/Quest/Tab/enabled/2#";
var 正在进行中蓝 = "#fUI/UIWindow/MonsterCarnival/icon1#";
var 完成红 = "#fUI/UIWindow/MonsterCarnival/icon0#";
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
			text += "\t\t  #e#v3992017#欢迎来到在线奖励系统#v3992017#\r\n"
			text += "         #e#d#k#v4000443#当天在线时长：#r" + cm.getPlayer().getzxsj() +" #k#e分钟#k\r\n\r\n"
			if(cm.getPlayer().getzxsj() >= 60 && cm.getPlayer().getBossLog("zxsj") == 0){
					text += "#L2##r"+完成红+"在线时间超过60分钟 "+完成+"#v5310000#x1. #l\r\n\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() >= 60 && cm.getPlayer().getBossLog("zxsj") > 0){
					text += ""+完成红+"#r在线时间超过60分钟 #l"+完成+"\r\n\r\n"//3
				} else if(cm.getPlayer().getzxsj() < 60 && cm.getPlayer().getBossLog("zxsj") == 0){
					text += "#L1#"+正在进行中蓝+"#r在线时间超过60分钟 #l"+正在进行中+"#v5310000#x1\r\n\r\n"
			}else{
				text += ""+正在进行中蓝+"#r在线时间超过60分钟 #l"+"#v5310000#x1\r\n\r\n"
			}
			
			if(cm.getPlayer().getzxsj() >= 120 && cm.getPlayer().getBossLog("zxsj") == 1){
					text += "#L3##r"+完成红+"在线时间超过120分钟 "+完成+"#v5130000#x1.#l\r\n\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() >= 120 && cm.getPlayer().getBossLog("zxsj") > 1){
					text += ""+完成红+"#r在线时间超过120分钟 #l"+完成+"\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() < 120 && cm.getPlayer().getBossLog("zxsj") == 1){
					text += "#L1#"+正在进行中蓝+"#r在线时间超过120分钟 #l"+正在进行中+"#v5130000#x1\r\n\r\n"
			}else{
				text += ""+正在进行中蓝+"#r在线时间超过120分钟 #l"+"#v5130000#x1\r\n\r\n"
			}
			
			if(cm.getPlayer().getzxsj() >= 180 && cm.getPlayer().getBossLog("zxsj") == 2){
					text += "#L4##r"+完成红+"在线时间超过180分钟 "+完成+"#v5072000#x1.#l\r\n\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() >= 180 && cm.getPlayer().getBossLog("zxsj") > 2){
					text += ""+完成红+"#r在线时间超过180分钟 #l"+完成+"\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() < 180 && cm.getPlayer().getBossLog("zxsj") == 2){
					text += "#L1#"+正在进行中蓝+"#r在线时间超过180分钟 #l"+正在进行中+"#v5072000#x1\r\n\r\n"
			}else{
				text += ""+正在进行中蓝+"#r在线时间超过180分钟 #l"+"#v5072000#x1\r\n\r\n"
			}
			
			if(cm.getPlayer().getzxsj() >= 240 && cm.getPlayer().getBossLog("zxsj") == 3){
					text += "#L5##r"+完成红+"在线时间超过240分钟 "+完成+"#v4001322#x1.#l\r\n\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() >= 240 && cm.getPlayer().getBossLog("zxsj") > 3){
					text += ""+完成红+"#r在线时间超过240分钟 #l"+完成+"\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() < 240 && cm.getPlayer().getBossLog("zxsj") == 3){
					text += "#L1#"+正在进行中蓝+"#r在线时间超过240分钟 #l"+正在进行中+"#v4001322#x1.\r\n\r\n"
			}else{
				text += ""+正在进行中蓝+"#r在线时间超过240分钟 #l"+"#v4001322#x1.\r\n\r\n"
			}
			
			if(cm.getPlayer().getzxsj() >= 300 && cm.getPlayer().getBossLog("zxsj") == 4){
					text += "#L6##r"+完成红+"在线时间超过300分钟 "+完成+" 抵用卷x200.#l\r\n\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() >= 300 && cm.getPlayer().getBossLog("zxsj") > 4){
					text += ""+完成红+"#r在线时间超过300分钟 #l"+完成+"\r\n\r\n"
				} else if(cm.getPlayer().getzxsj() < 300 && cm.getPlayer().getBossLog("zxsj") == 4){
					text += "#L1#"+正在进行中蓝+"#r在线时间超过300分钟 #l"+正在进行中+" 抵用卷x200\r\n\r\n"
			}else{
				text += ""+正在进行中蓝+"#r在线时间超过300分钟 #l"+" 抵用卷x200\r\n\r\n"
			}
            cm.sendSimple(text);
			
        }else if (selection == 1) {
			cm.sendOk("请继续努力哦！");
			cm.safeDispose();
        }else if (selection == 2) {
			cm.gainItem(5310000, 1, 1);//幸运御守
			cm.getPlayer().setBossLog('zxsj');
            cm.sendOk("领取奖励成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了60分钟在线奖励 幸运御守.");
            cm.dispose();
        } else if (selection == 3) {
			cm.gainItem(5130000, 1, 1);//护身符
			cm.getPlayer().setBossLog('zxsj');
            cm.sendOk("领取奖励成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了120分钟在线奖励 护身符.");
            cm.dispose();
        } else if (selection == 4) {
			cm.gainItem(5072000, 1);//喇叭
			cm.getPlayer().setBossLog('zxsj');
            cm.sendOk("领取奖励成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了180分钟在线奖励 高质地喇叭.");
            cm.dispose();
        } else if (selection == 5) {
			if (cm.getInventory(5).isFull(1)){
			cm.sendOk("#b请保证特殊栏位至少有1个空格,否则无法领取.");
			cm.dispose();
		} else {
			cm.gainItem(4001322, 1);//蓝宝石
			cm.getPlayer().setBossLog('zxsj');
            cm.sendOk("领取奖励成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了240分钟在线奖励 蓝宝石.");
            cm.dispose();
			}
        } else if (selection == 6) {
			if (cm.getInventory(5).isFull(1)){
			cm.sendOk("#b请保证特殊栏位至少有1个空格,否则无法领取.");
			cm.dispose();
		} else {
			cm.gainD(200);//抵用卷
			cm.getPlayer().setBossLog('zxsj');
            cm.sendOk("领取奖励成功！");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]领取了300分钟在线奖励 抵用卷.");
            cm.dispose();
		}}
    }
}


