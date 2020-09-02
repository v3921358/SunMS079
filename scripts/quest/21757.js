/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.forceStartQuest();qm.removeAll(4032330);qm.gainItem(4032330,1);qm.dispose()}function end(a,b,c){qm.forceCompleteQuest();qm.removeAll(4032330);qm.gainExp(1E3);qm.sendNextS("\u6211\u4f1a\u5bf9\u4fe1\u7684\u5185\u5bb9\u8fdb\u884c\u614e\u91cd\u7684\u8ba8\u8bba\u548c\u7814\u7a76\u3002\u600e\u4e48\u770b\u4e0a\u53bb\u6709\u70b9\u4e0d\u592a\u53ef\u9760\u554a\uff1f",3);qm.dispose()};