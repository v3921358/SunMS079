/* 
 * Shawn, Victoria Road: Excavation Site<Camp> (101030104)
 * Guild Quest Info
 */

var status;
var selectedOption;

function start() {
    selectedOption = -1;
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (mode == 1 && status == 3) {
	status = 0;
    }
    if (status == 0) {
	if (cm.getQuestStatus(6201) == 1) {
	    var dd = cm.getEventManager("Relic");
	    if (dd != null) {
		dd.startInstance(cm.getPlayer());
	    } else {
		cm.sendOk("发生未知错误.");
	    }
	    cm.dispose();
	} else {
	    var prompt = "\r\n.#l";
	    if (selectedOption == -1) {
		prompt = "我们的联盟会， 一直试图破译（翡翠平板）, 一个珍贵的文物，很长一段时间。因此, 我们发现了鲁碧安, 他从过去的神秘国家,在这里沉睡。我们还发现了线索 #t4001024#, 一个传奇，神话般的珠宝，可能鲁碧安仍然在这里。这就是为什么家族任务最终找到 #t4001024#." + prompt;
	    } else {
		prompt = "你还有其他问题吗?" + prompt;
	    }
	    cm.sendSimple(prompt);
	}
    } else if (status == 1) {
	selectedOption = selection;
	if (selectedOption == 0) {
	    cm.sendNext("sharenian从曾在维多利亚岛的各个区域控制过去的文化文明。魔像的寺庙，在地牢深处的神殿，和其他古老的建筑结构，没有人知道是谁建造了它确实是在sharenian时期.");
	}
	else if (selectedOption == 1) {
	    cm.sendNext("#t4001024# 是一个传奇的宝石，它给拥有它的人带来永恒的青春。具有讽刺意味的是，它似乎是每个人都有#t4001024# 结束了受压迫的，应说明sharenian倒台。");
	    status = -1;
	}
	else if (selectedOption == 2) {
	    cm.sendNext("我已经派组探险家sharenian之前，但他们都没有回来，这促使我们开始工会任务。我们一直在等待的行会是强大到足以承担艰难的挑战，公会喜欢你.");
	}
	else if (selectedOption == 3) {
	    cm.sendOk("真的？如果你有别的事要问，请随时和我说话.");
	    cm.dispose();
	}
	else {
	    cm.dispose();
	}
    }
    else if (status == 2) { //should only be available for options 0 and 2
	if (selectedOption == 0) {
	    cm.sendNextPrev("sharenian的最后一个国王被一位叫砂仁III，显然他是个非常聪明和富有同情心的国王。但是有一天，整个王国都崩溃了，并没有为它的解释.");
	}
	else if (selectedOption == 2) {
	    cm.sendNextPrev("本公会追求的终极目标是探索和发现sharenian #t4001024#. 这不是一个权力解决一切的任务。这里更重要的是团队合作。");
	}
	else {
	    cm.dispose();
	}
    }
}