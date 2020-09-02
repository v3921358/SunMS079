/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function init(){scheduleNew()}function scheduleNew(){em.setProperty("docked","true");em.setProperty("entry","true");em.schedule("stopEntry",24E4);em.schedule("takeoff",3E5)}function stopEntry(){em.setProperty("entry","false")}function takeoff(){em.setProperty("docked","false");em.warpAllPlayer(600010004,600010005);em.warpAllPlayer(600010002,600010003);em.schedule("arrived",6E4)}function arrived(){em.warpAllPlayer(600010005,600010001);em.warpAllPlayer(600010003,103000100);scheduleNew()}function cancelSchedule(){};