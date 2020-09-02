/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getMap().getReactorByName("kinggate").getState())return a.warp(990000900,1),null!=a.getEventInstance().getProperty("boss")&&a.getEventInstance().getProperty("boss").equals("true")&&a.changeMusic("Bgm10/Eregos"),!0;a.playerMessage("\u8fd9\u95e8\u662f\u5173\u95ed\u7684.");return!1};