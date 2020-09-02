/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
if (cm.getPlayer().getMapId() != 140090000) {
if (status == 0) {
	cm.sendSimple("等待!下面列出的信息都可以简单地通过10级播放，所以这不是你需要学习的方式提前。只有那些想学习这些，在前进的人应该继续从这里出去 . \n\r 好的，这其中你想学习更多的 ?  \n\r #b#L1#迷你地图#l \n\r #b#L2#任务窗口#l \n\r #b#L3#使用药水#l \n\r #b#L4#普通攻击 #l \n\r #b#L5#拾取物品 #l \n\r #b#L6#装备物品#l \n\r #b#L7#技能窗口 #l \n\r #b#L8#使用快捷技能 #l \n\r #b#L9#打破箱子 #l \n\r #b#L10#坐在椅子上 #l \n\r #b#L11#查询大地图#l");
} else {
    cm.summonMsg(selection);
    cm.dispose();
}
} else {
    if (cm.getInfoQuest(21019).equals("")) {
	if (status == 0) {
	    cm.sendNext("你…终于醒了 !");
	} else if (status == 1) {
	    cm.sendNextPrevS("……你是谁？ ", 2);
	} else if (status == 2) {
	    cm.sendNextPrev("我一直在等你。等待战斗的英雄，黑色精灵终于醒来...!");
	} else if (status == 3) {
	    cm.sendNextPrevS("等等，你在说什么？你是谁……？ ", 2);
	} else if (status == 4) {
	    cm.sendNextPrevS("等待...谁是我？我不记得一件事从过去。哎哟...我有一个可怕的头痛！", 2);
	} else if (status == 5) {
	    cm.updateInfoQuest(21019, "helper=clear");
	    cm.showWZEffect("Effect/Direction1.img/aranTutorial/face");
	    cm.showWZEffect("Effect/Direction1.img/aranTutorial/ClickLirin");
	    cm.playerSummonHint(true);
	    cm.dispose();
	}
    } else {
	if (status == 0) {
	    cm.sendNext("你没事吗？ ");
	} else if (status == 1) {
	    cm.sendNextPrevS(". ..真的不记得一件事…我在哪里？你是谁？ ", 2);
	} else if (status == 2) {
	    cm.sendNextPrev("放松。黑魔法师的诅咒是你没有任何记忆的原因。没有必要担心过去发生的事情。我会详细向你解释 .");
	} else if (status == 3) {
	    cm.sendNextPrev("你在这里一个真正的英雄。几百年前，你和你的朋友们在黑色巫师的战斗中，从一定的毁灭中拯救了枫叶的世界。但在最后一个可能的时刻，黑色精灵给了你一个诅咒，你在冰上冰冻了很长一段时间，同时彻底抹去了你的记忆 .");
	} else if (status == 4) {
	    cm.sendNextPrev("你现在在一个岛屿叫做里恩，和它的黑巫师选择陷阱你几百年来岛上。由于他的诅咒，这个岛上总是覆盖着冰雪，即使天气是没有接近这个水平。你被发现在山洞深处的某个地方 .");
	} else if (status == 5) {
	    cm.sendNextPrev("我的名字是利琳，现在…希望终于得到了回报。你在这里，站在我的面前，活生生的传说.");
	} else if (status == 6) {
	    cm.sendNextPrev("我可能给你太多的信息了。如果你还没有抓住一切，那么这是好的。你迟早会发现的。平均时间， #b你应该去镇上 #k. 如果你在进城之前有任何问题，请随时问我 .");
	} else if (status == 7) {
	    cm.playerSummonHint(true);
	    cm.warp(140090100, 1);
	    cm.dispose();
	}
    }
}
}