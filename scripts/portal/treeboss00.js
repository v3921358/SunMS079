/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(0>=a.getPlayerCount(541020800))return a.getMap(541020800).resetFully(),a.playPortalSE(),a.warp(541020800,"sp"),!0;if(0==a.getMap(541020800).getSpeedRunStart()&&(0>=a.getMonsterCount(541020800)||a.getMap(541020800).isDisconnected(a.getPlayer().getId())))return a.playPortalSE(),a.warp(541020800,"sp"),!0;a.playerMessage(5,"\u6218\u6597\u5df2\u7ecf\u5f00\u59cb\u4e86\uff0c\u6240\u4ee5\u4f60\u53ef\u80fd\u65e0\u6cd5\u8fdb\u5165\u8fd9\u4e2a\u5730\u65b9.");return!1};