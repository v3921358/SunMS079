/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){rm.mapMessage(6,"\u7eff\u8272\u8fce\u6708\u82b1\u79cd\u5b50\u79cd\u690d\u6210\u529f!");var a=rm.getEventManager("HenesysPQ");if(null!=a){var b=rm.getMap().getReactorByName("fullmoon");a.setProperty("stage",parseInt(a.getProperty("stage"))+1);b.forceHitReactor(b.getState()+1);a.getProperty("stage").equals("6")&&(rm.mapMessage(6,"\u5f00\u59cb\u4fdd\u62a4\u6708\u5999!!!"),rm.getMap().setSpawns(!0),rm.getMap().respawn(!0),rm.getMap().spawnMonsterOnGroundBelow(a.getMonster(9300061),new java.awt.Point(-183,-433)),rm.achievement(50))}};