/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(6132)){var b=a.getEventManager("s4resurrection");if(null==b)a.playerMessage("\u4f60\u4e0d\u5f97\u4e0e\u4e0d\u660e\u539f\u56e0\u8fdb\u5165\u3002\u518d\u8bd5\u4e00\u6b21.");else{var c=b.getProperty("started");if(null==c||c.equals("false"))return b.startInstance(a.getPlayer()),!0;a.playerMessage("\u6709\u4eba\u6b63\u5728\u5c1d\u8bd5\u7684\u4efb\u52a1.")}}else a.playerMessage("\u4f60\u4e0d\u80fd\u8fdb\u5165\u5bc6\u5c01\u5904.");return!1};