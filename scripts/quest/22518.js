/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u8bf7\u6d88\u9664100\u5b62\u5b50.");qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.canHold(2000004,20)&&qm.canHold(2000002,20)&&qm.canHold(4032457,1)?(qm.gainItem(4032457,1),qm.gainItem(2000004,20),qm.gainItem(2000002,20),qm.getPlayer().gainSP(1,0),qm.gainExp(520),qm.forceCompleteQuest()):qm.sendNext("\u8bf7\u7a7a\u51fa\u80cc\u5305\u7a7a\u95f4.");qm.dispose()};