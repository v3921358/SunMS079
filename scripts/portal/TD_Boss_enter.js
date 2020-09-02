/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(null!=a.getPlayer().getParty()&&a.isLeader()){var c=a.getPlayer().getParty().getMembers();a.getPlayer().getMapId();for(var b=!0,e=0,c=c.iterator();c.hasNext();){var d=c.next(),d=a.getPlayer().getMap().getCharacterById(d.getId());if(null==d){b=!1;break}e+=d.isGM()?4:1}if(b&&(a.getPlayer().isGM()||2<=e)){for(b=0;7>b;b++)if(null!=a.getMap(a.getMapId()+1+b)&&0==a.getMap(a.getMapId()+1+b).getCharactersSize()){a.warpParty(a.getMapId()+1+b);a.dispose();return}a.playerMessage("\u53e6\u4e00\u65b9\u7684\u4efb\u52a1\u5df2\u7ecf\u8fdb\u5165\u8be5\u901a\u9053.")}else a.playerMessage("\u961f\u4f0d\u9700\u89812+\u6210\u5458\u5fc5\u987b\u662f\u8fd9\u91cc.")}else a.playerMessage("\u961f\u4f0d\u7684\u961f\u957f\u5fc5\u987b\u5728\u8fd9\u91cc.")};