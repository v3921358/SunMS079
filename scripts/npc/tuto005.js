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
        cm.sendNextS("你是谁？你迷路了?", 3);
    else if (chat == 1)
        cm.sendNextPrevS("我之后，你有追求了一段时间，现在你在我面前站立。光的命运战士.", 1, 0, 1106001);
    else if (chat == 2)
        cm.sendNextPrevS("你在说什么?", 3);
    else if (chat == 3)
        cm.sendNextPrevS("记住你的礼貌，on！ 这是皇后!", 1, 0, 1106000);
    else if (chat == 4)
        cm.sendNextPrevS("你是从前的家伙！ 这里发生了什么？ 你谈到的那个家伙，克莱迪尔......我在阁楼上找到了一封来自他的信。 是林伯特的真名还是什么?", 3);
    else if (chat == 5)
        cm.sendNextPrevS("你知道的名字Chromile？光的骑士?", 1, 0, 1106000);
    else if (chat == 6)
        cm.sendNextPrevS("Chromile和林伯特先生没有连接，为您节省。Chromile...是你的父亲.", 1, 0, 1106001);
    else if (chat == 7)
        cm.sendNextPrevS("我的父亲在我小的时候把我留在这里。 他放弃了我这个老鸡鸡.", 3);
    else if (chat == 8)
        cm.sendNextPrevS("他没有放弃你。 你的父亲离开你在你的母亲去世后，以拯救你的生活。 他的道路不是你可以跟随的...", 1, 0, 1106001);
    else if (chat == 9)
        cm.sendNextPrevS("救我？ 他没救我。 他让我在这个棚子里成为奴隶。 他甚至没有给我一个名字！ 现在我发现我在这里等着一个永远不会回来的父亲...", 3);
    else if (chat == 10)
        cm.sendNextPrevS("只有最黑暗的夜晚才能产生辉煌的日出。 放下你的愤怒，和我一起。 你会发现你寻求的光.", 1, 0, 1106001);
    else if (chat == 11)
        cm.sendNextPrevS("皇后，我对这个男孩没有信心。 我们对他一无所知。 我认为他不适合成为光之骑士.", 1, 0, 1106000);
    else if (chat == 12)
        cm.sendNextPrevS("亲爱的Neinheart，我应该比你更好地认为你会相信信心。 去测试他，但要温柔.", 1, 0, 1106001);
    else if (chat == 13)
        cm.sendNextPrevS("等待什么?", 3);
    else if (chat == 14) {
        cm.introEnableUI(0);
        cm.introDisableUI(false);
        cm.forceCompleteQuest(20034);
        cm.forceStartQuest(20035);
        cm.mihileAssailantSummon();
        cm.dispose();
    }
}