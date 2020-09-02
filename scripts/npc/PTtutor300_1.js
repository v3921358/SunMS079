var status = -1;

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0)
        cm.sendNextS("This will be a cakewalk. I hope...", 17);
    else if (status == 1) {
        cm.warp(915000301, 0);
        cm.dispose();
    }
}