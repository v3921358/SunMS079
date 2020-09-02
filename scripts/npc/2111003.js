/* Author: aaroncsn(MapleSea Like)(Incomplete)
	NPC Name: 		Humanoid A
	Map(s): 		Sunset Road: Magatia(2610000000)
	Description: 		Unknown
*/

function start(){
	if (cm.isQuestActive(3335)) {
	    cm.sendNext("任务完成.");
	    cm.forceCompleteQuest(3335);
	} else {
	    cm.sendNext("我想成为一个人类，一个有着温暖的心的人……所以也许我可以握着她的手。不幸的是，现在是不可能的.");
	}
	cm.dispose();
}