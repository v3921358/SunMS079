var status = 0;
importPackage(java.lang);
importPackage(Packages.tools);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		var Editing = false //true=显示;false=开始活动
          if(Editing){
          cm.sendOk("暂停运作");
          cm.dispose();
          return;
        } 
			cm.sendSimple("  赞助充值可以领取积分参加市场段段的积分抽奖!!\r\n  赞助充值1000点券可获得10点积分以此类推.\r\n#b  购买前请确认自己的背包是否有位子\r\n#k          #v4000009#您的当前积分为：#r"+ cm.getcz() +"#k\r\n#L3##v4001232# 10点积分购买赞助礼包 #l \r\n#L1##v5520000# 30点积分一个点击购买#l\r\n#L0# #v1142006# 100点积分一个点击购买  #l\r\n#L5##v4032226# 50点积分修改一次角色名字#l \r\n  #L6##v5590000#租借#z5590000# (#r需要2000点卷#k)#l");
        } else if (status == 1) {
            if (selection == 0) {
				if(cm.getPlayer().getOneTimeLog("libao10") >= 1){//判断永久记录
				cm.sendOk("赞助礼包只能兑换1次!");
				cm.dispose();
             } else { 
			 if (cm.getcz() >= 100 ) {
                    cm.gaincz(-100);                   
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142006); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142006)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(10);//给力量
		toDrop. setDex(10);//给敏捷 
		toDrop. setInt(10);//给智力
		toDrop. setLuk(10);//给运气
		toDrop. setHp(200);//HP
        toDrop. setMp(200);//MP
		toDrop. setWatk(5);//攻击力    
		toDrop. setMatk(5);//魔法力
		//toDrop. setAvoid(0);//回避力
		toDrop. setHands(10);//手技
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                    cm.sendOk("成功兑换了礼包！感谢赞助！");
					cm.getPlayer().setOneTimeLog("libao10");//给永久记录	
cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 使用积分兑换 一线海冒险岛偶像明星勋章 ，大家一起祝贺他(她)吧!");
                    cm.dispose();
                } else {
                    cm.sendOk("积分不足!");
                    cm.dispose();
                }
				}
            } else if (selection == 1) {
				if (cm.getcz() >= 30 ) {
                    cm.gaincz(-30);
                    cm.gainItem(5520000, 1);
                    cm.gainItem(2022245, 2);					
                    cm.sendOk("成功兑换了礼包！感谢赞助！");
					
cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 使用积分兑换 宿命剪刀，大家一起祝贺他(她)吧!");
                    cm.dispose();
                } else {
                    cm.sendOk("积分不足!");
                    cm.dispose();
                }
            } else if (selection == 5) {
				if (cm.getcz() >= 50 ) {
                    cm.gaincz(-50);
                    cm.gainItem(4032226, 1);
                    cm.gainItem(2022245, 2);					
                    cm.sendOk("你获得了一次修改角色名字的机会,请把#v4032226#交给管理员修改你的角色名字吧!");
					
cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 使用积分兑换黄金猪猪，获得一次修改角色名字的机会!");
                    cm.dispose();
                } else {
                    cm.sendOk("积分不足!");
                    cm.dispose();
                }
            } else if (selection == 6) {
			if(cm.getPlayer().getCSPoints(1) >= 2000){
                    cm.gainDJ(-2000);
                    cm.gainItem(5590000, 1, 5);					
                    cm.sendOk("租借成功,时效5天!");				
                    cm.dispose();
                } else {
                    cm.sendOk("点卷不足!");
                    cm.dispose();
                }				
            } else if (selection == 3) {
                if(cm.getPlayer().getOneTimeLog("libao20") >= 1){//判断永久记录
				cm.sendOk("赞助礼包只能兑换1次!");
				cm.dispose();
            } else {
				if (cm.getcz() >= 10 ) {
                    cm.gaincz(-10);
                    cm.gainItem(5150040, 2);
                    cm.gainItem(5074000, 2);
                    cm.gainItem(3012001, 1);
                    cm.gainItem(1432009, 1, 1);					
		            cm.gainMeso(100000);//给金币	
cm.getPlayer().setOneTimeLog("libao20");//给永久记录					
                    cm.sendOk("成功兑换了礼包！感谢赞助！");
cm.worldMessage(6,"恭喜[ " + cm.getPlayer().getName() + "] 成功兑换赞助礼包，大家一起祝贺他(她)吧!");
                    cm.dispose();
                } else {
                    cm.sendOk("积分不足!");
                    cm.dispose();					
                }				
}
}
}
}}
