/* Nana(0)
	Fame Seller
	by 爱上冒险岛
*/

var wui = 0;
var price = 500000;
var fame = 1
var qty;


function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, selection) {
	if (mode == 1)
		status++;
	else 
		cm.dispose();
if (status == 0 && mode == 1) {
cm.sendYesNo("#d你需要换人气吗？我可以给你加，不过要#r50W#d一点，哈哈！ #fUI/UIWindow.img/QuestIcon/6/0# ");
}
else if (status == 1 && mode == 1) {
		var prompt = "#b你想换多少人气?";
		cm.sendGetNumber(prompt,1,1,100)
}
else if (status == 2 && mode == 1) {
qty = selection;
cm.sendYesNo("#b你将花费#r"+qty*price+"#b金币兑换#r"+qty+"#b点人气,你确定要兑换？");
}
else if (status == 3 && mode == 1) {
if (cm.getMeso() >= price) 
{
	cm.gainFame(+fame*qty);
	cm.gainMeso(-price*qty);
	var say = "#b成功兑换 " +qty+ "点人气?";
	cm.sendOk(say);
	cm.dispose();
	} else {
			cm.sendOk("对不起，你金币不足.");
			cm.dispose();
}
}
else
	cm.dispose();
}

