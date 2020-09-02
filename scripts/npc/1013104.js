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
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if(status == 0){
			if(cm.isQuestActive(22007)){
				cm.sendNext("#b(您在蛋获得。它送到犹他.)#k");
				cm.gainItem(4032451, 1);
			}else{
				cm.sendOk("#b您不必现在就取一个鸡蛋.#k");
			}
			cm.dispose();
		}
	}
}