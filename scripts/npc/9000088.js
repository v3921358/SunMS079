var status = -1;

function start() {
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0)
        cm.sendYesNo("Would you like to move to the legendary town of Ardentmill? In #bArdentmill#k, you can learn the 5 Profession skills of #bHerbalism, Mining, Equipment Crafting, Accessory Crafting, and Alchemy#k.");
    else if (status == 1) {
        cm.warp(910001000);
        cm.saveReturnLocation("ARDENTMILL");
        cm.dispose();
    }
}