/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getSavedLocation("ARDENTMILL");b.clearSavedLocation("ARDENTMILL");0>a&&(a=1E8);var a=b.getMap(a),c=a.getPortal("profession");null==c&&(c=a.getPortal(0));b.getMapId()!=a&&b.getPlayer().changeMap(a,c)};