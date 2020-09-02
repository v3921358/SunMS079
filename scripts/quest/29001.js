/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){}function end(a,b,c){qm.canHold(1142002,1)&&!qm.haveItem(1142002,1)&&800<=qm.getPlayer().getNumQuest()&&(qm.gainItem(1142002,1),qm.forceStartQuest(),qm.forceCompleteQuest());qm.dispose()};