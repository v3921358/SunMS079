/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var dungeonid=106021600,dungeons=10;function enter(b){for(var a=0;a<dungeons;a++)if(0==b.getMap(dungeonid+a).getCharactersSize())return b.warp(dungeonid+a,0),!0;b.playerMessage(5,"\u73b0\u5728\u6b63\u5728\u4f7f\u7528,\u8bf7\u7a0d\u540e\u518d\u8bd5.");return!1};