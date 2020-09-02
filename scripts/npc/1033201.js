/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendPlayerToNpc("Afrien？弗洛伊德！你还好吗？");
	} else if (status == 1) {
		cm.sendNextNoESC("奔驰...你还活着.");
	} else if (status == 2) {
		cm.sendPlayerToNpc("当然。我设法窃取他带走。我不能让自己死了点。你怎么样？和其他人呢？他们在哪里?");
	} else if (status == 3) {
		cm.sendNextNoESC("我们可能已经击败了黑法师，但他把大家在这最后的法术不同的方向飞行。我们很幸运，我们在同一个地方结束了.");
	} else if (status == 4) {
		cm.sendPlayerToNpc("我不知道走了多远，我们结束了。至少我们是安全的。我感觉浑身无力......冷......它已经一向白雪皑皑在这里？它是热的，但雪正在下降。奇怪...");
	} else if (status == 5) {
		cm.sendNextNoESC("你感觉不到吗？奔驰，伟大的诅咒......一直在你身上发生，弗洛伊德，和其他人。一个冰冷冷的诅咒，抱住你。它看起来像黑法师是不是让我们那么容易..");
	} else if (status == 6) {
		cm.sendPlayerToNpc("C-诅咒......你应该可以吧，但什么生存弗洛伊德？他看起来很虚弱...");
	} else if (status == 7) {
		cm.sendNextNoESC("我会照顾他。就目前而言，我更担心你。你是#b精灵的统治者.#k 如果诅咒你，它会被放在#r所有的精灵!#k 快点回#bElluel#k 如果 #b黑法师的诅咒是所有精灵#k, 那么你必须回到你的人.");
	} else if (status == 8) {
		cm.sendPlayerToNpc("...! 好吧！ Afrien，我们会再见面!");
	} else if (status == 9) {
		cm.sendPlayerToNpc("(其他英雄会让它通过某种方式。现在，我会用我的技能返回镇.)");
	} else if (status == 10) {
		cm.warp(910150001,0);
		cm.dispose();
	}
}