/* ==================
 脚本类型: 反应堆    
 脚本作者：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */function act(){var a=rm.getEventInstance();if(null!=a){var b=a.getProperty("goldkey");b++;a.setProperty("goldkey",b);rm.playerMessage("Acquired key "+b+".")}};