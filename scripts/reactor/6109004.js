/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getEventManager("CWKPQ");null!=a&&(610030200==rm.getMap().getId()?(rm.mapMessage(6,"\u6d77\u76d7\u5370\u8bb0\u5df2\u88ab\u6fc0\u6d3b!"),a.setProperty("glpq2",parseInt(a.getProperty("glpq2"))+1),a.getProperty("glpq2").equals("5")&&(rm.mapMessage(6,"\u8be5Antellion\u6388\u4e88\u60a8\u8bbf\u95ee\u5230\u4e0b\u4e00\u4e2a\u95e8\u6237\u7f51\u7ad9\uff01\u7ee7\u7eed!"),rm.getMap().changeEnvironment("2pt",2))):610030300==rm.getMap().getId()&&(rm.mapMessage(6,"\u6d77\u76d7\u5370\u8bb0\u5df2\u88ab\u6fc0\u6d3b! \u4f60\u542c\u5230\u9f7f\u8f6e\u8f6c\u52a8\uff01\u8be5\u5de8\u77f3\u9632\u5fa1\u7cfb\u7edf\u88ab\u6fc0\u6d3b\uff01\u8fd0\u884c!"),a.setProperty("glpq3",parseInt(a.getProperty("glpq3"))+1),rm.getMap().moveEnvironment("menhir5",1),a.getProperty("glpq3").equals("10")&&(rm.mapMessage(6,"\u8be5Antellion\u6388\u4e88\u60a8\u8bbf\u95ee\u5230\u4e0b\u4e00\u4e2a\u95e8\u6237\u7f51\u7ad9\uff01\u7ee7\u7eed!"),rm.getMap().changeEnvironment("3pt",2))))};