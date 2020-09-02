/* RED 1st impact
    Inside the Dangerous Forest
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendNextS("I should accept Mai's quest first.",16);
		cm.dispose();
    }
}