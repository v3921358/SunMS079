/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){0<qm.getPlayer().getCurrentRep()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u5e72\u5f97\u597d!")):qm.sendNext("Please, get some Rep!");qm.dispose()}function end(a,b,c){0<qm.getPlayer().getCurrentRep()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u5e72\u5f97\u597d!")):qm.sendNext("Please, get some Rep!");qm.dispose()};