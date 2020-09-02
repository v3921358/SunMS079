
function action(mode, type, selection) {
    if (cm.getQuestStatus(6230) == 1) {
	if (!cm.haveItem(4031456)) {
	    if (cm.haveItem(4031476)) {
		if (cm.canHold(4031456)) {
		    cm.gainItem(4031456, 1);
		    cm.gainItem(4031476, -1);
		    cm.sendOk("枫叶吸收到闪闪发光的玻璃球." );
		    cm.safeDispose();
		} else {
		    cm.sendOk("枫叶珠可以得到，但是你背包已经满了。 请留空，然后重试." );
		    cm.safeDispose();
		}
	    } else {
		cm.dispose();
	    }
	} else {
	    cm.dispose();
	}
    } else {
	cm.dispose();
    }
}