/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null!=a.getPlayer().getParty()){var b=!1;switch(a.getMapId()/10){case 92304010:b=0==a.getMap().getAllMonstersThreadsafe().size();break;case 92304020:b=a.haveItem(2430364,20);break;case 92304030:b=null!=a.getPlayer().getEventInstance()&&null!=a.getPlayer().getEventInstance().getProperty("kentaSaving")&&a.getPlayer().getEventInstance().getProperty("kentaSaving").equals("0")}b?(a.removeAll(2430364),a.warpParty(a.getMapId()+100),a.playPortalSE()):a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528.")}else a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528.")};