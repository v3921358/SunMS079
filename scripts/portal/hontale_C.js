/* ==================
 ???????:  NPC	    
 ?????????????     
 ???????297870163    609654666
 =====================
 */function enter(a){var b=a.getPlayer().getEventInstance();if(null==b.getProperty("choiceCave"))return a.getPlayer().message(5,"\u9152\u6d41 \u60bc\u5954\u635e \u6025\u7436\u767b\u7624 \u81fc\u75bd\u56bc\u806a\u4fc3."),!1;b.getProperty("choiceCave").equals("0")?a.allPartyWarp(240050300,!1):a.allPartyWarp(240050310,!1);return!0};