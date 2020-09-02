/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(b){var a=b.getEventManager("HorntailBattle");if(null!=a){var c=a.getProperty("preheadCheck");null!=c&&c.equals("0")&&(b.mapMessage(6,"\u5de8\u5927\u7684\u751f\u7269\u6b63\u4ece\u6df1\u6d1e\u7a74\u8fdb\u5165."),a.setProperty("preheadCheck","1"))}};