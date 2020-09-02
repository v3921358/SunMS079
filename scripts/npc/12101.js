/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
	
    if (status == 0) {
	cm.sendNext("这里叫 #b彩虹村#k, 位于冒险岛世界的东北部。你知道彩虹村是适合新手的，对吗？我很高兴认识你！在这个地方只有微弱的怪物.");
    } else if (status == 1) {
	cm.dispose();
    }
}