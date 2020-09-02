/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */var baseid=105090311,dungeonid=105090320,dungeons=1;function enter(a){if(a.getMapId()==baseid){for(var b=0;b<dungeons;b++)if(0==a.getPlayerCount(dungeonid+b))return a.warp(dungeonid+b,0),!0;a.playerMessage(5,"\u6240\u6709\u7684\u5730\u4e0b\u57ce\u90fd\u5728\u4f7f\u7528\u4e2d\uff0c\u8bf7\u7a0d\u540e\u518d\u5c1d\u8bd5\u3002")}else a.warp(baseid,"MD00");return!0};