/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getSavedLocation("PACH");0>a&&(a=1E8);var a=b.getPlayer().getClient().getChannelServer().getMapFactory().getMap(a),c=a.getPortal("pachinkoDoor");null==c&&(c=a.getPortal(0));b.clearSavedLocation("PACH");b.getPlayer().changeMap(a,c);return!0};