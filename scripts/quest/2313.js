/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.client);var status=-1;function start(a,b,c){status++;if(1!=a)if(1==b&&0==a)status-=2;else{qm.sendOk("\u6ca1\u6709\u592a\u591a\u7684\u65f6\u95f4\u3002\u8bf7\u5feb\u70b9.");qm.dispose();return}0==status&&qm.sendAcceptDecline("\u6211\u5df2\u7ecf\u544a\u8bc9\u6211\u4eec#b\u5185\u653f\u90e8\u957f#k \u7684\u80fd\u529b\u3002 \u8bf7\u7acb\u5373\u53bb\u62dc\u8bbf\u4ed6.");1==status&&(qm.forceStartQuest(),qm.sendOk("\u62ef\u6551\u6211\u4eec\u7684\u738b\u56fd\uff01 \u6211\u4eec\u76f8\u4fe1\u4f60!"),qm.dispose())}function end(a,b,c){status++;if(1!=a)if(1==b&&0==a)status-=2;else{qm.dispose();return}0==status&&qm.forceCompleteQuest();qm.gainExp(4E3);qm.dispose()};