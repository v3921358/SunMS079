/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getEventManager("Romeo");null!=b&&b.getProperty("stage6_"+((a.getMapId()%10|0)-1)+"_"+a.getPortal().getName().substring(2,3)+"_"+a.getPortal().getName().substring(3,4)+"").equals("1")?(a.warpS(a.getMapId(),31<=a.getPortal().getId()?a.isGMS()?35:13:a.getPortal().getId()+4),a.playerMessage(-1,"\u6b63\u786e\u7684\u9053\u8def.\u7ee7\u7eed!"),a.isGMS()&&a.getMap().changeEnvironment("an"+a.getPortal().getName().substring(2,4),2)):(a.warpS(a.getMapId(),4>=a.getPortal().getId()?a.isGMS()?13:0:a.getPortal().getId()-4),a.playerMessage(-1,"\u6b63\u786e\u7684\u65b9\u5411.\u7ee7\u7eed!"))};