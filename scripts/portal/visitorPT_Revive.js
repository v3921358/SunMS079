/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getPlayer().getEventInstance();null==a||null==a.getProperty("current_instance")||a.getProperty("current_instance").equals("0")?b.warp(502029E3,0):b.warp(parseInt(a.getProperty("current_instance")),0)};