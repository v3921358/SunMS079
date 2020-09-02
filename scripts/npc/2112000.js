var status = -1;

function action(mode, type, selection) {
    var em = cm.getEventManager("Romeo");
    if (em == null) {
	cm.sendOk("尝试着使用技能攻击,消灭所有怪物!");
	cm.dispose();
	return;
    }
    if (em.getProperty("stage").equals("1") && em.getProperty("stage5").equals("0")) {
	//advance to angry!
	cm.sendOk("什么...一个可疑的阴谋？这不可能...");
	em.setProperty("stage", "2");
    } else if (em.getProperty("stage5").equals("1") && cm.getMap().getAllMonstersThreadsafe().size() == 0) {
	cm.sendOk("继续.");	
	em.setProperty("stage5", "2");
	cm.getMap().setReactorState();
    } else {
	cm.sendOk("尝试着使用技能攻击,消灭所有怪物!");
    }
    cm.dispose();
}