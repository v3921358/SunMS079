/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
【】var status = -1;
var sel = 0;
var sec = 0;

function action(mode, type, selection) {
	var battlers = cm.getPlayer().getBoxed();
	if (mode != 1) {
    		cm.dispose();
	} else {
		status++;
		if (status == 0) {
			if (battlers.size() <= 0) {
				cm.sendOk("你没有怪物储存.");
				cm.dispose();
				return;
			}
			var selStr = "检查其统计?\r\n\r\n#b";
			for (var i = 0; i < battlers.size(); i++) {
				if (battlers.get(i) != null) {
					selStr += "#L" + i + "#" + battlers.get(i).getName() + " (#o" + battlers.get(i).getMonsterId() + "#) Level " + battlers.get(i).getLevel() + " " + battlers.get(i).getGenderString() + "#l\r\n";
				}
			}
			cm.sendSimple(selStr);
		} else if (status == 1) {
			if (selection < 0 || selection >= battlers.size() || battlers.get(selection) == null) {
				cm.dispose();
				return;
			}
			sel = selection;
			var info = "#e" + battlers.get(selection).getName() + "#n (#o" + battlers.get(selection).getMonsterId() + "#)\r\n";
			info += "Level " + battlers.get(selection).getLevel() + " " + battlers.get(selection).getGenderString() + "\r\n";
			info += "EXP " + battlers.get(selection).getExp() + "/" + battlers.get(selection).getNextExp() + "\r\n";
			info += "HP " + battlers.get(selection).calcHP() + "\r\n";
			info += "ATK: " + battlers.get(selection).getATK(0) +  ", DEF: " + battlers.get(selection).getDEF() + "%\r\n";
			info += "Sp.ATK: " + battlers.get(selection).getSpATK(0) +  ", Sp.DEF: " + battlers.get(selection).getSpDEF() + "%\r\n";
			info += "Speed: " + battlers.get(selection).getSpeed() +  ", Evasion: " + battlers.get(selection).getEVA() + ", Accuracy: " + battlers.get(selection).getACC() + "\r\n";
			info += "Element: " + battlers.get(selection).getElementString() + "\r\n";
			info += "Nature: " + battlers.get(selection).getNatureString() + "\r\n";
			info += "Item: " + battlers.get(selection).getItemString() + "\r\n";
			info += "Ability: " + battlers.get(selection).getAbilityString() + "\r\n";
			info += "\r\n#b";
			info += "#L0#我如何演变这个?#l\r\n";
			info += "#L1#释放这个怪物.#l\r\n";
			info += "#L3#重命名此怪物.#l\r\n";
			info += "#L5#借此怪兽.#l\r\n";
			info += "#L6#放弃/取件.#l\r\n";
			info += "#L7#评价这个妖怪.#l\r\n";
			cm.sendSimple(info);
		} else if (status == 2) {
			sec = selection;
			if (selection == 0) { //how i evolve
				var evo = battlers.get(sel).getEvolutionType().value;
				if (evo == 0) {
					cm.sendNext("恭喜你，你已经达到了进化的最后阶段.");
					cm.dispose();
				} else if (evo == 1) {
					cm.sendNext("你有一个安静很长的路要走，因为你必须平整更多一些.");
					cm.dispose();
				} else if (evo == 2) {
					var selStr = "您只能通过某一个项目发展。我可以发展它。让我们来看看这里...\r\n\r\n";
					if (cm.haveItem(battlers.get(sel).getFamily().evoItem.id)) {
						cm.sendSimple(selStr + "#L0##v" + battlers.get(sel).getFamily().evoItem.id + "##z" + battlers.get(sel).getFamily().evoItem.id + "##l");
					} else {
						cm.sendNext(selStr + "您没有进化项目需要。需要: #v" + battlers.get(sel).getFamily().evoItem.id + "##z" + battlers.get(sel).getFamily().evoItem.id + "#");
						cm.dispose();
					}
				}
				
			} else if (selection == 1) {
				cm.sendYesNo("您确定要释放的怪物 " + battlers.get(sel).getName() + " (#o" + battlers.get(sel).getMonsterId() + "#)?");
			} else if (selection == 3) {
				cm.sendGetText("请输入您的怪物的新名称。 （最小：2个字符，最多：20个字符）");
			} else if (selection == 5) {
				if (cm.getPlayer().countBattlers() >= 6) {
					cm.sendOk("你已经有六个怪物.");
					cm.dispose();
					return;
				}
				var battt = cm.getPlayer().getBattlers();	
				for (var i = 0; i < battt.length; i++) {
					if (battt[i] != null && battlers.get(sel).getMonsterId() == battt[i].getMonsterId()) {
						cm.sendOk("你已经有了这个怪物.");
						cm.dispose();
						return;
					}
				}
				cm.getPlayer().getBattlers()[cm.getPlayer().countBattlers()] = battlers.get(sel);
				battlers.remove(sel);
				cm.getPlayer().changedBattler();
				cm.sendOk("怪物已经被带走了.");
			} else if (selection == 6) {
				if (battlers.get(sel).getItem() != null) {
					if (cm.canHold(battlers.get(sel).getItem().id, 1)) {
						cm.gainItem(battlers.get(sel).getItem().id, 1);	
						cm.sendOk("你已经从这个怪物的项目.");
						battlers.get(sel).setItem(0);
					} else {
						cm.sendOk("请库存空间.");
					}
					cm.dispose();
					return;
				}
				var selStr = "你想给这个怪物哪个项目?#b\r\n";
				var hi = cm.getAllHoldItems();
				var pass = false;
				for (var i = 0; i < hi.length; i++) {
					if (cm.haveItem(hi[i].id, 1)) {
						pass = true;
						selStr += "#L" + i + "##i" + hi[i].id + "#" + hi[i].customName + "#l\r\n";
					}
				}
				if (!pass) {
					cm.sendNext("您没有保留项目.");
					cm.dispose();
				} else {
					cm.sendSimple(selStr);
				}
			} else if (selection == 7) {
				cm.sendNext(battlers.get(sel).getIVString());
				cm.dispose();
			}
		} else if (status == 3) {
			if (sec == 0) {
				if (cm.haveItem(battlers.get(sel).getFamily().evoItem.id)) {
					cm.gainItem(battlers.get(sel).getFamily().evoItem.id, -1);
					battlers.get(sel).evolve(true, cm.getPlayer());
					cm.getPlayer().changedBattler();
					cm.playSound(false, "5th_Maple/gaga");
					cm.sendNext("你的怪物已经演变!!!");
				}
			} else if (sec == 1) {
				battlers.remove(sel);
				cm.getPlayer().changedBattler();
				cm.sendNext("它已被释放!");
			} else if (sec == 3) {
				if (cm.getText().length() < 2 || cm.getText().length() > 20) {
					cm.sendOk(cm.getText() + " 不能接受.");
				} else {
					cm.getPlayer().changedBattler();
					battlers.get(sel).setName(cm.getText());
				}
			} else if (sec == 6) {
				var hi = cm.getAllHoldItems()[selection];
				if (cm.haveItem(hi.id, 1)) {
					cm.gainItem(hi.id, -1);
					battlers.get(sel).setItem(hi.id);
					cm.sendOk("该项目已设置到怪物.");
				}
			}
			cm.dispose();
		}
	}
}