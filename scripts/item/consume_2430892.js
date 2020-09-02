/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function start(){1>im.getInventory(1).getNumFreeSlot()||3>im.getInventory(2).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(2290723,1),im.gainItem(25E5,1),im.gainPotentialItem(1402015,1,19),im.gainItem(2430909,1),10>im.nextInt(100)&&im.gainItem(1052526,1),im.removeItem(2430892));im.dispose()};