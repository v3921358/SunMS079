/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u8ba9\u6211\u4eec\u6765\u8ddf\u6211\u8bf4\u8bdd.");qm.forceStartQuest();qm.getPlayer().gainSP(1,5);qm.forceCompleteQuest();qm.dispose()}function end(a,b,c){qm.getPlayer().gainSP(1,5);qm.forceCompleteQuest();qm.dispose()};