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
        Array(2044003, 4000463, 1, "双手剑"),
        Array(2044303, 4000463, 1, "枪"),
        Array(2043003, 4000463, 1, "单手剑"),
        Array(2044403, 4000463, 1, "矛"),
        Array(2044203, 4000463, 1, "双手钝器"),
        Array(2044103, 4000463, 1, "双手斧"),
        Array(2043103, 4000463, 1, "单手斧"),
        Array(2040807, 4000463, 1, "手套"),
        Array(2040506, 4000463, 1, "全身敏捷"),
        Array(2040710, 4000463, 1, "鞋子"),
        Array(2040303, 4000463, 1, "耳环智力"),
        Array(2044503, 4000463, 1, "弓"),
        Array(2044603, 4000463, 1, "弩"),
        Array(2043303, 4000463, 1, "短剑"),
        Array(2044703, 4000463, 1, "拳套"),
        Array(2044908, 4000463, 1, "短枪"),
        Array(2044815, 4000463, 1, "指节"),
        Array(2043803, 4000463, 1, "长杖"),
        Array(2043703, 4000463, 1, "短杖")
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