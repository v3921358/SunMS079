/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u54c7. \u7b49\u4e00\u7b49\uff0c\u800c\u6211\u8fd9\u4e2a\u7ffb\u8bd1..");qm.haveItem(4032032,1)&&(qm.gainItem(4032032,-1),qm.forceStartQuest());qm.dispose()}function end(a,b,c){qm.sendNext("\u7ed9\u4f60!");qm.canHold(4032018,1)&&(qm.gainItem(4032018,1),qm.forceCompleteQuest());qm.dispose()};