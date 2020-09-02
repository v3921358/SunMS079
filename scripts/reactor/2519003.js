/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getEventManager("Pirate");null!=a&&(rm.mapMessage(6,"\u6210\u529f\u4e0a\u9501\u4e00\u9053\u95e8!"),a.setProperty("stage4",parseInt(a.getProperty("stage4"))+1),a.getProperty("stage4").equals("4")&&rm.mapMessage(6,"Proceed!"))};