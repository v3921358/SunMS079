/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){null!=a.getPlayer().getParty()&&0==a.getPlayer().getMap().getAllMonstersThreadsafe().size()?(.9<java.lang.Math.random()?a.warpParty(921160400):a.warp(921160300+10*(Math.floor(6*java.lang.Math.random())|0),0),a.playPortalSE()):a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528. \u6740\u6b7b\u6240\u6709\u7684\u602a\u7269.")};