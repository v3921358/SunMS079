/* ==================
 脚本类型: 脚本
 版权：游戏盒团队     
 联系扣扣：297870163    609654666
 =====================
 */
var Message = new Array("需要商业授权可以找扣扣297870163购买!");

var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    setupTask = em.schedule("start", 900000);
}

function cancelSchedule() {
	setupTask.cancel(false);
}

function start() {
    scheduleNew();
    em.broadcastYellowMsg("[" + em.getChannelServer().getServerName() + "] " + Message[Math.floor(Math.random() * Message.length)]);
}