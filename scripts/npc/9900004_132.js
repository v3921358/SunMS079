/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
 
var 红色箭头 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2# ";
var status = 0;
var zones = 0;
var ItemId = Array(
	Array(1302081,4031456,200, "永恒破甲剑"),
    Array(1402046,4031456,200, "1"),//永恒玄冥剑
	Array(1432047,4031456,200, "2"),//永恒显圣枪
	Array(1442063,4031456,200, "3"),//永恒神光戟
	Array(1412033,4031456,200, "4"),//永恒碎鼋斧
    Array(1332073,4031456,200, "4"),//永恒狂鲨锯
    Array(1452057,4031456,200, "5"),//永恒惊电弓
	Array(1462050,4031456,200, "6"),//永恒永恒冥雷弩
	Array(1472068,4031456,200, "7"),//永恒大悲赋
    Array(1332073,4031456,200, "8"),//永恒断首刃
    Array(1372044,4031456,200, "9"),//永恒蝶翼杖
    Array(1382057,4031456,200, "10"),//永恒冰轮杖
	Array(1482023,4031456,200, "11"),//永恒孔雀翎
	Array(1492023,4031456,200, "12"),//永恒凤凰铳
    Array(1002776,4031456,200, "13"),//永恒冠军盔
    Array(1002777,4031456,200, "14"),//永恒玄妙帽
    Array(1002778,4031456,200, "15"),//永恒霓翎帽
	Array(1002779,4031456,200, "16"),//永恒迷踪帽
	Array(1002780,4031456,200, "17"),//永恒海王星
    Array(1082238,4031456,200, "18"),//永恒抚浪手套
    Array(1082237,4031456,200, "19"),//永恒探云手套
    Array(1082236,4031456,200, "20"),//永恒白云手套
	Array(1082235,4031456,200, "21"),//永恒逍遥手套
	Array(1082234,4031456,200, "22"),//永恒定边手套
    Array(1072359,4031456,200, "23"),//永恒定海靴
    Array(1072358,4031456,200, "24"),//永恒舞空靴
    Array(1072357,4031456,200, "25"),//永恒彩虹鞋
	Array(1072356,4031456,200, "26"),//永恒缥缈鞋
	Array(1072355,4031456,200, "27"),//永恒坚壁靴
    Array(1052155,4031456,200, "28"),//永恒演武铠
    Array(1052156,4031456,200, "29"),//永恒奥神袍
    Array(1052157,4031456,200, "30"),//永恒巡礼者
	Array(1052158,4031456,200, "31"),//永恒翻云服
	Array(1052159,4031456,200, "32")//永恒霸七海

        );

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
    if (status == 0) {
           var sl = cm.getPlayer().getItemQuantity(4031456, false);
            var StringS = "#b#n   角色剩余:" + sl + " 颗#v4031456#";
            for (var i = 0; i < ItemId.length; i++) {
                StringS += "\r\n#L" + i + "##b" + 红色箭头 + "#b#v " + ItemId[i][1] + " #" + ItemId[i][2] + "#n颗#k 兑换 #r#v" + ItemId[i][0] + "##z" + ItemId[i][0] + "##k#l";
            }
            cm.sendSimple(StringS, 2);
            zones == 0;

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
    }
}}