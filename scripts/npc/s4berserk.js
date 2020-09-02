/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */
 function enter(pi) {
    if (pi.getQuestStatus(6153) == 1) {
	if (!pi.haveItem(4031471)) {
	    if (pi.haveItem(4031475)) {
		var em = pi.getEventManager("4jberserk");
		if (em == null) {
		    pi.playerMessage("你不被允许进入未知的原因。再试一次." );
		} else {
		    em.startInstance(pi.getPlayer());
			//pi.getMap(910500200).resetFully();//地图刷新
		    return true;
		}
	    } else {
		pi.playerMessage("要进入，你需要一个遗忘的神殿钥匙.");
	    }
	} else {
	    pi.playerMessage("赛里木已经屏蔽。");
	}
    } else {
	pi.playerMessage("你不能进入这个地方.");
    }
    return false;
}