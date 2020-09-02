/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getEventManager("CWKPQ");null!=b&&(b.getProperty("glpq1").equals("1")?(b.setProperty("glpq1","2"),a.warp(a.getMapId(),0),a.mapMessage("[\u63a2\u9669]\u4e00\u4e2a\u5192\u9669\u5bb6\u5df2\u7ecf\u901a\u8fc7\u4e86\u95e8!")):b.getProperty("glpq1").equals("2")?a.warp(610030200,0):a.playerMessage(5,"\u8bf7\u786e\u4fdd\u961f\u957f\u5148\u544a\u8bc9\u6770\u514b\u5148\u751f\u6709\u5173\u60c5\u51b5!"))};