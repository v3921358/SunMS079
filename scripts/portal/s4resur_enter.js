/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){a.haveItem(4031448)?a.playerMessage("\u4f60\u5df2\u7ecf\u5b8c\u6210\u4e86\u6b64\u4efb\u52a1."):1==a.getQuestStatus(6134)?(map=a.getPlayer().getMap(),a.warp(92202E4,0),a.getPlayer().startMapTimeLimitTask(1200,map)):a.playerMessage("\u4f60\u5c1a\u672a\u63a5\u53d7\u76f8\u5173\u4efb\u52a1,\u4e0d\u80fd\u8fdb\u5165\u6b64\u5904.");return!0};