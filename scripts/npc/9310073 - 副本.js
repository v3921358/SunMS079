var status = -1;

function start()
{
	cm.sendSimple("你现在是#r高级玩家#k啦，带新人完成副本任务可以奖励你一个#b冒险岛纪念币#k.凑齐#b25#k个找我兑换你想要的奖励吧！\r\n#L0##b使用25个#v4001129# 兑换 #v2048010# #k#l\r\n#L1##b使用25个#v4001129# 兑换 #v2048011# #k#l\r\n#L2##b使用25个#v4001129# 兑换 #v2048012# #k#l\r\n#L3##b使用25个#v4001129# 兑换 #v2048013# #k#l");
	
}

function action(mode, type, selection) {
	if(selection == 0)
	{
		if(!cm.haveItem(4001129, 25))
		{
			cm.sendOk("很可惜哦, 你并没有#b25#k个#v4001129#.");
			cm.dispose();
			return;
		}
		
		if(!cm.canHold(2048010, 1))
		{
			cm.sendOk("您的#b背包#k已经没有空间了, 请及时清理您的#b背包#k哦！");
			cm.dispose();
			return;
		}

		cm.gainItem(4001129, -25);
		cm.gainItem(2048010, 1);
		cm.sendOk("兑换成功, #v2048010# #bx1#k.");
		cm.dispose();
	}
	else if(selection == 1)
	{
		if(!cm.haveItem(4001129, 25))
		{
			cm.sendOk("很可惜哦, 你并没有#b25#k个#v4001129#.");
			cm.dispose();
			return;
		}
		
		if(!cm.canHold(2048011, 1))
		{
			cm.sendOk("您的#b背包#k已经没有空间了, 请及时清理您的#b背包#k哦！");
			cm.dispose();
			return;
		}

		cm.gainItem(4001129, -25);
		cm.gainItem(2048011, 1);
		cm.sendOk("兑换成功, #v2048011# #bx1#k.");
		cm.dispose();
	}
	else if(selection == 2)
	{
		if(!cm.haveItem(4001129, 25))
		{
			cm.sendOk("很可惜哦, 你并没有#b25#k个#v4001129#.");
			cm.dispose();
			return;
		}
		
		if(!cm.canHold(2048012, 1))
		{
			cm.sendOk("您的#b背包#k已经没有空间了, 请及时清理您的#b背包#k哦！");
			cm.dispose();
			return;
		}

		cm.gainItem(4001129, -25);
		cm.gainItem(2048012, 1);
		cm.sendOk("兑换成功, #v2048012# #bx1#k.");
		cm.dispose();
	}
	else if(selection == 3)
	{
		if(!cm.haveItem(4001129, 25))
		{
			cm.sendOk("很可惜哦, 你并没有#b25#k个#v4001129#.");
			cm.dispose();
			return;
		}
		
		if(!cm.canHold(2048013, 1))
		{
			cm.sendOk("您的#b背包#k已经没有空间了, 请及时清理您的#b背包#k哦！");
			cm.dispose();
			return;
		}

		cm.gainItem(4001129, -25);
		cm.gainItem(2048013, 1);
		cm.sendOk("兑换成功, #v2048013# #bx1#k.");
		cm.dispose();
	}
}