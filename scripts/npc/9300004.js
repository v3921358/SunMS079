/* 
	脚本类型: 		NPC
	所在地图:		孤星殿
	脚本名字:		离婚NPC
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
        cm.sendSimple("你好，你来到这想要做什么？\r\n#b#L0#我要离婚#l#k");
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendYesNo("离婚吗？你确定吗？你想离婚？确定想好要离婚了吗？");
        } else {
            var selStr = "删除戒指吗？让我看看你有什么...";
            var found = false;
            for (var i = 1112300; i < 1112312; i++) {
                if (cm.haveItem(i)) {
                    found = true;
                    selStr += "\r\n#L" + i + "##v" + i + "##t" + i + "##l";
                }
            }
            for (var i = 2240004; i < 2240016; i++) {
                if (cm.haveItem(i)) {
                    found = true;
                    selStr += "\r\n#L" + i + "##v" + i + "##t" + i + "##l";
                }
            }
            if (!found) {
                cm.sendOk("你没有可删除的戒指。");
                cm.dispose();
            } else {
                cm.sendSimple(selStr);
            }
        }
    } else if (status == 2) {
        if (selection == -1) {
            if (cm.getPlayer().getMarriageId() <= 0) {
                cm.sendNext("我不清楚你是什么进来的，不过你好像还没有结婚或者已经离婚成功！");
                cm.dispose();
            } else {
				if (cm.getPlayer().hasEquipped(1112804)) {
		cm.sendOk("请卸下你的结婚戒指！.");
		cm.dispose();
           } else if (cm.getMeso() >= 1000000 && cm.getPlayer().getCSPoints(1) >=10000) {
                    cm.handleDivorce();
                    cm.gainMeso( - 1000000);
		            cm.gainDJ(-10000);//扣除点券
					cm.removeAll(1112804);
					cm.sendOk("离婚成功！.");
		            cm.dispose();
                } else {
                    cm.sendNext("离婚手续费需要100万金币跟1W点券,你没有足够的点券或者金币。");
                    cm.dispose();
                }
            }
        } else {
            if (selection >= 1112300 && selection < 1112312) {
                cm.gainItem(selection, -1);
                cm.sendOk("已经将你的 #v" + selection + "##t" + selection + "##l 删除..");
            } else if (selection >= 2240004 && selection < 2240016) {
                cm.gainItem(selection, -1);
                cm.sendOk("已经将你的 #v" + selection + "##t" + selection + "##l 删除..");
				cm.removeAll(1112804);
            }
        }
        cm.dispose();
    }
}