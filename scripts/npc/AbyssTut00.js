/* Dawnveil
    Cutscene Root Abyss Ereve
	Neinheart
    Made by Daenerys
*/
var chat = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 /*End Chat*/ || mode == 0 && chat == 0 /*Due to no chat -1*/) {
        cm.dispose();
        return;
    }
    mode == 1 ? chat++ : chat--;
    if (chat == 0)
	    cm.sendNextS("I've been waiting for you.",1);
	else if (chat == 1)	
	    cm.sendNextPrevS("What's going on? I was in the middle of very important loot-related business.",3);
	else if (chat == 2)
        cm.sendNextPrevS("The Alliance has received some very shocking news. A previously uncharted area has appeared in the northern regions of the Sleepywood.",1);
	else if (chat == 3)
        cm.sendNextPrevS("Appeared?",3);
	else if (chat == 4)
        cm.sendNextPrevS("Yes, it's quite odd. I believe it was hidden by some sort of old magic.",1);
	else if (chat == 5)
        cm.sendNextPrevS("The scout who brought me this information said he felt a very evil presence there. It could have something to do with the Black Mage.",1);
	else if (chat == 6)
	    cm.sendNextPrevS("Sounds like we need to get over there right away.",3);
	else if (chat == 7)
	     cm.sendNextPrevS("I've already dispatched the Cygnus Knights. The topography of the area is complex, and a thick fog covers much of the landscape.",1);
    else if (chat == 8)  
         cm.sendNextPrevS("...What should I do?",3);	
	else if (chat == 9) 
	     cm.sendNextPrevS("Go look around. The loss of one explorer would be far more acceptable than all of the Cygnus Knights.",1);
	else if (chat == 10) 
	     cm.sendNextPrevS("I will send you to #b#e#m105010000##n#k to investigate the area. Report back immediately if you find anything, and try to send up a flare or something if you're going to get yourself killed.",1);
	else if (chat == 11) 
	     cm.sendNextPrevS("I'll send you to #b#e#m105010000##n#k with Shinsoo's power.",1);
	else if (chat == 12) {
		cm.introEnableUI(0);
        cm.introDisableUI(false);
        cm.warp(105010000,3);		
        cm.dispose();
    }
}
