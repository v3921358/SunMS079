/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */

function start() {
    //everything can be done in one status, so let's do it here.
    var eim = cm.getEventInstance();
    if (eim == null) {
        cm.warp(990001100);
    } else {
        if (eim.getProperty("leader").equals(cm.getName())) {
            if (cm.getMap().getReactorByName("watergate").getState() > 0) {
                cm.sendOk("您可以继续.");
            } else {
                var currentCombo = eim.getProperty("stage3combo");
                if (currentCombo == null || currentCombo.equals("reset")) {
                    var newCombo = makeCombo();
                    eim.setProperty("stage3combo", newCombo);
                    //cm.playerMessage("Debug: " + newCombo);
                    eim.setProperty("stage3attempt", "1");
                    cm.sendNext("喷水池前的四个石像是古代圣瑞尼亚的宗臣。他们拥有国王生前赐予的贵重宝物。如果你将国王的赏赐品作为供品，将能打开通往成立的秘密之门\r\n#v4001027##b勇猛勋章\t\t#v4001028#兵法卷轴\r\n#v4001029#过期的食物\t\t#v4001030#700年青蛇酒\r\n#k就是这些，但我们不清楚谁需要哪种供品，而且其中也会有没用的供品。\r\n把供品放在石像前进行对话，会问是不是你所需要的。\r\n但是如果错的太多，宗臣会发怒，一定要在#r100次#k内猜对。")  
                } else {
                    var attempt = parseInt(eim.getProperty("stage3attempt"));
                    var combo = parseInt(currentCombo);
                    var guess = getGroundItems();
                    if (guess != null) {
                        if (combo == guess) {
							var react = cm.getMap().getReactorByName("watergate");//后添加
                            cm.getMap().getReactorByName("watergate").forceHitReactor(react.getState() + 1);
                            cm.sendOk("您可能会继续进行。");
                            cm.showEffect(true, "quest/party/clear");
                            cm.playSound(true, "Party1/Clear");
                            var prev = eim.setProperty("stage3clear", "true", true);
                            if (prev == null) {
                                cm.gainGP(1500);
                            }
                        } else {
                            if (attempt < 20) {
                                //cm.playerMessage("Combo : " + combo);
                                //cm.playerMessage("Guess : " + guess);
                                var parsedCombo = parsePattern(combo);
                                var parsedGuess = parsePattern(guess);
                                var results = compare(parsedCombo, parsedGuess);
                                var string = "";
                                //cm.playerMessage("Results - Correct: " + results[0] + " | Incorrect: " + results[1] + " | Unknown: " + results[2]);
                                if (results[0] != 0) {
                                    if (results[0] == 1) {
                                        string +="1 位宗臣很高兴这是他要的供品.\r\n";
                                    } else {
                                        string += results[0] + "宗臣说供品正确.\r\n";
                                    }
                                }
                                if (results[1] != 0) {
                                    if (results[1] == 1) {
                                        string += "1 位宗臣已经收到了不正确供品\r\n";
                                    } else {
                                        string += results[1] + " 位宗臣收到了不正确供品.\r\n";
                                    }
                                }
                                if (results[2] != 0) {
                                    if (results[2] == 1) {
                                        string +="1 位宗臣说不认识供品.\r\n";
                                    } else {
                                        string += results[2] + "位宗臣收到了不认识的供品.\r\n";
                                    }
                                }
                                string += "这是你的 ";
                                switch (attempt) {
                                case 1:
                                    string += "第#r1#k次";
                                    break;
                                case 2:
                                    string += "第#r2#k次";
                                    break;
                                case 3:
                                    string += "第#r3#k次";
                                    break;
                                case 4:
                                    string += "第#r4#k次";
                                    break;
                                case 5:
                                    string += "第#r5#k次";
                                    break;
                                case 6:
                                    string += "第#r6#k次";
                                    break;
                                case 7:
                                    string += "第#r7#k次";
                                    break;
                                case 8:
                                    string += "第#r8#k次";
                                    break;
                                case 9:
                                    string += "第#r9#k次";
                                    break;
                                case 10:
                                    string += "第#r10#k次";
                                    break;
                                case 11:
                                    string += "第#r10#k次";
                                    break;
                                case 12:
                                    string += "第#r10#k次";
                                    break;
                                case 13:
                                    string += "第#r10#k次";
                                    break;
                                case 14:
                                    string += "第#r10#k次";
                                    break;
                                case 15:
                                    string += "第#r10#k次";
                                    break;
                                case 16:
                                    string += "第#r10#k次";
                                    break;
                                case 17:
                                    string += "第#r10#k次";
                                    break;
                                case 18:
                                    string += "第#r10#k次";
                                    break;
                                case 19:
                                    string += "第#r10#k次";
                                    break;
                                case 20:
                                    string += "第#r10#k次";
                                    break;
                                default:
                                    string += attempt + "th";
                                    break;
                                }
                                string += " 尝试.";

                                //spawn one black and one myst knight
                                cm.spawnMob(9300036, -350, 150);
                                cm.spawnMob(9300037, 400, 150);

                                cm.sendOk(string);
                                eim.setProperty("stage3attempt", attempt + 1);
                            } else {
                                //reset the combo and mass spawn monsters
                                eim.setProperty("stage3combo", "reset");
                                cm.sendOk("本次考验你已经失败。请从新尝试！");

                                for (var i = 0; i < 5; i++) {
                                    //keep getting new monsters, lest we spawn the same monster five times o.o!
                                    cm.spawnMob(9300036, randX(), 150);
                                    cm.spawnMob(9300037, randX(), 150);
                                }
                            }
                        }
                    } else {
                        cm.sendOk("请确保您尝试在宗臣前设置正确，并再次跟我说话.");
                    }
                }
            }
        } else {
            cm.sendOk("请你的领导跟我说话.");
        }
    }
    cm.dispose();
}

function action(mode, type, selection) {}

function makeCombo() {
    var combo = 0;

    for (var i = 0; i < 4; i++) {
        combo += Math.floor(Math.random() * 4) * Math.pow(10, i);
    }

    return combo;
}

//check the items on ground and convert into an applicable string; null if items aren't proper
function getGroundItems() {
    var items = cm.getMap().getItemsInRange(cm.getPlayer().getPosition(), java.lang.Double.POSITIVE_INFINITY);
    var itemInArea = new Array( - 1, -1, -1, -1);

    if (items.size() != 4) {
        cm.playerMessage("地图上还有的项目太多。请删除一些");
        return null;
    }

    var iter = items.iterator();
    while (iter.hasNext()) {
        var item = iter.next();
        var id = item.getItem().getItemId();
        if (id < 4001027 || id > 4001030) {
            cm.playerMessage("在地图上的一些项目不需要4个项目的一部分");
            return null;
        } else {
            //check item location
            for (var i = 0; i < 4; i++) {
                if (cm.getMap().getArea(i).contains(item.getPosition())) {
                    itemInArea[i] = id - 4001027;
                    //cm.playerMessage("Item in area "+i+": " + id);
                    break;
                }
            }
        }
    }

    //guaranteed four items that are part of the stage 3 item set by this point, check to see if each area has an item
    if (itemInArea[0] == -1 || itemInArea[1] == -1 || itemInArea[2] == -1 || itemInArea[3] == -1) {
        cm.playerMessage("请正确的位置放置这些: " + (itemInArea[0] == -1 ? "Statue 1, ": "") + (itemInArea[1] == -1 ? "Statue 2, ": "") + (itemInArea[2] == -1 ? "Statue 3, ": "") + (itemInArea[3] == -1 ? "Statue 4. ": ""));
        /*  for (var i = 0; i < 4; i++) {
                        cm.playerMessage("Item in area "+i+": " + itemInArea[i]);
                }*/
        return null;
    }

    return (itemInArea[0] * 1000 + itemInArea[1] * 100 + itemInArea[2] * 10 + itemInArea[3]);
}

//convert an integer for answer or guess into int array for comparison
function parsePattern(pattern) {
    var tempPattern = pattern;
    var items = new Array( - 1, -1, -1, -1);
    for (var i = 0; i < 4; i++) {
        items[i] = Math.floor(tempPattern / Math.pow(10, 3 - i));
        tempPattern = tempPattern % Math.pow(10, 3 - i);
    }
    return items;
}

// compare two int arrays for the puzzle
function compare(answer, guess) {
    var correct = 0;
    var incorrect = 0;
    /*var debugAnswer = "Combo : ";
        var debugGuess = "Guess : ";
        
        for (var d = 0; d < answer.length; d++) {
                debugAnswer += answer[d] + " ";
                debugGuess += guess[d] + " ";
        }
        
        cm.playerMessage(debugAnswer);
        cm.playerMessage(debugGuess);*/

    for (var i = 0; i < answer.length; i) {
        if (answer[i] == guess[i]) {
            correct++;
            //cm.playerMessage("Item match : " + answer[i]);

            //pop the answer/guess at i
            if (i != answer.length - 1) {
                answer[i] = answer[answer.length - 1];
                guess[i] = guess[guess.length - 1];
            }

            answer.pop();
            guess.pop();

            /*/debugAnswer = "Combo : ";
                        debugGuess = "Guess : ";

                        for (var d = 0; d < answer.length; d++) {
                                debugAnswer += answer[d] + " ";
                                debugGuess += guess[d] + " ";
                        }

                        cm.playerMessage(debugAnswer);
                        cm.playerMessage(debugGuess);*/
        }
        else {
            i++;
        }
    }

    //check remaining answers for "incorrect": correct item in incorrect position
    var answerItems = new Array(0, 0, 0, 0);
    var guessItems = new Array(0, 0, 0, 0);

    for (var j = 0; j < answer.length; j++) {
        var aItem = answer[j];
        var gItem = guess[j]
        answerItems[aItem]++;
        guessItems[gItem]++;
    }

    /*for (var d = 0; d < answer.length; d++) {
                cm.playerMessage("Item " + d + " in combo: " + answerItems[d] + " | in guess: " + guessItems[d]);
        }*/

    for (var k = 0; k < answerItems.length; k++) {
        var inc = Math.min(answerItems[k], guessItems[k]);
        //cm.playerMessage("Incorrect for item " + k + ": " + inc);
        incorrect += inc;
    }

    return new Array(correct, incorrect, (4 - correct - incorrect));
}

//for mass spawn
function randX() {
    return - 350 + Math.floor(Math.random() * 750);
}