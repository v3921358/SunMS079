/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import constants.GameConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import server.MapleItemInformationProvider;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

/**
 *
 * @author Itzik
 */
public class ShopAnalyzer {

    public static void main(String[] args) throws IOException {
        //System.out.println("Length:" + HexTool.getByteArrayFromHexString("40 E0 FD 3B 37 4F 01 00 80 05 BB 46 E6 17 02 00 00 00 00 00 00 00 00 00 01 00 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 75 96 8F 00 00 00 00 00 76 96 8F 00 00 00 00 00 77 96 8F 00 00 00 00 00 78 96 8F 00 00 00 00 00").length);
        Scanner input = new Scanner(System.in);
        String data = "";
        //Loading Data
        //System.out.println("Print in the packet data");
        //data = HexTool.getByteArrayFromHexString(input.next());

        //todo: read data file
        //Creating a new File for output
        //System.out.println("\r\nPlease enter the file name of the text file the shop item data will be saved into: \r\n");
        //FileOutputStream out = new FileOutputStream(input.next() + ".txt", false);
        System.out.println("\r\n\r\n");
        //Parsing Data
        StringBuilder all = new StringBuilder();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (int size = 1; size < data.split("@").length; size++) {
            byte[] hexdata = HexTool.getByteArrayFromHexString(data.split("@")[size]);
            final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
            StringBuilder sb = new StringBuilder();
            final LittleEndianAccessor slea2 = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
            int header = slea2.readShort(); //Header
            if (header > 1 && header < 0xFFFF) {
                slea.readShort(); //header
            }
            int scriptedItem = slea.readInt();
            int shopid = slea.readInt(); //Shop Id (Also npc id in gms)
            sb.append("#SQL:\r\n");
            sb.append("#shops:\r\n\r\n");
            sb.append("INSERT INTO shops (`shopid`, `npcid`) VALUES (").append(shopid).append(", ").append(shopid).append(");\r\n\r\n");
            sb.append("#shopitems:\r\n\r\n");
            boolean ranks = slea.readByte() == 1; //0 = no ranks 1 = ranks
            int ranksize;
            int rank = 0;
            String rankmsg = null;
            if (ranks) {
                ranksize = slea.readByte();
                for (int r = 0; r < ranksize; r++) {
                    rank = slea.readInt();
                    rankmsg = slea.readMapleAsciiString();
                }
            }
            short itemsize = slea.readShort(); //Items in shop + Rebuy items
            //Shop Items
            int itemid;
            int price;
            byte discountR;
            int reqItem;
            int reqItemQuantity;
            int expiration;
            int minLevel;
            int category;
            short buyable;
            short quantity;
            for (int i = 1; i <= itemsize; i++) {
                if (slea.available() < 29) {
                    break;
                }
                itemid = slea.readInt();
                price = slea.readInt();
                discountR = slea.readByte(); //Discount Rate
                reqItem = slea.readInt();
                reqItemQuantity = slea.readInt();
                expiration = slea.readInt();
                minLevel = slea.readInt();
                slea.readInt();
                boolean recharge = GameConstants.isThrowingStar(itemid) || GameConstants.isBullet(itemid);
                if (slea.available() > 28 + (recharge ? 4 : 0)) {
                    slea.readLong();
                    slea.readLong();
                    category = slea.readInt();
                    slea.readByte();
                    slea.readInt();
                    if (!recharge) {
                        quantity = slea.readShort();
                        buyable = slea.readShort();
                    } else {
                        quantity = 1;
                        slea.readLong();
                        buyable = ii.getSlotMax(itemid);
                    }
                } else {
                    category = 0;
                    quantity = 1;
                    buyable = ii.getSlotMax(itemid);
                }
                /*
                 slea.readInt(); //0
                 slea.readInt(); //0
                 if ((!GameConstants.isThrowingStar(itemid)) && (!GameConstants.isBullet(itemid))) {
                 slea.readShort(); //always 1
                 slea.readShort(); //always 1000 (might be recharge price)
                 } else {
                 slea.readInt(); //0
                 slea.readShort(); //0
                 slea.readShort(); //price? might be recharge price
                 short itemsPerSlot = slea.readShort();
                 }
                 */
                slea.skip(49 + (recharge ? 4 : 0));
                //Building the String

                //SQL Script
                //sb.append("#SQL:\r\n");
                //sb.append("#shops:\r\n\r\n");
                //sb.append("INSERT INTO shops (`shopid`, `npcid`) VALUES (").append(shopid).append(", ").append(shopid).append(");\r\n\r\n");
                //sb.append("#shopitems:\r\n\r\n");
                //for (int j = 1; j <= itemsize; j++) {
                if (itemid < 1000000 || itemid > 6000000 || itemid / 10000 == 207 || itemid / 10000 == 233) {
                    continue;
                }
                sb.append("INSERT INTO shopitems (`shopid`, `itemid`, `price`, `position`, `reqitem`, `reqitemq`, `rank`, `quantity`, `buyable`, `category`) VALUES(").append(shopid).append(", ").append(itemid).append(", ").append(price).append(", ").append(i).append(", ").append(reqItem).append(", ").append(reqItemQuantity).append(", ").append(rank).append(", ").append(quantity).append(", ").append(buyable).append(", ").append(category).append(");\r\n");
                //}
            }
            //Writing into the file
            File outfile = new File("ShopAnalyzer");
            outfile.mkdir();
            FileOutputStream out = new FileOutputStream(outfile + "/" + shopid + ".txt", false);
            out.write(sb.toString().getBytes());
            all.append(sb.toString());
            if (size == 1) {
                System.out.println(sb.toString());
            }
        }
        File outfile = new File("ShopAnalyzer");
        outfile.mkdir();
        FileOutputStream out = new FileOutputStream(outfile + "/all.txt", false);
        out.write(all.toString().getBytes());
    }
}
