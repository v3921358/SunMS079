/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.client);function enter(a){if(0==a.getMap().getAllMonstersThreadsafe().size())a.warp(a.getPlayer().getMapId()+100,0);else return a.playerMessage(5,"\u8bf7\u6d88\u706d\u6240\u6709\u7684\u602a\u7269\uff0c\u5f97\u5230\u4f60\u7684\u5956\u52b1!"),!1;return!0};