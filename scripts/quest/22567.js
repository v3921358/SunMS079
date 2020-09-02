/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("Make a Growth Accelerant.");qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.isQuestFinished(22568)||qm.haveItem(4032468,10)?(qm.getPlayer().gainSP(2,3),qm.forceCompleteQuest()):(qm.sendNext("Make a growth accelerant."),qm.forceStartQuest());qm.dispose()};