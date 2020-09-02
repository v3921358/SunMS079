/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[1132174,1132175,1132176,1132177,1132178,1102481,1102482,1102483,1102484,1102485,1072743,1072744,1072745,1072746,1072747];function start(){2>im.getInventory(1).getNumFreeSlot()||3>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(3).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(2430441,1),im.gainItem(2501E3,1),im.gainItem(2430768+im.nextInt(5),1),im.gainPotentialItem(1003635,1,19),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),10>im.nextInt(100)&&im.gainItem(3700049,1),im.removeItem(2430895));im.dispose()};