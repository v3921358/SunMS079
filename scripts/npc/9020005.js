importPackage(java.lang);

var status = 0;
var minLevel = 50; // GMS = 50 
var maxLevel = 250; // GMS = 200? recommended 50 - 69
var minPlayers = 1; // GMS = 3
var maxPlayers = 6; // GMS = 4 || but 6 makes it better :p
var open = false; //open or not
var PQ = 'EscapePQ';

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, selection) {
	if (status >= 1 && mode == 0) {
		cm.sendOk("Ask your friends to join your party. You can use the Party Search funtion (hotkey O) to find a party anywhere, anytime.");
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
		cm.sendSimple("Do you really want to leave this map now, and have to start all over if you want to try again?#b\r\n#L0#Yes! Get me out of here..#l");
	} else if (cm.getPlayer().getMapId() == 910002000 || cm.getPlayer().getMapId() == 923040000) {
		cm.sendSimple("#e <Party Quest: Escape>#n \r\nThe truth is, I wanted to run away... But I couldn't leave him behind. He's trapped in the Aerial Prison, and needs someone to bust him out. #b\r\n#L1#I'll help the Explorer trapped in the castle!#l\r\n#L3#Please tell me more about the Prison Guard Key.#l\r\n#L5#Check the number of tries for today.#l#k");
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
     	cm.sendYesNo("You need to create a party to do a Party Quest. Do you want to use the Party Search funtion?");
    } else if (!cm.isLeader()) { // Not Party Leader
    	cm.sendOk("由你的队伍组人决定.");
    	cm.dispose();
    } else if (cm.getPQLog(PQ) >= 10){
    	cm.sendOk("Sorry, but you have done this quest 10 times today. Please come back tomorrow.");
    	cm.dispose();
    } else if (!cm.allMembersHere()) {
    	cm.sendOk("Some of your 队员 are in a different map. Please try again once everyone is together.");
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
	}
	if (next) {
		var em = cm.getEventManager("Prison");
		if (em == null || open == false) {
			cm.sendSimple("This PQ is not currently available.");
			cm.dispose();
		} else {
			var prop = em.getProperty("state");
			if (prop == null || prop.equals("0")) {
				em.startInstance(cm.getParty(),cm.getMap(), 70);
			} else {
				cm.sendSimple("有人正在尝试任务。 请等待他们完成，或找到另一个频道.");
			}
			cm.removeAll(4001528);
			cm.setPQLog(PQ);
			cm.dispose();
		} 
	} else {
		cm.sendYesNo("你的队伍不是之间的一方 " + minPlayers + " 和 " + maxPlayers + " 队员。 请回来，当你有之间 " + minPlayers + " 和 " + maxPlayers + " 队员.");
	} 
}
} else if (selection == 2) {
	cm.sendNext("Kenta was researching sea creatures through samples he got from Explorers, but that was only effective for a while. When his research needed to go further, he decided to go into the Dangerous Sea Areas to conduct direct research. I haven't heard from him since he left... he must be in trouble.#b\r\n#L12#Go on.#l#k");
} else if (selection == 3) {
	cm.sendOk("#rPrison Guard Keys#k are keys held by the Hidden Tower's Prison Guards. If you bring me #b50#k of them, I'll give you a small gift. Getting so many would mean that you've saved a lot of people, after all.\r\n#L94##v1132094:##b#t1132094##k#l\r\n#L95##v1132095:##b#t1132095##k#l\r\n#L96##v1132096:##b#t1132096##k#l\r\n#L97##v1132097:##b#t1132097##k#l\r\n#L98##v1132098:##b#t1132098##k#l");
}else if (selection == 5) {
	var pqtry = 10 - cm.getPQLog(PQ);
	cm.sendOk("You can do this quest " + pqtry + " time(s) today.");
	cm.dispose();
}
}else if (status == 2) {
	if (selection == 94 || selection == 95 || selection == 96 || selection == 97 || selection == 98) {
		if (!cm.canHold(1132000 + selection, 1)) {
			cm.sendOk("Make room in Equip.");
			cm.dispose();
		} else if (cm.haveItem(4001534,50)) { //TODO JUMP
			cm.gainItem(1132000 + selection, 1);
			cm.gainItem(4001534, -50);
			cm.dispose();
		} else {
			cm.sendOk("Come back with 50 Guard Key.");
			cm.dispose();
		}
	}else{
		cm.OpenUI("21");
		cm.dispose();
	}
}else if (mode == 0) { 
	cm.sendSimple("error?");
	cm.dispose();
} 
}