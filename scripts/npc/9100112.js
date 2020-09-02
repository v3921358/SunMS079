/*
 By 梓條
 */

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		var Editing = false //true=顯示;false=開始活動
          if(Editing){
          cm.sendOk("暫停運作");
          cm.dispose();
          return;
        } 
			cm.sendSimple("~~~" +
            "#k\r\n#L101##rZZZZZZZZZZZZZZZ");
        } else if (status == 1) {
            
            if (selection == 101) {
                if (cm.haveItem(2370000, 1) ) {
                    //cm.gainItem(4032226, -1);
                    //cm.gainItem(3010054, 1);
					cm.gainExp(100000)
                    cm.sendOk("获得经验100000");
                    cm.dispose();
                } else {
                    cm.sendOk("你在说什么?我不知道!!");
                    cm.dispose();
                }
            } else if (selection == 102) {
                if (cm.haveItem(4032226, 5) ) {
                    cm.gainItem(4032226, -5);
                    cm.gainItem(2022483, 1);
                    cm.sendOk("獲得#i2022483#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 103) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022484, 1);
                    cm.sendOk("獲得#i2022484#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 104) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022485, 1);
                    cm.sendOk("獲得#i2022485#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 105) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022486, 1);
                    cm.sendOk("獲得#i2022486#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 106) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022487, 1);
                    cm.sendOk("獲得#i2022487#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 107) {
                if (cm.haveItem(4032226, 1) ) {
                    cm.gainItem(4032226, -1);
                    cm.gainItem(2022488, 1);
                    cm.sendOk("獲得#i2022488#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 108) {
                if (cm.haveItem(4032226, 20) ) {
                    cm.gainItem(4032226, -20);
                    cm.gainItem(2022489, 1);
                    cm.sendOk("獲得#i2022489#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }else if (selection == 109) {
                if (cm.haveItem(4032226, 20) ) {
                    cm.gainItem(4032226, -20);
                    cm.gainItem(2022490, 1);
                    cm.sendOk("獲得#i2022490#x1");
                    cm.dispose();
                } else {
                    cm.sendOk("您身上沒有#i4000332#,請在次確認");
                    cm.dispose();
				}
			 }
        }
    }
}

	