/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){a.haveItem(4000381)?0>=a.getPlayerCount(541010100)?(a.getMap(541010100).resetFully(),a.playPortalSE(),a.warp(541010100,"sp")):0==a.getMap(541010100).getSpeedRunStart()&&(0>=a.getMonsterCount(541010100)||a.getMap(541010100).isDisconnected(a.getPlayer().getId()))?(a.playPortalSE(),a.warp(541010100,"sp")):a.playerMessage(5,"\u6218\u6597\u5df2\u7ecf\u5f00\u59cb\uff0c\u6240\u4ee5\u4f60\u4e0d\u80fd\u8fdb\u5165\u8fd9\u4e2a\u5730\u65b9."):a.playerMessage(5,"\u4f60\u6ca1\u6709\u767d\u7cbe\u534e.")};