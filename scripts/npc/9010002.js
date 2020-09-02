function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	cm.sendOk("你好，我是米娅.");
	cm.safeDispose();
}