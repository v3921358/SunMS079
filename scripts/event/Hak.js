/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var returnTo=[200000141,250000100],rideTo=[250000100,200000141],birdRide=[200090300,200090310],myRide,returnMap,map,docked,timeOnRide=60;function init(){em.setProperty("isRiding","false")}function playerEntry(b,a){myRide=em.getProperty("myRide");docked=em.getChannelServer().getMapFactory().getMap(rideTo[myRide]);returnMap=em.getChannelServer().getMapFactory().getMap(returnTo[myRide]);onRide=em.getChannelServer().getMapFactory().getMap(birdRide[myRide]);em.setProperty("isRiding","true");em.schedule("timeOut",1E3*timeOnRide);a.changeMap(onRide,onRide.getPortal(0));a.getClient().getSession().write(tools.MaplePacketCreator.getClock(timeOnRide))}function playerDisconnected(b,a){return 0}function cancelSchedule(){};