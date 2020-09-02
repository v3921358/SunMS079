/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function start() {
    var status = cm.getQuestStatus(20706);
    
    if (status == 0) {
        cm.sendNext("它看起来像有什么在该地区的可疑.");
    } else if (status == 1) {
        cm.forceCompleteQuest(20706);
        cm.sendNext("你已经发现了阴影！更好地报告 #p1103001#.");
    } else if (status == 2) {
        cm.sendNext("阴影已经被发现。更好地报告 #p1103001#.");
    }
    cm.dispose();
}
function action(mode, type, selection) {
    cm.dispose();
}