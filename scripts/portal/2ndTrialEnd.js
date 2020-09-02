/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(java.lang);function enter(a){if(a.haveItem(4031013,20)){a.getPlayer().setKeyValue("2ndJobTrialComplete","1");a.removeAll(4031013);a.warp(1E8);var b=a.getPlayer().getKeyValue("2ndTrialStartTime");a.getPlayer().setKeyValue2("2ndJobTrialCompleteTime2",System.currentTimeMillis()/1E3-b+"");return!0}a.getPlayer().message(5,"\u4f60\u5df2\u7ecf\u5b8c\u6210\u4e86\u4efb\u52a1.");return!1};