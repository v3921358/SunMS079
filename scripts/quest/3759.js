/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){qm.sendOk("\u53bb#b\u9996\u5e2dTatamo#k \u5728Leafre \u5e76\u5e26\u56de\u4e00\u4e2a\u9f99\u82d4\u6d78\u818f.");qm.forceStartQuest();qm.dispose()}function end(a,b,c){status++;0==status?qm.haveItem(4032531,1)?qm.sendNext("\u5927\uff01 \u8bf7\u7b49\u5230\u6211\u628a\u8fd9\u4e9b\u6210\u5206\u6df7\u5408\u5728\u4e00\u8d77..."):(qm.sendOk("\u8bf7\u53bb#b\u9996\u5e2dTatamo#k Leafre\u7684\u5e76\u5e26\u56de\u4e00\u4e2a\u9f99\u82d4\u6d78\u818f."),qm.forceStartQuest(),qm.dispose()):(qm.teachSkill(qm.getPlayer().getStat().getSkillByJob(1026,qm.getPlayer().getJob()),1,0),qm.gainExp(11E3),qm.removeAll(4032531),qm.sendOk("\u6211\u4eec\u53bb\uff01 \u4f60\u5df2\u7ecf\u5b66\u4f1a\u4e86\u7ff1\u7fd4\u7684\u6280\u80fd\uff0c\u5e76\u5c06\u80fd\u591f\u98de\u7fd4\uff0c\u4f7f\u7528\u5927\u91cf\u7684MP."),qm.forceCompleteQuest(),qm.dispose())};