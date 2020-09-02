/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getPlayer().getEventInstance(),b=a.getProperty("openedBoxes");a.setProperty("openedBoxes",b+1);rm.spawnMonster(9300109,3);rm.spawnMonster(9300110,5);rm.mapMessage(5,"\u4e00\u4e9b\u602a\u7269\u88ab\u53ec\u5524.")};