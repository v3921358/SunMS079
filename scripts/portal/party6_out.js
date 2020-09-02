/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */
 function enter(pi) {
	if (pi.canHold(4001198,1) && (pi.getMap().getAllMonstersThreadsafe().size() == 0 || pi.getMap().getMonsterById(9300183) != null) && (pi.getMap().getReactorByName("") == null || pi.getMap().getReactorByName("").getState() == 1)) {
		pi.warp(930000800,0);
        pi.gainExp(100000);//给个人经验
		pi.getPlayer().endPartyQuest(1206);
		pi.addTrait("will", 30);
		pi.gainItem(4001198,1);
	} else {
		pi.playerMessage(5, "请消灭怪物,拯救毒雾森林的任务交给你了!");
	}
}