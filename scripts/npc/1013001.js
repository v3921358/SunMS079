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
    else if (mode == 0)
        status--;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendNext("你，谁注定是一个龙主人...你有\r\n终于到了.");
    } else if (status == 1) {
        cm.sendNextPrev("去找龙大师履行你的职责...");
    } else if (status == 2) {
        cm.warp(900090101);
        cm.dispose();
    }
}