/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(java.lang);function init(){}function setup(a,b){for(var c=925020100;925020114>=c;c++){var d=em.getProperty(String.valueOf(c));if(null==d||d.equals("not"))return em.setProperty(""+c,"entered"),a=em.newInstance("dojoparty"+b),a.setProperty("select",String.valueOf(c)),a}a.disposeIfPlayerBelow(100,0);return a}function playerEntry(a,b){var c=em.getMapFactory().getMap(parseInt(a.getProperty("select")));b.changeMap(c,c.getPortal(0))}function changedMap(a,b,c){}function scheduledTimeout(a){}function allMonstersDead(a){}function playerDead(a,b){}function playerRevive(a,b){}function playerDisconnected(a,b){}function monsterValue(a,b){}function leftParty(a,b){}function disbandParty(a,b){}function clearPQ(a){}function removePlayer(a,b){}function registerCarnivalParty(a,b){}function onMapLoad(a,b){}function cancelSchedule(){};