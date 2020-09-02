/* ==================
 脚本类型:  NPC	    
 脚本作者：月亮     
 联系方式：2412614144
 =====================
 */
var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;
var qty;
var equip;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	cm.dispose();
    if (status == 0 && mode == 1) {
	var selStr = "嗨，我是伯坚 有什么我可以帮助你的？？.#b"
	var options = new Array("制作矿石", "制作宝石矿", "制作高级宝石矿", "制作高级水晶", "制作材料","制作弓箭");
	for (var i = 0; i < options.length; i++){
	    selStr += "\r\n#L" + i + "# " + options[i] + "#l";
	}
	cm.sendSimple(selStr);
    } else if (status == 1 && mode == 1) {
	selectedType = selection;
	if (selectedType == 0){ //mineral refine
	    var selStr = "需要什么吗？？#b";
	    var minerals = new Array ("#t4011000#", "#t4011001#", "#t4011002#", "#t4011003#", "#t4011004#", "#t4011005#", "#t4011006#");
	    for (var i = 0; i < minerals.length; i++){
		selStr += "\r\n#L" + i + "# " + minerals[i] + "#l";
	    }
	    equip = false;
	    cm.sendSimple(selStr);
	} else if (selectedType == 1){ //jewel refine
	    var selStr = "需要什么吗？？#b";
	    var jewels = new Array ("#t4021000#", "#t4021001#", "#t4021002#", "#t4021003#", "#t4021004#", "#t4021005#", "#t4021006#", "#t4021007#", "#t4021008#");
	    for (var i = 0; i < jewels.length; i++){
		selStr += "\r\n#L" + i + "# " + jewels[i] + "#l";
	    }
	    equip = false;
	    cm.sendSimple(selStr);
	} else if (selectedType == 2){ //rock refine
	    var selStr = "需要什么吗？？#b";
	    var items = new Array ("#t4011007#", "#t4021009#");
	    for (var i = 0; i < items.length; i++){
		selStr += "\r\n#L" + i + "# " + items[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	} else if (selectedType == 3){ //crystal refine
	    var selStr = "需要什么吗？？#b";
	    var crystals = new Array ("#t4005000#", "#t4005001#", "#t4005002#", "#t4005003#", "#t4005004#");
	    for (var i = 0; i < crystals.length; i++){
		selStr += "\r\n#L" + i + "# " + crystals[i] + "#l";
	    }
	    equip = false;
	    cm.sendSimple(selStr);
	} else if (selectedType == 4){ //material refine
	    var selStr = "需要什么吗？？#b";
	    var materials = new Array ("树枝制作木材","木块制作木材","制作螺丝钉");
	    for (var i = 0; i < materials.length; i++){
		selStr += "\r\n#L" + i + "# " + materials[i] + "#l";
	    }
	    equip = false;
	    cm.sendSimple(selStr);
	} else if (selectedType == 5){ //arrow refine
	    var selStr = "需要什么吗？？#b";
	    var arrows = new Array ("#t2060000#", "#t2061000#", "#t2060001#", "#t2061001#", "#t2060002#", "#t2061002#");
	    for (var i = 0; i < arrows.length; i++){
		selStr += "\r\n#L" + i + "# " + arrows[i] + "#l";
	    }
	    equip = true;
	    cm.sendSimple(selStr);
	}
	if (equip)
	    status++;
    } else if (status == 2 && mode == 1) {
	selectedItem = selection;
	if (selectedType == 0){ //mineral refine
	    var itemSet = new Array(4011000,4011001,4011002,4011003,4011004,4011005,4011006);
	    var matSet = new Array(4010000,4010001,4010002,4010003,4010004,4010005,4010006);
	    var matQtySet = new Array(10,10,10,10,10,10,10);
	    var costSet = new Array(5000,5000,5000,5000,5000,5000,5000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	} else if (selectedType == 1){ //jewel refine
	    var itemSet = new Array(4021000,4021001,4021002,4021003,4021004,4021005,4021006,4021007,4021008);
	    var matSet = new Array(4020000,4020001,4020002,4020003,4020004,4020005,4020006,4020007,4020008);
	    var matQtySet = new Array(10,10,10,10,10,10,10,10,10);
	    var costSet = new Array (5000,5000,5000,5000,5000,5000,5000,5000,5000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 2){ //rock refine
	    var itemSet = new Array(4011007,4021009);
	    var matSet = new Array(new Array(4011000,4011001,4011002,4011003,4011004,4011005,4011006), new Array(4021000,4021001,4021002,4021003,4021004,4021005,4021006,4021007,4021008));
	    var matQtySet = new Array(new Array(1,1,1,1,1,1,1),new Array(1,1,1,1,1,1,1,1,1));
	    var costSet = new Array(10000,15000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 3){ //crystal refine
	    var itemSet = new Array (4005000,4005001,4005002,4005003,4005004);
	    var matSet = new Array(4004000,4004001,4004002,4004003,4004004);
	    var matQtySet = new Array (10,10,10,10,10);
	    var costSet = new Array (20000,20000,20000,20000,1000000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 4){ //material refine
	    var itemSet = new Array (4003001,4003001,4003000);
	    var matSet = new Array(4000003,4000018,new Array (4011000,4011001));
	    var matQtySet = new Array (10,5,new Array (1,1));
	    var costSet = new Array (0,0,0);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
		
	var prompt = "你想要做一些 #t" + item + "#? 在这种情况下, 我为了要做出最棒的品质，我建议你确保装备栏空间足够。";
		
	cm.sendGetNumber(prompt,1,1,100)
    }
    else if (status == 3 && mode == 1) {
	if (equip)
	{
	    selectedItem = selection;
	    qty = 1;
	}
	else
	    qty = selection;
		
	if (selectedType == 5){ //arrow refine
	    var itemSet = new Array(2060000,2061000,2060001,2061001,2060002,2061002);
	    var matSet = new Array(new Array (4003001,4003004),new Array (4003001,4003004),new Array (4011000,4003001,4003004),new Array (4011000,4003001,4003004),
		new Array (4011001,4003001,4003005),new Array (4011001,4003001,4003005));
	    var matQtySet = new Array (new Array (1,1),new Array (1,1),new Array (1,3,10),new Array (1,3,10),new Array (1,5,15),new Array (1,5,15));
	    var costSet = new Array (0,0,0,0,0,0);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
		
	var prompt = "你想要我做 ";
	if (qty == 1)
	    prompt += "a #t" + item + "#?";
	else
	    prompt += qty + " #t" + item + "#?";
			
	prompt += "我为了要做出最棒的品质，我建议你确保装备栏空间足够!#b";
		
	if (mats instanceof Array){
	    for(var i = 0; i < mats.length; i++){
		prompt += "\r\n#i"+mats[i]+"# " + matQty[i] * qty + " #t" + mats[i] + "#";
	    }
	}
	else {
	    prompt += "\r\n#i"+mats+"# " + matQty * qty + " #t" + mats + "#";
	}
		
	if (cost > 0)
	    prompt += "\r\n#i4031138# " + cost * qty + " 金币";
		
	cm.sendYesNo(prompt);
    } else if (status == 4 && mode == 1) {
	var complete = false;
		
	if (cm.getMeso() < cost * qty) {
	    cm.sendOk("糟糕...你的钱好像不够哦...")
	    cm.dispose();
	    return;
	} else {
	    if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++) {
		    complete = cm.haveItem(mats[i], matQty[i] * qty);
		    if (!complete) {
			break;
		    }
		}
	    } else {
		complete = cm.haveItem(mats, matQty * qty);
	    }	
        }
	if (!complete) {
	    cm.sendOk("很抱歉由于你的材料不足，所以我不想帮你做了。");
	} else {
	    if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++){
		    cm.gainItem(mats[i], -(matQty[i] * qty));
		}
	    } else
		cm.gainItem(mats, -matQty * qty);
					
	    if (cost > 0)
		cm.gainMeso(-(cost * qty));
	    if (item >= 2060000 && item <= 2060002) //bow arrows
		cm.gainItem(item, 1000 - (item - 2060000) * 100);
	    else if (item >= 2061000 && item <= 2061002) //xbow arrows
		cm.gainItem(item, 1000 - (item - 2061000) * 100);
	    else if (item == 4003000)//screws
		cm.gainItem(4003000, 15 * qty);
	    else
		cm.gainItem(item, qty);
	    cm.sendOk("制作完毕");
	}
	cm.dispose();
    }
}