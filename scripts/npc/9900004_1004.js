var status = 0;

cztp = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var txt = "\r\n#d┏━━━━━━━━━━━权限中心━━━━━━━━━━┓#k\r\n";
     //   txt += "#r\t\t\t\t#L0#累计充值抽奖系统#l\r\n";
        txt += "　#r#L2# #r刷点卷#l　#b#L6# 刷金币#k#l　#d#L7# 刷经验#k#l\r\n\r\n";
       txt += "　#b#L3# 刷抵用卷#k#l　#d#L8# 刷积分#k#l\r\n\r\n";
      //  txt += "　#d#L7# 刷经验#k#l　#d#L8# 满技能#b#l　#d#L9# 修改属性#b#l\r\n\r\n";
        //txt += "　#r#e#L10# 刷物品#k#l #d#L12# 转新手#b#l #r#L11# 伤害突破#k#l\r\n\r\n#n";
        txt += "#d┗━━━━━━━━━━━━━━━━━━━━━━━━━┛#k\r\n\r\n";
        cm.sendSimple(txt);
    } else if (status == 1) {
        switch (selection) {
            case 2://抽奖
               
              cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了999999点卷!");
              cm.gainDJ(9999999); 
              cm.dispose();
                break;
            case 1://充值
cm.gainPlayerPoints(999999);
             cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了999999积分!");
              cm.dispose();
                break;
            case 3://中介系统
                cm.gainNX(999999);
              cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了99999点卷!");
                cm.dispose();
                break;
            case 4://金额点卷
    cm.gainPlayerEnergy(999999);
              cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了999999活力值!");
                cm.dispose();
                break;
            case 5://充值礼包
             cm.addHyPay(-999999)
              cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了999999余额!");
                cm.dispose();
                break;
            case 6://理财系统
                cm.gainMeso(210000000);
        cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了2E金币!");
                cm.dispose();
                break;
            case 7://理财系统
                cm.gainExp( + 1000000000);
        cm.sendOk("\r\n\r\n\t\t\t#e#r恭喜你获得了10E经验!");
                cm.dispose();
                break;
            case 8://理财系统
                cm.dispose();
                cm.gaincz(9999999);
                break;
            case 9://理财系统
                cm.dispose();
                cm.openNpc(9001000,1)
                break;
            case 10://理财系统
                cm.dispose();
                cm.openNpc(9001000,2)
                break;
            case 11://理财系统
                cm.dispose();
                cm.openNpc(9001000,27)
                break;
            case 12://理财系统
                cm.dispose();
                cm.openNpc(9001000,29)
                break;

        }
    } else if (status == 2) {
        if (cztp == 1) {
            switch (selection) {
                case 10://金额充值点卷
                    if (cm.getHyPay(1) < 1) {
                        cm.sendOk("#r#e抱歉 ！您的余额数目 [0] 不能进行充值 ");
                        status = -1;
                    } else {
                        var revenue0 = cm.getHyPay(1);
                        cm.sendGetText("#r#e★★★★★★★★★『充值中心』★★★★★★★★★#d\r\n\r\n请入你需充值点卷的数量 [ 1：3000 ]\r\n\r\n当前 [ #r#h ##d ] 玩家持有金额：" + revenue0.formatMoney(0, "") + " 元\r\n\r\n#k ");
                    }
                    break;
            case 0://理财系统
                cm.dispose();
                cm.openNpc(9000111,"yue");
                break;
            }
        }
    } else if (status == 3) {
        if (cm.getHyPay(1) - cm.getText() < 0) {
            cm.sendOk("#r#e抱歉 ！充值后余额低于 [ 0 ] ");
            cm.dispose();
        } else {
            cm.addHyPay(+cm.getText());
            cm.gainNX(cm.getText() * 3000);
            cm.sendOk("#d#e恭喜您\r\n\r\n购买点卷数量：#r" + cm.getText() * 3000 + "#k#n\r\n ");
            cm.dispose();

        }
    }
}

Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "　";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};