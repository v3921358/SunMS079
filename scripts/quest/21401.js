/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){0>=qm.getPlayerCount(91402E4)?(qm.forceStartQuest(),qm.getMap(91402E4).resetFully(),qm.warp(91402E4,0)):qm.sendOk("\u91cc\u9762\u6709\u4eba!");qm.dispose()}function end(a,b,c){qm.changeJob(2112);qm.gainItem(1142132,1);qm.forceCompleteQuest();qm.dispose()};