/* RED 1st impact
    Scarf Snowman
    Made by Daenerys
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else 
        if (status == 0) {
		    cm.sendNext("你需要更多的时间装饰圣诞树，是吗?如果你想离开这个地方，可以来和我说话!");
            cm.dispose();
        status--;
    }
    if (status == 0) {
		cm.sendYesNo("你把你的树装饰得很好吗?这是一个有趣的体验，至少可以说，装饰一棵圣诞树的创新。你不认为这很有趣吗?哦，是的…你想离开这个地方?");
	} else if (status == 1) {	   	
		cm.warp(209000000,4);
		cm.dispose();
	}
}