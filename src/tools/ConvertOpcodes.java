/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Itzik
 */
public class ConvertOpcodes {

    public static void main(String[] args) throws IOException {
        boolean decimal;
        StringBuilder sb = new StringBuilder();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Operation Code convertor. \r\nYour opcodes will be converted into hexadecimal numbers or decimal numbers (Whatever you choose), \r\nAnd they will be saved in a new text file.");
        //RecvPacketOpcode.reloadValues();
        //SendPacketOpcode.reloadValues();
        System.out.println("What would you like to convert the opcodes to? HEX, or Decimal?");
        decimal = "decimal".equals(input.next().toLowerCase());
        sb.append("RecvOps.txt converted to hex data:").append("\r\n");
        for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
            sb.append("\r\n").append(recv.name()).append(" = ").append(decimal ? recv.getValue() : HexTool.getOpcodeToString(recv.getValue()));
        }
        System.out.println("\r\nPlease enter the file name of the text file the new opcodes will be saved into: \r\n");
        FileOutputStream out = new FileOutputStream(input.next() + ".txt", false);
        sb.append("SendOps.txt converted to hex data:").append("\r\n");
        for (SendPacketOpcode send : SendPacketOpcode.values()) {
            sb.append("\r\n").append(send.name()).append(" = ").append(decimal ? send.getValue() : HexTool.getOpcodeToString(send.getValue()));
        }
        System.out.println("\r\n\r\n");
        out.write(sb.toString().getBytes());
    }
}
