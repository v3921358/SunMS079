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
    if (status >= 0 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.haveItem(4031035)) {
	    cm.sendNext("嗯，那是我哥哥的信！可能骂我以为我不工作…嗯？啊…你是我哥哥的建议和训练你的宠物，到了这里，是吗？好的！既然你很努力地到这里来，我会用你的宠物来提高你的亲密程度.");
	} else {
	    cm.sendOk("我哥哥告诉我要照顾宠物障碍物课程，但…因为我离他那么远，我忍不住想游手好闲…呵呵，因为我没有看到他，不妨冷静几分钟.");
	    cm.dispose();
	}
    } else if (status == 1) {
	if (cm.getPlayer().getPet(0) == null) {
	    cm.sendNextPrev("嗯...你真的和你的宠物在一起了吗？这些障碍是为宠物而设定。你没有它，你在这里干什么？？离开这里!");
	} else {
	    cm.gainItem(4031035, -1);
	    cm.gainClosenessAll(2);
	    cm.sendNextPrev("您是怎么想的？难道你不认为你已经离你的宠物更近了吗？如果你有时间，在这个障碍物上训练你的宠物……当然，我哥哥必须同意。");
	}
	cm.dispose();
    }
}