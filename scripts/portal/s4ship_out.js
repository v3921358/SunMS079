/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getEventManager("KyrinTrainingGroundC");if(null==b)return a.warp(120000101,0),!1;if(12E4>b.getInstance("KyrinTrainingGroundC").getTimeLeft())a.warp(912010200,0);else return a.playerMessage("\u8bf7\u7ee7\u7eed\u5fcd\u53d7\u514b\u6797\u7684\u653b\u51fb\u4e00\u4f1a\u513f!"),!1;return!0};