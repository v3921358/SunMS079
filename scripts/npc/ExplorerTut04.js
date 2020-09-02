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
		    cm.sendNext("Let me know when you're ready to fight those monsters. They're ruining everything!");
            cm.dispose();
        status--;
    }
    if (status == 0) {
	    cm.sendYesNo("I'll let you on board. Go defeat the monsters rampaging my ship!");
	} else if (status == 1) {
        cm.warp(4000033,0);
        cm.dispose();
    }
}