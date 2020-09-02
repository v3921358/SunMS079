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
			cm.sendSimple("#b欢迎玩家 #r#h ##k ,收获不小嘛,那么就把你钓到的鱼在我这里兑换奖励吧!小鱼我可不要!要大的!\r\n#r特别提示:兑换前请确认背包格子,满包后果自负!" +
            "#k\r\n#b   #v1112907#四维+4.HP/MP+50.攻击/法术+3(有泡泡特效)     #r#L101#兑换#v1112907#需要：#k#v4031640#113cm#v4031644#148cm各30条.金币1000万\r\n\r\n\r\n\r\n#l------------------------------------------------------#v4031648#这里可以用你钓到的#r鲑鱼#k兑换绝版点装\r\n #L115#20条#v4031648#兑换#v1004197##b帽子#k#l#L116#30条#v4031648#兑换#v1702366##b武器#k #l\r\n ");
        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(4031640, 30) & cm.haveItem(4031644, 30) && cm.getMeso() > 10000000) {
                    cm.gainItem(4031640, -30);
                    cm.gainItem(4031644, -30);
                    cm.gainMeso(-5000000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1112907); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1112907)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(4);//给力量
		toDrop. setDex(4);//给敏捷 
		toDrop. setInt(4);//给智力
		toDrop. setLuk(4);//给运气
		toDrop. setHp(50);//HP
        toDrop. setMp(50);//MP
		toDrop. setWatk(3);//攻击力    
		toDrop. setMatk(3);//魔法力
		//toDrop. setAvoid(0);//回避力
		//toDrop. setHands(0);//手技
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                    cm.sendOk("获得#v1112907#小鱼戒指!.");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 钓鱼满载而归兑换了 小鱼戒指 !");//公告					
                    cm.dispose();					
                } else {
                    cm.sendOk("您身上没有足够的鱼或者金币.");
                    cm.dispose();
                }
///				
            } else if (selection == 102) {
                if (cm.haveItem(4001200, 20) & cm.haveItem(4001200, 5) && cm.getMeso() > 500000) {
                    cm.gainItem(4001200, -20);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142146); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142146)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(4);//给力量
		toDrop. setDex(4);//给敏捷 
		toDrop. setInt(4);//给智力
		toDrop. setLuk(4);//给运气
		toDrop. setHp(50);//HP
        toDrop. setMp(50);//MP
		//toDrop. setWatk(1);//攻击力    
		//toDrop. setMatk(1);//魔法力
		//toDrop. setAvoid(20);//回避力
	    //toDrop. setWdef(10);//防御
        //toDrop. setMdef(10);//魔法防御
	    toDrop. setJump(10);//跳跃
	    toDrop. setAcc(15);//命中
	    toDrop. setSpeed(20);//速度
		//toDrop. setHands(10);//手技
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
		            cm.gainMeso(-500000);
                    cm.sendOk("获得#v1142146#钓鱼王勋章.");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 钓鱼满载而归兑换了 钓鱼王勋章 ! ");//公告					
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有足够的鱼或者金币.");
                    cm.dispose();
				}
			 }else if (selection == 103) {
                if (cm.haveItem(1142146, 20) & cm.haveItem(4001200, 5) && cm.getMeso() > 15000000) {
                    cm.gainItem(1142146, -1);
                    cm.gainItem(4001200, -10);
                    cm.gainItem(2340000, 1);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142610); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142610)).copy(); // 生成一个Equip类
		////toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(12);//给力量
		toDrop. setDex(12);//给敏捷 
		toDrop. setInt(12);//给智力
		toDrop. setLuk(12);//给运气
		toDrop. setHp(1200);//HP
                toDrop. setMp(1200);//MP
		toDrop. setWatk(12);//攻击力    
		toDrop. setMatk(12);//魔法力
		//toDrop. setAvoid(20);//回避力
		//toDrop. setHands(10);//手技
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
		            cm.gainMeso(-500000);
                    cm.sendOk("获得#v1142610#天才钓鱼王勋章.");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有足够的道具.");
                    cm.dispose();
				}
			 }else if (selection == 104) {
                if (cm.haveItem(4031628, 1) & cm.haveItem(4031641, 1) && cm.haveItem(4031642, 1) & cm.haveItem(4031643, 1) && cm.haveItem(4031630, 1) & cm.haveItem(4031637, 1) && cm.haveItem(4031638, 1) & cm.haveItem(4031639, 1) && cm.getMeso() > 10000) {
                    cm.gainItem(4031628, -1);
                    cm.gainItem(4031641, -1);
                    cm.gainItem(4031642, -1);
                    cm.gainItem(4031643, -1);
                    cm.gainItem(4031630, -1);
                    cm.gainItem(4031637, -1);
                    cm.gainItem(4031638, -1);
                    cm.gainItem(4031639, -1);
                    cm.gainItem(4001200, 1);
		    cm.gainMeso(-10000);
                    cm.sendOk("获得#v4001200#小鱼x1.");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有足够的鱼或者金币.");
                    cm.dispose();
				}
				
///			               if (cm.haveItem(4001126, selection)) {
                  
					
			 }else if (selection == 105) {
                if (cm.haveItem(4031630, 1) ) {
                    cm.gainItem(4031630, -1);
				    cm.gainMeso(3000);			    
                    cm.sendOk("出售鱼获得3000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}				
			 }else if (selection == 106) {
                if (cm.haveItem(4031637, 1) ) {
                    cm.gainItem(4031637, -1);
				    cm.gainMeso(5000);			    
                    cm.sendOk("出售鱼获得5000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}	
			 }else if (selection == 107) {
                if (cm.haveItem(4031638, 1) ) {
                    cm.gainItem(4031638, -1);
				    cm.gainMeso(8000);	
                    cm.sendOk("出售鱼获得8000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 108) {
                if (cm.haveItem(4031639, 1) ) {
                    cm.gainItem(4031639, -1);
				    cm.gainMeso(10000);			    
                    cm.sendOk("出售鱼获得10000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 109) {
                if (cm.haveItem(4031640, 1) ) {
                    cm.gainItem(4031640, -1);
				    cm.gainMeso(12000);			    
                    cm.sendOk("出售鱼获得12000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 110) {
                if (cm.haveItem(4031628, 1) ) {
                    cm.gainItem(4031628, -1);
				    cm.gainMeso(3000);			    
                    cm.sendOk("出售鱼获得3000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}	
			 }else if (selection == 111) {
                if (cm.haveItem(4031641, 1) ) {
                    cm.gainItem(4031641, -1);
				    cm.gainMeso(5000);			    
                    cm.sendOk("出售鱼获得5000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 112) {
                if (cm.haveItem(4031642, 1) ) {
                    cm.gainItem(4031642, -1);
				    cm.gainMeso(8000);			    
                    cm.sendOk("出售鱼获得8000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 113) {
                if (cm.haveItem(4031643, 1) ) {
                    cm.gainItem(4031643, -1);
				    cm.gainMeso(10000);			    
                    cm.sendOk("出售鱼获得10000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 114) {
                if (cm.haveItem(4031644, 1) ) {
                    cm.gainItem(4031644, -1);
				    cm.gainMeso(12000);			    
                    cm.sendOk("出售鱼获得12000金币");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 115) {
                if (cm.haveItem(4031648, 20) ) {
                    cm.gainItem(4031648, -20);
                    cm.gainItem(1004197, 1);		    
                    cm.sendOk(" 恭喜获得道具 ");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 钓鱼满载而归兑换了 夺命鲨鱼 ! ");//公告					
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 116) {
                if (cm.haveItem(4031648, 30) ) {
                    cm.gainItem(4031648, -30);
                    cm.gainItem(1702366, 1);		    
                    cm.sendOk(" 恭喜获得道具 ");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 钓鱼满载而归兑换了 冰鲨鱼 ! ");//公告						
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}
			 }else if (selection == 117) {
                if (cm.haveItem(4031648, 80) ) {
                    cm.gainItem(4031648, -80);
                    cm.gainItem(5000227, 1);		    
                    cm.sendOk(" 恭喜获得宠物 ");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 钓鱼满载而归兑换了 小鲨鱼宠物 ! ");//公告						
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有鱼了,请在次确认");
                    cm.dispose();
				}					
			 }else if (selection == 150) {
                if (cm.haveItem(4032226, 20) ) {
                    cm.gainItem(4032226, -20);
                    cm.gainItem(2022490, 1);
                    cm.sendOk("获得#i2022490#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上没有#i4032226#,请在次确认");
                    cm.dispose();
				}
			 }
        }
    }
}

	