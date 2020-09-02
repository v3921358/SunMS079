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
	if (status <= 1) {
	    cm.sendNext("需要去再来找我吧!");
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
    cm.sendSimple("你有没有听过海滩与所谓的 #b黄金海滩#k, 这个地方在维多利亚呢?? 我可以现在帮助你到那个地方只需要 #b15000 金币#k, 或者如果你有一张 #bVIP 到黄金海滩的票#k 那么就可以免费去..\r\n\r\n#L0##b 我愿意付 15000 金币.#l\r\n#L1# 我有一张 VIP 到黄金海滩的票.#l\r\n#L2# 什么是VIP到黄金海滩的票#k?#l");
    } else if (status == 1) {
	if (selection == 0) {
	    cm.sendYesNo("所以你想付 #b15000 金币#k 然后去黄金海滩??");
	} else if (selection == 1) {
	    status = 2;
	    cm.sendYesNo("所以你有一张 #bVIP 到黄金海滩的票#k?");
	} else if (selection == 2) {
	    status = 4;
	    cm.sendNext("就是有了它能让你免费去黄金海滩体验风景人情.");
	}
    } else if (status == 2) {
	if (cm.getMeso() < 15000) {
	    cm.sendNext("你没有足够的金币也没有VIP票 滚吧!");
	    cm.dispose();
	} else {
	    cm.gainMeso(-15000);
	    cm.saveLocation("FLORINA");
	    cm.warp(110000000, 0);
	    cm.dispose();
	}
    } else if (status == 3) {
	if (cm.haveItem(4031134)) {
	    cm.saveLocation("FLORINA");
	    cm.warp(110000000, 0);
	    cm.dispose();
	} else {
	    cm.sendNext("你确定你有#bVIP 到黄金海滩的票#k. 吗? 确认一下好吗....");
	    cm.dispose();
	}
    } else if (status == 4) {
	cm.sendNext("就是有了它能让你免费去黄金海滩体验风景人情.");
    } else if (status == 5) {
	cm.sendNextPrev("如果你有请一定要记得带来交给我~谢谢你。");
    } else if (status == 6) {
	cm.dispose();
    }
}