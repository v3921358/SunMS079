/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null==a.getEventInstance())a.warp(101030104);else if(null!=a.getEventInstance().getProperty("canEnter")&&a.getEventInstance().getProperty("canEnter").equals("true"))a.warp(990000100);else return a.playerMessage("\u8be5\u95e8\u5c1a\u672a\u6253\u5f00.\u8bf7\u8ba9\u53c2\u8d5b\u8005\u90fd\u5728\u6b64\u7b49\u5f85!"),!1;return!0};