var status = 0;
var random = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
var randomizer = [Math.floor(Math.random() * random.length), Math.floor(Math.random() * random.length), Math.floor(Math.random() * random.length), Math.floor(Math.random() * random.length), Math.floor(Math.random() * random.length), Math.floor(Math.random() * random.length)];

function start() {
    status = -1;
    action(1, 0, 0);
} 

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else if (mode == -1)
        status--;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendOk("Welcome!\r\nAs a starting gift, i would like to give you a nx code which contains 5,000 NX Cash.\r\nThe code will be sent to you in a note from MapleGM as soon as you login, change channel or enter the cash shop. \r\n" + random[randomizer[0]] + random[randomizer[1]] + random[randomizer[2]] + random[randomizer[3]] + random[randomizer[4]] + random[randomizer[5]] + "");
        cm.getPlayer().addNXCode(random[randomizer[0]] + random[randomizer[1]] + random[randomizer[2]] + random[randomizer[3]] + random[randomizer[4]] + random[randomizer[5]], null, 1, 5000);
        cm.getPlayer().sendNote(cm.getPlayer().getName(), "MapleGM", "Your NX Code is:\r\n" + random[randomizer[0]] + random[randomizer[1]] + random[randomizer[2]] + random[randomizer[3]] + random[randomizer[4]] + random[randomizer[5]] + "\r\nTo activate this code, go to the Cash Shop and press the Code button, then enter your code.");
        cm.dispose();
    }
}