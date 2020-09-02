var status = 0;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if(status == 0){
		cm.sendNext("你们已经分道扬镳，别伤心别气馁相信你一定会遇到赏识你的恋人！那我送你出去把");
		status++;
	} else {
		cm.warp(700000000)
		cm.dispose();
	}
}