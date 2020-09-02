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

status = -1;
var sel, sel2;

function start() {
    cm.sendSimple("你好，冒险岛周末市场今天开幕。#b\r\n#L0#移动到冒险岛周末市场地图\r\n#L1#听冒险岛周末市场的说明");
}

function action(mode, type, selection) {
    status++;
    if (status == 6 && mode == 1) {
        sel2 = undefined;
        status = 0;
    }
    if (mode != 1) {
        if (mode == 0 && type == 0)
            status -= 2;
        else {
            cm.dispose();
            return;
        }
    }
    if (status == 0) {
        if (sel == undefined)
            sel = selection;
        if (selection == 0) {
            cm.sendNext("好的，我们会送你到冒险岛周末的市场地图.");
        } else
            cm.sendSimple("你想知道关于冒险岛周末市场?#b\r\n#L0#冒险岛周末市场在哪里发生?\r\n#L1#你能在冒险岛周末市场做什么?\r\n#L2#我没有任何问题.");
    } else if(status == 1) {
        if (sel == 0) {
            cm.warp(680100000 + parseInt(Math.random() * 3));
            cm.getPlayer().saveLocation("EVENT");//harhar
            cm.dispose();
        } else if (selection == 0) {
            cm.sendNext("冒险岛周末市场仅在星期天开幕。你可以进入，如果你发现我在任何城镇，射手村，新叶市村，废弃都市，玩具城，!");
            status -= 2;
        } else if (selection == 1)
            cm.sendSimple("你可以找到罕见的商品，很难找到其他地方在冒险岛周末市场.#b\r\n#L0#购买商品中介\r\n#L1#帮助农场主人");
        else {
            cm.sendNext("我想你没有任何问题。请让我们保持在你的想法，并问你是否对任何事情都感到好奇.");
            cm.dispose();
        }
    } else if (status == 2) {
        if (sel2 == undefined)
            sel2 = selection;
        if (sel2 == 0)
            cm.sendNext("你可以在冒险岛周末市场找到许多物品。价格会有变化，所以你最好在价格便宜的时候给他们!");
        else
            cm.sendNext("除了商人，你还可以找到在冒险岛周末市场农场老板的懒惰的女儿。帮助咪咪和孵化她的蛋，直到它长大成为一只鸡!");
    } else if (status == 3) {
        if (sel2 == 0)
            cm.sendNextPrev("在这里购买可以卖给商人的中介，阿德拉。他不会接受任何一周多的东西，所以确保你在星期六之前再卖了!");
        else
            cm.sendNextPrev("因为她不能只信任任何人，她会要求存钱。付她存款，好好照顾鸡蛋.");
    } else if (status == 4) {
        if (sel2 == 0)
            cm.sendNextPrev("阿德拉倒卖率以及调整自己，所以应该卖的时候你可以获得最大的利润。价格往往会波动每小时，所以记得经常检查.");
        else
            cm.sendNextPrev("如果你成功地把鸡蛋放进一只鸡，并把它带回咪咪，咪咪会奖励你。她可能很懒，但她不是不感激.");
    } else if (status == 5) {
        if (sel2 == 0)
            cm.sendNextPrev("测试你的商业智慧，通过购买在低价格冒险岛周末市场，并出售给商家的中介时，其价值上升!");
        else
            cm.sendNextPrev("你可以点击鸡蛋来检查它的生长。你必须勤奋升级鸡蛋会与你一起成长.");
    }
}