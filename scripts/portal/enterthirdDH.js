/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(20601)||1==a.getQuestStatus(20602)||1==a.getQuestStatus(20603)||1==a.getQuestStatus(20604)||1==a.getQuestStatus(20605))if(0==a.getPlayerCount(913010200)){var b=a.getMap(913010200);b.killAllMonsters(!1);b.respawn(!0);a.warp(913010200,0)}else a.playerMessage("\u6709\u4eba\u5df2\u5728\u6b64\u5730\u56fe\u4e2d.");else a.playerMessage("\u7b2c\u4e09\u6f14\u7ec3\u573a\u4ec5\u9002\u7528\u4e8e110\u7ea7\u6b63\u5728\u8bad\u7ec3\u6280\u80fd\u7684\u4eba.")};