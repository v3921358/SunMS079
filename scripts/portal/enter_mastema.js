/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){1==a.getQuestStatus(23210)?a.warp(931050100,1):1==a.getQuestStatus(23213)?a.warp(931050110,1):2==a.getQuestStatus(23214)&&1!=a.getQuestStatus(23215)&&2!=a.getQuestStatus(23215)&&99<a.getPlayer().getLevel()&&(a.forceStartQuest(23215),a.warp(220050300))};