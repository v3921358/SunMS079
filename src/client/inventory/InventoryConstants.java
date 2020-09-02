package client.inventory;

public class InventoryConstants {

    public static final short WEAPON = -11;
    public static final short MOUNT = -18;
    public static final short BOTTOM = -6;
    public static final short SHIELD = -10;
    public static final short MEDAL = -49;
    public static final short SHOE = -7;
    public static final short TOP = -5;
    public static final short EARRING = -4;
    public static final short HAT = -1;
    public static final short GLOVE = -8;
    public static final short CAPE = -9;
    public static final short FACE = -13;
    public static final short RING1 = -12;
    public static final short RING2 = -13;
    public static final short RING3 = -14;
    public static final short RING4 = -15;
    public static final short SADDLE = -19;
    public static final short PENDANT = -17;
    public static final short BELT = -50;
    public static final short PET_EQUIP = -123;
    public static final short AUTO_HP = -124;
    public static final short AUTO_MP = -125;
    public static final short CS_RING1 = -112;
    public static final short CS_RING2 = -113;
    public static final short CS_RING3 = -114;
    public static final short CS_RING4 = -115;
    public static final short CODEX = 55;
    public static final int LOCK = 1;
    public static final int SPIKES = 2;
    public static final int COLD = 4;
    public static final int UNTRADEABLE = 8;
    public static final int KARMA = 16;
    public static final int CHARM_EXP = 32;
    public static final int PET_COME = 128;
    public static final int UNKNOWN_SKILL = 256;
    public static final byte HIDDEN = 1;
    public static final byte RARE = 17;
    public static final byte EPIC = 18;
    public static final byte UNIQUE = 19;
    public static final byte LEGENDARY = 20;

    public static boolean idMatchesSlot(int itemid, short slot) {
        return ((slot != -11) || (isWeapon(itemid))) && ((slot != -18) || (isMount(itemid))) && ((slot != -6) || (isBottom(itemid))) && ((slot != -10) || (isShield(itemid)) || (itemid / 10000 == 135)) && ((slot != -49) || (itemid / 10000 == 114)) && ((slot != -7) || (isShoe(itemid))) && ((slot != -5) || (isTop(itemid)) || (isOverall(itemid))) && ((slot != -4) || (isEarring(itemid))) && ((slot != -1) || (isHat(itemid))) && ((slot != -8) || (isGlove(itemid))) && ((slot != -9) || (isCape(itemid))) && ((slot != -13) || (isFaceAccessory(itemid))) && ((slot == -12) || (slot == -13) || (slot == -14) || (slot == -15) || (slot == -112) || (slot == -113) || (slot == -114) || (slot == -115)) && (isRing(itemid)) && ((slot != -19) || (isSaddle(itemid))) && ((slot != -17) || (isNeckAccessory(itemid))) && ((slot != -50) || (isBelt(itemid))) && ((slot == -123) || (slot == -124) || (slot == -125)) && (isPetEquip(itemid)) && ((slot != 55) || (itemid == 1172000));
    }

    public static boolean isRechargeable(int itemid) {
        return (isThrowingStar(itemid)) || (isBullet(itemid));
    }

    public static boolean isThrowingStar(int itemId) {
        return itemId / 10000 == 207;
    }

    public static boolean isBullet(int itemId) {
        return itemId / 10000 == 233;
    }

    public static boolean isArrowForCrossBow(int itemId) {
        return itemId / 1000 == 2061;
    }

    public static boolean isArrowForBow(int itemId) {
        return itemId / 1000 == 2060;
    }

    public static boolean isOverall(int itemId) {
        return itemId / 10000 == 105;
    }

    public static boolean isHat(int itemid) {
        return itemid / 10000 == 100;
    }

    public static boolean isFaceAccessory(int itemid) {
        return itemid / 10000 == 101;
    }

    public static boolean isTop(int itemid) {
        return itemid / 10000 == 104;
    }

    public static boolean isBottom(int itemid) {
        return itemid / 10000 == 106;
    }

    public static boolean isShoe(int itemid) {
        return itemid / 10000 == 107;
    }

    public static boolean isGlove(int itemid) {
        return itemid / 10000 == 108;
    }

    public static boolean isCape(int itemid) {
        return itemid / 10000 == 110;
    }

    public static boolean isNeckAccessory(int itemid) {
        return itemid / 10000 == 112;
    }

    public static boolean isBelt(int itemid) {
        return itemid / 10000 == 113;
    }

    public static boolean isEarring(int itemid) {
        return itemid / 10000 == 103;
    }

    public static boolean isEyeAccessory(int itemid) {
        return itemid / 10000 == 102;
    }

    public static boolean isArmor(int itemid) {
        return (isTop(itemid)) || (isBottom(itemid)) || (isOverall(itemid)) || (isHat(itemid)) || (isGlove(itemid)) || (isShoe(itemid)) || (isCape(itemid));
    }

    public static boolean isAccessory(int id) {
        return (isFaceAccessory(id)) || (isNeckAccessory(id)) || (isEarring(id)) || (isEyeAccessory(id)) || (isBelt(id)) || (isMedal(id));
    }

    public static boolean isMedal(int id) {
        return id / 10000 == 114;
    }

    public static boolean isShield(int id) {
        return id / 10000 == 109;
    }

    public static boolean isRing(int id) {
        return id / 10000 == 111;
    }

    public static boolean isMount(int id) {
        return (id / 10000 == 190) || (id / 10000 == 193);
    }

    public static boolean isSaddle(int id) {
        return id / 10000 == 191;
    }

    public static boolean isTamingMob(int id) {
        return (isMount(id)) || (isSaddle(id));
    }

    public static boolean isPetEquip(int id) {
        return (id / 10000 >= 180) && (id / 10000 <= 183);
    }

    public static boolean isDragon(int id) {
        return (id / 10000 >= 194) && (id / 10000 <= 197);
    }

    public static boolean isMechanic(int id) {
        return (id / 10000 >= 161) && (id / 10000 <= 165);
    }

    public static boolean isMonsterBook(int id) {
        return id == 1172000;
    }

    public static boolean isAndroid(int id) {
        return (id / 10000 >= 166) && (id / 10000 <= 167);
    }

    public static boolean isWeapon(int id) {
        return ((id / 10000 >= 130) && (id / 10000 <= 153)) || (id / 10000 == 170);
    }

    public static boolean isFamiliar(int id) {
        return id / 10000 == 996;
    }

    public static boolean isConsume(int id) {
        return (id / 10000 >= 200) && (id / 10000 < 300);
    }

    public static boolean isEtc(int id) {
        return (id / 10000 >= 400) && (id / 10000 < 500);
    }

    public static boolean isPet(int id) {
        return id / 10000 == 500;
    }

    public static boolean isInstall(int id) {
        return (id / 10000 >= 300) && (id / 10000 < 400);
    }

    public static boolean isCashNotEquip(int id) {
        return id / 1000 >= 500;
    }

    public static boolean isSpecial(int id) {
        return (id / 10000 >= 900) && (id / 10000 < 1000);
    }

    public static boolean is1hWeapon(int id) {
        return id / 10000 < 140;
    }

    public static boolean is2hWeapon(int id) {
        return !is1hWeapon(id);
    }
}
