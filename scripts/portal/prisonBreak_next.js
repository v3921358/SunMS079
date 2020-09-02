/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null!=a.getPlayer().getParty()){var b=!1;switch(a.getMapId()/100){case 9211601:case 9211605:b=!0;break;case 9211602:case 9211604:b=0==a.getMap().getAllMonstersThreadsafe().size();break;case 9211606:b=null!=a.getPlayer().getEventInstance()&&null!=a.getPlayer().getEventInstance().getProperty("kentaSaving")&&a.getPlayer().getEventInstance().getProperty("kentaSaving").equals("0")}b?(a.warpParty(a.getMapId()+100),a.playPortalSE()):a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528.")}else a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528.")};