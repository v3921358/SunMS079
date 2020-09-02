/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getSavedLocation("MULUNG_TC");b.clearSavedLocation("MULUNG_TC");0>a&&(a=102E6);var c=b.getMap(a),a=23E7==a?c.getPortal("market01"):c.getPortal("market00");null==a&&(a=c.getPortal(0));b.getMapId()!=c&&b.getPlayer().changeMap(c,a)};