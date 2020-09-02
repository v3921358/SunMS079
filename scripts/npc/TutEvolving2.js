/* Dawnveil
    Evolving Tutorial 2
	Orchid + Gelimer
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
	    cm.sendNextS("莲花？ 莲花！ 你醒着么?!",1,9075005);
	else if (chat == 1)	
	    cm.sendNextPrevS("......", 1,0,9075001);
	else if (chat == 2)
        cm.sendNextPrevS("哦，兄弟，我错过了你这么多！ 你一直在打盹所有这一次，我不得不打败了很多人.", 1,0,9075005);
	else if (chat == 3)
        cm.sendNextPrevS("......", 1,0,9075001);
	else if (chat == 4)
        cm.sendNextPrevS("现在我们可以再次在一起！ 我们可以接管这个整个星球的愚蠢的猴子，并统治它，我们应该!", 1,0,9075005);
	else if (chat == 5)
        cm.sendNextPrevS("......", 1,0,9075001);
	else if (chat == 6)
	    cm.sendNextPrevS("莲花？ 你能听见我吗？ 记住你的妹妹，兰花?", 1,0,9075005);
	else if (chat == 7)
	     cm.sendNextPrevS("......", 1,0,9075001);
	else if (chat == 8)
	     cm.sendNextPrevS("我以为你说他正在醒来，Gelimer！ 如果你乱了他的大脑，我会把你内心了!", 1,0,9075005);
	else if (chat == 9)
	     cm.sendNextPrevS("我向你保证，指挥官兰花。 莲花是完美的。 你想看看吗？ 在这里...执行程序Alpha-97.",1);
    else if (chat == 10) {	
		cm.introEnableUI(0);
        cm.introDisableUI(false);
		cm.warp(310010000);	
        cm.dispose();
    }
}

