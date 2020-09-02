/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var b=rm.getPlayer().getEventInstance();rm.getPlayer().getEventInstance().getPlayers();var c=b.getMapFactory().getMap(240050100),a=rm.getPlayer().getMapId();rm.mapMessage(6,"\u94a5\u5319\u4f20\u9001\u5230\u67d0\u5904...");switch(a){case 240050101:a=4001087;break;case 240050102:a=4001088;break;case 240050103:a=4001089;break;case 240050104:a=4001090;break;default:a=-1}var a=new client.Item(a,0,1),d=c.getReactorByName("keyDrop1"),b=b.getPlayers().get(0);c.spawnItemDrop(d,b,a,d.getPosition(),!0,!0);c.dropMessage(5,"\u5149\u7684\u660e\u4eae\u7684\u95ea\u5149\uff0c\u7136\u540e\u662f\u5173\u952e\u7684\u5730\u65b9\u7a81\u7136\u51fa\u73b0\u5728\u5730\u56fe\u4e0a.")};