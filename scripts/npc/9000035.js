【】var status = 0;
var wui = 0;
var jobName;
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
	cm.sendSimple ("#eMake a selection #h #,#n#d " +
                 "#k\r\n#L81##rLow Rebirth Shop" +
                 "#k\r\n#L80##rHigh Rebirth Shop#l" +
				 "#k\r\n#L82##rCharacter Statistics#k");

            } else if (selection == 80) {
                cm.sendSimple ("Hi, Which of the following would you like to purchase\r\n (#r#eMake sure you have enough room in your Inventory!#k#e)#d"+
                 "#k\r\n#L0##bSpecial Job#k -  (#r250 Rebirths & 13337 Chickens#k)" +
                 "#k\r\n#L1##b3K Stat Earrings#k - (#r45 Reborns & 1000 Chickens#k) " +
                 "#k\r\n#L2##b5K stat Earrings#k - (#r75 Reborns & 5000 Chickens#k) " +
                 "#k\r\n\r\n#L3##rBecome Beginner");

            } else if (selection == 0) {
                if (cm.getPlayer().getReborns() > 249 && cm.haveItem(4000252, 13337)) {
                    cm.gainItem (4000252, -13337);
                    cm.changeJobById(900);
                    cm.reloadChar();
                    cm.sendOk ("Congratulations, You have purchased the Regular GM Job!");
                    cm.dispose();
                } else {
                    cm.sendOk ("#r#eYou don't have enough #v4000252# or you don't have enough reborns!");
                    cm.dispose();
                    }
            } else if (selection == 1) {
                if (cm.getPlayer().getReborns() > 29 && !cm.haveItem(1032034) && cm.haveItem(4000252, 1000)) {
         cm.gainItem (1032036);
         cm.gainItem (4000252, -1000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "str", 3000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "dex", 3000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "luk", 3000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "int", 3000);
        cm.reloadChar();
        cm.dispose();
         } else {
        cm.sendOk ("You don't have the Required ammount of Reborns, you already have the Item, or you don't have 1000 #v4000252#");
        cm.dispose();
}
            } else if (selection == 2) {
                if (cm.getPlayer().getReborns() > 74 && !cm.haveItem(1032034) && cm.haveItem(4000252, 5000)) {
         cm.gainItem (4000252, -5000);
         cm.gainItem (1032036);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "str", 5000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "dex", 5000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "luk", 5000);
        cm.editEquipById(cm.getPlayer(), 1, 1032036, "int", 5000);
        cm.reloadChar();
        cm.dispose();
         } else {
        cm.sendOk ("You don't have the Required ammount of Reborns, you already have the Item, or you don't have 5000 #v4000252#");
        cm.dispose();
}
            } else if (selection == 81) {
                cm.sendSimple ("Hi, Which of the following would you like to purchase\r\n (#r#eMake sure you have enough room in your Inventory!#e#k)#d" +
                 "#k\r\n#L4##b500 Stat Ring#k - (#r15 Reborns & 500 Chickens#k)" +
                 "#k\r\n#L5##b1000 Stat Earring#k - (#r25 Reborns & 1000 Chickens#k)");
            } else if (selection == 4) {
                if (cm.getPlayer().getReborns() > 14 && !cm.haveItem(1032038) && cm.haveItem(4000252, 500)) {
         cm.gainItem (4000252, -500);
         cm.gainItem (1032038);
        cm.editEquipById(cm.getPlayer(), 1, 1032038, "str", 500);
        cm.editEquipById(cm.getPlayer(), 1, 1032038, "dex", 500);
        cm.editEquipById(cm.getPlayer(), 1, 1032038, "luk", 500);
        cm.editEquipById(cm.getPlayer(), 1, 1032038, "int", 500);
        cm.reloadChar();
        cm.dispose();
         } else {
        cm.sendOk ("You don't have the Required ammount of Reborns, you already have the Item, or you don't have 500 #v4000252#");
        cm.dispose();
}
            } else if (selection == 5) {
                if (cm.getPlayer().getReborns() > 24 && !cm.haveItem(1032039) && cm.haveItem(4000252, 1000)) {
         cm.gainItem (4000252, -1000);
         cm.gainItem (1032039);
        cm.editEquipById(cm.getPlayer(), 1, 1032039, "str", 1000);
        cm.editEquipById(cm.getPlayer(), 1, 1032039, "dex", 1000);
        cm.editEquipById(cm.getPlayer(), 1, 1032039, "luk", 1000);
        cm.editEquipById(cm.getPlayer(), 1, 1032039, "int", 1000);
        cm.reloadChar();
        cm.dispose();
         } else {
        cm.sendOk ("You don't have the Required ammount of Reborns, you already have the Item, or you don't have 1000 #v4000252#");
        cm.dispose();
}

	} else if (selection == 82) {
		cm.sendOk("#eCharacter Statistics#n\r\nCharacter Name: #e#r#h ##k#n\r\nRebirths: #e#r" +cm.getPlayer().getReborns() + "#n#k\r\nLevel: #e#r" +cm.getPlayer().getLevel()+"#n#k");
		cm.dispose();
		
            } else if (selection == 3) {
           if(cm.getJobId() == 900) {
                    cm.changeJobById(000);
                    cm.sendOk ("Job: Beginner (#rAccepted#k).");
                    cm.dispose();
                  } else {
                    cm.sendOk ("Job: Regular GM(#rDeclined#k).");
                    cm.dispose();
}
}
}
}