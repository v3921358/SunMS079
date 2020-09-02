/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null!=a.getPlayer().getParty()&&0==a.getMap().getAllMonstersThreadsafe().size()&&a.isLeader()){for(var d=a.getMap().getCharactersThreadsafe(),c=0;c<d.size();c++){var b=d.get(c).getJob()%1E3/100+2022651|0;2022651==b?b=2022652:2022654==b?b=2022655:2022655==b&&(b=2022654);a.gainItem(b,1,!1,0,0,"",d.get(c).getClient())}a.addTrait("will",40);a.addTrait("charisma",10);a.gainExp_PQ(200,1.5);a.warpParty(240080050);a.playPortalSE()}else a.playerMessage(5,"\u6b64\u95e8\u4e0d\u53ef\u7528.")};