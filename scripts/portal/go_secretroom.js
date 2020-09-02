/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var baseid=106021E3,dungeonid=106021001,dungeons=10;function enter(a){if(!a.haveItem(4032405))return a.playerMessage(5,"\u9700\u8981\u5bc6\u5ba4\u94a5\u5319\u624d\u80fd\u8fdb\u5165"),!1;if(a.getPlayer().getMapId()==baseid){for(var b=0;b<dungeons;b++)if(0==a.getMap(dungeonid+b).getCharactersSize())return a.warp(dungeonid+b,0),!0;a.playerMessage(5,"\u91cc\u9762\u5df2\u7ecf\u6709\u4eba\u6311\u6218\u8bf7\u7a0d\u540e\u5728\u8bd5\uff01\uff01");return!1}a.warp(baseid,"in00");return!0};