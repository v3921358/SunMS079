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
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendNextNoESC("那么，你为什么要到枫树岛呢？没有多少人前往该方式的这些日子。通过你的衣服来看，你是不是旅游或者.");
	} else if (status == 1) {
		cm.sendPlayerToNpc("我要去枫岛进行训练......在那之后，我前往维多利亚岛成为一个伟大的冒险家！这就是它是如何工作的，对吧?");
	} else if (status == 2) {
		cm.sendNextNoESC("它肯定不会！枫树岛是训练的好地方，因为没有危险的怪物存在。另外，你会让很多朋友，学习基础知识。当你准备好，有一个大的，广阔的世界在那里你探索!");
	} else if (status == 3) {
		cm.sendPlayerToNpc("嘿，我都等不及了！我会真的很辛苦训练，并学会承担了所有的强大的怪物。我完全准备!");
	} else if (status == 4) {
		cm.sendNextNoESC("真是一个伟大的态度！这将帮助你成功。你永远不能肯定会发生什么，但。 只要记住, #b一切发生的原因.#k");
	} else if (status == 5) {
		cm.sendPlayerToNpc("嘿，你有没有听到什么?");
	} else if (status == 6) {
		cm.sendDirectionStatus(4, 0);
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionInfo("Effect/Summon.img/15");
		cm.sendDirectionStatus(1, 2000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/npc/0");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/0");
		cm.sendDirectionStatus(1, 1000);
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face02");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/npc/1");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/1");
		cm.sendDirectionStatus(1, 1000);
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face05");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog/0");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Mob/8150000.img/attack2/info/effect");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/2");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionStatus(3, 6);
		cm.sendDirectionInfo("Mob/8130100.img/attack1/info/effect");
		cm.sendDirectionInfo("Mob/8130100.img/attack1/info/hit");
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face01");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog/1");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/3");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Mob/8150000.img/attack2/info/hit");
		cm.warp(912060100,0);
		cm.dispose();
	}
}