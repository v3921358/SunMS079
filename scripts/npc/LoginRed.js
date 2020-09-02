/* RED 1st impact
    RED Login events level 10-250
	Cassandra + Maple Admin
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else 
        if (status == 1) {
            cm.dispose();
        status--;
    }
    if (status == 0) {
	    cm.sendNextS("Hello! To celebrate the awesome new pets of RED, we're giving away 1 of the 3 pets, completely for free! Remember, only one pet will be given to each account so make sure to take the pet with the right character! Come see me when you're ready for your bundle of joy!",4);	
	} else if (status == 1) {
	if (cm.getPlayer().itemQuantity(5230000) > 0) {
        cm.dispose();
	} else
	    cm.sendYesNoS("I've got a special gift for you, slacker!\r\n#b #t5230000:#  #k\r\n I'm giving five The Owl of Minerva every Monday between 12/9 and 12/30! You're in for a fantastic surprise!\r\n#r (Only one Character per account can participate in The Owl of Minerva Item Give Event.)#k ",5,9010000);
	} else if (status == 2) {
	    cm.sendNextS("You The Owl of Minerva has been given.\r\n#i5230000:# #t5230000#! Check your inventory.",5,9010000);
		cm.gainItem(5230000,1);
	} else if (status == 3) {
        cm.dispose();
    }
  }