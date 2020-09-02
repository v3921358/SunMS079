/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){0<qm.getPlayer().getCurrentRep()&&qm.getPlayer().getTotalRep()>qm.getPlayer().getCurrentRep()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u5e72\u5f97\u597d!")):qm.sendNext("\u8bf7\uff0c\u5f97\u5230\u4e00\u4e9b\u4ee3\u8868!");qm.dispose()}function end(a,b,c){0<qm.getPlayer().getCurrentRep()&&qm.getPlayer().getTotalRep()>qm.getPlayer().getCurrentRep()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u5e72\u5f97\u597d!")):qm.sendNext("\u8bf7\uff0c\u5f97\u5230\u4e00\u4e9b\u4ee3\u8868!");qm.dispose()};