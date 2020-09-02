/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(20201)||1==a.getQuestStatus(20202)||1==a.getQuestStatus(20203)||1==a.getQuestStatus(20204)||1==a.getQuestStatus(20205))if(0==a.getPlayerCount(108000600)){var b=a.getMap(108000600);b.killAllMonsters(!1);b.respawn(!0);a.warp(108000600,0)}else a.playerMessage("\u6709\u4eba\u5df2\u5728\u6b64\u5730\u56fe\u4e2d.");else a.playerMessage("\u7b2c\u4e8c\u6f14\u7ec3\u573a\u4ec5\u9002\u7528\u4e8e\u9002\u5f53\u7684\u4eba\u4fee\u70bc\u804c\u4e1a.")};