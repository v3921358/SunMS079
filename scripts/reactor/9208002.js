/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var b=rm.getEventInstance();if(null!=b){var a=b.getProperty("stage1status");if(null!=a&&!a.equals("waiting")){var c=parseInt(b.getProperty("stage1phase"));a.equals("display")?(a=b.getProperty("stage1combo"),a+=rm.getReactor().getObjectId(),b.setProperty("stage1combo",a),a.length==6*(c+3)&&(b.setProperty("stage1status","active"),rm.mapMessage("\u7ec4\u5408\u5df2\u663e\u793a; \u7ee7\u7eed\u5c0f\u5fc3."),b.setProperty("stage1guess",""))):(a=b.getProperty("stage1guess"),a.length!=6*(c+3)&&(a+=rm.getReactor().getObjectId(),b.setProperty("stage1guess",a)))}}};