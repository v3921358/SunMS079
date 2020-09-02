/* Author: Xterminator
	NPC Name: 		罗宾
	Map(s): 		Maple Road : Snail Hunting Ground I (40000)
	Description: 		Beginner Helper
*/
var mainmenu = "欢迎你来到这里！在开始冒险之前还有什么疑问尽管问吧。\r\n#b#L0#基本移动方法？#l\r\n#L1#打退怪物的方法？#l\r\n#L2#捡取物品的方法？#l\r\n#L3#死后怎么办？#l\r\n#L4#怎么学习职业技能？#l\r\n#L5#告诉我关于这个岛#l\r\n#L6#告诉我可以当战士的办法。#l\r\n#L7#告诉我可以当弓箭手的办法。#l\r\n#L8#告诉我可以当飞侠的办法。#l\r\n#L9#告诉我可以当魔法师的办法。#l\r\n#L10#告诉我可以当海盗的办法。#l\r\n#L12#在哪儿会确认我拣取的道具？#l\r\n#L13#怎么会装备道具？#l\r\n#L14#怎么会确认我在装备的道具？#l\r\n#L15#技能(K)是什么？#l\r\n#L16#怎么会去金银岛？#l\r\n#L17#金币是什么？#l";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status == 0 && mode == 0) {
            cm.dispose();
            return;
        } else if (status == 2 && mode == 0) {
            cm.sendNext("以下是如何拿下一个怪物。每个怪物都拥有自己的HP，你会取下来用任何武器或通过法术攻击。当然，强大的他们，就越难取下来.");
        }
        if (mode == 1) status++;
        else status--;
        if (status == 0) {
            cm.sendSimple(mainmenu);
        } else if (status == 1) {
            if (selection == 0) { // How do I move?
                status = -1;
                cm.sendNext("我告诉你基本移动方法。用#b左右键#k可以在平地或斜坡上行走，用#b Alt键#k可以跳跃。有些鞋子可以增加你的移动速度或跳跃能力。\r\\r\#fUI/DialogImage.img/Help/0#");
            } else if (selection == 1) { // How do I take down the monsters?
                cm.sendNext("我告诉你打退怪物的方法。每只怪物都有自己的体力，用物理攻击或魔法攻击都可以打退怪物。但是有些怪物很厉害，打退它们很不容易");
            } else if (selection == 2) { // How can I pick up an item?
                status = 5;
                cm.sendNext("我告诉你捡取物品的方法。打退怪物后战利品会在掉在地上，这时候站在物品前按#b Z键#k或#b数字0 键#k，就可以拣取物品。\r\n\r\n#fUI/DialogImage.img/Help/2#");
            } else if (selection == 3) { // What happens when I die?
                status = 8;
                cm.sendNext("想知道死后要怎么办？与怪物战斗中如果体力值为0，你就会变成幽灵。在死去的位置会出现墓碑，除了可以说话，其它操作都不能进行。");
            } else if (selection == 4) { // When can I choose a job?
                status = 11;
                cm.sendNext("想知道什么时候能转职？哈哈~！你真性急。每个职业都有固有的转职条件。一般8~10级你就可以选择职业。努力啊！");
            } else if (selection == 5) { // Tell me more about this island!
                status = 14;
                cm.sendNext("想知道这个岛的情况？这里是叫彩虹岛的空中浮动岛。从远古就在天空上飞行了，因此这里很少出现凶猛的怪物。所以是相对安全的岛，是新手练习的好地方。");
            } else if (selection == 6) { // What should I do to become a Warrior?
                status = -1;
                cm.sendNext("你想当#b战士#k吗？嗯。。。那么你必须要去金银岛。金银岛北部有战士之村，叫#r勇士部落#k。去那里找#b武术教练#k后收下他的培训，你就会当战士。但要当战士你的等级必需达到10级.");
            } else if (selection == 7) { // What should I do to become a Bowman?
                status = -1;
                cm.sendNext("你想当弓箭手吗？在金银岛你会当弓箭手。在金银岛南部有弓箭手的村落，射手村。在那里赫丽娜会告诉你当弓箭手的方法。但关键是要当弓箭手你的等级应该是10级。");
            } else if (selection == 8) {
                status = -1;
                cm.sendNext("你想当飞侠吗？那你要去金银岛西部的废弃都市。废都的达克鲁就会告诉你当飞侠的办法。关键的是为了当飞侠，你的等级应该是10级。");
            } else if (selection == 9) { // What should I do to become a Magician?
                status = 19;
                cm.sendNext("你想当魔法师是吗？那你要去金银岛东部的魔法密林。在那里你会见到很多魔法师。而且在那里你要见汉斯。他就会让你当魔法师。");
            } else if (selection == 10) { // What should I do to become a Thief?
                status = -1;
                cm.sendNext("你想成为#b海盗#k吗？想要转职的话，必须到金银岛去。在#r诺特勒斯#k的航海室里，可以见到#b凯琳#。");
            } else if (selection == 11) { // How do I raise the character stats? (S)
                status = 22;
                cm.sendNext("你想知道如何提高你的角色的能力统计？首先按#bS#k以检查出的能力的窗口。每次你的水平的时候，你会获得又名能力5点AP的。分配这些AP的到您选择的能力。就这么简单");
            } else if (selection == 12) { // How do I check the items that I just picked up?
                status = -1;
                cm.sendNext("你想知道在哪里会确认你拣取的道具呢？你按下Z键可以拣取地上的物品，那些物品自动放到道具背包。你按下I键可以确认背包的内容。");
                cm.ShowWZEffect("UI/tutorial.img/28");
            } else if (selection == 13) { // How do I put on an item?
                status = -1;
                cm.sendNext("你想装备道具吗？先打开背包(I)吧。那你可以确认你所有的道具然后双击一个道具吧。那么你就可以装备该道具。但你要注意的是大多装备道具有职业，等级，能力等限制。所以你先确认道具的装备条件后再使用道具吧。而且你打开装备窗(E)单击道具后直接把该道具移动到你想装备的部分。");
                cm.ShowWZEffect("UI/tutorial.img/29");
            } else if (selection == 14) { // How do I check out the items that I'm wearing?
                status = -1;
                cm.sendNext("你想确认现在装备的道具吗？按下E键你可以打开装备窗。在那里你就可以确认你的装备。在装备窗双击道具的话，道具就被回到背包(I)。");
            } else if (selection == 15) { // What are skills? (K)
                status = -1;
                cm.sendNext("转职后你可以学习更多的专业技能，你可以设定快捷键，让这些技能使用起来更容易。攻击技能也不用按Ctrl键，只用快捷键就可以发出。按下K键你可以打开技能窗。");
                cm.ShowWZEffect("UI/tutorial.img/23");
            } else if (selection == 16) { // How do I get to Victoria Island?
                status = -1;
                cm.sendNext("在南港乘船你会去金银岛。按下W键你可以确认你现在的位置。但要乘船需要一定的金币，你在这里打猎怪物赚钱吧。");
            } else if (selection == 17) { // What are mesos?
                status = -1;
                cm.sendNext("金币是冒险岛的货币。用金币你可以购买各种道具。打猎怪物或在商店卖道具或完成任务后你就可以获得金币。");
            }
        } else if (status == 2) { // How do I take down the monsters?
            cm.sendNextPrev("为了打退怪物，你应该装备武器。按#b I键#k打开背包，单击#b 装备#k然后双击你想装备的武器即可。武器装备好后按#b Ctrl键#k，就可以使用武器。只要你掌握了窍门，就可以更容易地打退怪物。\r\n\r\n#fUI/DialogImage.img/Help/1#");
        } else if (status == 3) { // How do I take down the monsters?
            cm.sendNextPrev("转职后你可以学习更多的专业技能，你可以设定快捷键，让这些技能使用起来更容易。攻击技能也不用按Ctrl键，只用快捷键就可以发出。");
        } else if (status == 4) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 5) { // How can I pick up an item?
            cm.sendNext("我告诉你捡取物品的方法。打退怪物后战利品会在掉在地上，这时候站在物品前按#b Z键#k或#b数字0 键#k，就可以拣取物品。\r\n\r\n#fUI/DialogImage.img/Help/2#");
        } else if (status == 6) { // How can I pick up an item?
            cm.sendNextPrev("但是如果你的背包满了，就不能再拣取物品。所以应该把不需要的物品卖到商店里去。背包的容量在转职后会增加。");
        } else if (status == 7) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 8) { // What happens when I die?
            cm.sendNext("想知道死后要怎么办？与怪物战斗中如果体力值为0，你就会变成幽灵。在死去的位置会出现墓碑，除了可以说话，其它操作都不能进行。");
        } else if (status == 9) { // What happens when I die?
            cm.sendNextPrev("新手被打死，是没有任何损失的。可是对有职业的人来说影响就大了，因为他们死后就丢失部分经验值。要保重啊");
        } else if (status == 10) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 11) { // When can I choose a job?
            cm.sendNext("想知道什么时候能转职？哈哈~！你真性急。每个职业都有固有的转职条件。一般8~10级你就可以选择职业。努力啊！");
        } else if (status == 12) { // When can I choose a job?
            cm.sendNextPrev("等级并不是决定地位的唯一的事情，虽然。还需要提高了的基础上，占用一个特定能力的水平。例如，要成为一个战士，你的STR已超过35，等等，你知道我在说什么？请确保您提高了有直接的影响到你的工作能力.");
        } else if (status == 13) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 14) { // Tell me more about this island!
            cm.sendNext("想知道这个岛的情况？这里是叫彩虹岛的空中浮动岛。从远古就在天空上飞行了，因此这里很少出现凶猛的怪物。所以是相对安全的岛，是新手练习的好地方。");
        } else if (status == 15) { // Tell me more about this island!
            cm.sendNextPrev("但是如果你想变得更强大，就要离开这里。在这里你不可能学到职业技能。这个岛下面有更大的岛，叫做金银岛。那里地域广阔，这里没法跟它比。");
        } else if (status == 16) { // Tell me more about this island!
            cm.sendNextPrev("怎么去金银岛？在这个岛的南部有一个叫#m60000#的港口，在那里有很大的飞船，在船前你会遇到那艘船的船长。问他就知道了。");
        } else if (status == 17) { // Tell me more about this island!
            cm.sendNextPrev("啊！我还要告诉你一件事。如果你不知道现在在哪儿，就按#bw键#k。会出现大地图，你可以确认你的位置，不必担心迷路。按#bEsc键或者#k再次按#bw键#k，大地图就会关闭。");
        } else if (status == 18) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 19) { // What should I do to become a Magician?
            cm.sendNext("你想当魔法师是吗？那你要去金银岛东部的魔法密林。在那里你会见到很多魔法师。而且在那里你要见汉斯。他就会让你当魔法师。");
        } else if (status == 20) { // What should I do to become a Magician?
            cm.sendNextPrev("啊，魔法师跟别的职业不同，是在8级转职。虽然魔法师会早点转职，但要做的努力也比别的职业大。你好好选择职业吧。");
        } else if (status == 21) {
            status = 0;
            cm.sendSimple(mainmenu);
        } else if (status == 22) { // How do I raise the character stats? (S)
            cm.sendNext("你想知道如何提高你的角色的能力统计？首先按#bS#k检查出的能力的窗口。每次你的水平的时候，你会获得又名能力5点AP的。分配这些AP的到您选择的能力。就这么简单.");
        } else if (status == 23) { // How do I raise the character stats? (S)
            cm.sendNextPrev("广场上各种能力的顶级鼠标光标的简要说明。例如，STR是战士，DEX的弓箭手，int对于魔术师和LUK窃贼。这本身是不是你需要知道的一切，所以你会通过分配点需要长期艰苦想就怎么来强调你的性格的长处.");
        } else if (status == 24) {
            status = 0;
            cm.sendSimple(mainmenu);
        }
    }
}