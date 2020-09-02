var status; 

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == 1) { 
        status++; 
    }else{ 
        status--; 
    } 
    if (status == 0) { 
    if (cm.getPlayer().getjob() == 0) { 
        cm.sendNext("欢迎光临，开始浏览器！在冒险岛，你可以\r\n选择一个#r职业#k 当你达到 #rLv 10#k (Lv 8 对于魔术师).\r\n\r\n换句话说，你会选择自己的未来之路!\r\n当你得到一个 职业,你能使用各种技能和魔法，这将让你在冒险岛更enjoyable.So经验，努力开拓自己的命运"); 
    } else { 
        cm.sendOk("看起来你已经做了职业进步!\r\n运输只能由初学者使用"); 
        cm.dispose(); 
    } 
    } else if (status == 1) { 
        cm.sendNextPrev("我的角色是帮助你成为一个#r魔法师.#k\r\n\r\n魔法师具有强大的短远程攻击和高强度，从而使他们就是总是在战斗的最前沿。 它是一个职业 类，与每一个有效的基本攻击技能开始，并导致了更大的力量再一次高级技能的路径被收购."); 
    } else if (status == 2) { 
        cm.sendNextPrev("使用的武器包括： #b长杖#k, #b短杖#k, #b布兰妮#k 和 #b长棍#k.\r\n\r\n需要等级: #r过度Lv 10#k\r\n位置: #r魔法师的保护区#k在#bPerion#k\r\n职业讲师: #r与炎魔共舞#k"); 
    } else if (status == 3) { 
        cm.sendSimple("你想成为一个#r魔法师?#k\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (status == 4) { 
      if (selection == 0) { 
        cm.sendSimple("为了使 职业 进步，你必须访问#r与炎魔共舞#k 在#r魔法师的保护区#k在#bPerion#k.你想现在就在那里运？不能用-The运输服务，一旦你做 职业 进步-\r\n\r\n#b#L0#Yes#l\r\n#L1#No#l#k"); 
    } else if (selection == 1) { 
        cm.sendNext("如果您有任何疑问，请再和我说话。"); 
        cm.dispose(); 
    } 
    } else if (status == 5) { 
      if (selection == 0) { 
        cm.sendNext("Alright.I现在带你去#r魔法师的保护区#k在#bPerion.#k"); 
    } else if (selection == 1) { 
        cm.sendNext("如果您有任何疑问，请再次跟我说话."); 
        cm.dispose(); 
    } 
  } else if (status == 6) { 
    cm.warp(101000003, 10); 
	cm.dispose();
  } 
}  