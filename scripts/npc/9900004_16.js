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
            text += " #ro#b○#do#g○#bo#d○#ro#g○#do#r○#bo#d○#go#r○#k<#b#e节日礼包#n#k>#r○#go#d○#go#r○#do#b○#go#r○#bo#d○#go#r○#b#go#k\r\n\r\n"
			text += " #b中国共产党#k于1921年7月23日成立后，在反动军阀政府的残暴统治之下，只能处于秘密状态，没有公开进行活动的环境。在大革命时期，党忙于国共合作、开展工农运动和支援北伐战争，没有条件对党的诞生进行纪念。\r\n\r\n"
            text += " 把#b7月1日#k作为中国共产党的诞辰纪念日，是毛泽东同志于1938年5月提出来的。当时，毛泽东在《论持久战》一文中提出：“今年七月一日，是中国共产党建立的十七周年纪念日”。这是中央领导同志第一次明确提出“七一”是党的诞生纪念日.\r\n"			
            text += "#L1##r#v4031347#领取建党节日礼包#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if(cm.getPlayer().getOneTimeLog("lixinlibao13") >= 0){
			cm.sendOk("#v4031347#7月1日是#r建党节#k\r\n节日礼包只能在节日当天领取,或者你已经领取过了");
            cm.dispose();
			}else{
			if (cm.getInventory(5).isFull(5)){
			cm.sendOk("#b请保证背包中有空位,否则无法领取.");
			cm.dispose();		
			} else {		
	        cm.getPlayer().modifyCSPoints(2, +500, true);//给抵用卷			
			cm.gainMeso(100000);			
            cm.sendOk("领取成功!建党节快乐");
			cm.getPlayer().setOneTimeLog("lixinlibao12");
			cm.worldMessage(6,"玩家：["+cm.getName()+"] 领取 建党节日礼包 祝大家节日快乐!");
            cm.dispose();				
			}
		}
    }
}
}	

