/* Dawnveil
    Elder
	Grant
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
	if (mode == 1)
	    status++;
	 else
	    status--;
    if (status == 0) {
	cm.sendNext("Interested in Professions, eh? Let me give you a brief introduction. Our town is home to 5 master artisans who practice #bHerbalism, Mining, Smithing, Accessory Crafting, and Alchemy#k. The Master Artisan Association has a rule that limits each individual to 2 Professions, and each Profession you learn must complement each other. Accordingly, you can select and learn up to #r2 Professions#k.");
    } else if (status == 1) {
    cm.sendPrev("#b- Herbalism + Alchemy - Mining + Smithing - Mining + Accessory Crafting#k. You can learn any of these 3 combinations of Professions. It is up to you to choose your own path from there.");
    cm.dispose();
    }
}

function end(mode, type, selection) {
	cm.dispose();
}