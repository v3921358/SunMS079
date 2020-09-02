/* 
 * Sharen III's Soul, Sharenian: Sharen III's Grave (990000700)
 * Guild Quest - end of stage 4
 */

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (cm.getEventInstance().getProperty("leader").equals(cm.getPlayer().getName())) {
            if (cm.getEventInstance().getProperty("stage4clear") != null && cm.getEventInstance().getProperty("stage4clear").equals("true")) {
				cm.sendOk("之后，我认为将是一个不朽的睡眠，我终于发现有人能救他。我现在可以真正地休息了.");
                cm.safeDispose();
            } else {
                var prev = cm.getEventInstance().setProperty("stage4clear", "true", true);
                if (prev == null) {
                    cm.sendNext("我以为会是一个不朽的睡眠之后，我终于发现有人，将节省杀人年。这老头现在铺平道路为你完成任务.");
                } else { //if not null, was set before, and Gp already gained
                    cm.sendOk("之后，我认为将是一个不朽的睡眠，我终于发现有人能救他。我现在可以真正地休息了.");
                    cm.safeDispose();
                }
            }
        } else {
            if (cm.getEventInstance().getProperty("stage4clear") != null && cm.getEventInstance().getProperty("stage4clear").equals("true")) {
                cm.sendOk("之后，我认为将是一个不朽的睡眠，我终于发现有人能救他。我现在可以真正地休息了.");
            } else {
                cm.sendOk("我需要你的队伍的队长跟我说话.");
            }
            cm.safeDispose();
        }
    } else if (status == 1) {
        var react = cm.getPlayer().getEventInstance().getMapFactory().getMap(990000700).getReactorByName("ghostgate");//后添加修复
        cm.getPlayer().getEventInstance().getMapFactory().getMap(990000700).getReactorByName("ghostgate").forceHitReactor(react.getState() + 1);//后添加修复
        //cm.getMap().getReactorByName("ghostgate").hitReactor(cm.getC());//原版
        cm.gainGP(180);
		cm.showEffect(true, "quest/party/clear");
        cm.playSound(true, "Party1/Clear");
        cm.dispose();
    }
}