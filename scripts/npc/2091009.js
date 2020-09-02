/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */function action(a,b,c){map=cm.getPlayer().getMap();cm.isQuestActive(21747)?0>=!cm.getPlayerCount(925040100)?cm.sendOk("\u91cc\u9762\u6709\u4eba\uff0c\u8bf7\u7a0d\u540e!"):(cm.getMap(925040100).resetFully(),cm.warp(925040100,0),cm.getPlayer().startMapTimeLimitTask(600,map)):cm.sendOk("\u4f60\u6ca1\u6709\u63a5\u53d7\u76f8\u5173\u4efb\u52a1\uff0c\u65e0\u6cd5\u8fdb\u5165!");cm.dispose()};