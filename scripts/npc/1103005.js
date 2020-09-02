/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

 function action(mode, type, selection) {
    if (cm.getPlayer().getLevel() >= 10) {
        cm.warp(130000000, 0);
    } else {
        cm.warp(130030000, 0);
    }
    cm.dispose();
}