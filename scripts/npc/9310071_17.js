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
            text += "		#v4001221# #v4001222# #v4001223# #v4001224# #v4001225#\r\n "
			text += "  #r完成提交任务之前请检查背包空位!\r\n "
			text += "  #d任务条件不具备唯一性,所需材料不定期变动!\r\n "
//			text += "#L1##e#d第一项#r 奖励:1万金币 1万经验 2*#v4000487# 2*#v4001266# \r\n"//3
//			text += "#L2##e#d第二项#r 奖励:10万金币 5万经验 3*#v4000487# 3*#v4001266# \r\n"//3
//			text += "#L3##e#d第三项#r 奖励:10万金币 5万经验 3*#v4000487# 3*#v4001266# \r\n"//3
//			text += "#L4##e#d第四项#r 奖励:20万金币 20万经验 5*#v4000487# 5*#v4001266# \r\n"//3
//			text += "#L5##e#d第五项#r 奖励:20万金币 20万经验 5*#v4000487# 5*#v4001266# \r\n"//3
//     		text += "#L6##e#d第六项#r 奖励:50万金币 50万经验 10*#v4000487# 10*#v4001266# \r\n"//3
			text += "#L7##e#d领取#r 奖励:一些赏金和1*#v4001225# \r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
			if(cm.getLevel() >= 10){
			cm.openNpc(9900004, 151);
		}else{
				cm.sendOk("你等级未达到10级!");
				cm.dispose();
			}		
        } else if (selection == 2) {
			if(cm.getLevel() >= 10){
		//			if(cm.getLevel() >= 20 && cm.getLevel() <= 50){
		cm.openNpc(9900004, 152);
		}else{
				cm.sendOk("你等级未达到10级!");
				cm.dispose();
			}
		} else if (selection == 3) {
			if(cm.getLevel() >= 10){
		//			if(cm.getLevel() >= 20 && cm.getLevel() <= 50){
		cm.openNpc(9900004, 153);
		}else{
				cm.sendOk("你等级未达到10级!");
				cm.dispose();
			}	
        } else if (selection == 4) {
			if(cm.getLevel() >= 10){
		//						if(cm.getLevel() >= 51 && cm.getLevel() <= 100){
		cm.openNpc(9900004, 154);
		}else{			
				cm.sendOk("你等级未达到10级!");
				cm.dispose();
			}
		} else if (selection == 5) {
	if(cm.getLevel() >= 10){
		//						if(cm.getLevel() >= 51 && cm.getLevel() <= 100){
		cm.openNpc(9900004, 155);
		}else{			
				cm.sendOk("你等级未达到10级!");
				cm.dispose();
			}	
		} else if (selection == 6) {
						if(cm.getLevel() >= 100 && cm.getLevel() <= 200){
		cm.openNpc(9900004, 156);
		}else{
			
				cm.sendOk("你等级未达到100级!");
				cm.dispose();
			}	
		} else if (selection == 7) {
						if(cm.getLevel() >= 51 && cm.getLevel() <= 70){
		cm.openNpc(9310071, 7);
		}else{
			
				cm.sendOk("你等级需达到51-70级范围内!");
				cm.dispose();
			}
	}
    }
}


