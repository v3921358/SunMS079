/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=0;if(1==a.getQuestStatus(20774))b=913070330;else if(1==a.getQuestStatus(20775)||1==a.getQuestStatus(20776))b=913070340;if(0<b)if(0==a.getPlayerCount(b)){var c=a.getMap(b);c.resetFully();c.respawn(!0);a.warp(b,0)}else a.playerMessage("\u6709\u4eba\u5df2\u5728\u6b64\u5730\u56fe\u4e2d.");else a.playerMessage("\u4f60\u4e0d\u80fd\u8fdb\u5165.")};