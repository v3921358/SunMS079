/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getEventManager("HorntailBattle");if(null!=a){var c=b.getMapId();24006E4==c?a.getProperty("state").equals("2")?a.warpAllPlayer(24006E4,240060100):b.playerMessage("\u95e8\u88ab\u963b\u6b62."):240060100==c&&(a.getProperty("state").equals("3")?a.warpAllPlayer(240060100,240060200):b.playerMessage("\u95e8\u88ab\u963b\u6b62."))}};