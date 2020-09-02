importPackage(java.lang);
importPackage(Packages.client);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.net.channel);
importPackage(Packages.tools);
importPackage(Packages.scripting);
importPackage(Packages.tools.packet);
importPackage(Packages.tools.data);
importPackage(Packages.tools);
var status = 0;
var itemList =   
Array(   
			Array(4000463,10,1,1),//国庆币

        	Array(3010898,100,1,1),//迷你神兽椅子
        	Array(3010465,100,1,1),//可爱音符椅子
        	Array(3010460,100,1,1),//爆竹椅子
        	Array(3010450,100,1,1),//艾莉珍椅子
        	Array(3010455,100,1,1),//扎昆的帝王椅子
        	Array(3010456,100,1,1),//杜鹃花椅子
        	Array(3010458,100,1,1),//翻滚兔子椅子
        	Array(3010863,50,1,1),//愤怒的美发师椅子
        	Array(3010447,100,1,1),//睡觉小鸟椅子
        	Array(3010446,100,1,1),//皮肤皇后椅子
        	Array(3010448,100,1,1),//泡泡浴椅子
        	Array(3010434,100,1,1),//热带夏日椅子
        	Array(3010428,100,1,1),//水晶椅子
        	Array(3010409,100,1,1),//粽子椅子
      	    Array(3010094,100,1,1),//漂漂猪椅子-----------------------
        	Array(3010411,100,1,1),//鱼尾狮椅子
        	Array(3010085,50,1,1),//鬼娃娃椅子
        	Array(3010077,100,1,1),//猫头鹰椅子
        	Array(3010053,50,1,1),//兔子纪念版椅子
        	Array(3010151,200,1,1),//无人岛椅子
        	Array(3015415,50,1,1),//猴子椅子
        	Array(3010142,50,1,1),//水族馆椅子
        	Array(3010144,100,1,1),//七夕椅子
        	Array(3015430,100,1,1),//年兽椅子
        	Array(3010117,100,1,1),//魔法书椅子
        	Array(3015424,100,1,1),//超级饺子椅子
        	Array(3015428,100,1,1),//和猴子一起放烟花椅子
        	Array(3015427,100,1,1),//和猴子一起玩麻将椅子
        	Array(3010804,100,1,1),//军人月妙拜年椅子
        	Array(3015045,50,1,1),//羊绒咩咩椅子--------------
        	Array(3010375,50,1,1),//奥妙的超级药水椅子
        	Array(3015057,50,1,1),//烟花绽放椅子
        	Array(3010373,50,1,1),//云朵洗手间椅子
        	Array(3010799,50,1,1),//坟墓幽灵椅子
        	Array(3010737,50,1,1),//动物英雄团椅子
        	Array(3010734,100,1,1),//猫箱椅子
        	Array(3010738,50,1,1),//闪电三人组椅子
        	Array(3010740,100,1,1),//圣诞树椅子
        	Array(3010365,100,1,1),//休彼德蔓的古代石掌椅子
        	Array(3010306,100,1,1),//生如夏花椅子
        	Array(3010301,50,1,1),//高级HP药水椅子
        	Array(3010188,100,1,1),//班·雷昂椅子
        	Array(3010186,100,1,1),//兔子椅子
        	Array(3010184,50,1,1),//冰钓椅子
         	Array(3010173,50,1,1),//万圣节塔罗椅子
        	Array(3010175,50,1,1),//名画家椅子
        	Array(3010195,100,1,1),//无价之宝椅子
        	Array(3010210,100,1,1),//草莓冰淇淋月饼椅子--------------
        	Array(3010224,300,1,1),//年糕冰淇淋椅子
        	Array(3010036,50,1,1),//浪漫秋千
        	Array(3010044,50,1,1),//同一红伞下
        	Array(3010111,100,1,1),//虎虎生威
        	Array(3010115,100,1,1),//熊宝宝床
        	Array(3010126,50,1,1),//蝙蝠魔王座
        	Array(3010313,50,1,1),//粉红扎昆椅子
        	Array(3010128,50,1,1),//黑龙王座
        	Array(3010133,50,1,1),//帐篷椅
        	Array(3010152,200,1,1),//黄三角帐篷椅
        	Array(3010291,50,1,1),//早日康复床
        	Array(3010172,50,1,1),//星空椅子
        	Array(3010168,50,1,1),//友谊万岁椅子
        	Array(3010185,100,1,1),//小伙伴品克缤
        	Array(3010622,100,1,1),//我的好友丹珍之椅
        	Array(3010453,50,1,1),//兔子乘风椅
        	Array(3010493,50,1,1),//那就是我--------------------------------
        	Array(3010527,50,1,1),//深海章鱼
        	Array(3010531,50,1,1),//小企鹅合唱团
        	Array(3010565,50,1,1),//我的女皇椅子
        	Array(3010587,50,1,1),//时间胶囊
        	Array(3010609,50,1,1),//不是在做梦
        	Array(3010608,50,1,1),//完美的名画椅子
        	Array(3010661,50,1,1),//欢乐相框椅子
        	Array(3010678,50,1,1),//海加顿之安息
        	Array(3010739,50,1,1),//雪人香波椅子
        	Array(3010744,50,1,1),//冒险岛的砖头朋友们
        	Array(3010752,50,1,1),//玫瑰鸟笼
        	Array(3010756,50,1,1),//新年招财猫椅子
        	Array(3010767,50,1,1),//雪人椅子
        	Array(3010760,50,1,1),//古代浴池椅子
        	Array(3010795,50,1,1),//森林中休息处
        	Array(3010806,50,1,1),//桃樱芳菲椅
        	Array(3010848,50,1,1),//红地毯主人公椅子
        	Array(3012024,50,1,1)//沙滩排球椅子
);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
           selStr = "#e#r该玩具箱可以获得以下物品，99%获得\r\n\#b是否消耗 200点豆豆来一发？#n#k\r\n";
		   for (i = 0; i < itemList.length; i++) {
                selStr += "#d#v" + itemList[i][0] + "#";
            }
			
			cm.sendOk(selStr);
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
		selStr = "\r\n";
		   for (i = 0; i < itemList.length; i++) {
                selStr += "#d#v" + itemList[i][0] + "#";
            }
        if (cm.getPlayer().getBeans() >= 200) {//判断豆豆
            cm.sendYesNo("只要你有200点豆豆，就可以随机可以获得以下物品，99%获得\r\n\#b是否消耗 200点豆豆来一发？?"+selStr);
        } else {
            cm.sendOk("只要你有200点豆豆，就可以随机获得以下物品，99%获得\r\n\#b是否消耗 200点豆豆来一发?"+selStr);
            cm.safeDispose();
        }
    } else if (status == 1) {
		var ii = MapleItemInformationProvider.getInstance();
        var chance = Math.floor(Math.random()*500);
        var finalitem = Array();
        for (var i = 0; i < itemList.length; i++) {
            if (itemList[i][1] >= chance) {
                finalitem.push(itemList[i]);
            }
        }
        if (finalitem.length != 0) {
            
            var random = new java.util.Random();
            var finalchance = random.nextInt(finalitem.length);
            var itemId = finalitem[finalchance][0];
            var quantity = finalitem[finalchance][2];
           //var notice = finalitem[finalchance][3];
           // item = cm.gainGachaponItem(itemId, quantity,);//, notice
			var Laba = finalitem[finalchance][3];
			       
				   
				   if(ii.getInventoryType(itemId).getType() == 1){
			        	var toDrop = ii.randomizeStats(ii.getEquipById(itemId)).copy();
						MapleInventoryManipulator.addFromDrop(cm.getC(),toDrop,false);
				}else{
				 	 var toDrop = new Equip(itemId,0).copy();
				cm.gainItem(itemId,quantity);
				}
				
            if (Laba == 1) {
			
			 cm.itemlaba("打算发送颠覆",toDrop)
                //cm.gainBeans(-200);//扣除豆豆
                cm.sendOk("你获得了 #b#t" + itemId + "##k " + quantity + "个。");
            } else {//不喇叭
                cm.sendOk("你获得了 #b#t" + itemId + "##k " + quantity + "个。");
            }
            cm.safeDispose();
        } else {
            cm.sendOk("今天的运气可真差，什么都没有拿到。");
            cm.gainBeans(-200);//扣除豆豆
            cm.safeDispose();
        }
    }
}