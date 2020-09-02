/* ==================
 脚本类型: NPC	    
 脚本版权：游戏盒团队
 联系扣扣：297870163    609654666
 =====================
 */
var status = 0;
var skin = Array(0, 1, 2, 3, 4);
var price;

function start() {
    cm.sendSimple("欢迎来到射手村护肤中心!是否有想要染的皮肤呢?? 就像我的健康皮肤??如果你有#b#t5153000##k,就可以随意染的想到的皮肤~~~\r\n\#L2#使用护肤高级会员卡!#l");
}

function action(mode, type, selection) {
    if (mode < 1)
        cm.dispose();
    else {
        status++;
        if (status == 1)
            cm.sendStyle("选一个想要的风格.",skin);
        else {
            if (cm.haveItem(5153000)){
                cm.gainItem(5153000, -1);
                cm.setSkin(selection);
                cm.sendOk("享受你的新皮肤!");
            } else
                cm.sendOk("您貌似没有#b#t5153000##k..");
            cm.dispose();
        }
    }
}
