/* RED 1st impact
    The Sangri-La
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	//    cm.sendNextS("The ship is ready to set sail!",1);
	    cm.warp(4000004);
		cm.gainExp(1242);
		cm.forceStartQuest(17901);
		cm.forceCompleteQuest(32216);
		cm.introEnableUI(0);
        cm.introDisableUI(false);
		cm.dispose();
    }
}