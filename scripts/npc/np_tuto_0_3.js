function start() {
    cm.sendNextS("It's the guards from the royal palace! They must be buying time until the rest of the force gets here. We need to be gone before that happens!", 9);
}

function action(mode, type, selection) {
    status++;
    switch (status) {
        case 1:
            cm.sendNextPrevS("You're sending those pieces of junk after ME? I thought I had a better reputation than that.", 3);
            break;
        case 2:
            cm.dispose();
            cm.spawnJettGuards();
            break;
    }
}