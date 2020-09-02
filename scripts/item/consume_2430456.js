/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[5064E3,552E4,5150053,5152057,5062E3,523E4,5072E3,5030004,5130003,551E4,1112908,1182006,1302173,1312072,1322107,1332148,1332149,1342040,1362022,1402111,1412071,1422073,1432099,1442136,1452129,1472141,1482102,1492101,1522020];function start(){1>im.getInventory(1).getNumFreeSlot()||2>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(5).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(25E5,1),im.gainItem(2022740+im.nextInt(6),1),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),im.removeItem(2430456));im.dispose()};