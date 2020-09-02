/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */importPackage(net.sf.Start.client);var status=-1;function end(a,b,c){-1==a?qm.dispose():(1==a?status++:status--,0==status?qm.sendNext("\u795d\u8d3a\u4f60\u52aa\u529b\u5347\u7ea7\u3002\u4f5c\u4e3a\u5347\u7ea7\u7684\u795d\u8d3a\uff0c\u6211\u9001\u4f60\u4e00\u4e2a\u67ab\u53f6\u3002\u5e0c\u671b\u4f60\u80fd\u5728#r#e\u5192\u9669\u5c9b#k#k\u5ea6\u8fc7\u6109\u5feb\u7684\u4e00\u5929\uff5e\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v4001126# 29\u4e2a"):1==status&&(qm.forceCompleteQuest(9981),qm.gainItem(4001126,29),qm.dispose()))};