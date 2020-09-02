/*
Moose, Power of Shield
*/
var status = -1;

function action(mode, type, selection){
    if (mode == 1) {
	status++
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
	if (cm.getMapId() == 924000002) { // At Exit Map
	    cm.warp(240010400, 0);
		cm.dispose();
	} else if (cm.getMapId() == 924000000) { // At start map
	    cm.sendNext("我必须让你知道一件事，我送你去训练场。 你必须持有 #b#t1092041##k 在去盾牌训练场。 否则，你死定了.");
	} else {
	    cm.warp(924000002, 0);
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.sendSimple("不要忘记#r一定要佩戴#t1092041##k才进去! \r\n #b#L0#我想得到#r#t1092041##l\r\n#b#L1# 让我进去#m924000001##l\r\n#b#L2# 让我出去#l");

    } else if (status == 2) {
	if (selection == 0) {
	    if (!cm.haveItem(1092041) && !cm.getPlayer().hasEquipped(1092041)) {
		if (cm.canHold(1092041)) {
		    cm.gainItem(1092041, 1);
		    cm.sendOk("我给了你 #t1092041#. 检查库存。 你必须配备它!");
		} else {
		    cm.sendOk("我不能给你 #t1092041##k 因为在背包已经满了。或者已经给过你了!" );
		}
	    } else {
		cm.sendOk("你已经拥有了 #r#t1092041##k.不需要更多.");
	    }
	    cm.safeDispose();
	} else if (selection == 1) {
		 map = cm.getPlayer().getMap();
	    cm.warp(924000001, 0);
		cm.getPlayer().startMapTimeLimitTask(1200, map);
	    cm.dispose();
	} else {
	    cm.warp(240010400, 0);
	    cm.dispose();
	}
    }}
