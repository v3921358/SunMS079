/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.forceStartQuest();qm.dispose()}function end(a,b,c){status++;0==status?qm.sendNext("\u6211\u7684\u773c\u955c\u5728\u54ea\u91cc\uff0c\u554a\u627e\u5230\u4e86\uff01\u8c22\u8c22\u4f60\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2030019# 5 #t2030019#s\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0#  1000 \u7ecf\u9a8c\u503c"):(qm.gainItem(2030019,5),qm.gainExp(1E3),qm.forceCompleteQuest(),qm.dispose())};