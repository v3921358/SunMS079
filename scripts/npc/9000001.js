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
	cm.sendNext("你好,我是活动向导点击我前往活动跳跳地图");
    } else if (status == 1) {
	cm.sendYesNo("是否前往活动跳跳地图?");
    } else if (status == 2) {
	cm.warp(103000906,0);
	cm.dispose();
    }
}	