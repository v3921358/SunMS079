/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(c){try{var a="",b="";switch(c.getPortal().getId()){case 18:a="gate00";b="gt00PI";break;case 19:a="gate01";b="gt01PI";break;case 20:a="gate02";b="gt02PI";break;case 21:a="gate03";b="gt03PI";break;case 22:a="gate04";b="gt04PI";break;case 23:a="gate05";b="gt05PI";break;case 24:a="gate06",b="gt06PI"}4<=c.getMap().getReactorByName(a).getState()&&c.warp(670010600,b+"A")}catch(d){c.playerMessage(5,"\u9519\u8bef: "+d)}};