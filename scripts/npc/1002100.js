/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var amount = -1;
var item;
var cost;
var rec;
var recName;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status <= 2 && mode == 0) {
	cm.dispose();
	return;
    } else if (status >= 3 && mode == 0) {
	cm.sendNext("我仍然有相当多的材料，你得到了之前。这些物品都有，所以请选择你的时间.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getQuestStatus(2013) == 2) {
	    cm.sendNext("是你呀！多亏了你，我才能完成很多事情。现在我已经做了一堆的物品。如果你需要任何东西，请告诉我.");
	} else {
	    if (cm.getQuestStatus(2010) == 2)
		cm.sendNext("你似乎没有足够的强大到能够购买我的药水 ...");
	    else
		cm.sendOk("我的梦想是到处旅行，很像你。我的父亲，但是，不允许我这样做，因为他认为这是非常危险的。他可能会说是的，虽然，如果我给他一些证明，我不是他认为我是胆小的女孩 ...");
	    cm.dispose();
	}
    } else if (status == 1) {
	var selStr = "你想买哪一种?#b";
	var items = new Array(2000002, 2022003, 2022000, 2001000);
	var costs = new Array(310, 1060, 1600, 3120);
	for (var i = 0; i < items.length; i++) {
	    selStr += "\r\n#L" + i + "##z" + items[i] + "# (价格 : " + costs[i] + " 金币)#l";
	}
	cm.sendSimple(selStr);
    } else if (status == 2) {
	var itemSet = new Array(2000002, 2022003, 2022000, 2001000);
	var costSet = new Array(310, 1060, 1600, 3120);
	var recHpMp = new Array(300, 1000, 800, 1000);
	var recNames = new Array("HP", "HP", "MP","HP and MP");
	item = itemSet[selection];
	cost = costSet[selection];
	rec = recHpMp[selection];
	recName = recNames[selection];
	cm.sendGetNumber("你想要的 #b#t" + item + "##k? #t" + item + "# 让你恢复 " + rec + " " + recName + ". 你想买多少?", 1, 1, 100);
    } else if (status == 3) {
	cm.sendYesNo("你要购买 #r" + selection + "#k #b#t" + item + "##k? #t" + item + "# 一个 " + cost + " 金币, 所以总数是 #r" + cost * selection + "#k 金币.");
	amount = selection;
    } else if (status == 4) {
	if (cm.getMeso() < cost * amount || !cm.canHold(item)) {
	    cm.sendNext("你没有背包空格吗？请检查，看看你是否有足够的空间，你至少要有 #r" + cost * amount + "#k 金币.");
	} else {
	    cm.gainMeso(-cost * amount);
	    cm.gainItem(item, amount);
	    cm.sendNext("谢谢您的光临。这里的东西都是可以做的，所以如果你需要的东西，请再来找我.");
	}
	cm.dispose();
    }
}