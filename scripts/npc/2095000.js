function action(mode, type, selection) {
    if (cm.getQuestStatus(6410) == 1) {
	cm.forceStartQuest(6411, "p2");
	cm.sendNext("谢谢你!");
    } else {
	cm.sendNext("谢谢你!");
	cm.warp(120000000)
    }
    cm.dispose();
}