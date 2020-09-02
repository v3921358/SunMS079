/* 
 * @Author Lerk
 * 
 * Tiger Statue (990000900)
 * 
 * Guild Quest - end of boss
 */

importPackage(java.lang);

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    if (eim != null) {
	if (eim.getProperty("leader").equals(cm.getName())) {
	    if (cm.haveItem(4001024)) {
		cm.removeAll(4001024);
		var prev = eim.setProperty("bossclear","true",true);
		if (prev == null) {
		    var start = parseInt(eim.getProperty("entryTimestamp"));
		    var diff = System.currentTimeMillis() - start;
		    var points = 10000 - Math.floor(diff / 1000);
		    cm.gainGP(points);
		}
		eim.finishPQ();
	    } else {
		cm.sendOk("这是你最后的挑战。打败邪恶怪物把#v4001024##r#z4001024##k还给我。就这些了....");
	    }
	}
    } else {
	cm.warp(990001100);
    }
	cm.dispose();
}
