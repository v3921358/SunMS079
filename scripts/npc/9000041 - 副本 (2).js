 
importPackage(Packages.client);
importPackage(Packages.client.inventory);


var status = -1;
var beauty = 0;
var tosend = 0;
var sl;
var mats;
function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            if (status == 0) {
                cm.sendNext("如果需要抵用卷中介服务在来找我吧。");
                cm.dispose();
            }
            status--;
        }
        if (status == 0) {
            var gsjb = "";
            gsjb ="  #e此处兑换 #b- 抵用券 -\r\n\r\n";
            gsjb +="  当前点卷:#r" + cm.getPlayer().getCSPoints(1) + "#k#d";
			gsjb +="  当前抵用卷:#r" + cm.getPlayer().getCSPoints(2) + "#k\r\n#d";
			gsjb +="#L4##b兑换抵用卷 #fUI/Basic/BtHide3/mouseOver/0# #b比例 - (#r1 = 3#b)#l\r\n\r\n\r\n";
            cm.sendSimple(gsjb);
        } else if (status == 1) {
            if (cm.getPlayer().getGMLevel() >= 1 && cm.getPlayer().getGMLevel() <= 5) {
                cm.sendOk("GM不能参与兑换。");
                cm.dispose();
            }
            if (selection == 0) {
                if (cm.getPlayer().getCSPoints(2) / 100 == 0) {
                    cm.sendNext("您的帐户抵用卷不足无法兑换。");
                    status = -1;
                } else {
                    beauty = 1;
                    cm.sendGetNumber("请输入#r抵用卷#k兑换#b#z4031250##k的数量:\r\n#b比例 - (#r100 = 1#b)\r\n你的账户信息 - \r\n    抵用卷数量: #r" +
                            cm.getPlayer().getCSPoints(2) + " \r\n", 1, 1, cm.getPlayer().getCSPoints(2) / 100);

                }

            
            } else if (selection == 1) {
                var iter = cm.getChar().getInventory(MapleInventoryType.ETC).listById(4031250).iterator();
                if (cm.haveItem(4031250) == 0) {
                    cm.sendNext("您的帐户#z4031250#数量不足兑换抵用卷。");
                    status = -1;
                } else {
                    beauty = 2;
                    cm.sendGetNumber("请输入#b#z4031250##k兑换#r抵用卷#k的数量:\r\n#b比例 - (#r1 = 100#b)\r\n你的账户信息 - \r\n    抵用卷数量: #r" +
                            cm.getPlayer().getCSPoints(2) + "    \r\n", 1, 1, iter.next().getQuantity());

                }
            } else if (selection == 3) {
                var iter = cm.getChar().getInventory(MapleInventoryType.ETC).listById(4031250).iterator();
                if (cm.haveItem(4031250) == 0) {
                    cm.sendNext("您的帐户#v4031250#数量不足兑换点卷。");
                    status = -1;
                } else {
                    beauty = 4;
                    cm.sendGetNumber("请输入#b#z4031250##k兑换#r点卷#k的数量:\r\n#b比例 - (#r2 = 1#b)\r\n你的账户信息 - \r\n点卷数量: #r" +
                            cm.getPlayer().getCSPoints(1) + "   \r\n", 1, 1, iter.next().getQuantity());

                }

			  } else if (selection == 4) {
                var iter = cm.getChar().getInventory(MapleInventoryType.ETC).listById(4031250).iterator();
                if (cm.haveItem(4031250) == 0) {
                    cm.sendNext("您的帐户#v4031250#数量不足兑换抵用卷。");
                    status = -1;
                } else {
                    beauty = 3;
                    cm.sendGetNumber("请输入#b#z4031250##k兑换#r抵用卷#k的数量:\r\n#b比例 - (#r1 = 3#b)\r\n你的账户信息 - \r\n抵用卷数量: #r" +
                            cm.getPlayer().getCSPoints(2) + "   \r\n", 1, 1, iter.next().getQuantity());

                }

            }


        } else if (status == 2) {
            if (beauty == 1) {
                if (selection <= 0) {
                    cm.sendOk("输入的兑换数字错误。");
                    cm.dispose();
                /*
                } else if (selection >= 200) {
                    sl = (selection / 200) + 1;
                } else {
                    sl = 3;
                }

                //if(cm.getPlayer().getInventory(net.sf.cherry.client.MapleInventoryType.getByType(1)).isFull()){
                if (cm.getSpace(4) < sl) {
                    cm.sendOk("你的背包“其它”空间不足!请至少有" + sl + "个空间以上.\r\n如果上面有出现小数的话请入位!\r\n如：出现<至少有7.5个空间以上>那么您就需要留8个空间!");
                    cm.dispose();
*/
                } else if (cm.getPlayer().getCSPoints(2) >= selection * 300) {
                    cm.gainD(-selection * 300);
                    cm.gainItem(4031250, selection);
                    cm.sendOk("您成功将 #r " + (selection * 300) + " #k抵用卷 兑换成#v4031250# x #r" + selection + " #k")
                } else {
                    cm.sendNext("兑换" + selection + "个#z4031250##v4031250# 需要#r " + (selection * 300) + "#k抵用卷。您没有足够的抵用卷。");
                    cm.dispose();
                }
            } else if (beauty == 2) {
                if (cm.haveItem(4031250, selection)) {
                    cm.gainItem(4031250, -selection);
                    cm.gainD(+300 * selection);
                    cm.sendOk("您成功将#z4031250##v4031250# x #r" + selection + " #k换为#r " + (300 * selection) + " #k抵用卷。");
                } else {
                    cm.sendNext("您的输入的数量错误，无法兑换抵用卷。");
                    cm.dispose();
                }

            } else if (beauty == 3) {
                if (cm.haveItem(4031250, selection)) {
                    cm.gainItem(4031250, -selection);
                    cm.gainD(+Math.floor(3 * selection));
                    cm.sendOk("您成功将#z4031250##v4031250# x #r" + selection + " #k换为#r " + Math.floor(3 * selection) + " #k抵用券。");
                } else {
                    cm.sendNext("您的输入的数量错误，无法兑换点卷。");
                    cm.dispose();
                }
            
} else if (beauty == 4) {
                if (cm.haveItem(4031250, 2 * selection)) {
                    cm.gainItem(4031250, - (2 * selection));
                    cm.gainDJ(+Math.floor(1 * selection));
                    cm.sendOk("您成功将4444#z4031250##v4031250# x #r" + (2 * selection) + " #k换为#r " + Math.floor(3 * selection) + " #k点卷。");
                } else {
                    cm.sendNext("您的输入的数量错误，无法兑换抵用卷。");
                    cm.dispose();
                }
            }
            status = -1;
        } else {
            cm.dispose();
        }
    }
}
