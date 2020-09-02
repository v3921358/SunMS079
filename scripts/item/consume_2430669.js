/* ==================
 脚本类型: 道具
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function start(){1>im.getInventory(2).getNumFreeSlot()?(im.sendOk("\u8bf7\u505a\u4e00\u4e9b\u5e93\u5b58\u7a7a\u95f4."),im.dispose()):im.sendSimple("\u4f60\u559c\u6b22\u4ec0\u4e48\u6eda\u52a8?\r\n#L0##i2046070:##t2046070##l\r\n#L1##i2046071:##t2046071##l\r\n#L2##i2046146:##t2046146##l\r\n#L3#End Chat#l")}function action(b,c,a){1==b&&3!=a&&(0==a?im.gainItem(2046070,1):1==a?im.gainItem(2046071,1):2==a&&im.gainItem(2046146,1),im.removeItem(2430669));im.dispose()};