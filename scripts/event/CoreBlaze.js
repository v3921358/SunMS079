/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){em.setProperty("state","0");em.setProperty("leader","true")}function setup(a,b){em.setProperty("state","1");em.setProperty("leader","true");a=em.newInstance("CoreBlaze"+b);a.setInstanceMap(802000803).resetFully();a.startEventTimer(144E5);return a}function playerEntry(a,b){var c=a.getMapInstance(0);b.changeMap(c,c.getPortal(0))}function playerRevive(a,b){return!1}function scheduledTimeout(a){end(a)}function changedMap(a,b,c){802000803!=c&&(a.unregisterPlayer(b),c=em.getChannelServer().getMapFactory().getMap(802000800),b.changeMap(c,c.getPortal(0)),a.disposeIfPlayerBelow(0,0)&&(em.setProperty("state","0"),em.setProperty("leader","true")))}function playerDisconnected(a,b){return 0}function monsterValue(a,b){return 1}function playerExit(a,b){a.unregisterPlayer(b);a.disposeIfPlayerBelow(0,0)&&(em.setProperty("state","0"),em.setProperty("leader","true"))}function end(a){a.disposeIfPlayerBelow(100,802000800);em.setProperty("state","0");em.setProperty("leader","true")}function clearPQ(a){end(a)}function allMonstersDead(a){a.getMapInstance(0).spawnNpc(9120026,new java.awt.Point(1478,326))}function leftParty(a,b){}function disbandParty(a){}function playerDead(a,b){}function cancelSchedule(){};