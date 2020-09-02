/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(a.isLeader())if(0!=a.getMap().getAllMonstersThreadsafe().size())a.playerMessage(5,"\u8bf7\u6e05\u9664\u6240\u6709\u7684\u602a\u7269!");else if(4==(a.getMapId()%10|0))if(1>a.getMap().getReactorByName("switch0").getState()||1>a.getMap().getReactorByName("switch1").getState())a.playerMessage(5,"\u8fd9\u4e24\u6b3e\u4ea4\u6362\u673a\u5fc5\u987b\u6253\u5f00.");else{var b=a.getMapId()+66;90!=(b%100|0)&&(b+=10);a.warpParty(b,0)}else a.warpParty(a.getMapId()+1,3==(a.getMapId()%10|0)?1:2);else a.playerMessage(5,"\u961f\u957f\u5fc5\u987b\u5728\u8fd9\u91cc")};