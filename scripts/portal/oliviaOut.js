/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null!=a.getPlayer().getEventInstance()){var b=parseInt(a.getPlayer().getEventInstance().getProperty("mode"));a.gainExp(0==b?1500:1==b?5500:16E3);a.gainNX(0==b?150:1==b?300:600)}a.warp(682E6,0)};