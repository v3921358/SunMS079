package server.shops;

public class MapleGeneralShop {

    public static final int shopId = 111111;

    public static enum GeneralShop {

        I(1, shopId, 1302000, 1, 1, 0, 0, (byte) 0, 0, 0, 0, 0, 0, 0);

        /*
         * Levels:
         * 0 - All Shops
         * 1 - Advanced potions & throwing stars & bullets
         * 2 - More advanced potions
         * 
         * Additions:
         * 0 - No additions
         * 1 - Magnifying glasses
         * 2 - Basic throwing stars & basic bullets
         * 3 - Socket creators
         * 4 - Kerning Square airplane
         */
        private final byte rank;
        private final int shopitemid, shopid, itemid, price, position, reqitem, reqitemq, buyable, category, minlevel, expiration;
        private final int level, additions;

        GeneralShop(int shopitemid, int shopid, int itemid, int price, int position, int reqitem, int reqitemq, byte rank, int buyable, int category, int minlevel, int expiration, int level, int additions) {
            this.shopitemid = shopitemid;
            this.shopid = shopid;
            this.itemid = itemid;
            this.price = price;
            this.position = position;
            this.reqitem = reqitem;
            this.reqitemq = reqitemq;
            this.rank = rank;
            this.buyable = buyable;
            this.category = category;
            this.minlevel = minlevel;
            this.expiration = expiration;
            this.level = level;
            this.additions = additions;
        }

        public int getShopItemId() {
            return shopitemid;
        }

        public int getShopId() {
            return shopid;
        }

        public int getItemId() {
            return itemid;
        }

        public int getPrice() {
            return price;
        }

        public int getPosition() {
            return position;
        }

        public int getReqItem() {
            return reqitem;
        }

        public int getReqItemQ() {
            return reqitemq;
        }

        public byte getRank() {
            return rank;
        }

        public int getBuyable() {
            return buyable;
        }

        public int getCategory() {
            return category;
        }

        public int getMinLevel() {
            return minlevel;
        }

        public int getExpiration() {
            return expiration;
        }
    }
}
