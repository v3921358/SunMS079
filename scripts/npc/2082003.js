/*
	NPC Name: 		Cobra - Retired dragon trainer
	Map(s): 		Leafre : Cabin
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
	cm.sendSimple("你是否愿意前往时间神殿,在这之前你能变成一只可爱的龙！\r\n #L0##b出发时间神殿#k#l");
    } else if (status == 1) {
	cm.useItem(2210016);
	cm.warp(200090500, 0);
	cm.dispose();
    }
}