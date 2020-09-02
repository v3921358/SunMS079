/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[5064E3,552E4,5150053,5152057,5062E3,523E4,5072E3,5030004,5130003,551E4,1112908,1182006,1122149,1112662,1132104,1032110,1012283,1112663,1012284,1012285,1112597,1112593];function start(){2>im.getInventory(1).getNumFreeSlot()||6>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(4).getNumFreeSlot()||1>im.getInventory(5).getNumFreeSlot()?im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4."):(im.gainItem(2501E3,1),im.gainItem(2430473,1),im.gainItem(2430441,1),im.gainPotentialItem(1003359,1,18),im.gainItem(4310027,20),im.gainItem(25E5,1),im.gainItem(2022740+im.nextInt(6),1),10>im.nextInt(100)&&im.gainItem(extra[im.nextInt(extra.length)],1),im.removeItem(2430471));im.dispose()};