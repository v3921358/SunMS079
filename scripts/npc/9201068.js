/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;

function start() {
    status = -1;
    Subway = cm.getEventManager("Subway");
    action(1, 0, 0);
}

function action(mode, type, selection) {
    status++;
    if(mode == 0) {
	cm.sendNext("你有一些经济的负担而无法搭船对吧?");
	cm.dispose();
	return;
    }
    if (status == 0) {
	if(Subway == null) {
	    cm.sendNext("找不到脚本请联系GM！");
	    cm.dispose();
	} else if(Subway.getProperty("entry").equals("true")) {
	    cm.sendYesNo("看起来这里有很多空间。 请让你的票准备好，所以我可以让你，乘坐将很长，但你会到达你的目的地很好。 你怎么看？ 你想上这个旅程吗？");
	} else if(Subway.getProperty("entry").equals("false") && Subway.getProperty("docked").equals("true")) {
	    cm.sendNext("地铁正准备起飞。 对不起，但你必须下一轮。");
	    cm.dispose();
	} else {
	    cm.sendNext("我们将在起飞前1分钟开始登机。 请耐心等待几分钟。 请注意，地铁将准时起飞，我们将在1分钟前停止接收车票，因此请务必准时到达");
	    cm.dispose();
	}
    } else if(status == 1) {
	if(cm.haveItem(4031713)) {
		cm.gainItem(4031713, -1);
	    cm.warp(600010002);
		cm.dispose();
} else if(cm.haveItem(4031711)) {
		cm.gainItem(4031711, -1);
	    cm.warp(600010004);
		cm.dispose();
	} else {
        cm.sendNext("哦不，我不认为你有机票。 请在票务亭买票!.");
	}
	cm.dispose();
    }
}