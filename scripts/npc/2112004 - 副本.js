/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status = 0;

var fbmc = "绯红组队副本";//副本名称

var eventname = "CWKPQ";//副本配置文件

var maxjinbi = 50000;//判断征集令金币

var minLevel = 50;
var maxLevel = 200;//等级设置

var minPartySize = 1;
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

            cm.sendOk("欢迎下次再来挑战！");
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
			if (cm.getMapId() == 610030100) { //inside orbis pq
	    if (status == 0) {
		cm.sendNext("哎，让我告诉你真正的快：他们已经抓住了我们。主人的守护者将在大约一分钟内来到这里。我们最好快点。 ");
	    } else if (status == 1) {
		cm.sendNext("门的主人是扭曲了。我们必须寻找一种替代的方式，一个将带我们通过许多死亡陷阱。");
	    } else if (status == 2) {
		cm.sendNext("可以在这里找到一个入口…你最好找到它，快。我会赶上。 ");
		cm.dispose();
		em.setProperty("glpq1", "1");
		return;
	}
			if (cm.getMapId() == 610030200) { //inside orbis pq
	   if (status == 0) {
		cm.sendNext("这是一个成功！现在，这一路，我相信我们需要一个每个冒险家班拿过去。");
	   } else if (status == 1) {
		cm.sendNext("他们需要使用他们的技能在这些东西叫做印记。一旦所有五个已经完成，我们可以得到过去。 ");
		cm.dispose();
		return;
	}
	if (cm.getMapId() == 610030300) { //inside orbis pq
	   if (status == 0) {
		cm.sendNext("现在我们这里有更多的印记。五的冒险者们爬上，穿过门。 ");
	   } else if (status == 1) {
		cm.sendNext("当心这些死亡陷阱，他们真的打了一拳。 ");
		cm.dispose();
		return;
	}
	if (cm.getMapId() == 610030400) { //inside orbis pq
	   if (status == 0) {
		cm.sendNext("现在我们这里有更多的印记。然而，他们中的一些人不工作。");
	   } else if (status == 1) {
		cm.sendNext("但他们只是分心。试试这些印记每个人直到他们的工作。");
		cm.dispose();
		return;
	   }
	if (cm.getMapId() == 610030500) { //inside orbis pq
	   if (status == 0) {
		cm.sendNext("很惊讶你做了这么远！你在这里看到的是雕像，但它没有任何的武器。 ");
	   } else if (status == 1) {
		cm.sendNext("有五个房间，每一个雕像附近有一个雕像，雕像周围。 ");
	   } else if (status == 2) {
		cm.sendNext("我怀疑，这些房间里的每一个都有一个雕像的五个武器。");
	   } else if (status == 3) {
		cm.sendNext("带回武器，并恢复他们掌握的遗物！");
		cm.dispose();
		return;
	}
	if (cm.getMapId() == 610030700) { //inside orbis pq
	   cm.sendNext("那是一些很好的工作！这通向扭曲大师的军械库");
	   cm.dispose();
		return;
	}
   } else if (selection == 1) {
		cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
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
		var em = cm.getEventManager("CWKPQ");
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
		cm.sendSimple("你的队伍貌似没有达到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".");
	    }
		cm.dispose();
	}
        } else if (selection == 2) {
		if (cm.getMeso() >= maxjinbi){//判断多少金币
        cm.gainMeso(- maxjinbi );//扣除多少金币
	    cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "["+ fbmc +"]需要勇士一起完成",11);
        cm.dispose();
        }else{
        cm.sendOk("你的冒险币不足" + maxjinbi + "。无法发送征集令");
        cm.dispose();
					}	
	}
    }
	}

