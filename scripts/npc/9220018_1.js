/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
function action(mode, type, selection) {
	    if (cm.isQuestActive(5067)==0) {//判断任务
		cm.sendOk("你还没有接受相关任务,请找旁边的拉拉接受任务.");
		cm.dispose();
	    } else {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("队伍队长必须在这里.");
		cm.dispose();
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < 50) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 2 : 1);
		}	
		if (next && size >= 3) {
			var em = cm.getEventManager("MV");
			if (em == null) {
				cm.sendOk("请稍后再试。.");
			} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
		    } else {
			cm.sendOk("另一个组队已经进入.");
		    }
			}
		} else {
			cm.sendOk("你的队伍必须大于3人,并且等级50级以上");
		}
	    }
	cm.dispose();
}}