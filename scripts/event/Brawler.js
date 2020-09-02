/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){}function playerEntry(a,b){var c=a.setInstanceMap(91204E4);b.changeMap(c,c.getPortal(0));b.getClient().getSession().write(tools.MaplePacketCreator.getClock(600));a.schedule("warpOut",6E5)}function warpOut(a){for(var b=em.getChannelServer().getMapFactory().getMap(120000101),c=a.getPlayers().iterator();c.hasNext();)player=c.next(),player.changeMap(b,b.getPortal(0)),a.unregisterPlayer(player);a.dispose()}function playerDisconnected(a,b){return 0}function playerDead(a,b){}function playerRevive(a,b){a.unregisterPlayer(b);a.dispose()}function leftParty(a,b){}function disbandParty(a){}function cancelSchedule(){};