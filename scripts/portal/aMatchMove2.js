/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getSavedLocation("ARIANT");0>b&&(b=91E7);a.playPortalSE();a.clearSavedLocation("ARIANT");a.warp(b,0);return!0};