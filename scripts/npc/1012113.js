/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
function action(mode, type, selection) {
	cm.getPlayer().removeAll(4001101);//年糕
	cm.getPlayer().removeAll(4001095);//绿色种子
	cm.getPlayer().removeAll(4001096);//紫色种子
	cm.getPlayer().removeAll(4001097);//粉红种子
	cm.getPlayer().removeAll(4001098);//褐色种子
	cm.getPlayer().removeAll(4001099);//黄色种子
	cm.getPlayer().removeAll(4001100);//蓝色种子
	cm.warp(100000200);	
	cm.dispose();
}