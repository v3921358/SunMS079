/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){switch(a.getMapId()){case 93E7:a.warp(930000100,0);break;case 930000100:0==a.getMap().getAllMonstersThreadsafe().size()?a.warp(930000200,0):a.playerMessage(5,"\u6d88\u706d\u6240\u6709\u7684\u602a\u7269.");break;case 930000200:null!=a.getMap().getReactorByName("spine")&&4>a.getMap().getReactorByName("spine").getState()?a.playerMessage(5,"\u8bf7\u628a4\u4e2a\u7a00\u91ca\u7684\u6bd2\u7d20\u4e22\u5728\u6b63\u786e\u7684\u4f4d\u7f6e,\u65b9\u53ef\u8fc7\u5173"):a.warp(930000300,0)}};