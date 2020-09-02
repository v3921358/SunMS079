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
	    Array(1003209,4000463,500, "0"),//圣诞鹿变身帽
        Array(1102691,4000463,500, "1"),//11周年神圣斗篷
        Array(1042244,4000463,500, "2"),//2014运动会优胜组上衣
        Array(1062205,4000463,500, "3"),//2014运动会优胜组短裤
        Array(1072872,4000463,500, "4"),//黏糊鞋子
        Array(1072672,4000463,500, "5"),//风暴鞋子
        Array(1113039,4000463,500, "6"),//只可带一个
        Array(1113117,4000463,500, "7"),//迅捷之戒
        Array(1032221,4000463,500, "8"),//中级贝勒德耳环
        Array(1132244,4000463,500, "9"),//中级贝勒德刻印腰带
        Array(1012319,4000463,500, "11"),//8周年点点红
        Array(1022232,4000463,500, "12"),//布莱克缤瞳印
        Array(1022182,4000463,500, "13"),//混沌品克缤瞳印
        Array(1142499,4000463,500, "14"),//超级巨星★
        Array(1142488,4000463,500, "15"),//到达极意的人
        Array(1142574,4000463,500, "17"),//官方认证女生
        Array(1142705,4000463,500, "18"),//时空门大师
        Array(1142704,4000463,500, "19"),//键盘战神
        Array(1142676,4000463,500, "20"),//酷劲爆棚
        Array(1142677,4000463,500, "21"),//犀利爆棚
        Array(1142678,4000463,500, "22"),//犀利爆棚
        Array(1142679,4000463,500, "23"),//可爱爆棚
        Array(1142658,4000463,500, "24"),//玫瑰骑士
        Array(1142656,4000463,500, "25"),//椅子王勋章
        Array(1142650,4000463,500, "26"),//我已今非昔比
        Array(1142611,4000463,500, "27"),//难道是流氓?!
        Array(1142594,4000463,500, "28"),//永久王者归来勋章
        Array(1142535,4000463,500, "29"),//枫叶城堡VIP
        Array(1142513,4000463,500, "30"),//鱼尾狮守卫
        Array(1142695,4000463,500, "31"),//桃乐丝挑战者
        Array(1142536,4000463,500, "32"),//世界树守护者
        Array(1142716,4000463,500, "34"),//圣瑞尼亚勋章
        Array(1142521,4000463,500, "35"),//古韵尤茨
        Array(1142559,4000463,500, "36"),//荣誉夏季
        Array(1142590,4000463,500, "39"),//2014春节
        Array(1142713,4000463,500, "40"),//永恒守夜
        Array(1142613,4000463,500, "41"),//10周年广告模特
        Array(1142616,4000463,500, "42"),//10周年画家
        Array(1142409,4000463,500, "43")//梦幻宝贝
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