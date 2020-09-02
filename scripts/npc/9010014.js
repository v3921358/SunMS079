/* 	Aramia
 * 	Henesys fireworks NPC
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
		 if (cm.getPlayer().getClient().getChannel() != 1) {
			cm.sendNext("该活动只能在1频道进行.");
			cm.dispose();
			return;
		}
    if (status == 0) {
	cm.sendNext("嗨,我阿米乐.我知道如何做鞭炮!如果你可以收集然后给我，我们可以制作一个烟花！请把所有的火药桶，你从怪物身上获得火药桶.");
    } else if (status == 1) {
	cm.sendSimple("每次玩家收集所需的火药桶，我可以拿来制作烟花! \n\r #b#L0# 捐赠火药桶.#l#k \n\r #b#L1# 查看收集的火药桶的进度.#l#k");
    } else if (status == 2) {
	if (selection == 1) {
	    cm.sendNext("火药桶进度状态 \n\r #B"+cm.getKegs()+"# \n\r 如果我们收集满，我们可以开始放烟花...");
	    cm.safeDispose();
	} else if (selection == 0) {
	    cm.sendGetNumber("你带来了火药桶吗？那么，请给我 #b火药桶#k 你有。我会做一个好的爆竹。你愿意给我多少? \n\r #b< 背包火药桶数 : #c4001128# >#k", 0, 0, 10000);
	}
    } else if (status == 3) {
	var num = selection;
	if (num == 0) {
	    cm.sendOk("我需要火药桶开始的制作烟花....\r\n 请再想一想，和我说话.");
	} else if (cm.haveItem(4001128, num)) {
	    cm.gainItem(4001128, -num);
	    cm.giveKegs(num);
	    cm.sendOk("别忘了给我当你得到他们的火药桶.");
	}
	cm.safeDispose();
    }
}