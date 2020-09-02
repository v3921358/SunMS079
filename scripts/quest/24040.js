/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */var status=-1;function start(a,b,c){-1==status?(qm.sendNext("\u8bc5\u5492\u5bf9\u6211\u8fd9\u6837\u505a\u4e86\u5417\uff1f \u8fd9\u5fc5\u5b9a\u662f\u4e00\u573a\u5669\u68a6."),qm.forceCompleteQuest(),status++):(qm.ShowWZEffect("Effect/Direction5.img/mersedesQuest/Scene2"),qm.showWZEffect("Effect/OnUserEff.img/questEffect/mercedes/q24040"),qm.forceCompleteQuest(29952),qm.gainItem(1142336,1),qm.dispose())}function end(a,b,c){-1==status?(qm.sendNext("\u8bc5\u5492\u5bf9\u6211\u8fd9\u6837\u505a\u4e86\u5417\uff1f \u8fd9\u5fc5\u5b9a\u662f\u4e00\u573a\u5669\u68a6."),qm.forceCompleteQuest(),status++):(qm.ShowWZEffect("Effect/Direction5.img/mersedesQuest/Scene2"),qm.showWZEffect("Effect/OnUserEff.img/questEffect/mercedes/q24040"),qm.forceCompleteQuest(29952),qm.gainItem(1142336,1),qm.dispose())};