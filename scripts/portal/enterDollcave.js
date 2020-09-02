/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(21728))return a.warp(910510001,0),!0;if(0>=a.getPlayerCount(910510201)&&1==a.isQuestActive(21731))return a.getMap(910510201).resetFully(),a.warp(910510201),!0;a.playerMessage(5,"\u91cc\u9762\u6709\u4eba\u6216\u8005\u6ca1\u6709\u63a5\u53d7\u76f8\u5173\u4efb\u52a1\u800c\u65e0\u6cd5\u8fdb\u5165\u6b64\u6d1e\u7a74\u3002");return!1};