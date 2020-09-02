/* RED Zero
    Ace
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else 
        if (status == 0) {
		    cm.sendNext("I guess you don't like traveling to unfamiliar places.");
            cm.dispose();
        status--;
    }
    if (status == 0) {
	    cm.sendYesNo("Do you want to go to Edelstein? The fee is 800 Mesos. Hop on if you want to go.");
	} else if (status == 1) {	
        cm.warp(310000010,0);
        cm.gainMeso(-800);		
	    cm.dispose(); 
    }
}