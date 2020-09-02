/**
 * @author: OdinMS
 * @editor: Eric
 * @npc: Cesar
 * @func: Ariant PQ (Outdated GMS-like text, AriantPQ is closed off and I am unable to get this.)
*/

importPackage(Packages.tools);
importPackage(Packages.client);

var status = -1;
var sel;

function start() {
    if ((cm.getPlayer().getLevel() < 20) && !cm.getPlayer().isGM()) {
		cm.sendNext("你等级不在20~30级,对不起，您可能不参加.");
        cm.dispose();
        return;
    }
    if(cm.getPlayer().getMapId() % 10 == 1)
        cm.sendSimple("你有什么需要帮忙的吗?\r\n#b#L0#给我#t2270002#和#t2100067##l\r\n#L1#我该做什么?#l\r\n#L2#让我离开这里.#l");
    else
        cm.sendSimple(cm.getPlayer().getAriantRoomLeaderName(((cm.getPlayer().getMapId() / 100) % 10) - 1) == cm.getPlayer().getName() ? "现在开始比赛!#b\r\n#b#L3#准备进入战场!#l\r\n#L1#我想踢另一个角色.#l\r\n#L2#让我离开这里.#l" : "你想做什么?#b\r\n#L2# 让我离开这里.#l");
}

function action(mode, type, selection){
    status++;
    if (mode != 1) {
        if (mode == 0 && type == 0)
            status -= 2;
        else {
            cm.dispose();
            return;
        }
    }
    if (cm.getPlayer().getMapId() % 10 == 1) {
        if (status == 0) {
            if (sel == undefined)
                sel = selection;
            if (sel == 0) {
                if (cm.haveItem(2270002))
                    cm.sendNext("你已经拥有#b#t2270002##k.");
                else if (cm.canHold(2270002) && cm.canHold(2100067)) {
                    if (cm.haveItem(2100067))
                        cm.removeAll(2100067);
                    cm.gainItem(2270002, 32);
                    cm.gainItem(2100067, 5);
                    cm.sendNext("现在降低怪物的HP并使用#b#t2270002##k吸收他们的灵魂!");
                } else
                    cm.sendNext("请检查背包空间");
                cm.dispose();
            } else if(sel == 1) {
				status = 1;
                cm.sendNext("你需要做什么？你一定是新来的。请允许我详细解释.");
            } else
                cm.sendYesNo("你确定你要离开吗?"); //No GMS like.
        } else if (status == 1) {
            if (mode == 1) {
                cm.warp(980010020);
                cm.dispose();
                return;
            }
		} else if (status == 2) {
            cm.sendNextPrev("这真的很简单你会得到#b#t2270002##k 从我身上,你的任务就是要把怪物的HP降到比较低的数值,然后使用#b#t2270002##k吸取其灵魂宝石.");
        } else if (status == 3)
            cm.sendNextPrev("很简单。如果你吸收了怪物灵魂的#b#t2270002##k,然后你会做#b#t4031868##k, 这是女王阿列达爱。与大多数竞技场玩家战斗获胜。为了赢得比赛，为了防止别人的吸收，自己必须学聪明点！！.");
        else if (status == 4)
            cm.sendNextPrev("还有一件事. #r战斗开始不允许使用宠物#k你明白了吗?");
        else if (status == 5)
            cm.dispose();
    } else {
        var nextchar = cm.getMap(cm.getPlayer().getMapId()).getCharacters().iterator();
        if (status == 0) {
            if (sel == undefined)
                sel = selection;
            if (sel == 1)
                if (cm.getPlayerCount(cm.getPlayer().getMapId()) > 1) {
                    var text = "你想把谁踢出房间?"; //Not GMS like text
                    var name;
                    for (var i = 0; nextchar.hasNext(); i++) {
                        name = nextchar.next().getName();
                        if (!cm.getPlayer().getAriantRoomLeaderName(((cm.getPlayer().getMapId() / 100) % 10) - 1).equals(name))
                            text += "\r\n#b#L" + i + "#" + name + "#l";
                    }
                    cm.sendSimple(text);
                } else {
                    cm.sendNext("现在没有什么可以被踢的角色。");
                    cm.dispose();
                }
            else if (sel == 2) {
                if (cm.getPlayer().getAriantRoomLeaderName(((cm.getPlayer().getMapId() / 100) % 10) - 1) == cm.getPlayer().getName())
                    cm.sendYesNo("你确定你要离开吗？你是竞技场的队长，所以如果你离开，竞技场房间将关闭.");
                else
                    cm.sendYesNo("你确定你要离开吗?"); //No GMS like.
            } else if (sel == 3)
                if (cm.getPlayerCount(cm.getPlayer().getMapId()) > 0 )
                    cm.sendYesNo("房间都是一套,没有其他的角色可以加入这场战斗的竞技场。你想现在开始游戏吗?");
                else {
                    cm.sendNext("你至少需要2名参与者来开始比赛.");
                    cm.dispose();
                }
        } else if (status == 1) {
            if (sel == 1) {
                for (var i = 0; nextchar.hasNext(); i++)
                    if (i == selection) {
                        nextchar.next().changeMap(cm.getMap(980010000));
                        break;
                    } else
                        nextchar.next();
                cm.sendNext("玩家被踢出了房间."); //Not GMS like
            } else if(sel == 2) {
                if (cm.getPlayer().getAriantRoomLeaderName(((cm.getPlayer().getMapId() / 100) % 10) - 1) != cm.getPlayer().getName())
                    cm.warp(980010000);
                else {
                    cm.getPlayer().removeAriantRoom((cm.getPlayer().getMapId() / 100) % 10);
                    cm.mapMessage(6, cm.getPlayer().getName() + " 已经离开了房间，所以房间现在将关闭.");
                    cm.warpMap(980010000, 0);
                }
            } else {
				cm.startAriantPQ(cm.getPlayer().getMapId() + 1);
            }
            cm.dispose();
        }
    }
}
