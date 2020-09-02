/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
function action(mode, type, selection) {
	if (cm.getPlayerCount(913030000) == 0) {
		cm.removeNpc(913030000, 1104002);
		var map = cm.getMap(913030000);
		map.killAllMonsters(false);
		map.spawnNpc(1104002, new java.awt.Point(-430, 88));
		cm.warp(913030000, 0);
	} else {
	    cm.playerMessage("黑女巫正在被别人打.");
	}
	cm.dispose();
}