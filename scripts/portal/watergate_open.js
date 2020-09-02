/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系方式：2412614144
 =====================
 */function enter(a){if(1==a.getMap().getReactorByName("watergate").getState()||null!=a.getPlayer().getEventInstance()&&null!=a.getPlayer().getEventInstance().getProperty("stage3clear")&&a.getPlayer().getEventInstance().getProperty("stage3clear").equals("true"))return a.removeAll(4001027),a.removeAll(4001028),a.removeAll(4001029),a.removeAll(4001030),a.warp(990000600),!0;a.playerMessage("\u8fd9\u79cd\u524d\u8fdb\u7684\u8def\u8fd8\u6ca1\u6709\u6253\u5f00.");return!1};