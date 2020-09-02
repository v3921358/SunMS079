/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;
var beauty = 0;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("嗨，我是#p1052014#，我这里收集者世界各国的精美发型头套，如果你有 #b#t5150038##k , 我就可以为你打造超级明星的发型。\r\n#L0#使用: #i5150038##t5150038##l\r\n");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [30200,30220,30260,30280,30350,30360,30440,30480,30540,30610,30630,30840,30660,30780,30880,30420,30640,30700,30010,30040,30060,30190,30290,30300,30310,30320,30330,30490,30510,30520,30530,30550,30620,30760,30820,30830];
	    } else {
		hair_Colo_new = [31020,31070,31090,31140,31160,31170,31210,31250,31260,31310,31320,31340,31350,31410,31440,31450,31460,31470,31490,31510,31530,31520,31550,31610,31620,31640,31650,31660,31670,31680,31800,31810,31820];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("确定要使用 #b#t5150038##k随机更换超级明星发型？");

	} else if (selection == 1) {
	    var currenthaircolo = Math.floor((cm.getPlayerStat("HAIR") / 10)) * 10;
	    hair_Colo_new = [];
	    beauty = 2;

	    for (var i = 0; i < 8; i++) {
		hair_Colo_new[i] = currenthaircolo + i;
	    }
	    cm.sendYesNo("确定要使用 #b#t5150038##k 随机染发了？？");
	}
    } else if (status == 2){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150038, hair_Colo_new) == 1) {
		cm.sendOk("对你的超级发型还满意吗？");
	    } else {
		cm.sendOk("啊.... 貌似没有#b#t5150038##k。");
	    }
	} else {
	    if (cm.setRandomAvatar(5150038, hair_Colo_new) == 1) {
		cm.sendOk("查看你的新发型吧！");
	    } else {
		cm.sendOk("啊.... 貌似没有#b#t5150038##k。");
	    }
	}
	cm.safeDispose();
    }
}