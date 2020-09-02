/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){if(1==a.getQuestStatus(6153))if(a.haveItem(4031471))a.playerMessage("\u8d5b\u91cc\u6728\u5df2\u7ecf\u5c4f\u853d\u3002");else if(a.haveItem(4031475)){var b=a.getEventManager("4jberserk");if(null==b)a.playerMessage("\u4f60\u4e0d\u88ab\u5141\u8bb8\u8fdb\u5165\u672a\u77e5\u7684\u539f\u56e0\u3002\u518d\u8bd5\u4e00\u6b21.");else return b.startInstance(a.getPlayer()),!0}else a.playerMessage("\u8981\u8fdb\u5165\uff0c\u4f60\u9700\u8981\u4e00\u4e2a\u9057\u5fd8\u7684\u795e\u6bbf\u94a5\u5319.");else a.playerMessage("\u4f60\u4e0d\u80fd\u8fdb\u5165\u8fd9\u4e2a\u5730\u65b9.");return!1};