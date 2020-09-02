/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getPlayer().getEventInstance(),b=a.getPlayers(),c=Integer.parseInt(a.getProperty("openedDoors")),d=a.getMapFactory().getMap(670010600);a.setProperty("openedDoors",c+1);for(a=0;a<b.size();a++)b.get(a).changeMap(d,d.getPortal(2*(c+1)))};