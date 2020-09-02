/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status;
var fbmc = "废弃都市-(废弃副本)";//副本名称
var minLevel = 21;
var maxLevel = 200;
var minPartySize = 3;
var maxPartySize = 6;
var maxjinbi = 5000;//判断征集令金币
var eventname = "KerningPQ";//副本配置文件

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
            text += "#k\t\t   #r#v4030001#" + fbmc + "#v4030001##k\r\n#b副本要求:#k\r\n①人数限制:#r " + minPartySize + " #b- #r" + maxPartySize + "#k 队员\t②等级限制：#r " + minLevel + " #b- #r" + maxLevel + "级 #k\r\n"//3
            text += "#L1##r#v4030011#开始组队副本#l  #L2##r#v4030010#副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n"//3
			text += "        #L3##r我要兑换兑换#v1072369##z1072369##b#l\r\n\r\n"
			//text += "   #L4##r#v4001323#领取高级玩具带新人奖励#k(#b经验#k)\r\n\r\n#l"//3
			//text += "         #L5##e高级玩家带新人说明#k\r\n"//3
            cm.sendSimple(text);
	} else if (selection == 1) {
        if (cm.getParty() == null) { // No Party
            cm.sendOk("你没有队伍无法进入！");
            cm.dispose();
        } else if (!cm.isLeader()) { // Not Party Leader
            cm.sendOk("请让你的队长和我说话~");
            cm.dispose();
        } else {
            var party = cm.getParty().getMembers();
            var inMap = cm.partyMembersInMap();
            var levelValid = 0;
            for (var i = 0; i < party.size(); i++) {
                if (party.get(i).getLevel() >= minLevel && party.get(i).getLevel() <= maxLevel)
                    levelValid++;
            }
            if (inMap < minPartySize || inMap > maxPartySize) {
                cm.sendOk("你的队伍人数不足"+minPartySize+"人.请把你的队伍人员召集到废弃都市在进入副本.");
                cm.dispose();
            } else if (levelValid != inMap) {
                cm.sendOk("请确保你的队伍里所有人员都在本地图，且最小等级在 "+minLevel+" 和 "+maxLevel+"之间.");
                cm.dispose();
            } else {
                var em = cm.getEventManager("KerningPQ");
                if (em == null) {
                    cm.sendOk("这台电脑是当前不可用.");
                //} else if (em.getProperty("KPQOpen").equals("true")) {
                } else {
        if (cm.getPlayerCount(103000800) <= 0 && cm.getPlayerCount(103000801) <= 0 && cm.getPlayerCount(103000802) <= 0 && cm.getPlayerCount(103000803) <= 0 && cm.getPlayerCount(103000804) <= 0) {
        cm.getMap(103000804).resetFully();
		cm.getMap(103000803).resetFully();
		cm.getMap(103000802).resetFully();
		cm.getMap(103000801).resetFully();
		cm.getMap(103000800).resetFully();
        /*cm.spawnMobOnMap(9300002,1,297,-2188,103000804);
        cm.spawnMobOnMap(9300002,1,433,-2192,103000804);
        cm.spawnMobOnMap(9300002,1,132,-2193,103000804);
		cm.spawnMobOnMap(9300000,1,-18,-1480,103000804);
		cm.spawnMobOnMap(9300000,1,80,-1486,103000804);
		cm.spawnMobOnMap(9300000,1,391,-1488,103000804);
		cm.spawnMobOnMap(9300000,1,247,-1485,103000804);
		cm.spawnMobOnMap(9300000,1,-111,-1475,103000804);
		cm.spawnMobOnMap(9300000,1,299,-1485,103000804);
		cm.spawnMobOnMap(9300003,1,162,-451,103000804);
        //var papuMap = pi.getMap(103000804);
		//pi.getPlayer().setbosslog(1);
        //pi.playPortalSE();
*/
			//}
				//em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
                em.startInstance(cm.getParty(), cm.getPlayer().getMap());
		} else {
                            cm.sendOk("请稍等...任务正在进行中.");
                        }
						// Capt. Lac Map
				//em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
                    // Begin the PQ.
                //    em.startInstance(cm.getParty(), cm.getPlayer().getMap());
                    // Remove Passes and Coupons GMS DOESNT DO THIS!!!
                    //party = cm.getPlayer().getEventInstance().getPlayers();
                    //cm.removeFromParty(4001008, party);
                    //cm.removeFromParty(4001007, party);
                  //  em.setProperty("KPQOpen" , "false");
                //} else {
                 //   cm.sendNext("There is already another party inside. Please wait !");
                }
                cm.dispose();
						 }
		 }
        } else if (selection == 5) {
            if (cm.haveItem(4001316, 100) ){
		        cm.gainItem(4001316, -100);
                cm.dispose();
            }else{
                cm.sendOk(" #r说明:#k\r\n\r\n等级到达#b50级#k的玩家带领新人完成#b废弃组队任务副本#k\r\n即可获得#v4001323##z4001323# 一枚\r\n#v4001323#可以找#b拉克里斯#k兑换自己等级相应的#b经验#k和#b抵用卷#k奖励");
                cm.dispose();					
		 }
        } else if (selection == 4) {
            if (cm.haveItem(4001323, 1) ){
		        cm.gainItem(4001323, -1);
                cm.sendOk("恭喜您获得了奖励!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 带领新人完成废弃组队副本获得了奖励 ! ");//公告							
                cm.gainExp(cm.getPlayer().getLevel()*cm.getPlayer().getLevel()*20);
			    cm.getPlayer().modifyCSPoints(2, +20, true);//给抵用卷
                cm.dispose();
            }else{
                cm.sendOk("你的物品不足!带领新人完成副本可以获得#v4001323##z4001323#!");
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
                            //cm.sendOk(".");
                //cm.dispose();
    }
			    } else if (selection == 3) {
		if(cm.getPlayer().getOneTimeLog("feiqi1") >= 100){
			cm.sendOk("你已经兑换过#v1072369##r#z1072369#！");
            cm.dispose();
		}else if(cm.getPlayer().getOneTimeLog("feiqi") <= 20){
			cm.sendOk("你挑战次数还没有满20次,所以无法兑换#v1072369##r#z1072369#！\r\n#k当前挑战了：#r"+cm.getPlayer().getOneTimeLog("feiqi")+"#k次");
            cm.dispose();
		}else if (cm.getInventory(1).isFull(0)){
			cm.sendOk("#b请保证装备栏位至少有1个空格,否则无法兑换.");
			cm.dispose();		
			} else {
			cm.gainItem(1072369, 1);
            cm.sendOk("兑换成功!");
			cm.getPlayer().setOneTimeLog("feiqi1");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]完成了废弃副本20次成功兑换了绿黏液鞋子,真社会!");
            cm.dispose();
	}
}}