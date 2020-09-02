/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){try{var b=a.getEventManager("Pirate");null!=b&&b.getProperty("stage2").equals("3")?a.warp(925100200,0):a.playerMessage(5,"\u8be5\u95e8\u5c1a\u672a\u6253\u5f00")}catch(c){a.playerMessage(5,"\u9519\u8bef: "+c)}};