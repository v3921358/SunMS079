/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getPlayer().getEventInstance(),c=b.getMapInstance(922010900),d=c.getPortal("st00");if(null==b.getProperty("8stageclear"))return a.getPlayer().dropMessage(5,"\u4e00\u4e9b\u5bc6\u5c01\u963b\u585e\u8fd9\u6247\u95e8."),!1;a.getPlayer().changeMap(c,d);return!0};