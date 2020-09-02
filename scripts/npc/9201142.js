var status;
var selected;
var selected2;
var foodExchange = [
[2022623, [[4000522, 1], [4000001, 5]], [[4000522, 1], [4000015, 2]], ["Kekeke... Wise Choice. This dish is made using #r1 Frog Egg and 5 caps of Orange Mushrooms #keasily found around town.", "Kekeke... Wise Choice. This dish is made using #r1 Frog Egg and 2 caps of Horny Mushrooms #keasily found around town."], "This dish is a delicious one made of Frog Eggs and seasoned Mushrooms. Doesn't that just make your mouth water? Kekeke....\r\n#b#L0#Please make it using #rOrange Mushroom Caps.#b#l\r\n#L1#Please make it using#r Horny Mushroom Caps#k#l#k"],
[2022624, [[4000523, 1], [4000012, 5]], [[4000523, 1], [4000009, 2]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r1 Cursed Cat Spittle and 5 caps from Green Mushrooms#k found easily around town.", "Kekeke... Wise Choice. The ingredients for this recipe are #r1 Cursed Cat Spittle and 2 caps from Blue Mushrooms #kfound easily around town."], "This dish is an aromatic Bloody Mushroom Wine made with delicious Frog Eggs. Your mouth is watering now, isn't it? Kekeke...\r\n#b#L0#Please make it using #rGreen Mushroom Caps#b.#l\r\n#L1#Please make it using #rBlue Mushroom Caps#k.#l#k"],
[2022625, [[2022623, 1], [4000006, 5]], [[2022623, 1], [4000013, 2]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r1 Seasoned Frog Eggs and Mushrooms #kthat I made and #r5 Octopus Legs #kmost often found in Kerning City.", "Kekeke... Wise Choice. The ingredients for this recipe are #r1 Seasoned Frog Eggs and Mushrooms#k that I made and #r2 Curse Eye Tails#k most often found deep in the scary forest."], "This dish is one that is never missing at Witch Parties: the Slimy Canape! Is that your stomach grumbling that I hear? Kekeke....\r\n#b#L0#Please make it using #rOctopus Legs.#b#l\r\n#L1#Please make it using #rCurse Eye Tails.#k#l#k"],
[2022626, [[2022624, 1], [4000042, 5]], [[2022624, 1], [4000007, 2]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r1 Bloody Mushroom Wine#k that I made and #r5 Stirge Wings #kmost often found in the Subway.", "Kekeke... Wise Choice. The ingredients for this recipe are #r1 Bloody Mushroom Wine#k that I made and #r2 Evil Eye Tails#k most often found deep in the scary forest."], "This dish is one that comes to mind, especially on rainy days: the Zingy Kabab. I know it sounds delectable, but you don't have to drool! Kekeke....\r\n#b#L0#Please make it using #rStirge Wings.#b #l\r\n#L1#Please make it using #rEvil Eye Tails#k.#l#k"],
[2022627, [[2022623, 3], [4000032, 5]], [[2022623, 3], [4000440, 2]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r3 Seasoned Frog Eggs and Mushrooms #kthat I made and #r5 Ligator Skins#k usually found in the swamp area.", "Kekeke... Wise Choice. The ingredients for this recipe are #r3 Seasoned Frog Eggs and Mushrooms #kthat I made and#r 2 Tough Leather#k."], "This dish is the Swamp Wraps, a meal that will allow you to experience the aromatic smell of my home in the Swamps. Doesn't that really make your mouth water? Kekeke... \r\n#b#L0#Please make it using #rLigator Skins#b #l\r\n#L1#Please make it using #rTough Leather#k.#l#k"],
[2022628, [[2022624, 3], [4000155, 5]], [[2022624, 3], [4000033, 2]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r3 Bloody Mushroom Wines #kthat I made and #r5 Seal Skins #kfound in Aqua Road.", "Kekeke... Wise Choice. The ingredients for this recipe are #r3 Bloody Mushroom Wines#k that I made and #r2 Croco Skins#k found in the swamp. "], "This is my best dish... all I have to do is grill it. It's known as Rough Leather Steak! Chew it if you can! Ha! Can you taste all those colorful flavors on your tongue? Kekeke...\r\n#b#L0#Please make it using #rSeal Skins#b.#l\r\n#L1#Please make it using #rCroco Skins.#l#k"],
[2022629, [[2022627, 1], [2022628, 1]], [[2022625, 3], [2022626, 3]], ["Kekeke... Wise Choice. The ingredients for this recipe are #r1 Swamp Wrap and 1 Rough Leather Steak.#k", "Kekeke... Wise Choice. The ingredients for this recipe are #r3 Slimy Canapes and 3 Zingy Kebabs.#k"], "You want to eat the Witch's Special Stew? It won't be easy to find the ingredients! The reason is that this stew requires using some of the dishes that I put together. It's really tasty! Keke... Which ingredient should I use? \r\n#b#L0#A steak stew full of the swamp's aroma would be good.#l\r\n#L1#Please make it taste sharp and slimy.#l#k"]
];
var tokenExchange = [
[4000522, 1],
[4000523, 1],
[1003027, 30],
[1302131, 50],
[2049113, 20],
[1132014, 80],
[1132015, 100],
[1132016, 150],
[2049114, 100]
];

function start() {
    status = 0;
    cm.sendSimple("What do you want little kid?\r\n#b#L0#Please make me some Witch food.#l\r\n#L1#What is a Dark Token?#l\r\n#L2#I've brought the Dark Token.#l#k");
}

function action(mode, type, selection) {
    if (mode != 1) {
        if (selected == 2 && mode == -1) {
            cm.sendOk("It's so hard to figure out what people are thinking. Talk to me again once you decide what you want to exchange with the Dark Tokens.");
        }
        cm.dispose();
        return;
    }
    status++;
    switch (status) {
        case 1:
            selected = selection;
            if (selected == 0) {
                cm.sendNext("Hey you! Have you seen my frogs and cats? The frogs and cats I've trained using black magic have all ran away.\r\nThey ruined my plans to turn this town into a beautiful Witch town! \r\nI'm here in secret, so I really don't have much time, but if you can bring me ingredients from those putrid frogs and cats, I'll make you some Witch food. \r\nWouldn't that just make your day? Ha!");
            } else if (selected == 1) {
                cm.sendNext("You want to know about the Dark Tokens? Ha! Do you think I'd just tell you without making you work for it? Actually, it's related to the greatest secret mission that can only be completed in Maple World. There are many sects in the Witch World; I just happen to serve the Great Baba Yaga. My mission is to gather the most Dark Tokens in the fastest amount of time compared to those in other sects so that Baba Yaga will be happy.");
            } else if (selected == 2) {
                cm.sendSimple("Oh so you have " + cm.itemQuantity(4000524) + " Dark Tokens. Go ahead and choose what you would like. \r\n#b#L0#Cursed Frog Egg: 1 tokens#l\r\n#L1#Cursed Cat Spittle: 1 tokens#l\r\n#L2#Talking Witch Hat: 30 tokens#l\r\n#L3#Broomstick: 50 tokens#l\r\n#L4#Witch Scroll: 20 tokens#l\r\n#L5#Witch's Crimson Belt: 80 tokens#l\r\n#L6# Witch's Ocean Blue Belt: 100 tokens#l\r\n#L7# Witch's Deep Purple Belt: 150 tokens#l\r\n#L8# Witch's Belt Scroll: 100 tokens#l\r\n#L9#I want to see the exchange item list.#l#k");
            }
            break;
        case 2:
            if (selected == 0) {
                cm.sendSimple("I'm glad to hear that you want to eat some of my food. Just tell me which dish you would like.\r\n#b#L0##i2022623:# #t2022623##l\r\n#L1##i2022624:# #t2022624##l\r\n#L2##i2022625:# #t2022625##l\r\n#L3##i2022626:# #t2022626##l\r\n#L4##i2022627:# #t2022627##l\r\n#L5##i2022628:# #t2022628##l\r\n#L6##i2022629:# #t2022629##l\r\n\r\n#L7#I don't want to eat anything that a Witch made!#l#k");
            } else if (selected == 1) {
                cm.sendNextPrev("There's no need for you to know what we do with the Dark Tokens. The most important thing is that I don't have the time to gather all the Dark Tokens that are spread all throughout the land. Therefore, I need adventurers like yourself to collect them for me. I, of course, will repay you for your effort.");
            } else if (selected == 2) {
                if (selection == 9) {
                    cm.sendNext("Dark Token Exchange List:\r\n#i4000522:# #t4000522#: 1 tokens\r\n#i4000523:# #t4000523#: 1 tokens\r\n#i1003027:# #t1003027#: 30 tokens\r\n#i1302131:# #t1302131#: 50 tokens\r\n#i2049113:# #t2049113#: 20 tokens\r\n#i1132014:# #t1132014#: 80 tokens\r\n#i1132015:# #t1132015#: 100 tokens\r\n#i1132016:# #t1132016#: 150 tokens\r\n#i2049114:# #t2049114#: 100 tokens");
                    cm.dispose();
                    return;
                }
                selected2 = selection;
                cm.sendYesNo("Do you want to trade your #b" + tokenExchange[selection][1] + " #t4000524#s#k with my #t" + tokenExchange[selection][0] + "#? \r\n\r\n#i" + tokenExchange[selection][0] + ":#  #t" + tokenExchange[selection][0] + "#");
            }
            break;
        case 3:
            if (selected == 0) {
                selected2 = selection;
                if (selected2 == 7) {
                    cm.sendOk("What did you say? You rude, spoiled little brat... I should make you eat it by force!");
                    cm.dispose();
                    return;
                }
                cm.sendSimple(foodExchange[selected2][4]);
            } else if (selected == 1) {
                cm.sendPrev("Dark Token Exchange List:\r\n#i4000522:# #t4000522#: 1 tokens\r\n#i4000523:# #t4000523#: 1 tokens\r\n#i1003027:# #t1003027#: 30 tokens\r\n#i1302131:# #t1302131#: 50 tokens\r\n#i2049113:# #t2049113#: 20 tokens\r\n#i1132014:# #t1132014#: 80 tokens\r\n#i1132015:# #t1132015#: 100 tokens\r\n#i1132016:# #t1132016#: 150 tokens\r\n#i2049114:# #t2049114#: 100 tokens");
                cm.dispose();
            } else if (selected == 2) {
                if (!cm.haveItem(4000524, tokenExchange[selected2][1])) {
                    cm.sendNext("You don't have enough Dark Tokens or space in your inven. Empty a slot each on your Equip, Use, and Etc. inventory window.");
                    cm.dispose();
                    return;
                }
                cm.gainItem(4000524, -tokenExchange[selected2][1]);
                cm.gainItem(tokenExchange[selected2][0], 1);
                cm.dispose();
            }
            break;
        case 4:
            if (selected == 0) {
                if (!cm.haveItem(foodExchange[selected2][selection + 1][0][0], foodExchange[selected2][selection + 1][0][1]) ||
                    !cm.haveItem(foodExchange[selected2][selection + 1][1][0], foodExchange[selected2][selection + 1][1][1])) {
                    cm.sendNext(foodExchange[selected2][3][selection]);
                    return;
                }
                cm.gainItem(foodExchange[selected2][selection + 1][0][0], -foodExchange[selected2][selection + 1][0][1]);
                cm.gainItem(foodExchange[selected2][selection + 1][1][0], -foodExchange[selected2][selection + 1][1][1]);
                cm.gainItem(foodExchange[selected2][0], 1);
                cm.sendNext("Oh good, you've brought all of the ingredients. Now, shall we start the cooking? Katool-katool-ewww-kitool-kittol-come-now-ugh-stomach....spit-spat-ptui! Kekeke....there we go, it's finished! Take it. Oh, it's not as good as what Mama Witch used to make, but it's still full of my special Magic.' Kekeke...");
                cm.dispose();
            }
            break;
        case 5:
            if (selected == 0) {
                cm.sendNextPrev("As I've told you already, frogs come to the towns once in awhile in order to steal food. Although Cursed Frogs usually look like normal frogs, don't be tricked. Their true selves are quite wicked, so you should be careful. I can't guarantee that you'll be able to handle them. Kekeke.");
            }
            break;
        case 6:
            if (selected == 0) {
                cm.sendPrev("Come back to me when you've gathered all of the ingredients.");
                cm.dispose();
            }
            break;
    }
}