/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */var extra=[2430457,1122017,2049413];function start(){if(3>im.getInventory(1).getNumFreeSlot()||1>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(4).getNumFreeSlot())im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4.");else{im.gainPotentialItem(1003550,1,18);im.gainPotentialItem(1003414,1,18);im.gainItem(4310034,20);var a=extra[im.nextInt(extra.length)];1122017==a?im.gainItemPeriod(a,1,7):im.gainItem(a,1);im.removeItem(2430880)}im.dispose()};