/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){1==a?(status++,0==status?qm.sendNext("\u60a8\u597d\uff01\u8fd9\u662f\u4e0d\u662f\u53ea\u662f\u4e00\u4e2a\u65c5\u7a0b\u5b8c\u7f8e\u7684\u5929\u6c14\u600e\u4e48\u6837\uff1f\u6211\u662f\u961f\u957f\uff0c\u8fd9\u6837\u7684\u597d\u8239\u957f\u3002\u4f60\u5fc5\u987b\u662f\u4e00\u4e2a\u65b0\u7684\u6d4f\u89c8\u5668\uff0c\u662f\u5417\uff1f\u5f88\u9ad8\u5174\u8ba4\u8bc6\u4f60."):1==status?qm.sendAcceptDecline("\u6211\u4eec\u8fd8\u6ca1\u6709\u51c6\u5907\u597d\u79bb\u5f00\uff0c\u53ef\u4ee5\u968f\u610f\u770b\u770b\u5468\u56f4\u7684\u8239\u5728\u6211\u4eec\u7b49\u5f85."):2==status&&(qm.forceCompleteQuest(),qm.warp(3E6,0),qm.dispose())):qm.dispose()}function end(a,b,c){qm.dispose()};