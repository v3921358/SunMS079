/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function start(){if(1>im.getInventory(1).getNumFreeSlot()||5>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(3).getNumFreeSlot()||1>im.getInventory(4).getNumFreeSlot())im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4.");else{im.gainItem(4310066,10);im.gainPotentialItem(1072777,1,18);im.gainItem(3080004,1);im.gainItem(2028174,1);im.gainItem(2430442,1);im.gainItem(2431405,1);im.gainItem(2290285,1);var a=im.nextInt(3);im.gainItem(0==a?2046964:1==a?2046965:2047801,1);im.removeItem(2431811)}im.dispose()};