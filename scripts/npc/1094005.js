/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var item;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getQuestStatus(2186) == 1) {
	    var rand = Math.floor(Math.random() * 2);
	    if (rand == 0 && !cm.haveItem(4031853)) {
		item = 4031853;
	    } else if (rand == 1) {
		item = 4031854;
	    } else {
		item = 4031855;
	    }
	    cm.gainItem(item, 1);
	    if (item == 4031853) {
		cm.sendNext("我找到了阿别的眼镜");
	    } else {
		cm.sendOk("我发现了一副眼镜，但这似乎不是阿别的...");
	    }
	}
	cm.dispose();
    }
}