importPackage(java.lang);
importPackage(java.util);
importPackage(Packages.tools);
importPackage(Packages.server.quest);
importPackage(Packages.client);
importPackage(Packages.scripting);
importPackage(Packages.handling.channel);
importPackage(Packages.handling);
importPackage(Packages.handling.word);
function start() {
    status = -1;

    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.getPlayer().getMapId() == 108010101 || cm.getPlayer().getMapId() == 108010201 || cm.getPlayer().getMapId() == 108010301 || cm.getPlayer().getMapId() == 108010401 || cm.getPlayer().getMapId() == 108010501) {
                cm.sendOk("本地图暂时无法使用使用拍卖功能");
                cm.dispose();
                return;
            }
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "              #v3994084##v3994083##v3994071##v3994082##v3994062#\r\n" //showghrs
            text += "               #e#b当天在线时长：#r" + cm.getPlayer().getzxsj() + "#b 分钟\r\n"//2
            text += "#k当前点卷：#r" +cm.getPlayer().getCSPoints(1) +  "#k点 当前积分为:#r"+ cm.getcz() +"#k点 豆豆：#r" + cm.getPlayer().getBeans() + "#k点\r\n#k"
            text += "#L1##e#r#v3992025#在线奖励#l #L2##e#r#v3992025#游戏赞助#l #L3##e#r#v3992025#排 行 榜#l\r\n"
            text += "#L7##e#r#v3992025#戒指合成#l #L5##e#r#v3992025#勋章系统#l #L4##e#r#v3992025#删除道具#l\r\n"
			text += "#L17##e#r#v3992025#每周礼包#l #L6##e#r#v3992025#每日任务#l #L18##e#r#v3992025#学习技能#l #l\r\n\r\n"
            text += "#L1000##v3992025##b快捷传送#l#L1001##v3992025#快速转职#l#L1002##v3992025#加满技能#l\r\n#L1003##v3992025#刷物品#l#L1004##v3992025#刷点券金币经验#l"
			
            cm.sendOk(text); 
        } else if (selection == 1) {
            cm.openNpc(9900004, 1); //在线奖励		
        } else if (selection == 2) {
            cm.openNpc(9900004, 2); //赞助
        } else if (selection == 3) {
            cm.openNpc(9900004, 3); //排行榜
        } else if (selection == 4) {
            cm.openNpc(9900004, 4); //删除道具
	    } else if (selection == 5) {
            cm.openNpc(9900004, 5); //副本勋章
	    } else if (selection == 6) {
			if(cm.getLevel() >= 51){
			cm.openNpc(9900004, 18);//活动奖励
		}else{
				cm.sendOk("你等级未达到51级,不能参加每日任务!");
				cm.dispose();
}				
		} else if (selection == 7) {
            cm.openNpc(9900004, 7); //戒指合成
		} else if (selection == 8) {
		    cm.openNpc(9900004, 30); //积分兑换
		} else if (selection == 16) {
            cm.openNpc(9900004, 16); //节日礼包			
		} else if (selection == 17) {
            cm.openNpc(9900004, 17); //没有	
		} else if (selection == 18) {
            cm.openNpc(9310073, 51); //没有	
 } else if (selection == 1000) {//
            cm.openNpc(9900004, 1000);
        } else if (selection == 1001) {//
            cm.openNpc(9900004, 1001);		
        } else if (selection == 1002) {//
            cm.openNpc(9900004, 1002);	
        } else if (selection == 1003) {//
            cm.openNpc(9900004, 1003);	
        } else if (selection == 1004) {//
            cm.openNpc(9900004, 1004);	
        }
    }
}
