importPackage(net.sf.sunms.tools);
importPackage(net.sf.sunms.client);
importPackage(net.sf.sunms.server);
importPackage(java.util);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools);
importPackage(Packages.tools.packet);
var status = 0;

	function start() {
		status = -1;
		action(1, 0, 0);
		}
	function action(mode, type, selection) {
		if (mode == -1) {
		cm.dispose();
		} else {
		if (status >= 0 && mode == 0) {
		cm.dispose();
		return;
		}
		if (mode == 1)
		status++;
		else
		status--;


	if (status == 0) {

	    var textz = "#ro#b○#do#g○#bo#d○#ro#g○#do#r○#bo#d○#go#r○#k<#r#e个人 赞助#n#k>#r○#go#d○#go#r○#do#b○#go#r○#bo#d○#go#r○#b#k\r\n  赞助属个人行为，一线海并不给予特别的待遇或者福利.\r\n赞助比例: #b 1 : 100点券 : 1点积分 #k\r\n当前点卷：#r"+cm.getPlayer().getCSPoints(1)+" #k点\r\n当前积分：#r" + cm.getcz() + "#k 点\r\n#k#b#e 一线海为纯公益免费开放给大家娱乐,并不收取任何费用\r\n    如打不开网页请复制网址切换成IE游览器\r\n ";
        textz += "#e#r#L1#单击转到赞助网站#l#n\r\n\r\n"
		textz += "#e#r#L2#【当前充值有#b "+cm.getwzcz() +"#r 点尚未领取 请点击领取 】#l#n\r\n\r\n\r\n";;
		cm.sendSimple (textz);  

        } else if (selection == 1) {
		if(cm.getwzcz()==0){
         cm.openWeb("new.shoukabao.com/Payment/Service/8e9522d5b8c94b6973ae92a366b502e1");
		 cm.sendOk("充值完毕后,#r请单击拍卖--在线充值#k进行自助领取!\r\n");
         cm.dispose();	
         return;
            }
         cm.openWeb("new.shoukabao.com/Payment/Service/8e9522d5b8c94b6973ae92a366b502e1");
		 cm.sendOk("充值完毕后,#r请单击拍卖--在线充值#k进行自助领取!\r\n");
         cm.dispose();
}else if (selection == 2){//充值领取
		if(cm.getwzcz()==0){
		cm.sendOk("您当前未兑现金额为"+ cm.getwzcz() +"点 ，兑换失败！\r\n#r充值比例1元=100点卷.\r\n当前积分为"+ cm.getcz() +"点\r\n\r\n#r充值比例1元=100点券1积分.");
		cm.dispose();	
		}else{
		var  j = cm.getwzcz();
		cm.setwzcz(-j);
		cm.gainDJ(j*1);
        cm.gaincz(j*0.01);	//积分
        cm.sendOk("兑现成功！获得"+ j*1 + "点券！\r\n当前拥有\r\n未兑现金额为"+ cm.getwzcz() +"点 \r\n当前积分为"+ cm.getcz() +"点\r\n\r\n#r充值比例1元=100点券1积分.");
		cm.dispose();
		}
			}
			}

}


