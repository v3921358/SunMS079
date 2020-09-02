/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){if(7<=rm.getReactor().getState()){rm.mapMessage(6,"\u4e00\u4e2a\u70e7\u676f\u5df2\u7ecf\u5b8c\u6210.");var a=rm.getEventManager(926100100==rm.getMapId()?"Romeo":"Juliet");if(null!=a&&7<=rm.getReactor().getState()){var b=rm.getMap().getReactorByName(926100100==rm.getMapId()?"rnj2_door":"jnr2_door");a.setProperty("stage3",parseInt(a.getProperty("stage3"))+1);b.forceHitReactor(b.getState()+1)}}};