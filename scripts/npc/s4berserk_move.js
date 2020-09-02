/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */
 function enter(pi) {//cm.spawnMonster(230000003,9001009,1); // Transforming
    var num = pi.getMap(910500200).getSpawnedMonstersOnMap();

    if (num <= 0) {
	//pi.playPortalSE();
	pi.warp(910500200, "pt00");
	//pi.spawnMonster(8130100,1,88,-1130);
	//pi.getMap(910500200).resetFully();//地图刷新
	//pi.spawnMonster(910500200,9001009,1);
	return true;
    }
    pi.playerMessage("门已被封锁,请消灭地图里的怪物");
    return true;
}