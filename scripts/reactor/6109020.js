/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getEventManager("CWKPQ");null!=a&&(rm.mapMessage(6,"\u6d77\u76d7\u5370\u8bb0\u5df2\u88ab\u6fc0\u6d3b!"),a.setProperty("glpq4",parseInt(a.getProperty("glpq4"))+1),a.getProperty("glpq4").equals("5")&&(rm.mapMessage(6,"\u8be5Antellion\u6388\u4e88\u60a8\u8bbf\u95ee\u5230\u4e0b\u4e00\u4e2a\u95e8\u6237\u7f51\u7ad9\uff01\u7ee7\u7eed!"),rm.getMap().changeEnvironment("4pt",2)))};