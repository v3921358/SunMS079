/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;
var random = java.lang.Math.floor(Math.random() * 9 + 1);
var random1 = java.lang.Math.floor(Math.random() * 15000 + 1);
var random2 = java.lang.Math.floor(Math.random() * 10 + 1);

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendSimple("你好，我是小老虎  #b兴儿#k.#b\r\n#L1#查看说明#l\r\n#L2#离开地图#l\r\n#L0#我给你带来了年糕!#l");
    } else if (status == 1) {
        if (selection == 0) {
            if (!cm.isLeader()) {
                cm.sendOk("请队长与我谈话.");
            } else {
                if (cm.haveItem(4001101,15)) {
                    cm.removeAll(4001101);
                    cm.givePartyExp(1800);
                    cm.endPartyQuest(1200);
					cm.getPlayer().setOneTimeLog("yuemiao");
                    cm.warpParty(910010100);
                } else {
                    cm.sendNext("你没有带来 #r15#k 块月妙的年糕... ");
                }
            }
        } else if (selection == 1) {
            cm.sendNext("收集花朵掉落的种子将其有顺序的摆放到六块土地上,全部开花时满月出现。满月下会召唤月妙兔子，每隔一段时间月妙小仙子会捣出年糕，收集#r15#k块年糕后交给队长然后交给NPC，即可通关。\r\n#r注：在月妙仙子捣年糕的时候保护它，月妙仙子被怪物攻击后死亡则任务失败.");
        } else if (selection == 2) {
        cm.removeAll(4001095);
		cm.removeAll(4001101);
        cm.removeAll(4001096);
        cm.removeAll(4001097);
        cm.removeAll(4001098);
        cm.removeAll(4001099);
        cm.removeAll(4001100);
        cm.warp(910010100);
        }
        cm.dispose();
    }
}