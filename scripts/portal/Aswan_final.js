/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.client);function enter(a){switch(a.getMapId()){case 955000300:0==a.getMap().getAllMonstersThreadsafe().size()?(a.getPlayer().gainExp(3E4,!0,!0,!0),a.getPlayer().addHonourExp(100*a.getPlayer().getHonourLevel(),!0),a.warp(26201E4,0),a.worldMessage(6,"[Azwan] "+a.getPlayer().getName()+" \u5b8c\u6210\u5e0c\u62c9\u7684\u521a\u5728\u901a\u9053\u7684Azwan\u89e3\u653e "+a.getClient().getChannel()+".")):a.playerMessage(5,"\u4f60\u5fc5\u987b\u6d88\u706d\u6240\u6709\u7684\u602a\u7269\u624d\u80fd\u7ee7\u7eed.")}};