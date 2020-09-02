/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getPlayer().getEventInstance();if(null!=a&&null!=a.getProperty("stage5")){var b=parseInt(a.getProperty("stage5"));if(1==b)rm.mapMessage("\u95e8\u5df2\u7ecf\u89e3\u9501.");else if(1<b)rm.spawnMonster(9420024+6*parseInt(a.getProperty("mode"))),rm.spawnMonster(9420027+6*parseInt(a.getProperty("mode"))),rm.spawnMonster(9420029+6*parseInt(a.getProperty("mode"))),rm.mapMessage("Some monsters have been summoned.");else if(0>=b)return;a.setProperty("stage5",""+(b-1))}};