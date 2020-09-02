/* Dawnveil
    Guild tasks
	Lea
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("Hello, I am Lea. I am in charge of guild support. For guild management, I can arrange transportation to the Hall of Heroes. Would you like to move to the Hall of Heroes for your guild-related issues?");
    } else if (status == 1) {
	cm.sendNext("Well then, I will immediately transport you.");
	} else if (status == 2) {
	cm.warp(200000301);
	cm.dispose();
    }
}