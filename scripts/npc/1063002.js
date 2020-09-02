/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var itemSet = new Array(4020007, 4020008, 4010006);
var rand = Math.floor(Math.random() * itemSet.length);

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(105040300);
            
	if (cm.getQuestStatus(2054) == 1 && !cm.haveItem(4031028)) {
	    cm.gainItem(4031028, 30);
	} else {
	    cm.gainItem(itemSet[rand], 2);
	}
    }
    cm.dispose();
}