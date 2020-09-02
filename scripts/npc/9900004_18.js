importPackage(Packages.client);
var status = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        if (status == 0) {
            var txt = "";
            txt = " 每日收集需要材料:#k    #v4002000#3张,#v4002001#2张,#v4002002#1张\r\n\r\n #b请根据任务要求完成,提交前再次检查背包是否留有位置#k\r\n\r\n #r任务奖励:\r\n\r\n    #v5200000#*1  #v4000313#*5  #v4110010#*1  #v2370004#*1  #v4001266#*1 #k#l \r\n";

            if (cm.getPlayer().getBossLog("meitianrenwu5") == 1){
				txt += " #r#e            你今天已经完成过任务#k";
                cm.sendOk(txt);
                cm.dispose();

            }else{
                txt += "#L1##v4002000##b兑换奖励";
                cm.sendSimple(txt);
            }

        } else if (selection == 1) {
           	   if (cm.haveItem(4002000, 3)&&cm.haveItem(4002001, 2)&&cm.haveItem(4002002, 1)){
		        cm.gainItem(4002000, -3);
				cm.gainItem(4002001, -2);
				cm.gainItem(4002002, -1);
                cm.sendOk("恭喜您获得了奖励!");
		cm.worldMessage(6,"恭喜玩家：["+cm.getName()+"] 完成每日任务获得了奖励! ");//公告				
				cm.gainItem(5200000, 1);//给这个物品1个的意思
				cm.gainItem(4000313, 5);//给这个物品1个的意思
				cm.gainItem(4110010, 1);//给这个物品1个的意思
				cm.gainItem(2370004, 1);//给这个物品1个的意思
				cm.gainItem(4001266, 1);//给这个物品1个的意思
				cm.getPlayer().setBossLog('meitianrenwu5');
                cm.dispose();
            }else{
                cm.sendOk("你的邮票不足我需要的是\r\n\r\n #v4002000#3张,#v4002001#2张,#v4002002#1张 !");
                cm.dispose();
            }
        }
    }
}
