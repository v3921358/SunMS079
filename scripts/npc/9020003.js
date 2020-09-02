importPackage(java.lang);

var status = 0;
var minLevel = 50; // GMS = 50 
var maxLevel = 250; // GMS = 200? recommended 50 - 69
var minPlayers = 1; // GMS = 3
var maxPlayers = 6; // GMS = 4 || but 6 makes it better :p
var open = false; //open or not
var PQ = 'KentaPQ';

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (status >= 1 && mode == 0) {
        cm.sendOk("问问你的朋友加入你的队伍。您可以使用甲方搜索功能（快捷键GO）在任何地方找到一个政党，任何时候。");
        cm.dispose();
        return;
    }
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
    if (cm.getPlayer().getMapId() != 910002000) { // not in pq lobby
        cm.sendSimple("你真的想立即离开这个地图，并且必须从头开始，如果你想再试一次?#b\r\n#L0#是的！让我离开这里..#l");
    } else if (cm.getPlayer().getMapId() == 220000306 || cm.getPlayer().getMapId() == 910002000) {
        cm.sendSimple("#e <党的任务：健太危险>#n \r\n唉，可怜健太！你必须帮助他。他听说做了一些海洋生物，其中表现奇怪的，就去看看。他还没有回来呢，和我越来越担心。我们mustFind健太。你能帮助我们? #b\r\n#L1#I will go find Kenta.#l\r\n#L3#我想有健太的新的Goggles.#l\r\n#L2#Are there any other details?#l\r\n#L5#How many more times can I try today?#l#k");
    } else {
        cm.dispose();
    }
} else if (status == 1) {
    if (selection == 0) {
        cm.saveLocation("MULUNG_TC");
        cm.warp(910002000,0);
        cm.dispose();
    } else if (selection == 1) {
     if (cm.getParty() == null) { // No Party
        cm.sendYesNo("您需要创建一个第三方完成党的任务。你要使用的搜索党funtion?");
    } else if (!cm.isLeader()) { // Not Party Leader
        cm.sendOk("它是由您的党的领导进行.");
        cm.dispose();
    } else if (cm.getPQLog(PQ) >= 10){
        cm.sendOk("对不起......你试过今天10次已..");
        cm.dispose();
    } else if (!cm.allMembersHere()) {
        cm.sendOk("你的一些党员在不同的地图。请重试一次，大家在一起.");
        cm.dispose();
    } else {
    // Check if all 队员 are over lvl 50
    var party = cm.getParty().getMembers();
    var mapId = cm.getMapId();
    var next = true;
    var levelValid = 0;
    var inMap = 0;
    var it = party.iterator();
    while (it.hasNext()) {
        var cPlayer = it.next();
        if (cPlayer.getLevel() >= minLevel && cPlayer.getLevel() <= maxLevel) {
            levelValid += 1;
        } else {
            cm.sendOk("你需要的水平之间 " + minLevel + " 和 " + maxLevel + " 采取这个史诗般的挑战!");
            cm.dispose();
            next = false;
        } 
        if (cPlayer.getMapid() == mapId) {
            inMap += 1;
        }
    }
    if (party.size() > maxPlayers || inMap < minPlayers) {
        next = false;
    }
    if (next) {
        var em = cm.getEventManager("Kenta");
        if (em == null || open == false) {
            cm.sendSimple("这PQ当前不可用.");
            cm.dispose();
        } else {
            var prop = em.getProperty("state");
            if (prop == null || prop.equals("0")) {
                em.startInstance(cm.getParty(),cm.getMap(), 70);
            } else {
                cm.sendSimple("有人正在尝试任务。 请等待他们完成，或找到另一个频道.");
            }
            cm.removeAll(4001453);
            cm.setPQLog(PQ);
            cm.dispose();
        } 
    } else {
        cm.sendYesNo("你的党不是之间的一方 " + minPlayers + " 和 " + maxPlayers + " 队员。 请回来，当你有之间 " + minPlayers + " 和 " + maxPlayers + " 队员.");
    } 
}
} else if (selection == 2) {
    cm.sendNext("Kenta was researching sea creatures through samples he got from Explorers, but that was only effective for a while. When his research needed to go further, he decided to go into the Dangerous Sea Areas to conduct direct research. I haven't heard from him since he left... he must be in trouble.#b\r\n#L12#Go on.#l#k");
        } else if (selection == 3) {
            cm.sendNext("Oh, so you want #v1022123:# kenta's Goggles, do you? Kenta's Goggles is a gift for people who have helped with sea life research. If you bring about #b 100 Pianus Scales#k for research, I will give you the gift. You can obtain a Pianus Scale from eliminating Pianus. If 100 Pianus Scales are to much for you, then just bring #b 10 Pianus Scales#k to get a Pet Equipment Scroll. Good luck!");
         } else if (selection == 4) {
        } else if (selection == 5) {
            var pqtry = 10 - cm.getPQLog(PQ);
            cm.sendOk("You can do this quest " + pqtry + " time(s) today.");
            cm.dispose();
        }
    }else if (status == 2) {
       if (selection == 12) {
        cm.sendNextPrev("Please find Kenta, and be careful! The area is very dangerous.\r\nHere's what you can expect:\r\n \r\n1. Eliminate any enraged sea creatures on your way to find Kenta.\r\n2. Kenta has been gone for a long time, so he might not have enough air. Obtain some Air Bubbles for him.\r\n3. When you find Kenta, protect him from the enraged sea creatures.\r\n4. Lastly, if Kenta insist on finishing his research, help him do it and return safely.");
        cm.dispose();
    }else{
        cm.sendSimple("Would you like to use Pianus Scales for Kenta's Research?\r\n#b#L10#10 Pianus Scales - Pet related Scrolls#l\r\n#L11#100 Pianus Scales - #v1022123:# Kenta's Goggles#l#k");
    }
   }
   else if (status == 3) { 
       if (selection == 10) {
        if (!cm.canHold(1032100,1)) {
            cm.sendOk("Please make some room for the scroll.");
        }else if (cm.haveItem(4001535,10)) {
            cm.gainItem(2048030, 1);//random pet scroll? //dex atm
            cm.gainItem(4001535, -10);
            cm.sendOk("谢谢你。 Enjoy");
            cm.dispose();
        }else{
            cm.sendOk("Check to see if you really have Pianus Scales.");
            cm.dispose();
        }  
    } else if (selection == 11) {
        if (!cm.canHold(1072510,1)) {
            cm.sendOk("Please make some room in your EQUIP for these Googles.");
        }else if (cm.haveItem(4001535,100)) {
            cm.gainItem(1022123, 1);//Googles
            cm.gainItem(4001535, -100);
            cm.sendOk("谢谢你。 Enjoy");
            cm.dispose();
        }else{
            cm.sendOk("Check to see if you really have Pianus Scales.");
            cm.dispose();
        }
    }else{
      cm.sendOk("error?");
      cm.dispose();  
  }
}       else if (mode == 0) { 
    cm.dispose();
} 
}