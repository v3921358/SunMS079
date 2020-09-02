/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u54c7. \u628a\u8fd9\u4e2a\u7ed9\u6770\u514b!");qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.sendNext("\u8c22\u8c22!");qm.haveItem(4032018,1)&&(qm.gainItem(4032018,-1),qm.forceCompleteQuest());qm.dispose()};