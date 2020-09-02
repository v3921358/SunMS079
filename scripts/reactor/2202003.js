/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){rm.dropItems();var a=rm.getPlayer().getEventInstance();if(null!=a){var b=parseInt(a.getProperty("stage2"))+1;10>=b&&(a.setProperty("stage2",b),rm.getMap().startSimpleMapEffect("\u60a8\u5df2\u6536\u96c6 "+b+" \u901a\u8fc7.",5120018))}};