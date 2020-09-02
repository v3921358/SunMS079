/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function start(){if(1>im.getInventory(1).getNumFreeSlot()||2>im.getInventory(2).getNumFreeSlot()||1>im.getInventory(4).getNumFreeSlot())im.sendOk("\u8bf7\u589e\u52a0\u5e93\u5b58\u7a7a\u95f4.");else{im.gainItem(4310066,5);im.gainItem(1672028,1);im.gainItem(2431405,1);var a=im.nextInt(3);im.gainItem(0==a?2046964:1==a?2046965:2047801,1);im.removeItem(2431808)}im.dispose()};