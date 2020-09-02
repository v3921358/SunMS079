/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(20611)||1==a.getQuestStatus(20612)||1==a.getQuestStatus(20613)||1==a.getQuestStatus(20614)||1==a.getQuestStatus(20615))if(0==a.getPlayerCount(913020300)){var b=a.getMap(913020300);b.killAllMonsters(!1);b.respawn(!0);a.warp(913020300,0)}else a.playerMessage("\u6709\u4eba\u5df2\u5728\u6b64\u5730\u56fe\u4e2d.");else a.playerMessage("\u7b2c\u56db\u6f14\u7ec3\u573a\u4ec5\u9002\u7528\u4e8e110\u7ea7\u6b63\u5728\u8bad\u7ec3\u6280\u80fd\u7684\u4eba.")};