/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.dispose()}function end(a,b,c){0<qm.getPlayer().getMarriageId()&&0<qm.getPlayer().getGuildId()&&0<qm.getPlayer().getJunior1()&&qm.canHold(1142081,1)?(qm.sendNext("\u54c7. \u7ed9\u4f60!"),qm.forceCompleteQuest(),qm.gainItem(1142081,1)):qm.sendNext("\u6211\u4e0d\u8ba4\u4e3a\u4f60\u9002\u5408\u7684\u8981\u6c42\u3002 \u53c2\u52a0\u5a5a\u793c\uff0c\u5bb6\u5ead\u548c\u516c\u4f1a.");qm.dispose()};