function start() {
    if (cm.getMap().getAllMonstersThreadsafe().size() <= 0) {
        cm.sendOk("There are no monsters in this map.");
        cm.dispose();
        return;
    }
    var selStr = "Select which monster you wish to check.\r\n\r\n#b";
    var monsterIterator = cm.getMap().getAllUniqueMonsters().iterator();
    while (monsterIterator.hasNext()) {
        var nextMonster = monsterIterator.next();
        selStr += "#L" + nextMonster + "##o" + nextMonster + "##l\r\n";
    } 
    cm.sendSimple(selStr);
}

function action(mode, type, selection) {
    cm.sendOk(cm.checkDrop(selection));
    cm.dispose();
}