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
    //物品1     物品2    数量    备注
    Array(2340000,4000463, 1, "祝福卷轴"),
	Array(2049100,4000463, 1, "混沌卷轴")
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
        sl = cm.getPlayer().getItemQuantity(4000463, false);
        StringS = "#b#n   角色剩余:" + sl + " 颗#v4000463#";
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
            cm.sendOk("#v4000463#不足！");
            cm.dispose();
        }
        }
    }
    }
}	