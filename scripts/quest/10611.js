/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){if(1==a)status++;else{if(0==status){qm.dispose();return}status--}0==status?qm.sendNext("\u6697\u5f71\u53cc\u5200\u8fbe\u5230\u4e8630\u7ea7\uff01\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n\r\n#i3800008# \u732b\u5934\u9e70\u56fe\u6807 1\u4e2a\u3002\r\n\r\n#i2040121# #t2040121# 1\u4e2a\u3002"):1==status&&(qm.isQuestFinished(10611)||(qm.sendOk("\u9886\u53d6\u6210\u529f\u4e86\u3002"),qm.gainItem(2040121,1),qm.gainItem(3800008,1),qm.forceCompleteQuest()),qm.dispose())};