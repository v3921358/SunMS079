/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
 
var jj = cm.getPlayer.getItemQuantity(1002448,1002448, false);

var status = -1;
var itemList = Array(
	Array(1002448,5000,200, "1"),
	Array(1002543,5000,200, "2"),
	Array(1002583,5000,200, "3"),
	Array(1002589,5000,200, "4"),
	Array(1002609,5000,200, "5"),
	Array(1002665,5000,200, "6"),
	Array(1002695,5000,200, "7"),
	Array(1002760,5000,200, "8"),
	Array(1002761,5000,200, "9"),
	Array(1002789,5000,200, "10")

        );
var selectedItem = -1;
var selequantity = -1;
var selectedCos = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status >= 0) {
            cm.dispose();
            return;
        }
        status--;
    }
    if (status == 0) {
        var selStr = "您好，请选择您需要兑换的物品                          ★#r豆豆点：#r" + "#k点\r\n";
        for (var i = 0; i < itemList.length; i++) {
            selStr += "\r\n#L" + i + "##i" + itemList[i][0] + ":# #b#t" + itemList[i][0] + "# #k需要 #r" + itemList[i][1] + "#k豆豆#l";
        }
        cm.sendSimple(selStr);
    } else if (status == 1) {
        var item = itemList[selection];
        if (item != null) {
            selectedItem = item[0];
            selequantity = item[1];
            cm.sendYesNo("兑换 #i" + selectedItem + "# 需要 #r" + selequantity + "#k豆豆。你确定兑换吗?");//修炼点代码 记得换 40000000是蓝蜗牛壳
        } else {
            cm.sendOk("兑换出错,请联系管理员。");
            cm.dispose();
        }
    } else if (status == 2) {
        if (selequantity <= 0 || selectedItem <= 0) {
            cm.sendOk("兑换出错,请联系管理员。");
            cm.dispose();
            return;
        }
        if (cm.canHold(2300001,selequantity)) {//修炼点代码 记得换
            if (cm.canHold(selectedItem)) {
				cm.gainItem(-selequantity);
                cm.gainItem(selectedItem, 1);
                cm.sendOk("兑换成功,商品#i" + selectedItem + ":# #b#t" + selectedItem + "##k已送往背包。");
                        cm.dispose();
						} else {
                cm.sendOk("背包所有栏目窗口有一格以上的空间才可以进行兑换。");
				cm.dispose();
            }

        } else {
            cm.sendOk("你没有足够的豆豆兑换。");
			            cm.dispose();
						            return;
        }
        status = -1;
    }
}