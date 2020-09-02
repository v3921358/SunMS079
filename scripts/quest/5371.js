/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){0<a?(status++,0==status?qm.sendAcceptDecline("\u4f60\u786e\u5b9a\u8981\u9886\u52cb\u7ae0\u4e86\u5417??"):1==status&&(qm.sendOk("\u606d\u559c\u4f60\u5b8c\u6210\u3002"),qm.gainItem(1142103,1),qm.forceCompleteQuest(),qm.dispose())):qm.dispose()};