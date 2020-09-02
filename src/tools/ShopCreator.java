/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Itzik
 */
public class ShopCreator {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the next values: {shop id, npc id}");
        sb.append("INSERT INTO shops (`shopid`, `npcid`) VALUES(").append(input.next()).append(");\r\n\r\n");
        System.out.println("Please enter again the shop id:");
        int shopid = Integer.parseInt(input.next());
        System.out.println("Please enter a command: {next or done}");
        int position = 1;
        while (!"done".equals(input.next())) {
            if (position >= 250) {
                System.out.println("Warning: you better stop adding items, as too many items will cause lags and crash.");
            }
            System.out.println("Please enter the next values: {item id, price}");
            sb.append("\r\nINSERT INTO shopitems (`shopid`, `itemid`, `price`, `position`) VALUES(").append(shopid).append(",").append(input.next()).append(",").append(position).append(");");
            position++;
        }
        FileOutputStream out = new FileOutputStream("newshop.txt", false);
        out.write(sb.toString().getBytes());
        System.out.println("A new file has been created, newshop.txt. It contains the sql commands.");
    }
}
