function start() {
    status = -1;

    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.getPlayer().getMapId() == 108010101 || cm.getPlayer().getMapId() == 108010201 || cm.getPlayer().getMapId() == 108010301 || cm.getPlayer().getMapId() == 108010401 || cm.getPlayer().getMapId() == 108010501) {
                cm.sendOk("本地图暂时无法使用使用拍卖功能");
                cm.dispose();
                return;
            }
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
             text += "#L1##e#r直接给经验#l"
			//text += "#L3##e#r9点福利#l\r\n\r\n"

            cm.sendOk(text); 
        } else if (selection == 1) {
            if (cm.getPlayer().getMapId() == 910000000) {
                cm.sendOk("你已经在市场了，别逗了好吗?");
                cm.dispose();
                return;
            }
            cm.gainExp(200000);
            cm.dispose();
        }
    }
}