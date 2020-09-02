/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u67e5\u627e\u7eef\u7ea2\u8272\u6728\u68af\u5f62.");qm.forceStartQuest();qm.isQuestActive(8223)||qm.isQuestFinished(8223)||qm.forceStartQuest(8223);qm.dispose()}function end(a,b,c){qm.isQuestFinished(8223)?(qm.forceCompleteQuest(),qm.sendNext("\u5e72\u5f97\u597d.\u73b0\u5728\u6211\u4eec\u53ef\u4ee5\u7ee7\u7eed.")):qm.sendNext("\u8bf7\u627e\u5230\u5b83!");qm.dispose()};