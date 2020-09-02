/* ==================
 脚本类型: NPC	    
 脚本版权：一线海冒险岛团队
 联系扣扣：297870163    609654666
 =====================
 */
 var status = -1;

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
        cm.sendOk("今天天气真好啊?");
	cm.dispose();
    }
}
