/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){map=a.getPlayer().getMap();if(a.isQuestActive(21737))return a.warp(200060001,0),!0;a.isQuestActive(21739)?0>=a.getPlayerCount(920030001)?(a.getMap(920030001).resetFully(),a.warp(920030001,0),a.getPlayer().startMapTimeLimitTask(600,map)):a.playerMessage(5,"\u91cc\u9762\u6709\u4eba,\u8bf7\u7a0d\u540e..."):a.playerMessage(5,"\u6ca1\u6709\u63a5\u53d7\u76f8\u5173\u4efb\u52a1...");return!0};