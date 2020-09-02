/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var setupTask;function init(){scheduleNew()}function scheduleNew(){var a=java.util.Calendar.getInstance();a.set(java.util.Calendar.SECOND,5);for(a=a.getTimeInMillis();a<=java.lang.System.currentTimeMillis();)a+=18E5;em.broadcastYellowMsg("\u5192\u9669\u5c9b : \u672c\u670d\u5df2\u81ea\u52a8\u5b58\u6863,\u5982\u6709Lag\u5ef6\u8fdf\u73b0\u8c61\u5b58\u5c5e\u6b63\u5e38");setupTask=em.scheduleAtTimestamp("start",a)}function cancelSchedule(){setupTask.cancel(!0)}function start(){scheduleNew();em.getChannelServer().saveAll();for(var a=em.getInstances().iterator();a.hasNext();)a.next()};