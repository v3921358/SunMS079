/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(a.isLeader()){var b=a.getPlayer().getEventInstance();if(null==b||null==b.getProperty("mode"))a.warp(502029E3,0);else{var c=50203E4+16*parseInt(b.getProperty("mode")),d=0==a.getMap().getAllMonstersThreadsafe().size();if(d)switch(a.getMapId()-c){case 5:case 8:d=b.getProperty("stage"+(a.getMapId()-c)).equals("0")}if(d){b=a.getMapId()+1;switch(a.getMapId()-c){case 13:case 14:case 15:b=c+(a.getMapId()-c)-4;break;case 8:case 9:case 10:b=c+(a.getMapId()-c)+5;break;case 11:b=502029E3}a.warpParty(b,0)}else a.playerMessage("\u8fd9\u95e8\u4e0a\u9501\u3002 \u9700\u8981\u628a\u6240\u6709\u7684\u602a\u7269\u51fb\u8d25.")}}else a.playerMessage("\u53ea\u6709\u961f\u957f\u624d\u53ef\u4ee5\u8fdb\u5165\u8fd9\u4e2a\u95e8.")};