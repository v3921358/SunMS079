/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status = 0;
//副本名称
var fbmc = "通天塔-(女神副本)";

var eventname = "OrbisPQ";//副本配置文件

var maxjinbi = 10000;//判断征集令金币

var minLevel = 51;
var maxLevel = 200;//等级设置

var minPartySize = 3;
var maxPartySize = 6;//人数设置


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
            text += "#k\t\t\t    #r#v4030001#" + fbmc + "#v4030001##k\r\n";
            text += "#b副本要求:#k\r\n";
			text += "①等级限制：#r" + minLevel + "#k - #r" + maxLevel + "#k级\t\t②人数限制：#r" + minPartySize + " #k- #r" + maxPartySize + "队员#k\r\n";
			text += "#L1##r#v4030011#开始 " + fbmc + "#l\r\n\r\n";//#L0##l
			text += "#L2##r30个#v4001158#兑换#v1082232##z1082232##l\r\n\r\n";
            text += "#L3##r副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n";
            cm.sendSimple(text);
        } else if (selection == 1) {
	if (cm.getMapId() == 920010000) { //inside orbis pq
		cm.sendOk("我们必须拯救他 需要20个云的碎片");
		cm.dispose();
		return;
	}
	for (var i = 4001044; i < 4001064; i++) {
		cm.removeAll(i); //holy
	}
	if (cm.getParty() == null) { // No Party
	    cm.sendSimple("你貌似没有达到要求...:\r\n\r\n#r玩家成员要求: " + minPartySize + " , 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".");
		cm.dispose();
	} else if (!cm.isLeader()) { // Not Party Leader
	    cm.sendSimple("如果你想做任务，请#b队长#k 跟我谈.");
		cm.dispose();
	} else {
	    // Check if all 队员 are within PQ levels
	    var party = cm.getParty().getMembers();
	    var mapId = cm.getMapId();
	    var next = true;
	    var levelValid = 0;
	    var inMap = 0;
	    var it = party.iterator();

	    while (it.hasNext()) {
		var cPlayer = it.next();
		if ((cPlayer.getLevel() >= minLevel) && (cPlayer.getLevel() <= maxLevel)) {
		    levelValid += 1;
		} else {
		    next = false;
		}
		if (cPlayer.getMapid() == mapId) {
		    inMap += (cPlayer.getJobId() == 900 ? 6 : 1);
		}
	    }
	    if (party.size() > maxPartySize || inMap < minPartySize) {
		next = false;
	    }
	    if (next) {
		var em = cm.getEventManager("OrbisPQ");
		if (em == null) {
		    cm.sendSimple("找不到脚本请联络GM#b\r\n");
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("其他队伍已经在里面做 #r组队任务了#k 请尝试换频道或者等其他队伍完成。");
			cm.dispose();
		    }
		}
	    } else {
		cm.sendSimple("你的队伍貌似没有达到要求.\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".");
	    }
		cm.dispose();
	}
	        } else if (selection == 2) {
	if (!cm.canHold(1082232,1)) {
	    cm.sendOk("做好了。");
	} else if (cm.haveItem(4001158,30)) {
	    cm.gainItem(1082232, 1, true);
	    cm.gainItem(4001158, -30, true); 
	   cm.worldMessage(6,"玩家：["+cm.getName()+"] 完成了女神副本30次成功兑换了女神手镯,么么哒!");		
		cm.dispose();
	} else {
	    cm.sendOk("你没有30个#t4001158#.");
		cm.dispose();
					}	
        } else if (selection == 3) {
		if (cm.getMeso() >= maxjinbi){//判断多少金币
        cm.gainMeso(- maxjinbi );//扣除多少金币
	    cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "[" + fbmc + "] 求组队一起完成",11);
        cm.dispose();
        }else{
        cm.sendOk("你的冒险币不足" + maxjinbi + " 无法发送征集令");
        cm.dispose();
					}	
	}
    }
	}

