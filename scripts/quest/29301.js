/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){200<=qm.getPlayer().getLevel()&&2==(qm.getPlayer().getJob()/100|0)&&qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.canHold(1142010,1)&&!qm.haveItem(1142010,1)&&200<=qm.getPlayer().getLevel()&&2==(qm.getPlayer().getJob()/100|0)&&(qm.gainItem(1142010,1),qm.forceStartQuest(),qm.forceCompleteQuest());qm.dispose()};