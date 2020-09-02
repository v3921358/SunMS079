/* 
 * @Author Lerk
 * 
 * Bulletin Board, Victoria Road: Excavation Site<Camp> (101030104) AND Sharenian: Excavation Site (990000000)
 * 
 * Start of Guild Quest
 */


function action(mode, type, selection) {
    cm.sendOk("你是一个有足够多的勇气和信任的家族的一部分吗？然后，对家族的探索和挑战自己!\r\n\r\n#b参与 :#k\r\n1.家族的队伍必须至少6人!\r\n2.家族任务的队长必须是族长或是副族长!\r\n3.如果家族成员的人数下降到6以下，或如果队长决定提前结束，则家族任务可能会提前结束!");
    cm.safeDispose();
}