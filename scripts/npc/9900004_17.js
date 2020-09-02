var status = 0;
importPackage(java.lang);
importPackage(Packages.tools);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
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
            for (i = 0; i < 10; i++) {
                text += "";
            }
            //text += " #ro#b○#do#g○#bo#d○#ro#g○#do#r○#bo#d○#go#r○#k<#b#e节日礼包#n#k>#r○#go#d○#go#r○#do#b○#go#r○#bo#d○#go#r○#b#go#k\r\n\r\n"
			text +=  "#b一线海们#k,每个周的周末都可以领取一次哟,单个角色不可重复领取,但可同个帐号多个角色领取,通过#r商城转移#k.你懂得!\r\n\r\n"
			//text +=  "  #r领取奖励#k :  #v5041000# x 1 .\r\n\r\n"
            //text += " 中国官方没有设立正式的#r父亲节#k但内地民众习惯上使用6月第三个星期日当做父亲节,台湾父亲节是8月8日.台湾的父亲节订于每年的八月八日，又称为“八八节”。这是因为“八八”和爸爸相近，而且“八八”两字连缀起来，又好象一个 “父”字，所以父亲节特别被定于八月八日.\r\n"			
            text += "#L1##r#v4031347#每周礼包#l"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if(cm.getPlayer().getOneTimeLog("meizhoulibao5") >= 1){
			cm.sendOk("#v4031347#每个礼拜的周日,领取一次.");
            cm.dispose();
			}else{
			if (cm.getInventory(5).isFull(5)){
			cm.sendOk("#b请保证背包中有空位,否则无法领取.");
			cm.dispose();		
			} else {		
            cm.gainItem(5041000,1,7);
            cm.gainMeso(30000);		
            cm.getPlayer().modifyCSPoints(2, +500, true);//给抵用卷			
            cm.sendOk("领取成功!");
			cm.getPlayer().setOneTimeLog("meizhoulibao5");
			cm.worldMessage(6,"玩家：["+cm.getName()+"] 领取 每周礼包,大家周末愉快!");
            cm.dispose();				
			}
		}
    }
}
}	

