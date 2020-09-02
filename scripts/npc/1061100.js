/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */

var status = 0;
var regcost = 5000;
var vipcost = 10000;
var tempvar;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++; if (mode == 0 && status == 1) {
	cm.dispose();
	return;
    } if (mode == 0 && status == 2) {
	cm.sendNext("仔细在想想吧!");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendNext("欢迎来到林中之城旅馆。我们尽心为您提供最好的服务。如果您累了，来这里休息一下如何？");
    }
    if (status == 1) {
	cm.sendSimple("我们提供两种房间，请选择你想要的\r\n#b#L0#普通桑拿房#l\r\n#L1#高级桑拿房#l");
    }
    if (status == 2) {
	tempvar = selection;
	if (tempvar == 0) {
	    cm.sendYesNo("你选择了普通桑拿房，你的HP和MP会回复得很快，你也可以在里面购买商品，你确定要进入吗?所需#r金币5000。");
	}
	if (tempvar == 1) {
		cm.sendYesNo("你选择了高级桑拿房，你的HP和MP会比一般桑拿室回复得更快，也可以在里面找到特殊的物品，你确定要进入吗？所需#r金币10000.");
	}
    }
    if (status == 3) {
	if (tempvar == 0) {
	    if (cm.getMeso() >= regcost) {
		cm.warp(105040401);
		cm.gainMeso(-regcost);
	    } else {
        cm.sendNext("很抱歉，看起来您似乎没有足够的金币。你至少要有 " + regcost + " 金币才能去普通桑拿房");
	    }
	} if (tempvar == 1) {
	    if (cm.getMeso() >= vipcost) {
		cm.warp(105040402);
		cm.gainMeso(-vipcost);
	    } else {
        cm.sendNext("很抱歉，看起来您似乎没有足够的金币。你至少要有 " + vipcost + " 金币才能去高级桑拿房");
	    }
	}
	cm.dispose();
    }
}
