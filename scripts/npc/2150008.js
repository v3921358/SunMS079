/* RED Zero
    Ace
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendSimple("Would you like to leave Edelstein and travel to a different continent? I can take you to Victoria Island and the Orbis area of Ossyria. The cost is 800 Mesos. Where would you like to go?\r\n#b#L0#Victoria Island#l\r\n#b#L1#Orbis#l");
    } else if (status == 1) {
        sel = selection;
	if (selection == 0) {		
	    cm.warp(104020130,0);
		cm.dispose();
    } else if (selection == 1) {
		cm.warp(200000100,0);
		cm.gainMeso(-800);
		cm.dispose();
  }
}