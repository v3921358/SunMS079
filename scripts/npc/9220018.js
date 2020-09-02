/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status;
var fbmc = "盖福克斯-(鬼屋活动)";//副本名称
var minLevel = 21;//最低等级
var maxLevel = 200;
var minPartySize = 1;//最少人数
var maxPartySize = 6;
var maxjinbi = 50000;//判断征集令金币
var eventname = "MV";//副本配置文件

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#k\t\t\t\t欢迎来到#r" + fbmc + "#k\r\n副本进入要求如下：\r\n①人数限制:#r " + minPartySize + " #b- #r" + maxPartySize + "#k队员\t②等级限制：#r " + minLevel + " #b- #r" + maxLevel + "级 #k\r\n"//3
            text += "#L1##r开始组队副本#l      #L2##r副本征集令#k" + maxjinbi + "金币/次#l  \r\n"//3
			//text += "#L2##r副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n"//3
            cm.sendSimple(text);
	} else if (selection == 1) {
	    if (cm.isQuestActive(5067)==0) {//判断任务
		cm.sendOk("你还没有接受相关任务,请找旁边的拉拉接受任务.");
		cm.dispose();
	    } else {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("队伍队长必须在这里.");
		cm.dispose();
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < minLevel) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 2 : 1);
		}	
		if (next && size >= minPartySize) {
			var em = cm.getEventManager("MV");
			if (em == null) {
				cm.sendOk("请稍后再试。.");
			} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
		    } else {
			cm.sendOk("另一个组队已经进入.");
		    }
			}
		} else {
			cm.sendOk("你的队伍必须大于"+minPartySize+"人,并且等级"+minLevel+"级以上");
		}
	    }
	cm.dispose();
}
	} else if (selection == 2) {
            if (cm.getMeso() >= maxjinbi){//判断多少金币
                cm.gainMeso(- maxjinbi );//扣除多少金币
				cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "[" + fbmc + "]需要勇士一起完成",11);
                cm.dispose();
                }else{
                    cm.sendOk("你的冒险币不足" + maxjinbi + "。无法发送征集令");
                    cm.dispose();
                            //cm.sendOk(".");
                //cm.dispose();
    }
	}
}