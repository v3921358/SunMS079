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
            txt = "请根据任务要求完成,提交前再次检查背包是否留有位置.#k\r\n#e任务奖励： #v4032393#*1  #v4001136#*1 #l\r\n";

            if (cm.getPlayer().getBossLog("meitianrenwu4") == 1){
                txt += "#r你今天已经完成过了,请明天在来吧!";
                cm.sendOk(txt);
                cm.dispose();
            }else{
				txt += "#L1##b收集100个#v4031400#";
                cm.sendSimple(txt);
            }

        } else if (selection == 1) {
            if (cm.haveItem(4031400, 100) ){
				cm.gainItem(4031400, -100);
//				cm.gainItem(4000176, -2);
                cm.sendOk("恭喜您获得了奖励!");
				cm.gainItem(4032393, 1);//给这个物品1个的意思
				cm.gainItem(4001136, 1);//给这个物品1个的意思
//				cm.gainMeso(25000);
				cm.gainExp(30000);
				cm.getPlayer().setBossLog('meitianrenwu4');
                cm.dispose();
            }else{
                cm.sendOk("物品不足!请收集100个#v4031400#!");
                cm.dispose();
            }
        }
    }
}
