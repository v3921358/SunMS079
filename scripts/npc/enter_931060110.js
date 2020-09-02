var state;

function start() {
    state = -1;
    cm.sendNextS("I'll tell you how to enter your farm now.", 5);
}

function action(mode, type, selection) {
    state++;
    switch (state) {
        case 0:
            cm.enter_931060110();
            break;
        case 1:
            cm.sendNextS("When the window pops up, press OK to move.", 5);
            break;
        case 2:
            cm.sendNextPrevS("If you press No, I guess you and I are through.", 5);
            break;
        case 3:
            cm.sendYesNoS("Did you get all that? I hate explaining things multiple times.\r\n(Press Yes to return your original location.)", 5);
            break;
        case 4:
            if (mode == 1) {
                cm.warp(getSavedLocation("TUTORIAL"));
            } else {
                cm.warp(931060110);
            }
            break;
    }
}