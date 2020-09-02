importPackage(Packages.client); 
importPackage(Packages.server); 


var status = 0, sel = 0, sel1 = 0; 
var OPT_ = ["Hats", "Bow-tie", "Gloves", "Shoes", "Belt"]; 
var reqs, prizes; 


function start() { 
    prizes = initPrizes(); 
    reqs = initReqs(); 
    cm.sendSimple("你好，我是 #b动物项目#k vendor. \r\nRight now, we only offer 2 available item category. More will be available really soon though! \r\n\r\n/chooseone #e#b\r\n\r\n#L0#Hats#l\r\n#L1#Bow-tie#l\r\n#L69#See requirement tree (advanced)#l\r\n#L70#See requirement tree (simple)#l"); 
} 

function action(m,t,s) { 
    if (m < 1) cm.dispose(); 
    else { 
        if (status == 0) { 
            var e = ""; 
            sel = s; 
            if (s < 69) { 
                e += "/chooseone\r\n\r\n#b#e"; 
                for (i = 0; i < prizes[s].length; i++) e += "#L" + i + "#Tier "+i+" : #t" + prizes[s][i][0] + "# #v" + prizes[s][i][0] + "##l\r\n"; 
            } else { 
                if (s == 69) e += "This is the advanced requirement tree, it is faster to browse. If you do not understand it, please choose the simplified requirement tree instead, you may find it more suited to you.\r\n\r\n" + parseTree(true); 
                else e += "Choose a category\r\n\r\n /chooseone \r\n\r\n" + parseSimpleTree(0, -1, -1); 
            } 
            if (e.indexOf("#L") != -1) cm.sendSimple(e); 
            else { 
                cm.sendOk(e); 
                cm.dispose(); 
            } 
        } else if (status == 1) { 
            var e = ""; 
            sel1 = s; 
            if (sel < 70) { 
                var ee = ""; 
                for (i = 0; i < reqs[sel][sel1].length; i++) { 
                    var owned = cm.getPlayer().itemQuantity(reqs[sel][sel1][i][0]); 
                    var needed = reqs[sel][sel1][i][1]; 
                    var percentage = (owned / needed * 100).toFixed(2); 
                    if (percentage > 100) percentage = 100; 
                    ee += "#b#v" + reqs[sel][sel1][i][0] + "# x" + reqs[sel][sel1][i][1] + " #t" + reqs[sel][sel1][i][0] + "# #r(" + percentage + "%)\r\n"; 
                } 
                e += "Do you have the requirements for this item? May i remind you that these are what you need to obtain for this item:\r\n\r\n#e#b" + ee; 
            } else e += "Choose a tier\r\n\r\n /chooseone \r\n\r\n" + parseSimpleTree(1, s, -1);
            if (sel >= 70) cm.sendSimple(e); 
            else cm.sendYesNo(e); 
        } else if (status == 2) { 
            var e = ""; 
            var missing = new Array(); 
            if (sel < 70) { 
                for (i = 0; i < reqs[sel][sel1].length; i++) { 
                    if (!cm.haveItem(reqs[sel][sel1][i][0], reqs[sel][sel1][i][1])) { 
                        missing.push(new Array(i, cm.getPlayer().itemQuantity(reqs[sel][sel1][i][0]))); 
                    } 
                } 
                if (missing.length > 0) { 
                    e += "HEY! You are missing the following requirement " + (missing.length > 1 ? "s" : "") + "\r\n\r\n#e#b"; 
                    missing.sort(sortMissingRequirements); 
                    for (j = 0; j < missing.length; j++) { 
                        var itemid = reqs[sel][sel1][missing[j][0]][0]; 
                        var quantity = missing[j][1]; 
                        var needed = reqs[sel][sel1][missing[j][0]][1] 
                        var percentage = (quantity / needed * 100).toFixed(2); 
                        e += "#v" + itemid + "##t" + itemid + "# (" + quantity + " / " + needed + ", #r" + percentage + "% #B" + (quantity / needed * 100).toFixed(0) + "##b)\r\n"; 
                    } 
                } else { 
                    e += "Congratulation on your #r#etier " + (sel1 + 1) + " " +  OPT_[sel] + " #k#n!"; 
                    for (i = 0; i < reqs[sel][sel1].length; i++) { 
                        cm.gainItem(reqs[sel][sel1][i][0], -reqs[sel][sel1][i][1]); 
                    } 
                    var i = MapleItemInformationProvider.getInstance().getEquipById(prizes[sel][sel1][0]); 
                    i.setStr(prizes[sel][sel1][1]); 
                    i.setDex(prizes[sel][sel1][1]); 
                    i.setInt(prizes[sel][sel1][1]); 
                    i.setLuk(prizes[sel][sel1][1]); 
                    i.setWatk(prizes[sel][sel1][2]); 
                    MapleInventoryManipulator.addFromDrop(cm.getClient(), i, true); 
                } 
            } else e += "Here are the requirements for the following item: \r\n#b#t" + prizes[sel1][s][0] + "# #v" + prizes[sel1][s][0] + "# #r#e(tier " + (s + 1) + ")#k#n \r\n" + parseSimpleTree(2, sel1, s);     
            cm.sendOk(e); 
        } else cm.dispose(); 
    } status++; 
} 

function sortMissingRequirements(x, y) { 
    var p1 = x[1] / reqs[sel][sel1][x[0]][1] * 100; 
    var p2 = y[1] / reqs[sel][sel1][y[0]][1] * 100; 
    return p2 - p1; 
} 


///////////////////////////////// 
//*SEE REQUIREMENTS AND STUFF*/// 
//////////////////////////////// 

function parseTree(gd) { 
    var e = ""; 
    for (i = 0; i < reqs.length; i++) { //for each categories 
        e += "#e#bCategory " + i + ": " + OPT_[i] + "#k#n\r\n"; 
        for (j = 0; j < reqs[i].length; j++) { //for each tiers 
            e += "                    #e#rTier " + (j + 1) + ": " + (gd ? "#v" : "") + prizes[i][j][0] + (gd ? "#" : "") + "#k#n\r\n"; 
            if (gd) e += "                                #dRequirements:#k \r\n"; 
            for (k = 0; k < reqs[i][j].length; k++) { //for each requirements 
                e += "                                 " + (gd ? "                " : "") + (gd ? "#v" : "") + reqs[i][j][k][0] + (gd ? "#" : "") +  ", "+reqs[i][j][k][1]+"\r\n"; 
            } 
        } 
        e += "\r\n\r\n\r\n"; 
    } 
    return e; 
} 

function parseSimpleTree(step, data1, data2) { 
    //step 0 = categories, data1 = -1, data2 = -1 
    //step 1 = tiers, data1 = CHOSEN CTGR, data2 = -1 
    //step 2 = requirements, data1 = CHOSEN CTGR, data2 = CHOSEN TIER  

    var e = ""; 
    if (step == 0) { 
        for (i = 0; i < prizes.length; i++) e += "#b#e#L" + i + "# " + OPT_[i] + "#l\r\n"; 
    } else if (step == 1) { 
        var ctgr = reqs[data1]; 
        for (i = 0; i < ctgr.length; i++) e += "#b#e#L" + i + "#Tier " + (i + 1) + " #v" + prizes[data1][i][0] + "##l\r\n"; 
    } else if (step == 2) { 
        var reqq = reqs[data1][data2]; 
        for (i = 0; i < reqq.length; i++) e += "\r\n#b#e#v" + reqq[i][0] + "# x" + reqq[i][1] + " - #t" + reqq[i][0] + "#"; 
    } 

    return e; 
} 


/////////////////////////////////// 
///*CONSTANTS INITIALIZING BELOW*// 
/////////////////////////////////// 

function initPrizes() { 
    var prize_ = new Array(); 

    var hat = new Array(); 
    hat.push(new Array(1002020, 222, 22)); 
    hat.push(new Array(1002500, 888, 50)); 
    hat.push(new Array(1002512, 1111, 69)); 
    hat.push(new Array(1002603, 4000, 100)); 
    hat.push(new Array(1002553, 15000, 127)); 
    prize_.push(hat); 

    var bowtie = new Array(); 
    bowtie.push(new Array(1122006, 127, 8)); 
    bowtie.push(new Array(1122002, 255, 16)); 
    bowtie.push(new Array(1122005, 512, 32)); 
    bowtie.push(new Array(1122004, 1024, 64)); 
    bowtie.push(new Array(1122003, 32767, 128)); 
    prize_.push(bowtie); 

    return prize_; 
} 

function initReqs() { 
     
    var req_ = new Array(); 

    var hat = new Array(); 
    for (i = 0; i < 5; i++) hat.push(new Array()); 
    req_.push(hat); 

    var bowtie = new Array(); 
    for (i = 0; i < 5; i++) bowtie.push(new Array()); 
    req_.push(bowtie); 

    hat[0].push(new Array(4000353, 60)); 
    hat[1].push(new Array(prizes[0][0][0], 1)); 
    hat[1].push(new Array(4000353, 120)); 
    hat[1].push(new Array(4000268, 150)); 
    hat[2].push(new Array(prizes[0][1][0], 1)); 
    hat[2].push(new Array(4000353, 130)); 
    hat[2].push(new Array(4000282, 150)); 
    hat[2].push(new Array(4000440, 200)); 
    hat[3].push(new Array(prizes[0][2][0], 1)); 
    hat[3].push(new Array(4000353, 140)); 
    hat[3].push(new Array(4000086, 200)); 
    hat[3].push(new Array(4000087, 200)); 
    hat[3].push(new Array(4000069, 100)); 
    hat[4].push(new Array(prizes[0][3][0], 1)); 
    hat[4].push(new Array(4000353, 150)); 
    hat[4].push(new Array(4000082, 50)); 
    hat[4].push(new Array(4000121, 120)); 
    hat[4].push(new Array(4000134, 200)); 
    hat[4].push(new Array(4000151, 10)); 
    hat[4].push(new Array(4000152, 10)); 

    bowtie[0].push(new Array(4000353, 100)); 
    bowtie[0].push(new Array(4001129, 25)); 
    bowtie[1].push(new Array(prizes[1][0][0], 1)); 
    bowtie[1].push(new Array(4000353, 127)); 
    bowtie[1].push(new Array(4000099, 300)); 
    bowtie[2].push(new Array(prizes[1][1][0], 1)); 
    bowtie[2].push(new Array(4000353, 255)); 
    bowtie[2].push(new Array(4000103, 250)); 
    bowtie[2].push(new Array(4000106, 250)); 
    bowtie[3].push(new Array(prizes[1][2][0], 1)); 
    bowtie[3].push(new Array(4000353, 512)); 
    bowtie[3].push(new Array(4031871, 250)); 
    bowtie[3].push(new Array(4031902, 25)); 
    bowtie[3].push(new Array(4031905, 10)); 
    bowtie[4].push(new Array(prizes[1][3][0], 1)); 
    bowtie[4].push(new Array(4000353, 512)); 
    bowtie[4].push(new Array(4000313, 5)); 
    bowtie[4].push(new Array(4000329, 200)); 
    bowtie[4].push(new Array(4000269, 400)); 
    bowtie[4].push(new Array(4000270, 400)); 

    return req_; 
}