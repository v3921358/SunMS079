importPackage(java.lang);
importPackage(Packages.client);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.constants);
importPackage(Packages.net.channel);
importPackage(Packages.tools);
importPackage(Packages.scripting);
importPackage(Packages.tools.packet);
importPackage(Packages.tools.data);
importPackage(Packages.tools);
var status = 0;
var itemList =   
Array(   
			Array(5150038,100,1,1),//��������5000016
			Array(5150040,1000,1,1),//�ʼ�1032026
			Array(5150001,1000,1,1),//��Ա��
			Array(5151001,1000,1,1),//��Ա��
			Array(5152001,1000,1,1),//��Ա��
			Array(5153000,1000,1,1),//��Ա��
			Array(5390006,100,1,1),//��������5390006
			Array(5390000,1000,1,1),//����5390006
			Array(5390001,1000,1,1),//����5390006
			Array(5390002,1000,1,1),//����5390006
			Array(5390003,1000,1,1),//����5390006
			Array(5390004,1000,1,1),//����5390006
			Array(5390005,100,1,1),//����5390006
			Array(5152100,1000,1,1),//�۾�
			Array(5152101,1000,1,1),//�۾�
			Array(5152102,1000,1,1),//�۾�
			Array(5152103,1000,1,1),//�۾�
			Array(5152104,1000,1,1),//�۾�
			Array(5152105,1000,1,1),//�۾�
			Array(5152106,1000,1,1),//�۾�
			Array(5152107,1000,1,1),//�۾�
			Array(5110000,1000,1,1),//�ɿ���
			Array(4001136,1000,1,1),//������Ƭ
			Array(4032391,1000,1,1),//������Ƭ
			Array(4032392,1000,1,1),//������Ƭ
			Array(4032393,1000,1,1),//������Ƭ
			Array(4031559,1000,1,1),//������Ƭ
			Array(4031560,1000,1,1),//������Ƭ
			Array(4031561,1000,1,1),//������Ƭ
			Array(1002418,1000,1,1),//�ϱ�ֽͷ��
			Array(1004556,200,1,1),//��Ҷ��״ñ��
			Array(1003451,400,1,1),//��Ҷͷ��0
			Array(1003452,400,1,1),//��Ҷͷ��8
			Array(1003453,400,1,1),//��Ҷͷ��30
			Array(1002510,500,1,1),//��Ҷͷ��0
			Array(1002419,500,1,1),//��Ҷñ
			Array(1002517,500,1,1),//��Ҷͷ��30
			Array(1002424,1000,1,1),//����ñ��
			Array(1002425,1000,1,1),//����ñ��
			Array(1002441,1000,1,1),//��Ѫͷ��
			Array(1002452,1000,1,1),//����ñ��
			Array(1002543,1000,1,1),//����ñ��
			Array(1002554,400,1,1),//ͷ������
			Array(1002600,80,1,1),//��Ҷͷ����ɫ
			Array(1002601,80,1,1),//��Ҷͷ����ɫ
			Array(1002602,80,1,1),//��Ҷͷ����ɫ
			Array(1002603,800,1,1),//��Ҷͷ����ɫ
			Array(1003110,1000,1,1),//��Ҷͷ��30��ɫ
			Array(1003111,1000,1,1),//��Ҷͷ��30��ɫ
			Array(1002662,1000,1,1),//�����껨
			Array(1002665,1000,1,1),//������ñ��
			Array(1002699,200,1,1),//�Ϲ�ñ��
			Array(1002723,1000,1,1),//�±�ñ��
			Array(1002737,200,1,1),//�Թ���ͷ��
			Array(1002760,1000,1,1),//����ñ
			Array(1002761,1000,1,1),//��Ҷ���
			Array(1002788,800,1,1),//è���
			Array(1002799,1000,1,1),//�¹��
			Array(1002812,200,1,1),//���ӻ�
			Array(1002841,1000,1,1),//����С��
			Array(1002851,100,1,1),//��ñ
			Array(1002894,1000,1,1),//���Ʒ�����
			Array(1002895,1000,1,1),//��֯������
			Array(1002896,1000,1,1),//��֯������
			Array(1002897,1000,1,1),//��֯������
			Array(1002898,1000,1,1),//��֯������
			Array(1002899,1000,1,1),//��֯������
			Array(1002900,1000,1,1),//��֯������
			Array(1002902,200,1,1),//��֯������+2g
			Array(1002901,1000,1,1),//��֯������
			Array(1002391,1000,1,1),//����ͷ��(��) 
			Array(1002392,1000,1,1),//����ͷ��(��) 
			Array(1002393,1000,1,1),//����ͷ��(��) 
			Array(1002394,1000,1,1),//����ͷ��(��) 
			Array(1002395,1000,1,1),//����ͷ��(��)
			Array(1002585,1000,1,1),//��ɫ������ñ��
			Array(1002586,1000,1,1),//��ɫ������ñ��
			Array(1002587,1000,1,1),//��ɫ������ñ��
			Array(1002967,1000,1,1),//�������޶���
			Array(1002971,300,1,1),//Ʒ�˱�ñ��
			Array(1002985,1000,1,1),//����ñ��
			Array(1002989,100,1,1),//����ñ��
			Array(1002997,1000,1,1),//�˱����
			Array(1003027,100,1,1),//ħŮñ��
			Array(1003075,1000,1,1),//��ɫ����
			Array(1003273,1000,1,1),//��������
			Array(1022021,200,1,1),//�κ�������
			Array(1022022,200,1,1),//�κ����۾�
			Array(1022066,200,1,1),//�������۾�
			Array(1022047,100,1,1),//èͷӥ
			Array(1022058,100,1,1),//��è
			Array(1022060,100,1,1),//����
			Array(1022067,100,1,1),//�ں���
			Array(1032025,400,1,1),//��Ҷ�Ӷ���
			Array(1032032,400,1,1),//��Ҷ����
			Array(1032041,400,1,1),//��Ҷ����
			Array(1032040,400,1,1),//��Ҷ����
			Array(1032041,400,1,1),//��Ҷ����
			Array(1032042,400,1,1),//��Ҷ����
			Array(1032047,400,1,1),//���ֶ���
			Array(1032059,400,1,1),//��ȿ��ֶ���
			Array(1032098,100,1,1),//���ܲ�����
			Array(1032026,100,1,1),//��ˮ������
			Array(1032027,400,1,1),//��ˮ������
			Array(1032028,100,1,1),//��ˮ������
			Array(1012056,500,1,1),//������
			Array(1012325,80,1,1),//��Ҷ����
			Array(1012377,80,1,1),//��Ҷ������
			Array(1012376,1,1,1),//��Ҷ������
			Array(1012471,1,1,1),//��Ҷ������
			Array(1012011,100,1,1),//ʥ��¹����
			Array(1012012,100,1,1),//ʥ��¹����
			Array(1012013,100,1,1),//ʥ��¹����
			Array(1012014,100,1,1),//ʥ��¹����
			Array(1012015,100,1,1),//ʥ��¹����
			Array(1012016,100,1,1),//ʥ��¹����
			Array(1012017,100,1,1),//ʥ��¹����
			Array(1012018,100,1,1),//ʥ��¹����
			Array(1012019,100,1,1),//ʥ��¹����
			Array(1012020,100,1,1),//ʥ��¹����
			Array(1012058,150,1,1),//ƥŵ�ܱ���
			Array(1012059,150,1,1),//ƥŵ�ܱ���
			Array(1012060,150,1,1),//ƥŵ�ܱ���
			Array(1012061,150,1,1),//ƥŵ�ܱ���
			Array(1012070,80,1,1),//��ݮѩ��
			Array(1012071,80,1,1),//�ɿ���ѩ��
			Array(1012072,300,1,1),//���ѩ��
			Array(1012073,300,1,1),//����ѩ��
			//Array(1012084,400,1,1),//С������״
			//Array(1012086,400,1,1),//С������״
			//Array(1012087,400,1,1),//С������״
			//Array(1012088,400,1,1),//С������״
			Array(1012100,800,1,1),//��ɫ����
			Array(1012132,50,1,1),//����
			Array(1012146,50,1,1),//��ߺ����
			Array(1082145,300,1,1),//���ػ�
			Array(1082146,300,1,1),//���غ�
			Array(1082147,300,1,1),//������
			Array(1082148,300,1,1),//������
			Array(1082149,100,1,1),//���غ�
			Array(1082150,300,1,1),//���ػ�
			Array(1082252,500,1,1),//��Ҷ����
			Array(1092022,100,1,1),//��ɫ�����
			Array(1092035,100,1,1),//��ɫ�����
			Array(1092039,100,1,1),//��Ҷ���Ƶ�״
			Array(1092045,80,1,1),//��Ҷħ��ʦ����
			Array(1092047,80,1,1),//��Ҷ��������
			Array(1092050,80,1,1),//����˫��������
			Array(1092051,500,1,1),//ơ�ƶ���
			Array(1092054,80,1,1),//ͨ�����϶���
			Array(1102079,1000,1,1),//�ƾɵĺ�ɫ����
			Array(1102081,1000,1,1),//�ƾɵĻ�ɫ����
			Array(1102082,500,1,1),//�ƾɵĺ�ɫ����
			Array(1102040,100,1,1),//���������
			Array(1102041,100,1,1),//���������
			Array(1102042,100,1,1),//����������
			Array(1102043,100,1,1),//���������
			Array(1102166,500,1,1),//��Ҷ����20
			Array(1102167,400,1,1),//��Ҷ����40
			Array(1102297,80,1,1),//��������
			Array(1102298,80,1,1),//��������
			Array(1102299,80,1,1),//��������
			Array(1102379,200,1,1),//��������
			Array(1072262,400,1,1),//��ɫ����˹ƤЬ
			Array(1072264,400,1,1),//��ɫ����˹ƤЬ
			Array(1072263,500,1,1),//��ɫ����˹ƤЬ
			Array(1072239,100,1,1),//��ɫ��Ь
			Array(1072238,100,1,1),//�Ͻ�Ь
			Array(1050100,100,1,1),//ԡ����
			Array(1050127,80,1,1),//ԡ����+2����
			Array(1051098,100,1,1),//ԡ��Ů
			Array(1051140,100,1,1),//ԡ��Ů
        	Array(1402014,10,1,1),//�¶ȼ�
			Array(1382015,10,1,1),//��Ģ��
			Array(1382016,10,1,1),//�㹽
			Array(1302080,100,1,1),//ð�յ�С����
			Array(1302085,100,1,1),//����
			Array(1302087,1000,1,1),//���
			Array(1402044,10,1,1),//�Ϲϵ���
			Array(1302061,100,1,1),//���ٱ���
			Array(1432046,300,1,1),//ʥ��������
			Array(1432013,100,1,1),//�Ϲ�ǹ
        	Array(1302024,500,1,1),//�ϱ�ֽ����
        	Array(1422011,300,1,1),//��ƿ
        	Array(1332053,100,1,1),//Ұ���տ�
        	Array(1372017,100,1,1),//��·��
        	Array(1422036,100,1,1),//��ߴ���
			Array(1302021,1000,1,1),//��ߴ���1302021
        	Array(1322027,300,1,1),//ƽ�׹�
        	Array(1442039,100,1,1),//������
        	Array(1332021,100,1,1),//������
			Array(1302049,100,1,1),//���߱�
			Array(1312012,500,1,1),//����Ȧ
			Array(1322006,1000,1,1),//�ֹ�
			Array(1302339,1000,1,1),//����
			Array(1302340,1000,1,1),//����
			Array(1302341,1000,1,1),//����
			Array(1312013,600,1,1),//�̹ر�
			Array(1312014,600,1,1),//������
			Array(1322034,1000,1,1),//����
			Array(1322051,1000,1,1),//��Ϧ
			Array(1322102,1000,1,1),//����
			Array(1332030,200,1,1),//����
			Array(1372008,100,1,1),//����
			Array(1332048,100,1,1),//����
			Array(1442065,100,1,1),//���˰�30
			Array(1442066,100,1,1),//���˰�55
			Array(1442121,100,1,1),//õ��30
			Array(1442122,100,1,1),//õ��60
			Array(1442123,100,1,1),//õ��90
			Array(1302058,100,1,1),//��Ҷɡ
			Array(1302025,500,1,1),//����ɡ
			Array(1302026,200,1,1),//����ɽ
			Array(1302027,800,1,1),//����ɽ
			Array(1302028,800,1,1),//����ɡ
			Array(1302029,500,1,1),//����ɡ
			Array(1302071,1000,1,1),//��ɫ��ӾȦ
			Array(1322021,1000,1,1),//��ӾȦ
			Array(1322022,500,1,1),//��ӾȦ
			Array(1322023,1000,1,1),//��ӾȦ
			Array(1322024,300,1,1),//��ӾȦ
			Array(1432015,400,1,1),//��ɫ��ѩ��
			Array(1432016,400,1,1),//��ɫ��ѩ��
			Array(1432017,300,1,1),//��ɫ��ѩ��
			Array(1432018,200,1,1),//��ɫ��ѩ��
			Array(1442054,1000,1,1),//����
			Array(1442055,300,1,1),//����
			Array(1442012,1000,1,1),//ѩ��0
			Array(1442013,1000,1,1),//ѩ��12
			Array(1442014,1000,1,1),//ѩ��24
			Array(1442015,700,1,1),//ѩ��36
			Array(1442030,100,1,1),//
			Array(1442021,1000,1,1),//���ϰ�
			Array(1442022,80,1,1),//���ϰ�
			Array(1442023,700,1,1),//���ϰ�
			Array(1422030,10,1,1),//�ۺ캣������
			Array(1422031,10,1,1),//��ɫ��������
			Array(1422011,300,1,1),//���
			Array(2000005,100,1,1),//����ҩˮ
			Array(2000004,500,1,1),//ҩˮ		
        	Array(3010112,500,1,1),//�������
		    Array(3010043,100,1,1),//ħŮ�ķ�ɨ��
		    Array(3015051,10,1,1),//���ްԹ��ʾ���
		    Array(3015183,10,1,1),//��ˮ�鱳������
		    Array(3015096,10,1,1),//���������������
		    Array(3010180,1000,1,1),//HP����
		    Array(3010009,10,1,1),//��ɫ������Ͱ
		    Array(3010028,100,1,1),//�Ϻ�������
		    Array(3012006,50,1,1),//�紵����
        	Array(3010013,10,1,1),//�Ƴ�����
        	Array(3010014,500,1,1),//������
        	Array(3010018,1000,1,1),//Ҭ����ɳ̲��
        	Array(3010025,1000,1,1),//��Ҷ�����
        	Array(3012001,1000,1,1),//����
        	Array(3012002,100,1,1),//ԡͰ---
        	Array(3010073,10,1,1),//PB����
        	Array(3010049,50,1,1),//ѩ����
        	Array(3010068,50,1,1),//¶ˮ����
        	Array(3010070,10,1,1),//���ް�Ʒ����
        	Array(3010095,100,1,1),//ʯͷ������
//-----����ֲ������-----
        	Array(3010898,500,1,1),//������������
        	Array(3010465,500,1,1),//�ɰ���������
        	Array(3010460,100,1,1),//��������
        	Array(3010450,100,1,1),//����������
        	Array(3010455,100,1,1),//�����ĵ�������
        	Array(3010456,100,1,1),//�ž黨����
        	Array(3010458,100,1,1),//������������
        	Array(3010863,50,1,1),//��ŭ������ʦ����
        	Array(3010447,100,1,1),//˯��С������
        	Array(3010446,100,1,1),//Ƥ���ʺ�����
        	Array(3010448,100,1,1),//����ԡ����
        	Array(3010434,100,1,1),//�ȴ���������
        	Array(3010428,100,1,1),//ˮ������
        	Array(3010409,100,1,1),//��������
      	    Array(3010094,100,1,1),//ƯƯ������-----------------------
        	Array(3010411,100,1,1),//��βʨ����
        	Array(3010085,50,1,1),//����������
        	Array(3010077,100,1,1),//èͷӥ����
        	Array(3010053,50,1,1),//���Ӽ��������
        	Array(3010151,200,1,1),//���˵�����
        	Array(3015415,50,1,1),//��������
        	Array(3010142,50,1,1),//ˮ�������
        	Array(3010144,100,1,1),//��Ϧ����
        	Array(3015430,100,1,1),//��������
        	Array(3010117,100,1,1),//ħ��������
        	Array(3015424,100,1,1),//������������
        	Array(3015428,100,1,1),//�ͺ���һ����̻�����
        	Array(3015427,100,1,1),//�ͺ���һ�����齫����
        	Array(3010804,100,1,1),//���������������
        	Array(3015045,50,1,1),//������������--------------
        	Array(3010375,50,1,1),//����ĳ���ҩˮ����
        	Array(3015057,50,1,1),//�̻���������
        	Array(3010373,50,1,1),//�ƶ�ϴ�ּ�����
        	Array(3010799,50,1,1),//��Ĺ��������
        	Array(3010737,50,1,1),//����Ӣ��������
        	Array(3010734,100,1,1),//è������
        	Array(3010738,50,1,1),//��������������
        	Array(3010740,100,1,1),//ʥ��������
        	Array(3010365,100,1,1),//�ݱ˵����ĹŴ�ʯ������
        	Array(3010306,100,1,1),//�����Ļ�����
        	Array(3010301,50,1,1),//�߼�HPҩˮ����
        	Array(3010188,100,1,1),//�ࡤ�װ�����
        	Array(3010186,100,1,1),//��������
        	Array(3010184,50,1,1),//��������
         	Array(3010173,50,1,1),//��ʥ����������
        	Array(3010175,50,1,1),//����������
        	Array(3010195,100,1,1),//�޼�֮������
        	Array(3010210,100,1,1),//��ݮ������±�����--------------
        	Array(3010224,300,1,1),//�����������
        	Array(3010036,50,1,1),//������ǧ
        	Array(3010044,50,1,1),//ͬһ��ɡ��
        	Array(3010111,100,1,1),//��������
        	Array(3010115,100,1,1),//�ܱ�����
        	Array(3010126,50,1,1),//����ħ����
        	Array(3010313,50,1,1),//�ۺ���������
        	Array(3010128,50,1,1),//��������
        	Array(3010133,50,1,1),//������
        	Array(3010152,200,1,1),//������������
        	Array(3010291,50,1,1),//���տ�����
        	Array(3010172,50,1,1),//�ǿ�����
        	Array(3010168,50,1,1),//������������
        	Array(3010185,100,1,1),//С���Ʒ����
        	Array(3010622,100,1,1),//�ҵĺ��ѵ���֮��
        	Array(3010453,50,1,1),//���ӳ˷���
        	Array(3010493,50,1,1),//�Ǿ�����--------------------------------
        	Array(3010527,50,1,1),//�����
        	Array(3010531,50,1,1),//С���ϳ���
        	Array(3010565,50,1,1),//�ҵ�Ů������
        	Array(3010587,50,1,1),//ʱ�佺��
        	Array(3010609,50,1,1),//����������
        	Array(3010608,50,1,1),//��������������
        	Array(3010661,50,1,1),//�����������
        	Array(3010678,50,1,1),//���Ӷ�֮��Ϣ
        	Array(3010739,50,1,1),//ѩ���㲨����
        	Array(3010744,50,1,1),//ð�յ���שͷ������
        	Array(3010752,50,1,1),//õ������
        	Array(3010756,50,1,1),//�����в�è����
        	Array(3010767,50,1,1),//ѩ������
        	Array(3010760,50,1,1),//�Ŵ�ԡ������
        	Array(3010795,50,1,1),//ɭ������Ϣ��
        	Array(3010806,50,1,1),//��ӣ������
        	Array(3010848,50,1,1),//���̺���˹�����
        	Array(3012024,100,1,1)//ɳ̲��������
);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
           selStr = "#e#r���ְٱ�����Ի��������Ʒ��99%���\r\n\#b�Ƿ�����һ��#v5220000#��#n#k\r\n";
		   for (i = 0; i < itemList.length; i++) {
                selStr += "#d#v" + itemList[i][0] + "#";
            }
			
			cm.sendOk(selStr);
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
		selStr = "\r\n";
		   for (i = 0; i < itemList.length; i++) {
                selStr += "#d#v" + itemList[i][0] + "#";
            }
        if (cm.haveItem(4170005, 1)) {//�ж���Ʒ
            cm.sendYesNo("�������#v4170005#�ҿ��԰����ҿ�������������ʲô��\r\n#g�ң�"+selStr);
        } else {
            cm.sendOk("�������#v4170005#�ҿ��԰����ҿ�������������ʲô��\r\n#e��Ʒ�б���"+selStr);
            cm.safeDispose();
        }
    } else if (status == 1) {
		var ii = MapleItemInformationProvider.getInstance();
        var chance = Math.floor(Math.random()*800);
        var finalitem = Array();
        for (var i = 0; i < itemList.length; i++) {
            if (itemList[i][1] >= chance) {
                finalitem.push(itemList[i]);
            }
        }
        if (finalitem.length != 0) {
            
            var random = new java.util.Random();
            var finalchance = random.nextInt(finalitem.length);
            var itemId = finalitem[finalchance][0];
            var quantity = finalitem[finalchance][2];
           //var notice = finalitem[finalchance][3];
           // item = cm.gainGachaponItem(itemId, quantity,);//, notice
			var Laba = finalitem[finalchance][3];
			       
				   
				   if(ii.getInventoryType(itemId).getType() == 1){
			        	var toDrop = ii.randomizeStats(ii.getEquipById(itemId)).copy();
						MapleInventoryManipulator.addFromDrop(cm.getC(),toDrop,false);
				}else{
				 	 var toDrop = new Equip(itemId,0).copy();
				cm.gainItem(itemId,quantity);
				}
				
            if (Laba == 1) {
			
			 cm.itemlaba("ϲ���콵",toDrop)
                cm.gainItem(4170005, -1);//�۳���Ʒ
                cm.sendOk("������ #b#t" + itemId + "##k " + quantity + "����");
            } else {//������
                cm.sendOk("������ #b#t" + itemId + "##k " + quantity + "����");
            }
            cm.safeDispose();
        } else {
            cm.sendOk("�������������ʲô��û���õ���");
            cm.gainItem(4170005, -1);//�۳���Ʒ
            cm.safeDispose();
        }
    }
}