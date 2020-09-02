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
            text +="#v3991033##v3991026##v3991041##v3991041##v3991050#           #v3990001##v3990009##v3990000##v3990006#\r\n";
			text +="#v3991039##v3991030##v3991048#    #v3991050##v3991030##v3991026##v3991043#\r\n";
			text +="#L1#50个#v3994044#兑换#v1142218#（全属+5，攻击/魔法+2#l\r\n\r\n";
			text +=" PS：不是鸡吧的鸡吧，是收集“鸡” 字吧，年轻人思想不要太肮脏，我这没有去污粉O__O …\r\n";
            cm.sendSimple(text);
		} else if (selection == 1) {
        			if (cm.getInventory(1).isFull(1)){
			cm.sendOk("#b请保证装备栏位至少有2个空格,否则无法兑换.");
			cm.dispose();	
        } else if (cm.haveItem(3994044, 58)){
                cm.gainItem(3994044,-58);
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142218); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142218)).copy(); // 生成一个Equip类
		////toDrop. setFlag(1);//上锁不能与时间一起，否则会BUG不消失//上锁
		toDrop. setStr(5);//给力量
		toDrop. setDex(5);//给敏捷 
		toDrop. setInt(5);//给智力
		toDrop. setLuk(5);//给运气
		//toDrop. setHp(1);//HP
        //toDrop. setMp(1);//MP
		toDrop. setWatk(2);//攻击力    
		toDrop. setMatk(2);//魔法力
		toDrop. setAvoid(0);//回避力
		toDrop. setHands(0);//手技

		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包
                cm.sendOk("合成#v1142218#成功！");
                cm.dispose();
            } else {
                cm.sendOk("合成失败！材料不足!");
                cm.dispose();
			}
	}
    }
}