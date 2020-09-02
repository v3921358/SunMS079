
/*  
 
 NPC版权:                千雪冒险岛 	        
 NPC类型: 		        综合NPC
 制作人：故事丶
 */

var status = -1;
var itemList = Array(
	Array(3010036,600, "1"),//浪漫秋千
    Array(3010043,600, "1"),//魔女的飞扫把
	Array(3010044,600, "2"),//同一红伞下
	Array(3010049,600, "3"),//魔女的飞扫把
	Array(1052668,600, "4"),//雪房子
	Array(3010071,600, "5"),//神兽椅
	Array(3010077,600, "6"),//猫头鹰椅子
	Array(3010085,600, "7"),//鬼娃娃椅子
	Array(3010094,600, "8"),//漂漂猪椅子
	Array(3010098,600, "9"),//电视宅人
	Array(3010100,600, "10"),//财神椅子
	Array(3010099,600, "11"),//北极熊椅子
	Array(3010110,600, "12"),//舒适大白熊椅子
	Array(3010111,600, "13"),//虎虎生威
	Array(3010113,600, "14"),//幽魂发条熊椅子
	Array(3010114,600, "15"),//俘虏我吧！椅子
	Array(3010115,600, "16"),//熊宝宝床
	Array(3010116,600, "17"),//摇滚之魂椅子
	Array(3010124,600, "18"),//都纳斯喷气椅子
	Array(3010126,600, "19"),//蝙蝠魔王座
	Array(3010313,800, "20"),//粉红扎昆椅子
	Array(3010128,800, "21"),//黑龙王座
	Array(3010131,800, "22"),//贪吃熊猫椅
	Array(3010132,800, "23"),//撒娇喵咪椅
	Array(3010133,800, "24"),//帐篷椅
	Array(3010152,800, "25"),//黄三角帐篷椅
	Array(3010025,800, "26"),//5周年枫叶纪念凳
	Array(3010146,800, "27"),//周年庆水晶枫叶椅子
	Array(3010291,800, "28"),//早日康复床
	Array(3010139,800, "29"),//私密空间
	Array(3010171,800, "30"),//过来抱抱椅
	Array(3010172,800, "31"),//星空椅子
	Array(3010168,800, "32"),//友谊万岁椅子
	Array(3010180,800, "33"),//HP椅子
	Array(3010181,800, "34"),//MP椅子
	Array(3010185,800, "35"),//小伙伴品克缤
	Array(3010057,800, "36"),//血色玫瑰
	Array(3010622,800, "38"),//我的好友丹珍之椅
	Array(3010545,800, "41"),//千年狐椅子
	Array(3010453,800, "42"),//兔子乘风椅
	Array(3010454,800, "43"),//爱心云朵椅
	Array(3010493,800, "44"),//那就是我
	Array(3010494,800, "45"),//TV椅子
	Array(3010527,800, "46"),//深海章鱼
	Array(3010531,800, "47"),//小企鹅合唱团
	Array(3010565,800, "48"),//我的女皇椅子
	Array(3010589,800, "49"),//需要充电！
	Array(3010587,800, "50"),//时间胶囊
	Array(3010593,800, "51"),//新年快
	Array(3010609,800, "52"),//不是在做梦
	Array(3010608,800, "53"),//完美的名画椅子
	Array(3010664,800, "54"),//进化椅子
	Array(3010661,800, "55"),//欢乐相框椅子
	Array(3010660,800, "56"),//梦幻公主城堡椅子
	Array(3010658,800, "57"),//夏日西瓜冰椅子
	Array(3010678,800, "58"),//海加顿之安息
	Array(3010690,800, "59"),//滑浪飞船椅子
	Array(3010739,800, "60"),//雪人香波椅子
	Array(3010744,800, "61"),//冒险岛的砖头朋友们
	Array(3010752,800, "62"),//玫瑰鸟笼
	Array(3010754,800, "63"),//百鬼夜行
	Array(3010756,800, "64"),//新年招财猫椅子
	Array(3010767,800, "65"),//雪人椅子
	Array(3010760,800, "66"),//古代浴池椅子
	Array(3010795,800, "67"),//森林中休息处
	Array(3010806,800, "68"),//桃樱芳菲椅
	Array(3010848,2500, "70"),//红地毯主人公椅子
	Array(3010849,2500, "71"),//最佳新人椅子
	Array(3010947,2500, "72"),//与飞龙在天派一起
	Array(3010850,3000, "73"),//潮流评论家椅子
	Array(3012024,3000, "74"),//沙滩排球椅子
	Array(3015058,3000, "77"),//五彩缤纷花车巡游椅
	Array(3015060,3000, "78")//五彩糖罐椅子

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