/* ==================
 �ű�����:  ����	    
 �ű���Ȩ����Ϸ���Ŷ�
 ��ϵ�ۿۣ�297870163    609654666
 =====================
 */function start(a,b,c){qm.forceStartQuest();qm.dispose()}var status=-1;function end(a,b,c){if(1==a)status++;else{if(4==status){qm.sendNext("\u54e6\uff0c\u90a3\u6837\u554a\u3002\u82f1\u96c4\u679c\u7136\u5f88\u5fd9\u554a....\u54ed\u54ed\u3002\u8981\u662f\u6539\u53d8\u4e3b\u610f\u4e86\uff0c\u968f\u65f6\u53ef\u4ee5\u6765\u627e\u6211\u3002");qm.dispose();return}status--}0==status?qm.sendNext("\u521a\u624d\u6211\u597d\u50cf\u542c\u5230\u8bf4\u201c\u82f1\u96c4\u56de\u6765\u4e86...\u201d\uff0c\u662f\u6211\u542c\u9519\u4e86\u5417\uff1f\u4ec0\u4e48\uff1f\u6ca1\u542c\u9519\u5417\uff1f\u771f\u7684\u8fd9\u4f4d...\u8fd9\u4f4d\u662f\u82f1\u96c4\u5417\uff1f\uff01"):1==status?qm.sendNextPrev("   #i4001171#"):2==status?qm.sendNextPrev("\u771f\u662f\u9ad8\u5174\u554a...\u7adf\u7136\u80fd\u8fd9\u6837\u89c1\u5230\u82f1\u96c4\uff0c\u771f\u662f\u8363\u5e78\u554a\uff01\u6c42\u60a8\u63e1\u4e2a\u624b\u5427\uff0c\u987a\u4fbf\u518d\u62b1\u4e00\u4e0b\u6211\u5c31\u66f4\u597d\u4e86\uff0c\u4f46\u9996\u5148\u8fd8\u662f\u5148\u7b7e\u4e2a\u540d\u5427, #p1201000#."):3==status?qm.sendNextPrev("\u53ef\u662f...\u82f1\u96c4\u600e\u4e48\u6ca1\u6709\u5e26\u6b66\u5668\u5462\u3002\u636e\u6211\u6240\u77e5\u82f1\u96c4\u6709\u81ea\u5df1\u6b66\u5668...\u554a\uff01\u5e94\u8be5\u662f\u548c\u9ed1\u9b54\u6cd5\u5e08\u51b3\u6597\u65f6\u5f04\u6389\u4e86\u3002"):4==status?qm.sendYesNo("\u51d1\u5408\u7740\u7528\u53ef\u80fd\u4f1a\u592a\u5bd2\u9178\uff0c\u4e0d\u8fc7#b\u8bf7\u4f60\u5148\u6536\u4e0b\u8fd9\u628a\u5251\u5427\uff01#k \u8fd9\u662f\u6211\u9001\u7ed9\u82f1\u96c4\u7684\u793c\u7269\u3002\u82f1\u96c4\u7a7a\u7740\u624b\u603b\u662f\u6709\u70b9\u5947\u602a... \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i1302000##t1302000# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 \u7ecf\u9a8c\u503c"):5==status?(1==qm.getQuestStatus(21011)&&(qm.gainItem(1302E3,1),qm.gainExp(35)),qm.forceCompleteQuest(),qm.sendNextPrevS("#b(\u8fde\u6280\u80fd\u4e00\u70b9\u90fd\u4e0d\u50cf\u82f1\u96c4...\u8fde\u5251\u90fd\u597d\u964c\u751f\u3002\u6211\u4e4b\u524d\u771f\u7684\u6709\u7528\u8fc7\u5251\u5417\uff1f\u5251\u8be5\u600e\u4e48\u914d\u6234\u5462\uff1f)#k",3)):6==status&&(qm.summonMsg(16),qm.dispose())};