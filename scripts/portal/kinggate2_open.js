/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getMap().getReactorByName("kinggate").getState())return a.warp(990000900,2),null!=a.getEventInstance().getProperty("boss")&&a.getEventInstance().getProperty("boss").equals("true")&&a.changeMusic("Bgm10/Eregos"),!0;a.playerMessage("\u8fd9\u6761\u88c2\u7f1d\u4f3c\u4e4e\u88ab\u9644\u8fd1\u7684\u95e8\u6321\u4f4f\u4e86.");return!1};