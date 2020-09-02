/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){2==a.getQuestStatus(32204)&&0==a.getQuestStatus(32207)?a.warp(4000013,0):(a.openNpc(10310,"ExplorerTut01"),2==a.getQuestStatus(32207)&&0==a.getQuestStatus(32210)?a.warp(4000014,0):(a.openNpc(10310,"ExplorerTut02"),1==a.getQuestStatus(32210)?a.warp(4000020,0):a.openNpc(10310,"ExplorerTut03")))};