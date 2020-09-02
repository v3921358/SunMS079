/* Dawnveil
    Evolving Tutorial 1
	Orchid + Gelimer
    Made by Daenerys
*/
var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	    cm.sendNextS("Gelimer！你为什么要搬到这里莲花未经我授权?",1);
	} else if (status == 1) {
	    cm.sendNextPrevS("M-夫人兰花。你是......早...", 1,0,9075004);
	} else if (status == 2) {
	    cm.sendNextPrevS("关闭你的陷阱，你油腻的老书呆子！ 你不动我的兄弟，除非我告诉你搬我的兄弟！ 我的小莲花需要靠近我或他会害怕!",1);
	} else if (status == 3) {
	    cm.sendNextPrevS("请降低你的声音，亲爱的。 有一些发展...", 1,0,9075004);
	} else if (status == 4) {
	    cm.sendNextPrevS("我正在开发一个需要设置你的胡子火，Gelimer。 你认为你可以继续延迟这些实验多久？ 莲花应该在几个月前醒了。 你知道我要对你做什么，如果你不成功，不是你?",1);
	} else if (status == 5) {
	    cm.sendNextPrevS("莲花很快就会醒来，我向你保证。 他很快就会醒来...", 1,0,9075004);
	} else if (status == 6) {
	    cm.sendNextPrevS("你想要更多的时间吗？ 然后买一个新的手表！ 我想我的兄弟现在醒来!",1);
	} else if (status == 7) {
	     cm.sendNextPrevS("也许他只需要听到你的声音...来吧，看看.", 1,0,9075004);
   } else if (status == 8) {
	    cm.warp(957020002);
        cm.dispose();
    }
  } 
