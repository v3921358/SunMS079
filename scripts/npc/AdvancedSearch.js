var v1, v2, v3;

function start() {
    v1 = "0";
    v2 = "";
    v3 = -1;
    v2 += "                      Advanced Search Generator                      ";
    v2 += "\r\n#L1#Item";
    v2 += "\r\n#L2#NPC";
    v2 += "\r\n#L3#Map";
    v2 += "\r\n#L4#Mob";
    v2 += "\r\n#L5#Quest";
    v2 += "\r\n#L6#Skill";
    cm.sendSimple(v2);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    switch (v1) {
        case "0":
            v2 = selection;
            cm.sendGetText("Name:");
            v1 = "1";
            break;
        case "1":
            cm.sendOk(cm.searchData(v2, cm.getText()));
            v1 = "2";
            break;
        case "2":
            if (!cm.foundData(v2, cm.getText())) {
                cm.dispose();
                return;
            }
            v3 = selection;
            switch (v2) {
                case 1:
                    cm.sendGetNumber("Amount:", 1, 1, 92);
                    break;
                case 2:
                    cm.dispose();
                    cm.openNpc(selection);
                    break;
                case 3:
                    cm.warp(selection, 0);
                    cm.dispose();
                    break;
                case 4:
                    cm.sendGetNumber("Amount:", 1, 1, 100);
                    break;
                case 5:
                    cm.sendSimple(" \r\n#L0#Start Quest#l\r\n#L1#End Quest#l");
                    break;
                case 6:
                    cm.sendGetNumber("Skill Level:", 1, 1, 30);
                    break;
            }
            v1 = "3";
            break;
        case "3":
            switch (v2) {
                case 1:
                    if (v3 >= 2000000) {
                        for (var i = 0; i < selection; i++)
                            if (cm.canHold(v3))
                                cm.gainItem(v3, 1);
                    } else {
                        if (cm.canHold(v3))
                            cm.gainItem(v3, 1);
                    }
                    cm.dispose();
                    break;
                case 4:
                    cm.spawnMonster(v3, selection);
                    cm.dispose();
                    break;
                case 5:
                    cm.dispose();
                    switch (selection) {
                        case 0:
                            cm.startQuest(v3);
                            break;
                        case 1:
                            cm.completeQuest(v3);
                            break;
                    }
                    break;
                case 6:
                    cm.useSkill(v3, selection);
                    cm.dispose();
                    break;
            }
    }
}