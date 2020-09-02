importPackage(Packages.tools);
importPackage(Packages.client);

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

	    var textz = "                  #r欢迎来到冒险岛#b\r\n     在我这里每天可以领取一些道具以及签到证明\r\n   #v4001266#兑换签到奖励，更多奖励请点击拍卖签到换物\r\n 以及签到:"
		+cm.getmoneyb() +"次,满30次获得大奖,并且重置\r\n";

            textz += "#r#L0##r签到\r\n";//setmoneyb

		cm.sendSimple (textz);  

	}else if (status == 1) {
if (selection == 0){
		if (cm.getLevel() <= 29) {
	            cm.sendOk("等级大于30才能领取");
                    cm.dispose();
			} else if( cm.getPlayer().getBossLog("meiri") > 0 ){	

						cm.sendOk("                 每天只能领取一次");
					cm.dispose();
				}else{
                  cm.getPlayer().setBossLog("meiri")
				 cm.setmoneyb(1);//签到+1
				if(cm.getmoneyb() ==30 ){//重置
				cm.setmoneyb(-30);	
				 cm.sendOk("这里给大奖");
			    }
                if(cm.getmoneyb() ==2 ){//签2天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("2天给大奖");
				} 
				 if(cm.getmoneyb() ==3 ){//签3天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("3天给大奖");
				} 
				 if(cm.getmoneyb() ==4 ){//签4天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("4天给大奖");
				} 
				if(cm.getmoneyb() ==5 ){//签5天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("5天给大奖");
				} 
				if(cm.getmoneyb() ==6 ){//签6天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("6天给大奖");
				} 
				if(cm.getmoneyb() ==7 ){//签7天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("7天给大奖");
				} 
				if(cm.getmoneyb() ==8 ){//签8天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("8天给大奖");
				} 
				if(cm.getmoneyb() ==9 ){//签9天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("9天给大奖");
				} 
				if(cm.getmoneyb() ==10 ){//签10天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("10天给大奖");
				} 
				if(cm.getmoneyb() ==11 ){//签11天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("11天给大奖");
				} 
				if(cm.getmoneyb() ==12 ){//签12天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("12天给大奖");
				} 
				if(cm.getmoneyb() ==13 ){//签13天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("13天给大奖");
				} 
				if(cm.getmoneyb() ==14 ){//签14天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("14天给大奖");
				} 
				if(cm.getmoneyb() ==15 ){//签15天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("15天给大奖");
				} 
				if(cm.getmoneyb() ==16 ){//签16天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("16天给大奖");
				} 
				if(cm.getmoneyb() ==17 ){//签17天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("17天给大奖");
				} 
				if(cm.getmoneyb() ==18 ){//签18天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("18天给大奖");
				} 
				if(cm.getmoneyb() ==19 ){//签19天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("19天给大奖");
				} 
				if(cm.getmoneyb() ==20 ){//签20天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("20天给大奖");
				} 
				if(cm.getmoneyb() ==21 ){//签21天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("21天给大奖");
				} 
				if(cm.getmoneyb() ==22 ){//签22天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("22天给大奖");
				} 
				if(cm.getmoneyb() ==23 ){//签23天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("23天给大奖");
				} 
				if(cm.getmoneyb() ==24 ){//签24天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("14天给大奖");
				} 
				if(cm.getmoneyb() ==25 ){//签25天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("25天给大奖");
				} 
				if(cm.getmoneyb() ==26 ){//签26天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("26天给大奖");
				} 
				if(cm.getmoneyb() ==27 ){//签27天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("27天给大奖");
				} 
				if(cm.getmoneyb() ==28 ){//签28天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("28天给大奖");
				} 
				if(cm.getmoneyb() ==29 ){//签29天给什么
					cm.gainItem(4001266, 1);
				   cm.sendOk("29天给大奖");
				} 
				 //就这样下设置 
				 cm.dispose();	 
				}

}
}
}
}
