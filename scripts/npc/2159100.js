/* 
 * NPC :      Mihai
 * Map :      Timu's Forest
 */

function start() {
    cm.sendNext("呵呵...难道我刚刚发现的东西吗？那么只有一个办法了！让我们像打 #r变身术士#k!");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(cm.getMapId(), cm.getNpc());
	cm.spawnMonster(9001031,1); // Transforming
    }
    cm.dispose();
}