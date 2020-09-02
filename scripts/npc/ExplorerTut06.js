/* RED 1st impact
    Maple Tree Hill
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendSelfTalk("Who was that girl? Why did she run away when she saw me?");
	} else if (status == 1) {	
	    cm.sendSelfTalk("Maybe I'll follow her..");
	} else if (status == 2) {
        cm.introEnableUI(0);
        cm.introDisableUI(false);
        cm.dispose();
    }
}