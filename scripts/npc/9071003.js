/* RED 1st impact
    Monster Park Shuttle
    Made by Daenerys
*/

var status = 0;
var m;

function start() {
    if (cm.getMapId() == 951000000) {
        cm.sendYesNo("Would you like to go back?");
        m = 1;
        return;
    }
    cm.sendYesNo("Ah, our favorite customer! Would you like to go to Spiegelmann's Monster Park?");
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (m == 1) {
            cm.warp(cm.getSavedLocation("MONSTER_PARK"));
        } else {
            cm.warp(951000000);
            cm.saveReturnLocation("MONSTER_PARK");
        }
    }
    cm.dispose();
}