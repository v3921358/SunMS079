/* ==================
 脚本类型:  地图 
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */importPackage(Packages.scripting);importPackage(java.lang);function start(a){try{a.getDirectionStatus(!0),a.EnableUI(1),a.getMap().resetFully(),a.spawnReactorOnGroundBelow(1008010,365,216),a.getDirectionInfo(1,3E3),Thread.sleep(3E3),a.getDirectionInfo("Effect/Direction3.img/effect/tuto/BalloonMsg0/3",2100,0,-120,0,0),a.getDirectionInfo(1,1800),Thread.sleep(1800),a.topMsg("\u6309Ctrl\u952e\u4f7f\u7528\u666e\u901a\u653b\u51fb."),a.EnableUI(0)}catch(b){null!=b.rhinoException?b.rhinoException.printStackTrace():null!=b.javaException&&b.javaException.printStackTrace()}};