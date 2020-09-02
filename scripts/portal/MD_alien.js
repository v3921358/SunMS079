/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var baseid=22104E4,dungeonid=221040001;function enter(a){try{if(a.getMapId()==baseid){if(null!=a.getParty())if(a.isLeader()){if(0==a.getPlayerCount(dungeonid)){a.warpParty(dungeonid);return}}else a.playerMessage(5,"\u4f60\u4e0d\u662f\u961f\u4f0d\u7684\u961f\u957f.");else if(0==a.getPlayerCount(dungeonid)){a.warp(dungeonid);return}a.playerMessage(5,"\u73b0\u5728\u6b63\u5728\u4f7f\u7528,\u8bf7\u7a0d\u540e\u518d\u8bd5.")}else a.playPortalSE(),a.warp(baseid,"MD00")}catch(b){a.playerMessage("\u9519\u8bef: "+b)}};