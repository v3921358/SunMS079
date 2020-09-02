/* ==================
 脚本类型:  任务	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */function start(a,b,c){qm.dispose()}function end(a,b,c){0==qm.getQuestStatus(50012)?qm.forceStartQuest():(qm.forceCompleteQuest(50015),qm.forceCompleteQuest());qm.dispose()};