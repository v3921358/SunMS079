/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){null!=a.getPlayer().getParty()&&0==a.getMap().getAllMonstersThreadsafe().size()&&a.isLeader()?(a.warpParty(921120100),a.playPortalSE()):a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528. \u9996\u5148\u6bc1\u706d\u6240\u6709\u602a\u7269.")};