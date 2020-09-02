/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(a.isQuestActive(20033))for(var c=!0,b=913070020;913070039>=b;b++){if(0==a.getPlayerCount(b))return 1<=a.itemQuantity(4033196)&&a.gainItem(4033196,-a.itemQuantity(4033196)),a.resetMap(b),a.warp(b,0),!0}else return a.topMsg("\u6797\u4f2f\u7279\u60f3\u89c1\u4f60."),!1;c&&a.getPlayer().dropMessage(5,"\u6709\u4eba\u5df2\u5728\u6b64\u5730\u56fe\u4e2d.");return!0};