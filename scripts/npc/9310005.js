function start() {
        if (cm.haveItem(4000194, 50)&&cm.haveItem(4002000, 1)) {
    cm.gainItem(4000194, -50);
	cm.gainItem(4002000, -1);
    cm.warp(701010322, "sp");	
    cm.dispose();
	} else {
	    cm.sendOk("挑战大王蜈蚣,#v4002000# 1张 和 黑羊毛50个!");
    cm.dispose();
}
}