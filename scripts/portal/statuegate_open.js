/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getMap().getReactorByName("statuegate").getState()||null!=a.getPlayer().getEventInstance()&&null!=a.getPlayer().getEventInstance().getProperty("stage1clear")&&a.getPlayer().getEventInstance().getProperty("stage1clear").equals("true"))return a.warp(990000301),!0;a.playerMessage("\u5927\u95e8\u662f\u5173\u95ed\u7684\u3002");return!1};