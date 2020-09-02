/* ==================
 脚本类型:  NPC	    
 脚本作者：一线海冒险岛团队    
 联系扣扣：297870163   609654666 
 =====================
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
		cm.sendOk("好的如果要挑战#b妖僧#k随时来找我.");
		cm.dispose();
	} 
	else 
	{
	if (mode == 1)
	status++;
	else
	status--;
		
	if (status == 0)
		var party = cm.getPlayer().getParty();		
		if (party == null || party.getLeader().getId() != cm.getPlayer().getId()) {
                cm.sendOk("你不是队长。请你们队长来说话吧！");
                cm.dispose();
                }else if( cm.getPlayer().getBossLog("shaoling") >= 2) {
	            cm.sendOk("您好,限定每天只能挑战2次！");
                cm.dispose();
		        }else if(!party.getMembers().size() < 3) {//判断人数
	            cm.sendOk("需要 3 人以上的组队才能进入！!");
                cm.dispose();
				}else if(!cm.getPlayerCount(702060000) <= 0) {//判断里面有没有人
	            cm.sendOk("里面有人暂时无法进入！!");
                cm.dispose();
		}else{
	//map = cm.getPlayer().getMap();
	cm.getMap(702060000).resetFully();//地图刷新
	cm.warpParty(702060000, 0);
	cm.getPlayer().setBossLog("shaoling");
	//cm.getPlayer().startMapTimeLimitTask(1200, map);//给地图时间
	cm.dispose();
		}
	}
}
