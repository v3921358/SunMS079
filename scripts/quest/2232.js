/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){0<qm.getPlayer().getJunior1()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u68d2\u6781\u4e86\uff01\uff01")):qm.sendNext("\u8bf7\u627e\u4e00\u4e2a\u5f92\u5f1f\u6765\u89c1\u6211\uff01");qm.dispose()}function end(a,b,c){0<qm.getPlayer().getJunior1()?(qm.forceCompleteQuest(),qm.gainExp(3E3),qm.sendNext("\u68d2\u6781\u4e86\uff01\uff01")):qm.sendNext("\u8bf7\u627e\u4e00\u4e2a\u5f92\u5f1f\u6765\u89c1\u6211\uff01");qm.dispose()};