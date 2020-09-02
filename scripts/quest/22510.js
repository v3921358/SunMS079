/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.canHold(4032455,1)?(qm.sendNext("\u53bb\u5b9e\u73b0\u8fd9Henesys\u9996\u5e2d\u65af\u5766."),qm.gainItem(4032455,1),qm.forceStartQuest()):qm.sendNext("\u8bf7\u6709\u4e00\u4e9b\u7a7a\u95f4.");qm.dispose()}function end(a,b,c){qm.haveItem(4032455,1)?(qm.sendNext("\u8c22\u8c22."),qm.getPlayer().gainSP(1,0),qm.gainExp(450),qm.gainItem(4032455,-1),qm.forceCompleteQuest()):qm.sendNext("\u8bf7\u7ed9\u6211\u7684\u4fe1.");qm.dispose()};