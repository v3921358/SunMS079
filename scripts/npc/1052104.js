/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 var status = 0

function start(){
	action(1, 0, 0);
}

function action(mode, type ,selection){//1050122 1051130
	if(mode == 1) {
		status++;
	} else if(mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if(status == 1){
		cm.openShop(112);
		cm.dispose();
	} else {
		cm.dispose();
	}
}