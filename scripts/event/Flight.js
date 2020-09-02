/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){scheduleNew()}function scheduleNew(){em.setProperty("docked","true");em.setProperty("entry","true");em.schedule("stopEntry",24E4);em.schedule("takeoff",3E5)}function stopEntry(){em.setProperty("entry","false")}function takeoff(){em.warpAllPlayer(200000132,200090200);em.warpAllPlayer(240000111,200090210);em.broadcastShip(200000131,3);em.broadcastShip(240000110,3);em.setProperty("docked","false");em.schedule("arrived",42E4)}function arrived(){em.warpAllPlayer(200090200,240000100);em.warpAllPlayer(200090210,200000100);em.broadcastShip(200000131,1);em.broadcastShip(240000110,1);scheduleNew()}function cancelSchedule(){};