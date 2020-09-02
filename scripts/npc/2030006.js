/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var qChars = new Array ("Q1: 龙骑士是属于什么职业?#法师#弓箭手#战士#飞侠#3",
    "Q1: 魔导师是属于几转的法师职业?#4转#3转#2转#1转#1",
    "Q1: 推荐朋友来玩可以领取什么?#点卷#抵用卷#金币#装备#1",    
	"Q1: 在经典冒险岛里你觉得谁是最丑的那个？#自己#管理员#其他玩家#微笑#1",
	"Q1: 冒险岛最高等级为多少？#120级#150级#180级#200级#4",
"Q1:怪物所掉落战利品是正确的是哪一种?#大幽灵-幽灵头带#蝙蝠-蝙蝠翅膀#皮卡丘 - 精灵球#猪 - 丝带#2");
var qItems = new Array( "Q2: 在天空之城没有出现的怪物是哪一个？#黑龙#小石球#艾利杰#黑色飞狮#1",
    "Q2: 在彩虹岛看不到的怪物是哪一个?#蓝蜗牛#红蜗牛#花蘑菇#蝙蝠怪#4",
    "Q2: 你醒来的第一件事是做什么?#打开手机#洗脸#刷牙#睁开眼睛#4",
    "Q2: 你6岁的时候你妹妹3岁,那么你70岁的时候你妹妹几岁？#67岁#35岁#15岁#55岁#1",
    "Q2: 给自己定个小目标,比如先赚它1个亿,是哪个人装的逼？#丁磊#李嘉诚#马云#王健林#4",
    "Q2: 本服的名字叫做什么？#中国好声音#经典冒险岛#非诚勿扰#天天向上#2");
var qMobs = new Array("Q3:扎昆有几只手？#2只手#4只手#6只手#8只手#4",
    "Q3: 双倍经验卡三小时权在商城的售价是多少点卷？#200点卷#300点卷#100点卷#500点卷#3",
    "Q3: 几级可以去废弃带领新人完成高级玩家带人奖励#100级#80级#70级#50级#4",
    "Q3: 2转需要等级到达多少？#10级#20级#40级#30级#4",
    "Q3: 哪种动物最没有方向感？#麋鹿#老虎#狮子#大象#1",
    "Q3: 老师叫小明擦黑板,小明说:我擦,我才不擦呢,那么小明擦黑板了吗?#擦了#没擦#老师擦了#不知道#2");
var qQuests = new Array("Q4:下面哪种怪物比较黑？#红蜗牛#绿蜗牛#黑蜗牛#蓝蜗牛#3",
    "Q4: 金银岛没有的村落? #农村#勇士部落#林中之城#射手村#1",
    "Q4: 猴子最不喜欢什么线？#斑马线#直线#电线#平行线#4",
    "Q4: 哪个月没有28天?#1月#每个月都有28天#2月#12月#2",
    "Q4: 小白加小白等于什么？#怕怕#小白兔#小白小白#小小白白#2",
    "Q4: 青蛙为什么可以跳得比树高？#它是跳高冠军#它站在树上跳#因为腿长#因为它是青蛙王子#2");
var qTowns = new Array( "Q5:每日任务需要收集的物品是多少个？ #50个#40个#30个#20个#1",
    "Q5: 橡皮,老虎皮,鳄鱼皮,蛇皮哪种皮最差？#橡皮#老虎皮#鳄鱼皮#蛇皮#1",
    "Q5: 有一只狼来到北极,不小心掉到冰海中,被捞起来会变成什么？#冰棒#白狼#冻坏了的狼#槟榔#4",
    "Q5: 有两个人掉到陷阱里,死的人叫死人,活的人叫什么？#叫活人#叫命大#叫鸡爱母#叫救命#4",
    "Q5: 麒麟飞到北极会变成什么？#会冻死#麒麟不会飞#麒麟不会变身#变成冰麒麟#4",
    "Q5: 如果有一辆车,小明是司机,小红坐在副驾驶,小东坐在后面,那么这辆车是谁的?#小明的#如果的#小红的#小东的#2");
var correctAnswer = 0;

function start() {
		if (cm.isQuestActive(100113)) {
		cm.sendOk("请问找我有什么事情吗?");
        cm.dispose();
        } else if (!cm.isQuestActive(100112)) {
		cm.sendOk("请问找我有什么事情吗?");
        cm.dispose();
        } else if (cm.haveItem(4031058, 1)) {
		cm.sendOk("#h #,你已经有了 #t4031058#!");
		cm.dispose();
	}
    if (!(cm.haveItem(4031058, 1))) {
        cm.sendNext("我是 #b神圣的石头#k.看来你来到这个阶段非常的不容易啊!");
    }
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0) {
            cm.sendOk("下次再见.");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 1)
            cm.sendNextPrev("如果你给我#b黑暗水晶#k.我将会让你试着回答5个问题,若您5个问题都答对您将得到 #v4031058# #b智慧项链#k.");
        else if (status == 2) {
           if (cm.getInventory(4).isFull(0)){//判断第4个也就是装备栏的装备栏是否有一个空格		
           cm.sendOk("#b请保证其它栏位至少有1个空格,否则无法参加挑战.");		
           cm.dispose();
	       } else if (!cm.haveItem(4005004)) {
                cm.sendOk("#h #, 你没有 #b黑暗水晶#k");
                cm.dispose();
            } else {
                cm.gainItem(4005004, -1);
                cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qChars[Math.floor(Math.random() * qChars.length)]));
                status = 2;
            }
        } else if (status == 3) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题?");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 4)
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qItems[Math.floor(Math.random() * qItems.length)]));
        else if (status == 5) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题?");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 6) {
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qMobs[Math.floor(Math.random() * qMobs.length)]));
            status = 6;
        } else if (status == 7) {
            if (selection == correctAnswer)
                cm.sendOk("#h # 你答对了.\n准备答下一题??");
            else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 8)
            cm.sendSimple("测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qQuests[Math.floor(Math.random() * qQuests.length)]));
        else if (status == 9) {
            if (selection == correctAnswer) {
                cm.sendOk("#h # 你答对了.\n准备答下一题?");
                status = 9;
            } else {
                cm.sendOk("你答错了的答案!.\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        } else if (status == 10) {
            cm.sendSimple("最后一个问题.\r\n测验开始 #b接受挑战吧!#k.\r\n\r\n" + getQuestion(qTowns[Math.floor(Math.random() * qTowns.length)]));
            status = 10;
        } else if (status == 11) {
            if (selection == correctAnswer) {
                cm.gainItem(4031058, 1);
				cm.forceStartQuest(100113); //开始任务
				//cm.warp(211000001, 0);
                cm.sendOk("拿着这个 #v4031058# 去找你的转职教官吧!");
                cm.dispose();
            } else {
                cm.sendOk("太可惜了,差一题就可以通关了,继续加油!\r\n很抱歉你必须在给我一个 #b黑暗水晶#k 才可以再挑战!");
                cm.dispose();
            }
        }
    }
}
function getQuestion(qSet){
    var q = qSet.split("#");
    var qLine = q[0] + "\r\n\r\n#L0#" + q[1] + "#l\r\n#L1#" + q[2] + "#l\r\n#L2#" + q[3] + "#l\r\n#L3#" + q[4] + "#l";
    correctAnswer = parseInt(q[5],10);
    correctAnswer--;
    return qLine;
}