var state;

function start() {
    state = -1;
    cm.sendNextS("Now, let me tell you how to enter another user's farm.", 5);
}

function action(mode, type, selection) {
    state++;
    switch (state) {
        case 0:
            cm.sendNextS("Let's see who's in my People to Belittle book... Ah, Orchid! Let's go pay her farm a visit.", 5);
            break;
        case 1:
            cm.enter_931060120();
            break;
        case 2:
            cm.sendNextS("When the window pops up, press OK to move.", 5);
            break;
        case 3:
            cm.sendNextPrevS("If you press No, I guess you and I are through.", 5);
            break;
        case 4:
            cm.sendYesNoS("Did you get all that? I hate explaining things multiple times.\r\n(Press Yes to return your original location.)", 5);
            break;
        case 5:
            if (mode == 1) {
                cm.warp(getSavedLocation("TUTORIAL"));
            } else {
                cm.warp(931060120);
            }
            break;
    }
}