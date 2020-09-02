/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1,skills=[21001003,21E6,21100002,21100004,21100005,21110002];function start(a,b,c){if(1==a)status++;else{if(0==status){qm.sendNext("#b(\u518d\u8003\u8651\u4e00\u4e0b\u597d\u4e86...)#k");qm.dispose();return}if(2==status){qm.MovieClipIntroUI(!0);qm.warp(914090100,0);qm.dispose();return}status--}if(0==status)qm.sendYesNo("#b(\u6211\u81ea\u5df1\u786e\u4fe1\u662f\u4f7f\u7528\u8fc7 #p1201001#\u7684\u82f1\u96c4\u5417\uff1f \u786e\u5b9a\u7684\u8bdd\u5c31\u62ff\u51fa\u529b\u91cf\u6293\u4f4f #p1201001#\u5427 \u4e00\u5b9a\u4f1a\u6709\u4ec0\u4e48\u53cd\u5e94\u3002)#k");else if(1==status){if(2E3==qm.getJob()){qm.changeJob(2100);qm.gainItem(1142129,1);qm.gainItem(1442077,1);qm.forceCompleteQuest();for(a=0;a<skills.length;a++);qm.sendNextS("#b(\u597d\u50cf\u60f3\u8d77\u4ec0\u4e48\u4e86...)#k",3)}}else 2==status&&qm.dispose()}function end(a,b,c){qm.dispose()};