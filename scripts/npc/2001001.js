/*
 *  Branch Bucket Snowman - Happy Ville NPC
 */

function start() {
    cm.sendSimple("你好,我是#p2001001#. 你可以通过我进入有幽静的树的房间！ 有关更多信息，请咨询#b#p2001000##k. 你会进入哪个房间? \n\r #b#L0#房间里有第一棵树#l \n\r #L1#房间里有第二棵树#l \n\r #L2#房间里有第三棵树#l \n\r #L3#房间里有第四棵树#l \n\r #L4#房间里有第五棵树#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(209000001 + selection, 0);
    }
    cm.dispose();
}
