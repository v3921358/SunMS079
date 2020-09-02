var v1;

function start() {
    v1 = 0;
    cm.sendSimple("#L100#Hat#l#L101#Face#l");
}

function action(mode, type, selection) {
    cm.dispose();
    cm.openShop(selection);
}