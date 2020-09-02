var status = -1;

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0)
        cm.sendNextS("I believe it's time to make an appearance.", 17);
    else if (status == 1)
        cm.sendNextPrevS("My heart is racing! It's been ages since I've felt so alive. Or anxious. I am terribly anxious.", 17);
    else if (status == 2)
        cm.sendNextPrevS("If I stand here any longer, I'll lose the nerve. It's now or never!", 17);
    else if (status == 3) {
        cm.introEnableUI(0);
        cm.dispose();
    }
}