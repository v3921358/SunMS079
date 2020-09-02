/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){1<=a.getMap().getReactorByName("sMob1").getState()&&1<=a.getMap().getReactorByName("sMob2").getState()&&1<=a.getMap().getReactorByName("sMob3").getState()&&1<=a.getMap().getReactorByName("sMob4").getState()?a.isLeader()?a.warpParty(925100500):a.playerMessage(5,"\u961f\u957f\u5fc5\u987b\u5728\u8fd9\u91cc"):a.playerMessage(5,"\u8be5\u95e8\u5c1a\u672a\u6253\u5f00.")};