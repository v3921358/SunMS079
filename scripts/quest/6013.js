/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){}function end(a,b,c){200<=qm.getPlayer().getLevel()&&5==qm.getPlayer().getJob()/100&&(qm.gainItem(1902002,1),qm.forceStartQuest(),qm.forceCompleteQuest());qm.dispose()};