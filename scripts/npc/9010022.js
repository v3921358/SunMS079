importPackage(Packages.constants);
/**
 * Dimensional Mirror
 * Warps you to Party Quests/Special Maps
 */

function start() {
    cm.sendSlideMenu(0, cm.getSlideMenuSelection(0));
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    var mapid = 100000000;
    var portal = 0;
    switch (selection) {
        case 0: // Ariant Coliseum
            mapid = 682020000, portal = 3;
            break;
        case 1: // Mu Lung 培训中心
            mapid = 925020000, portal = 4;
            break;
        case 2: // Monster Carnival 1
            mapid = 980000000, portal = 4;
            break;
        case 3: // Monster Carnival 2
            mapid = 980030000, portal = 4;
            break;
        case 4: // Dual Raid
            mapid = 923020000, portal = 0;
            break;
        case 5: // Nett's Pyramid
            mapid = 926010000, portal = 4;
            break;
        case 6: // Kerning Subway
            mapid = 910320000, portal = 2;
            break;
        case 7: // Happyville
            mapid = 209000000, portal = 0;
            break;
        case 8: // Golden Temple
            mapid = 950100000, portal = 9;
            break;
        case 9: // Moon Bunny
            mapid = 910010500, portal = 0;
            break;
        case 10: // First Time Together
            mapid = 910340700, portal = 0;
            break;
        case 11: // Dimensional Crack
            mapid = 221023300, portal = 2;
            break;
        case 12: // Forest of Poison Haze
            mapid = 300030100, portal = 1;
            break;
        case 13: // Remnant of the Goddess
            mapid = 200080101, portal = 1;
            break;
        case 14: // Lord Pirate
            mapid = 251010404, portal = 2;
            break;
        case 15: // Romeo and Juliet
            mapid = 261000021, portal = 5;
            break;
        case 16: // Resurrection of the Hoblin King
            mapid = 211000002, portal = 0;
            break;
        case 17: // Dragon's Nest
            mapid = 240080000, portal = 2;
            break;
        case 19: // Haunted Mansion
            mapid = 682000000, portal = 0;
            break;
        case 21: // Kenta In Danger
            mapid = 923040000, portal = 0;
            break;
        case 22: // Escape
            mapid = 921160000, portal = 0;
            break;
        case 23: // Ice Knight
            mapid = 932000000, portal = 0;
            break;
        case 25: // Alliance Union
            mapid = 913050010, portal = 0;
            break;
        case 26: // Halloween
            mapid = 682000700, portal = 0;
            break;
        case 27: //Fight for Azwan
            mapid = 262010000, portal = 0;
            break;
		case 36: //Party Quests
			mapid = 910002000, portal = 2;
			break;
        case 87: // Old Maple
            mapid = 690000040, portal = 0;
            break;
        case 88: // NLC
            mapid = 600000000, portal = 0;
            break;
        case 98: // Astaroth
            mapid = 677000010, portal = 0;
            break;
        case 99: // Dragon's Nest
            mapid = 683010000, portal = 0;
            break;
		case 201: // Twisted Aqua
			mapid = 860000000, portal = 6;
			break;
        default:
            cm.dispose();
            return;
    }
    cm.saveReturnLocation("MULUNG_TC");
    cm.warp(mapid, portal);
    //cm.warp(cm.getSlideMenuDataInteger(0), 0);
    cm.dispose();
}

/*
var text = "";

function start() {
    if (cm.getPlayerStat("LVL") < 10) {
        cm.sendSlideMenu(0, "");
        cm.dispose();
        return;
    }
    //if (cm.getPlayerStat("LVL") >= 20 && cm.getPlayerStat("LVL") <= 30) {
    //    text += "#0# Ariant Coliseum"; // Ariant Coliseum it got removed by nexon :(
    //}
    if (cm.getPlayerStat("LVL") >= 90) {
        text += "#1# Mu Lung 培训中心";
    }
    //if (cm.getPlayerStat("LVL") >= 30) {
    //    text += "#2# Monster Carnival 1";
    //}
    //if (cm.getPlayerStat("LVL") >= 50) {
    //    text += "#3# Monster Carnival 2";
    //}
    if (cm.getPlayerStat("LVL") >= 95 && cm.getPlayerStat("LVL") <= 150) {
        text += "#4# Sea of Fog";
    }
    //if (cm.getPlayerStat("LVL") >= 40) {
    //    text += "#5# Nett's Pyramid";
    //}
    if (cm.getPlayerStat("LVL") >= 25 && cm.getPlayerStat("LVL") <= 30) {
        text += "#6# Kerning Subway";
    }
    if (EventConstants.Christmas) {
        text += "#7# Happyville";
    }
    if (EventConstants.Golden_Temple) {
        text += "#8# Golden Temple";
    }
    if (cm.getPlayerStat("LVL") >= 20) {
        text += "#9# Moon Bunny";
    }
    if (cm.getPlayerStat("LVL") >= 20) {
        text += "#10# First Time Together";
    }
    if (cm.getPlayerStat("LVL") >= 20) {
        text += "#11# Dimensional Crack";
    }
    if (cm.getPlayerStat("LVL") >= 70) {
        text += "#12# Forest of Poison Haze";
    }
    if (cm.getPlayerStat("LVL") >= 70) {
        text += "#13# Remnant of the Goddess";
    }
    if (cm.getPlayerStat("LVL") >= 70) {
        text += "#14# Lord Pirate";
    }
    if (cm.getPlayerStat("LVL") >= 70) {
        text += "#15# Romeo and Juliet";
    }
    if (cm.getPlayerStat("LVL") >= 120) {
        text += "#16# Resurrection of the Hoblin King";
    }
    if (cm.getPlayerStat("LVL") >= 120) {
        text += "#17# Crimson Sky Dock"; // dragon rider pq
    }
    if (EventConstants.Moon_Bunny) {
        text += "#18# The Moon";
    }
    if (EventConstants.Halloween) {
        text += "#19# Haunted Mansion";
    }
    if (cm.getPlayerStat("LVL") >= 120) {
        text += "#21# Kenta in Danger";
    }
    if (cm.getPlayerStat("LVL") >= 120) {
        text += "#22# Escape"; //
    }
    if (cm.getPlayerStat("LVL") >= 30) {
        text += "#23# Ice Knight"; //
    }
    if (cm.getPlayerStat("LVL") >= 75) {
        text += "#25# Alliance Union";
    }
    if (EventConstants.Halloween) {
        text += "#26# Halloween";
    }
    if (cm.getPlayerStat("LVL") >= 13 && EventConstants.Old_Maple) {
        text += "#87# Crack in the Dimensional Mirror";
    }
    if (cm.getPlayerStat("LVL") >= 13 && cm.getMapId() != 600000000) {
        text += "#88# New Leaf City";
    }
    if (cm.getPlayerStat("LVL") >= 25 && cm.getPlayerStat("LVL") <= 40) {
        text += "#98# Astaroth";
    }
    if (EventConstants.Dragon_Nest) {
        text += "#99# Nest of Dead dragon"; // 683010000
    }
    cm.sendSlideMenu(0, text);
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (cm.getPlayerStat("LVL") < 10) { // they cannot use any
            cm.dispose();
            return;
        }
        switch (selection) {
            case 0: // Boss Party Quest / Ariant Coliseum
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(682020000, 3);
                break;
            case 1: // Mu Lung 培训中心
                if (cm.getPlayerStat("LVL") >= 25) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(925020000, 4);
                }
                break;
            case 2: // Monster Carnival 1
                if (cm.getPlayerStat("LVL") >= 30) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(980000000, 4);
                }
                break;
            case 3: // Monster Carnival 2
                if (cm.getPlayerStat("LVL") >= 50) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(980030000, 4);
                }
                break;
            case 4: // Dual Raid
                if (cm.getPlayerStat("LVL") >= 60) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(923020000, 0);
                }
                break;
            case 5: // Nett's Pyramid
                if (cm.getPlayerStat("LVL") >= 40) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(926010000, 4);
                }
                break;
            case 6: // Kerning Subway
                if (cm.getPlayerStat("LVL") >= 25 && cm.getPlayerStat("LVL") <= 30) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(910320000, 2);
                }
                break;
            case 7: // Happyville
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(209000000, 0);
                break;
            case 8: // Golden Temple
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(950100000, 9);
                break;
            case 9: // Moon Bunny
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(910010500, 0);
                break;
            case 10: // First Time Together
                if (cm.getPlayerStat("LVL") >= 20) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(910340700, 0);
                }
                break;
            case 11: // Dimensional Crack
                if (cm.getPlayerStat("LVL") >= 30) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(221023300, 2);
                }
                break;
            case 12: // Forest of Poison Haze
                if (cm.getPlayerStat("LVL") >= 40) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(300030100, 1);
                }
                break;
            case 13: // Remnant of the Goddess
                if (cm.getPlayerStat("LVL") >= 50) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(200080101, 1);
                }
                break;
            case 14: // Lord Pirate
                if (cm.getPlayerStat("LVL") >= 60) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(251010404, 2);
                }
                break;
            case 15: // Romeo and Juliet
                if (cm.getPlayerStat("LVL") >= 70) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(261000021, 5);
                }
                break;
            case 16: // Resurrection of the Hoblin King
                if (cm.getPlayerStat("LVL") >= 80) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(211000002, 0);
                }
                break;
            case 17: // Dragon's Nest
                if (cm.getPlayerStat("LVL") >= 100) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(240080000, 2);
                }
                break;
            case 19: // Haunted Mansion
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(682000000, 0);
                break;
            case 21: // Kenta In Danger
                if (cm.getPlayerStat("LVL") >= 120) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(923040000, 0);
                }
                break;
            case 22: // Escape
                if (cm.getPlayerStat("LVL") >= 120) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(921160000, 0);
                }
                break;
            case 23: // Ice Knight
                if (cm.getPlayerStat("LVL") >= 30) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(932000000, 0);
                }
                break;
            case 25: // Alliance Union
                if (cm.getPlayerStat("LVL") >= 75) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(913050010, 0);
                }
                break;
            case 26: // Halloween
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(682000700, 0);
                break;
            case 87: // Old Maple
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(690000040, 0);
                break;
            case 88: // NLC
                if (cm.getPlayerStat("LVL") >= 13) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(600000000, 0);
                }
                break;
            case 98: // Astaroth
                if (cm.getPlayerStat("LVL") >= 25) {
                    cm.saveReturnLocation("MULUNG_TC");
                    cm.warp(677000010, 0);
                }
                break;
            case 99: // Dragon's Nest
                cm.saveReturnLocation("MULUNG_TC");
                cm.warp(683010000, 0);
                break;
        }
    }
    cm.dispose();
}
*/