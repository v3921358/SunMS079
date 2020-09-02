/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var jobId;
var jobName;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0 && status == 2) {
		cm.sendOk("请重试.");
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		if (cm.getJob() == 0) {
			if (cm.getPlayer().getLevel() >= 10) {
				cm.sendNext("你要转职成为一位 #r飞侠#k ?");
			} else {
				cm.sendOk("你还没有资格能转职成为#r飞侠#k");
				cm.dispose();
			}
		} else {
			if (cm.getPlayer().getLevel() >= 30 && cm.getJob() == 400) { // 飞侠
				if (cm.haveItem(4031012, 1)) {
					if (cm.haveItem(4031012, 1)) {
						status = 20;
						cm.sendNext("我看到你完成了测试. 想要继续转职请点下一页!");
					} else {
						if (!cm.haveItem(4031011)) {
							cm.gainItem(4031011, 1);
						}
						cm.sendOk("请去找 #r飞侠转职教官#k.")
						cm.dispose();
					}
				} else {
					status = 10;
					cm.sendNext("你已经可以转职了,要转职请点下一页.");
				}
			} else if (cm.getPlayer().getLevel() >= 70 && cm.getJob() == 410 || cm.getJob() == 420) {
				if (cm.isQuestActive(100111)) {
				cm.sendOk("你完成了一个考验，现在去找 #艾瑞克#k.位于冰封雪域#b长老公馆#k!");
				cm.dispose();
				} else if (!cm.isQuestActive(100110)) {
				cm.sendOk("请先去去找 #r艾瑞克#k.接受相关任务在来找我!");
				cm.dispose();
				} else if (cm.haveItem(4031059, 1)) {
					//cm.gainItem(4031057, 1);
					cm.gainItem(4031059, -1);
					//cm.warp(211000001, 0);
					cm.forceStartQuest(100111); //开始任务
					cm.sendOk("你完成了一个考验，现在去找 #b艾瑞克#k.位于冰封雪域#b长老公馆#k!");
				} else {
					cm.sendOk("嗨, #b#h0##k! 我需要一个 #b黑符#k. 快去#r猴子沼泽地Ⅱ#k找#r异次元空间#k拿给我");
				}
				cm.dispose();
			} else if ((cm.getJob() == 412 || cm.getJob() == 422) && cm.isQuestActive(6141)) {
				cm.sendYesNo(" 你是否想要进入我的修炼场打败我的徒弟?");
				status = 30;
			} else {
				cm.sendOk("你好,我是飞侠转职官.");
				cm.dispose();
			}
		}
	} else if (status == 1) {
		cm.sendNextPrev("一旦转职了就不能反悔,如果不想转职请点上一页.");
	} else if (status == 2) {
		cm.sendYesNo("你真的要成为一位#r飞侠#k?");
	} else if (status == 3) {
		if (cm.getJob() == 0 && cm.getPlayer().getLevel() == 10) {
		            cm.changeJob(400); // 飞侠
            cm.resetStats(4, 4, 4, 25);
			} else if (cm.getJob() == 0 && cm.getPlayer().getLevel() >= 10) {
				cm.changeJob(400); // 飞侠
				 cm.resetStats(4, 4, 4, 25);
				cm.getPlayer().gainSP((cm.getPlayer().getLevel()-10)*3);//转职给技能点
        }
		cm.gainItem(1332063, 1);
		cm.gainItem(1472000, 1);
		cm.gainItem(2070000, 500);
		cm.gainItem(2070000, 500);
		cm.sendOk("转职成功!");
		cm.dispose();
	} else if (status == 11) {
		cm.sendNextPrev("你可以选择你要转职成为一位 #r刺客#k, #r侠客#k. ")
	} else if (status == 12) {
		cm.askAcceptDecline("但是我必须先测试你,你准备好了吗 ?");
	} else if (status == 13) {
		cm.removeAll(4031011);
		cm.gainItem(4031011, 1);
		//cm.warp(102040000);
		cm.sendOk("请去找 #b飞侠二转教官#k他位于#b废弃都市北方工地#k.他会帮助你的!");
		cm.dispose();
	} else if (status == 21) {
		cm.sendSimple("你想要成为什么?#b\r\n#L0#刺客#l\r\n#L1#侠客#l#k");
	} else if (status == 22) {
		var jobName;
		if (selection == 0) {
			jobName = "刺客";
			job = 410; // FIGHTER
		} else if (selection == 1) {
			jobName = "侠盗";
			job = 420; // PAGE
		}
		cm.sendYesNo("你真的要成为一位 #r" + jobName + "#k?");
	} else if (status == 23) {
		cm.changeJob(job);
		cm.removeAll(4031857);
	    cm.removeAll(4031856);
	    cm.removeAll(4031012);
		cm.sendOk("转职成功!");
		cm.dispose();
	} else if (status == 31) {
		 map = cm.getPlayer().getMap();
		if (cm.getPlayerCount(910300000) <= 0) {
			cm.resetMap(910300000);
			cm.warp(910300000, 0);
			cm.getPlayer().startMapTimeLimitTask(1200, map);
		} else {
			cm.sendOk("地图内已有其他玩家，请耐心等候。!");
		}
		cm.dispose();
	}
}
