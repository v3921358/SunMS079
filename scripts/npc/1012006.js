/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
	cm.dispose();
	return;
    } else if (status >= 1 && mode == 0) {
	cm.sendNext("需要的时候可以来找我。");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendSimple("你想要跟我谈什么？？\r\n#L0##b我想要训练宠物。#l\r\n#L1#我想要学习群宠技能#s8##z8##l");
    } else if (status == 1) {
	if (selection == 0) {
	    if (cm.haveItem(4031035)) {
		cm.sendNext("拿到这一封信，跳跃过那些障碍到达顶部把这封信给我弟弟他会给你奖励...");
		cm.dispose();
	    } else {
		cm.gainItem(4031035, 1);
        cm.sendNext("拿到这一封信，跳跃过那些障碍到达顶部把这封信给我弟弟他会给你奖励...");
        cm.dispose();
		}
    } else if (status == 1) {
					if(cm.haveItem(5460000) && cm.getJob() >=0 && cm.getJob() <= 900){
						cm.teachSkill(0000008, 1, 1);
						cm.gainItem(5460000, -1);
						cm.sendOk("恭喜你,已经学会群宠技能!!");
						cm.dispose();
					} else if(cm.haveItem(5460000) && cm.getJob() >=2000 && cm.getJob() <= 2112){
						cm.teachSkill(20000024, 1, 1);
						cm.gainItem(5460000, -1);
						cm.sendOk("恭喜你,已经学会群宠技能!!");
						cm.dispose();	
					} else if(cm.haveItem(5460000) && cm.getJob() >=1000 && cm.getJob() <= 1999){
						cm.teachSkill(10000018, 1, 1);
						cm.gainItem(5460000, -1);
						cm.sendOk("恭喜你,已经学会群宠技能!!");
						cm.dispose();	
                    } else {
						cm.sendOk("我需要一个#r#v5460000##z5460000##k,请到商城购买后在来找我吧!!");
						cm.dispose();
					}
    }
}}