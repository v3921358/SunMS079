/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(23120))if(0==a.getPlayerCount(931000420)){var b=a.getMap(931000420);b.resetFully();a.warp(931000420,0)}else a.playerMessage("\u6b63\u5728\u88ab\u5176\u4ed6\u4eba\u641c\u7d22\u3002 \u66f4\u597d\u5730\u56de\u6765\u4ee5\u540e.");else 1==a.getQuestStatus(23023)||1==a.getQuestStatus(23024)||1==a.getQuestStatus(23025)?0==a.getPlayerCount(931000100)?(a.removeNpc(931000100,2159100),b=a.getMap(931000100),b.killAllMonsters(!1),b.spawnNpc(2159100,new java.awt.Point(-157,-23)),a.warp(931000100,0)):a.playerMessage("\u6b63\u5728\u88ab\u5176\u4ed6\u4eba\u641c\u7d22\u3002 \u66f4\u597d\u5730\u56de\u6765\u4ee5\u540e."):a.warp(310000010,0)};