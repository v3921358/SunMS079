/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?qm.sendNext("\u662f\u5426\u5df2\u7ecf\u51c6\u5907\u597d\u524d\u8fdb\u6c99\u6f20\u4e86??"):1==status?qm.sendAcceptDecline("\u522b\u602a\u6211\u8bf4\u6ca1\u6709\u8b66\u544a\u4f60..."):2==status&&(qm.forceStartQuest(),qm.dispose()))}function end(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?50>qm.getPlayerStat("HP")?(qm.sendNext("\u6211\u770b\u4f60\u597d\u50cf\u8fd8\u6ca1\u51c6\u5907\u597d\uff0c\u51c6\u5907\u597d\u5728\u6765\u627e\u6211\u5427\u3002"),qm.dispose()):qm.sendNext("\u662f\u5426\u5df2\u7ecf\u51c6\u5907\u597d\u81ea\u5df1\u8d70\u8def\u524d\u8fdb\u6c99\u6f20\u4e86??"):1==status?qm.sendNextPrev("\u6211\u5df2\u7ecf\u8b66\u544a\u8fc7\u4f60."):2==status&&(qm.forceCompleteQuest(),qm.dispose()))};