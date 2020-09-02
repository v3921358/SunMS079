/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.getMap().killAllMonsters(!0);qm.spawnMonster(3300008,1);qm.sendNext("\u8bf7\u5e2e\u6211\u9664\u6389\u8fd9\u4e2a\u4eba\uff01\uff01");qm.forceCompleteQuest(2332);qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.gainItem(4032386,1);qm.gainItem(4032387,1);qm.forceCompleteQuest();qm.dispose()};