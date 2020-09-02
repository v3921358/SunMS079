/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
var status = -1;
var fbmc = "毒雾森林-(毒雾副本)";//副本名称
var minLevel = 45;
var maxLevel = 55;
var minPartySize = 3;
var maxPartySize = 6;
var maxjinbi = 10000;//判断征集令金币
var eventname = "Ellin";//副本配置文件
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	    cm.givePartyItems(4001161, 0, true);
	    cm.givePartyItems(4001162, 0, true);
	    cm.givePartyItems(4001163, 0, true);
	    cm.givePartyItems(4001169, 0, true);
	    cm.givePartyItems(2270004, 0, true);
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
	//显示物品ID图片用的代码是  #v这里写入ID#
    text += "#k\t\t\t#r#v4030001#" + fbmc + "#v4030001##k\r\n#b副本进入要求如下#k：\r\n①人数限制:#r " + minPartySize + " #b- #r" + maxPartySize + "#k队员\t②等级限制：#r " + minLevel + " #b- #r" + maxLevel + "级 #k\r\n"//3
    text += "#L3##r#v4030011#开始组队副本#l    #L2##r#v4030011#副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n#L0##v4030011##b兑换阿尔泰耳环#k#l\t#L1##v4030011##b兑换#r发光#k#b阿尔泰耳环#l\r\n"//
    cm.sendSimple(text);
    } else if (status == 1) {
	if (selection == 0) {
	    if (!cm.haveItem(1032060) && cm.haveItem(4001198, 15)) {
		cm.gainItem(1032060,1, true);
	 cm.worldMessage(6,"玩家：["+cm.getName()+"]完成了毒物副本15次成功兑换了阿尔泰耳环,么么哒!");		
		cm.gainItem(4001198, -15);
	    } else {
		cm.sendOk("你需要15个阿尔泰碎片,或者是你已经有阿尔泰耳环了");
	    }
		
	} else if (selection == 1){
	    if (cm.haveItem(1032060) && !cm.haveItem(1032061) && cm.haveItem(4001198, 20)) {
		cm.gainItem(1032060,-1);
		cm.gainItem(1032061, 1, true);
	 cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功升级阿尔泰耳环,么么哒!");		
		cm.gainItem(4001198, -20);
	    } else {
		cm.sendOk("你需要20个阿尔泰碎片跟阿尔泰耳环,或者是你已经有发光阿尔泰耳环了");
	    }
	} else if (selection == 2){
            if (cm.getMeso() >= maxjinbi){//判断多少金币
                cm.gainMeso(- maxjinbi );//扣除多少金币
				cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "[" + fbmc + "]求组队一起完成",11);
                cm.dispose();
                }else{
                    cm.sendOk("你的冒险币不足" + maxjinbi + "。无法发送征集令");
                    cm.dispose();
	}
	} else if (selection == 3) {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("找您的队长来和我谈话。");
		cm.dispose();
		} else if (!cm.haveItem(4002000, 2)) {//判断物品
		cm.sendOk("进去副本需要2张#v4002000#,1张#v4002001#。");
		cm.dispose();
		} else if (!cm.haveItem(4002001, 1)) {//判断物品
		cm.sendOk("进去副本需要2张#v4002000#,1张#v4002001#。");
		cm.dispose();
	    } else {
		cm.gainItem(4002000, -2);//扣除物品
		cm.gainItem(4002001, -1);//扣除物品
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < minLevel || ccPlayer.getJob() > 900 || ccPlayer.getLevel() > maxLevel) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && size >= minPartySize) {
			var em = cm.getEventManager("Ellin");
			if (em == null) {
				cm.sendOk("当前副本有问题,请联络管理员.");
			} else {
				var prop = em.getProperty("state");
                if (prop.equals("0") || prop == null) {
					em.startInstance(cm.getParty(), cm.getMap());
					cm.dispose();
					return;
				} else {
					cm.sendOk("里面已经有人了,请你稍后在进入看看,或者更换频道");
				}

			}
		} else {
			cm.sendOk("你的队伍#b成员#k需要#b3个#k以上等级45~55的队员才能进入!");
		}
	    }
	}
	cm.dispose();
    }
	
}