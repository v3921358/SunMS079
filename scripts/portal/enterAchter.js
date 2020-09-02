/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){map=a.getPlayer().getMap();a.isQuestFinished(21754)?a.warp(100000201,1):0>=!a.getPlayerCount(91005E4)&&a.isQuestFinished(21754)?a.playerMessage(5,"\u91cc\u9762\u6709\u4eba,\u8bf7\u7a0d\u540e."):a.isQuestFinished(21753)?(a.getMap(91005E4).resetFully(),a.warp(91005E4,1),a.getPlayer().startMapTimeLimitTask(600,map)):a.warp(100000201,1)};