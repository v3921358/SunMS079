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
            text += "#L1##r制作奈基女神的守护需要以下材料:\r\n";
            text += "#v1112915#*1 #v4001221#*25 #v4001222#*25 #v4001223#*10 #v4001224#*10 #v4001225#*10 #v4021000#*5 #v4021001#*5 #v4021002#*5 #v4021003#*5 #v4021004#*5 #v4021005#*5 #v4021006#*5 #v4021007#*5 +金币200万\r\n";
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.haveItem(4001221, 25) && cm.haveItem(4001222, 25) && cm.haveItem(4001223, 10) && cm.haveItem(4001224, 10) && cm.haveItem(4001225, 10) && cm.haveItem(4021000, 5) && cm.haveItem(4021001, 5) && cm.haveItem(4021002, 5) && cm.haveItem(4021003, 5) && cm.haveItem(4021004, 5) && cm.haveItem(4021005, 5) && cm.haveItem(4021006, 5) && cm.haveItem(4021007, 5) && cm.haveItem(1112915, 1) &&cm.getMeso() >=2000000) {
                cm.gainItem(4001221,-25);
				cm.gainItem(4001222,-25); 
				cm.gainItem(4001223,-10);
				cm.gainItem(4001224,-10);
				cm.gainItem(4001225,-10);
				cm.gainItem(4021000,-5);
				cm.gainItem(4021001,-5);
				cm.gainItem(4021002,-5);
				cm.gainItem(4021003,-5);
				cm.gainItem(4021004,-5);
				cm.gainItem(4021005,-5);
				cm.gainItem(4021006,-5);
				cm.gainItem(4021007,-5);
				cm.gainItem(1112915,-1);
                cm.gainMeso(-2000000);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1112932); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1112932)).copy(); // 生成一个Equip类
		////toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(5);//给力量
		toDrop. setDex(5);//给敏捷 
		toDrop. setInt(5);//给智力
		toDrop. setLuk(5);//给运气
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1112932#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足!");
                cm.dispose();
            }
            }
			}
		}
    


