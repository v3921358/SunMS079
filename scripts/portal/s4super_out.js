/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getEventManager("KyrinTrainingGroundV");if(null==b)a.warp(120000101,0);else if(12E4>b.getInstance("KyrinTrainingGroundV").getTimeLeft())a.warp(912010200,0);else return a.playerMessage("\u8bf7\u7ee7\u7eed\u5fcd\u53d7\u51ef\u7433\u7684\u653b\u51fb!"),!1;return!0};