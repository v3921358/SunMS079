/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
 function start() {
	 	if(cm.getPlayer().getOneTimeLog("lixinlibao") >= 1){
            cm.dispose();
			}else{
			cm.gainItem(1002552, 1);
			cm.gainItem(1052077, 1);
			cm.gainItem(1142263, 1);
			cm.gainMeso(50000);
			cm.getPlayer().setOneTimeLog("lixinlibao");
			cm.worldMessage(6,"欢迎新岛友：["+cm.getName()+"]来到最新冒险岛.获得了新手礼包一份!大家欢迎他/她吧!");
            cm.dispose();
			}
	}