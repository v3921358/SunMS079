/*
	亲亲嘴冒险专用脚本

	少林妖僧 -- 出口NPC
	
	by-- 芯碎王子
		
	QQ:7851103

*/
var status = 0;

function start() 
	{
	status = -1;
	action(1, 0, 0);


	}

function action(mode, type, selection)
{
	if (mode == -1)
	{
		cm.dispose();
	}
	else if (mode == 0)
	{
		cm.sendOk("好的如果要出去随时来找我.");
		cm.dispose();
	}else 
	{
		if (mode == 1)
			status++;
		else
			status--;		
	if (status == 0)
	{		
		cm.sendYesNo("您是否要出去呢?" );	
	}
	else if (status == 1) 
        {
		var party = cm.getPlayer().getParty();	
		if (party == null) {
		cm.warp(702070400,0);
		}else{		
			cm.warpParty(702070400);
		}
		cm.dispose();	
	}
}
}
	