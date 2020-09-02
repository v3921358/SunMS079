/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(a.haveMonster(9300093)){var b=a.getEventManager("ProtectTylus");if(null==b)a.warp(211000001,0);else if(18E4>b.getInstance("ProtectTylus").getTimeLeft())a.warp(921100301,0);else return a.playerMessage("\u8bf7\u4fdd\u62a4\u5192\u724c\u6cf0\u52d2\u65af\u4e0d\u88ab\u7ed1\u67b6!"),!1}else a.warp(211000001,0),a.playerMessage("\u4e0d\u597d\u4e86\uff01 \u5192\u724c\u6cf0\u52d2\u65af\u88ab\u7ed1\u67b6\uff01");return!0};