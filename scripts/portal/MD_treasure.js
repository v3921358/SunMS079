/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var baseid=251010402,dungeonid=251010410,dungeons=30;function enter(a){if(a.getMapId()==baseid)if(10>a.getPlayer().getFame())a.playerMessage(5,"\u4f60\u9700\u898110\u540d\u624d\u80fd\u8fdb\u5165.");else{if(null!=a.getParty())if(a.isLeader())for(var b=0;b<dungeons;b++){if(0==a.getPlayerCount(dungeonid+b))return a.warpParty(dungeonid+b),!0}else return a.playerMessage(5,"\u4f60\u4e0d\u662f\u961f\u4f0d\u7684\u961f\u957f."),!1;else for(b=0;b<dungeons;b++)if(0==a.getPlayerCount(dungeonid+b))return a.warp(dungeonid+b),!0;a.playerMessage(5,"\u73b0\u5728\u6b63\u5728\u4f7f\u7528,\u8bf7\u7a0d\u540e\u518d\u8bd5.");return!1}else return a.playPortalSE(),a.warp(baseid,"MD00"),!0};