var status = -1;

function action(mode, type, selection) {
   if (cm.haveItem(4031579,4) ){
	cm.removeAll(4031579);
    cm.playerMessage("任务完成.");
	cm.forceCompleteQuest(3923);
	cm.forceCompleteQuest(3924);
	cm.forceCompleteQuest(3925);
    cm.forceCompleteQuest(3926);
	cm.gainExp(1000);
	cm.dispose();
    } else if (cm.isQuestActive(3929)) {
	cm.playerMessage("任务完成.");
	cm.forceCompleteQuest(3929);
	cm.gainExp(1000);
	cm.dispose();
	} else {
    cm.sendOk("请问你找我有什么事情吗?");
	cm.dispose();
    }
}