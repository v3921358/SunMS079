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
            txt = "请根据任务要求完成,提交前再次检查背包是否留有位置.#k\r\n#e任务奖励： #v4032391#*1  #v5072000#*1 #l\r\n";

            if (cm.getPlayer().getBossLog("meitianrenwu") == 1){// cm.getPS()  的意思是 读取跑商值如果等于0 就得出他没有开始跑商 就运行他进行第一环跑商!
			    txt += "#r你今天已经完成过了,请明天在来吧!";
                cm.sendOk(txt);
                cm.dispose();
            }else{
                txt += "#L1##b收集100个#v4000019##l";
                cm.sendSimple(txt);
            }

        } else if (selection == 1) {
            if (cm.haveItem(1112804) >= 2){
				cm.gainItem(1112804, 1);
                cm.sendOk("恭喜您获得了奖励!");
				cm.gainItem(4032391, 1);
				cm.gainItem(5072000, 1);
				//cm.gainMeso(10000);//给金币1万的意思
				cm.gainExp(5000);//给2万经验的意思
				cm.getPlayer().setBossLog('meitianrenwu');
                cm.dispose();
            }else{
                cm.sendOk("物品不足!请收集100个#v4000019#交给我!");
                cm.dispose();
            }
        }
    }
}
