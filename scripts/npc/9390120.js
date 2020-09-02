/* Dawnveil
    Enter Gollux Head
	Heart Tree Guardian
    Made by Daenerys
*/
var status = -1;
var selection = -1;

function start() {
    status = -1;
    selection = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    mode == 1 ? status++ : status--;
    if (status == 0) {
        cm.sendSimpleS("#e#r<Road to Gollux>#n#k\r\nThis is where #rGollux#k, the Heart tree is located. Good luck on defeating him, #h #.\r\n\r\n#b#L0#Use #i4033981#Fragile Heart Tree Key to fight Gollux now. (Lv. 140 Required)#n#k #l\r\n#L1##bGo later#k#l",5);
    } else if (status == 1) {
    if (selection == 0) {
	    if (cm.getPlayer().getLevel() >= 140 && cm.haveItem(4033981, 1)) {
		cm.warp(863000920);
		cm.dispose();
		}else
         cm.sendOk("Make sure you are #e#bLv. 140#n#k and have a #i4033981##e#rFragile Heart Tree Key#k.");
         cm.dispose();
		return;
	} else if (selection == 1) {		
		 cm.sendNext("Come back to me when you have changed your mind.");
         cm.dispose();
		 return;	 
     }
   }
}
