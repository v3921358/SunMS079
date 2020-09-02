/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var baseid=105040304,dungeonid=105040320,dungeons=30;function enter(a){if(a.getMapId()==baseid){for(var b=0;b<dungeons;b++)if(0==a.getPlayerCount(dungeonid+b))return a.warp(dungeonid+b,0),!0;a.playerMessage(5,"\u6b63\u5728\u4f7f\u7528\u4e2d\uff0c\u8bf7\u7a0d\u540e\u518d\u5c1d\u8bd5\u3002")}else a.warp(baseid,"MD00");return!0};