/*
	红鸾店 - 守卫兵 天长
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendSimple("这里结婚的红鸾宫门口。你想做什么？\r\n#L5#我想回家。#l\r\n");
    } else if (status == 1) {
        if (selection == 0) { //我想进去红鸾宫
            if (cm.getParty() == null || !cm.isLeader()) {
                cm.sendOk("只有组队长来跟我说才可以允许让你们进去。你们俩中队长来跟我说吧。");
                cm.dispose();
            } else {
                cm.sendYesNo("哦……你想进去红鸾殿吗？结婚乃人生大事，要进去必须要满足几个条件，如果你想听你要满足的条件。我愿给你说明。");
            }
        } else if (selection == 5) { //我想进去孤星殿。
            status = 15;
            cm.sendNext("你想回去吗？你这次下去再次上来的时候还要付费。");
        }
    } else if (status == 2) {
        cm.sendNext("好！我看看你是否满足结婚的条件后，就送你到宫殿里。");
    } else if (status == 15) {
            cm.warp(700000101);
            cm.dispose();
    } else if (status == 16) {
        var returnMap = cm.getSavedLocation("MULUNG_TC");
        cm.clearSavedLocation("MULUNG_TC");
        if (returnMap < 0) {
            returnMap = 211000000;
        }
        var target = cm.getMap(returnMap);
        var portal = target.getPortal("unityPortal2");
        if (portal == null) {
            portal = target.getPortal(0);
        }
        if (cm.getMapId() != target) {
            cm.getPlayer().changeMap(target, portal);
        }
        cm.dispose();
    }
}