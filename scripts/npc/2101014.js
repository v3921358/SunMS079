/**
 * @author: Eric
 * @npc: Cesar
 * @func: Ariant PQ
*/

var status = 0;
var sel;
var empty = [false, false, false];
var closed = false;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection){
    (mode == 1 ? status++ : status--);
    if (status == 0) {
		cm.sendSimple("#e<组队副本：阿里安特竞技场>#n\r\n欢迎来到竞技场里可以对抗其他玩家和展示你的能力.#b\r\n#L0#[进入开设房间].\r\n#L1#[游戏内容说明]\r\n#L2#[向我解释玩法]\r\n#L3#[剩余挑战次数]\.\r\n#L4#[兑换物品奖励].");
	} else if (status == 1) {
		if (selection == 0) {
			if (closed || (cm.getPlayer().getLevel() < 21 && !cm.getPlayer().isGM())) {
				cm.sendOk(closed ? "阿里安特竞技场是一个好玩的地方,请稍后再回来." : "21级~200级. 对不起，您可能不参加.");
				cm.dispose();
				return;
			}
			var text = "你想要什么?#b";
			for(var i = 0; i < 3; i += 1)
				if (cm.getPlayerCount(980010100 + (i * 100)) > 0)
					if (cm.getPlayerCount(980010101 + (i * 100)) > 0)
						continue;
					else
						text += "\r\n#L" + i + "# 竞技场房间 " + (i + 1) + " (" + cm.getPlayerCount(980010100 + (i * 100)) + "/" + cm.getPlayer().getAriantSlotsRoom(i) + " 角色,队长: " + cm.getPlayer().getAriantRoomLeaderName(i) + ")#l";
				else {
					empty[i] = true;
					text += "\r\n#L" + i + "# 竞技场房间 " + (i + 1) + " (空)#l";
					if (cm.getPlayer().getAriantRoomLeaderName(i) != "")
						cm.getPlayer().removeAriantRoom(i);
				}
			cm.sendSimple(text);
		} else if (selection == 1) {
			cm.sendNext("阿里安特竞技场是一个激烈的战场，真正的战士将被崇拜！即使你是个胆小鬼，你也不要害怕！一个冒险家能使阿列达喜爱的宝石最会被选为最好的宝物！很简单，是吧？\r\n - #e等级#n : 50或以上#r(推荐等级 : 50 - 80 )#k\r\n - #e时间限制#n : 8 分钟\r\n - #e队员#n : 2-6\r\n - #e物品#n#i3010018:#椰子树沙滩椅");
			cm.dispose();
		} else if (selection == 2) {
			status = 9;
			cm.sendNext("你想知道竞技场的玩法吗？好吧,我会向你详细说明");
		} else if (selection == 3) {
			var ariant = cm.getQuestRecord(150139);
			var data = ariant.getCustomData();
			if (data == null) {
				ariant.setCustomData("10");
				data = "10";
			}
			cm.sendNext("#r#h ##k, 你可以参加竞技场 #b" + parseInt(data) + "#k 时间（今天）.");
			cm.dispose();
			return;
		} else if (selection == 4) {
			status = 4;
			cm.sendNext("你有什么资本兑换物品？如果你的竞技场分数高于 150, 你将得到 #i3010018:# #b椰子树沙滩椅#k.\r\n");
		}
	} else if (status == 2) {
		var sel = selection;
		if(cm.getPlayer().getAriantRoomLeaderName(sel) != "" && empty[sel])
            empty[sel] = false;
        else if(cm.getPlayer().getAriantRoomLeaderName(sel) != "") {
			cm.warp(980010100 + (sel * 100));
            cm.dispose();
            return;
        }
        if (!empty[sel]) {
            cm.sendNext("另一个勇士取得了竞技场第一,我建议你要么建立一个新的，要么加入战斗竞技场已经成立.");
            cm.dispose();
            return;
        }
		cm.getPlayer().setApprentice(sel);
        cm.sendGetNumber("有多少玩家可以参加这场比赛? (2~6人)", 0, 2, 6);
	} else if (status == 3) {
		var sel = cm.getPlayer().getApprentice(); // how 2 final in javascript.. const doesn't work for shit
		if (cm.getPlayer().getAriantRoomLeaderName(sel) != "" && empty[sel])
			empty[sel] = false;
        if (!empty[sel]) {
            cm.sendNext("另一个勇士取得了竞技场第一,我建议你要么建立一个新的，要么加入战斗竞技场已经成立.");
            cm.dispose();
            return;
        }
        cm.getPlayer().setAriantRoomLeader(sel, cm.getPlayer().getName());
        cm.getPlayer().setAriantSlotRoom(sel, selection);
        cm.warp(980010100 + (sel * 100));
		cm.getPlayer().setApprentice(0);
        cm.dispose();
	} else if (status == 5) {
		cm.sendNextPrev("你的竞技场分数只有 #b0#k. 你必须得分高于#b150#k 得到 #b椰子树沙滩椅#k.足够高的分数来证明你有资格获得.");
	} else if (status == 6) { // todo: code champion rings :c
		cm.dispose();
	} else if (status == 10) {
		cm.sendNextPrev("让我告诉你最简单的规则,#b灵魂宝石#k数量将被选为最佳胜利者！当然,如果你赢得了一场比赛的话，你会得到更高的赞美#b无数冠军#k.\r\n\r\n(#b当比赛结束时，你的排名将会被你所拥有的灵魂宝石所决定。此外，如果更多的参与者继续，您将获得更多的奖励.)#k");
	} else if (status == 11) {
		cm.sendNextPrev("即使你不够坚强,也不要担心。如果你能获得#b至少15#k 灵魂的宝石,也会获得奖励.\r\n\r\n(如果获得#b至少 15 灵魂宝石,你将获得一份奖励.)#k");
	} else if (status == 12) {
		cm.sendNextPrev("如果你获得的#r灵魂宝石#多于15个.当然我们会对这种特殊的冠军奖励更多的奖励！这并不意味着你会得到 #r无限的量奖励#k, 虽然。如果你获得#b30#k宝石,你会得到的#r丰厚奖励#k.\r\n\r\n(用#b30#r灵魂宝石#k获得更加丰厚奖励.)#k");
	} else if (status == 13) {
		cm.sendNextPrev("如果你一事无成,并无法获得15个#b灵魂宝石#,那就意味着你不会得到任何奖励? 不，那不可能是这样！我们美丽的女王阿烈达吩咐我们给予一定的奖励 #b冠军谁甚至失败了至少有15个#k宝石。在这种情况下，你会得到 #r较少的奖励#k. 有什么抱怨吗？如果你不喜欢它，参加比赛也是一种乐趣！\r\n\r\n(如果你获得#b少于15个灵魂宝石，你将获得比较少的奖励.)#k");
	} else if (status == 14) {
		cm.sendNextPrev("当然,一个懒惰的失败者,即使拥有#b6个灵魂宝石#k都觉得太多了#r你要做的#k, 那么，这仅仅意味着你没有达到标准。不管怎样，你几乎不会获得#r任何奖励#k 为比赛中的比赛。所以,加油获得至少6个或更多的宝石.\r\n\r\n(如果你获得#b5个或更少的灵魂宝石，你将不会获得任何奖励.)#k");
	} else if (status == 15) {
		cm.sendNextPrev("最后, #r失败者#k 和不能完成任务的失败者 #b时间限制#k 将获得一些奖励的基础上 #r过去的时间#k.\r\n\r\n(#b如果竞技场就在它的中间停了下来，奖励将根据经过的时间获得相对应的奖励.)#k");
		cm.dispose();
		return;
	}
}
