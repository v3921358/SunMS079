/*
	This file is part of the OdinMS Maple Story Server
	Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU Affero General Public License as
	published by the Free Software Foundation version 3 as published by
	the Free Software Foundation. You may not use, modify or distribute
	this program under any other version of the GNU Affero General Public
	License.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU Affero General Public License for more details.

	You should have received a copy of the GNU Affero General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Guild Alliance NPC
 */

var status;
var choice;
var guildName;
var partymembers;

function start() {
	//cm.sendOk("The Guild Alliance is currently under development.");
	//cm.dispose();
	partymembers = cm.getPartyMembers();
	status = -1;
	action(1,0,0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendSimple("你好！我是 #b蕾那丽#k\r\n#b#L0#你能告诉我家族联盟是什么吗?#l\r\n#L1#我如何组成家族联盟?#l\r\n#L2#我想创建联盟.#l\r\n#L3#我要提高联盟人数上限.#l\r\n#L4#我想解散联盟.#l");
	} else if (status == 1) {
		choice = selection;
	    if (selection == 0) {
		    cm.sendOk("家族联盟，就像说，一个多个家族联盟形成一个联盟。我负责管理这些家族.");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendOk("要组成一个联盟，2个家族的族长需要组队。这个组队的队长将被分配作为家族的主人.");
			cm.dispose();
		} else if(selection == 2) {
			if (cm.getPlayer().getParty() == null || partymembers == null || partymembers.size() != 2 || !cm.isLeader()) {
				cm.sendOk("你可能不会创建一个联盟，直到你进入一个2人的一方"); //Not real text
				cm.dispose();
			} else if (partymembers.get(0).getGuildId() <= 0 || partymembers.get(0).getGuildRank() > 1) {
				cm.sendOk("你不能形成一个联盟，直到你拥有一个家族");
				cm.dispose();
			} else if (partymembers.get(1).getGuildId() <= 0 || partymembers.get(1).getGuildRank() > 1) {
				cm.sendOk("你的队员似乎没有家族.");
				cm.dispose();
			} else {
				var gs = cm.getGuild(cm.getPlayer().getGuildId());
				var gs2 = cm.getGuild(partymembers.get(1).getGuildId());
				if (gs.getAllianceId() > 0) {
					cm.sendOk("如果你已经加入了一个不同的联盟，你不能形成一个家族联盟.");
					cm.dispose();
				} else if (gs2.getAllianceId() > 0) {
					cm.sendOk("你的队员已经加入了家族.");
					cm.dispose();
				} else if (cm.partyMembersInMap() < 2) {
					cm.sendOk("请在同一张地图上找到你的另一方成员.");
					cm.dispose();
				} else
                			cm.sendYesNo("哦，你有兴趣组建家族联盟吗?");
			}
		} else if (selection == 3) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("要增加能力，你需要付出 10,000,000 金币. 你确定要继续吗?"); //ExpandGuild Text
			} else {
			    cm.sendOk("只有家族联盟的主人可以扩大家族的人数上限.");
				cm.dispose();
			}
		} else if(selection == 4) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("你确定你想解散家族联盟?");
			} else {
				cm.sendOk("只有家族联盟的主人可以解散家族联盟。");
				cm.dispose();
			}
		}
	} else if(status == 2) {
	    if (choice == 2) {
		    cm.sendGetText("现在请输入你的新家族联盟的名字. (最多12个字母)");
		} else if (choice == 3) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("你不能增加一个不存在的家族联盟.");
				cm.dispose();
			} else {
				if (cm.addCapacityToAlliance()) {
					cm.sendOk("你已经增加了你的联盟的能力.");
				} else {
					cm.sendOk("你的联盟已经有太多的家族。5是最大值.");
				}
				cm.dispose();
			}
		} else if (choice == 4) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("你不能解散一个不存在的家族联盟.");
				cm.dispose();
			} else {
				if (cm.disbandAlliance()) {
					cm.sendOk("你的家族联盟被解散");
				} else {
					cm.sendOk("一个错误发生的时候解散公会联盟");
				}
				cm.dispose();
			}
		}
	} else if (status == 3) {
		guildName = cm.getText();
	    cm.sendYesNo("将 #b"+ guildName + "#k 是你家族联盟的名字?");
	} else if (status == 4) {
			if (!cm.createAlliance(guildName)) {
				cm.sendNext("此名称不可用，请选择另一个名称"); //Not real text
				status = 1;
				choice = 2;
			} else
				cm.sendOk("你已经成功组建了一个家族联盟.");
			cm.dispose();
	}
}