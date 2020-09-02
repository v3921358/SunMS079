/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[1302174,1312073,1322108,1332150,1332151,1342041,1362023,1372101,1382125,1402112,1412072,1422074,1432100,1442137,1452130,1462119,1472142,1482103,1492102,1522021,1532038];function start(){2>im.getInventory(1).getNumFreeSlot()||2>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(3).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(2430441,1),im.gainItem(2501E3,1),im.gainPotentialItem(1003624,1,19),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),10>im.nextInt(100)&&im.gainItem(3700049,1),im.removeItem(2430891));im.dispose()};