/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){}function setup(a){var b=em.newInstance("Olivia"+a);b.setProperty("stage","0");b.setProperty("mode",a);a=b.setInstanceMap(682010100+parseInt(a));a.resetFully();a.getPortal(2).setScriptName("oliviaOut");b.startEventTimer(6E5);return b}function playerEntry(a,b){var c=a.getMapInstance(0);b.changeMap(c,c.getPortal(0))}function changedMap(a,b,c){682010100!=c&&682010101!=c&&682010102!=c&&playerExit(a,b)}function playerDisconnected(a,b){return 0}function scheduledTimeout(a){end(a)}function monsterValue(a,b){return 1}function playerExit(a,b){a.unregisterPlayer(b);a.disposeIfPlayerBelow(0,0)&&em.setProperty("state","0")}function allMonstersDead(a){}function end(a){a.disposeIfPlayerBelow(100,682E6)}function playerRevive(a,b){return!1}function clearPQ(a){}function leftParty(a,b){}function disbandParty(a){}function playerDead(a,b){}function cancelSchedule(){};