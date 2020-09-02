var chat = -1;
var select, itemid;
var invalid = [
2022766, 1042003, 1062007, 1002140, 1003142, 1322013, 1002959,
1082392, 1082393, 1082394, 2003561, 2003552, 2003553, 1142229, 
2430130, 2430131, 2430403, 2430404, 2022728, 2022729
];
var invalidarrays = [
//[starting value, ending value]
[5000000, 5010000],
[2100000, 2110000],
[2003516, 2003520],
[2213000, 2214000],
[5211000, 5220000],
[5360000, 5370000],
[2450000, 2460000],
[2230000, 2230004]
];

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 /*End Chat*/ || mode == -1 && chat == 0 /*Due to no chat -1*/) {
        cm.dispose();
        return;
    }
    if (mode == 1) //Next/Ok/Yes/Accept
        chat++;
    else if (mode == -1) //Previous/No/Delience
        chat--;
    startChat(selection);
}

function startChat(selection) {
    if (chat == 0)
        cm.sendSimple("Ello there, I'm the ID Shop.\r\nWhat would you like me to do for you?\r\n#L0#Have an Item by it's ID#l\r\n#L1#Look up for an Item's ID"/* + "\r\n#L2#Max my skills#l"*/ + "\r\n#L3#Give me NX!#l");
    else if (chat == 1) {
        switch (selection) {
            case 0:
                cm.sendGetNumber("Enter Item's ID:", 0, 1002000, 6000000);
                select = 0;
                break;
            case 1:
                cm.sendGetText("Enter Item's Name:");
                select = 1;
                break;
            case 2:
                if (cm.getPlayer().getLevel() >= 150) {
                    cm.sendOk("Your skill(s) level is being maxed.");
                    cm.maxAllSkills();
                } else
                    cm.sendOk("You have to be level 150+ in order to max skills :)");
                cm.dispose();
                break;
            case 3:
                cm.sendGetNumber("Amount of NX:", 1, 1, 49999);
                select = 3;
                break;
        }
    } else if (chat == 2) {
        switch (select) {
            case 0:
                itemid = selection;
                for (var i = 0; i < invalid.length; i++) {
                    if (itemid == invalid[i]) {
                        cm.sendOk("This is an invalid item.");
                        cm.dispose();
                        return;
                    }
                }
                for (var a = 0; a < invalidarrays.length; a++) {
                    if (itemid >= invalidarrays[a][0] && itemid <= invalidarrays[a][1]) {
                        cm.sendOk("This is an invalid item.");
                        cm.dispose();
                        return;
                    }
                }
                cm.sendGetNumber("Enter the amount you want for the item:", 1, 1, 100);
                break;
            case 1:
                cm.sendPrev(cm.searchId(4, cm.getText()));
                selection = 0;
                chat = 1;
                break;
            case 3:
                cm.gainD(selection);
                cm.dispose();
                break;
        }
    } else if (chat == 3) {
        if (itemid >= 1000000 && itemid < 2000000) {
            for (var amount = 0; amount < selection; amount++)
                if (cm.canHold(itemid, selection))
                    cm.gainItem(itemid, 1);
                else {
                    cm.sendOk("It seems that you can't hold the item, please check that the item exists and make sure you have enough inventory slots.");
                    cm.dispose();
                    return;
                }
        } else {
            if (cm.canHold(itemid, selection))
                cm.gainItem(itemid, selection);
            else {
                cm.sendOk("It seems that you can't hold the item, please check that the item exists and make sure you have enough inventory slots.");
                cm.dispose();
                return;
            }
        }
        cm.sendOk("I hope you enjoy my services.");
    } else
        cm.dispose();
}