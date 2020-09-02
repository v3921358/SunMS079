/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var status = -1;


function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else 
        if (status == 0) {
		    cm.sendNext("你还有一些生意要打理，对吗？在走出去之前，放松这一地区并获得充分的力量是不错的主意，这是一个不错的主意.");
            cm.dispose();
        status--;
    }
    if (status == 0) {
		
	    cm.sendYesNo("你没有别的事要做，嗯？你想回去吗？如果是的话，我可以把你送回去。您是怎么想的？你想回去吗?");
    } else if (status == 1) {
		
		var returnMap = cm.getSavedLocation("MULUNG_TC");
			if (returnMap < 0) {
		returnMap = 101000000;
	}	
	cm.clearSavedLocation("MULUNG_TC");
	cm.warp(returnMap, 0); 
		cm.dispose();
    }
}

