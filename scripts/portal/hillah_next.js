/* ==================
 ???????:  NPC	    
 ?????????????     
 ???????297870163    609654666
 =====================
 */importPackage(java.lang);importPackage(Packages.packet.creators);function enter(a){var c=a.getPlayer().getEventInstance();if(null==c)return a.warp(262031200),!0;var b=Integer.parseInt(c.getProperty("Global_StartMap")),d=Integer.parseInt(c.getProperty("CurrentStage")),b=b+100*(d-1);null==c.getProperty(b+"Prepared")&&(a.prepareAswanMob(b,c),c.setProperty(b+"Prepared","1"));if(b==a.getPlayer().getMapId())return a.getPlayer().send(UIPacket.showInfo("\u5e26\u5088 \u90f4\u72fc \u9601\u80f6\u78d0\u752b \u845b\u6ef4 \u68f1\u9152\u5177 \u4fc3\u6f9c \u80f6\u629b\u635e\u7624\u80ba \u635e\u60bc\u4e14 \u8350 \u4e50\u56bc\u806a\u4fc3.")),a.getPlayer().message(5,"\u9601\u80f6\u78d0\u752b \u845b\u6ef4 \u68f1\u680f\u811a \u9976 \u4fc3\u6f9c \u5668\u5455\u80ba \u635e\u60bc\u79e6 \u6797\u6280\u5938."),!1;a.warp(b);return!0};