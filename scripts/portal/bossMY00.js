/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1!=a.getPlayer().getClient().getChannel()&&2!=a.getPlayer().getClient().getChannel())return a.playerMessage(5,"\u66b4\u529b\u718a\u4e0e\u5fc3\u75a4\u72ee\u738b\u53ea\u80fd\u57281\u9891\u9053\u548c2\u9891\u9053\u6311\u6218\uff01"),!1;a.haveItem(4032246)?0>=a.getPlayerCount(551030200)?(a.getMap(551030200).resetFully(),a.playPortalSE(),a.warp(551030200,"sp")):0==a.getMap(551030200).getSpeedRunStart()&&(0>=a.getMonsterCount(551030200)||a.getMap(551030200).isDisconnected(a.getPlayer().getId()))?(a.playPortalSE(),a.warp(551030200,"sp")):a.playerMessage(5,"\u6218\u6597\u5df2\u7ecf\u5f00\u59cb\uff0c\u6240\u4ee5\u4f60\u53ef\u80fd\u4e0d\u4f1a\u8fdb\u5165\u8fd9\u4e2a\u5730\u65b9."):a.playerMessage(5,"\u4f60\u6ca1\u6709\u68a6\u5e7b\u4e3b\u9898\u5a03\u5a03\uff0c\u6240\u4ee5\u65e0\u6cd5\u6311\u6218.")};