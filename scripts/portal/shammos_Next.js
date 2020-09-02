/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){try{null!=a.getPlayer().getParty()&&null==a.getMap().getMonsterById(9300275)&&a.isLeader()?(a.warpParty(100*(a.getPlayer().getMapId()/100+1)-a.getPlayer().getMapId()%100),a.playPortalSE()):a.playerMessage(5,"\u8bf7\u8ba9\u961f\u957f\u8fdb\u5165\u8fd9\u4e2a\u95e8, \u5e76\u786e\u4fdd\u90aa\u6469\u65af\u5728\u8fd9\u91cc.")}catch(b){a.playerMessage(5,"\u9519\u8bef: "+b)}};