/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){1==a.getQuestStatus(13103)&&1>a.itemQuantity(3994656)?(a.warp(910028310,1),a.gainItem(3994656,5)):(a.topMsg("You can't enter the Party Hall now."),1==a.getQuestStatus(13107)&&1>a.itemQuantity(3994660)?(a.warp(910028330,1),a.gainItem(3994660,10)):1==a.getQuestStatus(13110)&&1>a.itemQuantity(3994663)&&(a.warp(910028350,1),a.gainItem(3994663,1)))};