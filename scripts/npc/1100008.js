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
		    cm.sendNext("没兴趣？ 好吧。..");
            cm.dispose();
        status--;
    }
    if (status == 0) {
	    cm.sendYesNo("这艘船会往前走 #b#m130000000##k, 一个岛屿，你会发现深红色的树叶浸泡在阳光下，轻柔的微风掠过溪流，和槭树的皇后，天鹅座。 如果你有兴趣加入天鹅骑士，那么你肯定应该拜访那里。 你有兴趣参观 #m130000000#?\r\n\r\n这次旅行会花费你 #b1000#k 金币.");
	} else if (status == 1) {	
        cm.warp(130000210,0);	
	    cm.dispose(); 
    }
}