/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import server.MapleItemInformationProvider;

/**
 *
 * @author Itzik
 */
public class ChairDump {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream("ChairDump.txt", false);
        StringBuilder sb = new StringBuilder();
        int shopId = 145274;
        int npcId = 9010000;
        sb.append("INSERT INTO shops (`shopid`, `npcid`) VALUES(").append(shopId).append(", ").append(npcId).append(");\r\n");
        int price = 1;
        for (Pair<Integer, String> item : MapleItemInformationProvider.getInstance().getAllItems2()) {
            if (item.getLeft() >= 3010000 && item.getLeft() < 3020000) {
                sb.append("INSERT INTO shopitems (`shopid`, `itemid`, `price`, `position`) VALUES(").append(shopId).append(", ").append(item.getLeft()).append(", ").append(price).append(", 0);\r\n");
            }
        }
        out.write(sb.toString().getBytes());
    }
}
