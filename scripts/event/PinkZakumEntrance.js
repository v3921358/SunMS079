/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){}function setup(a){a=em.newInstance("PinkZakumEntrance"+a);beginQuest(a);return a}function beginQuest(a){null!=a&&a.startEventTimer(6E4)}function playerEntry(a,b){var c=a.getMapInstance(0);b.changeMap(c,c.getPortal(0))}function changedMap(a,b,c){a.unregisterPlayer(b);a.disposeIfPlayerBelow(0,0)}function scheduledTimeout(a){a.disposeIfPlayerBelow(100,689013E3);a.dispose()}function allMonstersDead(a){}function playerDead(a,b){}function playerRevive(a,b){return!0}function playerDisconnected(a,b){return 0}function monsterValue(a,b){return 0}function leftParty(a,b){}function disbandParty(a,b){}function clearPQ(a){}function removePlayer(a,b){a.dispose()}function registerCarnivalParty(a,b){}function onMapLoad(a,b){}function cancelSchedule(){};