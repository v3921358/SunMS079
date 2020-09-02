/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.forceStartQuest();qm.sendNextS("\u5411#p1002104#\u8bf4\u660e\u4e86#m200000000#\u53d1\u751f\u7684\u6240\u6709\u4e8b\u60c5\u4e4b\u540e\uff0c#p1002104#\u8bf4\u6700\u597d\u628a\u8fd9\u4e9b\u4e8b\u60c5\u544a\u8bc9#b#p1201000##k\u3002\u3002",3);qm.dispose()}function end(a,b,c){qm.teachSkill(21100004,qm.getPlayer().getSkillLevel(21100004),20);qm.forceCompleteQuest();qm.sendNextS("#r\u6597\u6c14\u638c\u63e1#k\u6280\u80fd\u5c31\u4ea4\u7ed9\u4f60\u4e86!",3);qm.dispose()};