/* Dawnveil
    PvP(enis)
	Maximus
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendOk("The path to the Battle Mode has changed. Take the Dimensional Mirror to the Battle Square, and you can join in the #bGrand Battle#k!");
	} else if (status == 1) {
	cm.dispose();
    }
}	