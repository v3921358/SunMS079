/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getEventManager("CWKPQ");null!=b&&(a.warpS(610030300,0),b.getProperty("glpq3").equals("10")||(b.setProperty("glpq3",parseInt(b.getProperty("glpq3"))+1),a.mapMessage(6,"\u4e00\u4e2a\u5192\u9669\u5bb6\u5df2\u7ecf\u901a\u8fc7!"),b.getProperty("glpq3").equals("10")&&(a.mapMessage(6,"\u8be5Antellion\u6388\u4e88\u60a8\u8bbf\u95ee\u5230\u4e0b\u4e00\u4e2a\u95e8\uff01\u7ee7\u7eed!"),a.getMap().changeEnvironment("3pt",2))))};