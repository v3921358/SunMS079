/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){var b=a.getPlayer().getEventInstance(),c=b.getMapInstance(922010200);if(null!=b.getProperty("stage1status"))return a.getPlayer().changeMap(c,c.getPortal("st00")),a.removeAll(4001022),!0;a.getPlayer().dropMessage(5,"\u73b0\u5728\u8fd8\u4e0d\u80fd\u8fdb\u5165\u4e0b\u4e00\u9636\u6bb5\u3002");return!1};