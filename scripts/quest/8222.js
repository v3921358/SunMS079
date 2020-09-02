/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendNext("\u627e\u523010\u4e2a\u98ce\u66b4\u7a81\u51fb\u8005\u5fbd\u7ae0.");qm.forceStartQuest();qm.dispose()}function end(a,b,c){qm.haveItem(4032006,10)?(qm.sendNext("\u5e72\u5f97\u597d!"),qm.gainExp(85E3),qm.forceCompleteQuest(),qm.gainItem(4032006,-10)):qm.sendNext("\u8bf7\u627e\u523010\u4e2a\u98ce\u66b4\u7a81\u51fb\u8005\u5fbd\u7ae0.");qm.dispose()};