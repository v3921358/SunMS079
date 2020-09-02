/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var em;function enter(a){222020100==a.getMapId()?a.getClient().getChannelServer().getEventSM().getEventManager("elevator").getProperty("isDown").equals("true")?(a.playPortalSE(),a.warp(222020110,"sp")):a.playerMessage("\u7535\u68af\u5c1a\u672a\u5230\u8fbe"):a.getClient().getChannelServer().getEventSM().getEventManager("elevator").getProperty("isUp").equals("true")?(a.playPortalSE(),a.warp(222020210,"sp")):a.playerMessage("\u7535\u68af\u5c1a\u672a\u5230\u8fbe")};