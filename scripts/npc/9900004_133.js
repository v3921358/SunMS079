/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
var 红色箭头 = "#fUI/UIWindow.img/PvP/Scroll/enabled/next2# ";

importPackage(net.sf.Start.client);
var status = 0;
var zones = 0;
var ItemId = Array(
    //物品1     物品2    货币    数量
	Array(1402213,4031456,200, "0"),//勇猛之翼双手剑
    Array(1302292,4031456,200, "1"),//勇猛之翼单手剑
	Array(1432181,4031456,200, "2"),//勇猛之翼枪
	Array(1442237,4031456,200, "3"),//勇猛之翼矛
	Array(1312168,4031456,200, "4"),//勇猛之翼单手斧
    Array(1412150,4031456,200, "5"),//勇猛之翼双手斧
	Array(1452219,4031456,200, "7"),//迅速之翼弓
	Array(1462207,4031456,200, "8"),//迅速之翼弩
	Array(1332241,4031456,200, "9"),//勇猛之翼短刀
	Array(1472229,4031456,200, "10"),//勇猛之翼拳套
	Array(1482182,4031456,200, "11"),//勇猛之翼指节
	Array(1492193,4031456,200, "12"),//勇猛之翼短枪
	Array(1332241,4031456,200, "13"),//勇猛之翼短刀
	Array(1472229,4031456,200, "14"),//勇猛之翼拳套
	Array(1482182,4031456,200, "15"),//勇猛之翼指节
	Array(1492193,4031456,200, "16"),//勇猛之翼短枪
	Array(1372191,4031456,200, "17"),//智慧之翼
	Array(1382225,4031456,200, "18"),//智慧之翼
	Array(1004224,4031456,300, "19"),//穆斯佩尔战士帽
	Array(1004225,4031456,300, "20"),//穆斯佩尔魔法师帽
	Array(1004226,4031456,300, "21"),//穆斯佩尔弓箭手帽
	Array(1004227,4031456,300, "22"),//穆斯佩尔飞侠帽
	Array(1004228,4031456,300, "23"),//穆斯佩尔海盗帽
	Array(1082603,4031456,300, "24"),//穆斯佩尔战士手套
	Array(1082604,4031456,300, "25"),//穆斯佩尔魔法师手套
	Array(1082605,4031456,300, "26"),//穆斯佩尔弓箭手手套
	Array(1082606,4031456,300, "27"),//穆斯佩尔飞侠手套
	Array(1082607,4031456,300, "28"),//穆斯佩尔海盗手套
	Array(1072962,4031456,300, "29"),//穆斯佩尔战士鞋
	Array(1072963,4031456,300, "30"),//穆斯佩尔魔法师鞋
	Array(1072964,4031456,300, "31"),//穆斯佩尔弓箭手鞋
	Array(1072965,4031456,300, "32"),//穆斯佩尔飞侠鞋
	Array(1072966,4031456,300, "33"),//穆斯佩尔海盗鞋
	Array(1052794,4031456,300, "34"),//穆斯佩尔战士套服
	Array(1052795,4031456,300, "35"),//穆斯佩尔魔法师套服
	Array(1052796,4031456,300, "36"),//穆斯佩尔弓箭手套服
	Array(1052797,4031456,300, "37"),//穆斯佩尔飞侠套服
	Array(1052798,4031456,300, "38")//穆斯佩尔海盗套服
    //如需其它道具兑换，请按照此格式自行添置。
    //代码,价格,介绍
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
        sl = cm.getPlayer().getItemQuantity(4031456, false);
        StringS = "#b#n   角色剩余:" + sl + " 颗#v4031456#";
        for (var i = 0; i < ItemId.length; i++) {
        StringS += "\r\n#L" + i + "##b" + 红色箭头 + "#b#v " + ItemId[i][1] + " #" + ItemId[i][2] + "#n颗#k 兑换 #r#v" + ItemId[i][0] + "##z" + ItemId[i][0] + "##k#l";
        }
        cm.sendSimple(StringS, 2);
        zones == 0;

    } else if (status == 1) {
        if (zones == 0) {
            if (cm.getInventory(1).isFull()){
            cm.sendOk("#b请保证装备栏位至少有2个空格,否则无法购买.");
            cm.dispose();
            } else if (cm.getInventory(2).isFull()){
            cm.sendOk("#b请保证消耗栏位至少有2个空格,否则无法购买.");
            cm.dispose();
            } else if (cm.getInventory(3).isFull()){
            cm.sendOk("#b请保证设置栏位至少有2个空格,否则无法购买.");
            cm.dispose();
            } else if (cm.getInventory(4).isFull()){
            cm.sendOk("#b请保证其他栏位至少有2个空格,否则无法购买.");
            cm.dispose();
        } else if (cm.haveItem(ItemId[selection][1], ItemId[selection][2])) {
            cm.gainItem(ItemId[selection][1], -ItemId[selection][2]);
            cm.gainItem(ItemId[selection][0], 1);
            cm.sendOk("兑换成功，请检背包!"); 
            cm.dispose();
        } else {
            cm.sendOk("#v4031456#不足！");
            cm.dispose();
        }
        }
    }
    }
}	