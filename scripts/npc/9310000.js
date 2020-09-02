var status = 0;
var cost = 50000;
function start() {
    cm.sendYesNo("你想去上海吗?需要5万金币");
}

function action(mode, type, selection) {
    if (mode != 1) {
        if (mode == 0)
        cm.sendOk("既然你不要那就算了.");
        cm.dispose();
        return;
    }
    status++;
    if (status == 1) {
		if(cm.getMeso() < cost) {
		cm.sendOk("你的金币貌似不足50000.");
		cm.dispose();
		} else {
		cm.gainMeso(-cost);
		cm.warp(701000100, 0);
        cm.dispose();
    }
}
}