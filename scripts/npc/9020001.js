/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

importPackage(Packages.tools);
importPackage(java.awt);

var status;
var curMap;
var random = java.lang.Math.floor(Math.random() * 9 + 1);
var random1 = java.lang.Math.floor(Math.random() * 20000 + 1);
var random2 = java.lang.Math.floor(Math.random() * 19 + 1);
var questions = Array("第一个问题：转职成战士的最低等级是多少？#b\r\n（打倒怪物，获取相应数量的证书。）",
    "第一个问题：转职成战士的最低力量值（SEX）是多少？#b\r\n（打倒怪物，获取相应数量的证书。）",
    "第一个问题：转职成魔法师的最低智力值（INT）是多少？#b\r\n（打倒怪物，获取相应数量的证书。）",
    "第一个问题：转职成弓箭手的最低敏捷值（DEX）是多少？#b\r\n（打倒怪物，获取相应数量的证书。）",
    "第一个问题：转职成飞侠的最低敏捷值（DEX）是多少？#b\r\n（打倒怪物，获取相应数量的证书。）",
    "第一个问题：等级1 ～ 等级2 所需的经验值是多少？#b\r\n（打倒怪物，获取相应数量的证书。）");
var qanswers = Array(10, 35, 20, 25, 25, 15);
var party;
var preamble; // we dont even need this mother fucker ! --
var stage2Rects = Array(Rectangle(-770,-132,28,178),Rectangle(-733,-337,26,105),Rectangle(-601,-328,29,105),Rectangle(-495,-125,24,165));
//var stage2Rects = Array(Rectangle(-755,-132,4,218),Rectangle(-721,-340,4,166),Rectangle(-586,-326,4,150),Rectangle(-483,-181,4,222));
var stage3Rects = Array(Rectangle(608,-180,140,50),Rectangle(791,-117,140,45),
    Rectangle(958,-180,140,50),Rectangle(876,-238,140,45),
    Rectangle(702,-238,140,45));
var stage4Rects = Array(Rectangle(910,-236,35,5),Rectangle(877,-184,35,5),
    Rectangle(946,-184,35,5),Rectangle(845,-132,35,5),
    Rectangle(910,-132,35,5),Rectangle(981,-132,35,5));
var stage2combos = Array(Array(0,1,1,1),Array(1,0,1,1),Array(1,1,0,1),Array(1,1,1,0));
var stage3combos = Array(Array(0,0,1,1,1),Array(0,1,0,1,1),Array(0,1,1,0,1),
    Array(0,1,1,1,0),Array(1,0,0,1,1),Array(1,0,1,0,1),
    Array(1,0,1,1,0),Array(1,1,0,0,1),Array(1,1,0,1,0),
    Array(1,1,1,0,0));
var stage4combos = Array(Array(0,0,0,1,1,1),Array(0,0,1,0,1,1),Array(0,0,1,1,0,1),
    Array(0,0,1,1,1,0),Array(0,1,0,0,1,1),Array(0,1,0,1,0,1),
    Array(0,1,0,1,1,0),Array(0,1,1,0,0,1),Array(0,1,1,0,1,0),
    Array(0,1,1,1,0,0),Array(1,0,0,0,1,1),Array(1,0,0,1,0,1),
    Array(1,0,0,1,1,0),Array(1,0,1,0,0,1),Array(1,0,1,0,1,0),
    Array(1,0,1,1,0,0),Array(1,1,0,0,0,1),Array(1,1,0,0,1,0),
    Array(1,1,0,1,0,0),Array(1,1,1,0,0,0));	
var eye = 9300002;
var necki = 9300000;
var slime = 9300003;
var monsterIds = Array(eye, eye, eye, necki, necki, necki, necki, necki, necki, slime);
var prizeIdScroll = Array(2040502, 2040505,					// Overall DEX and DEF
    2040802,										// Gloves for DEX
    2040002, 2040402, 2040602);						// Helmet, Topwear and Bottomwear for DEF
var prizeIdUse = Array(2000001, 2000002, 2000003, 2000006,	// Orange, White and Blue Potions and Mana Elixir
    2000004, 2022000, 2022003);						// Elixir, Pure Water and Unagi
var prizeQtyUse = Array(50, 50, 50, 30,
    5, 10, 10);
var prizeIdEquip = Array(1032004, 1032005, 1032009,			// Level 20-25 Earrings
    1032006, 1032007, 1032010,						// Level 30 Earrings
    1032002,										// Level 35 Earring
    1002026, 1002089, 1002090);						// Bamboo Hats
var prizeIdEtc = Array(4010000, 4010001, 4010002, 4010003,	// Mineral Ores
    4010004, 4010005, 4010006,						// Mineral Ores
    4020000, 4020001, 4020002, 4020003,				// Jewel Ores
    4020004, 4020005, 4020006,						// Jewel Ores
    4020007, 4020008, 4003000);						// Diamond and Black Crystal Ores and Screws
var prizeQtyEtc = Array(10, 10, 10, 10,
    8, 8, 8,
    8, 8, 8, 8,
    8, 8, 8,
    3, 3, 10);
		
function start() {
    status = -1;
    curMap = cm.getPlayer().getMapId() - 103000799;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (type == 0 && mode == 0)
        status--;
    else {
        cm.dispose();
        return;
    }
    if (curMap == 1) { // First Stage.
        if (cm.isLeader()) {
            var eim = cm.getPlayer().getEventInstance();
            party = eim.getPlayers();
            preamble = eim.getProperty("leader1stpreamble");
            if (preamble == null) {
                cm.sendNext("你好，欢迎来到第一个阶段，在这里你可能会考到很多凶狠的鳄鱼，跟我对话，我会给你们每一个人出一道题目，你们再打倒凶狠的鳄鱼获取相应数目的证书卡交给我，就行了。之后我会给你们一张通行证，你们把通行证全部交给组队长，组队长再和我讲话，就可以顺利通关了，那么祝你一切顺利！");
                eim.setProperty("leader1stpreamble","done");
                cm.dispose();
            } else {
				var complete = eim.getProperty(curMap.toString() + "stageclear");
				if (complete != null) {
                    cm.sendNext("请赶快到下一个阶段，传送门已经打开!");
                    cm.dispose();
                } else {
                    var numpasses = party.size() - 1; // All the players in the party need to get a pass besides the leader.
                    var strpasses = "#b" + numpasses + " 张#k";
                    if (!cm.haveItem(4001008, numpasses)) {
                        cm.sendNext("抱歉，您并没有足够的#v4001008#。\r\n你需要给我正确的数量:#r " + numpasses + " #k张\r\n请告诉你的队员解决问题，收集了#v4001008#，并让他们给到你");
                        cm.dispose();
                    } else {
                        cm.sendNext("你收集了 " + strpasses + "#v4001008#! 恭喜！我会让发送你下一阶段的门户。有一个关于获得有时间限制，所以请抓紧时间。祝您好运给大家！");
                        clear(1, eim, cm);
                        cm.givePartyExp(2000, party);
                        cm.gainItem(4001008, -numpasses);
                        cm.dispose();
                    // TODO: Make the shiny thing flash
                    }
                }
            }
        } else { // Not leader
            var eim = cm.getPlayer().getEventInstance();
            pstring = "member1stpreamble" + cm.getPlayer().getId();
            preamble = eim.getProperty(pstring);
            if (status == 0) {
                if (preamble == null) {
                    var qstring = "member1st" + cm.getPlayer().getId();
                    var question = eim.getProperty(qstring);
                    if (question == null) {
                        // Select a random question to ask the player.
                        var questionNum = Math.floor(Math.random() * questions.length);
                        eim.setProperty(qstring, questionNum);
                    }
                    cm.sendNext("好了，你需要收集#b相应数目#k的证书给我。\r\n在这里，你需要击败相同数量的鳄鱼作为答案单独提出的问题收集#v4001007# .");
                } else { // Otherwise, check for stage completed
                    var complete = eim.getProperty(curMap + "stageclear");
                    if (complete != null) { // Strage completed
                        cm.sendNext("现在可以到下一个关卡了，如果不快点的话，门可能就关闭了。");
						//cm.sendNext("Please hurry on to the next stage, the portal is open!");
                        cm.dispose();
                    } else {
                        // Reply to player correct/incorrect response to the question they have been asked
                        var qstring = "member1st" + cm.getPlayer().getId();
                        var numcoupons = qanswers[parseInt(eim.getProperty(qstring))];
                        var qcorr = cm.itemQuantity(4001007);
						var enough = false;
						var passed = eim.getProperty(cm.getPlayer().getId().toString() + "passed");
                        if (passed == 1) { 
                            cm.sendOk("我已经给过你了通行证，请帮助其它队友吧");
                            cm.dispose();
							//判断交过任务不能兑换
                        } else if (numcoupons == qcorr) {
							 cm.sendOk("祝贺你，得到通行证#v4001008#1张，请把通行证交给队长之后，帮助队友吧！.");
							cm.gainItem(4001007, -numcoupons);
                            cm.gainItem(4001008, 1);
							enough = true;
							eim.setProperty(cm.getPlayer().getId().toString() + "passed", 1);
								}
							}
							if (!enough) {
								var questionstring = "member1st" + cm.getPlayer().getId().toString();
								var thequestion = parseInt(eim.getProperty(qstring));
								cm.sendNext("你给我的证书数量和答案不对应，你的问题是：\r\n" + questions[thequestion]);
							}
							cm.dispose();
						//}
					}
            } else if (status == 1) {
                if (preamble == null) {
                    var qstring = "member1st" + cm.getPlayer().getId();
                    var question = parseInt(eim.getProperty(qstring));
                    cm.sendNextPrev(questions[question]);
                } else { // Shouldn't happen, if it does then just dispose
                    cm.dispose();
                }
            } else if (status == 2) { // Preamble completed
                eim.setProperty(pstring,"done");
                cm.dispose();
            }
        } // End first map scripts
    } else if (2 <= curMap && 4 >= curMap) {
        rectanglestages(cm);
    } else if (curMap == 5) { // Final stage
        var eim = cm.getPlayer().getEventInstance();
        var stage5done = eim.getProperty("5stageclear");
        if (stage5done == null) {
            if (cm.isLeader()) { // Leader
                if (cm.haveItem(4001008, 10)) {
                    // Clear stage
                    cm.sendNext("这里可以通过最后一个关卡，这里有很多凶猛的怪物，我衷心祝福你和你的组队能通过这项挑战。");
						//cm.sendNext("Here's the portal that leads you to the last, bonus stage. It's a stage that allows you to defeat regular monsters a little easier. You'll be given a set amount of time to hunt as much as possible, but you can always leave the stage in the middle of it through the NPC. Again, congratulations on clearing all the stages. Take care...");
                    party = eim.getPlayers();
                    cm.gainItem(4001008, -10);
                    clear(5, eim, cm);
                    cm.givePartyExp(6000, party);
                    cm.dispose();
                } else { // Not done yet
                 	cm.sendNext("你好，欢迎来到第#r5#k阶段，到处走走，可能会发现很多凶猛的怪物，打败它们，获取通行证，再把他们交给我。记住，怪物可能比你强大很多，请小心一点，祝你通过这一关。");
					//   cm.sendNext("Hello. Welcome to the 5th and final stage. Walk around the map and you'll be able to find some Boss monsters. Defeat all of them, gather up #bthe passes#k, and please get them to me. Once you earn your pass, the leader of your party will collect them, and then get them to me once the #bpasses#k are gathered up. The monsters may be familiar to you, but they may be much stronger than you think, so please be careful. Good luck!\r\nAs a result of complaints, it is now mandatory to kill all the Slimes! Do it!");
                }
                cm.dispose();
            } else { // Members
                //cm.sendNext("Welcome to the 5th and final stage.  Walk around the map and you will be able to find some Boss monsters.  Defeat them all, gather up the #bpasses#k, and give them to your leader.  Once you are done, return to me to collect your reward.");
                cm.sendNext("欢迎来到第#r5#k阶段，在地图上走走，你就会看见许多凶猛的怪物，打败他们获取他们身上的通行证，交给你们的组队长。");
					cm.dispose();
            }
        } else { // Give rewards and warp to bonus
            if (status == 0) {
                cm.sendNext("不敢相信！你和你的组队员们终于完成了所有挑战！做为奖励，我将送你一些东西，请确保你的消耗栏、其它栏、装备栏是否有一个栏目以上的空格？");
            } else if (status == 1) {
                getPrize(eim,cm);
                cm.dispose();
            }
        }
    } else { // No map found
        cm.sendNext("无效的地图，这意味着阶段是不完整的.");
        cm.dispose();
    }
}

function clear(stage, eim, cm) {
	eim.setProperty(stage.toString() + "stageclear","true");
   // eim.setProperty(stage + "stageclear", "true");
    var map = eim.getMapInstance(cm.getPlayer().getMapId());
   // map.broadcastMessage(MaplePacketCreator.showEffect("quest/party/clear"));
    //map.broadcastMessage(MaplePacketCreator.playSound("Party1/Clear"));
   // map.broadcastMessage(MaplePacketCreator.environmentChange("gate", 2));
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
    var mf = eim.getMapFactory();
    map = mf.getMap(103000800 + stage);
    var nextStage = eim.getMapInstance(103000800 + stage);
    var portal = nextStage.getPortal("next00");
    if (portal != null) {
		//portal.setScriptName("kpq" + (stage+1).toString());
        portal.setScriptName("kpq" + stage);
    }
}

function failstage(eim, cm) {
    var map = eim.getMapInstance(cm.getPlayer().getMapId());
    cm.showEffect(true, "quest/party/wrong_kor");
    cm.playSound(true, "Party1/Failed");
   // map.broadcastMessage(MaplePacketCreator.showEffect("quest/party/wrong_kor"));
   // map.broadcastMessage(MaplePacketCreator.playSound("Party1/Failed"));
}

function rectanglestages (cm) {
    var eim = cm.getPlayer().getEventInstance();
    var nthtext;
    var nthobj;
    var nthverb;
    var nthpos;
    var curArray;
    var curCombo;
    var objset;
    if (curMap == 2) {
        nthtext = "2";
        nthobj = "ropes";
        nthverb = "hang";
        nthpos = "hang on the ropes too low";
        curArray = stage2Rects;
        curCombo = stage2combos;
        objset = [0,0,0,0];
    } else if (curMap == 3) {
        nthtext = "3";
        nthobj = "platforms";
        nthverb = "stand";
        nthpos = "stand too close to the edges";
        curArray = stage3Rects;
        curCombo = stage3combos;
        objset = [0,0,0,0,0];
    } else if (curMap == 4) {
        nthtext = "4";
        nthobj = "barrels";
        nthverb = "stand";
        nthpos = "stand too close to the edges";
        curArray = stage4Rects;
        curCombo = stage4combos;
        objset = [0,0,0,0,0,0];
    }
    if (cm.isLeader()) { // Check if player is leader
        if (status == 0) {
            party = eim.getPlayers();
            preamble = eim.getProperty("leader" + nthtext + "preamble");
            if (preamble == null) { // first time talking.
                cm.sendNext("欢迎来到第#r " + nthtext + "#k 阶段。 在我旁边你可能会看到很多绳子，让三名组队成员爬上绳子，找到正确的位置，就可以通过此关卡!");
                eim.setProperty("leader" + nthtext + "preamble","done");
                var sequenceNum = Math.floor(Math.random() * curCombo.length);
                eim.setProperty("stage" + nthtext + "combo", sequenceNum.toString());
                cm.dispose();
            } else {
                var complete = eim.getProperty(curMap + "stageclear");
                if (complete != null) {
                    cm.sendNext("请赶快到下一个阶段，传送门已经打开!");
                    cm.dispose();
                } else { // Check for people on ropes and their positions
                    var playersOnCombo = 0;
                    for (var i = 0; i < party.size(); i++) {
                        for (var y = 0; y < curArray.length; y++) {
                            if (curArray[y].contains(party.get(i).getPosition())) {
                                playersOnCombo++;
                                objset[y] = 1;
                                break;
                            }
                        }
                    }
                   // if (playersOnCombo == 3 || cm.getPlayer().gmLevel() > 0) {
                    if (playersOnCombo == 3) {
                        var combo = curCombo[parseInt(eim.getProperty("stage" + nthtext + "combo"))];
                        var correctCombo = true;
                        for (i = 0; i < objset.length && correctCombo; i++)
                            if (combo[i] != objset[i])
                                correctCombo = false;
                      //  if (correctCombo || cm.getPlayer().gmLevel() > 0) {
                        if (correctCombo) {
                            clear(curMap, eim, cm);
                            //var exp = (Math.pow(10, curMap) * 50);
							
                            var exp = 0;
							if(curMap == 2){
								exp = 2000;
							}else if(curMap == 3){
							exp = 3000;
							}else if(curMap == 4){
							exp = 5000;
							}
                            cm.givePartyExp(exp, party);
                            cm.dispose();
                        } else { // Wrong
                            failstage(eim, cm);
                            cm.dispose();
                        }
                    } else {
                        cm.sendNext("你看起来好像没有发现正确的位置，别气馁，让组队成员在绳子上找到正确的位置。\r\n看起来你还没有发现合适的组合。请想想不同组合 . 需#r3人#k同时爬在绳子上, 如果不足3人员将无法完成本阶段，所以请记住这一点。 请继续加油!");
                        cm.dispose();
                    }
                }
            }
        } else {
            var complete = eim.getProperty(curMap + "stageclear");
            if (complete != null) {
                var target = eim.getMapInstance(103000800 + curMap);
                var targetPortal = target.getPortal("st00");
                cm.getPlayer().changeMap(target, targetPortal);
            }
            cm.dispose();
        }
    } else { // Not leader
        var complete = eim.getProperty(curMap.toString() + "stageclear");
        if (complete != null) {
            cm.sendNext("时间不多了，请快点到达下一个关卡。");
        } else {
            cm.sendNext("请让你的组队长和我谈话。");
        }
        cm.dispose();
    }
}

function getPrize(eim,cm) {
    var itemSetSel = Math.random();
    var itemSet;
    var itemSetQty;
    var hasQty = false;
	var ItemName;
	var item;
    if (itemSetSel < 0.3)
	itemSet = prizeIdScroll;
    else if (itemSetSel < 0.6)
	itemSet = prizeIdEquip;
    else if (itemSetSel < 0.9) {
	itemSet = prizeIdUse;
	itemSetQty = prizeQtyUse;
	hasQty = true;
    } else {
	itemSet = prizeIdEtc;
	itemSetQty = prizeQtyEtc;
	hasQty = true;
    }
    var sel = Math.floor(Math.random()*itemSet.length);
    var qty = 1;
    if (hasQty)
	qty = itemSetQty[sel];
	ItemName = cm.getItemName(itemSet[sel]);
	item = new Packages.client.inventory.Item(itemSet[sel], 0, qty, 0);
	if(cm.getLevel() >= 20){//判断等级
    cm.gainGetItemMegas1(itemSet[sel], qty);
    cm.gainItem(4002000, 1);//邮票
    cm.getPlayer().setOneTimeLog("feiqi");
    cm.removeAll(4001007);
    cm.removeAll(4001008);
    cm.getPlayer().endPartyQuest(1201);
	var msg = "x"+qty+" 恭喜玩家-"+ cm.getPlayer().getName() + "-完成废弃组队副本,大家恭喜他们吧!";
	} else {
    cm.gainGetItemMegas1(itemSet[sel], qty);
    cm.getPlayer().setOneTimeLog("feiqi");
    cm.removeAll(4001007);
    cm.removeAll(4001008);
    cm.getPlayer().endPartyQuest(1201);
	var msg = "x"+qty+" 恭喜玩家-"+ cm.getPlayer().getName() + "-完成废弃组队副本,大家恭喜他们吧!";
	if(itemSet[sel] / 1000 == 1032){
		World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("『废弃组队』 ", " : " + msg, item,  0, cm.getPlayer().getClient().getChannel()).getBytes());
	} 
	} 
	cm.warp(103000805);
	eim.restartEventTimer(120000);//给副本新时间
		cm.dispose();
	}
		
	
