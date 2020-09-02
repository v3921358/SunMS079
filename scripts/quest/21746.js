/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){map=qm.getPlayer().getMap();0>=qm.getPlayerCount(925040001)?(qm.getMap(925040001).resetFully(),qm.warp(925040001,1),qm.getPlayer().startMapTimeLimitTask(1200,map),qm.forceStartQuest(21746)):qm.sendNextS("\u91cc\u9762\u6709\u4eba\u6682\u65f6\u65e0\u6cd5\u8fdb\u5165,\u8bf7\u7a0d\u540e\u3002\u3002",3);qm.dispose()}function end(a,b,c){qm.forceCompleteQuest(21746);qm.dispose()};