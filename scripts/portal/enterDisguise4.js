/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(20301)||1==a.getQuestStatus(20302)||1==a.getQuestStatus(20303)||1==a.getQuestStatus(20304)||1==a.getQuestStatus(20305))if(0==a.getPlayerCount(913002300))if(a.haveItem(4032179,1)){a.removeNpc(913002300,1104103);var b=a.getMap(913002300);b.killAllMonsters(!1);b.spawnNpc(1104103,new java.awt.Point(-1766,88));a.warp(913002300,0)}else a.playerMessage("\u4f60\u6ca1\u6709\u5723\u5730\u641c\u67e5\u8bc1\u7684\u8bdd\uff0c\u8bf7\u4ece\u5357\u54c8\u7279\u5f97\u5230\u5b83.");else a.playerMessage("\u68ee\u6797\u5df2\u88ab\u5176\u4ed6\u4eba\u641c\u7d22\u3002 \u66f4\u597d\u5730\u56de\u6765\u4ee5\u540e.");else a.warp(130010120,"out00")};