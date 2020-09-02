/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Wiz the Librarian - Helios Tower <Library>(222020000)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
---------------------------------------------------------------------------------------------------
**/

var status = 0;
var questid = new Array(3615,3616,3617,3618,3630,3633,3639);
var questitem = new Array(4031235,4031236,4031237,4031238,4031270,4031280,4031298);
var i;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	var counter = 0;
	var books = "";

	for (var i = 0; i < questid.length; i++) {
	    if (cm.getQuestStatus(questid[i]) == 2) {
		counter++;
		books += "\r\n#v"+questitem[i]+"# #b#t"+questitem[i]+"##k";
	    }
	}
	if(counter == 0) {
	    cm.sendOk("#b#h ##k 没有返回一个单一的故事吗.");
	    cm.safeDispose();
	} else {
	    cm.sendNext("让我想想。.. #b#h ##k已经归还了 #b"+counter+"#k 书.返回的书籍列表如下:"+books);
	}
    } else if (status == 1) {
	cm.sendNextPrev("图书馆现在稳定下来了，主要是感谢你, #b#h ##k巨大的帮助。如果这个故事再次被混淆，那么我将指望你再解决它一次.");
    } else if (status == 2) {
	cm.dispose();
    }
}