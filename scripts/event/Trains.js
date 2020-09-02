/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){scheduleNew()}function scheduleNew(){em.setProperty("docked","true");em.setProperty("entry","true");em.schedule("stopEntry",24E4);em.schedule("takeoff",3E5)}function stopEntry(){em.setProperty("entry","false")}function takeoff(){em.warpAllPlayer(200000122,200090100);em.warpAllPlayer(220000111,200090110);em.broadcastShip(200000121,3);em.broadcastShip(220000110,3);em.setProperty("docked","false");em.schedule("arrived",42E4)}function arrived(){em.warpAllPlayer(200090100,220000110);em.warpAllPlayer(200090110,200000121);em.broadcastShip(200000121,1);em.broadcastShip(220000110,1);scheduleNew()}function cancelSchedule(){};