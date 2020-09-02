/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
var pop = 2;

function enter(a) {
	if (1 != a.getPlayer().getClient().getChannel() && 2 != a.getPlayer().getClient().getChannel()) return a.playerMessage(5, "帕普拉图斯只能在頻道1和2能打。"), !1;
	if (a.haveItem(4031870) && 1 > !a.getMonsterCount(220080001)) return a.warp(922020300, 0), !0;
	if (200 <= a.getPlayer().getBossLog("Populatus00")) return a.playerMessage(5, "每天只能挑战200次！"), !1;
	if (0 >= a.getPlayerCount(220080001)) return a.getMap(220080001).resetFully(), a.getMap(22008E4).resetReactors(), a.warp(220080001, "st00"), a.getPlayer().setBossLog("Populatus00"), !0;
	if (0 == a.getMap(220080001).getSpeedRunStart() && (0 >= a.getMonsterCount(220080001) || a.getMap(220080001).isDisconnected(a.getPlayer().getId()))) return a.warp(220080001, "st00"), a.getPlayer().setBossLog("Populatus00"), !0;
	a.playerMessage(5, "里面的战斗已经开始，请稍后再尝试。");
	return !1
};