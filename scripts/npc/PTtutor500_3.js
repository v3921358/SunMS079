importPackage(Packages.tools.packet);

var status = 42;

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    mode == 1 ? status++ : status--;
    if (status == 43) {
        cm.sendNextS("Ah...", 5, 1402100);
    } else if (status == 44)
        cm.sendNextPrevS("Not even a flicker.", 5, 1402400);
    else if (status == 45)
        cm.sendNextPrevS("...", 5, 1402100);
    else if (status == 46)
        cm.sendNextPrevS("It's too early to decide.", 5, 1402102);
    else if (status == 47)
        cm.sendNextPrevS("That's right. We still don't even know if that stone is real.", 5, 1402106);
    else if (status == 48)
        cm.sendNextPrevS("R-right! Even I can make light with magic!", 5, 1402103);
    else if (status == 49)
        cm.sendNextPrevS("When Shinsoo returns, she'll decide.", 5, 1402104);
    else if (status == 50)
        cm.sendNextPrevS("If you faiter, the Cygnus Knights faiter as well. Stay strong.", 5, 1402105);
    else if (status == 51)
        cm.sendNextPrevS("Your alliance is the foundation for a new era in Maple World. This could all be a scheme to place doubt on your position. We cannot listen to her untill she presends solid proof.", 5, 1402101);
    else if (status == 52)
        cm.sendNextPrevS("Everyone...", 5, 1402100);
    else if (status == 53)
        cm.sendNextPrevS("Your hounds are trying desperately to ignore the truth.", 5, 1402400);
    else if (status == 54)
        cm.sendNextPrevS("I won't deny your hard work, Cygnus. You have been suprisingly wise for a young girl, but I ugre you to make the right decision!", 5, 1402400);
    else if (status == 55)
        cm.sendNextPrevS("Acknowledge me as the real empress and step away from this charade before it's too late.", 5, 1402400);
    else if (status == 56)
        cm.sendNextPrevS("Tell the alliance that they will follow me now.", 5, 1402400);
    else if (status == 57)
        cm.sendNextPrevS("Of course, I'm not without sympathy to your situation. I'll give you some time to take all of this in. Do whatever you need to do to reconcile yourself with the truth.", 5, 1402400);
    else if (status == 58)
        cm.sendNextPrevS("When you are finished, you will find that the true Empress of Maple World is not Cygnus, but Hilla.", 5, 1402400);
    else if (status == 59)
        cm.sendNextPrevS("(Gaston should be ready about now. Time to take the plunge!)", 17);
    else if (status == 60) {
        cm.dispose();
        cm.showPhantomWait();
    }
}