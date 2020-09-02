/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var zones = 0;
var names = Array("沉睡森林1", "沉睡森林2", "沉睡森林3");
var maps = Array(105040310, 105040312, 105040314);
var selectedMap = -1;

function start() {
    cm.sendNext("你觉得周围的这尊雕像的神秘力量。");
    if (cm.getQuestStatus(2054))
        zones = 3;
    else if (cm.getQuestStatus(2053))
        zones = 2;
    else if (cm.getQuestStatus(2052))
        zones = 1;
    else
        zones = 0;
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (status >= 2 && mode == 0) {
            cm.sendOk("好吧，下次再见。");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 1) {
            if (zones == 0)
                cm.dispose();
            else {
                var selStr = "它的动力让你自己将就深林里.#b";
                for (var i = 0; i < zones; i++)
                    selStr += "\r\n#L" + i + "#" + names[i] + "#l";
                cm.sendSimple(selStr);
            }
        } else if (status == 2) {
            cm.warp(maps[selection],0);
            cm.dispose();
        }
    }
}	