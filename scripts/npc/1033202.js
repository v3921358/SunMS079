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
	if (cm.isQuestActive(24007) || cm.isQuestFinished(24007)) {
		cm.sendNext("请救救我们.");
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendPlayerToNpc("长老！你还活着！发生什么事了?");
	} else if (status == 1) {
		cm.sendNextNoESC("一场激烈的，冷冻的诅咒在镇已经下降，还有你，殿下。从你的，最重要的是，事实上。这是黑法师的力量?");
	} else if (status == 2) {
		cm.sendNextNoESC("孩子们已经被困在冰。大人会跟着他们;它需要时间来冻结更强精灵，所有这就是为什么你没事，但我们不.", 1033204);
	} else if (status == 3) {
		cm.sendPlayerToNpc("这是我的错。我让黑法师骂我们呢...");
	} else if (status == 4) {
		cm.sendNextNoESC("所以，这是他做的......我知道这.", 1033203);
	} else if (status == 5) {
		cm.sendNextNoESC("黑魔导士咒诅我们的主权，并诅咒已经蔓延...");
	} else if (status == 6) {
		cm.sendPlayerToNpc("拜托，我不是故意要做到这一点。我应该更小心...");
	} else if (status == 7) {
		cm.sendNextNoESC("甚至超越了密封条，黑法师挥舞寻求权力......这是一个奇迹，你能封住他.");
	} else if (status == 8) {
		cm.sendNextNoESC("这不是你的错。没有人可以阻止这一点。你是英雄.", 1033204);
	} else if (status == 9) {
		cm.sendPlayerToNpc("我不应该在所有的打他！我要是知道会发生这种事......我没有我的人...");
	} else if (status == 10) {
		cm.sendNextNoESC("不要说的事情经过测试！即使你让他做，他都为我们迟早.", 1033204);
	} else if (status == 11) {
		cm.sendNextNoESC("这是我们的错，我们就失败了你，你的殿下.");
	} else if (status == 12) {
		cm.sendPlayerToNpc("不！这不是你的错！我不后悔战斗......我只是遗憾没能保护你.");
	} else if (status == 13) {
		cm.sendNextNoESC("这是不是一个人你的负担。决定打精灵什么样的决定，我们都共享成果，不管他们可能是.", 1033204);
	} else if (status == 14) {
		cm.sendPlayerToNpc("...大家...");
	} else if (status == 15) {
		cm.sendNextNoESC("无论如何，我们才能生存下来。我们将共同克服这一点。对于精灵的希望住在只要殿下是安全的.");
	} else if (status == 16) {
		cm.sendNextNoESC("我们不能阻止诅咒，但我们可以活得比它。我们应该封Elluel前的诅咒可以扩散到村. #b我们应该精灵都在这里沉睡，不受干扰.#k时间是站在我们这一边，我们没有什么可担心的.", 1033204);
	} else if (status == 17) {
		cm.sendNextNoESC("最终，我们都将唤醒在一起。甚至没有诅咒将永远持续下去;我们会出现胜利者.");
	} else if (status == 18) {
		cm.sendPlayerToNpc("好的。我会封村与我的剩余强度...");
		cm.forceStartQuest(24007, "1");
		cm.dispose();
	}
}