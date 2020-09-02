/*
	NPC Name: 		Vikin
	Map(s): 		Victoria Road: Lith Harbor (104000000)
*/
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendOk("嘿，嘿！！！寻找宝藏卷轴！我迷失了方向。在某个地方，我不能离开它.");
	cm.dispose();
    }
}