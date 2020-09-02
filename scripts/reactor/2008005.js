/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getEventManager("OrbisPQ");null!=a&&(a.setProperty("stage",parseInt(a.getProperty("stage"))+1),a=rm.getMap().getReactorByName("minerva"),a.forceHitReactor(a.getState()+1))};