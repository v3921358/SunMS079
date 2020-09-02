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
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text +=" 嗨.我是戒指合成管理员\r\n"
            text += " #r如果你有以下材料的话可以找我兑换.\r\n"
			text += "#L0##e#d升级说明#l\r\n"
			text += "#L1##e#d合成#v4001322##r#z4001322##l\r\n"
			text += "#L2##e#d合成#v1112446##r#z1112446##l\r\n"
			text += "#L3##e#d升级#v1112447##r#z1112447##l\r\n"
			text += "#L4##e#d升级#v1112448##r#z1112448##l\r\n"
			text += "#L5##e#d升级#v1112449##r#z1112449##l\r\n"
			text += "#L6##e#d升级#v1112450##r#z1112450##l\r\n"
			text += "#L7##e#d升级#v1112451##r#z1112451##l\r\n"
			text += "#L8##e#d升级#v1112452##r#z1112452##l\r\n"
			text += "#L9##e#d升级#v1112453##r#z1112453##l\r\n"
			text += "#L10##e#d升级#v1112454##r#z1112454##l\r\n"
			text += "#L11##e#d升级#v1112455##r#z1112455##l\r\n"
			text += "#L12##e#d升级#v1112456##r#z1112456##l\r\n"
			text += "#L13##e#d升级#v1112457##r#z1112457##l\r\n"
			text += "#L14##e#d升级#v1112458##r#z1112458##l\r\n"
			text += "#L15##e#d升级#v1112459##r#z1112459##l\r\n"
			text += "#L16##e#d升级#v1112460##r#z1112460##l\r\n"
			text += "#L17##e#d升级#v1112461##r#z1112461##l\r\n"
			text += "#L18##e#d升级#v1112462##r#z1112462##l\r\n"
			text += "#L19##e#d升级#v1112463##r#z1112463##l\r\n"
			text += "#L20##e#d升级#v1112464##r#z1112464##l\r\n"
			text += "#L21##e#d升级#v1112465##r#z1112465##l\r\n"
			text += "#L22##e#d升级#v1112466##r#z1112466##l\r\n"
			text += "#L23##e#d升级#v1112467##r#z1112467##l\r\n"
			text += "#L24##e#d升级#v1112468##r#z1112468##l\r\n"
			text += "#L25##e#d升级#v1112469##r#z1112469##l\r\n"
			text += "#L26##e#d升级#v1112470##r#z1112470##l\r\n"
			text += "#L27##e#d升级#v1112471##r#z1112471##l\r\n"
			text += "#L28##e#d升级#v1112472##r#z1112472##l\r\n"
			text += "#L29##e#d升级#v1112473##r#z1112473##l\r\n"
			text += "#L30##e#d升级#v1112474##r#z1112474##l\r\n"
			text += "#L31##e#d升级#v1112475##r#z1112475##l\r\n"
			text += "#L32##e#d升级#v1112476##r#z1112476##l\r\n"
			text += "#L33##e#d升级#v1112477##r#z1112477##l\r\n"
			text += "#L34##e#d升级#v1112478##r#z1112478##l\r\n"
			text += "#L35##e#d升级#v1112479##r#z1112479##l\r\n"
			text += "#L36##e#d升级#v1112480##r#z1112480##l\r\n"
			text += "#L37##e#d升级#v1112481##r#z1112481##l\r\n"
			text += "#L38##e#d升级#v1112482##r#z1112482##l\r\n"
			text += "#L39##e#d升级#v1112483##r#z1112483##l\r\n"
			text += "#L40##e#d升级#v1112484##r#z1112484##l\r\n"
			text += "#L41##e#d升级#v1112485##r#z1112485##l\r\n"
			text += "#L42##e#d升级#v1112486##r#z1112486##l\r\n"
			text += "#L43##e#d升级#v1112487##r#z1112487##l\r\n"
			text += "#L44##e#d升级#v1112488##r#z1112488##l\r\n"
			text += "#L45##e#d升级#v1112489##r#z1112489##l\r\n"
			text += "#L46##e#d升级#v1112490##r#z1112490##l\r\n"
			text += "#L47##e#d升级#v1112491##r#z1112491##l\r\n"
			text += "#L48##e#d升级#v1112492##r#z1112492##l\r\n"
			text += "#L49##e#d升级#v1112493##r#z1112493##l\r\n"
			text += "#L50##e#d升级#v1112494##r#z1112494##l\r\n"
			text += "#L51##e#d升级#v1112495##r#z1112495##l\r\n"
			
            cm.sendOk(text);
        } else if (selection == 0) {
		cm.sendOk("#b升级说明:#k\r\n1.#v4001322#可通过金币+任意矿石成品+#v4000313##z4000313#5个获得! \r\n合成1级老公老婆戒指需要#v4000017##z4000017#x10\r\n 2-10级老公老婆戒指需要#v4001322##z4001322#x10\r\n11-20级老公老婆戒指需要#v4001322##z4001322#x20\r\n21-30级老公老婆戒指需要#v4001322##z4001322#x30\r\n30-50级老公老婆戒指需要#v4001322##z4001322#x50\r\n\r\ #r每一个等级的老公老婆戒指只允许装备一个!#k");
		cm.dispose();
        } else if (selection == 1) {
		if (cm.haveItem(4000313, 5) && cm.haveItem(4011001, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011001, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币		
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011004, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011004, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011006, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011006, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011002, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011002, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011003, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011003, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011005, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011005, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量 
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021000, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021000, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021001, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021001, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量 
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021002, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021002, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021003, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021003, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021004, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021004, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021005, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021005, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021006, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021006, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021007, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021007, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011000, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011000, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量 
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021008, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021008, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021005, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021005, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4021006, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4021006, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
		}else if (cm.haveItem(4000313, 5) && cm.haveItem(4011008, 1) && cm.getMeso() > 20000) {//判断物品
		cm.gainItem(4000313, -5);//扣除物品
		cm.gainItem(4011008, -1);//扣除物品
		cm.gainItem(4001322, 1);//奖励物品+数量
        cm.gainMeso(-20000);//扣除金币			
		cm.sendOk("合成#v4001322##z4001322#成功!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了白雪人法老的蓝宝石!大家一起来祝贺他/她吧!");//公告
		cm.dispose();			
		}else{
		cm.sendOk("材料不足!#v4000313##z4000313#不足5个或者没有任意矿石和2万金币!");
        cm.dispose();
			}					
        } else if (selection == 2) {
		if (!cm.haveItem(4000017, 10)) {//判断
		cm.sendOk("您没有10个#v4000017##r#z4000017#!");
		cm.dispose();
		}else{
		cm.gainItem(4000017, -10);//扣除
		cm.gainItem(1112446, 1);//奖励物品+数量
        cm.gainMeso(-50000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 合成了1级老公老婆戒指!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 3) {
		if (!cm.haveItem(1112446, 1)) {//判断
		cm.sendOk("您没有1个#v1112446##r#z1112446#或者5万金币!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 50000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#!");
		cm.dispose();
		}else{
		cm.gainItem(1112446, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112447, 1);//奖励物品
		cm.gainMeso(-50000);//扣除金币	
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指2级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 4) {
		if (!cm.haveItem(1112447, 1)) {//判断
		cm.sendOk("您没有1个#v1112447##r#z1112447#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 50000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者5万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112447, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112448, 1);//奖励物品
		cm.gainMeso(-50000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指3级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 5) {
		if (!cm.haveItem(1112448, 1)) {//判断
		cm.sendOk("您没有1个#v1112448##r#z1112448#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 50000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者5万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112448, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112449, 1);//奖励物品
		cm.gainMeso(-50000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指4级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 6) {
		if (!cm.haveItem(1112449, 1)) {//判断
		cm.sendOk("您没有1个#v1112449##r#z1112449#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 50000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者5万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112449, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112450, 1);//奖励物品
		cm.gainMeso(-50000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指5级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 7) {
		if (!cm.haveItem(1112450, 1)) {//判断
		cm.sendOk("您没有1个#v1112450##r#z1112450#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 300000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者30万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112450, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112451, 1);//奖励物品
		cm.gainMeso(-300000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指6级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 8) {
		if (!cm.haveItem(1112451, 1)) {//判断
		cm.sendOk("您没有1个#v1112451##r#z1112451#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 300000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者30万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112451, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112452, 1);//奖励物品
		cm.gainMeso(-300000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指7级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 9) {
		if (!cm.haveItem(1112452, 1)) {//判断
		cm.sendOk("您没有1个#v1112452##r#z1112452#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 300000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者30万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112452, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112453, 1);//奖励物品
		cm.gainMeso(-300000);//扣除金币	
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指8级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 10) {
		if (!cm.haveItem(1112453, 1)) {//判断
		cm.sendOk("您没有1个#v1112453##r#z1112453#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 300000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者30万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112453, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112454, 1);//奖励物品
		cm.gainMeso(-300000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指9级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 11) {
		if (!cm.haveItem(1112454, 1)) {//判断
		cm.sendOk("您没有1个#v1112454##r#z1112454#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 10)||cm.getMeso() <= 300000) {//判断
		cm.sendOk("您没有10个#v4001322##r#z4001322#或者30万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112454, -1);//扣除物品
		cm.gainItem(4001322, -10);//扣除物品
		cm.gainItem(1112455, 1);//奖励物品
		cm.gainMeso(-300000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指10级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 12) {
		if (!cm.haveItem(1112455, 1)) {//判断
		cm.sendOk("您没有1个#v1112455##r#z1112455#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 500000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者50万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112455, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112456, 1);//奖励物品
		cm.gainMeso(-500000);//扣除金币			
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指11级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 13) {
		if (!cm.haveItem(1112456, 1)) {//判断
		cm.sendOk("您没有1个#v1112456##r#z1112456#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 500000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者50万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112456, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112457, 1);//奖励物品
		cm.gainMeso(-500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指12级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 14) {
		if (!cm.haveItem(1112457, 1)) {//判断
		cm.sendOk("您没有1个#v1112457##r#z1112457#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 500000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者50万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112457, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112458, 1);//奖励物品
		cm.gainMeso(-500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指13级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 15) {
		if (!cm.haveItem(1112458, 1)) {//判断
		cm.sendOk("您没有1个#v1112458##r#z1112458#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 500000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者50万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112458, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112459, 1);//奖励物品
		cm.gainMeso(-500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指14级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 16) {
		if (!cm.haveItem(1112459, 1)) {//判断
		cm.sendOk("您没有1个#v1112459##r#z1112459#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 500000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者50万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112459, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112460, 1);//奖励物品
		cm.gainMeso(-500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指15级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 17) {
		if (!cm.haveItem(1112460, 1)) {//判断
		cm.sendOk("您没有1个#v1112460##r#z1112460#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112460, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112461, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指16级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 18) {
		if (!cm.haveItem(1112461, 1)) {//判断
		cm.sendOk("您没有1个#v1112461##r#z1112461#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112461, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112462, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指17级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 19) {
		if (!cm.haveItem(1112462, 1)) {//判断
		cm.sendOk("您没有1个#v1112462##r#z1112462#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112462, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112463, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指18级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 20) {
		if (!cm.haveItem(1112463, 1)) {//判断
		cm.sendOk("您没有1个#v1112463##r#z1112463#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112463, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112464, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指19级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 21) {
		if (!cm.haveItem(1112464, 1)) {//判断
		cm.sendOk("您没有1个#v1112464##r#z1112464#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 20)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有20个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112464, -1);//扣除物品
		cm.gainItem(4001322, -20);//扣除物品
		cm.gainItem(1112465, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指20级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 22) {
		if (!cm.haveItem(1112465, 1)) {//判断
		cm.sendOk("您没有1个#v1112465##r#z1112465#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112465, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112466, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指21级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 23) {
		if (!cm.haveItem(1112466, 1)) {//判断
		cm.sendOk("您没有1个#v1112466##r#z1112466#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112466, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112467, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指22级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 24) {
		if (!cm.haveItem(1112467, 1)) {//判断
		cm.sendOk("您没有1个#v1112467##r#z1112467#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112467, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112468, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指23级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 25) {
		if (!cm.haveItem(1112468, 1)) {//判断
		cm.sendOk("您没有1个#v1112468##r#z1112468#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112468, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112469, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指24级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 26) {
		if (!cm.haveItem(1112469, 1)) {//判断
		cm.sendOk("您没有1个#v1112469##r#z1112469#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112469, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112470, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指25级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}			
       } else if (selection == 27) {
		if (!cm.haveItem(1112470, 1)) {//判断
		cm.sendOk("您没有1个#v1112470##r#z1112470#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112470, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112471, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指26级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 28) {
		if (!cm.haveItem(1112471, 1)) {//判断
		cm.sendOk("您没有1个#v1112471##r#z1112471#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112471, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112472, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指27级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 29) {
		if (!cm.haveItem(1112472, 1)) {//判断
		cm.sendOk("您没有1个#v1112472##r#z1112472#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112472, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112473, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指28级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
        } else if (selection == 30) {
		if (!cm.haveItem(1112473, 1)) {//判断
		cm.sendOk("您没有1个#v1112473##r#z1112473#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112473, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112474, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指29级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
        } else if (selection == 31) {
		if (!cm.haveItem(1112474, 1)) {//判断
		cm.sendOk("您没有1个#v1112474##r#z1112474#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 30)||cm.getMeso() <= 1000000) {//判断
		cm.sendOk("您没有30个#v4001322##r#z4001322#或者100万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112474, -1);//扣除物品
		cm.gainItem(4001322, -30);//扣除物品
		cm.gainItem(1112475, 1);//奖励物品
		cm.gainMeso(-1000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指30级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}			
		} else if (selection == 32) {
		if (!cm.haveItem(1112475, 1)) {//判断
		cm.sendOk("您没有1个#v1112475##r#z1112475#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112475, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112476, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指31级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}			
		} else if (selection == 33) {
		if (!cm.haveItem(1112476, 1)) {//判断
		cm.sendOk("您没有1个#v1112476##r#z1112476#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112476, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112477, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指32级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 34) {
		if (!cm.haveItem(1112477, 1)) {//判断
		cm.sendOk("您没有1个#v1112477##r#z1112477#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112477, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112478, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指33级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 35) {
		if (!cm.haveItem(1112478, 1)) {//判断
		cm.sendOk("您没有1个#v1112478##r#z1112478#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112478, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112479, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指34级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 36) {
		if (!cm.haveItem(1112479, 1)) {//判断
		cm.sendOk("您没有1个#v1112479##r#z1112479#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112479, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112480, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指35级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 37) {
		if (!cm.haveItem(1112480, 1)) {//判断
		cm.sendOk("您没有1个#v1112480##r#z1112480#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112480, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112481, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指36级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 38) {
		if (!cm.haveItem(1112481, 1)) {//判断
		cm.sendOk("您没有1个#v1112481##r#z1112481#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#!或者150万金币!");
		cm.dispose();
		}else{
		cm.gainItem(1112481, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112482, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指37级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 39) {
		if (!cm.haveItem(1112482, 1)) {//判断
		cm.sendOk("您没有1个#v1112482##r#z1112482#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112482, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112483, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指38级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 40) {
		if (!cm.haveItem(1112483, 1)) {//判断
		cm.sendOk("您没有1个#v1112483##r#z1112483#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112483, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112484, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指39级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 41) {
		if (!cm.haveItem(1112484, 1)) {//判断
		cm.sendOk("您没有1个#v1112484##r#z1112484#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 1500000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者150万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112484, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112485, 1);//奖励物品
		cm.gainMeso(-1500000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指40级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}		
		} else if (selection == 42) {
		if (!cm.haveItem(1112485, 1)) {//判断
		cm.sendOk("您没有1个#v1112485##r#z1112485#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 2000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者200万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112485, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112486, 1);//奖励物品
		cm.gainMeso(-2000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指41级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 43) {
		if (!cm.haveItem(1112486, 1)) {//判断
		cm.sendOk("您没有1个#v1112486##r#z1112486#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 2000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者200万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112486, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112487, 1);//奖励物品
		cm.gainMeso(-2000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指42级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 44) {
		if (!cm.haveItem(1112487, 1)) {//判断
		cm.sendOk("您没有1个#v1112487##r#z1112487#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 2000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#!或者200万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112487, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112488, 1);//奖励物品
		cm.gainMeso(-2000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指43级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}
       } else if (selection == 45) {
		if (!cm.haveItem(1112488, 1)) {//判断
		cm.sendOk("您没有1个#v1112488##r#z1112488#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 2000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者200万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112488, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112489, 1);//奖励物品
		cm.gainMeso(-2000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指44级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}
       } else if (selection == 46) {
		if (!cm.haveItem(1112489, 1)) {//判断
		cm.sendOk("您没有1个#v1112489##r#z1112489#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 2000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者200万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112489, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112490, 1);//奖励物品
		cm.gainMeso(-2000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指45级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}
       } else if (selection == 47) {
		if (!cm.haveItem(1112490, 1)) {//判断
		cm.sendOk("您没有1个#v1112490##r#z1112490#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 3000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者300万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112490, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112491, 1);//奖励物品
		cm.gainMeso(-3000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指46级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 48) {
		if (!cm.haveItem(1112491, 1)) {//判断
		cm.sendOk("您没有1个#v1112491##r#z1112491#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 3000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者300万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112491, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112492, 1);//奖励物品
		cm.gainMeso(-3000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指47级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 49) {
		if (!cm.haveItem(1112492, 1)) {//判断
		cm.sendOk("您没有1个#v1112492##r#z1112492#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 3000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#!或者300万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112492, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112493, 1);//奖励物品
		cm.gainMeso(-3000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指48级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 50) {
		if (!cm.haveItem(1112493, 1)) {//判断
		cm.sendOk("您没有1个#v1112493##r#z1112493#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 3000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#!或者300万金币!!");
		cm.dispose();
		}else{
		cm.gainItem(1112493, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112494, 1);//奖励物品
		cm.gainMeso(-3000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指49级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}	
       } else if (selection == 51) {
		if (!cm.haveItem(1112494, 1)) {//判断
		cm.sendOk("您没有1个#v1112494##r#z1112494#!");
		cm.dispose();
		}else if (!cm.haveItem(4001322, 50)||cm.getMeso() <= 5000000) {//判断
		cm.sendOk("您没有50个#v4001322##r#z4001322#或者500万金币!!!");
		cm.dispose();
		}else{
		cm.gainItem(1112494, -1);//扣除物品
		cm.gainItem(4001322, -50);//扣除物品
		cm.gainItem(1112495, 1);//奖励物品
		cm.gainMeso(-5000000);//扣除金币		
		cm.sendOk("兑换成功");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 升级了老公老婆戒指50级!大家一起来祝贺他/她吧!");//公告
		cm.dispose();
			}				

	}
    }}



