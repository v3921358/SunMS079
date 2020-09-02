var itemList = [5150016, 3010045, 3010049, 3010021, 2002099, 2022465, 2022466, 2022468, 2022246, 2022245];
var chance = [70, 70, 70, 70, 500, 500, 500, 500, 500, 500];
var sum = [];

for(var i = 1 ; i < itemList.length; i++)
    chance[i] = chance[i-1] + chance[i];

var randNum = Math.floor(Math.random()* chance[chance.length-1]);

var randItem = chance[chance.length-1]

for(var i = 0 ; i < chance.length; i++) {
    if ( randNum <= chance[i] ) {
        randItem = itemList[i];
        break;
    }
}


var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        cm.sendSimple("#b#L1#我要兑换白色礼物盒#l\r\n#L2#我要兑换红色礼物盒#l\r\n#L3#我要兑换蓝色礼物盒#l\r\n#L4#我要兑换紫色礼物盒#l\r\n#L5#我要领取魔法手套#l\r\n#L6#我要领取一组巨型雪球(消耗栏要空一格喔)#l\r\n#L7#我要给你温暖的雪花#l#k");
    } else if (status == 1) {
        if (selection == 1) {
            if (cm.haveItem(4000422)) {
                cm.gainItem(4000422, -1);
                cm.gainItem(randItem, 1);
            } else {
                cm.sendOk("你没有礼物盒-.-");
            }
            cm.dispose();
        } else if (selection == 2) {
            if (cm.haveItem(4000423)) {
                cm.gainItem(4000423, -1);
                cm.gainItem(randItem, 1);
            } else {
                cm.sendOk("你没有礼物盒-.-");
            }
            cm.dispose();
        } else if (selection == 3) {
            if (cm.haveItem(4000424)) {
                cm.gainItem(4000424, -1);
                cm.gainItem(randItem, 1);
            } else {
                cm.sendOk("你没有礼物盒-.-");
            }
            cm.dispose();
        } else if (selection == 4) {
            if (cm.haveItem(4000425)) {
                cm.gainItem(4000425, -1);
                cm.gainItem(randItem, 1);
            } else {
                cm.sendOk("你没有礼物盒-.-");
            }
            cm.dispose();
        } else if (selection == 5) {
            cm.gainItem(1472063, 1);
            cm.dispose();
        } else if (selection == 6) {
            cm.gainItem(2060006, 800);
            cm.dispose();
        } else if (selection == 7) {
            cm.sendGetNumber("你带上火药桶呀？那么，请给我#b火药库#k 你有。我会做一个很好的鞭炮。有多少人，你愿意给我? \n\r #b< 火药桶的库存数量 : 0 >#k", 0, 0, 1000);
        }
    } else if (status == 2) {
        var num = selection;
        if (num == 7) {
            cm.sendOk("T.T我需要的火药桶，启动烟花。...\r\n请再想想和我说话.");
        } else if (cm.haveItem(4031875, num)) {
            cm.gainItem(4031875, -num);
            cm.giveKegs(num);
            cm.sendOk("不要忘了给我的火药桶，当你获得它们."); cm.safeDispose();
        } else if (selection == 8) {
            cm.sendNext("火药桶征收状况\n\r #B"+cm.getKegs()+"# \n\r 如果我们收集所有这些，我们就可以开始了烟花...");
        }
        cm.safeDispose();
    }
}
