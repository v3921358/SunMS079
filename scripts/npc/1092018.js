/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = -1;

function action(mode, type, selection) {
    if (status == -1) {
	status = 0;
	cm.sendNext("一个半书面信件...也许这是很重要的！我应该看看?");
    } else if (status == 0) {
	if (cm.haveItem(4031839)) {
	    cm.sendOk("我已经挑一个。我不认为我会需要拿起另外一个.");
	    cm.safeDispose();
	} else {
	    cm.gainItem(4031839,1);
	    cm.sendOk("我可以勉强了这一点......但它读取Kyrin.");
	    cm.safeDispose();
	}
    }
}