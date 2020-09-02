/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status&&(qm.sendOk("\u662f\u4f4d\u65b0\u7684\u65c5\u884c\u8005\u5427\uff1f\u8fd8\u5f88\u964c\u751f\u5427\uff1f\u6211\u662f\u739b\u4e3d\u4e9a\uff0c\u6253\u5f00(\u5feb\u6377\u952eW)\u5c31\u53ef\u4ee5\u67e5\u770b\u6240\u6709\u5192\u9669\u5c9b\u4e16\u754c\u7684\u6240\u6709\u5730\u56fe"),qm.gainExp(11),qm.forceCompleteQuest(),qm.dispose()))};