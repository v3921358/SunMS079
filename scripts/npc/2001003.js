/* RED 1st impact
    Straw Hat Snowman
    Made by Daenerys
*/

function start() {
    cm.sendSimple("你好我是#p2001003#. 你想进入那个房间，要了解更多信息，可以与#b#p2001000##k对话. 你要进入哪个房间? \n\r #b#L0#第一个房间#l \n\r #L1#第二个房间#l \n\r #L2#第三个房间#l \n\r #L3#第四个房间#l \n\r #L4#第五个房间#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(209000011 + selection, 0);
    }
    cm.dispose();
}
