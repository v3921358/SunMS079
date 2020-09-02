/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){map=a.getPlayer().getMap();if(1==a.getQuestStatus(6240)||1==a.getQuestStatus(6241))if(a.haveItem(4001113))a.playerMessage("\u4f60\u5df2\u7ecf\u6709\u51e4\u51f0\u7684\u86cb\u3002\u4f60\u4e0d\u80fd\u8fdb\u5165.");else{if(0==a.getPlayerCount(921100200))return a.playPortalSE(),a.warp(921100200,0),a.getPlayer().startMapTimeLimitTask(300,map),!0;a.playerMessage("\u5176\u4ed6\u4eba\u7269\u90fd\u5728\u8bf7\u6c42\u3002\u4f60\u4e0d\u80fd\u8fdb\u5165.")}else if(2==a.getQuestStatus(6240)&&0==a.getQuestStatus(6241))if(a.haveItem(4001113))a.playerMessage("\u4f60\u5df2\u7ecf\u6709\u51e4\u51f0\u7684\u86cb\u3002\u4f60\u4e0d\u80fd\u8fdb\u5165.");else return a.playPortalSE(),a.warp(921100200,0),a.getPlayer().startMapTimeLimitTask(300,map),!0;else a.playerMessage("\u4f60\u4e0d\u80fd\u8fdb\u5165\u5bc6\u5c01\u5904.");return!1};