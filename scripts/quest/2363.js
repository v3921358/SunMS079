/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function end(a,b,c){qm.forceCompleteQuest();400==qm.getJob()&&(qm.changeJob(430),qm.expandInventory(1,4),qm.expandInventory(2,4),qm.expandInventory(3,4),qm.expandInventory(4,4),qm.gainItem(1342E3,1),qm.sendNext("\u4f60\u73b0\u5728\u662f\u4e00\u4e2a\u53cc\u5200."));qm.dispose()}function start(a,b,c){qm.dispose()};