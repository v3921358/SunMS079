/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (cm.getPlayer().getLevel() < 120) {
	cm.sendOk("现在离开..你受伤之前.");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你出现强劲。你想前往蝙蝠魔殿?");
    } else if (status == 1) {
	cm.warp(105100100);
	cm.dispose();
    }
}