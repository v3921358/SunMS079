/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(org.rise.server.maps);importPackage(org.rise.net.channel);importPackage(org.rise.tools);function enter(a){var b=a.getPlayer().getEventInstance(),c=b.getMapInstance(670010400),d=c.getPortal("st00");if(null==b.getProperty("2stageclear"))return a.getClient().getSession().write(MaplePacketCreator.serverNotice(6,"\u8fd9\u6247\u95e8\u662f\u5173\u95ed\u7684.")),!1;a.getPlayer().changeMap(c,d);return!0};