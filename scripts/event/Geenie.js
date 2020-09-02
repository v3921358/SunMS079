/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){scheduleNew()}function scheduleNew(){em.setProperty("docked","true");em.setProperty("entry","true");em.schedule("stopEntry",24E4);em.schedule("takeoff",3E5)}function stopEntry(){em.setProperty("entry","false")}function takeoff(){em.warpAllPlayer(200000152,200090400);em.warpAllPlayer(260000110,200090410);em.broadcastShip(200000151,3);em.broadcastShip(260000100,3);em.setProperty("docked","false");em.schedule("arrived",42E4)}function arrived(){em.warpAllPlayer(200090400,260000100);em.warpAllPlayer(200090410,200000100);em.broadcastShip(200000151,1);em.broadcastShip(260000100,1);scheduleNew()}function cancelSchedule(){};