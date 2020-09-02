/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(!a.canHold(4001261,1))return a.playerMessage(5,"\u8bf7\u7a7a\u51fa1\u4e2a\u80cc\u5305\u7a7a\u95f4."),!1;a.gainExpR(105100301==a.getPlayer().getMapId()?13E4:26E4);a.gainNX(105100301==a.getPlayer().getMapId()?2E3:3E3);a.gainItem(4001261,1);a.warp(105100100,0);a.playPortalSE()};