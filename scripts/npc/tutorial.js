var chat = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 /*End Chat*/ || mode == 0 && chat == 0 /*Due to no chat -1*/) {
        cm.dispose();
        return;
    }
    if (mode == 1) //Next/Ok/Yes/Accept
        chat++;
    else if (mode == 0) //Previous/No/Delience
        chat--;
    startChat();
}

function startChat() {
    if (chat == 0) {
        cm.sendNextPrev("等等等等");
    } else {
        cm.MovieClipIntroUI(false);
        cm.dispose();
    }
}