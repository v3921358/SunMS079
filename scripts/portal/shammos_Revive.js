/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){null!=a.getPlayer().getEventInstance()&&(a.gainExp_PQ(200,1.5),a.gainNX(2500),a.addTrait("will",15),a.addTrait("insight",3),a.canHold(4001530,1)&&a.isGMS()&&a.gainItem(4001530,1));a.warp(211000002,0)};