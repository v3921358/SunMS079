/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getPlayer().getEventInstance();switch(a.getMapId()){case 910340100:null==b.getProperty("1stageclear")?a.playerMessage(5,"\u95e8\u88ab\u963b\u6b62."):a.warp(910340200,0);break;case 910340200:null==b.getProperty("2stageclear")?a.playerMessage(5,"\u95e8\u88ab\u963b\u6b62."):a.warp(910340300,0);break;case 910340300:null==b.getProperty("3stageclear")?a.playerMessage(5,"\u95e8\u88ab\u963b\u6b62."):a.warp(910340400,0);break;case 910340400:null==b.getProperty("4stageclear")?a.playerMessage(5,"\u95e8\u88ab\u963b\u6b62."):a.warp(910340500,0)}};