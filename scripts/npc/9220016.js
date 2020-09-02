function action(mode, type, selection) {
    if (cm.getPlayer().getBossLog("baozang") == 3){
    cm.sendOk("每天最多只可以接3次任务!");
	cm.dispose();
	} else {
    if (cm.isQuestActive(5067)==0) {//判断任务
	cm.forceStartQuest(5067); //开始任务
	cm.getPlayer().setBossLog('baozang');
	cm.dispose();
	}}}