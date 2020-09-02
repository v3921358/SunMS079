/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){try{var b=a.getEventManager("LudiPQ");null!=b&&b.getProperty("stage6_"+a.getPortal().getName().substring(2,4)+"").equals("1")?(a.warpS(a.getMapId(),a.getPortal().getName().startsWith("pt9")?14:a.getPortal().getId()+3),a.playerMessage(-1,"\u6b63\u786e\u7ec4\u5408!"),a.getMap().changeEnvironment("an"+a.getPortal().getName().substring(2,4),2)):(a.warpS(a.getMapId(),a.getPortal().getName().startsWith("pt0")?13:a.getPortal().getId()-3),a.playerMessage(-1,"\u7ec4\u5408\u9519\u8bef."))}catch(c){a.getPlayer().dropMessage(5,"\u9519\u8bef: "+c)}};