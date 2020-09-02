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
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 60; i++) {
                text += "";
            }
            text +=" #k欢迎来到Machi&麻吉冒险岛戒指合成!\r\n制作之前请先确保检查背包装备栏还有空位没.如果因为背包问题造成的损失GM概不负责!\r\n";
            text +="      #r升级#v1112774#C级（四维+5 攻击+2 魔力+4 命中+15）\r\n      需要以下材料:\r\n ";
			text +="     请先把#v1112657#卸下放在背包里\r\n";
			text +="  #v4011004#10个 #v4021004#10个 #v4021005#10个 #v4005002#5个\r\n";
			text +="  #v4021008#10个  #v4005004#5个 #v4021009#5个 #v4011007#5个 #v4003000#200个 \r\n";
			text +="  #v4031560#15个 #v4031559#10个  #v4031561#5个 #v4032392#5个 #v4032393#5个\r\n";
			text +="                  合成费用：2,000,000金币（二百万）\r\n";
			text += "                                    #L1##r我要合成\r\n";
			
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.haveItem(1112657, 1) && cm.haveItem(4011004, 10) && cm.haveItem(4021000, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4005002, 5) && cm.haveItem(4021008, 10) && cm.haveItem(4005004, 5) && cm.haveItem(4021009, 5) && cm.haveItem(4011007, 5) && cm.haveItem(4003000, 200) && cm.haveItem(4031560, 10) && cm.haveItem(4031559, 15) && cm.haveItem(4031561, 5) && cm.haveItem(4032392, 5) && cm.haveItem(4032393, 5) && cm.getMeso() >=2000000) {
                cm.gainItem(1112657,-1);
				cm.gainItem(4011004,-10);
				cm.gainItem(4021000,-10);
				cm.gainItem(4021005,-10);
				cm.gainItem(4005002,-5);
				cm.gainItem(4021008,-10);
				cm.gainItem(4005004,-5);
				cm.gainItem(4021009,-5);
				cm.gainItem(4011007,-5);
				cm.gainItem(4003000,-200);
				cm.gainItem(4031560,-10);
				cm.gainItem(4031559,-15);
				cm.gainItem(4031561,-5);
				cm.gainItem(4032392,-5);
				cm.gainItem(4032393,-5);
                cm.gainMeso(-2000000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1112774); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1112774)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁
		toDrop. setStr(5);//给力量
		toDrop. setDex(5);//给敏捷 
		toDrop. setInt(5);//给智力
		toDrop. setLuk(5);//给运气
		toDrop. setHp(0);//HP
        toDrop. setMp(0);//MP
		toDrop. setWatk(2);//攻击力    
		toDrop. setMatk(4);//魔法力
		toDrop. setWdef(0);//物理防御
		toDrop. setMdef(0);//魔法防御
		toDrop. setAcc(15);//命中
		toDrop. setAvoid(0);//回避
		toDrop. setHands(0);//手技
		toDrop. setSpeed(0);//移动速度
		toDrop. setJump(0);//跳跃

		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1112774#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足!");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (cm.haveItem(1113164, 1) && cm.haveItem(4001266, 50) && cm.haveItem(4000487, 50) && cm.haveItem(4031559, 5) && cm.haveItem(4003002, 5) && cm.haveItem(4003003, 5) && cm.haveItem(4000463, 1) && cm.haveItem(4021000, 10) && cm.haveItem(4021004, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4021004, 10) && cm.haveItem(4021006, 10) && cm.getMeso() >=5000000) {
                cm.gainItem(1113164,-1);
				cm.gainItem(4001266,-50);
				cm.gainItem(4000487,-50);
                cm.gainItem(4031559,-5);
				cm.gainItem(4003002,-5);
				cm.gainItem(4003003,-5);
				cm.gainItem(4000463,-1);
				cm.gainItem(4021000,-10);
				cm.gainItem(4021004,-10);
				cm.gainItem(4021005,-10);
				cm.gainItem(4021004,-10);
				cm.gainItem(4021006,-10);
                cm.gainMeso(-5000000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1113165); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1113165)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁
		toDrop. setStr(1);//给力量
		toDrop. setDex(1);//给敏捷 
		toDrop. setInt(1);//给智力
		toDrop. setLuk(1);//给运气
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1113165#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113164#*1个 #v4001266#*50个 #v4000487#*50个 #v4031559#*5个 #v4003002#*5个 #v4003003#*5个 #v4000463#*1个 #v4021004#*10个 #v4021004#*10个 #v4021005#*10个 #v4021004#*10个 #v4021006#*10个 500万金币");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (cm.haveItem(1113165, 1) && cm.haveItem(4001266, 100) && cm.haveItem(4000487, 100) && cm.haveItem(4031559, 8) && cm.haveItem(4003002, 10) && cm.haveItem(4003003, 10) && cm.haveItem(4031561, 5) && cm.haveItem(4000463, 7) && cm.haveItem(4021005, 10) && cm.haveItem(4021004, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4021006, 10) && cm.haveItem(4021008, 10) &&cm.getMeso() >=10000000) {
                cm.gainItem(1113165,-1);
				cm.gainItem(4001266,-100);
				cm.gainItem(4000487,-100);
                cm.gainItem(4031559,-8);
				cm.gainItem(4003002,-10);
				cm.gainItem(4003003,-10);
				cm.gainItem(4031561,-5);
				cm.gainItem(4000463,-7);
				cm.gainItem(4021005,-10);
				cm.gainItem(4021004,-10);
				cm.gainItem(4021005,-10);
				cm.gainItem(4021005,-10);
				cm.gainItem(4021006,-10);
				cm.gainItem(4021008,-10);
                cm.gainMeso(-10000000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1113166); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1113166)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁
		toDrop. setStr(1);//给力量
		toDrop. setDex(1);//给敏捷 
		toDrop. setInt(1);//给智力
		toDrop. setLuk(1);//给运气
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1113166#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113165#*1个 #v4001266#*100个 #v4000487#*100个 #v4031559#*8个 #v4003002#*10个 #v4003003#*10个 #v4031561#*5个 #v4000463#*7个 #v4021005#*10个 #v4021004#*10个 #v4021005#*10个 #v4021005#*10个 #v4021006#*10个 #v4021008#*10个+ 1000万金币");
                cm.dispose();
            }
        } else if (selection == 4) {
            if (cm.haveItem(1113166, 1) && cm.haveItem(4001266, 200) && cm.haveItem(4000487, 200) && cm.haveItem(4003002, 20) && cm.haveItem(4003003, 20) && cm.haveItem(4001006, 50) && cm.haveItem(4031561, 10) && cm.haveItem(4031560, 10) && cm.haveItem(4021000, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4021010, 2) && cm.haveItem(4000463, 5) && cm.getMeso() >=10000000) {
                cm.gainItem(1113166,-1);
				cm.gainItem(4001266,-200);
				cm.gainItem(4000487,-200);
                cm.gainItem(4003002,-20);
				cm.gainItem(4003003,-20);
				cm.gainItem(4001006,-50);
				cm.gainItem(4031561,-10);
				cm.gainItem(4031560,-10);
				cm.gainItem(4021000,-10);
				cm.gainItem(4021005,-10);
				cm.gainItem(4021010,-2);
				cm.gainItem(4000463,-5);
                cm.gainMeso(-10000000);
				cm.gainItem(1113167,1);
                cm.sendOk("合成#v1113167#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113166#*1个 #v4001266#*200个 #v4000487#200个 #v4003002#20个 #v4003003#*20个 #v4001006#*50个 #v4031561#*10个 #v4031560#*10个 #v4021004#*10个 #v4021005#*10个 #v4021010#*2个 #v4000463#*5个+ 1000万金币");
                cm.dispose();
            }
        } else if (selection == 5) {
            if (cm.haveItem(1113167, 1) && cm.haveItem(4001266, 700) && cm.haveItem(4000487, 700) && cm.haveItem(4001006, 100) && cm.haveItem(4031561, 20) && cm.haveItem(4031560, 20) && cm.haveItem(4021000, 20) && cm.haveItem(4021005, 20) && cm.haveItem(4021010, 5) && cm.haveItem(4000257, 10) && cm.haveItem(4000463, 10) && cm.getMeso() >=30000000) {
                cm.gainItem(1113167,-1);
				cm.gainItem(4001266,-300);
				cm.gainItem(4000487,-300);
                cm.gainItem(4001006,-100);
				cm.gainItem(4031561,-20);
				cm.gainItem(4031560,-20);
				cm.gainItem(4021000,-20);
				cm.gainItem(4021005,-20);
				cm.gainItem(4021010,-5);
				cm.gainItem(4000257,-10);
				cm.gainItem(4000463,-10);
                cm.gainMeso(-30000000);
				cm.gainItem(1113168,1);
                cm.sendOk("合成#v1113168#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113167#*1个 #v4001266#*300个 #v4000487#*300个 #v4001006#*100个 #v4031561#*20个 #v4031560#*20个 #v4021004#*20个 #v4021005#*20个 #v4021010#*5个 #v4000257#*10个 #v4000463#*10个+ 3000万金币");
                cm.dispose();
            }
        } else if (selection == 6) {
			
            if (cm.haveItem(1112679, 7) && cm.haveItem(4000313, 1250) && cm.haveItem(4021004, 50) && cm.haveItem(4021007, 50) && cm.haveItem(4021006, 50) && cm.haveItem(4021005, 50) && cm.haveItem(4021004, 50) && cm.haveItem(4251202, 5) && cm.haveItem(4001083, 5) && cm.haveItem(4001084, 10) && cm.haveItem(4001085, 10) && cm.haveItem(4000175, 10) && cm.haveItem(4001126, 12500) && cm.haveItem(4000487, 100) && cm.haveItem(4002000, 500) && cm.haveItem(4002001, 75) && cm.haveItem(4002002, 25) && cm.haveItem(4002003, 90) && cm.haveItem(4031559, 50) && cm.getMeso() >=50000000) {
                cm.gainItem(1112679,-7);
				cm.gainItem(4000313,-1250);
				cm.gainItem(4021004,-50);
                cm.gainItem(4021007,-50);
				cm.gainItem(4021006,-50);
				cm.gainItem(4021005,-50);
				cm.gainItem(4021004,-50);
				cm.gainItem(4251202,-5);
				cm.gainItem(4001083,-5);
				cm.gainItem(4001084,-10);
				cm.gainItem(4001085,-10);
				cm.gainItem(4000175,-10);
				cm.gainItem(4001126,-12500);
				cm.gainItem(4000487,-100);
				cm.gainItem(4002000,-500);
				cm.gainItem(4002001,-100);
				cm.gainItem(4002002,-25);
				cm.gainItem(4002003,-90);
				cm.gainItem(4031559,-50);
                cm.gainMeso(-50000000);
				cm.gainItem(1113178,1);
                cm.sendOk("合成#v1113178#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v4000313#*1250个 #v1112679#*7个 #v4021004#*50个  #v4021007#*50个   #v4021006#*50个   #v4021005#*50个  #v4021004#*50个   #v4251202#*5个 #v4001083#*5个 #v4001084#*10个 #v4001085#*10个 #v4000175#*10个  #v4001126#*12500个   #v4000487#*100个  #v4002000#*500个  #v4002001#*100个 #v4002002#*25个 #v4002003#*90个 #v4031559#*50个 5000万金币");
                cm.dispose();
            }
			        } else if (selection == 7) {
            if (cm.haveItem(1112679, 7) && cm.haveItem(4000313, 1250) && cm.haveItem(4021004, 50) && cm.haveItem(4021007, 50) && cm.haveItem(4021006, 50) && cm.haveItem(4021005, 50) && cm.haveItem(4021004, 50) && cm.haveItem(4251202, 5) && cm.haveItem(4001083, 5) && cm.haveItem(4001084, 10) && cm.haveItem(4001085, 10) && cm.haveItem(4000175, 10) && cm.haveItem(4001126, 12500) && cm.haveItem(4000487, 100) && cm.haveItem(4002000, 500) && cm.haveItem(4002001, 75) && cm.haveItem(4002002, 25) && cm.haveItem(4002003, 90) && cm.haveItem(4031559, 50) && cm.getMeso() >=50000000) {
                cm.gainItem(1112679,-7);
				cm.gainItem(4000313,-1250);
				cm.gainItem(4021004,-50);
                cm.gainItem(4021007,-50);
				cm.gainItem(4021006,-50);
				cm.gainItem(4021005,-50);
				cm.gainItem(4021004,-50);
				cm.gainItem(4251202,-5);
				cm.gainItem(4001083,-5);
				cm.gainItem(4001084,-10);
				cm.gainItem(4001085,-10);
				cm.gainItem(4000175,-10);
				cm.gainItem(4001126,-12500);
				cm.gainItem(4000487,-100);
				cm.gainItem(4002000,-500);
				cm.gainItem(4002001,-100);
				cm.gainItem(4002002,-25);
				cm.gainItem(4002003,-90);
				cm.gainItem(4031559,-50);
                cm.gainMeso(-50000000);
				cm.gainItem(1113037,1);
                cm.sendOk("合成#v1113037#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v4000313#*1250个 #v1112679#*7个 #v4021004#*50个 #v4021007#*50个 #v4021006#*50个 #v4021005#*50个 #v4021004#*50个 #v4251202#*5个  #v4001083#*5个   #v4001084#*10个   #v4001085#*10个   #v4000175#*10个  #v4001126#*12500个  #v4000487#*100个  #v4002000#*500个  #v4002001#*100个  #v4002002#*25个 #v4002003#*90个 #v4031559#*50个 5000万金币");
                cm.dispose();
            }
			}
		}
    }
    


