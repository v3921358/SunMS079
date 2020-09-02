/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){1==a?(status++,0==status?qm.askAcceptDecline("\u5f53\u5fc3\uff0c\u56e0\u4e3a\u4ed6\u4f3c\u4e4e......\u6bd4\u4ee5\u524d\u66f4\u52a0\u5f3a\u5927\u3002\u4e0d\u8981\u4f4e\u4f30\u4ed6!"):1==status&&(qm.forceStartQuest(),qm.dispose())):(qm.sendNext("...\u5b83\u662f\u4ec0\u4e48\uff1f\u554a\uff0c\u6211\u770b\u5230\u4ed6\u7684\u5230\u6765\u975e\u5e38\u63a5\u8fd1!"),qm.dispose())}function end(a,b,c){qm.forceCompleteQuest();qm.dispose()};