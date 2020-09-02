/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[1212017,1222017,1302173,1312072,1322107,1332148,1332149,1342040,1362022,1372100,1382124,1402111,1412071,1422073,1432099,1442136,1452129,1462118,1472141,1482102,1492101,1522020,1532037];function start(){2>im.getInventory(1).getNumFreeSlot()||1>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(3).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainPotentialItem(1003409,1,19),im.gainItem(2501E3,1),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),10>im.nextInt(100)&&im.gainItem(3700049,1),im.removeItem(2430887));im.dispose()};