/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.server.quest);function enter(a){a.isQuestActive(25100)||a.isQuestActive(25101)?(a.isQuestActive(25101)&&MapleQuest.getInstance(25101).forfeit(a.getPlayer()),a.isQuestActive(25100)&&a.completeQuest(25100),a.warp(91501E4,"out00")):a.playerMessage("\u6211\u6ca1\u6709\u4ec0\u4e48\u53ef\u770b\u90a3\u91cc.");return!0};