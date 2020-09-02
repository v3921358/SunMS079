/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status&&(qm.sendNext("\u54e6\uff0c\u6211\u7684\u5929\u54ea\uff0c\u4f60\u4ece\u6211\u4eec\u7b2c\u4e00\u6b21\u89c1\u9762\u540e\u5c31\u6210\u957f\u4e86\uff01 \u4f60\u5931\u53bb\u4e86\u4f60\u7684\u56de\u5fc6\uff1f \u6211\u4f1a\u7167\u987e\u7684."),qm.forceCompleteQuest(),qm.forceCompleteQuest(3507),qm.dispose()))}function end(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status&&(qm.sendNextPrev("Test"),qm.dispose()))};