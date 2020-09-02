/* RED 1st impact
    RED Login events level 1-10
	Cassandra + Maple Admin
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendNextS("Hello! To celebrate the awesome new pets of RED, we're giving away 1 of the 3 pets, completely for free! Remember, only one pet will be given to each account so make sure to take the pet with the right character! Come see me when you're ready for your bundle of joy!",4);	
	} else if (status == 1) {
	    cm.sendNextS("Did you notice the red-haired boy icon on the left side of you screen?\r\n#v3800475# It is Tot's Know How that tells you the right quests and contents #e#rfor 1~60 level#n#k that #e#rcan help you level up instantly#n#k when you complete the quests!\r\nPress the button on the left or hotkey #e#r' - '#n#k to open it now!",5,9010000);
	} else if (status == 2) {
        cm.dispose();
    }
}
