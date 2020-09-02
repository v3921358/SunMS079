/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[1212044,1222044,1302248,1312135,1322181,1332205,1342075,1362074,1372161,1382192,1402172,1412122,1422124,1432150,1442202,1452189,1462177,1472197,1482151,1492162,1522078,1532081];function start(){2>im.getInventory(1).getNumFreeSlot()||3>im.getInventory(2).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(2290724,1),im.gainItem(25E5,1),im.gainPotentialItem(1222006,1,19),im.gainItem(2430909,1),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),im.removeItem(2430896));im.dispose()};