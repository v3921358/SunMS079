/**
 * @触发条件：开拍卖功能
 * @每日签到：领取物品 npc
 * @npcName：冒险岛运营员
 * @npcID：   9900004
 **/

var status = 0;
var 黑水晶 = 4021008;
var 蓝色箭头 = "#fUI/UIWindow/Quest/icon2/7#";
var 红色箭头 = "#fUI/UIWindow/Quest/icon6/7#";
var 圆形 = "#fUI/UIWindow/Quest/icon3/6#";
var 美化new = "#fUI/UIWindow/Quest/icon5/1#";
var 感叹号 = "#fUI/UIWindow/Quest/icon0#";
var 正方箭头 = "#fUI/Basic/BtHide3/mouseOver/0#";
var mh1   ="#fUI/GuildMark/Mark/Animal/00002011/1#";//兔兔
var mh2   ="#fUI/GuildMark/Mark/Animal/00002011/5#";//兔兔
var mh3   ="#fUI/GuildMark/Mark/Animal/00002011/4#";//兔兔
var mh4   ="#fUI/GuildMark/Mark/Animal/00002011/9#";//兔兔
var mh5   ="#fUI/GuildMark/Mark/Animal/00002011/8#";//兔兔
var mh6   ="#fUI/GuildMark/Mark/Animal/00002011/10#";//兔兔
var mh7   ="#fUI/GuildMark/Mark/Animal/00002011/12#";//兔兔
var mh8   ="#fUI/GuildMark/Mark/Animal/00002011/15#";//兔兔
var mh9   ="#fUI/GuildMark/Mark/Animal/00002011/16#";//兔兔
var mh10   ="#fUI/GuildMark/Mark/Animal/00002011/6#";//兔兔
var 彩心 = "#fEffect/CharacterEff/1112904/0/1#";

var 忠告 = "#k温馨提示：任何非法程序和外挂封号处理.封杀侥幸心理.";
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == -1) { 
cm.dispose(); 
} else { 
if (mode == 0 && status == 0) { 
cm.dispose(); 
return; 
} 
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {



            var txt1 = "#d#L1#" + 彩心 + "#b-玩家人气排行榜#l";

            var txt2 = "#d#L2#" + 彩心 + "#b-玩家等级荣誉榜\r\n\r\n";

            var txt3 = "#d#L3#" + 彩心 + "#d-强势家族荣誉榜";

            var txt4 = "#d#L4#" + 彩心 + "#d-玩家财富榜#l\r\n";
			


            cm.sendSimple("#ro#b○#do#g○#bo#d○#ro#g○#do#r○#bo#d○#go#r○#k<#r#e排行榜#n#k>#r○#go#d○#go#r○#do#b○#go#r○#bo#d○#go#r○#b#k\r\n\r\n" + txt1 + "" + txt2 + "" + txt3 +  "" + txt4 + "");

        } else if (status == 1) {
            if (selection == 1) {  //每日答题
cm.showfame();
  cm.dispose();


            } else if (selection == 2) { //运势占卦
 cm.showlvl();
  cm.dispose();


            } else if (selection == 3) {//家族荣誉榜
cm.displayGuildRanks();

cm.dispose();

            } else if (selection == 4) { // 数字猜猜
cm.showmeso();

cm.dispose();

            } else if (selection == 5) { //挖矿
if (cm.getPlayer().getCSPoints(2) >= 10000) {
cm.gainD(-10000);
cm.gainItem(5360014, 1, 6);
cm.喇叭(2, "[" + cm.getPlayer().getName() + "]使用抵用卷成功购买双倍爆率7天使用权！");
cm.dispose();
}else{
cm.sendOk("道具不足无法换购！");
cm.dispose();
}
            } else if (selection == 6) { //海盗
if (cm.getPlayer().getCSPoints(2) >= 10000) {
cm.gainD(-10000);
cm.gainItem(5211047, 1, 240);
cm.喇叭(2, "[" + cm.getPlayer().getName() + "]使用抵用卷成功购买精灵吊坠10天使用权！");
cm.dispose();
}else{
cm.sendOk("道具不足无法换购！");
cm.dispose();
} 
            } else if (selection == 7) { //妙月
                cm.openNpc(1012112, 0);
            } else if (selection == 8) { //英语村
                cm.warp(702090400);
            } else if (selection == 9) { //嘉年华
                cm.warp(980000000);
                cm.dispose();
            } else if (selection == 10) { //家族
                cm.warp(101030104);
                cm.dispose();
            } else if (selection == 100) {//废弃介绍
		cm.sendOk("#e#b副本名称:#r废气组队任务#k\r\n#e#b任务要求:#r15-255级 1-6人\r\n#e#r奖励:#v4002003#和随机奖励#k\r\n#e攻略#k#n:\r\n注意组队的时候法师越多越好，最好是四法，因为法师不仅远距离攻击，杀鳄鱼时有绝对优势，而且最后一关打那个超级大绿水灵的时候，27级战士,飞狭打都miss，这个时候主要靠法师大显身手，如果你的队伍中法师少于2个，我只能深表同情，慢慢折腾吧。当然，级别高点能力足够的法师最好能发扬风格，带一带其他职业级别较低的弟弟妹妹，出来混都不容易，一定要发扬团队精神\r\n战士转职=10 转职力量=35（这个就是传说中的头奖，如果有两人都中奖，最好赶紧出去重新来过，你要愿意慢慢打我也不反对） \r\n弓手转职=10 转职敏捷=25\r\n飞贼转职=10 转职运气=25\r\n法师转职=8 转职智力=20\r\n从1级升到2级需要经验=15\r\n接下来三关都是排列组合的问题，要找出正确的组合其实一点也不难\r\n第二关：4条绳子3个人爬，有四种组合，分别空出不同的绳子就行了。爬绳子一定要爬得够高才行哦\r\n第三关：5个台阶3个人站，注意台阶的编号是这样看的，台阶上小猫有几只就是几号\r\n检验次序如下：123 124 125 134 135 145 234 235 245 345\r\n站1的人一开始不要乱动，让另外两个人跳就行了。\r\n第四关：6个桶3个人站，检验次序如下：123 124 125 126\r\n最后一关：又是打怪"); 
cm.dispose();
            } else if (selection == 1000) { //废弃奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 101) { //玩具介绍
                cm.sendOk("#e#b副本名称:#r玩具组队任务#k\r\n#e#b任务要求:#r35-255级 4-6人\r\n#e#r奖励:随机卷轴 +大量经验\r\n#e攻略#k#n:\r\n第一阶段.\r\n开始没什么说的，直接打就行了，上去后注意有个地方是2边都有怪，这时候左边的人打右边的怪，右边的打左边的怪，互相打的话可以节省时间。中间有个地方需要用法师的瞬移技能才能拿到通行证，如果有20级轻功技能也可以跳过去拿到的。打白色和黑色发条鼠会掉通行证，共需要25枚，而也只有25只怪。由队长收集后与NPC对话便可进入下一段阶。\r\n第二阶段\r\n打盒子收集通行证，从上面往下数的第2个盒子最后打，因为会转入另外的地图，大家都知道我就不多说了，但是要注意的是下去的时候1人负责1个，不要几个人打一个盒子，浪费时间！还有就是站在第2个盒子那守着的人，可以先打3下盒子，这样在下面监视的人说可以打的时候打一次就可以进入了。\r\n第三阶段\r\n打章鱼拿卡卡，没什么特点，唯一要注意的就是不要把在墙角的怪打死，那样很有可能会拿不到卡的！\r\n第四阶段\r\n打里面的怪获得通行证，一共需要6张。这张地图是完全黑暗的，只能看到怪的眼睛（这种怪是全黑色球状，只有眼睛是白的）。在地图4有2只，其他地图只有一只。 需要注意的是在进去打怪之前可以先在门口丢点垃圾什么的东西做记号，这样就可以避免别人重复的进去浪费时间，可以丢10元钱，又少又快的。丢了东西在进人！如果一个洞进去了2个人，那千万不要2个人都打，一个人打就行了，因为我测试过很多次，要是2个或者以上的人一起打那个眼睛怪兽的话它消失的频率会变的非常非常的高，这样就浪费时间了，所以一个人打最好！还有一点，在塔的中间，也就是将塔一分为二的那个平台上的第一个洞（=。=|| 应该是进去了从下往上数的第4个洞），这个洞里面的那个怪眼神很忧郁，打完了我也很忧郁，因为它的防御极其的高！最好是队伍中攻击最高的人打这个怪，要不打死它的时间可以郁闷死其他等候的队员，推荐标标打。攻击频率为1秒1下，基本怪不会消失，出现就一直被打，直到死亡。更新点东西，有个好心的人说了，塔分两层，上面3个洞口，里面的怪物是魔法防御低，给法师打，下面的2个洞里的物理防御低（其中有个洞里面有两个怪物），标标，战士等职业的人进去……\r\n第五阶段\r\n这个阶段一样是打盒子收集卡卡，在开始前队伍中满轻功的标标先给所有队员都加个轻功！打的时候也是要先在门口丢垃圾东西做记号，丢了东西在进人。最下面的要飞飞隐身过，最上面的要法师瞬移过，这些我想大家都是早就知道的了，还有就是每个洞的通行证都是4张！打的时候注意打齐！\r\n第六阶段\r\n跳盒子，很多人记不住，连我这样的超级垃圾记性的人都记的住呢。教不会记的人一个窍门，（133221）（333）（123）（111）这样分开记就方便很多的要是有不会的人问数是多少也可以这样分开打 方便核对\r\n第七阶段\r\n让标飞或弓手爬上绳子打右边的怪就可以了，1个人打其余的都下去等那个笨蛋BOSS出现好KO它。需要注意的是上面的人要依本组整体实力来决定打怪的时间，整体不强就一个一个打，整体都是高手就用高频率的，一次把3个怪打死，下面3个就一起召唤出来打。掌握好时间！\r\n第八阶段\r\n直接通过\r\n第九阶段\r\n这关没什么说的，目的就是BOSS死你不死废话。牧师注意队员的血，队员也别离牧师太远，每位队员一定带好圣水和眼药，有钱的话用万能药水也可以。BOSS的肚子有一圈黑色的东西的时候就是它用技能的时候，这时候跳一下可能不会中封锁技能或者诅咒。#r还有一个所有阶段都要注意的：那就是在气球附近丢通行证，这样方便组长收集，不要到处乱丢。网速不好的人不要拣卡，要不掉了线会害大家都没办法继续下去的。#k"); 
cm.dispose();
            } else if (selection == 1001) { //玩具奖励
                cm.openNpc(9040004, 0);    

            } else if (selection == 102) { //天空介绍
                cm.sendOk(" #e#b副本名称:#r女神任务#k\r\n#e#b任务要求:#r70-255级 1-1人\r\n#b#e主要掉落#k:[#v4032226#][#v4001254#]"); 
                cm.dispose();
            } else if (selection == 1002) { //天空奖励
                cm.openNpc(9040004, 0); 

            } else if (selection == 103) { //毒雾介绍
                cm.sendOk("#e#b副本名称:#r毒雾森林#k\r\n#e#b任务要求:#r70-255级 3-6人\r\n#e攻略#k#n:\r\n根据NPC提示收集材料,不懂可查询官方流程."); 
            } else if (selection == 1003) { //毒物奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 104) { //男女介绍
cm.sendOk("#e#b副本名称:#r狗男女副本#k\r\n#e#b任务要求:#r70-255级 3-6人\r\n#e攻略#k#n:\r\n根据NPC提示收集材料,不懂可查询官方流程"); 
cm.dispose();
            } else if (selection == 105) { //海盗介绍
                cm.sendOk("#e#b副本名称:#r海盗船副本#k\r\n#e#b任务要求:#r100-255级 3-6人\r\n#e攻略#k#n:\r\n根据NPC提示收集材料,不懂可查询官方流程"); 
            } else if (selection == 1005) { //海盗奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 1004) { //男女奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 106) { //妙月介绍
                cm.sendOk("#e#b副本名称:#r迎月花保护月妙组队任务#k\r\n#e#b任10级以上 人数要求:3-6人均可\r\n#e攻略#k#n:\r\n10分钟 第一阶段: 点NPC[达尔利]进入[迎月花山丘]地图,打 [迎月花稻谷],得 [迎月花种子] 收集6颗不同颜色的 [迎月花种子] 将不同颜色的[迎月花种子]放在方形梯子上,月牙变满月,出现[月妙] 附:颜色与固定的位置 -蓝色绿色- 黄色紫色 -棕色-粉色 接着保护[月妙],得到10个[月妙的年糕]后,点NPC获取奖励,不懂可查询官方流程"); 
            } else if (selection == 1006) { //妙月奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 107) { //英语村介绍
                cm.sendOk("#e#b副本名称:#r新的组队任务#k\r\n#e#b任10级以上 人数要求:3-6人均可\r\n#e攻略#k#n:\r\n有初,中,高三个地图可供选择.在每个城镇都有 蘑菇博士 .和它对话进入地图.进去之后会出现一个问题.需要回答问题:,不懂可查询官方流程"); 

            } else if (selection == 1007) { //英语村奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 108) { //嘉年华介绍
                cm.sendOk("#e#b副本名称:#r嘉年华组队任务#k\r\n#e#b任30级以上 人数最低要求:2-2人均可\r\n#e攻略#k#n:和别的队比赛看谁打的怪最多GP最高获得胜利\r\n,不懂可查询官方流程"); 
            } else if (selection == 1008) { //嘉年华奖励
                cm.openNpc(9040004, 0); 
            } else if (selection == 109) { //家族介绍
                cm.sendOk("#e#b副本名称:#r家族组队任务#k\r\n#e#b人数要求:家族人员均可参与\r\n#e攻略#k#n:为自己的家族获取荣誉,家族GP\r\n,不懂可查询官方流程"); 
            } else if (selection == 1009) { //家族奖励
                cm.openNpc(9040004, 0); 

            } else if (selection == 11) { //积分换点卷
                cm.openNpc(9900004, 1);//
            } else if (selection == 12) { //活跃度系统
                cm.openNpc(9100106, 0); //日本高级快乐百宝箱
            } else if (selection == 13) { //待添加
                cm.openNpc(9000018, 0); //待添加
            }
        }
    }
}
