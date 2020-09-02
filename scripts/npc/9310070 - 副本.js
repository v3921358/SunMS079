var status = 0;
var mmm = 0;
var kkk = 0;
var chance1 = Math.floor(Math.random()*6+1);
var itemchance = Math.floor(Math.random()*30+1);
var mgb = new Array(4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031996,4031995,4031995,4031995,4031995,4031995,4031994);
var randmgb = Math.floor(Math.random() * mgb.length);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (status == 6 || mode == -1) {
		cm.dispose();
	} else {
		if (status == 5) {
			status = 6;
			return;
		}
	if (mode == 1)
		status++;
	else
		status--;
		if (status == 0) {
				if(cm.getPlayer().getBossLog("meiriJ2")>=10){
					cm.sendOk("您今天已经满10次任务了，明天再来吧");
					cm.dispose();
				}else{
					if(cm.getPlayer().getBossLog("meiriJ2")==0){
						cm.getPlayer().setBossLog("meiriJ1");
					}
					if (cm.getPlayer().getBossLog("meiriJ01") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000034#(蛇皮100个),#v4000021#动物皮,#v4000135# 大海贼王戴的帽子各50个,..");
					}else if (cm.getPlayer().getBossLog("meiriJ02") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要火独眼兽之尾100个#v4000007#,#v4000143#僵尸娃娃,#v4000180#鲨鱼尖锐的假牙各100个,...");
					}else if (cm.getPlayer().getBossLog("meiriJ03") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000013#(风独眼兽的尾巴), #v4000129#红小丑的小珠,#v4000263#发出红光的红海龟龙的背壳各100个,..");
					}else if (cm.getPlayer().getBossLog("meiriJ04") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000171#虎皮,#v4000110#木马骑兵的剑,#v4000267#邪恶双刀蜥蜴肩膀上的铠甲各100个,");
					}else if (cm.getPlayer().getBossLog("meiriJ05") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000465#鼻环,#v4000232#半人马的火花,#v4000268#红飞龙的红色翅膀各30个,");
					}else if (cm.getPlayer().getBossLog("meiriJ06") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000035#小幽灵的桌布,#v4000291#银人心,#v4000274#骷髅龙头顶上而断裂的角各50个,");
					}else if (cm.getPlayer().getBossLog("meiriJ07") == 1 ){
							mmm = 2;
							cm.sendNext("收集到我的东西了吗?.我需要#v4000039#用钢铁来做的铁甲猪的铁蹄,#v4000019#绿色蜗牛壳,#v4000270#附在黑飞龙翅膀两端的尖锐指甲各100个,");										
					}else{
						mmm = 1;
						cm.sendNext("欢迎您参加今日的试练任务.每天最多只能做10次任务\r\n每次做完成后，能得到1点#rmeiri成长值#k\r\n金,银,铜蘑菇奖牌随机一个\r\n全部做完后还有额外5点#rmeiri成长值#k\r\n当然了.要注意中途是不能放弃的哟.如果你感兴趣的话现在就可以开始哟...\r\n#r今天试练任务(" + cm.getPlayer().getBossLog("meiriJ1") + "/10)#k");
					}
				}
		} else if (status == 1) {
			if (mmm == 1){
			if (chance1 == 1){
				cm.sendOk("去外面寻找#v4000034#(蛇皮100个),#v4000021#动物皮,#v4000135# 大海贼王戴的帽子,各50个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ01");
				cm.dispose();
			}else if (chance1 == 2){
				cm.sendOk("去外面寻找火独眼兽之尾100个#v4000007#,#v4000143#僵尸娃娃,#v4000180#鲨鱼尖锐的假牙,各100个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ02");
				cm.dispose();
			}else if (chance1 == 3){
				cm.sendOk("去外面寻找#v4000013#(风独眼兽的尾巴), #v4000129#红小丑的小珠,#v4000263#发出红光的红海龟龙的背壳,各100个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ03");
				cm.dispose();
			}else if (chance1 == 4){
				cm.sendOk("去外面寻找#v4000171#虎皮,#v4000110#木马骑兵的剑,#v4000267#邪恶双刀蜥蜴肩膀上的铠甲,各100个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ04");
				cm.dispose();
			}else if (chance1 == 5){
				cm.sendOk("去外面寻找#v4000465#鼻环,#v4000232#小猎犬的尖牙,#v4000268#红飞龙的红色翅膀,各30个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ05");
				cm.dispose();
			}else if (chance1 == 6){
				cm.sendOk("去外面寻找#v4000035#小幽灵的桌布,#v4000291#银人心,#v4000274#骷髅龙头顶上而断裂的角,各50个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ06");
				cm.dispose();
			}else if (chance1 == 7){
				cm.sendOk("去外面寻找#v000039#用钢铁来做的铁甲猪的铁蹄,#v4000019#绿色蜗牛壳,#v4000270#附在黑飞龙翅膀两端的尖锐指甲,各100个,找到后在来找我吧!");
				cm.getPlayer().setBossLog("meiriJ07");
				cm.dispose();
				}
			}else if (mmm ==2){
			if (cm.getPlayer().getBossLog("meiriJ01") == 1){
				if (cm.haveItem(4000034,100) && cm.haveItem(4000021,100) && cm.haveItem(4000135,100)){
					kkk = 1;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ02") == 1){
				if (cm.haveItem(4000007,100) && cm.haveItem(4000143,100) && cm.haveItem(4000180,100)){
					kkk = 2;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ03") == 1){
				if (cm.haveItem(4000013,100) && cm.haveItem(4000129,100) && cm.haveItem(4000263,100)){
					kkk = 3;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ04") == 1){
				if (cm.haveItem(4000171,100) && cm.haveItem(4000110,100) && cm.haveItem(4000267,100)){
					kkk = 4;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ05") == 1){
				if (cm.haveItem(4000465,100) && cm.haveItem(4000232,100) && cm.haveItem(4000268,100)){
					kkk = 5;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ06") == 1){
				if (cm.haveItem(4000035,100) && cm.haveItem(4000291,100) && cm.haveItem(4000274,100)){
					kkk = 6;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
				}
			}else if(cm.getPlayer().getBossLog("meiriJ07") == 1){
				if (cm.haveItem(4000039,100) && cm.haveItem(4000019,100) && cm.haveItem(4000270,100)){
					kkk = 7;
					cm.sendNext("这么快就收集到了我想要的物品了哟,看看能获得什么好的物品吧!");
				}else{
					cm.sendOk("看来你还没有收集到我需要的物品,做任务是需要有耐心的..加油吧!!!..!");
					cm.dispose();
					}
				}//最后一个IF结尾
			}
		} else if (status == 2) {
            if (kkk ==1){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000034, -100);
				cm.gainItem(4000021, -100);
				cm.gainItem(4000135, -100);
				cm.getPlayer().setBossLog("meiriJ01");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==2){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000007, -100);
				cm.gainItem(4000143, -100);
				cm.gainItem(4000180, -100);
				cm.getPlayer().setBossLog("meiriJ02");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==3){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000013, -100);
				cm.gainItem(4000129, -100);
				cm.gainItem(4000263, -100);
				cm.getPlayer().setBossLog("meiriJ03");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==4){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000171, -100);
				cm.gainItem(4000110, -100);
				cm.gainItem(4000267, -100);
				cm.getPlayer().setBossLog("meiriJ04");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==5){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000465, -100);
				cm.gainItem(4000232, -100);
				cm.gainItem(4000268, -100);
				cm.getPlayer().setBossLog("meiriJ05");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==6){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000035, -100);
				cm.gainItem(4000291, -100);
				cm.gainItem(4000274, -100);
				cm.getPlayer().setBossLog("meiriJ06");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }else if (kkk ==7){
				cm.sendNext("恭喜你做完此次任务\r\n#rmeiri成长值#k+1\r\n并获得#z" + mgb[randmgb] + "#x1");
				cm.gainItem(4000039, -100);
				cm.gainItem(4000019, -100);
				cm.gainItem(4000270, -100);
				cm.getPlayer().setBossLog("meiriJ07");
				cm.getPlayer().setBossLog("meiriJ1",1);
				cm.getPlayer().setBossLog("meiriJ2");
            }
			if(cm.getPlayer().getBossLog("meiriJ1",1) == 10){
				cm.worldMessage("[VIP每日任务]：恭喜玩家:["+cm.getChar().getName()+"]完成了10次VIP任务,额外获得了5的meiri成长值");
			}else{
				cm.dispose();
			}
		} else if (status == 3) {
			cm.sendOk("恭喜你今天完成了10次任务，额外获得5点#rmeiri成长值#k");
			cm.dispose();
		} else if (status == 4) {
			cm.sendNext("#b...#k");
		} else if (status == 5) {
			cm.sendOk("..!");
			cm.dispose();
		}
	}
} 
