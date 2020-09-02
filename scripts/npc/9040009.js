/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

var status = -1;
var stage;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    var eim = cm.getEventInstance();
    if (eim == null) {
        cm.warp(990001100);
    } else {
        if (eim.getProperty("leader").equals(cm.getName())) {
            if (cm.getMap().getReactorByName("statuegate").getState() > 0) {
                cm.sendOk("进行。");
                cm.safeDispose();
            } else {
                if (status == 0) {
                    if (eim.getProperty("stage1status") == null || eim.getProperty("stage1status").equals("waiting")) {
                        if (eim.getProperty("stage1phase") == null) {
                            stage = 1;
                            eim.setProperty("stage1phase", stage);
                        } else {
                            stage = parseInt(eim.getProperty("stage1phase"));
                        }
                        if (stage == 1) {
                            cm.sendOk("在这个挑战中，我将展示我周围的雕像的图案。当我给这个词，重复的模式，我继续。");
                        } else {
                            cm.sendOk("我将为你呈现一个更难的谜题。好运。")
                        }
                    } else if (eim.getProperty("stage1status").equals("active")) {
                        stage = parseInt(eim.getProperty("stage1phase"));
                        if (eim.getProperty("stage1combo").equals(eim.getProperty("stage1guess"))) {
                            if (stage == 3) {
								var react = cm.getMap().getReactorByName("statuegate");
                                cm.getMap().getReactorByName("statuegate").forceHitReactor(react.getState() + 1);
                                //cm.getMap().getReactorByName("statuegate").hitReactor(cm.getClient());
                                cm.sendOk("漂亮。请进入下一阶段。");
                                cm.showEffect(true, "quest/party/clear");
                                cm.playSound(true, "Party1/Clear");
                                var prev = eim.setProperty("stage1clear", "true", true);
                                if (prev == null) {
                                    cm.gainGP(900);
                                }
                            } else {
                                cm.sendOk("很好。然而你仍然有更多的来完成。当你准备好的时候再和我说话。");
                                eim.setProperty("stage1phase", stage + 1);
                                cm.mapMessage("你已经完成了部分 " + stage + " 的石像测试。");
                            }
                        } else {
                            cm.sendOk("你不及格。");
                            cm.mapMessage("你已经失败了。");
                            eim.setProperty("stage1phase", "1")
                        }
                        eim.setProperty("stage1status", "waiting");
                        cm.safeDispose();
                    } else {
                        cm.sendOk("请等待。");
                        cm.safeDispose();
                    }
                } else if (status == 1) {
                    //only applicable for "waiting"
                    var reactors = getReactors();
                    var combo = makeCombo(reactors);
                    var reactorString = "调试：反应堆地图: ";
                                                for (var i = 0; i < reactors.length; i++) {
                                                        reactorString += reactors[i] + " ";
                                                }
                                                cm.playerMessage(reactorString);
                                                reactorString = "调试：在反应器组合: ";
                                                for (var i = 0; i < combo.length; i++) {
                                                        reactorString += combo[i] + " ";
                                                }
                                                cm.playerMessage(reactorString);
                    cm.mapMessage("请稍候，该组合显示。");
                    var delay = 5000;
                    for (var i = 0; i < combo.length; i++) {
                        cm.getMap().getReactorByOid(combo[i]).delayedHitReactor(cm.getClient(), delay + 3500 * i);
                    }
                    eim.setProperty("stage1status", "display");
                    eim.setProperty("stage1combo", "");
                    cm.dispose();
                }
            }
        } else {
            cm.sendOk("你不是族长！请叫你们族长来找我！");
            cm.safeDispose();
        }
    }
}

//method for getting the statue reactors on the map by oid
function getReactors() {
    var reactors = new Array();
    var iter = cm.getPlayer().getMap().getAllReactorsThreadsafe().iterator();
    while (iter.hasNext()) {
        var mo = iter.next();
        if (!mo.getName().equals("statuegate")) {
            reactors.push(mo.getObjectId());
        }
    }
    return reactors;
}

function makeCombo(reactors) {
    var combo = new Array();
    while (combo.length < (stage + 3)) {
        var chosenReactor = reactors[Math.floor(Math.random() * reactors.length)];
        //cm.log("Debug: Chosen Reactor " + chosenReactor)
        var repeat = false;
        if (combo.length > 0) {
            for (var i = 0; i < combo.length; i++) {
                if (combo[i] == chosenReactor) {
                    repeat = true;
                    //cm.log("Debug: repeat reactor: " + chosenReactor);
                    break;
                }
            }
        }
        if (!repeat) {
            //cm.log("Debug: unique reactor: " + chosenReactor);
            combo.push(chosenReactor);
        }
    }
    return combo;
}