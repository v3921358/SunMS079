/* Author: aaroncsn(MapleSea Like)
	NPC Name: 		Assistant Cheng
	Map(s): 		Ludibrium: Toy Factory Zone 1(220020000)
	Description: 		Unknown
*/

function start(){
	if (cm.isQuestActive(3239)==1 && cm.haveItem(4031092,10)) {
	cm.removeAll(4031092);
	cm.forceCompleteQuest(3239); //完成任务
	cm.warp(922000009);
	cm.dispose();
	}else if (cm.isQuestFinished(3239)) {
	cm.sendNext("你已经完成了#r修理配件的下落#k任务");
	cm.dispose();
	}else if (!cm.isQuestActive(3239)==1) {
	cm.sendNext("你没有#r修理配件的下落#k相关任务,所以我不发送过进去");
	cm.dispose();
	}else{
    cm.warp(922000000);
    cm.dispose();
                    }
	}