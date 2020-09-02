/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?qm.sendAcceptDecline("\u6b63\u503c\u6691\u5047\u671f\u95f4\uff0c\u6211\u51c6\u5907\u4e86\u5f88\u591a\u5feb\u901f\u95ee\u7b54\u9898\u3002\u4f60\u8981\u4e0d\u8981\u53c2\u52a0\uff1f"):1==status?(qm.forceStartQuest(9991),qm.sendNext("\u4e0d\u8fc7\u6211\u73b0\u5728\u6ca1\u65f6\u95f4\uff0c\u4f60\u8fc7\u4e00\u4f1a\u518d\u6765\u627e\u6211\u5427\u3002")):2==status&&qm.dispose())};