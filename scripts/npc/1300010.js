/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
		if(status == 0 && mode == 0){
			cm.sendNext("您已取消使用的项目.");
			cm.gainItem(2430014, 1);
			cm.dispose();
		}
		if (mode == 1)
            status++;
        else
            status--;
		}
	if(status == 0){
		cm.sendYesNo("你要使用#b杀手孢菇#k?....#e#r* 注意#n..请不直接对人体申请！..如果误食，请就近看病!");
	}if(status == 1)
		cm.PlayerToNpc("真棒，屏障被打破!!!");
	if(status == 2){
		cm.playerMessage("蘑菇森林屏障已被删除，并渗透.");
		cm.dispose();
	}
}
			