/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){try{var b=a.getEventManager("OrbisPQ");null!=b&&b.getProperty("stage6_"+a.getPortal().getName().substring(2,5)+"").equals("1")?(a.warpS(a.getMapId(),a.getPortal().getName().startsWith("rp16")?"pt03":a.getPortal().getId()+4),a.playerMessage(5,"\u7ec4\u5408\u6b63\u786e")):(a.warpS(a.getMapId(),a.getPortal().getName().startsWith("rp01")?5:a.getPortal().getName().startsWith("rp05")?1:a.getPortal().getId()-4),a.playerMessage(5,"\u7ec4\u5408\u4e0d\u6b63\u786e"))}catch(c){a.getPlayer().dropMessage(5,"\u9519\u8bef: "+c)}};