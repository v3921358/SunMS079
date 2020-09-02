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
public class CashShop {

    public static void main(String[] args) {
        Properties data = new Properties();
        InputStreamReader is;
        try {
            is = new FileReader("CashShop.txt");
            data.load(is);
            is.close();
        } catch (IOException ex) {
            System.out.println("Failed to load CashShop.txt");
        }
        dumpPackages(data);
        dumpCategories(data);
        dumpMenuItems(data);
        System.out.println("Action Complete,\r\nData Location: /CashShop");
    }

    public static void dumpPackages(Properties data) {
        byte[] hexdata = HexTool.getByteArrayFromHexString(data.getProperty("packages"));
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
        StringBuilder sb = new StringBuilder();
        sb.append("int[][][] packages = {\r\n");
        int length = slea.readInt();
        for (int i = 0; i < length; i++) {
            sb.append("{{");
            sb.append(slea.readInt());
            sb.append("}, {");
            int snlength = slea.readInt();
            for (int l = 0; l < snlength; l++) {
                sb.append(slea.readInt());
                if (snlength - l != 1) {
                    sb.append(", ");
                }
            }
            sb.append("}}");
            if (length - i != 1) {
                sb.append(",");
            }
            sb.append("\r\n");
        }
        sb.append("};");
        System.out.println("Packages:\r\n" + sb.toString());
        try {
            File outfile = new File("CashShop");
            outfile.mkdir();
            FileOutputStream out = new FileOutputStream(outfile + "/packages.txt", false);
            out.write(sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to save data into a file");
        }
    }

    public static void dumpCategories(Properties data) {
        byte[] hexdata = HexTool.getByteArrayFromHexString(data.getProperty("categories"));
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
        StringBuilder sb = new StringBuilder();
        sb.append("/* Dumping data for table `cashshop_categories` */");
        int category, parent, flag, sold;
        String name;
        try {
            slea.skip(slea.readByte() == 3 ? 1 : 3);
            int length = slea.readByte();
            for (int i = 0; i < length; i++) {
                category = slea.readInt();
                name = slea.readMapleAsciiString();
                parent = slea.readInt();
                flag = slea.readInt();
                sold = slea.readInt();
                sb.append("\r\nINSERT INTO cashshop_categories (`categoryid`, `name`, `parent`, `flag`, `sold`) ");
                sb.append("VALUES (").append(category).append(", '").append(name).append("', ").append(parent);
                sb.append(", ").append(flag).append(", ").append(sold).append(");");
            }
        } catch (Exception ex) {
            System.out.println("Failed to read categories.\r\n" + ex);
            return;
        }
        try {
            File outfile = new File("CashShop");
            outfile.mkdir();
            FileOutputStream out = new FileOutputStream(outfile + "/categories.sql", false);
            out.write(sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to save data into a file");
        }
    }

    public static void dumpMenuItems(Properties data) {
        byte[] hexdata;
        LittleEndianAccessor slea;
        StringBuilder sb = new StringBuilder();
        sb.append("/* Dumping data for table `cashshop_menuitems` */");
        int category, subcategory, parent, sn, itemid, flag = 0, price, discountPrice, quantity, expire, gender, likes;
        String image;
        try {
            for (int menu = 1; menu <= 4; menu++) {
                String menuStr = "menuitems" + menu;
                hexdata = HexTool.getByteArrayFromHexString(data.getProperty(menuStr));
                slea = new LittleEndianAccessor(new ByteArrayByteStream(hexdata));
                byte a = slea.readByte();
                slea.skip(a == 4 || a == 5 || a == 6 || a == 8 ? 1 : 3);
                int length = slea.readByte();
                for (int i = 0; i < length; i++) {
                    category = slea.readInt();
                    subcategory = slea.readInt();
                    parent = slea.readInt();
                    image = slea.readMapleAsciiString();
                    sn = slea.readInt();
                    itemid = slea.readInt();
                    slea.skip(4 * 4);
                    price = slea.readInt();
                    slea.skip(8 * 4);
                    discountPrice = slea.readInt();
                    slea.skip(4);
                    quantity = slea.readInt();
                    expire = slea.readInt();
                    slea.skip(1 * 5);
                    gender = slea.readInt();
                    likes = slea.readInt();
                    slea.skip(4 * 4);
                    for (int p = 0; p < slea.readInt(); p++) {
                        slea.skip(4 * 9);
                    }
                    sb.append("INSERT INTO cashshop_menuitems (`category`, `subcategory`, `parent`, `image`, ");
                    sb.append("`sn`, `itemid`, `flag`, `price`, `discountPrice`, `quantity`, `expire`, `gender`, `likes`) ");
                    sb.append("VALUES ('").append(category).append("', '").append(subcategory).append("', '");
                    sb.append(parent).append("', '").append(image).append("', '").append(sn).append("', '");
                    sb.append(itemid).append("', '").append(flag).append("', '").append(price).append("', '");
                    sb.append(discountPrice).append("', '").append(quantity).append("', '");
                    sb.append(expire).append("', '").append(gender).append("', '").append(likes).append("');\r\n");
//                    sb.append("\r\nINSERT INTO cashshop_menuitems (`category`, `subcategory`, `parent`, `image`, `sn`, `itemid`, `flag`, `price`, `discountPrice`, `quantity`, `expire`, `gender`, `likes`) ");
//                    sb.append("VALUES (").append(category).append(", ").append(subcategory).append(", ");
//                    sb.append(parent).append(", '").append(image).append("', ").append(sn).append(", ");
//                    sb.append(itemid).append(", ").append(flag).append(", ").append(price).append(", ");
//                    sb.append(discountPrice).append(", ").append(quantity).append(", ").append(expire);
//                    sb.append(", ").append(gender).append(", ").append(likes).append(");");
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to read items.\r\n" + ex);
            return;
        }
        try {
            File outfile = new File("CashShop");
            outfile.mkdir();
            FileOutputStream out = new FileOutputStream(outfile + "/menuitems.sql", false);
            out.write(sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to save data into a file");
        }
    }
}
