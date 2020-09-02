/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 var status = -1;

function action(mode, type, selection) {
    if (cm.getQuestStatus(21002) == 0) {
	if (mode == 1) {
	    status++;
	} else {
	    status--;
	}
	if (status == 0) {
	    cm.sendNext("哦，战神，你醒了！受伤怎么样？…什么？你想知道现在发生了什么吗 ?");
	} else if (status == 1) {
	    cm.sendNextPrev("我们都准备好了离开这个地方。我们有船上的每个人在方舟上，和神圣的鸟提供保护我们的方舟在飞行过程中，所以没有什么你需要担心。一旦我们完成了一切，我们将继续前进，逃到金银岛.");
	} else if (status == 2) {
	    cm.sendNextPrev("战神…？好...他们去战斗了黑魔法师。当我们逃跑时，他们决定带上黑色巫师。什么？你想和他们一起参加战斗吗？不，没有办法！你受伤了！你现在应该在船上！ ");
	} else if (status == 3) {
	    cm.forceStartQuest(21002, "1");
	    // Ahh, Oh No. The kid is missing
	    cm.showWZEffect("Effect/Direction1.img/aranTutorial/Trio");
	    cm.dispose();
	}
    } else {
	if (mode == 1) {
	    status++;
	} else {
	    status--;
	}
	if (status == 0) {
	    cm.sendSimple("我们处于紧急状态。你想知道什么？\r #b#L0#孩子在哪里？#l \r #b#L1#逃跑的准备如何？#l \r #b#L2#同志们怎么样？#l");
	} else if (status == 1) {
	    switch (selection) {
		case 0:
		    cm.sendOk("我听说那个黑魔法师已经接近我们现在的地方了。我们甚至不能通过森林逃脱，因为龙的黑色精灵控制。这就是为什么我们想出了方舟作为我们的逃生路线。我们能离开这个地方的唯一办法就是乘着飞往金银岛的飞机 .");
		    break;
		case 1:
		    cm.sendOk("我们有船上的每个人都在方舟里，我们都准备好了，并准备逃离这个地方。我们只需要更多的在船上，我们去金银岛。在我们的飞行，神鸟提供保护，因为她没有人保护此时.");
		    break;
		case 2:
		    cm.sendOk("你的战友…离开这里，战斗黑精灵自己，买一些时间，因为我们使逃脱。他们决定不带你，因为你是受伤和所有。一旦我们拯救孩子的时候，你应该一上船离开了我们。");
		    break;
	    }
	    cm.safeDispose();
	}
    }
}