importPackage(Packages.tools.packet);

var status = 0;

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    mode == 1 ? status++ : status--;
    if (status == 1) {
        cm.sendNextS("(Cygnus and her knights look very serious. Can't say that I blame them.)", 17);
    } else if (status == 2)
        cm.sendNextPrevS("(The senators don't seem so thrilled either, I wonder what they're thinking? Maybe I can get closer...)", 17);
    else if (status == 3)
        cm.sendNextS("Could it be true? Could Cygnus have been a fake this whole time?", 5, 1402200);
    else if (status == 4)
        cm.sendNextPrevS("Are you listening to yourself? How could that even be possible?\r\nCygnus is the empress now and she'll be the empress tomorrow!", 5, 1402201);
    else if (status == 5)
        cm.sendNextPrevS("I'm not arguing that she's a total impostor, but if there really is someone with the treasure of Ereve...", 5, 1402203);
    else if (status == 6)
        cm.sendNextPrevS("Aria was supposed to have valued that gem above all other things. She was going to pass it on to the rightful heir.", 5, 1402202);
    else if (status == 7)
        cm.sendNextPrevS("If that treasure proves she is the real empress, I mean if someone other than Cygnus even belongs to Aria's bloodline everything we've worked for could be in jeopardy.", 5, 1402200);
    else if (status == 8)
        cm.sendNextPrevS("I won't betray Cygnus after all she's done for Ereve, but I can't ignore the legitimacy of this woman's claims either!", 5, 1402203);
    else if (status == 9)
        cm.sendNextPrevS("The Maple World alliance was just about to form a unified front. The only reason most of these people are here is because they trusted Cygnus. The alliance may fail to pieces if someone else steps in.", 5, 1402202);
    else if (status == 10)
        cm.sendNextPrevS("We could stand here and speculate all day. I think it is time we let this accuser speak for herself.", 5, 1402201);
    else if (status == 11)
        cm.sendNextPrevS("Shhh... She is coming.", 5, 1402201);
    else if (status == 12)
        cm.sendNextS("(The director of this convoluted play finally arrives.)", 17);
    else if (status == 13) {
        cm.dispose();
        cm.showHilla();
    }
}