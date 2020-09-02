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
            text +=" #k欢迎来到Machi&麻吉冒险岛项链合成!\r\n制作之前请先确保检查背包装备栏还有空位没.因此制作造成的损失自行承担!\r\n";
            text += "      #r制作#v1122020#（四维+1，攻击+3，魔力+3）需要以下材料:\r\n";
            text +="            #v4011000#1个  #v4011001#1个  #v4011002#1个  #v4011003#1个\r\n";
			text +="  #v4011004#1个  #v4011005#1个  #v4011006#1个  #v4021000#1个  #v4021001#1个\r\n";
			text +="  #v4021002#1个  #v4021003#1个  #v4021004#1个  #v4021005#1个  #v4021006#1个\r\n";
			text +="  #v4021007#1个  #v4021008#1个  #v4005000#1个  #v4005001#1个  #v4005002#1个\r\n";
			text +="  #v4005003#1个 #v4005004#1个 #v4032391#10个 #v4032392#10个 #v4032393#10个\r\n";
			text +="                 #v4002000#40个 合成费用：800，000金币（八十万）\r\n";
			text += "                                    #L1##r我要合成\r\n";
			
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.haveItem(4011000, 1) && cm.haveItem(4002000, 40) && cm.haveItem(4011001, 1) && cm.haveItem(4011002, 1) && cm.haveItem(4011003, 1) && cm.haveItem(4011004, 1) && cm.haveItem(4011005, 1) && cm.haveItem(4011006, 1) && cm.haveItem(4021000, 1) && cm.haveItem(4021001, 1) && cm.haveItem(4021002, 1) && cm.haveItem(4021003, 1) && cm.haveItem(4021004, 1) && cm.haveItem(4021005, 1) && cm.haveItem(4021006, 1) && cm.haveItem(4021007, 1) && cm.haveItem(4021008, 1) && cm.haveItem(4021008, 1) && cm.haveItem(4005000, 1) && cm.haveItem(4005001, 1) && cm.haveItem(4005002, 1) && cm.haveItem(4005003, 1) && cm.haveItem(4005004, 1) && cm.haveItem(4032391, 10) && cm.haveItem(4032392, 10) && cm.haveItem(4032393, 10) &&cm.getMeso() >=800000) {
                cm.haveItem(4011000,-1);
                cm.haveItem(4011001,-1);
                cm.haveItem(4011002,-1);
                cm.haveItem(4011003,-1);
                cm.haveItem(4011004,-1);
                cm.haveItem(4011005,-1);
                cm.haveItem(4011006,-1);
                cm.haveItem(4021000,-1);
                cm.haveItem(4021001,-1);
                cm.haveItem(4021002,-1);
                cm.haveItem(4021003,-1);
                cm.haveItem(4021004,-1);
                cm.haveItem(4021005,-1);
                cm.haveItem(4021006,-1);
                cm.haveItem(4021007,-1);
                cm.haveItem(4021008,-1);
                cm.haveItem(4021008,-1);
                cm.haveItem(4005000,-1);
                cm.haveItem(4005001,-1);
                cm.haveItem(4005002,-1);
                cm.haveItem(4005003,-1);
                cm.haveItem(4005004,-1);
				cm.haveItem(4002000,-40);
                cm.haveItem(4032391,-10);
                cm.haveItem(4032392,-10);
                cm.haveItem(4032393,-10);
                cm.gainMeso(-800000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1122020); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1122020)).copy(); // 生成一个Equip类
		//toDrop. setFlag(1);//上锁
		toDrop. setStr(1);//给力量
		toDrop. setDex(1);//给敏捷 
		toDrop. setInt(1);//给智力
		toDrop. setLuk(1);//给运气
		//toDrop. setHp(1);//HP
        //toDrop. setMp(1);//MP
		toDrop. setWatk(3);//攻击力    
		toDrop. setMatk(3);//魔法力

		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1122020#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足!");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (cm.haveItem(1113164, 1) && cm.haveItem(4001266, 50) && cm.haveItem(4000487, 50) && cm.haveItem(2210006, 5) && cm.haveItem(4003002, 5) && cm.haveItem(4003003, 5) && cm.haveItem(4000463, 1) && cm.haveItem(4011000, 10) && cm.haveItem(4011002, 10) && cm.haveItem(4011003, 10) && cm.haveItem(4011004, 10) && cm.haveItem(4011006, 10) && cm.getMeso() >=5000000) {
                cm.gainItem(1113164,-1);
				cm.gainItem(4001266,-50);
				cm.gainItem(4000487,-50);
                cm.gainItem(2210006,-5);
				cm.gainItem(4003002,-5);
				cm.gainItem(4003003,-5);
				cm.gainItem(4000463,-1);
				cm.gainItem(4011000,-10);
				cm.gainItem(4011002,-10);
				cm.gainItem(4011003,-10);
				cm.gainItem(4011004,-10);
				cm.gainItem(4011006,-10);
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
                cm.sendOk("合成失败！材料不足！#v1113164#*1个 #v4001266#*50个 #v4000487#*50个 #v2210006#*5个 #v4003002#*5个 #v4003003#*5个 #v4000463#*1个 #v4011000#*10个 #v4011002#*10个 #v4011003#*10个 #v4011004#*10个 #v4011006#*10个 500万金币");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (cm.haveItem(1113165, 1) && cm.haveItem(4001266, 100) && cm.haveItem(4000487, 100) && cm.haveItem(2210006, 8) && cm.haveItem(4003002, 10) && cm.haveItem(4003003, 10) && cm.haveItem(4000040, 5) && cm.haveItem(4000463, 3) && cm.haveItem(4021001, 10) && cm.haveItem(4021002, 10) && cm.haveItem(4021003, 10) && cm.haveItem(4021005, 10) && cm.haveItem(4021006, 10) && cm.haveItem(4021008, 10) &&cm.getMeso() >=10000000) {
                cm.gainItem(1113165,-1);
				cm.gainItem(4001266,-100);
				cm.gainItem(4000487,-100);
                cm.gainItem(2210006,-8);
				cm.gainItem(4003002,-10);
				cm.gainItem(4003003,-10);
				cm.gainItem(4000040,-5);
				cm.gainItem(4000463,-3);
				cm.gainItem(4021001,-10);
				cm.gainItem(4021002,-10);
				cm.gainItem(4021003,-10);
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
                cm.sendOk("合成失败！材料不足！#v1113165#*1个 #v4001266#*100个 #v4000487#*100个 #v2210006#*8个 #v4003002#*10个 #v4003003#*10个 #v4000040#*5个 #v4000463#*3个 #v4021001#*10个 #v4021002#*10个 #v4021003#*10个 #v4021005#*10个 #v4021006#*10个 #v4021008#*10个+ 1000万金币");
                cm.dispose();
            }
        } else if (selection == 4) {
            if (cm.haveItem(1113166, 1) && cm.haveItem(4001266, 200) && cm.haveItem(4000487, 200) && cm.haveItem(4003002, 20) && cm.haveItem(4003003, 20) && cm.haveItem(4001006, 50) && cm.haveItem(4000040, 10) && cm.haveItem(4000176, 10) && cm.haveItem(4021009, 10) && cm.haveItem(4011007, 10) && cm.haveItem(4021010, 2) && cm.haveItem(4000463, 5) && cm.getMeso() >=10000000) {
                cm.gainItem(1113166,-1);
				cm.gainItem(4001266,-200);
				cm.gainItem(4000487,-200);
                cm.gainItem(4003002,-20);
				cm.gainItem(4003003,-20);
				cm.gainItem(4001006,-50);
				cm.gainItem(4000040,-10);
				cm.gainItem(4000176,-10);
				cm.gainItem(4021009,-10);
				cm.gainItem(4011007,-10);
				cm.gainItem(4021010,-2);
				cm.gainItem(4000463,-5);
                cm.gainMeso(-10000000);
				cm.gainItem(1113167,1);
                cm.sendOk("合成#v1113167#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113166#*1个 #v4001266#*200个 #v4000487#200个 #v4003002#20个 #v4003003#*20个 #v4001006#*50个 #v4000040#*10个 #v4000176#*10个 #v4021009#*10个 #v4011007#*10个 #v4021010#*2个 #v4000463#*5个+ 1000万金币");
                cm.dispose();
            }
        } else if (selection == 5) {
            if (cm.haveItem(1113167, 1) && cm.haveItem(4001266, 300) && cm.haveItem(4000487, 300) && cm.haveItem(4001006, 100) && cm.haveItem(4000040, 20) && cm.haveItem(4000176, 20) && cm.haveItem(4021009, 20) && cm.haveItem(4011007, 20) && cm.haveItem(4021010, 5) && cm.haveItem(4000257, 10) && cm.haveItem(4000463, 10) && cm.getMeso() >=30000000) {
                cm.gainItem(1113167,-1);
				cm.gainItem(4001266,-300);
				cm.gainItem(4000487,-300);
                cm.gainItem(4001006,-100);
				cm.gainItem(4000040,-20);
				cm.gainItem(4000176,-20);
				cm.gainItem(4021009,-20);
				cm.gainItem(4011007,-20);
				cm.gainItem(4021010,-5);
				cm.gainItem(4000257,-10);
				cm.gainItem(4000463,-10);
                cm.gainMeso(-30000000);
				cm.gainItem(1113168,1);
                cm.sendOk("合成#v1113168#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足！#v1113167#*1个 #v4001266#*300个 #v4000487#*300个 #v4001006#*100个 #v4000040#*20个 #v4000176#*20个 #v4021009#*20个 #v4011007#*20个 #v4021010#*5个 #v4000257#*10个 #v4000463#*10个+ 3000万金币");
                cm.dispose();
            }
        } else if (selection == 6) {
			
            if (cm.haveItem(1112679, 3) && cm.haveItem(4000313, 1250) && cm.haveItem(4011004, 50) && cm.haveItem(4021007, 50) && cm.haveItem(4011006, 50) && cm.haveItem(4021005, 50) && cm.haveItem(4021002, 50) && cm.haveItem(4251202, 5) && cm.haveItem(4001083, 5) && cm.haveItem(4001084, 15) && cm.haveItem(4001085, 15) && cm.haveItem(4000175, 15) && cm.haveItem(4001126, 12500) && cm.haveItem(4000487, 100) && cm.haveItem(4002000, 500) && cm.haveItem(4002001, 75) && cm.haveItem(4002002, 25) && cm.haveItem(4002003, 90) && cm.haveItem(4031559, 50) && cm.getMeso() >=50000000) {
                cm.gainItem(1112679,-3);
				cm.gainItem(4000313,-1250);
				cm.gainItem(4011004,-50);
                cm.gainItem(4021007,-50);
				cm.gainItem(4011006,-50);
				cm.gainItem(4021005,-50);
				cm.gainItem(4021002,-50);
				cm.gainItem(4251202,-5);
				cm.gainItem(4001083,-5);
				cm.gainItem(4001084,-15);
				cm.gainItem(4001085,-15);
				cm.gainItem(4000175,-15);
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
                cm.sendOk("合成失败！材料不足！#v4000313#*1250个 #v1112679#*3个 #v4011004#*50个  #v4021007#*50个   #v4011006#*50个   #v4021005#*50个  #v4021002#*50个   #v4251202#*5个 #v4001083#*5个 #v4001084#*15个 #v4001085#*15个 #v4000175#*15个  #v4001126#*12500个   #v4000487#*100个  #v4002000#*500个  #v4002001#*100个 #v4002002#*25个 #v4002003#*90个 #v4031559#*50个 5000万金币");
                cm.dispose();
            }
			        } else if (selection == 7) {
            if (cm.haveItem(1112679, 3) && cm.haveItem(4000313, 1250) && cm.haveItem(4011004, 50) && cm.haveItem(4021007, 50) && cm.haveItem(4011006, 50) && cm.haveItem(4021005, 50) && cm.haveItem(4021002, 50) && cm.haveItem(4251202, 5) && cm.haveItem(4001083, 5) && cm.haveItem(4001084, 15) && cm.haveItem(4001085, 15) && cm.haveItem(4000175, 15) && cm.haveItem(4001126, 12500) && cm.haveItem(4000487, 100) && cm.haveItem(4002000, 500) && cm.haveItem(4002001, 75) && cm.haveItem(4002002, 25) && cm.haveItem(4002003, 90) && cm.haveItem(4031559, 50) && cm.getMeso() >=50000000) {
                cm.gainItem(1112679,-3);
				cm.gainItem(4000313,-1250);
				cm.gainItem(4011004,-50);
                cm.gainItem(4021007,-50);
				cm.gainItem(4011006,-50);
				cm.gainItem(4021005,-50);
				cm.gainItem(4021002,-50);
				cm.gainItem(4251202,-5);
				cm.gainItem(4001083,-5);
				cm.gainItem(4001084,-15);
				cm.gainItem(4001085,-15);
				cm.gainItem(4000175,-15);
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
                cm.sendOk("合成失败！材料不足！#v4000313#*1250个 #v1112679#*3个 #v4011004#*50个 #v4021007#*50个 #v4011006#*50个 #v4021005#*50个 #v4021002#*50个 #v4251202#*5个  #v4001083#*5个   #v4001084#*15个   #v4001085#*15个   #v4000175#*15个  #v4001126#*12500个  #v4000487#*100个  #v4002000#*500个  #v4002001#*100个  #v4002002#*25个 #v4002003#*90个 #v4031559#*50个 5000万金币");
                cm.dispose();
            }
			}
		}
    }
    


