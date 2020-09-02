importPackage(java.lang);

var status = 0;
var minLevel = 30;
var maxLevel = 250;
var minPlayers = 1; // GMS = 3
var maxPlayers = 6; // GMS = 4 || but 6 makes it better :p
var open = false; //open or not
var PQ = 'IcePQ';

function start() {
    status = -1;
    action(1, 0, 0);
}
   function action(mode, type, selection) {
    if (status >= 1 && mode == 0) {
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
		cm.sendSimple("#e <Party Quest: The Ice Knight's Curse>#n \r\n Shhh! The Ice Knight will find us if we are loud! If you get cursed, you'll end up like my friend. Do you want that? No? Then help my friend break his curse! #b\r\n#L0#Go to the Party Quest Lobby.")
	} else if (cm.getPlayer().getMapId() == 910002000) { // Normal
		cm.sendSimple("#e <Party Quest: The Ice Knight's Curse>#n \r\nShhh! The Ice Knight will find us if we are loud! If you get cursed, you'll end up like my friend. Do you want that? No? Then help my friend break his curse! \r\n#b#L1#Okay, I'll help your friend.#l\r\n#L3#I need more details.#l\r\n#L4#Look, I just want the Ice Knight's special item.#l#k");
    
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
     	cm.sendSimple("This place is surrounded by the mysterious aura of the full moon, so you can't enter by yourself. If you want to enter, your party leader must talk to me.");
     	cm.dispose();
    } else if (!cm.isLeader()) { // Not Party Leader
		cm.sendOk("由你的队伍组人决定.");
        cm.dispose();
	} else if (cm.getPQLog(PQ) >= 10){ // Done for today
        cm.sendOk("抱歉。 您已超过今天的最大尝试次数。 请明天回来.");
        cm.dispose();
    } else if (!cm.allMembersHere()) { // Check for members
    	cm.sendSimple("I'm sorry, but the party you're a member of does NOT consist of at least 2 members. Please adjust your party to make sure that your party consists of at least 2 members that are all at Level 30 or higher. Let me know when you're done.");
        cm.dispose();
    } else {

	// Check if all 队员 are over correct lvl
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
        cm.sendOk("你需要在等级大于 " + minLevel + " 和 " + maxLevel + " 才可以挑战!");
        cm.dispose();
		next = false;
    } 
	if (cPlayer.getMapid() == mapId) {
		inMap += 1; 
	}
	}
	if (party.size() > maxPlayers || inMap < minPlayers) {
	    next = false;
	}//check if all 队员 here i think
	if (next) {
	    var em = cm.getEventManager("IceKnightPQ");
	if (em == null || open == false) {
		cm.sendSimple("此任务脚本当前未打开.");
        cm.dispose();
	} else {
	var prop = em.getProperty("state");
	if (prop == null || prop.equals("0")) {
		em.startInstance(cm.getParty(),cm.getMap(), 70);
	} else {
		cm.sendSimple("有人正在尝试任务。 请等待他们完成，或找到另一个频道.");
	}
        cm.setPQLog(PQ);
        cm.dispose();
	} 
    } else { // Not correct lvl or members
	    cm.sendYesNo("你的队伍不是之间的一方 " + minPlayers + " 和 " + maxPlayers + " 队员。 请回来，当你有之间 " + minPlayers + " 和 " + maxPlayers + " 队员.");
	} 
    }
	} else if (selection == 3) {
        cm.sendOk("#e <Party Quest: Moon Bunny's Rice Cake>#n \r\n A mysterious Moon Bunny that only appears in #b#m910010000##k durning full moons. #b#p1012112##k of #b#m100000200##k 正在寻找Maplers寻找 #rMoon Bunny's Rice Cake#k for #b#p1012114##k. If you want to meet the Moon Bunny, plant Primrose Seeds in the designated locations and summon forth a full moon. Protect the Moon Bunny from wild animals until all #r10 Rice Cakes#k are made.\r\n #e - Level:#n 10 or above #r (Recommended Level: 10 - 20)#k \r\n #e - Time Limit:#n 10 min \r\n #e - Number of Participants:#n 3 to 6 \r\n #e - Reward:#n #i1003266:# Rice Cake Topper #b \r\n(obtained by giving Tory 100 Rice Cakes)#k \r\n #e - Items:#n #i1002798:# A Rice Cake on Top of My Head #b \r\n(obtained by giving Tory 10 Rice Cakes).");
        cm.dispose();
	} else if (selection == 4) {
		cm.sendOk("Oh, my! You brought Moon Bunny's Rice Cakes for me? Well, I've prepared some gifts to show you my appreciation. How many rice cakes do you want to give me?#b\r\n#L10#Moon Bunny's Rice Cake x10 - A Rice Cake on Top of My Head#l\r\n#L11#Moon Bunny's Rice Cake x100 - Rice Cake Topper");
	} else if (selection == 5) {
    	var pqtry = 10 - cm.getPQLog(PQ);
	cm.sendOk("You can do this quest " + pqtry + " time(s) today.");
	cm.dispose();
	}
    } else if (status == 2) { 
	if (selection == 10) {
		if (!cm.canHold(1002798,1)) {
		cm.sendOk("Make room for this Hat.");
	}else if (cm.haveItem(4001101,10)) {
		cm.gainItem(1002798, 1);
		cm.gainItem(4001101, -10);
		cm.sendOk("谢谢你。 I'm really going to enjoy these cakes!");
		cm.dispose();
	}else{
        cm.sendOk("Please make sure you have the amount of cakes needed.");
		cm.dispose();
	}  
	} else if (selection == 11) {
	if (!cm.canHold(1003266,1)) {
		cm.sendOk("Make room for this Hat.");
	}else if (cm.haveItem(4001101,100)) {
		cm.gainItem(1003266, 1);
		cm.gainItem(4001101, -100);
		cm.sendOk("谢谢你。 I'm really going to enjoy these cakes!");
		cm.dispose();
	} else{
        cm.sendOk("Please make sure you have the amount of cakes needed.");
		cm.dispose();
	}
	} if (mode == 0) { 
        cm.dispose();
    } 
}
}