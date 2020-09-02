/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(org.server.maps);function enter(b){var a=b.getSavedLocation("SLEEP");b.clearSavedLocation("SLEEP");0>a&&(a=102E6);var a=b.getMap(a),c=a.getPortal("inn00");null==c&&(c=a.getPortal(0));b.getPlayer().changeMap(a,c);return!0};