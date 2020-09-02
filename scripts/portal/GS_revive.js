/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getPlayer().getEventInstance();null!=a&&null!=b.getPlayer().getCarnivalParty()&&(0==b.getPlayer().getCarnivalParty().getTeam()?a.getProperty("Red_Stage").equals("B")?b.warp(a.getMapInstance(5).getId(),1):a.getProperty("Red_Stage").equals("BC")?b.warp(a.getMapInstance(0).getId(),1):b.warp(a.getMapInstance(5+parseInt(a.getProperty("Red_Stage"))).getId(),1):a.getProperty("Blue_Stage").equals("B")?b.warp(a.getMapInstance(5).getId(),1):a.getProperty("Blue_Stage").equals("BC")?b.warp(a.getMapInstance(0).getId(),1):b.warp(a.getMapInstance(10+parseInt(a.getProperty("Blue_Stage"))).getId(),1))};