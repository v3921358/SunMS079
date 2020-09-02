/* Dawnveil
    Guild Emblem
	Lea
    Made by Daenerys
*/
var status = 0;
var sel;
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;

		if (status == 0)
			cm.sendSimple("你好，我是蕾雅，负责家族徽章的相关业务。\r\n#b#L0#创建/改变家族徽章#l#k");
		else if (status == 1)
		{
			sel = selection;
			if (selection == 0)
			{
				if (cm.getChar().getGuildRank() == 1)
					cm.sendYesNo("创建或者改变家族徽章需要#b500万金币#k,是否继续？");
				else
					cm.sendOk("你不是家族族长，因此你不能创建和改变家族徽章，请转告你的族长，让他来找我..");

			}
				
		}
		else if (status == 2)
		{
			if (sel == 0)
			{
				cm.genericGuildMessage(17);
				cm.dispose();
			}
		}
	}
}