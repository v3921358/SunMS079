/* ==================
 脚本类型:  NPC    
 脚本版权：一线海冒险岛团队
 联系扣扣：297870163    609654666
 =====================
 */
importPackage(net.sf.cherry.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		if(cm.getLevel() < 30 || cm.getPlayer().getBossLog("wugong") == 5){
        cm.sendOk("挑战蜈蚣大王需要条件：\r\n最低等级 ：30级\t\t你的等级：#r"+cm.getLevel()+"级#k\r\n每天进入次数：5次\t你已经进入：#r"+cm.getPlayer().getBossLog("wugong")+"#k次 !");
		cm.dispose();
		return;
		}else{
		cm.warp(701010321, 0);
		cm.getPlayer().setBossLog('wugong');
		//cm.sendNext("每天进入次数：10次！\t你已经进入：#r"+cm.getBossLog("wugong")+"次#k,看来你在执行秘密任务，祝你好运气。。。");
		cm.dispose();
			}
	}
}}	
