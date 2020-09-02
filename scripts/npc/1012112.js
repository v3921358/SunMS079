/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var fbmc = "射手村公园-(月妙副本)";//副本名称
var minLevel = 8;
var maxLevel = 200;
var minPartySize = 2;
var maxPartySize = 6;
var maxjinbi = 100;//判断征集令金币
var eventname = "HenesysPQ";//副本配置文件


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) status++;
        else status--;
        if (status == 0) {
            cm.sendSimple("#k\t\t    #r#v4030001#" + fbmc + "#r#v4030001##k\r\n#b副本要求:#k\r\n①人数限制:#r " + minPartySize + " #b- #r" + maxPartySize + "#k 队员\t  ②等级限制：#r " + minLevel + " #b- #r" + maxLevel + "级 #k\r\n#r#L0##v4030011#开始组队副本#l\t#L2##r#v4030010#副本征集令#k" + maxjinbi + "金币/次#l\r\n\r\n          #L3##r我要兑换#v1002798##z1002798##l ")
        } else if (status == 1) {
            if (selection == 0) {
                if (cm.getParty() == null) { // 没有组队
                    cm.sendOk("请组队后和我谈话。");
                    cm.dispose();
                } else if (!cm.isLeader()) { // 不是队长
                    cm.sendOk("请叫队长和我谈话。");
                    cm.dispose();
                } else {
					cm.givePartyItems(4001095,-1,true);
					cm.givePartyItems(4001096,-1,true);
					cm.givePartyItems(4001097,-1,true);
					cm.givePartyItems(4001098,-1,true);
					cm.givePartyItems(4001099,-1,true);
					cm.givePartyItems(4001100,-1,true);
                    var party = cm.getParty().getMembers();
                    var mapId = cm.getPlayer().getMapId();
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
                            inMap += 1;
                        }
                    }
                    if (party.size() < minPartySize || party.size() > maxPartySize || inMap < minPartySize) {
                        next = false;
                    }
                    if (next) {
                        var em = cm.getEventManager("HenesysPQ");
                        if (em == null) {
                            cm.sendOk("此任务正在建设当中。");
                        } else {
                            var prop = em.getProperty("state");
                            if (prop.equals("0") || prop == null) {
                                em.startInstance(cm.getParty(), cm.getMap());
                                cm.removeAll(4001022);
                                cm.removeAll(4001023);
                                cm.dispose();
                                return;
                            } else {
                                cm.sendOk("任务正在进行中...请稍等!");
                            }
                        }
                        cm.dispose();
                    } else {
                        cm.sendOk("请确认你的组队员：\r\n\r\n#b1、组队员必须要" + minPartySize + "人以上，" + maxPartySize + "人以下。\r\n2、组队员等级必须要在" + minLevel + "级以上。\r\n\r\n（#r如果仍然错误, 重新下线,再登陆 或者请重新组队。#k#b）");
                        cm.dispose();
                    }
                } 
            } else if (selection == 1) {
                cm.sendOk("请确认你的组队员：\r\n\r\n#b1、组队员必须要" + minPartySize + "人以上，" + maxPartySize + "人以下。\r\n2、组队员等级必须要在" + minLevel + "级以上。\r\n\r\n（#r如果仍然错误, 重新下线,再登陆 或者请重新组队。#k#b）");
                cm.dispose();
            } else if (selection == 2) {
		if (cm.getMeso() >= maxjinbi){//判断多少金币
        cm.gainMeso(- maxjinbi );//扣除多少金币
	    cm.laba(cm.getPlayer().getName() + " [征集令]" + " : " + "["+ fbmc +"] 求组队一起完成",11);
        cm.dispose();
        }else{
        cm.sendOk("你的冒险币不足" + maxjinbi + "。无法发送征集令");
        cm.dispose();
				}
		    } else if (selection == 3) {
		if(cm.getPlayer().getOneTimeLog("yuemiao1") >= 10){
			cm.sendOk("你已经兑换过10次#v1002798##r#z1002798#！");
            cm.dispose();
		}else if(cm.getPlayer().getOneTimeLog("yuemiao") <= 5){
			cm.sendOk("你挑战次数还没有满5次,所以无法兑换#v1002798##r#z1002798#！\r\n#k当前挑战了：#r"+cm.getPlayer().getOneTimeLog("yuemiao")+"#k次");
            cm.dispose();
		}else if (cm.getInventory(1).isFull(0)){
			cm.sendOk("#b请保证装备栏位至少有1个空格,否则无法兑换.");
			cm.dispose();		
			} else {
			cm.gainItem(1002798, 1);
            cm.sendOk("兑换成功！");
			cm.getPlayer().setOneTimeLog("yuemiao1");
			cm.worldMessage(6,"玩家：["+cm.getName()+"]完成了月妙副本5次成功兑换了年糕帽子,真社会!");
            cm.dispose();
				}
            }
        }
}
}