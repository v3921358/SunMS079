/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b,c=2;switch(a.getMapId()){case 130030001:b=20010;c=1;break;case 130030002:b=20011;break;case 130030003:b=20012;break;case 130030004:b=20013;break;default:return}a.getQuestStatus(b)==c?(a.playPortalSE(),a.warp(a.getMapId()+1,"sp")):a.playerMessage(5,"\u4f60\u53ef\u80fd\u4e0d\u4f1a\u79bb\u5f00.")};