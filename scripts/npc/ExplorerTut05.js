/* RED 1st impact
    Port
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else 
        if (status == 0) {
		    cm.sendNext("Let me know when you're ready to board.");
            cm.dispose();
        status--;
    }
    if (status == 0) {
	    cm.sendYesNo("Thanks to you, we're ready to set sail. You ready to board?");
	} else if (status == 1) {
        cm.warp(4000032,0);
        cm.dispose();
    }
}