importPackage(Packages.tools.packet);

var status = 12;

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    mode == 1 ? status++ : status--;
    if (status == 13) {
        cm.sendNextS("Thank you all for coming.", 5, 1402400);
    } else if (status == 14)
        cm.sendNextPrevS("...", 5, 1402100);
    else if (status == 15)
        cm.sendNextPrevS("We're here to prove that you're lying.", 5, 1402101);
    else if (status == 16)
        cm.sendNextPrevS("So you would like to think, but i'd like to talk about an old tale that the people of Ereve seem to have conveniently forgotten. A tale of Aria, the empress during the time of the Black Mage's reign...", 5, 1402400);
    else if (status == 17)
        cm.sendNextPrevS("(Aria...)", 17);
    else if (status == 18)
        cm.sendNextPrevS("As you all must know, there was no historical record left over after the Black Mage rose to power, but there WAS a persistent story of a gem that Aria clung to. The treasure called Skaia.", 5, 1402400);
    else if (status == 19)
        cm.sendNextPrevS("Skaia, Aria's lost treasure. It's said to have been passed down from empress to empress to protect them! It holds many wondrous powers!", 5, 1402400);
    else if (status == 20)
        cm.sendNextPrevS("The stories about Aria's possession of Skaia are not in question, but there are no records of what powers the jewel actually possessed.", 5, 1402104);
    else if (status == 21)
        cm.sendNextPrevS("I stand before you with proof of my lineage and you argue semantics? I have the jewel, not Cygnus!", 5, 1402400);
    else if (status == 22)
        cm.sendNextPrevS("When the Black Mage's army decimated Ereve, the Skaia was thought lost. I'm sure that's the old story you've all heard. But do you all really think such an important treasure would be written off as a loss? Do you think our forefathers would have let it linger in some tomb?", 5, 1402400);
    else if (status == 23)
        cm.sendNextPrevS("That is lunacy! The Skaia was protected from the Black Mage, quietly passed down for hundreds of years untill I could reveal my birthright!", 5, 1402400);
    else if (status == 24)
        cm.sendNextPrevS("So that's your argument?", 5, 1402105);
    else if (status == 25)
        cm.sendNextPrevS("That is the truth.", 5, 1402400);
    else if (status == 26)
        cm.sendNextPrevS("How can you prove that the Skaia you have is real? It could be a fake.", 5, 1402103);
    else if (status == 27)
        cm.sendNextPrevS("A valid question, but ultimately foolish. The name Skaia is well known, but very few actually seen it. In fact, the only people in Maple World that would have even seen its picture are here today. That means all of YOU are the proof that my Skaia is real!", 5, 1402400);
    else if (status == 28)
        cm.sendNextPrevS("Do you not recognize the Skaia in my hand? Is it not the jewel you have all seen before?", 5, 1402400);
    else if (status == 29)
        cm.sendNextPrevS("Listen to what you're saying! Jewels can be forged and reproduced. There is no way we can be certain that the one you have is the real thing.", 5, 1402106);
    else if (status == 30)
        cm.sendNextPrevS("I'm sorry, Sir Hawkeye, but were you alive hundreds of years ago? No. Your opinion the validity of this gem is of no importance.", 5, 1402400);
    else if (status == 31)
        cm.sendNextPrevS("Besides, we have not yet gotten to the real argument. I ask you all, why is the Lady Cygnus so frail? If she is indeed the true heir, she would not be overpowered by Shinsoo's strength. Lady Cygnus, you yourself must know that you were not meant to wield the power you've stolen.", 5, 1402400);
    else if (status == 32)
        cm.sendNextPrevS("Such insolence!", 5, 1402102);
    else if (status == 33)
        cm.sendNextPrevS("Oh... was I being insolent? Is speaking the truth now a sign of betrayal?", 5, 1402400);
    else if (status == 34)
        cm.sendNextPrevS("Think on my words and decide for yourselves! Isn't that your role, Cygnus? To act on what is best for the people?", 5, 1402400);
    else if (status == 35)
        cm.sendNextPrevS("She is right, I am no one special and I am unable to fully absorb Shinsoo's power. I don't know why, but I was born like this.", 5, 1402100);
    else if (status == 36)
        cm.sendNextPrevS("If this woman questions my validity as your empress, we must at leasst allow her to discuss it, or we will be no better than the Black Mage...", 5, 1402100);
    else if (status == 37)
        cm.sendNextPrevS("Lady Cygnus!", 5, 1402101);
    else if (status == 38)
        cm.sendNextPrevS("I have to do what is right! I've asked the world to fight for me while I sit here under the protection of more people I've asked to fight for me. If I've done that without any real authority or qualification...", 5, 1402100);
    else if (status == 39)
        cm.sendNextPrevS("Then I am no better than a tyrant.", 5, 1402100);
    else if (status == 40)
        cm.sendNextPrevS("(Her voice is shaking like a leaf, but her eyes are firm. She really is Aria's niece... and she seems to be quite popular.)", 17);
    else if (status == 41)
        cm.sendNextPrevS("I believe we've dragged this on long enough! Let's see who truly carries the bloodline of the empress. It is said that Skaia will shine in the hands of an Empress. Do you care to test your mettle, little Cygnus?", 5, 1402400);
    else if (status == 42)
        cm.sendNextPrevS("It shines in my hands. Will it shine in yours?", 5, 1402400);
    else if (status == 43) {
        cm.dispose();
        cm.showSkaia();
    }
}