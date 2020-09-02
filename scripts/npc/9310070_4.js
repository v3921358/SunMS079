/*合成NPC 作者:bay 廖*/
function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 60; i++) {
                text += "";
            }
            text +="是否要将#v1122143#升级为#v1122122#吗? \r\n费用是#r1200万金币#l#k和#r十枚蛋蛋币#v4310029##k\r\n";
            text += "#L1#我准备好材料了 #l\r\n\r\n";
			text +="记得把#v1122143#卸下来放在背包里. \r\n";
			text +="合成材料:  #v4250802#*1个 #v4031560#*50张 #v4031559#*80张 #v4031561#*25张 \r\n";
			text +="#v4000313#*600个  #v4001126#*3000个  #v4003000#*500个  #v4021007#*30个\r\n";
			text +="#v4021000#*30个 #v4021004#*30个 #v4011002#*30个  #v4021008#*30个 #v4005004#*30个\r\n";
			text +="#v4000040#*15个 #v4000176#*15个 \r\n";
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.haveItem(1122143, 1) && cm.haveItem(4250802, 1) && cm.haveItem(4031560, 50) && cm.haveItem(4031559, 80) && cm.haveItem(4031561, 25) && cm.haveItem(4000313, 600) && cm.haveItem(4001126, 3000) && cm.haveItem(4003000, 500) && cm.haveItem(4021007, 30) && cm.haveItem(4021000, 30) && cm.haveItem(4021004, 30) && cm.haveItem(4011002, 30) && cm.haveItem(4021008, 30) && cm.haveItem(4005004, 30) && cm.haveItem(4310029, 10) && cm.haveItem(4000040, 15) && cm.haveItem(4000176, 15) && cm.getMeso() >=12000000) {
                cm.gainItem(1122143,-1);
                cm.gainItem(4250802,-1);
                cm.gainItem(4031560,-50);
                cm.gainItem(4031559,-80);
                cm.gainItem(4031561,-25);
                cm.gainItem(4000313,-600);
                cm.gainItem(4001126,-3000);
                cm.gainItem(4003000,-500);
                cm.gainItem(4021007,-30);
                cm.gainItem(4021000,-30);
                cm.gainItem(4021004,-30);
                cm.gainItem(4011002,-30);
                cm.gainItem(4021008,-30);
                cm.gainItem(4005004,-30);
                cm.gainItem(4310029,-10);
                cm.gainItem(4000040,-15);
                cm.gainItem(4000176,-15);
                //cm.gainItem(4005001,-1);
                //cm.gainItem(4005002,-1);
                //cm.gainItem(4005003,-1);
                //cm.gainItem(4005004,-1);
                //cm.gainItem(4001126,-300);
                //cm.gainItem(4000313,-100);
                //cm.gainItem(4031560,-10);
                cm.gainMeso(-12000000);
                cm.gainItem(1122122,16,4,0,0,200,0,50,0,20,20,20,20,0,0);
                cm.sendOk("合成#v1122122#成功！");
                cm.dispsoe();
            } else {
                cm.sendOk("准备的东西还不够,继续收集吧");
                cm.dispsoe();
            } 
			}
		}
    }


