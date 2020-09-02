/* 
 * Spiegelmann - Monster Carnival
 */

var status = -1;
var rank = "D";
var exp = 0;

function start() {
    if (cm.getCarnivalParty() != null) {
        status = 99;
    }
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (mode == -1) {
	cm.dispose();
	return;
    }
    if (status == 0) {
        cm.sendSimple("你想要做什么？如果你从来没有参加过怪物嘉年华,你可以试着尝试新的玩法！\r\n#b#L0# 移动到休彼得曼办公室.#l");
    } else if (status == 1) {
        switch (selection) {
            case 0: {
                var level = cm.getPlayerStat("LVL");
                if ( level < 30) {
                    cm.sendOk("对不起，需要等级30级以上才可以参加怪物嘉年华.");
                } else {
					cm.saveLocation("MULUNG_TC");
                    cm.warp( 980000000, "st00" );
                }
                cm.dispose();
            }
            default: {
                cm.dispose();
                break;
            }
            break;
        }
    } else if (status == 100) {
        var carnivalparty = cm.getCarnivalParty();
        if (carnivalparty.getTotalCP() >= 501) {
            rank = "A";
            exp = 30000;
        } else if (carnivalparty.getTotalCP() >= 251) {
            rank = "B";
            exp  = 22500;
        } else if (carnivalparty.getTotalCP() >= 101) {
            rank = "C";
            exp = 16500;
        } else if (carnivalparty.getTotalCP() >= 0) {
            rank = "D";
            exp = 10000;
        }
	cm.getPlayer().endPartyQuest(1301);
        if (carnivalparty.isWinner()) {
            cm.sendOk("你赢得了战斗，尽管你惊人的表现。胜利是属于你的. \r\n#b怪物嘉年华 Rank : " + rank);
        } else {
            cm.sendOk("不幸的是，你要么捆绑或失去了战斗，尽管你惊人的表现。胜利应该属于你的下一次. \r\n#b怪物嘉年华 Rank : " + rank);
        }
    } else if (status == 101) {
        var carnivalparty = cm.getCarnivalParty();
	var los = parseInt(cm.getPlayer().getOneInfo(1301, "lose"));
	var vic = parseInt(cm.getPlayer().getOneInfo(1301, "vic"));
        if (carnivalparty.isWinner()) {
	    vic++;
	    cm.getPlayer().updateOneInfo(1301, "vic", "" + vic);
            carnivalparty.removeMember(cm.getChar());
            cm.gainExpR(exp);
        } else {
	    los++;
	    cm.getPlayer().updateOneInfo(1301, "lose", "" + los);
            carnivalparty.removeMember(cm.getChar());
            cm.gainExpR(exp / 2);

        }
	cm.getPlayer().updateOneInfo(1301, "VR", "" + (java.lang.Math.ceil((vic * 100) / los)));
            cm.warp(980000000);
            cm.dispose();
    }

}