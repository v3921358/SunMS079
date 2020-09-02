/*
 * Job Advancer
 * @author Aristocat
 */


var job = [
[[100, "Warrior"], [200, "Magician"], [300, "Bowman"], [400, "Thief"], [500, "Pirate"]],
[[1100, "Dawn Warrior"], [1200, "Blaze Wizard"], [1300, "Wind Archer"], [1400, "Night Walker"], [1500, "Thunder Breaker"]],
[[3200, "Battle Mage"], [3300, "Wild Hunter"], [3500, "Mechanic"]],
[[110, "Fighter"], [120, "Page"], [130, "Spearman"]],
[[210, "Wizard (F/P)"], [220, "Wizard (I/L)"], [230, "Cleric"]],
[[310, "Hunter"], [320, "Crossbow Man"]],
[[410, "Assassin"], [420, "Bandit"]],
[[510, "Brawler"], [520, "Gunslinger"]]];
var extrajobs = [
[2300, "Mercedes"], [3100, "Demon Slayer"]
];
var specialextrajobs = [
[9400, "Dual Blade"], [9501, "Cannoneer"], [9508, "Jett"]
];
var extra = true;
var status = -1;
var select;
var tempest = true; //V123+
var jobindex;

function start() {
    //gainImperialSet();
    jobindex = null;
    select = null;
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 || mode == -1 && status == 0) {
        cm.dispose();
        return;
    } else
        (mode == 1 ? status++ : status--);
    if (status == 0) {
        if ((cm.getPlayer().getLevel() >= 10 &&
            (cm.getPlayer().getJob() % 1000 == 0 || cm.getPlayer().getJob() == 501 || cm.getPlayer().getJob() == 3001 || cm.getPlayer().getJob() >= 2001 && cm.getPlayer().getJob() <= 2003) ||
            cm.getPlayer().getLevel() >= 30 && (cm.getPlayer().getJob() % 1000 > 0 && cm.getPlayer().getJob() % 100 == 0 ||
                cm.getPlayer().getJob() == 508) ||
            cm.getPlayer().getLevel() >= (tempest ? 60 : 70) && cm.getPlayer().getJob() % 10 == 0 && cm.getPlayer().getJob() % 100 != 0 ||
            cm.getPlayer().getLevel() >= (tempest ? 100 : 120) && cm.getPlayer().getJob() % 10 == 1 ||
            cm.getPlayer().getLevel() >= 20 && cm.getPlayer().getJob() == 400 && cm.getPlayer().getSubcategory() == 1 ||
            cm.getPlayer().getLevel() >= 30 && cm.getPlayer().getJob() == 430 || cm.getPlayer().getLevel() >= (tempest ? 45 : 55) && cm.getPlayer().getJob() == 431 || cm.getPlayer().getLevel() >= (tempest ? 60 : 70) && cm.getPlayer().getJob() == 432 || cm.getPlayer().getLevel() >= (tempest ? 100 : 120) && cm.getPlayer().getJob() == 433) &&
        (cm.getPlayer().getJob() % 10 != 2 && cm.getPlayer().getJob() % 10 != 4 || cm.getPlayer().getJob() == 432)) {
            if (isExplorer(cm.getPlayer().getJob())) {
               // if (cm.getPlayer().getRemainingSp() > 0) {
                 //   cm.sendOk("You have to use all of your SP points in order to job advance.");
                   // cm.dispose();
                   // return;
               // }
            }
            cm.sendYesNo("Would you like to job advance?");
        } else {
            cm.sendOk("You may not advance at the current state.");
            cm.dispose();
        }
    } else if (status == 1) {
        if (cm.getPlayer().getSubcategory() == 1 && cm.getPlayer().getJob() == 0) { //Dual Blade
            cm.getPlayer().changeJob(400);
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getSubcategory() == 1 && cm.getPlayer().getJob() == 400) { //Dual Blade
            cm.getPlayer().changeJob(430);
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getSubcategory() == 10 && cm.getPlayer().getJob() == 0) { //Jett
            cm.getPlayer().changeJob(508);
            cm.getPlayer().forceChangeChannel(cm.getPlayer().getClient().getChannel());
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getSubcategory() == 2 && cm.getPlayer().getJob() == 0) { //Cannoneer
            cm.getPlayer().changeJob(501);
            cm.dispose();
            return;
        }
        switch (cm.getPlayer().getJob()) {
            //Jobs with selections
            case 0: // Beginner
                jobSelection(0);
                break;
            case 1000: // Noblesse
                jobSelection(1);
                break;
            //Note: Heroes doesn't get job selection, the same goes about Nova.
            case 3000: // Citizen
                jobSelection(2);
                break;
            case 100: // Warrior
                jobSelection(3);
                break;
            case 200: // Magician
                jobSelection(4);
                break;
            case 300: // Bowman
                jobSelection(5);
                break;
            case 400: // Thief
                jobSelection(6);
                break;
            case 500: // Pirate
                jobSelection(7);
                break;
            //Special Jobs
            case 501: // Pirate(Cannoneer)
                cm.getPlayer().changeJob(530);
                cm.dispose();
                return;
            case 508: // Jett
                cm.getPlayer().changeJob(570);
                cm.dispose();
                return;
            case 2000: // Legend(Aran)
                cm.getPlayer().changeJob(2100);
                cm.dispose();
                return;
            case 2001: // Farmer(Evan)
                cm.getPlayer().changeJob(2200);
                cm.dispose();
                return;
            case 2002: // Mercedes
                cm.getPlayer().changeJob(2300);
                cm.dispose();
                return;
            case 2003: // Phantom
                cm.getPlayer().changeJob(2400);
                cm.dispose();
                return;
            case 2004: // Luminous
                cm.getPlayer().changeJob(2700);
                cm.dispose();
                return;
            case 3001: // Demon Slayer
                cm.getPlayer().changeJob(3100);
                cm.dispose();
                return;
            case 5000: // Nameless Warden (Mihile)
                cm.getPlayer().changeJob(5100);
                cm.dispose();
                return;
            case 6000: // Kaiser
                cm.getPlayer().changeJob(6100);
                cm.dispose();
                return;
            case 6001: // Angelic Burster
                cm.getPlayer().changeJob(6500);
                cm.dispose();
                return;
            // Dual Blader
            case 430: // Blade Reqruit
            case 431: // Blade Acolyte
            case 432: // Blade Specialist
            case 433: // Blade Lord
                cm.getPlayer().changeJob(cm.getPlayer().getJob() + 1);
                cm.dispose();
                return;
            //1st Job
            case 900:  // GM lol
            case 1100: // Dawn Warrior
            case 1200: // Blaze Wizard
            case 1300: // Wind Archer
            case 1400: // Night Walker
            case 1500: // Thunder Breaker
            case 2100: // Aran
            case 2300: // Mercedes
            case 2400: // Phantom
            case 2700: // Luminous
            case 3100: // Demon Slayer
            case 3200: // Battle Mage
            case 3300: // Wild Hunter
            case 3500: // Mechanic
            case 5100: // Mihile
            case 6100: // Kaiser
            case 6500: // Angelic Burster
                cm.getPlayer().changeJob(cm.getPlayer().getJob() + 10);
                cm.dispose();
                return;
            //2nd Job
            case 110: // Fighter
            case 120: // Page
            case 130: // Spearman
            case 210: // Wizard(F/P)
            case 220: // Wizard(I/L)
            case 230: // Cleric
            case 310: // Hunter
            case 320: // Crossbow man
            case 410: // Assassin
            case 420: // Bandit
            case 510: // Brawler
            case 520: // Gunslinger
            case 530: // Cannoneer
            case 570: // Jett
            case 1110: // Dawn Warrior
            case 1210: // Blaze Wizard
            case 1310: // Wind Archer
            case 1410: // Night Walker
            case 1510: // Thunder Breaker
            case 2110: // Aran
            case 2310: // Mercedes
            case 2410: // Phantom
            case 2710: // Luminous
            case 3110: // Demon Slayer
            case 3210: // Battle Mage
            case 3310: // Wild Hunter
            case 3510: // Mechanic
            case 5110: // Mihile
            case 6110: // Kaiser
            case 6510: // Angelic Burster
            //3rd Job
            case 111: // Crusader
            case 121: // White Knight
            case 131: // Dragon Knight
            case 211: // Mage(F/P)
            case 221: // Mage(I/L)
            case 231: // Priest
            case 311: // Ranger
            case 321: // Sniper
            case 411: // Hermit
            case 421: // Chief Bandit
            case 511: // Marauder
            case 521: // Outlaw
            case 531: // Cannon Trooper
            case 571: // Jett
            case 1111: // Dawn Warrior
            case 1211: // Blaze Wizard
            case 1311: // Wind Archer
            case 1411: // Night Walker
            case 1511: // Thunder Breaker
            case 2111: // Aran
            case 2311: // Mercedes
            case 2411: // Phantom
            case 2711: // Luminous
            case 3111: // Demon Slayer
            case 3211: // Battle Mage
            case 3311: // Wild Hunter
            case 3511: // Mechanic
            case 5111: // Mihile
            case 6111: // Kaiser
            case 6511: // Angelic Burster
                cm.getPlayer().changeJob(cm.getPlayer().getJob() + 1);
                cm.dispose();
                return;
            default:
                cm.sendOk("An error occured, or a new job found.\r\nPlease report to the Admins.\r\nYour job id: " + cm.getPlayer().getJob() + "");
                cm.dispose();
                return;
        }
    } else if (status == 2) {
        select = selection;
        cm.sendYesNo("Are you sure you want to Job Advance"/* + selection <= job[jobindex].length ? (" into a(n) #b" + job[jobindex][selection][1] + "#k") : ""*/ + "?");
    } else if (status == 3) {
        if (select != 3100) {
            cm.getPlayer().changeJob(getRealJob(select));
            if (!specialSecondaryWeaponJob(getRealJob(select)))
                cm.dispose();
            return;
        } else 
            cm.sendSimple("As a Demon Slayer, you will have to choose a #bDemon Marking#k.\r\n#L1012276##i1012276##l\r\n#L1012277##i1012277##l\r\n#L1012278##i1012278##l\r\n#L1012279##i1012279##l\r\n#L1012280##i1012280##l");
        if (getSubcategory(select) != 0) {
            cm.getPlayer().changeJob(getRealJob(select));
            cm.getPlayer().setSubcategory(getSubcategory(select));
            cm.getPlayer().dropMessage(0, "You will change channel now so the special job change will effect you. No worries, you will land on the same channel you were in before.");
            cm.dispose();
            return;
        }
    } else if (status == 4) {
        cm.getPlayer().setDemonMarking(selection);
        cm.getPlayer().setSkinColor(4);
        cm.getPlayer().changeJob(getRealJob(select));
        if (select == 3100) {
            cm.sendOk("As a Demon Slayer, your Mana Points(MP) will turn into Demon Force (DF) as soon as you log off.");
        }
        cm.dispose();
        return;
    }
}

function jobSelection(index) {
    jobindex = index;
    var choose = "Please, select your job:"
    for (var i = 0; i < job[index].length; i++)
        choose += "\r\n#L" + job[index][i][0] + "#" + job[index][i][1] + "#l";
    if (extra == true && index <= 2/*Beginner Jobs Only*/) {
        choose += "\r\n\r\n#e#bExtra Jobs#k#n: #e#r(New)#k#n";
        for (var e = 0; e < extrajobs.length; e++)
            choose += "\r\n#L" + extrajobs[e][0] + "#" + extrajobs[e][1] + "#l";
        for (var s = 0; s < specialextrajobs.length; s++)
            choose += "\r\n#L" + specialextrajobs[s][0] + "#" + specialextrajobs[s][1] + "#l";
    }
    cm.sendSimple(choose);
}

function getSubcategory(special) {
    switch (special) {
        case 9400:
        case 430:
        case 431:
        case 432:
        case 433:
        case 434:
            return 1;
        case 9501:
            return 2;
        case 9508:
            return 10;
    }
    return 0;
}

function getRealJob(fakejob) {
    switch (fakejob) {
        case 9400:
            return 400;
        case 9501:
            return 501;
        case 9508:
            return 508;
    }
    return fakejob;
}

function specialSecondaryWeaponJob(job) {
    switch (job) {
        case 508:
        case 570:
        case 571:
        case 572:
        case 3001:
        case 3100:
        case 3110:
        case 3111:
        case 3112:
        case 5100:
        case 5110:
        case 5111:
        case 5112:
            return true;
    }
    return false;
}

function isExplorer(job) {
    return job / 1000 == 0;
}