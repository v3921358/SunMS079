/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var cost = 50000;

function start() {
    cm.sendYesNo("你好,我是码头售票员。你想离开金银岛到天空之城吗? 从这到#b天空之城#k的船需要花费#b"+cost+"金币#k 购买#b#t4031045##k才可以启航.");
}

function action(mode, type, selection) {
    if(mode == -1)
        cm.dispose();
    else {
        if(mode == 0) {
            cm.sendNext("你貌似有不足的金币？");
            cm.dispose();
            return;
        }
        status++;
        if(status == 1) {
            if (cm.getMeso() >= cost && cm.canHold(4031045)) {
                cm.gainItem(4031045,1);
                cm.gainMeso(-cost);
                cm.dispose();
            } else {
                cm.sendOk("请问你有 #b"+cost+"金币#k? 如果有的话,我劝您检查下身上其他栏位看是否有没有满了.");
                cm.dispose();
            }
        }
    }
}
