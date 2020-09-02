var status = -1;
var sel = -1;

function start() {
    status = -1;
    sel = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    mode == 1 ? status++ : status--;
    if (status == 0) {
        //cm.sendSimple("#b#L0#Learn/Unlearn Mining#l\r\n#L1#Trade Ore Fragments#l");
        cm.sendSimple("Now what can I do for ya?\r\n#b#L2#Hear an explanation about #eMining#n.#l\r\n#L0#Learn#eMining#n.");
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            if(cm.getPlayer().getProfessionLevel(92030000) > 0 || cm.getPlayer().getProfessionLevel(92020000) > 0) {
                cm.sendOk("Hey, don't you also have a crafting Profession, too? Reset that skill first.");
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
                //cm.sendYesNo("Are you sure you wish to unlearn Mining? You will lose all your EXP/levels in Mining.");
                cm.sendYesNo("What? You'll forget everything i taught you. You'll have no level in Mining, no Mastery... You really want to do this?");
            } else if (cm.getPlayer().getProfessionLevel(92000000) > 0 || cm.getPlayer().getProfessionLevel(92040000) > 0) {
                cm.sendOk("Hey, ain't you an Herbalist? Don't waste my time. Go learn #bAlchemy,#k instead.");
                cm.dispose();
            } else {
                //cm.sendYesNo("Would you like to learn Mining?");
                var numprofessions = 0;
                if (cm.getPlayer().getProfessionLevel(92000000) > 0)
                    numprofessions += 1;
                if (cm.getPlayer().getProfessionLevel(92010000) > 0)
                    numprofessions += 1;
                if (cm.getPlayer().getProfessionLevel(92020000) > 0)
                    numprofessions += 1;
                if (cm.getPlayer().getProfessionLevel(92030000) > 0)
                    numprofessions += 1;
                if (cm.getPlayer().getProfessionLevel(92040000) > 0)
                    numprofessions += 1;
                cm.sendYesNo("Wanna learn #bMining#k? All right! That'll be #b5000 Mesos#k. You do have that much, right?\r\n#b(Number of Professions Learned: " + numprofessions + ")");
            }
        } else if (sel == 2) {
            cm.sendNext("If you're looking to get yourself some minerals, all you need's a tool such as a Pickaxe and some Mining skill. Refine the minerals you collect in one of them molds #e#bNack#n#k sells, and you can use it to craft all sorts of useful items.");
            cm.dispose();
        } 
    } else if (status == 1) {
        if (sel == 0) {
            if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
                cm.sendNext("You are no longer a Miner. If you ever change your mind, I'll be waiting.");
                cm.teachSkill(92010000, 0, 0);
            } else {
                if(cm.getPlayer().getMeso() >= 5000) {
                    cm.gainMeso(-5000);
                    cm.sendNext("Okay, them's the basics of Mining. Work on increasing your Mastery, and I'll teach you some new tricks.");
                    cm.teachSkill(92010000, 0x1000000, 0); //00 00 00 01
                    if (cm.canHold(1512000,1)) {
                        cm.gainItem(1512000,1);
                    }
                }else{
                    cm.sendNext("You don't got enough Mesos. I need #b5000 Mesos#k from every student, no exceptions.");
                }
            }
            cm.dispose();
        }
    }
}