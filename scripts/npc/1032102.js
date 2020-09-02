/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var pet = null;
var theitems = Array();

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0) {
			cm.sendOk("好吧，下次见.");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendSimple("我是宠物专家，我主要处理宠物. #b\r\n#L0#进化宠物龙.#l\r\n#L1#进化机器人.#l\r\n#L2#复活我的宠物.#l#k");
		} else if (status == 1) {
			if (selection == 0) {
				var currentpet = null;
				for (var i = 0; i < 3; i++) {
					currentpet = cm.getPlayer().getPet(i);
					if (currentpet != null && pet == null) {
						if (currentpet.getSummoned() && currentpet.getPetItemId() > 5000028 && currentpet.getPetItemId() < 5000034 && currentpet.getLevel() >= 15) {
							pet = currentpet;
							break;
						}
					}
				}
				if (pet == null || !cm.haveItem(5380000,1)) {
					cm.sendOk("你不符合要求。你需要 #i5380000##t5380000#, 以及任何一个 #d#i5000029##t5000029##k, #g#i5000030##t5000030##k, #r#i5000031##t5000031##k, #b#i5000032##t5000032##k, 或 #e#i5000033##t5000033##n 宠物等级15级以上，再来与我对话吧。");
					cm.dispose();
				} else {
					var id = pet.getPetItemId();
					var name = pet.getName();
					var level = pet.getLevel();
					var closeness = pet.getCloseness();
					var fullness = pet.getFullness();
					var slot = pet.getInventoryPosition();
					var flag = pet.getFlags();
					var rand = 0;
					var after = id;
					while (after == id) {
						rand = 1 + Math.floor(Math.random() * 10);
						if (rand >= 1 && rand <= 3) {
							after = 5000030;
						} else if (rand >= 4 && rand <= 6) {
							after = 5000031;
						} else if (rand >= 7 && rand <= 9) {
							after = 5000032;
						} else if (rand == 10) {
							after = 5000033;
						}
					}
					if (name.equals(cm.getItemName(id))) {
						name = cm.getItemName(after);
					}
					cm.getPlayer().unequipPet(pet, true, false);
					cm.gainItem(5380000, -1);
					cm.removeSlot(5, slot, 1);
					cm.gainPet(after, name, level, closeness, fullness, 45, flag);
					cm.getPlayer().spawnPet(slot);
					cm.sendOk("你的龙现在进化了！它曾经是一个 #i" + id + "##t" + id + "#, 现在是一个 #i" + after + "##t" + after + "#!");
					cm.dispose();
				}
			} else if (selection == 1) {
				var currentpet = null;
				for (var i = 0; i < 3; i++) {
					currentpet = cm.getPlayer().getPet(i);
					if (currentpet != null && pet == null) {
						if (currentpet.getSummoned() && currentpet.getPetItemId() > 5000047 && currentpet.getPetItemId() < 5000054 && currentpet.getLevel() >= 15) {
							pet = currentpet;
							break;
						}
					}
				}
				if (pet == null || !cm.haveItem(5380000,1)) {
					cm.sendOk("你不符合要求。你需要 #i5380000##t5380000#, 以及任何一个 #g#i5000048##t5000048##k, #r#i5000049##t5000049##k, #b#i5000050##t5000050##k, #d#i5000051##t5000051##k, #d#i5000052##t5000052##k, 或 #e#i5000053##t5000053##n 宠物等级15级以上，再来与我对话吧。");
					cm.dispose();
				} else {
					var id = pet.getPetItemId();
					var name = pet.getName();
					var level = pet.getLevel();
					var closeness = pet.getCloseness();
					var fullness = pet.getFullness();
					var slot = pet.getInventoryPosition();
					var flag = pet.getFlags();
					var rand = 0;
					var after = id;
					while (after == id) {
						rand = 1 + Math.floor(Math.random() * 9);
						if (rand >= 1 && rand <= 2) {
							after = 5000049;
						} else if (rand >= 3 && rand <= 4) {
							after = 5000050;
						} else if (rand >= 5 && rand <= 6) {
							after = 5000051;
						} else if (rand >= 7 && rand <= 8) {
							after = 5000052;
						} else if (rand == 9) {
							after = 5000053;
						}
					}
					if (name.equals(cm.getItemName(id))) {
						name = cm.getItemName(after);
					}
					cm.getPlayer().unequipPet(pet, true, false);
					cm.gainItem(5380000, -1);
					cm.removeSlot(5, slot, 1);
					cm.gainPet(after, name, level, closeness, fullness, 45, flag);
					cm.getPlayer().spawnPet(slot);
					cm.sendOk("你的机器现在已经进化！它曾经是一个 #i" + id + "##t" + id + "#, a现在它是一个 #i" + after + "##t" + after + "#!");
					cm.dispose();
				}
			} else if (selection == 2) { //revive	
				var inv = cm.getInventory(5);
				var pets = cm.getPlayer().getPets(); //includes non-summon
				for (var i = 0; i <= inv.getSlotLimit(); i++) {
					var it = inv.getItem(i);
					if (it != null && it.getItemId() >= 5000000 && it.getItemId() < 5010000 && it.getExpiration() > 0 && it.getExpiration() < cm.getCurrentTime()) {
						theitems.push(it);

					}
				}
				if (theitems.length <= 0) {
					cm.sendOk("你没有死亡的宠物.");
					cm.dispose();
				} else {
					var selStr = "请选择哪一种宠物来复活。你需要生命之水来复活它.#b\r\n";
					for (var i = 0; i < theitems.length; i++) {
						selStr += "\r\n#L" + i + "##v" + theitems[i].getItemId() + "##i" + theitems[i].getItemId() + "##l";
					}
					cm.sendSimple(selStr);
				}
			}
		} else if (status == 2) {
			if (theitems.length <= 0) {
				cm.sendOk("你没有死亡的宠物.");
			} else if (!cm.haveItem(5180000,1)) {
				cm.sendOk("You will need the #v5180000##i5180000#.");
			} else {
				theitems[selection].setExpiration(cm.getCurrentTime() + (45 * 24 * 60 * 60 * 1000));
				cm.getPlayer().fakeRelog();
				cm.sendOk("你的宠物的寿命从今天起一直延长到45天以后.");
				cm.gainItem(5180000,-1);
			}
			cm.dispose();
		}
	}
}