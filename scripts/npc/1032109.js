/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function action(a,b,c){1==a?status++:status--;0==status&&(cm.isQuestActive(21764)||cm.forceStartQuest(21764,"1"),cm.sendNext("\u53bb\u627e\u8d6b\u96f7\u5a1c\u8c08\u8c08\u5427!"),cm.dispose())};