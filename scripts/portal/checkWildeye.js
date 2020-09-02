/* ==================
 脚本类型:  传送门    
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function enter(a){null==a.getPlayer().getInventory(a.getInvType(-1)).findById(1003036)?(a.playerMessage(5,"\u4f60\u5fc5\u987b\u88c5\u5907\u7b26\u5492\u72ec\u773c\u91ce\u732a\u5e3d\u5b50\u3002"),a.warp(105050400)):1<=a.getPlayer().getBossLog("diyu")?(a.playerMessage(5,"\u6bcf\u5929\u53ea\u80fd\u8fdb\u51651\u6b21\u3002"),a.warp(105050400)):30<=a.getPlayer().getLevel()&&40>=a.getPlayer().getLevel()||(a.playerMessage(5,"\u7b49\u7ea7\u5fc5\u987b\u572830-40\u7ea7\u3002"),a.warp(105050400));return!0};