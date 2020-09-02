importPackage(net.sf.Start.client);

var status = 0;
var fee;//自己下多少点卷选择
var chance = Math.floor(Math.random()*4+1);
var mingzi = "点卷博彩系统";
var zuidi = "100";//最低点卷
var zuididianjuan = 100;//最低点卷
var zuigao = "5万";//最高点卷
var zuigaodianjuan = 50000;//最高点卷

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.sendOk("欢迎下次光临,再见!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendYesNo("\t\t#e欢迎来到#r" + cm.getChannelServer().getServerName() + "#k-#d" + mingzi + "#n#k\r\n\r\n下注要求:最低" + zuidi + "点卷-------最高" + zuigao + "点卷");
        } else if (status == 1) {
            cm.sendGetText("你想下注多少点卷呢?如果赢了的话可以获得3倍的收益,呵呵,话不多说啦!快输入你想要博彩的点卷吧!输了别哭喔!");
        } else if (status == 2) {
			
            fee = cm.getText();
            cm.sendYesNo("你确定要下注 #r" + fee + "#k 点卷吗?请先检查你有没有那么多点卷哦!");
        } else if (status == 3) {
            if (cm.getPlayer().getCSPoints(1) < fee) {
                cm.sendOk("哦呵，不好意思你没那么多点卷了，去赚点点卷再来吧，这可不是免费的!");
                cm.dispose();
			} else if (cm.getPlayer().getCSPoints(1) < zuididianjuan) {
                cm.sendOk("对不起,您的账号不足#r" + zuidi + "点卷#k,所以不能参于此次下注");
                cm.dispose();
            } else if (cm.getText() < zuididianjuan) {
                cm.sendOk("对不起,最低下注:#r" + zuidi + "点卷#k,所以不能参于此次下注!");
                cm.dispose();
            } else if (cm.getText() > zuigaodianjuan) {
                cm.sendOk("对不起,最高下注:#r" + zuigao + "点卷#k,所以不能参于此次下注!");
                cm.dispose();
            } else {
                 if (chance <= 1) { 
	                    cm.gainD(-fee); 
	                    cm.sendNext("哦··你的运气不怎么好哦··哈哈 再来一把嘛"); 
                        cm.laba(cm.getPlayer().getName() + "『赌场公告』" + " : " + "在点卷赌场输了，大家安慰一下吧",9);
	                    cm.dispose(); 
	                } 
	                else if (chance == 2) { 
	                    cm.gainD(-fee); 
	                    cm.sendNext("哦··你的运气不怎么好哦··哈哈 再来一把嘛"); 
                        cm.laba(cm.getPlayer().getName() + "『赌场公告』" + " : " + "在点卷赌场输了，大家安慰一下吧",9);
	                    cm.dispose(); 
	                } 
	                else if (chance == 3) { 
	                    cm.gainD(-fee); 
	                    cm.sendNext("哦··你的运气不怎么好哦··哈哈 再来一把嘛"); 
                        cm.laba(cm.getPlayer().getName() + "『赌场公告』" + " : " + "在点卷赌场输了，大家安慰一下吧",9);
	                    cm.dispose(); 
	                } 
                else if (chance >= 4) {
                    cm.gainD(fee * 3);                   
                    cm.sendNext("#r哈哈，恭喜你#k! 你赢了! 获得3倍的点卷赔偿");
                    cm.laba(cm.getPlayer().getName() + "『赌场公告』" + " : " + "在点卷赌场赢得3倍的点卷，大家一起祝贺他吧",9);
                    cm.dispose();
                }
            }
        }
    }
}
