/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function action(a,b,c){1==a?status++:status--;0==status&&(0>=cm.getMap().getAllMonstersThreadsafe().size()?(cm.forceStartQuest(21733),cm.forceStartQuest(21762,"2"),cm.warp(104000004)):cm.sendNext("\u8bf7\u6d88\u706d\u4eba\u5076\u5e08\u5728\u6765\u89e3\u6551\u6211\u5427!"),cm.dispose())};