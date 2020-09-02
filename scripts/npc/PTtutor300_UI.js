var status = -1;

function start() {
    action(1, 0, 0)    
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0)
        cm.tutorialUI("UI/tutorial/phantom/0/0");
    else if (status == 1)
        cm.tutorialUI("UI/tutorial/phantom/1/0");
    else if (status == 2)
        cm.tutorialUI("UI/tutorial/phantom/2/0");
    else if (status == 3)
        cm.tutorialUI("UI/tutorial/phantom/3/0");
    else if (status == 4)
        cm.tutorialUI("UI/tutorial/phantom/4/0");
    else
        cm.dispose();
}