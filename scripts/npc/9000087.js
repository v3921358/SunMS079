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
        cm.sendYesNo("Would you like to move to the #bFree Market#k, where you can trade items with other players?")
    else if (status == 1) {
        cm.warp(910000000);
        cm.saveReturnLocation("FREE_MARKET");
        cm.dispose();
    }
}