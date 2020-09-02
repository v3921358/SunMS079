/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getSavedLocation("MULUNG_TC");0>b&&(b=1E8);a.playPortalSE();a.clearSavedLocation("MULUNG_TC");a.warp(b,0);return!0};