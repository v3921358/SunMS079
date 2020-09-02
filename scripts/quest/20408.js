/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){end(a,b,c)}function end(a,b,c){if(0==a){if(0==status){qm.sendNext("\u6211\u731c\u4f60\u8fd8\u6ca1\u51c6\u5907\u597d\u3002");qm.dispose();return}if(2<=status)status--;else{qm.dispose();return}}else status++;0==status?qm.sendYesNo("\u4f60\u5b58\u5728\u7687\u5bb6\u9a91\u58eb\u56e2\uff0c\u90a3\u4e48\u4f60\u60f3\u6210\u4e3a\u4e00\u540d\u9a91\u58eb\u5b98\u5458\uff1f"):1==status?(qm.forceCompleteQuest(),1111==qm.getJob()?qm.changeJob(1112):1211==qm.getJob()?qm.changeJob(1212):1311==qm.getJob()?qm.changeJob(1312):1411==qm.getJob()?qm.changeJob(1412):1511==qm.getJob()&&qm.changeJob(1512),qm.sendNext("\u4f60\u73b0\u5728\u7687\u5bb6\u9a91\u58eb\u56e2\u7684\u9a91\u58eb\u5b98\u5458\u3002")):3==status&&(qm.sendPrev("\u73b0\u5728\u56de\u53bb\u627e\u5973\u7687\u5427\u3002"),qm.dispose())};