/* ==================
 脚本类型: NPC	    
 脚本版权：枫之谷团队
 联系扣扣：297870163    609654666
 =====================
 */
importPackage(java.lang);
importPackage(Packages.tools);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet); 
var status = -1;
var beauty = 0;
var hair_Colo_new;

function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendSimple("          #b一线海们#k,在这里可以兑换相应的#r副本勋章#k哟 . \r\n  #L1#兑换月秒副本勋章#v1142014#需要#v4001101#5个 and #v2140000##b5万#k#l\r\n  #L2#兑换废弃副本勋章#v1142026#需要#v4002000#20个 and #v2140000##b20万#k#l\r\n  #L3#兑换玩具副本勋章#v1142065#需要#v4002001#20个 and #v2140001##b100万#k#l\r\n  #L4#兑换女神副本勋章#v1142067#需要#v4002002#20个 and #v2140001##b200万#k#l\r\n  #L5#兑换海盗副本勋章#v1142079#需要#v4002003#20个 and #v2140002##b500万#k#l");
    } else if (status == 1) {
	if (selection == 0) {
	    var hair = cm.getPlayerStat("HAIR");
	    hair_Colo_new = [];
	    beauty = 1;

	    if (cm.getPlayerStat("GENDER") == 0) {
		hair_Colo_new = [35000,35090,35220,38360,35060,35100,35150,35200,35260,35270,35340,35350,35290,35300,35420,35450,35310,35330,35360,35430,35440,35460,35470,35510,35550,35560,35600,35660,36690,36710,36750,36760,36810,36820,36920,36340,36030,33810,33980,33670,33580,33320,36000,36420,36460,36470,36480,36510,36520,36530,36560,36580,36590,36640,36680,36700,33550,33820,33380,33930,32120,33150,33310,33600,33640,36310,33750,33250,33350,33440,35050,35170,35180,33290,33040,36300,33780,33700,33390,33260,33340,33240,33120,33000,35070,36290,33750,36310,36220,36180,36330,36120,36540,36770,33800,33740,33690,33630,33180,34440,33280,33300,33220,36360,33830,36010,36020,35020,32470,35130,35160,36550,36380,32440,];
	    } else {
		hair_Colo_new = [37980,37860,37820,37670,37640,37300,37260,37140,37030,34670,38030,38050,38060,38220,38240,38320,37310,35080,35110,34980,35190,35210,38380,38390,38470,38480,38500,38310,38270,38130,38400,38410,38420,38430,38450,38530,38540,38600,38610,38440,38460,38490,38520,38560,38570,38580,38590,38620,36640,38650,38680,38690,38700,38770,31610,31560,31230,37640,37690,36980,38070,37990,37960,37930,37920,34450,37950,37810,37190,37060,37000,34970,34890,34860,34810,34770,34750,34670,34600,33970,34450,33140,37440,37450,37490,37560,34160,34300,34260,34240,34210,38290,38160,38100,38020,38010,38120,37330,37340,34060,37710,34870,34800,34760,37700,37320,34330,34840,34850,34830,34110,34510,34250,34270,37400,37370,37380,37350,37530,37520];
	    }
	    for (var i = 0; i < hair_Colo_new.length; i++) {
		hair_Colo_new[i] = hair_Colo_new[i] + (hair % 10);
	    }
	    cm.sendYesNo("确定要使用 #b#t5150040##k 随机剪发了??");
        }else if(selection == 10){
			if(cm.getPlayer().getCSPoints(1) >= 980){
				cm.gainDJ(-980);
				cm.gainItem(5150040, 1);
				cm.sendOk("购买成功！");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 购买了1张皇家理发卷 祝他/她好运吧.");
				cm.dispose();
			}else{
				cm.sendOk("点卷不足无法购买!");
				cm.dispose();
			}
        }else if(selection == 20){
			if(cm.getPlayer().getCSPoints(1) >= 9500){
				cm.gainDJ(-9500);
				cm.gainItem(5150040, 10);
				cm.sendOk("购买成功！");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 购买了10张皇家理发卷 祝他/她好运吧.");
				cm.dispose();
			}else{
				cm.sendOk("点卷不足无法购买!");
				cm.dispose();
			}		
	    }else if(selection == 1){
	    if (cm.haveItem(4001101, 5)||cm.getMeso() <= 50000) { 
		cm.gainItem(4001101, -5);//扣除猪物品
        cm.gainMeso(-50000);		
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142014); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142014)).copy(); // 生成一个Equip类
		toDrop. setHp(50);//HP
		toDrop. setDex(2);//给敏捷 
        toDrop. setMp(50);//MP
		toDrop. setAvoid(5);//回避力
        toDrop. setSpeed(1);//速度		
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包				
				cm.sendOk("恭喜兑换成功");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功的兑换了月秒副本勋章.一起来恭喜他/她!");
				cm.dispose();
			}else{
				cm.sendOk("你的金币不足或者#v4001101#不够5个!");
				cm.dispose();
			}		
	    }else if(selection == 2){
	    if (cm.haveItem(4002000, 20)||cm.getMeso() <= 200000) { 
		cm.gainItem(4002000, -20);//扣除猪物品
        cm.gainMeso(-200000);		
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142026); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142026)).copy(); // 生成一个Equip类
		toDrop. setHp(100);//HP
        toDrop. setMp(100);//MP
		toDrop. setAvoid(5);//回避力
		toDrop. setAcc(5);//命中
		toDrop. setWdef(10);//防御
        toDrop. setSpeed(5);//速度
		toDrop. setHands(1);//手技		
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包				
				cm.sendOk("恭喜兑换成功");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功的兑换了废弃副本勋章.一起来恭喜他/她!");
				cm.dispose();
			}else{
				cm.sendOk("你的金币不足或者#v4002000#不够20个!");
				cm.dispose();
			}		
	    }else if(selection == 3){
	    if (cm.haveItem(4002001, 20)||cm.getMeso() <= 1000000) { 
		cm.gainItem(4002001, -20);//扣除猪物品
        cm.gainMeso(-1000000);		
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142065); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142065)).copy(); // 生成一个Equip类
		toDrop. setHp(150);//HP		
        toDrop. setMp(150);//MP
		toDrop. setAvoid(10);//回避力
		toDrop. setAcc(5);//命中
		toDrop. setWdef(20);//防御
        toDrop. setSpeed(10);//速度
		toDrop. setHands(5);//手技
		toDrop. setWatk(1);//攻击力
		toDrop. setMatk(1);//魔法力		
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包				
				cm.sendOk("恭喜兑换成功");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功的兑换了玩具副本勋章.一起来恭喜他/她!");
				cm.dispose();
			}else{
				cm.sendOk("你的金币不足或者#v4002001#不够20个!");
				cm.dispose();
			}
	    }else if(selection == 4){
	    if (cm.haveItem(4002002, 20)||cm.getMeso() <= 3000000) { 
		cm.gainItem(4002002, -20);//扣除猪物品
        cm.gainMeso(-3000000);		
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142067); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142067)).copy(); // 生成一个Equip类
		toDrop. setStr(1);//给力量
        toDrop. setDex(1);//给敏捷 
		toDrop. setInt(1);//给智力
		toDrop. setLuk(1);//给运气		
		toDrop. setHp(200);//HP		
        toDrop. setMp(200);//MP
		toDrop. setAvoid(20);//回避力
		toDrop. setAcc(10);//命中
		toDrop. setWdef(20);//防御
        toDrop. setSpeed(10);//速度		
		toDrop. setHands(10);//手技
		toDrop. setWatk(1);//攻击力
		toDrop. setMatk(1);//魔法力		
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包				
				cm.sendOk("恭喜兑换成功");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功的兑换了女神副本勋章.一起来恭喜他/她!");
				cm.dispose();
			}else{
				cm.sendOk("你的金币不足或者#v4002002#不够20个!");
				cm.dispose();
			}
	    }else if(selection == 5){
	    if (cm.haveItem(4002003, 20)||cm.getMeso() <= 5000000) { 
		cm.gainItem(4002003, -20);//扣除猪物品
        cm.gainMeso(-5000000);		
		var ii = MapleItemInformationProvider.getInstance();		                
		var type = ii.getInventoryType(1142079); //获得装备的类形
		var toDrop = ii.randomizeStats(ii.getEquipById(1142079)).copy(); // 生成一个Equip类
		//toDrop. setStr(1);//给力量		
		cm.getPlayer().getInventory(type).addItem(toDrop);//将这个装备放入包中
		cm.getC().getSession().write(CWvsContext.InventoryPacket.addInventorySlot(type, toDrop, false)); //刷新背包				
				cm.sendOk("恭喜兑换成功");
				cm.worldMessage(6,"玩家：["+cm.getName()+"] 成功的兑换了海盗副本勋章.一起来恭喜他/她!");
				cm.dispose();
			}else{
				cm.sendOk("你的金币不足或者#v4002003#不够20个!");
				cm.dispose();
			}		
		}			
    } else if (status == 30){
	if (beauty == 1){
	    if (cm.setRandomAvatar(5150040, hair_Colo_new) == 1) {
		cm.sendOk("对你的新发型满意吗?");
	    } else {
		cm.sendOk("貌似没有#b#t5150040##k。");
	    }
	} 
	cm.safeDispose();
    }
	
}
