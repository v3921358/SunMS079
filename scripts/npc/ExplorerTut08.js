/* RED 1st impact
    Maple Leaf
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendNextS("A Maple Leaf? OH, I remember seeing a huge Maple Tree on Maple Island. How did it follow me here?",17);
	} else if (status == 1) {	
	    cm.sendNextPrevS("I guess I can keep it in my #bExplorer Book#k to remind me of Maple Island.",17);
	} else if (status == 2) {
	    cm.sendNextPrevS("Set your #e#bAdventure Journal#k#n shortcut in the Key Settings window to open the cabinet and peruse it.",17);
    } else if (status == 3) {	
	    cm.topMsg("You got an Adventure Journal!");
		cm.openUI(191);
        cm.dispose();
    }
}