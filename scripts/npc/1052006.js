/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var meso = new Array(5000, 15000, 25000);
var item = new Array(4031036, 4031037, 4031038);
var selector;
var menu = "";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
	cm.dispose();
	return;
    } else if (status == 1 && mode == 0) {
	cm.sendNext("一旦你买了票,你就可以进入这个地方,我听说有奇怪的设备，在那里到处都是，但最终，罕见的珍贵物品等着你。所以让我知道，如果你曾经决定改变主意.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getPlayerStat("LVL") <= 19) {
	    cm.sendNext("你可以进入,一旦你买了票,但是它似乎不喜欢你可以进入这里.有国外的设备,可能是太多的你来处理,所以请训练自己，准备，然后回来.");
	    cm.dispose();
	} else {
	    for(var x=0; x < 3; x++) {
		if (cm.getPlayerStat("LVL") >= 20 && cm.getPlayerStat("LVL") <= 29) {
		    menu += "\r\n#L" + x + "##b工地 B" + x + "#k#l";
		    break;
		} else if (cm.getPlayerStat("LVL") >= 30 && cm.getPlayerStat("LVL") <= 39 && x < 2) {
		    menu += "\r\n#L" + x + "##b工地 B" + x + "#k#l";
		} else {
		    menu += "\r\n#L" + x + "##b工地 B" + x + "#k#l";
		}
	    }
	    cm.sendSimple("你必须购买票才能进入,一旦你购买了,你可以通过右边的检票口进入工地.你想买什么?" + menu);
	}
    } else if (status == 1) {
	selector = selection;
	selection += 1;
	cm.sendYesNo("你会买这张票吗 #b工地 B" + selection + "#k? 它会花费你 " + meso[selector] + " 金币. 在购买之前，请确保你背包有足够的空间");
    } else if (status == 2) {
	if (cm.getMeso() < meso[selector]) {
	    cm.sendNext("请检查你的背包空格.");
	    cm.dispose();
	} else {
	    if (selector == 0) {
		cm.sendNext("你可以在售票口插入票。我听说1区有一些珍贵的物品，但有这么多的陷阱，所有的地方最早回来了。祝你好运.");
	    } else if (selector == 1) {
		cm.sendNext("你可以在售票口中插入票。我听说2区有难得的，珍贵的物品，但有这么多的陷阱，所有的地方最早回来了。请注意安全.");
	    } else {
		cm.sendNext("你可以在售票口中插入票。我听说3区有非常罕见的，非常珍贵的项目，但有这么多的陷阱，所有的地方最早回来了。祝你好运.");
	    }
	    cm.gainMeso(-meso[selector]);
	    cm.gainItem(item[selector], 1);
	    cm.dispose();
	}
    }
}