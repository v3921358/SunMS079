/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */


function start() {
    if (cm.haveItem(4031013,30)) {
        cm.sendNext("恭喜通过这次个考验 你已经是个强大的弓箭手了,请拿着我给你的英雄证书去找#b赫丽娜#k!");
    } else {
        cm.sendOk("你还没有 #b30 #t4031013##k. 请收集完毕再来找我,祝你好运.");
        cm.dispose();
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.warp(106010000, 0);
		cm.removeAll(4031013);
		//cm.gainItem(4031010, -1);
		cm.gainItem(4031012, 1);
	}
	cm.dispose();
}