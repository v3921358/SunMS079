/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u53bb\u8c08\u8bdd \u9996\u5e2dLeafre\u7684Tatamo.");qm.forceStartQuest();qm.getPlayer().gainSP(1,2);qm.forceCompleteQuest();qm.dispose()}function end(a,b,c){qm.getPlayer().gainSP(1,2);qm.forceCompleteQuest();qm.dispose()};