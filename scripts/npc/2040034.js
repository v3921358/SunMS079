/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status = 0;
//副本名称
var fbmc = "玩具城-(玩具塔101副本)";

var eventname = "LudiPQ";//副本配置文件

var maxjinbi = 10000;//判断征集令金币

var minLevel = 35;
var maxLevel = 200;//等级设置

var minPartySize = 3;
var maxPartySize = 6;//人数设置

var open = true;//false true//其他设置

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
            text += "#k\t\t    #r#v4030001#" + fbmc + "#r#v4030001##k\r\n";
            text += "#b副本进入要求如下：#k\r\n";
			text += "①等级限制：#r" + minLevel + "#k - #r" + maxLevel + "#k级\t\t②人数限制：#r" + minPartySize + " #k- #r" + maxPartySize + " 队员#k\r\n";
			text += "#L1##r#v4030011#开始组队副本#l   #L2##r#v4030010#副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n";
			text += "       #L3##r我有#v4002001#兑换#v1022073##z1022073##b#l\r\n\r\n";
			//text += "   #L4##r#v4001324#领取高级玩具带新人奖励#k(#b经验#k)\r\n\r\n#l"//3
			//text += "         #L5##e高级玩家带新人说明#k\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
	cm.removeAll(4001022);
	cm.removeAll(4001023);
        if (!cm.isLeader()) { // Not Party Leader
	   cm.sendSimple("如果你想做任务，请 #b队长#k 跟我谈."); 
	   cm.dispose();
	} else if (cm.getParty() == null) { // No Party
	   cm.sendSimple("你貌似没有达到要求...:\r\n\r\n#r玩家成员要求: " + minPartySize + " , 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".");
	   cm.dispose();
	} else {
		cm.getMap(922010401).resetFully();
		cm.getMap(922010402).resetFully();
		cm.getMap(922010403).resetFully();
		cm.getMap(922010404).resetFully();
		cm.getMap(922010405).resetFully();
		cm.spawnMobOnMap(9300008,1,-65,-1578,922010401);
		cm.spawnMobOnMap(9300008,1,335,131,922010402);
		cm.spawnMobOnMap(9300014,1,-610,177,922010403);
		cm.spawnMobOnMap(9300014,1,-669,189,922010404);
		cm.spawnMobOnMap(9300014,1,-610,177,922010405);
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
		var em = cm.getEventManager("LudiPQ");
		if (em == null) {
		    cm.sendSimple("找不到脚本请联络GM#b\r\n");//#L0#我要兑换有裂痕的眼镜#l
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.removeAll(4001022);
			cm.removeAll(4001023);
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("其他队伍已经在里面做 #r组队任务了#k 请尝试换频道或者等其他队伍完成。");//#b\r\n#L0#我要兑换有裂痕的眼镜#l
		    cm.dispose();
			}
		}
	    } else {
		cm.sendSimple("你的队伍貌似没有达到要求.\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".");//#b\r\n#L0#我要兑换有裂痕的眼镜#l
		cm.dispose();
	    }
	cm.dispose();
    }		
        } else if (selection == 2) {
		if (cm.getMeso() >= maxjinbi){//判断多少金币
        cm.gainMeso(- maxjinbi );//扣除多少金币
	    cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "[" + fbmc + "] 求组队一起完成",11);
        cm.dispose();
        }else{
        cm.sendOk("你的冒险币不足" + maxjinbi + "。无法发送征集令");
        cm.dispose();
					}
        } else if (selection == 5) {
            if (cm.haveItem(4001316, 100) ){
		        cm.gainItem(4001316, -100);
                cm.dispose();
            }else{
                cm.sendOk(" #r说明:#k\r\n\r\n等级到达#b71级#k的玩家带领新人完成#b玩具组队任务副本#k\r\n即可获得#v4001324##z4001324# 一枚\r\n#v4001324#可以找#b玩具组队任务NPC#k兑换自己等级相应的#b经验#k和#b抵用卷#k奖励");
                cm.dispose();					
		 }					
        } else if (selection == 4) {
            if (cm.haveItem(4001324, 1) ){
		        cm.gainItem(4001324, -1);
                cm.sendOk("恭喜您获得了奖励!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 带领新人完成玩具组队副本获得了奖励 ! ");//公告							
                cm.gainExp(cm.getPlayer().getLevel()*cm.getPlayer().getLevel()*20);
			    cm.getPlayer().modifyCSPoints(2, +30, true);//给抵用卷
                cm.dispose();
            }else{
                cm.sendOk("你的物品不足!带领新人完成副本可以获得#v4001324##z4001324#!");
                cm.dispose();			
        }					
        } else if (selection == 3) {	
	if (cm.haveItem(4002001, 20)) {//判断猪物品
	cm.gainItem(4002001, -20);//扣除猪物品
		cm.gainItem(1022073, 1);
	    cm.sendOk("做好了。");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]完成了玩具副本20次成功兑换了划痕眼镜,么么哒!");		
		}else{
		cm.sendOk("你没有20个#v4002001##r#z4002001#!");
		cm.dispose();
			}		
	}
    }
}





