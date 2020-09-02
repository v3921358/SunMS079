function start() {
    status = -1;

    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.getPlayer().getMapId() == 108010101 || cm.getPlayer().getMapId() == 108010201 || cm.getPlayer().getMapId() == 108010301 || cm.getPlayer().getMapId() == 108010401 || cm.getPlayer().getMapId() == 108010501) {
                cm.sendOk("本地图暂时无法使用使用拍卖功能");
                cm.dispose();
                return;
            }
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "\r\n\r\n官方网站充值 ：#r您已经充值过：#r" + cm.getcz() + "#k 点卷\r\n由于维持服务器需要费用,希望得到大家支持.充值比例1:100点卷！#k\r\n"//2
            text += "#e#r#L1#单击转到充值网站#l#n\r\n\r\n"
			text += "#e#r#L2#【充值领取-当前#g "+cm.getwzcz() +"#r点券尚未领取请点击领取】#l#n\r\n\r\n"

            cm.sendOk(text); 
        } else if (selection == 1) {
		if(cm.getwzcz()==0){
         cm.openWeb("new.shoukabao.com/Payment/Service/f93a58fbb293f0c2eb1328434462924a");
		 cm.sendOk("充值完毕后,#r请单击拍卖--充值领取#k进行领取!\r\n");
         cm.dispose();	
         return;
            }
         cm.openWeb("new.shoukabao.com/Payment/Service/f93a58fbb293f0c2eb1328434462924a");
		 cm.sendOk("充值完毕后,#r请单击拍卖--充值领取#k进行领取!\r\n");
         cm.dispose();
        } else if (selection == 2) {
            if(cm.getwzcz()==0){
		cm.sendOk("您当前未兑现金额为"+ cm.getwzcz() +"点券 ，兑换失败！\r\n#r充值比例1点券=100点卷.");
		cm.dispose();	
		}else{
		var  j = cm.getwzcz();
		cm.setwzcz(-j);
		cm.gainDJ(j*1);
        cm.gaincz(+j);	
        cm.sendOk("兑现成功！获得"+ j*1 + "点卷！\r\n#r充值比例1点券=100点卷.");
		cm.dispose();
        }
    }
}}