/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?2==qm.getQuestStatus(4760)?(qm.sendOk("\u4f60\u5df2\u7ecf\u9886\u53d6\u8fc7\u5956\u52b1\uff0c\u7ee7\u7eed\u52aa\u529b\u52308\u7ea7\u53ef\u4ee5\u83b7\u5f97\u66f4\u591a\u5956\u52b1\u5594"),qm.completeQuest(),qm.dispose()):qm.sendNext("\u606d\u559c\u4f60\u5f53\u524d\u7b49\u7ea7\u5df2\u7ecf\u5230\u8fbe#b5#k\u7ea7\u3002"):1==status&&(qm.sendOk("\u606d\u559c\u4f60\u83b7\u5f97\u7cfb\u7edf\u5956\u52b1\uff01\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2000000# x 50\u4e2a #v2010009# x 50\u4e2a"),qm.gainItem(2E6,50),qm.gainItem(2010009,50),qm.completeQuest(),qm.dispose()))};