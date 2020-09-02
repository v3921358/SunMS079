/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(!a.haveItem(4001049)&&null!=a.getPlayer().getParty()&&a.isLeader())return a.warpParty(920010700),!1;a.getPlayer().dropMessage(5,"\u60a8\u5df2\u7ecf\u5b8c\u6210\u4e86\u6b64\u5173\u4efb\u52a1\uff0c\u7981\u6b62\u5165\u5185\u3002");return!0};