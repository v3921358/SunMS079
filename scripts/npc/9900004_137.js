
/*  
 
 NPC版权:                千雪冒险岛 	        
 NPC类型: 		        综合NPC
 制作人：故事丶
 */

var status = -1;
var itemList = Array(
	Array(1402037,7000,200, "龙背刃"),
    Array(1302063,5000,200, "1"),//燃烧的火焰刀
	Array(1092022,3000,200, "2"),//调色板
	Array(1302067,2000,200, "3"),//枫叶庆典旗
    Array(1432188,2000,200, "4"),//枫叶庆典旗
	Array(1432039,1500,200, "5"),//钓鱼竿
	Array(1092051,1500,200, "6"),//啤酒杯盾牌
	Array(1099000,1500,200, "7"),//忍耐军团盾
	Array(1092035,1500,200, "8"),//可乐战盾
	Array(1402044,800,200, "9"),//南瓜灯笼
	Array(1402147,800,200, "10"),//我的好友虎鲸
	Array(1422011,800,200, "11"),//酒瓶
    Array(1402029,800,200, "12"),//鬼刺狼牙棒
	Array(1402013,800,200, "13"),//白日剑
	Array(1422128,800,200, "14"),//惊魂电锯
    Array(1322027,700,200, "15"),//米伽勒的平底锅
	Array(1382015,700,200, "16"),//毒蘑菇
	Array(1302221,600,200, "17"),//金刚小鸡
	Array(1302223,600,200, "18"),//萌兔兔
	Array(1302049,500,200, "19"),//光线鞭子
    Array(1332032,500,200, "20"),//圣诞树
	Array(1432046,500,200, "21"),//圣诞树手杖
    Array(1432013,500,200, "22"),//南瓜枪
	Array(1302230,500,200, "23"),//粉色花边游泳圈
	Array(1322158,500,200, "24"),//兔子年糕
	Array(1302219,500,200, "25"),//神秘之书
	Array(1302252,500,200, "26"),//冒险岛雨伞
	Array(1442028,500,200, "27"),//蓝色冲浪板
	Array(1442066,500,200, "28"),//红色冲浪板
    Array(1442121,500,200, "29"),//敏捷战士的玫瑰
	Array(1442122,500,200, "30"),//恐怖战士的玫瑰
	Array(1442123,500,200, "31"),//愤怒战士的玫瑰
	Array(1302218,500,200, "32"),//赌气的比恩宝宝
	Array(1422031,500,200, "33"),//蓝色海豹抱枕
	Array(1422030,500,200, "34"),//粉红海豹抱枕
	Array(1442046,400,200, "35"),//超级滑雪板
	Array(1332053,400,200, "36"),//野外烧烤串
	Array(1442021,400,200, "37"),//黄拖把
	Array(1432009,400,200, "38"),//木精灵枪
	Array(1432008,400,200, "39"),//海神叉
	Array(1302028,400,200, "40"),//紫雨伞
	Array(1322031,400,200, "41"),//葵花宝典
	Array(1312012,400,200, "42"),//乾坤圈
	Array(1312014,400,200, "43"),//阎王笔
	Array(1332030,400,200, "44"),//团扇
    Array(1302201,300,200, "45"),//福袋
	Array(1432015,300,200, "46"),//红色滑雪板
	Array(1432016,300,200, "47"),//橙色滑雪板
	Array(1432017,300,200, "48"),//绿色滑雪板
    Array(1432018,300,200, "49"),//蓝色滑雪板
	Array(1302128,200,200, "50"),//火柴
    Array(1302085,200,200, "51"),//叉子
	Array(1472088,200,200, "52"),//叉子护腕
	Array(1302087,200,200, "53")//火炬

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
        var selStr = "您好，请选择您需要兑换的物品                          ★#r点卷:#r"+cm.getPlayer().getCSPoints(1) + "#k点\r\n";
        for (var i = 0; i < itemList.length; i++) {
            selStr += "\r\n#L" + i + "##i" + itemList[i][0] + ":# #b#t" + itemList[i][0] + "# #k需要 #r" + itemList[i][1] + "#k点卷#l";
        }
        cm.sendSimple(selStr);
    } else if (status == 1) {
        var item = itemList[selection];
        if (item != null) {
            selectedItem = item[0];
            selequantity = item[1];
            cm.sendYesNo("兑换 #i" + selectedItem + "# 需要 #r" + selequantity + "#k点卷。你确定兑换吗?");//修炼点代码 记得换 40000000是蓝蜗牛壳
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
        if (cm.getPlayer().getCSPoints(1) >=selequantity) {//修炼点代码 记得换
            if (cm.canHold(selectedItem)) {
				cm.gainD(-selequantity, true);
                cm.gainItem(selectedItem, 1);
                cm.sendOk("兑换成功,商品#i" + selectedItem + ":# #b#t" + selectedItem + "##k已送往背包。");
                        cm.dispose();
						} else {
                cm.sendOk("背包所有栏目窗口有一格以上的空间才可以进行兑换。");
				cm.dispose();
            }

        } else {
            cm.sendOk("你没有足够的点卷兑换。");
			            cm.dispose();
						            return;
        }
        status = -1;
    }
}