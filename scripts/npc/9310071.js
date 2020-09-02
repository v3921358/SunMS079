/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
 

var status = -1;
var itemList = Array(
    
	Array(4110010,1000,200, "16"),
	Array(1002543,5000,200, "1"),
	Array(1002583,5000,200, "2"),
	Array(1002589,5000,200, "3"),
	Array(1002609,5000,200, "4"),
	Array(1002665,5000,200, "5"),
	Array(1002695,5000,200, "6"),
	Array(1002760,5000,200, "7"),
	Array(1002761,5000,200, "8"),
	Array(1002789,5000,200, "9"),
	Array(1092108,5000,200, "10"),	
	Array(3015426,8000,200, "11"),	
	Array(3010738,8000,200, "12"),
	Array(3010499,8000,200, "13"),	
	Array(3010739,10000,200, "14"),		
    Array(3010737,10000,200, "15")

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
        var selStr = "           #b#v4110010#您好,这里是豆豆商城#k#v4110010#\r\n           #v4110010#当前豆豆点：#r" + cm.getPlayer().getBeans() + "#k 点#v4110010#";
        for (var i = 0; i < itemList.length; i++) {
            selStr += "\r\n#L" + i + "##i" + itemList[i][0] + ":# #b#t" + itemList[i][0] + "# #k需要 #r" + itemList[i][1] + "#k 豆豆#l";
        }
        cm.sendSimple(selStr);
    } else if (status == 1) {
        var item = itemList[selection];
        if (item != null) {
            selectedItem = item[0];
            selequantity = item[1];
            cm.sendYesNo("兑换 #i" + selectedItem + "# 需要 #r" + selequantity + "#k 豆豆.你确定兑换吗?");//修炼点代码 记得换 40000000是蓝蜗牛壳
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
        if (cm.getPlayer().getBeans() >=selequantity) {//修炼点代码 记得换
            if (cm.canHold(selectedItem)) {
				cm.getPlayer().gainBeans(-selequantity);
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