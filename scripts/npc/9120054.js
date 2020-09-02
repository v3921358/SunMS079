
function action(mode, type, selection) {
cm.dispose();
if (selection == 0) {
       if(cm.haveItem(5252006) == true) {
        cm.gainItem(5252006,-1);
        cm.summonMob(9400592, 200000000, 40000, 1);//猎魔人     
cm.serverNotice("『绯红骑士团公告』：【"+ cm.getChar().getName() +"】带领他的队友开始挑战绯红骑士团终极BOSS【猎魔人】！"); 
        }else{
        cm.sendOk("抱歉你没有绯红念力钥匙。我不能为您召唤"); 
	cm.dispose();}
} else if (selection == 1) {
       if(cm.haveItem(5252006) == true) {
        cm.gainItem(5252006,-1);
cm.serverNotice("『绯红骑士团公告』：【"+ cm.getChar().getName() +"】带领他的队友开始挑战绯红骑士团终极BOSS【血焰将军】！");  
        cm.summonMob(9400591, 200000000, 40000, 1);//血焰将军     
        }else{
        cm.sendOk("抱歉你没有绯红念力钥匙。我不能为您召唤"); 
	cm.dispose();}
} else if (selection == 2) {
       if(cm.haveItem(5252006) == true) {
        cm.gainItem(5252006,-1);
cm.serverNotice("『会员公告』：【"+ cm.getChar().getName() +"】带领他的队友开始挑战绯红骑士团终极BOSS【地狱船长】！"); 
        cm.summonMob(9400589, 200000000, 40000, 1);//地狱船长     
        }else{
        cm.sendOk("抱歉你没有绯红念力钥匙。我不能为您召唤"); 
	cm.dispose();}
} else if (selection == 3) { 
        if (!cm.haveItem(4001009,1)) {
        cm.sendOk("抱歉，你没有1张#v4001009#无法为你开启");
        } else if (!cm.haveItem(4001010,1)) {
        cm.sendOk("抱歉，你没有1张#v4001010#无法为你开启"); 
        } else if (!cm.haveItem(4001011,1)) {
        cm.sendOk("抱歉，你没有1张#v4001011#无法为你开启"); 
        } else if (!cm.haveItem(4001012,1)) {
        cm.sendOk("抱歉，你没有1张#v4001012#无法为你开启"); 
        } else if (!cm.haveItem(4001013,1)) {
        cm.sendOk("抱歉，你没有1张#v4001013#无法为你开启"); 
        }else{
	cm.gainItem(4001009,-1);
	cm.gainItem(4001010,-1);
	cm.gainItem(4001011,-1);
	cm.gainItem(4001012,-1);
	cm.gainItem(4001013,-1);
	cm.gainItem(4021010,1);
	cm.dispose();
}
} else if (selection == 4) {
        if (!cm.haveItem(4021010,1)) {
        cm.sendOk("抱歉，你没有#v4021010#无法为你开启");
cm.dispose();
    } else {
cm.warp(803001400, 0);
cm.dispose();
}
}else if (selection == 8) {
    cm.warp(910000000, 0);
    cm.dispose();
}else if (selection == 5) {
cm.sendOk("在这里必须击败所有的BOSS，而每一个BOSS都会爆出一种凭证#r（凭证暴率90%）#k。收集5个凭证后，您可以找我兑换通关证明。然后在点我，我将把你们传送到领奖地图。");      
cm.dispose();
} else if (selection == 6) {
       if(cm.haveItem(5252006) == true) {
        cm.gainItem(5252006,-1);
cm.serverNotice("『会员公告』：【"+ cm.getChar().getName() +"】带领他的队友开始挑战绯红骑士团终极BOSS【海之魔女】！"); 
        cm.summonMob(9400590, 200000000, 40000, 1);//地狱船长     
        }else{
        cm.sendOk("抱歉你没有绯红念力钥匙。我不能为您召唤"); 
	cm.dispose();}
} else if (selection == 7) {
       if(cm.haveItem(5252006) == true) {
        cm.gainItem(5252006,-1);
cm.serverNotice("『会员公告』：【"+ cm.getChar().getName() +"】带领他的队友开始挑战绯红骑士团终极BOSS【暗影杀手】！"); 
        cm.summonMob(9400593, 200000000, 40000, 1);//地狱船长     
        }else{
        cm.sendOk("抱歉你没有绯红念力钥匙。我不能为您召唤"); 
	cm.dispose();}
} else if (selection == 8) {
	if(cm.getMeso() <= 50000000) {
        cm.sendOk("抱歉你没有5000万。我不能为您召唤"); 
        }else{ 
        cm.gainMeso(-50000000);
        cm.summonMob(9400300, 100000000, 175000000, 1);//恶僧
	cm.dispose(); } 
} else if (selection == 9) {
	if(cm.getMeso() <= 50000000) {
        cm.sendOk("抱歉你没有5000万。我不能为您召唤"); 
        }else{ 
        cm.gainMeso(-50000000);
        cm.summonMob(9400549, 1, 200300000, 1);//火马
	cm.dispose(); } 
} 
}