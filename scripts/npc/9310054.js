/*
	脚本类型: 		NPC
	所在地图:		闹鬼宅邸
	脚本名字:		闹鬼宅邸进入npc
           制作：                     一纸离人醉丶
           技术指导：              芬芬时尚潮流
*/
function start() {
    cm.sendYesNo("你真的要进去嘛?");
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.warp(682000100);
    }
    cm.dispose();
}