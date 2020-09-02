/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var setupTask;function init(){scheduleNew()}function scheduleNew(){for(var a=java.util.Calendar.getInstance().getTimeInMillis();a<=java.lang.System.currentTimeMillis();)a+=3E5;setupTask=em.scheduleAtTimestamp("start",a)}function cancelSchedule(){setupTask.cancel(!0)}function start(){scheduleNew();em.getChannelServer().AutoNx(1)};