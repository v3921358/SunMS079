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
            text +=" #k欢迎来到枫之谷戒指制作!制作之前请先确保检查背包装备栏还有空位没.因此制作造成的损失自行承担!\r\n";
            text += "#L1##r制作蓝调戒指需要以下材料:\r\n";
            text += "#v1112915#*1 #v4001221#*15 #v4001222#*15 #v4001223#*5 #v4001224#*5 #v4001225#*5 #v4011000#*5 #v4011001#*5 #v4011002#*5 #v4011003#*5 #v4011004#*5 #v4011005#*5 #v4011006#*5 +金币100万\r\n";
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.haveItem(4001221, 15) && cm.haveItem(4001222, 15) && cm.haveItem(4001223, 5) && cm.haveItem(4001224, 5) && cm.haveItem(4001225, 5) && cm.haveItem(4011000, 5) && cm.haveItem(4011001, 5) && cm.haveItem(4011002, 5) && cm.haveItem(4011003, 5) && cm.haveItem(4011004, 5) && cm.haveItem(4011005, 5) && cm.haveItem(4011006, 5) && cm.haveItem(1112907, 1) &&cm.getMeso() >=1000000) {
                cm.gainItem(4001221,-15);
				cm.gainItem(4001222,-15); 
				cm.gainItem(4001223,-5);
				cm.gainItem(4001224,-5);
				cm.gainItem(4001225,-5);
				cm.gainItem(4011000,-5);
				cm.gainItem(4011001,-5);
				cm.gainItem(4011002,-5);
				cm.gainItem(4011003,-5);
				cm.gainItem(4011004,-5);
				cm.gainItem(4011005,-5);
				cm.gainItem(4011006,-5);
				cm.gainItem(1112907,-1);
                cm.gainMeso(-1000000);
        var ii = MapleItemInformationProvider.getInstance();		                
        var type = ii.getInventoryType(1112915); //获得装备的类形
        var toDrop = ii.randomizeStats(ii.getEquipById(1112915)).copy(); // 生成一个Equip类
		var temptime = (System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000); //时间
		toDrop.setExpiration(temptime); 
		////toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(3);//给力量
		toDrop. setDex(3);//给敏捷 
		toDrop. setInt(3);//给智力
		toDrop. setLuk(3);//给运气
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1112915#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足!");
                cm.dispose();
            }
            }
			}
		}
    


