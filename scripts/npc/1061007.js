/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var status = 0;
var zones = 0;
var selectedMap = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 1 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
        
    if (status == 0) {
	cm.sendNext("摇摇欲坠的雕像让你伤心");
    } else if (status == 1) {
	cm.sendYesNo("你想摆脱悲伤吗?");
    } else if (status == 2) {
	cm.warp(105040300);
	cm.dispose();
    }
}	