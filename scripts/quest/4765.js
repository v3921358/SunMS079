/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?2==qm.getQuestStatus(4765)?(qm.sendOk("\u4f60\u5df2\u7ecf\u9886\u53d6\u8fc7\u5956\u52b1\uff0c\u7ee7\u7eed\u52aa\u529b\u523040\u7ea7\u53ef\u4ee5\u83b7\u5f97\u66f4\u591a\u5956\u52b1\u5594"),qm.completeQuest(),qm.dispose()):qm.sendNext("\u606d\u559c\u4f60\u5f53\u524d\u7b49\u7ea7\u5df2\u7ecf\u5230\u8fbe#b30#k\u7ea7\u3002"):1==status&&(qm.sendOk("\u606d\u559c\u4f60\u83b7\u5f97\u7cfb\u7edf\u5956\u52b1\uff01\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022345# x2 #v5390000# x2 #v3010009# x 1  \u62b5\u7528\u5377 x500 "),qm.gainItem(2022345,2),qm.gainItem(539E4,2),qm.gainItem(3010009,1),qm.gainD(500),qm.completeQuest(),qm.dispose()))};