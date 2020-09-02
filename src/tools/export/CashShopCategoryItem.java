/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import tools.HexTool;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

/**
 *
 * @author Itzik
 */
public class CashShopCategoryItem {

    public static void main(String[] args) {
        Properties data = new Properties();
        InputStreamReader is;
        try {
            is = new FileReader("CashShopItems.txt");
            data.load(is);
            is.close();
        } catch (IOException ex) {
            System.out.println("Failed to load CashShop.txt");
        }
        StringBuilder all = new StringBuilder();
        for (Object property : data.keySet()) {
            byte[] hexdata = HexTool.getByteArrayFromHexString(data.getProperty(String.valueOf(property)));
            final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
            StringBuilder sb = new StringBuilder();
            sb.append("/* Dumping data for table `cashshop_items` */\r\n");
            try {
                if (slea.readByte() != 0xB) {
                    continue;
                }
//                if (slea.readByte() != 1) {
//                    continue;
//                }
                slea.readByte();
                int length = slea.readByte();
                for (int i = 0; i < length; i++) {
                    int category = slea.readInt(); //1000000
                    int subcategory = slea.readInt();
                    int parent = slea.readInt();
                    String image = slea.readMapleAsciiString();
                    int sn = slea.readInt();
                    if (all.toString().contains(String.valueOf(sn))) {
                        continue;
                    }
                    int itemId = slea.readInt();
                    slea.readInt();
                    int flag = slea.readInt();
                    int pack = slea.readInt();
                    int starterpack = slea.readInt();
                    int price = slea.readInt();
                    slea.readLong();
                    slea.readLong();
                    slea.readLong();
                    slea.readLong();
                    int discountPrice = slea.readInt();
                    slea.readInt();
                    int quantity = slea.readInt();
                    int expire = slea.readInt();
                    slea.skip(5);
                    int gender = slea.readInt();
                    int likes = slea.readInt();
                    slea.readInt();
                    slea.readMapleAsciiString();
                    slea.readShort();
                    slea.readInt();
                    slea.readInt();
                    if (pack == 0) {
                        slea.readInt();
                    } else {
                        int packsize = slea.readInt();
                        for (int ii = 0; ii < packsize; ii++) {
                            slea.readInt(); //should be pack item sn
                            slea.readInt();
                            slea.readInt();//1
                            slea.readInt(); //pack item usual price
                            slea.readInt(); //pack item discounted price
                            slea.readInt();
                            slea.readInt();
                            slea.readInt();
                            slea.readInt();
                        }
                    }
//                    int flag = 0;
                    sb.append("INSERT INTO cashshop_items (`category`, `subcategory`, `parent`, `image`, ");
                    sb.append("`sn`, `itemId`, `flag`, `price`, `discountPrice`, `quantity`, `expire`, `gender`, `likes`) ");
                    sb.append("VALUES ('").append(category).append("', '").append(subcategory).append("', '");
                    sb.append(parent).append("', '").append(image).append("', '").append(sn).append("', '");
                    sb.append(itemId).append("', '").append(flag).append("', '").append(price).append("', '");
                    sb.append(discountPrice).append("', '").append(quantity).append("', '");
                    sb.append(expire).append("', '").append(gender).append("', '").append(likes).append("');\r\n");
                }
            } catch (Exception ex) {
                System.out.println("Failed to read items. property " + property/* + "\r\n" + ex*/);
                continue;
            }
            try {
                File outfile = new File("CashShop/items");
                outfile.mkdirs();
                FileOutputStream out = new FileOutputStream(outfile + "/category_" + property + ".sql", false);
                out.write(sb.toString().getBytes());
                sb.append("\r\n\r\n");
                all.append(sb);
            } catch (IOException ex) {
                System.out.println("Failed to save data into a file");
            }
        }
        try {
            File outfile = new File("CashShop/items");
            outfile.mkdirs();
            FileOutputStream out = new FileOutputStream(outfile + "/category_all.sql", false);
            out.write(all.toString().getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to save data into a file");
        }
        System.out.println("done");
    }
}
