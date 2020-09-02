/*
 * Papulatus
 */

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status >= 1) {
	    status--;
	} else {
	    cm.dispose();
	    return;
	}
    }
    if (status == 0) {
	cm.sendNext("您准备好恢复原来的时间段吗？ 尺寸裂纹目前是开放的。 记住，这是我第一次这样做，所以有失败的可能性。 说的是，我很有信心这将工作！ 我将确保您发送回您的原始时间段!");
    } else if (status == 1) {
	cm.sendNextPrev("现在，我们开始之前，想的时间和地点，你以前住在该裂缝裂缝将认识到思想，送你到那个地方。我想看看你的未来!");
    } else if (status == 2) {
	if (cm.getPlayer().getSkillLevel(5121010) <= 0) {
	   // cm.teachSkill(5121010, 0, 10);
	}
	cm.forceStartQuest(6364,"2"); //开始任务
	cm.removeAll(4031870);
	cm.warp(220080000);
	cm.dispose();
    }
}