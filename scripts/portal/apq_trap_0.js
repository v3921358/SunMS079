/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(4<=a.getPlayer().getMap().getReactorByName("gate00").getState())return a.warp(670010600,2),!0;a.getClient().getSession().write(org.rise.tools.MaplePacketCreator.serverNotice(5,"\u95e8\u662f\u5173\u95ed\u7684."));return!1};